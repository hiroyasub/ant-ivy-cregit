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
name|List
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
name|plugins
operator|.
name|latest
operator|.
name|ArtifactInfo
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
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|LatestConflictManager
extends|extends
name|AbstractConflictManager
block|{
specifier|public
specifier|static
class|class
name|NoConflictResolvedYetException
extends|extends
name|RuntimeException
block|{     }
specifier|protected
specifier|static
specifier|final
class|class
name|IvyNodeArtifactInfo
implements|implements
name|ArtifactInfo
block|{
specifier|private
specifier|final
name|IvyNode
name|node
decl_stmt|;
specifier|private
name|IvyNodeArtifactInfo
parameter_list|(
name|IvyNode
name|dep
parameter_list|)
block|{
name|node
operator|=
name|dep
expr_stmt|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
name|long
name|lastModified
init|=
name|node
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastModified
operator|==
literal|0
condition|)
block|{
comment|// if the last modified timestamp is unknown, we can't resolve
comment|// the conflicts now, and trigger an exception which will be catched
comment|// in the main resolveConflicts method
throw|throw
operator|new
name|NoConflictResolvedYetException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|lastModified
return|;
block|}
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|node
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
return|;
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
block|}
specifier|private
name|LatestStrategy
name|strategy
decl_stmt|;
specifier|private
name|String
name|strategyName
decl_stmt|;
specifier|public
name|LatestConflictManager
parameter_list|()
block|{
block|}
specifier|public
name|LatestConflictManager
parameter_list|(
name|LatestStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
specifier|public
name|LatestConflictManager
parameter_list|(
name|String
name|name
parameter_list|,
name|LatestStrategy
name|strategy
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
argument_list|<
name|IvyNode
argument_list|>
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
for|for
control|(
name|IvyNode
name|node
range|:
name|conflicts
control|)
block|{
name|DependencyDescriptor
name|dd
init|=
name|node
operator|.
name|getDependencyDescriptor
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|dd
operator|!=
literal|null
operator|&&
name|dd
operator|.
name|isForce
argument_list|()
operator|&&
name|parent
operator|.
name|getResolvedId
argument_list|()
operator|.
name|equals
argument_list|(
name|dd
operator|.
name|getParentRevisionId
argument_list|()
argument_list|)
condition|)
block|{
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
comment|/*          * If the list of conflicts contains dynamic revisions, delay the conflict calculation until          * they are resolved. TODO: we probably could already evict some of the dynamic revisions!          */
for|for
control|(
name|IvyNode
name|node
range|:
name|conflicts
control|)
block|{
name|ModuleRevisionId
name|modRev
init|=
name|node
operator|.
name|getResolvedId
argument_list|()
decl_stmt|;
if|if
condition|(
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
operator|.
name|isDynamic
argument_list|(
name|modRev
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
name|ArrayList
argument_list|<
name|IvyNode
argument_list|>
name|unevicted
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyNode
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|node
range|:
name|conflicts
control|)
block|{
if|if
condition|(
operator|!
name|node
operator|.
name|isCompletelyEvicted
argument_list|()
condition|)
name|unevicted
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unevicted
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|conflicts
operator|=
name|unevicted
expr_stmt|;
block|}
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
name|conflicts
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
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|latest
operator|.
name|getNode
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|conflicts
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
comment|// according to the resolveConflicts contract, we must return null
return|return
literal|null
return|;
block|}
block|}
specifier|protected
name|ArtifactInfo
index|[]
name|toArtifactInfo
parameter_list|(
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|conflicts
parameter_list|)
block|{
name|List
argument_list|<
name|ArtifactInfo
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactInfo
argument_list|>
argument_list|(
name|conflicts
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|IvyNode
name|node
range|:
name|conflicts
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|IvyNodeArtifactInfo
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactInfo
index|[
name|artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|LatestStrategy
name|getStrategy
parameter_list|()
block|{
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|strategyName
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|=
name|getSettings
argument_list|()
operator|.
name|getLatestStrategy
argument_list|(
name|strategyName
argument_list|)
expr_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"unknown latest strategy: "
operator|+
name|strategyName
argument_list|)
expr_stmt|;
name|strategy
operator|=
name|getSettings
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|strategy
operator|=
name|getSettings
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|strategy
return|;
block|}
comment|/**      * To conform to configurator API      *       * @param latestStrategy      */
specifier|public
name|void
name|setLatest
parameter_list|(
name|String
name|strategyName
parameter_list|)
block|{
name|this
operator|.
name|strategyName
operator|=
name|strategyName
expr_stmt|;
block|}
specifier|public
name|void
name|setStrategy
parameter_list|(
name|LatestStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|strategy
operator|!=
literal|null
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|strategy
argument_list|)
else|:
name|strategyName
return|;
block|}
block|}
end_class

end_unit

