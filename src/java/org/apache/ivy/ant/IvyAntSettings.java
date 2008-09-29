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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Ivy
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
name|IvySettings
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
name|ivy
operator|.
name|util
operator|.
name|url
operator|.
name|CredentialsStore
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
name|url
operator|.
name|URLHandler
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
name|url
operator|.
name|URLHandlerDispatcher
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
name|url
operator|.
name|URLHandlerRegistry
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
name|taskdefs
operator|.
name|Property
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
name|types
operator|.
name|DataType
import|;
end_import

begin_class
specifier|public
class|class
name|IvyAntSettings
extends|extends
name|DataType
block|{
specifier|public
specifier|static
class|class
name|Credentials
block|{
specifier|private
name|String
name|realm
decl_stmt|;
specifier|private
name|String
name|host
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|passwd
decl_stmt|;
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
return|return
name|this
operator|.
name|passwd
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
name|this
operator|.
name|passwd
operator|=
name|passwd
expr_stmt|;
block|}
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|this
operator|.
name|realm
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
name|this
operator|.
name|realm
operator|=
name|format
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
name|this
operator|.
name|host
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
name|this
operator|.
name|host
operator|=
name|format
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|this
operator|.
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|format
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Ivy
name|ivyEngine
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|file
init|=
literal|null
decl_stmt|;
specifier|private
name|URL
name|url
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|realm
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|host
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|userName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|passwd
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|id
init|=
literal|"ivy.instance"
decl_stmt|;
specifier|private
name|boolean
name|autoRegistered
init|=
literal|false
decl_stmt|;
comment|/**      * Returns the default ivy settings of this classloader. If it doesn't exist yet, a new one is      * created using the given project to back the VariableContainer.      *       * @param  project  TODO add text.      * @return  An IvySetting instance.      */
specifier|public
specifier|static
name|IvyAntSettings
name|getDefaultInstance
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|Object
name|defaultInstanceObj
init|=
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.instance"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultInstanceObj
operator|!=
literal|null
operator|&&
name|defaultInstanceObj
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|!=
name|IvyAntSettings
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
condition|)
block|{
name|project
operator|.
name|log
argument_list|(
literal|"ivy.instance reference an ivy:settings defined in an other classloader.  "
operator|+
literal|"An new default one will be used in this project."
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
name|defaultInstanceObj
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|defaultInstanceObj
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|defaultInstanceObj
operator|instanceof
name|IvyAntSettings
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"ivy.instance reference a "
operator|+
name|defaultInstanceObj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" an not an IvyAntSettings.  Please don't use this reference id ()"
argument_list|)
throw|;
block|}
if|if
condition|(
name|defaultInstanceObj
operator|==
literal|null
condition|)
block|{
name|project
operator|.
name|log
argument_list|(
literal|"No ivy:settings found for the default reference 'ivy.instance'.  "
operator|+
literal|"A default instance will be used"
argument_list|,
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
name|IvyAntSettings
name|settings
init|=
operator|new
name|IvyAntSettings
argument_list|()
decl_stmt|;
name|settings
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|project
operator|.
name|addReference
argument_list|(
literal|"ivy.instance"
argument_list|,
name|settings
argument_list|)
expr_stmt|;
name|settings
operator|.
name|createIvyEngine
argument_list|()
expr_stmt|;
return|return
name|settings
return|;
block|}
else|else
block|{
return|return
operator|(
name|IvyAntSettings
operator|)
name|defaultInstanceObj
return|;
block|}
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
return|return
name|passwd
return|;
block|}
specifier|public
name|void
name|setPasswd
parameter_list|(
name|String
name|aPasswd
parameter_list|)
block|{
name|passwd
operator|=
name|aPasswd
expr_stmt|;
block|}
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|realm
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|aRealm
parameter_list|)
block|{
name|realm
operator|=
name|format
argument_list|(
name|aRealm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|aHost
parameter_list|)
block|{
name|host
operator|=
name|format
argument_list|(
name|aHost
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|aUserName
parameter_list|)
block|{
name|userName
operator|=
name|format
argument_list|(
name|aUserName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProject
parameter_list|(
name|Project
name|p
parameter_list|)
block|{
name|super
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"ivy.instance"
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
name|getProject
argument_list|()
operator|.
name|getReferences
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// register ourselfs as default settings, just in case the id attribute is not set
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.instance"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|autoRegistered
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|format
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|==
literal|null
condition|?
name|str
else|:
operator|(
name|str
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|str
operator|.
name|trim
argument_list|()
operator|)
return|;
block|}
specifier|public
name|void
name|addConfiguredCredentials
parameter_list|(
name|Credentials
name|c
parameter_list|)
block|{
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|addCredentials
argument_list|(
name|c
operator|.
name|getRealm
argument_list|()
argument_list|,
name|c
operator|.
name|getHost
argument_list|()
argument_list|,
name|c
operator|.
name|getUsername
argument_list|()
argument_list|,
name|c
operator|.
name|getPasswd
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|confUrl
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|this
operator|.
name|url
operator|=
operator|new
name|URL
argument_list|(
name|confUrl
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
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
comment|/*      * This is usually not necessary to define a reference in Ant, but it's the only      * way to know the id of the settings, which we use to set ant properties.      */
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|autoRegistered
operator|&&
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
name|this
operator|.
name|id
argument_list|)
operator|==
name|this
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|getReferences
argument_list|()
operator|.
name|remove
argument_list|(
name|this
operator|.
name|id
argument_list|)
expr_stmt|;
name|autoRegistered
operator|=
literal|false
expr_stmt|;
block|}
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Return the configured Ivy instance.      * @return Returns the configured Ivy instance.      */
specifier|public
name|Ivy
name|getConfiguredIvyInstance
parameter_list|()
block|{
if|if
condition|(
name|ivyEngine
operator|==
literal|null
condition|)
block|{
name|createIvyEngine
argument_list|()
expr_stmt|;
block|}
return|return
name|ivyEngine
return|;
block|}
name|void
name|createIvyEngine
parameter_list|()
block|{
name|Property
name|prop
init|=
operator|new
name|Property
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|addProperties
argument_list|(
name|getDefaultProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|prop
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|init
argument_list|()
expr_stmt|;
name|prop
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvyAntVariableContainer
name|ivyAntVariableContainer
init|=
operator|new
name|IvyAntVariableContainer
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|IvySettings
name|settings
init|=
operator|new
name|IvySettings
argument_list|(
name|ivyAntVariableContainer
argument_list|)
decl_stmt|;
name|settings
operator|.
name|setBaseDir
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|==
literal|null
operator|&&
name|url
operator|==
literal|null
condition|)
block|{
name|defineDefaultSettingFile
argument_list|(
name|ivyAntVariableContainer
argument_list|)
expr_stmt|;
block|}
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|pushLogger
argument_list|(
operator|new
name|AntMessageLogger
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|Message
operator|.
name|showInfo
argument_list|()
expr_stmt|;
try|try
block|{
name|configureURLHandler
argument_list|()
expr_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"settings file does not exist: "
operator|+
name|file
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|configure
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
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
name|AssertionError
argument_list|(
literal|"ivy setting should have either a file, either an url,"
operator|+
literal|" and if not defineDefaultSettingFile must set it."
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|configure
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
name|ivyAntVariableContainer
operator|.
name|updateProject
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|ivyEngine
operator|=
name|ivy
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to configure ivy:settings with given "
operator|+
operator|(
name|file
operator|!=
literal|null
condition|?
literal|"file: "
operator|+
name|file
else|:
literal|"url: "
operator|+
name|url
operator|)
operator|+
literal|" : "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to configure ivy:settings with given "
operator|+
operator|(
name|file
operator|!=
literal|null
condition|?
literal|"file: "
operator|+
name|file
else|:
literal|"url: "
operator|+
name|url
operator|)
operator|+
literal|" : "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|popLogger
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|Properties
name|getDefaultProperties
parameter_list|()
block|{
name|URL
name|url
init|=
name|IvySettings
operator|.
name|getDefaultPropertiesURL
argument_list|()
decl_stmt|;
comment|// this is copy of loadURL code from ant Property task (not available in 1.5.1)
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|verbose
argument_list|(
literal|"Loading "
operator|+
name|url
argument_list|)
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|props
return|;
block|}
comment|/**      * Set file or url to its default value      *       * @param variableContainer      */
specifier|private
name|void
name|defineDefaultSettingFile
parameter_list|(
name|IvyVariableContainer
name|variableContainer
parameter_list|)
block|{
name|String
name|settingsFileName
init|=
name|variableContainer
operator|.
name|getVariable
argument_list|(
literal|"ivy.conf.file"
argument_list|)
decl_stmt|;
if|if
condition|(
name|settingsFileName
operator|!=
literal|null
operator|&&
operator|!
name|settingsFileName
operator|.
name|equals
argument_list|(
name|variableContainer
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.file"
argument_list|)
argument_list|)
condition|)
block|{
name|info
argument_list|(
literal|"DEPRECATED: 'ivy.conf.file' is deprecated, use 'ivy.settings.file' instead"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|settingsFileName
operator|=
name|variableContainer
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.file"
argument_list|)
expr_stmt|;
block|}
name|File
index|[]
name|settingsLocations
init|=
operator|new
name|File
index|[]
block|{
operator|new
name|File
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|,
name|settingsFileName
argument_list|)
block|,
operator|new
name|File
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|,
literal|"ivyconf.xml"
argument_list|)
block|,
operator|new
name|File
argument_list|(
name|settingsFileName
argument_list|)
block|,
operator|new
name|File
argument_list|(
literal|"ivyconf.xml"
argument_list|)
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|settingsLocations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|file
operator|=
name|settingsLocations
index|[
name|i
index|]
expr_stmt|;
name|verbose
argument_list|(
literal|"searching settings file: trying "
operator|+
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.14.compatible"
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|info
argument_list|(
literal|"no settings file found, using Ivy 1.4 default..."
argument_list|)
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
name|url
operator|=
name|IvySettings
operator|.
name|getDefault14SettingsURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|info
argument_list|(
literal|"no settings file found, using default..."
argument_list|)
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
name|url
operator|=
name|IvySettings
operator|.
name|getDefaultSettingsURL
argument_list|()
expr_stmt|;
block|}
block|}
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
specifier|private
name|void
name|info
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
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|configureURLHandler
parameter_list|()
block|{
comment|// TODO : the credentialStore should also be scoped
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|addCredentials
argument_list|(
name|getRealm
argument_list|()
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getUsername
argument_list|()
argument_list|,
name|getPasswd
argument_list|()
argument_list|)
expr_stmt|;
name|URLHandlerDispatcher
name|dispatcher
init|=
operator|new
name|URLHandlerDispatcher
argument_list|()
decl_stmt|;
name|URLHandler
name|httpHandler
init|=
name|URLHandlerRegistry
operator|.
name|getHttp
argument_list|()
decl_stmt|;
name|dispatcher
operator|.
name|setDownloader
argument_list|(
literal|"http"
argument_list|,
name|httpHandler
argument_list|)
expr_stmt|;
name|dispatcher
operator|.
name|setDownloader
argument_list|(
literal|"https"
argument_list|,
name|httpHandler
argument_list|)
expr_stmt|;
name|URLHandlerRegistry
operator|.
name|setDefault
argument_list|(
name|dispatcher
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

