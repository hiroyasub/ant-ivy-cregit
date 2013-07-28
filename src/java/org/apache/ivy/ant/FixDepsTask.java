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
name|List
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
name|module
operator|.
name|id
operator|.
name|ModuleId
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
name|report
operator|.
name|ResolveReport
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
name|XmlModuleDescriptorWriter
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

begin_class
specifier|public
class|class
name|FixDepsTask
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
name|File
name|dest
decl_stmt|;
specifier|private
name|List
comment|/*<Keep> */
name|keeps
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setToFile
parameter_list|(
name|File
name|dest
parameter_list|)
block|{
name|this
operator|.
name|dest
operator|=
name|dest
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|Keep
block|{
specifier|private
name|String
name|org
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|public
name|void
name|setOrg
parameter_list|(
name|String
name|org
parameter_list|)
block|{
name|this
operator|.
name|org
operator|=
name|org
expr_stmt|;
block|}
specifier|public
name|void
name|setModule
parameter_list|(
name|String
name|module
parameter_list|)
block|{
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
block|}
block|}
specifier|public
name|Keep
name|createKeep
parameter_list|()
block|{
name|Keep
name|k
init|=
operator|new
name|Keep
argument_list|()
decl_stmt|;
name|keeps
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
return|return
name|k
return|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Missing required parameter 'tofile'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
operator|&&
name|dest
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"The destination file '"
operator|+
name|dest
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' already exist and is a folder"
argument_list|)
throw|;
block|}
name|ResolveReport
name|report
init|=
name|getResolvedReport
argument_list|()
decl_stmt|;
name|List
comment|/*<ModuleId>*/
name|midToKeep
init|=
operator|new
name|ArrayList
argument_list|()
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
name|keeps
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|midToKeep
operator|.
name|add
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
operator|(
operator|(
name|Keep
operator|)
name|keeps
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|org
argument_list|,
operator|(
operator|(
name|Keep
operator|)
name|keeps
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|module
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|midToKeep
argument_list|)
decl_stmt|;
try|try
block|{
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|dest
argument_list|)
expr_stmt|;
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
literal|"Failed to write into the file "
operator|+
name|dest
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

