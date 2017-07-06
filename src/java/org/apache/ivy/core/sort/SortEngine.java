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
name|sort
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
name|LinkedHashMap
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
name|plugins
operator|.
name|circular
operator|.
name|CircularDependencyException
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
name|circular
operator|.
name|CircularDependencyStrategy
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
name|circular
operator|.
name|IgnoreCircularDependencyStrategy
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
name|Checks
import|;
end_import

begin_class
specifier|public
class|class
name|SortEngine
block|{
specifier|private
name|SortEngineSettings
name|settings
decl_stmt|;
specifier|public
name|SortEngine
parameter_list|(
name|SortEngineSettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"SortEngine.settings can not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
comment|/**      * Same as {@link #sortModuleDescriptors(Collection, SortOptions)} but for<code>IvyNode</code>      * s.      *      * @param nodes      *            a Collection of nodes to sort      * @param options      *            Options to use to sort the nodes.      * @return a List of sorted IvyNode      * @throws CircularDependencyException      *             if a circular dependency exists and circular dependency strategy decide to throw      *             an exception      */
specifier|public
name|List
argument_list|<
name|IvyNode
argument_list|>
name|sortNodes
parameter_list|(
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|nodes
parameter_list|,
name|SortOptions
name|options
parameter_list|)
block|{
comment|/*          * here we want to use the sort algorithm which work on module descriptors : so we first put          * dependencies on a map from descriptors to dependency, then we sort the keySet (i.e. a          * collection of descriptors), then we replace in the sorted list each descriptor by the          * corresponding dependency          */
name|Map
argument_list|<
name|ModuleDescriptor
argument_list|,
name|List
argument_list|<
name|IvyNode
argument_list|>
argument_list|>
name|dependenciesMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|IvyNode
argument_list|>
name|nulls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|IvyNode
name|node
range|:
name|nodes
control|)
block|{
if|if
condition|(
name|node
operator|.
name|getDescriptor
argument_list|()
operator|==
literal|null
condition|)
block|{
name|nulls
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|n
init|=
name|dependenciesMap
operator|.
name|get
argument_list|(
name|node
operator|.
name|getDescriptor
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|null
condition|)
block|{
name|n
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|dependenciesMap
operator|.
name|put
argument_list|(
name|node
operator|.
name|getDescriptor
argument_list|()
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|n
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|list
init|=
name|sortModuleDescriptors
argument_list|(
name|dependenciesMap
operator|.
name|keySet
argument_list|()
argument_list|,
name|options
argument_list|)
decl_stmt|;
specifier|final
name|double
name|adjustFactor
init|=
literal|1.3
decl_stmt|;
name|List
argument_list|<
name|IvyNode
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
operator|(
name|int
operator|)
operator|(
name|list
operator|.
name|size
argument_list|()
operator|*
name|adjustFactor
operator|+
name|nulls
operator|.
name|size
argument_list|()
operator|)
argument_list|)
decl_stmt|;
comment|// attempt to adjust the size to avoid too much list resizing
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|list
control|)
block|{
name|List
argument_list|<
name|IvyNode
argument_list|>
name|n
init|=
name|dependenciesMap
operator|.
name|get
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
name|ret
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|nulls
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
comment|/**      * Sorts the given ModuleDescriptors from the less dependent to the more dependent. This sort      * ensures that a ModuleDescriptor is always found in the list before all ModuleDescriptors      * depending directly on it.      *      * @param moduleDescriptors      *            a Collection of ModuleDescriptor to sort      * @param options      *            Options to use to sort the descriptors.      * @return a List of sorted ModuleDescriptors      * @throws CircularDependencyException      *             if a circular dependency exists and circular dependency strategy decide to throw      *             an exception      */
specifier|public
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|sortModuleDescriptors
parameter_list|(
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|moduleDescriptors
parameter_list|,
name|SortOptions
name|options
parameter_list|)
throws|throws
name|CircularDependencyException
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|options
argument_list|,
literal|"options"
argument_list|)
expr_stmt|;
name|ModuleDescriptorSorter
name|sorter
init|=
operator|new
name|ModuleDescriptorSorter
argument_list|(
name|moduleDescriptors
argument_list|,
name|getVersionMatcher
argument_list|()
argument_list|,
name|options
operator|.
name|getNonMatchingVersionReporter
argument_list|()
argument_list|,
name|options
operator|.
name|isUseCircularDependencyStrategy
argument_list|()
condition|?
name|getCircularStrategy
argument_list|()
else|:
name|IgnoreCircularDependencyStrategy
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|sorter
operator|.
name|sortModuleDescriptors
argument_list|()
return|;
block|}
specifier|protected
name|CircularDependencyStrategy
name|getCircularStrategy
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getCircularDependencyStrategy
argument_list|()
return|;
block|}
specifier|protected
name|VersionMatcher
name|getVersionMatcher
parameter_list|()
block|{
return|return
name|settings
operator|.
name|getVersionMatcher
argument_list|()
return|;
block|}
block|}
end_class

end_unit

