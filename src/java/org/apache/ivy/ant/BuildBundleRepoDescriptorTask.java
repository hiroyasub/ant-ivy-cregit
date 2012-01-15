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
name|ant
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
name|FileNotFoundException
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
name|OutputStream
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
name|core
operator|.
name|cache
operator|.
name|DefaultRepositoryCacheManager
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
name|cache
operator|.
name|RepositoryCacheManager
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
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|OBRXMLWriter
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
name|FSManifestIterable
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
name|ResolverManifestIterable
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
name|resolver
operator|.
name|BasicResolver
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
name|resolver
operator|.
name|DependencyResolver
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
name|tools
operator|.
name|ant
operator|.
name|BuildException
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

begin_class
specifier|public
class|class
name|BuildBundleRepoDescriptorTask
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|resolverName
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|file
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|cacheName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|encoding
init|=
literal|"UTF-8"
decl_stmt|;
specifier|private
name|boolean
name|indent
init|=
literal|true
decl_stmt|;
specifier|private
name|File
name|baseDir
decl_stmt|;
specifier|private
name|boolean
name|quiet
decl_stmt|;
specifier|public
name|void
name|setResolver
parameter_list|(
name|String
name|resolverName
parameter_list|)
block|{
name|this
operator|.
name|resolverName
operator|=
name|resolverName
expr_stmt|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
specifier|public
name|void
name|setOut
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
specifier|public
name|void
name|setIndent
parameter_list|(
name|boolean
name|indent
parameter_list|)
block|{
name|this
operator|.
name|indent
operator|=
name|indent
expr_stmt|;
block|}
specifier|public
name|void
name|setBaseDir
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|this
operator|.
name|baseDir
operator|=
name|dir
expr_stmt|;
block|}
specifier|public
name|void
name|setQuiet
parameter_list|(
name|boolean
name|quiet
parameter_list|)
block|{
name|this
operator|.
name|quiet
operator|=
name|quiet
expr_stmt|;
block|}
specifier|protected
name|void
name|prepareTask
parameter_list|()
block|{
if|if
condition|(
name|baseDir
operator|==
literal|null
condition|)
block|{
name|super
operator|.
name|prepareTask
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"No output file specified: use the attribute 'out'"
argument_list|)
throw|;
block|}
name|Iterator
comment|/*<ManifestAndLocation> */
name|it
decl_stmt|;
if|if
condition|(
name|resolverName
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|baseDir
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"specify only one of 'resolver' or 'baseDir'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|cacheName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"specify only one of 'resolver' or 'cache'"
argument_list|)
throw|;
block|}
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|resolverName
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"the resolver '"
operator|+
name|resolverName
operator|+
literal|"' was not found"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|resolver
operator|instanceof
name|BasicResolver
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"the type of resolver '"
operator|+
name|resolver
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' is not supported."
argument_list|)
throw|;
block|}
name|it
operator|=
operator|new
name|ResolverManifestIterable
argument_list|(
operator|(
name|BasicResolver
operator|)
name|resolver
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|baseDir
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cacheName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"specify only one of 'baseDir' or 'cache'"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|baseDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|baseDir
operator|+
literal|" is not a directory"
argument_list|)
throw|;
block|}
name|it
operator|=
operator|new
name|FSManifestIterable
argument_list|(
name|baseDir
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|cacheName
operator|!=
literal|null
condition|)
block|{
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|RepositoryCacheManager
name|cacheManager
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getRepositoryCacheManager
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|cacheManager
operator|instanceof
name|DefaultRepositoryCacheManager
operator|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"the type of cache '"
operator|+
name|cacheManager
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' is not supported."
argument_list|)
throw|;
block|}
name|File
name|basedir
init|=
operator|(
operator|(
name|DefaultRepositoryCacheManager
operator|)
name|cacheManager
operator|)
operator|.
name|getBasedir
argument_list|()
decl_stmt|;
name|it
operator|=
operator|new
name|FSManifestIterable
argument_list|(
name|basedir
argument_list|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"No resolver, cache or basedir specified: "
operator|+
literal|"please provide one of them through the attribute 'resolver', 'cache' or 'dir'"
argument_list|)
throw|;
block|}
name|OutputStream
name|out
decl_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|file
operator|+
literal|" was not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|ContentHandler
name|hd
decl_stmt|;
try|try
block|{
name|hd
operator|=
name|OBRXMLWriter
operator|.
name|newHandler
argument_list|(
name|out
argument_list|,
name|encoding
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Sax configuration error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
class|class
name|AntMessageLogger2
extends|extends
name|AntMessageLogger
block|{
name|AntMessageLogger2
parameter_list|()
block|{
name|super
argument_list|(
name|BuildBundleRepoDescriptorTask
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageLogger
argument_list|()
expr_stmt|;
name|Message
operator|.
name|setDefaultLogger
argument_list|(
operator|new
name|AntMessageLogger2
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|OBRXMLWriter
operator|.
name|writeManifests
argument_list|(
name|it
argument_list|,
name|hd
argument_list|,
name|quiet
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Sax serialisation error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// don't care
block|}
name|Message
operator|.
name|sumupProblems
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

