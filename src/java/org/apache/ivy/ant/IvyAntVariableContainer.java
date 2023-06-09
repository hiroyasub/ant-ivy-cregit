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
name|ant
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|settings
operator|.
name|IvyVariableContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|settings
operator|.
name|IvyVariableContainerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Property
import|;
end_import

begin_class
class|class
name|IvyAntVariableContainer
extends|extends
name|IvyVariableContainerImpl
implements|implements
name|IvyVariableContainer
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|overwrittenProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|public
name|IvyAntVariableContainer
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|r
init|=
name|overwrittenProperties
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|project
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|super
operator|.
name|getVariable
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|public
name|void
name|setVariable
parameter_list|(
name|String
name|varName
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
block|{
if|if
condition|(
name|overwrite
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"setting '"
operator|+
name|varName
operator|+
literal|"' to '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|overwrittenProperties
operator|.
name|put
argument_list|(
name|varName
argument_list|,
name|substitute
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setVariable
argument_list|(
name|varName
argument_list|,
name|value
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates the Ant Project used in this container with variables set in Ivy.      *      * All variables defined in Ivy will be set in the Ant project under two names:      *<ul>      *<li>the name of the variable</li>      *<li>the name of the variable suffixed with a dot + the given id, if the given id is not null      *</li>      *</ul>      *      * @param id      *            The identifier of the settings in which the variables have been set, which should      *            be used as property names suffix      */
specifier|public
name|void
name|updateProject
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|r
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|super
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|putAll
argument_list|(
name|overwrittenProperties
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|r
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|setPropertyIfNotSet
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|setPropertyIfNotSet
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"."
operator|+
name|id
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getEnvironmentPrefix
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Property
name|propTask
init|=
operator|new
name|Property
argument_list|()
decl_stmt|;
name|propTask
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|propTask
operator|.
name|setEnvironment
argument_list|(
name|getEnvironmentPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|propTask
operator|.
name|init
argument_list|()
expr_stmt|;
name|propTask
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setPropertyIfNotSet
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|project
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
operator|==
literal|null
condition|)
block|{
name|project
operator|.
name|setProperty
argument_list|(
name|property
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Object
name|clone
parameter_list|()
block|{
name|IvyAntVariableContainer
name|result
init|=
operator|(
name|IvyAntVariableContainer
operator|)
name|super
operator|.
name|clone
argument_list|()
decl_stmt|;
name|result
operator|.
name|overwrittenProperties
operator|=
operator|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
operator|(
operator|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|this
operator|.
name|overwrittenProperties
operator|)
operator|.
name|clone
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

