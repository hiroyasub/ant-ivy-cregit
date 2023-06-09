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
name|vfs
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
name|io
operator|.
name|IOException
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
name|assertNotEquals
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

begin_comment
comment|/**  * Testing was the single biggest hurdle I faced. I have tried to provide a complete test  * suite that covers all protocols and which can be easily extended. It does differ - somewhat - in  * structure from the resolver/repository test suites. Setting up smb, ftp, sftp will undoubtedly be  * your biggest headache (it was mine). Here are a few notes about the setup:  *<ul>  *<li>the VFS test suite uses the build/test/repositories area;  * when setting samba, sftp, etc. the corresponding user needs both read and write privileges.</li>  *<li>the tests assume that the user and password is the same for all services.</li>  *<li>a limited amount of configuration is available by setting the following properties in the  *<code>ivy.properties</code> file:</li>  *</ul>  *<pre>  *   vfs.host  *   vfs.username  *   vfs.password  *   vfs.samba_share  *</pre>  * Running the test requires that commons-io and ant jars are on the classpath. Also, I would  * recommend that at some time the tests be converted from straight junit to something which better  * supports functional testing. Although somewhat crude, I am using jsystem  * (http://jsystemtest.sourceforge.net/) in other projects and am finding it a much better solution  * than straight junit.  *<p>Stephen Nesbitt</p>  */
end_comment

begin_class
specifier|public
class|class
name|VfsRepositoryTest
block|{
specifier|private
name|VfsRepository
name|repo
init|=
literal|null
decl_stmt|;
specifier|private
name|VfsTestHelper
name|helper
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|scratchDir
init|=
literal|null
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
name|helper
operator|=
operator|new
name|VfsTestHelper
argument_list|()
expr_stmt|;
name|repo
operator|=
operator|new
name|VfsRepository
argument_list|()
expr_stmt|;
name|scratchDir
operator|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|VfsTestHelper
operator|.
name|SCRATCH_DIR
argument_list|)
argument_list|)
expr_stmt|;
name|scratchDir
operator|.
name|mkdir
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
name|repo
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Basic validation of happy path put - valid VFS URI and no conflict with existing file      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testPutValid
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
name|String
name|srcFile
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|testResource
argument_list|)
decl_stmt|;
name|String
name|destResource
init|=
name|VfsTestHelper
operator|.
name|SCRATCH_DIR
operator|+
literal|"/"
operator|+
name|testResource
decl_stmt|;
name|String
name|destFile
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|destResource
argument_list|)
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|destResource
argument_list|)
control|)
block|{
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|repo
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
name|srcFile
argument_list|)
argument_list|,
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Put didn't happen. Src VfsURI: "
operator|+
name|vfsURI
operator|.
name|toString
argument_list|()
operator|+
literal|".\nExpected file: "
operator|+
name|destFile
argument_list|,
operator|new
name|File
argument_list|(
name|srcFile
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we can overwrite an existing file      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testPutOverwriteTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
name|String
name|srcFile
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|testResource
argument_list|)
decl_stmt|;
name|String
name|destResource
init|=
name|VfsTestHelper
operator|.
name|SCRATCH_DIR
operator|+
literal|"/"
operator|+
name|testResource
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|destResource
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|destResource
argument_list|)
control|)
block|{
comment|// remove existing scratch dir and populate it with an empty file
comment|// that we can overwrite. We do this so that we can test file sizes.
comment|// seeded file has length 0, while put file will have a length> 0
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|destFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|repo
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
name|srcFile
argument_list|)
argument_list|,
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Put didn't happen. Src VfsURI: "
operator|+
name|vfsURI
operator|.
name|toString
argument_list|()
operator|+
literal|".\nExpected file: "
operator|+
name|destFile
argument_list|,
operator|new
name|File
argument_list|(
name|srcFile
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"Zero file size indicates file not overwritten"
argument_list|,
literal|0
argument_list|,
name|destFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we put will respect a request not to overwrite an existing file      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IOException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testPutOverwriteFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
name|String
name|srcFile
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|testResource
argument_list|)
decl_stmt|;
name|String
name|destResource
init|=
name|VfsTestHelper
operator|.
name|SCRATCH_DIR
operator|+
literal|"/"
operator|+
name|testResource
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|concat
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
argument_list|,
name|destResource
argument_list|)
argument_list|)
decl_stmt|;
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|destFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|destResource
argument_list|)
control|)
block|{
name|repo
operator|.
name|put
argument_list|(
operator|new
name|File
argument_list|(
name|srcFile
argument_list|)
argument_list|,
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test the retrieval of an artifact from the repository creating a new artifact      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testGetNoExisting
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
name|String
name|testFile
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|scratchDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|testResource
argument_list|)
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|testResource
argument_list|)
control|)
block|{
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|repo
operator|.
name|get
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|testFile
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected file: "
operator|+
name|testFile
operator|+
literal|"not found. Failed vfsURI: "
operator|+
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|testFile
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test the retrieval of an artifact from the repository overwriting an existing artifact      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testGetOverwriteExisting
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
name|File
name|testFile
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|concat
argument_list|(
name|scratchDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|testResource
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|testResource
argument_list|)
control|)
block|{
comment|// setup - remove existing scratch area and populate with a file to override
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|testFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|testFile
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|repo
operator|.
name|get
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
name|testFile
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected file: "
operator|+
name|testFile
operator|+
literal|"not found. Failed vfsURI: "
operator|+
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
name|testFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"Zero file size indicates file not overwritten"
argument_list|,
literal|0
argument_list|,
name|testFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we get a non null Resource instance when passed a well-formed VfsURI pointing      * to an existing file      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testGetResourceValidExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|testResource
argument_list|)
control|)
block|{
name|assertNotNull
argument_list|(
name|repo
operator|.
name|getResource
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we get a non null Resource instance when passed a well-formed VfsURI pointing      * to a non-existent file.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testGetResourceValidNoExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testResource
init|=
name|VfsTestHelper
operator|.
name|SCRATCH_DIR
operator|+
literal|"/nosuchfile.jar"
decl_stmt|;
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|testResource
argument_list|)
control|)
block|{
comment|// make sure the declared resource does not exist
if|if
condition|(
name|scratchDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|repo
operator|.
name|getResource
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

