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
operator|.
name|ant
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|FileReader
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
name|InputStreamReader
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
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|IvyConvertPomTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyConvertPom
name|task
init|=
operator|new
name|IvyConvertPom
argument_list|()
decl_stmt|;
name|task
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|setPomFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/test.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
literal|".xml"
argument_list|)
decl_stmt|;
name|destFile
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|task
operator|.
name|setIvyFile
argument_list|(
name|destFile
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|String
name|wrote
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|destFile
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-convertpom.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|readEntirely
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FileUtil
operator|.
name|readEntirely
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|IvyConvertPomTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|resource
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

