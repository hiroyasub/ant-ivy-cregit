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
comment|/**  * Implemented by settings element which need to perform validation when settings are loaded.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Validatable
block|{
comment|/**      * Validates the Validatable, throwing an {@link IllegalStateException} if the current state is      * not valid.      *       * @throws IllegalStateException      *             if the state of the {@link Validatable} is not valid.      */
specifier|public
name|void
name|validate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

