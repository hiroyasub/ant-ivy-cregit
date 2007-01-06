begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|url
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractURLHandler
implements|implements
name|URLHandler
block|{
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getURLInfo
argument_list|(
name|url
argument_list|)
operator|.
name|isReachable
argument_list|()
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
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
operator|.
name|isReachable
argument_list|()
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
name|getURLInfo
argument_list|(
name|url
argument_list|)
operator|.
name|getContentLength
argument_list|()
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
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
operator|.
name|getContentLength
argument_list|()
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
name|getURLInfo
argument_list|(
name|url
argument_list|)
operator|.
name|getLastModified
argument_list|()
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
name|getURLInfo
argument_list|(
name|url
argument_list|,
name|timeout
argument_list|)
operator|.
name|getLastModified
argument_list|()
return|;
block|}
block|}
end_class

end_unit

