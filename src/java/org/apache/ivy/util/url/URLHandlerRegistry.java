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
name|Message
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|URLHandlerRegistry
block|{
specifier|private
name|URLHandlerRegistry
parameter_list|()
block|{
block|}
specifier|private
specifier|static
name|URLHandler
name|defaultHandler
init|=
operator|new
name|BasicURLHandler
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|URLHandler
name|getDefault
parameter_list|()
block|{
return|return
name|defaultHandler
return|;
block|}
specifier|public
specifier|static
name|void
name|setDefault
parameter_list|(
name|URLHandler
name|def
parameter_list|)
block|{
name|defaultHandler
operator|=
name|def
expr_stmt|;
block|}
comment|/**      * This method is used to get appropriate http downloader dependening on Jakarta Commons      * HttpClient availability in classpath, or simply use jdk url handling in other cases.      *       * @return most accurate http downloader      */
specifier|public
specifier|static
name|URLHandler
name|getHttp
parameter_list|()
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.commons.httpclient.HttpClient"
argument_list|)
expr_stmt|;
name|Class
name|handler
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.ivy.util.url.HttpClientHandler"
argument_list|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"jakarta commons httpclient detected: using it for http downloading"
argument_list|)
expr_stmt|;
return|return
operator|(
name|URLHandler
operator|)
name|handler
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"jakarta commons httpclient not found: using jdk url handling"
argument_list|)
expr_stmt|;
return|return
operator|new
name|BasicURLHandler
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"error occurred while loading jakarta commons httpclient: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"Using jdk url handling instead."
argument_list|)
expr_stmt|;
return|return
operator|new
name|BasicURLHandler
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"couldn't instantiate HttpClientHandler: using jdk url handling"
argument_list|)
expr_stmt|;
return|return
operator|new
name|BasicURLHandler
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"couldn't instantiate HttpClientHandler: using jdk url handling"
argument_list|)
expr_stmt|;
return|return
operator|new
name|BasicURLHandler
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

