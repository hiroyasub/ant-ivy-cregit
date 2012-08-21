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
name|plugins
operator|.
name|resolver
operator|.
name|DependencyResolver
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
name|resolver
operator|.
name|IBiblioResolver
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
name|resolver
operator|.
name|IvyRepResolver
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

begin_class
specifier|public
class|class
name|IvyConfigureTest
extends|extends
name|TestCase
block|{
specifier|private
name|IvyConfigure
name|configure
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"myproperty"
argument_list|,
literal|"myvalue"
argument_list|)
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Ivy
name|getIvyInstance
parameter_list|()
block|{
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
name|project
argument_list|)
expr_stmt|;
name|task
operator|.
name|init
argument_list|()
expr_stmt|;
name|Reference
name|ref
init|=
operator|new
name|Reference
argument_list|(
name|configure
operator|.
name|getSettingsId
argument_list|()
argument_list|)
decl_stmt|;
comment|//        ref.setProject(project);
name|task
operator|.
name|setSettingsRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|task
operator|.
name|getIvyInstance
argument_list|()
return|;
block|}
specifier|public
name|void
name|testDefaultCacheDir
parameter_list|()
block|{
comment|// test with an URL
name|configure
operator|.
name|setUrl
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"ivysettings-defaultCacheDir.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setSettingsId
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"mycache"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.cache.dir.test"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test with a File
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivysettings-defaultCacheDir.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setSettingsId
argument_list|(
literal|"test2"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"mycache"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.cache.dir.test2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test if no defaultCacheDir is specified
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivysettings-noDefaultCacheDir.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setSettingsId
argument_list|(
literal|"test3"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|project
operator|.
name|getProperty
argument_list|(
literal|"ivy.cache.dir.test3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|Exception
block|{
comment|// by default settings look in the current directory for an ivysettings.xml file...
comment|// but Ivy itself has one, and we don't want to use it
name|configure
operator|.
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"no/settings/will/use/default.xml"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyResolver
name|publicResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"public"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|publicResolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|publicResolver
operator|instanceof
name|IBiblioResolver
argument_list|)
expr_stmt|;
name|IBiblioResolver
name|ibiblio
init|=
operator|(
name|IBiblioResolver
operator|)
name|publicResolver
decl_stmt|;
name|assertTrue
argument_list|(
name|ibiblio
operator|.
name|isM2compatible
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefault14
parameter_list|()
throws|throws
name|Exception
block|{
comment|// by default settings look in the current directory for an ivysettings.xml file...
comment|// but Ivy itself has one, and we don't want to use it
name|configure
operator|.
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"no/settings/will/use/default.xml"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.14.compatible"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyResolver
name|publicResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"public"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|publicResolver
operator|instanceof
name|IvyRepResolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFile
parameter_list|()
throws|throws
name|Exception
block|{
name|configure
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
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
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
name|testURL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|confUrl
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings-url.xml"
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
decl_stmt|;
name|String
name|confDirUrl
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories"
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
decl_stmt|;
if|if
condition|(
name|confDirUrl
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|confDirUrl
operator|=
name|confDirUrl
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|confDirUrl
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
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
name|confUrl
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
name|confDirUrl
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
name|testAntProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|confUrl
init|=
name|IvyConfigureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-test.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
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
name|assertEquals
argument_list|(
literal|"myvalue"
argument_list|,
name|settings
operator|.
name|getDefaultResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOverrideVariables
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|confUrl
init|=
name|IvyConfigureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-props.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
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
literal|"lib/test/[artifact]-[revision].[ext]"
argument_list|,
name|settings
operator|.
name|getVariables
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.retrieve.pattern"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExposeAntProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|confUrl
init|=
name|IvyConfigureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-props.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setSettingsId
argument_list|(
literal|"this.id"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|getIvyInstance
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|configure
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.test.variable"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|configure
operator|.
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.test.variable.this.id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIncludeTwice
parameter_list|()
throws|throws
name|Exception
block|{
comment|// IVY-601
name|configure
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivysettings-include-twice.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|getIvyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOverrideTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|configure
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
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setOverride
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|configure
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
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|getIvyInstance
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ivy
operator|!=
name|getIvyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOverrideFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|configure
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
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|IvyConfigure
name|newAntSettings
init|=
operator|new
name|IvyConfigure
argument_list|()
decl_stmt|;
name|newAntSettings
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|newAntSettings
operator|.
name|setOverride
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|newAntSettings
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
name|newAntSettings
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|ivy
operator|==
name|getIvyInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOverrideNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|configure
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
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setOverride
argument_list|(
literal|"notallowed"
argument_list|)
expr_stmt|;
name|configure
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
try|try
block|{
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"calling settings twice with the same id with "
operator|+
literal|"override=notallowed should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"notallowed"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|configure
operator|.
name|getSettingsId
argument_list|()
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testInvalidOverride
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|configure
operator|.
name|setOverride
argument_list|(
literal|"unknown"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"settings override with invalid value should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"unknown"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

