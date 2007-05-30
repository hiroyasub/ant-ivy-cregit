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
name|Collection
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
name|Message
import|;
end_import

begin_comment
comment|/**  * Inner helper class for sorting ModuleDescriptors.<br>  * ModuleDescriptorSorter use CollectionOfModulesToSort to find the dependencies of the modules, and use ModuleInSort  * to store some temporary values attached to the modules to sort.  * @see ModuleInSort  * @see CollectionOfModulesToSort  */
end_comment

begin_class
specifier|public
class|class
name|ModuleDescriptorSorter
block|{
specifier|private
specifier|final
name|CollectionOfModulesToSort
name|moduleDescriptors
decl_stmt|;
specifier|private
specifier|final
name|List
name|sorted
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|CircularDependencyStrategy
name|circularDepStrategy
decl_stmt|;
specifier|public
name|ModuleDescriptorSorter
parameter_list|(
name|Collection
name|modulesDescriptorsToSort
parameter_list|,
name|VersionMatcher
name|matcher
parameter_list|,
name|NonMatchingVersionReporter
name|nonMatchingVersionReporter
parameter_list|,
name|CircularDependencyStrategy
name|circularDepStrategy
parameter_list|)
block|{
name|this
operator|.
name|circularDepStrategy
operator|=
name|circularDepStrategy
expr_stmt|;
name|moduleDescriptors
operator|=
operator|new
name|CollectionOfModulesToSort
argument_list|(
name|modulesDescriptorsToSort
argument_list|,
name|matcher
argument_list|,
name|nonMatchingVersionReporter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Iterates over all modules calling sortModuleDescriptorsHelp.      * @return sorted module      * @throws CircularDependencyException      */
specifier|public
name|List
name|sortModuleDescriptors
parameter_list|()
throws|throws
name|CircularDependencyException
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Nbr of module to sort : "
operator|+
name|moduleDescriptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|_moduleDescriptorsIterator
init|=
name|moduleDescriptors
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|_moduleDescriptorsIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleInSort
name|next
init|=
operator|(
name|ModuleInSort
operator|)
name|_moduleDescriptorsIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|sortModuleDescriptorsHelp
argument_list|(
name|next
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
return|return
name|sorted
return|;
block|}
comment|/**      * If current module has already been added to list, returns,      * Otherwise invokes sortModuleDescriptorsHelp for all dependencies      * contained within set of moduleDescriptors.  Then finally adds self      * to list of sorted.<br/>      * When a loop is detected by a recursive call, the moduleDescriptors are not added      * immediately added to the sorted list.  They are added as loop dependencies of the root, and will be      * added to the sorted list only when the root itself will be added.       * @param current Current module to add to sorted list.      * @throws CircularDependencyException      */
specifier|private
name|void
name|sortModuleDescriptorsHelp
parameter_list|(
name|ModuleInSort
name|current
parameter_list|,
name|ModuleInSort
name|caller
parameter_list|)
throws|throws
name|CircularDependencyException
block|{
comment|//if already sorted return
if|if
condition|(
name|current
operator|.
name|isSorted
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|current
operator|.
name|checkLoop
argument_list|(
name|caller
argument_list|,
name|circularDepStrategy
argument_list|)
condition|)
block|{
return|return;
block|}
name|DependencyDescriptor
index|[]
name|descriptors
init|=
name|current
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"Sort dependencies of : "
operator|+
name|current
operator|.
name|toString
argument_list|()
operator|+
literal|" / Number of dependencies = "
operator|+
name|descriptors
operator|.
name|length
argument_list|)
expr_stmt|;
name|current
operator|.
name|setCaller
argument_list|(
name|caller
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
name|descriptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ModuleInSort
name|child
init|=
name|moduleDescriptors
operator|.
name|getModuleDescriptorDependency
argument_list|(
name|descriptors
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|sortModuleDescriptorsHelp
argument_list|(
name|child
argument_list|,
name|current
argument_list|)
expr_stmt|;
block|}
block|}
name|current
operator|.
name|endOfCall
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"Sort done for : "
operator|+
name|current
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|.
name|addToSortedListIfRequired
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

