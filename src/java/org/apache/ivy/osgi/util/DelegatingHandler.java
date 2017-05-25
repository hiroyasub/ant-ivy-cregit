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
name|Locale
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
class|class
name|DelegatingHandler
extends|extends
name|DefaultHandler
implements|implements
name|DTDHandler
implements|,
name|ContentHandler
implements|,
name|ErrorHandler
block|{
specifier|private
name|DelegatingHandler
name|delegate
init|=
literal|null
decl_stmt|;
name|DelegatingHandler
name|parent
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DelegatingHandler
argument_list|>
name|saxHandlerMapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DelegatingHandler
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ChildElementHandler
argument_list|<
name|?
argument_list|>
argument_list|>
name|childHandlerMapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DelegatingHandler
operator|.
name|ChildElementHandler
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
name|boolean
name|skipOnError
init|=
literal|false
decl_stmt|;
specifier|private
name|StringBuffer
name|charBuffer
init|=
operator|new
name|StringBuffer
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
name|DelegatingHandler
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|tagName
operator|=
name|name
expr_stmt|;
name|charBuffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
parameter_list|<
name|DH
extends|extends
name|DelegatingHandler
parameter_list|>
name|void
name|addChild
parameter_list|(
name|DH
name|saxHandler
parameter_list|,
name|ChildElementHandler
argument_list|<
name|DH
argument_list|>
name|elementHandler
parameter_list|)
block|{
name|saxHandlerMapping
operator|.
name|put
argument_list|(
name|saxHandler
operator|.
name|getName
argument_list|()
argument_list|,
name|saxHandler
argument_list|)
expr_stmt|;
name|childHandlerMapping
operator|.
name|put
argument_list|(
name|saxHandler
operator|.
name|getName
argument_list|()
argument_list|,
name|elementHandler
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|parent
operator|=
name|this
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
name|DelegatingHandler
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
name|void
name|setSkipOnError
parameter_list|(
name|boolean
name|skipOnError
parameter_list|)
block|{
name|this
operator|.
name|skipOnError
operator|=
name|skipOnError
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
annotation|@
name|Override
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
name|DelegatingHandler
name|subHandler
range|:
name|saxHandlerMapping
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
comment|/**      * Return an sort of identifier of the current element being parsed. It will only be used for      * logging purpose.      *       * @return an empty string by default      */
specifier|protected
name|String
name|getCurrentElementIdentifier
parameter_list|()
block|{
return|return
literal|""
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
name|DelegatingHandler
name|subHandler
range|:
name|saxHandlerMapping
operator|.
name|values
argument_list|()
control|)
block|{
name|subHandler
operator|.
name|stopDelegating
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|stopDelegating
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
for|for
control|(
name|DelegatingHandler
comment|/*<?> */
name|subHandler
range|:
name|saxHandlerMapping
operator|.
name|values
argument_list|()
control|)
block|{
name|subHandler
operator|.
name|stopDelegating
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
interface|interface
name|SkipOnErrorCallback
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
function_decl|;
block|}
specifier|private
name|void
name|skipOnError
parameter_list|(
name|SkipOnErrorCallback
name|callback
parameter_list|)
throws|throws
name|SAXException
block|{
try|try
block|{
name|callback
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
if|if
condition|(
name|skipOnError
condition|)
block|{
name|skip
argument_list|()
expr_stmt|;
name|log
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|startElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
name|String
name|n
parameter_list|,
specifier|final
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// reset the char buffer
name|charBuffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
comment|// we are already delegating, let's continue
name|skipOnError
argument_list|(
operator|new
name|SkipOnErrorCallback
argument_list|()
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
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
block|}
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
comment|// first time called ?
comment|// just for the root, check the expected element name
comment|// not need to check the delegated as the mapping is already taking care of it
if|if
condition|(
name|parent
operator|==
literal|null
operator|&&
operator|!
name|localName
operator|.
name|equals
argument_list|(
name|tagName
argument_list|)
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
name|skipOnError
argument_list|(
operator|new
name|SkipOnErrorCallback
argument_list|()
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
block|{
name|handleAttributes
argument_list|(
name|atts
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|skip
condition|)
block|{
comment|// we con't care anymore about that part of the xml tree
return|return;
block|}
comment|// time now to delegate for a new element
name|delegate
operator|=
name|saxHandlerMapping
operator|.
name|get
argument_list|(
name|localName
argument_list|)
expr_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|skipOnError
argument_list|(
operator|new
name|SkipOnErrorCallback
argument_list|()
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
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
block|}
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
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|endElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
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
specifier|final
name|DelegatingHandler
name|savedDelegate
init|=
name|delegate
decl_stmt|;
comment|// we are already delegating, let's continue
name|skipOnError
argument_list|(
operator|new
name|SkipOnErrorCallback
argument_list|()
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
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
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
comment|// we just stopped delegating, it means that the child has ended
specifier|final
name|ChildElementHandler
argument_list|<
name|?
argument_list|>
name|childHandler
init|=
name|childHandlerMapping
operator|.
name|get
argument_list|(
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|childHandler
operator|!=
literal|null
condition|)
block|{
name|skipOnError
argument_list|(
operator|new
name|SkipOnErrorCallback
argument_list|()
block|{
specifier|public
name|void
name|call
parameter_list|()
throws|throws
name|SAXException
block|{
name|childHandler
operator|.
name|_childHanlded
argument_list|(
name|savedDelegate
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// the current element is closed, let's tell the parent to stop delegating
name|stopDelegating
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
specifier|static
specifier|abstract
class|class
name|ChildElementHandler
parameter_list|<
name|DH
extends|extends
name|DelegatingHandler
parameter_list|>
block|{
specifier|public
specifier|abstract
name|void
name|childHanlded
parameter_list|(
name|DH
name|child
parameter_list|)
throws|throws
name|SAXParseException
function_decl|;
comment|// because we know what we're doing
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|_childHanlded
parameter_list|(
name|DelegatingHandler
name|delegate
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|childHanlded
argument_list|(
operator|(
name|DH
operator|)
name|delegate
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
comment|// //////////////////////
comment|// Functions related to error handling
comment|// //////////////////////
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|logLevel
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|logLevel
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|log
parameter_list|(
name|int
name|logLevel
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|Message
operator|.
name|log
argument_list|(
name|logLevel
argument_list|,
name|getLocation
argument_list|(
name|getLocator
argument_list|()
argument_list|)
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
specifier|private
name|void
name|skipOnError
parameter_list|(
name|DelegatingHandler
name|currentHandler
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|DelegatingHandler
argument_list|>
name|handlerClassToSkip
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|DelegatingHandler
name|handlerToSkip
init|=
name|currentHandler
decl_stmt|;
while|while
condition|(
operator|!
operator|(
name|handlerClassToSkip
operator|.
name|isAssignableFrom
argument_list|(
name|handlerToSkip
operator|.
name|getClass
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|handlerToSkip
operator|=
name|handlerToSkip
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
name|Message
operator|.
name|MSG_ERR
argument_list|,
name|message
operator|+
literal|". The '"
operator|+
name|handlerToSkip
operator|.
name|getName
argument_list|()
operator|+
literal|"' element "
operator|+
name|getCurrentElementIdentifier
argument_list|()
operator|+
literal|" is then ignored."
argument_list|)
expr_stmt|;
name|handlerToSkip
operator|.
name|skip
argument_list|()
expr_stmt|;
block|}
comment|// //////////////////////
comment|// Helpers to parse the attributes
comment|// //////////////////////
specifier|protected
name|String
name|getRequiredAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|String
name|value
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
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"Required attribute '"
operator|+
name|name
operator|+
literal|"' not found"
argument_list|,
name|getLocator
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
specifier|protected
name|String
name|getOptionalAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|value
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
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|value
return|;
block|}
specifier|protected
name|int
name|getRequiredIntAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|Integer
name|logLevel
parameter_list|)
throws|throws
name|SAXParseException
block|{
return|return
name|parseInt
argument_list|(
name|name
argument_list|,
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Integer
name|getOptionalIntAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|Integer
name|defaultValue
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|String
name|value
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
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
operator|new
name|Integer
argument_list|(
name|parseInt
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|int
name|parseInt
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SAXParseException
block|{
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"Attribute '"
operator|+
name|name
operator|+
literal|"' is expected to be an integer but was '"
operator|+
name|value
operator|+
literal|"' ("
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
block|}
specifier|protected
name|long
name|getRequiredLongAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXParseException
block|{
return|return
name|parseLong
argument_list|(
name|name
argument_list|,
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Long
name|getOptionalLongAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|Long
name|defaultValue
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|String
name|value
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
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
operator|new
name|Long
argument_list|(
name|parseLong
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|long
name|parseLong
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SAXParseException
block|{
try|try
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"Attribute '"
operator|+
name|name
operator|+
literal|"' is expected to be an long but was '"
operator|+
name|value
operator|+
literal|"' ("
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
block|}
specifier|protected
name|boolean
name|getRequiredBooleanAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|SAXParseException
block|{
return|return
name|parseBoolean
argument_list|(
name|name
argument_list|,
name|getRequiredAttribute
argument_list|(
name|atts
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Boolean
name|getOptionalBooleanAttribute
parameter_list|(
name|Attributes
name|atts
parameter_list|,
name|String
name|name
parameter_list|,
name|Boolean
name|defaultValue
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|String
name|value
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
name|value
operator|==
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|parseBoolean
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|static
specifier|final
name|String
name|TRUE
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|FALSE
init|=
name|Boolean
operator|.
name|FALSE
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|parseBoolean
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|SAXParseException
block|{
name|String
name|lowerValue
init|=
name|value
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
name|lowerValue
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|lowerValue
operator|.
name|equals
argument_list|(
name|FALSE
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"Attribute '"
operator|+
name|name
operator|+
literal|"' is expected to be a boolean but was '"
operator|+
name|value
operator|+
literal|"'"
argument_list|,
name|getLocator
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

