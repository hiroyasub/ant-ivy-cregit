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
name|util
operator|.
name|url
package|;
end_package

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
import|import
name|java
operator|.
name|net
operator|.
name|Authenticator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|PasswordAuthentication
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
comment|/**  * Tests {@link IvyAuthenticator}  */
end_comment

begin_class
specifier|public
class|class
name|IvyAuthenticatorTest
block|{
specifier|private
name|Authenticator
name|previousAuthenticator
decl_stmt|;
specifier|private
name|TestAuthenticator
name|testAuthenticator
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|previousAuthenticator
operator|=
name|IvyAuthenticator
operator|.
name|getCurrentAuthenticator
argument_list|()
expr_stmt|;
name|this
operator|.
name|setupTestAuthenticator
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setupTestAuthenticator
parameter_list|()
block|{
name|this
operator|.
name|testAuthenticator
operator|=
operator|new
name|TestAuthenticator
argument_list|()
expr_stmt|;
comment|// first setup our TestAuthenticator
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|this
operator|.
name|testAuthenticator
argument_list|)
expr_stmt|;
comment|// now install IvyAuthenticator on top of it
name|IvyAuthenticator
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|after
parameter_list|()
block|{
comment|// reset to the authenticator that was around before the test was run
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|previousAuthenticator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that when {@link IvyAuthenticator} can't handle a authentication request and falls back      * on an authenticator that was previously set, before IvyAuthenticator installed on top of it,      * the other authenticator gets passed all the relevant requesting information, including the      * {@link Authenticator#getRequestingURL() requesting URL} and      * {@link Authenticator#getRequestorType() request type}      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1557">IVY-1557</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testRequestURLAndType
parameter_list|()
throws|throws
name|Exception
block|{
name|testAuthenticator
operator|.
name|expectedHost
operator|=
literal|"localhost"
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedPort
operator|=
literal|12345
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedPrompt
operator|=
literal|"Test prompt - testRequestURLAndType"
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedProtocol
operator|=
literal|"HTTP/1.1"
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedURL
operator|=
operator|new
name|URL
argument_list|(
literal|"http"
argument_list|,
literal|"localhost"
argument_list|,
literal|12345
argument_list|,
literal|"/a/b/c"
argument_list|)
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedType
operator|=
name|Authenticator
operator|.
name|RequestorType
operator|.
name|PROXY
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedScheme
operator|=
literal|"BASIC"
expr_stmt|;
name|testAuthenticator
operator|.
name|expectedSite
operator|=
name|InetAddress
operator|.
name|getLoopbackAddress
argument_list|()
expr_stmt|;
comment|// trigger the authentication
specifier|final
name|PasswordAuthentication
name|auth
init|=
name|Authenticator
operator|.
name|requestPasswordAuthentication
argument_list|(
name|testAuthenticator
operator|.
name|expectedHost
argument_list|,
name|testAuthenticator
operator|.
name|expectedSite
argument_list|,
name|testAuthenticator
operator|.
name|expectedPort
argument_list|,
name|testAuthenticator
operator|.
name|expectedProtocol
argument_list|,
name|testAuthenticator
operator|.
name|expectedPrompt
argument_list|,
name|testAuthenticator
operator|.
name|expectedScheme
argument_list|,
name|testAuthenticator
operator|.
name|expectedURL
argument_list|,
name|testAuthenticator
operator|.
name|expectedType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected a password authentication, but got none"
argument_list|,
name|auth
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected username"
argument_list|,
literal|"dummy"
argument_list|,
name|auth
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected password"
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
literal|"dummy"
operator|.
name|toCharArray
argument_list|()
argument_list|,
name|auth
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|TestAuthenticator
extends|extends
name|Authenticator
block|{
specifier|private
name|String
name|expectedHost
decl_stmt|;
specifier|private
name|int
name|expectedPort
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|String
name|expectedPrompt
decl_stmt|;
specifier|private
name|String
name|expectedProtocol
decl_stmt|;
specifier|private
name|String
name|expectedScheme
decl_stmt|;
specifier|private
name|URL
name|expectedURL
decl_stmt|;
specifier|private
name|RequestorType
name|expectedType
decl_stmt|;
specifier|private
name|InetAddress
name|expectedSite
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|PasswordAuthentication
name|getPasswordAuthentication
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Unexpected requesting host"
argument_list|,
name|expectedHost
argument_list|,
name|getRequestingHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected requesting port"
argument_list|,
name|expectedPort
argument_list|,
name|getRequestingPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected prompt"
argument_list|,
name|expectedPrompt
argument_list|,
name|getRequestingPrompt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected protocol"
argument_list|,
name|expectedProtocol
argument_list|,
name|getRequestingProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected scheme"
argument_list|,
name|expectedScheme
argument_list|,
name|getRequestingScheme
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected requesting URL"
argument_list|,
name|expectedURL
argument_list|,
name|getRequestingURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected requesting type"
argument_list|,
name|expectedType
argument_list|,
name|getRequestorType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected requesting site"
argument_list|,
name|expectedSite
argument_list|,
name|getRequestingSite
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|PasswordAuthentication
argument_list|(
literal|"dummy"
argument_list|,
literal|"dummy"
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

