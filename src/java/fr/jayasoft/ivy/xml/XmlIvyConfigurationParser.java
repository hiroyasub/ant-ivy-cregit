begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|url
operator|.
name|URLHandlerRegistry
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * @author Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|XmlIvyConfigurationParser
extends|extends
name|DefaultHandler
block|{
specifier|private
name|Configurator
name|_configurator
decl_stmt|;
specifier|private
name|List
name|_configuratorTags
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
block|}
argument_list|)
decl_stmt|;
specifier|private
name|Ivy
name|_ivy
decl_stmt|;
specifier|private
name|String
name|_defaultResolver
decl_stmt|;
specifier|private
name|String
name|_defaultCM
decl_stmt|;
specifier|private
name|String
name|_defaultLatest
decl_stmt|;
specifier|private
name|String
name|_currentConfiguratorTag
decl_stmt|;
specifier|public
name|XmlIvyConfigurationParser
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
name|_ivy
operator|=
name|ivy
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|URL
name|configuration
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|_configurator
operator|=
operator|new
name|Configurator
argument_list|()
expr_stmt|;
comment|// put every type definition from ivy to configurator
name|Map
name|typeDefs
init|=
name|_ivy
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
name|_configurator
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
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doParse
parameter_list|(
name|URL
name|configuration
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
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
name|configuration
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
literal|"failed to configure with "
operator|+
name|configuration
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
name|_configurator
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
name|_configurator
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
name|_currentConfiguratorTag
argument_list|)
operator|&&
name|_configurator
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
name|_currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|_ivy
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
name|_currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|_ivy
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
name|_currentConfiguratorTag
argument_list|)
condition|)
block|{
name|child
operator|=
name|_ivy
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
name|_configurator
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
name|_configurator
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
name|_configurator
operator|.
name|setAttribute
argument_list|(
name|attName
argument_list|,
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|_ivy
operator|.
name|typeDef
argument_list|(
name|name
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|_configurator
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|ex
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
name|_ivy
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
name|String
name|propFilePath
init|=
name|_ivy
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
name|File
name|incFile
init|=
operator|new
name|File
argument_list|(
name|propFilePath
argument_list|)
decl_stmt|;
name|Map
name|variables
init|=
operator|new
name|HashMap
argument_list|(
name|_ivy
operator|.
name|getVariables
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|incFile
operator|.
name|exists
argument_list|()
condition|)
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
name|_ivy
operator|.
name|setConfigurationVariables
argument_list|(
name|incFile
argument_list|)
expr_stmt|;
operator|new
name|XmlIvyConfigurationParser
argument_list|(
name|_ivy
argument_list|)
operator|.
name|parse
argument_list|(
name|_configurator
argument_list|,
name|incFile
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
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
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|propFilePath
argument_list|)
decl_stmt|;
name|_ivy
operator|.
name|setConfigurationVariables
argument_list|(
name|url
argument_list|)
expr_stmt|;
operator|new
name|XmlIvyConfigurationParser
argument_list|(
name|_ivy
argument_list|)
operator|.
name|parse
argument_list|(
name|_configurator
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|_ivy
operator|.
name|setVariables
argument_list|(
name|variables
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
name|_ivy
operator|.
name|setDefaultCache
argument_list|(
operator|new
name|File
argument_list|(
name|_ivy
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
name|_ivy
operator|.
name|setValidate
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|_ivy
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
name|_ivy
operator|.
name|setCheckUpToDate
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|_ivy
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
name|_ivy
operator|.
name|setCacheIvyPattern
argument_list|(
name|_ivy
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
name|_ivy
operator|.
name|setCacheArtifactPattern
argument_list|(
name|_ivy
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
name|_ivy
operator|.
name|setUseRemoteConfig
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|_ivy
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
name|_defaultResolver
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
name|_defaultCM
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
name|_defaultLatest
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
block|}
if|else if
condition|(
name|_configuratorTags
operator|.
name|contains
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|_currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|_configurator
operator|.
name|setRoot
argument_list|(
name|_ivy
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
name|_currentConfiguratorTag
operator|=
name|qName
expr_stmt|;
name|Configurator
operator|.
name|MacroDef
name|macrodef
init|=
name|_configurator
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|_ivy
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
name|resolver
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"problem in config file"
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
name|_configurator
operator|.
name|getCurrent
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_configuratorTags
operator|.
name|contains
argument_list|(
name|qName
argument_list|)
operator|&&
name|_configurator
operator|.
name|getDepth
argument_list|()
operator|==
literal|1
condition|)
block|{
name|_configurator
operator|.
name|clear
argument_list|()
expr_stmt|;
name|_currentConfiguratorTag
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
name|_configurator
operator|.
name|getDepth
argument_list|()
operator|==
literal|1
condition|)
block|{
name|_configurator
operator|.
name|endMacroDef
argument_list|()
expr_stmt|;
name|_currentConfiguratorTag
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|_configurator
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
name|_defaultResolver
operator|!=
literal|null
condition|)
block|{
name|_ivy
operator|.
name|setDefaultResolver
argument_list|(
name|_ivy
operator|.
name|substitute
argument_list|(
name|_defaultResolver
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_defaultCM
operator|!=
literal|null
condition|)
block|{
name|_ivy
operator|.
name|setDefaultConflictManager
argument_list|(
name|_ivy
operator|.
name|getConflictManager
argument_list|(
name|_ivy
operator|.
name|substitute
argument_list|(
name|_defaultCM
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_defaultLatest
operator|!=
literal|null
condition|)
block|{
name|_ivy
operator|.
name|setDefaultLatestStrategy
argument_list|(
name|_ivy
operator|.
name|getLatestStrategy
argument_list|(
name|_ivy
operator|.
name|substitute
argument_list|(
name|_defaultLatest
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

