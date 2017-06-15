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
name|core
package|;
end_package

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
name|net
operator|.
name|MalformedURLException
import|;
end_import

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

begin_class
specifier|public
class|class
name|NormalRelativeUrlResolverTest
block|{
specifier|private
name|NormalRelativeUrlResolver
name|t
init|=
operator|new
name|NormalRelativeUrlResolver
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testRelativeHttpURL
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"http://xxx/file.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://xxx/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"file2.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelativeFileURL
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"file://xxx/file.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://xxx/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"file2.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelativeMixedURL
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"http://xxx/file.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"file://file2.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileAndUrlWithAbsoluteFile
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"file://xxx/file.txt"
argument_list|)
decl_stmt|;
name|File
name|absFile
init|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|absFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
name|absFile
operator|.
name|toString
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|absFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
name|absFile
operator|.
name|toString
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|absFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
name|absFile
operator|.
name|toString
argument_list|()
argument_list|,
literal|"somthing.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileAndUrlWithRelativeFile
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"file://xxx/file.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://xxx/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"file2.txt"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://xxx/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"file2.txt"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://xxx/sub/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|"sub/file2.txt"
argument_list|,
literal|"something"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileAndUrlWithAbsoluteUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"file://xxx/file.txt"
argument_list|)
decl_stmt|;
name|URL
name|otherBase
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:80/otherfile.txt"
argument_list|)
decl_stmt|;
name|String
name|absUrl
init|=
literal|"http://ibiblio.org/dir/file.txt"
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
name|absUrl
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|null
argument_list|,
name|absUrl
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
name|absUrl
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|otherBase
argument_list|,
literal|null
argument_list|,
name|absUrl
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileAndUrlWithRelativeUrl
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|URL
name|base
init|=
operator|new
name|URL
argument_list|(
literal|"file://xxx/file.txt"
argument_list|)
decl_stmt|;
name|URL
name|otherBase
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:80/otherfile.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"file://xxx/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|base
argument_list|,
literal|null
argument_list|,
literal|"file2.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:80/file2.txt"
argument_list|)
argument_list|,
name|t
operator|.
name|getURL
argument_list|(
name|otherBase
argument_list|,
literal|null
argument_list|,
literal|"file2.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

