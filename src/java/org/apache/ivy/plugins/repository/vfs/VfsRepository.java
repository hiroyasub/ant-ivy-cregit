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
name|IOException
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
name|Arrays
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
name|commons
operator|.
name|vfs
operator|.
name|impl
operator|.
name|StandardFileSystemManager
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
name|CopyProgressListener
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
name|Message
import|;
end_import

begin_comment
comment|/**  * Implementation of a VFS repository  *   * @author glen  * @author Matt Inger  * @author Stephen Nesbitt  *   */
end_comment

begin_class
specifier|public
class|class
name|VfsRepository
extends|extends
name|AbstractRepository
block|{
comment|/** 	 * Name of the resource defining the Ivy VFS Repo configuration. 	 */
specifier|private
specifier|static
specifier|final
name|String
name|IVY_VFS_CONFIG
init|=
literal|"ivy_vfs.xml"
decl_stmt|;
specifier|private
name|StandardFileSystemManager
name|_manager
init|=
literal|null
decl_stmt|;
specifier|private
specifier|final
name|CopyProgressListener
name|_progress
init|=
operator|new
name|RepositoryCopyProgressListener
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|/** 	 * Create a new Ivy VFS Repository Instance 	 * 	 */
specifier|public
name|VfsRepository
parameter_list|()
block|{
block|}
specifier|private
name|FileSystemManager
name|getVFSManager
parameter_list|()
throws|throws
name|IOException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|_manager
operator|==
literal|null
condition|)
block|{
name|_manager
operator|=
name|createVFSManager
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|_manager
return|;
block|}
specifier|private
name|StandardFileSystemManager
name|createVFSManager
parameter_list|()
throws|throws
name|IOException
block|{
name|StandardFileSystemManager
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|/* 			 * The DefaultFileSystemManager gets its configuration from the jakarta-vfs-common 			 * implementation which includes the res and tmp schemes which are of no use to use here. 			 * Using StandardFileSystemManager lets us specify which schemes to support as well as  			 * providing a mechanism to change this support without recompilation. 			 */
name|result
operator|=
operator|new
name|StandardFileSystemManager
argument_list|()
expr_stmt|;
name|result
operator|.
name|setConfiguration
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|IVY_VFS_CONFIG
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// Generate and print a list of available schemes
name|Message
operator|.
name|verbose
argument_list|(
literal|"Available VFS schemes..."
argument_list|)
expr_stmt|;
name|String
index|[]
name|schemes
init|=
name|result
operator|.
name|getSchemes
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|schemes
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|schemes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"VFS Supported Scheme: "
operator|+
name|schemes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileSystemException
name|e
parameter_list|)
block|{
comment|/* 			 * If our attempt to initialize a VFS Repository fails we log the failure 			 * but continue on. Given that an Ivy instance may involve numerous 			 * different repository types, it seems overly cautious to throw a runtime 			 * exception on the initialization failure of just one repository type. 			 */
name|Message
operator|.
name|error
argument_list|(
literal|"Unable to initialize VFS repository manager!"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|error
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
name|IOException
name|error
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
decl_stmt|;
name|error
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|error
throw|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|void
name|finalize
parameter_list|()
block|{
if|if
condition|(
name|_manager
operator|!=
literal|null
condition|)
block|{
name|_manager
operator|.
name|close
argument_list|()
expr_stmt|;
name|_manager
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/** 	 * Get a VfsResource 	 *  	 * @param source a<code>String</code> identifying a VFS Resource 	 * @throws<code>IOException</code> on failure 	 * @see "Supported File Systems in the jakarta-commons-vfs documentation" 	 */
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|vfsURI
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|VfsResource
argument_list|(
name|vfsURI
argument_list|,
name|getVFSManager
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Transfer a VFS Resource from the repository to the local file system. 	 *  	 * @param srcVfsURI a<code>String</code> identifying the VFS resource to be fetched 	 * @param destination a<code>File</code> identifying the destination file 	 * @throws<code>IOException</code> on failure 	 * @see "Supported File Systems in the jakarta-commons-vfs documentation" 	 */
specifier|public
name|void
name|get
parameter_list|(
name|String
name|srcVfsURI
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|IOException
block|{
name|VfsResource
name|src
init|=
operator|new
name|VfsResource
argument_list|(
name|srcVfsURI
argument_list|,
name|getVFSManager
argument_list|()
argument_list|)
decl_stmt|;
name|fireTransferInitiated
argument_list|(
name|src
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_GET
argument_list|)
expr_stmt|;
try|try
block|{
name|FileContent
name|content
init|=
name|src
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid vfs uri "
operator|+
name|srcVfsURI
operator|+
literal|": no content found"
argument_list|)
throw|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
name|content
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|destination
argument_list|,
name|_progress
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
block|}
comment|/** 	 * Return a listing of the contents of a parent directory. Listing is a set 	 * of strings representing VFS URIs. 	 *  	 * @param vfsURI providing identifying a VFS provided resource 	 * @throws IOException on failure. 	 * @see "Supported File Systems in the jakarta-commons-vfs documentation" 	 */
specifier|public
name|List
name|list
parameter_list|(
name|String
name|vfsURI
parameter_list|)
throws|throws
name|IOException
block|{
name|ArrayList
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"list called for URI"
operator|+
name|vfsURI
argument_list|)
expr_stmt|;
name|FileObject
name|resourceImpl
init|=
name|getVFSManager
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|vfsURI
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"resourceImpl="
operator|+
name|resourceImpl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"resourceImpl.exists()"
operator|+
name|resourceImpl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"resourceImpl.getType()"
operator|+
name|resourceImpl
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"FileType.FOLDER"
operator|+
name|FileType
operator|.
name|FOLDER
argument_list|)
expr_stmt|;
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
name|FileObject
index|[]
name|children
init|=
name|resourceImpl
operator|.
name|getChildren
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
name|children
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|FileObject
name|child
init|=
name|children
index|[
name|i
index|]
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"child "
operator|+
name|i
operator|+
name|child
operator|.
name|getName
argument_list|()
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|VfsResource
operator|.
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
return|return
name|list
return|;
block|}
comment|/** 	 * Transfer an Ivy resource to a VFS repository 	 *  	 * @param source a<code>File</code> indentifying the local file to transfer to the repository 	 * @param vfsURI a<code>String</code> identifying the destination VFS Resource. 	 * @param overwrite whether to overwrite an existing resource. 	 * @throws<code>IOException</code> on failure. 	 * @see "Supported File Systems in the jakarta-commons-vfs documentation" 	 *  	 */
specifier|public
name|void
name|put
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|vfsURI
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|VfsResource
name|dest
init|=
operator|new
name|VfsResource
argument_list|(
name|vfsURI
argument_list|,
name|getVFSManager
argument_list|()
argument_list|)
decl_stmt|;
name|fireTransferInitiated
argument_list|(
name|dest
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_PUT
argument_list|)
expr_stmt|;
if|if
condition|(
name|dest
operator|.
name|physicallyExists
argument_list|()
operator|&&
operator|!
name|overwrite
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot copy. Destination file: "
operator|+
name|dest
operator|.
name|getName
argument_list|()
operator|+
literal|" exists and overwrite not set."
argument_list|)
throw|;
block|}
if|if
condition|(
name|dest
operator|.
name|getContent
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid vfs uri "
operator|+
name|vfsURI
operator|+
literal|" to put data to: resource has no content"
argument_list|)
throw|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|source
argument_list|)
argument_list|,
name|dest
operator|.
name|getContent
argument_list|()
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|_progress
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

