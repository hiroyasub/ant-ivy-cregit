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
name|namespace
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
name|ModuleRevisionId
import|;
end_import

begin_interface
specifier|public
interface|interface
name|NamespaceTransformer
block|{
specifier|public
name|ModuleRevisionId
name|transform
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
function_decl|;
specifier|public
name|boolean
name|isIdentity
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

