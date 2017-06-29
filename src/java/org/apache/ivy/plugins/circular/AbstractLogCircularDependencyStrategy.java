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
name|circular
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|IvyContext
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
name|id
operator|.
name|ModuleRevisionId
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
name|resolve
operator|.
name|ResolveData
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLogCircularDependencyStrategy
extends|extends
name|AbstractCircularDependencyStrategy
block|{
specifier|protected
name|AbstractLogCircularDependencyStrategy
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|circularDependencies
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|handleCircularDependency
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
name|String
name|circularDependencyId
init|=
name|getCircularDependencyId
argument_list|(
name|mrids
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|circularDependencies
operator|.
name|contains
argument_list|(
name|circularDependencyId
argument_list|)
condition|)
block|{
name|circularDependencies
operator|.
name|add
argument_list|(
name|circularDependencyId
argument_list|)
expr_stmt|;
name|logCircularDependency
argument_list|(
name|mrids
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|logCircularDependency
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
function_decl|;
specifier|protected
name|String
name|getCircularDependencyId
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
name|String
name|contextPrefix
init|=
literal|""
decl_stmt|;
name|ResolveData
name|data
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getResolveData
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|contextPrefix
operator|=
name|data
operator|.
name|getOptions
argument_list|()
operator|.
name|getResolveId
argument_list|()
operator|+
literal|" "
expr_stmt|;
block|}
return|return
name|contextPrefix
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|mrids
argument_list|)
return|;
block|}
block|}
end_class

end_unit

