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
name|ant
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|DirectoryScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|FileSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Resource
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
name|NoSuchElementException
import|;
end_import

begin_class
class|class
name|EmptyFileSet
extends|extends
name|FileSet
block|{
specifier|private
name|DirectoryScanner
name|ds
init|=
operator|new
name|EmptyDirectoryScanner
argument_list|()
decl_stmt|;
specifier|public
name|Iterator
argument_list|<
name|Resource
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|EmptyIterator
argument_list|<>
argument_list|()
return|;
block|}
specifier|public
name|Object
name|clone
parameter_list|()
block|{
return|return
operator|new
name|EmptyFileSet
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|DirectoryScanner
name|getDirectoryScanner
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
return|return
name|ds
return|;
block|}
specifier|private
specifier|static
class|class
name|EmptyIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|T
name|next
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"EmptyFileSet Iterator"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EmptyFileSet Iterator"
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
class|class
name|EmptyDirectoryScanner
extends|extends
name|DirectoryScanner
block|{
specifier|public
name|String
index|[]
name|getIncludedFiles
parameter_list|()
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
block|}
block|}
end_class

end_unit
