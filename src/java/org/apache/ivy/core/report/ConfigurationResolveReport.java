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
name|CacheManager
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
comment|/**  * @author x.hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationResolveReport
block|{
specifier|private
name|ModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|String
name|_conf
decl_stmt|;
specifier|private
name|Date
name|_date
decl_stmt|;
specifier|private
name|Map
name|_dependencyReports
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|_dependencies
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
specifier|private
name|ResolveEngine
name|_resolveEngine
decl_stmt|;
specifier|private
name|Map
name|_modulesIdsMap
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
specifier|private
name|List
name|_modulesIds
decl_stmt|;
specifier|private
name|List
name|_previousDeps
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
name|_resolveEngine
operator|=
name|resolveEngine
expr_stmt|;
name|_md
operator|=
name|md
expr_stmt|;
name|_conf
operator|=
name|conf
expr_stmt|;
name|_date
operator|=
name|date
expr_stmt|;
comment|// parse previous deps from previous report file if any
name|CacheManager
name|cache
init|=
name|options
operator|.
name|getCache
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
name|_previousDeps
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|parser
operator|.
name|getDependencyRevisionIds
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
name|_previousDeps
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|_previousDeps
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|hasChanged
parameter_list|()
block|{
if|if
condition|(
name|_previousDeps
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
operator|!
operator|new
name|HashSet
argument_list|(
name|_previousDeps
argument_list|)
operator|.
name|equals
argument_list|(
name|getModuleRevisionIds
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns all non evicted and non error dependency mrids      * The returned set is ordered so that a dependency will always      * be found before their own dependencies      * @return all non evicted and non error dependency mrids      */
specifier|public
name|Set
name|getModuleRevisionIds
parameter_list|()
block|{
name|Set
name|mrids
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|getDependencies
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
name|_dependencies
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
name|_dependencies
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
name|_dependencyReports
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
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
name|_dependencies
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
name|_dependencies
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
name|adrs
init|=
operator|new
name|ArrayList
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
name|_conf
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
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ArtifactDownloadReport
name|artifactReport
init|=
name|report
operator|.
name|getArtifactReport
argument_list|(
name|artifacts
index|[
name|i
index|]
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
name|artifacts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|_dependencyReports
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
name|_conf
return|;
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|_date
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|_md
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getUnresolvedDependencies
parameter_list|()
block|{
name|List
name|unresolved
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
name|getDependencies
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
operator|(
name|IvyNode
index|[]
operator|)
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
name|getDependencies
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|(
name|_dependencies
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
name|evicted
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
name|getDependencies
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
name|isEvicted
argument_list|(
name|_conf
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
operator|(
name|IvyNode
index|[]
operator|)
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
specifier|public
name|IvyNode
index|[]
name|getDownloadedNodes
parameter_list|()
block|{
name|List
name|downloaded
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
name|getDependencies
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
operator|(
name|IvyNode
index|[]
operator|)
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
name|downloaded
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
name|getDependencies
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
operator|(
name|IvyNode
index|[]
operator|)
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
name|col
init|=
operator|(
name|Collection
operator|)
name|_dependencyReports
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
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
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
operator|(
name|IvyNode
operator|)
name|_dependencies
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
return|;
block|}
comment|/** 	 * gives all the modules ids concerned by this report, from the most dependent to the least one 	 * @return a list of ModuleId 	 */
specifier|public
name|List
name|getModuleIds
parameter_list|()
block|{
if|if
condition|(
name|_modulesIds
operator|==
literal|null
condition|)
block|{
name|List
name|sortedDependencies
init|=
name|_resolveEngine
operator|.
name|getSortEngine
argument_list|()
operator|.
name|sortNodes
argument_list|(
name|getDependencies
argument_list|()
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
name|Iterator
name|iter
init|=
name|sortedDependencies
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
name|dependency
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|deps
init|=
operator|(
name|Collection
operator|)
name|_modulesIdsMap
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
name|HashSet
argument_list|()
expr_stmt|;
name|_modulesIdsMap
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
name|_modulesIds
operator|=
operator|new
name|ArrayList
argument_list|(
name|_modulesIdsMap
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
name|_modulesIds
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|getNodes
parameter_list|(
name|ModuleId
name|mid
parameter_list|)
block|{
if|if
condition|(
name|_modulesIds
operator|==
literal|null
condition|)
block|{
name|getModuleIds
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|Collection
operator|)
name|_modulesIdsMap
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
name|_resolveEngine
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
name|Iterator
name|iter
init|=
name|_dependencyReports
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
name|Collection
name|reports
init|=
operator|(
name|Collection
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
specifier|public
name|ArtifactDownloadReport
index|[]
name|getDownloadedArtifactsReports
parameter_list|()
block|{
name|List
name|result
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
name|_dependencyReports
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
name|Collection
name|reports
init|=
operator|(
name|Collection
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|reports
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
operator|(
name|ArtifactDownloadReport
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|SUCCESSFUL
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|adr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|result
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
name|getFailedArtifactsReports
parameter_list|()
block|{
name|List
name|result
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
name|_dependencyReports
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
name|Collection
name|reports
init|=
operator|(
name|Collection
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|reports
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
operator|(
name|ArtifactDownloadReport
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|adr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|result
operator|.
name|size
argument_list|()
index|]
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
block|}
end_class

end_unit

