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
name|report
operator|.
name|DownloadStatus
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
name|resolver
operator|.
name|AbstractWorkspaceResolver
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
name|types
operator|.
name|DataType
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
name|types
operator|.
name|Resource
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
name|types
operator|.
name|ResourceCollection
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
name|types
operator|.
name|resources
operator|.
name|FileResource
import|;
end_import

begin_class
specifier|public
class|class
name|AntWorkspaceResolver
extends|extends
name|DataType
block|{
specifier|public
specifier|static
specifier|final
class|class
name|WorkspaceArtifact
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|ext
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|void
name|setExt
parameter_list|(
name|String
name|ext
parameter_list|)
block|{
name|this
operator|.
name|ext
operator|=
name|ext
expr_stmt|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|ResourceCollection
argument_list|>
name|allResources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|haltOnError
init|=
literal|true
decl_stmt|;
specifier|private
name|Resolver
name|resolver
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WorkspaceArtifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setHaltonerror
parameter_list|(
name|boolean
name|haltOnError
parameter_list|)
block|{
name|this
operator|.
name|haltOnError
operator|=
name|haltOnError
expr_stmt|;
block|}
specifier|public
name|void
name|addConfigured
parameter_list|(
name|ResourceCollection
name|resources
parameter_list|)
block|{
if|if
condition|(
operator|!
name|resources
operator|.
name|isFilesystemOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Only filesystem resource collection is supported"
argument_list|)
throw|;
block|}
name|allResources
operator|.
name|add
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WorkspaceArtifact
name|createArtifact
parameter_list|()
block|{
name|WorkspaceArtifact
name|a
init|=
operator|new
name|WorkspaceArtifact
argument_list|()
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
return|return
name|a
return|;
block|}
specifier|public
name|Resolver
name|getResolver
parameter_list|()
block|{
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"A name is required"
argument_list|)
throw|;
block|}
name|resolver
operator|=
operator|new
name|Resolver
argument_list|()
expr_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|resolver
return|;
block|}
specifier|private
name|String
name|getProjectName
parameter_list|(
name|File
name|ivyFile
parameter_list|)
block|{
return|return
name|ivyFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
class|class
name|Resolver
extends|extends
name|AbstractWorkspaceResolver
block|{
specifier|private
name|Map
argument_list|<
name|ModuleDescriptor
argument_list|,
name|File
argument_list|>
name|md2IvyFile
decl_stmt|;
specifier|private
specifier|synchronized
name|Map
argument_list|<
name|ModuleDescriptor
argument_list|,
name|File
argument_list|>
name|getModuleDescriptors
parameter_list|()
block|{
if|if
condition|(
name|md2IvyFile
operator|==
literal|null
condition|)
block|{
name|md2IvyFile
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|ResourceCollection
name|resources
range|:
name|allResources
control|)
block|{
for|for
control|(
name|Resource
name|resource
range|:
name|resources
control|)
block|{
name|File
name|ivyFile
init|=
operator|(
operator|(
name|FileResource
operator|)
name|resource
operator|)
operator|.
name|getFile
argument_list|()
decl_stmt|;
try|try
block|{
name|ModuleDescriptor
name|md
init|=
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|getParserSettings
argument_list|()
argument_list|,
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|isValidate
argument_list|()
argument_list|)
decl_stmt|;
name|md2IvyFile
operator|.
name|put
argument_list|(
name|md
argument_list|,
name|ivyFile
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"Add "
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|haltOnError
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to parse ivy file "
operator|+
name|ivyFile
operator|+
literal|" exception="
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
else|else
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"impossible to parse ivy file "
operator|+
name|ivyFile
operator|+
literal|" exception="
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|md2IvyFile
return|;
block|}
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ModuleDescriptor
argument_list|,
name|File
argument_list|>
name|md
range|:
name|getModuleDescriptors
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ResolvedModuleRevision
name|rmr
init|=
name|checkCandidate
argument_list|(
name|dd
argument_list|,
name|md
operator|.
name|getKey
argument_list|()
argument_list|,
name|getProjectName
argument_list|(
name|md
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmr
operator|!=
literal|null
condition|)
block|{
return|return
name|rmr
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Artifact
argument_list|>
name|createWorkspaceArtifacts
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|res
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|WorkspaceArtifact
name|wa
range|:
name|artifacts
control|)
block|{
name|String
name|name
init|=
name|wa
operator|.
name|name
decl_stmt|;
name|String
name|type
init|=
name|wa
operator|.
name|type
decl_stmt|;
name|String
name|ext
init|=
name|wa
operator|.
name|ext
decl_stmt|;
name|String
name|path
init|=
name|wa
operator|.
name|path
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
literal|"jar"
expr_stmt|;
block|}
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
name|ext
operator|=
literal|"jar"
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|path
operator|=
literal|"target"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"dist"
operator|+
name|File
operator|.
name|separator
operator|+
name|type
operator|+
literal|"s"
operator|+
name|File
operator|.
name|separator
operator|+
name|name
operator|+
literal|"."
operator|+
name|ext
expr_stmt|;
block|}
name|URL
name|url
decl_stmt|;
name|File
name|ivyFile
init|=
name|md2IvyFile
operator|.
name|get
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|ivyFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|url
operator|=
name|artifactFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
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
literal|"Unsupported file path : "
operator|+
name|artifactFile
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|res
operator|.
name|add
argument_list|(
operator|new
name|DefaultArtifact
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|url
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|res
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
comment|// Not much to do here - downloads are not required for workspace projects.
name|DownloadReport
name|dr
init|=
operator|new
name|DownloadReport
argument_list|()
decl_stmt|;
for|for
control|(
name|Artifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|ArtifactDownloadReport
name|adr
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|dr
operator|.
name|addArtifactReport
argument_list|(
name|adr
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|artifact
operator|.
name|getUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
operator|||
operator|!
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
condition|)
block|{
comment|// this is not an artifact managed by this resolver
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|FAILED
argument_list|)
expr_stmt|;
return|return
name|dr
return|;
block|}
name|File
name|f
decl_stmt|;
try|try
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|f
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|adr
operator|.
name|setLocalFile
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|NO
argument_list|)
expr_stmt|;
name|adr
operator|.
name|setSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t[IN WORKSPACE] "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
return|return
name|dr
return|;
block|}
block|}
block|}
end_class

end_unit

