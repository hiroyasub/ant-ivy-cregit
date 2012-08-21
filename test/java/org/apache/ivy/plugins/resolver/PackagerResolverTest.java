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
name|Date
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
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|resolver
operator|.
name|packager
operator|.
name|PackagerResolver
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
name|util
operator|.
name|DefaultMessageLogger
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
name|util
operator|.
name|FileUtil
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
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Tests PackagerResolver.  */
end_comment

begin_class
specifier|public
class|class
name|PackagerResolverTest
extends|extends
name|AbstractDependencyResolverTest
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|ResolveData
name|data
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|File
name|workdir
decl_stmt|;
specifier|private
name|File
name|builddir
decl_stmt|;
specifier|private
name|File
name|cachedir
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
operator|.
name|setDefaultLogger
argument_list|(
operator|new
name|DefaultMessageLogger
argument_list|(
literal|99
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|=
operator|new
name|IvySettings
argument_list|()
expr_stmt|;
name|ResolveEngine
name|engine
init|=
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
decl_stmt|;
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
comment|// Create work space with build and resource cache directories
name|workdir
operator|=
operator|new
name|File
argument_list|(
literal|"build/test/PackagerResolverTest"
argument_list|)
expr_stmt|;
name|builddir
operator|=
operator|new
name|File
argument_list|(
name|workdir
argument_list|,
literal|"build"
argument_list|)
expr_stmt|;
name|cachedir
operator|=
operator|new
name|File
argument_list|(
name|workdir
argument_list|,
literal|"resources"
argument_list|)
expr_stmt|;
name|cleanupTempDirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|builddir
operator|.
name|mkdirs
argument_list|()
operator|||
operator|!
name|cachedir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"can't create directories under "
operator|+
name|workdir
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|cleanupTempDirs
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|cleanupTempDirs
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|workdir
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
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
comment|// Create and configure resolver
name|PackagerResolver
name|resolver
init|=
operator|new
name|PackagerResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|File
name|repoRoot
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/packager/repo"
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|addIvyPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/ivy.xml"
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
name|setPackagerPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/packager.xml"
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
name|setBuildRoot
argument_list|(
name|builddir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setResourceCache
argument_list|(
name|cachedir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setPreserveBuildDirectories
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setVerbose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setProperty
argument_list|(
literal|"packager.website.url"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/repositories/packager/website"
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
name|setName
argument_list|(
literal|"packager"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"packager"
argument_list|,
name|resolver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get module descriptor
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"mod"
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
name|Date
name|pubdate
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2004
argument_list|,
literal|10
argument_list|,
literal|1
argument_list|,
literal|11
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|pubdate
argument_list|,
name|rmr
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
expr_stmt|;
comment|// Download artifact
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|pubdate
argument_list|,
literal|"mod"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"downloaddetails: "
operator|+
name|ar
operator|.
name|getDownloadDetails
argument_list|()
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
name|SUCCESSFUL
argument_list|,
name|ar
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// Verify resource cache now contains the distribution archive
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cachedir
argument_list|,
literal|"mod-1.0.tar.gz"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Download again, should use Ivy cache this time
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
comment|// Now download the maven2 artifact
name|artifact
operator|=
name|DefaultArtifact
operator|.
name|cloneWithAnotherName
argument_list|(
name|artifact
argument_list|,
literal|"foobar-janfu"
argument_list|)
expr_stmt|;
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
name|SUCCESSFUL
argument_list|,
name|ar
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testZipResourceInclusion
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
comment|// Create and configure resolver
name|PackagerResolver
name|resolver
init|=
operator|new
name|PackagerResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|File
name|repoRoot
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-1179/repo"
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|addIvyPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/ivy.xml"
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
name|setPackagerPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/packager.xml"
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
name|setBuildRoot
argument_list|(
name|builddir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setResourceCache
argument_list|(
name|cachedir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setPreserveBuildDirectories
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setVerbose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setProperty
argument_list|(
literal|"packager.website.url"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-1179/website"
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
name|setName
argument_list|(
literal|"packager"
argument_list|)
expr_stmt|;
comment|// Get module descriptor
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"A"
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
name|data
argument_list|)
decl_stmt|;
comment|// Download artifact
name|Artifact
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
literal|"A"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
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
comment|// assert that the file A.jar is extracted from the archive
name|File
name|jar
init|=
operator|new
name|File
argument_list|(
name|builddir
argument_list|,
literal|"org/A/1.0/artifacts/jars/A.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jar
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert that the file README is not extracted from the archive
name|File
name|readme
init|=
operator|new
name|File
argument_list|(
name|builddir
argument_list|,
literal|"org/A/1.0/extract/A-1.0/README"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|readme
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testTarResourceInclusion
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
comment|// Create and configure resolver
name|PackagerResolver
name|resolver
init|=
operator|new
name|PackagerResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|File
name|repoRoot
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-1179/repo"
argument_list|)
decl_stmt|;
name|resolver
operator|.
name|addIvyPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/ivy.xml"
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
name|setPackagerPattern
argument_list|(
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/packager.xml"
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
name|setBuildRoot
argument_list|(
name|builddir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setResourceCache
argument_list|(
name|cachedir
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setPreserveBuildDirectories
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setVerbose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setProperty
argument_list|(
literal|"packager.website.url"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-1179/website"
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
name|setName
argument_list|(
literal|"packager"
argument_list|)
expr_stmt|;
comment|// Get module descriptor
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"B"
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
name|data
argument_list|)
decl_stmt|;
comment|// Download artifact
name|Artifact
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
literal|"B"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
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
comment|// assert that the file B.jar is extracted from the archive
name|File
name|jar
init|=
operator|new
name|File
argument_list|(
name|builddir
argument_list|,
literal|"org/B/1.0/artifacts/jars/B.jar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jar
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert that the file README is not extracted from the archive
name|File
name|readme
init|=
operator|new
name|File
argument_list|(
name|builddir
argument_list|,
literal|"org/B/1.0/extract/B-1.0/README"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|readme
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

