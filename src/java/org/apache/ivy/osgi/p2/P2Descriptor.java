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
name|p2
package|;
end_package

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
name|BundleInfoAdapter
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
name|repo
operator|.
name|RepoDescriptor
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
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|P2Descriptor
extends|extends
name|RepoDescriptor
block|{
specifier|private
name|long
name|timestamp
decl_stmt|;
specifier|private
name|Map
comment|/*<String, Map<Version, String>> */
name|artifactUrlPatterns
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
comment|/*<String, Map<String, URI>> */
name|sourceURIs
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|P2Descriptor
parameter_list|(
name|URI
name|repoUri
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
block|{
name|super
argument_list|(
name|repoUri
argument_list|,
name|profileProvider
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
specifier|public
name|void
name|addBundle
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
if|if
condition|(
name|bundleInfo
operator|.
name|isSource
argument_list|()
condition|)
block|{
if|if
condition|(
name|bundleInfo
operator|.
name|getSymbolicNameTarget
argument_list|()
operator|==
literal|null
operator|||
name|bundleInfo
operator|.
name|getVersionTarget
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"The source bundle "
operator|+
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" did declare its target. Ignoring it"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Map
comment|/*<String, URI>*/
name|byVersion
init|=
operator|(
name|Map
operator|)
name|sourceURIs
operator|.
name|get
argument_list|(
name|bundleInfo
operator|.
name|getSymbolicNameTarget
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|byVersion
operator|==
literal|null
condition|)
block|{
name|byVersion
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|sourceURIs
operator|.
name|put
argument_list|(
name|bundleInfo
operator|.
name|getSymbolicNameTarget
argument_list|()
argument_list|,
name|byVersion
argument_list|)
expr_stmt|;
block|}
name|URI
name|sourceUri
init|=
name|getArtifactURI
argument_list|(
name|bundleInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceUri
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"The source bundle "
operator|+
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" has no actual artifact. Ignoring it"
argument_list|)
expr_stmt|;
return|return;
block|}
name|URI
name|old
init|=
operator|(
name|URI
operator|)
name|byVersion
operator|.
name|put
argument_list|(
name|bundleInfo
operator|.
name|getVersionTarget
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|sourceUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
operator|!
name|old
operator|.
name|equals
argument_list|(
name|sourceUri
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Duplicate source for the bundle "
operator|+
name|bundleInfo
operator|.
name|getSymbolicNameTarget
argument_list|()
operator|+
literal|"@"
operator|+
name|bundleInfo
operator|.
name|getVersionTarget
argument_list|()
operator|+
literal|" : "
operator|+
name|sourceUri
operator|+
literal|" is replacing "
operator|+
name|old
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|// before transforming it and adding it into the repo, let's add the artifacts
comment|// and if no artifact, then no bundle
name|bundleInfo
operator|.
name|setUri
argument_list|(
name|getArtifactURI
argument_list|(
name|bundleInfo
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|addBundle
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
specifier|private
name|URI
name|getArtifactURI
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
name|Map
comment|/*<Version, String> */
name|urlPatternsByVersion
init|=
operator|(
name|Map
operator|)
name|artifactUrlPatterns
operator|.
name|get
argument_list|(
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|urlPatternsByVersion
operator|!=
literal|null
condition|)
block|{
name|String
name|urlPattern
init|=
operator|(
name|String
operator|)
name|urlPatternsByVersion
operator|.
name|get
argument_list|(
name|bundleInfo
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|urlPattern
operator|!=
literal|null
condition|)
block|{
name|String
name|url
init|=
name|urlPattern
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{id\\}"
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
decl_stmt|;
name|url
operator|=
name|url
operator|.
name|replaceAll
argument_list|(
literal|"\\$\\{version\\}"
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|url
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
literal|"Unable to build the artifact uri of "
operator|+
name|bundleInfo
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addArtifactUrl
parameter_list|(
name|String
name|classifier
parameter_list|,
name|String
name|id
parameter_list|,
name|Version
name|version
parameter_list|,
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
operator|!
name|classifier
operator|.
name|equals
argument_list|(
literal|"osgi.bundle"
argument_list|)
condition|)
block|{
comment|// we only support OSGi bundle, no Eclipse feature or anything else
return|return;
block|}
name|Map
comment|/*<Version, String> */
name|byVersion
init|=
operator|(
name|Map
operator|)
name|artifactUrlPatterns
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|byVersion
operator|==
literal|null
condition|)
block|{
name|byVersion
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|artifactUrlPatterns
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|byVersion
argument_list|)
expr_stmt|;
block|}
name|byVersion
operator|.
name|put
argument_list|(
name|version
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|finish
parameter_list|()
block|{
name|artifactUrlPatterns
operator|=
literal|null
expr_stmt|;
name|Iterator
name|itModules
init|=
name|getModules
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itModules
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DefaultModuleDescriptor
name|md
init|=
operator|(
name|DefaultModuleDescriptor
operator|)
name|itModules
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|org
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|org
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|symbolicName
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Map
comment|/*<String, URI>*/
name|byVersion
init|=
operator|(
name|Map
operator|)
name|sourceURIs
operator|.
name|get
argument_list|(
name|symbolicName
argument_list|)
decl_stmt|;
if|if
condition|(
name|byVersion
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|rev
init|=
name|md
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|URI
name|source
init|=
operator|(
name|URI
operator|)
name|byVersion
operator|.
name|get
argument_list|(
name|rev
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|DefaultArtifact
name|sourceArtifact
init|=
name|BundleInfoAdapter
operator|.
name|buildArtifact
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|getBaseUri
argument_list|()
argument_list|,
name|source
argument_list|,
literal|"source"
argument_list|)
decl_stmt|;
name|md
operator|.
name|addArtifact
argument_list|(
name|BundleInfoAdapter
operator|.
name|CONF_NAME_DEFAULT
argument_list|,
name|sourceArtifact
argument_list|)
expr_stmt|;
block|}
name|sourceURIs
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

