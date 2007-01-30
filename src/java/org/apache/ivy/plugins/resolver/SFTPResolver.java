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
name|sftp
operator|.
name|SFTPRepository
import|;
end_import

begin_comment
comment|/**  * This resolver is able to work with any sftp server.  *   * It supports listing and publishing.  *   * The server host should absolutely be set using setHost.  *   * basedir defaults to .  * port default to 22  *   * username and password will be prompted using a dialog box if not set. So if you are in  * an headless environment, provide username and password.  */
end_comment

begin_class
specifier|public
class|class
name|SFTPResolver
extends|extends
name|AbstractSshBasedResolver
block|{
specifier|public
name|SFTPResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|SFTPRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"sftp"
return|;
block|}
specifier|public
name|SFTPRepository
name|getSFTPRepository
parameter_list|()
block|{
return|return
operator|(
name|SFTPRepository
operator|)
name|getRepository
argument_list|()
return|;
block|}
block|}
end_class

end_unit

