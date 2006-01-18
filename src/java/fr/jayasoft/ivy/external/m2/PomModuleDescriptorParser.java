begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|external
operator|.
name|m2
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
name|Date
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Configuration
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
name|DefaultDependencyDescriptor
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
name|DefaultModuleDescriptor
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
name|ModuleDescriptor
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
name|ModuleRevisionId
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
name|Configuration
operator|.
name|Visibility
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
name|parser
operator|.
name|AbstractModuleDescriptorParser
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
name|repository
operator|.
name|Resource
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
name|XMLHelper
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
name|xml
operator|.
name|XmlModuleDescriptorWriter
import|;
end_import

begin_class
specifier|public
class|class
name|PomModuleDescriptorParser
extends|extends
name|AbstractModuleDescriptorParser
block|{
specifier|public
specifier|static
specifier|final
name|Configuration
index|[]
name|MAVEN2_CONFIGURATIONS
init|=
operator|new
name|Configuration
index|[]
block|{
operator|new
name|Configuration
argument_list|(
literal|"master"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"contains only the artifact published by this module itself, with no transitive dependencies"
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"compile"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this is the default scope, used if none is specified. Compile dependencies are available in all classpaths."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"provided"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this is much like compile, but indicates you expect the JDK or a container to provide it. It is only available on the compilation classpath, and is not transitive."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"runtime"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this scope indicates that the dependency is not required for compilation, but is for execution. It is in the runtime and test classpaths, but not the compile classpath."
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"compile"
block|}
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"test"
argument_list|,
name|Visibility
operator|.
name|PRIVATE
argument_list|,
literal|"this scope indicates that the dependency is not required for normal use of the application, and is only available for the test compilation and execution phases."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"system"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this scope is similar to provided except that you have to provide the JAR which contains it explicitly. The artifact is always available and is not looked up in a repository."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
block|,     }
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
name|MAVEN2_CONF_MAPPING
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"compile"
argument_list|,
literal|"compile->@,master;runtime->@"
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"provided"
argument_list|,
literal|"provided->compile,provided,runtime,master"
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime->compile,runtime,master"
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"test->compile,runtime,master"
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"system"
argument_list|,
literal|"system->master"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|Parser
extends|extends
name|AbstractParser
block|{
specifier|private
name|Ivy
name|_ivy
decl_stmt|;
specifier|private
name|DefaultModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|Stack
name|_contextStack
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
specifier|private
name|String
name|_organisation
decl_stmt|;
specifier|private
name|String
name|_module
decl_stmt|;
specifier|private
name|String
name|_revision
decl_stmt|;
specifier|private
name|String
name|_scope
decl_stmt|;
specifier|private
name|DefaultDependencyDescriptor
name|_dd
decl_stmt|;
specifier|public
name|Parser
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|Resource
name|res
parameter_list|)
block|{
name|_ivy
operator|=
name|ivy
expr_stmt|;
name|setResource
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|_md
operator|=
operator|new
name|DefaultModuleDescriptor
argument_list|()
expr_stmt|;
name|_md
operator|.
name|setResolvedPublicationDate
argument_list|(
operator|new
name|Date
argument_list|(
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
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
name|MAVEN2_CONFIGURATIONS
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|_md
operator|.
name|addConfiguration
argument_list|(
name|MAVEN2_CONFIGURATIONS
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
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
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
name|_contextStack
operator|.
name|push
argument_list|(
name|qName
argument_list|)
expr_stmt|;
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
name|_organisation
operator|!=
literal|null
operator|&&
name|_module
operator|!=
literal|null
operator|&&
name|_revision
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_md
operator|.
name|getModuleRevisionId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|_md
operator|.
name|setModuleRevisionId
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_revision
argument_list|)
argument_list|)
expr_stmt|;
name|_organisation
operator|=
literal|null
expr_stmt|;
name|_module
operator|=
literal|null
expr_stmt|;
name|_revision
operator|=
literal|null
expr_stmt|;
block|}
if|else if
condition|(
literal|"project/dependencies/dependency"
operator|.
name|equals
argument_list|(
name|getContext
argument_list|()
argument_list|)
condition|)
block|{
name|_dd
operator|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|_md
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_revision
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|_scope
operator|=
name|_scope
operator|==
literal|null
condition|?
literal|"compile"
else|:
name|_scope
expr_stmt|;
name|String
name|mapping
init|=
operator|(
name|String
operator|)
name|MAVEN2_CONF_MAPPING
operator|.
name|get
argument_list|(
name|_scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapping
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"unknown scope "
operator|+
name|_scope
operator|+
literal|" in "
operator|+
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
name|mapping
operator|=
operator|(
name|String
operator|)
name|MAVEN2_CONF_MAPPING
operator|.
name|get
argument_list|(
literal|"compile"
argument_list|)
expr_stmt|;
block|}
name|parseDepsConfs
argument_list|(
name|mapping
argument_list|,
name|_dd
argument_list|)
expr_stmt|;
name|_md
operator|.
name|addDependency
argument_list|(
name|_dd
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|"dependency"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|_organisation
operator|=
literal|null
expr_stmt|;
name|_module
operator|=
literal|null
expr_stmt|;
name|_revision
operator|=
literal|null
expr_stmt|;
name|_scope
operator|=
literal|null
expr_stmt|;
block|}
name|_contextStack
operator|.
name|pop
argument_list|()
expr_stmt|;
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
name|String
name|txt
init|=
operator|new
name|String
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|context
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|_organisation
operator|==
literal|null
operator|&&
name|context
operator|.
name|endsWith
argument_list|(
literal|"groupId"
argument_list|)
condition|)
block|{
name|_organisation
operator|=
name|txt
expr_stmt|;
block|}
if|else if
condition|(
name|_module
operator|==
literal|null
operator|&&
name|context
operator|.
name|endsWith
argument_list|(
literal|"artifactId"
argument_list|)
condition|)
block|{
name|_module
operator|=
name|txt
expr_stmt|;
block|}
if|else if
condition|(
name|_revision
operator|==
literal|null
operator|&&
name|context
operator|.
name|endsWith
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|_revision
operator|=
name|txt
expr_stmt|;
block|}
if|else if
condition|(
name|_scope
operator|==
literal|null
operator|&&
name|context
operator|.
name|endsWith
argument_list|(
literal|"scope"
argument_list|)
condition|)
block|{
name|_scope
operator|=
name|txt
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getContext
parameter_list|()
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_contextStack
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
name|ctx
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|ctx
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|buf
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|setLength
argument_list|(
name|buf
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
if|if
condition|(
name|_md
operator|.
name|getModuleRevisionId
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|_md
return|;
block|}
block|}
specifier|private
specifier|static
name|PomModuleDescriptorParser
name|INSTANCE
init|=
operator|new
name|PomModuleDescriptorParser
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|PomModuleDescriptorParser
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|PomModuleDescriptorParser
parameter_list|()
block|{
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|URL
name|descriptorURL
parameter_list|,
name|Resource
name|res
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|Parser
name|parser
init|=
operator|new
name|Parser
argument_list|(
name|ivy
argument_list|,
name|res
argument_list|)
decl_stmt|;
try|try
block|{
name|XMLHelper
operator|.
name|parse
argument_list|(
name|descriptorURL
argument_list|,
literal|null
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|ex
parameter_list|)
block|{
name|ParseException
name|pe
init|=
operator|new
name|ParseException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|" in "
operator|+
name|descriptorURL
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|ex
parameter_list|)
block|{
name|IllegalStateException
name|ise
init|=
operator|new
name|IllegalStateException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|" in "
operator|+
name|descriptorURL
argument_list|)
decl_stmt|;
name|ise
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ise
throw|;
block|}
return|return
name|parser
operator|.
name|getDescriptor
argument_list|()
return|;
block|}
specifier|public
name|void
name|toIvyFile
parameter_list|(
name|URL
name|srcURL
parameter_list|,
name|Resource
name|res
parameter_list|,
name|File
name|destFile
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Resource
name|res
parameter_list|)
block|{
return|return
name|res
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pom"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

