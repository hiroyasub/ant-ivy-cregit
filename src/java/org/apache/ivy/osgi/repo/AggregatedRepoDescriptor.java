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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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

begin_class
specifier|public
class|class
name|AggregatedRepoDescriptor
extends|extends
name|RepoDescriptor
block|{
specifier|private
name|List
argument_list|<
name|RepoDescriptor
argument_list|>
name|repos
decl_stmt|;
specifier|public
name|AggregatedRepoDescriptor
parameter_list|(
name|List
argument_list|<
name|RepoDescriptor
argument_list|>
name|repos
parameter_list|)
block|{
name|this
operator|.
name|repos
operator|=
name|repos
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|getModules
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|RepoDescriptor
argument_list|>
name|itRepos
init|=
name|repos
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|()
block|{
specifier|private
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|current
init|=
literal|null
decl_stmt|;
specifier|private
name|ModuleDescriptorWrapper
name|next
init|=
literal|null
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
while|while
condition|(
name|next
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|current
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|itRepos
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RepoDescriptor
name|repo
init|=
name|itRepos
operator|.
name|next
argument_list|()
decl_stmt|;
name|current
operator|=
name|repo
operator|.
name|getModules
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|next
operator|=
name|current
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|current
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|ModuleDescriptorWrapper
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|ModuleDescriptorWrapper
name|ret
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getCapabilities
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepoDescriptor
name|repo
range|:
name|repos
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|capabilities
init|=
name|repo
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
if|if
condition|(
name|capabilities
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|capabilities
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
annotation|@
name|Override
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
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|ret
init|=
operator|new
name|HashSet
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepoDescriptor
name|repo
range|:
name|repos
control|)
block|{
name|Set
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|modules
init|=
name|repo
operator|.
name|findModule
argument_list|(
name|requirement
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|modules
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
annotation|@
name|Override
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
name|Set
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepoDescriptor
name|repo
range|:
name|repos
control|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|capabilityValues
init|=
name|repo
operator|.
name|getCapabilityValues
argument_list|(
name|capabilityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|capabilityValues
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|capabilityValues
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

