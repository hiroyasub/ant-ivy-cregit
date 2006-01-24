begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
package|;
end_package

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
name|text
operator|.
name|ParseException
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

begin_class
specifier|public
class|class
name|ConfigureTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configureDefault
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|ivy
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

