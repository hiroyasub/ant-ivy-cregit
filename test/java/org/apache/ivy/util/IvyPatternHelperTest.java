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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|IvyPatternHelper
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|IvyPatternHelperTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSubstitute
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"[organisation]/[module]/build/archives/[type]s/[artifact]-[revision].[ext]"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache/Test/build/archives/jars/test-1.0.jar"
argument_list|,
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"apache"
argument_list|,
literal|"Test"
argument_list|,
literal|"1.0"
argument_list|,
literal|"test"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCyclicSubstitute
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"${var}"
decl_stmt|;
name|Map
name|variables
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|variables
operator|.
name|put
argument_list|(
literal|"var"
argument_list|,
literal|"${othervar}"
argument_list|)
expr_stmt|;
name|variables
operator|.
name|put
argument_list|(
literal|"othervar"
argument_list|,
literal|"${var}"
argument_list|)
expr_stmt|;
try|try
block|{
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|pattern
argument_list|,
name|variables
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"cyclic var should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ok
block|}
catch|catch
parameter_list|(
name|Error
name|er
parameter_list|)
block|{
name|fail
argument_list|(
literal|"cyclic var shouldn't raise an error: "
operator|+
name|er
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOptionalSubstitute
parameter_list|()
block|{
name|Map
name|tokens
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|tokens
operator|.
name|put
argument_list|(
literal|"token"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
literal|"othertoken"
argument_list|,
literal|"myval"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-myval"
argument_list|,
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
literal|"test(-[token])(-[othertoken])"
argument_list|,
name|tokens
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
literal|"token"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-val-myval"
argument_list|,
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
literal|"test(-[token])(-[othertoken])"
argument_list|,
name|tokens
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrganization
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"[organization]/[module]/build/archives/[type]s/[artifact]-[revision].[ext]"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache/Test/build/archives/jars/test-1.0.jar"
argument_list|,
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"apache"
argument_list|,
literal|"Test"
argument_list|,
literal|"1.0"
argument_list|,
literal|"test"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpecialCharsInsidePattern
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"[organization]/[module]/build/archives (x86)/[type]s/[artifact]-[revision].[ext]"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"apache/Test/build/archives (x86)/jars/test-1.0.jar"
argument_list|,
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"apache"
argument_list|,
literal|"Test"
argument_list|,
literal|"1.0"
argument_list|,
literal|"test"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTokenRoot
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"lib/[type]/[artifact].[ext]"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"lib/"
argument_list|,
name|IvyPatternHelper
operator|.
name|getTokenRoot
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTokenRootWithOptionalFirstToken
parameter_list|()
block|{
name|String
name|pattern
init|=
literal|"lib/([type]/)[artifact].[ext]"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"lib/"
argument_list|,
name|IvyPatternHelper
operator|.
name|getTokenRoot
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

