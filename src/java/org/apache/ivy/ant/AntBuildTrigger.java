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
name|IvyContext
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
name|event
operator|.
name|IvyEvent
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
name|trigger
operator|.
name|AbstractTrigger
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
name|trigger
operator|.
name|Trigger
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
name|taskdefs
operator|.
name|Ant
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
name|taskdefs
operator|.
name|Property
import|;
end_import

begin_comment
comment|/**  * Triggers an ant build on an event occurrence.  *<p>  * Example of use:  *</p>  *<pre>  *&lt;ant-build-trigger event=&quot;pre-resolve-dependency&quot;  *                    filter=&quot;revision=latest.integration&quot;  *                    antfile=&quot;/path/to/[module]/build.xml&quot;  *                    target=&quot;compile&quot;/&gt;  *</pre>  *<p>  * Triggers an ant build for any dependency in asked in latest.integration, just before resolving  * the dependency.  *</p>  *<p>  * The onlyonce property is used to tell if the ant build should be triggered only once, or several  * times in the same build.  *</p>  *  * @see AntCallTrigger  * @since 1.4  */
end_comment

begin_class
specifier|public
class|class
name|AntBuildTrigger
extends|extends
name|AbstractTrigger
implements|implements
name|Trigger
block|{
specifier|private
name|boolean
name|onlyOnce
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|target
init|=
literal|null
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|builds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|buildFilePattern
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
name|File
name|f
init|=
name|getBuildFile
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|onlyOnce
operator|&&
name|isBuilt
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"target build file already built, skipping: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Ant
name|ant
init|=
operator|new
name|Ant
argument_list|()
decl_stmt|;
name|Project
name|project
init|=
operator|(
name|Project
operator|)
name|IvyContext
operator|.
name|peekInContextStack
argument_list|(
name|IvyTask
operator|.
name|ANT_PROJECT_CONTEXT_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|project
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
name|ant
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|ant
operator|.
name|setTaskName
argument_list|(
literal|"ant"
argument_list|)
expr_stmt|;
name|ant
operator|.
name|setAntfile
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ant
operator|.
name|setInheritAll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|target
init|=
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|ant
operator|.
name|setTarget
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
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
name|event
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Property
name|p
init|=
name|ant
operator|.
name|createProperty
argument_list|()
decl_stmt|;
name|p
operator|.
name|setName
argument_list|(
name|prefix
operator|==
literal|null
condition|?
name|entry
operator|.
name|getKey
argument_list|()
else|:
name|prefix
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"triggering build: "
operator|+
name|f
operator|+
literal|" target="
operator|+
name|target
operator|+
literal|" for "
operator|+
name|event
argument_list|)
expr_stmt|;
try|try
block|{
name|ant
operator|.
name|execute
argument_list|()
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
literal|"Exception occurred while executing target "
operator|+
name|target
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
name|markBuilt
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"triggered build finished: "
operator|+
name|f
operator|+
literal|" target="
operator|+
name|target
operator|+
literal|" for "
operator|+
name|event
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no build file found for dependency, skipping: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|markBuilt
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|builds
operator|.
name|add
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isBuilt
parameter_list|(
name|File
name|f
parameter_list|)
block|{
return|return
name|builds
operator|.
name|contains
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|File
name|getBuildFile
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
return|return
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
name|getBuildFilePattern
argument_list|()
argument_list|,
name|event
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getBuildFilePattern
parameter_list|()
block|{
return|return
name|buildFilePattern
return|;
block|}
specifier|public
name|void
name|setAntfile
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|buildFilePattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|String
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOnlyonce
parameter_list|()
block|{
return|return
name|onlyOnce
return|;
block|}
specifier|public
name|void
name|setOnlyonce
parameter_list|(
name|boolean
name|onlyonce
parameter_list|)
block|{
name|onlyOnce
operator|=
name|onlyonce
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|this
operator|.
name|prefix
operator|+=
literal|"."
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

