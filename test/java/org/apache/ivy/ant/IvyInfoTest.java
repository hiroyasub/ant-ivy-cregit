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
name|ant
package|;
end_package

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

begin_class
specifier|public
class|class
name|IvyInfoTest
extends|extends
name|TestCase
block|{
specifier|private
name|IvyInfo
name|info
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|info
operator|=
operator|new
name|IvyInfo
argument_list|()
expr_stmt|;
name|info
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|info
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.public.configurations"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAll
parameter_list|()
throws|throws
name|Exception
block|{
name|info
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-info-all.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"apache"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"info-all"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.revision"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"release"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.status"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default, test, private"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default, test"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.public.configurations"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"trunk"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.branch"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.extra.myextraatt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

