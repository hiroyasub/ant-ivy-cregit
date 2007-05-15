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

begin_comment
comment|/**  * This interface is responsible for handling some URL manipulation  * (stream opening, downloading, check reachability, ...).   *   *  */
end_comment

begin_interface
specifier|public
interface|interface
name|URLHandler
block|{
specifier|public
specifier|static
class|class
name|URLInfo
block|{
specifier|private
name|long
name|_contentLength
decl_stmt|;
specifier|private
name|long
name|_lastModified
decl_stmt|;
specifier|private
name|boolean
name|_available
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
name|_available
operator|=
name|available
expr_stmt|;
name|_contentLength
operator|=
name|contentLength
expr_stmt|;
name|_lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReachable
parameter_list|()
block|{
return|return
name|_available
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|_contentLength
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|_lastModified
return|;
block|}
block|}
specifier|public
specifier|static
specifier|final
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
comment|/**      * Please prefer getURLInfo when several infos are needed.      * @param url the url to check      * @return true if the target is reachable      */
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      * @param url the url to check      * @return true if the target is reachable      */
specifier|public
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
comment|/**      * Returns the length of the target if the given url is reachable, and without      * error code in case of http urls.      * Please prefer getURLInfo when several infos are needed.      * @param url the url to check      * @return the length of the target if available, 0 if not reachable      */
specifier|public
name|long
name|getContentLength
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Returns the length of the target if the given url is reachable, and without      * error code in case of http urls.      * @param url the url to check      * @param timeout the maximum time before considering an url is not reachable      *        a timeout of zero indicates no timeout      * @return the length of the target if available, 0 if not reachable      */
specifier|public
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
comment|/**      * Please prefer getURLInfo when several infos are needed.      * @param url the url to check      * @return last modified timestamp of the given url      */
specifier|public
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Please prefer getURLInfo when several infos are needed.      * @param url the url to check      * @return last modified timestamp of the given url      */
specifier|public
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
comment|/**      * never returns null, return UNAVAILABLE when url is not reachable      * @param url      * @return      */
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * never returns null, return UNAVAILABLE when url is not reachable      * @param url      * @return      */
specifier|public
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
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|public
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
block|}
end_interface

end_unit

