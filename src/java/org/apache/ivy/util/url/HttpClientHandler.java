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
name|URL
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|List
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpMethodBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|MultiThreadedHttpConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|NTCredentials
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|AuthPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|AuthScheme
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|AuthScope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|CredentialsNotAvailableException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|auth
operator|.
name|CredentialsProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|HeadMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|PutMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|params
operator|.
name|HttpMethodParams
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
name|HostUtil
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|HttpClientHandler
extends|extends
name|AbstractURLHandler
block|{
specifier|private
specifier|static
specifier|final
name|SimpleDateFormat
name|LAST_MODIFIED_FORMAT
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, d MMM yyyy HH:mm:ss z"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
comment|// proxy configuration: obtain from system properties
specifier|private
name|int
name|proxyPort
decl_stmt|;
specifier|private
name|String
name|proxyHost
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|proxyUserName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|proxyPasswd
init|=
literal|null
decl_stmt|;
specifier|private
name|HttpClientHelper
name|httpClientHelper
decl_stmt|;
specifier|private
specifier|static
name|HttpClient
name|httpClient
decl_stmt|;
specifier|public
name|HttpClientHandler
parameter_list|()
block|{
name|configureProxy
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|configureProxy
parameter_list|()
block|{
name|proxyHost
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
expr_stmt|;
comment|// TODO constant is better ...
if|if
condition|(
name|useProxy
argument_list|()
condition|)
block|{
name|proxyPort
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPort"
argument_list|,
literal|"80"
argument_list|)
argument_list|)
expr_stmt|;
name|proxyUserName
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyUser"
argument_list|)
expr_stmt|;
name|proxyPasswd
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPassword"
argument_list|)
expr_stmt|;
comment|// It seems there is no equivalent in HttpClient for
comment|// 'http.nonProxyHosts' property
name|Message
operator|.
name|verbose
argument_list|(
literal|"proxy configured: host="
operator|+
name|proxyHost
operator|+
literal|" port="
operator|+
name|proxyPort
operator|+
literal|" user="
operator|+
name|proxyUserName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no proxy configured"
argument_list|)
expr_stmt|;
block|}
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
name|GetMethod
name|get
init|=
name|doGet
argument_list|(
name|url
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checkStatusCode
argument_list|(
name|url
argument_list|,
name|get
argument_list|)
condition|)
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
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
name|Header
name|encoding
init|=
name|get
operator|.
name|getResponseHeader
argument_list|(
literal|"Content-Encoding"
argument_list|)
decl_stmt|;
return|return
name|getDecodingInputStream
argument_list|(
name|encoding
operator|==
literal|null
condition|?
literal|null
else|:
name|encoding
operator|.
name|getValue
argument_list|()
argument_list|,
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
return|;
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
name|GetMethod
name|get
init|=
name|doGet
argument_list|(
name|src
argument_list|,
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
comment|// We can only figure the content we got is want we want if the status is success.
if|if
condition|(
operator|!
name|checkStatusCode
argument_list|(
name|src
argument_list|,
name|get
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
name|Header
name|encoding
init|=
name|get
operator|.
name|getResponseHeader
argument_list|(
literal|"Content-Encoding"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|getDecodingInputStream
argument_list|(
name|encoding
operator|==
literal|null
condition|?
literal|null
else|:
name|encoding
operator|.
name|getValue
argument_list|()
argument_list|,
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setLastModified
argument_list|(
name|getLastModified
argument_list|(
name|get
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|upload
parameter_list|(
name|File
name|src
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
name|HttpClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
name|PutMethod
name|put
init|=
operator|new
name|PutMethod
argument_list|(
name|normalizeToString
argument_list|(
name|dest
argument_list|)
argument_list|)
decl_stmt|;
name|put
operator|.
name|setDoAuthentication
argument_list|(
name|useAuthentication
argument_list|(
name|dest
argument_list|)
operator|||
name|useProxyAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|put
operator|.
name|getParams
argument_list|()
operator|.
name|setBooleanParameter
argument_list|(
literal|"http.protocol.expect-continue"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|put
operator|.
name|setRequestEntity
argument_list|(
operator|new
name|FileRequestEntity
argument_list|(
name|src
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|statusCode
init|=
name|client
operator|.
name|executeMethod
argument_list|(
name|put
argument_list|)
decl_stmt|;
name|validatePutStatusCode
argument_list|(
name|dest
argument_list|,
name|statusCode
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|put
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
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
name|HttpMethodBase
name|method
init|=
literal|null
decl_stmt|;
try|try
block|{
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
name|method
operator|=
name|doHead
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|method
operator|=
name|doGet
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checkStatusCode
argument_list|(
name|url
argument_list|,
name|method
argument_list|)
condition|)
block|{
return|return
operator|new
name|URLInfo
argument_list|(
literal|true
argument_list|,
name|getResponseContentLength
argument_list|(
name|method
argument_list|)
argument_list|,
name|getLastModified
argument_list|(
name|method
argument_list|)
argument_list|,
name|method
operator|.
name|getRequestCharSet
argument_list|()
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|HttpException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"HttpClientHandler: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|":"
operator|+
name|e
operator|.
name|getReasonCode
argument_list|()
operator|+
literal|"="
operator|+
name|e
operator|.
name|getReason
argument_list|()
operator|+
literal|" url="
operator|+
name|url
argument_list|)
expr_stmt|;
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
literal|"HttpClientHandler: "
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
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// thrown by HttpClient to indicate the URL is not valid, this happens for instance
comment|// when trying to download a dynamic version (cfr IVY-390)
block|}
finally|finally
block|{
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
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
name|HttpMethodBase
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|status
init|=
name|method
operator|.
name|getStatusCode
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
name|method
operator|.
name|getName
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
name|method
operator|.
name|getStatusText
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
name|method
operator|.
name|getStatusText
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
specifier|private
name|long
name|getLastModified
parameter_list|(
name|HttpMethodBase
name|method
parameter_list|)
block|{
name|Header
name|header
init|=
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"last-modified"
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|String
name|lastModified
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|LAST_MODIFIED_FORMAT
operator|.
name|parse
argument_list|(
name|lastModified
argument_list|)
operator|.
name|getTime
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
comment|// ignored
block|}
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
block|}
specifier|private
name|long
name|getResponseContentLength
parameter_list|(
name|HttpMethodBase
name|head
parameter_list|)
block|{
return|return
name|getHttpClientHelper
argument_list|()
operator|.
name|getResponseContentLength
argument_list|(
name|head
argument_list|)
return|;
block|}
specifier|private
name|HttpClientHelper
name|getHttpClientHelper
parameter_list|()
block|{
if|if
condition|(
name|httpClientHelper
operator|==
literal|null
condition|)
block|{
comment|// use commons httpclient 3.0 if available
try|try
block|{
name|HttpMethodBase
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getResponseContentLength"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|httpClientHelper
operator|=
operator|new
name|HttpClientHelper3x
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"using commons httpclient 3.x helper"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"unable to get access to getResponseContentLength of "
operator|+
literal|"commons-httpclient HeadMethod. Please use commons-httpclient 3.0 or "
operator|+
literal|"use ivy with sufficient security permissions."
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|httpClientHelper
operator|=
operator|new
name|HttpClientHelper2x
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"using commons httpclient 2.x helper"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|httpClientHelper
operator|=
operator|new
name|HttpClientHelper2x
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"using commons httpclient 2.x helper"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|httpClientHelper
return|;
block|}
specifier|public
name|int
name|getHttpClientMajorVersion
parameter_list|()
block|{
name|HttpClientHelper
name|helper
init|=
name|getHttpClientHelper
argument_list|()
decl_stmt|;
return|return
name|helper
operator|.
name|getHttpClientMajorVersion
argument_list|()
return|;
block|}
specifier|private
name|GetMethod
name|doGet
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|normalizeToString
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|get
operator|.
name|setDoAuthentication
argument_list|(
name|useAuthentication
argument_list|(
name|url
argument_list|)
operator|||
name|useProxyAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|get
operator|.
name|setRequestHeader
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"gzip,deflate"
argument_list|)
expr_stmt|;
name|client
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
expr_stmt|;
return|return
name|get
return|;
block|}
specifier|private
name|HeadMethod
name|doHead
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpClient
name|client
init|=
name|getClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|HeadMethod
name|head
init|=
operator|new
name|HeadMethod
argument_list|(
name|normalizeToString
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|head
operator|.
name|setDoAuthentication
argument_list|(
name|useAuthentication
argument_list|(
name|url
argument_list|)
operator|||
name|useProxyAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|executeMethod
argument_list|(
name|head
argument_list|)
expr_stmt|;
return|return
name|head
return|;
block|}
specifier|private
name|HttpClient
name|getClient
parameter_list|()
block|{
if|if
condition|(
name|httpClient
operator|==
literal|null
condition|)
block|{
specifier|final
name|MultiThreadedHttpConnectionManager
name|connManager
init|=
operator|new
name|MultiThreadedHttpConnectionManager
argument_list|()
decl_stmt|;
name|httpClient
operator|=
operator|new
name|HttpClient
argument_list|(
name|connManager
argument_list|)
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|connManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|authPrefs
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|authPrefs
operator|.
name|add
argument_list|(
name|AuthPolicy
operator|.
name|DIGEST
argument_list|)
expr_stmt|;
name|authPrefs
operator|.
name|add
argument_list|(
name|AuthPolicy
operator|.
name|BASIC
argument_list|)
expr_stmt|;
name|authPrefs
operator|.
name|add
argument_list|(
name|AuthPolicy
operator|.
name|NTLM
argument_list|)
expr_stmt|;
comment|// put it at the end to give less priority (IVY-213)
name|httpClient
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|AuthPolicy
operator|.
name|AUTH_SCHEME_PRIORITY
argument_list|,
name|authPrefs
argument_list|)
expr_stmt|;
if|if
condition|(
name|useProxy
argument_list|()
condition|)
block|{
name|httpClient
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|setProxy
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
if|if
condition|(
name|useProxyAuthentication
argument_list|()
condition|)
block|{
name|httpClient
operator|.
name|getState
argument_list|()
operator|.
name|setProxyCredentials
argument_list|(
operator|new
name|AuthScope
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|,
name|AuthScope
operator|.
name|ANY_REALM
argument_list|)
argument_list|,
name|createCredentials
argument_list|(
name|proxyUserName
argument_list|,
name|proxyPasswd
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// user-agent
name|httpClient
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|HttpMethodParams
operator|.
name|USER_AGENT
argument_list|,
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
comment|// authentication
name|httpClient
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|CredentialsProvider
operator|.
name|PROVIDER
argument_list|,
operator|new
name|IvyCredentialsProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|httpClient
return|;
block|}
specifier|private
name|boolean
name|useProxy
parameter_list|()
block|{
return|return
name|proxyHost
operator|!=
literal|null
operator|&&
name|proxyHost
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
return|;
block|}
specifier|private
name|boolean
name|useAuthentication
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|hasCredentials
argument_list|(
name|url
operator|.
name|getHost
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|useProxyAuthentication
parameter_list|()
block|{
return|return
operator|(
name|proxyUserName
operator|!=
literal|null
operator|&&
name|proxyUserName
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|HttpClientHelper3x
implements|implements
name|HttpClientHelper
block|{
specifier|private
specifier|static
specifier|final
name|int
name|VERSION
init|=
literal|3
decl_stmt|;
specifier|private
name|HttpClientHelper3x
parameter_list|()
block|{
block|}
specifier|public
name|long
name|getResponseContentLength
parameter_list|(
name|HttpMethodBase
name|method
parameter_list|)
block|{
return|return
name|method
operator|.
name|getResponseContentLength
argument_list|()
return|;
block|}
comment|/**          * {@inheritDoc}          */
specifier|public
name|int
name|getHttpClientMajorVersion
parameter_list|()
block|{
return|return
name|VERSION
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|HttpClientHelper2x
implements|implements
name|HttpClientHelper
block|{
specifier|private
specifier|static
specifier|final
name|int
name|VERSION
init|=
literal|2
decl_stmt|;
specifier|private
name|HttpClientHelper2x
parameter_list|()
block|{
block|}
specifier|public
name|long
name|getResponseContentLength
parameter_list|(
name|HttpMethodBase
name|method
parameter_list|)
block|{
name|Header
name|header
init|=
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"Content-Length"
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Invalid content-length value: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|0
return|;
block|}
comment|/**          * {@inheritDoc}          */
specifier|public
name|int
name|getHttpClientMajorVersion
parameter_list|()
block|{
return|return
name|VERSION
return|;
block|}
block|}
specifier|public
interface|interface
name|HttpClientHelper
block|{
name|long
name|getResponseContentLength
parameter_list|(
name|HttpMethodBase
name|method
parameter_list|)
function_decl|;
name|int
name|getHttpClientMajorVersion
parameter_list|()
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|IvyCredentialsProvider
implements|implements
name|CredentialsProvider
block|{
specifier|public
name|Credentials
name|getCredentials
parameter_list|(
name|AuthScheme
name|scheme
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|boolean
name|proxy
parameter_list|)
throws|throws
name|CredentialsNotAvailableException
block|{
name|String
name|realm
init|=
name|scheme
operator|.
name|getRealm
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|Credentials
name|c
init|=
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|getCredentials
argument_list|(
name|realm
argument_list|,
name|host
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|createCredentials
argument_list|(
name|c
operator|.
name|getUserName
argument_list|()
argument_list|,
name|c
operator|.
name|getPasswd
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|Credentials
name|createCredentials
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|String
name|user
decl_stmt|;
name|String
name|domain
decl_stmt|;
name|int
name|backslashIndex
init|=
name|username
operator|.
name|indexOf
argument_list|(
literal|'\\'
argument_list|)
decl_stmt|;
if|if
condition|(
name|backslashIndex
operator|>=
literal|0
condition|)
block|{
name|user
operator|=
name|username
operator|.
name|substring
argument_list|(
name|backslashIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|domain
operator|=
name|username
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|backslashIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|username
expr_stmt|;
name|domain
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.auth.ntlm.domain"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|NTCredentials
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|HostUtil
operator|.
name|getLocalHostName
argument_list|()
argument_list|,
name|domain
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|FileRequestEntity
implements|implements
name|RequestEntity
block|{
specifier|private
name|File
name|file
decl_stmt|;
specifier|public
name|FileRequestEntity
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|file
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|writeRequest
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|instream
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|instream
argument_list|,
name|out
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|instream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

