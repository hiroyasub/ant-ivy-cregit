begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|repository
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
name|Comparator
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
name|TreeSet
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|latest
operator|.
name|ArtifactInfo
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
name|matcher
operator|.
name|RegexpPatternMatcher
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
name|MemoryUtil
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
comment|/**  * The repository management can be used to load all metadata from a repository, analyze them, and  * provide a bunch of information about the whole repository state.  *<p>  * Since loading all metadata from a repository is not a light task, this engine should only be used  * on a machine having good access to the repository (on the same filesystem being usually the best  * suited).  *</p>  *<p>  * To access information, you usually have before to call a method to init the data: {@link #load()}  * is used to load repository metadata, {@link #analyze()} is used to analyze them. These methods  * being very time consuming, they must always be called explicitly.  *</p>  *<p>  * On a large repository, this engine can be very memory consuming to use, it is not suited to be  * used in a long running process, but rather in short process loading data and taking action about  * the current state of the repository.  *</p>  *<p>  * This engine is not intended to be used concurrently with publish, the order of repository loaded  * being nondeterministic and long, it could end up in having an inconsistent in memory state.  *</p>  *<p>  * For better performance, we strongly suggest using this engine with cache in useOrigin mode.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryManagementEngine
block|{
specifier|private
specifier|static
specifier|final
name|double
name|THOUSAND
init|=
literal|1000.0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|KILO
init|=
literal|1024
decl_stmt|;
comment|// /////////////////////////////////////////
comment|// state loaded on #load()
comment|// /////////////////////////////////////////
comment|/**      * True if the repository has already been loaded, false otherwise.      */
specifier|private
name|boolean
name|loaded
decl_stmt|;
comment|/**      * ModuleDescriptors stored by ModuleRevisionId      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|ModuleDescriptor
argument_list|>
name|revisions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * ModuleRevisionId for which loading was not possible, with corresponding error message.      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|errors
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * List of ModuleDescriptor per ModuleId.      */
specifier|private
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
argument_list|>
name|modules
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// /////////////////////////////////////////
comment|// state loaded on #analyze()
comment|// /////////////////////////////////////////
comment|/**      * True when the repository has been analyzed, false otherwise      */
specifier|private
name|boolean
name|analyzed
decl_stmt|;
comment|/**      * Cache from requested module revision id to actual module revision id.      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|ModuleRevisionId
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * list of dependers per ModuleRevisionId.      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|>
name|dependers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// /////////////////////////////////////////
comment|// dependencies
comment|// /////////////////////////////////////////
specifier|private
name|SearchEngine
name|searchEngine
decl_stmt|;
specifier|private
name|ResolveEngine
name|resolveEngine
decl_stmt|;
specifier|private
name|RepositoryManagementEngineSettings
name|settings
decl_stmt|;
specifier|public
name|RepositoryManagementEngine
parameter_list|(
name|RepositoryManagementEngineSettings
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
comment|/**      * Loads data from the repository.      *<p>      * This method usually takes a long time to proceed. It should never be called from event      * dispatch thread in a GUI.      *</p>      */
specifier|public
name|void
name|load
parameter_list|()
block|{
name|long
name|startingMemoryUse
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|settings
operator|.
name|dumpMemoryUsage
argument_list|()
condition|)
block|{
name|startingMemoryUse
operator|=
name|MemoryUtil
operator|.
name|getUsedMemory
argument_list|()
expr_stmt|;
block|}
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Message
operator|.
name|rawinfo
argument_list|(
literal|"searching modules... "
argument_list|)
expr_stmt|;
name|ModuleRevisionId
index|[]
name|mrids
init|=
name|searchModules
argument_list|()
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"loading repository metadata..."
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleRevisionId
name|mrid
range|:
name|mrids
control|)
block|{
try|try
block|{
name|loadModuleRevision
argument_list|(
name|mrid
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
name|debug
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|errors
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|endTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%nrepository loaded: %d modules; %d revisions; %s%ss"
argument_list|,
name|modules
operator|.
name|size
argument_list|()
argument_list|,
name|revisions
operator|.
name|size
argument_list|()
argument_list|,
name|settings
operator|.
name|dumpMemoryUsage
argument_list|()
condition|?
operator|(
name|MemoryUtil
operator|.
name|getUsedMemory
argument_list|()
operator|-
name|startingMemoryUse
operator|)
operator|/
name|KILO
operator|+
literal|"kB; "
else|:
literal|""
argument_list|,
operator|(
name|endTime
operator|-
name|startTime
operator|)
operator|/
name|THOUSAND
argument_list|)
argument_list|)
expr_stmt|;
name|loaded
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Analyze data in the repository.      *<p>      * This method may take a long time to proceed. It should never be called from event dispatch      * thread in a GUI.      *</p>      *      * @throws IllegalStateException      *             if the repository has not been loaded yet      * @see #load()      */
specifier|public
name|void
name|analyze
parameter_list|()
block|{
name|ensureLoaded
argument_list|()
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\nanalyzing dependencies..."
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|revisions
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|DependencyDescriptor
name|dd
range|:
name|md
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|ModuleRevisionId
name|dep
init|=
name|getDependency
argument_list|(
name|dd
argument_list|)
decl_stmt|;
if|if
condition|(
name|dep
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"inconsistent repository: declared dependency not found: "
operator|+
name|dd
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDependers
argument_list|(
name|dep
argument_list|)
operator|.
name|add
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
name|analyzed
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Returns the number of Module Revision in the repository.      *      * @return the number of module revisions in the repository.      * @throws IllegalStateException      *             if the repository has not been loaded yet      * @see #load()      */
specifier|public
name|int
name|getRevisionsNumber
parameter_list|()
block|{
name|ensureLoaded
argument_list|()
expr_stmt|;
return|return
name|revisions
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns the number of ModuleId in the repository.      *      * @return the number of ModuleId in the repository.      * @throws IllegalStateException      *             if the repository has not been loaded yet      * @see #load()      */
specifier|public
name|int
name|getModuleIdsNumber
parameter_list|()
block|{
name|ensureLoaded
argument_list|()
expr_stmt|;
return|return
name|modules
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns Module Revisions which have no dependers.      *      * @return a Collection of the {@link ModuleRevisionId} of module revisions which have no      *         dependers in the repository.      * @throws IllegalStateException      *             if the repository has not been analyzed yet      * @see #analyze()      */
specifier|public
name|Collection
argument_list|<
name|ModuleRevisionId
argument_list|>
name|getOrphans
parameter_list|()
block|{
name|ensureAnalyzed
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|ModuleRevisionId
argument_list|>
name|orphans
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|revisions
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|orphans
operator|.
name|removeAll
argument_list|(
name|dependers
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|orphans
return|;
block|}
specifier|private
name|ModuleRevisionId
index|[]
name|searchModules
parameter_list|()
block|{
return|return
name|searchEngine
operator|.
name|listModules
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|)
argument_list|,
name|RegexpPatternMatcher
operator|.
name|INSTANCE
argument_list|)
return|;
block|}
specifier|private
name|ModuleRevisionId
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
name|ModuleRevisionId
name|askedMrid
init|=
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
name|VersionMatcher
name|vmatcher
init|=
name|settings
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
if|if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|askedMrid
argument_list|)
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|cache
operator|.
name|get
argument_list|(
name|askedMrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|mrid
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|getAllRevisions
argument_list|(
name|askedMrid
argument_list|)
control|)
block|{
if|if
condition|(
name|vmatcher
operator|.
name|needModuleDescriptor
argument_list|(
name|askedMrid
argument_list|,
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|vmatcher
operator|.
name|accept
argument_list|(
name|askedMrid
argument_list|,
name|md
argument_list|)
condition|)
block|{
name|mrid
operator|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
if|if
condition|(
name|vmatcher
operator|.
name|accept
argument_list|(
name|askedMrid
argument_list|,
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
condition|)
block|{
name|mrid
operator|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|mrid
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|cache
operator|.
name|put
argument_list|(
name|askedMrid
argument_list|,
name|mrid
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mrid
return|;
block|}
else|else
block|{
return|return
name|askedMrid
return|;
block|}
block|}
specifier|private
name|Collection
argument_list|<
name|ModuleRevisionId
argument_list|>
name|getDependers
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
name|depders
init|=
name|dependers
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|depders
operator|==
literal|null
condition|)
block|{
name|depders
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|dependers
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|depders
argument_list|)
expr_stmt|;
block|}
return|return
name|depders
return|;
block|}
specifier|private
name|void
name|loadModuleRevision
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
throws|throws
name|Exception
block|{
name|ResolvedModuleRevision
name|module
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|mrid
argument_list|)
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid
argument_list|,
literal|false
argument_list|)
argument_list|,
name|newResolveData
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"module not found while listed: "
operator|+
name|mrid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|revisions
operator|.
name|put
argument_list|(
name|module
operator|.
name|getId
argument_list|()
argument_list|,
name|module
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|getAllRevisions
argument_list|(
name|module
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
name|module
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|getAllRevisions
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|)
block|{
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|revisions
init|=
name|modules
operator|.
name|get
argument_list|(
name|id
operator|.
name|getModuleId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|revisions
operator|==
literal|null
condition|)
block|{
name|revisions
operator|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
operator|new
name|Comparator
argument_list|<
name|ModuleDescriptor
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ModuleDescriptor
name|md1
parameter_list|,
name|ModuleDescriptor
name|md2
parameter_list|)
block|{
comment|// we use reverse order compared to latest revision, to have latest revision
comment|// first
return|return
name|settings
operator|.
name|getDefaultLatestStrategy
argument_list|()
operator|.
name|sort
argument_list|(
operator|new
name|ArtifactInfo
index|[]
block|{
name|md1
operator|,
name|md2
block|}
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|md1
argument_list|)
condition|?
literal|1
else|:
operator|-
literal|1
expr_stmt|;
block|}
block|}
block|)
class|;
end_class

begin_expr_stmt
name|modules
operator|.
name|put
argument_list|(
name|id
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|revisions
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
unit|}         return
name|revisions
expr_stmt|;
end_expr_stmt

begin_function
unit|}      private
name|ResolveData
name|newResolveData
parameter_list|()
block|{
return|return
operator|new
name|ResolveData
argument_list|(
name|resolveEngine
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
specifier|private
name|void
name|ensureAnalyzed
parameter_list|()
block|{
if|if
condition|(
operator|!
name|analyzed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"repository must have been analyzed to perform this method"
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
specifier|private
name|void
name|ensureLoaded
parameter_list|()
block|{
if|if
condition|(
operator|!
name|loaded
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"repository must have be loaded to perform this method"
argument_list|)
throw|;
block|}
block|}
end_function

unit|}
end_unit

