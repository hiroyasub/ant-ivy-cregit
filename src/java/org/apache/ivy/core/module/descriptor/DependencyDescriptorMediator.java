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

begin_comment
comment|/**  * A DependencyDescriptorMediator is responsible for dependency descriptor mediation.  *<p>  * Dependency descriptor mediation consists in adjusting dependency descriptors according to a  * context, environment, the stack of dependers, ...  *</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyDescriptorMediator
block|{
comment|/**      * Mediates the given {@link DependencyDescriptor} according to this {@link ModuleDescriptor}.      *<p>      * This method gives the opportunity to a ModuleDescriptor to override dependency version      * information of any of its transitive dependencies, since it is called by dependency resolvers      * before actually resolving a dependency.      *</p>      *       * @param dd      *            the dependency descriptor which should be mediated.      * @return the mediated {@link DependencyDescriptor}, or the original      *         {@link DependencyDescriptor} if no mediation is required by this ModuleDescriptor.      */
name|DependencyDescriptor
name|mediate
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

