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
name|module
operator|.
name|status
package|;
end_package

begin_class
specifier|public
class|class
name|Status
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|boolean
name|integration
decl_stmt|;
specifier|public
name|Status
parameter_list|()
block|{
block|}
specifier|public
name|Status
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|integration
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|integration
operator|=
name|integration
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIntegration
parameter_list|()
block|{
return|return
name|integration
return|;
block|}
specifier|public
name|void
name|setIntegration
parameter_list|(
name|boolean
name|integration
parameter_list|)
block|{
name|this
operator|.
name|integration
operator|=
name|integration
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

