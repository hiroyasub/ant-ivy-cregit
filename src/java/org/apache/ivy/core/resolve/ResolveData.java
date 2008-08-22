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
name|Date
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
name|LinkedHashMap
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
name|Map
operator|.
name|Entry
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
name|event
operator|.
name|EventManager
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
name|ConfigurationResolveReport
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

begin_class
specifier|public
class|class
name|ResolveData
block|{
specifier|private
name|ResolveEngine
name|engine
decl_stmt|;
specifier|private
name|Map
name|visitData
decl_stmt|;
comment|// shared map of all visit data: Map (ModuleRevisionId -> VisitData)
specifier|private
name|ConfigurationResolveReport
name|report
decl_stmt|;
specifier|private
name|ResolveOptions
name|options
decl_stmt|;
specifier|private
name|VisitNode
name|currentVisitNode
init|=
literal|null
decl_stmt|;
specifier|private
name|ResolvedModuleRevision
name|currentResolvedModuleRevision
decl_stmt|;
specifier|public
name|ResolveData
parameter_list|(
name|ResolveData
name|data
parameter_list|,
name|boolean
name|validate
parameter_list|)
block|{
name|this
argument_list|(
name|data
operator|.
name|engine
argument_list|,
operator|new
name|ResolveOptions
argument_list|(
name|data
operator|.
name|options
argument_list|)
operator|.
name|setValidate
argument_list|(
name|validate
argument_list|)
argument_list|,
name|data
operator|.
name|report
argument_list|,
name|data
operator|.
name|visitData
argument_list|)
expr_stmt|;
name|setCurrentVisitNode
argument_list|(
name|data
operator|.
name|currentVisitNode
argument_list|)
expr_stmt|;
name|setCurrentResolvedModuleRevision
argument_list|(
name|data
operator|.
name|currentResolvedModuleRevision
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolveData
parameter_list|(
name|ResolveEngine
name|engine
parameter_list|,
name|ResolveOptions
name|options
parameter_list|)
block|{
name|this
argument_list|(
name|engine
argument_list|,
name|options
argument_list|,
literal|null
argument_list|,
operator|new
name|LinkedHashMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolveData
parameter_list|(
name|ResolveEngine
name|engine
parameter_list|,
name|ResolveOptions
name|options
parameter_list|,
name|ConfigurationResolveReport
name|report
parameter_list|)
block|{
name|this
argument_list|(
name|engine
argument_list|,
name|options
argument_list|,
name|report
argument_list|,
operator|new
name|LinkedHashMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolveData
parameter_list|(
name|ResolveEngine
name|engine
parameter_list|,
name|ResolveOptions
name|options
parameter_list|,
name|ConfigurationResolveReport
name|report
parameter_list|,
name|Map
name|visitData
parameter_list|)
block|{
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
name|this
operator|.
name|report
operator|=
name|report
expr_stmt|;
name|this
operator|.
name|visitData
operator|=
name|visitData
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
specifier|public
name|ConfigurationResolveReport
name|getReport
parameter_list|()
block|{
return|return
name|report
return|;
block|}
specifier|public
name|IvyNode
name|getNode
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|VisitData
name|visitData
init|=
name|getVisitData
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
return|return
name|visitData
operator|==
literal|null
condition|?
literal|null
else|:
name|visitData
operator|.
name|getNode
argument_list|()
return|;
block|}
specifier|public
name|Collection
name|getNodes
parameter_list|()
block|{
name|Collection
name|nodes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|visitData
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
name|VisitData
name|vdata
init|=
operator|(
name|VisitData
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|nodes
operator|.
name|add
argument_list|(
name|vdata
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|nodes
return|;
block|}
specifier|public
name|Collection
name|getNodeIds
parameter_list|()
block|{
return|return
name|visitData
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|VisitData
name|getVisitData
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
operator|(
name|VisitData
operator|)
name|visitData
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
return|;
block|}
comment|/**      * Returns the VisitNode currently visited, or<code>null</code> if there is no node currently      * visited in this context.      *       * @return the VisitNode currently visited      */
specifier|public
name|VisitNode
name|getCurrentVisitNode
parameter_list|()
block|{
return|return
name|currentVisitNode
return|;
block|}
comment|/**      * Sets the currently visited node.       * WARNING: This should only be called by Ivy core ResolveEngine!      *       * @param currentVisitNode      */
name|void
name|setCurrentVisitNode
parameter_list|(
name|VisitNode
name|currentVisitNode
parameter_list|)
block|{
name|this
operator|.
name|currentVisitNode
operator|=
name|currentVisitNode
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|VisitNode
name|node
parameter_list|)
block|{
name|register
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|VisitNode
name|node
parameter_list|)
block|{
name|VisitData
name|visitData
init|=
name|getVisitData
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|visitData
operator|==
literal|null
condition|)
block|{
name|visitData
operator|=
operator|new
name|VisitData
argument_list|(
name|node
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|visitData
operator|.
name|addVisitNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|this
operator|.
name|visitData
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|visitData
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|visitData
operator|.
name|setNode
argument_list|(
name|node
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|visitData
operator|.
name|addVisitNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates the visit data currently associated with the given mrid with the given node and the      * visit nodes of the old visitData for the given rootModuleConf      *       * @param mrid      *            the module revision id for which the update should be done      * @param node      *            the IvyNode to associate with the visit data to update      * @param rootModuleConf      *            the root module configuration in which the update is made      */
name|void
name|replaceNode
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|IvyNode
name|node
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
name|VisitData
name|visitData
init|=
name|getVisitData
argument_list|(
name|mrid
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
literal|"impossible to replace node for id "
operator|+
name|mrid
operator|+
literal|". No registered node found."
argument_list|)
throw|;
block|}
name|VisitData
name|keptVisitData
init|=
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
name|keptVisitData
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"impossible to replace node with "
operator|+
name|node
operator|+
literal|". No registered node found for "
operator|+
name|node
operator|.
name|getId
argument_list|()
operator|+
literal|"."
argument_list|)
throw|;
block|}
comment|// replace visit data in Map (discards old one)
name|this
operator|.
name|visitData
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|keptVisitData
argument_list|)
expr_stmt|;
comment|// update visit data with discarde visit nodes
name|keptVisitData
operator|.
name|addVisitNodes
argument_list|(
name|rootModuleConf
argument_list|,
name|visitData
operator|.
name|getVisitNodes
argument_list|(
name|rootModuleConf
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|updateDependency
argument_list|(
name|mrid
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setReport
parameter_list|(
name|ConfigurationResolveReport
name|report
parameter_list|)
block|{
name|this
operator|.
name|report
operator|=
name|report
expr_stmt|;
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|options
operator|.
name|getDate
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|options
operator|.
name|isValidate
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|options
operator|.
name|isTransitive
argument_list|()
return|;
block|}
specifier|public
name|ResolveOptions
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
specifier|public
name|ResolveEngineSettings
name|getSettings
parameter_list|()
block|{
return|return
name|engine
operator|.
name|getSettings
argument_list|()
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|engine
operator|.
name|getEventManager
argument_list|()
return|;
block|}
specifier|public
name|ResolveEngine
name|getEngine
parameter_list|()
block|{
return|return
name|engine
return|;
block|}
name|void
name|blacklist
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|visitData
operator|.
name|entrySet
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
name|Entry
name|entry
init|=
operator|(
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|VisitData
name|vdata
init|=
operator|(
name|VisitData
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|vdata
operator|.
name|getNode
argument_list|()
operator|==
name|node
operator|&&
operator|!
name|node
operator|.
name|getResolvedId
argument_list|()
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
comment|// this visit data was associated with the blacklisted node,
comment|// we discard this association
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isBlacklisted
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|IvyNode
name|node
init|=
name|getNode
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
return|return
name|node
operator|!=
literal|null
operator|&&
name|node
operator|.
name|isBlacklisted
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|public
name|DependencyDescriptor
name|mediate
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
name|VisitNode
name|current
init|=
name|getCurrentVisitNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
comment|// mediating dd through dependers stack
name|DependencyDescriptor
name|originalDD
init|=
name|dd
decl_stmt|;
name|List
name|dependers
init|=
operator|new
name|ArrayList
argument_list|(
name|current
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
comment|// the returned path contains the currently visited node, we are only interested in
comment|// the dependers, so we remove the currently visted node from the end
name|dependers
operator|.
name|remove
argument_list|(
name|dependers
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// we want to apply mediation going up in the dependers stack, not the opposite
name|Collections
operator|.
name|reverse
argument_list|(
name|dependers
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|dependers
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
name|VisitNode
name|n
init|=
operator|(
name|VisitNode
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
name|n
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|md
operator|!=
literal|null
condition|)
block|{
name|dd
operator|=
name|md
operator|.
name|mediate
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|originalDD
operator|!=
name|dd
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"dependency descriptor has been mediated: "
operator|+
name|originalDD
operator|+
literal|" => "
operator|+
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|getEngine
argument_list|()
operator|.
name|mediate
argument_list|(
name|dd
argument_list|,
name|getOptions
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets the last {@link ResolvedModuleRevision} which has been currently resolved.      *<p>      * This can be used especially in dependency resolvers, to know if another dependency resolver      * has already resolved the requested dependency, to take a decision if the resolver should try      * to resolve it by itself or not. Indeed, the dependency resolver is responsible for taking      * this decision, even when included in a chain. The chain responsibility is only to set this      * current resolved module revision to enable the resolver to take the decision.      *</p>      *       * @param mr      *            the last {@link ResolvedModuleRevision} which has been currently resolved.      */
specifier|public
name|void
name|setCurrentResolvedModuleRevision
parameter_list|(
name|ResolvedModuleRevision
name|mr
parameter_list|)
block|{
name|this
operator|.
name|currentResolvedModuleRevision
operator|=
name|mr
expr_stmt|;
block|}
comment|/**      * Returns the last {@link ResolvedModuleRevision} which has been currently resolved.      *<p>      * It can be<code>null</code>.      *</p>      *       * @return the last {@link ResolvedModuleRevision} which has been currently resolved.      */
specifier|public
name|ResolvedModuleRevision
name|getCurrentResolvedModuleRevision
parameter_list|()
block|{
return|return
name|currentResolvedModuleRevision
return|;
block|}
block|}
end_class

end_unit

