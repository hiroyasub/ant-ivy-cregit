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
name|settings
operator|.
name|NamedTimeoutConstraint
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
name|settings
operator|.
name|TimeoutConstraint
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
name|Assert
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
name|net
operator|.
name|InetSocketAddress
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
name|Random
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|resolver
operator|.
name|IBiblioResolver
operator|.
name|DEFAULT_M2_ROOT
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Test {@link HttpClientHandler}  */
end_comment

begin_class
specifier|public
class|class
name|HttpclientURLHandlerTest
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
specifier|private
specifier|final
name|TimeoutConstraint
name|defaultTimeoutConstraint
decl_stmt|;
block|{
name|defaultTimeoutConstraint
operator|=
operator|new
name|NamedTimeoutConstraint
argument_list|(
literal|"default-http-client-handler-timeout"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|NamedTimeoutConstraint
operator|)
name|defaultTimeoutConstraint
operator|)
operator|.
name|setConnectionTimeout
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
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
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
try|try
block|{
name|handler
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsReachable
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"URL resource was expected to be reachable"
argument_list|,
name|handler
operator|.
name|isReachable
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://www.google.fr/"
argument_list|)
argument_list|,
name|defaultTimeoutConstraint
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"URL resource was expected to be unreachable"
argument_list|,
name|handler
operator|.
name|isReachable
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://www.google.fr/unknownpage.html"
argument_list|)
argument_list|,
name|defaultTimeoutConstraint
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for IVY-390.      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-390">IVY-390</a>      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testGetURLInfo
parameter_list|()
throws|throws
name|Exception
block|{
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
name|DEFAULT_M2_ROOT
operator|+
literal|"/commons-lang/commons-lang/[1.0,3.0[/commons-lang-[1.0,3.0[.pom"
argument_list|)
argument_list|,
name|defaultTimeoutConstraint
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
annotation|@
name|Test
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
comment|/**      * Tests that the {@link HttpClientHandler}, backed by      * {@link CredentialsStore Ivy credentials store} works as expected when it interacts      * with a HTTP server which requires authentication for accessing resources.      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1336">IVY-1336</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testCredentials
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CredentialsStore
name|credentialsStore
init|=
name|CredentialsStore
operator|.
name|INSTANCE
decl_stmt|;
specifier|final
name|String
name|realm
init|=
literal|"test-http-client-handler-realm"
decl_stmt|;
specifier|final
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
specifier|final
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
specifier|final
name|String
name|userName
init|=
literal|"test-http-user-"
operator|+
name|random
operator|.
name|nextInt
argument_list|()
decl_stmt|;
specifier|final
name|String
name|password
init|=
literal|"pass-"
operator|+
name|random
operator|.
name|nextInt
argument_list|()
decl_stmt|;
name|credentialsStore
operator|.
name|addCredentials
argument_list|(
name|realm
argument_list|,
name|host
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
specifier|final
name|InetSocketAddress
name|serverBindAddr
init|=
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|12345
argument_list|)
decl_stmt|;
specifier|final
name|String
name|contextRoot
init|=
literal|"/testHttpClientHandler"
decl_stmt|;
specifier|final
name|Path
name|repoRoot
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories"
argument_list|)
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|repoRoot
operator|+
literal|" is not a directory"
argument_list|,
name|Files
operator|.
name|isDirectory
argument_list|(
name|repoRoot
argument_list|)
argument_list|)
expr_stmt|;
comment|// create a server backed by BASIC auth with the set of "allowed" credentials
try|try
init|(
specifier|final
name|AutoCloseable
name|server
init|=
name|TestHelper
operator|.
name|createBasicAuthHttpServerBackedRepo
argument_list|(
name|serverBindAddr
argument_list|,
name|contextRoot
argument_list|,
name|repoRoot
argument_list|,
name|realm
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
argument_list|)
init|)
block|{
specifier|final
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"downloaded.xml"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|target
operator|+
literal|" already exists"
argument_list|,
name|target
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|URL
name|src
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|serverBindAddr
operator|.
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|contextRoot
operator|+
literal|"/ivysettings.xml"
argument_list|)
decl_stmt|;
comment|// download it
name|handler
operator|.
name|download
argument_list|(
name|src
argument_list|,
name|target
argument_list|,
literal|null
argument_list|,
name|defaultTimeoutConstraint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"File "
operator|+
name|target
operator|+
literal|" was not downloaded from "
operator|+
name|src
argument_list|,
name|target
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// now create a server backed by BASIC auth with a set of credentials that do *not* match
comment|// with what the Ivy credentials store will return for a given realm+host combination, i.e.
comment|// Ivy credential store will return back invalid credentials and the server will reject them
try|try
init|(
specifier|final
name|AutoCloseable
name|server
init|=
name|TestHelper
operator|.
name|createBasicAuthHttpServerBackedRepo
argument_list|(
name|serverBindAddr
argument_list|,
name|contextRoot
argument_list|,
name|repoRoot
argument_list|,
name|realm
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"other-"
operator|+
name|userName
argument_list|,
literal|"other-"
operator|+
name|password
argument_list|)
argument_list|)
init|)
block|{
specifier|final
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
literal|"should-not-have-been-downloaded.xml"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File "
operator|+
name|target
operator|+
literal|" already exists"
argument_list|,
name|target
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|URL
name|src
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|serverBindAddr
operator|.
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|contextRoot
operator|+
literal|"/ivysettings.xml"
argument_list|)
decl_stmt|;
comment|// download it (expected to fail)
try|try
block|{
name|handler
operator|.
name|download
argument_list|(
name|src
argument_list|,
name|target
argument_list|,
literal|null
argument_list|,
name|defaultTimeoutConstraint
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Download from "
operator|+
name|src
operator|+
literal|" was expected to fail due to invalid credentials"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|// we catch it and check for presence of 401 in the exception message.
comment|// It's not exactly an contract that the IOException will have the 401 message
comment|// but for now that's how it's implemented and it's fine to check for the presence
comment|// of that message at the moment
name|assertTrue
argument_list|(
literal|"Expected to find 401 error message in exception"
argument_list|,
name|ioe
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"401"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|assertDownloadOK
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
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
argument_list|,
name|defaultTimeoutConstraint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Content from "
operator|+
name|url
operator|+
literal|" wasn't downloaded to "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected content at "
operator|+
name|file
operator|+
literal|" for resource that was downloaded from "
operator|+
name|url
argument_list|,
name|file
operator|.
name|isFile
argument_list|()
operator|&&
name|file
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

