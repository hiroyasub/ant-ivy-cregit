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
name|util
package|;
end_package

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
name|util
operator|.
name|cli
operator|.
name|ParseException
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
name|DTDHandler
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
name|ErrorHandler
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
name|SAXParseException
import|;
end_import

begin_class
specifier|public
class|class
name|DelegetingHandler
parameter_list|<
name|P
extends|extends
name|DelegetingHandler
parameter_list|<
name|?
parameter_list|>
parameter_list|>
implements|implements
name|DTDHandler
implements|,
name|ContentHandler
implements|,
name|ErrorHandler
block|{
specifier|private
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
name|delegate
init|=
literal|null
decl_stmt|;
specifier|private
name|P
name|parent
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
argument_list|>
name|mapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|String
name|tagName
decl_stmt|;
specifier|private
name|boolean
name|started
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|skip
init|=
literal|false
decl_stmt|;
specifier|private
name|StringBuilder
name|charBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|bufferingChar
init|=
literal|false
decl_stmt|;
specifier|private
name|Locator
name|locator
decl_stmt|;
specifier|public
name|DelegetingHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|P
name|parent
parameter_list|)
block|{
name|this
operator|.
name|tagName
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|mapping
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|charBuffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|tagName
return|;
block|}
specifier|public
name|P
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|setBufferingChar
parameter_list|(
name|boolean
name|bufferingChar
parameter_list|)
block|{
name|this
operator|.
name|bufferingChar
operator|=
name|bufferingChar
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBufferingChar
parameter_list|()
block|{
return|return
name|bufferingChar
return|;
block|}
specifier|public
name|String
name|getBufferedChars
parameter_list|()
block|{
return|return
name|charBuffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setDocumentLocator
parameter_list|(
name|Locator
name|locator
parameter_list|)
block|{
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
for|for
control|(
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
name|subHandler
range|:
name|mapping
operator|.
name|values
argument_list|()
control|)
block|{
name|subHandler
operator|.
name|setDocumentLocator
argument_list|(
name|locator
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Locator
name|getLocator
parameter_list|()
block|{
return|return
name|locator
return|;
block|}
specifier|public
name|void
name|skip
parameter_list|()
block|{
name|skip
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
name|subHandler
range|:
name|mapping
operator|.
name|values
argument_list|()
control|)
block|{
name|subHandler
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|reset
parameter_list|()
block|{
name|parent
operator|.
name|delegate
operator|=
literal|null
expr_stmt|;
name|skip
operator|=
literal|false
expr_stmt|;
name|started
operator|=
literal|false
expr_stmt|;
name|charBuffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
name|subHandler
range|:
name|mapping
operator|.
name|values
argument_list|()
control|)
block|{
name|subHandler
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|void
name|startDocument
parameter_list|()
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|startDocument
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doStartDocument
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doStartDocument
parameter_list|()
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|endDocument
parameter_list|()
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|endDocument
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doEndDocument
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doEndDocument
parameter_list|()
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|n
parameter_list|,
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|startElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|n
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|started
condition|)
block|{
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|tagName
argument_list|)
condition|)
block|{
name|handleAttributes
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
comment|// we are at the root and the saxed element doesn't match
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"The root element of the parsed document '"
operator|+
name|localName
operator|+
literal|"' didn't matched the expected one: '"
operator|+
name|tagName
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|mapping
operator|!=
literal|null
condition|)
block|{
name|DelegetingHandler
argument_list|<
name|?
argument_list|>
name|delegetingHandler
init|=
name|mapping
operator|.
name|get
argument_list|(
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|delegetingHandler
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|=
name|delegetingHandler
expr_stmt|;
block|}
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|startElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|n
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doStartElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|localName
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Called when the expected node is achieved      *       * @param atts      *            the xml attributes attached to the expected node      * @exception SAXException      *                in case the parsing should be completely stopped      */
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
comment|// nothing to do by default
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doStartElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|,
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|n
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|endElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|skip
condition|)
block|{
name|doEndElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|tagName
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @throws SAXException      */
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
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|characters
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|characters
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doCharacters
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doCharacters
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|bufferingChar
condition|)
block|{
name|charBuffer
operator|.
name|append
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|final
name|void
name|startPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|startPrefixMapping
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doStartPrefixMapping
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doStartPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|endPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|endPrefixMapping
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doEndPrefixMapping
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doEndPrefixMapping
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|ignorableWhitespace
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|ignorableWhitespace
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doIgnorableWhitespace
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doIgnorableWhitespace
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|notationDecl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|notationDecl
argument_list|(
name|name
argument_list|,
name|publicId
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doNotationDecl
argument_list|(
name|name
argument_list|,
name|publicId
argument_list|,
name|systemId
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doNotationDecl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|processingInstruction
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|processingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doProcessingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doProcessingInstruction
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|skippedEntity
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|skippedEntity
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doSkippedEntity
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doSkippedEntity
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
comment|/**      * @throws SAXException      */
specifier|public
specifier|final
name|void
name|unparsedEntityDecl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|notationName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|unparsedEntityDecl
argument_list|(
name|name
argument_list|,
name|publicId
argument_list|,
name|systemId
argument_list|,
name|notationName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doUnparsedEntityDecl
argument_list|(
name|name
argument_list|,
name|publicId
argument_list|,
name|systemId
argument_list|,
name|notationName
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doUnparsedEntityDecl
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|publicId
parameter_list|,
name|String
name|systemId
parameter_list|,
name|String
name|notationName
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
comment|// ERROR HANDLING
specifier|public
specifier|final
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|warning
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doWarning
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doWarning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|error
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
specifier|public
specifier|final
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|fatalError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doFatalError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @throws SAXException      */
specifier|protected
name|void
name|doFatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// by default do nothing
block|}
block|}
end_class

end_unit

