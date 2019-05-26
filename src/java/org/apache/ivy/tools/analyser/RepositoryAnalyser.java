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
name|tools
operator|.
name|analyser
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DefaultArtifact
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
name|RepositoryAnalyser
block|{
specifier|public
name|void
name|analyse
parameter_list|(
name|String
name|pattern
parameter_list|,
name|DependencyAnalyser
name|depAnalyser
parameter_list|)
block|{
name|JarModuleFinder
name|finder
init|=
operator|new
name|JarModuleFinder
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|ModuleDescriptor
index|[]
name|mds
init|=
name|depAnalyser
operator|.
name|analyze
argument_list|(
name|finder
operator|.
name|findJarModules
argument_list|()
argument_list|)
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"found "
operator|+
name|mds
operator|.
name|length
operator|+
literal|" modules"
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|mds
control|)
block|{
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|md
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"generating "
operator|+
name|ivyFile
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|ivyFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"usage: ivyanalyser path/to/jarjar.jar absolute-ivy-repository-pattern"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|jarjarLocation
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|pattern
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
name|JarJarDependencyAnalyser
name|a
init|=
operator|new
name|JarJarDependencyAnalyser
argument_list|(
operator|new
name|File
argument_list|(
name|jarjarLocation
argument_list|)
argument_list|)
decl_stmt|;
operator|new
name|RepositoryAnalyser
argument_list|()
operator|.
name|analyse
argument_list|(
name|pattern
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

