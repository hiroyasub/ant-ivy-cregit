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
name|Arrays
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Delete
import|;
end_import

begin_class
specifier|public
class|class
name|FixDepsTaskTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|FixDepsTask
name|fixDeps
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createCache
argument_list|()
expr_stmt|;
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|fixDeps
operator|=
operator|new
name|FixDepsTask
argument_list|()
expr_stmt|;
name|fixDeps
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|cleanCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanCache
parameter_list|()
block|{
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
expr_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/testFixDeps/testSimple.xml"
argument_list|)
decl_stmt|;
name|fixDeps
operator|.
name|setToFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|fixDeps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|dest
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|dest
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"release"
argument_list|,
name|md
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getExtends
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.2"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMulticonf
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-multiconf.xml"
argument_list|)
expr_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/testFixDeps/testMultiConf.xml"
argument_list|)
decl_stmt|;
name|fixDeps
operator|.
name|setToFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|fixDeps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|dest
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|dest
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"release"
argument_list|,
name|md
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getExtends
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"compile"
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|1
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|1
index|]
operator|.
name|getExtends
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.2"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"default"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"default"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"compile"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTransitivity
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-transitive.xml"
argument_list|)
expr_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/testFixDeps/testTransitivity.xml"
argument_list|)
decl_stmt|;
name|fixDeps
operator|.
name|setToFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|fixDeps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|dest
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
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
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|dest
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"release"
argument_list|,
name|md
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|0
index|]
operator|.
name|getExtends
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"compile"
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|1
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|md
operator|.
name|getConfigurations
argument_list|()
index|[
literal|1
index|]
operator|.
name|getExtends
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.2"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"default"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"default"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"compile"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mod1.2"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|isChanging
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|isForce
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.1"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getModuleConfigurations
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"compile"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getModuleConfigurations
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"*"
argument_list|,
name|md
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixedResolve
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-transitive.xml"
argument_list|)
expr_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/testFixDeps/testTransitivity.xml"
argument_list|)
decl_stmt|;
name|fixDeps
operator|.
name|setToFile
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|fixDeps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
name|dest
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|dest2
init|=
operator|new
name|File
argument_list|(
literal|"build/testFixDeps/testTransitivity2.xml"
argument_list|)
decl_stmt|;
name|fixDeps
operator|.
name|setToFile
argument_list|(
name|dest2
argument_list|)
expr_stmt|;
name|fixDeps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ModuleDescriptor
name|md1
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|dest
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md2
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|dest2
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|md1
argument_list|,
name|md2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md1
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|md2
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|toString
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md1
operator|.
name|getDependencies
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|toString
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md2
operator|.
name|getDependencies
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
comment|/*<String> */
name|toString
parameter_list|(
name|List
name|list
parameter_list|)
block|{
name|List
name|strings
init|=
operator|new
name|ArrayList
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
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
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|strings
operator|.
name|add
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|strings
return|;
block|}
block|}
end_class

end_unit
