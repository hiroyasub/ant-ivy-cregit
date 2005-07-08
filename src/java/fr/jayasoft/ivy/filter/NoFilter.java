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
name|filter
package|;
end_package

begin_class
class|class
name|NoFilter
implements|implements
name|Filter
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

