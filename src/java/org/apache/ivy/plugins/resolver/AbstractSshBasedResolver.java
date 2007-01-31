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
name|resolver
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
name|plugins
operator|.
name|repository
operator|.
name|ssh
operator|.
name|AbstractSshBasedRepository
import|;
end_import

begin_comment
comment|/**  * Abstract base class for all resolvers using SSH   *   * All necessary connection parameters can be set here via attributes.  * However all attributes defined in the pattern url of the resolver will have higher   * priority and will overwrite the values given here. To specify connection parameters  * in the pattern, you have to specify a full url and not just a path as pattern.  * e.g. pattern="/path/to/my/repos/[artifact].[ext]" will use all connection parameters   * from this class  * e.g. pattern="ssh://myserver.com/path/to/my/repos/[artifact].[ext]" will use all parameters   * from this class with the exception of the host, which will be "myserver.com"  * e.g. pattern="sftp://user:geheim@myserver.com:8022/path/to/my/repos/[artifact].[ext]" will  * use only the keyFile and keyFilePassword from this class (if needed). Rest will come from the url.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSshBasedResolver
extends|extends
name|RepositoryResolver
block|{
specifier|private
name|boolean
name|passfileSet
init|=
literal|false
decl_stmt|;
specifier|public
name|AbstractSshBasedResolver
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|private
name|AbstractSshBasedRepository
name|getSshBasedRepository
parameter_list|()
block|{
return|return
operator|(
operator|(
name|AbstractSshBasedRepository
operator|)
name|getRepository
argument_list|()
operator|)
return|;
block|}
comment|/**      * Sets the location of the Public Key file to use for authentication      * @param filePath full file path name      */
specifier|public
name|void
name|setKeyFile
parameter_list|(
name|String
name|filePath
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setKeyFile
argument_list|(
operator|new
name|File
argument_list|(
name|filePath
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**       * Optional password file. If set the repository will use it as an encypted property file, to load      * username and passwd entries, and to store them if the user choose to do so.      *       * Defaults to user.dir/.ivy/[host].sftp.passwd, set it to null to disable this feature.       */
specifier|public
name|void
name|setPassfile
parameter_list|(
name|String
name|passfile
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setPassFile
argument_list|(
name|passfile
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|File
argument_list|(
name|passfile
argument_list|)
argument_list|)
expr_stmt|;
name|passfileSet
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|setSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|super
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|passfileSet
condition|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setPassFile
argument_list|(
operator|new
name|File
argument_list|(
name|settings
operator|.
name|getDefaultIvyUserDir
argument_list|()
argument_list|,
name|getSshBasedRepository
argument_list|()
operator|.
name|getHost
argument_list|()
operator|+
literal|".ssh.passwd"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the password to authenticate the user if password based login is used      * if no password is set and password based login is used, user will be prompted for it      * the password can also be set by using a full url for the pattern, like      * "sftp://user:password@myserver.com/path/to/repos/[artifact].[ext]"      * @param password to use      */
specifier|public
name|void
name|setUserPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setUserPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the password to use for decrypting key file (if it is encrypted)      * if no password is set and the keyfile is encrypted, the user will be prompted for the password      * if the keyfile is passwordless, this parameter will be ignored if given      * @param password to use      */
specifier|public
name|void
name|setKeyFilePassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setKeyFilePassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * sets the user to use for the ssh communication      * the user can also be set by using a full url for the pattern, like      * "ssh://user@myserver.com/path/to/repos/[artifact].[ext]"      * @param user on the target system      */
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
comment|/**      * sets the host to use for the ssh communication      * the host can also be set by using a full url for the pattern, like      * "ssh://myserver.com/path/to/repos/[artifact].[ext]"      * @param host of the target system      */
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
comment|/**      * sets the port to use for the ssh communication      * port 22 is default      * the port can also be set by using a full url for the pattern, like      * "sftp://myserver.com:8022/path/to/repos/[artifact].[ext]"      * @param port of the target system      */
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|getSshBasedRepository
argument_list|()
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
specifier|abstract
specifier|public
name|String
name|getTypeName
parameter_list|()
function_decl|;
block|}
end_class

end_unit

