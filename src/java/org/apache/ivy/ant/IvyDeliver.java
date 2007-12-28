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
name|deliver
operator|.
name|DefaultPublishingDRResolver
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
name|deliver
operator|.
name|DeliverOptions
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
name|deliver
operator|.
name|PublishingDependencyRevisionResolver
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
name|module
operator|.
name|status
operator|.
name|StatusManager
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
name|taskdefs
operator|.
name|CallTarget
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
name|taskdefs
operator|.
name|Echo
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
name|taskdefs
operator|.
name|Input
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
name|taskdefs
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Trigger the delivery of a module, which may consist in a recursive delivery of dependencies and  * on the replacement in the ivy file of dynamic revisions (like latest.integration) by static ones.  */
end_comment

begin_class
specifier|public
class|class
name|IvyDeliver
extends|extends
name|IvyTask
block|{
specifier|private
specifier|final
class|class
name|DeliverDRResolver
extends|extends
name|DefaultPublishingDRResolver
block|{
specifier|public
name|String
name|resolve
parameter_list|(
name|ModuleDescriptor
name|published
parameter_list|,
name|String
name|publishedStatus
parameter_list|,
name|ModuleRevisionId
name|depMrid
parameter_list|,
name|String
name|depStatus
parameter_list|)
block|{
if|if
condition|(
name|StatusManager
operator|.
name|getCurrent
argument_list|()
operator|.
name|isIntegration
argument_list|(
name|publishedStatus
argument_list|)
condition|)
block|{
comment|// published status is integration one, nothing to ask
return|return
name|super
operator|.
name|resolve
argument_list|(
name|published
argument_list|,
name|publishedStatus
argument_list|,
name|depMrid
argument_list|,
name|depStatus
argument_list|)
return|;
block|}
comment|// we are publishing a delivery (a non integration module)
if|if
condition|(
operator|!
name|StatusManager
operator|.
name|getCurrent
argument_list|()
operator|.
name|isIntegration
argument_list|(
name|depStatus
argument_list|)
condition|)
block|{
comment|// dependency is already a delivery, nothing to ask
return|return
name|super
operator|.
name|resolve
argument_list|(
name|published
argument_list|,
name|publishedStatus
argument_list|,
name|depMrid
argument_list|,
name|depStatus
argument_list|)
return|;
block|}
comment|// the dependency is not a delivery
name|String
name|statusProperty
init|=
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|".status"
decl_stmt|;
name|String
name|versionProperty
init|=
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|".version"
decl_stmt|;
name|String
name|deliveredProperty
init|=
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|".delivered"
decl_stmt|;
name|String
name|version
init|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|versionProperty
argument_list|)
decl_stmt|;
name|String
name|status
init|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|statusProperty
argument_list|)
decl_stmt|;
name|String
name|delivered
init|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|deliveredProperty
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"found version = "
operator|+
name|version
operator|+
literal|" status="
operator|+
name|status
operator|+
literal|" delivered="
operator|+
name|delivered
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
operator|&&
name|status
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|delivered
argument_list|)
condition|)
block|{
comment|// delivery has already been done : just return the value
return|return
name|version
return|;
block|}
else|else
block|{
name|deliverDependency
argument_list|(
name|depMrid
argument_list|,
name|version
argument_list|,
name|status
argument_list|,
name|depStatus
argument_list|)
expr_stmt|;
name|loadDeliveryList
argument_list|()
expr_stmt|;
return|return
name|version
return|;
block|}
block|}
comment|/**              * By setting these properties: recursive.delivery.status and              * recursive.delivery.version, then if the specific status/version is not found, then we              * will use the status/version set in these global properties. This will apply to all              * artifacts in the system. This patch is meant to be used for recursive deliveries so              * that all deliveries will use the global status/version unless a more specific one is              * set.              */
name|String
name|globalStatusProperty
init|=
literal|"recursive.delivery.status"
decl_stmt|;
name|String
name|globalVersionProperty
init|=
literal|"recursive.delivery.version"
decl_stmt|;
name|version
operator|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|globalVersionProperty
argument_list|)
expr_stmt|;
name|status
operator|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|globalStatusProperty
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
operator|&&
name|status
operator|!=
literal|null
condition|)
block|{
comment|// found global delivery properties
name|delivered
operator|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"recursive."
operator|+
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|".delivered"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"found global version = "
operator|+
name|version
operator|+
literal|" and global status="
operator|+
name|status
operator|+
literal|" - delivered = "
operator|+
name|delivered
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|delivered
argument_list|)
condition|)
block|{
comment|// delivery has already been done : just return the value
return|return
name|version
return|;
block|}
else|else
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
name|statusProperty
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|deliverDependency
argument_list|(
name|depMrid
argument_list|,
name|version
argument_list|,
name|status
argument_list|,
name|depStatus
argument_list|)
expr_stmt|;
name|loadDeliveryList
argument_list|()
expr_stmt|;
return|return
name|version
return|;
block|}
block|}
comment|// we must ask the user what version and status he want to have
comment|// for the dependency
name|Input
name|input
init|=
operator|(
name|Input
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"input"
argument_list|)
decl_stmt|;
name|input
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// ask status
name|input
operator|.
name|setMessage
argument_list|(
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|": please enter a status: "
argument_list|)
expr_stmt|;
name|input
operator|.
name|setValidargs
argument_list|(
name|StatusManager
operator|.
name|getCurrent
argument_list|()
operator|.
name|getDeliveryStatusListString
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|setAddproperty
argument_list|(
name|statusProperty
argument_list|)
expr_stmt|;
name|input
operator|.
name|perform
argument_list|()
expr_stmt|;
name|status
operator|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|statusProperty
argument_list|)
expr_stmt|;
name|appendDeliveryList
argument_list|(
name|statusProperty
operator|+
literal|" = "
operator|+
name|status
argument_list|)
expr_stmt|;
comment|// ask version
name|input
operator|.
name|setMessage
argument_list|(
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|": please enter a version: "
argument_list|)
expr_stmt|;
name|input
operator|.
name|setValidargs
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|input
operator|.
name|setAddproperty
argument_list|(
name|versionProperty
argument_list|)
expr_stmt|;
name|input
operator|.
name|perform
argument_list|()
expr_stmt|;
name|version
operator|=
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|versionProperty
argument_list|)
expr_stmt|;
name|appendDeliveryList
argument_list|(
name|versionProperty
operator|+
literal|" = "
operator|+
name|version
argument_list|)
expr_stmt|;
name|deliverDependency
argument_list|(
name|depMrid
argument_list|,
name|version
argument_list|,
name|status
argument_list|,
name|depStatus
argument_list|)
expr_stmt|;
name|loadDeliveryList
argument_list|()
expr_stmt|;
return|return
name|version
return|;
block|}
specifier|public
name|void
name|deliverDependency
parameter_list|(
name|ModuleRevisionId
name|depMrid
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|status
parameter_list|,
name|String
name|depStatus
parameter_list|)
block|{
comment|// call deliver target if any
if|if
condition|(
name|deliverTarget
operator|!=
literal|null
operator|&&
name|deliverTarget
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|CallTarget
name|ct
init|=
operator|(
name|CallTarget
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"antcall"
argument_list|)
decl_stmt|;
name|ct
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|ct
operator|.
name|init
argument_list|()
expr_stmt|;
name|ct
operator|.
name|setTarget
argument_list|(
name|deliverTarget
argument_list|)
expr_stmt|;
name|ct
operator|.
name|setInheritAll
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ct
operator|.
name|setInheritRefs
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Property
name|param
init|=
name|ct
operator|.
name|createParam
argument_list|()
decl_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"dependency.name"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setValue
argument_list|(
name|depMrid
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|param
operator|=
name|ct
operator|.
name|createParam
argument_list|()
expr_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"dependency.published.status"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setValue
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|param
operator|=
name|ct
operator|.
name|createParam
argument_list|()
expr_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"dependency.published.version"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setValue
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|param
operator|=
name|ct
operator|.
name|createParam
argument_list|()
expr_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"dependency.version"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setValue
argument_list|(
name|depMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|param
operator|=
name|ct
operator|.
name|createParam
argument_list|()
expr_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"dependency.status"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setValue
argument_list|(
name|depStatus
operator|==
literal|null
condition|?
literal|"null"
else|:
name|depStatus
argument_list|)
expr_stmt|;
name|ct
operator|.
name|perform
argument_list|()
expr_stmt|;
name|String
name|deliveredProperty
init|=
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|depMrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|".delivered"
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
name|deliveredProperty
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|appendDeliveryList
argument_list|(
name|deliveredProperty
operator|+
literal|" = true"
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"recursive."
operator|+
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|".delivered"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|appendDeliveryList
argument_list|(
literal|"recursive."
operator|+
name|depMrid
operator|.
name|getName
argument_list|()
operator|+
literal|".delivered"
operator|+
literal|" = true"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|deliverpattern
decl_stmt|;
specifier|private
name|String
name|status
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
name|File
name|deliveryList
decl_stmt|;
specifier|private
name|boolean
name|replacedynamicrev
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|resolveId
decl_stmt|;
specifier|private
name|String
name|conf
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
name|getDeliverpattern
parameter_list|()
block|{
return|return
name|deliverpattern
return|;
block|}
specifier|public
name|void
name|setDeliverpattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|this
operator|.
name|deliverpattern
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
name|confs
parameter_list|)
block|{
name|conf
operator|=
name|confs
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
argument_list|,
name|resolveId
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
argument_list|,
name|resolveId
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
argument_list|,
name|resolveId
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
name|deliverpattern
operator|=
name|getProperty
argument_list|(
name|deliverpattern
argument_list|,
name|settings
argument_list|,
literal|"ivy.deliver.ivy.pattern"
argument_list|)
expr_stmt|;
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
name|deliveryList
operator|==
literal|null
condition|)
block|{
name|String
name|deliveryListPath
init|=
name|getProperty
argument_list|(
name|settings
argument_list|,
literal|"ivy.delivery.list.file"
argument_list|)
decl_stmt|;
if|if
condition|(
name|deliveryListPath
operator|==
literal|null
condition|)
block|{
name|deliveryList
operator|=
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
operator|+
literal|"/delivery.properties"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deliveryList
operator|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|settings
operator|.
name|substitute
argument_list|(
name|deliveryListPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|resolveId
operator|==
literal|null
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
literal|"no organisation provided for ivy deliver task: "
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
literal|"no module name provided for ivy deliver task: "
operator|+
literal|"It can either be set explicitely via the attribute 'module' "
operator|+
literal|"or via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
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
name|deliverpattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"deliver ivy pattern is missing: either provide it as parameters "
operator|+
literal|"or through ivy.deliver.ivy.pattern properties"
argument_list|)
throw|;
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
literal|"no status provided: either provide it as parameter or through "
operator|+
literal|"the ivy.status.default property"
argument_list|)
throw|;
block|}
name|ModuleRevisionId
name|mrid
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resolveId
operator|==
literal|null
condition|)
block|{
name|mrid
operator|=
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
expr_stmt|;
block|}
name|boolean
name|isLeading
init|=
literal|false
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|deliveryList
operator|.
name|exists
argument_list|()
condition|)
block|{
name|isLeading
operator|=
literal|true
expr_stmt|;
block|}
name|loadDeliveryList
argument_list|()
expr_stmt|;
name|PublishingDependencyRevisionResolver
name|drResolver
decl_stmt|;
if|if
condition|(
name|deliverTarget
operator|!=
literal|null
operator|&&
name|deliverTarget
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|drResolver
operator|=
operator|new
name|DeliverDRResolver
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|drResolver
operator|=
operator|new
name|DefaultPublishingDRResolver
argument_list|()
expr_stmt|;
block|}
name|DeliverOptions
name|options
init|=
operator|new
name|DeliverOptions
argument_list|(
name|status
argument_list|,
name|pubdate
argument_list|,
name|drResolver
argument_list|,
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|,
name|replacedynamicrev
argument_list|,
name|splitConfs
argument_list|(
name|conf
argument_list|)
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|resolveId
argument_list|)
decl_stmt|;
if|if
condition|(
name|mrid
operator|==
literal|null
condition|)
block|{
name|ivy
operator|.
name|deliver
argument_list|(
name|pubRevision
argument_list|,
name|deliverpattern
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ivy
operator|.
name|deliver
argument_list|(
name|mrid
argument_list|,
name|pubRevision
argument_list|,
name|deliverpattern
argument_list|,
name|options
argument_list|)
expr_stmt|;
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
literal|"impossible to deliver "
operator|+
name|mrid
operator|==
literal|null
condition|?
name|resolveId
else|:
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
finally|finally
block|{
if|if
condition|(
name|isLeading
condition|)
block|{
if|if
condition|(
name|deliveryList
operator|.
name|exists
argument_list|()
condition|)
block|{
name|deliveryList
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|loadDeliveryList
parameter_list|()
block|{
name|Property
name|property
init|=
operator|(
name|Property
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"property"
argument_list|)
decl_stmt|;
name|property
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|property
operator|.
name|init
argument_list|()
expr_stmt|;
name|property
operator|.
name|setFile
argument_list|(
name|deliveryList
argument_list|)
expr_stmt|;
name|property
operator|.
name|perform
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|appendDeliveryList
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|Echo
name|echo
init|=
operator|(
name|Echo
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"echo"
argument_list|)
decl_stmt|;
name|echo
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|echo
operator|.
name|init
argument_list|()
expr_stmt|;
name|echo
operator|.
name|setFile
argument_list|(
name|deliveryList
argument_list|)
expr_stmt|;
name|echo
operator|.
name|setMessage
argument_list|(
name|msg
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|echo
operator|.
name|setAppend
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|echo
operator|.
name|perform
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

