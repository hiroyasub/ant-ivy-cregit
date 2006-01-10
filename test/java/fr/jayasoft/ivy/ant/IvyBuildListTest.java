begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ant
package|;
end_package

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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_class
specifier|public
class|class
name|IvyBuildListTest
extends|extends
name|TestCase
block|{
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
literal|3
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
literal|3
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
name|testFilteredBuildList
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
comment|// A should be filtered out
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
name|testFilteredExcludeRootBuildList
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
comment|// A and C should be filtered out
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
block|}
end_class

end_unit

