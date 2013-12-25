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
name|FileNotFoundException
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
name|text
operator|.
name|ParseException
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
name|DefaultLogger
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
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|BuildOBRTask
name|buildObr
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
name|DefaultLogger
name|logger
init|=
operator|new
name|DefaultLogger
argument_list|()
decl_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setOutputPrintStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setErrorPrintStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|project
operator|.
name|addBuildListener
argument_list|(
name|logger
argument_list|)
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
name|project
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
specifier|private
name|BundleRepoDescriptor
name|readObr
parameter_list|(
name|File
name|obrFile
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|ParseException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|BundleRepoDescriptor
name|obr
decl_stmt|;
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|obrFile
argument_list|)
decl_stmt|;
try|try
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
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|obr
return|;
block|}
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
operator|new
name|Project
argument_list|()
decl_stmt|;
name|DefaultLogger
name|logger
init|=
operator|new
name|DefaultLogger
argument_list|()
decl_stmt|;
name|logger
operator|.
name|setOutputPrintStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setErrorPrintStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
name|otherProject
operator|.
name|addBuildListener
argument_list|(
name|logger
argument_list|)
expr_stmt|;
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

