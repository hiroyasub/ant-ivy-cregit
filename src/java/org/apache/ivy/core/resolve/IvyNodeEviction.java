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
name|plugins
operator|.
name|conflict
operator|.
name|ConflictManager
import|;
end_import

begin_class
specifier|public
class|class
name|IvyNodeEviction
block|{
comment|/**      * This class contains data about the eviction of an {@link IvyNode}.      */
specifier|public
specifier|static
class|class
name|EvictionData
block|{
comment|/**          * Can be null in case of transitive eviction.          */
specifier|private
name|IvyNode
name|parent
decl_stmt|;
comment|/**          * Can be null in case of transitive eviction.          */
specifier|private
name|ConflictManager
name|conflictManager
decl_stmt|;
comment|/**          * Can be null in case of transitive eviction.          */
specifier|private
name|Collection
name|selected
decl_stmt|;
comment|// Collection(IvyNode)
specifier|private
name|String
name|rootModuleConf
decl_stmt|;
comment|/**          * Creates a new object containing the eviction data of an {@link IvyNode}.          *           * @param rootModuleConf the rootmodule configuration          * @param parent the parent node (or<tt>null</tt> in case of transitive eviction)          * @param conflictManager the conflictmanager which evicted the node (or<tt>null</tt> in          *                        case of transitive eviction)          * @param selected a collection of {@link IvyNode}s which evict the evicted node (or           *<tt>null</tt> in case of transitive eviction)          */
specifier|public
name|EvictionData
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|IvyNode
name|parent
parameter_list|,
name|ConflictManager
name|conflictManager
parameter_list|,
name|Collection
name|selected
parameter_list|)
block|{
name|this
operator|.
name|rootModuleConf
operator|=
name|rootModuleConf
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|conflictManager
operator|=
name|conflictManager
expr_stmt|;
name|this
operator|.
name|selected
operator|=
name|selected
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|selected
operator|!=
literal|null
condition|)
block|{
return|return
name|selected
operator|+
literal|" in "
operator|+
name|parent
operator|+
literal|" ("
operator|+
name|conflictManager
operator|+
literal|") ["
operator|+
name|rootModuleConf
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
literal|"transitively ["
operator|+
name|rootModuleConf
operator|+
literal|"]"
return|;
block|}
block|}
specifier|public
name|ConflictManager
name|getConflictManager
parameter_list|()
block|{
return|return
name|conflictManager
return|;
block|}
specifier|public
name|IvyNode
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|Collection
name|getSelected
parameter_list|()
block|{
return|return
name|selected
return|;
block|}
specifier|public
name|String
name|getRootModuleConf
parameter_list|()
block|{
return|return
name|rootModuleConf
return|;
block|}
specifier|public
name|boolean
name|isTransitivelyEvicted
parameter_list|()
block|{
return|return
name|parent
operator|==
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ModuleIdConf
block|{
specifier|private
name|ModuleId
name|moduleId
decl_stmt|;
specifier|private
name|String
name|conf
decl_stmt|;
specifier|public
name|ModuleIdConf
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
if|if
condition|(
name|mid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"mid cannot be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"conf cannot be null"
argument_list|)
throw|;
block|}
name|moduleId
operator|=
name|mid
expr_stmt|;
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
specifier|final
name|ModuleId
name|getModuleId
parameter_list|()
block|{
return|return
name|moduleId
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
name|ModuleIdConf
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ModuleIdConf
operator|)
name|obj
operator|)
operator|.
name|getModuleId
argument_list|()
argument_list|)
operator|&&
name|getConf
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ModuleIdConf
operator|)
name|obj
operator|)
operator|.
name|getConf
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|//CheckStyle:MagicNumber| OFF
name|int
name|hash
init|=
literal|33
decl_stmt|;
name|hash
operator|+=
name|getModuleId
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|17
expr_stmt|;
name|hash
operator|+=
name|getConf
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|17
expr_stmt|;
comment|//CheckStyle:MagicNumber| ON
return|return
name|hash
return|;
block|}
block|}
specifier|private
name|IvyNode
name|node
decl_stmt|;
specifier|private
name|Map
name|selectedDeps
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleIdConf -> Set(Node)) // map indicating
comment|// for each dependency which node has been selected
specifier|private
name|Map
name|pendingConflicts
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleIdConf -> Set(Node)) // map
comment|// indicating for each dependency which nodes
comment|// are in pending conflict (conflict detected
comment|// but not yet resolved)
specifier|private
name|Map
name|evictedDeps
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleIdConf -> Set(Node)) // map indicating
comment|// for each dependency which node has been evicted
specifier|private
name|Map
name|evictedRevs
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleIdConf -> Set(ModuleRevisionId)) //
comment|// map indicating for each dependency which revision
comment|// has been evicted
specifier|private
name|Map
name|evicted
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (root module conf -> EvictionData) // indicates
comment|// if the node is evicted in each root module conf
specifier|public
name|IvyNodeEviction
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"node must not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|Collection
name|getResolvedNodes
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Collection
name|resolved
init|=
operator|(
name|Collection
operator|)
name|selectedDeps
operator|.
name|get
argument_list|(
operator|new
name|ModuleIdConf
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
decl_stmt|;
name|Set
name|ret
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolved
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|resolved
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|node
operator|.
name|getRealNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|Collection
name|getResolvedRevisions
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Collection
name|resolved
init|=
operator|(
name|Collection
operator|)
name|selectedDeps
operator|.
name|get
argument_list|(
operator|new
name|ModuleIdConf
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolved
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|HashSet
argument_list|()
return|;
block|}
else|else
block|{
name|Collection
name|resolvedRevs
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
name|resolved
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|resolvedRevs
operator|.
name|add
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|resolvedRevs
operator|.
name|add
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resolvedRevs
return|;
block|}
block|}
specifier|public
name|void
name|setResolvedNodes
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|rootModuleConf
parameter_list|,
name|Collection
name|resolved
parameter_list|)
block|{
name|ModuleIdConf
name|moduleIdConf
init|=
operator|new
name|ModuleIdConf
argument_list|(
name|moduleId
argument_list|,
name|rootModuleConf
argument_list|)
decl_stmt|;
name|selectedDeps
operator|.
name|put
argument_list|(
name|moduleIdConf
argument_list|,
operator|new
name|HashSet
argument_list|(
name|resolved
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
name|getEvictedNodes
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Collection
name|resolved
init|=
operator|(
name|Collection
operator|)
name|evictedDeps
operator|.
name|get
argument_list|(
operator|new
name|ModuleIdConf
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
decl_stmt|;
name|Set
name|ret
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolved
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|resolved
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|node
operator|.
name|getRealNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|Collection
name|getEvictedRevisions
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Collection
name|evicted
init|=
operator|(
name|Collection
operator|)
name|evictedRevs
operator|.
name|get
argument_list|(
operator|new
name|ModuleIdConf
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|evicted
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|HashSet
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|HashSet
argument_list|(
name|evicted
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|setEvictedNodes
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|rootModuleConf
parameter_list|,
name|Collection
name|evicted
parameter_list|)
block|{
name|ModuleIdConf
name|moduleIdConf
init|=
operator|new
name|ModuleIdConf
argument_list|(
name|moduleId
argument_list|,
name|rootModuleConf
argument_list|)
decl_stmt|;
name|evictedDeps
operator|.
name|put
argument_list|(
name|moduleIdConf
argument_list|,
operator|new
name|HashSet
argument_list|(
name|evicted
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
name|evictedRevs
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
name|evicted
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|evictedRevs
operator|.
name|add
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|evictedRevs
operator|.
name|add
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|evictedRevs
operator|.
name|put
argument_list|(
name|moduleIdConf
argument_list|,
name|evictedRevs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEvicted
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|cleanEvicted
argument_list|()
expr_stmt|;
name|IvyNode
name|root
init|=
name|node
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|ModuleId
name|moduleId
init|=
name|node
operator|.
name|getId
argument_list|()
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
name|Collection
name|resolvedRevisions
init|=
name|root
operator|.
name|getResolvedRevisions
argument_list|(
name|moduleId
argument_list|,
name|rootModuleConf
argument_list|)
decl_stmt|;
name|EvictionData
name|evictedData
init|=
name|getEvictedData
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
return|return
name|root
operator|!=
name|node
operator|&&
name|evictedData
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|resolvedRevisions
operator|.
name|contains
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
operator|||
name|evictedData
operator|.
name|isTransitivelyEvicted
argument_list|()
operator|)
return|;
block|}
specifier|public
name|boolean
name|isCompletelyEvicted
parameter_list|()
block|{
name|cleanEvicted
argument_list|()
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|isRoot
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
index|[]
name|rootModuleConfigurations
init|=
name|node
operator|.
name|getRootModuleConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rootModuleConfigurations
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
name|isEvicted
argument_list|(
name|rootModuleConfigurations
index|[
name|i
index|]
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
specifier|private
name|void
name|cleanEvicted
parameter_list|()
block|{
comment|// check if it was evicted by a node that we are now the real node for
for|for
control|(
name|Iterator
name|iter
init|=
name|evicted
operator|.
name|keySet
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
name|String
name|rootModuleConf
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|EvictionData
name|ed
init|=
operator|(
name|EvictionData
operator|)
name|evicted
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
name|Collection
name|sel
init|=
name|ed
operator|.
name|getSelected
argument_list|()
decl_stmt|;
if|if
condition|(
name|sel
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iterator
init|=
name|sel
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
name|n
init|=
operator|(
name|IvyNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|.
name|getRealNode
argument_list|()
operator|.
name|equals
argument_list|(
name|this
argument_list|)
condition|)
block|{
comment|// yes, we are the real node for a selected one !
comment|// we are no more evicted in this conf !
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|markEvicted
parameter_list|(
name|EvictionData
name|evictionData
parameter_list|)
block|{
name|evicted
operator|.
name|put
argument_list|(
name|evictionData
operator|.
name|getRootModuleConf
argument_list|()
argument_list|,
name|evictionData
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EvictionData
name|getEvictedData
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|cleanEvicted
argument_list|()
expr_stmt|;
return|return
operator|(
name|EvictionData
operator|)
name|evicted
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getEvictedConfs
parameter_list|()
block|{
name|cleanEvicted
argument_list|()
expr_stmt|;
return|return
operator|(
name|String
index|[]
operator|)
name|evicted
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|evicted
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
comment|/**      * Returns null if this node has only be evicted transitively, or the the colletion of selected      * nodes if it has been evicted by other selected nodes      *       * @return      */
specifier|public
name|Collection
name|getAllEvictingNodes
parameter_list|()
block|{
name|Collection
name|allEvictingNodes
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|evicted
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
name|EvictionData
name|ed
init|=
operator|(
name|EvictionData
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Collection
name|selected
init|=
name|ed
operator|.
name|getSelected
argument_list|()
decl_stmt|;
if|if
condition|(
name|selected
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|allEvictingNodes
operator|==
literal|null
condition|)
block|{
name|allEvictingNodes
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
block|}
name|allEvictingNodes
operator|.
name|addAll
argument_list|(
name|selected
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|allEvictingNodes
return|;
block|}
specifier|public
name|Collection
name|getAllEvictingConflictManagers
parameter_list|()
block|{
name|Collection
name|ret
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
name|evicted
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
name|EvictionData
name|ed
init|=
operator|(
name|EvictionData
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|ed
operator|.
name|getConflictManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * Returns the eviction data for this node if it has been previously evicted in the root, null      * otherwise (if it hasn't been evicted in root) for the given rootModuleConf. Note that this      * method only works if conflict resolution has already be done in all the ancestors.      *       * @param rootModuleConf      * @param ancestor      * @return      */
specifier|public
name|EvictionData
name|getEvictionDataInRoot
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|IvyNode
name|ancestor
parameter_list|)
block|{
name|Collection
name|selectedNodes
init|=
name|node
operator|.
name|getRoot
argument_list|()
operator|.
name|getResolvedNodes
argument_list|(
name|node
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|rootModuleConf
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|selectedNodes
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getResolvedId
argument_list|()
operator|.
name|equals
argument_list|(
name|this
operator|.
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
condition|)
block|{
comment|// the node is part of the selected ones for the root: no eviction data to return
return|return
literal|null
return|;
block|}
block|}
comment|// we didn't find this mrid in the selected ones for the root: it has been previously
comment|// evicted
return|return
operator|new
name|EvictionData
argument_list|(
name|rootModuleConf
argument_list|,
name|ancestor
argument_list|,
name|node
operator|.
name|getRoot
argument_list|()
operator|.
name|getConflictManager
argument_list|(
name|node
operator|.
name|getModuleId
argument_list|()
argument_list|)
argument_list|,
name|selectedNodes
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|getPendingConflicts
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|ModuleId
name|mid
parameter_list|)
block|{
name|Collection
name|resolved
init|=
operator|(
name|Collection
operator|)
name|pendingConflicts
operator|.
name|get
argument_list|(
operator|new
name|ModuleIdConf
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
decl_stmt|;
name|Set
name|ret
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolved
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|resolved
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|node
operator|.
name|getRealNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|setPendingConflicts
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|rootModuleConf
parameter_list|,
name|Collection
name|conflicts
parameter_list|)
block|{
name|ModuleIdConf
name|moduleIdConf
init|=
operator|new
name|ModuleIdConf
argument_list|(
name|moduleId
argument_list|,
name|rootModuleConf
argument_list|)
decl_stmt|;
name|pendingConflicts
operator|.
name|put
argument_list|(
name|moduleIdConf
argument_list|,
operator|new
name|HashSet
argument_list|(
name|conflicts
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

