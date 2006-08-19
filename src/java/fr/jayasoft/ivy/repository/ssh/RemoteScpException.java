begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * @author ace  * $Id:$  */
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
name|ssh
package|;
end_package

begin_comment
comment|/**  * This exception will be used for Remote SCP Exceptions (failures on the target system, no connetion probs)   */
end_comment

begin_class
specifier|public
class|class
name|RemoteScpException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3107198655563736600L
decl_stmt|;
specifier|public
name|RemoteScpException
parameter_list|()
block|{
block|}
comment|/**      * @param message      */
specifier|public
name|RemoteScpException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param cause      */
specifier|public
name|RemoteScpException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param message      * @param cause      */
specifier|public
name|RemoteScpException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

