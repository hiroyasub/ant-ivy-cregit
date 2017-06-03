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
name|plugins
operator|.
name|matcher
package|;
end_package

begin_comment
comment|/**  * An interface that defines a string matcher.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Matcher
block|{
comment|/**      * Check whether a given string is matched by this matcher.      *      * @param input      *            the string to be matched. Cannot be null.      * @return true if the input string is matched, false otherwise.      */
specifier|public
name|boolean
name|matches
parameter_list|(
comment|/* @NotNull */
name|String
name|input
parameter_list|)
function_decl|;
comment|/**      * Return if the matcher will match *only* if the expression equals the input.<i> WARN: This is      * used only as a performance trick, to avoid scanning for things when you already know exactly      * what you want. In the install task where it used it avoid scanning the repository to list all      * modules to find that only one matches, and that it has the name requested.</i>      *      * @return true if the matcher only matches when the expression is equals to the input, false      *         otherwise.      */
specifier|public
name|boolean
name|isExact
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

