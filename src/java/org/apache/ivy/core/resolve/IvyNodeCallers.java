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
name|resolve
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Stack
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
name|Artifact
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
name|DependencyDescriptor
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
name|ModuleId
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

begin_class
specifier|public
class|class
name|IvyNodeCallers
block|{
specifier|public
specifier|static
class|class
name|Caller
block|{
specifier|private
name|ModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|_mrid
decl_stmt|;
specifier|private
name|Map
name|_confs
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (String callerConf -> String[] dependencyConfs)
specifier|private
name|DependencyDescriptor
name|_dd
decl_stmt|;
specifier|private
name|boolean
name|_callerCanExclude
decl_stmt|;
specifier|public
name|Caller
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|DependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|callerCanExclude
parameter_list|)
block|{
name|_md
operator|=
name|md
expr_stmt|;
name|_mrid
operator|=
name|mrid
expr_stmt|;
name|_dd
operator|=
name|dd
expr_stmt|;
name|_callerCanExclude
operator|=
name|callerCanExclude
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|String
name|callerConf
parameter_list|,
name|String
index|[]
name|dependencyConfs
parameter_list|)
block|{
name|String
index|[]
name|prevDepConfs
init|=
operator|(
name|String
index|[]
operator|)
name|_confs
operator|.
name|get
argument_list|(
name|callerConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|prevDepConfs
operator|!=
literal|null
condition|)
block|{
name|Set
name|newDepConfs
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|prevDepConfs
argument_list|)
argument_list|)
decl_stmt|;
name|newDepConfs
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dependencyConfs
argument_list|)
argument_list|)
expr_stmt|;
name|_confs
operator|.
name|put
argument_list|(
name|callerConf
argument_list|,
operator|(
name|String
index|[]
operator|)
name|newDepConfs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|newDepConfs
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|_confs
operator|.
name|put
argument_list|(
name|callerConf
argument_list|,
name|dependencyConfs
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
index|[]
name|getCallerConfigurations
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|_confs
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_confs
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|_mrid
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Caller
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Caller
name|other
init|=
operator|(
name|Caller
operator|)
name|obj
decl_stmt|;
return|return
name|other
operator|.
name|_confs
operator|.
name|equals
argument_list|(
name|_confs
argument_list|)
operator|&&
name|_mrid
operator|.
name|equals
argument_list|(
name|other
operator|.
name|_mrid
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
literal|31
decl_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|_confs
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|_mrid
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|hash
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|_mrid
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getAskedDependencyId
parameter_list|()
block|{
return|return
name|_dd
operator|.
name|getDependencyRevisionId
argument_list|()
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|_md
return|;
block|}
specifier|public
name|boolean
name|canExclude
parameter_list|()
block|{
return|return
name|_callerCanExclude
operator|||
name|_md
operator|.
name|canExclude
argument_list|()
operator|||
name|_dd
operator|.
name|canExclude
argument_list|()
return|;
block|}
specifier|public
name|DependencyDescriptor
name|getDependencyDescriptor
parameter_list|()
block|{
return|return
name|_dd
return|;
block|}
block|}
comment|// Map (String rootModuleConf -> Map (ModuleRevisionId -> Caller)): key in second map is used to easily get a caller by its mrid
specifier|private
name|Map
name|_callersByRootConf
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// this map contains all the module ids calling this one (including transitively) as keys
comment|// the mapped nodes (values) correspond to a direct caller from which the transitive caller comes
specifier|private
name|Map
name|_allCallers
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleId -> IvyNode)
specifier|private
name|IvyNode
name|_node
decl_stmt|;
specifier|public
name|IvyNodeCallers
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|_node
operator|=
name|node
expr_stmt|;
block|}
comment|/**      *       * @param rootModuleConf      * @param mrid      * @param callerConf      * @param dependencyConfs '*' must have been resolved      * @param dd the dependency revision id asked by the caller      */
specifier|public
name|void
name|addCaller
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|IvyNode
name|callerNode
parameter_list|,
name|String
name|callerConf
parameter_list|,
name|String
index|[]
name|dependencyConfs
parameter_list|,
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
name|ModuleDescriptor
name|md
init|=
name|callerNode
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|callerNode
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|_node
operator|.
name|getId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"a module is not authorized to depend on itself: "
operator|+
name|_node
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
name|Map
name|callers
init|=
operator|(
name|Map
operator|)
name|_callersByRootConf
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|callers
operator|==
literal|null
condition|)
block|{
name|callers
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|_callersByRootConf
operator|.
name|put
argument_list|(
name|rootModuleConf
argument_list|,
name|callers
argument_list|)
expr_stmt|;
block|}
name|Caller
name|caller
init|=
operator|(
name|Caller
operator|)
name|callers
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|caller
operator|==
literal|null
condition|)
block|{
name|caller
operator|=
operator|new
name|Caller
argument_list|(
name|md
argument_list|,
name|mrid
argument_list|,
name|dd
argument_list|,
name|callerNode
operator|.
name|canExclude
argument_list|(
name|rootModuleConf
argument_list|)
argument_list|)
expr_stmt|;
name|callers
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|caller
argument_list|)
expr_stmt|;
block|}
name|caller
operator|.
name|addConfiguration
argument_list|(
name|callerConf
argument_list|,
name|dependencyConfs
argument_list|)
expr_stmt|;
name|IvyNode
name|parent
init|=
name|callerNode
operator|.
name|getRealNode
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|parent
operator|.
name|getAllCallersModuleIds
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleId
name|mid
init|=
operator|(
name|ModuleId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|_allCallers
operator|.
name|put
argument_list|(
name|mid
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
name|_allCallers
operator|.
name|put
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|callerNode
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Caller
index|[]
name|getCallers
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Map
name|callers
init|=
operator|(
name|Map
operator|)
name|_callersByRootConf
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|callers
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Caller
index|[
literal|0
index|]
return|;
block|}
return|return
operator|(
name|Caller
index|[]
operator|)
name|callers
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Caller
index|[
name|callers
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|Caller
index|[]
name|getAllCallers
parameter_list|()
block|{
name|Set
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_callersByRootConf
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
name|callers
init|=
operator|(
name|Map
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|callers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Caller
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|Caller
index|[
name|all
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|getAllCallersModuleIds
parameter_list|()
block|{
return|return
name|_allCallers
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|void
name|updateFrom
parameter_list|(
name|IvyNodeCallers
name|callers
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Map
name|nodecallers
init|=
operator|(
name|Map
operator|)
name|callers
operator|.
name|_callersByRootConf
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodecallers
operator|!=
literal|null
condition|)
block|{
name|Map
name|thiscallers
init|=
operator|(
name|Map
operator|)
name|_callersByRootConf
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|thiscallers
operator|==
literal|null
condition|)
block|{
name|thiscallers
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|_callersByRootConf
operator|.
name|put
argument_list|(
name|rootModuleConf
argument_list|,
name|thiscallers
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|nodecallers
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Caller
name|caller
init|=
operator|(
name|Caller
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|thiscallers
operator|.
name|containsKey
argument_list|(
name|caller
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
condition|)
block|{
name|thiscallers
operator|.
name|put
argument_list|(
name|caller
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|caller
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|IvyNode
name|getDirectCallerFor
parameter_list|(
name|ModuleId
name|from
parameter_list|)
block|{
return|return
operator|(
name|IvyNode
operator|)
name|_allCallers
operator|.
name|get
argument_list|(
name|from
argument_list|)
return|;
block|}
comment|/**      * Returns true if ALL callers exclude the given artifact in the given root module conf      * @param rootModuleConf      * @param artifact      * @return      */
name|boolean
name|doesCallersExclude
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|doesCallersExclude
argument_list|(
name|rootModuleConf
argument_list|,
name|artifact
argument_list|,
operator|new
name|Stack
argument_list|()
argument_list|)
return|;
block|}
name|boolean
name|doesCallersExclude
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|Stack
name|callersStack
parameter_list|)
block|{
if|if
condition|(
name|callersStack
operator|.
name|contains
argument_list|(
name|_node
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|callersStack
operator|.
name|push
argument_list|(
name|_node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Caller
index|[]
name|callers
init|=
name|getCallers
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|callers
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|callers
index|[
name|i
index|]
operator|.
name|canExclude
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ModuleDescriptor
name|md
init|=
name|callers
index|[
name|i
index|]
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|doesExclude
argument_list|(
name|md
argument_list|,
name|rootModuleConf
argument_list|,
name|callers
index|[
name|i
index|]
operator|.
name|getCallerConfigurations
argument_list|()
argument_list|,
name|callers
index|[
name|i
index|]
operator|.
name|getDependencyDescriptor
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|callersStack
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
finally|finally
block|{
name|callersStack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|doesExclude
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|rootModuleConf
parameter_list|,
name|String
index|[]
name|moduleConfs
parameter_list|,
name|DependencyDescriptor
name|dd
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|Stack
name|callersStack
parameter_list|)
block|{
comment|// artifact is excluded if it match any of the exclude pattern for this dependency...
if|if
condition|(
name|dd
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|dd
operator|.
name|doesExclude
argument_list|(
name|moduleConfs
argument_list|,
name|artifact
operator|.
name|getId
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|md
operator|.
name|doesExclude
argument_list|(
name|moduleConfs
argument_list|,
name|artifact
operator|.
name|getId
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// ... or if it is excluded by all its callers
name|IvyNode
name|c
init|=
name|_node
operator|.
name|getData
argument_list|()
operator|.
name|getNode
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
operator|.
name|doesCallersExclude
argument_list|(
name|rootModuleConf
argument_list|,
name|artifact
argument_list|,
name|callersStack
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

