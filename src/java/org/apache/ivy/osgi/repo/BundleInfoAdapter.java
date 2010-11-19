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
name|repo
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
name|URL
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
name|HashSet
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
name|Set
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
name|core
operator|.
name|BundleInfo
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
name|core
operator|.
name|BundleRequirement
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
name|core
operator|.
name|ExportPackage
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
name|repo
operator|.
name|osgi
operator|.
name|ExecutionEnvironmentProfileProvider
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
name|StringUtils
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
name|EXTRA_ATTRIBUTE_NAME
init|=
literal|"osgi"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Map
comment|/*<String, String> */
name|OSGI_BUNDLE
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|EXTRA_ATTRIBUTE_NAME
argument_list|,
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Map
comment|/*<String, String> */
name|OSGI_PACKAGE
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|EXTRA_ATTRIBUTE_NAME
argument_list|,
name|BundleInfo
operator|.
name|PACKAGE_TYPE
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Map
comment|/*<String, String> */
name|OSGI_SERVICE
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|EXTRA_ATTRIBUTE_NAME
argument_list|,
name|BundleInfo
operator|.
name|SERVICE_TYPE
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|DefaultModuleDescriptor
name|toModuleDescriptor
parameter_list|(
name|BundleInfo
name|bundle
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
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|asMrid
argument_list|(
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundle
operator|.
name|getVersion
argument_list|()
argument_list|,
name|OSGI_BUNDLE
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
comment|/*<String> */
name|exportedPkgNames
init|=
operator|new
name|HashSet
comment|/*<String> */
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
name|Iterator
name|itExport
init|=
name|bundle
operator|.
name|getExports
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itExport
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExportPackage
name|exportPackage
init|=
operator|(
name|ExportPackage
operator|)
name|itExport
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|Iterator
name|itUse
init|=
name|exportPackage
operator|.
name|getUses
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itUse
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|use
init|=
operator|(
name|String
operator|)
name|itUse
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|bundle
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DefaultArtifact
name|artifact
init|=
literal|null
decl_stmt|;
name|String
name|uri
init|=
name|bundle
operator|.
name|getUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"ivy://"
argument_list|)
condition|)
block|{
name|artifact
operator|=
name|decodeIvyLocation
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"file:"
operator|+
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"BUG IN BUSHEL, please report: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"\n"
operator|+
name|StringUtils
operator|.
name|getStackTrace
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
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
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
name|url
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
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
if|if
condition|(
name|profileProvider
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|itEnv
init|=
name|bundle
operator|.
name|getExecutionEnvironments
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itEnv
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|env
init|=
operator|(
name|String
operator|)
name|itEnv
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|Iterator
name|itPkg
init|=
name|profile
operator|.
name|getPkgNames
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itPkg
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|pkg
init|=
operator|(
name|String
operator|)
name|itPkg
operator|.
name|next
argument_list|()
decl_stmt|;
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
literal|""
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
name|OSGI_PACKAGE
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
return|return
name|md
return|;
block|}
specifier|public
specifier|static
name|String
name|encodeIvyLocation
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
name|encodeIvyLocation
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
name|String
name|encodeIvyLocation
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
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"ivy://"
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
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|DefaultArtifact
name|decodeIvyLocation
parameter_list|(
name|String
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
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'/'
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
literal|"Expecting an organisation in the ivy uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|org
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|i
operator|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
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
literal|"Expecting an module name in the ivy uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|name
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
index|[]
name|parameters
init|=
name|uri
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
literal|"Malformed query string in the ivy uri: "
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
literal|" in the query string of the ivy uri: "
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
name|DefaultArtifact
name|artifact
init|=
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
decl_stmt|;
return|return
name|artifact
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
comment|/*<String> */
name|exportedPkgNames
parameter_list|)
block|{
name|Iterator
name|itReq
init|=
name|bundleInfo
operator|.
name|getRequirements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itReq
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|BundleRequirement
name|requirement
init|=
operator|(
name|BundleRequirement
operator|)
name|itReq
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|type
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
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
name|Map
comment|/*<String, String> */
name|osgiAtt
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|EXTRA_ATTRIBUTE_NAME
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|ddmrid
init|=
name|asMrid
argument_list|(
name|name
argument_list|,
name|requirement
operator|.
name|getVersion
argument_list|()
argument_list|,
name|osgiAtt
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
name|type
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
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
specifier|private
specifier|static
name|ModuleRevisionId
name|asMrid
parameter_list|(
name|String
name|symbolicNAme
parameter_list|,
name|Version
name|v
parameter_list|,
name|Map
comment|/*<String, String> */
name|extraAttr
parameter_list|)
block|{
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
name|symbolicNAme
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
argument_list|,
name|extraAttr
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ModuleRevisionId
name|asMrid
parameter_list|(
name|String
name|symbolicNAme
parameter_list|,
name|VersionRange
name|v
parameter_list|,
name|Map
comment|/*                                                                                     *<String,                                                                                     * String>                                                                                     */
name|extraAttr
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
literal|""
argument_list|,
name|symbolicNAme
argument_list|,
name|revision
argument_list|,
name|extraAttr
argument_list|)
return|;
block|}
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
