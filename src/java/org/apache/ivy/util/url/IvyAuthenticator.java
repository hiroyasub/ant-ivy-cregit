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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Authenticator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|PasswordAuthentication
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
name|Credentials
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
specifier|final
class|class
name|IvyAuthenticator
extends|extends
name|Authenticator
block|{
specifier|private
name|Authenticator
name|original
decl_stmt|;
comment|/**      * Private c'tor to prevent instantiation.      */
specifier|private
name|IvyAuthenticator
parameter_list|(
name|Authenticator
name|original
parameter_list|)
block|{
name|this
operator|.
name|original
operator|=
name|original
expr_stmt|;
block|}
comment|/**      * Installs an<tt>IvyAuthenticator</tt> as default<tt>Authenticator</tt>.      * Call this method before opening HTTP(S) connections to enable Ivy      * authentication.      */
specifier|public
specifier|static
name|void
name|install
parameter_list|()
block|{
comment|// We will try to use the original authenticator as backup authenticator.
comment|// Since there is no getter available, so try to use some reflection to
comment|// obtain it. If that doesn't work, assume there is no original authenticator
name|Authenticator
name|original
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Field
name|f
init|=
name|Authenticator
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"theAuthenticator"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|original
operator|=
operator|(
name|Authenticator
operator|)
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Error occurred while getting the original authenticator: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|original
operator|instanceof
name|IvyAuthenticator
operator|)
condition|)
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
operator|new
name|IvyAuthenticator
argument_list|(
name|original
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// API ******************************************************************
comment|// Overriding Authenticator *********************************************
specifier|protected
name|PasswordAuthentication
name|getPasswordAuthentication
parameter_list|()
block|{
name|PasswordAuthentication
name|result
init|=
literal|null
decl_stmt|;
name|String
name|proxyHost
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
decl_stmt|;
if|if
condition|(
name|getRequestingHost
argument_list|()
operator|.
name|equals
argument_list|(
name|proxyHost
argument_list|)
condition|)
block|{
name|String
name|proxyUser
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyUser"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|proxyUser
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|proxyUser
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
name|String
name|proxyPass
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"http.proxyPassword"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"authenicating to proxy server with username ["
operator|+
name|proxyUser
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|PasswordAuthentication
argument_list|(
name|proxyUser
argument_list|,
name|proxyPass
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Credentials
name|c
init|=
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|getCredentials
argument_list|(
name|getRequestingPrompt
argument_list|()
argument_list|,
name|getRequestingHost
argument_list|()
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"authentication: k='"
operator|+
name|Credentials
operator|.
name|buildKey
argument_list|(
name|getRequestingPrompt
argument_list|()
argument_list|,
name|getRequestingHost
argument_list|()
argument_list|)
operator|+
literal|"' c='"
operator|+
name|c
operator|+
literal|"'"
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|PasswordAuthentication
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
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
name|result
operator|==
literal|null
operator|)
operator|&&
operator|(
name|original
operator|!=
literal|null
operator|)
condition|)
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|original
argument_list|)
expr_stmt|;
try|try
block|{
name|result
operator|=
name|Authenticator
operator|.
name|requestPasswordAuthentication
argument_list|(
name|getRequestingHost
argument_list|()
argument_list|,
name|getRequestingSite
argument_list|()
argument_list|,
name|getRequestingPort
argument_list|()
argument_list|,
name|getRequestingProtocol
argument_list|()
argument_list|,
name|getRequestingPrompt
argument_list|()
argument_list|,
name|getRequestingScheme
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

