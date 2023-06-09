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
name|io
operator|.
name|FileInputStream
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
name|URL
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
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_comment
comment|/**  * This task let user set ivy variables from ant.  */
end_comment

begin_class
specifier|public
class|class
name|IvyVar
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|File
name|file
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
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
name|void
name|setFile
parameter_list|(
name|File
name|aFile
parameter_list|)
block|{
name|file
operator|=
name|aFile
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|aName
parameter_list|)
block|{
name|name
operator|=
name|aName
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|aPrefix
parameter_list|)
block|{
name|prefix
operator|=
name|aPrefix
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|aUrl
parameter_list|)
block|{
name|url
operator|=
name|aUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|aValue
parameter_list|)
block|{
name|value
operator|=
name|aValue
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
if|if
condition|(
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|settings
operator|.
name|setVariable
argument_list|(
name|getVarName
argument_list|(
name|getName
argument_list|()
argument_list|)
argument_list|,
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|getUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
operator|new
name|URL
argument_list|(
name|getUrl
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"specify either name or file or url to ivy var task"
argument_list|)
throw|;
block|}
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to load variables from file: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
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
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|props
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|settings
operator|.
name|setVariable
argument_list|(
name|getVarName
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getVarName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|prefix
init|=
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
name|prefix
operator|+
name|name
return|;
block|}
else|else
block|{
return|return
name|prefix
operator|+
literal|"."
operator|+
name|name
return|;
block|}
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

