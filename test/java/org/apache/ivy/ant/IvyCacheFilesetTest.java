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

begin_class
specifier|public
class|class
name|IvyCacheFilesetTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyCacheFileset
name|fileset
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
operator|new
name|Project
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
name|cache
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
specifier|public
name|void
name|testFailure
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
literal|"failure-setid"
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raised an exception
block|}
block|}
specifier|public
name|void
name|testInvalidPattern
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
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raise an exception
block|}
block|}
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
specifier|public
name|void
name|testGetBaseDir
parameter_list|()
block|{
name|File
name|base
init|=
literal|null
decl_stmt|;
name|base
operator|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|base
argument_list|,
operator|new
name|File
argument_list|(
literal|"x/aa/b/c"
argument_list|)
argument_list|)
expr_stmt|;
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
name|base
operator|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|base
argument_list|,
operator|new
name|File
argument_list|(
literal|"x/aa/b/d/e"
argument_list|)
argument_list|)
expr_stmt|;
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
name|base
operator|=
name|fileset
operator|.
name|getBaseDir
argument_list|(
name|base
argument_list|,
operator|new
name|File
argument_list|(
literal|"x/ab/b/d"
argument_list|)
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
block|}
block|}
end_class

end_unit

