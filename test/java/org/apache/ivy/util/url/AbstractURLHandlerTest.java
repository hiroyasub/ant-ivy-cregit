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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|CopyProgressListener
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
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|AbstractURLHandlerTest
block|{
comment|/**      * JUnit test for IVY-923.      */
annotation|@
name|Test
specifier|public
name|void
name|testNormalizeToStringWithSpaceURL
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractURLHandler
name|handler
init|=
operator|new
name|TestURLHandler
argument_list|()
decl_stmt|;
name|String
name|normalizedUrl
init|=
name|handler
operator|.
name|normalizeToString
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://ant.apache.org/ivy/url with space/ivy-1.0.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://ant.apache.org/ivy/url%20with%20space/ivy-1.0.xml"
argument_list|,
name|normalizedUrl
argument_list|)
expr_stmt|;
block|}
comment|/**      * JUnit test for IVY-923.      */
annotation|@
name|Test
specifier|public
name|void
name|testNormalizeToStringWithPlusCharacter
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractURLHandler
name|handler
init|=
operator|new
name|TestURLHandler
argument_list|()
decl_stmt|;
name|String
name|normalizedUrl
init|=
name|handler
operator|.
name|normalizeToString
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://ant.apache.org/ivy/ivy-1.+.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://ant.apache.org/ivy/ivy-1.%2B.xml"
argument_list|,
name|normalizedUrl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNormalizeToStringWithUnderscoreInHostname
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractURLHandler
name|handler
init|=
operator|new
name|TestURLHandler
argument_list|()
decl_stmt|;
name|String
name|normalizedUrl
init|=
name|handler
operator|.
name|normalizeToString
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://peat_hal.users.sourceforge.net/m2repository"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://peat_hal.users.sourceforge.net/m2repository"
argument_list|,
name|normalizedUrl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNormalizeToStringWithUnderscoreInHostnameAndSpaceInPath
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractURLHandler
name|handler
init|=
operator|new
name|TestURLHandler
argument_list|()
decl_stmt|;
name|String
name|normalizedUrl
init|=
name|handler
operator|.
name|normalizeToString
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://peat_hal.users.sourceforge.net/m2 repository"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://peat_hal.users.sourceforge.net/m2%20repository"
argument_list|,
name|normalizedUrl
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestURLHandler
extends|extends
name|AbstractURLHandler
block|{
specifier|public
name|void
name|download
parameter_list|(
name|URL
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|download
parameter_list|(
specifier|final
name|URL
name|src
parameter_list|,
specifier|final
name|File
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|listener
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|openStream
parameter_list|(
specifier|final
name|URL
name|url
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|upload
parameter_list|(
name|File
name|src
parameter_list|,
name|URL
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|upload
parameter_list|(
specifier|final
name|File
name|src
parameter_list|,
specifier|final
name|URL
name|dest
parameter_list|,
specifier|final
name|CopyProgressListener
name|listener
parameter_list|,
specifier|final
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

