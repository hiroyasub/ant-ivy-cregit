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
name|NoSuchElementException
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|Project
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
name|PatternSet
operator|.
name|NameEntry
import|;
end_import

begin_comment
comment|/**  * Creates an ant fileset consisting in all artifacts found during a resolve. Note that this task is  * not compatible with the useOrigin mode.  */
end_comment

begin_class
specifier|public
class|class
name|IvyCacheFileset
extends|extends
name|IvyCacheTask
block|{
specifier|private
name|String
name|setid
decl_stmt|;
specifier|public
name|String
name|getSetid
parameter_list|()
block|{
return|return
name|setid
return|;
block|}
specifier|public
name|void
name|setSetid
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|setid
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|void
name|setUseOrigin
parameter_list|(
name|boolean
name|useOrigin
parameter_list|)
block|{
if|if
condition|(
name|useOrigin
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"the cachefileset task does not support the useOrigin mode, since filesets "
operator|+
literal|"require to have only one root directory. Please use the the cachepath "
operator|+
literal|"task instead"
argument_list|)
throw|;
block|}
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
name|setid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"setid is required in ivy cachefileset"
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|artifactDownloadReports
init|=
name|getArtifactReports
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactDownloadReports
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// generate an empty fileset
specifier|final
name|FileSet
name|emptyFileSet
init|=
operator|new
name|EmptyFileSet
argument_list|()
decl_stmt|;
name|emptyFileSet
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
name|setid
argument_list|,
name|emptyFileSet
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// find a common base dir of the resolved artifacts
specifier|final
name|File
name|baseDir
init|=
name|this
operator|.
name|requireCommonBaseDir
argument_list|(
name|artifactDownloadReports
argument_list|)
decl_stmt|;
specifier|final
name|FileSet
name|fileset
init|=
operator|new
name|FileSet
argument_list|()
decl_stmt|;
name|fileset
operator|.
name|setDir
argument_list|(
name|baseDir
argument_list|)
expr_stmt|;
name|fileset
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
comment|// enroll each of the artifact files into the fileset
for|for
control|(
specifier|final
name|ArtifactDownloadReport
name|artifactDownloadReport
range|:
name|artifactDownloadReports
control|)
block|{
if|if
condition|(
name|artifactDownloadReport
operator|.
name|getLocalFile
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
specifier|final
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
name|baseDir
argument_list|,
name|artifactDownloadReport
operator|.
name|getLocalFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|setid
argument_list|,
name|fileset
argument_list|)
expr_stmt|;
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
literal|"impossible to build ivy cache fileset: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a common base directory, determined from the {@link ArtifactDownloadReport#getLocalFile() local files} of the      * passed<code>artifactDownloadReports</code>. If no common base directory can be determined, this method throws a      * {@link BuildException}      *      * @param artifactDownloadReports The artifact download reports for which the common base directory of the artifacts      *                                has to be determined      * @return      */
specifier|private
name|File
name|requireCommonBaseDir
parameter_list|(
specifier|final
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|artifactDownloadReports
parameter_list|)
block|{
name|File
name|base
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|ArtifactDownloadReport
name|artifactDownloadReport
range|:
name|artifactDownloadReports
control|)
block|{
if|if
condition|(
name|artifactDownloadReport
operator|.
name|getLocalFile
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
comment|// use the parent dir of the artifact as the base
name|base
operator|=
name|artifactDownloadReport
operator|.
name|getLocalFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// try and find a common base directory between the current base
comment|// directory and the artifact's file
name|base
operator|=
name|getBaseDir
argument_list|(
name|base
argument_list|,
name|artifactDownloadReport
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
comment|// fail fast - we couldn't determine a common base directory, throw an error
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Cannot find a common base directory, from resolved artifacts, "
operator|+
literal|"for generating a cache fileset"
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
comment|// finally, we couldn't determine a common base directory, throw an error
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Cannot find a common base directory, from resolved artifacts, for generating "
operator|+
literal|"a cache fileset"
argument_list|)
throw|;
block|}
return|return
name|base
return|;
block|}
comment|/**      * Returns the path of the file relative to the given base directory.      *      * @param base      *            the parent directory to which the file must be evaluated.      * @param file      *            the file for which the path should be returned      * @return the path of the file relative to the given base directory.      */
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
comment|// skip the separator char as well
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
comment|/**      * Returns the common base directory between the passed<code>file1</code> and<code>file2</code>.      *<p>      * The returned base directory must be a parent of both the<code>file1</code> and<code>file2</code>.      *</p>      *      * @param file1      *            One of the files, for which the common base directory is being sought, may be null.      * @param file2      *            The other file for which the common base directory should be returned.      * @return the common base directory between a<code>file1</code> and<code>file2</code>. Returns null      *          if no common base directory could be determined or if either<code>file1</code> or<code>file2</code>      *          is null      */
name|File
name|getBaseDir
parameter_list|(
specifier|final
name|File
name|file1
parameter_list|,
specifier|final
name|File
name|file2
parameter_list|)
block|{
if|if
condition|(
name|file1
operator|==
literal|null
operator|||
name|file2
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Iterator
name|bases
init|=
name|getParents
argument_list|(
name|file1
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|final
name|Iterator
name|fileParents
init|=
name|getParents
argument_list|(
name|file2
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|File
name|result
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|bases
operator|.
name|hasNext
argument_list|()
operator|&&
name|fileParents
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|File
name|next
init|=
operator|(
name|File
operator|)
name|bases
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|equals
argument_list|(
name|fileParents
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|=
name|next
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * @return a list of files, starting with the root and ending with the file itself      */
specifier|private
name|LinkedList
comment|/*<File> */
name|getParents
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|LinkedList
name|r
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
while|while
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|addFirst
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|file
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|private
specifier|static
class|class
name|EmptyFileSet
extends|extends
name|FileSet
block|{
specifier|private
name|DirectoryScanner
name|ds
init|=
operator|new
name|EmptyDirectoryScanner
argument_list|()
decl_stmt|;
specifier|public
name|Iterator
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|EmptyIterator
argument_list|()
return|;
block|}
specifier|public
name|Object
name|clone
parameter_list|()
block|{
return|return
operator|new
name|EmptyFileSet
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|DirectoryScanner
name|getDirectoryScanner
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
return|return
name|ds
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|EmptyIterator
implements|implements
name|Iterator
block|{
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Object
name|next
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"EmptyFileSet Iterator"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EmptyFileSet Iterator"
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
class|class
name|EmptyDirectoryScanner
extends|extends
name|DirectoryScanner
block|{
specifier|public
name|String
index|[]
name|getIncludedFiles
parameter_list|()
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
block|}
block|}
end_class

end_unit

