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
name|net
operator|.
name|MalformedURLException
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
name|ArrayList
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
name|module
operator|.
name|descriptor
operator|.
name|ModuleDescriptor
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorWriter
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorWriter
operator|.
name|ConfigurationScopeMapping
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
name|plugins
operator|.
name|parser
operator|.
name|xml
operator|.
name|XmlModuleDescriptorParser
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

begin_comment
comment|/**  * Convert an ivy file to a pom  */
end_comment

begin_class
specifier|public
class|class
name|IvyMakePom
extends|extends
name|IvyTask
block|{
specifier|public
class|class
name|Mapping
block|{
specifier|private
name|String
name|conf
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
name|void
name|setConf
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
block|}
specifier|private
name|File
name|pomFile
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|ivyFile
init|=
literal|null
decl_stmt|;
specifier|private
name|Collection
name|mappings
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|File
name|getPomFile
parameter_list|()
block|{
return|return
name|pomFile
return|;
block|}
specifier|public
name|void
name|setPomFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|pomFile
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|File
name|getIvyFile
parameter_list|()
block|{
return|return
name|ivyFile
return|;
block|}
specifier|public
name|void
name|setIvyFile
parameter_list|(
name|File
name|ivyFile
parameter_list|)
block|{
name|this
operator|.
name|ivyFile
operator|=
name|ivyFile
expr_stmt|;
block|}
specifier|public
name|Mapping
name|createMapping
parameter_list|()
block|{
name|Mapping
name|mapping
init|=
operator|new
name|Mapping
argument_list|()
decl_stmt|;
name|this
operator|.
name|mappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
return|return
name|mapping
return|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
try|try
block|{
if|if
condition|(
name|ivyFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"source ivy file is required for makepom task"
argument_list|)
throw|;
block|}
if|if
condition|(
name|pomFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"destination pom file is required for makepom task"
argument_list|)
throw|;
block|}
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|ivyFile
operator|.
name|toURL
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|PomModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|mappings
operator|.
name|isEmpty
argument_list|()
condition|?
name|PomModuleDescriptorWriter
operator|.
name|DEFAULT_MAPPING
else|:
operator|new
name|ConfigurationScopeMapping
argument_list|(
name|getMappingsMap
argument_list|()
argument_list|)
argument_list|,
name|pomFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to convert given ivy file to url: "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"syntax errors in ivy file "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible convert given ivy file to pom file: "
operator|+
name|e
operator|+
literal|" from="
operator|+
name|ivyFile
operator|+
literal|" to="
operator|+
name|pomFile
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Map
name|getMappingsMap
parameter_list|()
block|{
name|Map
name|mappingsMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|mappings
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Mapping
name|mapping
init|=
operator|(
name|Mapping
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|mappingsMap
operator|.
name|put
argument_list|(
name|mapping
operator|.
name|getConf
argument_list|()
argument_list|,
name|mapping
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mappingsMap
return|;
block|}
block|}
end_class

end_unit

