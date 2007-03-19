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
name|TestHelper
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
name|Delete
import|;
end_import

begin_class
specifier|public
class|class
name|IvyResolveTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyResolve
name|_resolve
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
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|_resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|_resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setCache
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|_cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|_cache
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
name|_cache
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
comment|// depends on org="org1" name="mod1.2" rev="2.0"
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getResolvedIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"resolve-simple"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|getArchiveFileInCache
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|TestHelper
operator|.
name|getArchiveFileInCache
argument_list|(
name|getIvy
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
return|;
block|}
specifier|private
name|File
name|getIvyFileInCache
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|)
block|{
return|return
name|getIvy
argument_list|()
operator|.
name|getCacheManager
argument_list|(
name|_cache
argument_list|)
operator|.
name|getIvyFileInCache
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|File
name|getResolvedIvyFileInCache
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|)
block|{
return|return
name|getIvy
argument_list|()
operator|.
name|getCacheManager
argument_list|(
name|_cache
argument_list|)
operator|.
name|getResolvedIvyFileInCache
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|void
name|testInline
parameter_list|()
throws|throws
name|Exception
block|{
comment|// same as before, but expressing dependency directly without ivy file
name|_resolve
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setModule
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setRevision
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithSlashes
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/core/resolve/ivy-198.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|resolvedIvyFileInCache
init|=
name|getResolvedIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"myorg/mydep"
argument_list|,
literal|"system/module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|resolvedIvyFileInCache
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"yourorg/yourdep"
argument_list|,
literal|"yoursys/yourmod"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"yourorg/yourdep"
argument_list|,
literal|"yoursys/yourmod"
argument_list|,
literal|"1.0"
argument_list|,
literal|"yourmod"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDepsChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConflictingDepsChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/2/mod4.1/ivy-4.1.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDouble
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-double.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-double"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.1"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-failure.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raise an exception
block|}
block|}
specifier|public
name|void
name|testFailureOnBadDependencyIvyFile
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-failure2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raise an exception
block|}
block|}
specifier|public
name|void
name|testFailureOnBadStatusInDependencyIvyFile
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-failure3.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raise an exception
block|}
block|}
specifier|public
name|void
name|testHaltOnFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-failure.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure raised an exception with haltonfailure set to false"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testWithResolveId
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setResolveId
argument_list|(
literal|"testWithResolveId"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getResolvedIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"resolve-simple"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvy
argument_list|()
operator|.
name|getCacheManager
argument_list|(
name|_cache
argument_list|)
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
literal|"testWithResolveId"
argument_list|,
literal|"default"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// test the properties
name|Project
name|project
init|=
name|_resolve
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test the references
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDoubleResolveWithResolveId
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setResolveId
argument_list|(
literal|"testWithResolveId"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvyResolve
name|newResolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|newResolve
operator|.
name|setProject
argument_list|(
name|_resolve
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|newResolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|newResolve
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// test the properties
name|Project
name|project
init|=
name|_resolve
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.1"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test the references
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDifferentResolveWithSameResolveId
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setResolveId
argument_list|(
literal|"testWithResolveId"
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvyResolve
name|newResolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|newResolve
operator|.
name|setProject
argument_list|(
name|_resolve
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|newResolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|newResolve
operator|.
name|setResolveId
argument_list|(
literal|"testWithResolveId"
argument_list|)
expr_stmt|;
name|newResolve
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// test the properties
name|Project
name|project
init|=
name|_resolve
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple2"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.module.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.1"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.1"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.deps.changed.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test the references
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref.testWithResolveId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|_resolve
operator|.
name|getIvyInstance
argument_list|()
return|;
block|}
block|}
end_class

end_unit

