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
name|ModuleRevisionId
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
name|ResolvedModuleRevision
import|;
end_import

begin_comment
comment|/**  * Look for the latest module in the repository matching the given criteria,   * and sets a set of properties according to what was found.  *   * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|IvyFindRevision
extends|extends
name|IvyTask
block|{
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
name|_branch
decl_stmt|;
specifier|private
name|String
name|_revision
decl_stmt|;
specifier|private
name|String
name|_property
init|=
literal|"ivy.revision"
decl_stmt|;
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
name|revision
parameter_list|)
block|{
name|_revision
operator|=
name|revision
expr_stmt|;
block|}
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|_branch
return|;
block|}
specifier|public
name|void
name|setBranch
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
name|_branch
operator|=
name|branch
expr_stmt|;
block|}
specifier|public
name|String
name|getProperty
parameter_list|()
block|{
return|return
name|_property
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|_property
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
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
literal|"no organisation provided for ivy findmodules"
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
literal|"no module name provided for ivy findmodules"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_revision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no revision provided for ivy findmodules"
argument_list|)
throw|;
block|}
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|_branch
operator|==
literal|null
condition|)
block|{
name|ivy
operator|.
name|getDefaultBranch
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ResolvedModuleRevision
name|rmr
init|=
name|ivy
operator|.
name|findModule
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_branch
argument_list|,
name|_revision
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmr
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
name|_property
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

