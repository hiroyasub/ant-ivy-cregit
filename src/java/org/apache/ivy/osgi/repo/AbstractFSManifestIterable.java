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
name|osgi
operator|.
name|repo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFSManifestIterable
implements|implements
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
specifier|public
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|FSManifestIterator
argument_list|()
return|;
block|}
specifier|abstract
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|listBundleFiles
parameter_list|(
name|String
name|dir
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|abstract
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|listDirs
parameter_list|(
name|String
name|dir
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|abstract
specifier|protected
name|InputStream
name|getInputStream
parameter_list|(
name|String
name|f
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|protected
name|String
name|createBundleLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
return|return
name|location
return|;
block|}
class|class
name|FSManifestIterator
implements|implements
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
specifier|private
name|ManifestAndLocation
name|next
init|=
literal|null
decl_stmt|;
comment|/**          * Stack of list of directories. An iterator in the stack represents the current directory being lookup. The          * first element in the stack is the root directory. The next element in the stack is an iterator on the          * children on the root. The last iterator in the stack points to {@link #currentDir}.          */
specifier|private
name|Stack
argument_list|<
name|Iterator
argument_list|<
name|String
argument_list|>
argument_list|>
name|dirs
init|=
operator|new
name|Stack
argument_list|<
name|Iterator
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**          * The bundles files being lookup.          */
specifier|private
name|Iterator
argument_list|<
name|String
argument_list|>
name|bundleCandidates
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|currentDir
init|=
literal|null
decl_stmt|;
name|FSManifestIterator
parameter_list|()
block|{
name|dirs
operator|.
name|add
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|""
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**          * Deep first tree lookup for the directories and the bundles are searched on each found directory.          */
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
while|while
condition|(
name|next
operator|==
literal|null
condition|)
block|{
comment|// no current directory
if|if
condition|(
name|currentDir
operator|==
literal|null
condition|)
block|{
comment|// so get the next one
if|if
condition|(
name|dirs
operator|.
name|peek
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|currentDir
operator|=
name|dirs
operator|.
name|peek
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
try|try
block|{
name|bundleCandidates
operator|=
name|listBundleFiles
argument_list|(
name|currentDir
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Unlistable dir: "
operator|+
name|currentDir
operator|+
literal|" ("
operator|+
name|e
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|currentDir
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|dirs
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
comment|// no next directory, but we are at the root: finished
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// remove the top of the stack and continue with a sibling.
name|dirs
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|bundleCandidates
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|bundleCandidate
init|=
name|bundleCandidates
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|JarInputStream
name|in
init|=
operator|new
name|JarInputStream
argument_list|(
name|getInputStream
argument_list|(
name|bundleCandidate
argument_list|)
argument_list|)
decl_stmt|;
name|Manifest
name|manifest
init|=
name|in
operator|.
name|getManifest
argument_list|()
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
operator|new
name|ManifestAndLocation
argument_list|(
name|manifest
argument_list|,
name|createBundleLocation
argument_list|(
name|bundleCandidate
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"No manifest in jar: "
operator|+
name|bundleCandidate
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Jar file just removed: "
operator|+
name|bundleCandidate
operator|+
literal|" ("
operator|+
name|e
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Unreadable jar: "
operator|+
name|bundleCandidate
operator|+
literal|" ("
operator|+
name|e
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// no more candidate on the current directory
comment|// so lookup in the children directories
try|try
block|{
name|dirs
operator|.
name|add
argument_list|(
name|listDirs
argument_list|(
name|currentDir
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Unlistable dir: "
operator|+
name|currentDir
operator|+
literal|" ("
operator|+
name|e
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|dirs
operator|.
name|add
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|currentDir
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|ManifestAndLocation
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|ManifestAndLocation
name|manifest
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|manifest
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

