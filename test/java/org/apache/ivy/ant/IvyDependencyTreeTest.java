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

begin_class
specifier|public
class|class
name|IvyDependencyTreeTest
extends|extends
name|AntTaskTestCase
block|{
specifier|private
name|IvyDependencyTree
name|dependencyTree
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
name|TestHelper
operator|.
name|createCache
argument_list|()
expr_stmt|;
name|project
operator|=
name|configureProject
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
name|dependencyTree
operator|=
operator|new
name|IvyDependencyTree
argument_list|()
expr_stmt|;
name|dependencyTree
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
name|TestHelper
operator|.
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|TestHelper
operator|.
name|cleanCache
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
name|dependencyTree
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
name|dependencyTree
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependency tree for apache-resolve-simple"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"\\- org1#mod1.2;2.0"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveId
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
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// resolve another ivy file
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
literal|"test/java/org/apache/ivy/ant/ivy-latest.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|dependencyTree
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependency tree for apache-resolve-latest"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"\\- org1#mod1.2;latest.integration"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithResolveIdWithoutResolve
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|dependencyTree
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Task should have failed because no resolve was performed!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|e
parameter_list|)
block|{
comment|// this is expected!
block|}
block|}
specifier|public
name|void
name|testWithEvictedModule
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyTree
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-dyn-evicted.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyTree
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependency tree for apache-resolve-latest"
argument_list|)
expr_stmt|;
name|assertLogNotContaining
argument_list|(
literal|"+- org1#mod1.2;1+"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"+- org6#mod6.1;2.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"   \\- org1#mod1.2;2.2"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"\\- org1#mod1.2;2.2"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testShowEvictedModule
parameter_list|()
throws|throws
name|Exception
block|{
name|dependencyTree
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-dyn-evicted.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyTree
operator|.
name|setShowEvicted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dependencyTree
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"Dependency tree for apache-resolve-latest"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"+- org1#mod1.2;1+ evicted by [org1#mod1.2;2.2] in apache#resolve-latest;1.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"+- org6#mod6.1;2.0"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"   \\- org1#mod1.2;2.2"
argument_list|)
expr_stmt|;
name|assertLogContaining
argument_list|(
literal|"\\- org1#mod1.2;2.2"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

