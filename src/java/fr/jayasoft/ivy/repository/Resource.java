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
package|;
end_package

begin_interface
specifier|public
interface|interface
name|Resource
block|{
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
specifier|public
name|long
name|getLastModified
parameter_list|()
function_decl|;
specifier|public
name|long
name|getContentLength
parameter_list|()
function_decl|;
specifier|public
name|boolean
name|exists
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

