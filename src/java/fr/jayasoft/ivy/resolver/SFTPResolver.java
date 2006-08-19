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
name|sftp
operator|.
name|SFTPRepository
import|;
end_import

begin_comment
comment|/**  * This resolver is able to work with any sftp server.  *   * It supports listing and publishing.  *   * The server host should absolutely be set using setHost.  *   * basedir defaults to .  * port default to 22  *   * username and password will be prompted using a dialog box if not set. So if you are in  * an headless environment, provide username and password.  */
end_comment

begin_class
specifier|public
class|class
name|SFTPResolver
extends|extends
name|AbstractSshBasedResolver
block|{
specifier|public
name|SFTPResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|SFTPRepository
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
literal|"sftp"
return|;
block|}
specifier|public
name|SFTPRepository
name|getSFTPRepository
parameter_list|()
block|{
return|return
operator|(
name|SFTPRepository
operator|)
name|getRepository
argument_list|()
return|;
block|}
block|}
end_class

end_unit

