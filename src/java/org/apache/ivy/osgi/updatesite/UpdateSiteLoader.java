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
name|FileInputStream
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
name|io
operator|.
name|InputStream
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
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
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
name|p2
operator|.
name|P2ArtifactParser
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
name|p2
operator|.
name|P2CompositeParser
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
name|p2
operator|.
name|P2Descriptor
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
name|p2
operator|.
name|P2MetadataParser
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
name|p2
operator|.
name|XMLInputParser
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
name|updatesite
operator|.
name|xml
operator|.
name|EclipseFeature
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
name|updatesite
operator|.
name|xml
operator|.
name|EclipseUpdateSiteParser
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
name|updatesite
operator|.
name|xml
operator|.
name|FeatureParser
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
name|updatesite
operator|.
name|xml
operator|.
name|UpdateSite
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
name|updatesite
operator|.
name|xml
operator|.
name|UpdateSiteDigestParser
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
name|URLRepository
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
name|UpdateSiteLoader
block|{
specifier|private
specifier|final
name|RepositoryCacheManager
name|repositoryCacheManager
decl_stmt|;
specifier|private
specifier|final
name|URLRepository
name|urlRepository
init|=
operator|new
name|URLRepository
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|CacheResourceOptions
name|options
decl_stmt|;
specifier|public
name|UpdateSiteLoader
parameter_list|(
name|RepositoryCacheManager
name|repositoryCacheManager
parameter_list|,
name|EventManager
name|eventManager
parameter_list|,
name|CacheResourceOptions
name|options
parameter_list|)
block|{
name|this
operator|.
name|repositoryCacheManager
operator|=
name|repositoryCacheManager
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|urlRepository
operator|.
name|addTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|RepoDescriptor
name|load
parameter_list|(
name|URI
name|repoUri
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
if|if
condition|(
operator|!
name|repoUri
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
try|try
block|{
name|repoUri
operator|=
operator|new
name|URI
argument_list|(
name|repoUri
operator|.
name|toString
argument_list|()
operator|+
literal|"/"
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
literal|"Cannot make an uri for the repo"
argument_list|)
throw|;
block|}
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"Loading the update site "
operator|+
name|repoUri
argument_list|)
expr_stmt|;
comment|// first look for a p2 repository
name|RepoDescriptor
name|repo
init|=
name|loadP2
argument_list|(
name|repoUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
return|return
name|repo
return|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tNo P2 artifacts, falling back on the old fashioned updatesite"
argument_list|)
expr_stmt|;
comment|// then try the old update site
name|UpdateSite
name|site
init|=
name|loadSite
argument_list|(
name|repoUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|site
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|repo
operator|=
name|loadFromDigest
argument_list|(
name|site
argument_list|)
expr_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
return|return
name|repo
return|;
block|}
return|return
name|loadFromSite
argument_list|(
name|site
argument_list|)
return|;
block|}
specifier|private
name|P2Descriptor
name|loadP2
parameter_list|(
name|URI
name|repoUri
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|P2Descriptor
name|p2Descriptor
init|=
operator|new
name|P2Descriptor
argument_list|(
name|repoUri
argument_list|,
name|ExecutionEnvironmentProfileProvider
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|populateP2Descriptor
argument_list|(
name|repoUri
argument_list|,
name|p2Descriptor
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|p2Descriptor
return|;
block|}
specifier|private
name|boolean
name|populateP2Descriptor
parameter_list|(
name|URI
name|repoUri
parameter_list|,
name|P2Descriptor
name|p2Descriptor
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"Loading P2 repository "
operator|+
name|repoUri
argument_list|)
expr_stmt|;
name|boolean
name|artifactExists
init|=
name|readComposite
argument_list|(
name|repoUri
argument_list|,
literal|"compositeArtifacts"
argument_list|,
name|p2Descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactExists
condition|)
block|{
name|artifactExists
operator|=
name|readJarOrXml
argument_list|(
name|repoUri
argument_list|,
literal|"artifacts"
argument_list|,
operator|new
name|P2ArtifactParser
argument_list|(
name|p2Descriptor
argument_list|,
name|repoUri
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|boolean
name|contentExists
init|=
name|readComposite
argument_list|(
name|repoUri
argument_list|,
literal|"compositeContent"
argument_list|,
name|p2Descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|contentExists
condition|)
block|{
name|contentExists
operator|=
name|readJarOrXml
argument_list|(
name|repoUri
argument_list|,
literal|"content"
argument_list|,
operator|new
name|P2MetadataParser
argument_list|(
name|p2Descriptor
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactExists
operator|||
name|contentExists
return|;
block|}
specifier|private
name|boolean
name|readComposite
parameter_list|(
name|URI
name|repoUri
parameter_list|,
name|String
name|name
parameter_list|,
name|P2Descriptor
name|p2Descriptor
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|P2CompositeParser
name|p2CompositeParser
init|=
operator|new
name|P2CompositeParser
argument_list|()
decl_stmt|;
name|boolean
name|exist
init|=
name|readJarOrXml
argument_list|(
name|repoUri
argument_list|,
name|name
argument_list|,
name|p2CompositeParser
argument_list|)
decl_stmt|;
if|if
condition|(
name|exist
condition|)
block|{
name|Iterator
name|itChildLocation
init|=
name|p2CompositeParser
operator|.
name|getChildLocations
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itChildLocation
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|childLocation
init|=
operator|(
name|String
operator|)
name|itChildLocation
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|childLocation
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|childLocation
operator|+=
literal|"/"
expr_stmt|;
block|}
name|URI
name|childUri
init|=
name|repoUri
operator|.
name|resolve
argument_list|(
name|childLocation
argument_list|)
decl_stmt|;
name|populateP2Descriptor
argument_list|(
name|childUri
argument_list|,
name|p2Descriptor
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exist
return|;
block|}
specifier|private
name|boolean
name|readJarOrXml
parameter_list|(
name|URI
name|repoUri
parameter_list|,
name|String
name|baseName
parameter_list|,
name|XMLInputParser
name|reader
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|InputStream
name|readIn
init|=
literal|null
decl_stmt|;
comment|// the input stream from which the xml should be read
name|URL
name|contentUrl
init|=
name|repoUri
operator|.
name|resolve
argument_list|(
name|baseName
operator|+
literal|".jar"
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|contentUrl
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|report
init|=
name|repositoryCacheManager
operator|.
name|downloadRepositoryResource
argument_list|(
name|res
argument_list|,
name|baseName
argument_list|,
name|baseName
argument_list|,
literal|"jar"
argument_list|,
name|options
argument_list|,
name|urlRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
comment|// no jar file, try the xml one
name|contentUrl
operator|=
name|repoUri
operator|.
name|resolve
argument_list|(
name|baseName
operator|+
literal|".xml"
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|res
operator|=
operator|new
name|URLResource
argument_list|(
name|contentUrl
argument_list|)
expr_stmt|;
name|report
operator|=
name|repositoryCacheManager
operator|.
name|downloadRepositoryResource
argument_list|(
name|res
argument_list|,
name|baseName
argument_list|,
name|baseName
argument_list|,
literal|"xml"
argument_list|,
name|options
argument_list|,
name|urlRepository
argument_list|)
expr_stmt|;
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
comment|// no xml either
return|return
literal|false
return|;
block|}
name|readIn
operator|=
operator|new
name|FileInputStream
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// compressed, let's get the pointer on the actual xml
name|readIn
operator|=
name|findEntry
argument_list|(
name|in
argument_list|,
name|baseName
operator|+
literal|".xml"
argument_list|)
expr_stmt|;
if|if
condition|(
name|readIn
operator|==
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
try|try
block|{
name|reader
operator|.
name|parse
argument_list|(
name|readIn
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|readIn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|UpdateSite
name|loadSite
parameter_list|(
name|URI
name|repoUri
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|URI
name|siteUri
init|=
name|normalizeSiteUri
argument_list|(
name|repoUri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|URL
name|u
init|=
name|siteUri
operator|.
name|resolve
argument_list|(
literal|"site.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|report
init|=
name|repositoryCacheManager
operator|.
name|downloadRepositoryResource
argument_list|(
name|res
argument_list|,
literal|"site"
argument_list|,
literal|"updatesite"
argument_list|,
literal|"xml"
argument_list|,
name|options
argument_list|,
name|urlRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|UpdateSite
name|site
init|=
name|EclipseUpdateSiteParser
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|site
operator|.
name|setUri
argument_list|(
name|normalizeSiteUri
argument_list|(
name|site
operator|.
name|getUri
argument_list|()
argument_list|,
name|siteUri
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|site
return|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|URI
name|normalizeSiteUri
parameter_list|(
name|URI
name|uri
parameter_list|,
name|URI
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
name|String
name|uriString
init|=
name|uri
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|uriString
operator|.
name|endsWith
argument_list|(
literal|"site.xml"
argument_list|)
condition|)
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|uriString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uriString
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
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
literal|"Illegal uri"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|uriString
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|uriString
operator|+
literal|"/"
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
literal|"Illegal uri"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|uri
return|;
block|}
specifier|private
name|UpdateSiteDescriptor
name|loadFromDigest
parameter_list|(
name|UpdateSite
name|site
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|URI
name|digestBaseUri
init|=
name|site
operator|.
name|getDigestUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|digestBaseUri
operator|==
literal|null
condition|)
block|{
name|digestBaseUri
operator|=
name|site
operator|.
name|getUri
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|digestBaseUri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|digestBaseUri
operator|=
name|site
operator|.
name|getUri
argument_list|()
operator|.
name|resolve
argument_list|(
name|digestBaseUri
argument_list|)
expr_stmt|;
block|}
name|URL
name|digest
init|=
name|digestBaseUri
operator|.
name|resolve
argument_list|(
literal|"digest.zip"
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tReading "
operator|+
name|digest
argument_list|)
expr_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|digest
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|report
init|=
name|repositoryCacheManager
operator|.
name|downloadRepositoryResource
argument_list|(
name|res
argument_list|,
literal|"digest"
argument_list|,
literal|"digest"
argument_list|,
literal|"zip"
argument_list|,
name|options
argument_list|,
name|urlRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ZipInputStream
name|zipped
init|=
name|findEntry
argument_list|(
name|in
argument_list|,
literal|"digest.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|zipped
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|UpdateSiteDigestParser
operator|.
name|parse
argument_list|(
name|zipped
argument_list|,
name|site
argument_list|)
return|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|UpdateSiteDescriptor
name|loadFromSite
parameter_list|(
name|UpdateSite
name|site
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
throws|,
name|SAXException
block|{
name|UpdateSiteDescriptor
name|repoDescriptor
init|=
operator|new
name|UpdateSiteDescriptor
argument_list|(
name|site
operator|.
name|getUri
argument_list|()
argument_list|,
name|ExecutionEnvironmentProfileProvider
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|itFeatures
init|=
name|site
operator|.
name|getFeatures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itFeatures
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|EclipseFeature
name|feature
init|=
operator|(
name|EclipseFeature
operator|)
name|itFeatures
operator|.
name|next
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|site
operator|.
name|getUri
argument_list|()
operator|.
name|resolve
argument_list|(
name|feature
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|URLResource
name|res
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|report
init|=
name|repositoryCacheManager
operator|.
name|downloadRepositoryResource
argument_list|(
name|res
argument_list|,
name|feature
operator|.
name|getId
argument_list|()
argument_list|,
literal|"feature"
argument_list|,
literal|"jar"
argument_list|,
name|options
argument_list|,
name|urlRepository
argument_list|)
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ZipInputStream
name|zipped
init|=
name|findEntry
argument_list|(
name|in
argument_list|,
literal|"feature.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|zipped
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|EclipseFeature
name|f
init|=
name|FeatureParser
operator|.
name|parse
argument_list|(
name|zipped
argument_list|)
decl_stmt|;
name|f
operator|.
name|setURL
argument_list|(
name|feature
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|repoDescriptor
operator|.
name|addFeature
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|repoDescriptor
return|;
block|}
specifier|private
name|ZipInputStream
name|findEntry
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|String
name|entryName
parameter_list|)
throws|throws
name|IOException
block|{
name|ZipInputStream
name|zipped
init|=
operator|new
name|ZipInputStream
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|ZipEntry
name|zipEntry
init|=
name|zipped
operator|.
name|getNextEntry
argument_list|()
decl_stmt|;
while|while
condition|(
name|zipEntry
operator|!=
literal|null
operator|&&
operator|!
name|zipEntry
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|entryName
argument_list|)
condition|)
block|{
name|zipEntry
operator|=
name|zipped
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|zipEntry
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|zipped
return|;
block|}
block|}
end_class

end_unit

