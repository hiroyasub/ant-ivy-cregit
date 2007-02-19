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
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
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
name|vfs
operator|.
name|FileName
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
name|vfs
operator|.
name|FileSystem
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
name|vfs
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
name|vfs
operator|.
name|FileSystemOptions
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
name|vfs
operator|.
name|provider
operator|.
name|GenericFileName
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
name|vfs
operator|.
name|provider
operator|.
name|webdav
operator|.
name|WebdavFileProvider
import|;
end_import

begin_comment
comment|/**  * Modified version of the WebdavFileProvider from VFS which adds support for httpclient 3.x.  * See http://issues.apache.org/jira/browse/VFS-74 for more info.  *   * A provider for WebDAV.  *  * @author<a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>  * @author Maarten Coene  * @version $Revision: 417178 $ $Date: 2006-06-26 05:31:41 -0700 (Mon, 26 Jun 2006) $  */
end_comment

begin_class
specifier|public
class|class
name|IvyWebdavFileProvider
extends|extends
name|WebdavFileProvider
block|{
comment|/*********************************************************************************************** 	 * Creates a filesystem. 	 */
specifier|protected
name|FileSystem
name|doCreateFileSystem
parameter_list|(
specifier|final
name|FileName
name|name
parameter_list|,
specifier|final
name|FileSystemOptions
name|fileSystemOptions
parameter_list|)
throws|throws
name|FileSystemException
block|{
comment|// Create the file system
specifier|final
name|GenericFileName
name|rootName
init|=
operator|(
name|GenericFileName
operator|)
name|name
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|IvyWebdavClientFactory
operator|.
name|createConnection
argument_list|(
name|rootName
operator|.
name|getHostName
argument_list|()
argument_list|,
name|rootName
operator|.
name|getPort
argument_list|()
argument_list|,
name|rootName
operator|.
name|getUserName
argument_list|()
argument_list|,
name|rootName
operator|.
name|getPassword
argument_list|()
argument_list|,
name|fileSystemOptions
argument_list|)
decl_stmt|;
return|return
operator|new
name|IvyWebdavFileSystem
argument_list|(
name|rootName
argument_list|,
name|httpClient
argument_list|,
name|fileSystemOptions
argument_list|)
return|;
block|}
block|}
end_class

end_unit
