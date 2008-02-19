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
specifier|private
specifier|static
specifier|final
name|SAXParserFactory
name|VALIDATING_FACTORY
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|SAXParserFactory
name|FACTORY
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
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
name|DocumentBuilder
name|docBuilder
decl_stmt|;
static|static
block|{
name|VALIDATING_FACTORY
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|VALIDATING_FACTORY
operator|.
name|setValidating
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
parameter_list|)
throws|throws
name|ParserConfigurationException
throws|,
name|SAXException
block|{
if|if
condition|(
operator|!
name|canUseSchemaValidation
operator|||
name|schema
operator|==
literal|null
condition|)
block|{
return|return
name|FACTORY
operator|.
name|newSAXParser
argument_list|()
return|;
block|}
try|try
block|{
name|SAXParser
name|parser
init|=
name|VALIDATING_FACTORY
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
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
catch|catch
parameter_list|(
name|SAXNotRecognizedException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WARNING: problem while setting JAXP validating property on SAXParser... "
operator|+
literal|"XML validation will not be done: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|canUseSchemaValidation
operator|=
literal|false
expr_stmt|;
return|return
name|FACTORY
operator|.
name|newSAXParser
argument_list|()
return|;
block|}
block|}
comment|// IMPORTANT: validation errors are only notified to the given handler, and
comment|// do not cause exception
comment|// implement warning error and fatalError methods in handler to be informed
comment|// of validation errors
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
name|parse
argument_list|(
name|xmlStream
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"WARNING: problem while setting the lexical handler property on SAXParser: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
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
comment|/**      * Escapes invalid XML characters in the given character data using XML entities.      * For the moment, only the following characters are being escaped: (<), (&), (')       * and (").      *       * Remark: we don't escape the (>) character to keep the readability of the      * configuration mapping! The XML spec only requires that the (&) and (<)      * characters are being escaped inside character data.      *       * @param text the character data to escape      * @return the escaped character data      */
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
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|(
name|text
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|text
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|chars
index|[
name|i
index|]
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
name|chars
index|[
name|i
index|]
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
name|URL
name|descriptorURL
parameter_list|,
name|Resource
name|res
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
argument_list|()
decl_stmt|;
name|InputStream
name|pomStream
init|=
name|res
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|Document
name|pomDomDoc
decl_stmt|;
try|try
block|{
name|pomDomDoc
operator|=
name|docBuilder
operator|.
name|parse
argument_list|(
name|pomStream
argument_list|,
name|res
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|pomStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|pomDomDoc
return|;
block|}
specifier|public
specifier|static
name|DocumentBuilder
name|getDocBuilder
parameter_list|()
block|{
if|if
condition|(
name|docBuilder
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|docBuilder
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
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
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|docBuilder
return|;
block|}
specifier|private
name|XMLHelper
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

