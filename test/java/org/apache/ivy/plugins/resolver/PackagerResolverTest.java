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
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|net
operator|.
name|URL
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
name|plugins
operator|.
name|resolver
operator|.
name|packager
operator|.
name|SubProcess
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

begin_comment
comment|/**  * Tests PackagerResolver.  */
end_comment

begin_comment
comment|// junit
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
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|File
name|_workdir
decl_stmt|;
specifier|private
name|File
name|_builddir
decl_stmt|;
specifier|private
name|File
name|_cachedir
decl_stmt|;
specifier|private
name|File
name|_websitedir
decl_stmt|;
comment|// really a symlink
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
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
name|_cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
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
name|_cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|setDefaultCache
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
comment|// Create work space with build and resource cache directories
name|_workdir
operator|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
argument_list|,
literal|"PackagerResolverTest"
argument_list|)
expr_stmt|;
name|_builddir
operator|=
operator|new
name|File
argument_list|(
name|_workdir
argument_list|,
literal|"build"
argument_list|)
expr_stmt|;
name|_cachedir
operator|=
operator|new
name|File
argument_list|(
name|_workdir
argument_list|,
literal|"resources"
argument_list|)
expr_stmt|;
name|_websitedir
operator|=
operator|new
name|File
argument_list|(
name|_workdir
argument_list|,
literal|"website"
argument_list|)
expr_stmt|;
name|cleanupTempDirs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|_builddir
operator|.
name|mkdirs
argument_list|()
operator|||
operator|!
name|_cachedir
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
name|_workdir
argument_list|)
throw|;
block|}
comment|// Add symlink to create "website"
name|String
name|linkFrom
init|=
operator|new
name|File
argument_list|(
literal|"test/repositories/packager/website"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|String
name|linkTo
init|=
name|_websitedir
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|SubProcess
name|proc
init|=
operator|new
name|SubProcess
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"ln"
block|,
literal|"-sf"
block|,
name|linkFrom
block|,
name|linkTo
block|}
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|proc
operator|.
name|run
argument_list|()
operator|!=
literal|0
condition|)
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"can't symlink "
operator|+
name|linkFrom
operator|+
literal|" -> "
operator|+
name|linkTo
argument_list|)
throw|;
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
name|_cache
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
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
name|PackagerResolver
operator|.
name|deleteRecursive
argument_list|(
name|_builddir
argument_list|)
expr_stmt|;
name|PackagerResolver
operator|.
name|deleteRecursive
argument_list|(
name|_cachedir
argument_list|)
expr_stmt|;
name|_websitedir
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testFile
parameter_list|()
throws|throws
name|Exception
block|{
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
name|_settings
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
literal|""
operator|+
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/ivy.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setPackagerPattern
argument_list|(
literal|""
operator|+
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
literal|"[organisation]/[module]/[revision]/packager.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setBuildRoot
argument_list|(
name|_builddir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setResourceCache
argument_list|(
name|_cachedir
operator|.
name|getAbsolutePath
argument_list|()
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
name|_data
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
name|_cachedir
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
block|}
end_class

end_unit

