begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|IvyPatternHelper
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
name|repository
operator|.
name|Resource
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
name|BuildLogger
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
name|DefaultLogger
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
name|ProjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents one entry in the cache of a {@link PackagerResolver}.  */
end_comment

begin_class
specifier|public
class|class
name|PackagerCacheEntry
block|{
specifier|private
specifier|final
name|ModuleRevisionId
name|mr
decl_stmt|;
specifier|private
specifier|final
name|File
name|dir
decl_stmt|;
specifier|private
specifier|final
name|File
name|resourceCache
decl_stmt|;
specifier|private
specifier|final
name|String
name|resourceURL
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|validate
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|preserve
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|restricted
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|verbose
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|quiet
decl_stmt|;
specifier|private
name|boolean
name|built
decl_stmt|;
comment|// CheckStyle:ParameterNumber OFF
specifier|public
name|PackagerCacheEntry
parameter_list|(
name|ModuleRevisionId
name|mr
parameter_list|,
name|File
name|rootDir
parameter_list|,
name|File
name|resourceCache
parameter_list|,
name|String
name|resourceURL
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|boolean
name|preserve
parameter_list|,
name|boolean
name|restricted
parameter_list|,
name|boolean
name|verbose
parameter_list|,
name|boolean
name|quiet
parameter_list|)
block|{
name|this
operator|.
name|mr
operator|=
name|mr
expr_stmt|;
name|this
operator|.
name|dir
operator|=
name|getSubdir
argument_list|(
name|rootDir
argument_list|,
name|this
operator|.
name|mr
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceCache
operator|=
name|resourceCache
expr_stmt|;
name|this
operator|.
name|resourceURL
operator|=
name|resourceURL
expr_stmt|;
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
name|this
operator|.
name|preserve
operator|=
name|preserve
expr_stmt|;
name|this
operator|.
name|restricted
operator|=
name|restricted
expr_stmt|;
name|this
operator|.
name|verbose
operator|=
name|verbose
expr_stmt|;
name|this
operator|.
name|quiet
operator|=
name|quiet
expr_stmt|;
block|}
comment|// CheckStyle:ParameterNumber ON
comment|/**      * Attempt to build this entry.      *      * @param packagerResource      *            packager metadata resource      * @param properties      *            a map of properties to pass to the child Ant build responsible for dependency      *            packaging      *      * @throws IOException      *             if this entry has already been built      */
specifier|public
specifier|synchronized
name|void
name|build
parameter_list|(
name|Resource
name|packagerResource
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Sanity check
if|if
condition|(
name|this
operator|.
name|built
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"build in directory `"
operator|+
name|this
operator|.
name|dir
operator|+
literal|"' already completed"
argument_list|)
throw|;
block|}
comment|// Remove work directory if it exists (e.g. left over from last time)
if|if
condition|(
name|this
operator|.
name|dir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|cleanup
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"can't remove directory `"
operator|+
name|this
operator|.
name|dir
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
comment|// Create work directory
if|if
condition|(
operator|!
name|this
operator|.
name|dir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"can't create directory `"
operator|+
name|this
operator|.
name|dir
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// Write out packager XML
name|InputStream
name|packagerXML
init|=
name|packagerResource
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|saveFile
argument_list|(
literal|"packager.xml"
argument_list|,
name|packagerXML
argument_list|)
expr_stmt|;
comment|// Write packager XSLT
name|saveFile
argument_list|(
literal|"packager.xsl"
argument_list|)
expr_stmt|;
comment|// Write packager XSD
name|saveFile
argument_list|(
literal|"packager-1.0.xsd"
argument_list|)
expr_stmt|;
comment|// Write master Ant build file
name|saveFile
argument_list|(
literal|"build.xml"
argument_list|)
expr_stmt|;
comment|// Execute the Ant build file
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|init
argument_list|()
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ant.file"
argument_list|,
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"build.xml"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectHelper
operator|.
name|configureProject
argument_list|(
name|project
argument_list|,
operator|new
name|File
argument_list|(
name|dir
argument_list|,
literal|"build.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|project
operator|.
name|setBaseDir
argument_list|(
name|dir
argument_list|)
expr_stmt|;
comment|// Configure logging verbosity
name|BuildLogger
name|logger
init|=
operator|new
name|DefaultLogger
argument_list|()
decl_stmt|;
name|logger
operator|.
name|setMessageOutputLevel
argument_list|(
name|this
operator|.
name|verbose
condition|?
name|Project
operator|.
name|MSG_VERBOSE
else|:
name|this
operator|.
name|quiet
condition|?
name|Project
operator|.
name|MSG_WARN
else|:
name|Project
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setOutputPrintStream
argument_list|(
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setErrorPrintStream
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|project
operator|.
name|addBuildListener
argument_list|(
name|logger
argument_list|)
expr_stmt|;
comment|// Set properties
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.organisation"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|mr
operator|.
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.module"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|mr
operator|.
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.revision"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|mr
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.branch"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|mr
operator|.
name|getBranch
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|resourceCache
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.resourceCache"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|resourceCache
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|resourceURL
operator|!=
literal|null
condition|)
block|{
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.resourceURL"
argument_list|,
literal|""
operator|+
name|getResourceURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|validate
condition|)
block|{
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.validate"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.restricted"
argument_list|,
literal|""
operator|+
name|this
operator|.
name|restricted
argument_list|)
expr_stmt|;
name|project
operator|.
name|setUserProperty
argument_list|(
literal|"ivy.packager.quiet"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|quiet
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|project
operator|.
name|setUserProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Execute task
name|Message
operator|.
name|verbose
argument_list|(
literal|"performing packager resolver build in "
operator|+
name|this
operator|.
name|dir
argument_list|)
expr_stmt|;
try|try
block|{
name|project
operator|.
name|executeTarget
argument_list|(
literal|"build"
argument_list|)
expr_stmt|;
name|this
operator|.
name|built
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"packager resolver build failed: "
operator|+
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|/**      * Has this entry been successfully built?      *      * @return boolean      */
specifier|public
specifier|synchronized
name|boolean
name|isBuilt
parameter_list|()
block|{
return|return
name|this
operator|.
name|built
return|;
block|}
comment|/**      * Get a built artifact.      *      * @param artifact ditto      * @return ResolvedResource      * @throws IllegalStateException      *             if this entry's built has not (yet) completed successfully      */
specifier|public
name|ResolvedResource
name|getBuiltArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|built
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"build in directory `"
operator|+
name|this
operator|.
name|dir
operator|+
literal|"' has not yet successfully completed"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ResolvedResource
argument_list|(
operator|new
name|BuiltFileResource
argument_list|(
name|this
operator|.
name|dir
argument_list|,
name|artifact
argument_list|)
argument_list|,
name|this
operator|.
name|mr
operator|.
name|getRevision
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|cleanup
parameter_list|()
block|{
name|this
operator|.
name|built
operator|=
literal|false
expr_stmt|;
return|return
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|this
operator|.
name|dir
argument_list|)
return|;
block|}
specifier|protected
name|void
name|saveFile
parameter_list|(
name|String
name|name
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|input
argument_list|,
operator|new
name|File
argument_list|(
name|this
operator|.
name|dir
argument_list|,
name|name
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveFile
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|input
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"can't find resource `"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|saveFile
argument_list|(
name|name
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
comment|// @Override
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
try|try
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|preserve
condition|)
block|{
name|cleanup
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getResourceURL
parameter_list|()
block|{
name|String
name|baseURL
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|resourceURL
argument_list|,
name|mr
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mr
operator|.
name|getName
argument_list|()
argument_list|,
name|mr
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|mr
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|int
name|slash
init|=
name|baseURL
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|slash
operator|!=
operator|-
literal|1
condition|)
block|{
name|baseURL
operator|=
name|baseURL
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|slash
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|baseURL
return|;
block|}
specifier|private
specifier|static
name|File
name|getSubdir
parameter_list|(
name|File
name|rootDir
parameter_list|,
name|ModuleRevisionId
name|mr
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|rootDir
argument_list|,
name|mr
operator|.
name|getOrganisation
argument_list|()
operator|+
name|File
operator|.
name|separatorChar
operator|+
name|mr
operator|.
name|getName
argument_list|()
operator|+
name|File
operator|.
name|separatorChar
operator|+
name|mr
operator|.
name|getRevision
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

