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
name|LatestStrategy
import|;
end_import

begin_interface
specifier|public
interface|interface
name|HasLatestStrategy
block|{
specifier|public
name|LatestStrategy
name|getLatestStrategy
parameter_list|()
function_decl|;
specifier|public
name|void
name|setLatestStrategy
parameter_list|(
name|LatestStrategy
name|latestStrategy
parameter_list|)
function_decl|;
specifier|public
name|String
name|getLatest
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

