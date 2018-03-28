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
name|plugins
operator|.
name|matcher
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Base test classes for PatternMatcher testcase implementation  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPatternMatcherTest
block|{
specifier|protected
name|PatternMatcher
name|patternMatcher
decl_stmt|;
comment|// used by setUp() in subclasses
specifier|protected
name|void
name|setUp
parameter_list|(
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|patternMatcher
operator|=
name|matcher
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnyExpression
parameter_list|()
block|{
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|(
literal|"We shall transcend borders. The new is old."
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|(
literal|"        "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsExact
parameter_list|()
block|{
comment|// '*' is a special matcher
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|matches
argument_list|(
literal|"The words aren't what they were."
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
comment|// test some exact patterns for this matcher
name|String
index|[]
name|expressions
init|=
name|getExactExpressions
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|expression
range|:
name|expressions
control|)
block|{
name|matcher
operator|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expression '"
operator|+
name|expression
operator|+
literal|"' should be exact"
argument_list|,
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|matches
argument_list|(
literal|"The words aren't what they were."
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expression '"
operator|+
name|expression
operator|+
literal|"' should be exact"
argument_list|,
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// test some inexact patterns for this matcher
name|expressions
operator|=
name|getInexactExpressions
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|expression
range|:
name|expressions
control|)
block|{
name|matcher
operator|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Expression '"
operator|+
name|expression
operator|+
literal|"' should be inexact"
argument_list|,
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
name|matcher
operator|.
name|matches
argument_list|(
literal|"The words aren't what they were."
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Expression '"
operator|+
name|expression
operator|+
literal|"' should be inexact"
argument_list|,
name|matcher
operator|.
name|isExact
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|String
index|[]
name|getExactExpressions
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|String
index|[]
name|getInexactExpressions
parameter_list|()
function_decl|;
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNullInput
parameter_list|()
block|{
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|"some expression"
argument_list|)
decl_stmt|;
name|matcher
operator|.
name|matches
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|NullPointerException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNullExpression
parameter_list|()
block|{
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|testImplementation
parameter_list|()
function_decl|;
annotation|@
name|Test
specifier|public
name|void
name|testLoadTestMatches
parameter_list|()
block|{
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|"this.is.an.expression"
argument_list|)
decl_stmt|;
name|String
index|[]
name|inputs
init|=
block|{
literal|"this.is.an.expression"
block|,
literal|"this:is:an:expression"
block|,
literal|"this is an expression"
block|,
literal|"whatever this is"
block|,
literal|"maybe, maybe not"
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100000
condition|;
name|i
operator|++
control|)
block|{
name|String
name|input
init|=
name|inputs
index|[
name|i
operator|%
name|inputs
operator|.
name|length
index|]
decl_stmt|;
name|matcher
operator|.
name|matches
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadTestGetMatcher
parameter_list|()
block|{
name|String
index|[]
name|inputs
init|=
block|{
literal|"this.is.an.expression"
block|,
literal|"this:is:an:expression"
block|,
literal|"this is an expression"
block|,
literal|"whatever this is"
block|,
literal|"maybe, maybe not"
block|}
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100000
condition|;
name|i
operator|++
control|)
block|{
name|String
name|expression
init|=
name|inputs
index|[
name|i
operator|%
name|inputs
operator|.
name|length
index|]
decl_stmt|;
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

