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
name|URL
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
name|url
operator|.
name|URLHandler
operator|.
name|URLInfo
import|;
end_import

begin_comment
comment|/**  * Test HttpClientHandler  */
end_comment

begin_class
specifier|public
class|class
name|HttpclientURLHandlerTest
extends|extends
name|TestCase
block|{
comment|// remote.test
specifier|private
name|File
name|testDir
decl_stmt|;
specifier|private
name|HttpClientHandler
name|handler
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|testDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/HttpclientURLHandlerTest"
argument_list|)
expr_stmt|;
name|testDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|handler
operator|=
operator|new
name|HttpClientHandler
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
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsReachable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|handler
operator|.
name|isReachable
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://www.google.fr/"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|handler
operator|.
name|isReachable
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://www.google.fr/unknownpage.html"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetURLInfo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// IVY-390
name|URLHandler
name|handler
init|=
operator|new
name|HttpClientHandler
argument_list|()
decl_stmt|;
name|URLInfo
name|info
init|=
name|handler
operator|.
name|getURLInfo
argument_list|(
operator|new
name|URL
argument_list|(
literal|"https://repo1.maven.org/maven2/commons-lang/commons-lang/[1.0,3.0[/commons-lang-[1.0,3.0[.pom"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|URLHandler
operator|.
name|UNAVAILABLE
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testContentEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/daniels.html"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"gzip.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/daniels.html?deflate=on&zlib=on"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"deflate-zlib.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/daniels.html?deflate=on"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"deflate.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/a5.ps"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"a5-gzip.ps"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/a5.ps?deflate=on"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"a5-deflate.ps"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/nh80.pdf"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"nh80-gzip.pdf"
argument_list|)
argument_list|)
expr_stmt|;
name|assertDownloadOK
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://carsten.codimi.de/gzip.yaws/nh80.pdf?deflate=on"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"nh80-deflate.pdf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertDownloadOK
parameter_list|(
name|URL
name|url
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|handler
operator|.
name|download
argument_list|(
name|url
argument_list|,
name|file
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

