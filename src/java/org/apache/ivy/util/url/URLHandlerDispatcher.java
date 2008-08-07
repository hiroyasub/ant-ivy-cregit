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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * This class is used to dispatch downloading requests  */
end_comment

begin_class
specifier|public
class|class
name|URLHandlerDispatcher
implements|implements
name|URLHandler
block|{
specifier|private
name|Map
name|handlers
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|URLHandler
name|defaultHandler
init|=
operator|new
name|BasicURLHandler
argument_list|()
decl_stmt|;
specifier|public
name|URLHandlerDispatcher
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|isReachable
argument_list|(
name|url
argument_list|)
return|;
block|}
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
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|isReachable
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getContentLength
argument_list|(
name|url
argument_list|)
return|;
block|}
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
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getContentLength
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getLastModified
argument_list|(
name|url
argument_list|)
return|;
block|}
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
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getLastModified
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|public
name|URLInfo
name|getURLInfo
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|)
return|;
block|}
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
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getHandler
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|(
name|url
argument_list|)
return|;
block|}
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
block|{
name|getHandler
argument_list|(
name|src
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|download
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
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
block|{
name|getHandler
argument_list|(
name|dest
operator|.
name|getProtocol
argument_list|()
argument_list|)
operator|.
name|upload
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDownloader
parameter_list|(
name|String
name|protocol
parameter_list|,
name|URLHandler
name|downloader
parameter_list|)
block|{
name|handlers
operator|.
name|put
argument_list|(
name|protocol
argument_list|,
name|downloader
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URLHandler
name|getHandler
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|URLHandler
name|downloader
init|=
operator|(
name|URLHandler
operator|)
name|handlers
operator|.
name|get
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
return|return
name|downloader
operator|==
literal|null
condition|?
name|defaultHandler
else|:
name|downloader
return|;
block|}
specifier|public
name|URLHandler
name|getDefault
parameter_list|()
block|{
return|return
name|defaultHandler
return|;
block|}
specifier|public
name|void
name|setDefault
parameter_list|(
name|URLHandler
name|default1
parameter_list|)
block|{
name|defaultHandler
operator|=
name|default1
expr_stmt|;
block|}
block|}
end_class

end_unit

