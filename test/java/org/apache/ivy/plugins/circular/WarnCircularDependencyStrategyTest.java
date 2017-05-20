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
name|circular
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
name|TestHelper
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
name|IvyContext
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
name|event
operator|.
name|EventManager
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
name|resolve
operator|.
name|ResolveData
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
name|resolve
operator|.
name|ResolveEngine
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
name|resolve
operator|.
name|ResolveOptions
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
name|settings
operator|.
name|IvySettings
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
name|sort
operator|.
name|SortEngine
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
name|util
operator|.
name|MessageLoggerEngine
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
name|util
operator|.
name|MockMessageLogger
import|;
end_import

begin_class
specifier|public
class|class
name|WarnCircularDependencyStrategyTest
extends|extends
name|TestCase
block|{
specifier|private
name|CircularDependencyStrategy
name|strategy
decl_stmt|;
specifier|private
name|MessageLoggerEngine
name|loggerEngine
decl_stmt|;
specifier|private
name|MockMessageLogger
name|mockMessageLogger
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup a new IvyContext for each test
name|IvyContext
operator|.
name|pushNewContext
argument_list|()
expr_stmt|;
name|strategy
operator|=
name|WarnCircularDependencyStrategy
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|mockMessageLogger
operator|=
operator|new
name|MockMessageLogger
argument_list|()
expr_stmt|;
name|loggerEngine
operator|=
name|setupMockLogger
argument_list|(
name|mockMessageLogger
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|resetMockLogger
argument_list|(
name|loggerEngine
argument_list|)
expr_stmt|;
comment|// pop the context we setup before
name|IvyContext
operator|.
name|popContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testLog
parameter_list|()
throws|throws
name|Exception
block|{
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.0, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|mockMessageLogger
operator|.
name|assertLogWarningContains
argument_list|(
literal|"circular dependency found: #A;1.0->#B;1.0"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveDuplicates
parameter_list|()
throws|throws
name|Exception
block|{
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
comment|// should only log the circular dependency once
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockMessageLogger
operator|.
name|getWarns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveDuplicates2
parameter_list|()
throws|throws
name|Exception
block|{
name|setResolveContext
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
comment|// should only log the circular dependency once
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockMessageLogger
operator|.
name|getWarns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|setResolveContext
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
comment|// clear previous logs
name|mockMessageLogger
operator|.
name|clear
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
comment|// should log the message
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockMessageLogger
operator|.
name|getWarns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|handleCircularDependency
argument_list|(
name|TestHelper
operator|.
name|parseMridsToArray
argument_list|(
literal|"#A;1.1, #B;1.0"
argument_list|)
argument_list|)
expr_stmt|;
comment|// should not log the message again
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockMessageLogger
operator|.
name|getWarns
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setResolveContext
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
name|IvySettings
name|settings
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setResolveData
argument_list|(
operator|new
name|ResolveData
argument_list|(
operator|new
name|ResolveEngine
argument_list|(
name|settings
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|,
operator|new
name|SortEngine
argument_list|(
name|settings
argument_list|)
argument_list|)
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setResolveId
argument_list|(
name|resolveId
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MessageLoggerEngine
name|setupMockLogger
parameter_list|(
specifier|final
name|MockMessageLogger
name|mockLogger
parameter_list|)
block|{
if|if
condition|(
name|mockLogger
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|MessageLoggerEngine
name|loggerEngine
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|.
name|getLoggerEngine
argument_list|()
decl_stmt|;
name|loggerEngine
operator|.
name|pushLogger
argument_list|(
name|mockLogger
argument_list|)
expr_stmt|;
return|return
name|loggerEngine
return|;
block|}
specifier|private
name|void
name|resetMockLogger
parameter_list|(
specifier|final
name|MessageLoggerEngine
name|loggerEngine
parameter_list|)
block|{
if|if
condition|(
name|loggerEngine
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|loggerEngine
operator|.
name|popLogger
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

