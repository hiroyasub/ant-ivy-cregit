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
name|util
package|;
end_package

begin_comment
comment|/**  * Listen to copy progression  */
end_comment

begin_interface
specifier|public
interface|interface
name|CopyProgressListener
block|{
name|void
name|start
parameter_list|(
name|CopyProgressEvent
name|evt
parameter_list|)
function_decl|;
name|void
name|progress
parameter_list|(
name|CopyProgressEvent
name|evt
parameter_list|)
function_decl|;
name|void
name|end
parameter_list|(
name|CopyProgressEvent
name|evt
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

