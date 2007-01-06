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
name|repository
operator|.
name|vfs
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HostConfiguration
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
name|HttpConnection
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
name|HttpConnectionManager
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
name|HttpConnectionManagerParams
import|;
end_import

begin_comment
comment|/**  * Modified version of the WebdavConnectionManager from VFS which adds support for httpclient 3.x.  * See http://issues.apache.org/jira/browse/VFS-74 for more info.  *   * A connection manager that provides access to a single HttpConnection.  This  * manager makes no attempt to provide exclusive access to the contained  * HttpConnection.  *<p/>  * imario@apache.org: Keep connection in ThreadLocal.  *  * @author<a href="mailto:imario@apache.org">Mario Ivankovits</a>  * @author<a href="mailto:becke@u.washington.edu">Michael Becke</a>  * @author Eric Johnson  * @author<a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>  * @author<a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>  * @author Laura Werner  * @author Maarten Coene  */
end_comment

begin_class
class|class
name|IvyWebdavConnectionManager
implements|implements
name|HttpConnectionManager
block|{
comment|/**      * Since the same connection is about to be reused, make sure the      * previous request was completely processed, and if not      * consume it now.      *      * @param conn The connection      */
specifier|static
name|void
name|finishLastResponse
parameter_list|(
name|HttpConnection
name|conn
parameter_list|)
block|{
name|InputStream
name|lastResponse
init|=
name|conn
operator|.
name|getLastResponseInputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastResponse
operator|!=
literal|null
condition|)
block|{
name|conn
operator|.
name|setLastResponseInputStream
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|lastResponse
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|//FIXME: badness - close to force reconnect.
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * The thread data      */
specifier|protected
name|ThreadLocal
name|localHttpConnection
init|=
operator|new
name|ThreadLocal
argument_list|()
block|{
specifier|protected
name|Object
name|initialValue
parameter_list|()
block|{
return|return
operator|new
name|Entry
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/**      * Collection of parameters associated with this connection manager.      */
specifier|private
name|HttpConnectionManagerParams
name|params
init|=
operator|new
name|HttpConnectionManagerParams
argument_list|()
decl_stmt|;
comment|/**      * release the connection of the current thread      */
specifier|public
name|void
name|releaseLocalConnection
parameter_list|()
block|{
if|if
condition|(
name|getLocalHttpConnection
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|releaseConnection
argument_list|(
name|getLocalHttpConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|Entry
block|{
comment|/**          * The http connection          */
specifier|private
name|HttpConnection
name|conn
init|=
literal|null
decl_stmt|;
comment|/**          * The time the connection was made idle.          */
specifier|private
name|long
name|idleStartTime
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
block|}
specifier|public
name|IvyWebdavConnectionManager
parameter_list|()
block|{
block|}
specifier|protected
name|HttpConnection
name|getLocalHttpConnection
parameter_list|()
block|{
return|return
operator|(
operator|(
name|Entry
operator|)
name|localHttpConnection
operator|.
name|get
argument_list|()
operator|)
operator|.
name|conn
return|;
block|}
specifier|protected
name|void
name|setLocalHttpConnection
parameter_list|(
name|HttpConnection
name|conn
parameter_list|)
block|{
operator|(
operator|(
name|Entry
operator|)
name|localHttpConnection
operator|.
name|get
argument_list|()
operator|)
operator|.
name|conn
operator|=
name|conn
expr_stmt|;
block|}
specifier|protected
name|long
name|getIdleStartTime
parameter_list|()
block|{
return|return
operator|(
operator|(
name|Entry
operator|)
name|localHttpConnection
operator|.
name|get
argument_list|()
operator|)
operator|.
name|idleStartTime
return|;
block|}
specifier|protected
name|void
name|setIdleStartTime
parameter_list|(
name|long
name|idleStartTime
parameter_list|)
block|{
operator|(
operator|(
name|Entry
operator|)
name|localHttpConnection
operator|.
name|get
argument_list|()
operator|)
operator|.
name|idleStartTime
operator|=
name|idleStartTime
expr_stmt|;
block|}
comment|/**      * @see HttpConnectionManager#getConnection(org.apache.commons.httpclient.HostConfiguration)      */
specifier|public
name|HttpConnection
name|getConnection
parameter_list|(
name|HostConfiguration
name|hostConfiguration
parameter_list|)
block|{
return|return
name|getConnection
argument_list|(
name|hostConfiguration
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Gets the staleCheckingEnabled value to be set on HttpConnections that are created.      *      * @return<code>true</code> if stale checking will be enabled on HttpConections      * @see HttpConnection#isStaleCheckingEnabled()      */
specifier|public
name|boolean
name|isConnectionStaleCheckingEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|params
operator|.
name|isStaleCheckingEnabled
argument_list|()
return|;
block|}
comment|/**      * Sets the staleCheckingEnabled value to be set on HttpConnections that are created.      *      * @param connectionStaleCheckingEnabled<code>true</code> if stale checking will be enabled      *                                       on HttpConections      * @see HttpConnection#setStaleCheckingEnabled(boolean)      */
specifier|public
name|void
name|setConnectionStaleCheckingEnabled
parameter_list|(
name|boolean
name|connectionStaleCheckingEnabled
parameter_list|)
block|{
name|this
operator|.
name|params
operator|.
name|setStaleCheckingEnabled
argument_list|(
name|connectionStaleCheckingEnabled
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see HttpConnectionManager#getConnection(HostConfiguration, long)      * @since 3.0      */
specifier|public
name|HttpConnection
name|getConnectionWithTimeout
parameter_list|(
name|HostConfiguration
name|hostConfiguration
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|HttpConnection
name|httpConnection
init|=
name|getLocalHttpConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpConnection
operator|==
literal|null
condition|)
block|{
name|httpConnection
operator|=
operator|new
name|HttpConnection
argument_list|(
name|hostConfiguration
argument_list|)
expr_stmt|;
name|setLocalHttpConnection
argument_list|(
name|httpConnection
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setHttpConnectionManager
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|getParams
argument_list|()
operator|.
name|setDefaults
argument_list|(
name|this
operator|.
name|params
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// make sure the host and proxy are correct for this connection
comment|// close it and set the values if they are not
if|if
condition|(
operator|!
name|hostConfiguration
operator|.
name|hostEquals
argument_list|(
name|httpConnection
argument_list|)
operator|||
operator|!
name|hostConfiguration
operator|.
name|proxyEquals
argument_list|(
name|httpConnection
argument_list|)
condition|)
block|{
if|if
condition|(
name|httpConnection
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|httpConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|httpConnection
operator|.
name|setHost
argument_list|(
name|hostConfiguration
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setPort
argument_list|(
name|hostConfiguration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setProtocol
argument_list|(
name|hostConfiguration
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setLocalAddress
argument_list|(
name|hostConfiguration
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setProxyHost
argument_list|(
name|hostConfiguration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|httpConnection
operator|.
name|setProxyPort
argument_list|(
name|hostConfiguration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|finishLastResponse
argument_list|(
name|httpConnection
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove the connection from the timeout handler
name|setIdleStartTime
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
return|return
name|httpConnection
return|;
block|}
comment|/**      * @see HttpConnectionManager#getConnection(HostConfiguration, long)      * @deprecated Use #getConnectionWithTimeout(HostConfiguration, long)      */
specifier|public
name|HttpConnection
name|getConnection
parameter_list|(
name|HostConfiguration
name|hostConfiguration
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
return|return
name|getConnectionWithTimeout
argument_list|(
name|hostConfiguration
argument_list|,
name|timeout
argument_list|)
return|;
block|}
comment|/**      * @see HttpConnectionManager#releaseConnection(org.apache.commons.httpclient.HttpConnection)      */
specifier|public
name|void
name|releaseConnection
parameter_list|(
name|HttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|conn
operator|!=
name|getLocalHttpConnection
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected release of an unknown connection."
argument_list|)
throw|;
block|}
name|finishLastResponse
argument_list|(
name|getLocalHttpConnection
argument_list|()
argument_list|)
expr_stmt|;
comment|// track the time the connection was made idle
name|setIdleStartTime
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|closeIdleConnections
parameter_list|(
name|long
name|idleTimeout
parameter_list|)
block|{
name|long
name|maxIdleTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|idleTimeout
decl_stmt|;
if|if
condition|(
name|getIdleStartTime
argument_list|()
operator|<=
name|maxIdleTime
condition|)
block|{
name|getLocalHttpConnection
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** 	 * {@inheritDoc} 	 */
specifier|public
name|HttpConnectionManagerParams
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
specifier|public
name|void
name|setParams
parameter_list|(
name|HttpConnectionManagerParams
name|params
parameter_list|)
block|{
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
block|}
block|}
end_class

end_unit

