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
name|event
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

begin_interface
specifier|public
interface|interface
name|IvyListener
extends|extends
name|EventListener
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

