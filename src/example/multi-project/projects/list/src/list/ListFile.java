begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|list
package|;
end_package

begin_import
import|import
name|version
operator|.
name|Version
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
name|ArrayList
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

begin_class
specifier|public
class|class
name|ListFile
block|{
static|static
block|{
name|Version
operator|.
name|register
argument_list|(
literal|"list"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Collection
name|list
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|Collection
name|files
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
return|return
name|list
argument_list|(
name|dir
argument_list|,
name|files
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Collection
name|list
parameter_list|(
name|File
name|file
parameter_list|,
name|Collection
name|files
parameter_list|)
block|{
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
name|f
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
name|f
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|list
argument_list|(
name|f
index|[
name|i
index|]
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|files
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|files
return|;
block|}
block|}
end_class

end_unit

