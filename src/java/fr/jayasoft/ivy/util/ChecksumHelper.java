begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
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
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|ChecksumHelper
block|{
specifier|private
specifier|static
name|Map
name|_algorithms
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|_algorithms
operator|.
name|put
argument_list|(
literal|"md5"
argument_list|,
literal|"MD5"
argument_list|)
expr_stmt|;
name|_algorithms
operator|.
name|put
argument_list|(
literal|"sha1"
argument_list|,
literal|"SHA-1"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
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
name|expected
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
argument_list|()
decl_stmt|;
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
argument_list|()
decl_stmt|;
return|return
name|expected
operator|.
name|equals
argument_list|(
name|computed
argument_list|)
return|;
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
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
try|try
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
literal|2048
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
finally|finally
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
operator|(
name|String
operator|)
name|_algorithms
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
specifier|final
specifier|static
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
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|,
literal|'E'
block|,
literal|'F'
block|}
decl_stmt|;
comment|/** 	 * Convert a byte[] array to readable string format. This makes the "hex" readable! 	 * @return result String buffer in String format  	 * @param in byte[] buffer to convert to string format 	 */
specifier|public
specifier|static
name|String
name|byteArrayToHexString
parameter_list|(
name|byte
name|in
index|[]
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
name|StringBuffer
name|out
init|=
operator|new
name|StringBuffer
argument_list|(
name|in
operator|.
name|length
operator|*
literal|2
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
name|in
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ch
operator|=
operator|(
name|byte
operator|)
operator|(
name|in
index|[
name|i
index|]
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
operator|(
name|int
operator|)
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
name|in
index|[
name|i
index|]
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
operator|(
name|int
operator|)
name|ch
index|]
argument_list|)
expr_stmt|;
comment|// convert the nibble to a String Character
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

