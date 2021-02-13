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
name|version
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
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|latest
operator|.
name|LatestRevisionStrategy
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
name|VersionRangeMatcherTest
block|{
specifier|private
specifier|final
name|VersionMatcher
name|vm
init|=
operator|new
name|VersionRangeMatcher
argument_list|(
literal|"range"
argument_list|,
operator|new
name|LatestRevisionStrategy
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testMavenExcludeParenthesis
parameter_list|()
block|{
name|assertAccept
argument_list|(
literal|"[3.8,4.0)"
argument_list|,
literal|"3.7"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[3.8,4.0)"
argument_list|,
literal|"3.8"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[3.8,4.0)"
argument_list|,
literal|"3.9"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[3.8,4.0)"
argument_list|,
literal|"4.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[3.8,4.0)"
argument_list|,
literal|"4.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0]"
argument_list|,
literal|"3.7"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0]"
argument_list|,
literal|"3.8"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0]"
argument_list|,
literal|"3.9"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0]"
argument_list|,
literal|"4.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0]"
argument_list|,
literal|"4.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0)"
argument_list|,
literal|"3.7"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0)"
argument_list|,
literal|"3.8"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0)"
argument_list|,
literal|"3.9"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0)"
argument_list|,
literal|"4.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(3.8,4.0)"
argument_list|,
literal|"4.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[2.3.0,3.0.0)"
argument_list|,
literal|"3.0.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[2.3.0,3.0.0)"
argument_list|,
literal|"2.3.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDynamic
parameter_list|()
block|{
name|assertDynamic
argument_list|(
literal|"lastest.integration"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0]"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"(1.0)"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0;2.0]"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0,2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0,2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"(,1.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"(1.0, 2.0)"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"(1.0, 2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, 2.0)"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, 2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0, 2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, 2.0 ]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0, 2.0 ]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, 2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0, 2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, 2.0 ["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0, 2.0 ["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0, 2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"] 1.0, 2.0["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0, 2.0 ["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"] 1.0, 2.0 ["
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0, 2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"] 1.0, 2.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"]1.0, 2.0 ]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"] 1.0, 2.0 ]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[1.0, )"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0,)"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"[ 1.0, )"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"( ,1.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"(, 1.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"( , 1.0]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertDynamic
argument_list|(
literal|"( , 1.0 ]"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncludingFinite
parameter_list|()
block|{
name|assertAccept
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|"0.9"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|"2.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|"1.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0]"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0, 2.0]"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0, 2.0 ]"
argument_list|,
literal|"0.9"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0, 2.0]"
argument_list|,
literal|"2.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[ 1.0,2.0]"
argument_list|,
literal|"1.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[ 1.0 , 2.0 ]"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExcludingFinite
parameter_list|()
block|{
name|assertAccept
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|"0.9"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|"2.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0]"
argument_list|,
literal|"1.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|"1.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0["
argument_list|,
literal|"1.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,2.0["
argument_list|,
literal|"2.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0["
argument_list|,
literal|"2.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,2.0]"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[2.3.0,3.0.0["
argument_list|,
literal|"3.0.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[2.3.0,3.0.0["
argument_list|,
literal|"2.3.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncludingInfinite
parameter_list|()
block|{
name|assertAccept
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|"3.5.6"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|"1.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0,)"
argument_list|,
literal|"0.9"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0]"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0]"
argument_list|,
literal|"0.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0]"
argument_list|,
literal|"0.2.4"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0]"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0]"
argument_list|,
literal|"2.3"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0, )"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0 ,)"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[1.0 , )"
argument_list|,
literal|"3.5.6"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"[ 1.0, )"
argument_list|,
literal|"1.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExcludingInfinite
parameter_list|()
block|{
name|assertAccept
argument_list|(
literal|"]1.0,)"
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,)"
argument_list|,
literal|"2.0"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,)"
argument_list|,
literal|"3.5.6"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,)"
argument_list|,
literal|"1.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"]1.0,)"
argument_list|,
literal|"0.9"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0["
argument_list|,
literal|"1.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0["
argument_list|,
literal|"0.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0["
argument_list|,
literal|"0.2.4"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0["
argument_list|,
literal|"2.0"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
literal|"(,2.0["
argument_list|,
literal|"2.3"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// assertion helper methods
specifier|private
name|void
name|assertDynamic
parameter_list|(
name|String
name|askedVersion
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|b
argument_list|,
name|vm
operator|.
name|isDynamic
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"name"
argument_list|,
name|askedVersion
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAccept
parameter_list|(
name|String
name|askedVersion
parameter_list|,
name|String
name|depVersion
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|b
argument_list|,
name|vm
operator|.
name|accept
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"name"
argument_list|,
name|askedVersion
argument_list|)
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"name"
argument_list|,
name|depVersion
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

