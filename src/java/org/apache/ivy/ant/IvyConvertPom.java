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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorParser
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
name|repository
operator|.
name|url
operator|.
name|URLResource
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
comment|/**  * Convert a pom to an ivy file  */
end_comment

begin_class
specifier|public
class|class
name|IvyConvertPom
extends|extends
name|IvyTask
block|{
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
name|pomFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"source pom file is required for convertpom task"
argument_list|)
throw|;
block|}
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
literal|"destination ivy file is required for convertpom task"
argument_list|)
throw|;
block|}
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|pomFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|toIvyFile
argument_list|(
name|pomFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|,
operator|new
name|URLResource
argument_list|(
name|pomFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|,
name|getIvyFile
argument_list|()
argument_list|,
name|md
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
literal|"unable to convert given pom file to url: "
operator|+
name|pomFile
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
literal|"syntax errors in pom file "
operator|+
name|pomFile
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
literal|"impossible convert given pom file to ivy file: "
operator|+
name|e
operator|+
literal|" from="
operator|+
name|pomFile
operator|+
literal|" to="
operator|+
name|ivyFile
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

