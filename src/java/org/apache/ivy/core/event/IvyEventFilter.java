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
name|event
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|matcher
operator|.
name|ExactPatternMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|matcher
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|matcher
operator|.
name|PatternMatcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|AndFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|NoFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|NotFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|OrFilter
import|;
end_import

begin_comment
comment|/**  * A filter implementation filtering {@link IvyEvent} based upon  * an event name and a filter expression.  *   * The name will be matched against the event name using the   * {@link PatternMatcher} used to construct this object.  *   * The filter expression is a string describing how the event  * should be filtered according to its attributes values.  * The matching between the filter values and the event attribute values  * is done using the {@link PatternMatcher} used to construct this object.  *   * Here are some examples:  *<table>  *<tr><td>expression</td><td>effect</td></tr>  *<tr><td>type=zip</td><td>accepts event with a type attribute matching zip</td></tr>  *<tr><td>type=zip,jar</td><td>accepts event with a type attribute matching zip or jar</td></tr>  *<tr><td>type=src AND ext=zip</td><td>accepts event with a type attribute matching src AND an ext attribute matching zip</td></tr>  *<tr><td>type=src OR ext=zip</td><td>accepts event with a type attribute matching src OR an ext attribute matching zip</td></tr>  *<tr><td>NOT type=src</td><td>accepts event with a type attribute NOT matching src</td></tr>  *</table>  *   * Combination of these can be used, but no parentheses are supported right now, so only the default priority can be used.  * The priority order is this one: AND OR NOT =  *   * This means that artifact=foo AND ext=zip OR type=src will match event with artifact matching foo AND (ext matching zip OR type matching src)  *   * @since 1.4  *  */
end_comment

begin_class
specifier|public
class|class
name|IvyEventFilter
implements|implements
name|Filter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NOT
init|=
literal|"NOT "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OR
init|=
literal|" OR "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|AND
init|=
literal|" AND "
decl_stmt|;
specifier|private
name|PatternMatcher
name|_matcher
decl_stmt|;
specifier|private
name|Filter
name|_nameFilter
decl_stmt|;
specifier|private
name|Filter
name|_attFilter
decl_stmt|;
specifier|public
name|IvyEventFilter
parameter_list|(
name|String
name|event
parameter_list|,
name|String
name|filterExpression
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|_matcher
operator|=
name|matcher
operator|==
literal|null
condition|?
name|ExactPatternMatcher
operator|.
name|INSTANCE
else|:
name|matcher
expr_stmt|;
if|if
condition|(
name|event
operator|==
literal|null
condition|)
block|{
name|_nameFilter
operator|=
name|NoFilter
operator|.
name|INSTANCE
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Matcher
name|eventNameMatcher
init|=
name|_matcher
operator|.
name|getMatcher
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|_nameFilter
operator|=
operator|new
name|Filter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|IvyEvent
name|e
init|=
operator|(
name|IvyEvent
operator|)
name|o
decl_stmt|;
return|return
name|eventNameMatcher
operator|.
name|matches
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
name|_attFilter
operator|=
name|filterExpression
operator|==
literal|null
operator|||
name|filterExpression
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
name|NoFilter
operator|.
name|INSTANCE
else|:
name|parseExpression
argument_list|(
name|filterExpression
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Filter
name|parseExpression
parameter_list|(
name|String
name|filterExpression
parameter_list|)
block|{
comment|// expressions handled for the moment: (informal grammar)
comment|// EXP := SIMPLE_EXP | AND_EXP | OR_EXP | NOT_EXP
comment|// AND_EXP := EXP&& EXP
comment|// OR_EXP := EXP || EXP
comment|// NOT_EXP := ! EXP
comment|// SIMPLE_EXP := attname = comma, separated, list, of, accepted, values
comment|// example: organisation = foo&& module = bar, baz
name|filterExpression
operator|=
name|filterExpression
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|index
init|=
name|filterExpression
operator|.
name|indexOf
argument_list|(
name|AND
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|index
operator|=
name|filterExpression
operator|.
name|indexOf
argument_list|(
name|OR
argument_list|)
expr_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|filterExpression
operator|.
name|startsWith
argument_list|(
name|NOT
argument_list|)
condition|)
block|{
return|return
operator|new
name|NotFilter
argument_list|(
name|parseExpression
argument_list|(
name|filterExpression
operator|.
name|substring
argument_list|(
name|NOT
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|index
operator|=
name|filterExpression
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"bad filter expression: "
operator|+
name|filterExpression
operator|+
literal|": no equal sign found"
argument_list|)
throw|;
block|}
specifier|final
name|String
name|attname
init|=
name|filterExpression
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
index|[]
name|values
init|=
name|filterExpression
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|Matcher
index|[]
name|matchers
init|=
operator|new
name|Matcher
index|[
name|values
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|matchers
index|[
name|i
index|]
operator|=
name|_matcher
operator|.
name|getMatcher
argument_list|(
name|values
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Filter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|IvyEvent
name|e
init|=
operator|(
name|IvyEvent
operator|)
name|o
decl_stmt|;
name|String
name|val
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attname
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|matchers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|matchers
index|[
name|i
index|]
operator|.
name|matches
argument_list|(
name|val
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|OrFilter
argument_list|(
name|parseExpression
argument_list|(
name|filterExpression
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
argument_list|,
name|parseExpression
argument_list|(
name|filterExpression
operator|.
name|substring
argument_list|(
name|index
operator|+
name|OR
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|AndFilter
argument_list|(
name|parseExpression
argument_list|(
name|filterExpression
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
argument_list|,
name|parseExpression
argument_list|(
name|filterExpression
operator|.
name|substring
argument_list|(
name|index
operator|+
name|AND
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|IvyEvent
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|_nameFilter
operator|.
name|accept
argument_list|(
name|o
argument_list|)
operator|&&
name|_attFilter
operator|.
name|accept
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
end_class

end_unit

