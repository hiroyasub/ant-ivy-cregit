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
name|IOException
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
name|ModuleId
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
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorParser
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
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorParserTest
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
operator|.
name|Visibility
operator|.
name|PUBLIC
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLAssert
operator|.
name|assertXMLEqual
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|XmlModuleDescriptorWriterTest
block|{
specifier|private
specifier|static
name|String
name|LICENSE
decl_stmt|;
static|static
block|{
try|try
block|{
name|LICENSE
operator|=
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
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"license.xml"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|File
name|dest
init|=
operator|new
name|File
argument_list|(
literal|"build/test/test-write.xml"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultModuleDescriptor
name|md
init|=
operator|(
name|DefaultModuleDescriptor
operator|)
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-simple.xml"
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|md
operator|.
name|setResolvedPublicationDate
argument_list|(
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|4
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
argument_list|)
expr_stmt|;
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
literal|"NONE"
argument_list|)
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-simple.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultModuleDescriptor
name|md
init|=
operator|(
name|DefaultModuleDescriptor
operator|)
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-info.xml"
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|md
operator|.
name|setResolvedPublicationDate
argument_list|(
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|4
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
argument_list|)
expr_stmt|;
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
literal|"NONE"
argument_list|)
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-info.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-dependencies.xml"
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-dependencies.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFull
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test.xml"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-full.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraInfos
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-extrainfo.xml"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-extrainfo.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraInfosWithNestedElement
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-extrainfo-nested.xml"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-extrainfo-nested.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraInfosFromMaven
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
name|IvySettings
argument_list|()
argument_list|,
name|PomModuleDescriptorParserTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-versionPropertyDependencyMgt.pom"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|wrote
operator|=
name|wrote
operator|.
name|replaceFirst
argument_list|(
literal|"publication=\"([0-9])*\""
argument_list|,
literal|"publication=\"20140429153143\""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wrote
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-extrainfo-from-maven.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\r'
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
name|assertXMLEqual
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtends
parameter_list|()
throws|throws
name|Exception
block|{
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
operator|new
name|IvySettings
argument_list|()
argument_list|,
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"test-extends-all.xml"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
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
name|wrote
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
operator|.
name|replaceAll
argument_list|(
literal|"\r\n?"
argument_list|,
literal|"\n"
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|readEntirely
argument_list|(
literal|"test-write-extends.xml"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r\n?"
argument_list|,
literal|"\n"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|wrote
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that the transitive attribute is written for non-transitive configurations.      *      *<code>&lt;conf ... transitive="false" ... /&gt;</code>      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1207">IVY-1207</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testTransitiveAttributeForNonTransitiveConfs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a ModuleDescriptor with a non-transitive configuration
name|DefaultModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
literal|"myorg"
argument_list|,
literal|"myname"
argument_list|)
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|(
literal|"conf"
argument_list|,
name|PUBLIC
argument_list|,
literal|"desc"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
comment|// When the ModuleDescriptor is written
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
argument_list|)
expr_stmt|;
comment|// Then the transitive attribute must be set to false
name|String
name|output
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|String
name|writtenConf
init|=
name|output
operator|.
name|substring
argument_list|(
name|output
operator|.
name|indexOf
argument_list|(
literal|"<configurations>"
argument_list|)
operator|+
literal|16
argument_list|,
name|output
operator|.
name|indexOf
argument_list|(
literal|"</configurations>"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Transitive attribute not set to false: "
operator|+
name|writtenConf
argument_list|,
name|writtenConf
operator|.
name|contains
argument_list|(
literal|"transitive=\"false\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test that the transitive attribute is not written when the configuration IS transitive.      *      * This is the default and writing it will only add noise and cause a deviation from the known      * behavior (before fixing IVY-1207).      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1207">IVY-1207</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testTransitiveAttributeNotWrittenForTransitiveConfs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a ModuleDescriptor with a transitive configuration
name|DefaultModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
literal|"myorg"
argument_list|,
literal|"myname"
argument_list|)
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|Configuration
name|conf
init|=
operator|new
name|Configuration
argument_list|(
literal|"conf"
argument_list|,
name|PUBLIC
argument_list|,
literal|"desc"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
comment|// When the ModuleDescriptor is written
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|LICENSE
argument_list|,
name|dest
argument_list|)
expr_stmt|;
comment|// Then the transitive attribute must NOT be written
name|String
name|output
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|String
name|writtenConf
init|=
name|output
operator|.
name|substring
argument_list|(
name|output
operator|.
name|indexOf
argument_list|(
literal|"<configurations>"
argument_list|)
operator|+
literal|16
argument_list|,
name|output
operator|.
name|indexOf
argument_list|(
literal|"</configurations>"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Transitive attribute set: "
operator|+
name|writtenConf
argument_list|,
name|writtenConf
operator|.
name|contains
argument_list|(
literal|"transitive="
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|readEntirely
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
return|return
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
name|XmlModuleDescriptorWriterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|resource
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

