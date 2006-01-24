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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|DependencyDescriptor
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
name|ResolvedModuleRevision
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

begin_comment
comment|/**  * IBiblioResolver is a resolver which can be used to resolve dependencies found  * in the ibiblio maven repository, or similar repositories.  * For more flexibility with url and patterns, see {@link fr.jayasoft.ivy.resolver.URLResolver}.  */
end_comment

begin_class
specifier|public
class|class
name|IBiblioResolver
extends|extends
name|URLResolver
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATTERN
init|=
literal|"[module]/[type]s/[artifact]-[revision].[ext]"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ROOT
init|=
literal|"http://www.ibiblio.org/maven/"
decl_stmt|;
specifier|private
name|String
name|_root
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_pattern
init|=
literal|null
decl_stmt|;
specifier|public
name|IBiblioResolver
parameter_list|()
block|{
block|}
specifier|protected
name|ResolvedResource
name|findIvyFileRef
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
block|{
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
name|mrid
operator|=
name|convertM2IdForResourceSearch
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
name|ResolvedResource
name|rres
init|=
name|findResourceUsingPatterns
argument_list|(
name|mrid
argument_list|,
name|getIvyPatterns
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
literal|"pom"
argument_list|,
literal|"pom"
argument_list|,
name|data
operator|.
name|getDate
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|rres
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setM2compatible
parameter_list|(
name|boolean
name|m2compatible
parameter_list|)
block|{
name|super
operator|.
name|setM2compatible
argument_list|(
name|m2compatible
argument_list|)
expr_stmt|;
if|if
condition|(
name|m2compatible
condition|)
block|{
name|_root
operator|=
literal|"http://www.ibiblio.org/maven2/"
expr_stmt|;
name|_pattern
operator|=
literal|"[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|ensureConfigured
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
if|if
condition|(
name|ivy
operator|!=
literal|null
operator|&&
operator|(
name|_root
operator|==
literal|null
operator|||
name|_pattern
operator|==
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|_root
operator|==
literal|null
condition|)
block|{
name|String
name|root
init|=
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.root"
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|_root
operator|=
name|root
expr_stmt|;
block|}
else|else
block|{
name|ivy
operator|.
name|configureRepositories
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|_root
operator|=
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.root"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|_pattern
operator|==
literal|null
condition|)
block|{
name|String
name|pattern
init|=
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.pattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|_pattern
operator|=
name|pattern
expr_stmt|;
block|}
else|else
block|{
name|ivy
operator|.
name|configureRepositories
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|_pattern
operator|=
name|ivy
operator|.
name|getVariable
argument_list|(
literal|"ivy.ibiblio.default.artifact.pattern"
argument_list|)
expr_stmt|;
block|}
block|}
name|updateWholePattern
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getWholePattern
parameter_list|()
block|{
return|return
name|_root
operator|+
name|_pattern
return|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|_pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"pattern must not be null"
argument_list|)
throw|;
block|}
name|_pattern
operator|=
name|pattern
expr_stmt|;
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholePattern
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getRoot
parameter_list|()
block|{
return|return
name|_root
return|;
block|}
comment|/**      * Sets the root of the maven like repository.      * The maven like repository is necessarily an http repository.      * @param root the root of the maven like repository      * @throws IllegalArgumentException if root does not start with "http://"      */
specifier|public
name|void
name|setRoot
parameter_list|(
name|String
name|root
parameter_list|)
block|{
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"root must not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|root
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|_root
operator|=
name|root
operator|+
literal|"/"
expr_stmt|;
block|}
else|else
block|{
name|_root
operator|=
name|root
expr_stmt|;
block|}
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholePattern
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|updateWholePattern
parameter_list|()
block|{
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|setIvyPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getWholePattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setArtifactPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getWholePattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"publish not supported by IBiblioResolver"
argument_list|)
throw|;
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
block|{
return|return
literal|null
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
literal|null
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|mod
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|listRevisions
argument_list|(
name|mod
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"ibiblio"
return|;
block|}
comment|// override some methods to ensure configuration
specifier|public
name|ResolvedModuleRevision
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
throws|throws
name|ParseException
block|{
name|ensureConfigured
argument_list|(
name|data
operator|.
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
return|;
block|}
specifier|protected
name|ResolvedResource
name|findArtifactRef
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|findArtifactRef
argument_list|(
name|artifact
argument_list|,
name|date
argument_list|)
return|;
block|}
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|download
argument_list|(
name|artifacts
argument_list|,
name|ivy
argument_list|,
name|cache
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|exists
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|List
name|getArtifactPatterns
parameter_list|()
block|{
name|ensureConfigured
argument_list|(
name|getIvy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getArtifactPatterns
argument_list|()
return|;
block|}
block|}
end_class

end_unit

