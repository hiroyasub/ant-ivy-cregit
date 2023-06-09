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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|core
operator|.
name|IvyContext
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
name|core
operator|.
name|event
operator|.
name|IvyEvent
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
name|core
operator|.
name|event
operator|.
name|IvyListener
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
name|core
operator|.
name|event
operator|.
name|resolve
operator|.
name|EndResolveEvent
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
name|Checks
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
name|ChannelSftp
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
name|JSch
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
name|JSchException
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

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|UIKeyboardInteractive
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
name|UserInfo
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
name|agentproxy
operator|.
name|Connector
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
name|agentproxy
operator|.
name|ConnectorFactory
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
name|agentproxy
operator|.
name|RemoteIdentityRepository
import|;
end_import

begin_comment
comment|/**  * a class to cache SSH Connections and Channel for the SSH Repository each session is defined by  * connecting user / host / port two maps are used to find cache entries one map is using the above  * keys, the other uses the session itself  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SshCache
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SSH_DEFAULT_PORT
init|=
literal|22
decl_stmt|;
specifier|private
name|SshCache
parameter_list|()
block|{
block|}
specifier|private
specifier|static
name|SshCache
name|instance
init|=
operator|new
name|SshCache
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|SshCache
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
class|class
name|Entry
block|{
specifier|private
name|Session
name|session
init|=
literal|null
decl_stmt|;
specifier|private
name|ChannelSftp
name|channelSftp
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
name|String
name|user
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|port
init|=
name|SSH_DEFAULT_PORT
decl_stmt|;
comment|/**          * @return the host          */
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**          * @return the port          */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**          * @return the user          */
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
specifier|public
name|Entry
parameter_list|(
name|Session
name|newSession
parameter_list|,
name|String
name|newUser
parameter_list|,
name|String
name|newHost
parameter_list|,
name|int
name|newPort
parameter_list|)
block|{
name|session
operator|=
name|newSession
expr_stmt|;
name|host
operator|=
name|newHost
expr_stmt|;
name|user
operator|=
name|newUser
expr_stmt|;
name|port
operator|=
name|newPort
expr_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getEventManager
argument_list|()
operator|.
name|addIvyListener
argument_list|(
operator|new
name|IvyListener
argument_list|()
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|getSource
argument_list|()
operator|.
name|removeIvyListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|clearSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|EndResolveEvent
operator|.
name|NAME
argument_list|)
expr_stmt|;
block|}
comment|/**          * attach an sftp channel to this cache entry          *          * @param newChannel          *            to attach          */
specifier|public
name|void
name|setChannelSftp
parameter_list|(
name|ChannelSftp
name|newChannel
parameter_list|)
block|{
if|if
condition|(
name|channelSftp
operator|!=
literal|null
operator|&&
name|newChannel
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Only one sftp channelSftp per session allowed"
argument_list|)
throw|;
block|}
name|this
operator|.
name|channelSftp
operator|=
name|newChannel
expr_stmt|;
block|}
comment|/**          * @return the attached sftp channel          */
specifier|public
name|ChannelSftp
name|getChannelSftp
parameter_list|()
block|{
return|return
name|channelSftp
return|;
block|}
comment|/**          * @return the session          */
specifier|private
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
comment|/**          * remove channelSftp and disconnect if necessary          */
specifier|public
name|void
name|releaseChannelSftp
parameter_list|()
block|{
if|if
condition|(
name|channelSftp
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|channelSftp
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: closing sftp connection from "
operator|+
name|host
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|channelSftp
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|channelSftp
operator|=
literal|null
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: sftp connection closed from "
operator|+
name|host
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * key is username / host / port      *      * @see #createCacheKey(String, String, int) for details      */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Entry
argument_list|>
name|uriCacheMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * key is the session itself      */
specifier|private
specifier|final
name|Map
argument_list|<
name|Session
argument_list|,
name|Entry
argument_list|>
name|sessionCacheMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * retrieves a session entry for a given hostname from the cache      *      * @param user      *            to retrieve session for      * @param host      *            ditto      * @param port      *            ditto      * @return null or the existing entry      */
specifier|private
name|Entry
name|getCacheEntry
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
return|return
name|uriCacheMap
operator|.
name|get
argument_list|(
name|createCacheKey
argument_list|(
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a combined cache key from the given key parts      *      * @param user      *            name of the user      * @param host      *            of the connection      * @param port      *            of the connection      * @return key for the cache      */
specifier|private
specifier|static
name|String
name|createCacheKey
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|String
name|portToUse
init|=
literal|"22"
decl_stmt|;
if|if
condition|(
name|port
operator|!=
operator|-
literal|1
operator|&&
name|port
operator|!=
name|SSH_DEFAULT_PORT
condition|)
block|{
name|portToUse
operator|=
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
return|return
name|user
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
operator|+
literal|"@"
operator|+
name|host
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
operator|+
literal|":"
operator|+
name|portToUse
return|;
block|}
comment|/**      * retrieves a session entry for a given session from the cache      *      * @param session      *            to retrieve cache entry for      * @return null or the existing entry      */
specifier|private
name|Entry
name|getCacheEntry
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
return|return
name|sessionCacheMap
operator|.
name|get
argument_list|(
name|session
argument_list|)
return|;
block|}
comment|/**      * Sets a session to a given combined key into the cache If an old session object already      * exists, close and remove it      *      * @param user      *            of the session      * @param host      *            of the session      * @param port      *            of the session      * @param newSession      *            Session to save      */
specifier|private
name|void
name|setSession
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|Session
name|newSession
parameter_list|)
block|{
name|Entry
name|entry
init|=
name|uriCacheMap
operator|.
name|get
argument_list|(
name|createCacheKey
argument_list|(
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
decl_stmt|;
name|Session
name|oldSession
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|oldSession
operator|=
name|entry
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|oldSession
operator|!=
literal|null
operator|&&
operator|!
name|oldSession
operator|.
name|equals
argument_list|(
name|newSession
argument_list|)
operator|&&
name|oldSession
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|entry
operator|.
name|releaseChannelSftp
argument_list|()
expr_stmt|;
name|String
name|oldhost
init|=
name|oldSession
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SSH :: closing ssh connection from "
operator|+
name|oldhost
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|oldSession
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SSH :: ssh connection closed from "
operator|+
name|oldhost
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newSession
operator|==
literal|null
operator|&&
name|entry
operator|!=
literal|null
condition|)
block|{
name|uriCacheMap
operator|.
name|remove
argument_list|(
name|createCacheKey
argument_list|(
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getSession
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sessionCacheMap
operator|.
name|remove
argument_list|(
name|entry
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Entry
name|newEntry
init|=
operator|new
name|Entry
argument_list|(
name|newSession
argument_list|,
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|uriCacheMap
operator|.
name|put
argument_list|(
name|createCacheKey
argument_list|(
name|user
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
argument_list|,
name|newEntry
argument_list|)
expr_stmt|;
name|sessionCacheMap
operator|.
name|put
argument_list|(
name|newSession
argument_list|,
name|newEntry
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * discards session entries from the cache      *      * @param session      *            to clear      */
specifier|public
name|void
name|clearSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|Entry
name|entry
init|=
name|sessionCacheMap
operator|.
name|get
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|setSession
argument_list|(
name|entry
operator|.
name|getUser
argument_list|()
argument_list|,
name|entry
operator|.
name|getHost
argument_list|()
argument_list|,
name|entry
operator|.
name|getPort
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * retrieves an sftp channel from the cache      *      * @param session      *            to connect to      * @return channelSftp or null if not successful (channel not existent or dead)      * @throws IOException should never happen      */
specifier|public
name|ChannelSftp
name|getChannelSftp
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|IOException
block|{
name|ChannelSftp
name|channel
init|=
literal|null
decl_stmt|;
name|Entry
name|entry
init|=
name|getCacheEntry
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|channel
operator|=
name|entry
operator|.
name|getChannelSftp
argument_list|()
expr_stmt|;
if|if
condition|(
name|channel
operator|!=
literal|null
operator|&&
operator|!
name|channel
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|entry
operator|.
name|releaseChannelSftp
argument_list|()
expr_stmt|;
name|channel
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|channel
return|;
block|}
comment|/**      * attaches a channelSftp to an existing session cache entry      *      * @param session      *            to attach the channel to      * @param channel      *            channel to attach      */
specifier|public
name|void
name|attachChannelSftp
parameter_list|(
name|Session
name|session
parameter_list|,
name|ChannelSftp
name|channel
parameter_list|)
block|{
name|Entry
name|entry
init|=
name|getCacheEntry
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No entry for "
operator|+
name|session
operator|+
literal|" in the cache"
argument_list|)
throw|;
block|}
name|entry
operator|.
name|setChannelSftp
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Attempts to connect to a local SSH agent (using either UNIX sockets or PuTTY's Pageant)      *      * @param jsch      *            Connection to be attached to an available local agent      * @return true if connected to agent, false otherwise      */
specifier|private
name|boolean
name|attemptAgentUse
parameter_list|(
name|JSch
name|jsch
parameter_list|)
block|{
try|try
block|{
name|Connector
name|con
init|=
name|ConnectorFactory
operator|.
name|getDefault
argument_list|()
operator|.
name|createConnector
argument_list|()
decl_stmt|;
name|jsch
operator|.
name|setIdentityRepository
argument_list|(
operator|new
name|RemoteIdentityRepository
argument_list|(
name|con
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SSH :: Failure connecting to agent :: "
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Gets a session from the cache or establishes a new session if necessary      *      * @param host      *            to connect to      * @param port      *            to use for session (-1 == use standard port)      * @param username      *            for the session to use      * @param userPassword      *            to use for authentication (optional)      * @param pemFile      *            File to use for public key authentication      * @param pemPassword      *            to use for accessing the pemFile (optional)      * @param passFile      *            to store credentials      * @param allowedAgentUse      *            Whether to communicate with an agent for authentication      * @return session or null if not successful      * @throws IOException if something goes wrong      */
specifier|public
name|Session
name|getSession
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|userPassword
parameter_list|,
name|File
name|pemFile
parameter_list|,
name|String
name|pemPassword
parameter_list|,
name|File
name|passFile
parameter_list|,
name|boolean
name|allowedAgentUse
parameter_list|)
throws|throws
name|IOException
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|host
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|username
argument_list|,
literal|"user"
argument_list|)
expr_stmt|;
name|Entry
name|entry
init|=
name|getCacheEntry
argument_list|(
name|username
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|session
operator|=
name|entry
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|==
literal|null
operator|||
operator|!
name|session
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SSH :: connecting to "
operator|+
name|host
operator|+
literal|"..."
argument_list|)
expr_stmt|;
try|try
block|{
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|!=
operator|-
literal|1
condition|)
block|{
name|session
operator|=
name|jsch
operator|.
name|getSession
argument_list|(
name|username
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|jsch
operator|.
name|getSession
argument_list|(
name|username
argument_list|,
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowedAgentUse
condition|)
block|{
name|attemptAgentUse
argument_list|(
name|jsch
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pemFile
operator|!=
literal|null
condition|)
block|{
name|jsch
operator|.
name|addIdentity
argument_list|(
name|pemFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|pemPassword
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|setUserInfo
argument_list|(
operator|new
name|CfUserInfo
argument_list|(
name|host
argument_list|,
name|username
argument_list|,
name|userPassword
argument_list|,
name|pemFile
argument_list|,
name|pemPassword
argument_list|,
name|passFile
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|setDaemonThread
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Properties
name|config
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|config
operator|.
name|setProperty
argument_list|(
literal|"PreferredAuthentications"
argument_list|,
literal|"publickey,keyboard-interactive,password"
argument_list|)
expr_stmt|;
name|session
operator|.
name|setConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|session
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SSH :: connected to "
operator|+
name|host
operator|+
literal|"!"
argument_list|)
expr_stmt|;
name|setSession
argument_list|(
name|username
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
if|if
condition|(
name|passFile
operator|!=
literal|null
operator|&&
name|passFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|passFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|session
return|;
block|}
comment|/**      * feeds in password silently into JSch      */
specifier|private
specifier|static
class|class
name|CfUserInfo
implements|implements
name|UserInfo
implements|,
name|UIKeyboardInteractive
block|{
specifier|private
name|String
name|userPassword
decl_stmt|;
specifier|private
name|String
name|pemPassword
decl_stmt|;
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
specifier|final
name|File
name|pemFile
decl_stmt|;
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
specifier|private
specifier|final
name|File
name|passfile
decl_stmt|;
specifier|public
name|CfUserInfo
parameter_list|(
name|String
name|host
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|userPassword
parameter_list|,
name|File
name|pemFile
parameter_list|,
name|String
name|pemPassword
parameter_list|,
name|File
name|passfile
parameter_list|)
block|{
name|this
operator|.
name|userPassword
operator|=
name|userPassword
expr_stmt|;
name|this
operator|.
name|pemPassword
operator|=
name|pemPassword
expr_stmt|;
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|passfile
operator|=
name|passfile
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|pemFile
operator|=
name|pemFile
expr_stmt|;
block|}
specifier|public
name|void
name|showMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|promptYesNo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptPassword
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptPassphrase
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
if|if
condition|(
name|userPassword
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
name|userName
argument_list|,
name|userPassword
argument_list|)
argument_list|,
name|passfile
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|userName
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
block|}
return|return
name|userPassword
return|;
block|}
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
if|if
condition|(
name|pemPassword
operator|==
literal|null
operator|&&
name|pemFile
operator|!=
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
name|pemFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|userName
argument_list|,
name|pemPassword
argument_list|)
argument_list|,
name|passfile
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|userName
operator|=
name|c
operator|.
name|getUserName
argument_list|()
expr_stmt|;
name|pemPassword
operator|=
name|c
operator|.
name|getPasswd
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|pemPassword
return|;
block|}
specifier|public
name|String
index|[]
name|promptKeyboardInteractive
parameter_list|(
name|String
name|destination
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|instruction
parameter_list|,
name|String
index|[]
name|prompt
parameter_list|,
name|boolean
index|[]
name|echo
parameter_list|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|getPassword
argument_list|()
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

