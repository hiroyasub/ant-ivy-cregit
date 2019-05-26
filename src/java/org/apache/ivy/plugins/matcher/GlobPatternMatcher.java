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
name|plugins
operator|.
name|matcher
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|oro
operator|.
name|text
operator|.
name|GlobCompiler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|oro
operator|.
name|text
operator|.
name|regex
operator|.
name|MalformedPatternException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|oro
operator|.
name|text
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|oro
operator|.
name|text
operator|.
name|regex
operator|.
name|Perl5Matcher
import|;
end_import

begin_comment
comment|/**  * A pattern matcher matching input using unix-like glob matcher expressions. Meta characters are:  *<ul>  *<li>* - Matches zero or more characters</li>  *<li>? - Matches exactly one character.</li>  *</ul>  *<p>  * NOTE: this matcher is available only with  *<a href="http://jakarta.apache.org/oro">Apache Jakarta ORO 2.0.8</a>  * in your classpath.  *</p>  *  * @see<a href="http://jakarta.apache.org/oro/api/org/apache/oro/text/GlobCompiler.html">GlobCompiler</a>  */
end_comment

begin_class
specifier|public
comment|/* @Immutable */
specifier|final
class|class
name|GlobPatternMatcher
extends|extends
name|AbstractPatternMatcher
block|{
specifier|public
specifier|static
specifier|final
name|GlobPatternMatcher
name|INSTANCE
init|=
operator|new
name|GlobPatternMatcher
argument_list|()
decl_stmt|;
comment|/*      * NOTE: GlobCompiler does ~100K compilation/s - If necessary look into using ThreadLocal for      * GlobCompiler/Perl5Matcher to cut on useless object creation - If expression are reused over      * and over a LRU cache could make sense      */
specifier|public
name|GlobPatternMatcher
parameter_list|()
block|{
name|super
argument_list|(
name|GLOB
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|GlobMatcher
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|GlobMatcher
implements|implements
name|Matcher
block|{
specifier|private
name|Pattern
name|pattern
decl_stmt|;
specifier|private
name|String
name|expression
decl_stmt|;
specifier|private
name|Boolean
name|exact
decl_stmt|;
specifier|public
name|GlobMatcher
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|PatternSyntaxException
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
try|try
block|{
name|pattern
operator|=
operator|new
name|GlobCompiler
argument_list|()
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedPatternException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PatternSyntaxException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|expression
argument_list|,
literal|0
argument_list|)
throw|;
block|}
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
operator|new
name|Perl5Matcher
argument_list|()
operator|.
name|matches
argument_list|(
name|input
argument_list|,
name|pattern
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
if|if
condition|(
name|exact
operator|==
literal|null
condition|)
block|{
name|exact
operator|=
name|calculateExact
argument_list|()
expr_stmt|;
block|}
return|return
name|exact
return|;
block|}
specifier|private
name|Boolean
name|calculateExact
parameter_list|()
block|{
name|Boolean
name|result
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
for|for
control|(
name|char
name|ch
range|:
name|expression
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|ch
operator|==
literal|'*'
operator|||
name|ch
operator|==
literal|'?'
operator|||
name|ch
operator|==
literal|'['
operator|||
name|ch
operator|==
literal|']'
condition|)
block|{
name|result
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

