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
name|DefaultLogger
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
name|taskdefs
operator|.
name|Delete
import|;
end_import

begin_class
specifier|public
class|class
name|IvyResourcesTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyResources
name|resources
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createCache
argument_list|()
expr_stmt|;
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|DefaultLogger
name|logger
init|=
operator|new
name|DefaultLogger
argument_list|()
decl_stmt|;
name|logger
operator|.
name|setOutputPrintStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setErrorPrintStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
name|project
operator|.
name|addBuildListener
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|resources
operator|=
operator|new
name|IvyResources
argument_list|()
expr_stmt|;
name|resources
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|cleanCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanCache
parameter_list|()
block|{
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|File
name|getIvyFileInCache
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|ModuleRevisionId
name|id
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|)
decl_stmt|;
return|return
name|TestHelper
operator|.
name|getRepositoryCacheManager
argument_list|(
name|getIvy
argument_list|()
argument_list|,
name|id
argument_list|)
operator|.
name|getIvyFileInCache
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|File
name|getArchiveFileInCache
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|TestHelper
operator|.
name|getArchiveFileInCache
argument_list|(
name|getIvy
argument_list|()
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
return|;
block|}
specifier|private
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|resources
operator|.
name|getIvyInstance
argument_list|()
return|;
block|}
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|resources
operator|.
name|iterator
argument_list|()
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultiple
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|dependency
operator|=
operator|new
name|IvyDependency
argument_list|()
expr_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod2.3"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"0.7"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|resources
operator|.
name|iterator
argument_list|()
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.3"
argument_list|,
literal|"0.7"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.3"
argument_list|,
literal|"0.7"
argument_list|,
literal|"mod2.3"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.3"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.3"
argument_list|,
literal|"art21A"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.3"
argument_list|,
literal|"art21B"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleWithConf
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|dependency
operator|=
operator|new
name|IvyDependency
argument_list|()
expr_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod2.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"0.10"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setConf
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|resources
operator|.
name|iterator
argument_list|()
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"0.10"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"0.10"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.7"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.7"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleWithConf2
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod1.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|dependency
operator|=
operator|new
name|IvyDependency
argument_list|()
expr_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"mod2.2"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"0.10"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setConf
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|resources
operator|.
name|iterator
argument_list|()
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"0.10"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"0.10"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.7"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"0.7"
argument_list|,
literal|"mod2.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvyFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFail
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setName
argument_list|(
literal|"noexisting"
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setRev
argument_list|(
literal|"2.0"
argument_list|)
expr_stmt|;
name|resources
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
try|try
block|{
name|resources
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"A fail resolved should have raised a build exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
block|}
end_class

end_unit

