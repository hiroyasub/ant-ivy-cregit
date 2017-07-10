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
name|updatesite
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
name|cache
operator|.
name|CacheResourceOptions
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
name|DownloadListener
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
name|RepositoryCacheManager
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|AbstractOSGiResolver
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
name|UpdateSiteResolver
extends|extends
name|AbstractOSGiResolver
block|{
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|Long
name|metadataTtl
decl_stmt|;
specifier|private
name|Boolean
name|forceMetadataUpdate
decl_stmt|;
specifier|private
name|String
name|logLevel
decl_stmt|;
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|setMetadataTtl
parameter_list|(
name|Long
name|metadataTtl
parameter_list|)
block|{
name|this
operator|.
name|metadataTtl
operator|=
name|metadataTtl
expr_stmt|;
block|}
specifier|public
name|void
name|setForceMetadataUpdate
parameter_list|(
name|Boolean
name|forceMetadataUpdate
parameter_list|)
block|{
name|this
operator|.
name|forceMetadataUpdate
operator|=
name|forceMetadataUpdate
expr_stmt|;
block|}
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|logLevel
expr_stmt|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing url"
argument_list|)
throw|;
block|}
name|CacheResourceOptions
name|options
init|=
operator|new
name|CacheResourceOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadataTtl
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setTtl
argument_list|(
name|metadataTtl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|forceMetadataUpdate
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setForce
argument_list|(
name|forceMetadataUpdate
argument_list|)
expr_stmt|;
block|}
specifier|final
name|int
name|log
decl_stmt|;
if|if
condition|(
name|logLevel
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"debug"
operator|.
name|equalsIgnoreCase
argument_list|(
name|logLevel
argument_list|)
condition|)
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_DEBUG
expr_stmt|;
block|}
if|else if
condition|(
literal|"verbose"
operator|.
name|equalsIgnoreCase
argument_list|(
name|logLevel
argument_list|)
condition|)
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_VERBOSE
expr_stmt|;
block|}
if|else if
condition|(
literal|"info"
operator|.
name|equalsIgnoreCase
argument_list|(
name|logLevel
argument_list|)
condition|)
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_INFO
expr_stmt|;
block|}
if|else if
condition|(
literal|"warn"
operator|.
name|equalsIgnoreCase
argument_list|(
name|logLevel
argument_list|)
condition|)
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_WARN
expr_stmt|;
block|}
if|else if
condition|(
literal|"error"
operator|.
name|equalsIgnoreCase
argument_list|(
name|logLevel
argument_list|)
condition|)
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_ERR
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unknown log level: "
operator|+
name|logLevel
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|=
name|Message
operator|.
name|MSG_INFO
expr_stmt|;
block|}
name|options
operator|.
name|setListener
argument_list|(
operator|new
name|DownloadListener
argument_list|()
block|{
specifier|public
name|void
name|startArtifactDownload
parameter_list|(
name|RepositoryCacheManager
name|cache
parameter_list|,
name|ResolvedResource
name|rres
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|)
block|{
if|if
condition|(
name|log
operator|<=
name|Message
operator|.
name|MSG_INFO
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\tdownloading "
operator|+
name|rres
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|needArtifact
parameter_list|(
name|RepositoryCacheManager
name|cache
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
name|log
operator|<=
name|Message
operator|.
name|MSG_VERBOSE
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\ttrying to download "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|endArtifactDownload
parameter_list|(
name|RepositoryCacheManager
name|cache
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactDownloadReport
name|adr
parameter_list|,
name|File
name|archiveFile
parameter_list|)
block|{
if|if
condition|(
name|log
operator|<=
name|Message
operator|.
name|MSG_VERBOSE
condition|)
block|{
if|if
condition|(
name|adr
operator|.
name|isDownloaded
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tdownloaded to "
operator|+
name|archiveFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tnothing to download"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|UpdateSiteLoader
name|loader
init|=
operator|new
name|UpdateSiteLoader
argument_list|(
name|getRepositoryCacheManager
argument_list|()
argument_list|,
name|getEventManager
argument_list|()
argument_list|,
name|options
argument_list|,
name|this
operator|.
name|getTimeoutConstraint
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|setLogLevel
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|RepoDescriptor
name|repoDescriptor
decl_stmt|;
try|try
block|{
name|repoDescriptor
operator|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"IO issue while trying to read the update site ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to parse the updatesite ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Ill-formed updatesite ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
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
literal|"Ill-formed url ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|repoDescriptor
operator|==
literal|null
condition|)
block|{
name|setRepoDescriptor
argument_list|(
name|FAILING_REPO_DESCRIPTOR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No update site was found at the location: "
operator|+
name|url
argument_list|)
throw|;
block|}
name|setRepoDescriptor
argument_list|(
name|repoDescriptor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

