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

begin_comment
comment|/**  * Memory related utilities.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MemoryUtil
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SAMPLING_SIZE
init|=
literal|100
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|SLEEP_TIME
init|=
literal|100
decl_stmt|;
specifier|private
name|MemoryUtil
parameter_list|()
block|{
block|}
comment|/**      * Returns the approximate size of a default instance of the given class.      *       * @param clazz      *            the class to evaluate.      * @return the estimated size of instance, in bytes.      */
specifier|public
specifier|static
name|long
name|sizeOf
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|long
name|size
init|=
literal|0
decl_stmt|;
name|Object
index|[]
name|objects
init|=
operator|new
name|Object
index|[
name|SAMPLING_SIZE
index|]
decl_stmt|;
try|try
block|{
name|clazz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|long
name|startingMemoryUse
init|=
name|getUsedMemory
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
name|objects
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|objects
index|[
name|i
index|]
operator|=
name|clazz
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
name|long
name|endingMemoryUse
init|=
name|getUsedMemory
argument_list|()
decl_stmt|;
name|float
name|approxSize
init|=
operator|(
name|endingMemoryUse
operator|-
name|startingMemoryUse
operator|)
operator|/
operator|(
name|float
operator|)
name|objects
operator|.
name|length
decl_stmt|;
name|size
operator|=
name|Math
operator|.
name|round
argument_list|(
name|approxSize
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WARNING:couldn't instantiate"
operator|+
name|clazz
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
comment|/**      * Returns the currently used memory, after calling garbage collector and waiting enough to get      * maximal chance it is actually called. But since {@link Runtime#gc()} is only advisory,      * results returned by this method should be treated as rough approximation only.      *       * @return the currently used memory, in bytes.      */
specifier|public
specifier|static
name|long
name|getUsedMemory
parameter_list|()
block|{
name|gc
argument_list|()
expr_stmt|;
name|long
name|totalMemory
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|totalMemory
argument_list|()
decl_stmt|;
name|gc
argument_list|()
expr_stmt|;
name|long
name|freeMemory
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|freeMemory
argument_list|()
decl_stmt|;
name|long
name|usedMemory
init|=
name|totalMemory
operator|-
name|freeMemory
decl_stmt|;
return|return
name|usedMemory
return|;
block|}
specifier|private
specifier|static
name|void
name|gc
parameter_list|()
block|{
try|try
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_TIME
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sizeOf
argument_list|(
name|Class
operator|.
name|forName
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

