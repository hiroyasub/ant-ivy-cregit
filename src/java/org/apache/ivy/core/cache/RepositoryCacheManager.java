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
name|resolve
operator|.
name|ResolvedModuleRevision
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
comment|/**      * Saves the information of which resolver was used to resolve a md, so that this info can be      * retrieve later (even after a jvm restart) by getSavedResolverName(ModuleDescriptor md)      *       * @param md      *            the module descriptor resolved      * @param name      *            resolver name      */
specifier|public
specifier|abstract
name|void
name|saveResolver
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Saves the information of which resolver was used to resolve a md, so that this info can be      * retrieve later (even after a jvm restart) by getSavedArtResolverName(ModuleDescriptor md)      *       * @param md      *            the module descriptor resolved      * @param name      *            artifact resolver name      */
specifier|public
specifier|abstract
name|void
name|saveArtResolver
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|saveArtifactOrigin
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
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
specifier|public
specifier|abstract
name|void
name|removeSavedArtifactOrigin
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
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
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

