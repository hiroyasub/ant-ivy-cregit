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
name|osgi
operator|.
name|obr
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_class
specifier|public
class|class
name|Capability
block|{
specifier|private
name|List
comment|/*<CapabilityProperty> */
name|properties
init|=
operator|new
name|ArrayList
comment|/*<CapabilityProperty> */
argument_list|()
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|public
name|Capability
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
name|addProperty
parameter_list|(
name|String
name|n
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|properties
operator|.
name|add
argument_list|(
operator|new
name|CapabilityProperty
argument_list|(
name|n
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
comment|/*<CapabilityProperty> */
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

