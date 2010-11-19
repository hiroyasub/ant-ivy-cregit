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
name|osgi
operator|.
name|ivy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|Configuration
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
name|DefaultModuleDescriptor
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
name|descriptor
operator|.
name|ExcludeRule
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
name|License
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
name|ModuleDescriptor
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
name|plugins
operator|.
name|parser
operator|.
name|ModuleDescriptorParser
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
name|parser
operator|.
name|ModuleDescriptorParserRegistry
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
name|parser
operator|.
name|ParserSettings
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
name|parser
operator|.
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|repository
operator|.
name|url
operator|.
name|URLResource
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

begin_class
specifier|public
class|class
name|OsgiIvyParser
extends|extends
name|XmlModuleDescriptorParser
block|{
specifier|public
specifier|static
class|class
name|OsgiParser
extends|extends
name|Parser
block|{
specifier|private
name|ModuleDescriptor
name|manifestMD
init|=
literal|null
decl_stmt|;
specifier|public
name|OsgiParser
parameter_list|(
name|ModuleDescriptorParser
name|parser
parameter_list|,
name|ParserSettings
name|ivySettings
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|ivySettings
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|infoStarted
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|manifest
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"manifest"
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|manifestMD
operator|=
name|parseManifest
argument_list|(
name|manifest
argument_list|)
expr_stmt|;
name|includeMdInfo
argument_list|(
name|getMd
argument_list|()
argument_list|,
name|manifestMD
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
comment|// it is caught in the startElement method
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return;
block|}
name|super
operator|.
name|infoStarted
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleDescriptor
name|parseManifest
parameter_list|(
name|String
name|manifest
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|getDescriptorURL
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"A reference to a manifest is only supported on module descriptors which are parsed from an URL"
argument_list|)
throw|;
block|}
name|URL
name|includedUrl
decl_stmt|;
try|try
block|{
name|includedUrl
operator|=
name|getSettings
argument_list|()
operator|.
name|getRelativeUrlResolver
argument_list|()
operator|.
name|getURL
argument_list|(
name|getDescriptorURL
argument_list|()
argument_list|,
name|manifest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|SAXException
name|pe
init|=
operator|new
name|SAXException
argument_list|(
literal|"Incorrect relative url of the include in '"
operator|+
name|getDescriptorURL
argument_list|()
operator|+
literal|"' ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
name|URLResource
name|includeResource
init|=
operator|new
name|URLResource
argument_list|(
name|includedUrl
argument_list|)
decl_stmt|;
name|ModuleDescriptorParser
name|parser
init|=
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|getParser
argument_list|(
name|includeResource
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|manifestMd
decl_stmt|;
try|try
block|{
name|manifestMd
operator|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|includeResource
operator|.
name|getURL
argument_list|()
argument_list|,
name|includeResource
argument_list|,
name|isValidate
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|SAXException
name|pe
init|=
operator|new
name|SAXException
argument_list|(
literal|"Incorrect included md '"
operator|+
name|includeResource
operator|+
literal|"' in '"
operator|+
name|getDescriptorURL
argument_list|()
operator|+
literal|"' ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|SAXException
name|pe
init|=
operator|new
name|SAXException
argument_list|(
literal|"Unreadable included md '"
operator|+
name|includeResource
operator|+
literal|"' in '"
operator|+
name|getDescriptorURL
argument_list|()
operator|+
literal|"' ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
return|return
name|manifestMd
return|;
block|}
specifier|public
name|void
name|endDocument
parameter_list|()
throws|throws
name|SAXException
block|{
if|if
condition|(
name|manifestMD
operator|!=
literal|null
condition|)
block|{
name|includeMdDepedencies
argument_list|(
name|getMd
argument_list|()
argument_list|,
name|manifestMD
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Parser
name|newParser
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|)
block|{
return|return
operator|new
name|OsgiParser
argument_list|(
name|this
argument_list|,
name|ivySettings
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|includeMdInfo
parameter_list|(
name|DefaultModuleDescriptor
name|md
parameter_list|,
name|ModuleDescriptor
name|include
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|include
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|setModuleRevisionId
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
name|ModuleRevisionId
name|resolvedMrid
init|=
name|include
operator|.
name|getResolvedModuleRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolvedMrid
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|setResolvedModuleRevisionId
argument_list|(
name|resolvedMrid
argument_list|)
expr_stmt|;
block|}
name|String
name|description
init|=
name|include
operator|.
name|getDescription
argument_list|()
decl_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
name|String
name|homePage
init|=
name|include
operator|.
name|getHomePage
argument_list|()
decl_stmt|;
if|if
condition|(
name|homePage
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|setHomePage
argument_list|(
name|homePage
argument_list|)
expr_stmt|;
block|}
name|long
name|lastModified
init|=
name|include
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastModified
operator|>
name|md
operator|.
name|getLastModified
argument_list|()
condition|)
block|{
name|md
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
block|}
name|String
name|status
init|=
name|include
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
name|md
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
name|Map
comment|/*<String, String> */
name|extraInfo
init|=
name|include
operator|.
name|getExtraInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|extraInfo
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|itInfo
init|=
name|extraInfo
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itInfo
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
comment|/*<String, String> */
name|info
init|=
operator|(
name|Entry
operator|)
name|itInfo
operator|.
name|next
argument_list|()
decl_stmt|;
name|md
operator|.
name|addExtraInfo
argument_list|(
operator|(
name|String
operator|)
name|info
operator|.
name|getKey
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|info
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|License
index|[]
name|licenses
init|=
name|include
operator|.
name|getLicenses
argument_list|()
decl_stmt|;
if|if
condition|(
name|licenses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|licenses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|md
operator|.
name|addLicense
argument_list|(
name|licenses
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|Configuration
index|[]
name|configurations
init|=
name|include
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|configurations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|configurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|md
operator|.
name|addConfiguration
argument_list|(
name|configurations
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|includeMdDepedencies
parameter_list|(
name|DefaultModuleDescriptor
name|md
parameter_list|,
name|ModuleDescriptor
name|include
parameter_list|)
block|{
name|Artifact
index|[]
name|artifacts
init|=
name|include
operator|.
name|getAllArtifacts
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifacts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
index|[]
name|artifactConfs
init|=
name|artifacts
index|[
name|i
index|]
operator|.
name|getConfigurations
argument_list|()
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
name|artifactConfs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|md
operator|.
name|addArtifact
argument_list|(
name|artifactConfs
index|[
name|j
index|]
argument_list|,
name|artifacts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|DependencyDescriptor
index|[]
name|dependencies
init|=
name|include
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependencies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dependencies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|md
operator|.
name|addDependency
argument_list|(
name|dependencies
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|ExcludeRule
index|[]
name|excludeRules
init|=
name|include
operator|.
name|getAllExcludeRules
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludeRules
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|excludeRules
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|md
operator|.
name|addExcludeRule
argument_list|(
name|excludeRules
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
