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
name|resolver
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

begin_class
specifier|public
class|class
name|FileURLLister
implements|implements
name|URLLister
block|{
specifier|private
name|File
name|_basedir
decl_stmt|;
specifier|public
name|FileURLLister
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FileURLLister
parameter_list|(
name|File
name|baseDir
parameter_list|)
block|{
name|_basedir
operator|=
name|baseDir
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
return|;
block|}
specifier|public
name|List
name|listAll
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|path
init|=
name|url
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|File
name|file
init|=
name|_basedir
operator|==
literal|null
condition|?
operator|new
name|File
argument_list|(
name|path
argument_list|)
else|:
operator|new
name|File
argument_list|(
name|_basedir
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|files
operator|.
name|length
argument_list|)
decl_stmt|;
name|URL
name|context
init|=
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|url
else|:
operator|new
name|URL
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
operator|+
literal|"/"
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
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|URL
argument_list|(
name|context
argument_list|,
name|files
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"file lister"
return|;
block|}
block|}
end_class

end_unit
