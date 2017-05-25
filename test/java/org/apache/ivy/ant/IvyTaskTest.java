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
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|Ivy
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
name|tools
operator|.
name|ant
operator|.
name|BuildException
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
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|Reference
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
name|IvyTaskTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefaultSettings
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|Project
name|p
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setBasedir
argument_list|(
literal|"test/repositories"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"myproperty"
argument_list|,
literal|"myvalue"
argument_list|)
expr_stmt|;
name|IvyTask
name|task
init|=
operator|new
name|IvyTask
argument_list|()
block|{
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
block|}
block|}
decl_stmt|;
name|task
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
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
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/build/cache"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|settings
operator|.
name|getDefaultCache
argument_list|()
argument_list|)
expr_stmt|;
comment|// The next test doesn't always works on windows (mix C: and c: drive)
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.file"
argument_list|)
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.url"
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.dir"
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"myproperty"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReferencedSettings
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|Project
name|p
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"myproperty"
argument_list|,
literal|"myvalue"
argument_list|)
expr_stmt|;
name|IvyAntSettings
name|antSettings
init|=
operator|new
name|IvyAntSettings
argument_list|()
decl_stmt|;
name|antSettings
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
comment|// antSettings.setId("mySettings");
name|antSettings
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|addReference
argument_list|(
literal|"mySettings"
argument_list|,
name|antSettings
argument_list|)
expr_stmt|;
name|IvyTask
name|task
init|=
operator|new
name|IvyTask
argument_list|()
block|{
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
block|}
block|}
decl_stmt|;
name|task
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|task
operator|.
name|setSettingsRef
argument_list|(
operator|new
name|Reference
argument_list|(
literal|"mySettings"
argument_list|)
argument_list|)
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
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|settings
operator|.
name|getDefaultCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.url"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.settings.dir"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"myproperty"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIvyVersionAsAntProperty
parameter_list|()
block|{
name|Project
name|p
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setBasedir
argument_list|(
literal|"test/repositories"
argument_list|)
expr_stmt|;
name|IvyTask
name|task
init|=
operator|new
name|IvyTask
argument_list|()
block|{
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
block|}
block|}
decl_stmt|;
name|task
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
literal|"ivy.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

