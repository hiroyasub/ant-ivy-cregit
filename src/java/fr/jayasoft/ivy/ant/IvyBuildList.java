begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ant
package|;
end_package

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
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
name|ModuleDescriptor
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
name|ModuleId
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
name|DependencyDescriptor
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
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|DirectoryScanner
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
name|types
operator|.
name|FileList
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
name|types
operator|.
name|FileSet
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
name|types
operator|.
name|Path
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|Iterator
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
name|Set
import|;
end_import

begin_class
specifier|public
class|class
name|IvyBuildList
extends|extends
name|IvyTask
block|{
specifier|private
name|List
name|_buildFiles
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (FileSet)
specifier|private
name|String
name|_reference
decl_stmt|;
specifier|private
name|boolean
name|_haltOnError
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_skipBuildWithoutIvy
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|_reverse
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|_ivyFilePath
decl_stmt|;
specifier|private
name|String
name|_root
init|=
literal|"*"
decl_stmt|;
specifier|public
name|void
name|addFileset
parameter_list|(
name|FileSet
name|buildFiles
parameter_list|)
block|{
name|_buildFiles
operator|.
name|add
argument_list|(
name|buildFiles
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getReference
parameter_list|()
block|{
return|return
name|_reference
return|;
block|}
specifier|public
name|void
name|setReference
parameter_list|(
name|String
name|reference
parameter_list|)
block|{
name|_reference
operator|=
name|reference
expr_stmt|;
block|}
specifier|public
name|String
name|getRoot
parameter_list|()
block|{
return|return
name|_root
return|;
block|}
specifier|public
name|void
name|setRoot
parameter_list|(
name|String
name|root
parameter_list|)
block|{
name|_root
operator|=
name|root
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|_reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"reference should be provided in ivy build list"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_buildFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"at least one nested fileset should be provided in ivy build list"
argument_list|)
throw|;
block|}
name|_ivyFilePath
operator|=
name|getProperty
argument_list|(
name|_ivyFilePath
argument_list|,
name|getIvyInstance
argument_list|()
argument_list|,
literal|"ivy.buildlist.ivyfilepath"
argument_list|)
expr_stmt|;
name|Path
name|path
init|=
operator|new
name|Path
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|Map
name|buildFiles
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleDescriptor -> File buildFile)
name|Collection
name|mds
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|independent
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|rootModuleDescriptor
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|_buildFiles
operator|.
name|listIterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|FileSet
name|fs
init|=
operator|(
name|FileSet
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|DirectoryScanner
name|ds
init|=
name|fs
operator|.
name|getDirectoryScanner
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|builds
init|=
name|ds
operator|.
name|getIncludedFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|builds
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|buildFile
init|=
operator|new
name|File
argument_list|(
name|ds
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|builds
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|File
name|ivyFile
init|=
name|getIvyFileFor
argument_list|(
name|buildFile
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|_skipBuildWithoutIvy
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"skipping "
operator|+
name|buildFile
operator|+
literal|": ivy file "
operator|+
name|ivyFile
operator|+
literal|" doesn't exist"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no ivy file for "
operator|+
name|buildFile
operator|+
literal|": ivyfile="
operator|+
name|ivyFile
operator|+
literal|": adding it at the beginning of the path"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t(set skipbuildwithoutivy to true if you don't want this file to be added to the path)"
argument_list|)
expr_stmt|;
name|independent
operator|.
name|add
argument_list|(
name|buildFile
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|parseDescriptor
argument_list|(
name|getIvyInstance
argument_list|()
argument_list|,
name|ivyFile
operator|.
name|toURL
argument_list|()
argument_list|,
name|isValidate
argument_list|()
argument_list|)
decl_stmt|;
name|buildFiles
operator|.
name|put
argument_list|(
name|md
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
name|mds
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
if|if
condition|(
name|_root
operator|.
name|equals
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|rootModuleDescriptor
operator|=
name|md
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|_haltOnError
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to parse ivy file for "
operator|+
name|buildFile
operator|+
literal|": ivyfile="
operator|+
name|ivyFile
operator|+
literal|" exception="
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
else|else
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"impossible to parse ivy file for "
operator|+
name|buildFile
operator|+
literal|": ivyfile="
operator|+
name|ivyFile
operator|+
literal|" exception="
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\t=> adding it at the beginning of the path"
argument_list|)
expr_stmt|;
name|independent
operator|.
name|add
argument_list|(
name|buildFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|_root
argument_list|)
operator|&&
name|rootModuleDescriptor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to find root module "
operator|+
name|_root
operator|+
literal|" in build fileset"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rootModuleDescriptor
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"Filtering modules based on root "
operator|+
name|rootModuleDescriptor
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|mds
operator|=
name|filterModules
argument_list|(
name|mds
argument_list|,
name|rootModuleDescriptor
argument_list|)
expr_stmt|;
block|}
name|List
name|sortedModules
init|=
name|Ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|mds
argument_list|)
decl_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|independent
operator|.
name|listIterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|File
name|buildFile
init|=
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|addBuildFile
argument_list|(
name|path
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isReverse
argument_list|()
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|sortedModules
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ListIterator
name|iter
init|=
name|sortedModules
operator|.
name|listIterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|File
name|buildFile
init|=
operator|(
name|File
operator|)
name|buildFiles
operator|.
name|get
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|addBuildFile
argument_list|(
name|path
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
block|}
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|getReference
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a collection of ModuleDescriptors that are conatined in the input      * collection of ModuleDescriptors and upon which the root module depends      *      * @param mds input collection of ModuleDescriptors      * @param rootmd root module      * @return filtered list of modules      */
specifier|private
name|Collection
name|filterModules
parameter_list|(
name|Collection
name|mds
parameter_list|,
name|ModuleDescriptor
name|rootmd
parameter_list|)
block|{
comment|// Make a map of ModuleId objects -> ModuleDescriptors
name|Map
name|moduleIdMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|mds
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
name|ModuleDescriptor
name|md
init|=
operator|(
operator|(
name|ModuleDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
operator|)
decl_stmt|;
name|moduleIdMap
operator|.
name|put
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
argument_list|)
expr_stmt|;
block|}
comment|// recursively process the nodes
name|Set
name|toKeep
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|processFilterNode
argument_list|(
name|rootmd
argument_list|,
name|toKeep
argument_list|,
name|moduleIdMap
argument_list|)
expr_stmt|;
comment|// just for logging
for|for
control|(
name|Iterator
name|iter
init|=
name|toKeep
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
name|ModuleDescriptor
name|md
init|=
operator|(
operator|(
name|ModuleDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
operator|)
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"Kept module "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|toKeep
return|;
block|}
comment|/**      * Adds the current node to the toKeep collection and then processes the each of the direct dependencies      * of this node that appear in the moduleIdMap (indicating that the dependency is part of this BuildList)      *      * @param node the node to be processed      * @param toKeep the set of ModuleDescriptors that should be kept      * @param moduleIdMap reference mapping of moduleId to ModuleDescriptor that are part of the BuildList      */
specifier|private
name|void
name|processFilterNode
parameter_list|(
name|ModuleDescriptor
name|node
parameter_list|,
name|Set
name|toKeep
parameter_list|,
name|Map
name|moduleIdMap
parameter_list|)
block|{
name|toKeep
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|deps
init|=
name|node
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|deps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ModuleId
name|id
init|=
name|deps
index|[
name|i
index|]
operator|.
name|getDependencyId
argument_list|()
decl_stmt|;
if|if
condition|(
name|moduleIdMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|processFilterNode
argument_list|(
operator|(
name|ModuleDescriptor
operator|)
name|moduleIdMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|,
name|toKeep
argument_list|,
name|moduleIdMap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addBuildFile
parameter_list|(
name|Path
name|path
parameter_list|,
name|File
name|buildFile
parameter_list|)
block|{
name|FileList
name|fl
init|=
operator|new
name|FileList
argument_list|()
decl_stmt|;
name|fl
operator|.
name|setDir
argument_list|(
name|buildFile
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|FileList
operator|.
name|FileName
name|fileName
init|=
operator|new
name|FileList
operator|.
name|FileName
argument_list|()
decl_stmt|;
name|fileName
operator|.
name|setName
argument_list|(
name|buildFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fl
operator|.
name|addConfiguredFile
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|path
operator|.
name|addFilelist
argument_list|(
name|fl
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|getIvyFileFor
parameter_list|(
name|File
name|buildFile
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|buildFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|_ivyFilePath
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isHaltonerror
parameter_list|()
block|{
return|return
name|_haltOnError
return|;
block|}
specifier|public
name|void
name|setHaltonerror
parameter_list|(
name|boolean
name|haltOnError
parameter_list|)
block|{
name|_haltOnError
operator|=
name|haltOnError
expr_stmt|;
block|}
specifier|public
name|String
name|getIvyfilepath
parameter_list|()
block|{
return|return
name|_ivyFilePath
return|;
block|}
specifier|public
name|void
name|setIvyfilepath
parameter_list|(
name|String
name|ivyFilePath
parameter_list|)
block|{
name|_ivyFilePath
operator|=
name|ivyFilePath
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSkipbuildwithoutivy
parameter_list|()
block|{
return|return
name|_skipBuildWithoutIvy
return|;
block|}
specifier|public
name|void
name|setSkipbuildwithoutivy
parameter_list|(
name|boolean
name|skipBuildFilesWithoutIvy
parameter_list|)
block|{
name|_skipBuildWithoutIvy
operator|=
name|skipBuildFilesWithoutIvy
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReverse
parameter_list|()
block|{
return|return
name|_reverse
return|;
block|}
specifier|public
name|void
name|setReverse
parameter_list|(
name|boolean
name|reverse
parameter_list|)
block|{
name|_reverse
operator|=
name|reverse
expr_stmt|;
block|}
block|}
end_class

end_unit

