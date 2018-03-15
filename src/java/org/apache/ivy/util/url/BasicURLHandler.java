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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|settings
operator|.
name|TimeoutConstraint
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|BasicURLHandler
extends|extends
name|AbstractURLHandler
implements|implements
name|TimeoutConstrainedURLHandler
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
annotation|@
name|Override
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|)
block|{
return|return
name|this
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|int
name|timeout
parameter_list|)
block|{
return|return
name|this
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|createTimeoutConstraints
argument_list|(
name|timeout
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReachable
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
block|{
return|return
name|this
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeoutConstraint
argument_list|)
operator|.
name|isReachable
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getContentLength
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
block|{
return|return
name|this
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeoutConstraint
argument_list|)
operator|.
name|getContentLength
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getLastModified
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
block|{
return|return
name|this
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeoutConstraint
argument_list|)
operator|.
name|getLastModified
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
block|{
comment|// Install the IvyAuthenticator
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|||
literal|"https"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
specifier|final
name|int
name|connectionTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
specifier|final
name|int
name|readTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
decl_stmt|;
name|URLConnection
name|con
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|URL
name|normalizedURL
init|=
name|normalizeToURL
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|con
operator|=
name|normalizedURL
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|con
operator|.
name|setConnectTimeout
argument_list|(
name|connectionTimeout
argument_list|)
expr_stmt|;
name|con
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
name|con
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
name|getUserAgent
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
name|TimeoutConstrainedURLHandler
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
name|normalizedURL
argument_list|,
name|httpCon
argument_list|)
condition|)
block|{
name|String
name|bodyCharset
init|=
name|getCharSetFromContentType
argument_list|(
name|con
operator|.
name|getContentType
argument_list|()
argument_list|)
decl_stmt|;
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
argument_list|,
name|bodyCharset
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
comment|// TODO: not HTTP... maybe we *don't* want to default to ISO-8559-1 here?
name|String
name|bodyCharset
init|=
name|getCharSetFromContentType
argument_list|(
name|con
operator|.
name|getContentType
argument_list|()
argument_list|)
decl_stmt|;
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
argument_list|,
name|bodyCharset
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
literal|"Server access error at url "
operator|+
name|url
argument_list|,
name|e
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
comment|/**      * Extract the charset from the Content-Type header string, or default to ISO-8859-1 as per      * rfc2616-sec3.html#sec3.7.1 .      *      * @param contentType the Content-Type header string      * @return the charset as specified in the content type, or ISO-8859-1 if unspecified.      */
specifier|public
specifier|static
name|String
name|getCharSetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|String
name|charSet
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|el
range|:
name|contentType
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
control|)
block|{
name|String
name|element
init|=
name|el
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"charset="
argument_list|)
condition|)
block|{
name|charSet
operator|=
name|element
operator|.
name|substring
argument_list|(
literal|"charset="
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|charSet
operator|==
literal|null
operator|||
name|charSet
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// default to ISO-8859-1 as per rfc2616-sec3.html#sec3.7.1
name|charSet
operator|=
literal|"ISO-8859-1"
expr_stmt|;
block|}
return|return
name|charSet
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
comment|// IVY-1328: some servers return a 204 on a HEAD request
if|if
condition|(
literal|"HEAD"
operator|.
name|equals
argument_list|(
name|con
operator|.
name|getRequestMethod
argument_list|()
argument_list|)
operator|&&
operator|(
name|status
operator|==
literal|204
operator|)
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
annotation|@
name|Override
specifier|public
name|InputStream
name|openStream
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|this
operator|.
name|openStream
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|openStream
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Install the IvyAuthenticator
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|||
literal|"https"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
specifier|final
name|int
name|connectionTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
specifier|final
name|int
name|readTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
decl_stmt|;
name|URLConnection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|URL
name|normalizedURL
init|=
name|normalizeToURL
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|conn
operator|=
name|normalizedURL
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|conn
operator|.
name|setConnectTimeout
argument_list|(
name|connectionTimeout
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"gzip,deflate"
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
name|normalizedURL
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
name|normalizedURL
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
init|=
name|getDecodingInputStream
argument_list|(
name|conn
operator|.
name|getContentEncoding
argument_list|()
argument_list|,
name|conn
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
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
annotation|@
name|Override
specifier|public
name|void
name|download
parameter_list|(
specifier|final
name|URL
name|src
parameter_list|,
specifier|final
name|File
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|download
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|download
parameter_list|(
specifier|final
name|URL
name|src
parameter_list|,
specifier|final
name|File
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|listener
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Install the IvyAuthenticator
if|if
condition|(
literal|"http"
operator|.
name|equals
argument_list|(
name|src
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|||
literal|"https"
operator|.
name|equals
argument_list|(
name|src
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
specifier|final
name|int
name|connectionTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
specifier|final
name|int
name|readTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getReadTimeout
argument_list|()
decl_stmt|;
name|URLConnection
name|srcConn
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|URL
name|normalizedURL
init|=
name|normalizeToURL
argument_list|(
name|src
argument_list|)
decl_stmt|;
name|srcConn
operator|=
name|normalizedURL
operator|.
name|openConnection
argument_list|()
expr_stmt|;
name|srcConn
operator|.
name|setConnectTimeout
argument_list|(
name|connectionTimeout
argument_list|)
expr_stmt|;
name|srcConn
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
name|srcConn
operator|.
name|setRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
name|srcConn
operator|.
name|setRequestProperty
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"gzip,deflate"
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
name|normalizedURL
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
name|normalizedURL
operator|+
literal|" did not indicate a success."
operator|+
literal|" See log for more detail."
argument_list|)
throw|;
block|}
block|}
comment|// do the download
name|InputStream
name|inStream
init|=
name|getDecodingInputStream
argument_list|(
name|srcConn
operator|.
name|getContentEncoding
argument_list|()
argument_list|,
name|srcConn
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|inStream
argument_list|,
name|dest
argument_list|,
name|listener
argument_list|)
expr_stmt|;
comment|// check content length only if content was not encoded
if|if
condition|(
name|srcConn
operator|.
name|getContentEncoding
argument_list|()
operator|==
literal|null
condition|)
block|{
name|int
name|contentLength
init|=
name|srcConn
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentLength
operator|!=
operator|-
literal|1
operator|&&
name|dest
operator|.
name|length
argument_list|()
operator|!=
name|contentLength
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
name|normalizedURL
operator|+
literal|". Please retry."
argument_list|)
throw|;
block|}
block|}
comment|// update modification date
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
annotation|@
name|Override
specifier|public
name|void
name|upload
parameter_list|(
specifier|final
name|File
name|source
parameter_list|,
specifier|final
name|URL
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|upload
argument_list|(
name|source
argument_list|,
name|dest
argument_list|,
name|l
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|upload
parameter_list|(
specifier|final
name|File
name|src
parameter_list|,
specifier|final
name|URL
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|listener
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
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
comment|// Install the IvyAuthenticator
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
specifier|final
name|int
name|connectionTimeout
init|=
operator|(
name|timeoutConstraint
operator|==
literal|null
operator|||
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
operator|<
literal|0
operator|)
condition|?
literal|0
else|:
name|timeoutConstraint
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
name|HttpURLConnection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|URL
name|normalizedDestURL
init|=
name|normalizeToURL
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|conn
operator|=
operator|(
name|HttpURLConnection
operator|)
name|normalizedDestURL
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
name|setConnectTimeout
argument_list|(
name|connectionTimeout
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
name|getUserAgent
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
name|src
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
try|try
init|(
specifier|final
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|src
argument_list|)
init|)
block|{
specifier|final
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
name|listener
argument_list|)
expr_stmt|;
block|}
name|validatePutStatusCode
argument_list|(
name|normalizedDestURL
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
comment|/**      * Read and ignore the response body.      */
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
comment|// Skip content
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
comment|// Skip content
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

