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
name|extendable
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ExtendableItem
block|{
comment|/**      * Gets the value of an attribute      * Can be used to access the value of a standard attribute (like organisation, revision) or of an extra attribute.      * @param attName the name of the attribute to get      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
comment|/**      * Gets the value of a standard attribute      * Can be used only to access the value of a standard attribute (like organisation, revision), not an extra one      * @param attName the name of the standard attribute to get      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getStandardAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
comment|/**      * Gets the value of an extra attribute      * Can be used only to access the value of an extra attribute, not a standard one (like organisation, revision)      * @param attName the name of the extra attribute to get      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
comment|/**      * Returns a Map of all attributes of this extendable item, including standard and extra ones.      * The Map keys are attribute names as Strings, and values are corresponding attribute values (as String too)      * @return      */
name|Map
name|getAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns a Map of all standard attributes of this extendable item.      * The Map keys are attribute names as Strings, and values are corresponding attribute values (as String too)      * @return      */
name|Map
name|getStandardAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns a Map of all extra attributes of this extendable item.      * The Map keys are attribute names as Strings, and values are corresponding attribute values (as String too)      * @return      */
name|Map
name|getExtraAttributes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

