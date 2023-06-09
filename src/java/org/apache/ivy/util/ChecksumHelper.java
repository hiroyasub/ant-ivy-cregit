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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ChecksumHelper
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|2048
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|algorithms
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|algorithms
operator|.
name|put
argument_list|(
literal|"md5"
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
name|algorithms
operator|.
name|put
argument_list|(
literal|"sha1"
argument_list|,
literal|"SHA-1"
argument_list|)
expr_stmt|;
comment|// higher versions of JRE support these algorithms
comment|// https://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#MessageDigest
comment|// conditionally add them
if|if
condition|(
name|isAlgorithmSupportedInJRE
argument_list|(
literal|"SHA-256"
argument_list|)
condition|)
block|{
name|algorithms
operator|.
name|put
argument_list|(
literal|"SHA-256"
argument_list|,
literal|"SHA-256"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAlgorithmSupportedInJRE
argument_list|(
literal|"SHA-512"
argument_list|)
condition|)
block|{
name|algorithms
operator|.
name|put
argument_list|(
literal|"SHA-512"
argument_list|,
literal|"SHA-512"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAlgorithmSupportedInJRE
argument_list|(
literal|"SHA-384"
argument_list|)
condition|)
block|{
name|algorithms
operator|.
name|put
argument_list|(
literal|"SHA-384"
argument_list|,
literal|"SHA-384"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|boolean
name|isAlgorithmSupportedInJRE
parameter_list|(
specifier|final
name|String
name|algorithm
parameter_list|)
block|{
if|if
condition|(
name|algorithm
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Checks the checksum of the given file against the given checksumFile, and throws an      * IOException if the checksum is not compliant      *      * @param dest      *            the file to test      * @param checksumFile      *            the file containing the expected checksum      * @param algorithm      *            the checksum algorithm to use      * @throws IOException      *             if an IO problem occur while reading files or if the checksum is not compliant      */
specifier|public
specifier|static
name|void
name|check
parameter_list|(
name|File
name|dest
parameter_list|,
name|File
name|checksumFile
parameter_list|,
name|String
name|algorithm
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|csFileContent
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|checksumFile
argument_list|)
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|String
name|expected
decl_stmt|;
if|if
condition|(
name|csFileContent
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
operator|>
operator|-
literal|1
operator|&&
operator|(
name|csFileContent
operator|.
name|startsWith
argument_list|(
literal|"md"
argument_list|)
operator|||
name|csFileContent
operator|.
name|startsWith
argument_list|(
literal|"sha"
argument_list|)
operator|)
condition|)
block|{
name|int
name|lastSpaceIndex
init|=
name|csFileContent
operator|.
name|lastIndexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
name|expected
operator|=
name|csFileContent
operator|.
name|substring
argument_list|(
name|lastSpaceIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|spaceIndex
init|=
name|csFileContent
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|spaceIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|expected
operator|=
name|csFileContent
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|spaceIndex
argument_list|)
expr_stmt|;
comment|// IVY-1155: support some strange formats like this one:
comment|// https://repo1.maven.org/maven2/org/apache/pdfbox/fontbox/0.8.0-incubator/fontbox-0.8.0-incubator.jar.md5
if|if
condition|(
name|expected
operator|.
name|endsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|ch
range|:
name|csFileContent
operator|.
name|substring
argument_list|(
name|spaceIndex
operator|+
literal|1
argument_list|)
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isWhitespace
argument_list|(
name|ch
argument_list|)
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
name|expected
operator|=
name|result
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|expected
operator|=
name|csFileContent
expr_stmt|;
block|}
block|}
name|String
name|computed
init|=
name|computeAsString
argument_list|(
name|dest
argument_list|,
name|algorithm
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expected
operator|.
name|equals
argument_list|(
name|computed
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"invalid "
operator|+
name|algorithm
operator|+
literal|": expected="
operator|+
name|expected
operator|+
literal|" computed="
operator|+
name|computed
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|computeAsString
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|algorithm
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|byteArrayToHexString
argument_list|(
name|compute
argument_list|(
name|f
argument_list|,
name|algorithm
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|compute
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|algorithm
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
init|)
block|{
name|MessageDigest
name|md
init|=
name|getMessageDigest
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|md
operator|.
name|reset
argument_list|()
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|len
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|md
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
return|return
name|md
operator|.
name|digest
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isKnownAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
return|return
name|algorithms
operator|.
name|containsKey
argument_list|(
name|algorithm
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|MessageDigest
name|getMessageDigest
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|String
name|mdAlgorithm
init|=
name|algorithms
operator|.
name|get
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
if|if
condition|(
name|mdAlgorithm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown algorithm "
operator|+
name|algorithm
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|mdAlgorithm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown algorithm "
operator|+
name|algorithm
argument_list|)
throw|;
block|}
block|}
comment|// byte to hex string converter
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|CHARS
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
comment|/**      * Convert a byte[] array to readable string format. This makes the "hex" readable!      *      * @return result String buffer in String format      * @param in      *            byte[] buffer to convert to string format      */
specifier|public
specifier|static
name|String
name|byteArrayToHexString
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
name|byte
name|ch
init|=
literal|0x00
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|in
operator|.
name|length
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|(
name|in
operator|.
name|length
operator|*
literal|2
argument_list|)
decl_stmt|;
comment|// CheckStyle:MagicNumber OFF
for|for
control|(
name|byte
name|bt
range|:
name|in
control|)
block|{
name|ch
operator|=
operator|(
name|byte
operator|)
operator|(
name|bt
operator|&
literal|0xF0
operator|)
expr_stmt|;
comment|// Strip off high nibble
name|ch
operator|=
operator|(
name|byte
operator|)
operator|(
name|ch
operator|>>>
literal|4
operator|)
expr_stmt|;
comment|// shift the bits down
name|ch
operator|=
operator|(
name|byte
operator|)
operator|(
name|ch
operator|&
literal|0x0F
operator|)
expr_stmt|;
comment|// must do this is high order bit is on!
name|out
operator|.
name|append
argument_list|(
name|CHARS
index|[
name|ch
index|]
argument_list|)
expr_stmt|;
comment|// convert the nibble to a String Character
name|ch
operator|=
operator|(
name|byte
operator|)
operator|(
name|bt
operator|&
literal|0x0F
operator|)
expr_stmt|;
comment|// Strip off low nibble
name|out
operator|.
name|append
argument_list|(
name|CHARS
index|[
name|ch
index|]
argument_list|)
expr_stmt|;
comment|// convert the nibble to a String Character
block|}
comment|// CheckStyle:MagicNumber ON
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|ChecksumHelper
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

