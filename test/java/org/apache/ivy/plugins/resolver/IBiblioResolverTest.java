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
name|cache
operator|.
name|CacheManager
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
name|DefaultDependencyArtifactDescriptor
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
name|matcher
operator|.
name|ExactPatternMatcher
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|IBiblioResolverTest
extends|extends
name|TestCase
block|{
comment|// remote.test
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
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|_settings
argument_list|,
name|_cache
argument_list|)
argument_list|)
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
block|}
specifier|public
name|void
name|testDefaults
parameter_list|()
block|{
name|IBiblioResolver
name|resolver
init|=
operator|new
name|IBiblioResolver
argument_list|()
decl_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.root"
argument_list|,
literal|"http://www.ibiblio.org/mymaven/"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.pattern"
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
name|getArtifactPatterns
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
specifier|public
name|void
name|testInitFromConf
parameter_list|()
throws|throws
name|Exception
block|{
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.root"
argument_list|,
literal|"http://www.ibiblio.org/maven/"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.pattern"
argument_list|,
literal|"[module]/jars/[artifact]-[revision].jar"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"my.ibiblio.root"
argument_list|,
literal|"http://www.ibiblio.org/mymaven/"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
literal|"my.ibiblio.pattern"
argument_list|,
literal|"[module]/[artifact]-[revision].jar"
argument_list|)
expr_stmt|;
name|_settings
operator|.
name|load
argument_list|(
name|IBiblioResolverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ibiblioresolverconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|IBiblioResolver
name|resolver
init|=
operator|(
name|IBiblioResolver
operator|)
name|_settings
operator|.
name|getResolver
argument_list|(
literal|"ibiblioA"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|List
name|l
init|=
name|resolver
operator|.
name|getArtifactPatterns
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
literal|"http://www.ibiblio.org/mymaven/[module]/[artifact]-[revision].jar"
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|(
name|IBiblioResolver
operator|)
name|_settings
operator|.
name|getResolver
argument_list|(
literal|"ibiblioB"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
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
literal|"http://www.ibiblio.org/mymaven/[organisation]/jars/[artifact]-[revision].jar"
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|(
name|IBiblioResolver
operator|)
name|_settings
operator|.
name|getResolver
argument_list|(
literal|"ibiblioC"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resolver
operator|.
name|isM2compatible
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
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
literal|"http://www.ibiblio.org/maven2/[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
argument_list|,
name|l
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|=
operator|(
name|IBiblioResolver
operator|)
name|_settings
operator|.
name|getResolver
argument_list|(
literal|"ibiblioD"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|resolver
operator|.
name|isM2compatible
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resolver
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
literal|"http://www.ibiblio.org/maven/[module]/jars/[artifact]-[revision].jar"
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
specifier|public
name|void
name|testIBiblio
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ibiblioRoot
init|=
name|IBiblioHelper
operator|.
name|getIBiblioMirror
argument_list|()
decl_stmt|;
if|if
condition|(
name|ibiblioRoot
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|IBiblioResolver
name|resolver
init|=
operator|new
name|IBiblioResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setRoot
argument_list|(
name|ibiblioRoot
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resolver
operator|.
name|getName
argument_list|()
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
literal|"commons-fileupload"
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
literal|"commons-fileupload"
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
operator|new
name|DownloadOptions
argument_list|(
name|_settings
argument_list|,
name|_cache
argument_list|)
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
argument_list|(
name|_settings
argument_list|,
name|_cache
argument_list|)
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
name|testIBiblioArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ibiblioRoot
init|=
name|IBiblioHelper
operator|.
name|getIBiblioMirror
argument_list|()
decl_stmt|;
if|if
condition|(
name|ibiblioRoot
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|IBiblioResolver
name|resolver
init|=
operator|new
name|IBiblioResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setRoot
argument_list|(
name|ibiblioRoot
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|resolver
operator|.
name|getName
argument_list|()
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
literal|"nanning"
argument_list|,
literal|"0.9"
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addDependencyArtifactIncludes
argument_list|(
literal|"default"
argument_list|,
operator|new
name|DefaultDependencyArtifactDescriptor
argument_list|(
name|dd
argument_list|,
literal|"nanning-profiler"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|true
argument_list|,
name|ExactPatternMatcher
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyArtifactIncludes
argument_list|(
literal|"default"
argument_list|,
operator|new
name|DefaultDependencyArtifactDescriptor
argument_list|(
name|dd
argument_list|,
literal|"nanning-trace"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|true
argument_list|,
name|ExactPatternMatcher
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|resolver
operator|.
name|getDependency
argument_list|(
name|dd
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
name|DefaultArtifact
name|profiler
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
literal|"nanning-profiler"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|DefaultArtifact
name|trace
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
literal|"nanning-trace"
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
name|profiler
block|,
name|trace
block|}
argument_list|,
operator|new
name|DownloadOptions
argument_list|(
name|_settings
argument_list|,
name|_cache
argument_list|)
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
name|ArtifactDownloadReport
name|ar
init|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|profiler
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|profiler
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
name|ar
operator|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|trace
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|trace
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
name|profiler
block|,
name|trace
block|}
argument_list|,
operator|new
name|DownloadOptions
argument_list|(
name|_settings
argument_list|,
name|_cache
argument_list|)
argument_list|)
expr_stmt|;
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
name|ar
operator|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|profiler
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|profiler
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
name|ar
operator|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|trace
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|trace
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
name|testUnknown
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ibiblioRoot
init|=
name|IBiblioHelper
operator|.
name|getIBiblioMirror
argument_list|()
decl_stmt|;
if|if
condition|(
name|ibiblioRoot
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|IBiblioResolver
name|resolver
init|=
operator|new
name|IBiblioResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setRoot
argument_list|(
name|ibiblioRoot
argument_list|)
expr_stmt|;
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
name|assertNull
argument_list|(
name|resolver
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"unknown"
argument_list|,
literal|"unknown"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|,
name|_data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
