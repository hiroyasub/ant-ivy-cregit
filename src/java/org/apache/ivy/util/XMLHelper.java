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
name|util
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
name|net
operator|.
name|URL
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
name|DocumentBuilder
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
name|DocumentBuilderFactory
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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
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
name|SAXParserFactory
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
name|url
operator|.
name|URLHandlerRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|EntityResolver
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
name|SAXNotRecognizedException
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
name|ext
operator|.
name|LexicalHandler
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
name|DefaultHandler
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|XMLHelper
block|{
specifier|static
specifier|final
name|String
name|JAXP_SCHEMA_LANGUAGE
init|=
literal|"http://java.sun.com/xml/jaxp/properties/schemaLanguage"
decl_stmt|;
specifier|static
specifier|final
name|String
name|JAXP_SCHEMA_SOURCE
init|=
literal|"http://java.sun.com/xml/jaxp/properties/schemaSource"
decl_stmt|;
specifier|static
specifier|final
name|String
name|XERCES_LOAD_EXTERNAL_DTD
init|=
literal|"http://apache.org/xml/features/nonvalidating/load-external-dtd"
decl_stmt|;
specifier|static
specifier|final
name|String
name|XML_NAMESPACE_PREFIXES
init|=
literal|"http://xml.org/sax/features/namespace-prefixes"
decl_stmt|;
specifier|static
specifier|final
name|String
name|W3C_XML_SCHEMA
init|=
literal|"http://www.w3.org/2001/XMLSchema"
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|canUseSchemaValidation
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
name|Boolean
name|canDisableExternalDtds
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
name|SAXParser
name|newSAXParser
parameter_list|(
name|URL
name|schema
parameter_list|,
name|InputStream
name|schemaStream
parameter_list|,
name|boolean
name|loadExternalDtds
parameter_list|)
throws|throws
name|ParserConfigurationException
throws|,
name|SAXException
block|{
name|SAXParserFactory
name|parserFactory
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|parserFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|parserFactory
operator|.
name|setValidating
argument_list|(
name|canUseSchemaValidation
operator|&&
operator|(
name|schema
operator|!=
literal|null
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|loadExternalDtds
operator|&&
name|canDisableExternalDtds
argument_list|(
name|parserFactory
argument_list|)
condition|)
block|{
name|parserFactory
operator|.
name|setFeature
argument_list|(
name|XERCES_LOAD_EXTERNAL_DTD
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|SAXParser
name|parser
init|=
name|parserFactory
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
if|if
condition|(
name|canUseSchemaValidation
operator|&&
name|schema
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|parser
operator|.
name|setProperty
argument_list|(
name|JAXP_SCHEMA_LANGUAGE
argument_list|,
name|W3C_XML_SCHEMA
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setProperty
argument_list|(
name|JAXP_SCHEMA_SOURCE
argument_list|,
name|schemaStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXNotRecognizedException
name|ex
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while setting JAXP validating property on SAXParser... "
operator|+
literal|"XML validation will not be done"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|canUseSchemaValidation
operator|=
literal|false
expr_stmt|;
name|parserFactory
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|parser
operator|=
name|parserFactory
operator|.
name|newSAXParser
argument_list|()
expr_stmt|;
block|}
block|}
name|parser
operator|.
name|getXMLReader
argument_list|()
operator|.
name|setFeature
argument_list|(
name|XML_NAMESPACE_PREFIXES
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|parser
return|;
block|}
specifier|private
specifier|static
name|boolean
name|canDisableExternalDtds
parameter_list|(
name|SAXParserFactory
name|parserFactory
parameter_list|)
block|{
if|if
condition|(
name|canDisableExternalDtds
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|parserFactory
operator|.
name|getFeature
argument_list|(
name|XERCES_LOAD_EXTERNAL_DTD
argument_list|)
expr_stmt|;
name|canDisableExternalDtds
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|canDisableExternalDtds
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
return|return
name|canDisableExternalDtds
return|;
block|}
comment|/**      * Convert an URL to a valid systemId according to RFC 2396.      *      * @param url URL      * @return String      */
specifier|public
specifier|static
name|String
name|toSystemId
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
operator|.
name|toASCIIString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
return|return
name|url
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
block|}
comment|// IMPORTANT: validation errors are only notified to the given handler, and
comment|// do not cause exception implement warning error and fatalError methods in
comment|// handler to be informed of validation errors
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|URL
name|xmlURL
parameter_list|,
name|URL
name|schema
parameter_list|,
name|DefaultHandler
name|handler
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|parse
argument_list|(
name|xmlURL
argument_list|,
name|schema
argument_list|,
name|handler
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|URL
name|xmlURL
parameter_list|,
name|URL
name|schema
parameter_list|,
name|DefaultHandler
name|handler
parameter_list|,
name|LexicalHandler
name|lHandler
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|xmlStream
init|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|openStream
argument_list|(
name|xmlURL
argument_list|)
decl_stmt|;
try|try
block|{
name|InputSource
name|inSrc
init|=
operator|new
name|InputSource
argument_list|(
name|xmlStream
argument_list|)
decl_stmt|;
name|inSrc
operator|.
name|setSystemId
argument_list|(
name|toSystemId
argument_list|(
name|xmlURL
argument_list|)
argument_list|)
expr_stmt|;
name|parse
argument_list|(
name|inSrc
argument_list|,
name|schema
argument_list|,
name|handler
argument_list|,
name|lHandler
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|xmlStream
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
comment|// ignored
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|InputStream
name|xmlStream
parameter_list|,
name|URL
name|schema
parameter_list|,
name|DefaultHandler
name|handler
parameter_list|,
name|LexicalHandler
name|lHandler
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|xmlStream
argument_list|)
argument_list|,
name|schema
argument_list|,
name|handler
argument_list|,
name|lHandler
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|InputSource
name|xmlStream
parameter_list|,
name|URL
name|schema
parameter_list|,
name|DefaultHandler
name|handler
parameter_list|,
name|LexicalHandler
name|lHandler
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|parse
argument_list|(
name|xmlStream
argument_list|,
name|schema
argument_list|,
name|handler
argument_list|,
name|lHandler
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
specifier|static
name|void
name|parse
parameter_list|(
name|InputSource
name|xmlStream
parameter_list|,
name|URL
name|schema
parameter_list|,
name|DefaultHandler
name|handler
parameter_list|,
name|LexicalHandler
name|lHandler
parameter_list|,
name|boolean
name|loadExternalDtds
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|schemaStream
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|schemaStream
operator|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|openStream
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
name|SAXParser
name|parser
init|=
name|XMLHelper
operator|.
name|newSAXParser
argument_list|(
name|schema
argument_list|,
name|schemaStream
argument_list|,
name|loadExternalDtds
argument_list|)
decl_stmt|;
if|if
condition|(
name|lHandler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|parser
operator|.
name|setProperty
argument_list|(
literal|"http://xml.org/sax/properties/lexical-handler"
argument_list|,
name|lHandler
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|ex
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while setting the lexical handler property on SAXParser"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// continue without the lexical handler
block|}
block|}
name|parser
operator|.
name|parse
argument_list|(
name|xmlStream
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|schemaStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|schemaStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignored
block|}
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|canUseSchemaValidation
parameter_list|()
block|{
return|return
name|canUseSchemaValidation
return|;
block|}
comment|/**      * Escapes invalid XML characters in the given character data using XML entities. For the      * moment, only the following characters are being escaped: (&lt;), (&amp;), (') and (&quot;).      *      * Remark: we don't escape the (&gt;) character to keep the readability of the configuration      * mapping! The XML spec only requires that the (&amp;) and (&lt;) characters are being escaped      * inside character data.      *      * @param text      *            the character data to escape      * @return the escaped character data      */
specifier|public
specifier|static
name|String
name|escape
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|(
name|text
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|char
name|ch
range|:
name|text
operator|.
name|toCharArray
argument_list|()
control|)
block|{
switch|switch
condition|(
name|ch
condition|)
block|{
case|case
literal|'&'
case|:
name|result
operator|.
name|append
argument_list|(
literal|"&amp;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'<'
case|:
name|result
operator|.
name|append
argument_list|(
literal|"&lt;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\''
case|:
name|result
operator|.
name|append
argument_list|(
literal|"&apos;"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'\"'
case|:
name|result
operator|.
name|append
argument_list|(
literal|"&quot;"
argument_list|)
expr_stmt|;
break|break;
default|default:
name|result
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Document
name|parseToDom
parameter_list|(
name|InputSource
name|source
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|DocumentBuilder
name|docBuilder
init|=
name|getDocBuilder
argument_list|(
name|entityResolver
argument_list|)
decl_stmt|;
return|return
name|docBuilder
operator|.
name|parse
argument_list|(
name|source
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DocumentBuilder
name|getDocBuilder
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
try|try
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|docBuilder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityResolver
operator|!=
literal|null
condition|)
block|{
name|docBuilder
operator|.
name|setEntityResolver
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
block|}
return|return
name|docBuilder
return|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|XMLHelper
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

