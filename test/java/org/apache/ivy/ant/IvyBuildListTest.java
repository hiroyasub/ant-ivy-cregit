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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|types
operator|.
name|FileSet
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
name|types
operator|.
name|Path
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
name|assertEquals
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
name|assertNotNull
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

begin_comment
comment|// CheckStyle:MagicNumber| OFF
end_comment

begin_comment
comment|// The test very often use MagicNumber. Using a constant is less expressive.
end_comment

begin_class
specifier|public
class|class
name|IvyBuildListTest
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|private
name|IvyBuildList
name|buildlist
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
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
name|init
argument_list|()
expr_stmt|;
name|buildlist
operator|=
operator|new
name|IvyBuildList
argument_list|()
expr_stmt|;
name|buildlist
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
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
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
specifier|private
name|String
index|[]
name|getFiles
parameter_list|(
name|IvyBuildList
name|buildlist
parameter_list|)
block|{
name|buildlist
operator|.
name|setReference
argument_list|(
literal|"ordered.build.files"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Object
name|o
init|=
name|buildlist
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ordered.build.files"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o
operator|instanceof
name|Path
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
operator|(
name|Path
operator|)
name|o
decl_stmt|;
name|String
index|[]
name|files
init|=
name|path
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|files
argument_list|)
expr_stmt|;
return|return
name|files
return|;
block|}
specifier|private
name|void
name|assertListOfFiles
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
index|[]
name|expected
parameter_list|,
name|String
index|[]
name|actual
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|prefix
operator|+
name|expected
index|[
name|i
index|]
operator|+
literal|"/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|actual
index|[
name|i
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * Those tests use the ivy files A , B , C , D , E in test/buildlist The dependencies are : A ->      * C B has no dependency C -> B D -> A , B E has no dependency F -> G G -> F      */
annotation|@
name|Test
specifier|public
name|void
name|testSimple
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/build.xml,F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|,
literal|"E"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReverse
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/build.xml,F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setReverse
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"E"
block|,
literal|"D"
block|,
literal|"A"
block|,
literal|"C"
block|,
literal|"B"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithRoot
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// A and D should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithRootCircular
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"F"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// F and G should be in the list
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithTwoRoots
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"C,E"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// A and D should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|,
literal|"E"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithRootExclude
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setExcludeRoot
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// A, D and C should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithRootAndOnlyDirectDep
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnlydirectdep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// We should have only A and C
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"C"
block|,
literal|"A"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithLeaf
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// B should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithLeafCircular
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"F"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithTwoLeafs
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C,E"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// B should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|,
literal|"E"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithLeafExclude
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setExcludeLeaf
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// B and C should be filtered out
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"D"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithLeafAndOnlyDirectDep
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/**"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnlydirectdep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// We must have only A and C
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"C"
block|,
literal|"A"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRestartFrom
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/build.xml,F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRestartFrom
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|,
literal|"E"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnMissingDescriptor
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/build.xml,F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"tail"
argument_list|)
expr_stmt|;
comment|// IVY-805: new String instance
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|,
literal|"E"
block|,
literal|"H"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnMissingDescriptor2
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"E2/build.xml,F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
comment|// IVY-805: new String instance
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|,
literal|"E"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithModuleWithSameNameAndDifferentOrg
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setExcludes
argument_list|(
literal|"F/build.xml,G/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlist/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"B"
block|,
literal|"C"
block|,
literal|"A"
block|,
literal|"D"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
comment|// the order of E and E2 is undefined
name|List
name|other
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|other
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|files
index|[
literal|4
index|]
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|other
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|files
index|[
literal|5
index|]
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/E/build.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|,
name|other
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/E2/build.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|,
name|other
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoParents
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlists/testNoParents"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setHaltonerror
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlists/testNoParents/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"bootstrap-parent"
block|,
literal|"ireland"
block|,
literal|"germany"
block|,
literal|"master-parent"
block|,
literal|"croatia"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneParent
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlists/testOneParent"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setHaltonerror
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlists/testOneParent/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"bootstrap-parent"
block|,
literal|"master-parent"
block|,
literal|"croatia"
block|,
literal|"ireland"
block|,
literal|"germany"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoParents
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlists/testTwoParents"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setHaltonerror
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlists/testTwoParents/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"bootstrap-parent"
block|,
literal|"master-parent"
block|,
literal|"croatia"
block|,
literal|"ireland"
block|,
literal|"germany"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelativePathToParent
parameter_list|()
block|{
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlists/testRelativePathToParent"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setHaltonerror
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlists/testRelativePathToParent/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"bootstrap-parent"
block|,
literal|"master-parent"
block|,
literal|"croatia"
block|,
literal|"ireland"
block|,
literal|"germany"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAbsolutePathToParent
parameter_list|()
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"master-parent.dir"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/buildlists/testAbsolutePathToParent/master-parent"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlists/testAbsolutePathToParent"
argument_list|)
argument_list|)
expr_stmt|;
name|fs
operator|.
name|setIncludes
argument_list|(
literal|"**/build.xml"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|addFileset
argument_list|(
name|fs
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setOnMissingDescriptor
argument_list|(
literal|"skip"
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setHaltonerror
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
index|[]
name|files
init|=
name|getFiles
argument_list|(
name|buildlist
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertListOfFiles
argument_list|(
literal|"test/buildlists/testAbsolutePathToParent/"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"bootstrap-parent"
block|,
literal|"master-parent"
block|,
literal|"croatia"
block|,
literal|"ireland"
block|,
literal|"germany"
block|}
argument_list|,
name|files
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// CheckStyle:MagicNumber| ON
end_comment

end_unit

