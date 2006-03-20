begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|external
operator|.
name|m2
package|;
end_package

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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DependencyDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|parser
operator|.
name|AbstractModuleDescriptorParserTester
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|repository
operator|.
name|url
operator|.
name|URLResource
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|xml
operator|.
name|XmlModuleDescriptorParserTest
import|;
end_import

begin_class
specifier|public
class|class
name|PomModuleDescriptorParserTest
extends|extends
name|AbstractModuleDescriptorParserTester
block|{
comment|// junit test -- DO NOT REMOVE used by ant to know it's a junit test
specifier|public
name|void
name|testAccept
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|accept
argument_list|(
operator|new
name|URLResource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-simple.pom"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|accept
argument_list|(
operator|new
name|URLResource
argument_list|(
name|XmlModuleDescriptorParserTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test.xml"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-simple.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md
operator|.
name|getConfigurations
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|PomModuleDescriptorParser
operator|.
name|MAVEN2_CONFIGURATIONS
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Artifact
index|[]
name|artifact
init|=
name|md
operator|.
name|getArtifacts
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifact
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|artifact
index|[
literal|0
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|artifact
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParent
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-parent.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md
operator|.
name|getConfigurations
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|PomModuleDescriptorParser
operator|.
name|MAVEN2_CONFIGURATIONS
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Artifact
index|[]
name|artifact
init|=
name|md
operator|.
name|getArtifacts
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifact
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|artifact
index|[
literal|0
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|artifact
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependencies
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-dependencies.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.4"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWithoutVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-without-version.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|ModuleId
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.4"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-properties.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"drools"
argument_list|,
literal|"drools-smf"
argument_list|,
literal|"2.0-beta-18"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"drools"
argument_list|,
literal|"drools-core"
argument_list|,
literal|"2.0-beta-18"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReal
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"commons-lang-1.0.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"junit"
argument_list|,
literal|"junit"
argument_list|,
literal|"3.7"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOptional
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-optional.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"optional"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.4"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"optional"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile(*)"
block|,
literal|"runtime(*)"
block|,
literal|"master(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"optional"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"cglib"
argument_list|,
literal|"cglib"
argument_list|,
literal|"2.0.2"
argument_list|)
argument_list|,
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDependenciesWithScope
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-dependencies-with-scope.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"odmg"
argument_list|,
literal|"odmg"
argument_list|,
literal|"3.0"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile(*)"
block|,
literal|"runtime(*)"
block|,
literal|"master(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.4"
argument_list|)
argument_list|,
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"cglib"
argument_list|,
literal|"cglib"
argument_list|,
literal|"2.0.2"
argument_list|)
argument_list|,
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExclusion
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|Ivy
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-exclusion.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"fr.jayasoft"
argument_list|,
literal|"test"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|dds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dds
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-logging"
argument_list|,
literal|"commons-logging"
argument_list|,
literal|"1.0.4"
argument_list|)
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dds
index|[
literal|0
index|]
operator|.
name|getAllDependencyArtifactsExcludes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"dom4j"
argument_list|,
literal|"dom4j"
argument_list|,
literal|"1.6"
argument_list|)
argument_list|,
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|1
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertDependencyModulesExcludes
argument_list|(
name|dds
index|[
literal|1
index|]
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"compile"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"jaxme-api"
block|,
literal|"jaxen"
block|}
argument_list|)
expr_stmt|;
name|assertDependencyModulesExcludes
argument_list|(
name|dds
index|[
literal|1
index|]
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"runtime"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"jaxme-api"
block|,
literal|"jaxen"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"cglib"
argument_list|,
literal|"cglib"
argument_list|,
literal|"2.0.2"
argument_list|)
argument_list|,
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"compile"
block|,
literal|"runtime"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"master(*)"
block|,
literal|"compile(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"compile"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"runtime(*)"
block|}
argument_list|)
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dds
index|[
literal|2
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
literal|"runtime"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dds
index|[
literal|2
index|]
operator|.
name|getAllDependencyArtifactsExcludes
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

