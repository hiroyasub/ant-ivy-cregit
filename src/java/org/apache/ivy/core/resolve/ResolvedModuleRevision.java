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
name|resolve
package|;
end_package

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
name|util
operator|.
name|Date
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
name|resolver
operator|.
name|DependencyResolver
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResolvedModuleRevision
block|{
comment|/**      * The resolver which resolved this ResolvedModuleRevision      * @return The resolver which resolved this ResolvedModuleRevision      */
name|DependencyResolver
name|getResolver
parameter_list|()
function_decl|;
comment|/**      * The resolver to use to download artifacts      * @return The resolver to use to download artifacts      */
name|DependencyResolver
name|getArtifactResolver
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getId
parameter_list|()
function_decl|;
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
function_decl|;
name|boolean
name|isDownloaded
parameter_list|()
function_decl|;
name|boolean
name|isSearched
parameter_list|()
function_decl|;
specifier|public
name|URL
name|getLocalMDUrl
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

