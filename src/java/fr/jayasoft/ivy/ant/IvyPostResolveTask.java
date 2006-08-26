begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterHelper
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
comment|/**  * Base class for tasks needing to be performed after a resolve.   *   *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|IvyPostResolveTask
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|_conf
decl_stmt|;
specifier|private
name|boolean
name|_haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_transitive
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_inline
init|=
literal|false
decl_stmt|;
specifier|private
name|File
name|_cache
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
name|String
name|_revision
init|=
literal|"latest.integration"
decl_stmt|;
specifier|private
name|String
name|_type
decl_stmt|;
specifier|private
name|Filter
name|_artifactFilter
init|=
literal|null
decl_stmt|;
specifier|protected
name|void
name|prepareAndCheck
parameter_list|()
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
if|if
condition|(
name|isInline
argument_list|()
condition|)
block|{
name|_conf
operator|=
name|_conf
operator|==
literal|null
condition|?
literal|"*"
else|:
name|_conf
expr_stmt|;
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
literal|"no organisation provided for ivy cache task in inline mode: It can either be set explicitely via the attribute 'organisation' or via 'ivy.organisation' property"
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
literal|"no module name provided for ivy cache task in inline mode: It can either be set explicitely via the attribute 'module' or via 'ivy.module' property"
argument_list|)
throw|;
block|}
name|String
index|[]
name|toResolve
init|=
name|getConfsToResolve
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
operator|+
literal|"-caller"
argument_list|,
name|_conf
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|toResolve
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"using inline mode to resolve "
operator|+
name|getOrganisation
argument_list|()
operator|+
literal|" "
operator|+
name|getModule
argument_list|()
operator|+
literal|" "
operator|+
name|getRevision
argument_list|()
operator|+
literal|" ("
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|toResolve
argument_list|,
literal|", "
argument_list|)
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|IvyResolve
name|resolve
init|=
name|createResolve
argument_list|(
name|isHaltonfailure
argument_list|()
argument_list|)
decl_stmt|;
name|resolve
operator|.
name|setOrganisation
argument_list|(
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setModule
argument_list|(
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setRevision
argument_list|(
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setInline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|setConf
argument_list|(
name|_conf
argument_list|)
expr_stmt|;
name|resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"inline resolve already done for "
operator|+
name|getOrganisation
argument_list|()
operator|+
literal|" "
operator|+
name|getModule
argument_list|()
operator|+
literal|" "
operator|+
name|getRevision
argument_list|()
operator|+
literal|" ("
operator|+
name|_conf
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
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
name|StringUtils
operator|.
name|join
argument_list|(
name|getResolvedConfigurations
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
operator|+
literal|"-caller"
argument_list|,
literal|true
argument_list|)
argument_list|,
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"using standard ensure resolved"
argument_list|)
expr_stmt|;
name|ensureResolved
argument_list|(
name|isHaltonfailure
argument_list|()
argument_list|,
name|isTransitive
argument_list|()
argument_list|,
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
argument_list|,
name|getProperty
argument_list|(
name|_conf
argument_list|,
name|ivy
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
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
literal|"bad conf provided for ivy cache task: * can only be used with a prior call to<resolve/>"
argument_list|)
throw|;
block|}
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
name|_organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy cache task: It can either be set explicitely via the attribute 'organisation' or via 'ivy.organisation' property or a prior call to<resolve/>"
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
literal|"no module name provided for ivy cache task: It can either be set explicitely via the attribute 'module' or via 'ivy.module' property or a prior call to<resolve/>"
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
literal|"no conf provided for ivy cache task: It can either be set explicitely via the attribute 'conf' or via 'ivy.resolved.configurations' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
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
name|_artifactFilter
operator|=
name|FilterHelper
operator|.
name|getArtifactTypeFilter
argument_list|(
name|_type
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ModuleId
name|getResolvedModuleId
parameter_list|()
block|{
return|return
name|isInline
argument_list|()
condition|?
operator|new
name|ModuleId
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
operator|+
literal|"-caller"
argument_list|)
else|:
operator|new
name|ModuleId
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ResolveReport
name|getResolvedReport
parameter_list|()
block|{
return|return
name|getResolvedReport
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|isInline
argument_list|()
condition|?
name|getModule
argument_list|()
operator|+
literal|"-caller"
else|:
name|getModule
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|_type
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
name|_type
operator|=
name|type
expr_stmt|;
block|}
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
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_revision
return|;
block|}
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|rev
parameter_list|)
block|{
name|_revision
operator|=
name|rev
expr_stmt|;
block|}
specifier|public
name|Filter
name|getArtifactFilter
parameter_list|()
block|{
return|return
name|_artifactFilter
return|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|_transitive
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
name|_transitive
operator|=
name|transitive
expr_stmt|;
block|}
specifier|public
name|boolean
name|isInline
parameter_list|()
block|{
return|return
name|_inline
return|;
block|}
specifier|public
name|void
name|setInline
parameter_list|(
name|boolean
name|inline
parameter_list|)
block|{
name|_inline
operator|=
name|inline
expr_stmt|;
block|}
block|}
end_class

end_unit

