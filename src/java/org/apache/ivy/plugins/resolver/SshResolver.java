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
name|SshRepository
import|;
end_import

begin_comment
comment|/**  * Resolver for SSH resolver for ivy  */
end_comment

begin_class
specifier|public
class|class
name|SshResolver
extends|extends
name|AbstractSshBasedResolver
block|{
specifier|public
name|SshResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|SshRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * A four digit string (e.g., 0644, see "man chmod", "man open") specifying the permissions      * of the published files.      */
specifier|public
name|void
name|setPublishPermissions
parameter_list|(
name|String
name|permissions
parameter_list|)
block|{
operator|(
operator|(
name|SshRepository
operator|)
name|getRepository
argument_list|()
operator|)
operator|.
name|setPublishPermissions
argument_list|(
name|permissions
argument_list|)
expr_stmt|;
block|}
comment|/**      * sets the path separator used on the target system. Not sure if this is used or if '/' is used      * on all implementation. default is to use '/'      *       * @param sep      *            file separator to use on the target system      */
specifier|public
name|void
name|setFileSeparator
parameter_list|(
name|String
name|sep
parameter_list|)
block|{
if|if
condition|(
name|sep
operator|==
literal|null
operator|||
name|sep
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"File Separator has to be a single character and not "
operator|+
name|sep
argument_list|)
throw|;
block|}
operator|(
operator|(
name|SshRepository
operator|)
name|getRepository
argument_list|()
operator|)
operator|.
name|setFileSeparator
argument_list|(
name|sep
operator|.
name|trim
argument_list|()
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the command to get a directory listing the command has to be a shell command working on      * the target system and has to produce a listing of filenames, with each filename on a new line      * the term %arg can be used in the command to substitue the path to be listed       * (e.g. "ls -1 %arg | grep -v CVS" to get a listing without CVS directory) if %arg is not      * part of the command, the path will be appended to the command default is: "ls -1"      */
specifier|public
name|void
name|setListCommand
parameter_list|(
name|String
name|cmd
parameter_list|)
block|{
operator|(
operator|(
name|SshRepository
operator|)
name|getRepository
argument_list|()
operator|)
operator|.
name|setListCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the command to check for existence of a file the command has to be a shell command      * working on the target system and has to create an exit status of 0 for an existent file       * and<> 0 for a non existing file given as argument the term %arg can be used in the command      * to substitue the path to be listed if %arg is not part of the command, the path will be       * appended to the command default is: "ls"      */
specifier|public
name|void
name|setExistCommand
parameter_list|(
name|String
name|cmd
parameter_list|)
block|{
operator|(
operator|(
name|SshRepository
operator|)
name|getRepository
argument_list|()
operator|)
operator|.
name|setExistCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the command to create a directory on the target system the command has to be a shell      * command working on the target system and has to create a directory with the given argument      * the term %arg can be used in the command to substitue the path to be listed if %arg is not      * part of the command, the path will be appended to the command default is: "mkdir"      */
specifier|public
name|void
name|setCreateDirCommand
parameter_list|(
name|String
name|cmd
parameter_list|)
block|{
operator|(
operator|(
name|SshRepository
operator|)
name|getRepository
argument_list|()
operator|)
operator|.
name|setExistCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"ssh"
return|;
block|}
block|}
end_class

end_unit

