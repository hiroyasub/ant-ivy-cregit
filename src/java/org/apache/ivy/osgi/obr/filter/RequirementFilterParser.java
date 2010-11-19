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
name|osgi
operator|.
name|obr
operator|.
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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
name|osgi
operator|.
name|obr
operator|.
name|filter
operator|.
name|CompareFilter
operator|.
name|Operator
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
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|RequirementFilter
import|;
end_import

begin_class
specifier|public
class|class
name|RequirementFilterParser
block|{
specifier|public
specifier|static
name|RequirementFilter
name|parse
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
operator|new
name|Parser
argument_list|(
name|text
argument_list|)
operator|.
name|parse
argument_list|()
return|;
block|}
specifier|static
class|class
name|Parser
block|{
comment|/**          * text to parse          */
specifier|private
specifier|final
name|String
name|text
decl_stmt|;
comment|/**          * the length of the source          */
specifier|private
name|int
name|length
decl_stmt|;
comment|/**          * position in the source          */
specifier|private
name|int
name|pos
init|=
literal|0
decl_stmt|;
comment|/**          * last read character          */
specifier|private
name|char
name|c
decl_stmt|;
comment|/**          * Default constructor          *           * @param header          *            the header to parse          */
name|Parser
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|text
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
comment|/**          * Do the parsing          *           * @return          *           * @throws ParseException          */
name|RequirementFilter
name|parse
parameter_list|()
throws|throws
name|ParseException
block|{
return|return
name|parseFilter
argument_list|()
return|;
block|}
specifier|private
name|char
name|readNext
parameter_list|()
block|{
if|if
condition|(
name|pos
operator|==
name|length
condition|)
block|{
name|c
operator|=
literal|'\0'
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
name|text
operator|.
name|charAt
argument_list|(
name|pos
operator|++
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|private
name|void
name|unread
parameter_list|()
block|{
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
name|pos
operator|--
expr_stmt|;
block|}
block|}
specifier|private
name|RequirementFilter
name|parseFilter
parameter_list|()
throws|throws
name|ParseException
block|{
name|skipWhiteSpace
argument_list|()
expr_stmt|;
name|readNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|'('
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting '(' as the start of the filter"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'&'
case|:
return|return
name|parseAnd
argument_list|()
return|;
case|case
literal|'|'
case|:
return|return
name|parseOr
argument_list|()
return|;
case|case
literal|'!'
case|:
return|return
name|parseNot
argument_list|()
return|;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return
name|parseCompare
argument_list|()
return|;
block|}
block|}
specifier|private
name|RequirementFilter
name|parseCompare
parameter_list|()
throws|throws
name|ParseException
block|{
name|String
name|leftValue
init|=
name|parseCompareValue
argument_list|()
decl_stmt|;
name|Operator
name|operator
init|=
name|parseCompareOperator
argument_list|()
decl_stmt|;
name|String
name|rightValue
init|=
name|parseCompareValue
argument_list|()
decl_stmt|;
name|readNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|')'
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting ')' as the end of the filter"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
return|return
operator|new
name|CompareFilter
argument_list|(
name|leftValue
argument_list|,
name|operator
argument_list|,
name|rightValue
argument_list|)
return|;
block|}
specifier|private
name|String
name|parseCompareValue
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
do|do
block|{
name|readNext
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isOperator
argument_list|(
name|c
argument_list|)
operator|&&
name|c
operator|!=
literal|')'
operator|&&
name|c
operator|!=
literal|'('
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|unread
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isOperator
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|ch
operator|==
literal|'='
operator|||
name|ch
operator|==
literal|'<'
operator|||
name|ch
operator|==
literal|'>'
return|;
block|}
specifier|private
name|Operator
name|parseCompareOperator
parameter_list|()
throws|throws
name|ParseException
block|{
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'='
case|:
return|return
name|Operator
operator|.
name|EQUALS
return|;
case|case
literal|'>'
case|:
if|if
condition|(
name|readNext
argument_list|()
operator|==
literal|'='
condition|)
block|{
return|return
name|Operator
operator|.
name|GREATER_OR_EQUAL
return|;
block|}
name|unread
argument_list|()
expr_stmt|;
return|return
name|Operator
operator|.
name|GREATER_THAN
return|;
case|case
literal|'<'
case|:
if|if
condition|(
name|readNext
argument_list|()
operator|==
literal|'='
condition|)
block|{
return|return
name|Operator
operator|.
name|LOWER_OR_EQUAL
return|;
block|}
name|unread
argument_list|()
expr_stmt|;
return|return
name|Operator
operator|.
name|LOWER_THAN
return|;
default|default:
break|break;
block|}
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting an operator: =,<,<=,> or>="
argument_list|,
name|pos
argument_list|)
throw|;
block|}
specifier|private
name|RequirementFilter
name|parseAnd
parameter_list|()
throws|throws
name|ParseException
block|{
name|AndFilter
name|filter
init|=
operator|new
name|AndFilter
argument_list|()
decl_stmt|;
name|parseMultiOperator
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
specifier|private
name|RequirementFilter
name|parseOr
parameter_list|()
throws|throws
name|ParseException
block|{
name|OrFilter
name|filter
init|=
operator|new
name|OrFilter
argument_list|()
decl_stmt|;
name|parseMultiOperator
argument_list|(
name|filter
argument_list|)
expr_stmt|;
return|return
name|filter
return|;
block|}
specifier|private
name|void
name|parseMultiOperator
parameter_list|(
name|MultiOperatorFilter
name|filter
parameter_list|)
throws|throws
name|ParseException
block|{
do|do
block|{
name|readNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'('
condition|)
block|{
name|unread
argument_list|()
expr_stmt|;
name|filter
operator|.
name|add
argument_list|(
name|parseFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
if|if
condition|(
name|filter
operator|.
name|getSubFilters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting at least on sub filter"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
block|}
specifier|private
name|RequirementFilter
name|parseNot
parameter_list|()
throws|throws
name|ParseException
block|{
name|readNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|'('
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"The ! operator is expecting a filter"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
name|unread
argument_list|()
expr_stmt|;
return|return
operator|new
name|NotFilter
argument_list|(
name|parseFilter
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|skipWhiteSpace
parameter_list|()
block|{
do|do
block|{
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|' '
case|:
continue|continue;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
block|}
block|}
block|}
end_class

end_unit

