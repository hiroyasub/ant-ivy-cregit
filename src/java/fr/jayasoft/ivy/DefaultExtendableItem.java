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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * An item which is meant to be extended, i.e. defined using extra attributes  */
end_comment

begin_class
specifier|public
class|class
name|DefaultExtendableItem
block|{
specifier|private
name|Map
name|_attributes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|_attributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
return|;
block|}
specifier|public
name|void
name|setAttribute
parameter_list|(
name|String
name|attName
parameter_list|,
name|String
name|attValue
parameter_list|)
block|{
name|_attributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

