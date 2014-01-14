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
name|conflict
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
name|IvyNodeBlacklist
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
name|ivy
operator|.
name|core
operator|.
name|resolve
operator|.
name|ResolveData
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
name|RestartResolveProcess
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
name|settings
operator|.
name|IvySettings
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
name|latest
operator|.
name|LatestStrategy
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
name|version
operator|.
name|VersionMatcher
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
name|Message
import|;
end_import

begin_comment
comment|/**  * This conflict manager can be used to allow only compatible dependencies to be used together (like  * the strict conflict manager), but it has the advantage of using a best effort algorithm to find a  * set of compatible dependencies, even if it requires stepping back to older revisions (as long as  * they are in the set of compatibility).  *<p>  * Here is an example of what this conflict manager is able to do:<br/>  *<b>Available Modules</b>:  *   *<pre>  * #A;2-&gt;{ #B;[1.0,1.5] #C;[2.0,2.5] }  * #B;1.4-&gt;#D;1.5  * #B;1.5-&gt;#D;2.0  * #C;2.5-&gt;#D;[1.0,1.6]  *</pre>  *   *<b>Result</b>: #B;1.4, #C;2.5, #D;1.5<br/>  *<b>Details</b>The conflict manager finds that the latest matching version of #B (1.5) depends on  * a version of #D incompatible with what is expected by the latest matching version of #C. Hence  * the conflict manager blacklists #B;1.5, and the version range [1.0,1.5] is resolved again to end  * up with #B;1.4 which depends on #D;1.5, which is fine to work with #C;2.5.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|LatestCompatibleConflictManager
extends|extends
name|LatestConflictManager
block|{
specifier|public
name|LatestCompatibleConflictManager
parameter_list|()
block|{
block|}
specifier|public
name|LatestCompatibleConflictManager
parameter_list|(
name|String
name|name
parameter_list|,
name|LatestStrategy
name|strategy
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|)
block|{
if|if
condition|(
name|conflicts
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
return|return
name|conflicts
return|;
block|}
name|VersionMatcher
name|versionMatcher
init|=
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
name|Iterator
name|iter
init|=
name|conflicts
operator|.
name|iterator
argument_list|()
decl_stmt|;
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
name|ModuleRevisionId
name|mrid
init|=
name|node
operator|.
name|getResolvedId
argument_list|()
decl_stmt|;
if|if
condition|(
name|versionMatcher
operator|.
name|isDynamic
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|IvyNode
name|other
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
name|versionMatcher
operator|.
name|isDynamic
argument_list|(
name|other
operator|.
name|getResolvedId
argument_list|()
argument_list|)
condition|)
block|{
comment|// two dynamic versions in conflict, not enough information yet
return|return
literal|null
return|;
block|}
if|else if
condition|(
operator|!
name|versionMatcher
operator|.
name|accept
argument_list|(
name|mrid
argument_list|,
name|other
operator|.
name|getResolvedId
argument_list|()
argument_list|)
condition|)
block|{
comment|// incompatibility found
if|if
condition|(
operator|!
name|handleIncompatibleConflict
argument_list|(
name|parent
argument_list|,
name|conflicts
argument_list|,
name|node
argument_list|,
name|other
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|// no incompatibility nor dynamic version found, let's return the latest static version
if|if
condition|(
name|conflicts
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
comment|// very common special case of only two modules in conflict,
comment|// let's return the second one (static)
name|Iterator
name|it
init|=
name|conflicts
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
name|Collection
name|newConflicts
init|=
operator|new
name|LinkedHashSet
argument_list|(
name|conflicts
argument_list|)
decl_stmt|;
name|newConflicts
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|resolveConflicts
argument_list|(
name|parent
argument_list|,
name|newConflicts
argument_list|)
return|;
block|}
else|else
block|{
comment|// the first node is a static revision, let's see if all other versions match
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|IvyNode
name|other
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
operator|!
name|versionMatcher
operator|.
name|accept
argument_list|(
name|other
operator|.
name|getResolvedId
argument_list|()
argument_list|,
name|mrid
argument_list|)
condition|)
block|{
comment|// incompatibility found
if|if
condition|(
operator|!
name|handleIncompatibleConflict
argument_list|(
name|parent
argument_list|,
name|conflicts
argument_list|,
name|node
argument_list|,
name|other
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
comment|// no incompatibility found, let's return this static version
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|node
argument_list|)
return|;
block|}
block|}
comment|/**      * Handles an incompatible conflict      *<p>      * An incompatible conflicts is handled with this pseudo algorithm:      *       *<pre>      * take latest among two nodes in conflict      *   for all callers      *      if dependency is a version constraint (dynamic)      *         blacklist the mapped version      *      else      *         recurse for all callers      *   if a version constraint has been found      *     restart resolve      *   else      *     throw strict conflict exception      *</pre>      *       *</p>      *       * @param parent      *            the parent node of nodes in conflict      * @param conflicts      *            all the nodes in conflict      * @param node      *            one of the two incompatible nodes      * @param other      *            the other incompatible node      * @return true if the incompatible conflict has been handled, false otherwise (in which case      *         resolveConflicts should return null)      */
specifier|private
name|boolean
name|handleIncompatibleConflict
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|,
name|IvyNode
name|node
parameter_list|,
name|IvyNode
name|other
parameter_list|)
block|{
comment|// we never actually return anything else than false or throw an exception,
comment|// but returning a boolean make the calling code cleaner
try|try
block|{
name|IvyNodeArtifactInfo
name|latest
init|=
operator|(
name|IvyNodeArtifactInfo
operator|)
name|getStrategy
argument_list|()
operator|.
name|findLatest
argument_list|(
name|toArtifactInfo
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|IvyNode
index|[]
block|{
name|node
block|,
name|other
block|}
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|latest
operator|!=
literal|null
condition|)
block|{
name|IvyNode
name|latestNode
init|=
name|latest
operator|.
name|getNode
argument_list|()
decl_stmt|;
name|IvyNode
name|oldestNode
init|=
name|latestNode
operator|==
name|node
condition|?
name|other
else|:
name|node
decl_stmt|;
name|blackListIncompatibleCallerAndRestartResolveIfPossible
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|parent
argument_list|,
name|oldestNode
argument_list|,
name|latestNode
argument_list|)
expr_stmt|;
comment|// if we arrive here, we haven't managed to blacklist all paths to the latest
comment|// node, we try with the oldest
name|blackListIncompatibleCallerAndRestartResolveIfPossible
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|parent
argument_list|,
name|latestNode
argument_list|,
name|oldestNode
argument_list|)
expr_stmt|;
comment|// still not possible, we aren't able to find a solution to the incompatibility
name|handleUnsolvableConflict
argument_list|(
name|parent
argument_list|,
name|conflicts
argument_list|,
name|node
argument_list|,
name|other
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
comment|// never actually reached
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|NoConflictResolvedYetException
name|ex
parameter_list|)
block|{
comment|// we have not enough informations in the nodes to resolve conflict
comment|// according to the resolveConflicts contract, resolveConflicts must return null
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|void
name|blackListIncompatibleCallerAndRestartResolveIfPossible
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|IvyNode
name|parent
parameter_list|,
name|IvyNode
name|selected
parameter_list|,
name|IvyNode
name|evicted
parameter_list|)
block|{
name|Stack
name|callerStack
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
name|callerStack
operator|.
name|push
argument_list|(
name|evicted
argument_list|)
expr_stmt|;
specifier|final
name|Collection
name|toBlacklist
init|=
name|blackListIncompatibleCaller
argument_list|(
name|settings
operator|.
name|getVersionMatcher
argument_list|()
argument_list|,
name|parent
argument_list|,
name|selected
argument_list|,
name|evicted
argument_list|,
name|callerStack
argument_list|)
decl_stmt|;
if|if
condition|(
name|toBlacklist
operator|!=
literal|null
condition|)
block|{
specifier|final
name|StringBuffer
name|blacklisted
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|toBlacklist
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
name|IvyNodeBlacklist
name|blacklist
init|=
operator|(
name|IvyNodeBlacklist
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|blacklist
operator|.
name|getBlacklistedNode
argument_list|()
operator|.
name|blacklist
argument_list|(
name|blacklist
argument_list|)
expr_stmt|;
name|blacklisted
operator|.
name|append
argument_list|(
name|blacklist
operator|.
name|getBlacklistedNode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|blacklisted
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|rootModuleConf
init|=
name|parent
operator|.
name|getData
argument_list|()
operator|.
name|getReport
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|evicted
operator|.
name|markEvicted
argument_list|(
operator|new
name|EvictionData
argument_list|(
name|rootModuleConf
argument_list|,
name|parent
argument_list|,
name|this
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|selected
argument_list|)
argument_list|,
literal|"with blacklisting of "
operator|+
name|blacklisted
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|settings
operator|.
name|debugConflictResolution
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"evicting "
operator|+
name|evicted
operator|+
literal|" by "
operator|+
name|evicted
operator|.
name|getEvictedData
argument_list|(
name|rootModuleConf
argument_list|)
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|RestartResolveProcess
argument_list|(
literal|"trying to handle incompatibilities between "
operator|+
name|selected
operator|+
literal|" and "
operator|+
name|evicted
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|handleIncompatibleCaller
parameter_list|(
name|Stack
name|callerStack
parameter_list|,
name|IvyNode
name|node
parameter_list|,
name|IvyNode
name|callerNode
parameter_list|,
name|IvyNode
name|conflictParent
parameter_list|,
name|IvyNode
name|selectedNode
parameter_list|,
name|IvyNode
name|evictedNode
parameter_list|,
name|Collection
name|blacklisted
parameter_list|,
name|VersionMatcher
name|versionMatcher
parameter_list|)
block|{
if|if
condition|(
name|callerStack
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|callerStack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
comment|// circular dependency found and handled: the current top of the stack (node)
comment|// was already contained in the rest of the stack, the circle is closed, nothing
comment|// else to do
return|return
literal|true
return|;
block|}
else|else
block|{
name|callerStack
operator|.
name|push
argument_list|(
name|callerNode
argument_list|)
expr_stmt|;
name|Collection
name|sub
init|=
name|blackListIncompatibleCaller
argument_list|(
name|versionMatcher
argument_list|,
name|conflictParent
argument_list|,
name|selectedNode
argument_list|,
name|evictedNode
argument_list|,
name|callerStack
argument_list|)
decl_stmt|;
name|callerStack
operator|.
name|pop
argument_list|()
expr_stmt|;
if|if
condition|(
name|sub
operator|==
literal|null
condition|)
block|{
comment|// propagate the fact that a path with unblacklistable caller has been found
return|return
literal|false
return|;
block|}
else|else
block|{
name|blacklisted
operator|.
name|addAll
argument_list|(
name|sub
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
comment|/**      * Tries to blacklist exactly one version for all callers paths.      *       * @param versionMatcher      *            the version matcher to use to interpret versions      * @param conflictParent      *            the node in which the conflict is occurring      * @param selectedNode      *            the node in favor of which the conflict is resolved      * @param evictedNode      *            the node which will be evicted if we are able to blacklist all paths      * @param node      *            the node for which callers should be considered      * @return the collection of blacklisting to do, null if a blacklist is not possible in at least      *         one caller path      */
specifier|private
name|Collection
comment|/*<IvyNodeBlacklist> */
name|blackListIncompatibleCaller
parameter_list|(
name|VersionMatcher
name|versionMatcher
parameter_list|,
name|IvyNode
name|conflictParent
parameter_list|,
name|IvyNode
name|selectedNode
parameter_list|,
name|IvyNode
name|evictedNode
parameter_list|,
name|Stack
comment|/*<IvyNode> */
name|callerStack
parameter_list|)
block|{
name|Collection
comment|/*<IvyNodeBlacklist> */
name|blacklisted
init|=
operator|new
name|ArrayList
comment|/*<IvyNodeBlacklist> */
argument_list|()
decl_stmt|;
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|callerStack
operator|.
name|peek
argument_list|()
decl_stmt|;
name|String
name|rootModuleConf
init|=
name|conflictParent
operator|.
name|getData
argument_list|()
operator|.
name|getReport
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|Caller
index|[]
name|callers
init|=
name|node
operator|.
name|getCallers
argument_list|(
name|rootModuleConf
argument_list|)
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
name|callers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|IvyNode
name|callerNode
init|=
name|node
operator|.
name|findNode
argument_list|(
name|callers
index|[
name|i
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|callerNode
operator|.
name|isBlacklisted
argument_list|(
name|rootModuleConf
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|versionMatcher
operator|.
name|isDynamic
argument_list|(
name|callers
index|[
name|i
index|]
operator|.
name|getAskedDependencyId
argument_list|(
name|node
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|blacklisted
operator|.
name|add
argument_list|(
operator|new
name|IvyNodeBlacklist
argument_list|(
name|conflictParent
argument_list|,
name|selectedNode
argument_list|,
name|evictedNode
argument_list|,
name|node
argument_list|,
name|rootModuleConf
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|isEvicted
argument_list|(
name|rootModuleConf
argument_list|)
operator|&&
operator|!
name|handleIncompatibleCaller
argument_list|(
name|callerStack
argument_list|,
name|node
argument_list|,
name|callerNode
argument_list|,
name|conflictParent
argument_list|,
name|selectedNode
argument_list|,
name|evictedNode
argument_list|,
name|blacklisted
argument_list|,
name|versionMatcher
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|handleIncompatibleCaller
argument_list|(
name|callerStack
argument_list|,
name|node
argument_list|,
name|callerNode
argument_list|,
name|conflictParent
argument_list|,
name|selectedNode
argument_list|,
name|evictedNode
argument_list|,
name|blacklisted
argument_list|,
name|versionMatcher
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|blacklisted
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|callerStack
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|callerStack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|blacklisted
return|;
block|}
specifier|protected
name|void
name|handleUnsolvableConflict
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|,
name|IvyNode
name|node1
parameter_list|,
name|IvyNode
name|node2
parameter_list|)
block|{
throw|throw
operator|new
name|StrictConflictException
argument_list|(
name|node1
argument_list|,
name|node2
argument_list|)
throw|;
block|}
specifier|public
name|void
name|handleAllBlacklistedRevisions
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|Collection
comment|/*<ModuleRevisionId> */
name|foundBlacklisted
parameter_list|)
block|{
name|ResolveData
name|resolveData
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getResolveData
argument_list|()
decl_stmt|;
name|Collection
comment|/*<IvyNode> */
name|blacklisted
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|foundBlacklisted
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
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|blacklisted
operator|.
name|add
argument_list|(
name|resolveData
operator|.
name|getNode
argument_list|(
name|mrid
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iterator
init|=
name|blacklisted
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
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|IvyNodeBlacklist
name|bdata
init|=
name|node
operator|.
name|getBlacklistData
argument_list|(
name|resolveData
operator|.
name|getReport
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|handleUnsolvableConflict
argument_list|(
name|bdata
operator|.
name|getConflictParent
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|bdata
operator|.
name|getEvictedNode
argument_list|()
block|,
name|bdata
operator|.
name|getSelectedNode
argument_list|()
block|}
argument_list|)
argument_list|,
name|bdata
operator|.
name|getEvictedNode
argument_list|()
argument_list|,
name|bdata
operator|.
name|getSelectedNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

