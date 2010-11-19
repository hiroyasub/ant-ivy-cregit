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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|resources
operator|.
name|FileResource
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
specifier|private
name|List
name|asList
parameter_list|(
name|IvyResources
name|ivyResources
parameter_list|)
block|{
name|List
name|resources
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|ivyResources
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|r
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|r
operator|instanceof
name|FileResource
argument_list|)
expr_stmt|;
name|resources
operator|.
name|add
argument_list|(
operator|(
operator|(
name|FileResource
operator|)
name|r
operator|)
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
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
name|resources
operator|.
name|createDependency
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
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
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
argument_list|,
name|files
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
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
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
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
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExclude
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
name|IvyExclude
name|exclude
init|=
name|resources
operator|.
name|createExclude
argument_list|()
decl_stmt|;
name|exclude
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|exclude
operator|.
name|setModule
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencyExclude
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
name|IvyDependencyExclude
name|exclude
init|=
name|dependency
operator|.
name|createExclude
argument_list|()
decl_stmt|;
name|exclude
operator|.
name|setOrg
argument_list|(
literal|"org1"
argument_list|)
expr_stmt|;
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencyInclude
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyDependency
name|dependency
init|=
name|resources
operator|.
name|createDependency
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
name|dependency
operator|=
name|resources
operator|.
name|createDependency
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
literal|"0.9"
argument_list|)
expr_stmt|;
name|IvyDependencyInclude
name|include
init|=
name|dependency
operator|.
name|createInclude
argument_list|()
decl_stmt|;
name|include
operator|.
name|setName
argument_list|(
literal|"art22-1"
argument_list|)
expr_stmt|;
name|List
name|files
init|=
name|asList
argument_list|(
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
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
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|files
operator|.
name|contains
argument_list|(
name|getArchiveFileInCache
argument_list|(
literal|"org2"
argument_list|,
literal|"mod2.2"
argument_list|,
literal|"0.9"
argument_list|,
literal|"art22-1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
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
name|resources
operator|.
name|createDependency
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
