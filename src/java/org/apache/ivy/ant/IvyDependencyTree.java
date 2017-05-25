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
name|ant
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|report
operator|.
name|ResolveReport
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
name|IvyNode
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
name|IvyNodeCallers
operator|.
name|Caller
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
name|IvyNodeEviction
operator|.
name|EvictionData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_class
specifier|public
class|class
name|IvyDependencyTree
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
name|Map
comment|/*<ModuleRevisionId, List<IvyNode>> */
name|dependencies
init|=
operator|new
name|HashMap
comment|/*                                                                                  *<ModuleRevisionId,                                                                                  * List<IvyNode>>                                                                                  */
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|showEvicted
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
name|ResolveReport
name|report
init|=
name|getResolvedReport
argument_list|()
decl_stmt|;
name|log
argument_list|(
literal|"Dependency tree for "
operator|+
name|report
operator|.
name|getResolveId
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
comment|// make dependency tree easier to fetch informations
for|for
control|(
name|Iterator
name|iterator
init|=
name|report
operator|.
name|getDependencies
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|IvyNode
name|dependency
init|=
operator|(
name|IvyNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|populateDependencyTree
argument_list|(
name|dependency
argument_list|,
name|mrid
argument_list|,
name|report
argument_list|)
expr_stmt|;
block|}
name|List
name|dependencyList
init|=
operator|(
name|List
operator|)
name|dependencies
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependencyList
operator|!=
literal|null
condition|)
block|{
name|printDependencies
argument_list|(
name|dependencyList
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printDependencies
parameter_list|(
name|List
comment|/*<IvyNode> */
name|dependencyList
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iterator
init|=
name|dependencyList
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|IvyNode
name|dependency
init|=
operator|(
name|IvyNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|evicted
init|=
name|dependency
operator|.
name|isEvicted
argument_list|(
name|getConf
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|evicted
operator|&&
operator|!
name|showEvicted
condition|)
block|{
continue|continue;
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|indent
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indent
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
name|indent
operator|-
literal|1
operator|&&
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|hasDependencies
argument_list|(
name|dependency
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"   "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"|  "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"+- "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\\- "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|dependency
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|evicted
operator|&&
name|showEvicted
condition|)
block|{
name|EvictionData
name|evictedData
init|=
name|dependency
operator|.
name|getEvictedData
argument_list|(
name|getConf
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|evictedData
operator|.
name|isTransitivelyEvicted
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" transitively"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" evicted by "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|evictedData
operator|.
name|getSelected
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" in "
argument_list|)
operator|.
name|append
argument_list|(
name|evictedData
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|evictedData
operator|.
name|getDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|evictedData
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|log
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|printDependencies
argument_list|(
operator|(
name|List
operator|)
name|dependencies
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|indent
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasDependencies
parameter_list|(
name|IvyNode
name|dependency
parameter_list|)
block|{
name|List
name|dependencyList
init|=
operator|(
name|List
operator|)
name|dependencies
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|dependencyList
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
specifier|private
name|void
name|populateDependencyTree
parameter_list|(
name|IvyNode
name|dependency
parameter_list|,
name|ModuleRevisionId
name|currentMrid
parameter_list|,
name|ResolveReport
name|report
parameter_list|)
block|{
name|registerNodeIfNecessary
argument_list|(
name|dependency
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dependency
operator|.
name|getAllCallers
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Caller
name|caller
init|=
name|dependency
operator|.
name|getAllCallers
argument_list|()
index|[
name|i
index|]
decl_stmt|;
name|addDependency
argument_list|(
name|caller
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|dependency
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|registerNodeIfNecessary
parameter_list|(
name|ModuleRevisionId
name|moduleRevisionId
parameter_list|)
block|{
if|if
condition|(
operator|!
name|dependencies
operator|.
name|containsKey
argument_list|(
name|moduleRevisionId
argument_list|)
condition|)
block|{
name|dependencies
operator|.
name|put
argument_list|(
name|moduleRevisionId
argument_list|,
operator|new
name|ArrayList
comment|/*<IvyNode> */
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addDependency
parameter_list|(
name|ModuleRevisionId
name|moduleRevisionId
parameter_list|,
name|IvyNode
name|dependency
parameter_list|)
block|{
name|registerNodeIfNecessary
argument_list|(
name|moduleRevisionId
argument_list|)
expr_stmt|;
name|List
comment|/*<IvyNode> */
name|list
init|=
operator|(
name|List
operator|)
name|dependencies
operator|.
name|get
argument_list|(
name|moduleRevisionId
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isShowEvicted
parameter_list|()
block|{
return|return
name|showEvicted
return|;
block|}
specifier|public
name|void
name|setShowEvicted
parameter_list|(
name|boolean
name|showEvicted
parameter_list|)
block|{
name|this
operator|.
name|showEvicted
operator|=
name|showEvicted
expr_stmt|;
block|}
block|}
end_class

end_unit

