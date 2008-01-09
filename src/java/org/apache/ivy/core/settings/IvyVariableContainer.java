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
name|settings
package|;
end_package

begin_comment
comment|/**  * Store and provide access to the ivy variables.  *   * @author gscokart  */
end_comment

begin_interface
specifier|public
interface|interface
name|IvyVariableContainer
extends|extends
name|Cloneable
block|{
specifier|public
name|void
name|setVariable
parameter_list|(
name|String
name|varName
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
function_decl|;
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Specifies the prefix used to indicate a variable is an environment      * variable. If the prefix doesn't end with a '.', it will be added      * automatically.      *       * @param prefix the prefix to use for the environment variables      */
specifier|public
name|void
name|setEnvironmentPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
specifier|public
name|Object
name|clone
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

