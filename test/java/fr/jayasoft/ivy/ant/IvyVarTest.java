begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|Ivy
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

begin_class
specifier|public
class|class
name|IvyVarTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSimple
parameter_list|()
block|{
name|IvyVar
name|task
init|=
operator|new
name|IvyVar
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
name|setName
argument_list|(
literal|"mytest"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setValue
argument_list|(
literal|"myvalue"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|task
operator|.
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"mytest"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPrefix
parameter_list|()
block|{
name|IvyVar
name|task
init|=
operator|new
name|IvyVar
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
name|setName
argument_list|(
literal|"mytest"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setValue
argument_list|(
literal|"myvalue"
argument_list|)
expr_stmt|;
name|task
operator|.
name|setPrefix
argument_list|(
literal|"myprefix"
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|task
operator|.
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"myprefix.mytest"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testURL
parameter_list|()
block|{
name|IvyVar
name|task
init|=
operator|new
name|IvyVar
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
name|setUrl
argument_list|(
name|IvyVarTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"vartest.properties"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|task
operator|.
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue1"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"mytest1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue2"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"mytest2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testURLPrefix
parameter_list|()
block|{
name|IvyVar
name|task
init|=
operator|new
name|IvyVar
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
name|setUrl
argument_list|(
name|IvyVarTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"vartest.properties"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|setPrefix
argument_list|(
literal|"myprefix."
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|task
operator|.
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue1"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"myprefix.mytest1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue2"
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"myprefix.mytest2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

