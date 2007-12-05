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
name|cache
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
name|text
operator|.
name|ParseException
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
name|plugins
operator|.
name|repository
operator|.
name|ArtifactResourceResolver
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
name|ResourceDownloader
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
name|plugins
operator|.
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
import|;
end_import

begin_interface
specifier|public
interface|interface
name|RepositoryCacheManager
block|{
specifier|public
specifier|abstract
name|File
name|getRepositoryCacheRoot
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|File
name|getIvyFileInCache
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
function_decl|;
comment|/**      * Returns a File object pointing to where the artifact can be found on the local file system.      * This is usually in the cache, but it can be directly in the repository if it is local and if      * the resolve has been done with useOrigin = true      */
specifier|public
specifier|abstract
name|File
name|getArchiveFileInCache
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Returns a File object pointing to where the artifact can be found on the local file system.      * This is usually in the cache, but it can be directly in the repository if it is local and if      * the resolve has been done with useOrigin = true      */
specifier|public
specifier|abstract
name|File
name|getArchiveFileInCache
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|)
function_decl|;
comment|/**      * Returns a File object pointing to where the artifact can be found on the local file system,      * using or not the original location depending on the availability of origin information      * provided as parameter and the setting of useOrigin. If useOrigin is false, this method will      * always return the file in the cache.      */
specifier|public
specifier|abstract
name|File
name|getArchiveFileInCache
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|,
name|boolean
name|useOrigin
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|String
name|getArchivePathInCache
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|String
name|getArchivePathInCache
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|)
function_decl|;
comment|/**      * Saves the information of which resolvers were used to resolve a module (both for metadata and      * artifact), so that this info can be loaded later (even after a jvm restart) for the use of      * {@link #findModuleInCache(ModuleRevisionId, boolean, String)}.      *       * @param md      *            the module descriptor resolved      * @param metadataResolverName      *            metadata resolver name      * @param artifactResolverName      *            artifact resolver name      */
specifier|public
specifier|abstract
name|void
name|saveResolvers
parameter_list|(
name|ModuleDescriptor
name|descriptor
parameter_list|,
name|String
name|metadataResolverName
parameter_list|,
name|String
name|artifactResolverName
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|ArtifactOrigin
name|getSavedArtifactOrigin
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Search a module descriptor in cache for a mrid      *       * @param mrid      *            the id of the module to search      * @param validate      *            true to validate ivy file found in cache before returning      * @param expectedResolver      *            the resolver with which the md in cache must have been resolved to be returned,      *            null if this doesn't matter      * @return the ResolvedModuleRevision corresponding to the module found, null if none correct      *         has been found in cache      */
specifier|public
specifier|abstract
name|ResolvedModuleRevision
name|findModuleInCache
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|String
name|expectedResolver
parameter_list|)
function_decl|;
comment|/**      * Downloads an artifact to this cache.      *       * @param artifact      *            the artifact to download      * @param resourceResolver      *            a resource resolver to use if the artifact needs to be resolved to a Resource for      *            downloading      * @param resourceDownloader      *            a resource downloader to use if actual download of the resource is needed      * @param options      *            a set of options to adjust the download       * @return a report indicating how the download was performed      */
specifier|public
specifier|abstract
name|ArtifactDownloadReport
name|download
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactResourceResolver
name|resourceResolver
parameter_list|,
name|ResourceDownloader
name|resourceDownloader
parameter_list|,
name|CacheDownloadOptions
name|options
parameter_list|)
function_decl|;
specifier|public
name|ResolvedModuleRevision
name|cacheModuleDescriptor
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ResolvedResource
name|orginalMetadataRef
parameter_list|,
name|Artifact
name|requestedMetadataArtifact
parameter_list|,
name|ResourceDownloader
name|downloader
parameter_list|,
name|CacheMetadataOptions
name|options
parameter_list|)
throws|throws
name|ParseException
function_decl|;
specifier|public
name|void
name|originalToCachedModuleDescriptor
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ResolvedResource
name|orginalMetadataRef
parameter_list|,
name|Artifact
name|requestedMetadataArtifact
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|ModuleDescriptorWriter
name|writer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

