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
name|plugins
operator|.
name|repository
operator|.
name|vfs
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs2
operator|.
name|FileSystemException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|vfs2
operator|.
name|impl
operator|.
name|StandardFileSystemManager
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|VfsTestHelper
block|{
specifier|private
name|Ivy
name|ivy
init|=
literal|null
decl_stmt|;
specifier|public
specifier|final
name|StandardFileSystemManager
name|fsManager
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|VFS_CONF
init|=
literal|"ivy_vfs.xml"
decl_stmt|;
comment|// Ivy Variables
specifier|public
specifier|static
specifier|final
name|String
name|PROP_VFS_HOST
init|=
literal|"vfs.host"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROP_VFS_SAMBA_REPO
init|=
literal|"vfs.samba.share"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROP_VFS_USER_ID
init|=
literal|"vfs.user"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROP_VFS_USER_PASSWD
init|=
literal|"vfs.passwd"
decl_stmt|;
comment|// Resources
specifier|public
specifier|static
specifier|final
name|String
name|CWD
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_DIR
init|=
literal|"test/repositories"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IVY_CONFIG_FILE
init|=
name|FileUtil
operator|.
name|concat
argument_list|(
name|TEST_REPO_DIR
argument_list|,
literal|"ivysettings.xml"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_IVY_XML
init|=
literal|"2/mod5.1/ivy-4.2.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCRATCH_DIR
init|=
literal|"_vfsScratchArea"
decl_stmt|;
specifier|public
name|VfsTestHelper
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup and initialize VFS
name|fsManager
operator|=
operator|new
name|StandardFileSystemManager
argument_list|()
block|{
specifier|protected
name|void
name|configurePlugins
parameter_list|()
throws|throws
name|FileSystemException
block|{
comment|// disable automatic loading of potentially unsupported extensions
block|}
block|}
expr_stmt|;
name|fsManager
operator|.
name|setConfiguration
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|VFS_CONF
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|fsManager
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// setup and initialize ivy
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
name|IVY_CONFIG_FILE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generate a set of well-formed VFS resource identifiers      *      * @param resource      *            name of the resource      * @return {@link List} of well-formed VFS resource identifiers      */
specifier|public
name|List
argument_list|<
name|VfsURI
argument_list|>
name|createVFSUriSet
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|VfsURI
argument_list|>
name|set
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|scheme
range|:
name|VfsURI
operator|.
name|SUPPORTED_SCHEMES
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|VfsURI
operator|.
name|vfsURIFactory
argument_list|(
name|scheme
argument_list|,
name|resource
argument_list|,
name|ivy
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|set
return|;
block|}
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|ivy
return|;
block|}
block|}
end_class

end_unit

