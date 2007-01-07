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
name|matcher
package|;
end_package

begin_comment
comment|/**  * Interface for a pattern matcher.  *<p/>  * The pattern matcher is the main abstraction regarding the matching of an expression. Implementation may vary  * depending on the expression syntax handling that is desired.  */
end_comment

begin_interface
specifier|public
interface|interface
name|PatternMatcher
block|{
comment|/**      * 'exact' pattern matcher name      */
specifier|public
specifier|static
specifier|final
name|String
name|EXACT
init|=
literal|"exact"
decl_stmt|;
comment|/**      * pattern matcher name 'regexp'      */
specifier|public
specifier|static
specifier|final
name|String
name|REGEXP
init|=
literal|"regexp"
decl_stmt|;
comment|/**      * pattern matcher 'glob'      */
specifier|public
specifier|static
specifier|final
name|String
name|GLOB
init|=
literal|"glob"
decl_stmt|;
comment|/**      * pattern matcher name 'exactOrRegexp'      */
specifier|public
specifier|static
specifier|final
name|String
name|EXACT_OR_REGEXP
init|=
literal|"exactOrRegexp"
decl_stmt|;
comment|/**      * Any expression string: '*'      */
specifier|public
specifier|static
specifier|final
name|String
name|ANY_EXPRESSION
init|=
literal|"*"
decl_stmt|;
comment|/**      * Return the matcher for the given expression.      *      * @param expression the expression to be matched. Cannot be null ?      * @return the matcher instance for the given expression. Never null.      */
specifier|public
comment|/*@NotNull*/
name|Matcher
name|getMatcher
parameter_list|(
comment|/*@NotNull*/
name|String
name|expression
parameter_list|)
function_decl|;
comment|/**      * return the name of this pattern matcher      *      * @return the name of this pattern matcher. Never null.      * @see #EXACT      * @see #REGEXP      * @see #GLOB      * @see #EXACT_OR_REGEXP      */
specifier|public
comment|/*@NotNull*/
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

