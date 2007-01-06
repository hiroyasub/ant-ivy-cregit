begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|UsernamePasswordCredentials
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
name|fr
operator|.
name|jayasoft
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
operator|.
name|Credentials
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * @author Xavier Hanin  *  */
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
name|_proxyPort
decl_stmt|;
specifier|private
name|String
name|_proxyRealm
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_proxyHost
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_proxyUserName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_proxyPasswd
init|=
literal|null
decl_stmt|;
specifier|private
name|HttpClientHelper
name|_httpClientHelper
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
name|_proxyRealm
operator|=
literal|null
expr_stmt|;
comment|//no equivalent for realm in jdk proxy support ?
name|_proxyHost
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
expr_stmt|;
comment|//TODO constant is better ...
if|if
condition|(
name|useProxy
argument_list|()
condition|)
block|{
name|_proxyPort
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
name|_proxyUserName
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyUser"
argument_list|)
expr_stmt|;
name|_proxyPasswd
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPassword"
argument_list|)
expr_stmt|;
comment|//It seems there is no equivalent in HttpClient for
comment|// 'http.nonProxyHosts' property
name|Message
operator|.
name|verbose
argument_list|(
literal|"proxy configured: host="
operator|+
name|_proxyHost
operator|+
literal|" port="
operator|+
name|_proxyPort
operator|+
literal|" user="
operator|+
name|_proxyUserName
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
argument_list|)
decl_stmt|;
return|return
operator|new
name|GETInputStream
argument_list|(
name|get
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
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
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
name|HeadMethod
name|head
init|=
literal|null
decl_stmt|;
try|try
block|{
name|head
operator|=
name|doHead
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|head
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|head
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
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
name|getResponseContentLength
argument_list|(
name|head
argument_list|)
argument_list|,
name|getLastModified
argument_list|(
name|head
argument_list|)
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
name|error
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
name|head
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
name|warn
argument_list|(
literal|"SERVER ERROR: "
operator|+
name|head
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
name|Message
operator|.
name|debug
argument_list|(
literal|"HTTP response status: "
operator|+
name|status
operator|+
literal|"="
operator|+
name|head
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
literal|"You probably access the destination server through a proxy server that is not well configured."
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
finally|finally
block|{
if|if
condition|(
name|head
operator|!=
literal|null
condition|)
block|{
name|head
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
name|long
name|getLastModified
parameter_list|(
name|HeadMethod
name|head
parameter_list|)
block|{
name|Header
name|header
init|=
name|head
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
name|HeadMethod
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
name|_httpClientHelper
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
name|_httpClientHelper
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
literal|"unable to get access to getResponseContentLength of commons-httpclient HeadMethod. Please use commons-httpclient 3.0 or use ivy with sufficient security permissions."
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
name|_httpClientHelper
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
name|_httpClientHelper
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
name|_httpClientHelper
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
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|HttpClient
name|client
init|=
name|getClient
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
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
throws|,
name|HttpException
block|{
name|HttpClient
name|client
init|=
name|getClient
argument_list|(
name|url
argument_list|)
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
name|url
operator|.
name|toExternalForm
argument_list|()
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
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|HttpClient
name|client
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|List
name|authPrefs
init|=
operator|new
name|ArrayList
argument_list|(
literal|2
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
comment|// Exclude the NTLM authentication scheme because it is not supported by this class
name|client
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
name|client
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|setProxy
argument_list|(
name|_proxyHost
argument_list|,
name|_proxyPort
argument_list|)
expr_stmt|;
if|if
condition|(
name|useProxyAuthentication
argument_list|()
condition|)
block|{
name|client
operator|.
name|getState
argument_list|()
operator|.
name|setProxyCredentials
argument_list|(
name|_proxyRealm
argument_list|,
name|_proxyHost
argument_list|,
operator|new
name|UsernamePasswordCredentials
argument_list|(
name|_proxyUserName
argument_list|,
name|_proxyPasswd
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Credentials
name|c
init|=
name|getCredentials
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"found credentials for "
operator|+
name|url
operator|+
literal|": "
operator|+
name|c
argument_list|)
expr_stmt|;
name|client
operator|.
name|getState
argument_list|()
operator|.
name|setCredentials
argument_list|(
name|c
operator|.
name|getRealm
argument_list|()
argument_list|,
name|c
operator|.
name|getHost
argument_list|()
argument_list|,
operator|new
name|UsernamePasswordCredentials
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
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|private
name|boolean
name|useProxy
parameter_list|()
block|{
return|return
name|_proxyHost
operator|!=
literal|null
operator|&&
name|_proxyHost
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
name|getCredentials
argument_list|(
name|url
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|Credentials
name|getCredentials
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
name|getCredentials
argument_list|(
literal|null
argument_list|,
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
name|_proxyUserName
operator|!=
literal|null
operator|&&
name|_proxyUserName
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
name|GETInputStream
extends|extends
name|InputStream
block|{
specifier|private
name|InputStream
name|_is
decl_stmt|;
specifier|private
name|GetMethod
name|_get
decl_stmt|;
specifier|private
name|GETInputStream
parameter_list|(
name|GetMethod
name|get
parameter_list|)
throws|throws
name|IOException
block|{
name|_get
operator|=
name|get
expr_stmt|;
name|_is
operator|=
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|_is
operator|.
name|available
argument_list|()
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|_is
operator|.
name|close
argument_list|()
expr_stmt|;
name|_get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|_is
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|_is
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|void
name|mark
parameter_list|(
name|int
name|readlimit
parameter_list|)
block|{
name|_is
operator|.
name|mark
argument_list|(
name|readlimit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
name|_is
operator|.
name|markSupported
argument_list|()
return|;
block|}
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|_is
operator|.
name|read
argument_list|()
return|;
block|}
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_is
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_is
operator|.
name|read
argument_list|(
name|b
argument_list|)
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
throws|throws
name|IOException
block|{
name|_is
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|n
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_is
operator|.
name|skip
argument_list|(
name|n
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|_is
operator|.
name|toString
argument_list|()
return|;
block|}
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
name|HttpClientHelper3x
parameter_list|()
block|{
block|}
specifier|public
name|long
name|getResponseContentLength
parameter_list|(
name|HeadMethod
name|head
parameter_list|)
block|{
return|return
name|head
operator|.
name|getResponseContentLength
argument_list|()
return|;
block|}
comment|/** 		 * {@inheritDoc} 		 */
specifier|public
name|int
name|getHttpClientMajorVersion
parameter_list|()
block|{
return|return
literal|3
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
name|HttpClientHelper2x
parameter_list|()
block|{
block|}
specifier|public
name|long
name|getResponseContentLength
parameter_list|(
name|HeadMethod
name|head
parameter_list|)
block|{
name|Header
name|header
init|=
name|head
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
comment|/** 		 * {@inheritDoc} 		 */
specifier|public
name|int
name|getHttpClientMajorVersion
parameter_list|()
block|{
return|return
literal|2
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
name|HeadMethod
name|head
parameter_list|)
function_decl|;
name|int
name|getHttpClientMajorVersion
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

