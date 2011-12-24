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
name|LinkedHashSet
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
name|AbstractPatternsBasedResolver
extends|extends
name|BasicResolver
block|{
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
name|AbstractPatternsBasedResolver
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
specifier|public
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
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
comment|// convert 'M2'-organisation back to 'Ivy'-organisation
name|mrid
operator|=
name|convertM2ResourceSearchIdToNormal
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|findResource
argument_list|(
name|rress
argument_list|,
name|rmdparser
argument_list|,
name|mrid
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
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|convertM2TokenValuesForResourceSearch
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|convertM2TokenValuesForResourceSearch
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
block|}
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
name|LinkedHashSet
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
name|getModuleDescriptorExtension
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|convertM2TokenValuesForResourceSearch
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|convertM2TokenValuesForResourceSearch
argument_list|(
name|tokenValues
argument_list|)
expr_stmt|;
block|}
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
specifier|protected
name|String
name|getModuleDescriptorExtension
parameter_list|()
block|{
return|return
literal|"xml"
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
name|LinkedHashSet
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
name|List
name|vals
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
decl_stmt|;
name|filterNames
argument_list|(
name|vals
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|vals
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
name|value
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|value
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
name|value
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
name|value
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
name|value
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
name|value
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
name|convertM2ResourceSearchIdToNormal
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
operator|==
literal|null
operator|||
name|mrid
operator|.
name|getOrganisation
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'/'
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
literal|'/'
argument_list|,
literal|'.'
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
operator|==
literal|null
operator|||
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
specifier|protected
name|String
name|convertM2OrganizationForResourceSearch
parameter_list|(
name|String
name|org
parameter_list|)
block|{
return|return
name|org
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
specifier|protected
name|void
name|convertM2TokenValuesForResourceSearch
parameter_list|(
name|Map
name|tokenValues
parameter_list|)
block|{
if|if
condition|(
name|tokenValues
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
operator|instanceof
name|String
condition|)
block|{
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|convertM2OrganizationForResourceSearch
argument_list|(
operator|(
name|String
operator|)
name|tokenValues
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

