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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Collection
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
name|Map
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
name|Ivy
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
name|ArtifactOrigin
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
name|License
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
name|IvyNodeCallers
operator|.
name|Caller
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
name|util
operator|.
name|FileUtil
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
name|ivy
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|XmlReportOutputter
implements|implements
name|ReportOutputter
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|XML
return|;
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ResolveReport
name|report
parameter_list|,
name|File
name|destDir
parameter_list|)
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
argument_list|,
name|report
operator|.
name|getResolveId
argument_list|()
argument_list|,
name|confs
argument_list|,
name|destDir
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
parameter_list|,
name|String
name|resolveId
parameter_list|,
name|String
index|[]
name|confs
parameter_list|,
name|File
name|destDir
parameter_list|)
block|{
try|try
block|{
name|destDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|CacheManager
name|cacheMgr
init|=
operator|new
name|CacheManager
argument_list|(
literal|null
argument_list|,
name|destDir
argument_list|)
decl_stmt|;
name|File
name|reportFile
init|=
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|OutputStream
name|stream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|reportFile
argument_list|)
decl_stmt|;
name|output
argument_list|(
name|report
argument_list|,
name|confs
argument_list|,
name|stream
argument_list|)
expr_stmt|;
name|stream
operator|.
name|close
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\treport for "
operator|+
name|report
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|" "
operator|+
name|report
operator|.
name|getConfiguration
argument_list|()
operator|+
literal|" produced in "
operator|+
name|reportFile
argument_list|)
expr_stmt|;
name|File
name|reportXsl
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"ivy-report.xsl"
argument_list|)
decl_stmt|;
name|File
name|reportCss
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"ivy-report.css"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|reportXsl
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-report.xsl"
argument_list|)
argument_list|,
name|reportXsl
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|reportCss
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-report.css"
argument_list|)
argument_list|,
name|reportCss
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"impossible to produce report for "
operator|+
name|report
operator|.
name|getModuleDescriptor
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
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
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
block|{
name|output
argument_list|(
name|report
argument_list|,
operator|new
name|String
index|[]
block|{
name|report
operator|.
name|getConfiguration
argument_list|()
block|}
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ConfigurationResolveReport
name|report
parameter_list|,
name|String
index|[]
name|confs
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
block|{
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<?xml-stylesheet type=\"text/xsl\" href=\"ivy-report.xsl\"?>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<ivy-report version=\"1.0\">"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t<info"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\torganisation=\""
operator|+
name|mrid
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tmodule=\""
operator|+
name|mrid
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\trevision=\""
operator|+
name|mrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getBranch
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\tbranch=\""
operator|+
name|mrid
operator|.
name|getBranch
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|Map
name|extraAttributes
init|=
name|mrid
operator|.
name|getExtraAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|extraAttributes
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
name|out
operator|.
name|println
argument_list|(
literal|"\t\textra-"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"=\""
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t\tconf=\""
operator|+
name|report
operator|.
name|getConfiguration
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tconfs=\""
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|confs
argument_list|,
literal|", "
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tdate=\""
operator|+
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|report
operator|.
name|getDate
argument_list|()
argument_list|)
operator|+
literal|"\"/>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t<dependencies>"
argument_list|)
expr_stmt|;
comment|// create a list of ModuleRevisionIds indicating the position for each dependency
name|List
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|(
name|report
operator|.
name|getModuleRevisionIds
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|report
operator|.
name|getModuleIds
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
name|ModuleId
name|mid
init|=
operator|(
name|ModuleId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t<module organisation=\""
operator|+
name|mid
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\""
operator|+
literal|" name=\""
operator|+
name|mid
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
operator|+
literal|" resolver=\""
operator|+
name|report
operator|.
name|getResolveEngine
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolverName
argument_list|(
name|mid
argument_list|)
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|it2
init|=
name|report
operator|.
name|getNodes
argument_list|(
name|mid
argument_list|)
operator|.
name|iterator
argument_list|()
init|;
name|it2
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|IvyNode
name|dep
init|=
operator|(
name|IvyNode
operator|)
name|it2
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dep
operator|.
name|getModuleRevision
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|md
operator|=
name|dep
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getDescriptor
argument_list|()
expr_stmt|;
block|}
name|StringBuffer
name|details
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|dep
operator|.
name|isLoaded
argument_list|()
condition|)
block|{
name|details
operator|.
name|append
argument_list|(
literal|" status=\""
argument_list|)
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
literal|" pubdate=\""
argument_list|)
operator|.
name|append
argument_list|(
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|dep
operator|.
name|getPublication
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
literal|" resolver=\""
argument_list|)
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
literal|" artresolver=\""
argument_list|)
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getArtifactResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dep
operator|.
name|isEvicted
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
condition|)
block|{
name|EvictionData
name|ed
init|=
name|dep
operator|.
name|getEvictedData
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ed
operator|.
name|getConflictManager
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|details
operator|.
name|append
argument_list|(
literal|" evicted=\""
argument_list|)
operator|.
name|append
argument_list|(
name|ed
operator|.
name|getConflictManager
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|details
operator|.
name|append
argument_list|(
literal|" evicted=\"transitive\""
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dep
operator|.
name|hasProblem
argument_list|()
condition|)
block|{
name|details
operator|.
name|append
argument_list|(
literal|" error=\""
argument_list|)
operator|.
name|append
argument_list|(
name|dep
operator|.
name|getProblem
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|md
operator|!=
literal|null
operator|&&
name|md
operator|.
name|getHomePage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|details
operator|.
name|append
argument_list|(
literal|" homepage=\""
argument_list|)
operator|.
name|append
argument_list|(
name|md
operator|.
name|getHomePage
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|extraAttributes
operator|=
name|md
operator|!=
literal|null
condition|?
name|md
operator|.
name|getExtraAttributes
argument_list|()
else|:
name|dep
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getExtraAttributes
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|extraAttributes
operator|.
name|keySet
argument_list|()
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
name|String
name|attName
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|details
operator|.
name|append
argument_list|(
literal|" extra-"
argument_list|)
operator|.
name|append
argument_list|(
name|attName
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|extraAttributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
name|defaultValue
init|=
name|dep
operator|.
name|getDescriptor
argument_list|()
operator|!=
literal|null
condition|?
literal|" default=\""
operator|+
name|dep
operator|.
name|getDescriptor
argument_list|()
operator|.
name|isDefault
argument_list|()
operator|+
literal|"\""
else|:
literal|""
decl_stmt|;
name|int
name|position
init|=
name|dependencies
operator|.
name|indexOf
argument_list|(
name|dep
operator|.
name|getResolvedId
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t<revision name=\""
operator|+
name|dep
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
operator|+
operator|(
name|dep
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getBranch
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
literal|" branch=\""
operator|+
name|dep
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getBranch
argument_list|()
operator|+
literal|"\""
operator|)
operator|+
name|details
operator|+
literal|" downloaded=\""
operator|+
name|dep
operator|.
name|isDownloaded
argument_list|()
operator|+
literal|"\""
operator|+
literal|" searched=\""
operator|+
name|dep
operator|.
name|isSearched
argument_list|()
operator|+
literal|"\""
operator|+
name|defaultValue
operator|+
literal|" conf=\""
operator|+
name|toString
argument_list|(
name|dep
operator|.
name|getConfigurations
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
argument_list|)
operator|+
literal|"\""
operator|+
literal|" position=\""
operator|+
name|position
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
if|if
condition|(
name|md
operator|!=
literal|null
condition|)
block|{
name|License
index|[]
name|licenses
init|=
name|md
operator|.
name|getLicenses
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
name|licenses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|lurl
decl_stmt|;
if|if
condition|(
name|licenses
index|[
name|i
index|]
operator|.
name|getUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|lurl
operator|=
literal|" url=\""
operator|+
name|licenses
index|[
name|i
index|]
operator|.
name|getUrl
argument_list|()
operator|+
literal|"\""
expr_stmt|;
block|}
else|else
block|{
name|lurl
operator|=
literal|""
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t<license name=\""
operator|+
name|licenses
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
operator|+
name|lurl
operator|+
literal|"/>"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dep
operator|.
name|isEvicted
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
condition|)
block|{
name|EvictionData
name|ed
init|=
name|dep
operator|.
name|getEvictedData
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
name|selected
init|=
name|ed
operator|.
name|getSelected
argument_list|()
decl_stmt|;
if|if
condition|(
name|selected
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|it3
init|=
name|selected
operator|.
name|iterator
argument_list|()
init|;
name|it3
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|IvyNode
name|sel
init|=
operator|(
name|IvyNode
operator|)
name|it3
operator|.
name|next
argument_list|()
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t<evicted-by rev=\""
operator|+
name|sel
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Caller
index|[]
name|callers
init|=
name|dep
operator|.
name|getCallers
argument_list|(
name|report
operator|.
name|getConfiguration
argument_list|()
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
name|callers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|StringBuffer
name|callerDetails
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|Map
name|callerExtraAttributes
init|=
name|callers
index|[
name|i
index|]
operator|.
name|getDependencyDescriptor
argument_list|()
operator|.
name|getExtraAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|callerExtraAttributes
operator|.
name|keySet
argument_list|()
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
name|String
name|attName
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|callerDetails
operator|.
name|append
argument_list|(
literal|" extra-"
argument_list|)
operator|.
name|append
argument_list|(
name|attName
argument_list|)
operator|.
name|append
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|append
argument_list|(
name|callerExtraAttributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t<caller organisation=\""
operator|+
name|callers
index|[
name|i
index|]
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\""
operator|+
literal|" name=\""
operator|+
name|callers
index|[
name|i
index|]
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
operator|+
literal|" conf=\""
operator|+
name|toString
argument_list|(
name|callers
index|[
name|i
index|]
operator|.
name|getCallerConfigurations
argument_list|()
argument_list|)
operator|+
literal|"\""
operator|+
literal|" rev=\""
operator|+
name|callers
index|[
name|i
index|]
operator|.
name|getAskedDependencyId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
operator|+
name|callerDetails
operator|+
literal|"/>"
argument_list|)
expr_stmt|;
block|}
name|ArtifactDownloadReport
index|[]
name|adr
init|=
name|report
operator|.
name|getDownloadReports
argument_list|(
name|dep
operator|.
name|getResolvedId
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t<artifacts>"
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
name|adr
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t\t\t\t<artifact name=\""
operator|+
name|adr
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\" type=\""
operator|+
name|adr
index|[
name|i
index|]
operator|.
name|getType
argument_list|()
operator|+
literal|"\" ext=\""
operator|+
name|adr
index|[
name|i
index|]
operator|.
name|getExt
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|extraAttributes
operator|=
name|adr
index|[
name|i
index|]
operator|.
name|getArtifact
argument_list|()
operator|.
name|getExtraAttributes
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|extraAttributes
operator|.
name|keySet
argument_list|()
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
name|String
name|attName
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" extra-"
operator|+
name|attName
operator|+
literal|"=\""
operator|+
name|extraAttributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|" status=\""
operator|+
name|adr
index|[
name|i
index|]
operator|.
name|getDownloadStatus
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" size=\""
operator|+
name|adr
index|[
name|i
index|]
operator|.
name|getSize
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|ArtifactOrigin
name|origin
init|=
name|adr
index|[
name|i
index|]
operator|.
name|getArtifactOrigin
argument_list|()
decl_stmt|;
if|if
condition|(
name|origin
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t\t\t<origin-location is-local=\""
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|origin
operator|.
name|isLocal
argument_list|()
argument_list|)
operator|+
literal|"\""
operator|+
literal|" location=\""
operator|+
name|origin
operator|.
name|getLocation
argument_list|()
operator|+
literal|"\"/>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t\t</artifact>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t\t</artifacts>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\t</revision>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t\t</module>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t</dependencies>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</ivy-report>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|toString
parameter_list|(
name|String
index|[]
name|strs
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
name|strs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|strs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|strs
operator|.
name|length
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

