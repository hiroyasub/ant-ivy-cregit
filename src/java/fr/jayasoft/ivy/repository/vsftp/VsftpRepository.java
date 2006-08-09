begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
name|vsftp
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Ivy
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
name|IvyContext
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
name|event
operator|.
name|IvyEvent
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
name|event
operator|.
name|IvyListener
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
name|event
operator|.
name|resolve
operator|.
name|EndResolveEvent
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
name|repository
operator|.
name|AbstractRepository
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
name|repository
operator|.
name|Resource
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
comment|/**  * Repository using SecureCRT vsftp command line program to access an sftp repository  *   * This is especially useful to leverage the gssapi authentication supported by SecureCRT.  *   * In caseswhere usual sftp is enough, prefer the 100% java solution of sftp repository.  *   * This requires SecureCRT to be in the PATH.  *   * Tested with SecureCRT 5.0.5  *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|VsftpRepository
extends|extends
name|AbstractRepository
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PROMPT
init|=
literal|"vsftp> "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|SimpleDateFormat
name|FORMAT
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"MMM dd, yyyy HH:mm"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
specifier|private
name|String
name|_host
decl_stmt|;
specifier|private
name|String
name|_username
decl_stmt|;
specifier|private
name|String
name|_authentication
init|=
literal|"gssapi"
decl_stmt|;
specifier|private
name|Reader
name|_in
decl_stmt|;
specifier|private
name|Reader
name|_err
decl_stmt|;
specifier|private
name|PrintWriter
name|_out
decl_stmt|;
specifier|private
specifier|volatile
name|StringBuffer
name|_errors
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|private
name|long
name|_readTimeout
init|=
literal|3000
decl_stmt|;
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|lslToResource
argument_list|(
name|source
argument_list|,
name|sendCommand
argument_list|(
literal|"ls -l "
operator|+
name|source
argument_list|,
literal|true
argument_list|)
argument_list|)
return|;
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
name|File
name|destDir
init|=
name|destination
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|destDir
operator|!=
literal|null
condition|)
block|{
name|sendCommand
argument_list|(
literal|"lcd "
operator|+
name|destDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|.
name|exists
argument_list|()
condition|)
block|{
name|destination
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|int
name|index
init|=
name|source
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|srcName
init|=
name|index
operator|==
operator|-
literal|1
condition|?
name|source
else|:
name|source
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|File
name|to
init|=
name|destDir
operator|==
literal|null
condition|?
operator|new
name|File
argument_list|(
name|srcName
argument_list|)
else|:
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|srcName
argument_list|)
decl_stmt|;
name|sendCommand
argument_list|(
literal|"get "
operator|+
name|source
argument_list|,
name|getExpectedDownloadMessage
argument_list|(
name|source
argument_list|,
name|to
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|to
operator|.
name|renameTo
argument_list|(
name|destination
argument_list|)
expr_stmt|;
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
name|String
name|response
init|=
name|sendCommand
argument_list|(
literal|"ls -l "
operator|+
name|parent
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|startsWith
argument_list|(
literal|"ls"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|lines
init|=
name|response
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
decl_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|lines
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|lines
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
while|while
condition|(
name|lines
index|[
name|i
index|]
operator|.
name|endsWith
argument_list|(
literal|"\r"
argument_list|)
operator|||
name|lines
index|[
name|i
index|]
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|lines
index|[
name|i
index|]
operator|=
name|lines
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lines
index|[
name|i
index|]
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
name|lines
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|parent
operator|+
name|lines
index|[
name|i
index|]
operator|.
name|substring
argument_list|(
name|lines
index|[
name|i
index|]
operator|.
name|lastIndexOf
argument_list|(
literal|' '
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
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
if|if
condition|(
name|getResource
argument_list|(
name|destination
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|overwrite
condition|)
block|{
name|sendCommand
argument_list|(
literal|"rm "
operator|+
name|destination
argument_list|,
name|getExpectedRemoveMessage
argument_list|(
name|destination
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|int
name|index
init|=
name|destination
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|destDir
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|destDir
operator|=
name|destination
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|mkdirs
argument_list|(
name|destDir
argument_list|)
expr_stmt|;
name|sendCommand
argument_list|(
literal|"cd "
operator|+
name|destDir
argument_list|)
expr_stmt|;
block|}
name|String
name|to
init|=
name|destDir
operator|!=
literal|null
condition|?
name|destDir
operator|+
literal|"/"
operator|+
name|source
operator|.
name|getName
argument_list|()
else|:
name|source
operator|.
name|getName
argument_list|()
decl_stmt|;
name|sendCommand
argument_list|(
literal|"put "
operator|+
name|source
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|getExpectedUploadMessage
argument_list|(
name|source
argument_list|,
name|to
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|sendCommand
argument_list|(
literal|"mv "
operator|+
name|to
operator|+
literal|" "
operator|+
name|destination
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|mkdirs
parameter_list|(
name|String
name|destDir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dirExists
argument_list|(
name|destDir
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|destDir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|destDir
operator|=
name|destDir
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|destDir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|index
init|=
name|destDir
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|mkdirs
argument_list|(
name|destDir
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
empty_stmt|;
block|}
name|sendCommand
argument_list|(
literal|"mkdir "
operator|+
name|destDir
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|dirExists
parameter_list|(
name|String
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|!
name|sendCommand
argument_list|(
literal|"ls "
operator|+
name|dir
argument_list|,
literal|true
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"ls: "
argument_list|)
return|;
block|}
specifier|protected
name|String
name|sendCommand
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|sendCommand
argument_list|(
name|command
argument_list|,
literal|false
argument_list|,
name|_readTimeout
argument_list|)
return|;
block|}
specifier|protected
name|void
name|sendCommand
parameter_list|(
name|String
name|command
parameter_list|,
name|Pattern
name|expectedResponse
parameter_list|)
throws|throws
name|IOException
block|{
name|sendCommand
argument_list|(
name|command
argument_list|,
name|expectedResponse
argument_list|,
name|_readTimeout
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * The behaviour of vsftp with some commands is to log the resulting message on the error stream, 	 * even if everything is ok. 	 *   	 * So it's quite difficult if there was an error or not. 	 *  	 * Hence we compare the response with the expected message and deal with it. 	 * The problem is that this is very specific to the version of vsftp used for the test, 	 *  	 * That's why expected messages are obtained using overridable protected methods. 	 */
specifier|protected
name|void
name|sendCommand
parameter_list|(
name|String
name|command
parameter_list|,
name|Pattern
name|expectedResponse
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|response
init|=
name|sendCommand
argument_list|(
name|command
argument_list|,
literal|true
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expectedResponse
operator|.
name|matcher
argument_list|(
name|response
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"invalid response from server:"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"expected: '"
operator|+
name|expectedResponse
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"was:      '"
operator|+
name|response
operator|+
literal|"'"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|response
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|String
name|sendCommand
parameter_list|(
name|String
name|command
parameter_list|,
name|boolean
name|sendErrorAsResponse
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|sendCommand
argument_list|(
name|command
argument_list|,
name|sendErrorAsResponse
argument_list|,
name|_readTimeout
argument_list|)
return|;
block|}
specifier|protected
name|String
name|sendCommand
parameter_list|(
name|String
name|command
parameter_list|,
name|boolean
name|sendErrorAsResponse
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
name|ensureConnectionOpened
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"sending command '"
operator|+
name|command
operator|+
literal|"' to "
operator|+
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|_out
operator|.
name|println
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|_out
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|readResponse
argument_list|(
name|sendErrorAsResponse
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|protected
name|String
name|readResponse
parameter_list|(
name|boolean
name|sendErrorAsResponse
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|readResponse
argument_list|(
name|sendErrorAsResponse
argument_list|,
name|_readTimeout
argument_list|)
return|;
block|}
specifier|protected
name|String
name|readResponse
parameter_list|(
specifier|final
name|boolean
name|sendErrorAsResponse
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|StringBuffer
name|response
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|final
name|IOException
index|[]
name|exc
init|=
operator|new
name|IOException
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|boolean
index|[]
name|done
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|Thread
name|reader
init|=
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|_in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|response
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|.
name|length
argument_list|()
operator|>=
name|PROMPT
operator|.
name|length
argument_list|()
operator|&&
name|response
operator|.
name|substring
argument_list|(
name|response
operator|.
name|length
argument_list|()
operator|-
name|PROMPT
operator|.
name|length
argument_list|()
argument_list|,
name|response
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|PROMPT
argument_list|)
condition|)
block|{
name|response
operator|.
name|setLength
argument_list|(
name|response
operator|.
name|length
argument_list|()
operator|-
name|PROMPT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|_errors
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|sendErrorAsResponse
condition|)
block|{
name|response
operator|.
name|append
argument_list|(
name|_errors
argument_list|)
expr_stmt|;
name|_errors
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|chomp
argument_list|(
name|_errors
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|chomp
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|done
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|exc
index|[
literal|0
index|]
operator|=
name|e
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|reader
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|reader
operator|.
name|join
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
block|}
if|if
condition|(
name|exc
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exc
index|[
literal|0
index|]
throw|;
block|}
if|else if
condition|(
operator|!
name|done
index|[
literal|0
index|]
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"connection timeout to "
operator|+
name|getHost
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
if|if
condition|(
literal|"Not connected."
operator|.
name|equals
argument_list|(
name|response
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"vsftp connection to "
operator|+
name|getHost
argument_list|()
operator|+
literal|" reset"
argument_list|)
expr_stmt|;
name|closeConnection
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"not connected to "
operator|+
name|getHost
argument_list|()
argument_list|)
throw|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"received response '"
operator|+
name|response
operator|+
literal|"' from "
operator|+
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|response
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|protected
specifier|synchronized
name|void
name|ensureConnectionOpened
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|_in
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"connecting to "
operator|+
name|getUsername
argument_list|()
operator|+
literal|"@"
operator|+
name|getHost
argument_list|()
operator|+
literal|"... "
argument_list|)
expr_stmt|;
name|String
name|connectionCommand
init|=
name|getConnectionCommand
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"launching '"
operator|+
name|connectionCommand
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|Process
name|p
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|connectionCommand
argument_list|)
decl_stmt|;
name|_in
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|_err
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|)
expr_stmt|;
name|_out
operator|=
operator|new
name|PrintWriter
argument_list|(
name|p
operator|.
name|getOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|Ivy
name|ivy
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
decl_stmt|;
name|ivy
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
name|disconnect
argument_list|()
expr_stmt|;
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
block|}
block|}
argument_list|,
name|EndResolveEvent
operator|.
name|NAME
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
literal|"err-stream-reader"
argument_list|)
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|c
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|c
operator|=
name|_err
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|_errors
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
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|readResponse
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// waits for first prompt
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|closeConnection
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"impossible to connect to "
operator|+
name|getUsername
argument_list|()
operator|+
literal|"@"
operator|+
name|getHost
argument_list|()
operator|+
literal|" using "
operator|+
name|getAuthentication
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"connected to "
operator|+
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|disconnect
parameter_list|()
block|{
if|if
condition|(
name|_in
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"disconnecting from "
operator|+
name|getHost
argument_list|()
operator|+
literal|"... "
argument_list|)
expr_stmt|;
try|try
block|{
name|sendCommand
argument_list|(
literal|"exit"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
block|}
finally|finally
block|{
name|closeConnection
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"disconnected of "
operator|+
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|closeConnection
parameter_list|()
block|{
try|try
block|{
name|_in
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
block|}
try|try
block|{
name|_err
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
block|}
name|_out
operator|.
name|close
argument_list|()
expr_stmt|;
name|_in
operator|=
literal|null
expr_stmt|;
name|_out
operator|=
literal|null
expr_stmt|;
name|_err
operator|=
literal|null
expr_stmt|;
block|}
comment|/** 	 * Parses a ls -l line and transforms it in a resource 	 * @param file 	 * @param responseLine 	 * @return 	 */
specifier|protected
name|Resource
name|lslToResource
parameter_list|(
name|String
name|file
parameter_list|,
name|String
name|responseLine
parameter_list|)
block|{
if|if
condition|(
name|responseLine
operator|==
literal|null
operator|||
name|responseLine
operator|.
name|startsWith
argument_list|(
literal|"ls"
argument_list|)
condition|)
block|{
return|return
operator|new
name|VsftpResource
argument_list|(
name|this
argument_list|,
name|file
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
else|else
block|{
name|String
index|[]
name|parts
init|=
name|responseLine
operator|.
name|split
argument_list|(
literal|"\\s+"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|9
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"unrecognized ls format: "
operator|+
name|responseLine
argument_list|)
expr_stmt|;
return|return
operator|new
name|VsftpResource
argument_list|(
name|this
argument_list|,
name|file
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
else|else
block|{
try|try
block|{
name|long
name|contentLength
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
name|String
name|date
init|=
name|parts
index|[
literal|4
index|]
operator|+
literal|" "
operator|+
name|parts
index|[
literal|5
index|]
operator|+
literal|" "
operator|+
name|parts
index|[
literal|6
index|]
operator|+
literal|" "
operator|+
name|parts
index|[
literal|7
index|]
decl_stmt|;
return|return
operator|new
name|VsftpResource
argument_list|(
name|this
argument_list|,
name|file
argument_list|,
literal|true
argument_list|,
name|contentLength
argument_list|,
name|FORMAT
operator|.
name|parse
argument_list|(
name|date
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"impossible to parse server response: "
operator|+
name|responseLine
operator|+
literal|": "
operator|+
name|ex
argument_list|)
expr_stmt|;
return|return
operator|new
name|VsftpResource
argument_list|(
name|this
argument_list|,
name|file
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
block|}
block|}
block|}
specifier|protected
name|String
name|getConnectionCommand
parameter_list|()
block|{
return|return
literal|"vsftp -noprompt -auth "
operator|+
name|_authentication
operator|+
literal|" "
operator|+
name|_username
operator|+
literal|"@"
operator|+
name|_host
return|;
block|}
specifier|protected
name|Pattern
name|getExpectedDownloadMessage
parameter_list|(
name|String
name|source
parameter_list|,
name|File
name|to
parameter_list|)
block|{
return|return
name|Pattern
operator|.
name|compile
argument_list|(
literal|"Downloading "
operator|+
name|to
operator|.
name|getName
argument_list|()
operator|+
literal|" from [^\\s]+"
argument_list|)
return|;
block|}
specifier|protected
name|Pattern
name|getExpectedRemoveMessage
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
return|return
name|Pattern
operator|.
name|compile
argument_list|(
literal|"Removing [^\\s]+"
argument_list|)
return|;
block|}
specifier|protected
name|Pattern
name|getExpectedUploadMessage
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|to
parameter_list|)
block|{
return|return
name|Pattern
operator|.
name|compile
argument_list|(
literal|"Uploading "
operator|+
name|source
operator|.
name|getName
argument_list|()
operator|+
literal|" to [^\\s]+"
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAuthentication
parameter_list|()
block|{
return|return
name|_authentication
return|;
block|}
specifier|public
name|void
name|setAuthentication
parameter_list|(
name|String
name|authentication
parameter_list|)
block|{
name|_authentication
operator|=
name|authentication
expr_stmt|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|_host
return|;
block|}
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|_host
operator|=
name|host
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|_username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|_username
operator|=
name|username
expr_stmt|;
block|}
specifier|private
specifier|static
name|StringBuffer
name|chomp
parameter_list|(
name|StringBuffer
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|str
return|;
block|}
while|while
condition|(
literal|"\n"
operator|.
name|equals
argument_list|(
name|str
operator|.
name|substring
argument_list|(
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|||
literal|"\r"
operator|.
name|equals
argument_list|(
name|str
operator|.
name|substring
argument_list|(
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|str
operator|.
name|setLength
argument_list|(
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|getUsername
argument_list|()
operator|+
literal|"@"
operator|+
name|getHost
argument_list|()
operator|+
literal|" ("
operator|+
name|getAuthentication
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

