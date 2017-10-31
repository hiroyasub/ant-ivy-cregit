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
name|java
operator|.
name|net
operator|.
name|URL
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
name|extendable
operator|.
name|ExtendableItem
import|;
end_import

begin_comment
comment|/**  * This describes an artifact that is asked for a dependency. It is used to define an (additional)  * artifact not declared by a dependency module descriptor.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyArtifactDescriptor
extends|extends
name|ExtendableItem
block|{
comment|/**      * @return the dependency descriptor in which this dependency artifact descriptor is declared.      */
name|DependencyDescriptor
name|getDependencyDescriptor
parameter_list|()
function_decl|;
comment|/**      * @return the name of the artifact asked      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * @return the type of the artifact asked      */
name|String
name|getType
parameter_list|()
function_decl|;
comment|/**      * @return the ext of the artifact asked      */
name|String
name|getExt
parameter_list|()
function_decl|;
comment|/**      * @return the url to look this artifact up at      */
name|URL
name|getUrl
parameter_list|()
function_decl|;
comment|/**      * @return an array of configuration names of the module in which the artifact is asked      */
name|String
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

