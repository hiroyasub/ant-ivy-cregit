begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
specifier|public
name|void
name|testIsReachable
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
block|}
end_class

end_unit

