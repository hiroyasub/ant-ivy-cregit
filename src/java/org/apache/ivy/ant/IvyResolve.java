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
name|net
operator|.
name|MalformedURLException
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
name|LogOptions
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
name|DefaultExcludeRule
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
name|ExtendsDescriptor
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
name|ResolveProcessException
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
name|FilterHelper
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
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_comment
comment|/**  * This task allow to call the Ivy dependency resolution from ant.  */
end_comment

begin_class
specifier|public
class|class
name|IvyResolve
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|file
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|conf
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|organisation
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|module
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|branch
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|revision
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|pubdate
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|inline
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|showProgress
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|useCacheOnly
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|type
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|transitive
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|refresh
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|changing
init|=
literal|false
decl_stmt|;
specifier|private
name|Boolean
name|keep
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|failureProperty
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|useOrigin
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|resolveMode
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|resolveId
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|log
init|=
name|ResolveOptions
operator|.
name|LOG_DEFAULT
decl_stmt|;
specifier|private
name|boolean
name|checkIfChanged
init|=
literal|true
decl_stmt|;
comment|// for backward compatibility
specifier|private
name|List
argument_list|<
name|IvyDependency
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyDependency
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IvyExclude
argument_list|>
name|excludes
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyExclude
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IvyConflict
argument_list|>
name|conflicts
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyConflict
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|isUseOrigin
parameter_list|()
block|{
return|return
name|useOrigin
return|;
block|}
specifier|public
name|void
name|setUseOrigin
parameter_list|(
name|boolean
name|useOrigin
parameter_list|)
block|{
name|this
operator|.
name|useOrigin
operator|=
name|useOrigin
expr_stmt|;
block|}
specifier|public
name|String
name|getDate
parameter_list|()
block|{
return|return
name|pubdate
return|;
block|}
specifier|public
name|void
name|setDate
parameter_list|(
name|String
name|pubdate
parameter_list|)
block|{
name|this
operator|.
name|pubdate
operator|=
name|pubdate
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
block|}
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
specifier|public
name|void
name|setBranch
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|cacheAttributeNotSupported
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
name|void
name|setConf
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHaltonfailure
parameter_list|()
block|{
return|return
name|haltOnFailure
return|;
block|}
specifier|public
name|void
name|setHaltonfailure
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|)
block|{
name|this
operator|.
name|haltOnFailure
operator|=
name|haltOnFailure
expr_stmt|;
block|}
specifier|public
name|void
name|setShowprogress
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
name|this
operator|.
name|showProgress
operator|=
name|show
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseCacheOnly
parameter_list|()
block|{
return|return
name|useCacheOnly
return|;
block|}
specifier|public
name|void
name|setUseCacheOnly
parameter_list|(
name|boolean
name|useCacheOnly
parameter_list|)
block|{
name|this
operator|.
name|useCacheOnly
operator|=
name|useCacheOnly
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRefresh
parameter_list|()
block|{
return|return
name|refresh
return|;
block|}
specifier|public
name|void
name|setRefresh
parameter_list|(
name|boolean
name|refresh
parameter_list|)
block|{
name|this
operator|.
name|refresh
operator|=
name|refresh
expr_stmt|;
block|}
specifier|public
name|String
name|getLog
parameter_list|()
block|{
return|return
name|log
return|;
block|}
specifier|public
name|void
name|setLog
parameter_list|(
name|String
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
comment|/**      * @deprecated Use {@link #setFailureProperty(String)} instead      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setFailurePropery
parameter_list|(
name|String
name|failureProperty
parameter_list|)
block|{
name|log
argument_list|(
literal|"The 'failurepropery' attribute is deprecated. "
operator|+
literal|"Please use the 'failureproperty' attribute instead"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
name|setFailureProperty
argument_list|(
name|failureProperty
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setFailureProperty
parameter_list|(
name|String
name|failureProperty
parameter_list|)
block|{
name|this
operator|.
name|failureProperty
operator|=
name|failureProperty
expr_stmt|;
block|}
specifier|public
name|String
name|getFailureProperty
parameter_list|()
block|{
return|return
name|failureProperty
return|;
block|}
specifier|public
name|IvyDependency
name|createDependency
parameter_list|()
block|{
name|IvyDependency
name|dep
init|=
operator|new
name|IvyDependency
argument_list|()
decl_stmt|;
name|dependencies
operator|.
name|add
argument_list|(
name|dep
argument_list|)
expr_stmt|;
return|return
name|dep
return|;
block|}
specifier|public
name|IvyExclude
name|createExclude
parameter_list|()
block|{
name|IvyExclude
name|ex
init|=
operator|new
name|IvyExclude
argument_list|()
decl_stmt|;
name|excludes
operator|.
name|add
argument_list|(
name|ex
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
specifier|public
name|IvyConflict
name|createConflict
parameter_list|()
block|{
name|IvyConflict
name|c
init|=
operator|new
name|IvyConflict
argument_list|()
decl_stmt|;
name|conflicts
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|prepareTask
parameter_list|()
block|{
name|super
operator|.
name|prepareTask
argument_list|()
expr_stmt|;
name|Message
operator|.
name|setShowProgress
argument_list|(
name|showProgress
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
try|try
block|{
name|conf
operator|=
name|getProperty
argument_list|(
name|conf
argument_list|,
name|settings
argument_list|,
literal|"ivy.configurations"
argument_list|)
expr_stmt|;
name|type
operator|=
name|getProperty
argument_list|(
name|type
argument_list|,
name|settings
argument_list|,
literal|"ivy.resolve.default.type.filter"
argument_list|)
expr_stmt|;
name|String
index|[]
name|confs
init|=
name|splitConfs
argument_list|(
name|conf
argument_list|)
decl_stmt|;
name|boolean
name|childs
init|=
operator|!
name|dependencies
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|excludes
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|conflicts
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|ResolveReport
name|report
decl_stmt|;
if|if
condition|(
name|childs
condition|)
block|{
if|if
condition|(
name|isInline
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"the inline mode is incompatible with child elements"
argument_list|)
throw|;
block|}
if|if
condition|(
name|organisation
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'organisation' is not allowed with child elements"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'module' is not allowed with child elements"
argument_list|)
throw|;
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'file' not allowed with child elements"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|getAllowedLogOptions
argument_list|()
operator|.
name|contains
argument_list|(
name|log
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"invalid option for 'log': "
operator|+
name|log
operator|+
literal|". Available options are "
operator|+
name|getAllowedLogOptions
argument_list|()
argument_list|)
throw|;
block|}
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultModuleDescriptor
name|md
init|=
name|DefaultModuleDescriptor
operator|.
name|newBasicInstance
argument_list|(
name|mrid
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|IvyDependency
name|dep
range|:
name|dependencies
control|)
block|{
name|DependencyDescriptor
name|dd
init|=
name|dep
operator|.
name|asDependencyDescriptor
argument_list|(
name|md
argument_list|,
literal|"default"
argument_list|,
name|settings
argument_list|)
decl_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyExclude
name|exclude
range|:
name|excludes
control|)
block|{
name|DefaultExcludeRule
name|rule
init|=
name|exclude
operator|.
name|asRule
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|rule
operator|.
name|addConfiguration
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|md
operator|.
name|addExcludeRule
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyConflict
name|conflict
range|:
name|conflicts
control|)
block|{
name|conflict
operator|.
name|addConflict
argument_list|(
name|md
argument_list|,
name|settings
argument_list|)
expr_stmt|;
block|}
name|report
operator|=
name|ivy
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|,
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|isInline
argument_list|()
condition|)
block|{
if|if
condition|(
name|organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'organisation' is required when using inline mode"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'module' is required when using inline mode"
argument_list|)
throw|;
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'file' not allowed when using inline mode"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|getAllowedLogOptions
argument_list|()
operator|.
name|contains
argument_list|(
name|log
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"invalid option for 'log': "
operator|+
name|log
operator|+
literal|". Available options are "
operator|+
name|getAllowedLogOptions
argument_list|()
argument_list|)
throw|;
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
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|confs
index|[
name|i
index|]
operator|=
literal|"*(public)"
expr_stmt|;
block|}
block|}
if|if
condition|(
name|revision
operator|==
literal|null
condition|)
block|{
name|revision
operator|=
literal|"latest.integration"
expr_stmt|;
block|}
name|report
operator|=
name|ivy
operator|.
name|resolve
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy
argument_list|,
name|confs
argument_list|,
name|settings
argument_list|)
argument_list|,
name|changing
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|organisation
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'organisation' not allowed when not using 'org' attribute"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'module' not allowed when not using 'org' attribute"
argument_list|)
throw|;
block|}
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
name|file
operator|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|getProperty
argument_list|(
name|settings
argument_list|,
literal|"ivy.dep.file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|report
operator|=
name|ivy
operator|.
name|resolve
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy
argument_list|,
name|confs
argument_list|,
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|report
operator|.
name|hasError
argument_list|()
condition|)
block|{
if|if
condition|(
name|failureProperty
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
name|failureProperty
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isHaltonfailure
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"resolve failed - see output for details"
argument_list|)
throw|;
block|}
block|}
name|setResolved
argument_list|(
name|report
argument_list|,
name|resolveId
argument_list|,
name|isKeep
argument_list|()
argument_list|)
expr_stmt|;
name|confs
operator|=
name|report
operator|.
name|getConfigurations
argument_list|()
expr_stmt|;
if|if
condition|(
name|isKeep
argument_list|()
condition|)
block|{
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
comment|// put resolved infos in ant properties and ivy variables
comment|// putting them in ivy variables is important to be able to change from one resolve
comment|// call to the other
name|String
name|mdOrg
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
decl_stmt|;
name|String
name|mdName
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|mdRev
init|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.organisation"
argument_list|,
name|mdOrg
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.organisation"
argument_list|,
name|mdOrg
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.module"
argument_list|,
name|mdName
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.module"
argument_list|,
name|mdName
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.revision"
argument_list|,
name|mdRev
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.revision"
argument_list|,
name|mdRev
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
name|md
operator|.
name|getInheritedDescriptors
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ExtendsDescriptor
name|parent
init|=
name|md
operator|.
name|getInheritedDescriptors
argument_list|()
index|[
name|i
index|]
decl_stmt|;
name|String
name|parentOrg
init|=
name|parent
operator|.
name|getResolvedParentRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
decl_stmt|;
name|String
name|parentModule
init|=
name|parent
operator|.
name|getResolvedParentRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|parentRevision
init|=
name|parent
operator|.
name|getResolvedParentRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|String
name|parentBranch
init|=
name|parent
operator|.
name|getResolvedParentRevisionId
argument_list|()
operator|.
name|getBranch
argument_list|()
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].organisation"
argument_list|,
name|parentOrg
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].organisation"
argument_list|,
name|parentOrg
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].module"
argument_list|,
name|parentModule
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].module"
argument_list|,
name|parentModule
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].revision"
argument_list|,
name|parentRevision
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].revision"
argument_list|,
name|parentRevision
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentBranch
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].branch"
argument_list|,
name|parentBranch
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.parent["
operator|+
name|i
operator|+
literal|"].branch"
argument_list|,
name|parentBranch
argument_list|)
expr_stmt|;
block|}
block|}
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.parents.count"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|md
operator|.
name|getInheritedDescriptors
argument_list|()
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.parents.count"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|md
operator|.
name|getInheritedDescriptors
argument_list|()
operator|.
name|length
argument_list|)
argument_list|)
expr_stmt|;
name|Boolean
name|hasChanged
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getCheckIfChanged
argument_list|()
condition|)
block|{
name|hasChanged
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|hasChanged
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.deps.changed"
argument_list|,
name|hasChanged
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|,
name|hasChanged
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|mergeConfs
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|mergeConfs
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.file"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.file"
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resolveId
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.organisation."
operator|+
name|resolveId
argument_list|,
name|mdOrg
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.organisation."
operator|+
name|resolveId
argument_list|,
name|mdOrg
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.module."
operator|+
name|resolveId
argument_list|,
name|mdName
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.module."
operator|+
name|resolveId
argument_list|,
name|mdName
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.revision."
operator|+
name|resolveId
argument_list|,
name|mdRev
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.revision."
operator|+
name|resolveId
argument_list|,
name|mdRev
argument_list|)
expr_stmt|;
if|if
condition|(
name|getCheckIfChanged
argument_list|()
condition|)
block|{
comment|// hasChanged has already been set earlier
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.deps.changed."
operator|+
name|resolveId
argument_list|,
name|hasChanged
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.deps.changed."
operator|+
name|resolveId
argument_list|,
name|hasChanged
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.configurations."
operator|+
name|resolveId
argument_list|,
name|mergeConfs
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.configurations."
operator|+
name|resolveId
argument_list|,
name|mergeConfs
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.file."
operator|+
name|resolveId
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.file."
operator|+
name|resolveId
argument_list|,
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to convert given ivy file to url: "
operator|+
name|file
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"syntax errors in ivy file: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ResolveProcessException
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
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
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
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|getAllowedLogOptions
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|LogOptions
operator|.
name|LOG_DEFAULT
argument_list|,
name|LogOptions
operator|.
name|LOG_DOWNLOAD_ONLY
argument_list|,
name|LogOptions
operator|.
name|LOG_QUIET
argument_list|)
return|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|String
index|[]
name|confs
parameter_list|,
name|IvySettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|useOrigin
condition|)
block|{
name|settings
operator|.
name|useDeprecatedUseOrigin
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
operator|(
name|ResolveOptions
operator|)
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setLog
argument_list|(
name|log
argument_list|)
operator|)
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|)
operator|.
name|setArtifactFilter
argument_list|(
name|FilterHelper
operator|.
name|getArtifactTypeFilter
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|setRevision
argument_list|(
name|revision
argument_list|)
operator|.
name|setDate
argument_list|(
name|getPubDate
argument_list|(
name|pubdate
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|setUseCacheOnly
argument_list|(
name|useCacheOnly
argument_list|)
operator|.
name|setRefresh
argument_list|(
name|refresh
argument_list|)
operator|.
name|setTransitive
argument_list|(
name|transitive
argument_list|)
operator|.
name|setResolveMode
argument_list|(
name|resolveMode
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|resolveId
argument_list|)
operator|.
name|setCheckIfChanged
argument_list|(
name|checkIfChanged
argument_list|)
return|;
block|}
specifier|public
name|String
name|getModule
parameter_list|()
block|{
return|return
name|module
return|;
block|}
specifier|public
name|void
name|setModule
parameter_list|(
name|String
name|module
parameter_list|)
block|{
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|organisation
return|;
block|}
specifier|public
name|void
name|setOrganisation
parameter_list|(
name|String
name|organisation
parameter_list|)
block|{
name|this
operator|.
name|organisation
operator|=
name|organisation
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|transitive
return|;
block|}
specifier|public
name|void
name|setTransitive
parameter_list|(
name|boolean
name|transitive
parameter_list|)
block|{
name|this
operator|.
name|transitive
operator|=
name|transitive
expr_stmt|;
block|}
specifier|public
name|boolean
name|isChanging
parameter_list|()
block|{
return|return
name|changing
return|;
block|}
specifier|public
name|void
name|setChanging
parameter_list|(
name|boolean
name|changing
parameter_list|)
block|{
name|this
operator|.
name|changing
operator|=
name|changing
expr_stmt|;
block|}
specifier|public
name|boolean
name|isKeep
parameter_list|()
block|{
return|return
name|keep
operator|==
literal|null
condition|?
name|organisation
operator|==
literal|null
else|:
name|keep
operator|.
name|booleanValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setKeep
parameter_list|(
name|boolean
name|keep
parameter_list|)
block|{
name|this
operator|.
name|keep
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|keep
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInline
parameter_list|()
block|{
return|return
name|inline
return|;
block|}
specifier|public
name|void
name|setInline
parameter_list|(
name|boolean
name|inline
parameter_list|)
block|{
name|this
operator|.
name|inline
operator|=
name|inline
expr_stmt|;
block|}
specifier|public
name|String
name|getResolveId
parameter_list|()
block|{
return|return
name|resolveId
return|;
block|}
specifier|public
name|void
name|setResolveId
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
name|this
operator|.
name|resolveId
operator|=
name|resolveId
expr_stmt|;
block|}
specifier|public
name|String
name|getResolveMode
parameter_list|()
block|{
return|return
name|resolveMode
return|;
block|}
specifier|public
name|void
name|setResolveMode
parameter_list|(
name|String
name|resolveMode
parameter_list|)
block|{
name|this
operator|.
name|resolveMode
operator|=
name|resolveMode
expr_stmt|;
block|}
specifier|public
name|boolean
name|getCheckIfChanged
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

