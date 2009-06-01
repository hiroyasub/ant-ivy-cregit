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
name|sftp
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|BasicResource
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
name|Resource
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
name|TransferEvent
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
name|ssh
operator|.
name|AbstractSshBasedRepository
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
name|ssh
operator|.
name|SshCache
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
name|SftpATTRS
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
name|SftpException
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
name|SftpProgressMonitor
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
operator|.
name|LsEntry
import|;
end_import

begin_comment
comment|/**  * SFTP Repository, allow to use a repository accessed by sftp protocol. It supports all operations:  * get, put and list. It relies on jsch for sftp handling, and thus is compatible with sftp version  * 0, 1, 2 and 3  */
end_comment

begin_class
specifier|public
class|class
name|SFTPRepository
extends|extends
name|AbstractSshBasedRepository
block|{
comment|// this must be a long to ensure the multiplication done below uses longs
comment|// instead of ints which are not big enough to hold the result
specifier|private
specifier|static
specifier|final
name|long
name|MILLIS_PER_SECOND
init|=
literal|1000
decl_stmt|;
specifier|private
specifier|final
class|class
name|MyProgressMonitor
implements|implements
name|SftpProgressMonitor
block|{
specifier|private
name|long
name|totalLength
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|int
name|op
parameter_list|,
name|String
name|src
parameter_list|,
name|String
name|dest
parameter_list|,
name|long
name|max
parameter_list|)
block|{
name|totalLength
operator|=
name|max
expr_stmt|;
name|fireTransferStarted
argument_list|(
name|max
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|end
parameter_list|()
block|{
name|fireTransferCompleted
argument_list|(
name|totalLength
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|count
parameter_list|(
name|long
name|count
parameter_list|)
block|{
name|fireTransferProgress
argument_list|(
name|count
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
specifier|public
name|SFTPRepository
parameter_list|()
block|{
block|}
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
return|return
operator|new
name|SFTPResource
argument_list|(
name|this
argument_list|,
name|source
argument_list|)
return|;
block|}
comment|/**      * This method is similar to getResource, except that the returned resource is fully initialized      * (resolved in the sftp repository), and that the given string is a full remote path      *       * @param path      *            the full remote path in the repository of the resource      * @return a fully initialized resource, able to answer to all its methods without needing any      *         further connection      */
specifier|public
name|Resource
name|resolveResource
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Collection
name|r
init|=
name|c
operator|.
name|ls
argument_list|(
name|getPath
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|r
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|LsEntry
condition|)
block|{
name|LsEntry
name|entry
init|=
operator|(
name|LsEntry
operator|)
name|obj
decl_stmt|;
name|SftpATTRS
name|attrs
init|=
name|entry
operator|.
name|getAttrs
argument_list|()
decl_stmt|;
return|return
operator|new
name|BasicResource
argument_list|(
name|path
argument_list|,
literal|true
argument_list|,
name|attrs
operator|.
name|getSize
argument_list|()
argument_list|,
name|attrs
operator|.
name|getMTime
argument_list|()
operator|*
name|MILLIS_PER_SECOND
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"reolving resource error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// silent fail, return unexisting resource
block|}
return|return
operator|new
name|BasicResource
argument_list|(
name|path
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|SFTPResource
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|path
init|=
name|getPath
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|get
argument_list|(
name|path
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"impossible to open stream for "
operator|+
name|resource
operator|+
literal|" on "
operator|+
name|getHost
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
else|:
literal|""
operator|)
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"impossible to open stream for "
operator|+
name|resource
operator|+
literal|" on "
operator|+
name|getHost
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
else|:
literal|""
operator|)
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|void
name|get
parameter_list|(
name|String
name|source
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|IOException
block|{
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|source
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_GET
argument_list|)
expr_stmt|;
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|(
name|source
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|path
init|=
name|getPath
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|c
operator|.
name|get
argument_list|(
name|path
argument_list|,
name|destination
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|MyProgressMonitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"impossible to get "
operator|+
name|source
operator|+
literal|" on "
operator|+
name|getHost
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
else|:
literal|""
operator|)
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"impossible to get "
operator|+
name|source
operator|+
literal|" on "
operator|+
name|getHost
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
else|:
literal|""
operator|)
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|void
name|put
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|destination
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|destination
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_PUT
argument_list|)
expr_stmt|;
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|(
name|destination
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|path
init|=
name|getPath
argument_list|(
name|destination
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|overwrite
operator|&&
name|checkExistence
argument_list|(
name|path
argument_list|,
name|c
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"destination file exists and overwrite == false"
argument_list|)
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|mkdirs
argument_list|(
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|put
argument_list|(
name|source
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|path
argument_list|,
operator|new
name|MyProgressMonitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
name|void
name|mkdirs
parameter_list|(
name|String
name|directory
parameter_list|,
name|ChannelSftp
name|c
parameter_list|)
throws|throws
name|IOException
throws|,
name|SftpException
block|{
try|try
block|{
name|SftpATTRS
name|att
init|=
name|c
operator|.
name|stat
argument_list|(
name|directory
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|att
operator|.
name|isDir
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SftpException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|directory
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|mkdirs
argument_list|(
name|directory
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|directory
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|mkdir
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getPath
parameter_list|(
name|String
name|sftpURI
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|result
init|=
literal|null
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|sftpURI
argument_list|)
decl_stmt|;
name|result
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|URISyntaxException
argument_list|(
name|sftpURI
argument_list|,
literal|"Missing path in URI."
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|List
name|list
parameter_list|(
name|String
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|Collection
name|r
init|=
name|c
operator|.
name|ls
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|parent
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|parent
operator|=
name|parent
operator|+
literal|"/"
expr_stmt|;
block|}
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|r
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|LsEntry
condition|)
block|{
name|LsEntry
name|entry
init|=
operator|(
name|LsEntry
operator|)
name|obj
decl_stmt|;
if|if
condition|(
literal|"."
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
operator|||
literal|".."
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|result
operator|.
name|add
argument_list|(
name|parent
operator|+
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"Failed to return a listing for '"
operator|+
name|parent
operator|+
literal|"'"
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Checks the existence for a remote file      *       * @param file      *            to check      * @param channel      *            to use      * @returns true if file exists, false otherwise      * @throws IOException      * @throws SftpException      */
specifier|private
name|boolean
name|checkExistence
parameter_list|(
name|String
name|file
parameter_list|,
name|ChannelSftp
name|channel
parameter_list|)
throws|throws
name|IOException
throws|,
name|SftpException
block|{
try|try
block|{
return|return
name|channel
operator|.
name|stat
argument_list|(
name|file
argument_list|)
operator|!=
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Establish the connection to the server if not yet connected, and listen to ivy events for      * closing connection when resolve is finished. Not meant to be used in multi threaded      * environment.      *       * @return the ChannelSftp with which a connection is established      * @throws IOException      *             if any connection problem occurs      */
specifier|private
name|ChannelSftp
name|getSftpChannel
parameter_list|(
name|String
name|pathOrUri
parameter_list|)
throws|throws
name|IOException
block|{
name|Session
name|session
init|=
name|getSession
argument_list|(
name|pathOrUri
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|session
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|ChannelSftp
name|channel
init|=
name|SshCache
operator|.
name|getInstance
argument_list|()
operator|.
name|getChannelSftp
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|channel
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|channel
operator|=
operator|(
name|ChannelSftp
operator|)
name|session
operator|.
name|openChannel
argument_list|(
literal|"sftp"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: connected to "
operator|+
name|host
operator|+
literal|"!"
argument_list|)
expr_stmt|;
name|SshCache
operator|.
name|getInstance
argument_list|()
operator|.
name|attachChannelSftp
argument_list|(
name|session
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
return|return
name|channel
return|;
block|}
specifier|protected
name|String
name|getRepositoryScheme
parameter_list|()
block|{
comment|// use the Resolver type name here?
comment|// would be nice if it would be static, so we could use SFTPResolver.getTypeName()
return|return
literal|"sftp"
return|;
block|}
block|}
end_class

end_unit

