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
name|assertFalse
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
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Arrays
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

begin_class
specifier|public
class|class
name|VfsResourceTest
block|{
specifier|private
name|VfsTestHelper
name|helper
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
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|helper
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Validate VFSResource creation for a valid VFS URI pointing to an physically existing file      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testCreateResourceThatExists
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
argument_list|)
control|)
block|{
name|String
name|resId
init|=
name|vfsURI
operator|.
name|toString
argument_list|()
decl_stmt|;
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|resId
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected null value on VFS URI: "
operator|+
name|resId
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Resource does not exist and it should: "
operator|+
name|resId
argument_list|,
name|res
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// VFS apparently does some weird normalization so that resource id used to create
comment|// the VFS resource is not necessarily identical to the id returned from the getName
comment|// method<sigh>. We try to work around this by transforming things into java URIs.
name|assertEquals
argument_list|(
literal|"Failed on getName. Expected: "
operator|+
name|resId
operator|+
literal|". Actual: "
operator|+
name|res
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|URI
argument_list|(
name|escapeUrl
argument_list|(
name|resId
argument_list|)
argument_list|)
argument_list|,
operator|new
name|URI
argument_list|(
name|escapeUrl
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"Expected non-null file modification date for URI: "
operator|+
name|resId
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
literal|"Expected non-zero file length for URI: "
operator|+
name|resId
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Physical existence check returned false for existing resource: "
operator|+
name|resId
argument_list|,
name|res
operator|.
name|physicallyExists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Escape invalid URL characters (Copied from Wicket, just use StringUtils instead of Strings)      *      * @param queryString      *            The original querystring      * @return url The querystring with invalid characters escaped      */
specifier|private
name|String
name|escapeUrl
parameter_list|(
name|String
name|queryString
parameter_list|)
block|{
return|return
name|queryString
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|"%22"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"%"
argument_list|,
literal|"%26"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"="
argument_list|,
literal|"%3D"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|"%2F"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\+"
argument_list|,
literal|"%2B"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"&"
argument_list|,
literal|"%26"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"~"
argument_list|,
literal|"%7E"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\?"
argument_list|,
literal|"%3F"
argument_list|)
return|;
block|}
comment|/**      * Validating that resource can be created for files which don't physically exists - e.g.      * resources that are going to created.      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testCreateResourceThatDoesntExist
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
literal|"zzyyxx.zzyyxx"
argument_list|)
control|)
block|{
name|String
name|resId
init|=
name|vfsURI
operator|.
name|toString
argument_list|()
decl_stmt|;
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|resId
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected null value on VFS URI: "
operator|+
name|resId
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Resource does exist and it should not: "
operator|+
name|resId
argument_list|,
name|res
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// VFS apparently does some weird normalization so that resource id used to create
comment|// the VFS resource is not necessarily identical to the id returned from the getName
comment|// method<sigh>. We try to work around this by transforming things into java URIs.
name|assertEquals
argument_list|(
literal|"Failed on getName. Expected: "
operator|+
name|resId
operator|+
literal|". Actual: "
operator|+
name|res
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|URI
argument_list|(
name|escapeUrl
argument_list|(
name|resId
argument_list|)
argument_list|)
argument_list|,
operator|new
name|URI
argument_list|(
name|escapeUrl
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|normalize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected null file modification date for URI: "
operator|+
name|resId
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected zero file length for URI: "
operator|+
name|resId
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Physical existence check returned true for non-existent resource: "
operator|+
name|resId
argument_list|,
name|res
operator|.
name|physicallyExists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate VFSResource creation when given a poorly formed VFS identifier      */
annotation|@
name|Test
specifier|public
name|void
name|testBadURI
parameter_list|()
block|{
name|String
name|vfsURI
init|=
literal|"smb1:/goobeldygook"
decl_stmt|;
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|vfsURI
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Unexpected null value on VFS URI: "
operator|+
name|vfsURI
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Resource is marked as existing and it should not: "
operator|+
name|vfsURI
argument_list|,
name|res
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Failed on getName. Expected: "
operator|+
name|vfsURI
operator|+
literal|". Actual: "
operator|+
name|res
operator|.
name|getName
argument_list|()
argument_list|,
name|res
operator|.
name|getName
argument_list|()
argument_list|,
literal|"smb1:/goobeldygook"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected null file modification date for URI: "
operator|+
name|vfsURI
operator|+
literal|": "
operator|+
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected zero file length for URI: "
operator|+
name|vfsURI
argument_list|,
literal|0
argument_list|,
name|res
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Physical existence check returned true for non-existent resource: "
operator|+
name|vfsURI
argument_list|,
name|res
operator|.
name|physicallyExists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Validate getChildren when given a VFS URI for a directory      */
annotation|@
name|Test
specifier|public
name|void
name|testListFolderChildren
parameter_list|()
block|{
specifier|final
name|String
name|testFolder
init|=
literal|"2/mod10.1"
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|expectedFiles
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"ivy-1.0.xml"
argument_list|,
literal|"ivy-1.1.xml"
argument_list|,
literal|"ivy-1.2.xml"
argument_list|,
literal|"ivy-1.3.xml"
argument_list|)
decl_stmt|;
for|for
control|(
name|VfsURI
name|baseVfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|testFolder
argument_list|)
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|expectedFile
range|:
name|expectedFiles
control|)
block|{
name|String
name|resId
init|=
name|baseVfsURI
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
operator|+
name|expectedFile
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
name|resId
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|actual
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|baseVfsURI
operator|.
name|toString
argument_list|()
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|resId
range|:
name|res
operator|.
name|getChildren
argument_list|()
control|)
block|{
comment|// remove entries ending in .svn
if|if
condition|(
operator|!
name|resId
operator|.
name|endsWith
argument_list|(
literal|".svn"
argument_list|)
condition|)
block|{
name|actual
operator|.
name|add
argument_list|(
name|resId
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|actual
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\nExpected: "
operator|+
name|expected
operator|.
name|toString
argument_list|()
operator|+
literal|"\nActual: "
operator|+
name|actual
operator|.
name|toString
argument_list|()
argument_list|,
name|actual
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we don't get any results when we query a VFSResource file object for its      * children      */
annotation|@
name|Test
specifier|public
name|void
name|testListFileChildren
parameter_list|()
block|{
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
name|VfsTestHelper
operator|.
name|TEST_IVY_XML
argument_list|)
control|)
block|{
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
name|res
operator|.
name|getChildren
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getChildren query on file provided results when it shouldn't have"
argument_list|,
literal|0
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate that we don't get any results if we ask an IMAGINARY VFSResource - a nonexistent      * file - for a list of its children      */
annotation|@
name|Test
specifier|public
name|void
name|testListImaginary
parameter_list|()
block|{
for|for
control|(
name|VfsURI
name|vfsURI
range|:
name|helper
operator|.
name|createVFSUriSet
argument_list|(
literal|"idontexistzzxx"
argument_list|)
control|)
block|{
name|VfsResource
name|res
init|=
operator|new
name|VfsResource
argument_list|(
name|vfsURI
operator|.
name|toString
argument_list|()
argument_list|,
name|helper
operator|.
name|fsManager
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
name|res
operator|.
name|getChildren
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getChildren query on file provided results when it shouldn't have"
argument_list|,
literal|0
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

