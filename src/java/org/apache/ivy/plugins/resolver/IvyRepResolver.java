begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|ArrayList
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
name|HashMap
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
name|cache
operator|.
name|ArtifactOrigin
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
name|XMLHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * IvyRepResolver is a resolver which can be used to resolve dependencies found in the ivy official  * repository for ivy files and ibiblio maven repository for the artifacts, or similar repositories.  * For more flexibility with url and patterns, see  * {@link org.apache.ivy.plugins.resolver.URLResolver}.  */
end_comment

begin_class
specifier|public
class|class
name|IvyRepResolver
extends|extends
name|URLResolver
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_IVYPATTERN
init|=
literal|"[organisation]/[module]/ivy-[revision].xml"
decl_stmt|;
specifier|private
name|String
name|ivyroot
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|ivypattern
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|artroot
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|artpattern
init|=
literal|null
decl_stmt|;
specifier|public
name|IvyRepResolver
parameter_list|()
block|{
block|}
specifier|private
name|void
name|ensureArtifactConfigured
parameter_list|(
name|ResolverSettings
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
name|artroot
operator|==
literal|null
operator|||
name|artpattern
operator|==
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|artroot
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
literal|"ivy.ivyrep.default.artifact.root"
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|artroot
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
name|artroot
operator|=
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy.ivyrep.default.artifact.root"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|artpattern
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
literal|"ivy.ivyrep.default.artifact.pattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|artpattern
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
name|artpattern
operator|=
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy.ivyrep.default.artifact.pattern"
argument_list|)
expr_stmt|;
block|}
block|}
name|updateWholeArtPattern
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|ensureIvyConfigured
parameter_list|(
name|ResolverSettings
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
name|ivyroot
operator|==
literal|null
operator|||
name|ivypattern
operator|==
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|ivyroot
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
literal|"ivy.ivyrep.default.ivy.root"
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|ivyroot
operator|=
name|root
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ivyroot is mandatory on IvyRepResolver. "
operator|+
literal|"Make sure to set it in your settings, before setting ivypattern "
operator|+
literal|"if you wish to set ivypattern too."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|ivypattern
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
literal|"ivy.ivyrep.default.ivy.pattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|ivypattern
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
name|ivypattern
operator|=
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy.ivyrep.default.ivy.pattern"
argument_list|)
expr_stmt|;
block|}
block|}
name|updateWholeIvyPattern
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getWholeIvyPattern
parameter_list|()
block|{
if|if
condition|(
name|ivyroot
operator|==
literal|null
operator|||
name|ivypattern
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|ivyroot
operator|+
name|ivypattern
return|;
block|}
specifier|private
name|String
name|getWholeArtPattern
parameter_list|()
block|{
return|return
name|artroot
operator|+
name|artpattern
return|;
block|}
specifier|public
name|String
name|getIvypattern
parameter_list|()
block|{
return|return
name|ivypattern
return|;
block|}
specifier|public
name|void
name|setIvypattern
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
name|ivypattern
operator|=
name|pattern
expr_stmt|;
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholeIvyPattern
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getIvyroot
parameter_list|()
block|{
return|return
name|ivyroot
return|;
block|}
comment|/**      * Sets the root of the maven like repository. The maven like repository is necessarily an http      * repository.      *      * @param root      *            the root of the maven like repository      * @throws IllegalArgumentException      *             if root does not start with "http://"      */
specifier|public
name|void
name|setIvyroot
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
name|ivyroot
operator|=
name|root
operator|+
literal|"/"
expr_stmt|;
block|}
else|else
block|{
name|ivyroot
operator|=
name|root
expr_stmt|;
block|}
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholeIvyPattern
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setM2compatible
parameter_list|(
name|boolean
name|m2compatible
parameter_list|)
block|{
if|if
condition|(
name|m2compatible
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ivyrep does not support maven2 compatibility. "
operator|+
literal|"Please use ibiblio resolver instead, or even url or filesystem resolvers for"
operator|+
literal|" more specific needs."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|updateWholeIvyPattern
parameter_list|()
block|{
name|setIvyPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getWholeIvyPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateWholeArtPattern
parameter_list|()
block|{
name|setArtifactPatterns
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getWholeArtPattern
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
name|String
name|getArtroot
parameter_list|()
block|{
return|return
name|artroot
return|;
block|}
specifier|public
name|String
name|getArtpattern
parameter_list|()
block|{
return|return
name|artpattern
return|;
block|}
specifier|public
name|void
name|setArtpattern
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
name|artpattern
operator|=
name|pattern
expr_stmt|;
name|ensureArtifactConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholeArtPattern
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setArtroot
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
name|artroot
operator|=
name|root
operator|+
literal|"/"
expr_stmt|;
block|}
else|else
block|{
name|artroot
operator|=
name|root
expr_stmt|;
block|}
name|ensureArtifactConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|updateWholeArtPattern
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
block|{
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|URL
name|content
init|=
operator|new
name|URL
argument_list|(
name|ivyroot
operator|+
literal|"content.xml"
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|OrganisationEntry
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|XMLHelper
operator|.
name|parse
argument_list|(
name|content
argument_list|,
literal|null
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
literal|"organisation"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|org
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|org
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|OrganisationEntry
argument_list|(
name|IvyRepResolver
operator|.
name|this
argument_list|,
name|org
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|OrganisationEntry
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
comment|// ???
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"unable to parse content.xml file on ivyrep"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|listOrganisations
argument_list|()
return|;
block|}
comment|// overwrite parent to use only ivy patterns (and not artifact ones, cause ibiblio is too slow
comment|// to answer)
annotation|@
name|Override
specifier|public
name|ModuleEntry
index|[]
name|listModules
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|org
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|names
init|=
name|findIvyNames
argument_list|(
name|tokenValues
argument_list|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ModuleEntry
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|names
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|ModuleEntry
argument_list|(
name|org
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleEntry
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|mod
parameter_list|)
block|{
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|ensureArtifactConfigured
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
annotation|@
name|Override
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"ivyrep"
return|;
block|}
comment|// override some methods to ensure configuration
annotation|@
name|Override
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
name|ensureIvyConfigured
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
annotation|@
name|Override
specifier|public
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
name|ensureArtifactConfigured
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
annotation|@
name|Override
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
name|ensureArtifactConfigured
argument_list|(
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
annotation|@
name|Override
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ensureArtifactConfigured
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
annotation|@
name|Override
specifier|public
name|ArtifactOrigin
name|locate
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ensureArtifactConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|locate
argument_list|(
name|artifact
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIvyPatterns
parameter_list|()
block|{
name|ensureIvyConfigured
argument_list|(
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getIvyPatterns
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getArtifactPatterns
parameter_list|()
block|{
name|ensureArtifactConfigured
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
block|}
end_class

end_unit

