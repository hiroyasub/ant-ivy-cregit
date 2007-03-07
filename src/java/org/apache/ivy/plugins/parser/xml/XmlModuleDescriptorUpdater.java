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
name|plugins
operator|.
name|parser
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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|FileOutputStream
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|Ivy
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
name|core
operator|.
name|settings
operator|.
name|IvySettings
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
name|namespace
operator|.
name|Namespace
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
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|extendable
operator|.
name|ExtendableItemHelper
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

begin_comment
comment|/**  * Used to update ivy files. Uses ivy file as source and not ModuleDescriptor to preserve  * as much as possible the original syntax  *   * @author Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|XmlModuleDescriptorUpdater
block|{
specifier|public
specifier|static
name|String
name|LINE_SEPARATOR
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
comment|/**      * used to copy a module descriptor xml file (also known as ivy file)      * and update the revisions of its dependencies, its status and revision      *       * @param srcURL the url of the source module descriptor file      * @param destFile The file to which the updated module descriptor should be output      * @param resolvedRevisions Map from ModuleId of dependencies to new revision (as String)      * @param status the new status, null to keep the old one      * @param revision the new revision, null to keep the old one      */
specifier|public
specifier|static
name|void
name|update
parameter_list|(
name|URL
name|srcURL
parameter_list|,
name|File
name|destFile
parameter_list|,
specifier|final
name|Map
name|resolvedRevisions
parameter_list|,
specifier|final
name|String
name|status
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|Date
name|pubdate
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|update
argument_list|(
literal|null
argument_list|,
name|srcURL
argument_list|,
name|destFile
argument_list|,
name|resolvedRevisions
argument_list|,
name|status
argument_list|,
name|revision
argument_list|,
name|pubdate
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|update
parameter_list|(
specifier|final
name|IvySettings
name|settings
parameter_list|,
name|URL
name|srcURL
parameter_list|,
name|File
name|destFile
parameter_list|,
specifier|final
name|Map
name|resolvedRevisions
parameter_list|,
specifier|final
name|String
name|status
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|Date
name|pubdate
parameter_list|,
specifier|final
name|Namespace
name|ns
parameter_list|,
specifier|final
name|boolean
name|replaceInclude
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|update
argument_list|(
name|settings
argument_list|,
name|srcURL
operator|.
name|openStream
argument_list|()
argument_list|,
name|destFile
argument_list|,
name|resolvedRevisions
argument_list|,
name|status
argument_list|,
name|revision
argument_list|,
name|pubdate
argument_list|,
name|ns
argument_list|,
name|replaceInclude
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|update
parameter_list|(
specifier|final
name|IvySettings
name|settings
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|File
name|destFile
parameter_list|,
specifier|final
name|Map
name|resolvedRevisions
parameter_list|,
specifier|final
name|String
name|status
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|Date
name|pubdate
parameter_list|,
specifier|final
name|Namespace
name|ns
parameter_list|,
specifier|final
name|boolean
name|replaceInclude
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
if|if
condition|(
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|OutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|destFile
argument_list|)
decl_stmt|;
try|try
block|{
name|update
argument_list|(
name|settings
argument_list|,
name|in
argument_list|,
name|fos
argument_list|,
name|resolvedRevisions
argument_list|,
name|status
argument_list|,
name|revision
argument_list|,
name|pubdate
argument_list|,
name|ns
argument_list|,
name|replaceInclude
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
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
block|}
try|try
block|{
name|fos
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
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|UpdaterHandler
extends|extends
name|DefaultHandler
implements|implements
name|LexicalHandler
block|{
specifier|private
specifier|final
name|IvySettings
name|settings
decl_stmt|;
specifier|private
specifier|final
name|PrintWriter
name|out
decl_stmt|;
specifier|private
specifier|final
name|Map
name|resolvedRevisions
decl_stmt|;
specifier|private
specifier|final
name|String
name|status
decl_stmt|;
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
specifier|private
specifier|final
name|Date
name|pubdate
decl_stmt|;
specifier|private
specifier|final
name|Namespace
name|ns
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|replaceInclude
decl_stmt|;
specifier|private
name|boolean
name|inHeader
init|=
literal|true
decl_stmt|;
specifier|public
name|UpdaterHandler
parameter_list|(
specifier|final
name|IvySettings
name|settings
parameter_list|,
specifier|final
name|PrintWriter
name|out
parameter_list|,
specifier|final
name|Map
name|resolvedRevisions
parameter_list|,
specifier|final
name|String
name|status
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|Date
name|pubdate
parameter_list|,
specifier|final
name|Namespace
name|ns
parameter_list|,
specifier|final
name|boolean
name|replaceInclude
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|resolvedRevisions
operator|=
name|resolvedRevisions
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|pubdate
operator|=
name|pubdate
expr_stmt|;
name|this
operator|.
name|ns
operator|=
name|ns
expr_stmt|;
name|this
operator|.
name|replaceInclude
operator|=
name|replaceInclude
expr_stmt|;
block|}
comment|// never print *ln* cause \n is found in copied characters stream
comment|// nor do we need do handle indentation, original one is maintained except for attributes
specifier|private
name|String
name|_organisation
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_defaultConfMapping
init|=
literal|null
decl_stmt|;
comment|// defaultConfMapping of imported configurations, if any
specifier|private
name|Boolean
name|_confMappingOverride
init|=
literal|null
decl_stmt|;
comment|// confMappingOverride of imported configurations, if any
specifier|private
name|String
name|_justOpen
init|=
literal|null
decl_stmt|;
comment|// used to know if the last open tag was empty, to adjust termination with /> instead of></qName>
specifier|private
name|Stack
name|_context
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
specifier|public
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
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
name|inHeader
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|_justOpen
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|_context
operator|.
name|push
argument_list|(
name|qName
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"info"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|_organisation
operator|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"organisation"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"<info organisation=\""
operator|+
name|_organisation
operator|+
literal|"\" module=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"module"
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" revision=\""
operator|+
name|revision
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"revision"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" revision=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"revision"
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" status=\""
operator|+
name|status
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
literal|" status=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"status"
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pubdate
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" publication=\""
operator|+
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|pubdate
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"publication"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" publication=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"publication"
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|Collection
name|stdAtts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"organisation"
block|,
literal|"module"
block|,
literal|"revision"
block|,
literal|"status"
block|,
literal|"publication"
block|,
literal|"namespace"
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"namespace"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" namespace=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"namespace"
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|stdAtts
operator|.
name|contains
argument_list|(
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
operator|+
literal|"=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|replaceInclude
operator|&&
literal|"include"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
operator|&&
name|_context
operator|.
name|contains
argument_list|(
literal|"configurations"
argument_list|)
condition|)
block|{
try|try
block|{
name|URL
name|url
decl_stmt|;
name|String
name|fileName
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"file"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileName
operator|==
literal|null
condition|)
block|{
name|String
name|urlStr
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"url"
argument_list|)
argument_list|)
decl_stmt|;
name|url
operator|=
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
name|XMLHelper
operator|.
name|parse
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
block|{
name|boolean
name|_first
init|=
literal|true
decl_stmt|;
specifier|public
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
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
literal|"configurations"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|defaultconf
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"defaultconfmapping"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultconf
operator|!=
literal|null
condition|)
block|{
name|_defaultConfMapping
operator|=
name|defaultconf
expr_stmt|;
block|}
name|String
name|mappingOverride
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"confmappingoverride"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappingOverride
operator|!=
literal|null
condition|)
block|{
name|_confMappingOverride
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|mappingOverride
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
literal|"conf"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
comment|// copy
if|if
condition|(
operator|!
name|_first
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"/>\n\t\t"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|_first
operator|=
literal|false
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|"<"
operator|+
name|qName
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
operator|+
literal|"=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"exception occured while importing configurations: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SAXException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|else if
condition|(
literal|"dependency"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"<dependency"
argument_list|)
expr_stmt|;
name|String
name|org
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"org"
argument_list|)
argument_list|)
decl_stmt|;
name|org
operator|=
name|org
operator|==
literal|null
condition|?
name|_organisation
else|:
name|org
expr_stmt|;
name|String
name|module
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"branch"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|revision
init|=
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"rev"
argument_list|)
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|localMid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|ExtendableItemHelper
operator|.
name|getExtraAttributes
argument_list|(
name|attributes
argument_list|,
name|XmlModuleDescriptorParser
operator|.
name|DEPENDENCY_REGULAR_ATTRIBUTES
argument_list|)
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|systemMid
init|=
name|ns
operator|==
literal|null
condition|?
name|localMid
else|:
name|ns
operator|.
name|getToSystemTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|localMid
argument_list|)
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
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|attName
init|=
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"rev"
operator|.
name|equals
argument_list|(
name|attName
argument_list|)
condition|)
block|{
name|String
name|rev
init|=
operator|(
name|String
operator|)
name|resolvedRevisions
operator|.
name|get
argument_list|(
name|systemMid
argument_list|)
decl_stmt|;
if|if
condition|(
name|rev
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" rev=\""
operator|+
name|rev
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
literal|" rev=\""
operator|+
name|systemMid
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
literal|"org"
operator|.
name|equals
argument_list|(
name|attName
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" org=\""
operator|+
name|systemMid
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"name"
operator|.
name|equals
argument_list|(
name|attName
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" name=\""
operator|+
name|systemMid
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"branch"
operator|.
name|equals
argument_list|(
name|attName
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" branch=\""
operator|+
name|systemMid
operator|.
name|getBranch
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|attName
operator|+
literal|"=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|attName
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
literal|"dependencies"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
comment|// copy
name|out
operator|.
name|print
argument_list|(
literal|"<"
operator|+
name|qName
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
operator|+
literal|"=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
comment|// add default conf mapping if needed
if|if
condition|(
name|_defaultConfMapping
operator|!=
literal|null
operator|&&
name|attributes
operator|.
name|getValue
argument_list|(
literal|"defaultconfmapping"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" defaultconfmapping=\""
operator|+
name|_defaultConfMapping
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
comment|// add confmappingoverride if needed
if|if
condition|(
name|_confMappingOverride
operator|!=
literal|null
operator|&&
name|attributes
operator|.
name|getValue
argument_list|(
literal|"confmappingoverride"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" confmappingoverride=\""
operator|+
name|_confMappingOverride
operator|.
name|toString
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// copy
name|out
operator|.
name|print
argument_list|(
literal|"<"
operator|+
name|qName
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" "
operator|+
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
operator|+
literal|"=\""
operator|+
name|substitute
argument_list|(
name|settings
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
name|_justOpen
operator|=
name|qName
expr_stmt|;
comment|//            indent.append("\t");
block|}
specifier|private
name|String
name|substitute
parameter_list|(
name|IvySettings
name|ivy
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|ivy
operator|==
literal|null
condition|?
name|value
else|:
name|ivy
operator|.
name|substitute
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
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
name|_justOpen
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|_justOpen
operator|=
literal|null
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|start
operator|+
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|ch
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
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
name|qName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|qName
operator|.
name|equals
argument_list|(
name|_justOpen
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|print
argument_list|(
literal|"</"
operator|+
name|qName
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
name|_justOpen
operator|=
literal|null
expr_stmt|;
name|_context
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|endDocument
parameter_list|()
throws|throws
name|SAXException
block|{
name|out
operator|.
name|print
argument_list|(
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|e
throw|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|e
throw|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|e
throw|;
block|}
specifier|public
name|void
name|endCDATA
parameter_list|()
throws|throws
name|SAXException
block|{
block|}
specifier|public
name|void
name|endDTD
parameter_list|()
throws|throws
name|SAXException
block|{
block|}
specifier|public
name|void
name|startCDATA
parameter_list|()
throws|throws
name|SAXException
block|{
block|}
specifier|public
name|void
name|comment
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
operator|!
name|inHeader
condition|)
block|{
name|StringBuffer
name|comment
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|comment
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
name|out
operator|.
name|print
argument_list|(
literal|"<!--"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|comment
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"-->"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|endEntity
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
block|}
specifier|public
name|void
name|startEntity
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXException
block|{
block|}
specifier|public
name|void
name|startDTD
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
block|}
block|}
specifier|public
specifier|static
name|void
name|update
parameter_list|(
specifier|final
name|IvySettings
name|settings
parameter_list|,
name|InputStream
name|inStream
parameter_list|,
name|OutputStream
name|outStream
parameter_list|,
specifier|final
name|Map
name|resolvedRevisions
parameter_list|,
specifier|final
name|String
name|status
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|Date
name|pubdate
parameter_list|,
specifier|final
name|Namespace
name|ns
parameter_list|,
specifier|final
name|boolean
name|replaceInclude
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
specifier|final
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|outStream
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|BufferedInputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|inStream
argument_list|)
decl_stmt|;
name|in
operator|.
name|mark
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
comment|// assume the header is never larger than 10000 bytes.
name|copyHeader
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|in
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// reposition the stream at the beginning
try|try
block|{
name|UpdaterHandler
name|updaterHandler
init|=
operator|new
name|UpdaterHandler
argument_list|(
name|settings
argument_list|,
name|out
argument_list|,
name|resolvedRevisions
argument_list|,
name|status
argument_list|,
name|revision
argument_list|,
name|pubdate
argument_list|,
name|ns
argument_list|,
name|replaceInclude
argument_list|)
decl_stmt|;
name|XMLHelper
operator|.
name|parse
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
name|updaterHandler
argument_list|,
name|updaterHandler
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
name|IllegalStateException
name|ise
init|=
operator|new
name|IllegalStateException
argument_list|(
literal|"impossible to update Ivy files: parser problem"
argument_list|)
decl_stmt|;
name|ise
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ise
throw|;
block|}
block|}
comment|/**      * Copy xml header from src url ivy file to given printwriter      * In fact, copies everything before<ivy-module to out, except      * if<ivy-module is not found, in which case nothing is copied.      *       * The prolog<?xml version="..." encoding="...."?> is also replaced by      *<?xml version="1.0" encoding="UTF-8"?> if it was present.      *       * @param in      * @param out      * @throws IOException      */
specifier|private
specifier|static
name|void
name|copyHeader
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|r
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
operator|&&
name|line
operator|.
name|startsWith
argument_list|(
literal|"<?xml "
argument_list|)
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
argument_list|)
expr_stmt|;
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
name|line
operator|.
name|indexOf
argument_list|(
literal|">"
argument_list|)
operator|+
literal|1
argument_list|,
name|line
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
init|;
name|line
operator|!=
literal|null
condition|;
name|line
operator|=
name|r
operator|.
name|readLine
argument_list|()
control|)
block|{
name|int
name|index
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|"<ivy-module"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|//r.close();
block|}
block|}
end_class

end_unit

