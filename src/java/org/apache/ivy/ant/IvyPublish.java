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
name|_organisation
decl_stmt|;
specifier|private
name|String
name|_module
decl_stmt|;
specifier|private
name|String
name|_revision
decl_stmt|;
specifier|private
name|String
name|_pubRevision
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|String
name|_srcivypattern
decl_stmt|;
specifier|private
name|String
name|_status
decl_stmt|;
specifier|private
name|String
name|_conf
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_pubdate
decl_stmt|;
specifier|private
name|String
name|_deliverTarget
decl_stmt|;
specifier|private
name|String
name|_publishResolverName
init|=
literal|null
decl_stmt|;
specifier|private
name|List
name|_artifactspattern
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|File
name|_deliveryList
decl_stmt|;
specifier|private
name|boolean
name|_publishivy
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_warnonmissing
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_haltonmissing
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_overwrite
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|_update
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|_replacedynamicrev
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_forcedeliver
decl_stmt|;
specifier|private
name|Collection
name|_artifacts
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|File
name|getCache
parameter_list|()
block|{
return|return
name|_cache
return|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|_cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|String
name|getSrcivypattern
parameter_list|()
block|{
return|return
name|_srcivypattern
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
name|_srcivypattern
operator|=
name|destivypattern
expr_stmt|;
block|}
comment|/**      * @deprecated use getSrcivypattern instead      * @return      */
specifier|public
name|String
name|getDeliverivypattern
parameter_list|()
block|{
return|return
name|_srcivypattern
return|;
block|}
comment|/**      * @deprecated use setSrcivypattern instead      * @return      */
specifier|public
name|void
name|setDeliverivypattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|_srcivypattern
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
name|_module
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
name|_module
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
name|_organisation
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
name|_organisation
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
name|_pubdate
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
name|_pubdate
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
name|_pubRevision
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
name|_pubRevision
operator|=
name|pubRevision
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_revision
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
name|_revision
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
name|_status
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
name|_status
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
name|_conf
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
name|_deliverTarget
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
name|_deliveryList
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
name|_publishResolverName
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
name|_publishResolverName
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
name|_artifactspattern
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|_artifactspattern
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
name|_artifactspattern
operator|.
name|clear
argument_list|()
expr_stmt|;
name|_artifactspattern
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
name|_artifactspattern
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
name|_artifactspattern
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
name|_replacedynamicrev
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
name|_replacedynamicrev
operator|=
name|replacedynamicrev
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
name|_organisation
operator|=
name|getProperty
argument_list|(
name|_organisation
argument_list|,
name|settings
argument_list|,
literal|"ivy.organisation"
argument_list|)
expr_stmt|;
name|_module
operator|=
name|getProperty
argument_list|(
name|_module
argument_list|,
name|settings
argument_list|,
literal|"ivy.module"
argument_list|)
expr_stmt|;
name|_revision
operator|=
name|getProperty
argument_list|(
name|_revision
argument_list|,
name|settings
argument_list|,
literal|"ivy.revision"
argument_list|)
expr_stmt|;
name|_pubRevision
operator|=
name|getProperty
argument_list|(
name|_pubRevision
argument_list|,
name|settings
argument_list|,
literal|"ivy.deliver.revision"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_cache
operator|==
literal|null
condition|)
block|{
name|_cache
operator|=
name|settings
operator|.
name|getDefaultCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_artifactspattern
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
name|_artifactspattern
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
name|_srcivypattern
operator|==
literal|null
condition|)
block|{
name|_srcivypattern
operator|=
name|getArtifactspattern
argument_list|()
expr_stmt|;
block|}
name|_status
operator|=
name|getProperty
argument_list|(
name|_status
argument_list|,
name|settings
argument_list|,
literal|"ivy.status"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy publish task: It can either be set explicitely via the attribute 'organisation' or via 'ivy.organisation' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy publish task: It can either be set explicitely via the attribute 'module' or via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_revision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module revision provided for ivy publish task: It can either be set explicitely via the attribute 'revision' or via 'ivy.revision' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_artifactspattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no artifacts pattern: either provide it through parameter or through ivy.publish.src.artifacts.pattern property"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_publishResolverName
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
name|_revision
argument_list|)
condition|)
block|{
name|_revision
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
name|_pubdate
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|_pubRevision
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|_revision
operator|.
name|startsWith
argument_list|(
literal|"working@"
argument_list|)
condition|)
block|{
name|_pubRevision
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
name|_pubRevision
operator|=
name|_revision
expr_stmt|;
block|}
block|}
if|if
condition|(
name|_status
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no status provided: either provide it as parameter or through the ivy.status.default property"
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
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_revision
argument_list|)
decl_stmt|;
try|try
block|{
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|_srcivypattern
argument_list|,
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_pubRevision
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
name|_publishivy
operator|&&
operator|(
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
operator|||
name|_forcedeliver
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
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setCache
argument_list|(
name|getCache
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
name|_deliverTarget
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDeliveryList
argument_list|(
name|_deliveryList
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
name|setConf
argument_list|(
name|_conf
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|Collection
name|missing
init|=
name|ivy
operator|.
name|publish
argument_list|(
name|mrid
argument_list|,
name|_artifactspattern
argument_list|,
name|_publishResolverName
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
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|settings
argument_list|,
name|_cache
argument_list|)
argument_list|)
operator|.
name|setSrcIvyPattern
argument_list|(
name|_publishivy
condition|?
name|_srcivypattern
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
name|_artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|_artifacts
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
name|_overwrite
argument_list|)
operator|.
name|setUpdate
argument_list|(
name|_update
argument_list|)
operator|.
name|setConfs
argument_list|(
name|splitConfs
argument_list|(
name|_conf
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|_warnonmissing
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|missing
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"missing artifact: "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|_haltonmissing
operator|&&
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"missing published artifacts for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|missing
argument_list|)
throw|;
block|}
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
name|_artifacts
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
name|_publishivy
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
name|_publishivy
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
name|_warnonmissing
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
name|_warnonmissing
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
name|_haltonmissing
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
name|_haltonmissing
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
name|_overwrite
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
name|_overwrite
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
name|_forcedeliver
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
name|_forcedeliver
return|;
block|}
specifier|public
name|boolean
name|isUpdate
parameter_list|()
block|{
return|return
name|_update
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
name|_update
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
name|_ext
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|String
name|_type
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
name|_ext
operator|==
literal|null
condition|?
name|_type
else|:
name|_ext
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
name|_name
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
name|_type
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
name|_ext
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
name|_name
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
name|_type
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
block|}
specifier|public
specifier|static
class|class
name|ArtifactsPattern
block|{
specifier|private
name|String
name|_pattern
decl_stmt|;
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|_pattern
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
name|_pattern
operator|=
name|pattern
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

