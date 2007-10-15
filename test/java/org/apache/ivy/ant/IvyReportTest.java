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
name|IvyReportTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyReport
name|report
decl_stmt|;
specifier|private
name|Project
name|project
decl_stmt|;
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
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|report
operator|=
operator|new
name|IvyReport
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTaskName
argument_list|(
literal|"report"
argument_list|)
expr_stmt|;
name|report
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|report
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
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
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMultipleConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-multiconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-compile.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-compile.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRegularCircular
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/repositories/2/mod11.1/ivy-1.0.xml"
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|setXml
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// do not test any xsl transformation here, because of problems of build in our continuous
comment|// integration server
name|report
operator|.
name|setXsl
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|report
operator|.
name|setGraph
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/org11-mod11.1-compile.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

