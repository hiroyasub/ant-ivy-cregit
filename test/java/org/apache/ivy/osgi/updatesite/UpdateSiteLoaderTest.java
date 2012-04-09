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
name|updatesite
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
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|core
operator|.
name|cache
operator|.
name|CacheResourceOptions
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
name|ModuleDescriptor
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
name|ivy
operator|.
name|osgi
operator|.
name|repo
operator|.
name|RepoDescriptor
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
name|CacheCleaner
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
name|UpdateSiteLoaderTest
extends|extends
name|TestCase
block|{
specifier|private
name|UpdateSiteLoader
name|loader
decl_stmt|;
specifier|private
name|File
name|cache
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|IvySettings
name|ivySettings
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
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
name|ivySettings
operator|.
name|setDefaultCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|CacheResourceOptions
name|options
init|=
operator|new
name|CacheResourceOptions
argument_list|()
decl_stmt|;
name|loader
operator|=
operator|new
name|UpdateSiteLoader
argument_list|(
name|ivySettings
operator|.
name|getDefaultRepositoryCacheManager
argument_list|()
argument_list|,
literal|null
argument_list|,
name|options
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
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIvyDE
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
throws|,
name|URISyntaxException
block|{
name|RepoDescriptor
name|site
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://www.apache.org/dist/ant/ivyde/updatesite/"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|name
operator|.
name|startsWith
argument_list|(
literal|"org.apache.ivy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testM2Eclipse
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
throws|,
name|URISyntaxException
block|{
name|RepoDescriptor
name|site
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://m2eclipse.sonatype.org/sites/m2e/"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|50
argument_list|)
expr_stmt|;
name|Iterator
name|itModules
init|=
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itModules
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|itModules
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|name
argument_list|,
name|name
operator|.
name|indexOf
argument_list|(
literal|"org.maven"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|_disabled_testHeliosEclipse
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
throws|,
name|URISyntaxException
block|{
name|RepoDescriptor
name|site
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://download.eclipse.org/releases/helios/"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|900
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testComposite
parameter_list|()
throws|throws
name|Exception
block|{
name|RepoDescriptor
name|site
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/test-p2/composite/"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that the url of the artifact is correctly resolved
name|String
name|path
init|=
operator|new
name|File
argument_list|(
literal|"test/test-p2/ivyde-repo/"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|site
operator|.
name|getModules
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|md
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
operator|.
name|getUrl
argument_list|()
operator|.
name|toExternalForm
argument_list|()
operator|.
name|startsWith
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

