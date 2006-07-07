begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|circular
operator|.
name|CircularDependencyException
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|circular
operator|.
name|CircularDependencyHelper
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|version
operator|.
name|VersionMatcher
import|;
end_import

begin_comment
comment|/**  * Inner helper class for sorting ModuleDescriptors.  * @author baumkar (for most of the code)  * @author xavier hanin (for the sorting of nodes based upon sort of modules)  *  */
end_comment

begin_class
class|class
name|ModuleDescriptorSorter
block|{
specifier|public
specifier|static
name|List
name|sortNodes
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|,
name|Collection
name|nodes
parameter_list|)
block|{
comment|/* here we want to use the sort algorithm which work on module descriptors :          * so we first put dependencies on a map from descriptors to dependency, then we           * sort the keySet (i.e. a collection of descriptors), then we replace          * in the sorted list each descriptor by the corresponding dependency          */
name|Map
name|dependenciesMap
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
name|List
name|nulls
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|nodes
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|n
init|=
operator|(
name|List
operator|)
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
name|list
init|=
name|sortModuleDescriptors
argument_list|(
name|matcher
argument_list|,
name|dependenciesMap
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
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
literal|1.3
operator|+
name|nulls
operator|.
name|size
argument_list|()
operator|)
argument_list|)
decl_stmt|;
comment|//attempt to adjust the size to avoid too much list resizing
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
name|n
init|=
operator|(
name|List
operator|)
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
comment|/**      * Sorts the given ModuleDescriptors from the less dependent to the more dependent.      * This sort ensures that a ModuleDescriptor is always found in the list before all       * ModuleDescriptors depending directly on it.      * @param moduleDescriptors a Collection of ModuleDescriptor to sort      * @return a List of sorted ModuleDescriptors      * @throws CircularDependencyException if a circular dependency exists      */
specifier|public
specifier|static
name|List
name|sortModuleDescriptors
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|,
name|Collection
name|moduleDescriptors
parameter_list|)
throws|throws
name|CircularDependencyException
block|{
return|return
operator|new
name|ModuleDescriptorSorter
argument_list|(
name|moduleDescriptors
argument_list|)
operator|.
name|sortModuleDescriptors
argument_list|(
name|matcher
argument_list|)
return|;
block|}
specifier|private
specifier|final
name|Collection
name|moduleDescriptors
decl_stmt|;
specifier|private
specifier|final
name|Iterator
name|moduleDescriptorsIterator
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
specifier|public
name|ModuleDescriptorSorter
parameter_list|(
name|Collection
name|moduleDescriptors
parameter_list|)
block|{
name|this
operator|.
name|moduleDescriptors
operator|=
name|moduleDescriptors
expr_stmt|;
name|moduleDescriptorsIterator
operator|=
operator|new
name|LinkedList
argument_list|(
name|moduleDescriptors
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
comment|/**      * Iterates over all modules calling sortModuleDescriptorsHelp.      * @return sorted module      * @throws CircularDependencyException      */
specifier|public
name|List
name|sortModuleDescriptors
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|)
throws|throws
name|CircularDependencyException
block|{
while|while
condition|(
name|moduleDescriptorsIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sortModuleDescriptorsHelp
argument_list|(
name|matcher
argument_list|,
operator|(
name|ModuleDescriptor
operator|)
name|moduleDescriptorsIterator
operator|.
name|next
argument_list|()
argument_list|,
operator|new
name|Stack
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sorted
return|;
block|}
comment|/**      * If current module has already been added to list, returns,      * Otherwise invokes sortModuleDescriptorsHelp for all dependencies      * contained within set of moduleDescriptors.  Then finally adds self      * to list of sorted.      * @param current Current module to add to sorted list.      * @throws CircularDependencyException      */
specifier|private
name|void
name|sortModuleDescriptorsHelp
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|,
name|ModuleDescriptor
name|current
parameter_list|,
name|Stack
name|callStack
parameter_list|)
throws|throws
name|CircularDependencyException
block|{
comment|//if already sorted return
if|if
condition|(
name|sorted
operator|.
name|contains
argument_list|(
name|current
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|callStack
operator|.
name|contains
argument_list|(
name|current
argument_list|)
condition|)
block|{
name|callStack
operator|.
name|add
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"circular dependency ignored during sort: "
operator|+
name|CircularDependencyHelper
operator|.
name|formatMessage
argument_list|(
operator|(
name|ModuleDescriptor
index|[]
operator|)
name|callStack
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleDescriptor
index|[
name|callStack
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|ModuleDescriptor
name|moduleDescriptorDependency
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|descriptors
operator|!=
literal|null
operator|&&
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
name|moduleDescriptorDependency
operator|=
name|getModuleDescriptorDependency
argument_list|(
name|matcher
argument_list|,
name|descriptors
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|moduleDescriptorDependency
operator|!=
literal|null
condition|)
block|{
name|callStack
operator|.
name|push
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|sortModuleDescriptorsHelp
argument_list|(
name|matcher
argument_list|,
name|moduleDescriptorDependency
argument_list|,
name|callStack
argument_list|)
expr_stmt|;
name|callStack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
name|sorted
operator|.
name|add
argument_list|(
name|current
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param descriptor      * @return a ModuleDescriptor from the collection of module descriptors to sort.      * If none exists returns null.      */
specifier|private
name|ModuleDescriptor
name|getModuleDescriptorDependency
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|,
name|DependencyDescriptor
name|descriptor
parameter_list|)
block|{
name|Iterator
name|i
init|=
name|moduleDescriptors
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|md
operator|=
operator|(
name|ModuleDescriptor
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|descriptor
operator|.
name|getDependencyId
argument_list|()
operator|.
name|equals
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|md
return|;
block|}
if|else if
condition|(
name|matcher
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
condition|)
block|{
return|return
name|md
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

