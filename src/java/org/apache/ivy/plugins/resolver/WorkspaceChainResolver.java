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
name|settings
operator|.
name|IvySettings
import|;
end_import

begin_comment
comment|/**  * Resolver which decorate normal resolver so that the workspace resolver can hijack the resolve  * process<br />  * NB : it is for internal usage of Ivy only!  */
end_comment

begin_class
specifier|public
class|class
name|WorkspaceChainResolver
extends|extends
name|ChainResolver
block|{
specifier|public
name|WorkspaceChainResolver
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|DependencyResolver
name|delegate
parameter_list|,
name|AbstractWorkspaceResolver
name|workspaceResolver
parameter_list|)
block|{
name|setName
argument_list|(
literal|"workspace-chain-"
operator|+
name|delegate
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|setReturnFirst
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|workspaceResolver
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

