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

begin_class
specifier|public
class|class
name|IvyCleanCacheTest
extends|extends
name|TestCase
block|{
specifier|private
name|IvyCleanCache
name|cleanCache
init|=
operator|new
name|IvyCleanCache
argument_list|()
decl_stmt|;
specifier|private
name|File
name|cacheDir
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
name|p
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|cacheDir
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setProperty
argument_list|(
literal|"cache"
argument_list|,
name|cacheDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|cleanCache
operator|.
name|setProject
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|IvyAntSettings
name|settings
init|=
name|IvyAntSettings
operator|.
name|getDefaultInstance
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|settings
operator|.
name|setUrl
argument_list|(
name|IvyCleanCacheTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-cleancache.xml"
argument_list|)
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClean
parameter_list|()
throws|throws
name|Exception
block|{
name|cacheDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|cacheDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|cleanCache
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|cacheDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

