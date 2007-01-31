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
name|IvyConfigureTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyConfigure
name|_configure
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
name|project
operator|.
name|setProperty
argument_list|(
literal|"myproperty"
argument_list|,
literal|"myvalue"
argument_list|)
expr_stmt|;
name|_configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|_configure
operator|.
name|setProject
argument_list|(
name|project
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
name|_configure
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_configure
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
literal|"test/repositories/ivyconf.xml"
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
name|get
argument_list|(
literal|"ivy.conf.file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
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
name|get
argument_list|(
literal|"ivy.conf.url"
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
name|get
argument_list|(
literal|"ivy.conf.dir"
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
name|get
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
literal|"test/repositories/ivyconf.xml"
argument_list|)
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
name|_configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|_configure
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
name|get
argument_list|(
literal|"ivy.conf.url"
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
name|get
argument_list|(
literal|"ivy.conf.dir"
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
name|get
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
literal|"ivyconf-test.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|_configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|_configure
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
name|get
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
name|getDefaultCache
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
literal|"ivyconf-props.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|_configure
operator|.
name|setUrl
argument_list|(
name|confUrl
argument_list|)
expr_stmt|;
name|_configure
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
name|get
argument_list|(
literal|"ivy.retrieve.pattern"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Ivy
name|getIvyInstance
parameter_list|()
block|{
return|return
operator|(
name|Ivy
operator|)
name|_configure
operator|.
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.instance"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

