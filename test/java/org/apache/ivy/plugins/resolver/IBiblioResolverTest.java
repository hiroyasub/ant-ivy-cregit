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
name|assertFalse
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|IvyContext
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
name|IvyPatternHelper
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
name|descriptor
operator|.
name|DefaultIncludeRule
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
name|ArtifactId
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
name|search
operator|.
name|ModuleEntry
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
name|search
operator|.
name|OrganisationEntry
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
name|search
operator|.
name|RevisionEntry
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
name|ivy
operator|.
name|util
operator|.
name|MockMessageLogger
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|IBiblioResolverTest
extends|extends
name|AbstractDependencyResolverTest
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
argument_list|<
name|String
argument_list|>
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
annotation|@
name|Test
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
argument_list|<
name|String
argument_list|>
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
literal|"https://repo1.maven.org/maven2/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"
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
name|resolver
operator|=
operator|(
name|IBiblioResolver
operator|)
name|_settings
operator|.
name|getResolver
argument_list|(
literal|"ibiblioE"
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
literal|"http://www.ibiblio.org/mymaven/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"
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
literal|"ibiblioF"
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
literal|"http://www.ibiblio.org/mymaven/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"
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
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testMaven2Listing
parameter_list|()
throws|throws
name|Exception
block|{
name|IBiblioResolver
name|resolver
init|=
operator|new
name|IBiblioResolver
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
name|resolver
operator|.
name|setM2compatible
argument_list|(
literal|true
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
name|ModuleEntry
index|[]
name|modules
init|=
name|resolver
operator|.
name|listModules
argument_list|(
operator|new
name|OrganisationEntry
argument_list|(
name|resolver
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|modules
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"commons-lang"
argument_list|,
name|modules
index|[
literal|0
index|]
operator|.
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
name|RevisionEntry
index|[]
name|revisions
init|=
name|resolver
operator|.
name|listRevisions
argument_list|(
name|modules
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|revisions
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Map
name|otherTokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|otherTokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
literal|"commons-lang"
argument_list|)
expr_stmt|;
name|String
index|[]
name|values
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|otherTokenValues
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"commons-lang"
argument_list|,
name|values
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Map
index|[]
name|valuesMaps
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|}
argument_list|,
name|otherTokenValues
argument_list|)
decl_stmt|;
name|Set
name|vals
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
name|valuesMap
range|:
name|valuesMaps
control|)
block|{
name|vals
operator|.
name|add
argument_list|(
name|valuesMap
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|values
operator|=
operator|(
name|String
index|[]
operator|)
name|vals
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|vals
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"commons-lang"
argument_list|,
name|values
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testErrorReport
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"http://unknown.host.comx/"
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
name|setM2compatible
argument_list|(
literal|true
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
name|MockMessageLogger
name|mockMessageImpl
init|=
operator|new
name|MockMessageLogger
argument_list|()
decl_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|setDefaultLogger
argument_list|(
name|mockMessageImpl
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org.apache"
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
name|assertNull
argument_list|(
name|rmr
argument_list|)
expr_stmt|;
name|mockMessageImpl
operator|.
name|assertLogContains
argument_list|(
literal|"tried http://unknown.host.comx/org/apache/commons-fileupload/1.0/commons-fileupload-1.0.pom"
argument_list|)
expr_stmt|;
name|mockMessageImpl
operator|.
name|assertLogContains
argument_list|(
literal|"tried http://unknown.host.comx/org/apache/commons-fileupload/1.0/commons-fileupload-1.0.jar"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

