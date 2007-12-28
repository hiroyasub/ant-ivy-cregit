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
name|core
operator|.
name|report
operator|.
name|ResolveReport
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
name|plugins
operator|.
name|matcher
operator|.
name|PatternMatcher
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

begin_comment
comment|/**  * Allow to install a module or a set of module from repository to another one.  */
end_comment

begin_class
specifier|public
class|class
name|IvyInstall
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|organisation
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|private
name|String
name|revision
decl_stmt|;
specifier|private
name|boolean
name|overwrite
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|from
decl_stmt|;
specifier|private
name|String
name|to
decl_stmt|;
specifier|private
name|boolean
name|transitive
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|matcher
init|=
name|PatternMatcher
operator|.
name|EXACT
decl_stmt|;
specifier|private
name|boolean
name|haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
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
if|if
condition|(
name|organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'organisation' "
operator|+
literal|"or via 'ivy.organisation' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
operator|&&
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'module' "
operator|+
literal|"or via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|else if
condition|(
name|module
operator|==
literal|null
operator|&&
operator|!
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
name|module
operator|=
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
expr_stmt|;
block|}
if|if
condition|(
name|revision
operator|==
literal|null
operator|&&
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module revision provided for ivy publish task: "
operator|+
literal|"It can either be set explicitely via the attribute 'revision' "
operator|+
literal|"or via 'ivy.revision' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|else if
condition|(
name|revision
operator|==
literal|null
operator|&&
operator|!
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
name|revision
operator|=
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
expr_stmt|;
block|}
if|if
condition|(
name|from
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no from resolver name: please provide it through parameter 'from'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|to
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no to resolver name: please provide it through parameter 'to'"
argument_list|)
throw|;
block|}
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|)
decl_stmt|;
name|ResolveReport
name|report
decl_stmt|;
try|try
block|{
name|report
operator|=
name|ivy
operator|.
name|install
argument_list|(
name|mrid
argument_list|,
name|from
argument_list|,
name|to
argument_list|,
name|transitive
argument_list|,
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|,
name|overwrite
argument_list|,
name|FilterHelper
operator|.
name|getArtifactTypeFilter
argument_list|(
name|type
argument_list|)
argument_list|,
name|matcher
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to install "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|report
operator|.
name|hasError
argument_list|()
operator|&&
name|isHaltonfailure
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Problem happened while installing modules - see output for details"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|boolean
name|isHaltonfailure
parameter_list|()
block|{
return|return
name|haltOnFailure
return|;
block|}
specifier|public
name|void
name|setHaltonfailure
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|)
block|{
name|this
operator|.
name|haltOnFailure
operator|=
name|haltOnFailure
expr_stmt|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|cacheAttributeNotSupported
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getModule
parameter_list|()
block|{
return|return
name|module
return|;
block|}
specifier|public
name|void
name|setModule
parameter_list|(
name|String
name|module
parameter_list|)
block|{
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|organisation
return|;
block|}
specifier|public
name|void
name|setOrganisation
parameter_list|(
name|String
name|organisation
parameter_list|)
block|{
name|this
operator|.
name|organisation
operator|=
name|organisation
expr_stmt|;
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
name|void
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
block|}
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|void
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
block|}
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
specifier|public
name|String
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
specifier|public
name|void
name|setTo
parameter_list|(
name|String
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
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
name|void
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
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getMatcher
parameter_list|()
block|{
return|return
name|matcher
return|;
block|}
specifier|public
name|void
name|setMatcher
parameter_list|(
name|String
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
name|matcher
expr_stmt|;
block|}
block|}
end_class

end_unit

