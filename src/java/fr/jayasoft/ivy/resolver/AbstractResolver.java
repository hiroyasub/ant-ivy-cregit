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
name|resolver
package|;
end_package

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
name|DependencyResolver
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
name|IvyAware
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
name|LatestStrategy
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
name|ResolveData
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
name|namespace
operator|.
name|Namespace
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
name|ArtifactDownloadReport
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
name|DownloadReport
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
name|DownloadStatus
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
comment|/**  * This abstract resolver only provides handling for resolver name  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractResolver
implements|implements
name|DependencyResolver
implements|,
name|IvyAware
implements|,
name|HasLatestStrategy
block|{
comment|/**      * True if parsed ivy files should be validated against xsd, false if they should not,      * null if default behaviour should be used      */
specifier|private
name|Boolean
name|_validate
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|Ivy
name|_ivy
decl_stmt|;
comment|/**      * The latest strategy to use to find latest among several artifacts      */
specifier|private
name|LatestStrategy
name|_latestStrategy
decl_stmt|;
specifier|private
name|String
name|_latestStrategyName
decl_stmt|;
comment|/**      * The namespace to which this resolver belongs      */
specifier|private
name|Namespace
name|_namespace
decl_stmt|;
specifier|private
name|String
name|_namespaceName
decl_stmt|;
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|_ivy
return|;
block|}
specifier|public
name|void
name|setIvy
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
name|_ivy
operator|=
name|ivy
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
specifier|protected
name|boolean
name|doValidate
parameter_list|(
name|ResolveData
name|data
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
else|else
block|{
return|return
name|data
operator|.
name|isValidate
argument_list|()
return|;
block|}
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
specifier|public
name|void
name|reportFailure
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no failure report implemented by "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|reportFailure
parameter_list|(
name|Artifact
name|art
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no failure report implemented by "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
block|{
return|return
operator|new
name|OrganisationEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|ModuleEntry
index|[]
name|listModules
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
return|return
operator|new
name|ModuleEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
block|{
return|return
operator|new
name|RevisionEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|void
name|dumpConfig
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|" ["
operator|+
name|getTypeName
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Default implementation actually download the artifact      * Subclasses should overwrite this to avoid the download      */
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|DownloadReport
name|dr
init|=
name|download
argument_list|(
operator|new
name|Artifact
index|[]
block|{
name|artifact
block|}
argument_list|,
name|getIvy
argument_list|()
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getDefaultCache
argument_list|()
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|adr
init|=
name|dr
operator|.
name|getArtifactReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|DownloadStatus
operator|.
name|FAILED
return|;
block|}
specifier|public
name|LatestStrategy
name|getLatestStrategy
parameter_list|()
block|{
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getIvy
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_latestStrategyName
operator|!=
literal|null
operator|&&
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|_latestStrategyName
argument_list|)
condition|)
block|{
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getLatestStrategy
argument_list|(
name|_latestStrategyName
argument_list|)
expr_stmt|;
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"unknown latest strategy: "
operator|+
name|_latestStrategyName
argument_list|)
expr_stmt|;
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|getName
argument_list|()
operator|+
literal|": no latest strategy defined: using default"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no ivy instance found: impossible to get a latest strategy without ivy instance"
argument_list|)
throw|;
block|}
block|}
return|return
name|_latestStrategy
return|;
block|}
specifier|public
name|void
name|setLatestStrategy
parameter_list|(
name|LatestStrategy
name|latestStrategy
parameter_list|)
block|{
name|_latestStrategy
operator|=
name|latestStrategy
expr_stmt|;
block|}
specifier|public
name|void
name|setLatest
parameter_list|(
name|String
name|strategyName
parameter_list|)
block|{
name|_latestStrategyName
operator|=
name|strategyName
expr_stmt|;
block|}
specifier|public
name|String
name|getLatest
parameter_list|()
block|{
if|if
condition|(
name|_latestStrategyName
operator|==
literal|null
condition|)
block|{
name|_latestStrategyName
operator|=
literal|"default"
expr_stmt|;
block|}
return|return
name|_latestStrategyName
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
if|if
condition|(
name|_namespace
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getIvy
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_namespaceName
operator|!=
literal|null
condition|)
block|{
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getNamespace
argument_list|(
name|_namespaceName
argument_list|)
expr_stmt|;
if|if
condition|(
name|_namespace
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"unknown namespace: "
operator|+
name|_namespaceName
argument_list|)
expr_stmt|;
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getSystemNamespace
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getSystemNamespace
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|getName
argument_list|()
operator|+
literal|": no namespace defined: using system"
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
name|getName
argument_list|()
operator|+
literal|": no namespace defined nor ivy instance: using system namespace"
argument_list|)
expr_stmt|;
name|_namespace
operator|=
name|Namespace
operator|.
name|SYSTEM_NAMESPACE
expr_stmt|;
block|}
block|}
return|return
name|_namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespaceName
parameter_list|)
block|{
name|_namespaceName
operator|=
name|namespaceName
expr_stmt|;
block|}
block|}
end_class

end_unit

