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
name|ArrayList
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
name|Collections
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
name|LinkedHashSet
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
name|descriptor
operator|.
name|Configuration
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
name|ivy
operator|.
name|plugins
operator|.
name|conflict
operator|.
name|ConflictManager
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
name|util
operator|.
name|Checks
import|;
end_import

begin_comment
comment|/**  * A visit node is an object used to represent one visit from one parent on an {@link IvyNode} of  * the dependency graph. During dependency resolution, the {@link ResolveEngine} visits nodes of the  * depency graph following the dependencies, thus the same node can be visited several times, if it  * is requested from several module. In this case you will have one VisitNode per parent and per  * root module configuration. Thus VisitNode stores data specific to the visit:  *<ul>  *<li>parent</li>  * the node from which the visit is occuring  *<li>parentConf</li>  * the configuration of the parent in which this node is visited  *<li>rootModuleConf</li>  * the configuration of the root module which is currently resolved  *</ul>  */
end_comment

begin_class
specifier|public
class|class
name|VisitNode
block|{
comment|/**      * The node which is currently visited      */
specifier|private
name|IvyNode
name|node
decl_stmt|;
comment|/**      * Represents the current parent of the node during ivy visit of dependency graph.      */
specifier|private
name|VisitNode
name|parent
init|=
literal|null
decl_stmt|;
comment|/**      * The root node of the current visit It is null until it is required, see getRoot      */
specifier|private
name|VisitNode
name|root
init|=
literal|null
decl_stmt|;
comment|/**      * Direct path from root to this node. Note that the colleciton is ordered but is not a list      * implementation This collection is null until it is required, see getPath      */
specifier|private
name|Collection
name|path
init|=
literal|null
decl_stmt|;
comment|// Collection(VisitNode)
comment|/**      * The configuration of the parent module in the current visit      */
specifier|private
name|String
name|parentConf
init|=
literal|null
decl_stmt|;
comment|/**      * The configuration requested by the parent Note that this is the actual conf requested by the      * parent, not a configuration extended by the requested conf which actually trigger the node      * visit      */
specifier|private
name|String
name|requestedConf
decl_stmt|;
comment|/**      * The root configuration which is currently visited      */
specifier|private
name|String
name|rootModuleConf
decl_stmt|;
comment|/**      * Shared ResolveData instance, which can be used to get info on the current resolve process      */
specifier|private
name|ResolveData
name|data
decl_stmt|;
comment|/**      * Boolean.TRUE if a node with a same module id as the one visited has already been visited in      * the current path. null if not computed yet Boolean.FALSE otherwise      */
specifier|private
name|Boolean
name|isCircular
decl_stmt|;
specifier|public
name|VisitNode
parameter_list|(
name|ResolveData
name|data
parameter_list|,
name|IvyNode
name|node
parameter_list|,
name|VisitNode
name|parent
parameter_list|,
name|String
name|rootModuleConf
parameter_list|,
name|String
name|parentConf
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|data
argument_list|,
literal|"data"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|node
argument_list|,
literal|"node"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|rootModuleConf
argument_list|,
literal|"rootModuleConf"
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|rootModuleConf
operator|=
name|rootModuleConf
expr_stmt|;
name|this
operator|.
name|parentConf
operator|=
name|parentConf
expr_stmt|;
name|this
operator|.
name|data
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyNode
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
comment|/**      * @return Returns the configuration requested by the parent      */
specifier|public
name|String
name|getRequestedConf
parameter_list|()
block|{
return|return
name|requestedConf
return|;
block|}
specifier|public
name|void
name|setRequestedConf
parameter_list|(
name|String
name|requestedConf
parameter_list|)
block|{
name|this
operator|.
name|requestedConf
operator|=
name|requestedConf
expr_stmt|;
block|}
specifier|public
name|VisitNode
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|VisitNode
name|getRoot
parameter_list|()
block|{
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
name|root
operator|=
name|computeRoot
argument_list|()
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
specifier|public
name|Collection
comment|/*<VisitNode>*/
name|getPath
parameter_list|()
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
name|computePath
argument_list|()
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
specifier|private
name|Collection
comment|/*<VisitNode>*/
name|computePath
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|Collection
name|p
init|=
operator|new
name|LinkedHashSet
argument_list|(
name|parent
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|p
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
specifier|private
name|VisitNode
name|computeRoot
parameter_list|()
block|{
if|if
condition|(
name|node
operator|.
name|isRoot
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
if|else if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getRoot
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|String
name|getParentConf
parameter_list|()
block|{
return|return
name|parentConf
return|;
block|}
specifier|public
name|void
name|setParentConf
parameter_list|(
name|String
name|parentConf
parameter_list|)
block|{
name|this
operator|.
name|parentConf
operator|=
name|parentConf
expr_stmt|;
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
specifier|static
name|VisitNode
name|getRoot
parameter_list|(
name|VisitNode
name|parent
parameter_list|)
block|{
name|VisitNode
name|root
init|=
name|parent
decl_stmt|;
name|Collection
name|path
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|path
operator|.
name|add
argument_list|(
name|root
argument_list|)
expr_stmt|;
while|while
condition|(
name|root
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|root
operator|.
name|getNode
argument_list|()
operator|.
name|isRoot
argument_list|()
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
name|root
operator|.
name|getParent
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|root
return|;
block|}
name|root
operator|=
name|root
operator|.
name|getParent
argument_list|()
expr_stmt|;
name|path
operator|.
name|add
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
comment|/**      * Returns true if the current dependency descriptor is transitive and the parent configuration      * is transitive. Otherwise returns false.      *       * @return true if current node is transitive and the parent configuration is transitive.      */
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
operator|(
name|data
operator|.
name|isTransitive
argument_list|()
operator|&&
name|node
operator|.
name|getDependencyDescriptor
argument_list|(
name|getParentNode
argument_list|()
argument_list|)
operator|.
name|isTransitive
argument_list|()
operator|&&
name|isParentConfTransitive
argument_list|()
operator|)
return|;
block|}
comment|/**      * Checks if the current node's parent configuration is transitive.      *       * @param node      *            current node      * @return true if the node's parent configuration is transitive      */
specifier|protected
name|boolean
name|isParentConfTransitive
parameter_list|()
block|{
name|String
name|conf
init|=
name|getParent
argument_list|()
operator|.
name|getRequestedConf
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Configuration
name|parentConf
init|=
name|getParentNode
argument_list|()
operator|.
name|getConfiguration
argument_list|(
name|conf
argument_list|)
decl_stmt|;
return|return
name|parentConf
operator|.
name|isTransitive
argument_list|()
return|;
block|}
comment|/**      * Returns the 'real' node currently visited. 'Real' means that if we are visiting a node      * created originally with only a version constraint, and if this version constraint has been      * resolved to an existing node in the graph, we will return the existing node, and not the one      * originally used which is about to be discarded, since it's not possible to have in the graph      * two nodes for the same ModuleRevisionId      *       * @return the 'real' node currently visited.      */
specifier|public
name|IvyNode
name|getRealNode
parameter_list|()
block|{
name|IvyNode
name|node
init|=
name|this
operator|.
name|node
operator|.
name|getRealNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|node
return|;
block|}
else|else
block|{
return|return
name|this
operator|.
name|node
return|;
block|}
block|}
comment|/**      * Ask to the current visited node to use a real node only, if one exist. See getRealNode for      * details about what a 'real' node is.      */
specifier|public
name|void
name|useRealNode
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
comment|// use real node make sense only for non root module
name|IvyNode
name|node
init|=
name|data
operator|.
name|getNode
argument_list|(
name|this
operator|.
name|node
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
operator|&&
name|node
operator|!=
name|this
operator|.
name|node
condition|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|loadData
parameter_list|(
name|String
name|conf
parameter_list|,
name|boolean
name|shouldBePublic
parameter_list|)
block|{
name|boolean
name|loaded
init|=
name|node
operator|.
name|loadData
argument_list|(
name|rootModuleConf
argument_list|,
name|getParentNode
argument_list|()
argument_list|,
name|parentConf
argument_list|,
name|conf
argument_list|,
name|shouldBePublic
argument_list|)
decl_stmt|;
if|if
condition|(
name|loaded
condition|)
block|{
name|useRealNode
argument_list|()
expr_stmt|;
comment|// if the loaded revision is different from original one
comment|// we now register this node on the new resolved id
comment|// this includes two cases:
comment|// - the id refers to a dynamic revision, which has been resolved by loadData
comment|// - the loaded module descriptor has extra attributes in his info tag which are not
comment|//   used when declaring the dependency
if|if
condition|(
operator|!
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
condition|)
block|{
name|data
operator|.
name|register
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|loaded
return|;
block|}
specifier|public
name|Collection
name|getDependencies
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|Collection
name|deps
init|=
name|node
operator|.
name|getDependencies
argument_list|(
name|rootModuleConf
argument_list|,
name|conf
argument_list|,
name|requestedConf
argument_list|)
decl_stmt|;
name|Collection
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|deps
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|deps
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
name|depNode
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
name|traverseChild
argument_list|(
name|conf
argument_list|,
name|depNode
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/**      * Returns a VisitNode for the given node. The given node must be a representation of the same      * module (usually in another revision) as the one visited by this node. The given node must      * also have been already visited.      *       * @param node      *            the node to visit      * @return a VisitNode for the given node      */
name|VisitNode
name|gotoNode
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|node
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
literal|"You can't use gotoNode for a node which does not represent the same Module "
operator|+
literal|"as the one represented by this node.\nCurrent node module id="
operator|+
name|getModuleId
argument_list|()
operator|+
literal|" Given node module id="
operator|+
name|node
operator|.
name|getModuleId
argument_list|()
argument_list|)
throw|;
block|}
name|VisitData
name|visitData
init|=
name|data
operator|.
name|getVisitData
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|visitData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You can't use gotoNode with a node which has not been visited yet.\n"
operator|+
literal|"Given node id="
operator|+
name|node
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|visitData
operator|.
name|getVisitNodes
argument_list|(
name|rootModuleConf
argument_list|)
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
name|VisitNode
name|vnode
init|=
operator|(
name|VisitNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|parent
operator|==
literal|null
operator|&&
name|vnode
operator|.
name|getParent
argument_list|()
operator|==
literal|null
operator|)
operator|||
operator|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|vnode
operator|.
name|getParent
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|vnode
return|;
block|}
block|}
comment|// the node has not yet been visited from the current parent, we create a new visit node
return|return
name|traverse
argument_list|(
name|parent
argument_list|,
name|parentConf
argument_list|,
name|node
argument_list|)
return|;
block|}
specifier|private
name|VisitNode
name|traverseChild
parameter_list|(
name|String
name|parentConf
parameter_list|,
name|IvyNode
name|child
parameter_list|)
block|{
name|VisitNode
name|parent
init|=
name|this
decl_stmt|;
return|return
name|traverse
argument_list|(
name|parent
argument_list|,
name|parentConf
argument_list|,
name|child
argument_list|)
return|;
block|}
specifier|private
name|VisitNode
name|traverse
parameter_list|(
name|VisitNode
name|parent
parameter_list|,
name|String
name|parentConf
parameter_list|,
name|IvyNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|getPath
argument_list|()
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getCircularDependencyStrategy
argument_list|()
operator|.
name|handleCircularDependency
argument_list|(
name|toMrids
argument_list|(
name|getPath
argument_list|()
argument_list|,
name|node
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// we do not use the new parent, but the first one, to always be able to go up to the
comment|// root
comment|// parent = getVisitNode(depNode).getParent();
block|}
return|return
operator|new
name|VisitNode
argument_list|(
name|data
argument_list|,
name|node
argument_list|,
name|parent
argument_list|,
name|rootModuleConf
argument_list|,
name|parentConf
argument_list|)
return|;
block|}
specifier|private
name|ModuleRevisionId
index|[]
name|toMrids
parameter_list|(
name|Collection
name|path
parameter_list|,
name|ModuleRevisionId
name|last
parameter_list|)
block|{
name|ModuleRevisionId
index|[]
name|ret
init|=
operator|new
name|ModuleRevisionId
index|[
name|path
operator|.
name|size
argument_list|()
operator|+
literal|1
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|path
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|VisitNode
name|node
init|=
operator|(
name|VisitNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
index|[
name|i
index|]
operator|=
name|node
operator|.
name|getNode
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
name|ret
index|[
name|ret
operator|.
name|length
operator|-
literal|1
index|]
operator|=
name|last
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getResolvedId
parameter_list|()
block|{
return|return
name|node
operator|.
name|getResolvedId
argument_list|()
return|;
block|}
specifier|public
name|void
name|updateConfsToFetch
parameter_list|(
name|Collection
name|confs
parameter_list|)
block|{
name|node
operator|.
name|updateConfsToFetch
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|getId
parameter_list|()
block|{
return|return
name|node
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isEvicted
parameter_list|()
block|{
return|return
name|node
operator|.
name|isEvicted
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getRealConfs
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
return|return
name|node
operator|.
name|getRealConfs
argument_list|(
name|conf
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasProblem
parameter_list|()
block|{
return|return
name|node
operator|.
name|hasProblem
argument_list|()
return|;
block|}
specifier|public
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
return|return
name|node
operator|.
name|getConfiguration
argument_list|(
name|conf
argument_list|)
return|;
block|}
specifier|public
name|EvictionData
name|getEvictedData
parameter_list|()
block|{
return|return
name|node
operator|.
name|getEvictedData
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|public
name|DependencyDescriptor
name|getDependencyDescriptor
parameter_list|()
block|{
return|return
name|node
operator|.
name|getDependencyDescriptor
argument_list|(
name|getParentNode
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|IvyNode
name|getParentNode
parameter_list|()
block|{
return|return
name|parent
operator|==
literal|null
condition|?
literal|null
else|:
name|parent
operator|.
name|getNode
argument_list|()
return|;
block|}
comment|/**      * Returns true if this node can already be found in the path      *       * @return      */
specifier|public
name|boolean
name|isCircular
parameter_list|()
block|{
if|if
condition|(
name|isCircular
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|isCircular
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
comment|// asumme it's false, and see if it isn't by checking
comment|// the parent path
for|for
control|(
name|Iterator
name|iter
init|=
name|parent
operator|.
name|getPath
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
name|VisitNode
name|ancestor
init|=
operator|(
name|VisitNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|getId
argument_list|()
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|ancestor
operator|.
name|getId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
name|isCircular
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
break|break;
block|}
block|}
block|}
else|else
block|{
name|isCircular
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
return|return
name|isCircular
operator|.
name|booleanValue
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getConfsToFetch
parameter_list|()
block|{
return|return
name|node
operator|.
name|getConfsToFetch
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getRequiredConfigurations
parameter_list|(
name|VisitNode
name|in
parameter_list|,
name|String
name|inConf
parameter_list|)
block|{
return|return
name|node
operator|.
name|getRequiredConfigurations
argument_list|(
name|in
operator|.
name|getNode
argument_list|()
argument_list|,
name|inConf
argument_list|)
return|;
block|}
specifier|public
name|ModuleId
name|getModuleId
parameter_list|()
block|{
return|return
name|node
operator|.
name|getModuleId
argument_list|()
return|;
block|}
specifier|public
name|Collection
name|getResolvedRevisions
parameter_list|(
name|ModuleId
name|mid
parameter_list|)
block|{
return|return
name|node
operator|.
name|getResolvedRevisions
argument_list|(
name|mid
argument_list|,
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|public
name|void
name|markEvicted
parameter_list|(
name|EvictionData
name|evictionData
parameter_list|)
block|{
name|node
operator|.
name|markEvicted
argument_list|(
name|evictionData
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getRequiredConfigurations
parameter_list|()
block|{
return|return
name|node
operator|.
name|getRequiredConfigurations
argument_list|()
return|;
block|}
comment|/**      * Marks the current node as evicted by the the given selected IvyNodes, in the given parent and      * root module configuration, with the given {@link ConflictManager}      *       * @param parent      *            the VisitNode in which eviction has been made      * @param conflictMgr      *            the conflict manager responsible for the eviction      * @param selected      *            a Collection of {@link IvyNode} which have been selected      */
specifier|public
name|void
name|markEvicted
parameter_list|(
name|VisitNode
name|parent
parameter_list|,
name|ConflictManager
name|conflictMgr
parameter_list|,
name|Collection
name|selected
parameter_list|)
block|{
name|node
operator|.
name|markEvicted
argument_list|(
name|rootModuleConf
argument_list|,
name|parent
operator|.
name|getNode
argument_list|()
argument_list|,
name|conflictMgr
argument_list|,
name|selected
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
name|node
operator|.
name|getDescriptor
argument_list|()
return|;
block|}
specifier|public
name|EvictionData
name|getEvictionDataInRoot
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|VisitNode
name|ancestor
parameter_list|)
block|{
return|return
name|node
operator|.
name|getEvictionDataInRoot
argument_list|(
name|rootModuleConf
argument_list|,
name|ancestor
operator|.
name|getNode
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|getEvictedRevisions
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
return|return
name|node
operator|.
name|getEvictedRevisions
argument_list|(
name|moduleId
argument_list|,
name|rootModuleConf
argument_list|)
return|;
block|}
comment|// public void setRootModuleConf(String rootModuleConf) {
comment|// if (rootModuleConf != null&& !rootModuleConf.equals(rootModuleConf)) {
comment|// _confsToFetch.clear(); // we change of root module conf => we discard all confs to fetch
comment|// }
comment|// if (rootModuleConf != null&& rootModuleConf.equals(rootModuleConf)) {
comment|// _selectedDeps.put(new ModuleIdConf(_id.getModuleId(), rootModuleConf),
comment|// Collections.singleton(this));
comment|// }
comment|// rootModuleConf = rootModuleConf;
comment|// }
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|node
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

