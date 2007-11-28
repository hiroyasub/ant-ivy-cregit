begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|MalformedURLException
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
name|Iterator
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
name|CacheManager
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
name|PublishEngine
block|{
specifier|private
name|PublishEngineSettings
name|settings
decl_stmt|;
specifier|public
name|PublishEngine
parameter_list|(
name|PublishEngineSettings
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
comment|/**      * Publishes a module to the repository. The publish can update the ivy file to publish if      * update is set to true. In this case it will use the given pubrevision, pubdate and status. If      * pubdate is null it will default to the current date. If status is null it will default to the      * current ivy file status (which itself defaults to integration if none is found). If update is      * false, then if the revision is not the same in the ivy file than the one expected (given as      * parameter), this method will fail with an IllegalArgumentException. pubdate and status are      * not used if update is false. extra artifacts can be used to publish more artifacts than      * actually declared in the ivy file. This can be useful to publish additional metadata or      * reports. The extra artifacts array can be null (= no extra artifacts), and if non null only      * the name, type, ext url and extra attributes of the artifacts are really used. Other methods      * can return null safely.      */
specifier|public
name|Collection
name|publish
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Collection
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
name|getPubrevision
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|ivyFile
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
name|ivyFile
operator|=
operator|new
name|File
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
expr_stmt|;
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
block|}
else|else
block|{
name|CacheManager
name|cacheManager
init|=
name|getCacheManager
argument_list|(
name|options
argument_list|)
decl_stmt|;
name|ivyFile
operator|=
name|cacheManager
operator|.
name|getResolvedIvyFileInCache
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
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
name|IllegalStateException
argument_list|(
literal|"ivy file not found in cache for "
operator|+
name|mrid
operator|+
literal|": please resolve dependencies before publishing ("
operator|+
name|ivyFile
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
comment|// let's find the resolved module descriptor
name|ModuleDescriptor
name|md
init|=
literal|null
decl_stmt|;
name|URL
name|ivyFileURL
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ivyFileURL
operator|=
name|ivyFile
operator|.
name|toURL
argument_list|()
expr_stmt|;
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
name|getSrcIvyPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
name|confsToRemove
init|=
operator|new
name|HashSet
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
name|settings
argument_list|,
name|ivyFileURL
argument_list|,
name|tmp
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
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
argument_list|,
name|options
operator|.
name|getPubrevision
argument_list|()
argument_list|,
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
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|(
name|String
index|[]
operator|)
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
else|else
block|{
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
name|pubmrid
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"malformed url obtained for file "
operator|+
name|ivyFile
argument_list|)
throw|;
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
name|publish
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|Collection
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
name|missing
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Set
name|artifactsSet
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|options
operator|.
name|getConfs
argument_list|()
decl_stmt|;
if|if
condition|(
name|confs
operator|==
literal|null
operator|||
operator|(
name|confs
operator|.
name|length
operator|==
literal|1
operator|&&
literal|"*"
operator|.
name|equals
argument_list|(
name|confs
index|[
literal|0
index|]
argument_list|)
operator|)
condition|)
block|{
name|confs
operator|=
name|md
operator|.
name|getConfigurationsNames
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Artifact
index|[]
name|artifacts
init|=
name|md
operator|.
name|getArtifacts
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|artifacts
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|artifactsSet
operator|.
name|add
argument_list|(
name|artifacts
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|extraArtifacts
operator|.
name|length
condition|;
name|i
operator|++
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
name|extraArtifacts
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|extraArtifacts
index|[
name|i
index|]
operator|.
name|getType
argument_list|()
argument_list|,
name|extraArtifacts
index|[
name|i
index|]
operator|.
name|getExt
argument_list|()
argument_list|,
name|extraArtifacts
index|[
name|i
index|]
operator|.
name|getUrl
argument_list|()
argument_list|,
name|extraArtifacts
index|[
name|i
index|]
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Iterator
name|iter
init|=
name|artifactsSet
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// 1) copy the artifact using src patterns and resolver
name|boolean
name|published
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|srcArtifactPattern
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|published
condition|;
control|)
block|{
name|String
name|pattern
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|published
operator|=
name|publish
argument_list|(
name|artifact
argument_list|,
name|settings
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|)
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
if|if
condition|(
operator|!
name|published
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"missing artifact "
operator|+
name|artifact
operator|+
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|srcArtifactPattern
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|pattern
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\t"
operator|+
operator|new
name|File
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
operator|+
literal|" file does not exist"
argument_list|)
expr_stmt|;
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
if|if
condition|(
operator|!
name|publish
argument_list|(
name|artifact
argument_list|,
name|options
operator|.
name|getSrcIvyPattern
argument_list|()
argument_list|,
name|resolver
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"missing ivy file for "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|": "
operator|+
operator|new
name|File
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
operator|+
literal|" file does not exist"
argument_list|)
expr_stmt|;
name|missing
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|srcArtifactPattern
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
name|File
name|src
init|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|srcArtifactPattern
argument_list|,
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
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
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|CacheManager
name|getCacheManager
parameter_list|(
name|PublishOptions
name|options
parameter_list|)
block|{
name|CacheManager
name|cacheManager
init|=
name|options
operator|.
name|getCache
argument_list|()
decl_stmt|;
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
comment|// ensure that a cache is configured
name|cacheManager
operator|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getCacheManager
argument_list|()
expr_stmt|;
name|options
operator|.
name|setCache
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setCacheManager
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
block|}
return|return
name|cacheManager
return|;
block|}
block|}
end_class

end_unit

