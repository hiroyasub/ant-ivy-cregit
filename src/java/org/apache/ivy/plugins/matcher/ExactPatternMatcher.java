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
comment|/**  * Implementation of an exact matcher.<p/> The matching will be performed against an expression  * being a string. It will only matches if both strings are equal (per equals()) rule or if both  * strings are null.  */
end_comment

begin_class
specifier|public
comment|/* @Immutable */
specifier|final
class|class
name|ExactPatternMatcher
extends|extends
name|AbstractPatternMatcher
block|{
specifier|public
specifier|static
specifier|final
name|ExactPatternMatcher
name|INSTANCE
init|=
operator|new
name|ExactPatternMatcher
argument_list|()
decl_stmt|;
specifier|public
name|ExactPatternMatcher
parameter_list|()
block|{
name|super
argument_list|(
name|EXACT
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Matcher
name|newMatcher
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
operator|new
name|ExactMatcher
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|private
specifier|static
comment|/* @Immutable */
class|class
name|ExactMatcher
implements|implements
name|Matcher
block|{
specifier|private
name|String
name|expression
decl_stmt|;
specifier|public
name|ExactMatcher
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
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
return|return
name|input
operator|.
name|equals
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

