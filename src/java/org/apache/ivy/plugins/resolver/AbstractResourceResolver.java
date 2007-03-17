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
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * @author Xavier Hanin  *  */
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
name|_ivyPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (String pattern)
specifier|private
name|List
name|_artifactPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (String pattern)
specifier|private
name|boolean
name|_m2compatible
init|=
literal|false
decl_stmt|;
specifier|public
name|AbstractResourceResolver
parameter_list|()
block|{
block|}
specifier|protected
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
name|_ivyPatterns
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
name|_artifactPatterns
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
name|ResolvedResource
name|rres
init|=
literal|null
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
name|rres
operator|==
literal|null
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
name|rres
operator|=
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
expr_stmt|;
block|}
return|return
name|rres
return|;
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
comment|/**      * Output message to log indicating what have been done to look for an artifact which      * has finally not been found      *       * @param artifact the artifact which has not been found      */
specifier|protected
name|void
name|logIvyNotFound
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
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
name|Artifact
name|artifact
init|=
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|mrid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|logMdNotFound
argument_list|(
name|mrid
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|logMdNotFound
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|String
name|revisionToken
init|=
name|mrid
operator|.
name|getRevision
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"latest."
argument_list|)
condition|?
literal|"[any "
operator|+
name|mrid
operator|.
name|getRevision
argument_list|()
operator|.
name|substring
argument_list|(
literal|"latest."
operator|.
name|length
argument_list|()
argument_list|)
operator|+
literal|"]"
else|:
literal|"["
operator|+
name|mrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|"]"
decl_stmt|;
name|Artifact
name|latestArtifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|revisionToken
argument_list|)
argument_list|,
literal|null
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|_ivyPatterns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logIvyAttempt
argument_list|(
literal|"no ivy pattern => no attempt to find module descriptor file for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_ivyPatterns
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
name|String
name|resolvedFileName
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|logIvyAttempt
argument_list|(
name|resolvedFileName
argument_list|)
expr_stmt|;
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
name|mrid
argument_list|)
condition|)
block|{
name|resolvedFileName
operator|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|latestArtifact
argument_list|)
expr_stmt|;
name|logIvyAttempt
argument_list|(
name|resolvedFileName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Output message to log indicating what have been done to look for an artifact which      * has finally not been found      *       * @param artifact the artifact which has not been found      */
specifier|protected
name|void
name|logArtifactNotFound
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
name|_artifactPatterns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|artifact
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|logArtifactAttempt
argument_list|(
name|artifact
argument_list|,
literal|"no artifact pattern => no attempt to find artifact "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
name|Artifact
name|used
init|=
name|artifact
decl_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|used
operator|=
name|DefaultArtifact
operator|.
name|cloneWithAnotherMrid
argument_list|(
name|artifact
argument_list|,
name|convertM2IdForResourceSearch
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|_artifactPatterns
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
name|String
name|resolvedFileName
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|used
argument_list|)
decl_stmt|;
name|logArtifactAttempt
argument_list|(
name|artifact
argument_list|,
name|resolvedFileName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|used
operator|.
name|getUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|logArtifactAttempt
argument_list|(
name|artifact
argument_list|,
name|used
operator|.
name|getUrl
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// should be overridden by subclasses wanting to have listing features
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
block|}
comment|/**      * example of pattern : ~/Workspace/[module]/[module].ivy.xml      * @param pattern      */
specifier|public
name|void
name|addIvyPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|_ivyPatterns
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
name|_artifactPatterns
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
name|_ivyPatterns
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
name|_artifactPatterns
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setIvyPatterns
parameter_list|(
name|List
name|ivyPatterns
parameter_list|)
block|{
name|_ivyPatterns
operator|=
name|ivyPatterns
expr_stmt|;
block|}
specifier|protected
name|void
name|setArtifactPatterns
parameter_list|(
name|List
name|artifactPatterns
parameter_list|)
block|{
name|_artifactPatterns
operator|=
name|artifactPatterns
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
name|_ivyPatterns
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
name|_artifactPatterns
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
name|_m2compatible
return|;
block|}
specifier|public
name|void
name|setM2compatible
parameter_list|(
name|boolean
name|m2compatible
parameter_list|)
block|{
name|_m2compatible
operator|=
name|m2compatible
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
name|getExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

