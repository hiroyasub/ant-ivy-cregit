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
name|ReportOutputter
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
name|filter
operator|.
name|Filter
import|;
end_import

begin_comment
comment|/**  * Represents a whole resolution report for a module  */
end_comment

begin_class
specifier|public
class|class
name|ResolveReport
block|{
specifier|private
name|ModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|Map
name|_confReports
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
specifier|private
name|List
name|_problemMessages
decl_stmt|;
specifier|private
name|List
name|_dependencies
decl_stmt|;
comment|// the list of all dependencies resolved, ordered from the more dependent to the less dependent
specifier|private
name|List
name|_artifacts
decl_stmt|;
specifier|private
name|long
name|_resolveTime
decl_stmt|;
specifier|private
name|long
name|_downloadTime
decl_stmt|;
specifier|private
name|String
name|_resolveId
decl_stmt|;
specifier|public
name|ResolveReport
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|this
argument_list|(
name|md
argument_list|,
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
name|md
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResolveReport
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|resolveId
parameter_list|)
block|{
name|_md
operator|=
name|md
expr_stmt|;
name|_resolveId
operator|=
name|resolveId
expr_stmt|;
block|}
specifier|public
name|void
name|addReport
parameter_list|(
name|String
name|conf
parameter_list|,
name|ConfigurationResolveReport
name|report
parameter_list|)
block|{
name|_confReports
operator|.
name|put
argument_list|(
name|conf
argument_list|,
name|report
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurationResolveReport
name|getConfigurationReport
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationResolveReport
operator|)
name|_confReports
operator|.
name|get
argument_list|(
name|conf
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|_confReports
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_confReports
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
name|boolean
name|hasError
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|hasError
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|hasError
operator||=
name|report
operator|.
name|hasError
argument_list|()
expr_stmt|;
block|}
return|return
name|hasError
return|;
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ReportOutputter
index|[]
name|outputters
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|outputters
index|[
name|i
index|]
operator|.
name|output
argument_list|(
name|this
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
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
name|getEvictedNodes
parameter_list|()
block|{
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
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
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getEvictedNodes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|IvyNode
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|all
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
name|getUnresolvedDependencies
parameter_list|()
block|{
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
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
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getUnresolvedDependencies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|IvyNode
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|all
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
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
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
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getFailedArtifactsReports
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
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
specifier|public
name|boolean
name|hasChanged
parameter_list|()
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
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
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|hasChanged
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setProblemMessages
parameter_list|(
name|List
name|problems
parameter_list|)
block|{
name|_problemMessages
operator|=
name|problems
expr_stmt|;
block|}
specifier|public
name|List
name|getProblemMessages
parameter_list|()
block|{
return|return
name|_problemMessages
return|;
block|}
specifier|public
name|List
name|getAllProblemMessages
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|_problemMessages
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
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
name|ConfigurationResolveReport
name|r
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|IvyNode
index|[]
name|unresolved
init|=
name|r
operator|.
name|getUnresolvedDependencies
argument_list|()
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
name|unresolved
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|errMsg
init|=
name|unresolved
index|[
name|i
index|]
operator|.
name|getProblemMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|errMsg
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
literal|"unresolved dependency: "
operator|+
name|unresolved
index|[
name|i
index|]
operator|.
name|getId
argument_list|()
operator|+
literal|": "
operator|+
name|errMsg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|.
name|add
argument_list|(
literal|"unresolved dependency: "
operator|+
name|unresolved
index|[
name|i
index|]
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ArtifactDownloadReport
index|[]
name|adrs
init|=
name|r
operator|.
name|getFailedArtifactsReports
argument_list|()
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
name|adrs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
literal|"download failed: "
operator|+
name|adrs
index|[
name|i
index|]
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|setDependencies
parameter_list|(
name|List
name|dependencies
parameter_list|,
name|Filter
name|artifactFilter
parameter_list|)
block|{
name|_dependencies
operator|=
name|dependencies
expr_stmt|;
comment|// collect list of artifacts
name|_artifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|dependencies
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
if|if
condition|(
operator|!
name|dependency
operator|.
name|isCompletelyEvicted
argument_list|()
operator|&&
operator|!
name|dependency
operator|.
name|hasProblem
argument_list|()
condition|)
block|{
name|_artifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dependency
operator|.
name|getSelectedArtifacts
argument_list|(
name|artifactFilter
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// update the configurations reports with the dependencies
comment|// these reports will be completed later with download information, if any
name|String
index|[]
name|dconfs
init|=
name|dependency
operator|.
name|getRootModuleConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|dconfs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|ConfigurationResolveReport
name|configurationReport
init|=
name|getConfigurationReport
argument_list|(
name|dconfs
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationReport
operator|!=
literal|null
condition|)
block|{
name|configurationReport
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/** 	 * Returns the list of all dependencies concerned by this report as a List of IvyNode 	 * ordered from the more dependent to the least one 	 * @return 	 */
specifier|public
name|List
name|getDependencies
parameter_list|()
block|{
return|return
name|_dependencies
return|;
block|}
comment|/** 	 * Returns the list of all artifacts which should be downloaded per this resolve 	 * To know if the artifact have actually been downloaded use information found 	 * in ConfigurationResolveReport. 	 * @return 	 */
specifier|public
name|List
name|getArtifacts
parameter_list|()
block|{
return|return
name|_artifacts
return|;
block|}
comment|/** 	 * gives all the modules ids concerned by this report, from the most dependent to the least one 	 * @return a list of ModuleId 	 */
specifier|public
name|List
name|getModuleIds
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|sortedDependencies
init|=
operator|new
name|ArrayList
argument_list|(
name|_dependencies
argument_list|)
decl_stmt|;
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
if|if
condition|(
operator|!
name|ret
operator|.
name|contains
argument_list|(
name|mid
argument_list|)
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|mid
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|setResolveTime
parameter_list|(
name|long
name|elapsedTime
parameter_list|)
block|{
name|_resolveTime
operator|=
name|elapsedTime
expr_stmt|;
block|}
specifier|public
name|long
name|getResolveTime
parameter_list|()
block|{
return|return
name|_resolveTime
return|;
block|}
specifier|public
name|void
name|setDownloadTime
parameter_list|(
name|long
name|elapsedTime
parameter_list|)
block|{
name|_downloadTime
operator|=
name|elapsedTime
expr_stmt|;
block|}
specifier|public
name|long
name|getDownloadTime
parameter_list|()
block|{
return|return
name|_downloadTime
return|;
block|}
specifier|public
name|String
name|getResolveId
parameter_list|()
block|{
return|return
name|_resolveId
return|;
block|}
block|}
end_class

end_unit

