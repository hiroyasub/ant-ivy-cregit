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
comment|/**      * Returns the name of the repository cache manager.      * @return the name of the repository cache manager.      */
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Saves the information of which resolvers were used to resolve a module (both for metadata and      * artifact), so that this info can be loaded later (even after a jvm restart) for the use of      * {@link #findModuleInCache(DependencyDescriptor, CacheMetadataOptions, String)}.      *       * @param md      *            the module descriptor resolved      * @param metadataResolverName      *            metadata resolver name      * @param artifactResolverName      *            artifact resolver name      */
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
comment|/**      * Returns the artifact origin of the given artifact as saved in this cache, or      * {@link ArtifactOrigin#UNKNOWN} if the origin is unknown.      *       * @param artifact      *            the artifact for which the saved artifact origin should be returned.      * @return the artifact origin of the given artifact as saved in this cache      */
specifier|public
specifier|abstract
name|ArtifactOrigin
name|getSavedArtifactOrigin
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Search a module descriptor in cache for a mrid      *       * @param dd      *            the dependency descriptor identifying the module to search      * @param requestedRevisionId      *            the requested dependency module revision id identifying the module to search      * @param options      *            options on how caching should be handled      * @param expectedResolver      *            the resolver with which the md in cache must have been resolved to be returned,      *            null if this doesn't matter      * @return the ResolvedModuleRevision corresponding to the module found, null if none correct      *         has been found in cache      */
specifier|public
specifier|abstract
name|ResolvedModuleRevision
name|findModuleInCache
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ModuleRevisionId
name|requestedRevisionId
parameter_list|,
name|CacheMetadataOptions
name|options
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
comment|/**      * Caches an original module descriptor.      *<p>      * After this call, the original module descriptor file (with no modification nor conversion)      * should be available as a local file.      *</p>      *       * @param resolver      *            the dependency resolver from which the cache request comes from      * @param orginalMetadataRef      *            a resolved resource pointing to the remote original module descriptor      * @param dd      *            the dependency descriptor for which the module descriptor should be cached      * @param requestedMetadataArtifact      *            the module descriptor artifact as requested originally      * @param downloader      *            a ResourceDownloader able to download the original module descriptor resource if      *            required by this cache implementation      * @param options      *            options to apply to cache this module descriptor      * @return a {@link ResolvedModuleRevision} representing the local cached module descriptor, or      *         null if it failed      * @throws ParseException      *             if an exception occured while parsing the module descriptor      */
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
name|DependencyDescriptor
name|dd
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
comment|/**      * Stores a standardized version of an original module descriptor in the cache for later use.      *       * @param resolver      *            the dependency resolver from which the cache request comes from      * @param orginalMetadataRef      *            a resolved resource pointing to the remote original module descriptor      * @param requestedMetadataArtifact      *            the module descriptor artifact as requested originally      * @param rmr      *            the {@link ResolvedModuleRevision} representing the local cached module descriptor      * @param writer      *            a {@link ModuleDescriptorWriter} able to write the module descriptor to a stream.      */
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
name|ResolvedModuleRevision
name|rmr
parameter_list|,
name|ModuleDescriptorWriter
name|writer
parameter_list|)
function_decl|;
comment|/**      * Cleans the whole cache.      */
specifier|public
name|void
name|clean
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

