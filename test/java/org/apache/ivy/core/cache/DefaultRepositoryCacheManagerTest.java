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
name|cache
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
name|util
operator|.
name|Date
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
name|module
operator|.
name|descriptor
operator|.
name|Artifact
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
name|descriptor
operator|.
name|DefaultArtifact
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
name|ModuleId
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
name|settings
operator|.
name|IvySettings
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

begin_comment
comment|/**  * @see DefaultResolutionCacheManager  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryCacheManagerTest
extends|extends
name|TestCase
block|{
specifier|private
name|DefaultRepositoryCacheManager
name|cacheManager
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|ArtifactOrigin
name|origin
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|f
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivycache"
argument_list|,
literal|".dir"
argument_list|)
decl_stmt|;
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configureDefault
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
comment|// we want to use the file as a directory, so we delete the file itself
name|cacheManager
operator|=
operator|new
name|DefaultRepositoryCacheManager
argument_list|()
expr_stmt|;
name|cacheManager
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|cacheManager
operator|.
name|setBasedir
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|Artifact
name|originArtifact
init|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"pom.original"
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|origin
operator|=
operator|new
name|ArtifactOrigin
argument_list|(
name|originArtifact
argument_list|,
literal|true
argument_list|,
literal|"file:/some/where.pom"
argument_list|)
expr_stmt|;
name|cacheManager
operator|.
name|saveArtifactOrigin
argument_list|(
name|originArtifact
argument_list|,
name|origin
argument_list|)
expr_stmt|;
name|cacheManager
operator|.
name|saveArtifactOrigin
argument_list|(
name|artifact
argument_list|,
name|origin
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
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
name|cacheManager
operator|.
name|getRepositoryCacheRoot
argument_list|()
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
name|testArtifactOrigin
parameter_list|()
block|{
name|ArtifactOrigin
name|found
init|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|origin
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pom"
argument_list|,
name|found
operator|.
name|getArtifact
argument_list|()
operator|.
name|getExt
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type2"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUniqueness
parameter_list|()
block|{
name|cacheManager
operator|.
name|saveArtifactOrigin
argument_list|(
name|artifact
argument_list|,
name|origin
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org1"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|ArtifactOrigin
name|found
init|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module1"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev1"
argument_list|,
literal|"name"
argument_list|,
literal|"type"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name1"
argument_list|,
literal|"type"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type1"
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"org"
argument_list|,
literal|"module"
argument_list|,
literal|"rev"
argument_list|,
literal|"name"
argument_list|,
literal|"type"
argument_list|,
literal|"ext1"
argument_list|)
expr_stmt|;
name|found
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|found
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|rev
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
name|ModuleId
name|mid
init|=
operator|new
name|ModuleId
argument_list|(
name|org
argument_list|,
name|module
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
operator|new
name|ModuleRevisionId
argument_list|(
name|mid
argument_list|,
name|rev
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

