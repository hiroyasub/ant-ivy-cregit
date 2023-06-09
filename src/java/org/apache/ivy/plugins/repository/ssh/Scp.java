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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|FileOutputStream
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Channel
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
comment|/**  * This class is using the scp client to transfer data and information for the repository.  *<p>  * It is based on the SCPClient from the ganymed ssh library from Christian Plattner, released under  * a BSD style license.  *<p>  * To minimize the dependency to the ssh library and because we needed some additional  * functionality, we decided to copy'n'paste the single class rather than to inherit or delegate it  * somehow.  *<p>  * Nevertheless credit should go to the original author.  */
end_comment

begin_class
specifier|public
class|class
name|Scp
block|{
specifier|private
specifier|static
specifier|final
name|int
name|MODE_LENGTH
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|SEND_FILE_BUFFER_LENGTH
init|=
literal|40000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|SEND_BYTES_BUFFER_LENGTH
init|=
literal|512
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MIN_TLINE_LENGTH
init|=
literal|8
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CLINE_SPACE_INDEX2
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CLINE_SPACE_INDEX1
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MIN_C_LINE_LENGTH
init|=
literal|8
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_LINE_BUFFER_LENGTH
init|=
literal|30
decl_stmt|;
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
comment|/*      * Maximum length authorized for scp lines. This is a random limit - if your path names are      * longer, then adjust it.      */
specifier|private
specifier|static
specifier|final
name|int
name|MAX_SCP_LINE_LENGTH
init|=
literal|8192
decl_stmt|;
specifier|private
name|Session
name|session
decl_stmt|;
specifier|public
class|class
name|FileInfo
block|{
specifier|private
name|String
name|filename
decl_stmt|;
specifier|private
name|long
name|length
decl_stmt|;
specifier|private
name|long
name|lastModified
decl_stmt|;
comment|/**          * @param filename          *            The filename to set.          */
specifier|public
name|void
name|setFilename
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
block|}
comment|/**          * @return Returns the filename.          */
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|filename
return|;
block|}
comment|/**          * @param length          *            The length to set.          */
specifier|public
name|void
name|setLength
parameter_list|(
name|long
name|length
parameter_list|)
block|{
name|this
operator|.
name|length
operator|=
name|length
expr_stmt|;
block|}
comment|/**          * @return Returns the length.          */
specifier|public
name|long
name|getLength
parameter_list|()
block|{
return|return
name|length
return|;
block|}
comment|/**          * @param lastModified          *            The lastModified to set.          */
specifier|public
name|void
name|setLastModified
parameter_list|(
name|long
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
comment|/**          * @return Returns the lastModified.          */
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
block|}
specifier|public
name|Scp
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot accept null argument!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
specifier|private
name|void
name|readResponse
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|int
name|c
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|c
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp terminated unexpectedly."
argument_list|)
throw|;
block|}
if|if
condition|(
name|c
operator|!=
literal|1
operator|&&
name|c
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp sent illegal error code."
argument_list|)
throw|;
block|}
if|if
condition|(
name|c
operator|==
literal|2
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp terminated with error."
argument_list|)
throw|;
block|}
name|String
name|err
init|=
name|receiveLine
argument_list|(
name|is
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp terminated with error ("
operator|+
name|err
operator|+
literal|")."
argument_list|)
throw|;
block|}
specifier|private
name|String
name|receiveLine
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|DEFAULT_LINE_BUFFER_LENGTH
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
name|MAX_SCP_LINE_LENGTH
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp sent a too long line"
argument_list|)
throw|;
block|}
name|int
name|c
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp terminated unexpectedly."
argument_list|)
throw|;
block|}
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|parseCLine
parameter_list|(
name|String
name|line
parameter_list|,
name|FileInfo
name|fileInfo
parameter_list|)
throws|throws
name|RemoteScpException
block|{
comment|/* Minimum line: "xxxx y z" ---> 8 chars */
name|long
name|len
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|<
name|MIN_C_LINE_LENGTH
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary, line too short."
argument_list|)
throw|;
block|}
if|if
condition|(
name|line
operator|.
name|charAt
argument_list|(
name|CLINE_SPACE_INDEX1
argument_list|)
operator|!=
literal|' '
operator|||
name|line
operator|.
name|charAt
argument_list|(
name|CLINE_SPACE_INDEX2
argument_list|)
operator|==
literal|' '
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary."
argument_list|)
throw|;
block|}
name|int
name|lengthNameSep
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|,
name|CLINE_SPACE_INDEX2
argument_list|)
decl_stmt|;
if|if
condition|(
name|lengthNameSep
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary."
argument_list|)
throw|;
block|}
name|String
name|lengthSubstring
init|=
name|line
operator|.
name|substring
argument_list|(
name|CLINE_SPACE_INDEX2
argument_list|,
name|lengthNameSep
argument_list|)
decl_stmt|;
name|String
name|nameSubstring
init|=
name|line
operator|.
name|substring
argument_list|(
name|lengthNameSep
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|lengthSubstring
operator|.
name|length
argument_list|()
operator|<=
literal|0
operator|||
name|nameSubstring
operator|.
name|length
argument_list|()
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary."
argument_list|)
throw|;
block|}
if|if
condition|(
name|CLINE_SPACE_INDEX2
operator|+
literal|1
operator|+
name|lengthSubstring
operator|.
name|length
argument_list|()
operator|+
name|nameSubstring
operator|.
name|length
argument_list|()
operator|!=
name|line
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary."
argument_list|)
throw|;
block|}
try|try
block|{
name|len
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|lengthSubstring
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary, cannot parse file length."
argument_list|)
throw|;
block|}
if|if
condition|(
name|len
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary, illegal file length."
argument_list|)
throw|;
block|}
name|fileInfo
operator|.
name|setLength
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|fileInfo
operator|.
name|setFilename
argument_list|(
name|nameSubstring
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|parseTLine
parameter_list|(
name|String
name|line
parameter_list|,
name|FileInfo
name|fileInfo
parameter_list|)
throws|throws
name|RemoteScpException
block|{
comment|/* Minimum line: "0 0 0 0" ---> 8 chars */
name|long
name|modtime
decl_stmt|;
name|long
name|firstMsec
decl_stmt|;
name|long
name|atime
decl_stmt|;
name|long
name|secondMsec
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|<
name|MIN_TLINE_LENGTH
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed T line sent by remote SCP binary, line too short."
argument_list|)
throw|;
block|}
name|int
name|firstMsecBegin
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|firstMsecBegin
operator|==
literal|0
operator|||
name|firstMsecBegin
operator|>=
name|line
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed T line sent by remote SCP binary, line not enough data."
argument_list|)
throw|;
block|}
name|int
name|atimeBegin
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|,
name|firstMsecBegin
operator|+
literal|1
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|atimeBegin
operator|==
literal|0
operator|||
name|atimeBegin
operator|>=
name|line
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed T line sent by remote SCP binary, line not enough data."
argument_list|)
throw|;
block|}
name|int
name|secondMsecBegin
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|" "
argument_list|,
name|atimeBegin
operator|+
literal|1
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|secondMsecBegin
operator|==
literal|0
operator|||
name|secondMsecBegin
operator|>=
name|line
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed T line sent by remote SCP binary, line not enough data."
argument_list|)
throw|;
block|}
try|try
block|{
name|modtime
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstMsecBegin
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|firstMsec
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|firstMsecBegin
argument_list|,
name|atimeBegin
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|atime
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|atimeBegin
argument_list|,
name|secondMsecBegin
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|secondMsec
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|secondMsecBegin
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary, cannot parse file length."
argument_list|)
throw|;
block|}
if|if
condition|(
name|modtime
operator|<
literal|0
operator|||
name|firstMsec
operator|<
literal|0
operator|||
name|atime
operator|<
literal|0
operator|||
name|secondMsec
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Malformed C line sent by remote SCP binary, illegal file length."
argument_list|)
throw|;
block|}
name|fileInfo
operator|.
name|setLastModified
argument_list|(
name|modtime
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|sendFile
parameter_list|(
name|Channel
name|channel
parameter_list|,
name|String
name|localFile
parameter_list|,
name|String
name|remoteName
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
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
name|OutputStream
name|os
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|channel
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|SEND_FILE_BUFFER_LENGTH
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|channel
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|SEND_BYTES_BUFFER_LENGTH
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|channel
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|channel
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JSchException
name|jsche
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Channel connection problems"
argument_list|,
name|jsche
argument_list|)
throw|;
block|}
name|readResponse
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|localFile
argument_list|)
decl_stmt|;
name|long
name|remain
init|=
name|f
operator|.
name|length
argument_list|()
decl_stmt|;
name|String
name|cMode
init|=
name|mode
decl_stmt|;
if|if
condition|(
name|cMode
operator|==
literal|null
condition|)
block|{
name|cMode
operator|=
literal|"0600"
expr_stmt|;
block|}
name|String
name|cline
init|=
literal|"C"
operator|+
name|cMode
operator|+
literal|" "
operator|+
name|remain
operator|+
literal|" "
operator|+
name|remoteName
operator|+
literal|"\n"
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|cline
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readResponse
argument_list|(
name|is
argument_list|)
expr_stmt|;
try|try
init|(
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
init|)
block|{
while|while
condition|(
name|remain
operator|>
literal|0
condition|)
block|{
name|int
name|trans
decl_stmt|;
if|if
condition|(
name|remain
operator|>
name|buffer
operator|.
name|length
condition|)
block|{
name|trans
operator|=
name|buffer
operator|.
name|length
expr_stmt|;
block|}
else|else
block|{
name|trans
operator|=
operator|(
name|int
operator|)
name|remain
expr_stmt|;
block|}
if|if
condition|(
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|trans
argument_list|)
operator|!=
name|trans
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot read enough from local file "
operator|+
name|localFile
argument_list|)
throw|;
block|}
name|os
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|trans
argument_list|)
expr_stmt|;
name|remain
operator|-=
name|trans
expr_stmt|;
block|}
block|}
name|os
operator|.
name|write
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|readResponse
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"E\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Receive a file via scp and store it in a stream      *      * @param channel      *            ssh channel to use      * @param file      *            to receive from remote      * @param targetStream      *            to store file into (if null, get only file info)      * @return file information of the file we received      * @throws IOException      *             in case of network or protocol trouble      * @throws RemoteScpException      *             in case of problems on the target system (connection is fine)      */
specifier|private
name|FileInfo
name|receiveStream
parameter_list|(
name|Channel
name|channel
parameter_list|,
name|String
name|file
parameter_list|,
name|OutputStream
name|targetStream
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
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
name|OutputStream
name|os
init|=
name|channel
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|channel
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|channel
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|channel
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JSchException
name|jsche
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Channel connection problems"
argument_list|,
name|jsche
argument_list|)
throw|;
block|}
name|os
operator|.
name|write
argument_list|(
literal|0x0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|FileInfo
name|fileInfo
init|=
operator|new
name|FileInfo
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|c
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote scp terminated unexpectedly."
argument_list|)
throw|;
block|}
name|String
name|line
init|=
name|receiveLine
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'T'
condition|)
block|{
name|parseTLine
argument_list|(
name|line
argument_list|,
name|fileInfo
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|0x0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|c
operator|==
literal|1
operator|||
name|c
operator|==
literal|2
condition|)
block|{
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote SCP error: "
operator|+
name|line
argument_list|)
throw|;
block|}
if|if
condition|(
name|c
operator|==
literal|'C'
condition|)
block|{
name|parseCLine
argument_list|(
name|line
argument_list|,
name|fileInfo
argument_list|)
expr_stmt|;
break|break;
block|}
throw|throw
operator|new
name|RemoteScpException
argument_list|(
literal|"Remote SCP error: "
operator|+
operator|(
operator|(
name|char
operator|)
name|c
operator|)
operator|+
name|line
argument_list|)
throw|;
block|}
if|if
condition|(
name|targetStream
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
literal|0x0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
try|try
block|{
name|long
name|remain
init|=
name|fileInfo
operator|.
name|getLength
argument_list|()
decl_stmt|;
while|while
condition|(
name|remain
operator|>
literal|0
condition|)
block|{
name|int
name|trans
decl_stmt|;
if|if
condition|(
name|remain
operator|>
name|buffer
operator|.
name|length
condition|)
block|{
name|trans
operator|=
name|buffer
operator|.
name|length
expr_stmt|;
block|}
else|else
block|{
name|trans
operator|=
operator|(
name|int
operator|)
name|remain
expr_stmt|;
block|}
name|int
name|thisTimeReceived
init|=
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|trans
argument_list|)
decl_stmt|;
if|if
condition|(
name|thisTimeReceived
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Remote scp terminated connection unexpectedly"
argument_list|)
throw|;
block|}
name|targetStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|thisTimeReceived
argument_list|)
expr_stmt|;
name|remain
operator|-=
name|thisTimeReceived
expr_stmt|;
block|}
name|targetStream
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
if|if
condition|(
name|targetStream
operator|!=
literal|null
condition|)
block|{
name|targetStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|(
name|e
operator|)
throw|;
block|}
name|readResponse
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|0x0
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
name|fileInfo
return|;
block|}
comment|/**      * @return ChannelExec      * @throws JSchException if something goes wrong      */
specifier|private
name|ChannelExec
name|getExecChannel
parameter_list|()
throws|throws
name|JSchException
block|{
name|ChannelExec
name|channel
decl_stmt|;
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
return|return
name|channel
return|;
block|}
comment|/**      * Copy a local file to a remote site, uses the specified mode when creating the file on the      * remote side.      *      * @param localFile      *            Path and name of local file. Must be absolute.      * @param remoteTargetDir      *            Remote target directory where the file has to end up (optional)      * @param remoteTargetName      *            file name to use on the target system      * @param mode      *            a four digit string (e.g., 0644, see "man chmod", "man open")      * @throws IOException      *             in case of network problems      * @throws RemoteScpException      *             in case of problems on the target system (connection ok)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|void
name|put
parameter_list|(
name|String
name|localFile
parameter_list|,
name|String
name|remoteTargetDir
parameter_list|,
name|String
name|remoteTargetName
parameter_list|,
name|String
name|mode
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|localFile
operator|==
literal|null
operator|||
name|remoteTargetName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null argument."
argument_list|)
throw|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|mode
operator|.
name|length
argument_list|()
operator|!=
name|MODE_LENGTH
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid mode."
argument_list|)
throw|;
block|}
for|for
control|(
name|char
name|c
range|:
name|mode
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|c
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid mode."
argument_list|)
throw|;
block|}
block|}
block|}
name|String
name|cmd
init|=
literal|"scp -t "
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|cmd
operator|+=
literal|"-p "
expr_stmt|;
block|}
if|if
condition|(
name|remoteTargetDir
operator|!=
literal|null
operator|&&
name|remoteTargetDir
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|cmd
operator|+=
literal|"-d "
operator|+
name|remoteTargetDir
expr_stmt|;
block|}
try|try
block|{
name|channel
operator|=
name|getExecChannel
argument_list|()
expr_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|channel
argument_list|,
name|localFile
argument_list|,
name|remoteTargetName
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|channel
operator|.
name|disconnect
argument_list|()
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error during SCP transfer."
operator|+
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
comment|/**      * Download a file from the remote server to a local file.      *      * @param remoteFile      *            Path and name of the remote file.      * @param localTarget      *            Local file where to store the data. Must be absolute.      * @throws IOException      *             in case of network problems      * @throws RemoteScpException      *             in case of problems on the target system (connection ok)      */
specifier|public
name|void
name|get
parameter_list|(
name|String
name|remoteFile
parameter_list|,
name|String
name|localTarget
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|localTarget
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fop
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|get
argument_list|(
name|remoteFile
argument_list|,
name|fop
argument_list|)
expr_stmt|;
block|}
comment|/**      * Download a file from the remote server into an OutputStream      *      * @param remoteFile      *            Path and name of the remote file.      * @param localTarget      *            OutputStream to store the data.      * @throws IOException      *             in case of network problems      * @throws RemoteScpException      *             in case of problems on the target system (connection ok)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|void
name|get
parameter_list|(
name|String
name|remoteFile
parameter_list|,
name|OutputStream
name|localTarget
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|remoteFile
operator|==
literal|null
operator|||
name|localTarget
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null argument."
argument_list|)
throw|;
block|}
name|String
name|cmd
init|=
literal|"scp -p -f "
operator|+
name|remoteFile
decl_stmt|;
try|try
block|{
name|channel
operator|=
name|getExecChannel
argument_list|()
expr_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|receiveStream
argument_list|(
name|channel
argument_list|,
name|remoteFile
argument_list|,
name|localTarget
argument_list|)
expr_stmt|;
name|channel
operator|.
name|disconnect
argument_list|()
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error during SCP transfer. "
operator|+
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
comment|/**      * Initiates an SCP sequence but stops after getting fileinformation header      *      * @param remoteFile      *            to get information for      * @return the file information got      * @throws IOException      *             in case of network problems      * @throws RemoteScpException      *             in case of problems on the target system (connection ok)      */
specifier|public
name|FileInfo
name|getFileinfo
parameter_list|(
name|String
name|remoteFile
parameter_list|)
throws|throws
name|IOException
throws|,
name|RemoteScpException
block|{
name|ChannelExec
name|channel
init|=
literal|null
decl_stmt|;
name|FileInfo
name|fileInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|remoteFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null argument."
argument_list|)
throw|;
block|}
name|String
name|cmd
init|=
literal|"scp -p -f \""
operator|+
name|remoteFile
operator|+
literal|"\""
decl_stmt|;
try|try
block|{
name|channel
operator|=
name|getExecChannel
argument_list|()
expr_stmt|;
name|channel
operator|.
name|setCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|fileInfo
operator|=
name|receiveStream
argument_list|(
name|channel
argument_list|,
name|remoteFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|channel
operator|.
name|disconnect
argument_list|()
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
argument_list|(
literal|"Error during SCP transfer. "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
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
return|return
name|fileInfo
return|;
block|}
block|}
end_class

end_unit

