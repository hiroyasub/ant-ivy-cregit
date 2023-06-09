begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
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
comment|/**      * Gets the value of an attribute Can be used to access the value of a standard attribute (like      * organisation, revision) or of an extra attribute.      *      * @param attName      *            the name of the attribute to get      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
comment|/**      * Gets the value of an extra attribute Can be used only to access the value of an extra      * attribute, not a standard one (like organisation, revision)      *      * @param attName      *            the name of the extra attribute to get. This name can be either qualified or      *            unqualified.      * @return the value of the attribute, null if the attribute doesn't exist      */
name|String
name|getExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
function_decl|;
comment|/**      * Returns a Map of all attributes of this extendable item, including standard and extra ones.      * The Map keys are attribute names as Strings, and values are corresponding attribute values      * (as String too). Extra attributes are included in unqualified form only.      *      * @return A Map instance containing all the attributes and their values.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns a Map of all extra attributes of this extendable item. The Map keys are      *<b>unqualified</b> attribute names as Strings, and values are corresponding attribute values      * (as String too)      *      * @return A Map instance containing all the extra attributes and their values.      * @see #getQualifiedExtraAttributes()      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns a Map of all extra attributes of this extendable item.      *<p>      * The Map keys are<b>qualified</b> attribute names as Strings, and values are corresponding      * attribute values (as String too).      *</p>      *<p>      * An attribute name is qualified with a namespace exactly the same way xml attributes are      * qualified. Thus qualified attribute names are of the form<code>prefix:name</code>      *</p>      *      * @return A Map instance containing all the extra attributes and their values.      * @see #getExtraAttributes()      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQualifiedExtraAttributes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

