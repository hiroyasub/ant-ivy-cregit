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
name|report
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|LinkedHashMap
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
name|cache
operator|.
name|ResolutionCacheManager
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
name|ResolveEngine
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
name|ResolveOptions
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
name|sort
operator|.
name|SortOptions
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
name|report
operator|.
name|XmlReportParser
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
comment|/**  * Represents a whole resolution report for a module but for a specific configuration  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationResolveReport
block|{
specifier|private
specifier|final
name|ModuleDescriptor
name|md
decl_stmt|;
specifier|private
specifier|final
name|String
name|conf
decl_stmt|;
specifier|private
specifier|final
name|Date
name|date
decl_stmt|;
specifier|private
specifier|final
name|ResolveOptions
name|options
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|IvyNode
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|>
name|dependencyReports
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|IvyNode
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|IvyNode
argument_list|>
name|dependencies
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|ModuleRevisionId
argument_list|,
name|IvyNode
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|ResolveEngine
name|resolveEngine
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|Collection
argument_list|<
name|IvyNode
argument_list|>
argument_list|>
name|modulesIdsMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|ModuleId
argument_list|,
name|Collection
argument_list|<
name|IvyNode
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ModuleId
argument_list|>
name|modulesIds
decl_stmt|;
specifier|private
name|Boolean
name|hasChanged
init|=
literal|null
decl_stmt|;
specifier|public
name|ConfigurationResolveReport
parameter_list|(
name|ResolveEngine
name|resolveEngine
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|conf
parameter_list|,
name|Date
name|date
parameter_list|,
name|ResolveOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|resolveEngine
operator|=
name|resolveEngine
expr_stmt|;
name|this
operator|.
name|md
operator|=
name|md
expr_stmt|;
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
comment|/**      * Check if the set of dependencies has changed since the previous execution of a resolution.      *<p>      * This function use the report file found in the cache. So the function must be called before      * the new report is serialized there.      *</p>      *<p>      * This function also use the internal dependencies that must already be filled. This function      * might be 'heavy' because it may have to parse the previous report.      *</p>      */
specifier|public
name|void
name|checkIfChanged
parameter_list|()
block|{
name|ResolutionCacheManager
name|cache
init|=
name|resolveEngine
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
name|String
name|resolveId
init|=
name|options
operator|.
name|getResolveId
argument_list|()
decl_stmt|;
name|File
name|previousReportFile
init|=
name|cache
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
name|conf
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousReportFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|previousReportFile
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
name|previousDeps
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|parser
operator|.
name|getDependencyRevisionIds
argument_list|()
argument_list|)
decl_stmt|;
name|HashSet
argument_list|<
name|ModuleRevisionId
argument_list|>
name|previousDepSet
init|=
operator|new
name|HashSet
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|(
name|previousDeps
argument_list|)
decl_stmt|;
name|hasChanged
operator|=
operator|!
name|previousDepSet
operator|.
name|equals
argument_list|(
name|getModuleRevisionIds
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Error while parsing configuration resolve report "
operator|+
name|previousReportFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|hasChanged
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
block|}
else|else
block|{
name|hasChanged
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
block|}
comment|/**      * @pre checkIfChanged has been called.      * @return boolean      */
specifier|public
name|boolean
name|hasChanged
parameter_list|()
block|{
return|return
name|hasChanged
return|;
block|}
comment|/**      * Returns all non evicted and non error dependency mrids The returned set is ordered so that a      * dependency will always be found before their own dependencies      *      * @return all non evicted and non error dependency mrids      */
specifier|public
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|getModuleRevisionIds
parameter_list|()
block|{
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|mrids
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|node
range|:
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|node
operator|.
name|isEvicted
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
operator|&&
operator|!
name|node
operator|.
name|hasProblem
argument_list|()
condition|)
block|{
name|mrids
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
block|}
return|return
name|mrids
return|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|dependencies
operator|.
name|put
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|put
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|dependencyReports
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|Collections
operator|.
expr|<
name|ArtifactDownloadReport
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updateDependency
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|IvyNode
name|node
parameter_list|)
block|{
name|dependencies
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|IvyNode
name|node
parameter_list|,
name|DownloadReport
name|report
parameter_list|)
block|{
name|dependencies
operator|.
name|put
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|put
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|adrs
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|()
decl_stmt|;
name|Artifact
index|[]
name|artifacts
init|=
name|node
operator|.
name|getArtifacts
argument_list|(
name|conf
argument_list|)
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|ArtifactDownloadReport
name|artifactReport
init|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifactReport
operator|!=
literal|null
condition|)
block|{
name|adrs
operator|.
name|add
argument_list|(
name|artifactReport
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no report found for "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
name|dependencyReports
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|adrs
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getConfiguration
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|md
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getUnresolvedDependencies
parameter_list|()
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|unresolved
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
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|node
operator|.
name|hasProblem
argument_list|()
condition|)
block|{
name|unresolved
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|unresolved
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|unresolved
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|IvyNode
argument_list|>
argument_list|(
name|dependencies
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getEvictedNodes
parameter_list|()
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|evicted
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
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|node
operator|.
name|isEvicted
argument_list|(
name|conf
argument_list|)
condition|)
block|{
name|evicted
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|evicted
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|evicted
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|getEvictedMrids
parameter_list|()
block|{
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|evicted
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|()
decl_stmt|;
name|IvyNode
index|[]
name|evictedNodes
init|=
name|getEvictedNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|node
range|:
name|evictedNodes
control|)
block|{
name|evicted
operator|.
name|add
argument_list|(
name|node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|evicted
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getDownloadedNodes
parameter_list|()
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|downloaded
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
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|node
operator|.
name|isDownloaded
argument_list|()
operator|&&
name|node
operator|.
name|getRealNode
argument_list|()
operator|==
name|node
condition|)
block|{
name|downloaded
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|downloaded
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|downloaded
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getSearchedNodes
parameter_list|()
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|downloaded
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
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|node
operator|.
name|isSearched
argument_list|()
operator|&&
name|node
operator|.
name|getRealNode
argument_list|()
operator|==
name|node
condition|)
block|{
name|downloaded
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|downloaded
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|downloaded
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
index|[]
name|getDownloadReports
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|Collection
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|col
init|=
name|dependencyReports
operator|.
name|get
argument_list|(
name|getDependency
argument_list|(
name|mrid
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|ArtifactDownloadReport
index|[
literal|0
index|]
return|;
block|}
return|return
name|col
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|col
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IvyNode
name|getDependency
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
name|dependencies
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
return|;
block|}
comment|/**      * gives all the modules ids concerned by this report, from the most dependent to the least one      *      * @return a list of ModuleId      */
specifier|public
name|List
argument_list|<
name|ModuleId
argument_list|>
name|getModuleIds
parameter_list|()
block|{
if|if
condition|(
name|modulesIds
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|sortedDependencies
init|=
name|resolveEngine
operator|.
name|getSortEngine
argument_list|()
operator|.
name|sortNodes
argument_list|(
name|getDependencies
argument_list|()
argument_list|,
name|SortOptions
operator|.
name|SILENT
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|sortedDependencies
argument_list|)
expr_stmt|;
for|for
control|(
name|IvyNode
name|dependency
range|:
name|sortedDependencies
control|)
block|{
name|ModuleId
name|mid
init|=
name|dependency
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|deps
init|=
name|modulesIdsMap
operator|.
name|get
argument_list|(
name|mid
argument_list|)
decl_stmt|;
if|if
condition|(
name|deps
operator|==
literal|null
condition|)
block|{
name|deps
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|IvyNode
argument_list|>
argument_list|()
expr_stmt|;
name|modulesIdsMap
operator|.
name|put
argument_list|(
name|mid
argument_list|,
name|deps
argument_list|)
expr_stmt|;
block|}
name|deps
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
name|modulesIds
operator|=
operator|new
name|ArrayList
argument_list|<
name|ModuleId
argument_list|>
argument_list|(
name|modulesIdsMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|modulesIds
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|getNodes
parameter_list|(
name|ModuleId
name|mid
parameter_list|)
block|{
if|if
condition|(
name|modulesIds
operator|==
literal|null
condition|)
block|{
name|getModuleIds
argument_list|()
expr_stmt|;
block|}
return|return
name|modulesIdsMap
operator|.
name|get
argument_list|(
name|mid
argument_list|)
return|;
block|}
specifier|public
name|ResolveEngine
name|getResolveEngine
parameter_list|()
block|{
return|return
name|resolveEngine
return|;
block|}
specifier|public
name|int
name|getArtifactsNumber
parameter_list|()
block|{
name|int
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Collection
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
range|:
name|dependencyReports
operator|.
name|values
argument_list|()
control|)
block|{
name|total
operator|+=
name|reports
operator|==
literal|null
condition|?
literal|0
else|:
name|reports
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**      * Get every report on the download requests.      *      * @return the list of reports, never<code>null</code>      */
specifier|public
name|ArtifactDownloadReport
index|[]
name|getAllArtifactsReports
parameter_list|()
block|{
return|return
name|getArtifactsReports
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Get the report on the download requests. The list of download report can be restricted to a      * specific download status, and also remove the download report for the evicted modules.      *      * @param downloadStatus      *            the status of download to retrieve. Set it to<code>null</code> for no restriction      *            on the download status      * @param withEvicted      *            set it to<code>true</code> if the report for the evicted modules have to be      *            retrieved.      * @return the list of reports, never<code>null</code>      * @see ArtifactDownloadReport      */
specifier|public
name|ArtifactDownloadReport
index|[]
name|getArtifactsReports
parameter_list|(
name|DownloadStatus
name|downloadStatus
parameter_list|,
name|boolean
name|withEvicted
parameter_list|)
block|{
name|Collection
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|all
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ModuleRevisionId
argument_list|>
name|evictedMrids
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|withEvicted
condition|)
block|{
name|evictedMrids
operator|=
name|getEvictedMrids
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Collection
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
range|:
name|dependencyReports
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|ArtifactDownloadReport
name|report
range|:
name|reports
control|)
block|{
if|if
condition|(
name|downloadStatus
operator|!=
literal|null
operator|&&
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|downloadStatus
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|withEvicted
operator|||
operator|!
name|evictedMrids
operator|.
name|contains
argument_list|(
name|report
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
condition|)
block|{
name|all
operator|.
name|add
argument_list|(
name|report
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|all
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Get the report on the successful download requests with the evicted modules      *      * @return the list of reports, never<code>null</code>      */
specifier|public
name|ArtifactDownloadReport
index|[]
name|getDownloadedArtifactsReports
parameter_list|()
block|{
return|return
name|getArtifactsReports
argument_list|(
name|DownloadStatus
operator|.
name|SUCCESSFUL
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Get the report on the failed download requests with the evicted modules      *      * @return the list of reports, never<code>null</code>      */
specifier|public
name|ArtifactDownloadReport
index|[]
name|getFailedArtifactsReports
parameter_list|()
block|{
name|ArtifactDownloadReport
index|[]
name|allFailedReports
init|=
name|getArtifactsReports
argument_list|(
name|DownloadStatus
operator|.
name|FAILED
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|filterOutMergedArtifacts
argument_list|(
name|allFailedReports
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasError
parameter_list|()
block|{
return|return
name|getUnresolvedDependencies
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|||
name|getFailedArtifactsReports
argument_list|()
operator|.
name|length
operator|>
literal|0
return|;
block|}
specifier|public
name|int
name|getNodesNumber
parameter_list|()
block|{
return|return
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ArtifactDownloadReport
index|[]
name|filterOutMergedArtifacts
parameter_list|(
name|ArtifactDownloadReport
index|[]
name|allFailedReports
parameter_list|)
block|{
name|Collection
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|adrs
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|allFailedReports
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|iterator
init|=
name|adrs
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
name|ArtifactDownloadReport
name|adr
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getArtifact
argument_list|()
operator|.
name|getExtraAttribute
argument_list|(
literal|"ivy:merged"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|adrs
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|adrs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

