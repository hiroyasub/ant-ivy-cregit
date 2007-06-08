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
name|LinkedHashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|core
operator|.
name|sort
operator|.
name|WarningNonMatchingVersionReporter
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
name|parser
operator|.
name|ModuleDescriptorParserRegistry
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

begin_comment
comment|/**  * Creates an ant filelist of files (usually build.xml) ordered according to the dependencies  * declared in ivy files.  */
end_comment

begin_class
specifier|public
class|class
name|IvyBuildList
extends|extends
name|IvyTask
block|{
specifier|private
name|List
name|buildFileSets
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (FileSet)
specifier|private
name|String
name|reference
decl_stmt|;
specifier|private
name|boolean
name|haltOnError
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|skipBuildWithoutIvy
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|reverse
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|ivyFilePath
decl_stmt|;
specifier|private
name|String
name|root
init|=
literal|"*"
decl_stmt|;
specifier|private
name|boolean
name|excludeRoot
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|leaf
init|=
literal|"*"
decl_stmt|;
specifier|private
name|String
name|delimiter
init|=
literal|","
decl_stmt|;
specifier|private
name|boolean
name|excludeLeaf
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|addFileset
parameter_list|(
name|FileSet
name|buildFiles
parameter_list|)
block|{
name|buildFileSets
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
name|reference
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
name|this
operator|.
name|reference
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
name|root
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
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExcludeRoot
parameter_list|()
block|{
return|return
name|excludeRoot
return|;
block|}
specifier|public
name|void
name|setExcludeRoot
parameter_list|(
name|boolean
name|root
parameter_list|)
block|{
name|excludeRoot
operator|=
name|root
expr_stmt|;
block|}
specifier|public
name|String
name|getLeaf
parameter_list|()
block|{
return|return
name|leaf
return|;
block|}
specifier|public
name|void
name|setLeaf
parameter_list|(
name|String
name|leaf
parameter_list|)
block|{
name|this
operator|.
name|leaf
operator|=
name|leaf
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExcludeLeaf
parameter_list|()
block|{
return|return
name|excludeLeaf
return|;
block|}
specifier|public
name|void
name|setExcludeLeaf
parameter_list|(
name|boolean
name|excludeLeaf
parameter_list|)
block|{
name|this
operator|.
name|excludeLeaf
operator|=
name|excludeLeaf
expr_stmt|;
block|}
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|reference
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
name|buildFileSets
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
name|ivyFilePath
operator|=
name|getProperty
argument_list|(
name|ivyFilePath
argument_list|,
name|settings
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
name|Map
name|mdsMap
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map (String moduleName -> ModuleDescriptor)
name|List
name|independent
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Set
name|rootModuleNames
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|root
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|root
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|rootModuleNames
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Set
name|leafModuleNames
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|leaf
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|leaf
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|leafModuleNames
operator|.
name|add
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|ListIterator
name|iter
init|=
name|buildFileSets
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
name|skipBuildWithoutIvy
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
literal|"\t(set skipbuildwithoutivy to true if you don't want this"
operator|+
literal|" file to be added to the path)"
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
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|ivyFile
operator|.
name|toURL
argument_list|()
argument_list|,
name|doValidate
argument_list|(
name|settings
argument_list|)
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
name|mdsMap
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
operator|.
name|getName
argument_list|()
argument_list|,
name|md
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"Add "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|haltOnError
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
name|List
name|leafModuleDescriptors
init|=
name|convertModuleNamesToModuleDescriptors
argument_list|(
name|mdsMap
argument_list|,
name|leafModuleNames
argument_list|,
literal|"leaf"
argument_list|)
decl_stmt|;
name|List
name|rootModuleDescriptors
init|=
name|convertModuleNamesToModuleDescriptors
argument_list|(
name|mdsMap
argument_list|,
name|rootModuleNames
argument_list|,
literal|"root"
argument_list|)
decl_stmt|;
name|Collection
name|mds
init|=
operator|new
name|ArrayList
argument_list|(
name|mdsMap
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|rootModuleDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"Filtering modules based on roots "
operator|+
name|rootModuleNames
argument_list|)
expr_stmt|;
name|mds
operator|=
name|filterModulesFromRoot
argument_list|(
name|mds
argument_list|,
name|rootModuleDescriptors
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|leafModuleDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"Filtering modules based on leafs "
operator|+
name|leafModuleNames
argument_list|)
expr_stmt|;
name|mds
operator|=
name|filterModulesFromLeaf
argument_list|(
name|mds
argument_list|,
name|leafModuleDescriptors
argument_list|)
expr_stmt|;
block|}
name|WarningNonMatchingVersionReporter
name|nonMatchingVersionReporter
init|=
operator|new
name|WarningNonMatchingVersionReporter
argument_list|()
decl_stmt|;
name|List
name|sortedModules
init|=
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|mds
argument_list|,
name|nonMatchingVersionReporter
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
name|StringBuffer
name|order
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
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
name|order
operator|.
name|append
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|order
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
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
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.sorted.modules"
argument_list|,
name|order
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
name|convertModuleNamesToModuleDescriptors
parameter_list|(
name|Map
name|mdsMap
parameter_list|,
name|Set
name|moduleNames
parameter_list|,
name|String
name|kind
parameter_list|)
block|{
name|List
name|mds
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
name|moduleNames
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
name|String
name|name
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|mdsMap
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|md
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to find "
operator|+
name|kind
operator|+
literal|" module "
operator|+
name|name
operator|+
literal|" in build fileset"
argument_list|)
throw|;
block|}
name|mds
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
return|return
name|mds
return|;
block|}
comment|/**      * Returns a collection of ModuleDescriptors that are conatined in the input collection of      * ModuleDescriptors and upon which the root module depends      *       * @param mds      *            input collection of ModuleDescriptors      * @param rootmd      *            root module      * @return filtered list of modules      */
specifier|private
name|Collection
name|filterModulesFromRoot
parameter_list|(
name|Collection
name|mds
parameter_list|,
name|List
name|rootmds
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
name|LinkedHashSet
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|rootmds
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleDescriptor
name|rootmd
init|=
operator|(
name|ModuleDescriptor
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|processFilterNodeFromRoot
argument_list|(
name|rootmd
argument_list|,
name|toKeep
argument_list|,
name|moduleIdMap
argument_list|)
expr_stmt|;
comment|// With the excluderoot attribute set to true, take the rootmd out of the toKeep set.
if|if
condition|(
name|excludeRoot
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Excluded module "
operator|+
name|rootmd
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
name|toKeep
operator|.
name|remove
argument_list|(
name|rootmd
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Adds the current node to the toKeep collection and then processes the each of the direct      * dependencies of this node that appear in the moduleIdMap (indicating that the dependency is      * part of this BuildList)      *       * @param node      *            the node to be processed      * @param toKeep      *            the set of ModuleDescriptors that should be kept      * @param moduleIdMap      *            reference mapping of moduleId to ModuleDescriptor that are part of the BuildList      */
specifier|private
name|void
name|processFilterNodeFromRoot
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
name|processFilterNodeFromRoot
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
comment|/**      * Returns a collection of ModuleDescriptors that are conatined in the input collection of      * ModuleDescriptors which depends on the leaf module      *       * @param mds      *            input collection of ModuleDescriptors      * @param leafmd      *            leaf module      * @return filtered list of modules      */
specifier|private
name|Collection
name|filterModulesFromLeaf
parameter_list|(
name|Collection
name|mds
parameter_list|,
name|List
name|leafmds
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
name|LinkedHashSet
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|leafmds
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleDescriptor
name|leafmd
init|=
operator|(
name|ModuleDescriptor
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// With the excludeleaf attribute set to true, take the rootmd out of the toKeep set.
if|if
condition|(
name|excludeLeaf
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Excluded module "
operator|+
name|leafmd
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
else|else
block|{
name|toKeep
operator|.
name|add
argument_list|(
name|leafmd
argument_list|)
expr_stmt|;
block|}
name|processFilterNodeFromLeaf
argument_list|(
name|leafmd
argument_list|,
name|toKeep
argument_list|,
name|moduleIdMap
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Search in the moduleIdMap modules depending on node, add them to the toKeep set and process      * them recursively.      *       * @param node      *            the node to be processed      * @param toKeep      *            the set of ModuleDescriptors that should be kept      * @param moduleIdMap      *            reference mapping of moduleId to ModuleDescriptor that are part of the BuildList      */
specifier|private
name|void
name|processFilterNodeFromLeaf
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
for|for
control|(
name|Iterator
name|iter
init|=
name|moduleIdMap
operator|.
name|values
argument_list|()
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
name|ModuleDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|DependencyDescriptor
index|[]
name|deps
init|=
name|md
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
name|node
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|&&
operator|!
name|toKeep
operator|.
name|contains
argument_list|(
name|md
argument_list|)
condition|)
block|{
name|toKeep
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|processFilterNodeFromLeaf
argument_list|(
name|md
argument_list|,
name|toKeep
argument_list|,
name|moduleIdMap
argument_list|)
expr_stmt|;
block|}
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
name|ivyFilePath
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isHaltonerror
parameter_list|()
block|{
return|return
name|haltOnError
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
name|this
operator|.
name|haltOnError
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
name|ivyFilePath
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
name|this
operator|.
name|ivyFilePath
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
name|skipBuildWithoutIvy
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
name|this
operator|.
name|skipBuildWithoutIvy
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
name|reverse
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
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
block|}
block|}
end_class

end_unit

