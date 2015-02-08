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
name|ant
operator|.
name|AntWorkspaceResolver
operator|.
name|WorkspaceArtifact
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
name|FileSet
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
name|Path
import|;
end_import

begin_class
specifier|public
class|class
name|AntBuildResolverTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_MODULE1
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org.acme"
argument_list|,
literal|"module1"
argument_list|,
literal|"1.1"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ModuleRevisionId
name|MRID_PROJECT1
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org.apache.ivy.test"
argument_list|,
literal|"project1"
argument_list|,
literal|"0.1"
argument_list|)
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
specifier|private
name|IvyConfigure
name|configure
decl_stmt|;
specifier|private
name|WorkspaceArtifact
name|wa
decl_stmt|;
annotation|@
name|Override
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
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|DefaultLogger
name|logger
init|=
operator|new
name|DefaultLogger
argument_list|()
decl_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
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
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|AntWorkspaceResolver
name|antWorkspaceResolver
init|=
operator|new
name|AntWorkspaceResolver
argument_list|()
decl_stmt|;
name|antWorkspaceResolver
operator|.
name|setName
argument_list|(
literal|"test-workspace"
argument_list|)
expr_stmt|;
name|wa
operator|=
name|antWorkspaceResolver
operator|.
name|createArtifact
argument_list|()
expr_stmt|;
name|FileSet
name|fileset
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fileset
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace"
argument_list|)
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setIncludes
argument_list|(
literal|"*/ivy.xml"
argument_list|)
expr_stmt|;
name|antWorkspaceResolver
operator|.
name|addConfigured
argument_list|(
name|fileset
argument_list|)
expr_stmt|;
name|antWorkspaceResolver
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|=
operator|new
name|IvyConfigure
argument_list|()
expr_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|configure
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|configure
operator|.
name|addConfiguredWorkspaceResolver
argument_list|(
name|antWorkspaceResolver
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
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
annotation|@
name|Override
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
specifier|public
name|void
name|testNoProject
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_MODULE1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProject
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project2/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_PROJECT1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_MODULE1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/dist/jars/project1.jar"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getArtifact
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/dist/jars/project1.jar"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProjectFolder
parameter_list|()
throws|throws
name|Exception
block|{
name|wa
operator|.
name|setPath
argument_list|(
literal|"target/classes"
argument_list|)
expr_stmt|;
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project2/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_PROJECT1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_MODULE1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/classes"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getArtifact
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/classes"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencyArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project3/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|ResolveReport
name|report
init|=
operator|(
name|ResolveReport
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_PROJECT1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MRID_MODULE1
argument_list|,
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getDownloadStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/dist/jars/project1.jar"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getArtifact
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/dist/jars/project1.jar"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|MRID_PROJECT1
argument_list|)
index|[
literal|0
index|]
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCachePath
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project2/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvyCachePath
name|cachePath
init|=
operator|new
name|IvyCachePath
argument_list|()
decl_stmt|;
name|cachePath
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|cachePath
operator|.
name|setPathid
argument_list|(
literal|"test.cachepath.id"
argument_list|)
expr_stmt|;
name|cachePath
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Path
name|path
init|=
operator|(
name|Path
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"test.cachepath.id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|path
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/dist/jars/project1.jar"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|path
operator|.
name|list
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"org.acme/module1/jars/module1-1.1.jar"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|path
operator|.
name|list
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCachePathFolder
parameter_list|()
throws|throws
name|Exception
block|{
name|wa
operator|.
name|setPath
argument_list|(
literal|"target/classes"
argument_list|)
expr_stmt|;
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project2/ivy.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setKeep
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|IvyCachePath
name|cachePath
init|=
operator|new
name|IvyCachePath
argument_list|()
decl_stmt|;
name|cachePath
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|cachePath
operator|.
name|setPathid
argument_list|(
literal|"test.cachepath.id"
argument_list|)
expr_stmt|;
name|cachePath
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Path
name|path
init|=
operator|(
name|Path
operator|)
name|project
operator|.
name|getReference
argument_list|(
literal|"test.cachepath.id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|path
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/workspace/project1/target/classes"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|path
operator|.
name|list
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"org.acme/module1/jars/module1-1.1.jar"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|path
operator|.
name|list
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

