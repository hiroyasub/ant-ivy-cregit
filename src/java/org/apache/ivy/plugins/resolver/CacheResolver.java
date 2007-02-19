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
name|plugins
operator|.
name|resolver
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
name|Collections
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
name|report
operator|.
name|DownloadReport
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
name|DownloadStatus
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
name|DownloadOptions
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
name|IvyNode
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
name|ResolveData
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
name|ResolvedModuleRevision
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
name|search
operator|.
name|ModuleEntry
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
name|search
operator|.
name|OrganisationEntry
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
name|search
operator|.
name|RevisionEntry
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
name|settings
operator|.
name|IvySettings
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
name|util
operator|.
name|ResolvedResource
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
name|CacheResolver
extends|extends
name|FileSystemResolver
block|{
specifier|private
name|File
name|_configured
init|=
literal|null
decl_stmt|;
specifier|public
name|CacheResolver
parameter_list|()
block|{
block|}
specifier|public
name|CacheResolver
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"cache"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolvedModuleRevision
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
throws|throws
name|ParseException
block|{
name|clearIvyAttempts
argument_list|()
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
comment|// check revision
comment|// if we do not have to check modified and if the revision is exact and not changing,
comment|// we first search for it in cache
if|if
condition|(
operator|!
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
operator|.
name|isDynamic
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
name|ResolvedModuleRevision
name|rmr
init|=
name|data
operator|.
name|getCacheManager
argument_list|()
operator|.
name|findModuleInCache
argument_list|(
name|mrid
argument_list|,
name|doValidate
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmr
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": revision in cache: "
operator|+
name|mrid
argument_list|)
expr_stmt|;
return|return
name|rmr
return|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": no ivy file in cache found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|ensureConfigured
argument_list|(
name|data
operator|.
name|getSettings
argument_list|()
argument_list|,
name|data
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getCache
argument_list|()
argument_list|)
expr_stmt|;
name|ResolvedResource
name|ivyRef
init|=
name|findIvyFileRef
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|ivyRef
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": found ivy file in cache for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t\t=> "
operator|+
name|ivyRef
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|resolvedMrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|ivyRef
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
name|IvyNode
name|node
init|=
name|data
operator|.
name|getNode
argument_list|(
name|resolvedMrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getModuleRevision
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// this revision has already be resolved : return it
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": revision already resolved: "
operator|+
name|resolvedMrid
argument_list|)
expr_stmt|;
return|return
name|searchedRmr
argument_list|(
name|node
operator|.
name|getModuleRevision
argument_list|()
argument_list|)
return|;
block|}
name|ResolvedModuleRevision
name|rmr
init|=
name|data
operator|.
name|getCacheManager
argument_list|()
operator|.
name|findModuleInCache
argument_list|(
name|resolvedMrid
argument_list|,
name|doValidate
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmr
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": revision in cache: "
operator|+
name|resolvedMrid
argument_list|)
expr_stmt|;
return|return
name|searchedRmr
argument_list|(
name|rmr
argument_list|)
return|;
block|}
else|else
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": inconsistent cache: clean it and resolve again"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": no ivy file in cache found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|DownloadOptions
name|options
parameter_list|)
block|{
name|clearArtifactAttempts
argument_list|()
expr_stmt|;
name|DownloadReport
name|dr
init|=
operator|new
name|DownloadReport
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
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|ArtifactDownloadReport
name|adr
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
name|artifacts
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|dr
operator|.
name|addArtifactReport
argument_list|(
name|adr
argument_list|)
expr_stmt|;
name|File
name|archiveFile
init|=
name|options
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getArchiveFileInCache
argument_list|(
name|artifacts
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|archiveFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t[NOT REQUIRED] "
operator|+
name|artifacts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|)
expr_stmt|;
name|adr
operator|.
name|setSize
argument_list|(
name|archiveFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logArtifactNotFound
argument_list|(
name|artifacts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|FAILED
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dr
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ensureConfigured
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|exists
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|ensureConfigured
argument_list|()
expr_stmt|;
name|super
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
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
block|{
name|ensureConfigured
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|listOrganisations
argument_list|()
return|;
block|}
specifier|public
name|ModuleEntry
index|[]
name|listModules
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
name|ensureConfigured
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|listModules
argument_list|(
name|org
argument_list|)
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
block|{
name|ensureConfigured
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|listRevisions
argument_list|(
name|module
argument_list|)
return|;
block|}
specifier|public
name|void
name|dumpConfig
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|" [cache]"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ensureConfigured
parameter_list|()
block|{
if|if
condition|(
name|getSettings
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ensureConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|getSettings
argument_list|()
operator|.
name|getDefaultCache
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|ensureConfigured
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|==
literal|null
operator|||
name|cache
operator|==
literal|null
operator|||
operator|(
name|_configured
operator|!=
literal|null
operator|&&
name|_configured
operator|.
name|equals
argument_list|(
name|cache
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
name|setIvyPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|cache
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/"
operator|+
name|settings
operator|.
name|getCacheIvyPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setArtifactPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|cache
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/"
operator|+
name|settings
operator|.
name|getCacheArtifactPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"cache"
return|;
block|}
block|}
end_class

end_unit
