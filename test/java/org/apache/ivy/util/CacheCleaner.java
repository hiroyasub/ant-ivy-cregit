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
name|File
import|;
end_import

begin_class
specifier|public
class|class
name|CacheCleaner
block|{
comment|/** Delete the directory and all it contains.      * Previously, we used the ant delete task, but it occasionaly failed (access denied)      * on my machine for unknown reason.  The next code seems to work better (but I don't      * see the difference with ant task...       **/
specifier|public
specifier|static
name|void
name|deleteDir
parameter_list|(
name|File
name|toDelete
parameter_list|)
block|{
if|if
condition|(
name|toDelete
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"delete file : "
operator|+
name|toDelete
argument_list|)
expr_stmt|;
name|toDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|toDelete
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|childs
init|=
name|toDelete
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
name|childs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|deleteDir
argument_list|(
name|childs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"delete dir :"
operator|+
name|toDelete
argument_list|)
expr_stmt|;
name|toDelete
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|toDelete
operator|.
name|exists
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"!!! I just deleted '"
operator|+
name|toDelete
operator|+
literal|"', and it is still there !!!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

