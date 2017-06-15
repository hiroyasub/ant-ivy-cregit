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
name|settings
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
name|IvyPatternHelper
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

begin_class
specifier|public
class|class
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
name|variables
decl_stmt|;
specifier|private
name|String
name|envPrefix
decl_stmt|;
specifier|public
name|IvyVariableContainerImpl
parameter_list|()
block|{
name|this
operator|.
name|variables
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|IvyVariableContainerImpl
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|variables
parameter_list|)
block|{
name|this
operator|.
name|variables
operator|=
name|variables
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.ivy.core.settings.IvyVariableContainer#setVariable(java.lang.String,      * java.lang.String, boolean)      */
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
operator|||
operator|!
name|variables
operator|.
name|containsKey
argument_list|(
name|varName
argument_list|)
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
name|variables
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
name|Message
operator|.
name|debug
argument_list|(
literal|"'"
operator|+
name|varName
operator|+
literal|"' already set: discarding '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setEnvironmentPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
operator|(
name|prefix
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|this
operator|.
name|envPrefix
operator|=
name|prefix
operator|+
literal|"."
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|envPrefix
operator|=
name|prefix
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|substitute
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|value
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getVariables
parameter_list|()
block|{
return|return
name|variables
return|;
block|}
specifier|protected
name|String
name|getEnvironmentPrefix
parameter_list|()
block|{
return|return
name|envPrefix
return|;
block|}
comment|/*      * (non-Javadoc)      *      * @see org.apache.ivy.core.settings.IvyVariableContainer#getVariable(java.lang.String)      */
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|val
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|envPrefix
operator|!=
literal|null
operator|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|envPrefix
argument_list|)
condition|)
block|{
name|val
operator|=
name|System
operator|.
name|getenv
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|envPrefix
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|variables
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
specifier|public
name|Object
name|clone
parameter_list|()
block|{
name|IvyVariableContainerImpl
name|clone
decl_stmt|;
try|try
block|{
name|clone
operator|=
operator|(
name|IvyVariableContainerImpl
operator|)
name|super
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unable to clone a "
operator|+
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
name|clone
operator|.
name|variables
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|this
operator|.
name|variables
argument_list|)
expr_stmt|;
return|return
name|clone
return|;
block|}
block|}
end_class

end_unit

