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
name|util
package|;
end_package

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
name|List
import|;
end_import

begin_comment
comment|/**  * @author jerome@benois.fr  */
end_comment

begin_class
specifier|public
class|class
name|ParseUtil
block|{
comment|/**      * Parses delimited string and returns an array containing the tokens. This parser obeys quotes, so the delimiter      * character will be ignored if it is inside of a quote. This method assumes that the quote character is not      * included in the set of delimiter characters.      *       * @param value the delimited string to parse.      * @param delim the characters delimiting the tokens.      * @return an array of string tokens or null if there were no tokens.      */
comment|// method largely inspired by Apache Felix 1.0.4 ManifestParser method
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|String
index|[]
name|parseDelimitedString
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|delim
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|""
expr_stmt|;
block|}
specifier|final
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|final
name|int
name|CHAR
init|=
literal|1
decl_stmt|;
specifier|final
name|int
name|DELIMITER
init|=
literal|2
decl_stmt|;
specifier|final
name|int
name|STARTQUOTE
init|=
literal|4
decl_stmt|;
specifier|final
name|int
name|ENDQUOTE
init|=
literal|8
decl_stmt|;
specifier|final
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|expecting
init|=
operator|(
name|CHAR
operator||
name|DELIMITER
operator||
name|STARTQUOTE
operator|)
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
name|value
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|char
name|c
init|=
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|isDelimiter
init|=
operator|(
name|delim
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|>=
literal|0
operator|)
decl_stmt|;
specifier|final
name|boolean
name|isQuote
init|=
operator|(
name|c
operator|==
literal|'"'
operator|)
decl_stmt|;
if|if
condition|(
name|isDelimiter
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|DELIMITER
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|sb
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|expecting
operator|=
operator|(
name|CHAR
operator||
name|DELIMITER
operator||
name|STARTQUOTE
operator|)
expr_stmt|;
block|}
if|else if
condition|(
name|isQuote
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|STARTQUOTE
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|expecting
operator|=
name|CHAR
operator||
name|ENDQUOTE
expr_stmt|;
block|}
if|else if
condition|(
name|isQuote
operator|&&
operator|(
operator|(
name|expecting
operator|&
name|ENDQUOTE
operator|)
operator|>
literal|0
operator|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|expecting
operator|=
operator|(
name|CHAR
operator||
name|STARTQUOTE
operator||
name|DELIMITER
operator|)
expr_stmt|;
block|}
if|else if
condition|(
operator|(
name|expecting
operator|&
name|CHAR
operator|)
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid delimited string: "
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
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
name|list
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|list
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

