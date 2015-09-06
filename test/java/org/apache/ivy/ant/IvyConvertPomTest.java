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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|IvyConvertPomTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|IvyConvertPom
name|task
init|=
operator|new
name|IvyConvertPom
argument_list|()
decl_stmt|;
name|task
operator|.
name|setProject
argument_list|(
name|TestHelper
operator|.
name|newProject
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|setPomFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/test.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
literal|".xml"
argument_list|)
decl_stmt|;
name|destFile
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|task
operator|.
name|setIvyFile
argument_list|(
name|destFile
argument_list|)
expr_stmt|;
name|task
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// do not work properly on all platform and depends on the file date
comment|// keep the code in comments in case someone manage to fix this and to highlight the fact
comment|// that this is not checked
comment|// String wrote = FileUtil.readEntirely(new BufferedReader(new FileReader(destFile)));
comment|// String expected = readEntirely("test-convertpom.xml").replaceAll("\r\n", "\n").replace(
comment|// '\r', '\n');
comment|// assertEquals(expected, wrote);
block|}
comment|// private String readEntirely(String resource) throws IOException {
comment|// return FileUtil.readEntirely(
comment|// new BufferedReader(new InputStreamReader(IvyConvertPomTest.class.getResource(resource)
comment|// .openStream()))).replaceAll("\r\n", "\n").replace('\r', '\n');
comment|// }
block|}
end_class

end_unit

