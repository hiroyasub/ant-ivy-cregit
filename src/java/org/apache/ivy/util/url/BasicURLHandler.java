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
operator|.
name|url
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|CopyProgressListener
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
name|FileUtil
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
name|Message
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|BasicURLHandler
extends|extends
name|AbstractURLHandler
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|64
operator|*
literal|1024
decl_stmt|;
specifier|private
specifier|static
specifier|final
class|class
name|HttpStatus
block|{
specifier|static
specifier|final
name|int
name|SC_OK
init|=
literal|200
decl_stmt|;
specifier|static
specifier|final
name|int
name|SC_PROXY_AUTHENTICATION_REQUIRED
init|=
literal|407
decl_stmt|;
specifier|private
name|HttpStatus
parameter_list|()
block|{
block|}
block|}
specifier|public
name|BasicURLHandler
parameter_list|()
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"installing "
operator|+
name|IvyAuthenticator
operator|.
name|INSTANCE
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// do not remove,
comment|// ensure
comment|// IvyAuthenticator
comment|// class loading!
block|}
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getURLInfo
argument_list|(
name|url
argument_list|,
literal|0
argument_list|)
return|;
block|}
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|URLConnection
name|con
init|=
literal|null
decl_stmt|;
try|try
block|{
name|con
operator|=
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
if|if
condition|(
name|con
operator|instanceof
name|HttpURLConnection
condition|)
block|{
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|setRequestMethod
argument_list|(
literal|"HEAD"
argument_list|)
expr_stmt|;
name|int
name|status
init|=
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
return|return
operator|new
name|URLInfo
argument_list|(
literal|true
argument_list|,
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|getContentLength
argument_list|()
argument_list|,
name|con
operator|.
name|getLastModified
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|status
operator|==
name|HttpStatus
operator|.
name|SC_PROXY_AUTHENTICATION_REQUIRED
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Your proxy requires authentication."
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|String
operator|.
name|valueOf
argument_list|(
name|status
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"4"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"CLIENT ERROR: "
operator|+
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|getResponseMessage
argument_list|()
operator|+
literal|" url="
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|String
operator|.
name|valueOf
argument_list|(
name|status
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"5"
argument_list|)
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"SERVER ERROR: "
operator|+
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|getResponseMessage
argument_list|()
operator|+
literal|" url="
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"HTTP response status: "
operator|+
name|status
operator|+
literal|" url="
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|contentLength
init|=
name|con
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentLength
operator|<=
literal|0
condition|)
block|{
return|return
name|UNAVAILABLE
return|;
block|}
else|else
block|{
return|return
operator|new
name|URLInfo
argument_list|(
literal|true
argument_list|,
name|contentLength
argument_list|,
name|con
operator|.
name|getLastModified
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Host "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" not found. url="
operator|+
name|url
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"You probably access the destination server through "
operator|+
literal|"a proxy server that is not well configured."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Server access Error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" url="
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|disconnect
argument_list|(
name|con
argument_list|)
expr_stmt|;
block|}
return|return
name|UNAVAILABLE
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|conn
init|=
literal|null
decl_stmt|;
name|InputStream
name|inStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|inStream
operator|=
name|conn
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|outStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|len
decl_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|inStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|outStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|outStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|inStream
operator|!=
literal|null
condition|)
block|{
name|inStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|disconnect
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|download
parameter_list|(
name|URL
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|srcConn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|srcConn
operator|=
name|src
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|int
name|contentLength
init|=
name|srcConn
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|srcConn
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
if|if
condition|(
name|dest
operator|.
name|length
argument_list|()
operator|!=
name|contentLength
operator|&&
name|contentLength
operator|!=
operator|-
literal|1
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Downloaded file size doesn't match expected Content Length for "
operator|+
name|src
operator|+
literal|". Please retry."
argument_list|)
throw|;
block|}
name|long
name|lastModified
init|=
name|srcConn
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastModified
operator|>
literal|0
condition|)
block|{
name|dest
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|disconnect
argument_list|(
name|srcConn
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|disconnect
parameter_list|(
name|URLConnection
name|con
parameter_list|)
block|{
if|if
condition|(
name|con
operator|instanceof
name|HttpURLConnection
condition|)
block|{
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|con
operator|!=
literal|null
operator|&&
literal|"sun.net.www.protocol.file.FileURLConnection"
operator|.
name|equals
argument_list|(
name|con
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// ugly fix for a sun jre bug:
comment|// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4257700
comment|//
comment|// getting header info on the fileurlconnection opens the connection,
comment|// which opens a file input stream without closing it.
try|try
block|{
name|con
operator|.
name|getInputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignored
block|}
block|}
block|}
block|}
end_class

end_unit

