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
name|IvyInstallTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyInstall
name|_install
decl_stmt|;
specifier|private
name|Project
name|_project
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
name|cleanTestLib
argument_list|()
expr_stmt|;
name|_project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|_project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|_install
operator|=
operator|new
name|IvyInstall
argument_list|()
expr_stmt|;
name|_install
operator|.
name|setProject
argument_list|(
name|_project
argument_list|)
expr_stmt|;
name|_install
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
name|cleanTestLib
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
name|cleanTestLib
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
literal|"build/test/lib"
argument_list|)
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
name|testDependencyNotFoundFailure
parameter_list|()
block|{
name|_install
operator|.
name|setOrganisation
argument_list|(
literal|"xxx"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setModule
argument_list|(
literal|"yyy"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setRevision
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setTo
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
try|try
block|{
name|_install
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"unknown dependency, failure expected (haltunresolved=true)"
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
name|assertTrue
argument_list|(
literal|"invalid exception message, it should contain '1 unresolved',"
operator|+
literal|" but it's: '"
operator|+
name|be
operator|.
name|getMessage
argument_list|()
operator|+
literal|"'"
argument_list|,
name|be
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"1 unresolved"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testDependencyNotFoundSuccess
parameter_list|()
block|{
name|_install
operator|.
name|setOrganisation
argument_list|(
literal|"xxx"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setModule
argument_list|(
literal|"yyy"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setRevision
argument_list|(
literal|"zzz"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setFrom
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setTo
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|_install
operator|.
name|setHaltonunresolved
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|_install
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
literal|"unknown dependency, failure unexepected (haltunresolved=false)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

