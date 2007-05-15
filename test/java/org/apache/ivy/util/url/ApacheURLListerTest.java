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
name|util
operator|.
name|url
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests {@link ApacheURLLister}.  *  */
end_comment

begin_class
specifier|public
class|class
name|ApacheURLListerTest
extends|extends
name|TestCase
block|{
comment|/**      * Tests {@link ApacheURLLister#retrieveListing(URL, boolean, boolean)}.      *      * @throws Exception      */
specifier|public
name|void
name|testRetrieveListing
parameter_list|()
throws|throws
name|Exception
block|{
name|ApacheURLLister
name|lister
init|=
operator|new
name|ApacheURLLister
argument_list|()
decl_stmt|;
name|List
name|files
init|=
name|lister
operator|.
name|retrieveListing
argument_list|(
name|ApacheURLListerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"apache-file-listing.html"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|files
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|URL
name|file
init|=
operator|(
name|URL
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"found a non matching file: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|matches
argument_list|(
literal|".*/[^/]+\\.(jar|md5|sha1)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// try a directory listing
name|List
name|dirs
init|=
name|lister
operator|.
name|retrieveListing
argument_list|(
name|ApacheURLListerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"apache-dir-listing.html"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dirs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|dirs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|empty
init|=
name|lister
operator|.
name|retrieveListing
argument_list|(
name|ApacheURLListerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"apache-dir-listing.html"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|empty
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests {@link ApacheURLLister#retrieveListing(URL, boolean, boolean)}.      *      * @throws Exception      */
specifier|public
name|void
name|testRetrieveListingWithSpaces
parameter_list|()
throws|throws
name|Exception
block|{
name|ApacheURLLister
name|lister
init|=
operator|new
name|ApacheURLLister
argument_list|()
decl_stmt|;
name|List
name|files
init|=
name|lister
operator|.
name|retrieveListing
argument_list|(
name|ApacheURLListerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"listing-with-spaces.html"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

