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
name|vfs
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileSystemException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileSystemManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs
operator|.
name|FileType
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
name|resolver
operator|.
name|VfsResolver
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

begin_comment
comment|/**  * VFS implementation of the Resource interface  */
end_comment

begin_class
specifier|public
class|class
name|VfsResource
implements|implements
name|Resource
block|{
specifier|private
name|String
name|vfsURI
decl_stmt|;
specifier|private
name|FileSystemManager
name|fsManager
decl_stmt|;
specifier|private
specifier|transient
name|boolean
name|init
init|=
literal|false
decl_stmt|;
specifier|private
specifier|transient
name|boolean
name|exists
decl_stmt|;
specifier|private
specifier|transient
name|long
name|lastModified
decl_stmt|;
specifier|private
specifier|transient
name|long
name|contentLength
decl_stmt|;
specifier|private
specifier|transient
name|FileContent
name|content
init|=
literal|null
decl_stmt|;
specifier|private
specifier|transient
name|FileObject
name|resourceImpl
decl_stmt|;
comment|// Constructor
specifier|public
name|VfsResource
parameter_list|(
name|String
name|vfsURI
parameter_list|,
name|FileSystemManager
name|fsManager
parameter_list|)
block|{
name|this
operator|.
name|vfsURI
operator|=
name|vfsURI
expr_stmt|;
name|this
operator|.
name|fsManager
operator|=
name|fsManager
expr_stmt|;
name|this
operator|.
name|init
operator|=
literal|false
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
operator|!
name|init
condition|)
block|{
try|try
block|{
name|resourceImpl
operator|=
name|fsManager
operator|.
name|resolveFile
argument_list|(
name|vfsURI
argument_list|)
expr_stmt|;
name|content
operator|=
name|resourceImpl
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|exists
operator|=
name|resourceImpl
operator|.
name|exists
argument_list|()
expr_stmt|;
name|lastModified
operator|=
name|content
operator|.
name|getLastModifiedTime
argument_list|()
expr_stmt|;
name|contentLength
operator|=
name|content
operator|.
name|getSize
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileSystemException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exists
operator|=
literal|false
expr_stmt|;
name|lastModified
operator|=
literal|0
expr_stmt|;
name|contentLength
operator|=
literal|0
expr_stmt|;
block|}
name|init
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**      * Get a list of direct descendants of the given resource. Note that attempts to get a list of      * children does<em>not</em> result in an error. Instead an error message is      * logged and an empty ArrayList returned.      *      * @return A<code>ArrayList</code> of VFSResources      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getChildren
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|(
name|resourceImpl
operator|!=
literal|null
operator|)
operator|&&
name|resourceImpl
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|resourceImpl
operator|.
name|getType
argument_list|()
operator|==
name|FileType
operator|.
name|FOLDER
operator|)
condition|)
block|{
for|for
control|(
name|FileObject
name|child
range|:
name|resourceImpl
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|normalize
argument_list|(
name|child
operator|.
name|getName
argument_list|()
operator|.
name|getURI
argument_list|()
argument_list|)
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
name|Message
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|public
name|FileContent
name|getContent
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|content
return|;
block|}
comment|/**      * Get the name of the resource.      *      * @return a<code>String</code> representing the Resource URL.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|normalize
argument_list|(
name|vfsURI
argument_list|)
return|;
block|}
specifier|public
name|Resource
name|clone
parameter_list|(
name|String
name|cloneName
parameter_list|)
block|{
return|return
operator|new
name|VfsResource
argument_list|(
name|cloneName
argument_list|,
name|fsManager
argument_list|)
return|;
block|}
comment|/**      * The VFS FileName getURI method seems to have a bug in it where file: URIs will have 4 forward      * slashes instead of 3.      *      * @param vfsURI ditto      * @return a normalized String representing the VFS URI      */
specifier|public
specifier|static
name|String
name|normalize
parameter_list|(
name|String
name|vfsURI
parameter_list|)
block|{
if|if
condition|(
name|vfsURI
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
if|if
condition|(
name|vfsURI
operator|.
name|startsWith
argument_list|(
literal|"file:////"
argument_list|)
condition|)
block|{
name|vfsURI
operator|=
name|vfsURI
operator|.
name|replaceFirst
argument_list|(
literal|"////"
argument_list|,
literal|"///"
argument_list|)
expr_stmt|;
block|}
return|return
name|vfsURI
return|;
block|}
comment|/**      * Get the last modification time of the resource.      *      * @return a<code>long</code> indicating last modified time.      */
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|lastModified
return|;
block|}
comment|/**      * Get the size of the resource      *      * @return a<code>long</code> representing the size of the resource (in bytes).      */
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|contentLength
return|;
block|}
comment|/**      * Flag indicating whether a resource is available for querying      *      * @return<code>true</code> if the resource is available for querying,<code>false</code>      *         otherwise.      */
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
return|return
name|exists
return|;
block|}
comment|/**      * Return a flag indicating whether a provided VFS resource physically exists      *      * @return<code>true</code> if the resource physically exists,<code>false</code> otherwise.      */
specifier|public
name|boolean
name|physicallyExists
parameter_list|()
block|{
comment|// TODO: there is no need for this method anymore, replace it by calling exists();
name|init
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|resourceImpl
operator|.
name|exists
argument_list|()
return|;
comment|// originally I only checked for a FileSystemException. I expanded it to
comment|// include all exceptions when I found it would throw a NPE exception when the query was
comment|// run on ill-formed VFS URI.
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Fail to check the existence of the resource "
operator|+
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|VfsResolver
operator|.
name|prepareForDisplay
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getContent
argument_list|()
operator|.
name|getInputStream
argument_list|()
return|;
block|}
block|}
end_class

end_unit

