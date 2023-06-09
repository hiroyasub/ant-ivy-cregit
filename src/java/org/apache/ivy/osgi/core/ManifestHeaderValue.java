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
name|osgi
operator|.
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Parse a header of a manifest. The manifest header is composed with the following rules:  *  *<pre>  * header ::= header-element (',' header-element)*  * header-element ::= values (';' (attribute | directive) )*  * values ::= value (';' value)*  * value ::=&lt;any string value that does not have ';' or ','&gt;  * attribute ::= key '=' value  * directive ::= key '=' value  * key ::= token  * value ::= token | quoted-string | double-quoted-string  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|ManifestHeaderValue
block|{
specifier|private
name|List
argument_list|<
name|ManifestHeaderElement
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ManifestHeaderValue
parameter_list|()
block|{
comment|// just for unit testing
block|}
specifier|public
name|ManifestHeaderValue
parameter_list|(
name|String
name|header
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
operator|new
name|ManifestHeaderParser
argument_list|(
name|header
argument_list|)
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|ManifestHeaderElement
argument_list|>
name|getElements
parameter_list|()
block|{
return|return
name|elements
return|;
block|}
specifier|public
name|String
name|getSingleValue
parameter_list|()
block|{
if|if
condition|(
name|elements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|getElements
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValues
argument_list|()
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|values
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getValues
parameter_list|()
block|{
if|if
condition|(
name|elements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ManifestHeaderElement
name|element
range|:
name|getElements
argument_list|()
control|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|element
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
name|void
name|addElement
parameter_list|(
name|ManifestHeaderElement
name|element
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
class|class
name|ManifestHeaderParser
block|{
comment|/**          * header to parse          */
specifier|private
specifier|final
name|String
name|header
decl_stmt|;
comment|/**          * the length of the source          */
specifier|private
name|int
name|length
decl_stmt|;
comment|/**          * buffer          */
specifier|private
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
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
comment|/**          * the header element being build          */
specifier|private
name|ManifestHeaderElement
name|elem
init|=
operator|new
name|ManifestHeaderElement
argument_list|()
decl_stmt|;
comment|/**          * Once at true (at the first attribute parsed), only parameters are allowed          */
specifier|private
name|boolean
name|valuesParsed
decl_stmt|;
comment|/**          * the last parsed parameter name          */
specifier|private
name|String
name|paramName
decl_stmt|;
comment|/**          * true if the last parsed parameter is a directive (assigned via :=)          */
specifier|private
name|boolean
name|isDirective
decl_stmt|;
comment|/**          * Default constructor          *          * @param header          *            the header to parse          */
name|ManifestHeaderParser
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|header
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
comment|/**          * Do the parsing          *          * @throws ParseException if something goes wrong          */
name|void
name|parse
parameter_list|()
throws|throws
name|ParseException
block|{
do|do
block|{
name|elem
operator|=
operator|new
name|ManifestHeaderElement
argument_list|()
expr_stmt|;
name|int
name|posElement
init|=
name|pos
decl_stmt|;
name|parseElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|elem
operator|.
name|getValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|error
argument_list|(
literal|"No defined value"
argument_list|,
name|posElement
argument_list|)
expr_stmt|;
comment|// try to recover: ignore that element
continue|continue;
block|}
name|addElement
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
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
name|header
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
name|error
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|ParseException
block|{
name|error
argument_list|(
name|message
argument_list|,
name|pos
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|p
parameter_list|)
throws|throws
name|ParseException
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
name|message
argument_list|,
name|p
argument_list|)
throw|;
block|}
specifier|private
name|void
name|parseElement
parameter_list|()
throws|throws
name|ParseException
block|{
name|valuesParsed
operator|=
literal|false
expr_stmt|;
do|do
block|{
name|parseValueOrParameter
argument_list|()
expr_stmt|;
block|}
do|while
condition|(
name|c
operator|==
literal|';'
operator|&&
name|pos
operator|<
name|length
condition|)
do|;
block|}
specifier|private
name|void
name|parseValueOrParameter
parameter_list|()
throws|throws
name|ParseException
block|{
comment|// true if the value/parameter parsing has started, white spaces skipped
name|boolean
name|start
init|=
literal|false
decl_stmt|;
do|do
block|{
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'\0'
case|:
break|break;
case|case
literal|';'
case|:
case|case
literal|','
case|:
name|endValue
argument_list|()
expr_stmt|;
return|return;
case|case
literal|':'
case|:
case|case
literal|'='
case|:
name|endParameterName
argument_list|()
expr_stmt|;
name|parseSeparator
argument_list|()
expr_stmt|;
name|parseParameterValue
argument_list|()
expr_stmt|;
return|return;
case|case
literal|' '
case|:
case|case
literal|'\t'
case|:
case|case
literal|'\n'
case|:
case|case
literal|'\r'
case|:
if|if
condition|(
name|start
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|start
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
name|endValue
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|endValue
parameter_list|()
throws|throws
name|ParseException
block|{
if|if
condition|(
name|valuesParsed
condition|)
block|{
name|error
argument_list|(
literal|"Early end of a parameter"
argument_list|)
expr_stmt|;
comment|// try to recover: ignore it
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|error
argument_list|(
literal|"Empty value"
argument_list|)
expr_stmt|;
comment|// try to recover: just ignore the error
block|}
name|elem
operator|.
name|addValue
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|endParameterName
parameter_list|()
throws|throws
name|ParseException
block|{
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|error
argument_list|(
literal|"Empty parameter name"
argument_list|)
expr_stmt|;
comment|// try to recover: won't store the value
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
name|paramName
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|parseSeparator
parameter_list|()
throws|throws
name|ParseException
block|{
if|if
condition|(
name|c
operator|==
literal|'='
condition|)
block|{
name|isDirective
operator|=
literal|false
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|readNext
argument_list|()
operator|!=
literal|'='
condition|)
block|{
name|error
argument_list|(
literal|"Expecting '='"
argument_list|)
expr_stmt|;
comment|// try to recover: will ignore this parameter
name|pos
operator|--
expr_stmt|;
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
name|isDirective
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|void
name|parseParameterValue
parameter_list|()
throws|throws
name|ParseException
block|{
comment|// true if the value parsing has started, white spaces skipped
name|boolean
name|start
init|=
literal|false
decl_stmt|;
comment|// true if the value parsing is ended, then only white spaces are allowed
name|boolean
name|end
init|=
literal|false
decl_stmt|;
name|boolean
name|doubleQuoted
init|=
literal|false
decl_stmt|;
do|do
block|{
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'\0'
case|:
break|break;
case|case
literal|','
case|:
case|case
literal|';'
case|:
name|endParameterValue
argument_list|()
expr_stmt|;
return|return;
case|case
literal|'='
case|:
case|case
literal|':'
case|:
name|error
argument_list|(
literal|"Illegal character '"
operator|+
name|c
operator|+
literal|"' in parameter value of "
operator|+
name|paramName
argument_list|)
expr_stmt|;
comment|// try to recover: ignore that parameter
name|paramName
operator|=
literal|null
expr_stmt|;
break|break;
case|case
literal|'\"'
case|:
name|doubleQuoted
operator|=
literal|true
expr_stmt|;
case|case
literal|'\''
case|:
if|if
condition|(
name|end
operator|&&
name|paramName
operator|!=
literal|null
condition|)
block|{
name|error
argument_list|(
literal|"Expecting the end of a parameter value"
argument_list|)
expr_stmt|;
comment|// try to recover: ignore that parameter
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|start
condition|)
block|{
comment|// quote in the middle of the value, just add it as a quote
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|start
operator|=
literal|true
expr_stmt|;
name|appendQuoted
argument_list|(
name|doubleQuoted
argument_list|)
expr_stmt|;
name|end
operator|=
literal|true
expr_stmt|;
block|}
break|break;
case|case
literal|'\\'
case|:
if|if
condition|(
name|end
operator|&&
name|paramName
operator|!=
literal|null
condition|)
block|{
name|error
argument_list|(
literal|"Expecting the end of a parameter value"
argument_list|)
expr_stmt|;
comment|// try to recover: ignore that parameter
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
name|start
operator|=
literal|true
expr_stmt|;
name|appendEscaped
argument_list|()
expr_stmt|;
break|break;
case|case
literal|' '
case|:
case|case
literal|'\t'
case|:
case|case
literal|'\n'
case|:
case|case
literal|'\r'
case|:
if|if
condition|(
name|start
condition|)
block|{
name|end
operator|=
literal|true
expr_stmt|;
block|}
break|break;
default|default:
if|if
condition|(
name|end
operator|&&
name|paramName
operator|!=
literal|null
condition|)
block|{
name|error
argument_list|(
literal|"Expecting the end of a parameter value"
argument_list|)
expr_stmt|;
comment|// try to recover: ignore that parameter
name|paramName
operator|=
literal|null
expr_stmt|;
block|}
name|start
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|pos
operator|<
name|length
condition|)
do|;
name|endParameterValue
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|endParameterValue
parameter_list|()
throws|throws
name|ParseException
block|{
if|if
condition|(
name|paramName
operator|==
literal|null
condition|)
block|{
comment|// recovering from an incorrect parameter: skip the value
return|return;
block|}
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|error
argument_list|(
literal|"Empty parameter value"
argument_list|)
expr_stmt|;
comment|// try to recover: do not store the parameter
return|return;
block|}
name|String
name|value
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|isDirective
condition|)
block|{
name|elem
operator|.
name|addDirective
argument_list|(
name|paramName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|elem
operator|.
name|addAttribute
argument_list|(
name|paramName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|valuesParsed
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendQuoted
parameter_list|(
name|boolean
name|doubleQuoted
parameter_list|)
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
literal|'\0'
case|:
break|break;
case|case
literal|'\"'
case|:
if|if
condition|(
name|doubleQuoted
condition|)
block|{
return|return;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\''
case|:
if|if
condition|(
operator|!
name|doubleQuoted
condition|)
block|{
return|return;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\\'
case|:
break|break;
default|default:
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
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
specifier|private
name|void
name|appendEscaped
parameter_list|()
block|{
if|if
condition|(
name|pos
operator|<
name|length
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|readNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|ManifestHeaderValue
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ManifestHeaderValue
name|other
init|=
operator|(
name|ManifestHeaderValue
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|other
operator|.
name|elements
operator|.
name|size
argument_list|()
operator|!=
name|elements
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|ManifestHeaderElement
name|element
range|:
name|elements
control|)
block|{
if|if
condition|(
operator|!
name|other
operator|.
name|elements
operator|.
name|contains
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ManifestHeaderElement
name|element
range|:
name|elements
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|element
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|writeParseException
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|String
name|source
parameter_list|,
name|ParseException
name|e
parameter_list|)
block|{
name|out
operator|.
name|println
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"   "
operator|+
name|source
operator|+
literal|"\n   "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|e
operator|.
name|getErrorOffset
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|'^'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

