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
name|plugins
operator|.
name|circular
operator|.
name|CircularDependencyHelper
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
comment|/**  * Decorates a ModuleDescriptor with some attributes used during the sort. Thus every instance of a  * ModuleInSort can be used in only one ModuleDescriptorSorter at a time.<br>  * The added fields are :<br>  *<ul>  *<li><code>isSorted</code> : is true iff this module has already been added to the sorted list.</li>  *<li><code>loopElements</code> : When the module is the root of a loop (=the first element of a  * loop met during the sort),<code>loopElements</code> contains all ModuleInSort of the loop  * (excluding the root itself.</li>  *<li><code>isLoopIntermediateElement</code> : When a loop is detected, all modules included in  * the loop (except the root) have<code>isLoopIntermediateElement</code> set to true.</li>  *<li><code>caller</code> : During the sort, we traverse recursively the graph. When doing that,  * caller point to the parent element.  */
end_comment

begin_class
class|class
name|ModuleInSort
block|{
specifier|private
specifier|final
name|ModuleDescriptor
name|module
decl_stmt|;
specifier|private
name|boolean
name|isSorted
init|=
literal|false
decl_stmt|;
specifier|private
name|List
name|loopElements
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|isLoopIntermediateElement
init|=
literal|false
decl_stmt|;
specifier|private
name|ModuleInSort
name|caller
decl_stmt|;
specifier|public
name|ModuleInSort
parameter_list|(
name|ModuleDescriptor
name|moduleToSort
parameter_list|)
block|{
name|module
operator|=
name|moduleToSort
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInLoop
parameter_list|()
block|{
return|return
name|isLoopIntermediateElement
return|;
block|}
specifier|public
name|boolean
name|isSorted
parameter_list|()
block|{
if|if
condition|(
name|isSorted
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Module descriptor already sorted : "
operator|+
name|module
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|void
name|setCaller
parameter_list|(
name|ModuleInSort
name|caller
parameter_list|)
block|{
name|this
operator|.
name|caller
operator|=
name|caller
expr_stmt|;
block|}
specifier|public
name|void
name|endOfCall
parameter_list|()
block|{
name|caller
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Check if a adding this element as a dependency of caller will introduce a circular      * dependency. If it is, all the elements of the loop are flaged as 'loopIntermediateElement',      * and the loopElements of this module (which is the root of the loop) is updated. The      * depStrategy is invoked on order to report a correct circular loop message.      *       * @param futurCaller      * @param depStrategy      * @return true if a loop is detected.      */
specifier|public
name|boolean
name|checkLoop
parameter_list|(
name|ModuleInSort
name|futurCaller
parameter_list|,
name|CircularDependencyStrategy
name|depStrategy
parameter_list|)
block|{
if|if
condition|(
name|caller
operator|!=
literal|null
condition|)
block|{
name|LinkedList
name|elemOfLoop
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
name|elemOfLoop
operator|.
name|add
argument_list|(
name|this
operator|.
name|module
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ModuleInSort
name|stackElem
init|=
name|futurCaller
init|;
name|stackElem
operator|!=
name|this
condition|;
name|stackElem
operator|=
name|stackElem
operator|.
name|caller
control|)
block|{
name|elemOfLoop
operator|.
name|add
argument_list|(
name|stackElem
operator|.
name|module
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|stackElem
operator|.
name|isLoopIntermediateElement
operator|=
literal|true
expr_stmt|;
name|loopElements
operator|.
name|add
argument_list|(
name|stackElem
argument_list|)
expr_stmt|;
block|}
name|elemOfLoop
operator|.
name|add
argument_list|(
name|this
operator|.
name|module
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|ModuleRevisionId
index|[]
name|mrids
init|=
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|elemOfLoop
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|elemOfLoop
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|depStrategy
operator|.
name|handleCircularDependency
argument_list|(
name|mrids
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Add this module to the sorted list except if this module is an intermediary element of a      * loop. If this module is the 'root' of a loop, then all elements of that loops are added      * before.      *       * @param sorted      *            The list of sorted elements on which this module will be added      */
specifier|public
name|void
name|addToSortedListIfRequired
parameter_list|(
name|List
name|sorted
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isLoopIntermediateElement
condition|)
block|{
name|addToSortList
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add this module to the sorted list. If current is the 'root' of a loop, then all elements of      * that loops are added before.      */
specifier|private
name|void
name|addToSortList
parameter_list|(
name|List
name|sortedList
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|loopElements
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleInSort
name|moduleInLoop
init|=
operator|(
name|ModuleInSort
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|moduleInLoop
operator|.
name|addToSortList
argument_list|(
name|sortedList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|this
operator|.
name|isSorted
argument_list|()
condition|)
block|{
name|sortedList
operator|.
name|add
argument_list|(
name|module
argument_list|)
expr_stmt|;
name|this
operator|.
name|isSorted
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|module
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|DependencyDescriptor
index|[]
name|getDependencies
parameter_list|()
block|{
return|return
name|module
operator|.
name|getDependencies
argument_list|()
return|;
block|}
comment|/** Log a warning saying that a loop is detected */
specifier|public
specifier|static
name|void
name|logLoopWarning
parameter_list|(
name|List
name|loopElement
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"circular dependency detected during sort: "
operator|+
name|CircularDependencyHelper
operator|.
name|formatMessageFromDescriptors
argument_list|(
name|loopElement
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return true if this module match the DependencyDescriptor with the given versionMatcher. If      * this module has no version defined, then true is always returned.      */
specifier|public
name|boolean
name|match
parameter_list|(
name|DependencyDescriptor
name|descriptor
parameter_list|,
name|VersionMatcher
name|versionMatcher
parameter_list|)
block|{
name|ModuleDescriptor
name|md
init|=
name|module
decl_stmt|;
return|return
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|==
literal|null
operator|||
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
argument_list|)
operator|||
name|versionMatcher
operator|.
name|accept
argument_list|(
name|descriptor
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|md
argument_list|)
return|;
comment|// Checking md.getResolvedModuleRevisionId().getRevision().equals(Ivy.getWorkingRevision()
comment|// allow to consider any local non resolved ivy.xml
comment|// as a valid module.
block|}
specifier|public
name|ModuleDescriptor
name|getSortedModuleDescriptor
parameter_list|()
block|{
return|return
name|module
return|;
block|}
block|}
end_class

end_unit

