begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|url
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
name|core
operator|.
name|settings
operator|.
name|TimeoutConstraint
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
name|CopyProgressListener
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * A enhanced version of {@link URLHandler} which respects {@link TimeoutConstraint}s on  * the operations dealing with download, upload, reachability checks etc...  */
end_comment

begin_interface
specifier|public
interface|interface
name|TimeoutConstrainedURLHandler
extends|extends
name|URLHandler
block|{
comment|/**      * Returns true if the passed<code>URL</code> is reachable. Else returns false. Uses the      * passed<code>timeoutConstraint</code> for determining the connectivity to the URL.      *<p>      * Please use {@link #getURLInfo(URL, TimeoutConstraint)} if more one information about the      *<code>url</code> is needed      *</p>      *      * @param url               The URL to access      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @return boolean      * @since 2.5      */
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
function_decl|;
comment|/**      * Returns the number of bytes of data that's available for the resource at the passed      *<code>url</code>. Returns 0 if the passed<code>url</code> isn't reachable      *      * @param url               The URL to access      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @return long      * @since 2.5      */
name|long
name|getContentLength
parameter_list|(
name|URL
name|url
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
function_decl|;
comment|/**      * Returns the last modified timestamp of the resource accessible at the passed      *<code>url</code>.      *<p>      * Please use {@link #getURLInfo(URL, TimeoutConstraint)} if more one information about the      *<code>url</code> is needed      *</p>      *      * @param url               The URL to access      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @return long      * @since 2.5      */
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
function_decl|;
comment|/**      * Returns the {@link URLInfo} extracted from the given url, or {@link #UNAVAILABLE} when the      * url is not reachable. Never returns null.      *      * @param url               The URL for which the information is to be retrieved      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @return URLInfo      * @since 2.5      */
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
function_decl|;
comment|/**      * Opens and returns an {@link InputStream} to the passed<code>url</code>.      *      * @param url               The URL to which an {@link InputStream} has to be opened      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @return InputStream      * @throws IOException if something goes wrong      * @since 2.5      */
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Downloads the resource available at<code>src</code> to the target<code>dest</code>      *      * @param src               The source URL to download the resource from      * @param dest              The destination {@link File} to download the resource to      * @param listener          The listener that will be notified of the download progress      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @throws IOException if something goes wrong      * @since 2.5      */
name|void
name|download
parameter_list|(
name|URL
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|listener
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Uploads the<code>src</code> {@link File} to the target<code>dest</code> {@link URL}      *      * @param src               The source {@link File} to upload      * @param dest              The target URL where the {@link File} has to be uploaded      * @param listener          The listener that will be notified of the upload progress      * @param timeoutConstraint The connectivity timeout constraints. Can be null, in which case      *                          the timeouts are implementation specific      * @throws IOException if something goes wrong      * @since 2.5      */
name|void
name|upload
parameter_list|(
name|File
name|src
parameter_list|,
name|URL
name|dest
parameter_list|,
name|CopyProgressListener
name|listener
parameter_list|,
name|TimeoutConstraint
name|timeoutConstraint
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

