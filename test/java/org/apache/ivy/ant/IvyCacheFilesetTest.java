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
name|assertNull
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
name|Arrays
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
name|core
operator|.
name|report
operator|.
name|ArtifactDownloadReport
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
name|DirectoryScanner
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
name|Rule
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
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_class
specifier|public
class|class
name|IvyCacheFilesetTest
block|{
specifier|private
name|IvyCacheFileset
name|fileset
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expExc
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|TestHelper
operator|.
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
name|fileset
operator|=
operator|new
name|IvyCacheFileset
argument_list|()
expr_stmt|;
name|fileset
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
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|TestHelper
operator|.
name|cleanCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"simple-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Object
name|ref
init|=
name|project
operator|.
name|getReference
argument_list|(
literal|"simple-setid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|instanceof
name|FileSet
argument_list|)
expr_stmt|;
name|FileSet
name|fs
init|=
operator|(
name|FileSet
operator|)
name|ref
decl_stmt|;
name|DirectoryScanner
name|directoryScanner
init|=
name|fs
operator|.
name|getDirectoryScanner
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|directoryScanner
operator|.
name|getIncludedFiles
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
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
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|directoryScanner
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|directoryScanner
operator|.
name|getIncludedFiles
argument_list|()
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
name|fileset
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
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
return|return
name|TestHelper
operator|.
name|getArchiveFileInCache
argument_list|(
name|fileset
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
annotation|@
name|Test
specifier|public
name|void
name|testEmptyConf
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-108.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"emptyconf-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setConf
argument_list|(
literal|"empty"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Object
name|ref
init|=
name|project
operator|.
name|getReference
argument_list|(
literal|"emptyconf-setid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|instanceof
name|FileSet
argument_list|)
expr_stmt|;
name|FileSet
name|fs
init|=
operator|(
name|FileSet
operator|)
name|ref
decl_stmt|;
name|DirectoryScanner
name|directoryScanner
init|=
name|fs
operator|.
name|getDirectoryScanner
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|directoryScanner
operator|.
name|scan
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|directoryScanner
operator|.
name|getIncludedFiles
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-failure.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"failure-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test must fail with default haltonfailure setting.      *      * @throws Exception      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BuildException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInvalidPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings-invalidcachepattern.xml"
argument_list|)
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"simple-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHaltOnFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-failure.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"haltfailure-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"failure raised an exception with haltonfailure set to false"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithoutPreviousResolveAndNonDefaultCache
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|cache2
init|=
operator|new
name|File
argument_list|(
literal|"build/cache2"
argument_list|)
decl_stmt|;
name|cache2
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
try|try
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setSetid
argument_list|(
literal|"simple-setid"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache2
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Object
name|ref
init|=
name|project
operator|.
name|getReference
argument_list|(
literal|"simple-setid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ref
operator|instanceof
name|FileSet
argument_list|)
expr_stmt|;
name|FileSet
name|fs
init|=
operator|(
name|FileSet
operator|)
name|ref
decl_stmt|;
name|DirectoryScanner
name|directoryScanner
init|=
name|fs
operator|.
name|getDirectoryScanner
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|directoryScanner
operator|.
name|getIncludedFiles
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
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
argument_list|,
name|cache2
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|directoryScanner
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|directoryScanner
operator|.
name|getIncludedFiles
argument_list|()
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
finally|finally
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
name|cache2
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|getBaseDirCommonBaseDir
parameter_list|()
block|{
specifier|final
name|File
name|file1
init|=
operator|new
name|File
argument_list|(
literal|"x/aa/b/c"
argument_list|)
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
specifier|final
name|File
name|file2
init|=
operator|new
name|File
argument_list|(
literal|"x/aa/b/d/e"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|file3
init|=
operator|new
name|File
argument_list|(
literal|"x/ab/b/d"
argument_list|)
decl_stmt|;
comment|// A common base deep inside the tree
name|File
name|base
init|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|file1
argument_list|,
name|file2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"x/aa/b"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|base
argument_list|)
expr_stmt|;
comment|// A common base on top directory of the tree
name|base
operator|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|base
argument_list|,
name|file3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"x"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|base
argument_list|)
expr_stmt|;
comment|// A common base only on the fs-root.
specifier|final
name|File
index|[]
name|filesytemRoots
init|=
name|File
operator|.
name|listRoots
argument_list|()
decl_stmt|;
specifier|final
name|File
name|root1
init|=
name|filesytemRoots
index|[
literal|0
index|]
decl_stmt|;
specifier|final
name|File
name|file4
init|=
operator|new
name|File
argument_list|(
name|root1
argument_list|,
literal|"abcd/xyz"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|file5
init|=
operator|new
name|File
argument_list|(
name|root1
argument_list|,
literal|"pqrs/xyz"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|commonBase
init|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|file4
argument_list|,
name|file5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected common base dir between '"
operator|+
name|file4
operator|+
literal|"' and '"
operator|+
name|file5
operator|+
literal|"'"
argument_list|,
name|root1
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|commonBase
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that the {@link IvyCacheFileset} fails with an exception if it can't determine a common base directory      * while dealing with cached artifacts      *      * @see<a href="https://issues.apache.org/jira/browse/IVY-1475">IVY-1475</a> for more details      */
annotation|@
name|Test
specifier|public
name|void
name|getBaseDirNoCommonBaseDir
parameter_list|()
block|{
specifier|final
name|File
index|[]
name|fileSystemRoots
init|=
name|File
operator|.
name|listRoots
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileSystemRoots
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// single file system root isn't what we are interested in, in this test method
return|return;
block|}
specifier|final
name|File
name|root1
init|=
name|fileSystemRoots
index|[
literal|0
index|]
decl_stmt|;
specifier|final
name|File
name|root2
init|=
name|fileSystemRoots
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|File
name|fileOnRoot1
init|=
operator|new
name|File
argument_list|(
name|root1
argument_list|,
literal|"abc/file1"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|fileOnRoot2
init|=
operator|new
name|File
argument_list|(
name|root2
argument_list|,
literal|"abc/file2"
argument_list|)
decl_stmt|;
name|File
name|base
init|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|fileOnRoot1
argument_list|,
name|fileOnRoot2
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|base
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getBaseDirNullValues
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"Base directory was expected to be null"
argument_list|,
name|fileset
operator|.
name|getBaseDir
argument_list|(
literal|null
argument_list|,
operator|new
name|File
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Base directory was expected to be null"
argument_list|,
name|fileset
operator|.
name|getBaseDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"a"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|requireCommonBaseDirEmptyList
parameter_list|()
block|{
comment|// we expect a BuildException when we try to find a (non-existent) common base dir
comment|// across file system roots
name|expExc
operator|.
name|expect
argument_list|(
name|BuildException
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
init|=
name|Arrays
operator|.
name|asList
argument_list|()
decl_stmt|;
name|fileset
operator|.
name|requireCommonBaseDir
argument_list|(
name|reports
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"A BuildException was expected when trying to find a common base dir."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|requireCommonBaseDirNoCommon
parameter_list|()
block|{
specifier|final
name|File
index|[]
name|fileSystemRoots
init|=
name|File
operator|.
name|listRoots
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileSystemRoots
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|// single file system root isn't what we are interested in, in this test method
return|return;
block|}
comment|// we expect a BuildException when we try to find a (non-existent) common base dir
comment|// across file system roots
name|expExc
operator|.
name|expect
argument_list|(
name|BuildException
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|artifactDownloadReport
argument_list|(
operator|new
name|File
argument_list|(
name|fileSystemRoots
index|[
literal|0
index|]
argument_list|,
literal|"a/b/c/d"
argument_list|)
argument_list|)
argument_list|,
name|artifactDownloadReport
argument_list|(
operator|new
name|File
argument_list|(
name|fileSystemRoots
index|[
literal|1
index|]
argument_list|,
literal|"a/b/e/f"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|fileset
operator|.
name|requireCommonBaseDir
argument_list|(
name|reports
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"A BuildException was expected when trying to find a common base dir."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|requireCommonBaseDirCommon
parameter_list|()
block|{
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|artifactDownloadReport
argument_list|(
operator|new
name|File
argument_list|(
literal|"a/b/c/d"
argument_list|)
argument_list|)
argument_list|,
name|artifactDownloadReport
argument_list|(
operator|new
name|File
argument_list|(
literal|"a/b/e/f"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fileset
operator|.
name|requireCommonBaseDir
argument_list|(
name|reports
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArtifactDownloadReport
name|artifactDownloadReport
parameter_list|(
name|File
name|localFile
parameter_list|)
block|{
name|ArtifactDownloadReport
name|report
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|report
operator|.
name|setLocalFile
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
return|return
name|report
return|;
block|}
block|}
end_class

end_unit

