begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved   * VFS implementation of the Resource interface  *   * @author glen  * @author Matt Inger  * @author Stephen Nesbitt  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|repository
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
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
class|class
name|VfsResource
implements|implements
name|Resource
block|{
specifier|private
name|FileContent
name|_content
init|=
literal|null
decl_stmt|;
specifier|private
name|FileObject
name|_resourceImpl
decl_stmt|;
specifier|private
name|boolean
name|_isAvailable
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
name|_isAvailable
operator|=
literal|false
expr_stmt|;
name|_resourceImpl
operator|=
literal|null
expr_stmt|;
try|try
block|{
name|_resourceImpl
operator|=
name|fsManager
operator|.
name|resolveFile
argument_list|(
name|vfsURI
argument_list|)
expr_stmt|;
name|_content
operator|=
name|_resourceImpl
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|_isAvailable
operator|=
name|_content
operator|!=
literal|null
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
name|verbose
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get a list of direct descendents of the given resource.      * Note that attempts to get a list of children does<emphasize>not</emphasize>      * result in an error. Instead an error message is logged and an empty ArrayList returned.      *       * @return A<code>ArrayList</code> of VFSResources      *      */
specifier|public
name|List
name|getChildren
parameter_list|()
block|{
name|ArrayList
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|_resourceImpl
operator|.
name|exists
argument_list|()
operator|&&
name|_resourceImpl
operator|.
name|getType
argument_list|()
operator|==
name|FileType
operator|.
name|FOLDER
condition|)
block|{
name|FileObject
index|[]
name|children
init|=
name|_resourceImpl
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
return|return
name|_content
return|;
block|}
comment|/**      * Get the name of the resource.      *       * @return a<code>String</code> representing the Resource URL.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|exists
argument_list|()
condition|)
block|{
return|return
name|normalize
argument_list|(
name|_resourceImpl
operator|.
name|getName
argument_list|()
operator|.
name|getURI
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
comment|/**      * The VFS FileName getURI method seems to have a bug in it where      * file: URIs will have 4 forward slashes instead of 3.      *       * @param vfsURI      * @return a normalized<class>String</class> representing the VFS URI      */
specifier|private
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
operator|.
name|startsWith
argument_list|(
literal|"file:////"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Normalizing: "
operator|+
name|vfsURI
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Normalized to: "
operator|+
name|vfsURI
argument_list|)
expr_stmt|;
block|}
return|return
name|vfsURI
return|;
block|}
comment|/**      * Get the last modification time of the resource.      *       * @return a<code>long</code> indicating last modified time.      */
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
name|long
name|time
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|time
operator|=
name|_resourceImpl
operator|.
name|getContent
argument_list|()
operator|.
name|getLastModifiedTime
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
name|verbose
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|time
return|;
block|}
comment|/**       * Get the size of the resource       *        * @return a<code>long</code> representing the size of the resource (in bytes).       */
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
name|long
name|size
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|size
operator|=
name|_resourceImpl
operator|.
name|getContent
argument_list|()
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
name|verbose
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|size
return|;
block|}
comment|/**      * Flag indicating whether a resource is available for querying      *       * @return<code>true</code> if the resource is available for querying,      *<code>false</code> otherwise.      */
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|_isAvailable
return|;
block|}
comment|/**      * Return a flag indicating whether a provided VFS resource physically exists      *       * @return<code>true</code> if the resource physically exists,<code>false</code>      *         otherwise.      */
specifier|public
name|boolean
name|physicallyExists
parameter_list|()
block|{
try|try
block|{
return|return
name|_resourceImpl
operator|.
name|exists
argument_list|()
return|;
comment|// originally I only checked for a FileSystemException. I expanded it to
comment|// include all exceptions when I found it would throw a NPE exception when the query was
comment|// run on non-wellformed VFS URI.
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
name|e
operator|.
name|getLocalizedMessage
argument_list|()
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
name|getName
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

