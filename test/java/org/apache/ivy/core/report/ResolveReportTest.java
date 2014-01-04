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
name|report
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
name|Arrays
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|module
operator|.
name|descriptor
operator|.
name|DependencyDescriptor
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
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|resolve
operator|.
name|ResolveOptions
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
name|util
operator|.
name|CacheCleaner
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
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|ResolveReportTest
extends|extends
name|TestCase
block|{
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|File
name|deliverDir
decl_stmt|;
specifier|private
name|File
name|workDir
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
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
name|createCache
argument_list|()
expr_stmt|;
name|deliverDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/test/deliver"
argument_list|)
expr_stmt|;
name|deliverDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|workDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/test/work"
argument_list|)
expr_stmt|;
name|workDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|ivy
operator|=
name|Ivy
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
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
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|deliverDir
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|workDir
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
name|getResolveOptions
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|confs
argument_list|)
return|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
return|;
block|}
specifier|private
name|void
name|checkFixedMdDependency
parameter_list|(
name|DependencyDescriptor
name|dep
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|mod
parameter_list|,
name|String
name|rev
parameter_list|,
name|String
name|conf
parameter_list|,
name|String
index|[]
name|targetConfs
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|mod
argument_list|,
name|rev
argument_list|)
argument_list|,
name|dep
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dep
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|conf
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|targetConfs
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|dep
operator|.
name|getDependencyConfigurations
argument_list|(
name|conf
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixedMdSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|fixedMd
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|fixedMd
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|fixedMd
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fixedMd
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixedMdTransitiveDependencies
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mod2.1 depends on mod1.1 which depends on mod1.2
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org2/mod2.1/ivys/ivy-0.3.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|fixedMd
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|fixedMd
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|fixedMd
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|fixedMd
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixedMdMultipleExtends
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mod6.2 has two confs default and extension
comment|// mod6.2 depends on mod6.1 in conf (default->extension)
comment|// conf extension extends default
comment|// mod6.1 has two confs default and extension
comment|// mod6.1 depends on mod1.2 2.0 in conf (default->default)
comment|// conf extension extends default
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org6/mod6.2/ivys/ivy-0.3.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|,
literal|"extension"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|fixedMd
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org6"
argument_list|,
literal|"mod6.2"
argument_list|,
literal|"0.3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|fixedMd
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|,
literal|"extension"
block|}
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|fixedMd
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|fixedMd
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org6"
argument_list|,
literal|"mod6.1"
argument_list|,
literal|"0.4"
argument_list|,
literal|"extension"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"extension"
block|,
literal|"default"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|"org6"
argument_list|,
literal|"mod6.1"
argument_list|,
literal|"0.4"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"extension"
block|,
literal|"default"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|2
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"extension"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|3
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixedMdRange
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.4/ivys/ivy-1.0.2.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|fixedMd
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.4"
argument_list|,
literal|"1.0.2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|fixedMd
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|,
literal|"compile"
block|}
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|fixedMd
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|fixedMd
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
block|{
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"1.1"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"1.1"
argument_list|,
literal|"compile"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"1.1"
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"1.1"
argument_list|,
literal|"compile"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testFixedMdKeep
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.4/ivys/ivy-1.0.2.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|fixedMd
init|=
name|report
operator|.
name|toFixedModuleDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ModuleId
index|[]
block|{
name|ModuleId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|)
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.4"
argument_list|,
literal|"1.0.2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|fixedMd
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|,
literal|"compile"
block|}
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|fixedMd
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fixedMd
operator|.
name|getDependencies
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"[1.0,2.0["
argument_list|,
literal|"default"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
name|checkFixedMdDependency
argument_list|(
name|fixedMd
operator|.
name|getDependencies
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"[1.0,2.0["
argument_list|,
literal|"compile"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
