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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|/**  * Provides version range support.  */
end_comment

begin_class
specifier|public
class|class
name|VersionRange
block|{
specifier|private
name|boolean
name|startExclusive
decl_stmt|;
specifier|private
name|Version
name|startVersion
decl_stmt|;
specifier|private
name|boolean
name|endExclusive
decl_stmt|;
specifier|private
name|Version
name|endVersion
decl_stmt|;
specifier|public
name|VersionRange
parameter_list|(
name|String
name|versionStr
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|versionStr
argument_list|)
condition|)
block|{
name|startExclusive
operator|=
literal|false
expr_stmt|;
name|startVersion
operator|=
operator|new
name|Version
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|endExclusive
operator|=
literal|true
expr_stmt|;
name|endVersion
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
operator|new
name|VersionRangeParser
argument_list|(
name|versionStr
argument_list|)
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
block|}
class|class
name|VersionRangeParser
block|{
comment|/**          * value to parse          */
specifier|private
specifier|final
name|String
name|version
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
comment|/**          * Default constructor          *          * @param version          *            the version to parse          */
name|VersionRangeParser
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|length
operator|=
name|version
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
name|boolean
name|range
init|=
name|parseStart
argument_list|()
decl_stmt|;
name|startVersion
operator|=
name|parseVersion
argument_list|()
expr_stmt|;
if|if
condition|(
name|startVersion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting a number"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
if|if
condition|(
name|parseVersionSeparator
argument_list|()
condition|)
block|{
name|endVersion
operator|=
name|parseVersion
argument_list|()
expr_stmt|;
name|parseEnd
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|range
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting ,"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
else|else
block|{
comment|// simple number
name|endVersion
operator|=
literal|null
expr_stmt|;
name|startExclusive
operator|=
literal|false
expr_stmt|;
name|endExclusive
operator|=
literal|false
expr_stmt|;
block|}
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
name|version
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
name|boolean
name|parseStart
parameter_list|()
block|{
name|skipWhiteSpace
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'['
case|:
name|startExclusive
operator|=
literal|false
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'('
case|:
name|startExclusive
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
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
specifier|private
name|Version
name|parseVersion
parameter_list|()
block|{
name|Integer
name|major
init|=
name|parseNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|major
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Integer
name|minor
init|=
literal|0
decl_stmt|;
name|Integer
name|patch
init|=
literal|0
decl_stmt|;
name|String
name|qualifier
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parseNumberSeparator
argument_list|()
condition|)
block|{
name|minor
operator|=
name|parseNumber
argument_list|()
expr_stmt|;
if|if
condition|(
name|minor
operator|==
literal|null
condition|)
block|{
name|minor
operator|=
literal|0
expr_stmt|;
block|}
if|else if
condition|(
name|parseNumberSeparator
argument_list|()
condition|)
block|{
name|patch
operator|=
name|parseNumber
argument_list|()
expr_stmt|;
if|if
condition|(
name|patch
operator|==
literal|null
condition|)
block|{
name|patch
operator|=
literal|0
expr_stmt|;
block|}
if|else if
condition|(
name|parseNumberSeparator
argument_list|()
condition|)
block|{
name|qualifier
operator|=
name|parseQualifier
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|Version
argument_list|(
name|major
argument_list|,
name|minor
argument_list|,
name|patch
argument_list|,
name|qualifier
argument_list|)
return|;
block|}
specifier|private
name|Integer
name|parseNumber
parameter_list|()
block|{
name|skipWhiteSpace
argument_list|()
expr_stmt|;
name|Integer
name|n
init|=
literal|null
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
return|return
name|n
return|;
case|case
literal|'0'
case|:
case|case
literal|'1'
case|:
case|case
literal|'2'
case|:
case|case
literal|'3'
case|:
case|case
literal|'4'
case|:
case|case
literal|'5'
case|:
case|case
literal|'6'
case|:
case|case
literal|'7'
case|:
case|case
literal|'8'
case|:
case|case
literal|'9'
case|:
name|n
operator|=
operator|(
name|n
operator|==
literal|null
condition|?
literal|0
else|:
name|n
operator|*
literal|10
operator|)
operator|+
name|c
operator|-
literal|'0'
expr_stmt|;
break|break;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return
name|n
return|;
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
name|n
return|;
block|}
specifier|private
name|boolean
name|parseNumberSeparator
parameter_list|()
block|{
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|'.'
case|:
return|return
literal|true
return|;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|boolean
name|parseVersionSeparator
parameter_list|()
block|{
name|skipWhiteSpace
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|','
case|:
return|return
literal|true
return|;
default|default:
name|unread
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|String
name|parseQualifier
parameter_list|()
block|{
name|StringBuilder
name|q
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
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|||
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|||
name|c
operator|>=
literal|'0'
operator|&&
name|c
operator|<=
literal|'9'
operator|||
name|c
operator|==
literal|'-'
operator|||
name|c
operator|==
literal|'_'
condition|)
block|{
name|q
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
if|if
condition|(
name|q
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|q
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|parseEnd
parameter_list|()
throws|throws
name|ParseException
block|{
name|skipWhiteSpace
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|readNext
argument_list|()
condition|)
block|{
case|case
literal|']'
case|:
name|endExclusive
operator|=
literal|false
expr_stmt|;
break|break;
case|case
literal|')'
case|:
name|endExclusive
operator|=
literal|true
expr_stmt|;
break|break;
default|default:
name|unread
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Expecting ] or )"
argument_list|,
name|pos
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|VersionRange
parameter_list|(
name|boolean
name|startExclusive
parameter_list|,
name|Version
name|startVersion
parameter_list|,
name|boolean
name|endExclusive
parameter_list|,
name|Version
name|endVersion
parameter_list|)
block|{
name|this
operator|.
name|startExclusive
operator|=
name|startExclusive
expr_stmt|;
name|this
operator|.
name|startVersion
operator|=
name|startVersion
expr_stmt|;
name|this
operator|.
name|endExclusive
operator|=
name|endExclusive
expr_stmt|;
name|this
operator|.
name|endVersion
operator|=
name|endVersion
expr_stmt|;
block|}
specifier|public
name|VersionRange
parameter_list|(
name|Version
name|startVersion
parameter_list|)
block|{
name|this
operator|.
name|startExclusive
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|startVersion
operator|=
name|startVersion
expr_stmt|;
name|this
operator|.
name|endExclusive
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|endVersion
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|(
name|startExclusive
condition|?
literal|"("
else|:
literal|"["
operator|)
operator|+
name|startVersion
operator|.
name|toString
argument_list|()
operator|+
literal|","
operator|+
operator|(
name|endVersion
operator|==
literal|null
condition|?
literal|""
else|:
name|endVersion
operator|.
name|toString
argument_list|()
operator|)
operator|+
operator|(
name|endExclusive
condition|?
literal|")"
else|:
literal|"]"
operator|)
return|;
block|}
specifier|public
name|String
name|toIvyRevision
parameter_list|()
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|startExclusive
condition|?
literal|"("
else|:
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|startVersion
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
if|if
condition|(
name|endVersion
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|endExclusive
operator|||
name|startVersion
operator|.
name|equals
argument_list|(
name|endVersion
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|endVersion
operator|.
name|withNudgedPatch
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
name|endVersion
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isEndExclusive
parameter_list|()
block|{
return|return
name|this
operator|.
name|endExclusive
return|;
block|}
specifier|public
name|Version
name|getEndVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|endVersion
return|;
block|}
specifier|public
name|boolean
name|isStartExclusive
parameter_list|()
block|{
return|return
name|this
operator|.
name|startExclusive
return|;
block|}
specifier|public
name|Version
name|getStartVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|startVersion
return|;
block|}
specifier|public
name|boolean
name|isClosedRange
parameter_list|()
block|{
return|return
name|startVersion
operator|.
name|equals
argument_list|(
name|endVersion
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|versionStr
parameter_list|)
block|{
return|return
name|contains
argument_list|(
operator|new
name|Version
argument_list|(
name|versionStr
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|contains
parameter_list|(
name|Version
name|version
parameter_list|)
block|{
return|return
operator|(
name|startExclusive
condition|?
name|version
operator|.
name|compareUnqualified
argument_list|(
name|startVersion
argument_list|)
operator|>
literal|0
else|:
name|version
operator|.
name|compareUnqualified
argument_list|(
name|startVersion
argument_list|)
operator|>=
literal|0
operator|)
operator|&&
operator|(
name|endVersion
operator|==
literal|null
operator|||
operator|(
name|endExclusive
condition|?
name|version
operator|.
name|compareUnqualified
argument_list|(
name|endVersion
argument_list|)
operator|<
literal|0
else|:
name|version
operator|.
name|compareUnqualified
argument_list|(
name|endVersion
argument_list|)
operator|<=
literal|0
operator|)
operator|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|endExclusive
condition|?
literal|1231
else|:
literal|1237
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|endVersion
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|endVersion
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|startExclusive
condition|?
literal|1231
else|:
literal|1237
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|startVersion
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|startVersion
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
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
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
operator|||
operator|!
operator|(
name|obj
operator|instanceof
name|VersionRange
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|VersionRange
name|other
init|=
operator|(
name|VersionRange
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|endExclusive
operator|!=
name|other
operator|.
name|endExclusive
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|endVersion
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|endVersion
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|endVersion
operator|.
name|equals
argument_list|(
name|other
operator|.
name|endVersion
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|startExclusive
operator|==
name|other
operator|.
name|startExclusive
operator|&&
operator|(
name|startVersion
operator|==
literal|null
condition|?
name|other
operator|.
name|startVersion
operator|==
literal|null
else|:
name|startVersion
operator|.
name|equals
argument_list|(
name|other
operator|.
name|startVersion
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

