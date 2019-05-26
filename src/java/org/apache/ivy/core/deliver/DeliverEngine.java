begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|deliver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|IvyPatternHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|cache
operator|.
name|ResolutionCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DependencyDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|parser
operator|.
name|xml
operator|.
name|UpdateOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|parser
operator|.
name|xml
operator|.
name|XmlModuleDescriptorUpdater
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|report
operator|.
name|XmlReportParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|repository
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|ConfigurationUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_class
specifier|public
class|class
name|DeliverEngine
block|{
specifier|private
name|DeliverEngineSettings
name|settings
decl_stmt|;
specifier|public
name|DeliverEngine
parameter_list|(
name|DeliverEngineSettings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
comment|/**      * Delivers a resolved ivy file based upon last resolve call status. If resolve report file      * cannot be found in cache, then it throws an IllegalStateException (maybe resolve has not been      * called before ?).      *      * @param revision      *            the revision to which the module should be delivered      * @param destIvyPattern      *            the pattern to which the delivered ivy file should be written      * @param options      *            the options with which deliver should be done      * @throws IOException if something goes wrong      * @throws ParseException if something goes wrong      */
specifier|public
name|void
name|deliver
parameter_list|(
name|String
name|revision
parameter_list|,
name|String
name|destIvyPattern
parameter_list|,
name|DeliverOptions
name|options
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|String
name|resolveId
init|=
name|options
operator|.
name|getResolveId
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolveId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A resolveId must be specified for delivering."
argument_list|)
throw|;
block|}
name|File
index|[]
name|files
init|=
name|getCache
argument_list|()
operator|.
name|getConfigurationResolveReportsInCache
argument_list|(
name|resolveId
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No previous resolve found for id '"
operator|+
name|resolveId
operator|+
literal|"' Please resolve dependencies before delivering."
argument_list|)
throw|;
block|}
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|parser
operator|.
name|getResolvedModule
argument_list|()
decl_stmt|;
name|deliver
argument_list|(
name|mrid
argument_list|,
name|revision
argument_list|,
name|destIvyPattern
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResolutionCacheManager
name|getCache
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getResolutionCacheManager
argument_list|()
return|;
block|}
comment|/**      * Delivers a resolved ivy file based upon last resolve call status. If resolve report file      * cannot be found in cache, then it throws an IllegalStateException (maybe resolve has not been      * called before ?).      *      * @param mrid      *            the module revision id of the module to deliver      * @param revision      *            the revision to which the module should be delivered      * @param destIvyPattern      *            the pattern to which the delivered ivy file should be written      * @param options      *            the options with which deliver should be done      * @throws IOException if something goes wrong      * @throws ParseException if something goes wrong      */
specifier|public
name|void
name|deliver
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|destIvyPattern
parameter_list|,
name|DeliverOptions
name|options
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|Message
operator|.
name|info
argument_list|(
literal|":: delivering :: "
operator|+
name|mrid
operator|+
literal|" :: "
operator|+
name|revision
operator|+
literal|" :: "
operator|+
name|options
operator|.
name|getStatus
argument_list|()
operator|+
literal|" :: "
operator|+
name|options
operator|.
name|getPubdate
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\toptions = "
operator|+
name|options
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|destIvyPattern
operator|=
name|settings
operator|.
name|substitute
argument_list|(
name|destIvyPattern
argument_list|)
expr_stmt|;
comment|// 1) find the resolved module descriptor in cache
name|ModuleDescriptor
name|md
init|=
name|getCache
argument_list|()
operator|.
name|getResolvedModuleDescriptor
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|options
operator|.
name|getPubBranch
argument_list|()
operator|==
literal|null
condition|?
name|mrid
operator|.
name|getBranch
argument_list|()
else|:
name|options
operator|.
name|getPubBranch
argument_list|()
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|setResolvedPublicationDate
argument_list|(
name|options
operator|.
name|getPubdate
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2) parse resolvedRevisions From properties file
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedRevisions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Map (ModuleId -> String revision)
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedBranches
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Map (ModuleId -> String branch)
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|dependenciesStatus
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Map (ModuleId -> String status)
name|File
name|ivyProperties
init|=
name|getCache
argument_list|()
operator|.
name|getResolvedIvyPropertiesInCache
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ivyProperties
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ivy properties not found in cache for "
operator|+
name|mrid
operator|+
literal|"; please resolve dependencies before delivering!"
argument_list|)
throw|;
block|}
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|ivyProperties
argument_list|)
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|props
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|depMridStr
init|=
operator|(
name|String
operator|)
name|o
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|depMridStr
argument_list|)
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|decodedMrid
init|=
name|ModuleRevisionId
operator|.
name|decode
argument_list|(
name|depMridStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|isResolveDynamicRevisions
argument_list|()
condition|)
block|{
name|resolvedRevisions
operator|.
name|put
argument_list|(
name|decodedMrid
argument_list|,
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>=
literal|4
condition|)
block|{
if|if
condition|(
name|parts
index|[
literal|3
index|]
operator|!=
literal|null
operator|&&
operator|!
literal|"null"
operator|.
name|equals
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
condition|)
block|{
name|resolvedBranches
operator|.
name|put
argument_list|(
name|decodedMrid
argument_list|,
name|parts
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|dependenciesStatus
operator|.
name|put
argument_list|(
name|decodedMrid
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|isReplaceForcedRevisions
argument_list|()
condition|)
block|{
if|if
condition|(
name|parts
operator|.
name|length
operator|<=
literal|2
condition|)
block|{
comment|// maybe the properties file was generated by an older Ivy version
comment|// so it is possible that this part doesn't exist.
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ivy properties file generated by an older"
operator|+
literal|" version of Ivy which doesn't support replacing forced revisions!"
argument_list|)
throw|;
block|}
name|resolvedRevisions
operator|.
name|put
argument_list|(
name|decodedMrid
argument_list|,
name|parts
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|// 3) use pdrResolver to resolve dependencies info
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedDependencies
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Map (ModuleRevisionId -> String revision)
for|for
control|(
name|DependencyDescriptor
name|dependency
range|:
name|md
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|String
name|rev
init|=
name|resolvedRevisions
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rev
operator|==
literal|null
condition|)
block|{
name|rev
operator|=
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
expr_stmt|;
block|}
name|String
name|bra
init|=
name|resolvedBranches
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|bra
operator|==
literal|null
operator|||
literal|"null"
operator|.
name|equals
argument_list|(
name|bra
argument_list|)
condition|)
block|{
name|bra
operator|=
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getBranch
argument_list|()
expr_stmt|;
block|}
name|String
name|depStatus
init|=
name|dependenciesStatus
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid2
init|=
operator|(
name|bra
operator|==
literal|null
operator|)
condition|?
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|rev
argument_list|)
else|:
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|bra
argument_list|,
name|rev
argument_list|)
decl_stmt|;
name|resolvedDependencies
operator|.
name|put
argument_list|(
name|dependency
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|options
operator|.
name|getPdrResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
name|options
operator|.
name|getStatus
argument_list|()
argument_list|,
name|mrid2
argument_list|,
name|depStatus
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// 4) copy the source resolved ivy to the destination specified,
comment|// updating status, revision and dependency revisions obtained by
comment|// PublishingDependencyRevisionResolver
name|File
name|publishedIvy
init|=
name|settings
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|destIvyPattern
argument_list|,
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\tdelivering ivy file to "
operator|+
name|publishedIvy
argument_list|)
expr_stmt|;
name|String
index|[]
name|confs
init|=
name|ConfigurationUtils
operator|.
name|replaceWildcards
argument_list|(
name|options
operator|.
name|getConfs
argument_list|()
argument_list|,
name|md
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|confsToRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|confsToRemove
operator|.
name|removeAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|UpdateOptions
name|opts
init|=
operator|new
name|UpdateOptions
argument_list|()
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
operator|.
name|setResolvedRevisions
argument_list|(
name|resolvedDependencies
argument_list|)
operator|.
name|setStatus
argument_list|(
name|options
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|setRevision
argument_list|(
name|revision
argument_list|)
operator|.
name|setBranch
argument_list|(
name|options
operator|.
name|getPubBranch
argument_list|()
argument_list|)
operator|.
name|setPubdate
argument_list|(
name|options
operator|.
name|getPubdate
argument_list|()
argument_list|)
operator|.
name|setGenerateRevConstraint
argument_list|(
name|options
operator|.
name|isGenerateRevConstraint
argument_list|()
argument_list|)
operator|.
name|setMerge
argument_list|(
name|options
operator|.
name|isMerge
argument_list|()
argument_list|)
operator|.
name|setMergedDescriptor
argument_list|(
name|md
argument_list|)
operator|.
name|setConfsToExclude
argument_list|(
name|confsToRemove
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confsToRemove
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|resolvedBranches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|opts
operator|=
name|opts
operator|.
name|setResolvedBranches
argument_list|(
name|resolvedBranches
argument_list|)
expr_stmt|;
block|}
name|Resource
name|res
init|=
name|md
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
name|res
operator|.
name|openStream
argument_list|()
argument_list|,
name|res
argument_list|,
name|publishedIvy
argument_list|,
name|opts
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"bad ivy file in cache for "
operator|+
name|mrid
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tdeliver done ("
operator|+
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|)
operator|+
literal|"ms)"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

