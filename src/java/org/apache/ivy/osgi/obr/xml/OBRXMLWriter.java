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
name|OutputStream
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXTransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|TransformerHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|IvyContext
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
name|BundleCapability
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
name|core
operator|.
name|ManifestParser
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
operator|.
name|CapabilityHandler
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
operator|.
name|CapabilityPropertyHandler
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
operator|.
name|RepositoryHandler
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
operator|.
name|RequireHandler
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
operator|.
name|ResourceHandler
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
name|ManifestAndLocation
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
name|ContentHandler
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
name|helpers
operator|.
name|AttributesImpl
import|;
end_import

begin_class
specifier|public
class|class
name|OBRXMLWriter
block|{
specifier|public
specifier|static
name|ContentHandler
name|newHandler
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|String
name|encoding
parameter_list|,
name|boolean
name|indent
parameter_list|)
throws|throws
name|TransformerConfigurationException
block|{
name|SAXTransformerFactory
name|tf
init|=
operator|(
name|SAXTransformerFactory
operator|)
name|SAXTransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|TransformerHandler
name|hd
init|=
name|tf
operator|.
name|newTransformerHandler
argument_list|()
decl_stmt|;
name|Transformer
name|serializer
init|=
name|tf
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|StreamResult
name|stream
init|=
operator|new
name|StreamResult
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
name|indent
condition|?
literal|"yes"
else|:
literal|"no"
argument_list|)
expr_stmt|;
name|hd
operator|.
name|setResult
argument_list|(
name|stream
argument_list|)
expr_stmt|;
return|return
name|hd
return|;
block|}
specifier|public
specifier|static
name|void
name|writeManifests
parameter_list|(
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
name|manifestAndLocations
parameter_list|,
name|ContentHandler
name|handler
parameter_list|,
name|boolean
name|quiet
parameter_list|)
throws|throws
name|SAXException
block|{
name|int
name|level
init|=
name|quiet
condition|?
name|Message
operator|.
name|MSG_DEBUG
else|:
name|Message
operator|.
name|MSG_WARN
decl_stmt|;
name|handler
operator|.
name|startDocument
argument_list|()
expr_stmt|;
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|atts
argument_list|)
expr_stmt|;
name|int
name|nbOk
init|=
literal|0
decl_stmt|;
name|int
name|nbRejected
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ManifestAndLocation
name|manifestAndLocation
range|:
name|manifestAndLocations
control|)
block|{
name|BundleInfo
name|bundleInfo
decl_stmt|;
try|try
block|{
name|bundleInfo
operator|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|manifestAndLocation
operator|.
name|getManifest
argument_list|()
argument_list|)
expr_stmt|;
name|bundleInfo
operator|.
name|setUri
argument_list|(
name|manifestAndLocation
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|nbOk
operator|++
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|nbRejected
operator|++
expr_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageLogger
argument_list|()
operator|.
name|log
argument_list|(
literal|"Rejected "
operator|+
name|manifestAndLocation
operator|.
name|getUri
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|level
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|saxBundleInfo
argument_list|(
name|bundleInfo
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|)
expr_stmt|;
name|handler
operator|.
name|endDocument
argument_list|()
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
name|nbOk
operator|+
literal|" bundle"
operator|+
operator|(
name|nbOk
operator|>
literal|1
condition|?
literal|"s"
else|:
literal|""
operator|)
operator|+
literal|" added, "
operator|+
name|nbRejected
operator|+
literal|" bundle"
operator|+
operator|(
name|nbRejected
operator|>
literal|1
condition|?
literal|"s"
else|:
literal|""
operator|)
operator|+
literal|" rejected."
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|writeBundles
parameter_list|(
name|Iterable
argument_list|<
name|BundleInfo
argument_list|>
name|bundleInfos
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|handler
operator|.
name|startDocument
argument_list|()
expr_stmt|;
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|atts
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleInfo
name|bundleInfo
range|:
name|bundleInfos
control|)
block|{
name|saxBundleInfo
argument_list|(
name|bundleInfo
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|,
name|RepositoryHandler
operator|.
name|REPOSITORY
argument_list|)
expr_stmt|;
name|handler
operator|.
name|endDocument
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|saxBundleInfo
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|addAttr
argument_list|(
name|atts
argument_list|,
name|ResourceHandler
operator|.
name|SYMBOLIC_NAME
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|addAttr
argument_list|(
name|atts
argument_list|,
name|ResourceHandler
operator|.
name|VERSION
argument_list|,
name|bundleInfo
operator|.
name|getRawVersion
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bundleInfo
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addAttr
argument_list|(
name|atts
argument_list|,
name|ResourceHandler
operator|.
name|URI
argument_list|,
name|bundleInfo
operator|.
name|getUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|ResourceHandler
operator|.
name|RESOURCE
argument_list|,
name|ResourceHandler
operator|.
name|RESOURCE
argument_list|,
name|atts
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleCapability
name|capability
range|:
name|bundleInfo
operator|.
name|getCapabilities
argument_list|()
control|)
block|{
name|saxCapability
argument_list|(
name|capability
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|BundleRequirement
name|requirement
range|:
name|bundleInfo
operator|.
name|getRequirements
argument_list|()
control|)
block|{
name|saxRequirement
argument_list|(
name|requirement
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|ResourceHandler
operator|.
name|RESOURCE
argument_list|,
name|ResourceHandler
operator|.
name|RESOURCE
argument_list|)
expr_stmt|;
name|handler
operator|.
name|characters
argument_list|(
literal|"\n"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|saxCapability
parameter_list|(
name|BundleCapability
name|capability
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|String
name|type
init|=
name|capability
operator|.
name|getType
argument_list|()
decl_stmt|;
name|addAttr
argument_list|(
name|atts
argument_list|,
name|CapabilityHandler
operator|.
name|NAME
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|CapabilityHandler
operator|.
name|CAPABILITY
argument_list|,
name|CapabilityHandler
operator|.
name|CAPABILITY
argument_list|,
name|atts
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|)
condition|)
block|{
comment|// nothing to do, already handled with the resource tag
block|}
if|else if
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
name|saxCapabilityProperty
argument_list|(
literal|"package"
argument_list|,
name|capability
operator|.
name|getName
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|Version
name|v
init|=
name|capability
operator|.
name|getRawVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|saxCapabilityProperty
argument_list|(
literal|"version"
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|uses
init|=
operator|(
operator|(
name|ExportPackage
operator|)
name|capability
operator|)
operator|.
name|getUses
argument_list|()
decl_stmt|;
if|if
condition|(
name|uses
operator|!=
literal|null
operator|&&
operator|!
name|uses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuffer
name|builder
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|use
range|:
name|uses
control|)
block|{
if|if
condition|(
name|builder
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|use
argument_list|)
expr_stmt|;
block|}
name|saxCapabilityProperty
argument_list|(
literal|"uses"
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|SERVICE_TYPE
argument_list|)
condition|)
block|{
name|saxCapabilityProperty
argument_list|(
literal|"service"
argument_list|,
name|capability
operator|.
name|getName
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|Version
name|v
init|=
name|capability
operator|.
name|getRawVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|saxCapabilityProperty
argument_list|(
literal|"version"
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// oups
block|}
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|CapabilityHandler
operator|.
name|CAPABILITY
argument_list|,
name|CapabilityHandler
operator|.
name|CAPABILITY
argument_list|)
expr_stmt|;
name|handler
operator|.
name|characters
argument_list|(
literal|"\n"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|saxCapabilityProperty
parameter_list|(
name|String
name|n
parameter_list|,
name|String
name|v
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|saxCapabilityProperty
argument_list|(
name|n
argument_list|,
literal|null
argument_list|,
name|v
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|saxCapabilityProperty
parameter_list|(
name|String
name|n
parameter_list|,
name|String
name|t
parameter_list|,
name|String
name|v
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|addAttr
argument_list|(
name|atts
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|NAME
argument_list|,
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|addAttr
argument_list|(
name|atts
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|TYPE
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|addAttr
argument_list|(
name|atts
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|VALUE
argument_list|,
name|v
argument_list|)
expr_stmt|;
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|CAPABILITY_PROPERTY
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|CAPABILITY_PROPERTY
argument_list|,
name|atts
argument_list|)
expr_stmt|;
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|CAPABILITY_PROPERTY
argument_list|,
name|CapabilityPropertyHandler
operator|.
name|CAPABILITY_PROPERTY
argument_list|)
expr_stmt|;
name|handler
operator|.
name|characters
argument_list|(
literal|"\n"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|saxRequirement
parameter_list|(
name|BundleRequirement
name|requirement
parameter_list|,
name|ContentHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|atts
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|addAttr
argument_list|(
name|atts
argument_list|,
name|RequireHandler
operator|.
name|NAME
argument_list|,
name|requirement
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
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
name|addAttr
argument_list|(
name|atts
argument_list|,
name|RequireHandler
operator|.
name|OPTIONAL
argument_list|,
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addAttr
argument_list|(
name|atts
argument_list|,
name|RequireHandler
operator|.
name|FILTER
argument_list|,
name|buildFilter
argument_list|(
name|requirement
argument_list|)
argument_list|)
expr_stmt|;
name|handler
operator|.
name|startElement
argument_list|(
literal|""
argument_list|,
name|RequireHandler
operator|.
name|REQUIRE
argument_list|,
name|RequireHandler
operator|.
name|REQUIRE
argument_list|,
name|atts
argument_list|)
expr_stmt|;
name|handler
operator|.
name|endElement
argument_list|(
literal|""
argument_list|,
name|RequireHandler
operator|.
name|REQUIRE
argument_list|,
name|RequireHandler
operator|.
name|REQUIRE
argument_list|)
expr_stmt|;
name|handler
operator|.
name|characters
argument_list|(
literal|"\n"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|buildFilter
parameter_list|(
name|BundleRequirement
name|requirement
parameter_list|)
block|{
name|StringBuffer
name|filter
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|VersionRange
name|v
init|=
name|requirement
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|appendVersion
argument_list|(
name|filter
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
name|filter
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|requirement
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|requirement
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
return|return
name|filter
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|appendVersion
parameter_list|(
name|StringBuffer
name|filter
parameter_list|,
name|VersionRange
name|v
parameter_list|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|"(&"
argument_list|)
expr_stmt|;
name|Version
name|start
init|=
name|v
operator|.
name|getStartVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|start
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|v
operator|.
name|isStartExclusive
argument_list|()
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|"(version>="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|start
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|.
name|append
argument_list|(
literal|"(!"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"(version<="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|start
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"))"
argument_list|)
expr_stmt|;
block|}
block|}
name|Version
name|end
init|=
name|v
operator|.
name|getEndVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|end
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|v
operator|.
name|isEndExclusive
argument_list|()
condition|)
block|{
name|filter
operator|.
name|append
argument_list|(
literal|"(version<="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|end
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filter
operator|.
name|append
argument_list|(
literal|"(!"
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"(version>="
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
name|end
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|append
argument_list|(
literal|"))"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|addAttr
parameter_list|(
name|AttributesImpl
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|atts
operator|.
name|addAttribute
argument_list|(
literal|""
argument_list|,
name|name
argument_list|,
name|name
argument_list|,
literal|"CDATA"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|addAttr
parameter_list|(
name|AttributesImpl
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|atts
operator|.
name|addAttribute
argument_list|(
literal|""
argument_list|,
name|name
argument_list|,
name|name
argument_list|,
literal|"CDATA"
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

