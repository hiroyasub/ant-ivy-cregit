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
name|java
operator|.
name|util
operator|.
name|List
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
name|DefaultArtifact
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|report
operator|.
name|DownloadReport
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
name|report
operator|.
name|DownloadStatus
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
name|*
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|IvyRepResolverTest
extends|extends
name|AbstractDependencyResolverTest
block|{
specifier|private
name|IvySettings
name|_settings
decl_stmt|;
specifier|private
name|ResolveEngine
name|_engine
decl_stmt|;
specifier|private
name|ResolveData
name|_data
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|_settings
operator|=
operator|new
name|IvySettings
argument_list|()
expr_stmt|;
name|_engine
operator|=
operator|new
name|ResolveEngine
argument_list|(
name|_settings
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|,
operator|new
name|SortEngine
argument_list|(
name|_settings
argument_list|)
argument_list|)
expr_stmt|;
name|_data
operator|=
operator|new
name|ResolveData
argument_list|(
name|_engine
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
expr_stmt|;
name|TestHelper
operator|.
name|createCache
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|setDefaultCache
argument_list|(
name|TestHelper
operator|.
name|cache
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
name|testDefaults
parameter_list|()
block|{
name|IvyRepResolver
name|resolver
init|=
operator|new
name|IvyRepResolver
argument_list|()
decl_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ivyrep.default.ivy.root"
argument_list|,
literal|"http://www.jayasoft.fr/myivyrep/"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ivyrep.default.ivy.pattern"
argument_list|,
literal|"[organisation]/[module]/ivy-[revision].[ext]"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ivyrep.default.artifact.root"
argument_list|,
literal|"http://www.ibiblio.org/mymaven/"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ivyrep.default.artifact.pattern"
argument_list|,
literal|"[module]/jars/[artifact]-[revision].jar"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
name|List
name|l
init|=
name|resolver
operator|.
name|getIvyPatterns
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.jayasoft.fr/myivyrep/[organisation]/[module]/ivy-[revision].[ext]"
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|l
operator|=
name|resolver
operator|.
name|getArtifactPatterns
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.ibiblio.org/mymaven/[module]/jars/[artifact]-[revision].jar"
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMandatoryRoot
parameter_list|()
throws|throws
name|Exception
block|{
comment|// IVY-625: should fail if no ivyroot specified
name|IvyRepResolver
name|resolver
init|=
operator|new
name|IvyRepResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-cli"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
try|try
block|{
name|resolver
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid
argument_list|,
literal|false
argument_list|)
argument_list|,
name|_data
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"using ivyrep resolver without ivyroot should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"exception thrown when using ivyrep with no ivyroot should talk about the root"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ivyroot"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIvyRepWithLocalURL
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyRepResolver
name|resolver
init|=
operator|new
name|IvyRepResolver
argument_list|()
decl_stmt|;
name|String
name|rootpath
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/1"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"testLocal"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setIvyroot
argument_list|(
literal|"file:"
operator|+
name|rootpath
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setIvypattern
argument_list|(
literal|"[organisation]/[module]/ivys/ivy-[revision].xml"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setArtroot
argument_list|(
literal|"file:"
operator|+
name|rootpath
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setArtpattern
argument_list|(
literal|"[organisation]/[module]/jars/[artifact]-[revision].[ext]"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|resolver
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid
argument_list|,
literal|false
argument_list|)
argument_list|,
name|_data
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rmr
argument_list|)
expr_stmt|;
name|DefaultArtifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|rmr
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
literal|"mod1.1"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|DownloadReport
name|report
init|=
name|resolver
operator|.
name|download
argument_list|(
operator|new
name|Artifact
index|[]
block|{
name|artifact
block|}
argument_list|,
name|downloadOptions
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ArtifactDownloadReport
name|ar
init|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artifact
argument_list|,
name|ar
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DownloadStatus
operator|.
name|SUCCESSFUL
argument_list|,
name|ar
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// test to ask to download again, should use cache
name|report
operator|=
name|resolver
operator|.
name|download
argument_list|(
operator|new
name|Artifact
index|[]
block|{
name|artifact
block|}
argument_list|,
name|downloadOptions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ar
operator|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artifact
argument_list|,
name|ar
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|,
name|ar
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

