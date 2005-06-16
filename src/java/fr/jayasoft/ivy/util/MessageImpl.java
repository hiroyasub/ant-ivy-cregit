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
name|util
package|;
end_package

begin_interface
specifier|public
interface|interface
name|MessageImpl
block|{
specifier|public
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
function_decl|;
specifier|public
name|void
name|progress
parameter_list|()
function_decl|;
specifier|public
name|void
name|endProgress
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

