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
name|Date
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
name|taskdefs
operator|.
name|Echo
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
name|IvyPublish
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
name|_revision
decl_stmt|;
specifier|private
name|String
name|_pubRevision
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|String
name|_srcivypattern
decl_stmt|;
specifier|private
name|String
name|_status
decl_stmt|;
specifier|private
name|String
name|_pubdate
decl_stmt|;
specifier|private
name|String
name|_deliverTarget
decl_stmt|;
specifier|private
name|String
name|_publishResolverName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_artifactspattern
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|_deliveryList
decl_stmt|;
specifier|private
name|boolean
name|_publishivy
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_warnonmissing
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|_haltonmissing
init|=
literal|true
decl_stmt|;
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
name|getSrcivypattern
parameter_list|()
block|{
return|return
name|_srcivypattern
return|;
block|}
specifier|public
name|void
name|setSrcivypattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|_srcivypattern
operator|=
name|destivypattern
expr_stmt|;
block|}
comment|/**      * @deprecated use getSrcivypattern instead      * @return      */
specifier|public
name|String
name|getDeliverivypattern
parameter_list|()
block|{
return|return
name|_srcivypattern
return|;
block|}
comment|/**      * @deprecated use setSrcivypattern instead      * @return      */
specifier|public
name|void
name|setDeliverivypattern
parameter_list|(
name|String
name|destivypattern
parameter_list|)
block|{
name|_srcivypattern
operator|=
name|destivypattern
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
name|String
name|getPubdate
parameter_list|()
block|{
return|return
name|_pubdate
return|;
block|}
specifier|public
name|void
name|setPubdate
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
name|getPubrevision
parameter_list|()
block|{
return|return
name|_pubRevision
return|;
block|}
specifier|public
name|void
name|setPubrevision
parameter_list|(
name|String
name|pubRevision
parameter_list|)
block|{
name|_pubRevision
operator|=
name|pubRevision
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
name|getStatus
parameter_list|()
block|{
return|return
name|_status
return|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|_status
operator|=
name|status
expr_stmt|;
block|}
specifier|public
name|void
name|setDelivertarget
parameter_list|(
name|String
name|deliverTarget
parameter_list|)
block|{
name|_deliverTarget
operator|=
name|deliverTarget
expr_stmt|;
block|}
specifier|public
name|void
name|setDeliveryList
parameter_list|(
name|File
name|deliveryList
parameter_list|)
block|{
name|_deliveryList
operator|=
name|deliveryList
expr_stmt|;
block|}
specifier|public
name|String
name|getResolver
parameter_list|()
block|{
return|return
name|_publishResolverName
return|;
block|}
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|publishResolverName
parameter_list|)
block|{
name|_publishResolverName
operator|=
name|publishResolverName
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactspattern
parameter_list|()
block|{
return|return
name|_artifactspattern
return|;
block|}
specifier|public
name|void
name|setArtifactspattern
parameter_list|(
name|String
name|artifactsPattern
parameter_list|)
block|{
name|_artifactspattern
operator|=
name|artifactsPattern
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
name|_pubRevision
operator|=
name|getProperty
argument_list|(
name|_pubRevision
argument_list|,
name|ivy
argument_list|,
literal|"ivy.deliver.revision"
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
name|_artifactspattern
operator|=
name|getProperty
argument_list|(
name|_artifactspattern
argument_list|,
name|ivy
argument_list|,
literal|"ivy.publish.src.artifacts.pattern"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_srcivypattern
operator|==
literal|null
condition|)
block|{
name|_srcivypattern
operator|=
name|_artifactspattern
expr_stmt|;
block|}
name|_status
operator|=
name|getProperty
argument_list|(
name|_status
argument_list|,
name|ivy
argument_list|,
literal|"ivy.status"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_module
operator|==
literal|null
operator|||
name|_organisation
operator|==
literal|null
operator|||
name|_revision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"empty or incomplete module revision id provided for publish: either call resolve, give paramaters to publish, or provide ivy.module, ivy.organisation and ivy.revision properties"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_artifactspattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no artifacts pattern: either provide it through parameter or through ivy.publish.src.artifacts.pattern property"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_publishResolverName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no publish deliver name: please provide it through parameter 'resolver'"
argument_list|)
throw|;
block|}
name|Date
name|pubdate
init|=
name|getPubDate
argument_list|(
name|_pubdate
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|_pubRevision
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|_revision
operator|.
name|startsWith
argument_list|(
literal|"working@"
argument_list|)
condition|)
block|{
name|_pubRevision
operator|=
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|pubdate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|_pubRevision
operator|=
name|_revision
expr_stmt|;
block|}
block|}
if|if
condition|(
name|_status
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no status provided: either provide it as parameter or through the ivy.status.default property"
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
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_revision
argument_list|)
decl_stmt|;
try|try
block|{
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
name|_cache
argument_list|,
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|_srcivypattern
argument_list|,
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_pubRevision
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|_publishivy
operator|&&
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|IvyDeliver
name|deliver
init|=
operator|new
name|IvyDeliver
argument_list|()
decl_stmt|;
name|deliver
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setCache
argument_list|(
name|getCache
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDeliverpattern
argument_list|(
name|getSrcivypattern
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDelivertarget
argument_list|(
name|_deliverTarget
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setDeliveryList
argument_list|(
name|_deliveryList
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setModule
argument_list|(
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setOrganisation
argument_list|(
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setPubdate
argument_list|(
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|pubdate
argument_list|)
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setPubrevision
argument_list|(
name|getPubrevision
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setRevision
argument_list|(
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setStatus
argument_list|(
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|ivy
argument_list|)
argument_list|)
expr_stmt|;
name|deliver
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|Collection
name|missing
init|=
name|ivy
operator|.
name|publish
argument_list|(
name|mrid
argument_list|,
name|_pubRevision
argument_list|,
name|_cache
argument_list|,
name|_artifactspattern
argument_list|,
name|_publishResolverName
argument_list|,
name|_publishivy
condition|?
name|_srcivypattern
else|:
literal|null
argument_list|,
name|doValidate
argument_list|(
name|ivy
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|_warnonmissing
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|missing
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"missing artifact: "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|_haltonmissing
operator|&&
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"missing published artifacts for "
operator|+
name|mrid
operator|+
literal|": "
operator|+
name|missing
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to publish artifacts for "
operator|+
name|mrid
operator|+
literal|": "
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
specifier|private
name|void
name|loadDeliveryList
parameter_list|()
block|{
name|Property
name|property
init|=
operator|(
name|Property
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"property"
argument_list|)
decl_stmt|;
name|property
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|property
operator|.
name|init
argument_list|()
expr_stmt|;
name|property
operator|.
name|setFile
argument_list|(
name|_deliveryList
argument_list|)
expr_stmt|;
name|property
operator|.
name|perform
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|appendDeliveryList
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|Echo
name|echo
init|=
operator|(
name|Echo
operator|)
name|getProject
argument_list|()
operator|.
name|createTask
argument_list|(
literal|"echo"
argument_list|)
decl_stmt|;
name|echo
operator|.
name|setOwningTarget
argument_list|(
name|getOwningTarget
argument_list|()
argument_list|)
expr_stmt|;
name|echo
operator|.
name|init
argument_list|()
expr_stmt|;
name|echo
operator|.
name|setFile
argument_list|(
name|_deliveryList
argument_list|)
expr_stmt|;
name|echo
operator|.
name|setMessage
argument_list|(
name|msg
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|echo
operator|.
name|setAppend
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|echo
operator|.
name|perform
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPublishivy
parameter_list|()
block|{
return|return
name|_publishivy
return|;
block|}
specifier|public
name|void
name|setPublishivy
parameter_list|(
name|boolean
name|publishivy
parameter_list|)
block|{
name|_publishivy
operator|=
name|publishivy
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWarnonmissing
parameter_list|()
block|{
return|return
name|_warnonmissing
return|;
block|}
specifier|public
name|void
name|setWarnonmissing
parameter_list|(
name|boolean
name|warnonmissing
parameter_list|)
block|{
name|_warnonmissing
operator|=
name|warnonmissing
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHaltonmissing
parameter_list|()
block|{
return|return
name|_haltonmissing
return|;
block|}
specifier|public
name|void
name|setHaltonmissing
parameter_list|(
name|boolean
name|haltonmissing
parameter_list|)
block|{
name|_haltonmissing
operator|=
name|haltonmissing
expr_stmt|;
block|}
block|}
end_class

end_unit

