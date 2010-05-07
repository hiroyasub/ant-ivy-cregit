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
name|URL
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
name|HashMap
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
name|IvyPatternHelper
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
name|id
operator|.
name|ArtifactRevisionId
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
name|publish
operator|.
name|PublishOptions
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
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_comment
comment|/**  * This task allow to publish a module revision to an Ivy repository.  */
end_comment

begin_class
specifier|public
class|class
name|IvyPublish
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|organisation
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|private
name|String
name|revision
decl_stmt|;
specifier|private
name|String
name|pubRevision
decl_stmt|;
specifier|private
name|String
name|srcivypattern
decl_stmt|;
specifier|private
name|String
name|status
decl_stmt|;
specifier|private
name|String
name|conf
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|pubdate
decl_stmt|;
specifier|private
name|String
name|deliverTarget
decl_stmt|;
specifier|private
name|String
name|publishResolverName
init|=
literal|null
decl_stmt|;
specifier|private
name|List
name|artifactspattern
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|File
name|deliveryList
decl_stmt|;
specifier|private
name|boolean
name|publishivy
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|warnonmissing
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|haltonmissing
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|overwrite
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|update
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|merge
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|replacedynamicrev
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|forcedeliver
decl_stmt|;
specifier|private
name|Collection
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|pubBranch
decl_stmt|;
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
name|getSrcivypattern
parameter_list|()
block|{
return|return
name|srcivypattern
return|;
block|}
specifier|public
name|void
name|setSrcivypattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|srcivypattern
operator|=
name|destivypattern
expr_stmt|;
block|}
comment|/**      * @deprecated use {@link #getSrcivypattern()} instead.      */
specifier|public
name|String
name|getDeliverivypattern
parameter_list|()
block|{
return|return
name|srcivypattern
return|;
block|}
comment|/**      * @deprecated use {@link #setSrcivypattern(String)} instead.      */
specifier|public
name|void
name|setDeliverivypattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|srcivypattern
operator|=
name|destivypattern
expr_stmt|;
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
name|String
name|getPubdate
parameter_list|()
block|{
return|return
name|pubdate
return|;
block|}
specifier|public
name|void
name|setPubdate
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
name|getPubrevision
parameter_list|()
block|{
return|return
name|pubRevision
return|;
block|}
specifier|public
name|void
name|setPubrevision
parameter_list|(
name|String
name|pubRevision
parameter_list|)
block|{
name|this
operator|.
name|pubRevision
operator|=
name|pubRevision
expr_stmt|;
block|}
specifier|public
name|String
name|getPubbranch
parameter_list|()
block|{
return|return
name|pubBranch
return|;
block|}
specifier|public
name|void
name|setPubbranch
parameter_list|(
name|String
name|pubBranch
parameter_list|)
block|{
name|this
operator|.
name|pubBranch
operator|=
name|pubBranch
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
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
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
name|void
name|setDelivertarget
parameter_list|(
name|String
name|deliverTarget
parameter_list|)
block|{
name|this
operator|.
name|deliverTarget
operator|=
name|deliverTarget
expr_stmt|;
block|}
specifier|public
name|void
name|setDeliveryList
parameter_list|(
name|File
name|deliveryList
parameter_list|)
block|{
name|this
operator|.
name|deliveryList
operator|=
name|deliveryList
expr_stmt|;
block|}
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|publishResolverName
return|;
block|}
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|publishResolverName
parameter_list|)
block|{
name|this
operator|.
name|publishResolverName
operator|=
name|publishResolverName
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactspattern
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
operator|(
name|artifactspattern
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|artifactspattern
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
return|;
block|}
specifier|public
name|void
name|setArtifactspattern
parameter_list|(
name|String
name|artifactsPattern
parameter_list|)
block|{
name|artifactspattern
operator|.
name|clear
argument_list|()
expr_stmt|;
name|artifactspattern
operator|.
name|add
argument_list|(
name|artifactsPattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addArtifactspattern
parameter_list|(
name|String
name|artifactsPattern
parameter_list|)
block|{
name|artifactspattern
operator|.
name|add
argument_list|(
name|artifactsPattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredArtifacts
parameter_list|(
name|ArtifactsPattern
name|p
parameter_list|)
block|{
name|artifactspattern
operator|.
name|add
argument_list|(
name|p
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReplacedynamicrev
parameter_list|()
block|{
return|return
name|replacedynamicrev
return|;
block|}
specifier|public
name|void
name|setReplacedynamicrev
parameter_list|(
name|boolean
name|replacedynamicrev
parameter_list|)
block|{
name|this
operator|.
name|replacedynamicrev
operator|=
name|replacedynamicrev
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMerge
parameter_list|()
block|{
return|return
name|merge
return|;
block|}
specifier|public
name|void
name|setMerge
parameter_list|(
name|boolean
name|merge
parameter_list|)
block|{
name|this
operator|.
name|merge
operator|=
name|merge
expr_stmt|;
block|}
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
name|organisation
operator|=
name|getProperty
argument_list|(
name|organisation
argument_list|,
name|settings
argument_list|,
literal|"ivy.organisation"
argument_list|)
expr_stmt|;
name|module
operator|=
name|getProperty
argument_list|(
name|module
argument_list|,
name|settings
argument_list|,
literal|"ivy.module"
argument_list|)
expr_stmt|;
name|revision
operator|=
name|getProperty
argument_list|(
name|revision
argument_list|,
name|settings
argument_list|,
literal|"ivy.revision"
argument_list|)
expr_stmt|;
name|pubBranch
operator|=
name|getProperty
argument_list|(
name|pubBranch
argument_list|,
name|settings
argument_list|,
literal|"ivy.deliver.branch"
argument_list|)
expr_stmt|;
name|pubRevision
operator|=
name|getProperty
argument_list|(
name|pubRevision
argument_list|,
name|settings
argument_list|,
literal|"ivy.deliver.revision"
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifactspattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|p
init|=
name|getProperty
argument_list|(
literal|null
argument_list|,
name|settings
argument_list|,
literal|"ivy.publish.src.artifacts.pattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|artifactspattern
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|srcivypattern
operator|==
literal|null
condition|)
block|{
name|srcivypattern
operator|=
name|getArtifactspattern
argument_list|()
expr_stmt|;
block|}
name|status
operator|=
name|getProperty
argument_list|(
name|status
argument_list|,
name|settings
argument_list|,
literal|"ivy.status"
argument_list|)
expr_stmt|;
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
literal|"no organisation provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'organisation' "
operator|+
literal|"or via 'ivy.organisation' property or a prior call to<resolve/>"
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
literal|"no module name provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'module' "
operator|+
literal|"or via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|revision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module revision provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'revision' "
operator|+
literal|"or via 'ivy.revision' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|artifactspattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no artifacts pattern: either provide it through parameter or "
operator|+
literal|"through ivy.publish.src.artifacts.pattern property"
argument_list|)
throw|;
block|}
if|if
condition|(
name|publishResolverName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no publish deliver name: please provide it through parameter 'resolver'"
argument_list|)
throw|;
block|}
if|if
condition|(
literal|"working"
operator|.
name|equals
argument_list|(
name|revision
argument_list|)
condition|)
block|{
name|revision
operator|=
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
expr_stmt|;
block|}
name|Date
name|pubdate
init|=
name|getPubDate
argument_list|(
name|this
operator|.
name|pubdate
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pubRevision
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|revision
operator|.
name|startsWith
argument_list|(
literal|"working@"
argument_list|)
condition|)
block|{
name|pubRevision
operator|=
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|pubdate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pubRevision
operator|=
name|revision
expr_stmt|;
block|}
block|}
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no status provided: either provide it as parameter "
operator|+
literal|"or through the ivy.status.default property"
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
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|)
decl_stmt|;
try|try
block|{
name|File
name|ivyFile
init|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|srcivypattern
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|pubRevision
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|publishivy
operator|&&
operator|(
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
operator|||
name|forcedeliver
operator|)
condition|)
block|{
name|IvyDeliver
name|deliver
init|=
operator|new
name|IvyDeliver
argument_list|()
decl_stmt|;
name|deliver
operator|.
name|setSettingsRef
argument_list|(
name|getSettingsRef
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setTaskName
argument_list|(
name|getTaskName
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDeliverpattern
argument_list|(
name|getSrcivypattern
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDelivertarget
argument_list|(
name|deliverTarget
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDeliveryList
argument_list|(
name|deliveryList
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setModule
argument_list|(
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setOrganisation
argument_list|(
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setPubdate
argument_list|(
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|pubdate
argument_list|)
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setPubrevision
argument_list|(
name|getPubrevision
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setPubbranch
argument_list|(
name|getPubbranch
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setRevision
argument_list|(
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setStatus
argument_list|(
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setReplacedynamicrev
argument_list|(
name|isReplacedynamicrev
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setMerge
argument_list|(
name|merge
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setConf
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|ivy
operator|.
name|publish
argument_list|(
name|mrid
argument_list|,
name|artifactspattern
argument_list|,
name|publishResolverName
argument_list|,
operator|new
name|PublishOptions
argument_list|()
operator|.
name|setPubrevision
argument_list|(
name|getPubrevision
argument_list|()
argument_list|)
operator|.
name|setPubbranch
argument_list|(
name|getPubbranch
argument_list|()
argument_list|)
operator|.
name|setSrcIvyPattern
argument_list|(
name|publishivy
condition|?
name|srcivypattern
else|:
literal|null
argument_list|)
operator|.
name|setStatus
argument_list|(
name|getStatus
argument_list|()
argument_list|)
operator|.
name|setPubdate
argument_list|(
name|pubdate
argument_list|)
operator|.
name|setExtraArtifacts
argument_list|(
operator|(
name|Artifact
index|[]
operator|)
name|artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
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
name|setOverwrite
argument_list|(
name|overwrite
argument_list|)
operator|.
name|setUpdate
argument_list|(
name|update
argument_list|)
operator|.
name|setMerge
argument_list|(
name|merge
argument_list|)
operator|.
name|setWarnOnMissing
argument_list|(
name|warnonmissing
argument_list|)
operator|.
name|setHaltOnMissing
argument_list|(
name|haltonmissing
argument_list|)
operator|.
name|setConfs
argument_list|(
name|splitConfs
argument_list|(
name|conf
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|BuildException
condition|)
block|{
throw|throw
operator|(
name|BuildException
operator|)
name|e
throw|;
block|}
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to publish artifacts for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|PublishArtifact
name|createArtifact
parameter_list|()
block|{
name|PublishArtifact
name|art
init|=
operator|new
name|PublishArtifact
argument_list|()
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|art
argument_list|)
expr_stmt|;
return|return
name|art
return|;
block|}
specifier|public
name|boolean
name|isPublishivy
parameter_list|()
block|{
return|return
name|publishivy
return|;
block|}
specifier|public
name|void
name|setPublishivy
parameter_list|(
name|boolean
name|publishivy
parameter_list|)
block|{
name|this
operator|.
name|publishivy
operator|=
name|publishivy
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWarnonmissing
parameter_list|()
block|{
return|return
name|warnonmissing
return|;
block|}
specifier|public
name|void
name|setWarnonmissing
parameter_list|(
name|boolean
name|warnonmissing
parameter_list|)
block|{
name|this
operator|.
name|warnonmissing
operator|=
name|warnonmissing
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHaltonmissing
parameter_list|()
block|{
return|return
name|haltonmissing
return|;
block|}
specifier|public
name|void
name|setHaltonmissing
parameter_list|(
name|boolean
name|haltonmissing
parameter_list|)
block|{
name|this
operator|.
name|haltonmissing
operator|=
name|haltonmissing
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
block|}
specifier|public
name|void
name|setForcedeliver
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|forcedeliver
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isForcedeliver
parameter_list|()
block|{
return|return
name|forcedeliver
return|;
block|}
specifier|public
name|boolean
name|isUpdate
parameter_list|()
block|{
return|return
name|update
return|;
block|}
specifier|public
name|void
name|setUpdate
parameter_list|(
name|boolean
name|update
parameter_list|)
block|{
name|this
operator|.
name|update
operator|=
name|update
expr_stmt|;
block|}
specifier|public
class|class
name|PublishArtifact
implements|implements
name|Artifact
block|{
specifier|private
name|String
name|ext
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|ext
operator|==
literal|null
condition|?
name|type
else|:
name|ext
return|;
block|}
specifier|public
name|ArtifactRevisionId
name|getId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
literal|null
return|;
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
name|URL
name|getUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setExt
parameter_list|(
name|String
name|ext
parameter_list|)
block|{
name|this
operator|.
name|ext
operator|=
name|ext
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
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
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
name|getAttributes
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|()
return|;
block|}
specifier|public
name|String
name|getExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
name|getExtraAttributes
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|()
return|;
block|}
specifier|public
name|Map
name|getQualifiedExtraAttributes
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|()
return|;
block|}
specifier|public
name|String
name|getStandardAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
name|getStandardAttributes
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isMetadata
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|ArtifactsPattern
block|{
specifier|private
name|String
name|pattern
decl_stmt|;
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

