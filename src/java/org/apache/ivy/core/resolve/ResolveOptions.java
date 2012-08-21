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
name|resolve
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|util
operator|.
name|ConfigurationUtils
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
comment|/**  * A set of options used during resolve related tasks  *   * @see ResolveEngine  */
end_comment

begin_class
specifier|public
class|class
name|ResolveOptions
extends|extends
name|LogOptions
block|{
comment|/**      * Default resolve mode, using default revision constraints in dependency descriptors.      */
specifier|public
specifier|static
specifier|final
name|String
name|RESOLVEMODE_DEFAULT
init|=
literal|"default"
decl_stmt|;
comment|/**      * Dynamic resolve mode, using dynamic revision constraints in dependency descriptors.      */
specifier|public
specifier|static
specifier|final
name|String
name|RESOLVEMODE_DYNAMIC
init|=
literal|"dynamic"
decl_stmt|;
comment|/**      * Array of all available resolve modes.      */
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|RESOLVEMODES
init|=
operator|new
name|String
index|[]
block|{
name|RESOLVEMODE_DEFAULT
block|,
name|RESOLVEMODE_DYNAMIC
block|}
decl_stmt|;
comment|/**      * an array of configuration names to resolve - must not be null nor empty      */
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
comment|/**      * the revision of the module for which dependencies should be resolved. This revision is      * considered as the resolved revision of the module, unless it is null. If it is null, then a      * default revision is given if necessary (no revision found in ivy file)      */
specifier|private
name|String
name|revision
init|=
literal|null
decl_stmt|;
comment|/**      * the date for which the dependencies should be resolved. All obtained artifacts should have a      * publication date which is before or equal to the given date. The date can be null, in which      * case all artifacts will be considered      */
specifier|private
name|Date
name|date
init|=
literal|null
decl_stmt|;
comment|/**      * True if validation of module descriptors should done, false otherwise      */
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
comment|/**      * True if only the cache should be used for resolve, false if a real resolve with dependency      * resolvers should be done      */
specifier|private
name|boolean
name|useCacheOnly
init|=
literal|false
decl_stmt|;
comment|/**      * True if the dependencies should be resolved transitively, false if only direct dependencies      * should be resolved      */
specifier|private
name|boolean
name|transitive
init|=
literal|true
decl_stmt|;
comment|/**      * True if the resolve should also download artifacts, false if only dependency resolution with      * module descriptors should be done      */
specifier|private
name|boolean
name|download
init|=
literal|true
decl_stmt|;
comment|/**      * True if a report of the resolve process should be output at the end of the process, false      * otherwise      */
specifier|private
name|boolean
name|outputReport
init|=
literal|true
decl_stmt|;
comment|/**      * A filter to use to avoid downloading all artifacts.      */
specifier|private
name|Filter
name|artifactFilter
init|=
name|FilterHelper
operator|.
name|NO_FILTER
decl_stmt|;
comment|/**      * The resolve mode to use. Should be one of {@link #RESOLVEMODES}, or<code>null</code> to      * use settings configured resolve mode.      */
specifier|private
name|String
name|resolveMode
decl_stmt|;
comment|/**      * The id used to store the resolve information.      */
specifier|private
name|String
name|resolveId
decl_stmt|;
specifier|private
name|boolean
name|refresh
decl_stmt|;
comment|/**      *  True if the resolve should compare the new resolution against the previous report       **/
specifier|private
name|boolean
name|checkIfChanged
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|uncompress
decl_stmt|;
specifier|public
name|ResolveOptions
parameter_list|()
block|{
block|}
specifier|public
name|ResolveOptions
parameter_list|(
name|ResolveOptions
name|options
parameter_list|)
block|{
name|super
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|confs
operator|=
name|options
operator|.
name|confs
expr_stmt|;
name|revision
operator|=
name|options
operator|.
name|revision
expr_stmt|;
name|date
operator|=
name|options
operator|.
name|date
expr_stmt|;
name|validate
operator|=
name|options
operator|.
name|validate
expr_stmt|;
name|refresh
operator|=
name|options
operator|.
name|refresh
expr_stmt|;
name|useCacheOnly
operator|=
name|options
operator|.
name|useCacheOnly
expr_stmt|;
name|transitive
operator|=
name|options
operator|.
name|transitive
expr_stmt|;
name|download
operator|=
name|options
operator|.
name|download
expr_stmt|;
name|outputReport
operator|=
name|options
operator|.
name|outputReport
expr_stmt|;
name|resolveMode
operator|=
name|options
operator|.
name|resolveMode
expr_stmt|;
name|artifactFilter
operator|=
name|options
operator|.
name|artifactFilter
expr_stmt|;
name|resolveId
operator|=
name|options
operator|.
name|resolveId
expr_stmt|;
name|checkIfChanged
operator|=
name|options
operator|.
name|checkIfChanged
expr_stmt|;
block|}
specifier|public
name|Filter
name|getArtifactFilter
parameter_list|()
block|{
return|return
name|artifactFilter
return|;
block|}
specifier|public
name|ResolveOptions
name|setArtifactFilter
parameter_list|(
name|Filter
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
comment|/**      * Returns the resolve mode to use, or<code>null</code> to use settings configured resolve      * mode.      *       * @return the resolve mode to use.      */
specifier|public
name|String
name|getResolveMode
parameter_list|()
block|{
return|return
name|resolveMode
return|;
block|}
specifier|public
name|ResolveOptions
name|setResolveMode
parameter_list|(
name|String
name|resolveMode
parameter_list|)
block|{
name|this
operator|.
name|resolveMode
operator|=
name|resolveMode
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Indicates if the configurations use a special configuration       * * , *(private) or *(public).      * When special configurations are used, you must have the module      * descriptor in order to get the list of configurations.      * @see #getConfs()      * @see #getConfs(ModuleDescriptor)      */
specifier|public
name|boolean
name|useSpecialConfs
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|confs
operator|!=
literal|null
operator|&&
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|confs
index|[
literal|0
index|]
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @pre can only be called if useSpecialConfs()==false.  When it is true,       * you have to provide a module desciptor so that configurations can be resolved.      * @see #getConfs(ModuleDescriptor)      */
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
if|if
condition|(
name|useSpecialConfs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"ResolveOptions.getConfs() "
operator|+
literal|"can not be used for options used special confs."
argument_list|)
throw|;
block|}
return|return
name|confs
return|;
block|}
comment|/**       * Get the aksed confs.  Special confs (like *) use the moduleDescriptor to find the values *       * @param md Used to get the exact values for special confs.       * */
specifier|public
name|String
index|[]
name|getConfs
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
comment|//TODO add isInline, in that case, replace * by *(public).
return|return
name|ConfigurationUtils
operator|.
name|replaceWildcards
argument_list|(
name|confs
argument_list|,
name|md
argument_list|)
return|;
block|}
specifier|public
name|ResolveOptions
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
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
specifier|public
name|ResolveOptions
name|setDate
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isDownload
parameter_list|()
block|{
return|return
name|download
return|;
block|}
specifier|public
name|ResolveOptions
name|setDownload
parameter_list|(
name|boolean
name|download
parameter_list|)
block|{
name|this
operator|.
name|download
operator|=
name|download
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isOutputReport
parameter_list|()
block|{
return|return
name|outputReport
return|;
block|}
specifier|public
name|ResolveOptions
name|setOutputReport
parameter_list|(
name|boolean
name|outputReport
parameter_list|)
block|{
name|this
operator|.
name|outputReport
operator|=
name|outputReport
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|transitive
return|;
block|}
specifier|public
name|ResolveOptions
name|setTransitive
parameter_list|(
name|boolean
name|transitive
parameter_list|)
block|{
name|this
operator|.
name|transitive
operator|=
name|transitive
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUseCacheOnly
parameter_list|()
block|{
return|return
name|useCacheOnly
return|;
block|}
specifier|public
name|ResolveOptions
name|setUseCacheOnly
parameter_list|(
name|boolean
name|useCacheOnly
parameter_list|)
block|{
name|this
operator|.
name|useCacheOnly
operator|=
name|useCacheOnly
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
specifier|public
name|ResolveOptions
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
specifier|public
name|ResolveOptions
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
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
name|ResolveOptions
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
name|ResolveOptions
name|setRefresh
parameter_list|(
name|boolean
name|refresh
parameter_list|)
block|{
name|this
operator|.
name|refresh
operator|=
name|refresh
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isRefresh
parameter_list|()
block|{
return|return
name|refresh
return|;
block|}
specifier|public
name|ResolveOptions
name|setCheckIfChanged
parameter_list|(
name|boolean
name|checkIfChanged
parameter_list|)
block|{
name|this
operator|.
name|checkIfChanged
operator|=
name|checkIfChanged
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|getCheckIfChanged
parameter_list|()
block|{
return|return
name|checkIfChanged
return|;
block|}
specifier|public
specifier|static
name|String
name|getDefaultResolveId
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|ModuleId
name|module
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
decl_stmt|;
return|return
name|getDefaultResolveId
argument_list|(
name|module
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getDefaultResolveId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
return|return
name|moduleId
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"-"
operator|+
name|moduleId
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|ResolveOptions
name|setUncompress
parameter_list|(
name|boolean
name|uncompress
parameter_list|)
block|{
name|this
operator|.
name|uncompress
operator|=
name|uncompress
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUncompress
parameter_list|()
block|{
return|return
name|uncompress
return|;
block|}
block|}
end_class

end_unit

