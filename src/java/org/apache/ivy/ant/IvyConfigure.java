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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|BuildException
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
name|Task
import|;
end_import

begin_comment
comment|/**  * Configure Ivy with an ivysettings.xml file  */
end_comment

begin_class
specifier|public
class|class
name|IvyConfigure
extends|extends
name|Task
block|{
comment|/**      * Use to override a previous definition of settings with the same id      */
specifier|public
specifier|static
specifier|final
name|String
name|OVERRIDE_TRUE
init|=
literal|"true"
decl_stmt|;
comment|/**      * Use to avoid overriding a previous definition of settings with the same id      */
specifier|public
specifier|static
specifier|final
name|String
name|OVERRIDE_FALSE
init|=
literal|"false"
decl_stmt|;
comment|/**      * Use to raise an error if attempting to override a previous definition of settings with the      * same id      */
specifier|public
specifier|static
specifier|final
name|String
name|OVERRIDE_NOT_ALLOWED
init|=
literal|"notallowed"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|OVERRIDE_VALUES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|OVERRIDE_TRUE
argument_list|,
name|OVERRIDE_FALSE
argument_list|,
name|OVERRIDE_NOT_ALLOWED
argument_list|)
decl_stmt|;
specifier|private
name|String
name|override
init|=
name|OVERRIDE_NOT_ALLOWED
decl_stmt|;
specifier|private
name|IvyAntSettings
name|settings
init|=
operator|new
name|IvyAntSettings
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setSettingsId
parameter_list|(
name|String
name|settingsId
parameter_list|)
block|{
name|settings
operator|.
name|setId
argument_list|(
name|settingsId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSettingsId
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|void
name|setOverride
parameter_list|(
name|String
name|override
parameter_list|)
block|{
if|if
condition|(
operator|!
name|OVERRIDE_VALUES
operator|.
name|contains
argument_list|(
name|override
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid override value '"
operator|+
name|override
operator|+
literal|"'. "
operator|+
literal|"Valid values are "
operator|+
name|OVERRIDE_VALUES
argument_list|)
throw|;
block|}
name|this
operator|.
name|override
operator|=
name|override
expr_stmt|;
block|}
specifier|public
name|String
name|getOverride
parameter_list|()
block|{
return|return
name|override
return|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getFile
argument_list|()
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|settings
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getUrl
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|settings
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Cannot set a null URL"
argument_list|)
throw|;
block|}
name|settings
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getRealm
argument_list|()
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|settings
operator|.
name|setRealm
argument_list|(
name|realm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getHost
argument_list|()
return|;
block|}
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|settings
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getUsername
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|settings
operator|.
name|setUsername
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getPasswd
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPasswd
parameter_list|(
name|String
name|passwd
parameter_list|)
block|{
name|settings
operator|.
name|setPasswd
argument_list|(
name|passwd
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredWorkspaceResolver
parameter_list|(
name|AntWorkspaceResolver
name|resolver
parameter_list|)
block|{
name|settings
operator|.
name|addConfiguredWorkspaceResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|String
name|settingsId
init|=
name|settings
operator|.
name|getId
argument_list|()
decl_stmt|;
name|Object
name|otherRef
init|=
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
name|settingsId
argument_list|)
decl_stmt|;
if|if
condition|(
name|otherRef
operator|!=
literal|null
operator|&&
name|OVERRIDE_NOT_ALLOWED
operator|.
name|equals
argument_list|(
name|override
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Overriding a previous definition of ivy:settings with the id '"
operator|+
name|settingsId
operator|+
literal|"' is not allowed when using override='"
operator|+
name|OVERRIDE_NOT_ALLOWED
operator|+
literal|"'."
argument_list|)
throw|;
block|}
if|if
condition|(
name|otherRef
operator|!=
literal|null
operator|&&
name|OVERRIDE_FALSE
operator|.
name|equals
argument_list|(
name|override
argument_list|)
condition|)
block|{
name|verbose
argument_list|(
literal|"A settings definition is already available for "
operator|+
name|settingsId
operator|+
literal|": skipping"
argument_list|)
expr_stmt|;
return|return;
block|}
name|settings
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|settingsId
argument_list|,
name|settings
argument_list|)
expr_stmt|;
name|settings
operator|.
name|createIvyEngine
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verbose
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|msg
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

