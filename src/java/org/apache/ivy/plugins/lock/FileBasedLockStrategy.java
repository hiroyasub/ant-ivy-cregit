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
name|lock
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
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileLock
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
name|Map
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

begin_class
specifier|public
specifier|abstract
class|class
name|FileBasedLockStrategy
extends|extends
name|AbstractLockStrategy
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SLEEP_TIME
init|=
literal|100
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_TIMEOUT
init|=
literal|2
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_FILE
init|=
literal|"create-file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOCK_FILE
init|=
literal|"lock-file"
decl_stmt|;
specifier|private
name|String
name|fileLockMethod
init|=
name|CREATE_FILE
decl_stmt|;
specifier|private
name|long
name|timeout
init|=
name|DEFAULT_TIMEOUT
decl_stmt|;
specifier|private
name|Map
name|locks
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|boolean
name|acquireLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"acquiring lock on "
operator|+
name|file
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|CREATE_FILE
operator|.
name|equals
argument_list|(
name|fileLockMethod
argument_list|)
condition|)
block|{
do|do
block|{
try|try
block|{
if|if
condition|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
operator|||
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
if|if
condition|(
name|file
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"lock acquired on "
operator|+
name|file
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"file creation failed "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignored
name|Message
operator|.
name|verbose
argument_list|(
literal|"file creation failed due to an exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" ("
operator|+
name|file
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|<
name|timeout
condition|)
do|;
return|return
literal|false
return|;
block|}
if|else if
condition|(
name|LOCK_FILE
operator|.
name|equals
argument_list|(
name|fileLockMethod
argument_list|)
condition|)
block|{
do|do
block|{
try|try
block|{
if|if
condition|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
operator|||
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
name|RandomAccessFile
name|raf
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
decl_stmt|;
name|FileChannel
name|channel
init|=
name|raf
operator|.
name|getChannel
argument_list|()
decl_stmt|;
try|try
block|{
name|FileLock
name|l
init|=
name|channel
operator|.
name|tryLock
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|locks
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"lock acquired on "
operator|+
name|file
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
finally|finally
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignored
name|Message
operator|.
name|verbose
argument_list|(
literal|"file lock failed due to an exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" ("
operator|+
name|file
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|<
name|timeout
condition|)
do|;
return|return
literal|false
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unknown lock method "
operator|+
name|fileLockMethod
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|releaseLock
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
name|CREATE_FILE
operator|.
name|equals
argument_list|(
name|fileLockMethod
argument_list|)
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"lock released on "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|LOCK_FILE
operator|.
name|equals
argument_list|(
name|fileLockMethod
argument_list|)
condition|)
block|{
name|FileLock
name|l
init|=
operator|(
name|FileLock
operator|)
name|locks
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"file not previously locked: "
operator|+
name|file
argument_list|)
throw|;
block|}
try|try
block|{
name|l
operator|.
name|release
argument_list|()
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
literal|"problem while releasing lock on "
operator|+
name|file
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unknown lock method "
operator|+
name|fileLockMethod
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

