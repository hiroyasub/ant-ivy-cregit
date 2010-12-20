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
name|obr
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|cache
operator|.
name|CacheDownloadOptions
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
name|obr
operator|.
name|xml
operator|.
name|OBRXMLParser
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
name|BundleRepoResolver
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
name|RelativeURLRepository
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
name|ArtifactResourceResolver
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
name|Resource
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
name|ResourceDownloader
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
name|file
operator|.
name|FileRepository
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
name|FileUtil
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
name|OBRResolver
extends|extends
name|BundleRepoResolver
block|{
specifier|private
name|String
name|repoXmlURL
decl_stmt|;
specifier|private
name|String
name|repoXmlFile
decl_stmt|;
specifier|public
name|void
name|setRepoXmlFile
parameter_list|(
name|String
name|repositoryXmlFile
parameter_list|)
block|{
name|this
operator|.
name|repoXmlFile
operator|=
name|repositoryXmlFile
expr_stmt|;
block|}
specifier|public
name|void
name|setRepoXmlURL
parameter_list|(
name|String
name|repositoryXmlURL
parameter_list|)
block|{
name|this
operator|.
name|repoXmlURL
operator|=
name|repositoryXmlURL
expr_stmt|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|repoXmlFile
operator|!=
literal|null
operator|&&
name|repoXmlURL
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: repoXmlFile and repoXmlUrl cannot be set both"
argument_list|)
throw|;
block|}
if|if
condition|(
name|repoXmlFile
operator|!=
literal|null
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|repoXmlFile
argument_list|)
decl_stmt|;
name|setRepository
argument_list|(
operator|new
name|FileRepository
argument_list|(
name|f
operator|.
name|getParentFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|loadRepoFromFile
argument_list|(
name|f
argument_list|,
name|repoXmlFile
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|repoXmlURL
operator|!=
literal|null
condition|)
block|{
specifier|final
name|URL
name|url
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|repoXmlURL
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
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: repoXmlURL '"
operator|+
name|repoXmlURL
operator|+
literal|"' is not an URL"
argument_list|)
throw|;
block|}
comment|// compute the base URL
name|URL
name|baseUrl
decl_stmt|;
name|String
name|basePath
init|=
literal|"/"
decl_stmt|;
name|int
name|i
init|=
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|basePath
operator|=
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|baseUrl
operator|=
operator|new
name|URL
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|url
operator|.
name|getHost
argument_list|()
argument_list|,
name|url
operator|.
name|getPort
argument_list|()
argument_list|,
name|basePath
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
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: the base url couldn'd be extracted from the url "
operator|+
name|url
operator|+
literal|" ("
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
name|setRepository
argument_list|(
operator|new
name|RelativeURLRepository
argument_list|(
name|baseUrl
argument_list|)
argument_list|)
expr_stmt|;
comment|// get the obr descriptor into the cache
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"_obr_cache_"
argument_list|,
name|getName
argument_list|()
argument_list|,
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
literal|null
argument_list|,
literal|"obr"
argument_list|,
literal|"obr"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
name|CacheDownloadOptions
name|options
init|=
operator|new
name|CacheDownloadOptions
argument_list|()
decl_stmt|;
name|ArtifactDownloadReport
name|report
init|=
name|getRepositoryCacheManager
argument_list|()
operator|.
name|download
argument_list|(
name|artifact
argument_list|,
operator|new
name|ArtifactResourceResolver
argument_list|()
block|{
specifier|public
name|ResolvedResource
name|resolve
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
operator|new
name|ResolvedResource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|,
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|,
operator|new
name|ResourceDownloader
argument_list|()
block|{
specifier|public
name|void
name|download
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Resource
name|resource
parameter_list|,
name|File
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|dest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|dest
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|File
name|part
init|=
operator|new
name|File
argument_list|(
name|dest
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".part"
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|url
argument_list|,
name|part
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|part
operator|.
name|renameTo
argument_list|(
name|dest
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"impossible to move part file to definitive one: "
operator|+
name|part
operator|+
literal|" -> "
operator|+
name|dest
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|loadRepoFromFile
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|,
name|repoXmlURL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: repoXmlFile or repoXmlUrl is missing"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|loadRepoFromFile
parameter_list|(
name|File
name|repoFile
parameter_list|,
name|String
name|sourceLocation
parameter_list|)
block|{
name|FileInputStream
name|in
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
name|repoFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: the file "
operator|+
name|sourceLocation
operator|+
literal|" was not found"
argument_list|)
throw|;
block|}
try|try
block|{
name|setRepoDescriptor
argument_list|(
name|OBRXMLParser
operator|.
name|parse
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: the file "
operator|+
name|sourceLocation
operator|+
literal|" is incorrectly formed ("
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
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: the file "
operator|+
name|sourceLocation
operator|+
literal|" could not be read ("
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
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The OBR repository resolver "
operator|+
name|getName
argument_list|()
operator|+
literal|" couldn't be configured: the file "
operator|+
name|sourceLocation
operator|+
literal|" has incorrect XML ("
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
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// don't care
block|}
block|}
block|}
end_class

end_unit

