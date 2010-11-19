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
name|BundleRepo
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
name|InputSource
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
name|Locator
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
name|XMLReader
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
name|helpers
operator|.
name|XMLReaderFactory
import|;
end_import

begin_class
specifier|public
class|class
name|OBRXMLParser
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
name|REPOSITORY_LASTMODIFIED
init|=
literal|"lastmodified"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REPOSITORY_NAME
init|=
literal|"name"
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
name|RESOURCE_ID
init|=
literal|"id"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE_PRESENTATION_NAME
init|=
literal|"presentationname"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE_SYMBOLIC_NAME
init|=
literal|"symbolicname"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE_URI
init|=
literal|"uri"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESOURCE_VERSION
init|=
literal|"version"
decl_stmt|;
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
name|CAPABILITY_NAME
init|=
literal|"name"
decl_stmt|;
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
name|CAPABILITY_PROPERTY_NAME
init|=
literal|"n"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CAPABILITY_PROPERTY_VALUE
init|=
literal|"v"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CAPABILITY_PROPERTY_TYPE
init|=
literal|"t"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE
init|=
literal|"require"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE_NAME
init|=
literal|"name"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE_OPTIONAL
init|=
literal|"optional"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE_MULTIPLE
init|=
literal|"multiple"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE_EXTEND
init|=
literal|"extend"
decl_stmt|;
specifier|static
specifier|final
name|String
name|REQUIRE_FILTER
init|=
literal|"filter"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TRUE
init|=
literal|"true"
decl_stmt|;
specifier|static
specifier|final
name|String
name|FALSE
init|=
literal|"false"
decl_stmt|;
specifier|public
specifier|static
name|BundleRepo
name|parse
parameter_list|(
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
name|XMLReader
name|reader
decl_stmt|;
try|try
block|{
name|reader
operator|=
name|XMLReaderFactory
operator|.
name|createXMLReader
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|RepositoryHandler
name|handler
init|=
operator|new
name|RepositoryHandler
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setContentHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|reader
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|handler
operator|.
name|repo
return|;
block|}
specifier|private
specifier|static
class|class
name|RepositoryHandler
extends|extends
name|DelegetingHandler
comment|/*<DelegetingHandler<?>> */
block|{
name|BundleRepo
name|repo
decl_stmt|;
specifier|public
name|RepositoryHandler
parameter_list|()
block|{
name|super
argument_list|(
name|REPOSITORY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
operator|new
name|ResourceHandler
argument_list|(
name|this
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
name|BundleRepo
argument_list|()
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|REPOSITORY_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|lastModified
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|REPOSITORY_LASTMODIFIED
argument_list|)
decl_stmt|;
try|try
block|{
name|repo
operator|.
name|setLastModified
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|lastModified
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
name|printWarning
argument_list|(
name|this
argument_list|,
literal|"Incorrect last modified timestamp : "
operator|+
name|lastModified
operator|+
literal|". It will be ignored."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|ResourceHandler
extends|extends
name|DelegetingHandler
comment|/*<RepositoryHandler> */
block|{
name|BundleInfo
name|bundleInfo
decl_stmt|;
specifier|public
name|ResourceHandler
parameter_list|(
name|RepositoryHandler
name|repositoryHandler
parameter_list|)
block|{
name|super
argument_list|(
name|RESOURCE
argument_list|,
name|repositoryHandler
argument_list|)
expr_stmt|;
operator|new
name|ResourceDescriptionHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
operator|new
name|ResourceDocumentationHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
operator|new
name|ResourceLicenseHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
operator|new
name|ResourceSizeHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
operator|new
name|CapabilityHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
operator|new
name|RequireHandler
argument_list|(
name|this
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
name|RESOURCE_SYMBOLIC_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|symbolicname
operator|==
literal|null
condition|)
block|{
name|printError
argument_list|(
name|this
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
name|atts
operator|.
name|getValue
argument_list|(
name|RESOURCE_VERSION
argument_list|)
decl_stmt|;
name|Version
name|version
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|version
operator|=
operator|new
name|Version
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|NumberFormatException
name|e
parameter_list|)
block|{
name|printError
argument_list|(
name|this
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
name|RESOURCE_PRESENTATION_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|bundleInfo
operator|.
name|setUri
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|RESOURCE_URI
argument_list|)
argument_list|)
expr_stmt|;
name|bundleInfo
operator|.
name|setId
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|RESOURCE_ID
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
operator|(
operator|(
name|RepositoryHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|repo
operator|.
name|addBundle
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ResourceDescriptionHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
specifier|public
name|ResourceDescriptionHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
literal|"description"
argument_list|,
name|resourceHandler
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|bundleInfo
operator|.
name|setDescription
argument_list|(
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ResourceDocumentationHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
specifier|public
name|ResourceDocumentationHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
literal|"documentation"
argument_list|,
name|resourceHandler
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|bundleInfo
operator|.
name|setDocumentation
argument_list|(
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ResourceLicenseHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
specifier|public
name|ResourceLicenseHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
literal|"license"
argument_list|,
name|resourceHandler
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|bundleInfo
operator|.
name|setLicense
argument_list|(
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ResourceSizeHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
specifier|public
name|ResourceSizeHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
literal|"size"
argument_list|,
name|resourceHandler
argument_list|)
expr_stmt|;
name|setBufferingChar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|size
init|=
name|getBufferedChars
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
try|try
block|{
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
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
name|printWarning
argument_list|(
name|this
argument_list|,
literal|"Invalid size for the bundle"
operator|+
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
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
specifier|private
specifier|static
class|class
name|CapabilityHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
name|Capability
name|capability
decl_stmt|;
specifier|public
name|CapabilityHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
name|CAPABILITY
argument_list|,
name|resourceHandler
argument_list|)
expr_stmt|;
operator|new
name|CapabilityPropertyHandler
argument_list|(
name|this
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
name|atts
operator|.
name|getValue
argument_list|(
name|CAPABILITY_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Capability with no name"
argument_list|)
expr_stmt|;
return|return;
block|}
name|capability
operator|=
operator|new
name|Capability
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
try|try
block|{
name|CapabilityAdapter
operator|.
name|adapt
argument_list|(
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|bundleInfo
argument_list|,
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
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Invalid capability: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|CapabilityPropertyHandler
extends|extends
name|DelegetingHandler
comment|/*<CapabilityHandler> */
block|{
specifier|public
name|CapabilityPropertyHandler
parameter_list|(
name|CapabilityHandler
name|capabilityHandler
parameter_list|)
block|{
name|super
argument_list|(
name|CAPABILITY_PROPERTY
argument_list|,
name|capabilityHandler
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
name|atts
operator|.
name|getValue
argument_list|(
name|CAPABILITY_PROPERTY_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Capability property with no name on a capability "
operator|+
operator|(
operator|(
name|CapabilityHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|capability
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|value
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|CAPABILITY_PROPERTY_VALUE
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Capability property with no value on a capability "
operator|+
operator|(
operator|(
name|CapabilityHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|capability
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|type
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|CAPABILITY_PROPERTY_TYPE
argument_list|)
decl_stmt|;
operator|(
operator|(
name|CapabilityHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
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
specifier|private
specifier|static
class|class
name|RequireHandler
extends|extends
name|DelegetingHandler
comment|/*<ResourceHandler> */
block|{
specifier|private
name|Requirement
name|requirement
decl_stmt|;
specifier|public
name|RequireHandler
parameter_list|(
name|ResourceHandler
name|resourceHandler
parameter_list|)
block|{
name|super
argument_list|(
name|REQUIRE
argument_list|,
name|resourceHandler
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
name|atts
operator|.
name|getValue
argument_list|(
name|REQUIRE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Requirement with no name"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|filterText
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|REQUIRE_FILTER
argument_list|)
decl_stmt|;
name|RequirementFilter
name|filter
init|=
literal|null
decl_stmt|;
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
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Requirement with illformed filter: "
operator|+
name|filterText
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|Boolean
name|optional
init|=
literal|null
decl_stmt|;
try|try
block|{
name|optional
operator|=
name|parseBoolean
argument_list|(
name|atts
argument_list|,
name|REQUIRE_OPTIONAL
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Requirement with unrecognised optional: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|Boolean
name|multiple
init|=
literal|null
decl_stmt|;
try|try
block|{
name|multiple
operator|=
name|parseBoolean
argument_list|(
name|atts
argument_list|,
name|REQUIRE_MULTIPLE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Requirement with unrecognised multiple: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|Boolean
name|extend
init|=
literal|null
decl_stmt|;
try|try
block|{
name|extend
operator|=
name|parseBoolean
argument_list|(
name|atts
argument_list|,
name|REQUIRE_EXTEND
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Requirement with unrecognised extend: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
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
if|if
condition|(
name|extend
operator|!=
literal|null
condition|)
block|{
name|requirement
operator|.
name|setExtend
argument_list|(
name|extend
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doEndElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
try|try
block|{
name|RequirementAdapter
operator|.
name|adapt
argument_list|(
operator|(
operator|(
name|ResourceHandler
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|bundleInfo
argument_list|,
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
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Unsupported requirement filter: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|skipResourceOnError
argument_list|(
name|this
argument_list|,
literal|"Error in the requirement filter on the bundle: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|Boolean
name|parseBoolean
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|ParseException
block|{
name|String
name|v
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|TRUE
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
if|else if
condition|(
name|FALSE
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Unparsable boolean value: "
operator|+
name|v
argument_list|,
literal|0
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|skipResourceOnError
parameter_list|(
name|DelegetingHandler
comment|/*<?> */
name|handler
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|DelegetingHandler
comment|/*<?> */
name|resourceHandler
init|=
name|handler
decl_stmt|;
while|while
condition|(
operator|!
operator|(
name|resourceHandler
operator|instanceof
name|ResourceHandler
operator|)
condition|)
block|{
name|resourceHandler
operator|=
name|resourceHandler
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
name|BundleInfo
name|bundleInfo
init|=
operator|(
operator|(
name|ResourceHandler
operator|)
name|resourceHandler
operator|)
operator|.
name|bundleInfo
decl_stmt|;
name|printError
argument_list|(
name|handler
argument_list|,
name|message
operator|+
literal|". The resource "
operator|+
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" is then ignored."
argument_list|)
expr_stmt|;
name|resourceHandler
operator|.
name|skip
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|printError
parameter_list|(
name|DelegetingHandler
comment|/*<?> */
name|handler
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
name|getLocation
argument_list|(
name|handler
operator|.
name|getLocator
argument_list|()
argument_list|)
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|printWarning
parameter_list|(
name|DelegetingHandler
comment|/*<?> */
name|handler
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
name|getLocation
argument_list|(
name|handler
operator|.
name|getLocator
argument_list|()
argument_list|)
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getLocation
parameter_list|(
name|Locator
name|locator
parameter_list|)
block|{
if|if
condition|(
name|locator
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
literal|"[line "
operator|+
name|locator
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|" col. "
operator|+
name|locator
operator|.
name|getColumnNumber
argument_list|()
operator|+
literal|"] "
return|;
block|}
block|}
end_class

end_unit
