begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|GregorianCalendar
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DefaultModuleDescriptor
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|XmlModuleDescriptorWriterTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_dest
init|=
operator|new
name|File
argument_list|(
literal|"build/test/test-write.xml"
argument_list|)
decl_stmt|;
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
name|Ivy
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
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|_dest
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|_dest
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
name|_dest
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
name|Ivy
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
name|_dest
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|_dest
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
name|_dest
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
name|Ivy
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
literal|true
argument_list|)
decl_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|_dest
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|_dest
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
name|_dest
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
specifier|public
name|void
name|setUp
parameter_list|()
block|{
if|if
condition|(
name|_dest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|_dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|_dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|_dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|_dest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|_dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

