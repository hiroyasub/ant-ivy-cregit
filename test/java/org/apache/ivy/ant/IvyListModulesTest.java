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
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|IvyListModulesTest
block|{
specifier|private
name|IvyListModules
name|findModules
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|TestHelper
operator|.
name|createCache
argument_list|()
expr_stmt|;
name|Project
name|project
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|findModules
operator|=
operator|new
name|IvyListModules
argument_list|()
expr_stmt|;
name|findModules
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|TestHelper
operator|.
name|cleanCache
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExact
parameter_list|()
throws|throws
name|Exception
block|{
name|findModules
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setProperty
argument_list|(
literal|"found"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setValue
argument_list|(
literal|"[organisation]/[module]/[revision]"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org1/mod1.1/1.0"
argument_list|,
name|findModules
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"found"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllRevs
parameter_list|()
throws|throws
name|Exception
block|{
name|findModules
operator|.
name|setOrganisation
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setRevision
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setProperty
argument_list|(
literal|"found.[revision]"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|setValue
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|findModules
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|findModules
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"found.1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|findModules
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"found.1.1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|findModules
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"found.2.0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

