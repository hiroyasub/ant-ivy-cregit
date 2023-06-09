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
name|publish
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|ArrayList
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|IvyContext
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
name|event
operator|.
name|EventManager
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
name|event
operator|.
name|publish
operator|.
name|EndArtifactPublishEvent
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
name|event
operator|.
name|publish
operator|.
name|StartArtifactPublishEvent
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
name|Artifact
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
name|DefaultArtifact
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
name|MDArtifact
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
name|XmlModuleDescriptorParser
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
name|resolver
operator|.
name|DependencyResolver
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|ConfigurationUtils
operator|.
name|replaceWildcards
import|;
end_import

begin_class
specifier|public
class|class
name|PublishEngine
block|{
specifier|private
name|PublishEngineSettings
name|settings
decl_stmt|;
specifier|private
name|EventManager
name|eventManager
decl_stmt|;
specifier|public
name|PublishEngine
parameter_list|(
name|PublishEngineSettings
name|settings
parameter_list|,
name|EventManager
name|eventManager
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
block|}
comment|/**      * Publishes a module to the repository. The publish can update the ivy file to publish if      * update is set to true. In this case it will use the given pubrevision, pubdate and status. If      * pubdate is null it will default to the current date. If status is null it will default to the      * current ivy file status (which itself defaults to integration if none is found). If update is      * false, then if the revision is not the same in the ivy file than the one expected (given as      * parameter), this method will fail with an IllegalArgumentException. pubdate and status are      * not used if update is false. extra artifacts can be used to publish more artifacts than      * actually declared in the ivy file. This can be useful to publish additional metadata or      * reports. The extra artifacts array can be null (= no extra artifacts), and if non null only      * the name, type, ext url and extra attributes of the artifacts are really used. Other methods      * can return null safely.      *      * @param mrid ModuleRevisionId      * @param srcArtifactPattern a Collection of String      * @param resolverName String      * @param options PublishOptions      * @return Collection&lt;Artifact&gt;      * @throws IOException if something goes wrong      */
specifier|public
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|publish
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|srcArtifactPattern
parameter_list|,
name|String
name|resolverName
parameter_list|,
name|PublishOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
operator|.
name|info
argument_list|(
literal|":: publishing :: "
operator|+
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tvalidate = "
operator|+
name|options
operator|.
name|isValidate
argument_list|()
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
name|options
operator|.
name|setSrcIvyPattern
argument_list|(
name|settings
operator|.
name|substitute
argument_list|(
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|getPubBranch
argument_list|()
operator|==
literal|null
condition|)
block|{
name|options
operator|.
name|setPubbranch
argument_list|(
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getPubrevision
argument_list|()
operator|==
literal|null
condition|)
block|{
name|options
operator|.
name|setPubrevision
argument_list|(
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ModuleRevisionId
name|pubmrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|options
operator|.
name|getPubBranch
argument_list|()
argument_list|,
name|options
operator|.
name|getPubrevision
argument_list|()
argument_list|)
decl_stmt|;
comment|// let's find the resolved module descriptor
name|ModuleDescriptor
name|md
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|File
name|ivyFile
init|=
name|settings
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
argument_list|,
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|pubmrid
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ivy file to publish not found for "
operator|+
name|mrid
operator|+
literal|": call deliver before ("
operator|+
name|ivyFile
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|URL
name|ivyFileURL
init|=
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
try|try
block|{
name|md
operator|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|ivyFileURL
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|isUpdate
argument_list|()
condition|)
block|{
name|File
name|tmp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
literal|".xml"
argument_list|)
decl_stmt|;
name|tmp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|String
index|[]
name|confs
init|=
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
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
name|ivyFileURL
argument_list|,
name|tmp
argument_list|,
operator|new
name|UpdateOptions
argument_list|()
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
operator|.
name|setStatus
argument_list|(
name|options
operator|.
name|getStatus
argument_list|()
operator|==
literal|null
condition|?
name|md
operator|.
name|getStatus
argument_list|()
else|:
name|options
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|setRevision
argument_list|(
name|options
operator|.
name|getPubrevision
argument_list|()
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
operator|==
literal|null
condition|?
operator|new
name|Date
argument_list|()
else|:
name|options
operator|.
name|getPubdate
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
argument_list|)
expr_stmt|;
name|ivyFile
operator|=
name|tmp
expr_stmt|;
comment|// we parse the new file to get updated module descriptor
name|md
operator|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|options
operator|.
name|setSrcIvyPattern
argument_list|(
name|ivyFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"bad ivy file for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
if|else if
condition|(
operator|!
name|options
operator|.
name|getPubrevision
argument_list|()
operator|.
name|equals
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"cannot publish "
operator|+
name|ivyFile
operator|+
literal|" as "
operator|+
name|options
operator|.
name|getPubrevision
argument_list|()
operator|+
literal|": bad revision found in ivy file (Revision: "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"). Use forcedeliver or update."
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"bad ivy file for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|ResolutionCacheManager
name|cacheManager
init|=
name|settings
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
try|try
block|{
name|md
operator|=
name|cacheManager
operator|.
name|getResolvedModuleDescriptor
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"bad ivy file in cache for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|e
argument_list|)
throw|;
block|}
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
name|pubmrid
argument_list|)
expr_stmt|;
block|}
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|resolverName
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown resolver "
operator|+
name|resolverName
argument_list|)
throw|;
block|}
comment|// collect all declared artifacts of this module
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|missing
init|=
name|publish
argument_list|(
name|md
argument_list|,
name|srcArtifactPattern
argument_list|,
name|resolver
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tpublish done ("
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
return|return
name|missing
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|publish
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|srcArtifactPattern
parameter_list|,
name|DependencyResolver
name|resolver
parameter_list|,
name|PublishOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|Collection
argument_list|<
name|Artifact
argument_list|>
name|missing
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Artifact
argument_list|>
name|artifactsSet
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|conf
range|:
name|replaceWildcards
argument_list|(
name|options
operator|.
name|getConfs
argument_list|()
argument_list|,
name|md
argument_list|)
control|)
block|{
name|artifactsSet
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getArtifacts
argument_list|(
name|conf
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Artifact
index|[]
name|extraArtifacts
init|=
name|options
operator|.
name|getExtraArtifacts
argument_list|()
decl_stmt|;
if|if
condition|(
name|extraArtifacts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Artifact
name|extraArtifact
range|:
name|extraArtifacts
control|)
block|{
name|artifactsSet
operator|.
name|add
argument_list|(
operator|new
name|MDArtifact
argument_list|(
name|md
argument_list|,
name|extraArtifact
operator|.
name|getName
argument_list|()
argument_list|,
name|extraArtifact
operator|.
name|getType
argument_list|()
argument_list|,
name|extraArtifact
operator|.
name|getExt
argument_list|()
argument_list|,
name|extraArtifact
operator|.
name|getUrl
argument_list|()
argument_list|,
name|extraArtifact
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// now collects artifacts files
name|Map
argument_list|<
name|Artifact
argument_list|,
name|File
argument_list|>
name|artifactsFiles
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifactsSet
control|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|srcArtifactPattern
control|)
block|{
name|File
name|artifactFile
init|=
name|settings
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|settings
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|)
argument_list|,
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|artifactsFiles
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|artifactFile
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|artifactsFiles
operator|.
name|containsKey
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"missing artifact "
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
argument_list|)
operator|.
name|append
argument_list|(
literal|":\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|pattern
range|:
name|srcArtifactPattern
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\t"
argument_list|)
operator|.
name|append
argument_list|(
name|settings
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|artifact
argument_list|)
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" file does not exist\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|isWarnOnMissing
argument_list|()
operator|||
name|options
operator|.
name|isHaltOnMissing
argument_list|()
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|isHaltOnMissing
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"missing artifact "
operator|+
name|artifact
argument_list|)
throw|;
block|}
name|missing
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Artifact
name|artifact
init|=
name|MDArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
name|settings
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
argument_list|,
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"missing ivy file for "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|": \n"
operator|+
name|artifactFile
operator|+
literal|" file does not exist"
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|isWarnOnMissing
argument_list|()
operator|||
name|options
operator|.
name|isHaltOnMissing
argument_list|()
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|isHaltOnMissing
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"missing ivy artifact "
operator|+
name|artifact
argument_list|)
throw|;
block|}
name|missing
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifactsFiles
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|artifactFile
argument_list|)
expr_stmt|;
block|}
block|}
comment|// and now do actual publishing
name|boolean
name|successfullyPublished
init|=
literal|false
decl_stmt|;
try|try
block|{
name|resolver
operator|.
name|beginPublishTransaction
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
comment|// for each declared published artifact in this descriptor, do:
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Artifact
argument_list|,
name|File
argument_list|>
name|entry
range|:
name|artifactsFiles
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|publish
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|resolver
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resolver
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
name|successfullyPublished
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|successfullyPublished
condition|)
block|{
name|resolver
operator|.
name|abortPublishTransaction
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|missing
return|;
block|}
specifier|private
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|DependencyResolver
name|resolver
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|checkInterrupted
argument_list|()
expr_stmt|;
comment|// notify triggers that an artifact is about to be published
name|eventManager
operator|.
name|fireIvyEvent
argument_list|(
operator|new
name|StartArtifactPublishEvent
argument_list|(
name|resolver
argument_list|,
name|artifact
argument_list|,
name|src
argument_list|,
name|overwrite
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|successful
init|=
literal|false
decl_stmt|;
comment|// set to true once the publish succeeds
try|try
block|{
if|if
condition|(
name|src
operator|.
name|exists
argument_list|()
condition|)
block|{
name|resolver
operator|.
name|publish
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|successful
operator|=
literal|true
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// notify triggers that the publish is finished, successfully or not.
name|eventManager
operator|.
name|fireIvyEvent
argument_list|(
operator|new
name|EndArtifactPublishEvent
argument_list|(
name|resolver
argument_list|,
name|artifact
argument_list|,
name|src
argument_list|,
name|overwrite
argument_list|,
name|successful
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

