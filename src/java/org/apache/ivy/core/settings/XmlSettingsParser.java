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
name|core
operator|.
name|settings
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
name|Arrays
import|;
end_import

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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleId
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
name|status
operator|.
name|StatusManager
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
name|circular
operator|.
name|CircularDependencyStrategy
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
name|conflict
operator|.
name|ConflictManager
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
name|latest
operator|.
name|LatestStrategy
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
name|matcher
operator|.
name|PatternMatcher
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
name|Configurator
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
name|url
operator|.
name|URLHandlerRegistry
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
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|XmlSettingsParser
extends|extends
name|DefaultHandler
block|{
specifier|private
name|Configurator
name|configurator
decl_stmt|;
specifier|private
name|List
name|configuratorTags
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"resolvers"
block|,
literal|"namespaces"
block|,
literal|"parsers"
block|,
literal|"latest-strategies"
block|,
literal|"conflict-managers"
block|,
literal|"outputters"
block|,
literal|"version-matchers"
block|,
literal|"statuses"
block|,
literal|"circular-dependency-strategies"
block|,
literal|"triggers"
block|}
argument_list|)
decl_stmt|;
specifier|private
name|IvySettings
name|ivy
decl_stmt|;
specifier|private
name|String
name|defaultResolver
decl_stmt|;
specifier|private
name|String
name|defaultCM
decl_stmt|;
specifier|private
name|String
name|defaultLatest
decl_stmt|;
specifier|private
name|String
name|defaultCircular
decl_stmt|;
specifier|private
name|String
name|currentConfiguratorTag
decl_stmt|;
specifier|private
name|URL
name|settings
decl_stmt|;
specifier|private
name|boolean
name|deprecatedMessagePrinted
init|=
literal|false
decl_stmt|;
specifier|public
name|XmlSettingsParser
parameter_list|(
name|IvySettings
name|ivy
parameter_list|)
block|{
name|this
operator|.
name|ivy
operator|=
name|ivy
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|URL
name|settings
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|configurator
operator|=
operator|new
name|Configurator
argument_list|()
expr_stmt|;
comment|// put every type definition from ivy to configurator
name|Map
name|typeDefs
init|=
name|ivy
operator|.
name|getTypeDefs
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|typeDefs
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|configurator
operator|.
name|typeDef
argument_list|(
name|name
argument_list|,
operator|(
name|Class
operator|)
name|typeDefs
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|doParse
argument_list|(
name|settings
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doParse
parameter_list|(
name|URL
name|settingsUrl
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|this
operator|.
name|settings
operator|=
name|settingsUrl
expr_stmt|;
name|InputStream
name|stream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stream
operator|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|openStream
argument_list|(
name|settingsUrl
argument_list|)
expr_stmt|;
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newSAXParser
argument_list|()
operator|.
name|parse
argument_list|(
name|stream
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ParseException
name|pe
init|=
operator|new
name|ParseException
argument_list|(
literal|"failed to load settings from "
operator|+
name|settingsUrl
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stream
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
block|}
specifier|private
name|void
name|parse
parameter_list|(
name|Configurator
name|configurator
parameter_list|,
name|URL
name|configuration
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|this
operator|.
name|configurator
operator|=
name|configurator
expr_stmt|;
name|doParse
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
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
name|att
parameter_list|)
throws|throws
name|SAXException
block|{
comment|// we first copy attributes in a Map to be able to modify them
name|Map
name|attributes
init|=
operator|new
name|HashMap
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
name|att
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|att
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
argument_list|,
name|att
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
literal|"ivyconf"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|deprecatedMessagePrinted
operator|=
literal|true
expr_stmt|;
name|Message
operator|.
name|deprecated
argument_list|(
literal|"'ivyconf' element is deprecated, use 'ivysettings' instead ("
operator|+
name|settings
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurator
operator|.
name|getCurrent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"macrodef"
operator|.
name|equals
argument_list|(
name|currentConfiguratorTag
argument_list|)
operator|&&
name|configurator
operator|.
name|getTypeDef
argument_list|(
name|qName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|attributes
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"@{name}"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|configurator
operator|.
name|isTopLevelMacroRecord
argument_list|()
operator|&&
name|name
operator|.
name|indexOf
argument_list|(
literal|"@{name}"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|attributes
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributes
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"@{name}-"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|attributes
operator|.
name|get
argument_list|(
literal|"ref"
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|attributes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ref attribute should be the only one ! found "
operator|+
name|attributes
operator|.
name|size
argument_list|()
operator|+
literal|" in "
operator|+
name|qName
argument_list|)
throw|;
block|}
name|String
name|name
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"ref"
argument_list|)
decl_stmt|;
name|Object
name|child
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"resolvers"
operator|.
name|equals
argument_list|(
name|currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|ivy
operator|.
name|getResolver
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown resolver "
operator|+
name|name
operator|+
literal|": resolver should be defined before being referenced"
argument_list|)
throw|;
block|}
block|}
if|else if
condition|(
literal|"latest-strategies"
operator|.
name|equals
argument_list|(
name|currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|ivy
operator|.
name|getLatestStrategy
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown latest strategy "
operator|+
name|name
operator|+
literal|": latest strategy should be defined before being referenced"
argument_list|)
throw|;
block|}
block|}
if|else if
condition|(
literal|"conflict-managers"
operator|.
name|equals
argument_list|(
name|currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|ivy
operator|.
name|getConflictManager
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown conflict manager "
operator|+
name|name
operator|+
literal|": conflict manager should be defined before being referenced"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|child
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"bad reference "
operator|+
name|name
argument_list|)
throw|;
block|}
name|configurator
operator|.
name|addChild
argument_list|(
name|qName
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configurator
operator|.
name|startCreateChild
argument_list|(
name|qName
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|attributes
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|attName
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|configurator
operator|.
name|setAttribute
argument_list|(
name|attName
argument_list|,
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
literal|"classpath"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|urlStr
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|urlStr
operator|==
literal|null
condition|)
block|{
name|String
name|file
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"either url or file should be given for classpath element"
argument_list|)
throw|;
block|}
else|else
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|file
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
expr_stmt|;
block|}
name|ivy
operator|.
name|addClasspathURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"typedef"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|className
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"classname"
argument_list|)
argument_list|)
decl_stmt|;
name|Class
name|clazz
init|=
name|ivy
operator|.
name|typeDef
argument_list|(
name|name
argument_list|,
name|className
argument_list|)
decl_stmt|;
name|configurator
operator|.
name|typeDef
argument_list|(
name|name
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"property"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|override
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"override"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"missing attribute name on property tag"
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"missing attribute value on property tag"
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|setVariable
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|override
operator|==
literal|null
condition|?
literal|true
else|:
name|Boolean
operator|.
name|valueOf
argument_list|(
name|override
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|propFilePath
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|override
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"override"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"loading properties: "
operator|+
name|propFilePath
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|loadProperties
argument_list|(
operator|new
name|File
argument_list|(
name|propFilePath
argument_list|)
argument_list|,
name|override
operator|==
literal|null
condition|?
literal|true
else|:
name|Boolean
operator|.
name|valueOf
argument_list|(
name|override
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|fileEx
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"failed to load properties as file: trying as url: "
operator|+
name|propFilePath
argument_list|)
expr_stmt|;
try|try
block|{
name|ivy
operator|.
name|loadProperties
argument_list|(
operator|new
name|URL
argument_list|(
name|propFilePath
argument_list|)
argument_list|,
name|override
operator|==
literal|null
condition|?
literal|true
else|:
name|Boolean
operator|.
name|valueOf
argument_list|(
name|override
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|urlEx
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unable to load properties from "
operator|+
name|propFilePath
operator|+
literal|". Tried both as an url and a file, with no success. File exception: "
operator|+
name|fileEx
operator|+
literal|". URL exception: "
operator|+
name|urlEx
argument_list|)
throw|;
block|}
block|}
block|}
if|else if
condition|(
literal|"include"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|IvyVariableContainer
name|variables
init|=
operator|(
name|IvyVariableContainer
operator|)
name|ivy
operator|.
name|getVariableContainer
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|propFilePath
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
argument_list|)
decl_stmt|;
name|URL
name|settingsURL
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|propFilePath
operator|==
literal|null
condition|)
block|{
name|propFilePath
operator|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"url"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|propFilePath
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"bad include tag: specify file or url to include"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"including url: "
operator|+
name|propFilePath
argument_list|)
expr_stmt|;
name|settingsURL
operator|=
operator|new
name|URL
argument_list|(
name|propFilePath
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setSettingsVariables
argument_list|(
name|settingsURL
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|File
name|incFile
init|=
operator|new
name|File
argument_list|(
name|propFilePath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|incFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"impossible to include "
operator|+
name|incFile
operator|+
literal|": file does not exist"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"including file: "
operator|+
name|propFilePath
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|setSettingsVariables
argument_list|(
name|incFile
argument_list|)
expr_stmt|;
name|settingsURL
operator|=
name|incFile
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
operator|new
name|XmlSettingsParser
argument_list|(
name|ivy
argument_list|)
operator|.
name|parse
argument_list|(
name|configurator
argument_list|,
name|settingsURL
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ivy
operator|.
name|setVariableContainer
argument_list|(
name|variables
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
literal|"settings"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
operator|||
literal|"conf"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"conf"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
operator|&&
operator|!
name|deprecatedMessagePrinted
condition|)
block|{
name|Message
operator|.
name|deprecated
argument_list|(
literal|"'conf' is deprecated, use 'settings' instead ("
operator|+
name|settings
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
name|String
name|cache
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"defaultCache"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setDefaultCache
argument_list|(
operator|new
name|File
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|cache
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|defaultBranch
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"defaultBranch"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultBranch
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setDefaultBranch
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultBranch
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|validate
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"validate"
argument_list|)
decl_stmt|;
if|if
condition|(
name|validate
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setValidate
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|validate
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|up2d
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"checkUpToDate"
argument_list|)
decl_stmt|;
if|if
condition|(
name|up2d
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setCheckUpToDate
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|up2d
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|cacheIvyPattern
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"cacheIvyPattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheIvyPattern
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setCacheIvyPattern
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|cacheIvyPattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|cacheArtPattern
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"cacheArtifactPattern"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheArtPattern
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setCacheArtifactPattern
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|cacheArtPattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|useRemoteConfig
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"useRemoteConfig"
argument_list|)
decl_stmt|;
if|if
condition|(
name|useRemoteConfig
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setUseRemoteConfig
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|useRemoteConfig
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// we do not set following defaults here since no instances has been registered yet
name|defaultResolver
operator|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"defaultResolver"
argument_list|)
expr_stmt|;
name|defaultCM
operator|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"defaultConflictManager"
argument_list|)
expr_stmt|;
name|defaultLatest
operator|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"defaultLatestStrategy"
argument_list|)
expr_stmt|;
name|defaultCircular
operator|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"circularDependencyStrategy"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"version-matchers"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|configurator
operator|.
name|setRoot
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"usedefaults"
argument_list|)
argument_list|)
argument_list|)
condition|)
block|{
name|ivy
operator|.
name|configureDefaultVersionMatcher
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
literal|"statuses"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|StatusManager
name|m
init|=
operator|new
name|StatusManager
argument_list|()
decl_stmt|;
name|String
name|defaultStatus
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultStatus
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|setDefaultStatus
argument_list|(
name|defaultStatus
argument_list|)
expr_stmt|;
block|}
name|ivy
operator|.
name|setStatusManager
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|configurator
operator|.
name|setRoot
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|configuratorTags
operator|.
name|contains
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|configurator
operator|.
name|setRoot
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"macrodef"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|Configurator
operator|.
name|MacroDef
name|macrodef
init|=
name|configurator
operator|.
name|startMacroDef
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|macrodef
operator|.
name|addAttribute
argument_list|(
literal|"name"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"module"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|organisation
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"organisation"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|module
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|resolver
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"resolver"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"branch"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|cm
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"conflict-manager"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|matcher
init|=
name|ivy
operator|.
name|substitute
argument_list|(
operator|(
name|String
operator|)
name|attributes
operator|.
name|get
argument_list|(
literal|"matcher"
argument_list|)
argument_list|)
decl_stmt|;
name|matcher
operator|=
name|matcher
operator|==
literal|null
condition|?
name|PatternMatcher
operator|.
name|EXACT_OR_REGEXP
else|:
name|matcher
expr_stmt|;
if|if
condition|(
name|organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'organisation' is mandatory in module element: check your configuration"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'name' is mandatory in module element: check your configuration"
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|addModuleConfiguration
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|,
name|ivy
operator|.
name|getMatcher
argument_list|(
name|matcher
argument_list|)
argument_list|,
name|resolver
argument_list|,
name|branch
argument_list|,
name|cm
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"problem in config file: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"io problem while parsing config file: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
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
name|configurator
operator|.
name|getCurrent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|configuratorTags
operator|.
name|contains
argument_list|(
name|qName
argument_list|)
operator|&&
name|configurator
operator|.
name|getDepth
argument_list|()
operator|==
literal|1
condition|)
block|{
name|configurator
operator|.
name|clear
argument_list|()
expr_stmt|;
name|currentConfiguratorTag
operator|=
literal|null
expr_stmt|;
block|}
if|else if
condition|(
literal|"macrodef"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
operator|&&
name|configurator
operator|.
name|getDepth
argument_list|()
operator|==
literal|1
condition|)
block|{
name|configurator
operator|.
name|endMacroDef
argument_list|()
expr_stmt|;
name|currentConfiguratorTag
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|configurator
operator|.
name|endCreateChild
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|endDocument
parameter_list|()
throws|throws
name|SAXException
block|{
if|if
condition|(
name|defaultResolver
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setDefaultResolver
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultResolver
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultCM
operator|!=
literal|null
condition|)
block|{
name|ConflictManager
name|conflictManager
init|=
name|ivy
operator|.
name|getConflictManager
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultCM
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|conflictManager
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown conflict manager "
operator|+
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultCM
argument_list|)
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|setDefaultConflictManager
argument_list|(
name|conflictManager
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultLatest
operator|!=
literal|null
condition|)
block|{
name|LatestStrategy
name|latestStrategy
init|=
name|ivy
operator|.
name|getLatestStrategy
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultLatest
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|latestStrategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown latest strategy "
operator|+
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultLatest
argument_list|)
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|setDefaultLatestStrategy
argument_list|(
name|latestStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultCircular
operator|!=
literal|null
condition|)
block|{
name|CircularDependencyStrategy
name|strategy
init|=
name|ivy
operator|.
name|getCircularDependencyStrategy
argument_list|(
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultCircular
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown circular dependency strategy "
operator|+
name|ivy
operator|.
name|substitute
argument_list|(
name|defaultCircular
argument_list|)
argument_list|)
throw|;
block|}
name|ivy
operator|.
name|setCircularDependencyStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

