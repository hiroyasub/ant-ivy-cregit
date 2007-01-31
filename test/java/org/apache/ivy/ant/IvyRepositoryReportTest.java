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
name|IvyRepositoryReportTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyRepositoryReport
name|_report
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
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.conf.file"
argument_list|,
literal|"test/repositories/ivyconf-1.xml"
argument_list|)
expr_stmt|;
name|_report
operator|=
operator|new
name|IvyRepositoryReport
argument_list|()
expr_stmt|;
name|_report
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|_report
operator|.
name|setCache
argument_list|(
name|_cache
argument_list|)
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
name|test
parameter_list|()
block|{
block|}
comment|// no xslt transformation is possible in the junit test on our continuous integration server for the moment...
comment|//    public void testGraph() throws Exception {
comment|//    	_report.setOrganisation("org1");
comment|//		_report.setXml(false);
comment|//		_report.setGraph(true);
comment|//		_report.setTodir(_cache);
comment|//		_report.setOutputname("test-graph");
comment|//    	_report.execute();
comment|//    	File graphml = new File(_cache, "test-graph.graphml");
comment|//    	assertTrue(graphml.exists());
comment|//    	String g = FileUtil.readEntirely(new BufferedReader(new FileReader(graphml)));
comment|//    	assertFalse(g.indexOf("caller") != -1);
comment|//    	assertTrue(g.indexOf("mod1.1") != -1);
comment|//    }
comment|//
comment|//    public void testDot() throws Exception {
comment|//    	_report.setOrganisation("org1");
comment|//		_report.setXml(false);
comment|//		_report.setDot(true);
comment|//		_report.setTodir(_cache);
comment|//		_report.setOutputname("test-graph");
comment|//    	_report.execute();
comment|//    	File dot = new File(_cache, "test-graph.dot");
comment|//    	assertTrue(dot.exists());
comment|//    	String g = FileUtil.readEntirely(new BufferedReader(new FileReader(dot)));
comment|//    	assertFalse(g.indexOf("caller") != -1);
comment|//    	assertTrue(g.indexOf("mod1.1") != -1);
comment|//    }
block|}
end_class

end_unit

