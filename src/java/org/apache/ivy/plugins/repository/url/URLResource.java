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
name|url
package|;
end_package

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
name|MalformedURLException
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
name|plugins
operator|.
name|repository
operator|.
name|Resource
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
name|url
operator|.
name|URLHandlerRegistry
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
name|url
operator|.
name|URLHandler
operator|.
name|URLInfo
import|;
end_import

begin_class
specifier|public
class|class
name|URLResource
implements|implements
name|Resource
block|{
specifier|private
name|URL
name|url
decl_stmt|;
specifier|private
name|boolean
name|init
init|=
literal|false
decl_stmt|;
specifier|private
name|long
name|lastModified
decl_stmt|;
specifier|private
name|long
name|contentLength
decl_stmt|;
specifier|private
name|boolean
name|exists
decl_stmt|;
specifier|public
name|URLResource
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|url
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
specifier|public
name|Resource
name|clone
parameter_list|(
name|String
name|cloneName
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URLResource
argument_list|(
operator|new
name|URL
argument_list|(
name|cloneName
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"bad clone name provided: not suitable for an URLResource: "
operator|+
name|cloneName
argument_list|)
throw|;
block|}
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
if|if
condition|(
operator|!
name|init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|lastModified
return|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|URLInfo
name|info
init|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|contentLength
operator|=
name|info
operator|.
name|getContentLength
argument_list|()
expr_stmt|;
name|lastModified
operator|=
name|info
operator|.
name|getLastModified
argument_list|()
expr_stmt|;
name|exists
operator|=
name|info
operator|.
name|isReachable
argument_list|()
expr_stmt|;
name|init
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
if|if
condition|(
operator|!
name|init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|contentLength
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
if|if
condition|(
operator|!
name|init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|exists
return|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
block|}
end_class

end_unit

