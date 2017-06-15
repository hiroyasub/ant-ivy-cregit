begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|descriptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * A configuration which is actually a group of several configurations.  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationGroup
extends|extends
name|Configuration
block|{
specifier|private
specifier|final
name|Map
comment|/*<String, Configuration> */
name|members
decl_stmt|;
specifier|public
name|ConfigurationGroup
parameter_list|(
name|String
name|confName
parameter_list|,
name|Map
comment|/*<String, Configuration> */
name|members
parameter_list|)
block|{
name|super
argument_list|(
name|confName
argument_list|)
expr_stmt|;
name|this
operator|.
name|members
operator|=
name|members
expr_stmt|;
block|}
comment|/**      * Returns the list of configurations' names this object is a group of.      *<p>      * This list is built from the configuration name, if some of these configuration names have      * actually not been recognized in the module, they will be<code>null</code> when accessed from      * {@link org.apache.ivy.core.module.descriptor.ConfigurationIntersection#getIntersectedConfiguration(String)}.      *</p>      *      * @return the list of configurations' names this object is an intersection of.      */
specifier|public
name|String
index|[]
name|getMembersConfigurationNames
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|members
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|members
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Returns the {@link Configuration} object for the given conf name, or<code>null</code> if the      * given conf name is not part of this group or if this conf name isn't defined in the module in      * which this group has been built.      *      * @param confName      *            the name of the configuration to return.      * @return the member {@link Configuration} object for the given conf name      */
specifier|public
name|Configuration
name|getMemberConfiguration
parameter_list|(
name|String
name|confName
parameter_list|)
block|{
return|return
operator|(
name|Configuration
operator|)
name|members
operator|.
name|get
argument_list|(
name|confName
argument_list|)
return|;
block|}
specifier|public
name|Visibility
name|getVisibility
parameter_list|()
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|members
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Configuration
name|c
init|=
operator|(
name|Configuration
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
operator|&&
name|Visibility
operator|.
name|PRIVATE
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getVisibility
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Visibility
operator|.
name|PRIVATE
return|;
block|}
block|}
return|return
name|Visibility
operator|.
name|PUBLIC
return|;
block|}
block|}
end_class

end_unit

