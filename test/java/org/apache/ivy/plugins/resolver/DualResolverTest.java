begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|descriptor
operator|.
name|DependencyDescriptor
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|assertNull
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Test for DualResolver  */
end_comment

begin_class
specifier|public
class|class
name|DualResolverTest
extends|extends
name|AbstractDependencyResolverTest
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
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromConf
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|XmlSettingsParser
argument_list|(
name|settings
argument_list|)
operator|.
name|parse
argument_list|(
name|DualResolverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"dualresolverconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"dualok"
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
name|DualResolver
argument_list|)
expr_stmt|;
name|DualResolver
name|dual
init|=
operator|(
name|DualResolver
operator|)
name|resolver
decl_stmt|;
name|assertNotNull
argument_list|(
name|dual
operator|.
name|getIvyResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ivy"
argument_list|,
name|dual
operator|.
name|getIvyResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dual
operator|.
name|getArtifactResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artifact"
argument_list|,
name|dual
operator|.
name|getArtifactResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"dualnotenough"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resolver
operator|instanceof
name|DualResolver
argument_list|)
expr_stmt|;
name|dual
operator|=
operator|(
name|DualResolver
operator|)
name|resolver
expr_stmt|;
name|assertNotNull
argument_list|(
name|dual
operator|.
name|getIvyResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|dual
operator|.
name|getArtifactResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test fails due to bad resolver configuration.      *      * @throws IOException if something goes wrong      * @throws ParseException if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFromBadConf
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
block|{
operator|new
name|XmlSettingsParser
argument_list|(
name|settings
argument_list|)
operator|.
name|parse
argument_list|(
name|DualResolverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"dualresolverconf-bad.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test fails due to bad resolver configuration      *      * @throws ParseException if something goes wrong      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testBad
parameter_list|()
throws|throws
name|ParseException
block|{
name|DualResolver
name|dual
init|=
operator|new
name|DualResolver
argument_list|()
decl_stmt|;
name|dual
operator|.
name|setIvyResolver
argument_list|(
operator|new
name|IBiblioResolver
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"org"
argument_list|,
literal|"mod"
argument_list|,
literal|"rev"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|dual
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolve
parameter_list|()
throws|throws
name|Exception
block|{
name|DualResolver
name|dual
init|=
operator|new
name|DualResolver
argument_list|()
decl_stmt|;
name|MockResolver
name|ivyResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"ivy"
argument_list|,
literal|true
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|MockResolver
name|artifactResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"artifact"
argument_list|,
literal|false
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|dual
operator|.
name|setIvyResolver
argument_list|(
name|ivyResolver
argument_list|)
expr_stmt|;
name|dual
operator|.
name|setArtifactResolver
argument_list|(
name|artifactResolver
argument_list|)
expr_stmt|;
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
literal|"org"
argument_list|,
literal|"mod"
argument_list|,
literal|"rev"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|dual
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
name|assertEquals
argument_list|(
name|dual
argument_list|,
name|rmr
operator|.
name|getArtifactResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|DependencyDescriptor
operator|>
name|singletonList
argument_list|(
name|dd
argument_list|)
argument_list|,
name|ivyResolver
operator|.
name|askedDeps
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artifactResolver
operator|.
name|askedDeps
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveFromArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|DualResolver
name|dual
init|=
operator|new
name|DualResolver
argument_list|()
decl_stmt|;
name|MockResolver
name|ivyResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"ivy"
argument_list|,
literal|false
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|MockResolver
name|artifactResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"artifact"
argument_list|,
literal|true
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|dual
operator|.
name|setIvyResolver
argument_list|(
name|ivyResolver
argument_list|)
expr_stmt|;
name|dual
operator|.
name|setArtifactResolver
argument_list|(
name|artifactResolver
argument_list|)
expr_stmt|;
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
literal|"org"
argument_list|,
literal|"mod"
argument_list|,
literal|"rev"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|dual
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
name|assertEquals
argument_list|(
name|artifactResolver
argument_list|,
name|rmr
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|DependencyDescriptor
operator|>
name|singletonList
argument_list|(
name|dd
argument_list|)
argument_list|,
name|ivyResolver
operator|.
name|askedDeps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|DependencyDescriptor
operator|>
name|singletonList
argument_list|(
name|dd
argument_list|)
argument_list|,
name|artifactResolver
operator|.
name|askedDeps
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveFail
parameter_list|()
throws|throws
name|Exception
block|{
name|DualResolver
name|dual
init|=
operator|new
name|DualResolver
argument_list|()
decl_stmt|;
name|MockResolver
name|ivyResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"ivy"
argument_list|,
literal|false
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|MockResolver
name|artifactResolver
init|=
name|MockResolver
operator|.
name|buildMockResolver
argument_list|(
name|settings
argument_list|,
literal|"artifact"
argument_list|,
literal|false
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|1
argument_list|,
literal|20
argument_list|)
operator|.
name|getTime
argument_list|()
argument_list|)
decl_stmt|;
name|dual
operator|.
name|setIvyResolver
argument_list|(
name|ivyResolver
argument_list|)
expr_stmt|;
name|dual
operator|.
name|setArtifactResolver
argument_list|(
name|artifactResolver
argument_list|)
expr_stmt|;
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
literal|"org"
argument_list|,
literal|"mod"
argument_list|,
literal|"rev"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|dual
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
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|DependencyDescriptor
operator|>
name|singletonList
argument_list|(
name|dd
argument_list|)
argument_list|,
name|ivyResolver
operator|.
name|askedDeps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
expr|<
name|DependencyDescriptor
operator|>
name|singletonList
argument_list|(
name|dd
argument_list|)
argument_list|,
name|artifactResolver
operator|.
name|askedDeps
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

