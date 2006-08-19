begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
name|ssh
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

begin_comment
comment|/**  * Resource for SSH Ivy Repository  */
end_comment

begin_class
specifier|public
class|class
name|SshResource
implements|implements
name|Resource
block|{
specifier|private
name|boolean
name|resolved
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|uri
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|bExists
init|=
literal|false
decl_stmt|;
specifier|private
name|long
name|len
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|lastModified
init|=
literal|0
decl_stmt|;
specifier|private
name|SshRepository
name|repository
init|=
literal|null
decl_stmt|;
specifier|public
name|SshResource
parameter_list|()
block|{
name|resolved
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|SshResource
parameter_list|(
name|SshRepository
name|repository
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|resolved
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|SshResource
parameter_list|(
name|SshRepository
name|repository
parameter_list|,
name|String
name|uri
parameter_list|,
name|boolean
name|bExists
parameter_list|,
name|long
name|len
parameter_list|,
name|long
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|bExists
operator|=
name|bExists
expr_stmt|;
name|this
operator|.
name|len
operator|=
name|len
expr_stmt|;
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|resolved
operator|=
literal|true
expr_stmt|;
block|}
comment|/* (non-Javadoc) 	 * @see fr.jayasoft.ivy.repository.Resource#exists() 	 */
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resolved
condition|)
name|resolve
argument_list|()
expr_stmt|;
return|return
name|bExists
return|;
block|}
comment|/* (non-Javadoc) 	 * @see fr.jayasoft.ivy.repository.Resource#getContentLength() 	 */
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resolved
condition|)
name|resolve
argument_list|()
expr_stmt|;
return|return
name|len
return|;
block|}
comment|/* (non-Javadoc) 	 * @see fr.jayasoft.ivy.repository.Resource#getLastModified() 	 */
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resolved
condition|)
name|resolve
argument_list|()
expr_stmt|;
return|return
name|lastModified
return|;
block|}
specifier|private
name|void
name|resolve
parameter_list|()
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"SShResource: resolving "
operator|+
name|uri
argument_list|)
expr_stmt|;
name|SshResource
name|res
init|=
name|repository
operator|.
name|resolveResource
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|len
operator|=
name|res
operator|.
name|getContentLength
argument_list|()
expr_stmt|;
name|lastModified
operator|=
name|res
operator|.
name|getLastModified
argument_list|()
expr_stmt|;
name|bExists
operator|=
name|res
operator|.
name|exists
argument_list|()
expr_stmt|;
name|resolved
operator|=
literal|true
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"SShResource: resolved "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc) 	 * @see fr.jayasoft.ivy.repository.Resource#getName() 	 */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"SshResource:"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|")]"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
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
name|SshResource
argument_list|(
name|repository
argument_list|,
name|cloneName
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
name|repository
operator|.
name|openStream
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

