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
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Locale
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
name|StringUtils
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
name|Task
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
name|Reference
import|;
end_import

begin_comment
comment|/**  * Base class for all ivy ant tasks, deal particularly with ivy instance storage in ant project.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|IvyTask
extends|extends
name|Task
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ANT_PROJECT_CONTEXT_KEY
init|=
literal|"ant-project"
decl_stmt|;
specifier|private
name|Boolean
name|validate
init|=
literal|null
decl_stmt|;
specifier|private
name|Reference
name|antIvyEngineRef
init|=
literal|null
decl_stmt|;
specifier|protected
name|boolean
name|doValidate
parameter_list|(
name|IvySettings
name|ivy
parameter_list|)
block|{
if|if
condition|(
name|validate
operator|!=
literal|null
condition|)
block|{
return|return
name|validate
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
name|ivy
operator|.
name|doValidate
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
operator|==
literal|null
condition|?
literal|true
else|:
name|validate
operator|.
name|booleanValue
argument_list|()
return|;
block|}
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
name|Boolean
operator|.
name|valueOf
argument_list|(
name|validate
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSettingsRef
parameter_list|(
name|Reference
name|ref
parameter_list|)
block|{
name|antIvyEngineRef
operator|=
name|ref
expr_stmt|;
block|}
specifier|public
name|Reference
name|getSettingsRef
parameter_list|()
block|{
return|return
name|antIvyEngineRef
return|;
block|}
specifier|protected
name|IvySettings
name|getSettings
parameter_list|()
block|{
return|return
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
return|;
block|}
specifier|protected
name|Ivy
name|getIvyInstance
parameter_list|()
block|{
name|Object
name|antIvyEngine
decl_stmt|;
if|if
condition|(
name|antIvyEngineRef
operator|!=
literal|null
condition|)
block|{
name|antIvyEngine
operator|=
name|antIvyEngineRef
operator|.
name|getReferencedObject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|antIvyEngine
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|IvyAntSettings
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|antIvyEngineRef
operator|.
name|getRefId
argument_list|()
operator|+
literal|" doesn't reference an ivy:settings"
argument_list|,
name|getLocation
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|antIvyEngine
operator|instanceof
name|IvyAntSettings
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|antIvyEngineRef
operator|.
name|getRefId
argument_list|()
operator|+
literal|" has been defined in a different classloader.  "
operator|+
literal|"Please use the same loader when defining your task, or "
operator|+
literal|"redeclare your ivy:settings in this classloader"
argument_list|,
name|getLocation
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|antIvyEngine
operator|=
name|IvyAntSettings
operator|.
name|getDefaultInstance
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Ivy
name|ivy
init|=
operator|(
operator|(
name|IvyAntSettings
operator|)
name|antIvyEngine
operator|)
operator|.
name|getConfiguredIvyInstance
argument_list|()
decl_stmt|;
name|AntMessageLogger
operator|.
name|register
argument_list|(
name|this
argument_list|,
name|ivy
argument_list|)
expr_stmt|;
return|return
name|ivy
return|;
block|}
specifier|protected
name|void
name|setResolved
parameter_list|(
name|ResolveReport
name|report
parameter_list|,
name|boolean
name|keep
parameter_list|)
block|{
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|report
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|keep
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|,
name|report
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.configurations.ref"
argument_list|,
name|confs
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|,
name|md
argument_list|)
expr_stmt|;
block|}
name|String
name|suffix
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"."
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
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.report."
operator|+
name|suffix
argument_list|,
name|report
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.descriptor."
operator|+
name|suffix
argument_list|,
name|md
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.configurations.ref."
operator|+
name|suffix
argument_list|,
name|confs
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setResolved
parameter_list|(
name|ResolveReport
name|report
parameter_list|,
name|String
name|resolveId
parameter_list|,
name|boolean
name|keep
parameter_list|)
block|{
name|setResolved
argument_list|(
name|report
argument_list|,
name|keep
argument_list|)
expr_stmt|;
if|if
condition|(
name|resolveId
operator|!=
literal|null
condition|)
block|{
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|report
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.report."
operator|+
name|resolveId
argument_list|,
name|report
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.descriptor."
operator|+
name|resolveId
argument_list|,
name|md
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.resolved.configurations.ref."
operator|+
name|resolveId
argument_list|,
name|confs
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
index|[]
name|getResolvedConfigurations
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|getReference
argument_list|(
literal|"ivy.resolved.configurations.ref"
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
name|strict
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|getResolvedDescriptor
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
return|return
name|getResolvedDescriptor
argument_list|(
name|resolveId
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|getResolvedDescriptor
parameter_list|(
name|String
name|resolveId
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
name|Object
name|result
init|=
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor."
operator|+
name|resolveId
argument_list|)
decl_stmt|;
if|if
condition|(
name|strict
operator|&&
operator|(
name|result
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"ModuleDescriptor for resolve with id '"
operator|+
name|resolveId
operator|+
literal|"' not found."
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|Object
name|getResolvedDescriptor
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|)
block|{
return|return
name|getResolvedDescriptor
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|getResolvedDescriptor
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
return|return
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
name|strict
argument_list|)
return|;
block|}
specifier|private
name|Object
name|getReference
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
name|Object
name|reference
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|org
operator|!=
literal|null
operator|&&
name|module
operator|!=
literal|null
condition|)
block|{
name|reference
operator|=
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
name|prefix
operator|+
literal|"."
operator|+
name|org
operator|+
literal|"."
operator|+
name|module
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|strict
operator|&&
name|reference
operator|==
literal|null
condition|)
block|{
name|reference
operator|=
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
return|return
name|reference
return|;
block|}
specifier|protected
name|ResolveReport
name|getResolvedReport
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|resolveId
parameter_list|)
block|{
name|ResolveReport
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resolveId
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|(
name|ResolveReport
operator|)
name|getReference
argument_list|(
literal|"ivy.resolved.report"
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|(
name|ResolveReport
operator|)
name|getReference
argument_list|(
literal|"ivy.resolved.report."
operator|+
name|resolveId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|String
index|[]
name|splitConfs
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|confs
init|=
name|conf
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
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
name|confs
index|[
name|i
index|]
operator|=
name|confs
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|confs
return|;
block|}
specifier|protected
name|String
name|mergeConfs
parameter_list|(
name|String
index|[]
name|conf
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|join
argument_list|(
name|conf
argument_list|,
literal|", "
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
name|DateFormat
name|DATE_FORMAT
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMddHHmmss"
argument_list|)
decl_stmt|;
specifier|protected
name|Date
name|getPubDate
parameter_list|(
name|String
name|date
parameter_list|,
name|Date
name|def
parameter_list|)
block|{
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"now"
operator|.
name|equals
argument_list|(
name|date
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|Date
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|DATE_FORMAT
operator|.
name|parse
argument_list|(
name|date
argument_list|)
return|;
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
literal|"publication date provided in bad format. should be yyyyMMddHHmmss and not "
operator|+
name|date
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|def
return|;
block|}
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|String
name|value
parameter_list|,
name|IvySettings
name|ivy
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|getProperty
argument_list|(
name|ivy
argument_list|,
name|name
argument_list|)
return|;
block|}
else|else
block|{
name|value
operator|=
name|ivy
operator|.
name|substitute
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"parameter found as attribute value: "
operator|+
name|name
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|String
name|value
parameter_list|,
name|IvySettings
name|ivy
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|resolveId
parameter_list|)
block|{
if|if
condition|(
name|resolveId
operator|==
literal|null
condition|)
block|{
return|return
name|getProperty
argument_list|(
name|value
argument_list|,
name|ivy
argument_list|,
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getProperty
argument_list|(
name|value
argument_list|,
name|ivy
argument_list|,
name|name
operator|+
literal|"."
operator|+
name|resolveId
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|IvySettings
name|ivy
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|resolveId
parameter_list|)
block|{
if|if
condition|(
name|resolveId
operator|==
literal|null
condition|)
block|{
return|return
name|getProperty
argument_list|(
name|ivy
argument_list|,
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getProperty
argument_list|(
name|ivy
argument_list|,
name|name
operator|+
literal|"."
operator|+
name|resolveId
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
name|getProperty
parameter_list|(
name|IvySettings
name|ivy
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|val
init|=
name|ivy
operator|.
name|getVariable
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|ivy
operator|.
name|substitute
argument_list|(
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"parameter found as ant project property: "
operator|+
name|name
operator|+
literal|"="
operator|+
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"parameter not found: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|val
operator|=
name|ivy
operator|.
name|substitute
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"parameter found as ivy variable: "
operator|+
name|name
operator|+
literal|"="
operator|+
name|val
argument_list|)
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
comment|/**      * Called when task starts its execution.      */
specifier|protected
name|void
name|prepareTask
parameter_list|()
block|{
comment|// push current project on the stack in context
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|push
argument_list|(
name|ANT_PROJECT_CONTEXT_KEY
argument_list|,
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called when task is about to finish Should clean up all state related information (stacks for      * example)      */
specifier|protected
name|void
name|finalizeTask
parameter_list|()
block|{
if|if
condition|(
operator|!
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|pop
argument_list|(
name|ANT_PROJECT_CONTEXT_KEY
argument_list|,
name|getProject
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"ANT project poped from stack not equals current !. Ignoring"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Ant task execute. Calls prepareTask, doExecute, finalzeTask      */
specifier|public
specifier|final
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
try|try
block|{
name|prepareTask
argument_list|()
expr_stmt|;
name|doExecute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|finalizeTask
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * The real logic of task execution after project has been set in the context. MUST be      * implemented by subclasses      *       * @throws BuildException      */
specifier|public
specifier|abstract
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
function_decl|;
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|getTaskName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

