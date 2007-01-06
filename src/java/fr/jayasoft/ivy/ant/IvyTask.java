begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
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
name|Arrays
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
name|HashSet
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
name|Task
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|IvyContext
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|report
operator|.
name|ResolveReport
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Base class for all ivy ant tasks, deal particularly with ivy instance storage in ant project.  *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
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
name|_validate
init|=
literal|null
decl_stmt|;
specifier|protected
name|boolean
name|doValidate
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
if|if
condition|(
name|_validate
operator|!=
literal|null
condition|)
block|{
return|return
name|_validate
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
name|_validate
operator|==
literal|null
condition|?
literal|true
else|:
name|_validate
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
name|_validate
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|validate
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Ivy
name|getIvyInstance
parameter_list|()
block|{
name|ensureMessageInitialised
argument_list|()
expr_stmt|;
name|Object
name|ref
init|=
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.instances"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|ref
operator|instanceof
name|Map
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"ivy problem with ant: ivy.instances reference is not a Map. Please do not sett ivy.instances reference in your ant project. current reference: "
operator|+
name|ref
operator|+
literal|" class="
operator|+
name|ref
operator|.
name|getClass
argument_list|()
operator|+
literal|" classloader="
operator|+
name|ref
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
throw|;
block|}
name|Map
name|instances
init|=
operator|(
name|Map
operator|)
name|ref
decl_stmt|;
if|if
condition|(
name|instances
operator|==
literal|null
operator|||
operator|!
name|instances
operator|.
name|containsKey
argument_list|(
name|Ivy
operator|.
name|class
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no ivy instance found: auto configuring ivy"
argument_list|)
expr_stmt|;
name|IvyConfigure
name|configure
init|=
operator|new
name|IvyConfigure
argument_list|()
decl_stmt|;
name|configure
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|configure
operator|.
name|execute
argument_list|()
expr_stmt|;
name|instances
operator|=
operator|(
name|Map
operator|)
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.instances"
argument_list|)
expr_stmt|;
if|if
condition|(
name|instances
operator|==
literal|null
operator|||
operator|!
name|instances
operator|.
name|containsKey
argument_list|(
name|Ivy
operator|.
name|class
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"ivy internal problem: impossible to get ivy instance after configure... maybe a classloader problem"
argument_list|)
throw|;
block|}
block|}
return|return
operator|(
name|Ivy
operator|)
name|instances
operator|.
name|get
argument_list|(
name|Ivy
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**       * Every task MUST call ensureMessageInitialised when the execution method      * starts (at least before doing any log in order to set the correct task      * in the log.      */
specifier|protected
name|void
name|ensureMessageInitialised
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Message
operator|.
name|isInitialised
argument_list|()
condition|)
block|{
name|Message
operator|.
name|init
argument_list|(
operator|new
name|AntMessageImpl
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setIvyInstance
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
comment|// this reference is not used anymore, what is used is the instances map below
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.instance"
argument_list|,
name|ivy
argument_list|)
expr_stmt|;
if|if
condition|(
name|ivy
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"setting ivy.instance on "
operator|+
name|getProject
argument_list|()
operator|+
literal|": "
operator|+
name|ivy
operator|+
literal|" class="
operator|+
name|ivy
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" classloader="
operator|+
name|ivy
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// we keep a map of ivy instances per Ivy class, in case of multiple classloaders
name|Map
name|instances
init|=
operator|(
name|Map
operator|)
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.instances"
argument_list|)
decl_stmt|;
if|if
condition|(
name|instances
operator|==
literal|null
condition|)
block|{
name|instances
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
literal|"ivy.instances"
argument_list|,
name|instances
argument_list|)
expr_stmt|;
block|}
name|instances
operator|.
name|put
argument_list|(
name|ivy
operator|.
name|getClass
argument_list|()
argument_list|,
name|ivy
argument_list|)
expr_stmt|;
block|}
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
name|ensureResolved
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|,
name|boolean
name|useOrigin
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|)
block|{
name|ensureResolved
argument_list|(
name|haltOnFailure
argument_list|,
name|useOrigin
argument_list|,
literal|true
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|ensureResolved
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|,
name|boolean
name|useOrigin
parameter_list|,
name|boolean
name|transitive
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
name|ensureMessageInitialised
argument_list|()
expr_stmt|;
comment|//        if (org != null&& module != null) {
comment|//            return;
comment|//        }
name|String
index|[]
name|confs
init|=
name|getConfsToResolve
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
name|conf
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|confs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|IvyResolve
name|resolve
init|=
name|createResolve
argument_list|(
name|haltOnFailure
argument_list|,
name|useOrigin
argument_list|)
decl_stmt|;
name|resolve
operator|.
name|setTransitive
argument_list|(
name|transitive
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setConf
argument_list|(
name|StringUtils
operator|.
name|join
argument_list|(
name|confs
argument_list|,
literal|", "
argument_list|)
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|String
index|[]
name|getConfsToResolve
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|conf
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
name|ModuleDescriptor
name|reference
init|=
operator|(
name|ModuleDescriptor
operator|)
name|getResolvedDescriptor
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
name|strict
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"calculating configurations to resolve"
argument_list|)
expr_stmt|;
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"module not yet resolved, all confs still need to be resolved"
argument_list|)
expr_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
return|;
block|}
else|else
block|{
return|return
name|splitConfs
argument_list|(
name|conf
argument_list|)
return|;
block|}
block|}
if|else if
condition|(
name|conf
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|rconfs
init|=
name|getResolvedConfigurations
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
name|strict
argument_list|)
decl_stmt|;
name|String
index|[]
name|confs
decl_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|conf
argument_list|)
condition|)
block|{
name|confs
operator|=
name|reference
operator|.
name|getConfigurationsNames
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|confs
operator|=
name|splitConfs
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
name|HashSet
name|rconfsSet
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|rconfs
argument_list|)
argument_list|)
decl_stmt|;
name|HashSet
name|confsSet
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|confs
argument_list|)
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"resolved configurations:   "
operator|+
name|rconfsSet
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"asked configurations:      "
operator|+
name|confsSet
argument_list|)
expr_stmt|;
name|confsSet
operator|.
name|removeAll
argument_list|(
name|rconfsSet
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"to resolve configurations: "
operator|+
name|confsSet
argument_list|)
expr_stmt|;
return|return
operator|(
name|String
index|[]
operator|)
name|confsSet
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confsSet
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"module already resolved, no configuration to resolve"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
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
parameter_list|)
block|{
return|return
name|getResolvedReport
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
name|ResolveReport
name|getResolvedReport
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
name|strict
argument_list|)
return|;
block|}
specifier|protected
name|IvyResolve
name|createResolve
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|,
name|boolean
name|useOrigin
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no resolved descriptor found: launching default resolve"
argument_list|)
expr_stmt|;
name|IvyResolve
name|resolve
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|resolve
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setHaltonfailure
argument_list|(
name|haltOnFailure
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setUseOrigin
argument_list|(
name|useOrigin
argument_list|)
expr_stmt|;
if|if
condition|(
name|_validate
operator|!=
literal|null
condition|)
block|{
name|resolve
operator|.
name|setValidate
argument_list|(
name|_validate
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resolve
return|;
block|}
specifier|protected
name|boolean
name|shouldResolve
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|)
block|{
name|ensureMessageInitialised
argument_list|()
expr_stmt|;
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
return|return
literal|false
return|;
block|}
name|Object
name|reference
init|=
name|getResolvedDescriptor
argument_list|(
name|org
argument_list|,
name|module
argument_list|)
decl_stmt|;
return|return
operator|(
name|reference
operator|==
literal|null
operator|)
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
name|equalsIgnoreCase
argument_list|(
name|date
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
name|Ivy
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
name|Ivy
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
specifier|public
name|void
name|setProject
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|super
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|set
argument_list|(
name|ANT_PROJECT_CONTEXT_KEY
argument_list|,
name|project
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

