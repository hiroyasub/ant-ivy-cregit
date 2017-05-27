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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * Convenient class used only for uncapitalization. Usually use commons lang but here we do not want  * to have such a dependency for only one feature  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|StringUtils
block|{
specifier|private
name|StringUtils
parameter_list|()
block|{
comment|// Utility class
block|}
specifier|public
specifier|static
name|String
name|uncapitalize
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|==
literal|null
operator|||
name|string
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|string
return|;
block|}
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|string
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
return|return
name|string
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|+
name|string
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
comment|/**      * Returns the error message associated with the given Throwable. The error message returned      * will try to be as precise as possible, handling cases where e.getMessage() is not meaningful,      * like {@link NullPointerException} for instance.      *       * @param t      *            the throwable to get the error message from      * @return the error message of the given exception      */
specifier|public
specifier|static
name|String
name|getErrorMessage
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
if|if
condition|(
name|t
operator|instanceof
name|InvocationTargetException
condition|)
block|{
name|InvocationTargetException
name|ex
init|=
operator|(
name|InvocationTargetException
operator|)
name|t
decl_stmt|;
name|t
operator|=
name|ex
operator|.
name|getTargetException
argument_list|()
expr_stmt|;
block|}
name|String
name|errMsg
init|=
name|t
operator|instanceof
name|RuntimeException
condition|?
name|t
operator|.
name|getMessage
argument_list|()
else|:
name|t
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|errMsg
operator|==
literal|null
operator|||
name|errMsg
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
literal|"null"
operator|.
name|equals
argument_list|(
name|errMsg
argument_list|)
condition|)
block|{
name|errMsg
operator|=
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" at "
operator|+
name|t
operator|.
name|getStackTrace
argument_list|()
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|errMsg
return|;
block|}
comment|/**      * Returns the exception stack trace as a String.      *       * @param e      *            the exception to get the stack trace from.      * @return the exception stack trace      */
specifier|public
specifier|static
name|String
name|getStackTrace
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|printWriter
init|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|printWriter
argument_list|)
expr_stmt|;
return|return
name|sw
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Joins the given object array in one string, each separated by the given separator.      *       * Example:      *       *<pre>      * join(new String[] {"one", "two", "three"}, ", ") -> "one, two, three"      *</pre>      *       * @param objs      *            The array of objects (<code>toString()</code> is used).      * @param sep      *            The separator to use.      * @return The concatenated string.      */
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|Object
index|[]
name|objs
parameter_list|,
name|String
name|sep
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|objs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|objs
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
name|sep
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|objs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|setLength
argument_list|(
name|buf
operator|.
name|length
argument_list|()
operator|-
name|sep
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// delete sep
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// basic string codec (same algo as CVS passfile, inspired by ant CVSPass class
comment|/** Array contain char conversion data */
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|SHIFTS
init|=
block|{
literal|0
block|,
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|,
literal|9
block|,
literal|10
block|,
literal|11
block|,
literal|12
block|,
literal|13
block|,
literal|14
block|,
literal|15
block|,
literal|16
block|,
literal|17
block|,
literal|18
block|,
literal|19
block|,
literal|20
block|,
literal|21
block|,
literal|22
block|,
literal|23
block|,
literal|24
block|,
literal|25
block|,
literal|26
block|,
literal|27
block|,
literal|28
block|,
literal|29
block|,
literal|30
block|,
literal|31
block|,
literal|114
block|,
literal|120
block|,
literal|53
block|,
literal|79
block|,
literal|96
block|,
literal|109
block|,
literal|72
block|,
literal|108
block|,
literal|70
block|,
literal|64
block|,
literal|76
block|,
literal|67
block|,
literal|116
block|,
literal|74
block|,
literal|68
block|,
literal|87
block|,
literal|111
block|,
literal|52
block|,
literal|75
block|,
literal|119
block|,
literal|49
block|,
literal|34
block|,
literal|82
block|,
literal|81
block|,
literal|95
block|,
literal|65
block|,
literal|112
block|,
literal|86
block|,
literal|118
block|,
literal|110
block|,
literal|122
block|,
literal|105
block|,
literal|41
block|,
literal|57
block|,
literal|83
block|,
literal|43
block|,
literal|46
block|,
literal|102
block|,
literal|40
block|,
literal|89
block|,
literal|38
block|,
literal|103
block|,
literal|45
block|,
literal|50
block|,
literal|42
block|,
literal|123
block|,
literal|91
block|,
literal|35
block|,
literal|125
block|,
literal|55
block|,
literal|54
block|,
literal|66
block|,
literal|124
block|,
literal|126
block|,
literal|59
block|,
literal|47
block|,
literal|92
block|,
literal|71
block|,
literal|115
block|,
literal|78
block|,
literal|88
block|,
literal|107
block|,
literal|106
block|,
literal|56
block|,
literal|36
block|,
literal|121
block|,
literal|117
block|,
literal|104
block|,
literal|101
block|,
literal|100
block|,
literal|69
block|,
literal|73
block|,
literal|99
block|,
literal|63
block|,
literal|94
block|,
literal|93
block|,
literal|39
block|,
literal|37
block|,
literal|61
block|,
literal|48
block|,
literal|58
block|,
literal|113
block|,
literal|32
block|,
literal|90
block|,
literal|44
block|,
literal|98
block|,
literal|60
block|,
literal|51
block|,
literal|33
block|,
literal|97
block|,
literal|62
block|,
literal|77
block|,
literal|84
block|,
literal|80
block|,
literal|85
block|,
literal|223
block|,
literal|225
block|,
literal|216
block|,
literal|187
block|,
literal|166
block|,
literal|229
block|,
literal|189
block|,
literal|222
block|,
literal|188
block|,
literal|141
block|,
literal|249
block|,
literal|148
block|,
literal|200
block|,
literal|184
block|,
literal|136
block|,
literal|248
block|,
literal|190
block|,
literal|199
block|,
literal|170
block|,
literal|181
block|,
literal|204
block|,
literal|138
block|,
literal|232
block|,
literal|218
block|,
literal|183
block|,
literal|255
block|,
literal|234
block|,
literal|220
block|,
literal|247
block|,
literal|213
block|,
literal|203
block|,
literal|226
block|,
literal|193
block|,
literal|174
block|,
literal|172
block|,
literal|228
block|,
literal|252
block|,
literal|217
block|,
literal|201
block|,
literal|131
block|,
literal|230
block|,
literal|197
block|,
literal|211
block|,
literal|145
block|,
literal|238
block|,
literal|161
block|,
literal|179
block|,
literal|160
block|,
literal|212
block|,
literal|207
block|,
literal|221
block|,
literal|254
block|,
literal|173
block|,
literal|202
block|,
literal|146
block|,
literal|224
block|,
literal|151
block|,
literal|140
block|,
literal|196
block|,
literal|205
block|,
literal|130
block|,
literal|135
block|,
literal|133
block|,
literal|143
block|,
literal|246
block|,
literal|192
block|,
literal|159
block|,
literal|244
block|,
literal|239
block|,
literal|185
block|,
literal|168
block|,
literal|215
block|,
literal|144
block|,
literal|139
block|,
literal|165
block|,
literal|180
block|,
literal|157
block|,
literal|147
block|,
literal|186
block|,
literal|214
block|,
literal|176
block|,
literal|227
block|,
literal|231
block|,
literal|219
block|,
literal|169
block|,
literal|175
block|,
literal|156
block|,
literal|206
block|,
literal|198
block|,
literal|129
block|,
literal|164
block|,
literal|150
block|,
literal|210
block|,
literal|154
block|,
literal|177
block|,
literal|134
block|,
literal|127
block|,
literal|182
block|,
literal|128
block|,
literal|158
block|,
literal|208
block|,
literal|162
block|,
literal|132
block|,
literal|167
block|,
literal|209
block|,
literal|149
block|,
literal|241
block|,
literal|153
block|,
literal|251
block|,
literal|237
block|,
literal|236
block|,
literal|171
block|,
literal|195
block|,
literal|243
block|,
literal|233
block|,
literal|253
block|,
literal|240
block|,
literal|194
block|,
literal|250
block|,
literal|191
block|,
literal|155
block|,
literal|142
block|,
literal|137
block|,
literal|245
block|,
literal|235
block|,
literal|163
block|,
literal|242
block|,
literal|178
block|,
literal|152
block|}
decl_stmt|;
comment|/**      * Encrypt the given string in a way which anybody having access to this method algorithm can      * easily decrypt. This is useful only to avoid clear string storage in a file for example, but      * shouldn't be considered as a real mean of security. This only works with simple characters      * (char< 256).      *       * @param str      *            the string to encrypt      * @return the encrypted version of the string      */
specifier|public
specifier|static
specifier|final
name|String
name|encrypt
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|>=
name|SHIFTS
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"encrypt method can only be used with simple characters. '"
operator|+
name|c
operator|+
literal|"' not allowed"
argument_list|)
throw|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|SHIFTS
index|[
name|c
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Decrypts a string encrypted with encrypt.      *       * @param str      *            the encrypted string to decrypt      * @return The decrypted string.      */
specifier|public
specifier|static
specifier|final
name|String
name|decrypt
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|decrypt
argument_list|(
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|char
name|decrypt
parameter_list|(
name|char
name|c
parameter_list|)
block|{
for|for
control|(
name|char
name|i
init|=
literal|0
init|;
name|i
operator|<
name|SHIFTS
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|SHIFTS
index|[
name|i
index|]
operator|==
name|c
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Impossible to decrypt '"
operator|+
name|c
operator|+
literal|"'. Unhandled character."
argument_list|)
throw|;
block|}
specifier|public
specifier|static
name|String
name|repeat
parameter_list|(
name|String
name|str
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|str
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
block|}
end_class

end_unit

