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
name|core
operator|.
name|event
operator|.
name|resolve
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
name|core
operator|.
name|event
operator|.
name|IvyEvent
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|ModuleDescriptor
import|;
end_import

begin_class
specifier|public
class|class
name|ResolveEvent
extends|extends
name|IvyEvent
block|{
specifier|private
name|ModuleDescriptor
name|md
decl_stmt|;
specifier|protected
name|ResolveEvent
parameter_list|(
name|String
name|name
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|md
operator|=
name|md
expr_stmt|;
name|addMDAttributes
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|addConfsAttribute
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|md
return|;
block|}
block|}
end_class

end_unit

