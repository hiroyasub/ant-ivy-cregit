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
name|plugins
operator|.
name|namespace
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|MRIDTransformationRuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTransformation
parameter_list|()
block|{
name|MRIDTransformationRule
name|r
init|=
operator|new
name|MRIDTransformationRule
argument_list|()
decl_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"apache"
argument_list|,
literal|"commons.+"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$m0"
argument_list|,
literal|"$m0"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-client"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|MRIDTransformationRule
argument_list|()
expr_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|null
argument_list|,
literal|"commons\\-(.+)"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$o0.commons"
argument_list|,
literal|"$m1"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache.commons"
argument_list|,
literal|"client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|MRIDTransformationRule
argument_list|()
expr_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"(.+)\\.(.+)"
argument_list|,
literal|".+"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$o1"
argument_list|,
literal|"$o2-$m0"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache.commons"
argument_list|,
literal|"client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

