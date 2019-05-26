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
name|core
operator|.
name|module
operator|.
name|descriptor
package|;
end_package

begin_comment
comment|/**  * Objects implementing this interface are aware of module configurations, and can thus be added to  * configurations, and list their configurations.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ConfigurationAware
block|{
comment|/**      * Returns the configurations of the module to which the object is attached      *      * @return an array of configuration names to which the object is attached      */
name|String
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * Tells this object that it will now be part of the given configuration      *      * @param confName      *            the name of the configuration to which the object is now attached      */
name|void
name|addConfiguration
parameter_list|(
name|String
name|confName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

