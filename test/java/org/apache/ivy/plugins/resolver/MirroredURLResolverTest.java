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
name|resolver
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
name|module
operator|.
name|descriptor
operator|.
name|DefaultDependencyDescriptor
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|settings
operator|.
name|XmlSettingsParser
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
name|MirroredURLResolverTest
extends|extends
name|TestCase
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|ResolveEngine
name|engine
decl_stmt|;
specifier|private
name|ResolveData
name|data
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|settings
operator|=
operator|new
name|IvySettings
argument_list|()
expr_stmt|;
name|engine
operator|=
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
expr_stmt|;
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|data
operator|=
operator|new
name|ResolveData
argument_list|(
name|engine
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|settings
operator|.
name|setDefaultCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"test.mirroredurl.mirrorlist-solo.url"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"mirrorlist-solo.txt"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"test.mirroredurl.mirrorlist-failover.url"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"mirrorlist-failover.txt"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"test.mirroredurl.mirrorlist-fail.url"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"mirrorlist-fail.txt"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|XmlSettingsParser
argument_list|(
name|settings
argument_list|)
operator|.
name|parse
argument_list|(
name|MirroredURLResolverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"mirror-resolver-settings.xml"
argument_list|)
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
specifier|public
name|void
name|testSolo
parameter_list|()
throws|throws
name|Exception
block|{
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"solo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resolver
operator|instanceof
name|MirroredURLResolver
argument_list|)
expr_stmt|;
name|MirroredURLResolver
name|mirrored
init|=
operator|(
name|MirroredURLResolver
operator|)
name|resolver
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.4"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|mirrored
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rmr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFailover
parameter_list|()
throws|throws
name|Exception
block|{
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"failover"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resolver
operator|instanceof
name|MirroredURLResolver
argument_list|)
expr_stmt|;
name|MirroredURLResolver
name|mirrored
init|=
operator|(
name|MirroredURLResolver
operator|)
name|resolver
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.4"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|mirrored
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rmr
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
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"fail"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resolver
operator|instanceof
name|MirroredURLResolver
argument_list|)
expr_stmt|;
name|MirroredURLResolver
name|mirrored
init|=
operator|(
name|MirroredURLResolver
operator|)
name|resolver
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.4"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|mirrored
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|rmr
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

