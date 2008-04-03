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
name|retrieve
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|List
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
name|LogOptions
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
name|retrieve
operator|.
name|EndRetrieveEvent
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
name|retrieve
operator|.
name|StartRetrieveEvent
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
name|ModuleId
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
name|core
operator|.
name|report
operator|.
name|ArtifactDownloadReport
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
name|resolve
operator|.
name|ResolveOptions
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
name|ModuleDescriptorParser
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
name|ModuleDescriptorParserRegistry
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
name|url
operator|.
name|URLResource
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
name|FileUtil
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

begin_class
specifier|public
class|class
name|RetrieveEngine
block|{
specifier|private
specifier|static
specifier|final
name|int
name|KILO
init|=
literal|1024
decl_stmt|;
specifier|private
name|RetrieveEngineSettings
name|settings
decl_stmt|;
specifier|private
name|EventManager
name|eventManager
decl_stmt|;
specifier|public
name|RetrieveEngine
parameter_list|(
name|RetrieveEngineSettings
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
comment|/**      * example of destFilePattern : - lib/[organisation]/[module]/[artifact]-[revision].[type] -      * lib/[artifact].[type] : flatten with no revision moduleId is used with confs and      * localCacheDirectory to determine an ivy report file, used as input for the copy If such a      * file does not exist for any conf (resolve has not been called before ?) then an      * IllegalStateException is thrown and nothing is copied.      */
specifier|public
name|int
name|retrieve
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|destFilePattern
parameter_list|,
name|RetrieveOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|ModuleId
name|moduleId
init|=
name|mrid
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
if|if
condition|(
name|LogOptions
operator|.
name|LOG_DEFAULT
operator|.
name|equals
argument_list|(
name|options
operator|.
name|getLog
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|":: retrieving :: "
operator|+
name|moduleId
operator|+
operator|(
name|options
operator|.
name|isSync
argument_list|()
condition|?
literal|" [sync]"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|":: retrieving :: "
operator|+
name|moduleId
operator|+
operator|(
name|options
operator|.
name|isSync
argument_list|()
condition|?
literal|" [sync]"
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tcheckUpToDate="
operator|+
name|settings
operator|.
name|isCheckUpToDate
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
name|destFilePattern
operator|=
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|destFilePattern
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|destIvyPattern
init|=
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|options
operator|.
name|getDestIvyPattern
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|getConfs
argument_list|(
name|mrid
argument_list|,
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|LogOptions
operator|.
name|LOG_DEFAULT
operator|.
name|equals
argument_list|(
name|options
operator|.
name|getLog
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\tconfs: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tconfs: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|eventManager
operator|.
name|fireIvyEvent
argument_list|(
operator|new
name|StartRetrieveEvent
argument_list|(
name|mrid
argument_list|,
name|confs
argument_list|,
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Map
name|artifactsToCopy
init|=
name|determineArtifactsToCopy
argument_list|(
name|mrid
argument_list|,
name|destFilePattern
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|File
name|fileRetrieveRoot
init|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|getTokenRoot
argument_list|(
name|destFilePattern
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|ivyRetrieveRoot
init|=
name|destIvyPattern
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|getTokenRoot
argument_list|(
name|destIvyPattern
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
name|targetArtifactsStructure
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|// Set(File) set of all paths
comment|// which should be present at
comment|// then end of retrieve (useful
comment|// for sync)
name|Collection
name|targetIvysStructure
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|// same for ivy files
comment|// do retrieve
name|int
name|targetsCopied
init|=
literal|0
decl_stmt|;
name|int
name|targetsUpToDate
init|=
literal|0
decl_stmt|;
name|long
name|totalCopiedSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifactsToCopy
operator|.
name|keySet
argument_list|()
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
name|ArtifactDownloadReport
name|artifact
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|File
name|archive
init|=
name|artifact
operator|.
name|getLocalFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|archive
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tno local file available for "
operator|+
name|artifact
operator|+
literal|": skipping"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Set
name|dest
init|=
operator|(
name|Set
operator|)
name|artifactsToCopy
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tretrieving "
operator|+
name|archive
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it2
init|=
name|dest
operator|.
name|iterator
argument_list|()
init|;
name|it2
operator|.
name|hasNext
argument_list|()
condition|;
control|)
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
name|destFile
init|=
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|it2
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|settings
operator|.
name|isCheckUpToDate
argument_list|()
operator|||
operator|!
name|upToDate
argument_list|(
name|archive
argument_list|,
name|destFile
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t\tto "
operator|+
name|destFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|isMakeSymlinks
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|symlink
argument_list|(
name|archive
argument_list|,
name|destFile
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|archive
argument_list|,
name|destFile
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|totalCopiedSize
operator|+=
name|destFile
operator|.
name|length
argument_list|()
expr_stmt|;
name|targetsCopied
operator|++
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t\tto "
operator|+
name|destFile
operator|+
literal|" [NOT REQUIRED]"
argument_list|)
expr_stmt|;
name|targetsUpToDate
operator|++
expr_stmt|;
block|}
if|if
condition|(
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|targetIvysStructure
operator|.
name|addAll
argument_list|(
name|FileUtil
operator|.
name|getPathFiles
argument_list|(
name|ivyRetrieveRoot
argument_list|,
name|destFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|targetArtifactsStructure
operator|.
name|addAll
argument_list|(
name|FileUtil
operator|.
name|getPathFiles
argument_list|(
name|fileRetrieveRoot
argument_list|,
name|destFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|options
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tsyncing..."
argument_list|)
expr_stmt|;
name|String
index|[]
name|ignorableFilenames
init|=
name|settings
operator|.
name|getIgnorableFilenames
argument_list|()
decl_stmt|;
name|Collection
name|ignoreList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ignorableFilenames
argument_list|)
decl_stmt|;
name|Collection
name|existingArtifacts
init|=
name|FileUtil
operator|.
name|listAll
argument_list|(
name|fileRetrieveRoot
argument_list|,
name|ignoreList
argument_list|)
decl_stmt|;
name|Collection
name|existingIvys
init|=
name|ivyRetrieveRoot
operator|==
literal|null
condition|?
literal|null
else|:
name|FileUtil
operator|.
name|listAll
argument_list|(
name|ivyRetrieveRoot
argument_list|,
name|ignoreList
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileRetrieveRoot
operator|.
name|equals
argument_list|(
name|ivyRetrieveRoot
argument_list|)
condition|)
block|{
name|Collection
name|target
init|=
name|targetArtifactsStructure
decl_stmt|;
name|target
operator|.
name|addAll
argument_list|(
name|targetIvysStructure
argument_list|)
expr_stmt|;
name|Collection
name|existing
init|=
name|existingArtifacts
decl_stmt|;
name|existing
operator|.
name|addAll
argument_list|(
name|existingIvys
argument_list|)
expr_stmt|;
name|sync
argument_list|(
name|target
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sync
argument_list|(
name|targetArtifactsStructure
argument_list|,
name|existingArtifacts
argument_list|)
expr_stmt|;
if|if
condition|(
name|existingIvys
operator|!=
literal|null
condition|)
block|{
name|sync
argument_list|(
name|targetIvysStructure
argument_list|,
name|existingIvys
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|long
name|elapsedTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|String
name|msg
init|=
literal|"\t"
operator|+
name|targetsCopied
operator|+
literal|" artifacts copied"
operator|+
operator|(
name|settings
operator|.
name|isCheckUpToDate
argument_list|()
condition|?
operator|(
literal|", "
operator|+
name|targetsUpToDate
operator|+
literal|" already retrieved"
operator|)
else|:
literal|""
operator|)
operator|+
literal|" ("
operator|+
operator|(
name|totalCopiedSize
operator|/
name|KILO
operator|)
operator|+
literal|"kB/"
operator|+
name|elapsedTime
operator|+
literal|"ms)"
decl_stmt|;
if|if
condition|(
name|LogOptions
operator|.
name|LOG_DEFAULT
operator|.
name|equals
argument_list|(
name|options
operator|.
name|getLog
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
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
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tretrieve done ("
operator|+
operator|(
name|elapsedTime
operator|)
operator|+
literal|"ms)"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|eventManager
operator|.
name|fireIvyEvent
argument_list|(
operator|new
name|EndRetrieveEvent
argument_list|(
name|mrid
argument_list|,
name|confs
argument_list|,
name|elapsedTime
argument_list|,
name|targetsCopied
argument_list|,
name|targetsUpToDate
argument_list|,
name|totalCopiedSize
argument_list|,
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|targetsCopied
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"problem during retrieve of "
operator|+
name|moduleId
operator|+
literal|": "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
index|[]
name|getConfs
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|RetrieveOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
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
try|try
block|{
name|File
name|ivyFile
init|=
name|getCache
argument_list|()
operator|.
name|getResolvedIvyFileInCache
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"no explicit confs given for retrieve, using ivy file: "
operator|+
name|ivyFile
argument_list|)
expr_stmt|;
name|URL
name|ivySource
init|=
name|ivyFile
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|ivySource
argument_list|)
decl_stmt|;
name|ModuleDescriptorParser
name|parser
init|=
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|getParser
argument_list|(
name|res
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"using "
operator|+
name|parser
operator|+
literal|" to parse "
operator|+
name|ivyFile
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|md
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|ivySource
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|confs
operator|=
name|md
operator|.
name|getConfigurationsNames
argument_list|()
expr_stmt|;
name|options
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|ioex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ioex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ioex
throw|;
block|}
block|}
return|return
name|confs
return|;
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
specifier|private
name|void
name|sync
parameter_list|(
name|Collection
name|target
parameter_list|,
name|Collection
name|existing
parameter_list|)
block|{
name|Collection
name|toRemove
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|existing
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
name|File
name|file
init|=
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|toRemove
operator|.
name|add
argument_list|(
name|file
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|target
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
name|File
name|file
init|=
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|toRemove
operator|.
name|remove
argument_list|(
name|file
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|toRemove
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
name|File
name|file
init|=
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t\tdeleting "
operator|+
name|file
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Map
name|determineArtifactsToCopy
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|destFilePattern
parameter_list|,
name|RetrieveOptions
name|options
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|ModuleId
name|moduleId
init|=
name|mrid
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getResolveId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|options
operator|.
name|setResolveId
argument_list|(
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
name|moduleId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ResolutionCacheManager
name|cacheManager
init|=
name|getCache
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|getConfs
argument_list|(
name|mrid
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|String
name|destIvyPattern
init|=
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|options
operator|.
name|getDestIvyPattern
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
comment|// find what we must retrieve where
comment|// ArtifactDownloadReport source -> Set (String copyDestAbsolutePath)
specifier|final
name|Map
name|artifactsToCopy
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// String copyDestAbsolutePath -> Set (ArtifactRevisionId source)
specifier|final
name|Map
name|conflictsMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// String copyDestAbsolutePath -> Set (ArtifactDownloadReport source)
specifier|final
name|Map
name|conflictsReportsMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// String copyDestAbsolutePath -> Set (String conf)
specifier|final
name|Map
name|conflictsConfMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
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
specifier|final
name|String
name|conf
init|=
name|confs
index|[
name|i
index|]
decl_stmt|;
name|File
name|report
init|=
name|cacheManager
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|options
operator|.
name|getResolveId
argument_list|()
argument_list|,
name|conf
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|Collection
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|parser
operator|.
name|getArtifactReports
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|destIvyPattern
operator|!=
literal|null
condition|)
block|{
name|ModuleRevisionId
index|[]
name|mrids
init|=
name|parser
operator|.
name|getRealDependencyRevisionIds
argument_list|()
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
name|mrids
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|parser
operator|.
name|getMetadataArtifactReport
argument_list|(
name|mrids
index|[
name|j
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
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
name|ArtifactDownloadReport
name|artifact
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|destPattern
init|=
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|?
name|destIvyPattern
else|:
name|destFilePattern
decl_stmt|;
if|if
condition|(
operator|!
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
operator|!
name|options
operator|.
name|getArtifactFilter
argument_list|()
operator|.
name|accept
argument_list|(
name|artifact
operator|.
name|getArtifact
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
comment|// skip this artifact, the filter didn't accept it!
block|}
name|String
name|destFileName
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|destPattern
argument_list|,
name|artifact
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|conf
argument_list|)
decl_stmt|;
name|Set
name|dest
init|=
operator|(
name|Set
operator|)
name|artifactsToCopy
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|dest
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|artifactsToCopy
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|dest
argument_list|)
expr_stmt|;
block|}
name|String
name|copyDest
init|=
operator|new
name|File
argument_list|(
name|destFileName
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|dest
operator|.
name|add
argument_list|(
name|copyDest
argument_list|)
expr_stmt|;
name|Set
name|conflicts
init|=
operator|(
name|Set
operator|)
name|conflictsMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
decl_stmt|;
name|Set
name|conflictsReports
init|=
operator|(
name|Set
operator|)
name|conflictsReportsMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
decl_stmt|;
name|Set
name|conflictsConf
init|=
operator|(
name|Set
operator|)
name|conflictsConfMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
decl_stmt|;
if|if
condition|(
name|conflicts
operator|==
literal|null
condition|)
block|{
name|conflicts
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|conflictsMap
operator|.
name|put
argument_list|(
name|copyDest
argument_list|,
name|conflicts
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conflictsReports
operator|==
literal|null
condition|)
block|{
name|conflictsReports
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|conflictsReportsMap
operator|.
name|put
argument_list|(
name|copyDest
argument_list|,
name|conflictsReports
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conflictsConf
operator|==
literal|null
condition|)
block|{
name|conflictsConf
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|conflictsConfMap
operator|.
name|put
argument_list|(
name|copyDest
argument_list|,
name|conflictsConf
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conflicts
operator|.
name|add
argument_list|(
name|artifact
operator|.
name|getArtifact
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|conflictsReports
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|conflictsConf
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// resolve conflicts if any
for|for
control|(
name|Iterator
name|iter
init|=
name|conflictsMap
operator|.
name|keySet
argument_list|()
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
name|String
name|copyDest
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Set
name|artifacts
init|=
operator|(
name|Set
operator|)
name|conflictsMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
decl_stmt|;
name|Set
name|conflictsConfs
init|=
operator|(
name|Set
operator|)
name|conflictsConfMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|List
name|artifactsList
init|=
operator|new
name|ArrayList
argument_list|(
operator|(
name|Collection
operator|)
name|conflictsReportsMap
operator|.
name|get
argument_list|(
name|copyDest
argument_list|)
argument_list|)
decl_stmt|;
comment|// conflicts battle is resolved by a sort using a conflict resolving policy
comment|// comparator
comment|// which consider as greater a winning artifact
name|Collections
operator|.
name|sort
argument_list|(
name|artifactsList
argument_list|,
name|getConflictResolvingPolicy
argument_list|()
argument_list|)
expr_stmt|;
comment|// after the sort, the winning artifact is the greatest one, i.e. the last one
name|Message
operator|.
name|info
argument_list|(
literal|"\tconflict on "
operator|+
name|copyDest
operator|+
literal|" in "
operator|+
name|conflictsConfs
operator|+
literal|": "
operator|+
operator|(
operator|(
name|ArtifactDownloadReport
operator|)
name|artifactsList
operator|.
name|get
argument_list|(
name|artifactsList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|" won"
argument_list|)
expr_stmt|;
comment|// we now iterate over the list beginning with the artifact preceding the winner,
comment|// and going backward to the least artifact
for|for
control|(
name|int
name|i
init|=
name|artifactsList
operator|.
name|size
argument_list|()
operator|-
literal|2
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|ArtifactDownloadReport
name|looser
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|artifactsList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t\tremoving conflict looser artifact: "
operator|+
name|looser
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
comment|// for each loser, we remove the pair (loser - copyDest) in the artifactsToCopy
comment|// map
name|Set
name|dest
init|=
operator|(
name|Set
operator|)
name|artifactsToCopy
operator|.
name|get
argument_list|(
name|looser
argument_list|)
decl_stmt|;
name|dest
operator|.
name|remove
argument_list|(
name|copyDest
argument_list|)
expr_stmt|;
if|if
condition|(
name|dest
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|artifactsToCopy
operator|.
name|remove
argument_list|(
name|looser
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|artifactsToCopy
return|;
block|}
specifier|private
name|boolean
name|upToDate
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|target
parameter_list|)
block|{
if|if
condition|(
operator|!
name|target
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|source
operator|.
name|lastModified
argument_list|()
operator|<=
name|target
operator|.
name|lastModified
argument_list|()
return|;
block|}
comment|/**      * The returned comparator should consider greater the artifact which gains the conflict battle.      * This is used only during retrieve... prefer resolve conflict manager to resolve conflicts.      *       * @return      */
specifier|private
name|Comparator
name|getConflictResolvingPolicy
parameter_list|()
block|{
return|return
operator|new
name|Comparator
argument_list|()
block|{
comment|// younger conflict resolving policy
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|Artifact
name|a1
init|=
operator|(
operator|(
name|ArtifactDownloadReport
operator|)
name|o1
operator|)
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
name|Artifact
name|a2
init|=
operator|(
operator|(
name|ArtifactDownloadReport
operator|)
name|o2
operator|)
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
if|if
condition|(
name|a1
operator|.
name|getPublicationDate
argument_list|()
operator|.
name|after
argument_list|(
name|a2
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
condition|)
block|{
comment|// a1 is after a2<=> a1 is younger than a2<=> a1 wins the conflict battle
return|return
operator|+
literal|1
return|;
block|}
if|else if
condition|(
name|a1
operator|.
name|getPublicationDate
argument_list|()
operator|.
name|before
argument_list|(
name|a2
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
condition|)
block|{
comment|// a1 is before a2<=> a2 is younger than a1<=> a2 wins the conflict battle
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

