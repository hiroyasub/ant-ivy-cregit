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
name|osgi
operator|.
name|updatesite
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
name|text
operator|.
name|ParseException
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
name|cache
operator|.
name|RepositoryCacheManager
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
name|Artifact
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
name|osgi
operator|.
name|core
operator|.
name|BundleInfo
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
name|ChainResolver
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
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|resolver
operator|.
name|IBiblioResolver
operator|.
name|DEFAULT_M2_ROOT
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

begin_class
specifier|public
class|class
name|UpdateSiteAndIbiblioResolverTest
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|UpdateSiteResolver
name|resolver
decl_stmt|;
specifier|private
name|IBiblioResolver
name|resolver2
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|ResolveData
name|data
decl_stmt|;
specifier|private
name|ChainResolver
name|chain
decl_stmt|;
annotation|@
name|Before
specifier|public
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
name|chain
operator|=
operator|new
name|ChainResolver
argument_list|()
expr_stmt|;
name|chain
operator|.
name|setName
argument_list|(
literal|"chain"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|new
name|UpdateSiteResolver
argument_list|()
expr_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"ivyde-repo"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setUrl
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/ivyde-repo"
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
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|resolver2
operator|=
operator|new
name|IBiblioResolver
argument_list|()
expr_stmt|;
name|resolver2
operator|.
name|setName
argument_list|(
literal|"maven2"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.root"
argument_list|,
name|DEFAULT_M2_ROOT
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.pattern"
argument_list|,
literal|"[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
argument_list|)
expr_stmt|;
name|resolver2
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|resolver2
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"chain"
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
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|bind
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|getResolutionCacheManager
argument_list|()
operator|.
name|clean
argument_list|()
expr_stmt|;
name|RepositoryCacheManager
index|[]
name|caches
init|=
name|settings
operator|.
name|getRepositoryCacheManagers
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryCacheManager
name|cache
range|:
name|caches
control|)
block|{
name|cache
operator|.
name|clean
argument_list|()
expr_stmt|;
block|}
name|data
operator|=
operator|new
name|ResolveData
argument_list|(
name|ivy
operator|.
name|getResolveEngine
argument_list|()
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArtifactRef
parameter_list|()
throws|throws
name|ParseException
block|{
comment|// Simple Dependency for ibiblio
name|ModuleRevisionId
name|mrid1
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"log4j"
argument_list|,
literal|"log4j"
argument_list|,
literal|"1.2.16"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr1
init|=
name|chain
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid1
argument_list|,
literal|false
argument_list|)
argument_list|,
name|data
argument_list|)
decl_stmt|;
comment|// Simple Dependency for updatesite
name|ModuleRevisionId
name|mrid2
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"org.apache.ivy"
argument_list|,
literal|"2.0.0.final_20090108225011"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr2
init|=
name|chain
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid2
argument_list|,
literal|false
argument_list|)
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rmr1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|rmr2
argument_list|)
expr_stmt|;
name|Artifact
index|[]
name|artifacts1
init|=
name|rmr1
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getArtifacts
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|Artifact
index|[]
name|artifacts2
init|=
name|rmr2
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getArtifacts
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|chain
operator|.
name|exists
argument_list|(
name|artifacts2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|chain
operator|.
name|exists
argument_list|(
name|artifacts1
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

