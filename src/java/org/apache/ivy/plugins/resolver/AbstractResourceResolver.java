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
name|Date
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|IvyPatternHelper
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
name|DefaultArtifact
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
name|settings
operator|.
name|IvyPattern
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
name|plugins
operator|.
name|matcher
operator|.
name|Matcher
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
name|resolver
operator|.
name|util
operator|.
name|MDResolvedResource
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
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
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
name|resolver
operator|.
name|util
operator|.
name|ResourceMDParser
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
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractResourceResolver
extends|extends
name|BasicResolver
block|{
specifier|private
specifier|static
specifier|final
name|Map
name|IVY_ARTIFACT_ATTRIBUTES
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|IVY_ARTIFACT_ATTRIBUTES
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ARTIFACT_KEY
argument_list|,
literal|"ivy"
argument_list|)
expr_stmt|;
name|IVY_ARTIFACT_ATTRIBUTES
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
literal|"ivy"
argument_list|)
expr_stmt|;
name|IVY_ARTIFACT_ATTRIBUTES
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
name|ivyPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (String pattern)
specifier|private
name|List
name|artifactPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (String pattern)
specifier|private
name|boolean
name|m2compatible
init|=
literal|false
decl_stmt|;
specifier|public
name|AbstractResourceResolver
parameter_list|()
block|{
block|}
specifier|public
name|ResolvedResource
name|findIvyFileRef
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|mrid
operator|=
name|convertM2IdForResourceSearch
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
return|return
name|findResourceUsingPatterns
argument_list|(
name|mrid
argument_list|,
name|ivyPatterns
argument_list|,
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|mrid
argument_list|,
name|data
operator|.
name|getDate
argument_list|()
argument_list|)
argument_list|,
name|getRMDParser
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
argument_list|,
name|data
operator|.
name|getDate
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ResolvedResource
name|findArtifactRef
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|mrid
operator|=
name|convertM2IdForResourceSearch
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
return|return
name|findResourceUsingPatterns
argument_list|(
name|mrid
argument_list|,
name|artifactPatterns
argument_list|,
name|artifact
argument_list|,
name|getDefaultRMDParser
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
argument_list|,
name|date
argument_list|)
return|;
block|}
specifier|protected
name|ResolvedResource
name|findResourceUsingPatterns
parameter_list|(
name|ModuleRevisionId
name|moduleRevision
parameter_list|,
name|List
name|patternList
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ResourceMDParser
name|rmdparser
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|List
name|resolvedResources
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Set
name|foundRevisions
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|boolean
name|dynamic
init|=
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
operator|.
name|isDynamic
argument_list|(
name|moduleRevision
argument_list|)
decl_stmt|;
name|boolean
name|stop
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|patternList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|stop
condition|;
control|)
block|{
name|String
name|pattern
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ResolvedResource
name|rres
init|=
name|findResourceUsingPattern
argument_list|(
name|moduleRevision
argument_list|,
name|pattern
argument_list|,
name|artifact
argument_list|,
name|rmdparser
argument_list|,
name|date
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|rres
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|foundRevisions
operator|.
name|contains
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
comment|// only add the first found ResolvedResource for each revision
name|foundRevisions
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|resolvedResources
operator|.
name|add
argument_list|(
name|rres
argument_list|)
expr_stmt|;
name|stop
operator|=
operator|!
name|dynamic
expr_stmt|;
comment|// stop iterating if we are not searching a dynamic revision
block|}
block|}
if|if
condition|(
name|resolvedResources
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|ResolvedResource
index|[]
name|rress
init|=
operator|(
name|ResolvedResource
index|[]
operator|)
name|resolvedResources
operator|.
name|toArray
argument_list|(
operator|new
name|ResolvedResource
index|[
name|resolvedResources
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
return|return
name|findResource
argument_list|(
name|rress
argument_list|,
name|rmdparser
argument_list|,
name|moduleRevision
argument_list|,
name|date
argument_list|)
return|;
block|}
if|else if
condition|(
name|resolvedResources
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
operator|(
name|ResolvedResource
operator|)
name|resolvedResources
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|abstract
name|ResolvedResource
name|findResourceUsingPattern
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ResourceMDParser
name|rmdparser
parameter_list|,
name|Date
name|date
parameter_list|)
function_decl|;
specifier|public
name|ResolvedResource
name|findResource
parameter_list|(
name|ResolvedResource
index|[]
name|rress
parameter_list|,
name|ResourceMDParser
name|rmdparser
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
name|VersionMatcher
name|versionMatcher
init|=
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
name|ResolvedResource
name|found
init|=
literal|null
decl_stmt|;
name|List
name|sorted
init|=
name|getLatestStrategy
argument_list|()
operator|.
name|sort
argument_list|(
name|rress
argument_list|)
decl_stmt|;
name|List
name|rejected
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|foundBlacklisted
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|IvyContext
name|context
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|sorted
operator|.
name|listIterator
argument_list|(
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
init|;
name|iter
operator|.
name|hasPrevious
argument_list|()
condition|;
control|)
block|{
name|ResolvedResource
name|rres
init|=
operator|(
name|ResolvedResource
operator|)
name|iter
operator|.
name|previous
argument_list|()
decl_stmt|;
if|if
condition|(
name|filterNames
argument_list|(
operator|new
name|ArrayList
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": filtered by name: "
operator|+
name|rres
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|(
name|date
operator|!=
literal|null
operator|&&
name|rres
operator|.
name|getLastModified
argument_list|()
operator|>
name|date
operator|.
name|getTime
argument_list|()
operator|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": too young: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
operator|+
literal|" ("
operator|+
name|rres
operator|.
name|getLastModified
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|ModuleRevisionId
name|foundMrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
name|ResolveData
name|data
init|=
name|context
operator|.
name|getResolveData
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
operator|&&
name|data
operator|.
name|getReport
argument_list|()
operator|!=
literal|null
operator|&&
name|data
operator|.
name|isBlacklisted
argument_list|(
name|data
operator|.
name|getReport
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|foundMrid
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": blacklisted: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
operator|+
literal|" (blacklisted)"
argument_list|)
expr_stmt|;
name|foundBlacklisted
operator|.
name|add
argument_list|(
name|foundMrid
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|versionMatcher
operator|.
name|accept
argument_list|(
name|mrid
argument_list|,
name|foundMrid
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": rejected by version matcher: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|versionMatcher
operator|.
name|needModuleDescriptor
argument_list|(
name|mrid
argument_list|,
name|foundMrid
argument_list|)
condition|)
block|{
name|ResolvedResource
name|r
init|=
name|rmdparser
operator|.
name|parse
argument_list|(
name|rres
operator|.
name|getResource
argument_list|()
argument_list|,
name|rres
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": impossible to get module descriptor resource: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
operator|+
literal|" (no or bad MD)"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|ModuleDescriptor
name|md
init|=
operator|(
operator|(
name|MDResolvedResource
operator|)
name|r
operator|)
operator|.
name|getResolvedModuleRevision
argument_list|()
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|md
operator|.
name|isDefault
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": default md rejected by version matcher"
operator|+
literal|"requiring module descriptor: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
operator|+
literal|" (MD)"
argument_list|)
expr_stmt|;
continue|continue;
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
name|md
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": md rejected by version matcher: "
operator|+
name|rres
argument_list|)
expr_stmt|;
name|rejected
operator|.
name|add
argument_list|(
name|rres
operator|.
name|getRevision
argument_list|()
operator|+
literal|" (MD)"
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|found
operator|=
name|r
expr_stmt|;
block|}
block|}
else|else
block|{
name|found
operator|=
name|rres
expr_stmt|;
block|}
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|found
operator|.
name|getResource
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": resource not reachable for "
operator|+
name|mrid
operator|+
literal|": res="
operator|+
name|found
operator|.
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
name|logAttempt
argument_list|(
name|found
operator|.
name|getResource
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|found
operator|==
literal|null
operator|&&
operator|!
name|rejected
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logAttempt
argument_list|(
name|rejected
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|found
operator|==
literal|null
operator|&&
operator|!
name|foundBlacklisted
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// all acceptable versions have been blacklisted, this means that an unsolvable conflict
comment|// has been found
name|DependencyDescriptor
name|dd
init|=
name|context
operator|.
name|getDependencyDescriptor
argument_list|()
decl_stmt|;
name|IvyNode
name|parentNode
init|=
name|context
operator|.
name|getResolveData
argument_list|()
operator|.
name|getNode
argument_list|(
name|dd
operator|.
name|getParentRevisionId
argument_list|()
argument_list|)
decl_stmt|;
name|ConflictManager
name|cm
init|=
name|parentNode
operator|.
name|getConflictManager
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|handleAllBlacklistedRevisions
argument_list|(
name|dd
argument_list|,
name|foundBlacklisted
argument_list|)
expr_stmt|;
block|}
return|return
name|found
return|;
block|}
specifier|protected
name|Collection
name|findNames
parameter_list|(
name|Map
name|tokenValues
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|Collection
name|names
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|names
operator|.
name|addAll
argument_list|(
name|findIvyNames
argument_list|(
name|tokenValues
argument_list|,
name|token
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAllownomd
argument_list|()
condition|)
block|{
name|names
operator|.
name|addAll
argument_list|(
name|findArtifactNames
argument_list|(
name|tokenValues
argument_list|,
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
specifier|protected
name|Collection
name|findIvyNames
parameter_list|(
name|Map
name|tokenValues
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|Collection
name|names
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|tokenValues
operator|=
operator|new
name|HashMap
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ARTIFACT_KEY
argument_list|,
literal|"ivy"
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
literal|"ivy"
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
name|findTokenValues
argument_list|(
name|names
argument_list|,
name|getIvyPatterns
argument_list|()
argument_list|,
name|tokenValues
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|filterNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
return|return
name|names
return|;
block|}
specifier|protected
name|Collection
name|findArtifactNames
parameter_list|(
name|Map
name|tokenValues
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|Collection
name|names
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|tokenValues
operator|=
operator|new
name|HashMap
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ARTIFACT_KEY
argument_list|,
name|tokenValues
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|findTokenValues
argument_list|(
name|names
argument_list|,
name|getArtifactPatterns
argument_list|()
argument_list|,
name|tokenValues
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|filterNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
return|return
name|names
return|;
block|}
specifier|public
name|Map
index|[]
name|listTokenValues
parameter_list|(
name|String
index|[]
name|tokens
parameter_list|,
name|Map
name|criteria
parameter_list|)
block|{
name|Set
name|result
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|// use ivy patterns
name|List
name|ivyPatterns
init|=
name|getIvyPatterns
argument_list|()
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|(
name|criteria
argument_list|)
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
literal|"ivy"
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|ivyPatterns
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|ivyPattern
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|resolveTokenValues
argument_list|(
name|tokens
argument_list|,
name|ivyPattern
argument_list|,
name|tokenValues
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isAllownomd
argument_list|()
condition|)
block|{
name|List
name|artifactPatterns
init|=
name|getArtifactPatterns
argument_list|()
decl_stmt|;
name|tokenValues
operator|=
operator|new
name|HashMap
argument_list|(
name|criteria
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|artifactPatterns
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|artifactPattern
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|resolveTokenValues
argument_list|(
name|tokens
argument_list|,
name|artifactPattern
argument_list|,
name|tokenValues
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|Map
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|Map
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|Set
name|resolveTokenValues
parameter_list|(
name|String
index|[]
name|tokens
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Map
name|criteria
parameter_list|,
name|boolean
name|noMd
parameter_list|)
block|{
name|Set
name|result
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Set
name|tokenSet
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|tokens
argument_list|)
argument_list|)
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|criteria
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|tokenValues
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tokenSet
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no more tokens to resolve
name|result
operator|.
name|add
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
name|String
name|partiallyResolvedPattern
init|=
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
name|pattern
argument_list|,
name|tokenValues
argument_list|)
decl_stmt|;
name|String
name|token
init|=
name|IvyPatternHelper
operator|.
name|getFirstToken
argument_list|(
name|partiallyResolvedPattern
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|token
operator|==
literal|null
operator|)
operator|&&
name|exist
argument_list|(
name|partiallyResolvedPattern
argument_list|)
condition|)
block|{
comment|// no more tokens to resolve
name|result
operator|.
name|add
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
name|tokenSet
operator|.
name|remove
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
literal|null
decl_stmt|;
name|Object
name|criteriaForToken
init|=
name|criteria
operator|.
name|get
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|criteriaForToken
operator|instanceof
name|Matcher
condition|)
block|{
name|matcher
operator|=
operator|(
name|Matcher
operator|)
name|criteriaForToken
expr_stmt|;
block|}
name|String
index|[]
name|values
init|=
name|listTokenValues
argument_list|(
name|partiallyResolvedPattern
argument_list|,
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return
name|result
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
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|(
name|matcher
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|matcher
operator|.
name|matches
argument_list|(
name|values
index|[
name|i
index|]
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|tokenValues
operator|.
name|put
argument_list|(
name|token
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|String
name|moreResolvedPattern
init|=
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
name|partiallyResolvedPattern
argument_list|,
name|tokenValues
argument_list|)
decl_stmt|;
name|Map
name|newCriteria
init|=
operator|new
name|HashMap
argument_list|(
name|criteria
argument_list|)
decl_stmt|;
name|newCriteria
operator|.
name|put
argument_list|(
name|token
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|noMd
operator|&&
literal|"artifact"
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|newCriteria
operator|.
name|put
argument_list|(
literal|"module"
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|noMd
operator|&&
literal|"module"
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|newCriteria
operator|.
name|put
argument_list|(
literal|"artifact"
argument_list|,
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|addAll
argument_list|(
name|resolveTokenValues
argument_list|(
operator|(
name|String
index|[]
operator|)
name|tokenSet
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|tokenSet
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|moreResolvedPattern
argument_list|,
name|newCriteria
argument_list|,
name|noMd
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
specifier|abstract
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|token
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|boolean
name|exist
parameter_list|(
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Filters names before returning them in the findXXXNames or findTokenValues method.      *<p>      * Remember to call the super implementation when overriding this method.      *</p>      *       * @param names      *            the list to filter.      * @return the filtered list      */
specifier|protected
name|Collection
name|filterNames
parameter_list|(
name|Collection
name|names
parameter_list|)
block|{
name|getSettings
argument_list|()
operator|.
name|filterIgnore
argument_list|(
name|names
argument_list|)
expr_stmt|;
return|return
name|names
return|;
block|}
specifier|protected
name|void
name|findTokenValues
parameter_list|(
name|Collection
name|names
parameter_list|,
name|List
name|patterns
parameter_list|,
name|Map
name|tokenValues
parameter_list|,
name|String
name|token
parameter_list|)
block|{
comment|//to be overridden by subclasses wanting to have listing features
block|}
comment|/**      * example of pattern : ~/Workspace/[module]/[module].ivy.xml      *       * @param pattern      */
specifier|public
name|void
name|addIvyPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ivyPatterns
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addArtifactPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|artifactPatterns
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getIvyPatterns
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|ivyPatterns
argument_list|)
return|;
block|}
specifier|public
name|List
name|getArtifactPatterns
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|artifactPatterns
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setIvyPatterns
parameter_list|(
name|List
name|patterns
parameter_list|)
block|{
name|ivyPatterns
operator|=
name|patterns
expr_stmt|;
block|}
specifier|protected
name|void
name|setArtifactPatterns
parameter_list|(
name|List
name|patterns
parameter_list|)
block|{
name|artifactPatterns
operator|=
name|patterns
expr_stmt|;
block|}
comment|/*      * Methods respecting ivy conf method specifications      */
specifier|public
name|void
name|addConfiguredIvy
parameter_list|(
name|IvyPattern
name|p
parameter_list|)
block|{
name|ivyPatterns
operator|.
name|add
argument_list|(
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredArtifact
parameter_list|(
name|IvyPattern
name|p
parameter_list|)
block|{
name|artifactPatterns
operator|.
name|add
argument_list|(
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dumpSettings
parameter_list|()
block|{
name|super
operator|.
name|dumpSettings
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tm2compatible: "
operator|+
name|isM2compatible
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tivy patterns:"
argument_list|)
expr_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|getIvyPatterns
argument_list|()
operator|.
name|listIterator
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
name|p
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\t\t"
operator|+
name|p
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tartifact patterns:"
argument_list|)
expr_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|getArtifactPatterns
argument_list|()
operator|.
name|listIterator
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
name|p
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\t\t"
operator|+
name|p
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isM2compatible
parameter_list|()
block|{
return|return
name|m2compatible
return|;
block|}
specifier|public
name|void
name|setM2compatible
parameter_list|(
name|boolean
name|compatible
parameter_list|)
block|{
name|m2compatible
operator|=
name|compatible
expr_stmt|;
block|}
specifier|protected
name|ModuleRevisionId
name|convertM2IdForResourceSearch
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
if|if
condition|(
name|mrid
operator|.
name|getOrganisation
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|mrid
return|;
block|}
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
operator|.
name|getOrganisation
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
name|mrid
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

