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
comment|/**  * This interface is responsible for handling some URL manipulation (stream opening, downloading,  * check reachability, ...).  *<p>  *  * @deprecated Starting 2.5.0, the {@link TimeoutConstrainedURLHandler} is preferred in favour of this interface  */
end_comment

begin_interface
annotation|@
name|Deprecated
specifier|public
interface|interface
name|URLHandler
block|{
comment|/**      * Using the slower REQUEST method for getting the basic URL infos. Use this when getting      * errors behind a problematic/special proxy or firewall chain.      */
name|int
name|REQUEST_METHOD_GET
init|=
literal|1
decl_stmt|;
comment|/**      * Using the faster HEAD method for getting the basic URL infos. Works for most common      * networks.      */
name|int
name|REQUEST_METHOD_HEAD
init|=
literal|2
decl_stmt|;
class|class
name|URLInfo
block|{
specifier|private
name|long
name|contentLength
decl_stmt|;
specifier|private
name|long
name|lastModified
decl_stmt|;
specifier|private
name|boolean
name|available
decl_stmt|;
specifier|private
name|String
name|bodyCharset
decl_stmt|;
specifier|protected
name|URLInfo
parameter_list|(
name|boolean
name|available
parameter_list|,
name|long
name|contentLength
parameter_list|,
name|long
name|lastModified
parameter_list|)
block|{
name|this
argument_list|(
name|available
argument_list|,
name|contentLength
argument_list|,
name|lastModified
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|URLInfo
parameter_list|(
name|boolean
name|available
parameter_list|,
name|long
name|contentLength
parameter_list|,
name|long
name|lastModified
parameter_list|,
name|String
name|bodyCharset
parameter_list|)
block|{
name|this
operator|.
name|available
operator|=
name|available
expr_stmt|;
name|this
operator|.
name|contentLength
operator|=
name|contentLength
expr_stmt|;
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
name|this
operator|.
name|bodyCharset
operator|=
name|bodyCharset
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReachable
parameter_list|()
block|{
return|return
name|available
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|contentLength
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
specifier|public
name|String
name|getBodyCharset
parameter_list|()
block|{
return|return
name|bodyCharset
return|;
block|}
block|}
name|URLInfo
name|UNAVAILABLE
init|=
operator|new
name|URLInfo
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      *      * @param url the url to check      * @return true if the target is reachable      */
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      *      * @param url     the url to check      * @param timeout the timeout in milliseconds      * @return true if the target is reachable      */
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      *      * @param url the url to check      * @return the length of the target if the given url is reachable, 0 otherwise. No error code      * in case of http urls.      */
name|long
name|getContentLength
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * @param url     the url to check      * @param timeout the maximum time before considering an url is not reachable a      *                timeout of zero indicates no timeout      * @return the length of the target if the given url is reachable, 0 otherwise. No error code      * in case of http urls.      */
name|long
name|getContentLength
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      *      * @param url the url to check      * @return last modified timestamp of the given url      */
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      *      * @param url     the url to check      * @param timeout the timeout in milliseconds      * @return last modified timestamp of the given url      */
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
function_decl|;
comment|/**      * @param url The url from which information is retrieved.      * @return The URLInfo extracted from the given url, or {@link #UNAVAILABLE} instance when the      * url is not reachable.      */
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * @param url     The url from which information is retrieved.      * @param timeout The timeout in milliseconds.      * @return The URLInfo extracted from the given url, or {@link #UNAVAILABLE} when the url is      * not reachable, never null.      */
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
function_decl|;
comment|/**      * @param url ditto      * @return InputStream      * @throws IOException if something goes wrong      */
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * @param src  URL      * @param dest File      * @param l    CopyProgressListener      * @throws IOException if something goes wrong      */
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
name|l
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * @param src  File      * @param dest URL      * @param l    CopyProgressListener      * @throws IOException if something goes wrong      */
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
name|l
parameter_list|)
throws|throws
name|IOException
function_decl|;
name|void
name|setRequestMethod
parameter_list|(
name|int
name|requestMethod
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

