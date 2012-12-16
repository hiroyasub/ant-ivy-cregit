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
name|LinkedHashSet
import|;
end_import

begin_class
class|class
name|DeleteOnExitHook
block|{
static|static
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runHook
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|LinkedHashSet
name|files
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
specifier|private
name|DeleteOnExitHook
parameter_list|()
block|{
block|}
specifier|static
specifier|synchronized
name|void
name|add
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|files
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|synchronized
name|void
name|remove
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|files
operator|.
name|remove
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|synchronized
name|void
name|runHook
parameter_list|()
block|{
name|Iterator
name|itr
init|=
name|files
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
operator|(
operator|(
name|File
operator|)
name|itr
operator|.
name|next
argument_list|()
operator|)
operator|.
name|delete
argument_list|()
expr_stmt|;
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

