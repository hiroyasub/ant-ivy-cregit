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
name|plugins
operator|.
name|resolver
operator|.
name|packager
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
name|io
operator|.
name|IOException
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
name|Date
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
name|Map
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DefaultArtifact
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
name|resolver
operator|.
name|URLResolver
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
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
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
name|FileUtil
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
comment|/**  * Resolver that performs a "build" operation to resolve artifacts.  *  *<p>  * The resolver is configured with a base URL, from which the "ivy.xml"  * and "packager.xml" files are resolved. The latter file contains  * instructions describing how to build the actual artifacts.  */
end_comment

begin_class
specifier|public
class|class
name|PackagerResolver
extends|extends
name|URLResolver
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGER_ARTIFACT_NAME
init|=
literal|"packager"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGER_ARTIFACT_TYPE
init|=
literal|"packager"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGER_ARTIFACT_EXT
init|=
literal|"xml"
decl_stmt|;
specifier|private
specifier|final
name|HashMap
comment|/*<ModuleRevisionId, PackagerCacheEntry>*/
name|packagerCache
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|File
name|buildRoot
decl_stmt|;
specifier|private
name|File
name|resourceCache
decl_stmt|;
specifier|private
name|String
name|resourceURL
decl_stmt|;
specifier|private
name|Map
comment|/*<String,String>*/
name|properties
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|preserve
decl_stmt|;
specifier|private
name|boolean
name|restricted
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|verbose
decl_stmt|;
specifier|private
name|boolean
name|quiet
decl_stmt|;
specifier|public
name|PackagerResolver
parameter_list|()
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|clearCache
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|synchronized
name|void
name|clearCache
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|preserve
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Iterator
name|i
init|=
name|packagerCache
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|PackagerCacheEntry
name|entry
init|=
operator|(
name|PackagerCacheEntry
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|entry
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
name|packagerCache
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|buildRoot
operator|!=
literal|null
condition|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|this
operator|.
name|buildRoot
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set root directory under which builds take place.      */
specifier|public
name|void
name|setBuildRoot
parameter_list|(
name|File
name|buildRoot
parameter_list|)
block|{
name|this
operator|.
name|buildRoot
operator|=
name|buildRoot
expr_stmt|;
block|}
comment|/**      * Returns root directory under which builds take place.       */
specifier|public
name|File
name|getBuildRoot
parameter_list|()
block|{
return|return
name|buildRoot
return|;
block|}
comment|/**      * Set resource cache directory.      */
specifier|public
name|void
name|setResourceCache
parameter_list|(
name|File
name|resourceCache
parameter_list|)
block|{
name|this
operator|.
name|resourceCache
operator|=
name|resourceCache
expr_stmt|;
block|}
comment|/**      * Get resource cache directory.      */
specifier|public
name|File
name|getResourceCache
parameter_list|()
block|{
return|return
name|resourceCache
return|;
block|}
comment|/**      * Set base resource override URL pattern.      */
specifier|public
name|void
name|setResourceURL
parameter_list|(
name|String
name|resourceURL
parameter_list|)
block|{
name|this
operator|.
name|resourceURL
operator|=
name|resourceURL
expr_stmt|;
block|}
comment|/**      * Set pattern for locating "packager.xml" files.      */
specifier|public
name|void
name|setPackagerPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|ArrayList
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|setArtifactPatterns
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set whether to preserve build directories. Default is false.      */
specifier|public
name|void
name|setPreserveBuildDirectories
parameter_list|(
name|boolean
name|preserve
parameter_list|)
block|{
name|this
operator|.
name|preserve
operator|=
name|preserve
expr_stmt|;
block|}
comment|/**      * Set whether to enable restricted mode. Default is true.      */
specifier|public
name|void
name|setRestricted
parameter_list|(
name|boolean
name|restricted
parameter_list|)
block|{
name|this
operator|.
name|restricted
operator|=
name|restricted
expr_stmt|;
block|}
comment|/**      * Set whether to run ant with the -verbose flag. Default is false.      */
specifier|public
name|void
name|setVerbose
parameter_list|(
name|boolean
name|verbose
parameter_list|)
block|{
name|this
operator|.
name|verbose
operator|=
name|verbose
expr_stmt|;
block|}
comment|/**      * Set whether to run ant with the -quiet flag. Default is false.      */
specifier|public
name|void
name|setQuiet
parameter_list|(
name|boolean
name|quiet
parameter_list|)
block|{
name|this
operator|.
name|quiet
operator|=
name|quiet
expr_stmt|;
block|}
comment|/**      * Set whether to validate downloaded packager.xml files. Default is true.      */
specifier|public
name|void
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
block|}
specifier|public
name|void
name|setAllownomd
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"allownomd not supported by resolver "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDescriptor
parameter_list|(
name|String
name|rule
parameter_list|)
block|{
if|if
condition|(
name|DESCRIPTOR_OPTIONAL
operator|.
name|equals
argument_list|(
name|rule
argument_list|)
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"descriptor=\""
operator|+
name|DESCRIPTOR_OPTIONAL
operator|+
literal|"\" not supported by resolver "
operator|+
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|setDescriptor
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets a property to be passed to the child Ant build responsible for packaging the dependency.      *       * @param propertyKey the property to pass      * @param propertyValue the value of the property to pass      */
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|propertyKey
parameter_list|,
name|String
name|propertyValue
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|propertyKey
argument_list|,
name|propertyValue
argument_list|)
expr_stmt|;
block|}
comment|// @Override
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|super
operator|.
name|validate
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|buildRoot
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no buildRoot specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|getArtifactPatterns
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no packager pattern specified"
argument_list|)
throw|;
block|}
block|}
comment|// @Override
specifier|public
specifier|synchronized
name|ResolvedResource
name|findArtifactRef
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
comment|// For our special packager.xml file, defer to superclass
if|if
condition|(
name|PACKAGER_ARTIFACT_NAME
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|PACKAGER_ARTIFACT_TYPE
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|PACKAGER_ARTIFACT_EXT
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|findArtifactRef
argument_list|(
name|artifact
argument_list|,
name|date
argument_list|)
return|;
block|}
comment|// Check the cache
name|ModuleRevisionId
name|mr
init|=
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|PackagerCacheEntry
name|entry
init|=
operator|(
name|PackagerCacheEntry
operator|)
name|packagerCache
operator|.
name|get
argument_list|(
name|mr
argument_list|)
decl_stmt|;
comment|// Ignore invalid entries
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
operator|!
name|entry
operator|.
name|isBuilt
argument_list|()
condition|)
block|{
name|packagerCache
operator|.
name|remove
argument_list|(
name|mr
argument_list|)
expr_stmt|;
name|entry
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|entry
operator|=
literal|null
expr_stmt|;
block|}
comment|// Build the artifacts (if not done already)
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
name|ResolvedResource
name|packager
init|=
name|findArtifactRef
argument_list|(
operator|new
name|DefaultArtifact
argument_list|(
name|mr
argument_list|,
literal|null
argument_list|,
name|PACKAGER_ARTIFACT_NAME
argument_list|,
name|PACKAGER_ARTIFACT_TYPE
argument_list|,
name|PACKAGER_ARTIFACT_EXT
argument_list|)
argument_list|,
name|date
argument_list|)
decl_stmt|;
if|if
condition|(
name|packager
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|entry
operator|=
operator|new
name|PackagerCacheEntry
argument_list|(
name|mr
argument_list|,
name|this
operator|.
name|buildRoot
argument_list|,
name|this
operator|.
name|resourceCache
argument_list|,
name|this
operator|.
name|resourceURL
argument_list|,
name|this
operator|.
name|validate
argument_list|,
name|this
operator|.
name|preserve
argument_list|,
name|this
operator|.
name|restricted
argument_list|,
name|this
operator|.
name|verbose
argument_list|,
name|this
operator|.
name|quiet
argument_list|)
expr_stmt|;
try|try
block|{
name|entry
operator|.
name|build
argument_list|(
name|packager
operator|.
name|getResource
argument_list|()
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"can't build artifact "
operator|+
name|artifact
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|packagerCache
operator|.
name|put
argument_list|(
name|mr
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
comment|// Return reference to desired artifact
return|return
name|entry
operator|.
name|getBuiltArtifact
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"packager"
return|;
block|}
block|}
end_class

end_unit

