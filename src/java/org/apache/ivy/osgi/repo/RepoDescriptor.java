begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|osgi
operator|.
name|repo
package|;
end_package

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

begin_class
specifier|public
specifier|abstract
class|class
name|RepoDescriptor
block|{
specifier|public
specifier|abstract
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|getModules
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Set
argument_list|<
name|String
argument_list|>
name|getCapabilities
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|findModules
parameter_list|(
name|String
name|requirement
parameter_list|,
name|String
name|value
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|Set
argument_list|<
name|String
argument_list|>
name|getCapabilityValues
parameter_list|(
name|String
name|capabilityName
parameter_list|)
function_decl|;
block|}
end_class

end_unit

