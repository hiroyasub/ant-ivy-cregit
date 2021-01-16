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
name|p2
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
name|DownloadOptions
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
name|osgi
operator|.
name|updatesite
operator|.
name|UpdateSiteResolver
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
name|condition
operator|.
name|JavaVersion
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
name|Assume
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

begin_class
specifier|public
class|class
name|P2DescriptorTest
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|UpdateSiteResolver
name|p2SourceResolver
decl_stmt|;
specifier|private
name|UpdateSiteResolver
name|p2ZippedResolver
decl_stmt|;
specifier|private
name|UpdateSiteResolver
name|p2WithPackedResolver
decl_stmt|;
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|ResolveData
name|data
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
name|p2SourceResolver
operator|=
operator|new
name|UpdateSiteResolver
argument_list|()
expr_stmt|;
name|p2SourceResolver
operator|.
name|setName
argument_list|(
literal|"p2-sources"
argument_list|)
expr_stmt|;
name|p2SourceResolver
operator|.
name|setUrl
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/sources"
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
name|p2SourceResolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|p2SourceResolver
argument_list|)
expr_stmt|;
name|p2ZippedResolver
operator|=
operator|new
name|UpdateSiteResolver
argument_list|()
expr_stmt|;
name|p2ZippedResolver
operator|.
name|setName
argument_list|(
literal|"p2-zipped"
argument_list|)
expr_stmt|;
name|p2ZippedResolver
operator|.
name|setUrl
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/zipped"
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
name|p2ZippedResolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|p2ZippedResolver
argument_list|)
expr_stmt|;
name|p2WithPackedResolver
operator|=
operator|new
name|UpdateSiteResolver
argument_list|()
expr_stmt|;
name|p2WithPackedResolver
operator|.
name|setName
argument_list|(
literal|"p2-with-packed"
argument_list|)
expr_stmt|;
name|p2WithPackedResolver
operator|.
name|setUrl
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/packed"
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
name|p2WithPackedResolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|p2WithPackedResolver
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
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|sumupProblems
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveSource
parameter_list|()
throws|throws
name|Exception
block|{
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"p2-sources"
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
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
literal|"2.2.0.final_20100923230623"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|p2SourceResolver
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
name|mrid
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|DownloadReport
name|report
init|=
name|p2SourceResolver
operator|.
name|download
argument_list|(
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
argument_list|,
operator|new
name|DownloadOptions
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
literal|2
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|Artifact
name|artifact
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
index|[
name|i
index|]
decl_stmt|;
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
name|DownloadReport
name|report2
init|=
name|p2SourceResolver
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
operator|new
name|DownloadOptions
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report2
operator|.
name|getArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ar
operator|=
name|report2
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
annotation|@
name|Test
specifier|public
name|void
name|testResolveNotZipped
parameter_list|()
throws|throws
name|Exception
block|{
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"p2-zipped"
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"org.eclipse.e4.core.services"
argument_list|,
literal|"1.0.0.v20120521-2346"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|p2ZippedResolver
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
name|mrid
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|DownloadOptions
name|options
init|=
operator|new
name|DownloadOptions
argument_list|()
decl_stmt|;
name|DownloadReport
name|report
init|=
name|p2ZippedResolver
operator|.
name|download
argument_list|(
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
argument_list|,
name|options
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
name|Artifact
name|artifact
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
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
name|assertNull
argument_list|(
name|ar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveZipped
parameter_list|()
throws|throws
name|Exception
block|{
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"p2-zipped"
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"org.apache.ant"
argument_list|,
literal|"1.8.3.v20120321-1730"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|p2ZippedResolver
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
name|mrid
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|DownloadOptions
name|options
init|=
operator|new
name|DownloadOptions
argument_list|()
decl_stmt|;
name|DownloadReport
name|report
init|=
name|p2ZippedResolver
operator|.
name|download
argument_list|(
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|Artifact
name|artifact
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
index|[
name|i
index|]
decl_stmt|;
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
comment|// only the binary get unpacked
if|if
condition|(
name|ar
operator|.
name|getArtifact
argument_list|()
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"source"
argument_list|)
condition|)
block|{
name|assertNull
argument_list|(
name|ar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNotNull
argument_list|(
name|ar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolvePacked
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JavaVersion
name|java14OrHigher
init|=
operator|new
name|JavaVersion
argument_list|()
decl_stmt|;
name|java14OrHigher
operator|.
name|setAtLeast
argument_list|(
literal|"14"
argument_list|)
expr_stmt|;
name|Assume
operator|.
name|assumeFalse
argument_list|(
literal|"Pack200 tools and API have been removed since JDK 14"
argument_list|,
name|java14OrHigher
operator|.
name|eval
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"p2-with-packed"
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"org.junit"
argument_list|,
literal|"4.10.0.v4_10_0_v20120426-0900"
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|p2WithPackedResolver
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
name|mrid
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|DownloadOptions
name|options
init|=
operator|new
name|DownloadOptions
argument_list|()
decl_stmt|;
name|DownloadReport
name|report
init|=
name|p2WithPackedResolver
operator|.
name|download
argument_list|(
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
argument_list|,
name|options
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
name|Artifact
name|artifact
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
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
name|assertNotNull
argument_list|(
name|ar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

