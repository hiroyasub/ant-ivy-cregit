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
operator|.
name|xml
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|obr
operator|.
name|filter
operator|.
name|RequirementFilterParser
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
name|BundleRepoDescriptor
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
name|DelegetingHandler
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
name|XMLHelper
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_class
specifier|public
class|class
name|OBRXMLParser
block|{
specifier|public
specifier|static
name|BundleRepoDescriptor
name|parse
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|RepositoryHandler
name|handler
init|=
operator|new
name|RepositoryHandler
argument_list|(
name|baseUri
argument_list|)
decl_stmt|;
try|try
block|{
name|XMLHelper
operator|.
name|parse
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
name|handler
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|handler
operator|.
name|repo
return|;
block|}
specifier|static
class|class
name|RepositoryHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|REPOSITORY
init|=
literal|"repository"
decl_stmt|;
specifier|static
specifier|final
name|String
name|LASTMODIFIED
init|=
literal|"lastmodified"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
name|BundleRepoDescriptor
name|repo
decl_stmt|;
specifier|private
specifier|final
name|URI
name|baseUri
decl_stmt|;
specifier|public
name|RepositoryHandler
parameter_list|(
name|URI
name|baseUri
parameter_list|)
block|{
name|super
argument_list|(
name|REPOSITORY
argument_list|)
expr_stmt|;
name|this
operator|.
name|baseUri
operator|=
name|baseUri
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ResourceHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|repo
operator|.
name|addBundle
argument_list|(
operator|(
operator|(
name|ResourceHandler
operator|)
name|child
operator|)
operator|.
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
block|{
name|repo
operator|=
operator|new
name|BundleRepoDescriptor
argument_list|(
name|baseUri
argument_list|,
name|ExecutionEnvironmentProfileProvider
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|NAME
argument_list|)
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLastModified
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|LASTMODIFIED
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ResourceHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_VERSION
init|=
literal|"1.0.0"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE
init|=
literal|"resource"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"id"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PRESENTATION_NAME
init|=
literal|"presentationname"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SYMBOLIC_NAME
init|=
literal|"symbolicname"
decl_stmt|;
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"uri"
decl_stmt|;
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
decl_stmt|;
name|BundleInfo
name|bundleInfo
decl_stmt|;
specifier|public
name|ResourceHandler
parameter_list|()
block|{
name|super
argument_list|(
name|RESOURCE
argument_list|)
expr_stmt|;
name|setSkipOnError
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// if anything bad happen in any children, just ignore the
comment|// resource
name|addChild
argument_list|(
operator|new
name|ResourceSourceHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|String
name|uri
init|=
name|child
operator|.
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|uri
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
condition|)
block|{
comment|// the maven plugin is putting some useless source url sometimes...
name|log
argument_list|(
name|Message
operator|.
name|MSG_WARN
argument_list|,
literal|"A source uri is suspect, it is not ending with .jar, it is probably"
operator|+
literal|" a pointer to a download page. Ignoring it."
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|bundleInfo
operator|.
name|setSourceURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
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
name|log
argument_list|(
name|Message
operator|.
name|MSG_WARN
argument_list|,
literal|"Incorrect uri "
operator|+
name|uri
operator|+
literal|". The source of "
operator|+
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" is then ignored."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ResourceDescriptionHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|bundleInfo
operator|.
name|setDescription
argument_list|(
name|child
operator|.
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ResourceDocumentationHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|bundleInfo
operator|.
name|setDocumentation
argument_list|(
name|child
operator|.
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ResourceLicenseHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|bundleInfo
operator|.
name|setLicense
argument_list|(
name|child
operator|.
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ResourceSizeHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|String
name|size
init|=
name|child
operator|.
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
try|try
block|{
name|bundleInfo
operator|.
name|setSize
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|size
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|Message
operator|.
name|MSG_WARN
argument_list|,
literal|"Invalid size for the bundle "
operator|+
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|": "
operator|+
name|size
operator|+
literal|". This size is then ignored."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|CapabilityHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
throws|throws
name|SAXParseException
block|{
try|try
block|{
name|CapabilityAdapter
operator|.
name|adapt
argument_list|(
name|bundleInfo
argument_list|,
operator|(
operator|(
name|CapabilityHandler
operator|)
name|child
operator|)
operator|.
name|capability
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
name|SAXParseException
argument_list|(
literal|"Invalid capability: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|child
operator|.
name|getLocator
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|RequireHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
throws|throws
name|SAXParseException
block|{
try|try
block|{
name|RequirementAdapter
operator|.
name|adapt
argument_list|(
name|bundleInfo
argument_list|,
operator|(
operator|(
name|RequireHandler
operator|)
name|child
operator|)
operator|.
name|requirement
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedFilterException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"Unsupported requirement filter: "
operator|+
operator|(
operator|(
name|RequireHandler
operator|)
name|child
operator|)
operator|.
name|filter
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|getLocator
argument_list|()
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
name|SAXParseException
argument_list|(
literal|"Error in the requirement filter on the bundle: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|getLocator
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ExtendHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
throws|throws
name|SAXParseException
block|{
comment|// TODO handle fragment host
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|symbolicname
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|SYMBOLIC_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|symbolicname
operator|==
literal|null
condition|)
block|{
name|log
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|,
literal|"Resource with no symobilc name, skipping it."
argument_list|)
expr_stmt|;
name|skip
argument_list|()
expr_stmt|;
return|return;
block|}
name|String
name|v
init|=
name|getOptionalAttribute
argument_list|(
name|atts
argument_list|,
name|VERSION
argument_list|,
name|DEFAULT_VERSION
argument_list|)
decl_stmt|;
name|Version
name|version
decl_stmt|;
try|try
block|{
name|version
operator|=
operator|new
name|Version
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|,
literal|"Incorrect resource version: "
operator|+
name|v
operator|+
literal|". The resource "
operator|+
name|symbolicname
operator|+
literal|" is then ignored."
argument_list|)
expr_stmt|;
name|skip
argument_list|()
expr_stmt|;
return|return;
block|}
name|bundleInfo
operator|=
operator|new
name|BundleInfo
argument_list|(
name|symbolicname
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|bundleInfo
operator|.
name|setPresentationName
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|PRESENTATION_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|bundleInfo
operator|.
name|setUri
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
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
name|log
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|,
literal|"Incorrect uri "
operator|+
name|uri
operator|+
literal|". The resource "
operator|+
name|symbolicname
operator|+
literal|" is then ignored."
argument_list|)
expr_stmt|;
name|skip
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
name|bundleInfo
operator|.
name|setId
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getCurrentElementIdentifier
parameter_list|()
block|{
return|return
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|"/"
operator|+
name|bundleInfo
operator|.
name|getVersion
argument_list|()
return|;
block|}
block|}
specifier|static
class|class
name|ResourceSourceHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|SOURCE
init|=
literal|"source"
decl_stmt|;
specifier|public
name|ResourceSourceHandler
parameter_list|()
block|{
name|super
argument_list|(
name|SOURCE
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ResourceDescriptionHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
specifier|public
name|ResourceDescriptionHandler
parameter_list|()
block|{
name|super
argument_list|(
name|DESCRIPTION
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ResourceDocumentationHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|DOCUMENTATION
init|=
literal|"documentation"
decl_stmt|;
specifier|public
name|ResourceDocumentationHandler
parameter_list|()
block|{
name|super
argument_list|(
name|DOCUMENTATION
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ResourceLicenseHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|LICENSE
init|=
literal|"license"
decl_stmt|;
specifier|public
name|ResourceLicenseHandler
parameter_list|()
block|{
name|super
argument_list|(
name|LICENSE
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ResourceSizeHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|SIZE
init|=
literal|"size"
decl_stmt|;
specifier|public
name|ResourceSizeHandler
parameter_list|()
block|{
name|super
argument_list|(
name|SIZE
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|CapabilityHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|CAPABILITY
init|=
literal|"capability"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
name|Capability
name|capability
decl_stmt|;
specifier|public
name|CapabilityHandler
parameter_list|()
block|{
name|super
argument_list|(
name|CAPABILITY
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|CapabilityPropertyHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|String
name|name
init|=
operator|(
operator|(
name|CapabilityPropertyHandler
operator|)
name|child
operator|)
operator|.
name|name
decl_stmt|;
name|String
name|value
init|=
operator|(
operator|(
name|CapabilityPropertyHandler
operator|)
name|child
operator|)
operator|.
name|value
decl_stmt|;
name|String
name|type
init|=
operator|(
operator|(
name|CapabilityPropertyHandler
operator|)
name|child
operator|)
operator|.
name|type
decl_stmt|;
name|capability
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|name
init|=
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|NAME
argument_list|)
decl_stmt|;
name|capability
operator|=
operator|new
name|Capability
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|CapabilityPropertyHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|CAPABILITY_PROPERTY
init|=
literal|"p"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"n"
decl_stmt|;
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"v"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"t"
decl_stmt|;
name|String
name|name
decl_stmt|;
name|String
name|value
decl_stmt|;
name|String
name|type
decl_stmt|;
specifier|public
name|CapabilityPropertyHandler
parameter_list|()
block|{
name|super
argument_list|(
name|CAPABILITY_PROPERTY
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|name
operator|=
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|NAME
argument_list|)
expr_stmt|;
name|value
operator|=
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|VALUE
argument_list|)
expr_stmt|;
name|type
operator|=
name|atts
operator|.
name|getValue
argument_list|(
name|TYPE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|AbstractRequirementHandler
extends|extends
name|DelegetingHandler
block|{
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|static
specifier|final
name|String
name|OPTIONAL
init|=
literal|"optional"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MULTIPLE
init|=
literal|"multiple"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FILTER
init|=
literal|"filter"
decl_stmt|;
name|Requirement
name|requirement
decl_stmt|;
name|RequirementFilter
name|filter
decl_stmt|;
specifier|public
name|AbstractRequirementHandler
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|name
init|=
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|NAME
argument_list|)
decl_stmt|;
name|String
name|filterText
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|FILTER
argument_list|)
decl_stmt|;
name|filter
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|filterText
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|filter
operator|=
name|RequirementFilterParser
operator|.
name|parse
argument_list|(
name|filterText
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
name|SAXParseException
argument_list|(
literal|"Requirement with illformed filter: "
operator|+
name|filterText
argument_list|,
name|getLocator
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|Boolean
name|optional
init|=
name|getOptionalBooleanAttribute
argument_list|(
name|atts
argument_list|,
name|OPTIONAL
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Boolean
name|multiple
init|=
name|getOptionalBooleanAttribute
argument_list|(
name|atts
argument_list|,
name|MULTIPLE
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|requirement
operator|=
operator|new
name|Requirement
argument_list|(
name|name
argument_list|,
name|filter
argument_list|)
expr_stmt|;
if|if
condition|(
name|optional
operator|!=
literal|null
condition|)
block|{
name|requirement
operator|.
name|setOptional
argument_list|(
name|optional
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|multiple
operator|!=
literal|null
condition|)
block|{
name|requirement
operator|.
name|setMultiple
argument_list|(
name|multiple
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
class|class
name|RequireHandler
extends|extends
name|AbstractRequirementHandler
block|{
specifier|static
specifier|final
name|String
name|REQUIRE
init|=
literal|"require"
decl_stmt|;
specifier|public
name|RequireHandler
parameter_list|()
block|{
name|super
argument_list|(
name|REQUIRE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ExtendHandler
extends|extends
name|AbstractRequirementHandler
block|{
specifier|static
specifier|final
name|String
name|EXTEND
init|=
literal|"extend"
decl_stmt|;
specifier|public
name|ExtendHandler
parameter_list|()
block|{
name|super
argument_list|(
name|EXTEND
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

