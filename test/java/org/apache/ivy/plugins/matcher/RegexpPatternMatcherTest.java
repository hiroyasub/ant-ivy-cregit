begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_comment
comment|/**  * @see RegexpPatternMatcher  */
end_comment

begin_class
specifier|public
class|class
name|RegexpPatternMatcherTest
extends|extends
name|AbstractPatternMatcherTest
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
operator|new
name|RegexpPatternMatcher
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testImplementation
parameter_list|()
block|{
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|".*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|(
literal|".*"
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|matcher
operator|.
name|matches
argument_list|(
literal|"aa"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|matcher
operator|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail on invalid syntax"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

