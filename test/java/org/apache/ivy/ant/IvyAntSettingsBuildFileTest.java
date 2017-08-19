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
name|core
operator|.
name|report
operator|.
name|ResolveReport
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
name|BuildFileRule
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
name|Rule
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
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|IvyAntSettingsBuildFileTest
block|{
annotation|@
name|Rule
specifier|public
specifier|final
name|BuildFileRule
name|buildRule
init|=
operator|new
name|BuildFileRule
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|buildRule
operator|.
name|configureProject
argument_list|(
literal|"test/java/org/apache/ivy/ant/IvyAntSettingsBuildFile.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverrideNotSpecified
parameter_list|()
block|{
name|buildRule
operator|.
name|executeTarget
argument_list|(
literal|"testOverrideNotSpecified"
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|buildRule
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverrideSetToFalse
parameter_list|()
block|{
name|buildRule
operator|.
name|executeTarget
argument_list|(
literal|"testOverrideSetToFalse"
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|buildRule
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnnecessaryDefaultIvyInstance
parameter_list|()
block|{
name|buildRule
operator|.
name|executeTarget
argument_list|(
literal|"testUnnecessaryDefaultIvyInstance"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Default ivy.instance settings shouldn't have been loaded"
argument_list|,
name|buildRule
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.instance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for IVY-925.      *      * @see<a href="https://issues.apache.org/jira/browse/IVY-925">IVY-925</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testSettingsWithIdIvyInstance
parameter_list|()
block|{
name|buildRule
operator|.
name|executeTarget
argument_list|(
literal|"testSettingsWithPropertyAsId"
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|buildRule
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for IVY-924.      *      * @see<a href="https://issues.apache.org/jira/browse/IVY-924">IVY-924</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testStackOverflow
parameter_list|()
block|{
name|buildRule
operator|.
name|configureProject
argument_list|(
literal|"test/java/org/apache/ivy/ant/IvyAntSettingsBuildFileStackOverflow.xml"
argument_list|)
expr_stmt|;
name|buildRule
operator|.
name|executeTarget
argument_list|(
literal|"testStackOverflow"
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|buildRule
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

