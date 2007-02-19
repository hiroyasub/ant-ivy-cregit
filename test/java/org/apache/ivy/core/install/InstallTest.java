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
name|install
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
name|plugins
operator|.
name|matcher
operator|.
name|PatternMatcher
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
name|InstallTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|install
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
argument_list|,
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getDefaultResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"install"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|_cache
argument_list|,
name|PatternMatcher
operator|.
name|EXACT
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.2/ivy-2.0.xml"
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
literal|"build/test/install/org1/mod1.2/mod1.2-2.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencies
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|install
argument_list|(
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
argument_list|,
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getDefaultResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"install"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|_cache
argument_list|,
name|PatternMatcher
operator|.
name|EXACT
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.1/ivy-1.0.xml"
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
literal|"build/test/install/org1/mod1.1/mod1.1-1.0.jar"
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
literal|"build/test/install/org1/mod1.2/ivy-2.0.xml"
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
literal|"build/test/install/org1/mod1.2/mod1.2-2.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotTransitive
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|install
argument_list|(
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
argument_list|,
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getDefaultResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"install"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|_cache
argument_list|,
name|PatternMatcher
operator|.
name|EXACT
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.1/ivy-1.0.xml"
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
literal|"build/test/install/org1/mod1.1/mod1.1-1.0.jar"
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
literal|"build/test/install/org1/mod1.2/ivy-2.0.xml"
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
literal|"build/test/install/org1/mod1.2/mod1.2-2.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRegexpMatcher
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|install
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|".*"
argument_list|,
literal|".*"
argument_list|)
argument_list|,
literal|"1"
argument_list|,
literal|"install"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|_cache
argument_list|,
name|PatternMatcher
operator|.
name|REGEXP
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.1/ivy-1.0.xml"
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
literal|"build/test/install/org1/mod1.1/mod1.1-1.0.jar"
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
literal|"build/test/install/org1/mod1.1/ivy-1.1.xml"
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
literal|"build/test/install/org1/mod1.1/mod1.1-1.1.jar"
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
literal|"build/test/install/org1/mod1.2/ivy-2.0.xml"
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
literal|"build/test/install/org1/mod1.2/mod1.2-2.0.jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// mod1.3 is split because Ivy thinks there are two versions of the module:
comment|// this is the normal behaviour in this case
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/install/org1/mod1.3/ivy-B-3.0.xml"
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
literal|"build/test/install/org1/mod1.3/ivy-A-3.0.xml"
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
literal|"build/test/install/org1/mod1.3/mod1.3-A-3.0.jar"
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
literal|"build/test/install/org1/mod1.3/mod1.3-B-3.0.jar"
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
literal|"build/test/install/org1/mod1.4/ivy-1.0.1.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|_cache
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
name|cleanInstall
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
specifier|private
name|void
name|cleanInstall
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
operator|new
name|File
argument_list|(
literal|"build/test/install"
argument_list|)
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
