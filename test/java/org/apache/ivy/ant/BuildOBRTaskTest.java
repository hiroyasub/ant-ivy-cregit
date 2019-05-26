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
name|ant
package|;
end_package

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
name|FileInputStream
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
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|OBRXMLParser
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
name|osgi
operator|.
name|repo
operator|.
name|BundleRepoDescriptor
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
name|CollectionUtils
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_class
specifier|public
class|class
name|BuildOBRTaskTest
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|BuildOBRTask
name|buildObr
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|createCache
argument_list|()
expr_stmt|;
name|buildObr
operator|=
operator|new
name|BuildOBRTask
argument_list|()
expr_stmt|;
name|buildObr
operator|.
name|setProject
argument_list|(
name|TestHelper
operator|.
name|newProject
argument_list|()
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
specifier|private
name|BundleRepoDescriptor
name|readObr
parameter_list|(
name|File
name|obrFile
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|BundleRepoDescriptor
name|obr
decl_stmt|;
try|try
init|(
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|obrFile
argument_list|)
init|)
block|{
name|obr
operator|=
name|OBRXMLParser
operator|.
name|parse
argument_list|(
name|obrFile
operator|.
name|toURI
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|obr
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDir
parameter_list|()
throws|throws
name|Exception
block|{
name|buildObr
operator|.
name|setBaseDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-repo/bundlerepo"
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|obrFile
init|=
operator|new
name|File
argument_list|(
literal|"build/cache/obr.xml"
argument_list|)
decl_stmt|;
name|buildObr
operator|.
name|setOut
argument_list|(
name|obrFile
argument_list|)
expr_stmt|;
name|buildObr
operator|.
name|execute
argument_list|()
expr_stmt|;
name|BundleRepoDescriptor
name|obr
init|=
name|readObr
argument_list|(
name|obrFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|CollectionUtils
operator|.
name|toList
argument_list|(
name|obr
operator|.
name|getModules
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyDir
parameter_list|()
throws|throws
name|Exception
block|{
name|buildObr
operator|.
name|setBaseDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/composite"
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|obrFile
init|=
operator|new
name|File
argument_list|(
literal|"build/cache/obr.xml"
argument_list|)
decl_stmt|;
name|buildObr
operator|.
name|setOut
argument_list|(
name|obrFile
argument_list|)
expr_stmt|;
name|buildObr
operator|.
name|execute
argument_list|()
expr_stmt|;
name|BundleRepoDescriptor
name|obr
init|=
name|readObr
argument_list|(
name|obrFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|CollectionUtils
operator|.
name|toList
argument_list|(
name|obr
operator|.
name|getModules
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolve
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|otherProject
init|=
name|TestHelper
operator|.
name|newProject
argument_list|()
decl_stmt|;
name|otherProject
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/test-repo/bundlerepo/ivysettings.xml"
argument_list|)
expr_stmt|;
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|otherProject
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-repo/ivy-test-buildobr.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setResolveId
argument_list|(
literal|"withResolveId"
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|obrFile
init|=
operator|new
name|File
argument_list|(
literal|"build/cache/obr.xml"
argument_list|)
decl_stmt|;
name|buildObr
operator|.
name|setProject
argument_list|(
name|otherProject
argument_list|)
expr_stmt|;
name|buildObr
operator|.
name|setResolveId
argument_list|(
literal|"withResolveId"
argument_list|)
expr_stmt|;
name|buildObr
operator|.
name|setOut
argument_list|(
name|obrFile
argument_list|)
expr_stmt|;
name|buildObr
operator|.
name|execute
argument_list|()
expr_stmt|;
name|BundleRepoDescriptor
name|obr
init|=
name|readObr
argument_list|(
name|obrFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|CollectionUtils
operator|.
name|toList
argument_list|(
name|obr
operator|.
name|getModules
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

