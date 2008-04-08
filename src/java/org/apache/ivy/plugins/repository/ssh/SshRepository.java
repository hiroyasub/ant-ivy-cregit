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
name|BufferedReader
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
name|StringReader
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
name|ChannelExec
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

begin_comment
comment|/**  * Ivy Repository based on SSH  */
end_comment

begin_class
specifier|public
class|class
name|SshRepository
extends|extends
name|AbstractSshBasedRepository
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
name|String
name|ARGUMENT_PLACEHOLDER
init|=
literal|"%arg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|POLL_SLEEP_TIME
init|=
literal|500
decl_stmt|;
specifier|private
name|char
name|fileSeparator
init|=
literal|'/'
decl_stmt|;
specifier|private
name|String
name|listCommand
init|=
literal|"ls -1"
decl_stmt|;
specifier|private
name|String
name|existCommand
init|=
literal|"ls"
decl_stmt|;
specifier|private
name|String
name|createDirCommand
init|=
literal|"mkdir"
decl_stmt|;
specifier|private
name|String
name|publishPermissions
init|=
literal|"0600"
decl_stmt|;
comment|/**      * create a new resource with lazy initializing      */
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:getResource called: "
operator|+
name|source
argument_list|)
expr_stmt|;
return|return
operator|new
name|SshResource
argument_list|(
name|this
argument_list|,
name|source
argument_list|)
return|;
block|}
comment|/**      * Fetch the needed file information for a given file (size, last modification time) and report      * it back in a SshResource      *       * @param source       *            ssh uri for the file to get info for      * @return SshResource filled with the needed informations      * @see org.apache.ivy.plugins.repository.Repository#getResource(java.lang.String)      */
specifier|public
name|SshResource
name|resolveResource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:resolveResource called: "
operator|+
name|source
argument_list|)
expr_stmt|;
name|SshResource
name|result
init|=
literal|null
decl_stmt|;
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|session
operator|=
name|getSession
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|Scp
name|myCopy
init|=
operator|new
name|Scp
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|Scp
operator|.
name|FileInfo
name|fileInfo
init|=
name|myCopy
operator|.
name|getFileinfo
argument_list|(
operator|new
name|URI
argument_list|(
name|source
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|=
operator|new
name|SshResource
argument_list|(
name|this
argument_list|,
name|source
argument_list|,
literal|true
argument_list|,
name|fileInfo
operator|.
name|getLength
argument_list|()
argument_list|,
name|fileInfo
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|releaseSession
argument_list|(
name|session
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
operator|new
name|SshResource
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|releaseSession
argument_list|(
name|session
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
operator|new
name|SshResource
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RemoteScpException
name|e
parameter_list|)
block|{
name|result
operator|=
operator|new
name|SshResource
argument_list|()
expr_stmt|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:resolveResource end."
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Reads out the output of a ssh session exec      *       * @param channel      *            Channel to read from      * @param strStdout      *            StringBuffer that receives Session Stdout output      * @param strStderr      *            StringBuffer that receives Session Stderr output      * @throws IOException      *             in case of trouble with the network      */
specifier|private
name|void
name|readSessionOutput
parameter_list|(
name|ChannelExec
name|channel
parameter_list|,
name|StringBuffer
name|strStdout
parameter_list|,
name|StringBuffer
name|strStderr
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|stdout
init|=
name|channel
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|InputStream
name|stderr
init|=
name|channel
operator|.
name|getErrStream
argument_list|()
decl_stmt|;
try|try
block|{
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e1
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
operator|new
name|IOException
argument_list|(
literal|"Channel connection problems"
argument_list|)
operator|.
name|initCause
argument_list|(
name|e1
argument_list|)
throw|;
block|}
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
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|avail
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|avail
operator|=
name|stdout
operator|.
name|available
argument_list|()
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|stdout
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
operator|(
name|avail
operator|>
name|BUFFER_SIZE
operator|-
literal|1
condition|?
name|BUFFER_SIZE
else|:
name|avail
operator|)
argument_list|)
decl_stmt|;
name|strStdout
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
operator|(
name|avail
operator|=
name|stderr
operator|.
name|available
argument_list|()
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|stderr
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
operator|(
name|avail
operator|>
name|BUFFER_SIZE
operator|-
literal|1
condition|?
name|BUFFER_SIZE
else|:
name|avail
operator|)
argument_list|)
decl_stmt|;
name|strStderr
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|channel
operator|.
name|isClosed
argument_list|()
condition|)
block|{
break|break;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|POLL_SLEEP_TIME
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ee
parameter_list|)
block|{
comment|// ignored
block|}
block|}
name|int
name|avail
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|avail
operator|=
name|stdout
operator|.
name|available
argument_list|()
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|stdout
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
operator|(
name|avail
operator|>
name|BUFFER_SIZE
operator|-
literal|1
condition|?
name|BUFFER_SIZE
else|:
name|avail
operator|)
argument_list|)
decl_stmt|;
name|strStdout
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
operator|(
name|avail
operator|=
name|stderr
operator|.
name|available
argument_list|()
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|stderr
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
operator|(
name|avail
operator|>
name|BUFFER_SIZE
operator|-
literal|1
condition|?
name|BUFFER_SIZE
else|:
name|avail
operator|)
argument_list|)
decl_stmt|;
name|strStderr
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.repository.Repository#list(java.lang.String)      */
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
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:list called: "
operator|+
name|parent
argument_list|)
expr_stmt|;
name|ArrayList
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Session
name|session
init|=
literal|null
decl_stmt|;
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
name|session
operator|=
name|getSession
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|channel
operator|=
name|getExecChannel
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|URI
name|parentUri
init|=
literal|null
decl_stmt|;
try|try
block|{
name|parentUri
operator|=
operator|new
name|URI
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
comment|// failed earlier
block|}
name|String
name|fullCmd
init|=
name|replaceArgument
argument_list|(
name|listCommand
argument_list|,
name|parentUri
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|fullCmd
argument_list|)
expr_stmt|;
name|StringBuffer
name|stdOut
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|stdErr
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|readSessionOutput
argument_list|(
name|channel
argument_list|,
name|stdOut
argument_list|,
name|stdErr
argument_list|)
expr_stmt|;
if|if
condition|(
name|channel
operator|.
name|getExitStatus
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Ssh ListCommand exited with status != 0"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|error
argument_list|(
name|stdErr
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|stdOut
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * @param session      * @return      * @throws JSchException      */
specifier|private
name|ChannelExec
name|getExecChannel
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|IOException
block|{
name|ChannelExec
name|channel
decl_stmt|;
try|try
block|{
name|channel
operator|=
operator|(
name|ChannelExec
operator|)
name|session
operator|.
name|openChannel
argument_list|(
literal|"exec"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
return|return
name|channel
return|;
block|}
comment|/**      * Replace the argument placeholder with argument or append the argument if no placeholder is      * present      *       * @param command      *            with argument placeholder or not      * @param argument      * @return replaced full command      */
specifier|private
name|String
name|replaceArgument
parameter_list|(
name|String
name|command
parameter_list|,
name|String
name|argument
parameter_list|)
block|{
name|String
name|fullCmd
decl_stmt|;
if|if
condition|(
name|command
operator|.
name|indexOf
argument_list|(
name|ARGUMENT_PLACEHOLDER
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|fullCmd
operator|=
name|command
operator|+
literal|" "
operator|+
name|argument
expr_stmt|;
block|}
else|else
block|{
name|fullCmd
operator|=
name|command
operator|.
name|replaceAll
argument_list|(
name|ARGUMENT_PLACEHOLDER
argument_list|,
name|argument
argument_list|)
expr_stmt|;
block|}
return|return
name|fullCmd
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.repository.Repository#put(java.io.File, java.lang.String, boolean)      */
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
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:put called: "
operator|+
name|destination
argument_list|)
expr_stmt|;
name|Session
name|session
init|=
name|getSession
argument_list|(
name|destination
argument_list|)
decl_stmt|;
try|try
block|{
name|URI
name|destinationUri
init|=
literal|null
decl_stmt|;
try|try
block|{
name|destinationUri
operator|=
operator|new
name|URI
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// failed earlier in getSession()
block|}
name|String
name|filePath
init|=
name|destinationUri
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|int
name|lastSep
init|=
name|filePath
operator|.
name|lastIndexOf
argument_list|(
name|fileSeparator
argument_list|)
decl_stmt|;
name|String
name|path
decl_stmt|;
name|String
name|name
decl_stmt|;
if|if
condition|(
name|lastSep
operator|==
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|filePath
expr_stmt|;
name|path
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|filePath
operator|.
name|substring
argument_list|(
name|lastSep
operator|+
literal|1
argument_list|)
expr_stmt|;
name|path
operator|=
name|filePath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastSep
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|overwrite
condition|)
block|{
if|if
condition|(
name|checkExistence
argument_list|(
name|filePath
argument_list|,
name|session
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
block|}
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|makePath
argument_list|(
name|path
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
name|Scp
name|myCopy
init|=
operator|new
name|Scp
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|myCopy
operator|.
name|put
argument_list|(
name|source
operator|.
name|getCanonicalPath
argument_list|()
argument_list|,
name|path
argument_list|,
name|name
argument_list|,
name|publishPermissions
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|releaseSession
argument_list|(
name|session
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteScpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Tries to create a directory path on the target system      *       * @param path      *            to create      * @param connnection      *            to use      */
specifier|private
name|void
name|makePath
parameter_list|(
name|String
name|path
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|IOException
block|{
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
name|String
name|trimmed
init|=
name|path
decl_stmt|;
try|try
block|{
while|while
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|trimmed
operator|.
name|charAt
argument_list|(
name|trimmed
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
name|fileSeparator
condition|)
block|{
name|trimmed
operator|=
name|trimmed
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|trimmed
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|trimmed
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|checkExistence
argument_list|(
name|trimmed
argument_list|,
name|session
argument_list|)
condition|)
block|{
return|return;
block|}
name|int
name|nextSlash
init|=
name|trimmed
operator|.
name|lastIndexOf
argument_list|(
name|fileSeparator
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextSlash
operator|>
literal|0
condition|)
block|{
name|String
name|parent
init|=
name|trimmed
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|nextSlash
argument_list|)
decl_stmt|;
name|makePath
argument_list|(
name|parent
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
name|channel
operator|=
name|getExecChannel
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|String
name|mkdir
init|=
name|replaceArgument
argument_list|(
name|createDirCommand
argument_list|,
name|trimmed
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository: trying to create path: "
operator|+
name|mkdir
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|mkdir
argument_list|)
expr_stmt|;
name|StringBuffer
name|stdOut
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|stdErr
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|readSessionOutput
argument_list|(
name|channel
argument_list|,
name|stdOut
argument_list|,
name|stdErr
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * check for existence of file or dir on target system      *       * @param filePath      *            to the object to check      * @param session      *            to use      * @return true: object exists, false otherwise      */
specifier|private
name|boolean
name|checkExistence
parameter_list|(
name|String
name|filePath
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository: checkExistence called: "
operator|+
name|filePath
argument_list|)
expr_stmt|;
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
name|channel
operator|=
name|getExecChannel
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|String
name|fullCmd
init|=
name|replaceArgument
argument_list|(
name|existCommand
argument_list|,
name|filePath
argument_list|)
decl_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|fullCmd
argument_list|)
expr_stmt|;
name|StringBuffer
name|stdOut
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|stdErr
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|readSessionOutput
argument_list|(
name|channel
argument_list|,
name|stdOut
argument_list|,
name|stdErr
argument_list|)
expr_stmt|;
return|return
name|channel
operator|.
name|getExitStatus
argument_list|()
operator|==
literal|0
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.repository.Repository#get(java.lang.String, java.io.File)      */
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
name|Message
operator|.
name|debug
argument_list|(
literal|"SShRepository:get called: "
operator|+
name|source
operator|+
literal|" to "
operator|+
name|destination
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|destination
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|destination
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|Session
name|session
init|=
name|getSession
argument_list|(
name|source
argument_list|)
decl_stmt|;
try|try
block|{
name|URI
name|sourceUri
init|=
literal|null
decl_stmt|;
try|try
block|{
name|sourceUri
operator|=
operator|new
name|URI
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// fails earlier
block|}
if|if
condition|(
name|sourceUri
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"could not parse URI "
operator|+
name|source
argument_list|)
expr_stmt|;
return|return;
block|}
name|Scp
name|myCopy
init|=
operator|new
name|Scp
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|myCopy
operator|.
name|get
argument_list|(
name|sourceUri
operator|.
name|getPath
argument_list|()
argument_list|,
name|destination
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|releaseSession
argument_list|(
name|session
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteScpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * sets the list command to use for a directory listing listing must be only the filename and      * each filename on a separate line      *       * @param cmd      *            to use. default is "ls -1"      */
specifier|public
name|void
name|setListCommand
parameter_list|(
name|String
name|cmd
parameter_list|)
block|{
name|this
operator|.
name|listCommand
operator|=
name|cmd
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return the list command to use      */
specifier|public
name|String
name|getListCommand
parameter_list|()
block|{
return|return
name|listCommand
return|;
block|}
comment|/**      * @return the createDirCommand      */
specifier|public
name|String
name|getCreateDirCommand
parameter_list|()
block|{
return|return
name|createDirCommand
return|;
block|}
comment|/**      * @param createDirCommand      *            the createDirCommand to set      */
specifier|public
name|void
name|setCreateDirCommand
parameter_list|(
name|String
name|createDirCommand
parameter_list|)
block|{
name|this
operator|.
name|createDirCommand
operator|=
name|createDirCommand
expr_stmt|;
block|}
comment|/**      * @return the existCommand      */
specifier|public
name|String
name|getExistCommand
parameter_list|()
block|{
return|return
name|existCommand
return|;
block|}
comment|/**      * @param existCommand      *            the existCommand to set      */
specifier|public
name|void
name|setExistCommand
parameter_list|(
name|String
name|existCommand
parameter_list|)
block|{
name|this
operator|.
name|existCommand
operator|=
name|existCommand
expr_stmt|;
block|}
comment|/**      * The file separator is the separator to use on the target system On a unix system it is '/',      * but I don't know, how this is solved on different ssh implementations. Using the default      * might be fine      *       * @param fileSeparator      *            The fileSeparator to use. default '/'      */
specifier|public
name|void
name|setFileSeparator
parameter_list|(
name|char
name|fileSeparator
parameter_list|)
block|{
name|this
operator|.
name|fileSeparator
operator|=
name|fileSeparator
expr_stmt|;
block|}
comment|/**      * A four digit string (e.g., 0644, see "man chmod", "man open") specifying the permissions      * of the published files.      */
specifier|public
name|void
name|setPublishPermissions
parameter_list|(
name|String
name|permissions
parameter_list|)
block|{
name|this
operator|.
name|publishPermissions
operator|=
name|permissions
expr_stmt|;
block|}
comment|/**      * return ssh as scheme use the Resolver type name here? would be nice if it would be static, so      * we could use SshResolver.getTypeName()      */
specifier|protected
name|String
name|getRepositoryScheme
parameter_list|()
block|{
return|return
literal|"ssh"
return|;
block|}
comment|/**      * Not really streaming...need to implement a proper streaming approach?      *       * @param resource      *            to stream      * @return InputStream of the resource data      */
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|SshResource
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
name|Session
name|session
init|=
name|getSession
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Scp
name|scp
init|=
operator|new
name|Scp
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|scp
operator|.
name|get
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|releaseSession
argument_list|(
name|session
argument_list|,
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|RemoteScpException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

