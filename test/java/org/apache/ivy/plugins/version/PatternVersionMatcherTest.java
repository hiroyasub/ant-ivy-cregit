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
name|version
package|;
end_package

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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
import|;
end_import

begin_class
specifier|public
class|class
name|PatternVersionMatcherTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSingleMatch
parameter_list|()
block|{
name|PatternVersionMatcher
name|pvm
init|=
operator|new
name|PatternVersionMatcher
argument_list|()
decl_stmt|;
name|pvm
operator|.
name|addMatch
argument_list|(
name|generateRegexpMatch1
argument_list|()
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.4.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"2.3.1"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleMatchEqualRevisions
parameter_list|()
block|{
name|PatternVersionMatcher
name|pvm
init|=
operator|new
name|PatternVersionMatcher
argument_list|()
decl_stmt|;
name|pvm
operator|.
name|addMatch
argument_list|(
name|generateRegexpMatch1
argument_list|()
argument_list|)
expr_stmt|;
name|pvm
operator|.
name|addMatch
argument_list|(
name|generateRegexpMatch2
argument_list|()
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleMatchNonEqualRevisions
parameter_list|()
block|{
name|PatternVersionMatcher
name|pvm
init|=
operator|new
name|PatternVersionMatcher
argument_list|()
decl_stmt|;
name|pvm
operator|.
name|addMatch
argument_list|(
name|generateRegexpMatch1
argument_list|()
argument_list|)
expr_stmt|;
name|pvm
operator|.
name|addMatch
argument_list|(
name|generateRegexpMatch3
argument_list|()
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"bar(1,3)"
argument_list|,
literal|"1.3"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertAccept
argument_list|(
name|pvm
argument_list|,
literal|"foo(1,3)"
argument_list|,
literal|"1.3.1"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generates a Match instance that has the following xml representation:<match revision="foo"      * pattern="${major}\.${minor}\.\d+" args="major, minor" matcher="regexp" />      *       * @return      */
specifier|private
name|Match
name|generateRegexpMatch1
parameter_list|()
block|{
name|Match
name|match
init|=
operator|new
name|Match
argument_list|()
decl_stmt|;
name|match
operator|.
name|setRevision
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setPattern
argument_list|(
literal|"${major}\\.${minor}\\.\\d+"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setArgs
argument_list|(
literal|"major, minor"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setMatcher
argument_list|(
literal|"regexp"
argument_list|)
expr_stmt|;
return|return
name|match
return|;
block|}
comment|/**      * Generates a Match instance that has the following xml representation:<match revision="foo"      * pattern="${major}\.${minor}" args="major, minor" matcher="regexp" />      *       * @return      */
specifier|private
name|Match
name|generateRegexpMatch2
parameter_list|()
block|{
name|Match
name|match
init|=
operator|new
name|Match
argument_list|()
decl_stmt|;
name|match
operator|.
name|setRevision
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setPattern
argument_list|(
literal|"${major}\\.${minor}"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setArgs
argument_list|(
literal|"major, minor"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setMatcher
argument_list|(
literal|"regexp"
argument_list|)
expr_stmt|;
return|return
name|match
return|;
block|}
specifier|private
name|Match
name|generateRegexpMatch3
parameter_list|()
block|{
name|Match
name|match
init|=
operator|new
name|Match
argument_list|()
decl_stmt|;
name|match
operator|.
name|setRevision
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setPattern
argument_list|(
literal|"${major}\\.${minor}"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setArgs
argument_list|(
literal|"major, minor"
argument_list|)
expr_stmt|;
name|match
operator|.
name|setMatcher
argument_list|(
literal|"regexp"
argument_list|)
expr_stmt|;
return|return
name|match
return|;
block|}
specifier|private
name|void
name|assertAccept
parameter_list|(
name|PatternVersionMatcher
name|matcher
parameter_list|,
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
name|matcher
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

