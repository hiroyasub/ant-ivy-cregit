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
name|osgi
operator|.
name|repo
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
name|io
operator|.
name|FileInputStream
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
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
name|descriptor
operator|.
name|DefaultModuleDescriptor
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
name|report
operator|.
name|ResolveReport
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
name|core
operator|.
name|ManifestParser
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
name|repo
operator|.
name|BundleRepoResolver
operator|.
name|RequirementStrategy
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
name|DualResolver
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
name|FileSystemResolver
import|;
end_import

begin_class
specifier|public
class|class
name|BundleRepoResolverTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgitestbundle"
argument_list|,
literal|"1.2.3"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE_IMPORTING
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle.importing"
argument_list|,
literal|"3.2.1"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE_IMPORTING_VERSION
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle.importing.version"
argument_list|,
literal|"3.2.1"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE_IMPORTING_OPTIONAL
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle.importing.optional"
argument_list|,
literal|"3.2.1"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE_USE
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle.use"
argument_list|,
literal|"2.2.2"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_TEST_BUNDLE_EXPORTING_AMBIGUITY
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle.exporting.ambiguity"
argument_list|,
literal|"3.3.3"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|ResolveData
name|data
decl_stmt|;
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|BundleRepoResolver
name|bundleResolver
decl_stmt|;
specifier|private
name|BundleRepoResolver
name|bundleUrlResolver
decl_stmt|;
specifier|private
name|DualResolver
name|dualResolver
decl_stmt|;
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
name|bundleResolver
operator|=
operator|new
name|BundleRepoResolver
argument_list|()
expr_stmt|;
name|bundleResolver
operator|.
name|setRepoXmlFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"java/test-repo/bundlerepo/repo.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|bundleResolver
operator|.
name|setName
argument_list|(
literal|"bundle"
argument_list|)
expr_stmt|;
name|bundleResolver
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
name|bundleResolver
argument_list|)
expr_stmt|;
name|bundleUrlResolver
operator|=
operator|new
name|BundleRepoResolver
argument_list|()
expr_stmt|;
name|bundleUrlResolver
operator|.
name|setRepoXmlURL
argument_list|(
operator|new
name|File
argument_list|(
literal|"java/test-repo/bundlerepo/repo.xml"
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
name|bundleUrlResolver
operator|.
name|setName
argument_list|(
literal|"bundleurl"
argument_list|)
expr_stmt|;
name|bundleUrlResolver
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
name|bundleUrlResolver
argument_list|)
expr_stmt|;
name|dualResolver
operator|=
operator|new
name|DualResolver
argument_list|()
expr_stmt|;
name|BundleRepoResolver
name|resolver
init|=
operator|new
name|BundleRepoResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setRepoXmlFile
argument_list|(
literal|"java/test-repo/ivyrepo/repo.xml"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"dual-bundle"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|dualResolver
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|dualResolver
operator|.
name|setName
argument_list|(
literal|"dual"
argument_list|)
expr_stmt|;
name|File
name|ivyrepo
init|=
operator|new
name|File
argument_list|(
literal|"java/test-repo/ivyrepo"
argument_list|)
decl_stmt|;
name|FileSystemResolver
name|fileSystemResolver
init|=
operator|new
name|FileSystemResolver
argument_list|()
decl_stmt|;
name|fileSystemResolver
operator|.
name|addIvyPattern
argument_list|(
name|ivyrepo
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/[organisation]/[module]/[revision]/ivy.xml"
argument_list|)
expr_stmt|;
name|fileSystemResolver
operator|.
name|addArtifactPattern
argument_list|(
name|ivyrepo
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/[organisation]/[module]/[revision]/[type]s/[artifact]-[revision].[ext]"
argument_list|)
expr_stmt|;
name|fileSystemResolver
operator|.
name|setName
argument_list|(
literal|"dual-file"
argument_list|)
expr_stmt|;
name|fileSystemResolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|dualResolver
operator|.
name|add
argument_list|(
name|fileSystemResolver
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|dualResolver
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"bundle"
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|caches
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|caches
index|[
name|i
index|]
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
specifier|public
name|void
name|testSimpleResolve
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle"
argument_list|,
literal|"1.2.3"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
name|genericTestResolveDownload
argument_list|(
name|bundleResolver
argument_list|,
name|mrid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSimpleUrlResolve
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle"
argument_list|,
literal|"1.2.3"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
name|genericTestResolveDownload
argument_list|(
name|bundleUrlResolver
argument_list|,
name|mrid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveDual
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|"org.apache.ivy.osgi.testbundle"
argument_list|,
literal|"1.2.3"
argument_list|,
name|BundleInfoAdapter
operator|.
name|OSGI_BUNDLE
argument_list|)
decl_stmt|;
name|genericTestResolveDownload
argument_list|(
name|dualResolver
argument_list|,
name|mrid
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|genericTestResolveDownload
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
throws|throws
name|ParseException
block|{
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
operator|new
name|DownloadOptions
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
specifier|public
name|void
name|testResolveImporting
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportingOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.optional_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{}
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"optional"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"transitive-optional"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportingTransitiveOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.transitiveoptional_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{}
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"optional"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE_IMPORTING_OPTIONAL
block|}
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"transitive-optional"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|,
name|MRID_TEST_BUNDLE_IMPORTING_OPTIONAL
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportingVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.version_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportingRangeVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.rangeversion_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveUse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.use_2.2.2.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportingUse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.use_3.2.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE_USE
block|,
name|MRID_TEST_BUNDLE_IMPORTING
block|,
name|MRID_TEST_BUNDLE
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveRequire
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.require_1.1.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|,
name|MRID_TEST_BUNDLE_IMPORTING_VERSION
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveOptionalConf
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.require_1.1.1.jar"
decl_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"optional"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|,
name|MRID_TEST_BUNDLE_IMPORTING_VERSION
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportAmbiguity
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.ambiguity_3.2.1.jar"
decl_stmt|;
name|bundleResolver
operator|.
name|setImportPackageStrategy
argument_list|(
name|RequirementStrategy
operator|.
name|first
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE_EXPORTING_AMBIGUITY
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveImportNoAmbiguity
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.importing.ambiguity_3.2.1.jar"
decl_stmt|;
name|bundleResolver
operator|.
name|setImportPackageStrategy
argument_list|(
name|RequirementStrategy
operator|.
name|noambiguity
argument_list|)
expr_stmt|;
name|genericTestFailingResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResolveRequireAmbiguity
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jarName
init|=
literal|"org.apache.ivy.osgi.testbundle.require.ambiguity_1.1.1.jar"
decl_stmt|;
name|bundleResolver
operator|.
name|setImportPackageStrategy
argument_list|(
name|RequirementStrategy
operator|.
name|noambiguity
argument_list|)
expr_stmt|;
name|genericTestResolve
argument_list|(
name|jarName
argument_list|,
literal|"default"
argument_list|,
operator|new
name|ModuleRevisionId
index|[]
block|{
name|MRID_TEST_BUNDLE
block|,
name|MRID_TEST_BUNDLE_IMPORTING_VERSION
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|genericTestResolve
parameter_list|(
name|String
name|jarName
parameter_list|,
name|String
name|conf
parameter_list|,
name|ModuleRevisionId
index|[]
name|expectedMrids
parameter_list|)
throws|throws
name|Exception
block|{
name|JarInputStream
name|in
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"java/test-repo/bundlerepo/"
operator|+
name|jarName
argument_list|)
argument_list|)
decl_stmt|;
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|in
operator|.
name|getManifest
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultModuleDescriptor
name|md
init|=
name|BundleInfoAdapter
operator|.
name|toModuleDescriptor
argument_list|(
name|bundleInfo
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ResolveReport
name|resolveReport
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
operator|new
name|String
index|[]
block|{
name|conf
block|}
argument_list|)
operator|.
name|setOutputReport
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"resolve failed "
operator|+
name|resolveReport
operator|.
name|getProblemMessages
argument_list|()
argument_list|,
name|resolveReport
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|Set
comment|/*<ModuleRevisionId> */
name|actual
init|=
operator|new
name|HashSet
comment|/*<ModuleRevisionId> */
argument_list|()
decl_stmt|;
name|List
comment|/*<Artifact> */
name|artifacts
init|=
name|resolveReport
operator|.
name|getArtifacts
argument_list|()
decl_stmt|;
name|Iterator
name|itArtfact
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itArtfact
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Artifact
name|artfact
init|=
operator|(
name|Artifact
operator|)
name|itArtfact
operator|.
name|next
argument_list|()
decl_stmt|;
name|actual
operator|.
name|add
argument_list|(
name|artfact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Set
comment|/*<ModuleRevisionId> */
name|expected
init|=
operator|new
name|HashSet
comment|/*<ModuleRevisionId> */
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expectedMrids
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|genericTestFailingResolve
parameter_list|(
name|String
name|jarName
parameter_list|,
name|String
name|conf
parameter_list|)
throws|throws
name|Exception
block|{
name|JarInputStream
name|in
init|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"java/test-repo/bundlerepo/"
operator|+
name|jarName
argument_list|)
argument_list|)
decl_stmt|;
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|in
operator|.
name|getManifest
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultModuleDescriptor
name|md
init|=
name|BundleInfoAdapter
operator|.
name|toModuleDescriptor
argument_list|(
name|bundleInfo
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ResolveReport
name|resolveReport
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
operator|new
name|String
index|[]
block|{
name|conf
block|}
argument_list|)
operator|.
name|setOutputReport
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|resolveReport
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

