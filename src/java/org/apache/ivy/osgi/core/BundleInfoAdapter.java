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
name|core
package|;
end_package

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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
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
name|Ivy
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
name|Configuration
operator|.
name|Visibility
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
name|DefaultDependencyDescriptor
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
name|DefaultExcludeRule
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
name|ExtraInfoHolder
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
name|ArtifactId
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
name|ModuleId
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
name|osgi
operator|.
name|util
operator|.
name|Version
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
name|osgi
operator|.
name|util
operator|.
name|VersionRange
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
name|matcher
operator|.
name|ExactOrRegexpPatternMatcher
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
name|matcher
operator|.
name|PatternMatcher
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

begin_class
specifier|public
class|class
name|BundleInfoAdapter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONF_NAME_DEFAULT
init|=
literal|"default"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Configuration
name|CONF_DEFAULT
init|=
operator|new
name|Configuration
argument_list|(
name|CONF_NAME_DEFAULT
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONF_NAME_OPTIONAL
init|=
literal|"optional"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Configuration
name|CONF_OPTIONAL
init|=
operator|new
name|Configuration
argument_list|(
name|CONF_NAME_OPTIONAL
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"Optional dependencies"
argument_list|,
operator|new
name|String
index|[]
block|{
name|CONF_NAME_DEFAULT
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONF_NAME_TRANSITIVE_OPTIONAL
init|=
literal|"transitive-optional"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Configuration
name|CONF_TRANSITIVE_OPTIONAL
init|=
operator|new
name|Configuration
argument_list|(
name|CONF_NAME_TRANSITIVE_OPTIONAL
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"Optional dependencies"
argument_list|,
operator|new
name|String
index|[]
block|{
name|CONF_NAME_OPTIONAL
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONF_USE_PREFIX
init|=
literal|"use_"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EXTRA_INFO_EXPORT_PREFIX
init|=
literal|"_osgi_export_"
decl_stmt|;
specifier|public
specifier|static
name|DefaultModuleDescriptor
name|toModuleDescriptor
parameter_list|(
name|ModuleDescriptorParser
name|parser
parameter_list|,
name|URI
name|baseUri
parameter_list|,
name|BundleInfo
name|bundle
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
block|{
return|return
name|toModuleDescriptor
argument_list|(
name|parser
argument_list|,
name|baseUri
argument_list|,
name|bundle
argument_list|,
literal|null
argument_list|,
name|profileProvider
argument_list|)
return|;
block|}
comment|/**      *       * @param baseUri      *            uri to help build the absolute url if the bundle info has a relative uri.      * @return DefaultModuleDescriptor ditto      * @throws ProfileNotFoundException      */
specifier|public
specifier|static
name|DefaultModuleDescriptor
name|toModuleDescriptor
parameter_list|(
name|ModuleDescriptorParser
name|parser
parameter_list|,
name|URI
name|baseUri
parameter_list|,
name|BundleInfo
name|bundle
parameter_list|,
name|Manifest
name|manifest
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
throws|throws
name|ProfileNotFoundException
block|{
name|DefaultModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|parser
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|md
operator|.
name|addExtraAttributeNamespace
argument_list|(
literal|"o"
argument_list|,
name|Ivy
operator|.
name|getIvyHomeURL
argument_list|()
operator|+
literal|"osgi"
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|asMrid
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|md
operator|.
name|setResolvedPublicationDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|md
operator|.
name|setModuleRevisionId
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
name|CONF_DEFAULT
argument_list|)
expr_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
name|CONF_OPTIONAL
argument_list|)
expr_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
name|CONF_TRANSITIVE_OPTIONAL
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|exportedPkgNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|bundle
operator|.
name|getExports
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExportPackage
name|exportPackage
range|:
name|bundle
operator|.
name|getExports
argument_list|()
control|)
block|{
name|md
operator|.
name|getExtraInfos
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ExtraInfoHolder
argument_list|(
name|EXTRA_INFO_EXPORT_PREFIX
operator|+
name|exportPackage
operator|.
name|getName
argument_list|()
argument_list|,
name|exportPackage
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|exportedPkgNames
operator|.
name|add
argument_list|(
name|exportPackage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|confDependencies
init|=
operator|new
name|String
index|[
name|exportPackage
operator|.
name|getUses
argument_list|()
operator|.
name|size
argument_list|()
operator|+
literal|1
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|use
range|:
name|exportPackage
operator|.
name|getUses
argument_list|()
control|)
block|{
name|confDependencies
index|[
name|i
operator|++
index|]
operator|=
name|CONF_USE_PREFIX
operator|+
name|use
expr_stmt|;
block|}
name|confDependencies
index|[
name|i
index|]
operator|=
name|CONF_NAME_DEFAULT
expr_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
operator|new
name|Configuration
argument_list|(
name|CONF_USE_PREFIX
operator|+
name|exportPackage
operator|.
name|getName
argument_list|()
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"Exported package "
operator|+
name|exportPackage
operator|.
name|getName
argument_list|()
argument_list|,
name|confDependencies
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|requirementAsDependency
argument_list|(
name|md
argument_list|,
name|bundle
argument_list|,
name|exportedPkgNames
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseUri
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|BundleArtifact
name|bundleArtifact
range|:
name|bundle
operator|.
name|getArtifacts
argument_list|()
control|)
block|{
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|ext
init|=
literal|"jar"
decl_stmt|;
name|String
name|packaging
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bundle
operator|.
name|hasInnerClasspath
argument_list|()
operator|&&
operator|!
name|bundleArtifact
operator|.
name|isSource
argument_list|()
condition|)
block|{
name|packaging
operator|=
literal|"bundle"
expr_stmt|;
block|}
if|if
condition|(
literal|"packed"
operator|.
name|equals
argument_list|(
name|bundleArtifact
operator|.
name|getFormat
argument_list|()
argument_list|)
condition|)
block|{
name|ext
operator|=
literal|"jar.pack.gz"
expr_stmt|;
if|if
condition|(
name|packaging
operator|!=
literal|null
condition|)
block|{
name|packaging
operator|+=
literal|",pack200"
expr_stmt|;
block|}
else|else
block|{
name|packaging
operator|=
literal|"pack200"
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bundleArtifact
operator|.
name|isSource
argument_list|()
condition|)
block|{
name|type
operator|=
literal|"source"
expr_stmt|;
block|}
name|URI
name|uri
init|=
name|bundleArtifact
operator|.
name|getUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|DefaultArtifact
name|artifact
init|=
name|buildArtifact
argument_list|(
name|mrid
argument_list|,
name|baseUri
argument_list|,
name|uri
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|packaging
argument_list|)
decl_stmt|;
name|md
operator|.
name|addArtifact
argument_list|(
name|CONF_NAME_DEFAULT
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|profileProvider
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|env
range|:
name|bundle
operator|.
name|getExecutionEnvironments
argument_list|()
control|)
block|{
name|ExecutionEnvironmentProfile
name|profile
init|=
name|profileProvider
operator|.
name|getProfile
argument_list|(
name|env
argument_list|)
decl_stmt|;
if|if
condition|(
name|profile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ProfileNotFoundException
argument_list|(
literal|"Execution environment profile "
operator|+
name|env
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|pkg
range|:
name|profile
operator|.
name|getPkgNames
argument_list|()
control|)
block|{
name|ArtifactId
name|id
init|=
operator|new
name|ArtifactId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
argument_list|,
name|pkg
argument_list|)
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|)
decl_stmt|;
name|DefaultExcludeRule
name|rule
init|=
operator|new
name|DefaultExcludeRule
argument_list|(
name|id
argument_list|,
name|ExactOrRegexpPatternMatcher
operator|.
name|INSTANCE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|md
operator|.
name|getConfigurationsNames
argument_list|()
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
name|rule
operator|.
name|addConfiguration
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|md
operator|.
name|addExcludeRule
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entries
range|:
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|md
operator|.
name|addExtraInfo
argument_list|(
operator|new
name|ExtraInfoHolder
argument_list|(
name|entries
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entries
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|md
return|;
block|}
specifier|public
specifier|static
name|DefaultArtifact
name|buildArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|URI
name|baseUri
parameter_list|,
name|URI
name|uri
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|packaging
parameter_list|)
block|{
name|DefaultArtifact
name|artifact
decl_stmt|;
if|if
condition|(
literal|"ivy"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|artifact
operator|=
name|decodeIvyURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|uri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|uri
operator|=
name|baseUri
operator|.
name|resolve
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAtt
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|packaging
operator|!=
literal|null
condition|)
block|{
name|extraAtt
operator|.
name|put
argument_list|(
literal|"packaging"
argument_list|,
name|packaging
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|artifact
operator|=
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
literal|null
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
operator|new
name|URL
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|extraAtt
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to make the uri into the url"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|artifact
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getConfigurations
parameter_list|(
name|BundleInfo
name|bundle
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|confs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|confs
operator|.
name|add
argument_list|(
name|CONF_NAME_DEFAULT
argument_list|)
expr_stmt|;
name|confs
operator|.
name|add
argument_list|(
name|CONF_NAME_OPTIONAL
argument_list|)
expr_stmt|;
name|confs
operator|.
name|add
argument_list|(
name|CONF_NAME_TRANSITIVE_OPTIONAL
argument_list|)
expr_stmt|;
for|for
control|(
name|ExportPackage
name|exportPackage
range|:
name|bundle
operator|.
name|getExports
argument_list|()
control|)
block|{
name|confs
operator|.
name|add
argument_list|(
name|CONF_USE_PREFIX
operator|+
name|exportPackage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|confs
return|;
block|}
specifier|public
specifier|static
name|URI
name|buildIvyURI
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
return|return
name|asIvyURI
argument_list|(
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|URI
name|asIvyURI
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|rev
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|art
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
name|StringBuffer
name|builder
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"ivy:///"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|org
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
if|if
condition|(
name|branch
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"branch="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|branch
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rev
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"&rev="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|rev
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"&type="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|art
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"&art="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|art
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ext
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"&ext="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|ext
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"ill-formed ivy url"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|DefaultArtifact
name|decodeIvyURI
parameter_list|(
specifier|final
name|URI
name|uri
parameter_list|)
block|{
name|String
name|org
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|branch
init|=
literal|null
decl_stmt|;
name|String
name|rev
init|=
literal|null
decl_stmt|;
name|String
name|art
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|null
decl_stmt|;
name|String
name|ext
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"An ivy url should be of the form ivy:///org/module but was : "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|int
name|i
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expecting an organisation in the ivy url: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|org
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|name
operator|=
name|path
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|query
init|=
name|uri
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|String
index|[]
name|parameters
init|=
name|query
operator|.
name|split
argument_list|(
literal|"&"
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
name|parameters
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|String
name|parameter
init|=
name|parameters
index|[
name|j
index|]
decl_stmt|;
if|if
condition|(
name|parameter
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|String
index|[]
name|nameAndValue
init|=
name|parameter
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameAndValue
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Malformed query string in the ivy url: "
operator|+
name|uri
argument_list|)
throw|;
block|}
if|else if
condition|(
name|nameAndValue
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"branch"
argument_list|)
condition|)
block|{
name|branch
operator|=
name|nameAndValue
index|[
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|nameAndValue
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"rev"
argument_list|)
condition|)
block|{
name|rev
operator|=
name|nameAndValue
index|[
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|nameAndValue
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"art"
argument_list|)
condition|)
block|{
name|art
operator|=
name|nameAndValue
index|[
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|nameAndValue
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"type"
argument_list|)
condition|)
block|{
name|type
operator|=
name|nameAndValue
index|[
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|nameAndValue
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
literal|"ext"
argument_list|)
condition|)
block|{
name|ext
operator|=
name|nameAndValue
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized parameter '"
operator|+
name|nameAndValue
index|[
literal|0
index|]
operator|+
literal|" in the query string of the ivy url: "
operator|+
name|uri
argument_list|)
throw|;
block|}
block|}
name|ModuleRevisionId
name|amrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|name
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|amrid
argument_list|,
literal|null
argument_list|,
name|art
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|requirementAsDependency
parameter_list|(
name|DefaultModuleDescriptor
name|md
parameter_list|,
name|BundleInfo
name|bundleInfo
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|exportedPkgNames
parameter_list|)
block|{
for|for
control|(
name|BundleRequirement
name|requirement
range|:
name|bundleInfo
operator|.
name|getRequirements
argument_list|()
control|)
block|{
name|String
name|type
init|=
name|requirement
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|requirement
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
operator|&&
name|exportedPkgNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// don't declare package exported by the current bundle
continue|continue;
block|}
if|if
condition|(
name|BundleInfo
operator|.
name|EXECUTION_ENVIRONMENT_TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// execution environment are handled elsewhere
continue|continue;
block|}
name|ModuleRevisionId
name|ddmrid
init|=
name|asMrid
argument_list|(
name|type
argument_list|,
name|name
argument_list|,
name|requirement
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ddmrid
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|conf
init|=
name|CONF_NAME_DEFAULT
decl_stmt|;
if|if
condition|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// declare the configuration for the package
name|conf
operator|=
name|CONF_USE_PREFIX
operator|+
name|name
expr_stmt|;
name|md
operator|.
name|addConfiguration
argument_list|(
operator|new
name|Configuration
argument_list|(
name|CONF_USE_PREFIX
operator|+
name|name
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"Exported package "
operator|+
name|name
argument_list|,
operator|new
name|String
index|[]
block|{
name|CONF_NAME_DEFAULT
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|conf
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"optional"
operator|.
name|equals
argument_list|(
name|requirement
operator|.
name|getResolution
argument_list|()
argument_list|)
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|CONF_NAME_OPTIONAL
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|CONF_NAME_TRANSITIVE_OPTIONAL
argument_list|,
name|CONF_NAME_TRANSITIVE_OPTIONAL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|CONF_NAME_DEFAULT
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|asMrid
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|name
parameter_list|,
name|Version
name|v
parameter_list|)
block|{
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|name
argument_list|,
name|v
operator|==
literal|null
condition|?
literal|null
else|:
name|v
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|asMrid
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|name
parameter_list|,
name|VersionRange
name|v
parameter_list|)
block|{
name|String
name|revision
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|revision
operator|=
literal|"[0,)"
expr_stmt|;
block|}
else|else
block|{
name|revision
operator|=
name|v
operator|.
name|toIvyRevision
argument_list|()
expr_stmt|;
block|}
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|name
argument_list|,
name|revision
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
specifier|public
specifier|static
class|class
name|ProfileNotFoundException
extends|extends
name|RuntimeException
block|{
specifier|public
name|ProfileNotFoundException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

