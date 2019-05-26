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
name|report
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|XmlReportParserTest
block|{
specifier|private
name|Ivy
name|ivy
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
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
name|TestHelper
operator|.
name|createCache
argument_list|()
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
name|testGetResolvedModule
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
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
operator|.
name|setResolveId
argument_list|(
literal|"testGetResolvedModule"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|modRevId
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|ivy
operator|.
name|getResolutionCacheManager
argument_list|()
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
literal|"testGetResolvedModule"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|parsedModRevId
init|=
name|parser
operator|.
name|getResolvedModule
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Resolved module doesn't equals parsed module"
argument_list|,
name|modRevId
argument_list|,
name|parsedModRevId
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

