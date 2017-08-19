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
name|core
operator|.
name|deliver
package|;
end_package

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
name|ant
operator|.
name|IvyDeliver
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
name|ant
operator|.
name|IvyResolve
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
name|net
operator|.
name|URI
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
name|DeliverTest
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|File
name|deliverDir
decl_stmt|;
specifier|private
name|IvyDeliver
name|ivyDeliver
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|createCache
argument_list|()
expr_stmt|;
name|deliverDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/test/deliver"
argument_list|)
expr_stmt|;
name|deliverDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Project
name|project
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|project
operator|.
name|init
argument_list|()
expr_stmt|;
name|ivyDeliver
operator|=
operator|new
name|IvyDeliver
argument_list|()
expr_stmt|;
name|ivyDeliver
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|ivyDeliver
operator|.
name|setDeliverpattern
argument_list|(
name|deliverDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/[type]s/[artifact]-[revision](-[classifier]).[ext]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|deliverDir
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test case for IVY-1111.      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1111">IVY-1111</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testIVY1111
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|project
init|=
name|ivyDeliver
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/IVY-1111/ivysettings.xml"
argument_list|)
expr_stmt|;
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
operator|new
name|URI
argument_list|(
name|DeliverTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-1111.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|resolve
argument_list|(
name|ivyFile
argument_list|)
expr_stmt|;
name|ivyDeliver
operator|.
name|setReplacedynamicrev
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ivyDeliver
operator|.
name|doExecute
argument_list|()
expr_stmt|;
name|String
name|deliverContent
init|=
name|readFile
argument_list|(
name|deliverDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/ivys/ivy-1.0.xml"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|deliverContent
operator|.
name|contains
argument_list|(
literal|"rev=\"latest.integration\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|deliverContent
operator|.
name|contains
argument_list|(
literal|"name=\"b\" rev=\"1.5\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resolve
parameter_list|(
name|File
name|ivyFile
parameter_list|)
block|{
name|IvyResolve
name|ivyResolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|ivyResolve
operator|.
name|setProject
argument_list|(
name|ivyDeliver
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|ivyResolve
operator|.
name|setFile
argument_list|(
name|ivyFile
argument_list|)
expr_stmt|;
name|ivyResolve
operator|.
name|doExecute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|readFile
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|retval
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|ivyFile
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|retval
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

