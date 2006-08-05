begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
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
name|file
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
name|io
operator|.
name|InputStream
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

begin_class
specifier|public
class|class
name|FileResource
implements|implements
name|Resource
block|{
specifier|private
name|File
name|_file
decl_stmt|;
specifier|public
name|FileResource
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|_file
operator|=
name|f
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_file
operator|.
name|getPath
argument_list|()
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
name|FileResource
argument_list|(
operator|new
name|File
argument_list|(
name|cloneName
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|_file
operator|.
name|lastModified
argument_list|()
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|_file
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|_file
operator|.
name|exists
argument_list|()
return|;
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
name|File
name|getFile
parameter_list|()
block|{
return|return
name|_file
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|true
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
operator|new
name|FileInputStream
argument_list|(
name|_file
argument_list|)
return|;
block|}
block|}
end_class

end_unit

