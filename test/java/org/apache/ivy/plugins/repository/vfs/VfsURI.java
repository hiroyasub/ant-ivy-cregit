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
name|plugins
operator|.
name|repository
operator|.
name|vfs
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
name|Ivy
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|isNullOrEmpty
import|;
end_import

begin_class
specifier|public
class|class
name|VfsURI
block|{
specifier|private
name|String
name|host
decl_stmt|;
specifier|private
name|String
name|passwd
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|String
name|scheme
decl_stmt|;
specifier|private
name|String
name|user
decl_stmt|;
comment|// VFS Schemes
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_CIFS
init|=
literal|"smb"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_FILE
init|=
literal|"file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_FTP
init|=
literal|"ftp"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_HTTP
init|=
literal|"http"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_HTTPS
init|=
literal|"https"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_SFTP
init|=
literal|"sftp"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|static
specifier|final
name|String
name|SCHEME_WEBDAV
init|=
literal|"webdav"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|SUPPORTED_SCHEMES
init|=
operator|new
name|String
index|[]
block|{
comment|// add other schemes here if other can be tested on your machine
name|SCHEME_FILE
block|}
decl_stmt|;
comment|/**      * Create a set of valid VFS URIs for the file access protocol      *      * @param scheme String      * @param resource      *            relative path (from the base repo) to the resource to be accessed      * @param ivy Ivy      * @return VfsURI      */
specifier|public
specifier|static
name|VfsURI
name|vfsURIFactory
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|resource
parameter_list|,
name|Ivy
name|ivy
parameter_list|)
block|{
name|VfsURI
name|vfsURI
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|scheme
condition|)
block|{
case|case
name|SCHEME_CIFS
case|:
name|vfsURI
operator|=
operator|new
name|VfsURI
argument_list|(
name|SCHEME_CIFS
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_ID
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_PASSWD
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_HOST
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_SAMBA_REPO
argument_list|)
operator|+
literal|"/"
operator|+
name|resource
argument_list|)
expr_stmt|;
break|break;
case|case
name|SCHEME_FILE
case|:
name|vfsURI
operator|=
operator|new
name|VfsURI
argument_list|(
name|SCHEME_FILE
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|VfsTestHelper
operator|.
name|CWD
operator|+
literal|"/"
operator|+
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
operator|+
literal|"/"
operator|+
name|resource
argument_list|)
expr_stmt|;
break|break;
case|case
name|SCHEME_FTP
case|:
name|vfsURI
operator|=
operator|new
name|VfsURI
argument_list|(
name|SCHEME_FTP
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_ID
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_PASSWD
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_HOST
argument_list|)
argument_list|,
name|VfsTestHelper
operator|.
name|CWD
operator|+
literal|"/"
operator|+
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
operator|+
literal|"/"
operator|+
name|resource
argument_list|)
expr_stmt|;
break|break;
case|case
name|SCHEME_SFTP
case|:
name|vfsURI
operator|=
operator|new
name|VfsURI
argument_list|(
name|SCHEME_SFTP
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_ID
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_USER_PASSWD
argument_list|)
argument_list|,
name|ivy
operator|.
name|getVariable
argument_list|(
name|VfsTestHelper
operator|.
name|PROP_VFS_HOST
argument_list|)
argument_list|,
name|VfsTestHelper
operator|.
name|CWD
operator|+
literal|"/"
operator|+
name|VfsTestHelper
operator|.
name|TEST_REPO_DIR
operator|+
literal|"/"
operator|+
name|resource
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|vfsURI
return|;
block|}
comment|/**      * Create a wellformed VFS resource identifier      *      * @param scheme      *            the name of the scheme used to access the resource      * @param user      *            a user name. May be<code>null</code>      * @param passwd      *            a passwd. May be<code>null</code>      * @param host      *            a host identifier. May be<code>null</code>      * @param path      *            a scheme specific path to a resource      */
specifier|public
name|VfsURI
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|passwd
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|user
operator|=
name|user
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|user
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|passwd
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|passwd
operator|=
name|passwd
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|passwd
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|host
operator|=
name|host
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|host
operator|=
literal|null
expr_stmt|;
block|}
name|this
operator|.
name|path
operator|=
name|normalizePath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return a well-formed VFS Resource identifier      *      * @return<code>String</code> representing a well formed VFS resource identifier      */
specifier|public
name|String
name|getVfsURI
parameter_list|()
block|{
name|StringBuilder
name|uri
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|uri
operator|.
name|append
argument_list|(
name|this
operator|.
name|scheme
argument_list|)
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
expr_stmt|;
comment|// not all resource identifiers include user/passwd specifiers
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|user
argument_list|)
condition|)
block|{
name|uri
operator|.
name|append
argument_list|(
name|this
operator|.
name|user
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|passwd
argument_list|)
condition|)
block|{
name|this
operator|.
name|passwd
operator|=
name|passwd
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|passwd
operator|=
literal|""
expr_stmt|;
block|}
name|uri
operator|.
name|append
argument_list|(
name|this
operator|.
name|passwd
argument_list|)
operator|.
name|append
argument_list|(
literal|"@"
argument_list|)
expr_stmt|;
block|}
comment|// not all resource identifiers include a host specifier
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|this
operator|.
name|host
operator|=
name|host
operator|.
name|trim
argument_list|()
expr_stmt|;
name|uri
operator|.
name|append
argument_list|(
name|this
operator|.
name|host
argument_list|)
expr_stmt|;
block|}
name|uri
operator|.
name|append
argument_list|(
name|this
operator|.
name|path
argument_list|)
expr_stmt|;
return|return
name|uri
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Convert a resource path to the format required for a VFS resource identifier      *      * @param path      *<code>String</code> path to the resource      * @return<code>String</code> representing a normalized resource path      */
specifier|private
name|String
name|normalizePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// all backslashes replaced with forward slashes
name|String
name|normalizedPath
init|=
name|path
operator|.
name|replaceAll
argument_list|(
literal|"\\\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
comment|// collapse multiple instance of forward slashes to single slashes
name|normalizedPath
operator|=
name|normalizedPath
operator|.
name|replaceAll
argument_list|(
literal|"//+"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
comment|// ensure that our path starts with a forward slash
if|if
condition|(
operator|!
name|normalizedPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|normalizedPath
operator|=
literal|"/"
operator|+
name|normalizedPath
expr_stmt|;
block|}
return|return
name|normalizedPath
operator|.
name|trim
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getVfsURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
block|}
end_class

end_unit

