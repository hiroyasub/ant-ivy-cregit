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
name|install
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
name|Date
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
name|Configuration
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
name|DefaultDependencyDescriptor
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
name|DefaultModuleDescriptor
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
name|DownloadOptions
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
name|search
operator|.
name|SearchEngine
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
name|NoConflictManager
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
name|ExactPatternMatcher
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
name|MatcherHelper
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
name|PatternMatcher
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
name|DependencyResolver
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

begin_class
specifier|public
class|class
name|InstallEngine
block|{
specifier|private
name|InstallEngineSettings
name|settings
decl_stmt|;
specifier|private
name|ResolveEngine
name|resolveEngine
decl_stmt|;
specifier|private
name|SearchEngine
name|searchEngine
decl_stmt|;
specifier|public
name|InstallEngine
parameter_list|(
name|InstallEngineSettings
name|settings
parameter_list|,
name|SearchEngine
name|searchEngine
parameter_list|,
name|ResolveEngine
name|resolveEngine
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|searchEngine
operator|=
name|searchEngine
expr_stmt|;
name|this
operator|.
name|resolveEngine
operator|=
name|resolveEngine
expr_stmt|;
block|}
specifier|public
name|ResolveReport
name|install
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|,
name|InstallOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|DependencyResolver
name|fromResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|from
argument_list|)
decl_stmt|;
name|DependencyResolver
name|toResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|to
argument_list|)
decl_stmt|;
if|if
condition|(
name|fromResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown resolver "
operator|+
name|from
operator|+
literal|". Available resolvers are: "
operator|+
name|settings
operator|.
name|getResolverNames
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|toResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown resolver "
operator|+
name|to
operator|+
literal|". Available resolvers are: "
operator|+
name|settings
operator|.
name|getResolverNames
argument_list|()
argument_list|)
throw|;
block|}
name|PatternMatcher
name|matcher
init|=
name|settings
operator|.
name|getMatcher
argument_list|(
name|options
operator|.
name|getMatcherName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown matcher "
operator|+
name|options
operator|.
name|getMatcherName
argument_list|()
operator|+
literal|". Available matchers are: "
operator|+
name|settings
operator|.
name|getMatcherNames
argument_list|()
argument_list|)
throw|;
block|}
comment|// build module file declaring the dependency
name|Message
operator|.
name|info
argument_list|(
literal|":: installing "
operator|+
name|mrid
operator|+
literal|" ::"
argument_list|)
expr_stmt|;
name|DependencyResolver
name|oldDicator
init|=
name|resolveEngine
operator|.
name|getDictatorResolver
argument_list|()
decl_stmt|;
name|boolean
name|log
init|=
name|settings
operator|.
name|logNotConvertedExclusionRule
argument_list|()
decl_stmt|;
try|try
block|{
name|settings
operator|.
name|setLogNotConvertedExclusionRule
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolveEngine
operator|.
name|setDictatorResolver
argument_list|(
name|fromResolver
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"ivy-install"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|settings
operator|.
name|getStatusManager
argument_list|()
operator|.
name|getDefaultStatus
argument_list|()
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|resolveId
init|=
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
operator|new
name|Configuration
argument_list|(
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|addConflictManager
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|ExactPatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|ExactPatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|)
argument_list|,
name|ExactPatternMatcher
operator|.
name|INSTANCE
argument_list|,
operator|new
name|NoConflictManager
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|MatcherHelper
operator|.
name|isExact
argument_list|(
name|matcher
argument_list|,
name|mrid
argument_list|)
condition|)
block|{
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|md
argument_list|,
name|mrid
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|options
operator|.
name|isTransitive
argument_list|()
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"default"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ModuleRevisionId
index|[]
name|mrids
init|=
name|searchEngine
operator|.
name|listModules
argument_list|(
name|fromResolver
argument_list|,
name|mrid
argument_list|,
name|matcher
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
name|mrids
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\tfound "
operator|+
name|mrids
index|[
name|i
index|]
operator|+
literal|" to install: adding to the list"
argument_list|)
expr_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|md
argument_list|,
name|mrids
index|[
name|i
index|]
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|options
operator|.
name|isTransitive
argument_list|()
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"default"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
comment|// resolve using appropriate resolver
name|ResolveReport
name|report
init|=
operator|new
name|ResolveReport
argument_list|(
name|md
argument_list|,
name|resolveId
argument_list|)
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|":: resolving dependencies ::"
argument_list|)
expr_stmt|;
name|ResolveOptions
name|resolveOptions
init|=
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setResolveId
argument_list|(
name|resolveId
argument_list|)
operator|.
name|setConfs
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
operator|.
name|setValidate
argument_list|(
name|options
operator|.
name|isValidate
argument_list|()
argument_list|)
decl_stmt|;
name|IvyNode
index|[]
name|dependencies
init|=
name|resolveEngine
operator|.
name|getDependencies
argument_list|(
name|md
argument_list|,
name|resolveOptions
argument_list|,
name|report
argument_list|)
decl_stmt|;
name|report
operator|.
name|setDependencies
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dependencies
argument_list|)
argument_list|,
name|options
operator|.
name|getArtifactFilter
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|":: downloading artifacts to cache ::"
argument_list|)
expr_stmt|;
name|resolveEngine
operator|.
name|downloadArtifacts
argument_list|(
name|report
argument_list|,
name|options
operator|.
name|getArtifactFilter
argument_list|()
argument_list|,
operator|new
name|DownloadOptions
argument_list|()
argument_list|)
expr_stmt|;
comment|// now that everything is in cache, we can publish all these modules
name|Message
operator|.
name|info
argument_list|(
literal|":: installing in "
operator|+
name|to
operator|+
literal|" ::"
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
name|dependencies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ModuleDescriptor
name|depmd
init|=
name|dependencies
index|[
name|i
index|]
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|depmd
operator|!=
literal|null
condition|)
block|{
name|ModuleRevisionId
name|depMrid
init|=
name|depmd
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"installing "
operator|+
name|depMrid
argument_list|)
expr_stmt|;
name|boolean
name|successfullyPublished
init|=
literal|false
decl_stmt|;
try|try
block|{
name|toResolver
operator|.
name|beginPublishTransaction
argument_list|(
name|depMrid
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
comment|// publish artifacts
name|ArtifactDownloadReport
index|[]
name|artifacts
init|=
name|report
operator|.
name|getArtifactsReports
argument_list|(
name|depMrid
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
name|artifacts
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|artifacts
index|[
name|j
index|]
operator|.
name|getLocalFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|toResolver
operator|.
name|publish
argument_list|(
name|artifacts
index|[
name|j
index|]
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|artifacts
index|[
name|j
index|]
operator|.
name|getLocalFile
argument_list|()
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// publish metadata
name|File
name|localIvyFile
init|=
name|dependencies
index|[
name|i
index|]
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getReport
argument_list|()
operator|.
name|getLocalFile
argument_list|()
decl_stmt|;
name|toResolver
operator|.
name|publish
argument_list|(
name|depmd
operator|.
name|getMetadataArtifact
argument_list|()
argument_list|,
name|localIvyFile
argument_list|,
name|options
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
comment|// end module publish
name|toResolver
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
name|successfullyPublished
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|successfullyPublished
condition|)
block|{
name|toResolver
operator|.
name|abortPublishTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
name|Message
operator|.
name|info
argument_list|(
literal|":: install resolution report ::"
argument_list|)
expr_stmt|;
comment|// output report
name|resolveEngine
operator|.
name|outputReport
argument_list|(
name|report
argument_list|,
name|settings
operator|.
name|getResolutionCacheManager
argument_list|()
argument_list|,
name|resolveOptions
argument_list|)
expr_stmt|;
return|return
name|report
return|;
block|}
finally|finally
block|{
comment|// IVY-834: log the problems if there were any...
name|Message
operator|.
name|sumupProblems
argument_list|()
expr_stmt|;
name|resolveEngine
operator|.
name|setDictatorResolver
argument_list|(
name|oldDicator
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLogNotConvertedExclusionRule
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

