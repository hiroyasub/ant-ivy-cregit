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
name|event
operator|.
name|EventManager
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
name|SAXException
import|;
end_import

begin_class
specifier|public
class|class
name|OBRResolver
extends|extends
name|AbstractOSGiResolver
block|{
specifier|private
name|String
name|repoXmlURL
decl_stmt|;
specifier|private
name|String
name|repoXmlFile
decl_stmt|;
specifier|private
name|Long
name|metadataTtl
decl_stmt|;
specifier|private
name|Boolean
name|forceMetadataUpdate
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
annotation|@
name|Override
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
name|loadRepoFromFile
argument_list|(
name|f
operator|.
name|getParentFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|,
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
name|ArtifactDownloadReport
name|report
decl_stmt|;
name|EventManager
name|eventManager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|getRepository
argument_list|()
operator|.
name|addTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
name|Resource
name|obrResource
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
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
operator|.
name|longValue
argument_list|()
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
name|report
operator|=
name|getRepositoryCacheManager
argument_list|()
operator|.
name|downloadRepositoryResource
argument_list|(
name|obrResource
argument_list|,
literal|"obr"
argument_list|,
literal|"obr"
argument_list|,
literal|"xml"
argument_list|,
name|options
argument_list|,
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|getRepository
argument_list|()
operator|.
name|removeTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
block|}
name|URI
name|baseURI
decl_stmt|;
try|try
block|{
name|baseURI
operator|=
operator|new
name|URI
argument_list|(
name|repoXmlURL
argument_list|)
expr_stmt|;
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
literal|"illegal uri"
argument_list|)
throw|;
block|}
name|loadRepoFromFile
argument_list|(
name|baseURI
argument_list|,
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
name|URI
name|baseUri
parameter_list|,
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
name|baseUri
argument_list|,
name|in
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
argument_list|,
name|e
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

