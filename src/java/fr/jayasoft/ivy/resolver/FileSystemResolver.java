begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|resolver
package|;
end_package

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
name|file
operator|.
name|FileRepository
import|;
end_import

begin_comment
comment|/**  * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|FileSystemResolver
extends|extends
name|RepositoryResolver
block|{
specifier|public
name|FileSystemResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|FileRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"file"
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|getFileRepository
argument_list|()
operator|.
name|isLocal
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLocal
parameter_list|(
name|boolean
name|local
parameter_list|)
block|{
name|getFileRepository
argument_list|()
operator|.
name|setLocal
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
specifier|private
name|FileRepository
name|getFileRepository
parameter_list|()
block|{
return|return
operator|(
name|FileRepository
operator|)
name|getRepository
argument_list|()
return|;
block|}
block|}
end_class

end_unit

