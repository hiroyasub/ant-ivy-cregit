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
name|LinkedList
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
name|descriptor
operator|.
name|ModuleDescriptor
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
name|OSGiManifestParser
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
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorParser
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
name|parser
operator|.
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|Message
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ModuleDescriptorParserRegistry
extends|extends
name|AbstractModuleDescriptorParser
block|{
specifier|private
specifier|static
specifier|final
name|ModuleDescriptorParserRegistry
name|INSTANCE
init|=
operator|new
name|ModuleDescriptorParserRegistry
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|ModuleDescriptorParserRegistry
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|List
argument_list|<
name|ModuleDescriptorParser
argument_list|>
name|parsers
init|=
operator|new
name|LinkedList
argument_list|<
name|ModuleDescriptorParser
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ModuleDescriptorParserRegistry
parameter_list|()
block|{
name|parsers
operator|.
name|add
argument_list|(
name|PomModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|parsers
operator|.
name|add
argument_list|(
name|OSGiManifestParser
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|parsers
operator|.
name|add
argument_list|(
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a the given parser to this registry.      *      * @param parser      *            the parser to add      */
specifier|public
name|void
name|addParser
parameter_list|(
name|ModuleDescriptorParser
name|parser
parameter_list|)
block|{
comment|/*          * The parser is added in the front of the list of parsers. This is necessary because the          * XmlModuleDescriptorParser accepts all resources!          */
name|parsers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleDescriptorParser
index|[]
name|getParsers
parameter_list|()
block|{
return|return
name|parsers
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleDescriptorParser
index|[
name|parsers
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ModuleDescriptorParser
name|getParser
parameter_list|(
name|Resource
name|res
parameter_list|)
block|{
for|for
control|(
name|ModuleDescriptorParser
name|parser
range|:
name|parsers
control|)
block|{
if|if
condition|(
name|parser
operator|.
name|accept
argument_list|(
name|res
argument_list|)
condition|)
block|{
return|return
name|parser
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|ParserSettings
name|settings
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
name|ModuleDescriptorParser
name|parser
init|=
name|getParser
argument_list|(
name|res
argument_list|)
decl_stmt|;
if|if
condition|(
name|parser
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"no module descriptor parser found for "
operator|+
name|res
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|parser
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|descriptorURL
argument_list|,
name|res
argument_list|,
name|validate
argument_list|)
return|;
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
name|getParser
argument_list|(
name|res
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|toIvyFile
parameter_list|(
name|InputStream
name|is
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
name|ModuleDescriptorParser
name|parser
init|=
name|getParser
argument_list|(
name|res
argument_list|)
decl_stmt|;
if|if
condition|(
name|parser
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"no module descriptor parser found for "
operator|+
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parser
operator|.
name|toIvyFile
argument_list|(
name|is
argument_list|,
name|res
argument_list|,
name|destFile
argument_list|,
name|md
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

