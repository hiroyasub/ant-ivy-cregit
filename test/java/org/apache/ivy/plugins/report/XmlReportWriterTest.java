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
name|report
package|;
end_package

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
name|DefaultResolutionCacheManager
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
name|util
operator|.
name|CacheCleaner
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
name|XMLHelper
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
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_class
specifier|public
class|class
name|XmlReportWriterTest
extends|extends
name|TestCase
block|{
specifier|private
name|Ivy
name|_ivy
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
name|_ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|_ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|createCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|_cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|_cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
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
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWriteOrigin
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/special-encoding-root-ivy.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlReportWriter
name|writer
init|=
operator|new
name|XmlReportWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|output
argument_list|(
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
name|buffer
operator|.
name|toString
argument_list|(
name|XmlReportWriter
operator|.
name|REPORT_ENCODING
argument_list|)
decl_stmt|;
name|String
name|expectedLocation
init|=
literal|"location=\""
operator|+
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.2/jars/mod1.2-2.0.jar"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"\""
decl_stmt|;
name|String
name|expectedIsLocal
init|=
literal|"is-local=\"true\""
decl_stmt|;
name|String
name|expectedOrg
init|=
literal|"organisation=\"sp\u00E9cial\""
decl_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain artifact location attribute"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|expectedLocation
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain artifact is-local attribute"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|expectedIsLocal
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain the organisation"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|expectedOrg
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// check that the XML is valid
name|XMLHelper
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffer
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testEscapeXml
parameter_list|()
throws|throws
name|Exception
block|{
name|_ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-635/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/plugins/report/ivy-635.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlReportWriter
name|writer
init|=
operator|new
name|XmlReportWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|output
argument_list|(
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|expectedArtName
init|=
literal|"art1&amp;_.txt"
decl_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain escaped artifact name"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|expectedArtName
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWriteModuleInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/plugins/report/ivy-with-info.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
operator|.
name|setValidate
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlReportWriter
name|writer
init|=
operator|new
name|XmlReportWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|output
argument_list|(
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|orgAttribute
init|=
literal|"organisation=\"org1\""
decl_stmt|;
name|String
name|modAttribute
init|=
literal|"module=\"mod1\""
decl_stmt|;
name|String
name|revAttribute
init|=
literal|"revision=\"1.0\""
decl_stmt|;
name|String
name|extra1Attribute
init|=
literal|"extra-blabla=\"abc\""
decl_stmt|;
name|String
name|extra2Attribute
init|=
literal|"extra-blabla2=\"123\""
decl_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain organisation attribute"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|orgAttribute
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain module attribute"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|modAttribute
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain revision attribute"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|revAttribute
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain extra attribute 1"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|extra1Attribute
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"XML doesn't contain extra attribute 2"
argument_list|,
name|xml
operator|.
name|indexOf
argument_list|(
name|extra2Attribute
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
return|;
block|}
block|}
end_class

end_unit

