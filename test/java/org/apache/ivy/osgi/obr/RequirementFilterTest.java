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
name|osgi
operator|.
name|obr
package|;
end_package

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
name|Assert
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
name|osgi
operator|.
name|obr
operator|.
name|filter
operator|.
name|AndFilter
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
name|osgi
operator|.
name|obr
operator|.
name|filter
operator|.
name|CompareFilter
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
name|osgi
operator|.
name|obr
operator|.
name|filter
operator|.
name|CompareFilter
operator|.
name|Operator
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
name|osgi
operator|.
name|obr
operator|.
name|filter
operator|.
name|RequirementFilterParser
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
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|RequirementFilter
import|;
end_import

begin_class
specifier|public
class|class
name|RequirementFilterTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testParser
parameter_list|()
throws|throws
name|Exception
block|{
name|assertParseFail
argument_list|(
literal|"c>2"
argument_list|)
expr_stmt|;
name|assertParseFail
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertParseFail
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|RequirementFilter
name|cgt2
init|=
operator|new
name|CompareFilter
argument_list|(
literal|"c"
argument_list|,
name|Operator
operator|.
name|GREATER_THAN
argument_list|,
literal|"2"
argument_list|)
decl_stmt|;
name|checkParse
argument_list|(
name|cgt2
argument_list|,
literal|"(c>2)"
argument_list|)
expr_stmt|;
name|RequirementFilter
name|twoeqd
init|=
operator|new
name|CompareFilter
argument_list|(
literal|"2"
argument_list|,
name|Operator
operator|.
name|EQUALS
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|checkParse
argument_list|(
name|twoeqd
argument_list|,
literal|"(2=d)"
argument_list|)
expr_stmt|;
name|RequirementFilter
name|foodorbarge0dot0
init|=
operator|new
name|CompareFilter
argument_list|(
literal|"foo.bar"
argument_list|,
name|Operator
operator|.
name|GREATER_OR_EQUAL
argument_list|,
literal|"0.0"
argument_list|)
decl_stmt|;
name|checkParse
argument_list|(
name|foodorbarge0dot0
argument_list|,
literal|"(foo.bar>=0.0)"
argument_list|)
expr_stmt|;
name|RequirementFilter
name|and
init|=
operator|new
name|AndFilter
argument_list|(
operator|new
name|RequirementFilter
index|[]
block|{
name|foodorbarge0dot0
block|}
argument_list|)
decl_stmt|;
name|checkParse
argument_list|(
name|and
argument_list|,
literal|"(&(foo.bar>=0.0))"
argument_list|)
expr_stmt|;
name|RequirementFilter
name|and2
init|=
operator|new
name|AndFilter
argument_list|(
operator|new
name|RequirementFilter
index|[]
block|{
name|cgt2
block|,
name|twoeqd
block|,
name|foodorbarge0dot0
block|}
argument_list|)
decl_stmt|;
name|checkParse
argument_list|(
name|and2
argument_list|,
literal|"(&(c>2)(2=d)(foo.bar>=0.0))"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertParseFail
parameter_list|(
name|String
name|toParse
parameter_list|)
block|{
try|try
block|{
name|RequirementFilterParser
operator|.
name|parse
argument_list|(
name|toParse
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Expecting a ParseException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
comment|// OK
block|}
block|}
specifier|private
name|void
name|checkParse
parameter_list|(
name|RequirementFilter
name|expected
parameter_list|,
name|String
name|toParse
parameter_list|)
throws|throws
name|ParseException
block|{
name|RequirementFilter
name|parsed
init|=
name|RequirementFilterParser
operator|.
name|parse
argument_list|(
name|toParse
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|parsed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

