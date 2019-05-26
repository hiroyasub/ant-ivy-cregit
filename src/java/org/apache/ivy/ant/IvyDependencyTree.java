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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
specifier|final
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|List
argument_list|<
name|IvyNode
argument_list|>
argument_list|>
name|dependencies
init|=
operator|new
name|HashMap
argument_list|<>
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
if|if
condition|(
name|report
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"No resolution report was available to run the post-resolve task. Make sure resolve was done before this task"
argument_list|)
throw|;
block|}
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
comment|// make dependency tree easier to fetch information
for|for
control|(
name|IvyNode
name|dependency
range|:
name|report
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|populateDependencyTree
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|final
name|List
argument_list|<
name|IvyNode
argument_list|>
name|dependencyList
init|=
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
name|mrid
argument_list|,
name|dependencyList
argument_list|,
literal|0
argument_list|,
operator|new
name|HashSet
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|printDependencies
parameter_list|(
specifier|final
name|ModuleRevisionId
name|mrid
parameter_list|,
specifier|final
name|List
argument_list|<
name|IvyNode
argument_list|>
name|dependencyList
parameter_list|,
specifier|final
name|int
name|indent
parameter_list|,
specifier|final
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|ancestors
parameter_list|)
block|{
for|for
control|(
name|IvyNode
name|dependency
range|:
name|dependencyList
control|)
block|{
specifier|final
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|ancestorsForCurrentDep
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|ancestors
argument_list|)
decl_stmt|;
comment|// previous ancestors plus the module to whom these dependencies belong to
name|ancestorsForCurrentDep
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
specifier|final
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
specifier|final
name|boolean
name|isLastDependency
init|=
name|dependencyList
operator|.
name|indexOf
argument_list|(
name|dependency
argument_list|)
operator|==
name|dependencyList
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|ModuleRevisionId
name|dependencyMrid
init|=
name|dependency
operator|.
name|getId
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|circular
init|=
name|ancestorsForCurrentDep
operator|.
name|contains
argument_list|(
name|dependencyMrid
argument_list|)
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
name|isLastDependency
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
name|sb
operator|.
name|append
argument_list|(
name|isLastDependency
condition|?
literal|"\\- "
else|:
literal|"+- "
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|evicted
operator|&&
name|circular
condition|)
block|{
comment|// log and skip processing the (transitive) dependencies of this dependency
name|sb
operator|.
name|append
argument_list|(
literal|"(circularly depends on) "
argument_list|)
operator|.
name|append
argument_list|(
name|dependencyMrid
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|dependencyMrid
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|dependencyMrid
argument_list|,
name|dependencies
operator|.
name|get
argument_list|(
name|dependencyMrid
argument_list|)
argument_list|,
name|indent
operator|+
literal|1
argument_list|,
name|ancestorsForCurrentDep
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasDependencies
parameter_list|(
specifier|final
name|IvyNode
name|module
parameter_list|)
block|{
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|List
argument_list|<
name|IvyNode
argument_list|>
name|dependenciesForModule
init|=
name|dependencies
operator|.
name|get
argument_list|(
name|module
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|dependenciesForModule
operator|!=
literal|null
operator|&&
operator|!
name|dependenciesForModule
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|private
name|void
name|populateDependencyTree
parameter_list|(
name|IvyNode
name|dependency
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
name|Caller
name|caller
range|:
name|dependency
operator|.
name|getAllCallers
argument_list|()
control|)
block|{
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
specifier|final
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
argument_list|<
name|IvyNode
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addDependency
parameter_list|(
specifier|final
name|ModuleRevisionId
name|moduleRevisionId
parameter_list|,
specifier|final
name|IvyNode
name|dependency
parameter_list|)
block|{
name|registerNodeIfNecessary
argument_list|(
name|moduleRevisionId
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|get
argument_list|(
name|moduleRevisionId
argument_list|)
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

