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
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|LogOptions
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
name|retrieve
operator|.
name|RetrieveOptions
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
name|retrieve
operator|.
name|RetrieveReport
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
name|filter
operator|.
name|Filter
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
name|Mapper
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
name|PatternSet
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
name|util
operator|.
name|FileNameMapper
import|;
end_import

begin_comment
comment|/**  * This task allow to retrieve dependencies from the cache to a local directory like a lib dir.  */
end_comment

begin_class
specifier|public
class|class
name|IvyRetrieve
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
specifier|static
specifier|final
name|Collection
name|OVERWRITEMODE_VALUES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_ALWAYS
block|,
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_NEVER
block|,
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_NEWER
block|,
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_DIFFERENT
block|}
argument_list|)
decl_stmt|;
specifier|private
name|String
name|pattern
decl_stmt|;
specifier|private
name|String
name|ivypattern
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|sync
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|symlink
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|symlinkmass
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|overwriteMode
init|=
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_NEWER
decl_stmt|;
specifier|private
name|String
name|pathId
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|setId
init|=
literal|null
decl_stmt|;
specifier|private
name|Mapper
name|mapper
init|=
literal|null
decl_stmt|;
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|String
name|getPathId
parameter_list|()
block|{
return|return
name|pathId
return|;
block|}
specifier|public
name|void
name|setPathId
parameter_list|(
name|String
name|pathId
parameter_list|)
block|{
name|this
operator|.
name|pathId
operator|=
name|pathId
expr_stmt|;
block|}
specifier|public
name|String
name|getSetId
parameter_list|()
block|{
return|return
name|setId
return|;
block|}
specifier|public
name|void
name|setSetId
parameter_list|(
name|String
name|setId
parameter_list|)
block|{
name|this
operator|.
name|setId
operator|=
name|setId
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|getAllowedLogOptions
argument_list|()
operator|.
name|contains
argument_list|(
name|getLog
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"invalid option for 'log': "
operator|+
name|getLog
argument_list|()
operator|+
literal|". Available options are "
operator|+
name|getAllowedLogOptions
argument_list|()
argument_list|)
throw|;
block|}
name|pattern
operator|=
name|getProperty
argument_list|(
name|pattern
argument_list|,
name|getSettings
argument_list|()
argument_list|,
literal|"ivy.retrieve.pattern"
argument_list|)
expr_stmt|;
try|try
block|{
name|Filter
name|artifactFilter
init|=
name|getArtifactFilter
argument_list|()
decl_stmt|;
name|RetrieveReport
name|report
init|=
name|getIvyInstance
argument_list|()
operator|.
name|retrieve
argument_list|(
name|getResolvedMrid
argument_list|()
argument_list|,
operator|(
operator|(
name|RetrieveOptions
operator|)
operator|new
name|RetrieveOptions
argument_list|()
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
operator|)
operator|.
name|setConfs
argument_list|(
name|splitConfs
argument_list|(
name|getConf
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setDestArtifactPattern
argument_list|(
name|pattern
argument_list|)
operator|.
name|setDestIvyPattern
argument_list|(
name|ivypattern
argument_list|)
operator|.
name|setArtifactFilter
argument_list|(
name|artifactFilter
argument_list|)
operator|.
name|setSync
argument_list|(
name|sync
argument_list|)
operator|.
name|setOverwriteMode
argument_list|(
name|getOverwriteMode
argument_list|()
argument_list|)
operator|.
name|setUseOrigin
argument_list|(
name|isUseOrigin
argument_list|()
argument_list|)
operator|.
name|setMakeSymlinks
argument_list|(
name|symlink
argument_list|)
operator|.
name|setMakeSymlinksInMass
argument_list|(
name|symlinkmass
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|getResolveId
argument_list|()
argument_list|)
operator|.
name|setMapper
argument_list|(
name|mapper
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|MapperAdapter
argument_list|(
name|mapper
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|targetsCopied
init|=
name|report
operator|.
name|getNbrArtifactsCopied
argument_list|()
decl_stmt|;
name|boolean
name|haveTargetsBeenCopied
init|=
name|targetsCopied
operator|>
literal|0
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.nb.targets.copied"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|targetsCopied
argument_list|)
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.targets.copied"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|haveTargetsBeenCopied
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|getPathId
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|getPathId
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|report
operator|.
name|getRetrievedFiles
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
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getSetId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|FileSet
name|fileset
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fileset
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|getSetId
argument_list|()
argument_list|,
name|fileset
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setDir
argument_list|(
name|report
operator|.
name|getRetrieveRoot
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|report
operator|.
name|getRetrievedFiles
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
name|PatternSet
operator|.
name|NameEntry
name|ne
init|=
name|fileset
operator|.
name|createInclude
argument_list|()
decl_stmt|;
name|ne
operator|.
name|setName
argument_list|(
name|getPath
argument_list|(
name|report
operator|.
name|getRetrieveRoot
argument_list|()
argument_list|,
operator|(
name|File
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to ivy retrieve: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Collection
comment|/*<String> */
name|getAllowedLogOptions
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|LogOptions
operator|.
name|LOG_DEFAULT
block|,
name|LogOptions
operator|.
name|LOG_DOWNLOAD_ONLY
block|,
name|LogOptions
operator|.
name|LOG_QUIET
block|}
argument_list|)
return|;
block|}
specifier|public
name|String
name|getIvypattern
parameter_list|()
block|{
return|return
name|ivypattern
return|;
block|}
specifier|public
name|void
name|setIvypattern
parameter_list|(
name|String
name|ivypattern
parameter_list|)
block|{
name|this
operator|.
name|ivypattern
operator|=
name|ivypattern
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
comment|/**      * Option to create symlinks instead of copying.      */
specifier|public
name|void
name|setSymlink
parameter_list|(
name|boolean
name|symlink
parameter_list|)
block|{
name|this
operator|.
name|symlink
operator|=
name|symlink
expr_stmt|;
block|}
comment|/**      * Option to create symlinks in one mass action, instead of separately.      */
specifier|public
name|void
name|setSymlinkmass
parameter_list|(
name|boolean
name|symlinkmass
parameter_list|)
block|{
name|this
operator|.
name|symlinkmass
operator|=
name|symlinkmass
expr_stmt|;
block|}
specifier|public
name|void
name|setOverwriteMode
parameter_list|(
name|String
name|overwriteMode
parameter_list|)
block|{
if|if
condition|(
operator|!
name|OVERWRITEMODE_VALUES
operator|.
name|contains
argument_list|(
name|overwriteMode
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid overwriteMode value '"
operator|+
name|overwriteMode
operator|+
literal|"'. "
operator|+
literal|"Valid values are "
operator|+
name|OVERWRITEMODE_VALUES
argument_list|)
throw|;
block|}
name|this
operator|.
name|overwriteMode
operator|=
name|overwriteMode
expr_stmt|;
block|}
specifier|public
name|String
name|getOverwriteMode
parameter_list|()
block|{
return|return
name|overwriteMode
return|;
block|}
comment|/**      * Add a mapper to convert the file names.      *       * @param mapper      *            a<code>Mapper</code> value.      */
specifier|public
name|void
name|addMapper
parameter_list|(
name|Mapper
name|mapper
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|mapper
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot define more than one mapper"
argument_list|)
throw|;
block|}
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
block|}
comment|/**      * Add a nested filenamemapper.      *       * @param fileNameMapper      *            the mapper to add.      */
specifier|public
name|void
name|add
parameter_list|(
name|FileNameMapper
name|fileNameMapper
parameter_list|)
block|{
name|Mapper
name|m
init|=
operator|new
name|Mapper
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
name|fileNameMapper
argument_list|)
expr_stmt|;
name|addMapper
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the path of the file relative to the given base directory.      *       * @param base      *            the parent directory to which the file must be evaluated.      * @param file      *            the file for which the path should be returned      * @return the path of the file relative to the given base directory.      */
specifier|private
name|String
name|getPath
parameter_list|(
name|File
name|base
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|String
name|absoluteBasePath
init|=
name|base
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|int
name|beginIndex
init|=
name|absoluteBasePath
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// checks if the basePath ends with the file separator (which can for instance
comment|// happen if the basePath is the root on unix)
if|if
condition|(
operator|!
name|absoluteBasePath
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|beginIndex
operator|++
expr_stmt|;
comment|// skip the seperator char as well
block|}
return|return
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|substring
argument_list|(
name|beginIndex
argument_list|)
return|;
block|}
block|}
end_class

end_unit

