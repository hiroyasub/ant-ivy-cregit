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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|XmlModuleDescriptorWriter
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

begin_class
specifier|public
class|class
name|OSGiManifestParser
implements|implements
name|ModuleDescriptorParser
block|{
specifier|private
specifier|static
specifier|final
name|OSGiManifestParser
name|INSTANCE
init|=
operator|new
name|OSGiManifestParser
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|OSGiManifestParser
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|ExecutionEnvironmentProfileProvider
name|profileProvider
init|=
name|ExecutionEnvironmentProfileProvider
operator|.
name|getInstance
argument_list|()
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|ExecutionEnvironmentProfileProvider
name|pp
parameter_list|)
block|{
name|this
operator|.
name|profileProvider
operator|=
name|pp
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Resource
name|res
parameter_list|)
block|{
if|if
condition|(
name|res
operator|==
literal|null
operator|||
name|res
operator|.
name|getName
argument_list|()
operator|==
literal|null
operator|||
name|res
operator|.
name|getName
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|res
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|"MANIFEST.MF"
argument_list|)
return|;
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|,
name|URL
name|descriptorURL
parameter_list|,
name|Resource
name|res
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|Manifest
name|m
init|=
operator|new
name|Manifest
argument_list|(
name|res
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|m
argument_list|)
decl_stmt|;
try|try
block|{
name|bundleInfo
operator|.
name|addArtifact
argument_list|(
operator|new
name|BundleArtifact
argument_list|(
literal|false
argument_list|,
operator|new
name|URI
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
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
literal|"Unsupported repository, resources names are not uris"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|BundleInfoAdapter
operator|.
name|toModuleDescriptor
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|bundleInfo
argument_list|,
name|m
argument_list|,
name|profileProvider
argument_list|)
return|;
block|}
specifier|public
name|void
name|toIvyFile
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Resource
name|res
parameter_list|,
name|File
name|destFile
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
try|try
block|{
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|,
name|URL
name|descriptorURL
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|URLResource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|descriptorURL
argument_list|)
decl_stmt|;
return|return
name|parseDescriptor
argument_list|(
name|ivySettings
argument_list|,
name|descriptorURL
argument_list|,
name|resource
argument_list|,
name|validate
argument_list|)
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
literal|"manifest"
return|;
block|}
specifier|public
name|Artifact
name|getMetadataArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Resource
name|res
parameter_list|)
block|{
return|return
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|mrid
argument_list|,
operator|new
name|Date
argument_list|(
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"manifest parser"
return|;
block|}
block|}
end_class

end_unit

