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
name|List
name|resolvedResources
init|=
operator|new
name|ArrayList
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
if|if
condition|(
name|rres
operator|!=
literal|null
condition|)
block|{
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
name|rres
operator|=
name|findResource
argument_list|(
name|rress
argument_list|,
name|getName
argument_list|()
argument_list|,
name|getLatestStrategy
argument_list|()
argument_list|,
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
argument_list|,
name|rmdparser
argument_list|,
name|moduleRevision
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
specifier|public
specifier|static
name|ResolvedResource
name|findResource
parameter_list|(
name|ResolvedResource
index|[]
name|rress
parameter_list|,
name|String
name|name
parameter_list|,
name|LatestStrategy
name|strategy
parameter_list|,
name|VersionMatcher
name|versionMatcher
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
name|ResolvedResource
name|found
init|=
literal|null
decl_stmt|;
name|List
name|sorted
init|=
name|strategy
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
literal|": default md rejected by version matcher requiring module descriptor: "
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
return|return
name|found
return|;
block|}
comment|//    /**
comment|//     * Output message to log indicating what have been done to look for an artifact which
comment|//     * has finally not been found
comment|//     *
comment|//     * @param artifact the artifact which has not been found
comment|//     */
comment|//    protected void logIvyNotFound(ModuleRevisionId mrid) {
comment|//        if (isM2compatible()) {
comment|//            mrid = convertM2IdForResourceSearch(mrid);
comment|//        }
comment|//        Artifact artifact = DefaultArtifact.newIvyArtifact(mrid, null);
comment|//        logMdNotFound(mrid, artifact);
comment|//    }
comment|//
comment|//    protected void logMdNotFound(ModuleRevisionId mrid, Artifact artifact) {
comment|//        String revisionToken = mrid.getRevision().startsWith("latest.")?"[any "+mrid.getRevision().substring("latest.".length())+"]":"["+mrid.getRevision()+"]";
comment|//        Artifact latestArtifact = new DefaultArtifact(ModuleRevisionId.newInstance(mrid, revisionToken), null, artifact.getName(), artifact.getType(), artifact.getExt(), artifact.getExtraAttributes());
comment|//        if (_ivyPatterns.isEmpty()) {
comment|//            logIvyAttempt("no ivy pattern => no attempt to find module descriptor file for "+mrid);
comment|//        } else {
comment|//            for (Iterator iter = _ivyPatterns.iterator(); iter.hasNext();) {
comment|//                String pattern = (String)iter.next();
comment|//                String resolvedFileName = IvyPatternHelper.substitute(pattern, artifact);
comment|//                logIvyAttempt(resolvedFileName);
comment|//                if (getSettings().getVersionMatcher().isDynamic(mrid)) {
comment|//                    resolvedFileName = IvyPatternHelper.substitute(pattern, latestArtifact);
comment|//                    logIvyAttempt(resolvedFileName);
comment|//                }
comment|//            }
comment|//        }
comment|//    }
comment|//    /**
comment|//     * Output message to log indicating what have been done to look for an artifact which
comment|//     * has finally not been found
comment|//     *
comment|//     * @param artifact the artifact which has not been found
comment|//     */
comment|//    protected void logArtifactNotFound(Artifact artifact) {
comment|//        if (_artifactPatterns.isEmpty()) {
comment|//        	if (artifact.getUrl() == null) {
comment|//        		logArtifactAttempt(artifact, "no artifact pattern => no attempt to find artifact "+artifact);
comment|//        	}
comment|//        }
comment|//        Artifact used = artifact;
comment|//        if (isM2compatible()) {
comment|//        	used = DefaultArtifact.cloneWithAnotherMrid(artifact, convertM2IdForResourceSearch(artifact.getModuleRevisionId()));
comment|//        }
comment|//
comment|//        for (Iterator iter = _artifactPatterns.iterator(); iter.hasNext();) {
comment|//            String pattern = (String)iter.next();
comment|//            String resolvedFileName = IvyPatternHelper.substitute(pattern, used);
comment|//            logArtifactAttempt(artifact, resolvedFileName);
comment|//        }
comment|//    	if (used.getUrl() != null) {
comment|//    		logArtifactAttempt(artifact, used.getUrl().toString());
comment|//    	}
comment|//    }
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

