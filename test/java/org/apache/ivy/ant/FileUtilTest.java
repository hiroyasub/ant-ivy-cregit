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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|CopyProgressListener
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
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
comment|/**  * Tests {@link FileUtil}  *  * @author Jaikiran Pai  */
end_comment

begin_class
specifier|public
class|class
name|FileUtilTest
block|{
specifier|private
specifier|static
name|boolean
name|symlinkCapable
init|=
literal|false
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
try|try
block|{
specifier|final
name|Path
name|tmpFile
init|=
name|Files
operator|.
name|createTempFile
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tmpFile
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
specifier|final
name|Path
name|symlink
init|=
name|Files
operator|.
name|createSymbolicLink
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
literal|"symlink-test-file"
argument_list|)
argument_list|,
name|tmpFile
argument_list|)
decl_stmt|;
name|symlinkCapable
operator|=
literal|true
expr_stmt|;
name|symlink
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|// ignore and move on
name|symlinkCapable
operator|=
literal|false
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"Current system isn't considered to have symlink capability due to "
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests that {@link FileUtil#normalize(String)} works as expected for some basic file paths      */
annotation|@
name|Test
specifier|public
name|void
name|testSimpleNormalize
parameter_list|()
block|{
specifier|final
name|File
name|ivySettingsFile
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|normalizedIvySettingsFile
init|=
name|FileUtil
operator|.
name|normalize
argument_list|(
name|ivySettingsFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of file path "
operator|+
name|ivySettingsFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|ivySettingsFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|normalizedIvySettingsFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|normalizedIvySettingsFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" isn't a file"
argument_list|,
name|normalizedIvySettingsFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that {@link FileUtil#normalize(String)} works as expected when passed a path that starts with      * {@link File#listRoots() filesystem roots}      */
annotation|@
name|Test
specifier|public
name|void
name|testNormalizeOfFileSystemRootPath
parameter_list|()
block|{
for|for
control|(
specifier|final
name|File
name|fileSystemRoot
range|:
name|File
operator|.
name|listRoots
argument_list|()
control|)
block|{
specifier|final
name|File
name|normalized
init|=
name|FileUtil
operator|.
name|normalize
argument_list|(
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Normalized path was null for "
operator|+
name|fileSystemRoot
argument_list|,
name|normalized
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalized path for "
operator|+
name|fileSystemRoot
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
argument_list|,
name|normalized
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// use . and .. characters in the path to test out the normalize method
specifier|final
name|String
name|pathOne
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"."
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of "
operator|+
name|pathOne
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathOne
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pathTwo
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"."
operator|+
name|File
operator|.
name|separator
operator|+
literal|"foo-bar"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of "
operator|+
name|pathTwo
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"foo-bar"
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathTwo
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pathThree
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"foo-bar"
operator|+
name|File
operator|.
name|separator
operator|+
literal|".."
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of "
operator|+
name|pathThree
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathThree
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// append some additional file separator characters to the file system root and normalize it
specifier|final
name|String
name|pathFour
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of "
operator|+
name|pathFour
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathFour
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pathFive
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
operator|+
literal|"abcd"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of path "
operator|+
name|pathFive
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"abcd"
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathFive
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pathSix
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
operator|+
literal|"1234"
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of path "
operator|+
name|pathSix
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"1234"
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathSix
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|pathSeven
init|=
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"helloworld"
operator|+
name|File
operator|.
name|separator
operator|+
literal|".."
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
operator|+
name|File
operator|.
name|separator
operator|+
literal|"helloworld"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected normalization of path "
operator|+
name|pathSeven
argument_list|,
name|fileSystemRoot
operator|.
name|getPath
argument_list|()
operator|+
literal|"helloworld"
argument_list|,
name|FileUtil
operator|.
name|normalize
argument_list|(
name|pathSeven
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests that the call to {@link FileUtil#copy(File, File, CopyProgressListener)} doesn't corrupt      * the source file if the destination file resolves back to the source file being copied      *      * @throws Exception      * @see<a href="https://issues.apache.org/jira/browse/IVY-1602">IVY-1602</a> for more details      */
annotation|@
name|Test
specifier|public
name|void
name|testCopyOfSameFile
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
literal|"Skipping test due to system not having symlink capability"
argument_list|,
name|symlinkCapable
argument_list|)
expr_stmt|;
specifier|final
name|Path
name|srcDir
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|srcDir
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
comment|// create a src file
specifier|final
name|Path
name|srcFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|srcDir
operator|.
name|toString
argument_list|()
argument_list|,
literal|"helloworld.txt"
argument_list|)
decl_stmt|;
name|srcFile
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
specifier|final
name|byte
index|[]
name|fileContent
init|=
literal|"Hello world!!!"
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|srcFile
argument_list|,
name|fileContent
argument_list|)
expr_stmt|;
specifier|final
name|Path
name|destDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
literal|"symlink-dest"
argument_list|)
decl_stmt|;
name|destDir
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
comment|// now create a symlink to the dir containing the src file we intend to copy later
name|Files
operator|.
name|createSymbolicLink
argument_list|(
name|destDir
argument_list|,
name|srcDir
argument_list|)
expr_stmt|;
comment|// at this point destDir is a symlink to the srcDir and the srcDir contains the srcFile.
comment|// we now attempt to copy the srcFile to a destination which resolves back the same srcFile
specifier|final
name|Path
name|destFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|destDir
operator|.
name|toString
argument_list|()
argument_list|,
name|srcFile
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|srcFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|destFile
operator|.
name|toFile
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// make sure the copy didn't corrupt the source file
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Unexpected content in source file "
operator|+
name|srcFile
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|fileContent
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|srcFile
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// also check the dest file has the same content as source file after the copy operation
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Unexpected content in dest file "
operator|+
name|destFile
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|fileContent
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|destFile
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// do the same tests now with overwrite = true
name|FileUtil
operator|.
name|copy
argument_list|(
name|srcFile
operator|.
name|toFile
argument_list|()
argument_list|,
name|destFile
operator|.
name|toFile
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// make sure the copy didn't corrupt the source file
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Unexpected content in source file "
operator|+
name|srcFile
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|fileContent
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|srcFile
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// also check the dest file has the same content as source file after the copy operation
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Unexpected content in dest file "
operator|+
name|destFile
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|fileContent
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|destFile
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

