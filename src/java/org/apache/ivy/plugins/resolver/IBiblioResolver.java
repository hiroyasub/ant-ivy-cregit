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
name|plugins
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
name|ivy
operator|.
name|core
operator|.
name|IvyPatternHelper
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
name|Artifact
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
name|DefaultArtifact
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
name|DependencyDescriptor
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
name|DownloadReport
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
name|resolve
operator|.
name|DownloadOptions
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
name|resolve
operator|.
name|ResolveData
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|search
operator|.
name|ModuleEntry
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
name|search
operator|.
name|OrganisationEntry
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
name|search
operator|.
name|RevisionEntry
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
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
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

begin_comment
comment|/**  * IBiblioResolver is a resolver which can be used to resolve dependencies found in the ibiblio  * maven repository, or similar repositories. For more flexibility with url and patterns, see  * {@link org.apache.ivy.plugins.resolver.URLResolver}.  */
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
comment|// use poms if m2 compatible is true
specifier|private
name|boolean
name|_usepoms
init|=
literal|true
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
operator|&&
name|isUsepoms
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
name|DefaultArtifact
operator|.
name|newPomArtifact
argument_list|(
name|mrid
argument_list|,
name|data
operator|.
name|getDate
argument_list|()
argument_list|)
argument_list|,
name|getRMDParser
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
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
if|if
condition|(
name|_root
operator|==
literal|null
condition|)
block|{
name|_root
operator|=
literal|"http://repo1.maven.org/maven2/"
expr_stmt|;
block|}
if|if
condition|(
name|_pattern
operator|==
literal|null
condition|)
block|{
name|_pattern
operator|=
literal|"[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"
expr_stmt|;
block|}
name|updateWholePattern
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|ensureConfigured
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|settings
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
name|settings
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
name|settings
operator|.
name|configureRepositories
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|_root
operator|=
name|settings
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
name|settings
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
name|settings
operator|.
name|configureRepositories
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|_pattern
operator|=
name|settings
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
name|getSettings
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
comment|/**      * Sets the root of the maven like repository. The maven like repository is necessarily an http      * repository.      *       * @param root      *            the root of the maven like repository      * @throws IllegalArgumentException      *             if root does not start with "http://"      */
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
name|getSettings
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
operator|&&
name|isUsepoms
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
comment|// we do not allow to list organisations on ibiblio, nor modules in ibiblio 1
specifier|public
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|token
parameter_list|,
name|Map
name|otherTokenValues
parameter_list|)
block|{
if|if
condition|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
if|if
condition|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
operator|.
name|equals
argument_list|(
name|token
argument_list|)
operator|&&
operator|!
name|isM2compatible
argument_list|()
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
name|ensureConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|listTokenValues
argument_list|(
name|token
argument_list|,
name|otherTokenValues
argument_list|)
return|;
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
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|ensureConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|listModules
argument_list|(
name|org
argument_list|)
return|;
block|}
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
name|mod
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|getSettings
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
name|getSettings
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
name|getSettings
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
name|DownloadOptions
name|options
parameter_list|)
block|{
name|ensureConfigured
argument_list|(
name|options
operator|.
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|download
argument_list|(
name|artifacts
argument_list|,
name|options
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
name|getSettings
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
name|getSettings
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
specifier|public
name|boolean
name|isUsepoms
parameter_list|()
block|{
return|return
name|_usepoms
return|;
block|}
specifier|public
name|void
name|setUsepoms
parameter_list|(
name|boolean
name|usepoms
parameter_list|)
block|{
name|_usepoms
operator|=
name|usepoms
expr_stmt|;
name|updateWholePattern
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|dumpSettings
parameter_list|()
block|{
name|ensureConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|dumpSettings
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\troot: "
operator|+
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tpattern: "
operator|+
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tusepoms: "
operator|+
name|_usepoms
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

