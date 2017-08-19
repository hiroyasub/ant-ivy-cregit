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
name|ant
operator|.
name|testutil
operator|.
name|AntTaskTestCase
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|IvyDependencyUpdateCheckerTest
extends|extends
name|AntTaskTestCase
block|{
specifier|private
name|IvyDependencyUpdateChecker
name|dependencyUpdateChecker
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expExc
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|TestHelper
operator|.
name|createCache
argument_list|()
expr_stmt|;
name|Project
name|project
init|=
name|configureProject
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
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|TestHelper
operator|.
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|=
operator|new
name|IvyDependencyUpdateChecker
argument_list|()
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|TestHelper
operator|.
name|cleanCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|dependencyUpdateChecker
operator|.
name|getIvyInstance
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
comment|// depends on org="org1" name="mod1.1" rev="1.0"
comment|// has transitive dependency on org="org1" name="mod1.2" rev="2.0"
name|dependencyUpdateChecker
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple3.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
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
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.1\t1.0 -> 2.0"
argument_list|)
expr_stmt|;
name|assertLogNotContaining
argument_list|(
literal|"org1#mod1.2 (transitive)\t2.0 -> 2.1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleAndShowTransitiveDependencies
parameter_list|()
throws|throws
name|Exception
block|{
comment|// depends on org="org1" name="mod1.1" rev="1.0"
comment|// has transitive dependency on org="org1" name="mod1.2" rev="2.0"
name|dependencyUpdateChecker
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple3.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setShowTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
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
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.1\t1.0 -> 2.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2 (transitive)\t2.0 -> 2.1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveWithoutIvyFile
parameter_list|()
throws|throws
name|Exception
block|{
comment|// depends on org="org1" name="mod1.2" rev="2.0"
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2\t2.0 -> 2.2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInline
parameter_list|()
throws|throws
name|Exception
block|{
comment|// same as before, but expressing dependency directly without ivy file
name|dependencyUpdateChecker
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setModule
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setRevision
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2\t2.0 -> 2.2"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInlineForNonExistingModule
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
operator|.
name|setOrganisation
argument_list|(
literal|"org1XXYZ"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setModule
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setRevision
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setHaltonfailure
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test must fail because of missing configurations.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testFailureWithMissingConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|expExc
operator|.
name|expect
argument_list|(
name|BuildException
operator|.
name|class
argument_list|)
expr_stmt|;
name|expExc
operator|.
name|expectMessage
argument_list|(
literal|"unknown"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|setConf
argument_list|(
literal|"default,unknown"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFailureOnBadDependencyIvyFile
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFailureOnBadStatusInDependencyIvyFile
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHaltOnFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|dependencyUpdateChecker
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
name|dependencyUpdateChecker
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
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
annotation|@
name|Test
specifier|public
name|void
name|testExcludedConf
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-multiconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|setConf
argument_list|(
literal|"*,!default"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// assertTrue(getIvyFileInCache(ModuleRevisionId.newInstance("org1", "mod1.1", "2.0"))
comment|// .exists());
comment|// assertFalse(getIvyFileInCache(ModuleRevisionId.newInstance("org1", "mod1.2", "2.0"))
comment|// .exists());
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"All dependencies are up to date"
argument_list|)
expr_stmt|;
comment|// test the properties
name|Project
name|project
init|=
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for IVY-396.      *      * @see<a href="https://issues.apache.org/jira/browse/IVY-396">IVY-396</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testResolveWithAbsoluteFile
parameter_list|()
block|{
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
decl_stmt|;
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
name|ivyFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// assertTrue(getResolvedIvyFileInCache(
comment|// ModuleRevisionId.newInstance("apache", "resolve-simple", "1.0")).exists());
block|}
comment|/**      * Test case for IVY-396.      *      * @see<a href="https://issues.apache.org/jira/browse/IVY-396">IVY-396</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testResolveWithRelativeFile
parameter_list|()
block|{
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// assertTrue(getResolvedIvyFileInCache(
comment|// ModuleRevisionId.newInstance("apache", "resolve-simple", "1.0")).exists());
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2\t2.0 -> 2.2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleExtends
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyUpdateChecker
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-extends-multiconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyUpdateChecker
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.parents.count"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.parent[0].organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.parent[0].module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.parent[0].revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|dependencyUpdateChecker
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.parent[0].branch"
argument_list|)
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.1\t1.1 -> 2.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.1\t1.0 -> 2.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2\t2.1 -> 2.2"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"org2#mod2.1\t0.3 -> 0.7"
argument_list|)
expr_stmt|;
comment|// inherited from parent
name|assertLogContaining
argument_list|(
literal|"org1#mod1.2\t2.0 -> 2.2"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

