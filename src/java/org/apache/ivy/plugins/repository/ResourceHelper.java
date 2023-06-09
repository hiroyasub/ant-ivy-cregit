begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|net
operator|.
name|MalformedURLException
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
name|file
operator|.
name|FileResource
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
name|url
operator|.
name|URLResource
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
name|Message
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ResourceHelper
block|{
specifier|private
name|ResourceHelper
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|boolean
name|equals
parameter_list|(
name|Resource
name|res
parameter_list|,
name|File
name|f
parameter_list|)
block|{
if|if
condition|(
name|res
operator|==
literal|null
operator|&&
name|f
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|res
operator|==
literal|null
operator|||
name|f
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|res
operator|instanceof
name|FileResource
condition|)
block|{
return|return
operator|new
name|File
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|f
argument_list|)
return|;
block|}
if|else if
condition|(
name|res
operator|instanceof
name|URLResource
condition|)
block|{
try|try
block|{
return|return
name|f
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
operator|.
name|equals
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|long
name|getLastModifiedOrDefault
parameter_list|(
name|Resource
name|res
parameter_list|)
block|{
name|long
name|last
init|=
name|res
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|last
operator|>
literal|0
condition|)
block|{
return|return
name|last
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"impossible to get date for "
operator|+
name|res
operator|+
literal|": using 'now'"
argument_list|)
expr_stmt|;
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

