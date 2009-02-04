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
name|repository
operator|.
name|url
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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|HashMap
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
name|ListIterator
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
name|plugins
operator|.
name|repository
operator|.
name|AbstractRepository
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
name|plugins
operator|.
name|repository
operator|.
name|RepositoryCopyProgressListener
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
name|plugins
operator|.
name|repository
operator|.
name|Resource
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
name|plugins
operator|.
name|repository
operator|.
name|TransferEvent
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
name|FileUtil
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
name|url
operator|.
name|ApacheURLLister
import|;
end_import

begin_class
specifier|public
class|class
name|URLRepository
extends|extends
name|AbstractRepository
block|{
specifier|private
name|RepositoryCopyProgressListener
name|progress
init|=
operator|new
name|RepositoryCopyProgressListener
argument_list|(
name|this
argument_list|)
decl_stmt|;
specifier|private
name|Map
name|resourcesCache
init|=
operator|new
name|HashMap
argument_list|()
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
name|Resource
name|res
init|=
operator|(
name|Resource
operator|)
name|resourcesCache
operator|.
name|get
argument_list|(
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
name|res
operator|=
operator|new
name|URLResource
argument_list|(
operator|new
name|URL
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|resourcesCache
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
return|return
name|res
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
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|source
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_GET
argument_list|)
expr_stmt|;
try|try
block|{
name|Resource
name|res
init|=
name|getResource
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|long
name|totalLength
init|=
name|res
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|totalLength
operator|>
literal|0
condition|)
block|{
name|progress
operator|.
name|setTotalLength
argument_list|(
operator|new
name|Long
argument_list|(
name|totalLength
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
operator|new
name|URL
argument_list|(
name|source
argument_list|)
argument_list|,
name|destination
argument_list|,
name|progress
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|fireTransferError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|fireTransferError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
finally|finally
block|{
name|progress
operator|.
name|setTotalLength
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
operator|!
name|overwrite
operator|&&
name|getResource
argument_list|(
name|destination
argument_list|)
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"destination file exists and overwrite == false"
argument_list|)
throw|;
block|}
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|destination
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_PUT
argument_list|)
expr_stmt|;
try|try
block|{
name|long
name|totalLength
init|=
name|source
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|totalLength
operator|>
literal|0
condition|)
block|{
name|progress
operator|.
name|setTotalLength
argument_list|(
operator|new
name|Long
argument_list|(
name|totalLength
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
name|source
argument_list|,
operator|new
name|URL
argument_list|(
name|destination
argument_list|)
argument_list|,
name|progress
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|fireTransferError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|fireTransferError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
finally|finally
block|{
name|progress
operator|.
name|setTotalLength
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ApacheURLLister
name|lister
init|=
operator|new
name|ApacheURLLister
argument_list|()
decl_stmt|;
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
name|parent
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|List
name|urls
init|=
name|lister
operator|.
name|listAll
argument_list|(
operator|new
name|URL
argument_list|(
name|parent
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|urls
operator|!=
literal|null
condition|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|urls
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|urls
operator|.
name|listIterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
if|else if
condition|(
name|parent
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
name|String
name|path
decl_stmt|;
try|try
block|{
name|path
operator|=
operator|new
name|URI
argument_list|(
name|parent
argument_list|)
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|IOException
name|ioe
init|=
operator|new
name|IOException
argument_list|(
literal|"Couldn't list content of '"
operator|+
name|parent
operator|+
literal|"'"
argument_list|)
decl_stmt|;
name|ioe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ioe
throw|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
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
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
operator|new
name|URL
argument_list|(
name|parent
argument_list|)
else|:
operator|new
name|URL
argument_list|(
name|parent
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
operator|.
name|toExternalForm
argument_list|()
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

