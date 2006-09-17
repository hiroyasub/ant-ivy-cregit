begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
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
name|tools
operator|.
name|ant
operator|.
name|BuildException
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
name|Artifact
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
name|ModuleId
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
name|IvyPatternHelper
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
name|xml
operator|.
name|XmlReportParser
import|;
end_import

begin_comment
comment|/**  * Set a set of ant properties according to the last artifact resolved   *   * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|IvyArtifactProperty
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|_conf
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|String
name|_value
decl_stmt|;
specifier|private
name|String
name|_organisation
decl_stmt|;
specifier|private
name|String
name|_module
decl_stmt|;
specifier|private
name|boolean
name|_haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|_conf
return|;
block|}
specifier|public
name|void
name|setConf
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|_conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|_value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|_value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getModule
parameter_list|()
block|{
return|return
name|_module
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
name|_module
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
name|_organisation
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
name|_organisation
operator|=
name|organisation
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHaltonfailure
parameter_list|()
block|{
return|return
name|_haltOnFailure
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
name|_haltOnFailure
operator|=
name|haltOnFailure
expr_stmt|;
block|}
specifier|public
name|File
name|getCache
parameter_list|()
block|{
return|return
name|_cache
return|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|_cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|void
name|execute
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
name|_organisation
operator|=
name|getProperty
argument_list|(
name|_organisation
argument_list|,
name|ivy
argument_list|,
literal|"ivy.organisation"
argument_list|)
expr_stmt|;
name|_module
operator|=
name|getProperty
argument_list|(
name|_module
argument_list|,
name|ivy
argument_list|,
literal|"ivy.module"
argument_list|)
expr_stmt|;
name|ensureResolved
argument_list|(
name|isHaltonfailure
argument_list|()
argument_list|,
literal|false
argument_list|,
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
name|_conf
operator|=
name|getProperty
argument_list|(
name|_conf
argument_list|,
name|ivy
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|_conf
argument_list|)
condition|)
block|{
name|_conf
operator|=
name|getProperty
argument_list|(
name|ivy
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"bad provided for ivy artifactproperty: * can only be used with a prior call to<resolve/>"
argument_list|)
throw|;
block|}
block|}
name|_organisation
operator|=
name|getProperty
argument_list|(
name|_organisation
argument_list|,
name|ivy
argument_list|,
literal|"ivy.organisation"
argument_list|)
expr_stmt|;
name|_module
operator|=
name|getProperty
argument_list|(
name|_module
argument_list|,
name|ivy
argument_list|,
literal|"ivy.module"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_cache
operator|==
literal|null
condition|)
block|{
name|_cache
operator|=
name|ivy
operator|.
name|getDefaultCache
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy artifactproperty: It can either be set explicitely via the attribute 'organisation' or via 'ivy.organisation' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy artifactproperty: It can either be set explicitely via the attribute 'module' or via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no conf provided for ivy artifactproperty: It can either be set explicitely via the attribute 'conf' or via 'ivy.resolved.configurations' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
try|try
block|{
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|splitConfs
argument_list|(
name|_conf
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
name|Artifact
index|[]
name|artifacts
init|=
name|parser
operator|.
name|getArtifacts
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|)
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|,
name|_cache
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|artifacts
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|Artifact
name|artifact
init|=
name|artifacts
index|[
name|j
index|]
decl_stmt|;
name|String
name|name
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|getName
argument_list|()
argument_list|)
argument_list|,
name|artifact
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|getValue
argument_list|()
argument_list|)
argument_list|,
name|artifact
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"impossible to add artifact properties: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

