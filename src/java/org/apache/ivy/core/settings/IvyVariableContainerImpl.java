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
name|HashMap
name|variables
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.core.settings.IvyVariableContainer#setVariable(java.lang.String,      *      java.lang.String, boolean)      */
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
specifier|private
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
name|getVariables
argument_list|()
argument_list|)
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.core.settings.IvyVariableContainer#getVariables()      */
specifier|public
name|Map
name|getVariables
parameter_list|()
block|{
return|return
name|variables
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.ivy.core.settings.IvyVariableContainer#getVariable(java.lang.String)      */
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
operator|(
name|String
operator|)
name|variables
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|val
operator|==
literal|null
condition|?
name|val
else|:
name|substitute
argument_list|(
name|val
argument_list|)
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
operator|(
name|HashMap
operator|)
name|variables
operator|.
name|clone
argument_list|()
expr_stmt|;
return|return
name|clone
return|;
block|}
block|}
end_class

end_unit

