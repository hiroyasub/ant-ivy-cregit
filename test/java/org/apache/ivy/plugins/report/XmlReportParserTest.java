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

begin_class
specifier|public
class|class
name|XmlReportParserTest
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
name|testGetResolvedModule
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
name|_ivy
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

