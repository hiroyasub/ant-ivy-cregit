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
name|report
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|core
operator|.
name|report
operator|.
name|ResolveReport
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
class|class
name|LogReportOutputter
implements|implements
name|ReportOutputter
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|CONSOLE
return|;
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ResolveReport
name|report
parameter_list|,
name|ResolutionCacheManager
name|cacheMgr
parameter_list|)
throws|throws
name|IOException
block|{
name|IvySettings
name|settings
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
if|if
condition|(
name|settings
operator|.
name|logModulesInUse
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\t:: modules in use:"
argument_list|)
expr_stmt|;
name|List
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|(
name|report
operator|.
name|getDependencies
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|dependencies
argument_list|)
expr_stmt|;
if|if
condition|(
name|dependencies
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
index|[]
name|confs
init|=
name|report
operator|.
name|getConfigurations
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
name|dependencies
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|dependencies
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|isCompletelyEvicted
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
name|nodeConfs
init|=
operator|new
name|ArrayList
argument_list|(
name|confs
operator|.
name|length
argument_list|)
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
name|confs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|String
name|conf
init|=
name|confs
index|[
name|j
index|]
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|getConfigurationReport
argument_list|(
name|conf
argument_list|)
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|contains
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
condition|)
block|{
name|nodeConfs
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|info
argument_list|(
literal|"\t"
operator|+
name|node
operator|+
literal|" from "
operator|+
name|node
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getResolver
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" in "
operator|+
name|nodeConfs
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|IvyNode
index|[]
name|evicted
init|=
name|report
operator|.
name|getEvictedNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|evicted
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\t:: evicted modules:"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|evicted
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Collection
name|allEvictingNodes
init|=
name|evicted
index|[
name|i
index|]
operator|.
name|getAllEvictingNodesDetails
argument_list|()
decl_stmt|;
if|if
condition|(
name|allEvictingNodes
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\t"
operator|+
name|evicted
index|[
name|i
index|]
operator|+
literal|" transitively in "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|evicted
index|[
name|i
index|]
operator|.
name|getEvictedConfs
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|allEvictingNodes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\t"
operator|+
name|evicted
index|[
name|i
index|]
operator|+
literal|" by [] ("
operator|+
name|evicted
index|[
name|i
index|]
operator|.
name|getAllEvictingConflictManagers
argument_list|()
operator|+
literal|") in "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|evicted
index|[
name|i
index|]
operator|.
name|getEvictedConfs
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\t"
operator|+
name|evicted
index|[
name|i
index|]
operator|+
literal|" by "
operator|+
name|allEvictingNodes
operator|+
literal|" in "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|evicted
index|[
name|i
index|]
operator|.
name|getEvictedConfs
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|confs
init|=
name|evicted
index|[
name|i
index|]
operator|.
name|getEvictedConfs
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
name|confs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|EvictionData
name|evictedData
init|=
name|evicted
index|[
name|i
index|]
operator|.
name|getEvictedData
argument_list|(
name|confs
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|evictedData
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t  in "
operator|+
name|evictedData
operator|.
name|getParent
argument_list|()
operator|+
literal|" with "
operator|+
name|evictedData
operator|.
name|getConflictManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//CheckStyle:MagicNumber| OFF
name|char
index|[]
name|sep
init|=
operator|new
name|char
index|[
literal|69
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|sep
argument_list|,
literal|'-'
argument_list|)
expr_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
literal|"\t"
operator|+
operator|new
name|String
argument_list|(
name|sep
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuffer
name|line
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"\t"
argument_list|)
decl_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|""
argument_list|,
literal|18
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"modules"
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"artifacts"
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
name|line
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|line
operator|=
operator|new
name|StringBuffer
argument_list|(
literal|"\t"
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"conf"
argument_list|,
literal|18
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"number"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"search"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"dwnlded"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"evicted"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"number"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
literal|"dwnlded"
argument_list|,
literal|7
argument_list|)
expr_stmt|;
comment|//CheckStyle:MagicNumber| ON
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
name|line
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
literal|"\t"
operator|+
operator|new
name|String
argument_list|(
name|sep
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|confs
init|=
name|report
operator|.
name|getConfigurations
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|output
argument_list|(
name|report
operator|.
name|getConfigurationReport
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|rawinfo
argument_list|(
literal|"\t"
operator|+
operator|new
name|String
argument_list|(
name|sep
argument_list|)
argument_list|)
expr_stmt|;
name|IvyNode
index|[]
name|unresolved
init|=
name|report
operator|.
name|getUnresolvedDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|unresolved
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::          UNRESOLVED DEPENDENCIES         ::"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::"
argument_list|)
expr_stmt|;
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
name|unresolved
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t:: "
operator|+
name|unresolved
index|[
name|i
index|]
operator|+
literal|": "
operator|+
name|unresolved
index|[
name|i
index|]
operator|.
name|getProblemMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|unresolved
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::\n"
argument_list|)
expr_stmt|;
block|}
name|ArtifactDownloadReport
index|[]
name|errors
init|=
name|report
operator|.
name|getFailedArtifactsReports
argument_list|()
decl_stmt|;
if|if
condition|(
name|errors
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::              FAILED DOWNLOADS            ::"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"\t:: ^ see resolution messages for details  ^ ::"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::"
argument_list|)
expr_stmt|;
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
name|errors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t:: "
operator|+
name|errors
index|[
name|i
index|]
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|errors
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"\t::::::::::::::::::::::::::::::::::::::::::::::\n"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ConfigurationResolveReport
name|report
parameter_list|)
block|{
name|StringBuffer
name|line
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"\t"
argument_list|)
decl_stmt|;
comment|//CheckStyle:MagicNumber| OFF
name|append
argument_list|(
name|line
argument_list|,
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|,
literal|18
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getNodesNumber
argument_list|()
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getSearchedNodes
argument_list|()
operator|.
name|length
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getDownloadedNodes
argument_list|()
operator|.
name|length
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getEvictedNodes
argument_list|()
operator|.
name|length
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getArtifactsNumber
argument_list|()
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|line
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|getDownloadedArtifactsReports
argument_list|()
operator|.
name|length
argument_list|)
argument_list|,
literal|7
argument_list|)
expr_stmt|;
comment|//CheckStyle:MagicNumber| ON
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
name|line
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|append
parameter_list|(
name|StringBuffer
name|line
parameter_list|,
name|Object
name|o
parameter_list|,
name|int
name|limit
parameter_list|)
block|{
name|String
name|v
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|.
name|length
argument_list|()
operator|>=
name|limit
condition|)
block|{
name|v
operator|=
name|v
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|missing
init|=
name|limit
operator|-
name|v
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|half
init|=
name|missing
operator|/
literal|2
decl_stmt|;
name|char
index|[]
name|c
init|=
operator|new
name|char
index|[
name|limit
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|c
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|v
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|c
argument_list|,
name|missing
operator|-
name|half
argument_list|,
name|v
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|v
operator|=
operator|new
name|String
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|line
operator|.
name|append
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|line
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

