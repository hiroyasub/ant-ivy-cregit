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

begin_comment
comment|/**  * Simple encoder of byte arrays into String array using only the hexadecimal alphabet  */
end_comment

begin_class
specifier|public
class|class
name|HexEncoder
block|{
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|ALPHABET
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|}
decl_stmt|;
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|packet
parameter_list|)
block|{
name|StringBuffer
name|chars
init|=
operator|new
name|StringBuffer
argument_list|(
literal|16
argument_list|)
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
name|packet
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|highBits
init|=
operator|(
name|packet
index|[
name|i
index|]
operator|&
literal|0xF0
operator|)
operator|>>
literal|4
decl_stmt|;
name|int
name|lowBits
init|=
name|packet
index|[
name|i
index|]
operator|&
literal|0x0F
decl_stmt|;
name|chars
operator|.
name|append
argument_list|(
name|ALPHABET
index|[
name|highBits
index|]
argument_list|)
expr_stmt|;
name|chars
operator|.
name|append
argument_list|(
name|ALPHABET
index|[
name|lowBits
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|chars
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

