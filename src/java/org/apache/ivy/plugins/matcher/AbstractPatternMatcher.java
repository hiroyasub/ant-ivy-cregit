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
comment|/**  * An abstract implementation of the pattern matcher providing base template methods  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPatternMatcher
implements|implements
name|PatternMatcher
block|{
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
comment|/**      * Create a new instance of a pattern matcher      *      * @param name      *            the name of the pattern matcher. Never null.      */
specifier|public
name|AbstractPatternMatcher
parameter_list|(
comment|/* @NotNull */
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
comment|/* @NotNull */
name|Matcher
name|getMatcher
parameter_list|(
comment|/* @NotNull */
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
if|if
condition|(
name|ANY_EXPRESSION
operator|.
name|equals
argument_list|(
name|expression
argument_list|)
condition|)
block|{
return|return
name|AnyMatcher
operator|.
name|INSTANCE
return|;
block|}
return|return
name|newMatcher
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|public
comment|/* @NotNull */
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Returns an instance of the implementation specific matcher.      *      * @param expression      *            the string to be matched.      * @return the instance of the related matcher. Never null.      */
specifier|protected
specifier|abstract
comment|/* @NotNull */
name|Matcher
name|newMatcher
parameter_list|(
comment|/* @NotNull */
name|String
name|expression
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

