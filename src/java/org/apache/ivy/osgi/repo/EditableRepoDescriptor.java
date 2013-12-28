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
name|osgi
operator|.
name|repo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Iterator
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
name|osgi
operator|.
name|core
operator|.
name|BundleCapability
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
name|osgi
operator|.
name|core
operator|.
name|BundleInfo
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
name|osgi
operator|.
name|core
operator|.
name|ExecutionEnvironmentProfileProvider
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
name|EditableRepoDescriptor
extends|extends
name|RepoDescriptor
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
argument_list|>
name|moduleByCapabilities
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|modules
init|=
operator|new
name|HashSet
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|ExecutionEnvironmentProfileProvider
name|profileProvider
decl_stmt|;
specifier|private
specifier|final
name|URI
name|baseUri
decl_stmt|;
specifier|private
name|int
name|logLevel
init|=
name|Message
operator|.
name|MSG_INFO
decl_stmt|;
specifier|public
name|EditableRepoDescriptor
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|baseUri
expr_stmt|;
name|this
operator|.
name|profileProvider
operator|=
name|profileProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|int
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|logLevel
expr_stmt|;
block|}
specifier|public
name|int
name|getLogLevel
parameter_list|()
block|{
return|return
name|logLevel
return|;
block|}
specifier|public
name|URI
name|getBaseUri
parameter_list|()
block|{
return|return
name|baseUri
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|getModules
parameter_list|()
block|{
return|return
name|modules
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getCapabilities
parameter_list|()
block|{
return|return
name|moduleByCapabilities
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|findModule
parameter_list|(
name|String
name|requirement
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
name|modules
init|=
name|moduleByCapabilities
operator|.
name|get
argument_list|(
name|requirement
argument_list|)
decl_stmt|;
if|if
condition|(
name|modules
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|modules
operator|.
name|get
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getCapabilityValues
parameter_list|(
name|String
name|capabilityName
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
name|modules
init|=
name|moduleByCapabilities
operator|.
name|get
argument_list|(
name|capabilityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|modules
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
return|return
name|modules
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|value
parameter_list|,
name|ModuleDescriptorWrapper
name|md
parameter_list|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
name|map
init|=
name|moduleByCapabilities
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|moduleByCapabilities
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|bundleReferences
init|=
name|map
operator|.
name|get
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleReferences
operator|==
literal|null
condition|)
block|{
name|bundleReferences
operator|=
operator|new
name|HashSet
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|value
argument_list|,
name|bundleReferences
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|bundleReferences
operator|.
name|add
argument_list|(
name|md
argument_list|)
condition|)
block|{
if|if
condition|(
name|logLevel
operator|<=
name|Message
operator|.
name|MSG_DEBUG
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Duplicate module in the repo "
operator|+
name|baseUri
operator|+
literal|" for "
operator|+
name|type
operator|+
literal|" "
operator|+
name|value
operator|+
literal|": "
operator|+
name|md
operator|.
name|getBundleInfo
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"#"
operator|+
name|md
operator|.
name|getBundleInfo
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addBundle
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
name|ModuleDescriptorWrapper
name|md
init|=
operator|new
name|ModuleDescriptorWrapper
argument_list|(
name|bundleInfo
argument_list|,
name|baseUri
argument_list|,
name|profileProvider
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|md
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleCapability
name|capability
range|:
name|bundleInfo
operator|.
name|getCapabilities
argument_list|()
control|)
block|{
name|add
argument_list|(
name|capability
operator|.
name|getType
argument_list|()
argument_list|,
name|capability
operator|.
name|getName
argument_list|()
argument_list|,
name|md
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|modules
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|modules
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|modules
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EditableRepoDescriptor
name|other
init|=
operator|(
name|EditableRepoDescriptor
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|modules
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|modules
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|modules
operator|.
name|equals
argument_list|(
name|other
operator|.
name|modules
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

