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
name|core
operator|.
name|cache
package|;
end_package

begin_class
specifier|public
class|class
name|CacheResourceOptions
extends|extends
name|CacheDownloadOptions
block|{
comment|// by default, a ttl of 1 hour
specifier|private
name|long
name|ttl
init|=
literal|1000
operator|*
literal|60
operator|*
literal|60
decl_stmt|;
specifier|public
name|void
name|setTtl
parameter_list|(
name|long
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
specifier|public
name|long
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
block|}
end_class

end_unit

