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
name|StringWriter
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
name|Collections
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
name|url
operator|.
name|URLHandlerRegistry
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|FileUtil
block|{
comment|// tried some other values with empty files... seems to be the best one (512 * 1024 is very bad)
comment|// 8 * 1024 is also the size used by ant in its FileUtils... maybe they've done more study about it ;-)
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|8
operator|*
literal|1024
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|EMPTY_BUFFER
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
specifier|public
specifier|static
name|void
name|symlink
parameter_list|(
name|File
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|dest
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"impossible to copy: destination is not a file: "
operator|+
name|dest
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|overwrite
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|dest
operator|+
literal|" already exists, nothing done"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|dest
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|Runtime
name|runtime
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
decl_stmt|;
name|Process
name|process
init|=
name|runtime
operator|.
name|exec
argument_list|(
literal|"ln"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"-s"
block|,
literal|"-f"
block|,
name|src
operator|.
name|getAbsolutePath
argument_list|()
block|,
name|dest
operator|.
name|getPath
argument_list|()
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|process
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|InputStream
name|errorStream
init|=
name|process
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
name|InputStreamReader
name|isr
init|=
operator|new
name|InputStreamReader
argument_list|(
name|errorStream
argument_list|)
decl_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
name|isr
argument_list|)
decl_stmt|;
name|StringBuffer
name|error
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|line
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
name|error
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|error
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"error symlinking "
operator|+
name|src
operator|+
literal|" to "
operator|+
name|dest
operator|+
literal|":\n"
operator|+
name|error
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|x
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"symlink failed; falling back to copy"
argument_list|)
expr_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|x
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|copy
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|x
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|File
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
name|copy
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|File
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|dest
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"impossible to copy: destination is not a file: "
operator|+
name|dest
argument_list|)
throw|;
block|}
if|if
condition|(
name|overwrite
condition|)
block|{
if|if
condition|(
operator|!
name|dest
operator|.
name|canWrite
argument_list|()
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|// if dest is writable, the copy will overwrite it without requiring a delete
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|dest
operator|+
literal|" already exists, nothing done"
argument_list|)
expr_stmt|;
block|}
block|}
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|src
argument_list|)
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|long
name|srcLen
init|=
name|src
operator|.
name|length
argument_list|()
decl_stmt|;
name|long
name|destLen
init|=
name|dest
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcLen
operator|!=
name|destLen
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"size of source file "
operator|+
name|src
operator|.
name|toString
argument_list|()
operator|+
literal|"("
operator|+
name|srcLen
operator|+
literal|") differs from size of dest file "
operator|+
name|dest
operator|.
name|toString
argument_list|()
operator|+
literal|"("
operator|+
name|destLen
operator|+
literal|") - please retry"
argument_list|)
throw|;
block|}
name|dest
operator|.
name|setLastModified
argument_list|(
name|src
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copy
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
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|download
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
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
if|if
condition|(
name|dest
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|copy
argument_list|(
name|src
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|dest
argument_list|)
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|src
parameter_list|,
name|OutputStream
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
name|CopyProgressEvent
name|evt
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|evt
operator|=
operator|new
name|CopyProgressEvent
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|c
decl_stmt|;
name|long
name|total
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|l
operator|.
name|start
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
operator|(
name|c
operator|=
name|src
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|isInterrupted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"transfer interrupted"
argument_list|)
throw|;
block|}
name|dest
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|total
operator|+=
name|c
expr_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|l
operator|.
name|progress
argument_list|(
name|evt
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
name|c
argument_list|,
name|total
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|evt
operator|.
name|update
argument_list|(
name|EMPTY_BUFFER
argument_list|,
literal|0
argument_list|,
name|total
argument_list|)
expr_stmt|;
comment|// close the streams
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
name|dest
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
try|try
block|{
name|dest
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|l
operator|.
name|end
argument_list|(
name|evt
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|String
name|readEntirely
parameter_list|(
name|BufferedReader
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
name|line
init|=
name|in
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|line
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|concat
parameter_list|(
name|String
name|dir
parameter_list|,
name|String
name|file
parameter_list|)
block|{
return|return
name|dir
operator|+
literal|"/"
operator|+
name|file
return|;
block|}
specifier|public
specifier|static
name|void
name|forceDelete
parameter_list|(
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|sub
init|=
name|f
operator|.
name|listFiles
argument_list|()
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
name|sub
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|forceDelete
argument_list|(
name|sub
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns a list of Files composed of all directories being      * parent of file and child of root + file and root themselves.      *       * Example:      * getPathFiles(new File("test"), new File("test/dir1/dir2/file.txt"))      * => {new File("test/dir1"), new File("test/dir1/dir2"), new File("test/dir1/dir2/file.txt") }      *       * Note that if root is not an ancester of file, or if root is null, all directories from the      * file system root will be returned.       */
specifier|public
specifier|static
name|List
name|getPathFiles
parameter_list|(
name|File
name|root
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|file
operator|!=
literal|null
operator|&&
operator|!
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|equals
argument_list|(
name|root
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|file
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
comment|/** 	 * Returns a collection of all Files being contained in the given directory, 	 * recursively, including directories. 	 * @param dir 	 * @return 	 */
specifier|public
specifier|static
name|Collection
name|listAll
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
return|return
name|listAll
argument_list|(
name|dir
argument_list|,
operator|new
name|ArrayList
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Collection
name|listAll
parameter_list|(
name|File
name|file
parameter_list|,
name|Collection
name|list
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|file
operator|.
name|listFiles
argument_list|()
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
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|listAll
argument_list|(
name|files
index|[
name|i
index|]
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

