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
name|retrieve
package|;
end_package

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
name|module
operator|.
name|descriptor
operator|.
name|Artifact
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
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|FilterHelper
import|;
end_import

begin_comment
comment|/**  * A set of options used during retrieve related tasks  *  * @see RetrieveEngine  */
end_comment

begin_class
specifier|public
class|class
name|RetrieveOptions
extends|extends
name|LogOptions
block|{
specifier|public
specifier|static
specifier|final
name|String
name|OVERWRITEMODE_NEVER
init|=
literal|"never"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OVERWRITEMODE_ALWAYS
init|=
literal|"always"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OVERWRITEMODE_NEWER
init|=
literal|"newer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OVERWRITEMODE_DIFFERENT
init|=
literal|"different"
decl_stmt|;
comment|/**      * The names of configurations to retrieve. If the array consists only of '*', then all      * configurations of the module will be retrieved.      */
specifier|private
name|String
index|[]
name|confs
init|=
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
decl_stmt|;
comment|/**      * The pattern to which ivy files should be retrieved. If destIvyPattern is null no ivy files      * will be copied.      */
specifier|private
name|String
name|destIvyPattern
init|=
literal|null
decl_stmt|;
comment|/**      * The pattern to which artifacts should be retrieved.      */
specifier|private
name|String
name|destArtifactPattern
init|=
literal|null
decl_stmt|;
comment|/**      * The filter to apply before retrieving artifacts.      */
specifier|private
name|Filter
argument_list|<
name|Artifact
argument_list|>
name|artifactFilter
init|=
name|FilterHelper
operator|.
name|NO_FILTER
decl_stmt|;
comment|/**      * True if a synchronisation of the destination directory should be done, false if a simple copy      * is enough. Synchronisation means that after the retrieve only files which have been retrieved      * will be present in the destination directory, which means that some files may be deleted.      */
specifier|private
name|boolean
name|sync
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|overwriteMode
init|=
name|OVERWRITEMODE_NEWER
decl_stmt|;
comment|/**      * True if the original files should be used instead of their cache copy.      */
specifier|private
name|boolean
name|useOrigin
init|=
literal|false
decl_stmt|;
comment|/**      * True if symbolic links should be created instead of plain copy. Works only on OS supporting      * symbolic links.      */
specifier|private
name|boolean
name|makeSymlinks
init|=
literal|false
decl_stmt|;
annotation|@
name|Deprecated
specifier|private
name|boolean
name|makeSymlinksInMass
init|=
literal|false
decl_stmt|;
comment|/**      * The id used to store the resolve information.      */
specifier|private
name|String
name|resolveId
decl_stmt|;
specifier|private
name|FileNameMapper
name|mapper
decl_stmt|;
specifier|public
name|RetrieveOptions
parameter_list|()
block|{
block|}
specifier|public
name|RetrieveOptions
parameter_list|(
name|RetrieveOptions
name|options
parameter_list|)
block|{
name|super
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|this
operator|.
name|confs
operator|=
name|options
operator|.
name|confs
expr_stmt|;
name|this
operator|.
name|destIvyPattern
operator|=
name|options
operator|.
name|destIvyPattern
expr_stmt|;
name|this
operator|.
name|destArtifactPattern
operator|=
name|options
operator|.
name|destArtifactPattern
expr_stmt|;
name|this
operator|.
name|artifactFilter
operator|=
name|options
operator|.
name|artifactFilter
expr_stmt|;
name|this
operator|.
name|sync
operator|=
name|options
operator|.
name|sync
expr_stmt|;
name|this
operator|.
name|overwriteMode
operator|=
name|options
operator|.
name|overwriteMode
expr_stmt|;
name|this
operator|.
name|useOrigin
operator|=
name|options
operator|.
name|useOrigin
expr_stmt|;
name|this
operator|.
name|makeSymlinks
operator|=
name|options
operator|.
name|makeSymlinks
expr_stmt|;
name|this
operator|.
name|makeSymlinksInMass
operator|=
name|options
operator|.
name|makeSymlinksInMass
expr_stmt|;
name|this
operator|.
name|resolveId
operator|=
name|options
operator|.
name|resolveId
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
name|options
operator|.
name|mapper
expr_stmt|;
block|}
specifier|public
name|String
name|getDestArtifactPattern
parameter_list|()
block|{
return|return
name|destArtifactPattern
return|;
block|}
specifier|public
name|RetrieveOptions
name|setDestArtifactPattern
parameter_list|(
name|String
name|destArtifactPattern
parameter_list|)
block|{
name|this
operator|.
name|destArtifactPattern
operator|=
name|destArtifactPattern
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Filter
argument_list|<
name|Artifact
argument_list|>
name|getArtifactFilter
parameter_list|()
block|{
return|return
name|artifactFilter
return|;
block|}
specifier|public
name|RetrieveOptions
name|setArtifactFilter
parameter_list|(
name|Filter
argument_list|<
name|Artifact
argument_list|>
name|artifactFilter
parameter_list|)
block|{
name|this
operator|.
name|artifactFilter
operator|=
name|artifactFilter
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
return|return
name|confs
return|;
block|}
specifier|public
name|RetrieveOptions
name|setConfs
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getOverwriteMode
parameter_list|()
block|{
return|return
name|overwriteMode
operator|==
literal|null
condition|?
name|OVERWRITEMODE_NEWER
else|:
name|overwriteMode
return|;
block|}
specifier|public
name|RetrieveOptions
name|setOverwriteMode
parameter_list|(
name|String
name|overwriteMode
parameter_list|)
block|{
name|this
operator|.
name|overwriteMode
operator|=
name|overwriteMode
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getDestIvyPattern
parameter_list|()
block|{
return|return
name|destIvyPattern
return|;
block|}
specifier|public
name|RetrieveOptions
name|setDestIvyPattern
parameter_list|(
name|String
name|destIvyPattern
parameter_list|)
block|{
name|this
operator|.
name|destIvyPattern
operator|=
name|destIvyPattern
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isMakeSymlinks
parameter_list|()
block|{
comment|// we also do a check on makeSymlinkInMass just to allow backward compatibility for a version
comment|// or so, to allow users time to move away from symlinkinmass option
return|return
name|makeSymlinks
operator|||
name|makeSymlinksInMass
return|;
block|}
annotation|@
name|Deprecated
comment|/**      * @deprecated Starting 2.5, creating symlinks in mass is no longer supported and this      * method will always return false      */
specifier|public
name|boolean
name|isMakeSymlinksInMass
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|RetrieveOptions
name|setMakeSymlinks
parameter_list|(
name|boolean
name|makeSymlinks
parameter_list|)
block|{
name|this
operator|.
name|makeSymlinks
operator|=
name|makeSymlinks
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Deprecated
comment|/**      * @deprecated Starting 2.5, creating symlinks in mass is no longer supported and this      * method plays no role in creation of symlinks. Use {@link #setMakeSymlinks(boolean)} instead      */
specifier|public
name|RetrieveOptions
name|setMakeSymlinksInMass
parameter_list|(
name|boolean
name|makeSymlinksInMass
parameter_list|)
block|{
name|this
operator|.
name|makeSymlinksInMass
operator|=
name|makeSymlinksInMass
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"symlinkmass option has been deprecated and will no longer be supported"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
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
name|RetrieveOptions
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
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUseOrigin
parameter_list|()
block|{
return|return
name|useOrigin
return|;
block|}
specifier|public
name|RetrieveOptions
name|setUseOrigin
parameter_list|(
name|boolean
name|useOrigin
parameter_list|)
block|{
name|this
operator|.
name|useOrigin
operator|=
name|useOrigin
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getResolveId
parameter_list|()
block|{
return|return
name|resolveId
return|;
block|}
specifier|public
name|RetrieveOptions
name|setResolveId
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
name|this
operator|.
name|resolveId
operator|=
name|resolveId
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|FileNameMapper
name|getMapper
parameter_list|()
block|{
return|return
name|mapper
return|;
block|}
specifier|public
name|RetrieveOptions
name|setMapper
parameter_list|(
name|FileNameMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

