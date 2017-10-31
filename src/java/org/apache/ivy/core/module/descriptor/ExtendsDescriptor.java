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
name|module
operator|.
name|descriptor
package|;
end_package

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

begin_comment
comment|/**  * Describes parent descriptor information for a module descriptor.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExtendsDescriptor
block|{
comment|/**      * get the module revision id of the declared parent descriptor      *      * @return ModuleRevisionId      */
name|ModuleRevisionId
name|getParentRevisionId
parameter_list|()
function_decl|;
comment|/**      * get the resolved revision id for {@link #getParentRevisionId}, see      * {@link org.apache.ivy.core.module.descriptor.ModuleDescriptor#getResolvedModuleRevisionId()}      *      * @return ModuleRevisionId      */
name|ModuleRevisionId
name|getResolvedParentRevisionId
parameter_list|()
function_decl|;
name|ModuleDescriptor
name|getParentMd
parameter_list|()
function_decl|;
comment|/**      * If there is an explicit path to check for the parent descriptor, return it. Otherwise returns      * null.      *      * @return String      */
name|String
name|getLocation
parameter_list|()
function_decl|;
comment|/**      * Get the parts of the parent descriptor that are inherited. Default supported types are      *<code>info</code>,<code>description</code>,<code>configurations</code>,      *<code>dependencies</code>, and/or<code>all</code>. Ivy extensions may add support for      * additional extends types.      *      * @return String[]      */
name|String
index|[]
name|getExtendsTypes
parameter_list|()
function_decl|;
comment|/**      * @return true if the<code>all</code> extend type is specified, implying all other types      */
name|boolean
name|isAllInherited
parameter_list|()
function_decl|;
comment|/**      * @return true if parent info attributes are inherited (organisation, branch, revision, etc)      */
name|boolean
name|isInfoInherited
parameter_list|()
function_decl|;
comment|/**      * @return true if parent description is inherited      */
name|boolean
name|isDescriptionInherited
parameter_list|()
function_decl|;
comment|/**      * @return true if parent configurations are inherited      */
name|boolean
name|areConfigurationsInherited
parameter_list|()
function_decl|;
comment|/**      * @return true if parent dependencies are inherited      */
name|boolean
name|areDependenciesInherited
parameter_list|()
function_decl|;
name|boolean
name|isLocal
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

