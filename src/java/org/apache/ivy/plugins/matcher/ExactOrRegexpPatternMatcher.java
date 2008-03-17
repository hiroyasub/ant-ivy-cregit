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
comment|/**  * A pattern matcher that tries to match exactly the input with the expression, or match it as a  * pattern.<p/> The evaluation for matching is perform first by checking if expression and input  * are equals (via equals method) else it attempts to do it by trying to match the input using the  * expression as a regexp.  *   * @see ExactPatternMatcher  * @see RegexpPatternMatcher  */
end_comment

begin_class
specifier|public
comment|/* @Immutable */
specifier|final
class|class
name|ExactOrRegexpPatternMatcher
extends|extends
name|AbstractPatternMatcher
block|{
specifier|public
specifier|static
specifier|final
name|ExactOrRegexpPatternMatcher
name|INSTANCE
init|=
operator|new
name|ExactOrRegexpPatternMatcher
argument_list|()
decl_stmt|;
specifier|public
name|ExactOrRegexpPatternMatcher
parameter_list|()
block|{
name|super
argument_list|(
name|EXACT_OR_REGEXP
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
name|ExactOrRegexpMatcher
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ExactOrRegexpMatcher
implements|implements
name|Matcher
block|{
specifier|private
name|Matcher
name|exact
decl_stmt|;
specifier|private
name|Matcher
name|regexp
decl_stmt|;
specifier|public
name|ExactOrRegexpMatcher
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|exact
operator|=
name|ExactPatternMatcher
operator|.
name|INSTANCE
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|regexp
operator|=
name|RegexpPatternMatcher
operator|.
name|INSTANCE
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
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
name|exact
operator|.
name|matches
argument_list|(
name|input
argument_list|)
operator|||
name|regexp
operator|.
name|matches
argument_list|(
name|input
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
return|return
name|regexp
operator|.
name|isExact
argument_list|()
return|;
comment|//&& exact.isExact();
block|}
block|}
block|}
end_class

end_unit

