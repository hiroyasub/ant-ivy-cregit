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
name|DirectoryScanner
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
name|FileSet
import|;
end_import

begin_comment
comment|/**  * Checks the given ivy file using current configuration to see if all dependencies are available,  * with good confs. If a resolver name is given, it also checks that the declared publications are  * available in the corresponding resolver. Note that the check is not performed recursively, i.e.  * if a dependency has itself dependencies badly described or not available, this check will not  * discover it.  */
end_comment

begin_class
specifier|public
class|class
name|IvyCheck
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|file
init|=
literal|null
decl_stmt|;
specifier|private
name|List
name|filesets
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|resolvername
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
comment|/**      * Adds a set of files to check.      *       * @param set      *            a set of files to check      */
specifier|public
name|void
name|addFileset
parameter_list|(
name|FileSet
name|set
parameter_list|)
block|{
name|filesets
operator|.
name|add
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getResolvername
parameter_list|()
block|{
return|return
name|resolvername
return|;
block|}
specifier|public
name|void
name|setResolvername
parameter_list|(
name|String
name|resolverName
parameter_list|)
block|{
name|resolvername
operator|=
name|resolverName
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
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ivy
operator|.
name|check
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|resolvername
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"checked "
operator|+
name|file
operator|+
literal|": OK"
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filesets
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|FileSet
name|fs
init|=
operator|(
name|FileSet
operator|)
name|filesets
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DirectoryScanner
name|ds
init|=
name|fs
operator|.
name|getDirectoryScanner
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|fromDir
init|=
name|fs
operator|.
name|getDir
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|srcFiles
init|=
name|ds
operator|.
name|getIncludedFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|srcFiles
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|fromDir
argument_list|,
name|srcFiles
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|ivy
operator|.
name|check
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|resolvername
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"checked "
operator|+
name|file
operator|+
literal|": OK"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
literal|"impossible to convert a file to an url! "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

