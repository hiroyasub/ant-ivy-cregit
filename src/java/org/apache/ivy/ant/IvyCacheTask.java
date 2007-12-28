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
name|ant
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_comment
comment|/**  * Base class for the cache path related classes: cachepath and cachefileset. Most of the behviour  * is common to the two, since only the produced element differs.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|IvyCacheTask
extends|extends
name|IvyPostResolveTask
block|{
specifier|protected
name|List
name|getArtifactReports
parameter_list|()
throws|throws
name|BuildException
throws|,
name|ParseException
throws|,
name|IOException
block|{
name|Collection
name|artifacts
init|=
name|getAllArtifactReports
argument_list|()
decl_stmt|;
name|List
name|ret
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
name|artifacts
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
name|ArtifactDownloadReport
name|artifactReport
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|getArtifactFilter
argument_list|()
operator|.
name|accept
argument_list|(
name|artifactReport
operator|.
name|getArtifact
argument_list|()
argument_list|)
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|artifactReport
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|Collection
name|getAllArtifactReports
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|String
index|[]
name|confs
init|=
name|splitConfs
argument_list|(
name|getConf
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|all
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
name|ResolveReport
name|report
init|=
name|getResolvedReport
argument_list|()
decl_stmt|;
if|if
condition|(
name|report
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"using internal report instance to get artifacts list"
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ConfigurationResolveReport
name|configurationReport
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationReport
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"bad confs provided: "
operator|+
name|confs
index|[
name|i
index|]
operator|+
literal|" not found among "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|Set
name|revisions
init|=
name|configurationReport
operator|.
name|getModuleRevisionIds
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|revisions
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
name|ModuleRevisionId
name|revId
init|=
operator|(
name|ModuleRevisionId
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArtifactDownloadReport
index|[]
name|aReports
init|=
name|configurationReport
operator|.
name|getDownloadReports
argument_list|(
name|revId
argument_list|)
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|aReports
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"using stored report to get artifacts list"
argument_list|)
expr_stmt|;
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
name|String
name|resolvedId
init|=
name|getResolveId
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolvedId
operator|==
literal|null
condition|)
block|{
name|resolvedId
operator|=
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
name|getResolvedModuleId
argument_list|()
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|reportFile
init|=
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolvedId
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|reportFile
argument_list|)
expr_stmt|;
name|ArtifactDownloadReport
index|[]
name|aReports
init|=
name|parser
operator|.
name|getArtifactReports
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
name|aReports
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|all
return|;
block|}
block|}
end_class

end_unit

