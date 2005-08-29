begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
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
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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

begin_comment
comment|/**  * @author Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|IvyResolve
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|_file
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_conf
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|_cache
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_revision
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_pubdate
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|_haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_useCacheOnly
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|_type
init|=
literal|null
decl_stmt|;
specifier|public
name|String
name|getDate
parameter_list|()
block|{
return|return
name|_pubdate
return|;
block|}
specifier|public
name|void
name|setDate
parameter_list|(
name|String
name|pubdate
parameter_list|)
block|{
name|_pubdate
operator|=
name|pubdate
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
name|File
name|getFile
parameter_list|()
block|{
return|return
name|_file
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|_file
operator|=
name|file
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
name|void
name|setShowprogress
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
name|Message
operator|.
name|setShowProgress
argument_list|(
name|show
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseCacheOnly
parameter_list|()
block|{
return|return
name|_useCacheOnly
return|;
block|}
specifier|public
name|void
name|setUseCacheOnly
parameter_list|(
name|boolean
name|useCacheOnly
parameter_list|)
block|{
name|_useCacheOnly
operator|=
name|useCacheOnly
expr_stmt|;
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
try|try
block|{
if|if
condition|(
name|_file
operator|==
literal|null
condition|)
block|{
name|_file
operator|=
operator|new
name|File
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|,
name|getProperty
argument_list|(
name|ivy
argument_list|,
literal|"ivy.dep.file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|_conf
operator|=
name|getProperty
argument_list|(
name|_conf
argument_list|,
name|ivy
argument_list|,
literal|"ivy.configurations"
argument_list|)
expr_stmt|;
name|_revision
operator|=
name|getProperty
argument_list|(
name|_revision
argument_list|,
name|ivy
argument_list|,
literal|"ivy.revision"
argument_list|)
expr_stmt|;
name|_type
operator|=
name|getProperty
argument_list|(
name|_type
argument_list|,
name|ivy
argument_list|,
literal|"ivy.resolve.default.type.filter"
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
name|String
index|[]
name|confs
init|=
name|splitConfs
argument_list|(
name|_conf
argument_list|)
decl_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|_file
operator|.
name|toURL
argument_list|()
argument_list|,
name|_revision
argument_list|,
name|confs
argument_list|,
name|_cache
argument_list|,
name|getPubDate
argument_list|(
name|_pubdate
argument_list|,
literal|null
argument_list|)
argument_list|,
name|doValidate
argument_list|(
name|ivy
argument_list|)
argument_list|,
name|_useCacheOnly
argument_list|,
name|FilterHelper
operator|.
name|getArtifactTypeFilter
argument_list|(
name|_type
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isHaltonfailure
argument_list|()
operator|&&
name|report
operator|.
name|hasError
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"resolve failed - see output for details"
argument_list|)
throw|;
block|}
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|setResolved
argument_list|(
name|md
argument_list|)
expr_stmt|;
comment|// put resolved infos in ant properties and ivy variables
comment|// putting them in ivy variables is important to be able to change from one resolve call to the other
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.organisation"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.organisation"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.module"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.module"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.revision"
argument_list|,
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.revision"
argument_list|,
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.deps.changed"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|hasChanged
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|report
operator|.
name|hasChanged
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|_conf
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|mergeConfs
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|mergeConfs
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|_conf
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.resolved.configurations"
argument_list|,
name|_conf
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to convert given ivy file to url: "
operator|+
name|_file
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"syntax errors in ivy file"
argument_list|,
name|e
argument_list|)
throw|;
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
literal|"impossible to resolve dependencies: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

