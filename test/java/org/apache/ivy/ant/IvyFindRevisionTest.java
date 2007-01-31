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
name|IvyFindRevisionTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyFindRevision
name|_findRevision
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
literal|"ivy.conf.file"
argument_list|,
literal|"test/repositories/ivyconf.xml"
argument_list|)
expr_stmt|;
name|_findRevision
operator|=
operator|new
name|IvyFindRevision
argument_list|()
expr_stmt|;
name|_findRevision
operator|.
name|setProject
argument_list|(
name|project
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
name|testProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|_findRevision
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setProperty
argument_list|(
literal|"test.revision"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|_findRevision
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"test.revision"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLatest
parameter_list|()
throws|throws
name|Exception
block|{
name|_findRevision
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setRevision
argument_list|(
literal|"latest.integration"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|_findRevision
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLatestSubversion
parameter_list|()
throws|throws
name|Exception
block|{
name|_findRevision
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|setRevision
argument_list|(
literal|"1.0+"
argument_list|)
expr_stmt|;
name|_findRevision
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.1"
argument_list|,
name|_findRevision
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

