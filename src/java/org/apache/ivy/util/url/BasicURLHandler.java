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
name|FileInputStream
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
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPInputStream
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
name|Ivy
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
comment|// Install the IvyAuthenticator
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
name|URLConnection
name|con
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|normalizeToURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|con
operator|=
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|con
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
literal|"Apache Ivy/"
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|con
operator|instanceof
name|HttpURLConnection
condition|)
block|{
name|HttpURLConnection
name|httpCon
init|=
operator|(
name|HttpURLConnection
operator|)
name|con
decl_stmt|;
if|if
condition|(
name|getRequestMethod
argument_list|()
operator|==
name|URLHandler
operator|.
name|REQUEST_METHOD_HEAD
condition|)
block|{
name|httpCon
operator|.
name|setRequestMethod
argument_list|(
literal|"HEAD"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checkStatusCode
argument_list|(
name|url
argument_list|,
name|httpCon
argument_list|)
condition|)
block|{
return|return
operator|new
name|URLInfo
argument_list|(
literal|true
argument_list|,
name|httpCon
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
specifier|private
name|boolean
name|checkStatusCode
parameter_list|(
name|URL
name|url
parameter_list|,
name|HttpURLConnection
name|con
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|status
init|=
name|con
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
literal|true
return|;
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
name|con
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
name|con
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
return|return
literal|false
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
comment|// Install the IvyAuthenticator
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
name|URLConnection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|normalizeToURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|conn
operator|=
name|url
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
literal|"Apache Ivy/"
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
if|if
condition|(
name|conn
operator|instanceof
name|HttpURLConnection
condition|)
block|{
name|HttpURLConnection
name|httpCon
init|=
operator|(
name|HttpURLConnection
operator|)
name|conn
decl_stmt|;
if|if
condition|(
operator|!
name|checkStatusCode
argument_list|(
name|url
argument_list|,
name|httpCon
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"The HTTP response code for "
operator|+
name|url
operator|+
literal|" did not indicate a success."
operator|+
literal|" See log for more detail."
argument_list|)
throw|;
block|}
block|}
name|InputStream
name|inStream
decl_stmt|;
if|if
condition|(
literal|"gzip"
operator|.
name|equals
argument_list|(
name|conn
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
condition|)
block|{
name|inStream
operator|=
operator|new
name|GZIPInputStream
argument_list|(
name|conn
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inStream
operator|=
name|conn
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
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
comment|// Install the IvyAuthenticator
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
name|URLConnection
name|srcConn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|src
operator|=
name|normalizeToURL
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|srcConn
operator|=
name|src
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|srcConn
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
literal|"Apache Ivy/"
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
argument_list|)
expr_stmt|;
name|srcConn
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
if|if
condition|(
name|srcConn
operator|instanceof
name|HttpURLConnection
condition|)
block|{
name|HttpURLConnection
name|httpCon
init|=
operator|(
name|HttpURLConnection
operator|)
name|srcConn
decl_stmt|;
if|if
condition|(
operator|!
name|checkStatusCode
argument_list|(
name|src
argument_list|,
name|httpCon
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"The HTTP response code for "
operator|+
name|src
operator|+
literal|" did not indicate a success."
operator|+
literal|" See log for more detail."
argument_list|)
throw|;
block|}
block|}
name|int
name|contentLength
init|=
name|srcConn
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
name|InputStream
name|inStream
decl_stmt|;
if|if
condition|(
literal|"gzip"
operator|.
name|equals
argument_list|(
name|srcConn
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
condition|)
block|{
name|inStream
operator|=
operator|new
name|GZIPInputStream
argument_list|(
name|srcConn
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inStream
operator|=
name|srcConn
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
name|inStream
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
specifier|public
name|void
name|upload
parameter_list|(
name|File
name|source
parameter_list|,
name|URL
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Install the IvyAuthenticator
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
literal|"http"
operator|.
name|equals
argument_list|(
name|dest
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|&&
operator|!
literal|"https"
operator|.
name|equals
argument_list|(
name|dest
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"URL repository only support HTTP PUT at the moment"
argument_list|)
throw|;
block|}
name|HttpURLConnection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dest
operator|=
name|normalizeToURL
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|conn
operator|=
operator|(
name|HttpURLConnection
operator|)
name|dest
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestMethod
argument_list|(
literal|"PUT"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
literal|"Apache Ivy/"
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-type"
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-length"
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|source
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setInstanceFollowRedirects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|source
argument_list|)
decl_stmt|;
try|try
block|{
name|OutputStream
name|os
init|=
name|conn
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|os
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|in
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
comment|/* ignored */
block|}
block|}
name|validatePutStatusCode
argument_list|(
name|dest
argument_list|,
name|conn
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|conn
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|disconnect
argument_list|(
name|conn
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
if|if
condition|(
operator|!
literal|"HEAD"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|getRequestMethod
argument_list|()
argument_list|)
condition|)
block|{
comment|// We must read the response body before disconnecting!
comment|// Cfr. http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
comment|// [quote]Do not abandon a connection by ignoring the response body. Doing
comment|// so may results in idle TCP connections.[/quote]
name|readResponseBody
argument_list|(
operator|(
name|HttpURLConnection
operator|)
name|con
argument_list|)
expr_stmt|;
block|}
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
condition|)
block|{
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
specifier|private
name|void
name|readResponseBody
parameter_list|(
name|HttpURLConnection
name|conn
parameter_list|)
block|{
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
name|InputStream
name|inStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|inStream
operator|=
name|conn
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
while|while
condition|(
name|inStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|>
literal|0
condition|)
block|{
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
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
try|try
block|{
name|inStream
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
comment|// ignore
block|}
block|}
block|}
name|InputStream
name|errStream
init|=
name|conn
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|errStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
while|while
condition|(
name|errStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|>
literal|0
condition|)
block|{
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
try|try
block|{
name|errStream
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
comment|// ignore
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

