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
name|plugins
operator|.
name|repository
operator|.
name|ssh
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|repository
operator|.
name|AbstractRepository
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
name|CredentialsUtil
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSshBasedRepository
extends|extends
name|AbstractRepository
block|{
specifier|private
name|File
name|keyFile
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|passFile
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|userPassword
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|keyFilePassword
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|user
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|host
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|port
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|boolean
name|allowedAgentUse
init|=
literal|false
decl_stmt|;
specifier|public
name|AbstractSshBasedRepository
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * hashmap of user/hosts with credentials.      * key is hostname, value is Credentials      **/
specifier|private
specifier|static
name|HashMap
name|credentialsCache
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_CREDENTILAS_CACHE_SIZE
init|=
literal|100
decl_stmt|;
comment|/**      * get a new session using the default attributes if the given String is a full uri, use the      * data from the uri instead      *       * @param pathOrUri      *            might be just a path or a full ssh or sftp uri      * @return matching Session      */
specifier|protected
name|Session
name|getSession
parameter_list|(
name|String
name|pathOrUri
parameter_list|)
throws|throws
name|IOException
block|{
name|URI
name|uri
init|=
name|parseURI
argument_list|(
name|pathOrUri
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|String
name|user
init|=
name|getUser
argument_list|()
decl_stmt|;
name|String
name|userPassword
init|=
name|getUserPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|uri
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|host
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|port
operator|=
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|userInfo
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|userInfo
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|user
operator|=
name|userInfo
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|userInfo
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
expr_stmt|;
name|userPassword
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
name|userInfo
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"missing host information. host should be provided either "
operator|+
literal|"directly on the repository or in the connection URI"
argument_list|)
throw|;
block|}
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|Credentials
name|c
init|=
name|requestCredentials
argument_list|(
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
name|user
operator|=
name|c
operator|.
name|getUserName
argument_list|()
expr_stmt|;
name|userPassword
operator|=
name|c
operator|.
name|getPasswd
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"username is not set"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|SshCache
operator|.
name|getInstance
argument_list|()
operator|.
name|getSession
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|user
argument_list|,
name|userPassword
argument_list|,
name|getKeyFile
argument_list|()
argument_list|,
name|getKeyFilePassword
argument_list|()
argument_list|,
name|getPassFile
argument_list|()
argument_list|,
name|isAllowedAgentUse
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Just check the uri for sanity      *       * @param source      *            String of the uri      * @return URI object of the String or null      */
specifier|private
name|URI
name|parseURI
parameter_list|(
name|String
name|source
parameter_list|)
block|{
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|uri
operator|.
name|getScheme
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|equals
argument_list|(
name|getRepositoryScheme
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|source
argument_list|,
literal|"Wrong scheme in URI. Expected "
operator|+
name|getRepositoryScheme
argument_list|()
operator|+
literal|" as scheme!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|uri
operator|.
name|getHost
argument_list|()
operator|==
literal|null
operator|&&
name|getHost
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|source
argument_list|,
literal|"Missing host in URI or in resolver"
argument_list|)
throw|;
block|}
if|if
condition|(
name|uri
operator|.
name|getPath
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|source
argument_list|,
literal|"Missing path in URI"
argument_list|)
throw|;
block|}
comment|//if (uri.getUserInfo() == null&& getUser() == null) {
comment|//    throw new URISyntaxException(source, "Missing username in URI or in resolver");
comment|//}
return|return
name|uri
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|error
argument_list|(
literal|"The uri '"
operator|+
name|source
operator|+
literal|"' is in the wrong format."
argument_list|)
expr_stmt|;
name|Message
operator|.
name|error
argument_list|(
literal|"Please use "
operator|+
name|getRepositoryScheme
argument_list|()
operator|+
literal|"://user:pass@hostname/path/to/repository"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      *  Called, when user was not found in URL.      * Maintain static hashe of credentials and retrieve or ask credentials      * for host.      *      * @param host       *       host for which we want to get credentials.      * @return credentials for given host       **/
specifier|private
name|Credentials
name|requestCredentials
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|Object
name|o
init|=
name|credentialsCache
operator|.
name|get
argument_list|(
name|host
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|Credentials
name|c
init|=
name|CredentialsUtil
operator|.
name|promptCredentials
argument_list|(
operator|new
name|Credentials
argument_list|(
literal|null
argument_list|,
name|host
argument_list|,
name|user
argument_list|,
name|userPassword
argument_list|)
argument_list|,
name|getPassFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|credentialsCache
operator|.
name|size
argument_list|()
operator|>
name|MAX_CREDENTILAS_CACHE_SIZE
condition|)
block|{
name|credentialsCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|credentialsCache
operator|.
name|put
argument_list|(
name|host
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
else|else
block|{
return|return
operator|(
name|Credentials
operator|)
name|o
return|;
block|}
block|}
comment|/**      * closes the session and remove it from the cache (eg. on case of errors)      *       * @param session      *            key for the cache      * @param pathOrUri      *            to release      */
specifier|protected
name|void
name|releaseSession
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|pathOrUri
parameter_list|)
block|{
name|session
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|SshCache
operator|.
name|getInstance
argument_list|()
operator|.
name|clearSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the default user to use for the connection if no user is given or a PEM file is used      *       * @param user      *            to use      */
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
comment|/**      * @return the user to use for the connection if no user is given or a PEM file is used      */
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
comment|/**      * Sets the full file path to use for accessing a PEM key file      *       * @param filePath      *            fully qualified name      */
specifier|public
name|void
name|setKeyFile
parameter_list|(
name|File
name|filePath
parameter_list|)
block|{
name|this
operator|.
name|keyFile
operator|=
name|filePath
expr_stmt|;
if|if
condition|(
operator|!
name|keyFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Pemfile "
operator|+
name|keyFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" doesn't exist."
argument_list|)
expr_stmt|;
name|keyFile
operator|=
literal|null
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|keyFile
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Pemfile "
operator|+
name|keyFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" not readable."
argument_list|)
expr_stmt|;
name|keyFile
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Using "
operator|+
name|keyFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" as keyfile."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return the keyFile      */
specifier|public
name|File
name|getKeyFile
parameter_list|()
block|{
return|return
name|keyFile
return|;
block|}
comment|/**      * @param password      *            password to use for user/password authentication      */
specifier|public
name|void
name|setUserPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|userPassword
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * @return the keyFile password for public key based authentication      */
specifier|public
name|String
name|getKeyFilePassword
parameter_list|()
block|{
return|return
name|keyFilePassword
return|;
block|}
comment|/**      * @param keyFilePassword      *            sets password for public key based authentication      */
specifier|public
name|void
name|setKeyFilePassword
parameter_list|(
name|String
name|keyFilePassword
parameter_list|)
block|{
name|this
operator|.
name|keyFilePassword
operator|=
name|keyFilePassword
expr_stmt|;
block|}
comment|/**      * @return the user password      */
specifier|public
name|String
name|getUserPassword
parameter_list|()
block|{
return|return
name|userPassword
return|;
block|}
comment|/**      * @return the host      */
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * @param host      *            the host to set      */
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|/**      * @return the port      */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * @param port      *            the port to set      */
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
comment|/**      * @param passFile      *            the passfile to set      */
specifier|public
name|void
name|setPassFile
parameter_list|(
name|File
name|passFile
parameter_list|)
block|{
name|this
operator|.
name|passFile
operator|=
name|passFile
expr_stmt|;
block|}
comment|/**      * @return the passFile      */
specifier|public
name|File
name|getPassFile
parameter_list|()
block|{
return|return
name|passFile
return|;
block|}
comment|/**      * @return allowedAgentUse      *            Whether use of a local SSH agent for authentication is allowed      */
specifier|public
name|boolean
name|isAllowedAgentUse
parameter_list|()
block|{
return|return
name|allowedAgentUse
return|;
block|}
comment|/**      * @param allowedAgentUse      *            Whether use of a local SSH agent for authentication is allowed      */
specifier|public
name|void
name|setAllowedAgentUse
parameter_list|(
name|boolean
name|allowedAgentUse
parameter_list|)
block|{
name|this
operator|.
name|allowedAgentUse
operator|=
name|allowedAgentUse
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|String
name|getRepositoryScheme
parameter_list|()
function_decl|;
block|}
end_class

end_unit

