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
name|LinkedList
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
name|plugins
operator|.
name|version
operator|.
name|VersionMatcher
import|;
end_import

begin_comment
comment|/**  * Wrap a collection of descriptors wrapped themselves in ModuleInSort elements. It contains some  * dedicated function to retrieve module descriptors based on dependencies descriptors.  *<p>  *<i>This class is designed to be used internally by the ModuleDescriptorSorter.</i>  *</p>  */
end_comment

begin_class
class|class
name|CollectionOfModulesToSort
implements|implements
name|Iterable
argument_list|<
name|ModuleInSort
argument_list|>
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|ModuleInSort
argument_list|>
name|moduleDescriptors
decl_stmt|;
specifier|private
specifier|final
name|VersionMatcher
name|versionMatcher
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|Collection
argument_list|<
name|ModuleInSort
argument_list|>
argument_list|>
name|modulesByModuleId
decl_stmt|;
specifier|private
specifier|final
name|NonMatchingVersionReporter
name|nonMatchingVersionReporter
decl_stmt|;
comment|/**      * @param modulesToSort      *            The collection of ModuleDescriptor to sort      * @param matcher      *            The matcher to used to check if dependencyDescriptor match a module in this      *            collection      * @param nonMatchingVersionReporter ditto      */
specifier|public
name|CollectionOfModulesToSort
parameter_list|(
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|modulesToSort
parameter_list|,
name|VersionMatcher
name|matcher
parameter_list|,
name|NonMatchingVersionReporter
name|nonMatchingVersionReporter
parameter_list|)
block|{
name|this
operator|.
name|versionMatcher
operator|=
name|matcher
expr_stmt|;
name|this
operator|.
name|nonMatchingVersionReporter
operator|=
name|nonMatchingVersionReporter
expr_stmt|;
name|this
operator|.
name|modulesByModuleId
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|moduleDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|modulesToSort
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|modulesToSort
control|)
block|{
name|ModuleInSort
name|mdInSort
init|=
operator|new
name|ModuleInSort
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|moduleDescriptors
operator|.
name|add
argument_list|(
name|mdInSort
argument_list|)
expr_stmt|;
name|addToModulesByModuleId
argument_list|(
name|md
argument_list|,
name|mdInSort
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addToModulesByModuleId
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|ModuleInSort
name|mdInSort
parameter_list|)
block|{
name|ModuleId
name|mdId
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ModuleInSort
argument_list|>
name|mdInSortAsList
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|mdInSortAsList
operator|.
name|add
argument_list|(
name|mdInSort
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ModuleInSort
argument_list|>
name|previousList
init|=
name|modulesByModuleId
operator|.
name|put
argument_list|(
name|mdId
argument_list|,
name|mdInSortAsList
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousList
operator|!=
literal|null
condition|)
block|{
name|mdInSortAsList
operator|.
name|addAll
argument_list|(
name|previousList
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Iterator
argument_list|<
name|ModuleInSort
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|moduleDescriptors
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|moduleDescriptors
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Find a matching module descriptor in the list of module to sort.      *      * @param descriptor ditto      * @return a ModuleDescriptor from the collection of module descriptors to sort. If none exists      *         returns null.      */
specifier|public
name|ModuleInSort
name|getModuleDescriptorDependency
parameter_list|(
name|DependencyDescriptor
name|descriptor
parameter_list|)
block|{
name|Collection
argument_list|<
name|ModuleInSort
argument_list|>
name|modulesOfSameId
init|=
name|modulesByModuleId
operator|.
name|get
argument_list|(
name|descriptor
operator|.
name|getDependencyId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|modulesOfSameId
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|ModuleInSort
name|mdInSort
range|:
name|modulesOfSameId
control|)
block|{
if|if
condition|(
name|mdInSort
operator|.
name|match
argument_list|(
name|descriptor
argument_list|,
name|versionMatcher
argument_list|)
condition|)
block|{
return|return
name|mdInSort
return|;
block|}
else|else
block|{
name|nonMatchingVersionReporter
operator|.
name|reportNonMatchingVersion
argument_list|(
name|descriptor
argument_list|,
name|mdInSort
operator|.
name|getSortedModuleDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

