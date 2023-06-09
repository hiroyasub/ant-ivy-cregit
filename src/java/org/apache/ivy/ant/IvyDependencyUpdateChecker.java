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
name|ant
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
name|ResolveOptions
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|splitToArray
import|;
end_import

begin_class
specifier|public
class|class
name|IvyDependencyUpdateChecker
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
name|String
name|revisionToCheck
init|=
literal|"latest.integration"
decl_stmt|;
specifier|private
name|boolean
name|download
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|checkIfChanged
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|showTransitive
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
name|ModuleDescriptor
name|originalModuleDescriptor
init|=
name|getResolvedReport
argument_list|()
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
comment|// clone module descriptor
name|DefaultModuleDescriptor
name|latestModuleDescriptor
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|originalModuleDescriptor
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|originalModuleDescriptor
operator|.
name|getStatus
argument_list|()
argument_list|,
name|originalModuleDescriptor
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
decl_stmt|;
comment|// copy configurations
for|for
control|(
name|Configuration
name|configuration
range|:
name|originalModuleDescriptor
operator|.
name|getConfigurations
argument_list|()
control|)
block|{
name|latestModuleDescriptor
operator|.
name|addConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|// clone dependency and add new one with the requested revisionToCheck
for|for
control|(
name|DependencyDescriptor
name|dependencyDescriptor
range|:
name|originalModuleDescriptor
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|ModuleRevisionId
name|upToDateMrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dependencyDescriptor
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|revisionToCheck
argument_list|)
decl_stmt|;
name|latestModuleDescriptor
operator|.
name|addDependency
argument_list|(
name|dependencyDescriptor
operator|.
name|clone
argument_list|(
name|upToDateMrid
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// resolve
name|ResolveOptions
name|resolveOptions
init|=
operator|new
name|ResolveOptions
argument_list|()
decl_stmt|;
name|resolveOptions
operator|.
name|setDownload
argument_list|(
name|isDownload
argument_list|()
argument_list|)
expr_stmt|;
name|resolveOptions
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
expr_stmt|;
name|resolveOptions
operator|.
name|setConfs
argument_list|(
name|splitToArray
argument_list|(
name|getConf
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resolveOptions
operator|.
name|setCheckIfChanged
argument_list|(
name|checkIfChanged
argument_list|)
expr_stmt|;
name|ResolveReport
name|latestReport
decl_stmt|;
try|try
block|{
name|latestReport
operator|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolveEngine
argument_list|()
operator|.
name|resolve
argument_list|(
name|latestModuleDescriptor
argument_list|,
name|resolveOptions
argument_list|)
expr_stmt|;
name|displayDependencyUpdates
argument_list|(
name|getResolvedReport
argument_list|()
argument_list|,
name|latestReport
argument_list|)
expr_stmt|;
if|if
condition|(
name|showTransitive
condition|)
block|{
name|displayNewDependencyOnLatest
argument_list|(
name|getResolvedReport
argument_list|()
argument_list|,
name|latestReport
argument_list|)
expr_stmt|;
name|displayMissingDependencyOnLatest
argument_list|(
name|getResolvedReport
argument_list|()
argument_list|,
name|latestReport
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to resolve dependencies:\n\t"
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|displayDependencyUpdates
parameter_list|(
name|ResolveReport
name|originalReport
parameter_list|,
name|ResolveReport
name|latestReport
parameter_list|)
block|{
name|log
argument_list|(
literal|"Dependencies updates available :"
argument_list|)
expr_stmt|;
name|boolean
name|dependencyUpdateDetected
init|=
literal|false
decl_stmt|;
for|for
control|(
name|IvyNode
name|latest
range|:
name|latestReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
for|for
control|(
name|IvyNode
name|originalDependency
range|:
name|originalReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|originalDependency
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|latest
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|originalDependency
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|latest
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
comment|// is this dependency a transitive or a direct dependency?
comment|// (unfortunately .isTransitive() methods do not have the same meaning)
name|boolean
name|isTransitiveDependency
init|=
name|latest
operator|.
name|getDependencyDescriptor
argument_list|(
name|latest
operator|.
name|getRoot
argument_list|()
argument_list|)
operator|==
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isTransitiveDependency
operator|||
name|showTransitive
condition|)
block|{
name|log
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"\t%s#%s%s\t%s -> %s"
argument_list|,
name|originalDependency
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|originalDependency
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|isTransitiveDependency
condition|?
literal|" (transitive)"
else|:
literal|""
argument_list|,
name|originalDependency
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|,
name|latest
operator|.
name|getResolvedId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dependencyUpdateDetected
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|dependencyUpdateDetected
condition|)
block|{
name|log
argument_list|(
literal|"\tAll dependencies are up to date"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|displayMissingDependencyOnLatest
parameter_list|(
name|ResolveReport
name|originalReport
parameter_list|,
name|ResolveReport
name|latestReport
parameter_list|)
block|{
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
name|listOfMissingDependencyOnLatest
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|originalDependency
range|:
name|originalReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|boolean
name|dependencyFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|IvyNode
name|latest
range|:
name|latestReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|originalDependency
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|latest
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
name|dependencyFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dependencyFound
condition|)
block|{
name|listOfMissingDependencyOnLatest
operator|.
name|add
argument_list|(
name|originalDependency
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|listOfMissingDependencyOnLatest
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|log
argument_list|(
literal|"List of missing dependency on latest resolve :"
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleRevisionId
name|moduleRevisionId
range|:
name|listOfMissingDependencyOnLatest
control|)
block|{
name|log
argument_list|(
literal|"\t"
operator|+
name|moduleRevisionId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|displayNewDependencyOnLatest
parameter_list|(
name|ResolveReport
name|originalReport
parameter_list|,
name|ResolveReport
name|latestReport
parameter_list|)
block|{
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
name|listOfNewDependencyOnLatest
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|latest
range|:
name|latestReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|boolean
name|dependencyFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|IvyNode
name|originalDependency
range|:
name|originalReport
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|originalDependency
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|latest
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
name|dependencyFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dependencyFound
condition|)
block|{
name|listOfNewDependencyOnLatest
operator|.
name|add
argument_list|(
name|latest
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|listOfNewDependencyOnLatest
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|log
argument_list|(
literal|"List of new dependency on latest resolve :"
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleRevisionId
name|moduleRevisionId
range|:
name|listOfNewDependencyOnLatest
control|)
block|{
name|log
argument_list|(
literal|"\t"
operator|+
name|moduleRevisionId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getRevisionToCheck
parameter_list|()
block|{
return|return
name|revisionToCheck
return|;
block|}
specifier|public
name|void
name|setRevisionToCheck
parameter_list|(
name|String
name|revisionToCheck
parameter_list|)
block|{
name|this
operator|.
name|revisionToCheck
operator|=
name|revisionToCheck
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDownload
parameter_list|()
block|{
return|return
name|download
return|;
block|}
specifier|public
name|void
name|setDownload
parameter_list|(
name|boolean
name|download
parameter_list|)
block|{
name|this
operator|.
name|download
operator|=
name|download
expr_stmt|;
block|}
specifier|public
name|boolean
name|isShowTransitive
parameter_list|()
block|{
return|return
name|showTransitive
return|;
block|}
specifier|public
name|void
name|setShowTransitive
parameter_list|(
name|boolean
name|showTransitive
parameter_list|)
block|{
name|this
operator|.
name|showTransitive
operator|=
name|showTransitive
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCheckIfChanged
parameter_list|()
block|{
return|return
name|checkIfChanged
return|;
block|}
specifier|public
name|void
name|setCheckIfChanged
parameter_list|(
name|boolean
name|checkIfChanged
parameter_list|)
block|{
name|this
operator|.
name|checkIfChanged
operator|=
name|checkIfChanged
expr_stmt|;
block|}
block|}
end_class

end_unit

