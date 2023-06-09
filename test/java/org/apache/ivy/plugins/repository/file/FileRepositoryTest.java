begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|repository
operator|.
name|file
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
name|util
operator|.
name|FileUtil
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
name|Test
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
name|assertTrue
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|FileRepositoryTest
block|{
specifier|private
name|File
name|repoDir
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|repoDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/filerepo"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|repoDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|repoDir
operator|!=
literal|null
operator|&&
name|repoDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|putWrites
parameter_list|()
throws|throws
name|Exception
block|{
name|FileRepository
name|fp
init|=
operator|new
name|FileRepository
argument_list|(
name|repoDir
argument_list|)
decl_stmt|;
name|fp
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
literal|"build.xml"
argument_list|)
argument_list|,
literal|"foo/bar/baz.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repoDir
operator|+
literal|"/foo/bar/baz.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|putWontWriteOutsideBasedir
parameter_list|()
throws|throws
name|Exception
block|{
name|FileRepository
name|fp
init|=
operator|new
name|FileRepository
argument_list|(
name|repoDir
argument_list|)
decl_stmt|;
try|try
block|{
name|fp
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
literal|"build.xml"
argument_list|)
argument_list|,
literal|"../baz.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|getReads
parameter_list|()
throws|throws
name|Exception
block|{
name|FileRepository
name|fp
init|=
operator|new
name|FileRepository
argument_list|(
name|repoDir
argument_list|)
decl_stmt|;
name|fp
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
literal|"build.xml"
argument_list|)
argument_list|,
literal|"foo/bar/baz.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fp
operator|.
name|get
argument_list|(
literal|"foo/bar/baz.xml"
argument_list|,
operator|new
name|File
argument_list|(
literal|"build/filerepo/a.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repoDir
operator|+
literal|"/a.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getWontReadOutsideBasedir
parameter_list|()
throws|throws
name|Exception
block|{
name|FileRepository
name|fp
init|=
operator|new
name|FileRepository
argument_list|(
name|repoDir
argument_list|)
decl_stmt|;
try|try
block|{
name|fp
operator|.
name|get
argument_list|(
literal|"../../build.xml"
argument_list|,
operator|new
name|File
argument_list|(
literal|"build/filerepo/a.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

