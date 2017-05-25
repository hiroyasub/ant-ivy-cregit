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
operator|.
name|util
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
name|List
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
name|ApacheURLLister
import|;
end_import

begin_class
specifier|public
class|class
name|ApacheHttpURLLister
implements|implements
name|URLLister
block|{
specifier|private
name|ApacheURLLister
name|lister
init|=
operator|new
name|ApacheURLLister
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|accept
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|listAll
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|lister
operator|.
name|listAll
argument_list|(
name|url
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"apache http lister"
return|;
block|}
block|}
end_class

end_unit

