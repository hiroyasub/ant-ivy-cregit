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

begin_comment
comment|/**  * A {@link ResourceDownloader} is able to download a Resource to a File.  *<p>  * Depending on the implementation, the downloader may also choose to download checksums  * automatically and check the consistency of the downloaded resource.  *</p>  *<p>  * The implementation is also responsible for using a .part file during download, to ensure the  * destination file will exist only if the download is completed successfully.  *</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceDownloader
block|{
specifier|public
name|void
name|download
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Resource
name|resource
parameter_list|,
name|File
name|dest
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

