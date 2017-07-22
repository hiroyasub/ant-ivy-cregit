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
name|HashSet
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
name|SortOptions
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
specifier|public
specifier|static
specifier|final
class|class
name|OnMissingDescriptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|HEAD
init|=
literal|"head"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TAIL
init|=
literal|"tail"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SKIP
init|=
literal|"skip"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FAIL
init|=
literal|"fail"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WARN
init|=
literal|"warn"
decl_stmt|;
specifier|private
name|OnMissingDescriptor
parameter_list|()
block|{
block|}
block|}
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTOR_REQUIRED
init|=
literal|"required"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|FileSet
argument_list|>
name|buildFileSets
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
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
name|String
name|onMissingDescriptor
init|=
name|OnMissingDescriptor
operator|.
name|HEAD
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
specifier|private
name|boolean
name|onlydirectdep
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|restartFrom
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
name|boolean
name|getOnlydirectdep
parameter_list|()
block|{
return|return
name|onlydirectdep
return|;
block|}
specifier|public
name|void
name|setOnlydirectdep
parameter_list|(
name|boolean
name|onlydirectdep
parameter_list|)
block|{
name|this
operator|.
name|onlydirectdep
operator|=
name|onlydirectdep
expr_stmt|;
block|}
annotation|@
name|Override
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
argument_list|<
name|ModuleDescriptor
argument_list|,
name|File
argument_list|>
name|buildFiles
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|independent
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|noDescriptor
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|mds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|rootModuleNames
init|=
operator|new
name|LinkedHashSet
argument_list|<>
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
argument_list|<
name|String
argument_list|>
name|leafModuleNames
init|=
operator|new
name|LinkedHashSet
argument_list|<>
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
name|Set
argument_list|<
name|String
argument_list|>
name|restartFromModuleNames
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|restartFrom
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|restartFrom
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
comment|// Only accept one (first) module
name|restartFromModuleNames
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
for|for
control|(
name|FileSet
name|fs
range|:
name|buildFileSets
control|)
block|{
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
for|for
control|(
name|String
name|build
range|:
name|ds
operator|.
name|getIncludedFiles
argument_list|()
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
name|build
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
name|onMissingDescriptor
argument_list|(
name|buildFile
argument_list|,
name|ivyFile
argument_list|,
name|noDescriptor
argument_list|)
expr_stmt|;
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
name|toURI
argument_list|()
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
name|mds
operator|.
name|add
argument_list|(
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
argument_list|<
name|ModuleDescriptor
argument_list|>
name|leafModuleDescriptors
init|=
name|convertModuleNamesToModuleDescriptors
argument_list|(
name|mds
argument_list|,
name|leafModuleNames
argument_list|,
literal|"leaf"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|rootModuleDescriptors
init|=
name|convertModuleNamesToModuleDescriptors
argument_list|(
name|mds
argument_list|,
name|rootModuleNames
argument_list|,
literal|"root"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|restartFromModuleDescriptors
init|=
name|convertModuleNamesToModuleDescriptors
argument_list|(
name|mds
argument_list|,
name|restartFromModuleNames
argument_list|,
literal|"restartFrom"
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
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|sortedModules
init|=
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|mds
argument_list|,
name|SortOptions
operator|.
name|DEFAULT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|OnMissingDescriptor
operator|.
name|TAIL
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
condition|)
block|{
for|for
control|(
name|File
name|buildFile
range|:
name|noDescriptor
control|)
block|{
name|addBuildFile
argument_list|(
name|path
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|File
name|buildFile
range|:
name|independent
control|)
block|{
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
comment|// Remove modules that are before the restartFrom point
comment|// Independent modules (without valid ivy file) can not be addressed
comment|// so they are not removed from build path.
if|if
condition|(
operator|!
name|restartFromModuleDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|foundRestartFrom
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|keptModules
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|restartFromModuleDescriptor
init|=
name|restartFromModuleDescriptors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|sortedModules
control|)
block|{
if|if
condition|(
name|md
operator|.
name|equals
argument_list|(
name|restartFromModuleDescriptor
argument_list|)
condition|)
block|{
name|foundRestartFrom
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|foundRestartFrom
condition|)
block|{
name|keptModules
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
block|}
name|sortedModules
operator|=
name|keptModules
expr_stmt|;
block|}
name|StringBuilder
name|order
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|sortedModules
control|)
block|{
if|if
condition|(
name|order
operator|.
name|length
argument_list|()
operator|>
literal|0
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
name|addBuildFile
argument_list|(
name|path
argument_list|,
name|buildFiles
operator|.
name|get
argument_list|(
name|md
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|OnMissingDescriptor
operator|.
name|TAIL
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
condition|)
block|{
for|for
control|(
name|File
name|buildFile
range|:
name|noDescriptor
control|)
block|{
name|addBuildFile
argument_list|(
name|path
argument_list|,
name|buildFile
argument_list|)
expr_stmt|;
block|}
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
name|void
name|onMissingDescriptor
parameter_list|(
name|File
name|buildFile
parameter_list|,
name|File
name|ivyFile
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|noDescriptor
parameter_list|)
block|{
if|if
condition|(
name|OnMissingDescriptor
operator|.
name|SKIP
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
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
literal|": descriptor "
operator|+
name|ivyFile
operator|+
literal|" doesn't exist"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|OnMissingDescriptor
operator|.
name|FAIL
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"a module has no module descriptor and onMissingDescriptor=fail. "
operator|+
literal|"Build file: "
operator|+
name|buildFile
operator|+
literal|". Expected descriptor: "
operator|+
name|ivyFile
argument_list|)
throw|;
block|}
else|else
block|{
if|if
condition|(
name|OnMissingDescriptor
operator|.
name|WARN
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"a module has no module descriptor. "
operator|+
literal|"Build file: "
operator|+
name|buildFile
operator|+
literal|". Expected descriptor: "
operator|+
name|ivyFile
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"no descriptor for %s: descriptor=%s: adding it at the %s of the path"
argument_list|,
name|buildFile
argument_list|,
name|ivyFile
argument_list|,
operator|(
name|OnMissingDescriptor
operator|.
name|TAIL
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
condition|?
literal|"tail"
else|:
literal|"head"
operator|)
argument_list|)
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t(change onMissingDescriptor if you want to take another action"
argument_list|)
expr_stmt|;
name|noDescriptor
operator|.
name|add
argument_list|(
name|buildFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|convertModuleNamesToModuleDescriptors
parameter_list|(
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|mds
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|moduleNames
parameter_list|,
name|String
name|kind
parameter_list|)
block|{
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|foundModuleNames
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|mds
control|)
block|{
name|String
name|name
init|=
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
decl_stmt|;
if|if
condition|(
name|moduleNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|foundModuleNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|foundModuleNames
operator|.
name|size
argument_list|()
operator|<
name|moduleNames
operator|.
name|size
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|missingModules
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|moduleNames
argument_list|)
decl_stmt|;
name|missingModules
operator|.
name|removeAll
argument_list|(
name|foundModuleNames
argument_list|)
expr_stmt|;
name|StringBuilder
name|missingNames
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|sep
init|=
literal|""
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|missingModules
control|)
block|{
name|missingNames
operator|.
name|append
argument_list|(
name|sep
argument_list|)
expr_stmt|;
name|missingNames
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|sep
operator|=
literal|", "
expr_stmt|;
block|}
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to find "
operator|+
name|kind
operator|+
literal|" module(s) "
operator|+
name|missingNames
operator|.
name|toString
argument_list|()
operator|+
literal|" in build fileset"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Returns a collection of ModuleDescriptors that are contained in the input collection of      * ModuleDescriptors and upon which the root module depends      *      * @param mds      *            input collection of ModuleDescriptors      * @param rootmds      *            root module      * @return filtered list of modules      */
specifier|private
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|filterModulesFromRoot
parameter_list|(
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|mds
parameter_list|,
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|rootmds
parameter_list|)
block|{
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|ModuleDescriptor
argument_list|>
name|moduleIdMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|mds
control|)
block|{
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
argument_list|<
name|ModuleDescriptor
argument_list|>
name|toKeep
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|rootmd
range|:
name|rootmds
control|)
block|{
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
comment|// Only for logging purposes
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
block|}
else|else
block|{
name|toKeep
operator|.
name|add
argument_list|(
name|rootmd
argument_list|)
expr_stmt|;
block|}
block|}
comment|// just for logging
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|toKeep
control|)
block|{
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
comment|/**      * Adds the current node to the toKeep collection and then processes the each of the direct      * dependencies of this node that appear in the moduleIdMap (indicating that the dependency is      * part of this BuildList)      *      * @param node      *            the node to be processed      * @param toKeep      *            the set of ModuleDescriptors that should be kept      * @param moduleIdMap      *            reference mapping of moduleId to ModuleDescriptor that are part of the BuildList      */
specifier|private
name|void
name|processFilterNodeFromRoot
parameter_list|(
name|ModuleDescriptor
name|node
parameter_list|,
name|Set
argument_list|<
name|ModuleDescriptor
argument_list|>
name|toKeep
parameter_list|,
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|ModuleDescriptor
argument_list|>
name|moduleIdMap
parameter_list|)
block|{
comment|// toKeep.add(node);
for|for
control|(
name|DependencyDescriptor
name|dep
range|:
name|node
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|ModuleId
name|id
init|=
name|dep
operator|.
name|getDependencyId
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
name|moduleIdMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// we test if this module id has a module descriptor, and if it isn't already in the
comment|// toKeep Set, in which there's probably a circular dependency
if|if
condition|(
name|md
operator|!=
literal|null
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
if|if
condition|(
operator|!
name|getOnlydirectdep
argument_list|()
condition|)
block|{
name|processFilterNodeFromRoot
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
comment|/**      * Returns a collection of ModuleDescriptors that are contained in the input collection of      * ModuleDescriptors which depends on the leaf module      *      * @param mds      *            input collection of ModuleDescriptors      * @param leafmds      *            leaf module      * @return filtered list of modules      */
specifier|private
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|filterModulesFromLeaf
parameter_list|(
name|Collection
argument_list|<
name|ModuleDescriptor
argument_list|>
name|mds
parameter_list|,
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|leafmds
parameter_list|)
block|{
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|ModuleDescriptor
argument_list|>
name|moduleIdMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|mds
control|)
block|{
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
argument_list|<
name|ModuleDescriptor
argument_list|>
name|toKeep
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|leafmd
range|:
name|leafmds
control|)
block|{
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
name|ModuleDescriptor
name|md
range|:
name|toKeep
control|)
block|{
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
comment|/**      * Search in the moduleIdMap modules depending on node, add them to the toKeep set and process      * them recursively.      *      * @param node      *            the node to be processed      * @param toKeep      *            the set of ModuleDescriptors that should be kept      * @param moduleIdMap      *            reference mapping of moduleId to ModuleDescriptor that are part of the BuildList      */
specifier|private
name|void
name|processFilterNodeFromLeaf
parameter_list|(
name|ModuleDescriptor
name|node
parameter_list|,
name|Set
argument_list|<
name|ModuleDescriptor
argument_list|>
name|toKeep
parameter_list|,
name|Map
argument_list|<
name|ModuleId
argument_list|,
name|ModuleDescriptor
argument_list|>
name|moduleIdMap
parameter_list|)
block|{
for|for
control|(
name|ModuleDescriptor
name|md
range|:
name|moduleIdMap
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|DependencyDescriptor
name|dep
range|:
name|md
operator|.
name|getDependencies
argument_list|()
control|)
block|{
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
name|dep
operator|.
name|getDependencyId
argument_list|()
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
if|if
condition|(
operator|!
name|getOnlydirectdep
argument_list|()
condition|)
block|{
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
name|FileSet
name|fs
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setFile
argument_list|(
name|buildFile
argument_list|)
expr_stmt|;
name|path
operator|.
name|addFileset
argument_list|(
name|fs
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
name|String
name|getOnMissingDescriptor
parameter_list|()
block|{
return|return
name|onMissingDescriptor
return|;
block|}
specifier|public
name|void
name|setOnMissingDescriptor
parameter_list|(
name|String
name|onMissingDescriptor
parameter_list|)
block|{
name|this
operator|.
name|onMissingDescriptor
operator|=
name|onMissingDescriptor
expr_stmt|;
block|}
comment|/**      * @return boolean      * @deprecated use {@link #getOnMissingDescriptor()} instead.      */
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isSkipbuildwithoutivy
parameter_list|()
block|{
return|return
name|OnMissingDescriptor
operator|.
name|SKIP
operator|.
name|equals
argument_list|(
name|onMissingDescriptor
argument_list|)
return|;
block|}
comment|/**      * @param skipBuildFilesWithoutIvy boolean      * @deprecated use {@link #setOnMissingDescriptor(String)} instead.      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setSkipbuildwithoutivy
parameter_list|(
name|boolean
name|skipBuildFilesWithoutIvy
parameter_list|)
block|{
name|Message
operator|.
name|deprecated
argument_list|(
literal|"skipbuildwithoutivy is deprecated, use onMissingDescriptor instead."
argument_list|)
expr_stmt|;
name|this
operator|.
name|onMissingDescriptor
operator|=
name|skipBuildFilesWithoutIvy
condition|?
name|OnMissingDescriptor
operator|.
name|SKIP
else|:
name|OnMissingDescriptor
operator|.
name|FAIL
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
specifier|public
name|String
name|getRestartFrom
parameter_list|()
block|{
return|return
name|restartFrom
return|;
block|}
specifier|public
name|void
name|setRestartFrom
parameter_list|(
name|String
name|restartFrom
parameter_list|)
block|{
name|this
operator|.
name|restartFrom
operator|=
name|restartFrom
expr_stmt|;
block|}
block|}
end_class

end_unit

