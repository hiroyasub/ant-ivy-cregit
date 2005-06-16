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
name|repository
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
import|;
end_import

begin_comment
comment|/**  * Listen to repository transfer  */
end_comment

begin_interface
specifier|public
interface|interface
name|TransferListener
extends|extends
name|EventListener
block|{
name|void
name|transferProgress
parameter_list|(
name|TransferEvent
name|evt
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

