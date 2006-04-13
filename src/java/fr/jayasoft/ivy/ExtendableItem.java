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

begin_interface
specifier|public
interface|interface
name|ExtendableItem
block|{
comment|/**      * Gets the value of a dependency attribute      * Can be used to access the value of a standard attribute (like org, rev) or of an extra attribute.      * Note that standard attribute are not really standardized, i.e. some implementations my not return       * value for name, so avoid to rely on this in code in which you are not sure of the kind of module descriptor (ivy file, pom, ...) you use.      * @param attName the name of the attribute to get      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

