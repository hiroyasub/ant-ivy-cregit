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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|IvyPostResolveTaskTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyPostResolveTask
name|task
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
name|TestHelper
operator|.
name|newProject
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
name|task
operator|=
operator|new
name|IvyPostResolveTask
argument_list|()
block|{
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
block|}
block|}
expr_stmt|;
name|task
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
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithPreviousResolveInSameBuildAndLessConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"default,compile"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithPreviousResolveInSameBuildAndSameConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithPreviousResolveInSameBuildAndWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithPreviousResolveInSameBuildAndBothWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithPreviousResolveInSameBuildAndMoreConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"compile"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.1"
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
name|assertFalse
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
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"IvyPostResolveTask hasn't performed a resolve where it should have"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
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
name|testWithoutKeep
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"compile"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.1"
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
name|assertFalse
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
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
comment|// will trigger a resolve
name|task
operator|.
name|setKeep
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// don't keep the resolve results
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has kept the resolve report where it should have"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
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
name|testInlineWithoutKeep
parameter_list|()
throws|throws
name|Exception
block|{
name|task
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setRevision
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
comment|// will trigger a resolve
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"IvyPostResolveTask has kept the resolve report where it should have"
argument_list|,
name|reportAfter
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
literal|"2.1"
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
name|testInlineWithKeep
parameter_list|()
throws|throws
name|Exception
block|{
name|task
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setRevision
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
comment|// will trigger a resolve
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"IvyPostResolveTask has kept the resolve report where it should have"
argument_list|,
name|reportAfter
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
literal|"2.1"
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
name|testWithResolveIdAndPreviousResolveInSameBuildAndLessConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"default,compile"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report1
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
comment|// perform another resolve
name|resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|ResolveReport
name|report2
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|report1
argument_list|,
name|report2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveIdAndPreviousResolveInSameBuildAndSameConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report1
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
comment|// perform another resolve
name|resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|ResolveReport
name|report2
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|report1
argument_list|,
name|report2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveIdAndPreviousResolveInSameBuildAndWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report1
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
comment|// perform another resolve
name|resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|ResolveReport
name|report2
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|report1
argument_list|,
name|report2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveIdAndPreviousResolveInSameBuildAndBothWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report1
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
comment|// perform another resolve
name|resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|ResolveReport
name|report2
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"IvyPostResolveTask has performed a resolve where it shouldn't"
argument_list|,
name|report1
argument_list|,
name|report2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveIdAndPreviousResolveInSameBuildAndMoreConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"compile"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report1
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.1"
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
name|assertFalse
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
comment|// perform another resolve
name|resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
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
name|resolve
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportBefore
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|task
operator|.
name|setConf
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setResolveId
argument_list|(
literal|"testResolveId"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|reportAfter
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|ResolveReport
name|report2
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report.testResolveId"
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"IvyPostResolveTask hasn't performed a resolve where it should have"
argument_list|,
name|reportBefore
argument_list|,
name|reportAfter
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"IvyPostResolveTask hasn't performed a resolve where it should have"
argument_list|,
name|report1
argument_list|,
name|report2
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
name|task
operator|.
name|getIvyInstance
argument_list|()
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
block|}
end_class

end_unit

