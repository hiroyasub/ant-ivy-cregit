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
name|osgi
operator|.
name|util
package|;
end_package

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
name|ArtifactTokensTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGoodMatching
parameter_list|()
block|{
specifier|final
name|String
name|repoResource
init|=
literal|"java/test-ivy/osgi/eclipse/plugins/org.eclipse.datatools.connectivity.ui_1.0.1.v200808121010"
decl_stmt|;
specifier|final
name|ArtifactTokens
name|tokens
init|=
operator|new
name|ArtifactTokens
argument_list|(
name|repoResource
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java/test-ivy/osgi/eclipse/plugins/"
argument_list|,
name|tokens
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.eclipse.datatools.connectivity.ui"
argument_list|,
name|tokens
operator|.
name|module
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.1"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|numbersAsString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v200808121010"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokens
operator|.
name|isJar
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGoodMatching2
parameter_list|()
block|{
specifier|final
name|String
name|repoResource
init|=
literal|"java/test-ivy/osgi/eclipse/plugins/org.eclipse.datatools.connectivity.ui_1.0.1"
decl_stmt|;
specifier|final
name|ArtifactTokens
name|tokens
init|=
operator|new
name|ArtifactTokens
argument_list|(
name|repoResource
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java/test-ivy/osgi/eclipse/plugins/"
argument_list|,
name|tokens
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.eclipse.datatools.connectivity.ui"
argument_list|,
name|tokens
operator|.
name|module
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.1"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|numbersAsString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokens
operator|.
name|isJar
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGoodMatching3
parameter_list|()
block|{
specifier|final
name|String
name|repoResource
init|=
literal|"java/test-ivy/osgi/eclipse/plugins/org.myorg.module.one_3.21.100.v20070530"
decl_stmt|;
specifier|final
name|ArtifactTokens
name|tokens
init|=
operator|new
name|ArtifactTokens
argument_list|(
name|repoResource
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java/test-ivy/osgi/eclipse/plugins/"
argument_list|,
name|tokens
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.myorg.module.one"
argument_list|,
name|tokens
operator|.
name|module
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3.21.100"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|numbersAsString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v20070530"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokens
operator|.
name|isJar
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGoodMatching4
parameter_list|()
block|{
specifier|final
name|String
name|repoResource
init|=
literal|"java/test-ivy/osgi/eclipse/plugins/org.eclipse.mylyn.tasks.ui_3.0.1.v20080721-2100-e33.jar"
decl_stmt|;
comment|// String repoResource =
comment|// "java/test-ivy/osgi/eclipse/plugins/org.eclipse.mylyn.tasks.ui_3.0.1.v20080721.jar";
specifier|final
name|ArtifactTokens
name|tokens
init|=
operator|new
name|ArtifactTokens
argument_list|(
name|repoResource
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java/test-ivy/osgi/eclipse/plugins/"
argument_list|,
name|tokens
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.eclipse.mylyn.tasks.ui"
argument_list|,
name|tokens
operator|.
name|module
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3.0.1"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|numbersAsString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"v20080721-2100-e33"
argument_list|,
name|tokens
operator|.
name|version
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokens
operator|.
name|isJar
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBadMatching
parameter_list|()
block|{
specifier|final
name|String
name|repoResource
init|=
literal|"java/test-ivy/osgi/eclipse/plugins/fake"
decl_stmt|;
specifier|final
name|ArtifactTokens
name|tokens
init|=
operator|new
name|ArtifactTokens
argument_list|(
name|repoResource
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|tokens
operator|.
name|prefix
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|tokens
operator|.
name|module
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|tokens
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

