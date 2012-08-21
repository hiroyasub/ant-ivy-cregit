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
name|util
operator|.
name|FileUtil
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

begin_class
specifier|public
class|class
name|IvyInstallTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyInstall
name|install
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
name|cleanInstall
argument_list|()
expr_stmt|;
name|project
operator|=
name|AntTestHelper
operator|.
name|newProject
argument_list|()
expr_stmt|;
name|install
operator|=
operator|new
name|IvyInstall
argument_list|()
expr_stmt|;
name|install
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
name|cleanInstall
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanCache
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|cleanInstall
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install"
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallDummyDefault
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-dummydefaultresolver.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod1.4"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.4/ivy-1.0.1.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.1/ivy-2.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.1/mod1.1-2.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.2/ivy-2.2.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.2/mod1.2-2.2.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithAnyType
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-dummydefaultresolver.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org8"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod8.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setType
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org8/mod8.1/a-1.1.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithMultipleType
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-dummydefaultresolver.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org8"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod8.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setType
argument_list|(
literal|"unused,txt,other"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org8/mod8.1/a-1.1.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Normal case; no confs set (should use the default->* configuration).      */
specifier|public
name|void
name|testInstallWithConfsDefaultSettings
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/IVY-1313/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1/jars/mod1-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod2/jars/mod2-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod3/jars/mod3-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test retrieving artifacts under only the master and runtime configuration.      */
specifier|public
name|void
name|testInstallWithConfsRuntimeOnly
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/IVY-1313/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setConf
argument_list|(
literal|"master,runtime"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1/jars/mod1-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod2/jars/mod2-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod3/jars/mod3-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithClassifiers
parameter_list|()
throws|throws
name|Exception
block|{
comment|// IVY-1324
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.url"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/repositories/m2/ivysettings.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org.apache"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"test-sources"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setType
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"m2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"IVY-1324"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org.apache/test-sources/test-sources-1.0-javadoc.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org.apache/test-sources/test-sources-1.0-sources.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org.apache/test-sources/test-sources-1.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org.apache/test-sources/ivy-1.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithUnusedType
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-dummydefaultresolver.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org8"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod8.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setType
argument_list|(
literal|"unused"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org8/mod8.1/a-1.1.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIVY843
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-IVY843.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"mod1.4"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0.1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|cleanCache
argument_list|()
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install2/org1/mod1.4/ivy-1.0.1.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithBranch
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/branches/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"foo1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setBranch
argument_list|(
literal|"branch1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/foo/foo1/branch1/ivy-2.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithNamespace
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/namespace/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"systemorg"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"systemmod2"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"ns"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/systemorg/systemmod2/ivy-1.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/systemorg/systemmod/ivy-1.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInstallWithNamespace2
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/namespace/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"ns"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
try|try
block|{
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"installing module with namespace coordinates instead of system one should fail"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testInstallWithNamespace3
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/namespace/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTransitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"ns"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/systemorg/systemmod2/ivy-1.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/systemorg/systemmod/ivy-1.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencyNotFoundFailure
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"xxx"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"yyy"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"install"
argument_list|)
expr_stmt|;
try|try
block|{
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"unknown dependency, failure expected (haltonfailure=true)"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|be
parameter_list|)
block|{
comment|// success
block|}
block|}
specifier|public
name|void
name|testDependencyNotFoundSuccess
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setOrganisation
argument_list|(
literal|"xxx"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setModule
argument_list|(
literal|"yyy"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setRevision
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setTo
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|install
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|install
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|be
parameter_list|)
block|{
name|fail
argument_list|(
literal|"unknown dependency, failure unexpected (haltonfailure=false). Failure: "
operator|+
name|be
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

