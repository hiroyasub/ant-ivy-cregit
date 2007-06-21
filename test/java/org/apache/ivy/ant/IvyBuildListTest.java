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

begin_class
specifier|public
class|class
name|IvyBuildListTest
extends|extends
name|TestCase
block|{
comment|/*       * Those tests use the ivy files A , B , C , D , E in test/buildlist      * The dependencies are :      * A -> C      * B has no dependency      * C -> B      * D -> A , B      * E has no dependency      */
comment|//CheckStyle:MagicNumber| OFF
comment|//The test very often use MagicNumber.  Using a constant is less expessif.
specifier|public
name|void
name|testSimple
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
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
name|p
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
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/B/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|2
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/D/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|3
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
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
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|4
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReverse
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setReverse
argument_list|(
literal|true
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
name|setReference
argument_list|(
literal|"reverse.ordered.build.files"
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
name|p
operator|.
name|getReference
argument_list|(
literal|"reverse.ordered.build.files"
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
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|length
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
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/D/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|2
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|3
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/B/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|4
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithRoot
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"C"
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/B/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithTwoRoots
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setRoot
argument_list|(
literal|"C,E"
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/B/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
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
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|2
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithRootExclude
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/B/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithRootAndOnlyDirectDep
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithLeaf
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C"
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/D/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|2
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithTwoLeafs
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|buildlist
operator|.
name|setLeaf
argument_list|(
literal|"C,E"
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/D/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|2
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
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
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|3
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithLeafExclude
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/D/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithLeafAndOnlyDirectDep
parameter_list|()
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|IvyBuildList
name|buildlist
init|=
operator|new
name|IvyBuildList
argument_list|()
decl_stmt|;
name|buildlist
operator|.
name|setProject
argument_list|(
name|p
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
name|p
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
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/C/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|0
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/buildlist/A/build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|files
index|[
literal|1
index|]
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CheckStyle:MagicNumber| ON
end_comment

end_unit

