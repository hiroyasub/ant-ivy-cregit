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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_class
specifier|public
class|class
name|IvyEvent
block|{
specifier|private
name|Ivy
name|_source
decl_stmt|;
specifier|public
name|IvyEvent
parameter_list|(
name|Ivy
name|source
parameter_list|)
block|{
name|_source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
name|Ivy
name|getSource
parameter_list|()
block|{
return|return
name|_source
return|;
block|}
block|}
end_class

end_unit

