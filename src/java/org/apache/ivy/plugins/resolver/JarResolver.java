begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|resolver
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
name|MalformedURLException
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
name|jar
operator|.
name|JarFile
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
name|CacheResourceOptions
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
name|event
operator|.
name|EventManager
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|report
operator|.
name|DownloadStatus
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
name|plugins
operator|.
name|repository
operator|.
name|jar
operator|.
name|JarRepository
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
name|url
operator|.
name|URLRepository
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
name|url
operator|.
name|URLResource
import|;
end_import

begin_class
specifier|public
class|class
name|JarResolver
extends|extends
name|RepositoryResolver
block|{
specifier|private
name|URL
name|url
decl_stmt|;
specifier|public
name|JarResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|JarRepository
argument_list|(
operator|new
name|LazyTimeoutConstraint
argument_list|(
name|this
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"jar"
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|jarFile
parameter_list|)
block|{
name|setJarFile
argument_list|(
operator|new
name|File
argument_list|(
name|jarFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|jarUrl
parameter_list|)
block|{
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|jarUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"the jar repository "
operator|+
name|getName
argument_list|()
operator|+
literal|" has an malformed url : "
operator|+
name|jarUrl
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|JarRepository
name|getJarRepository
parameter_list|()
block|{
return|return
operator|(
name|JarRepository
operator|)
name|super
operator|.
name|getRepository
argument_list|()
return|;
block|}
specifier|private
name|void
name|setJarFile
parameter_list|(
name|File
name|jarLocalFile
parameter_list|)
block|{
name|JarFile
name|jar
decl_stmt|;
try|try
block|{
name|jar
operator|=
operator|new
name|JarFile
argument_list|(
name|jarLocalFile
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
operator|new
name|RuntimeException
argument_list|(
literal|"the jar repository "
operator|+
name|getName
argument_list|()
operator|+
literal|" could not be read ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|getJarRepository
argument_list|()
operator|.
name|setJarFile
argument_list|(
name|jar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSettings
parameter_list|(
name|ResolverSettings
name|settings
parameter_list|)
block|{
name|super
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// let's resolve the url
name|ArtifactDownloadReport
name|report
decl_stmt|;
name|EventManager
name|eventManager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|getRepository
argument_list|()
operator|.
name|addTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
name|Resource
name|jarResource
init|=
operator|new
name|URLResource
argument_list|(
name|url
argument_list|,
name|this
operator|.
name|getTimeoutConstraint
argument_list|()
argument_list|)
decl_stmt|;
name|CacheResourceOptions
name|options
init|=
operator|new
name|CacheResourceOptions
argument_list|()
decl_stmt|;
name|report
operator|=
name|getRepositoryCacheManager
argument_list|()
operator|.
name|downloadRepositoryResource
argument_list|(
name|jarResource
argument_list|,
literal|"jarrepository"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
name|options
argument_list|,
operator|new
name|URLRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|getRepository
argument_list|()
operator|.
name|removeTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|report
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The jar file "
operator|+
name|url
operator|.
name|toExternalForm
argument_list|()
operator|+
literal|" could not be downloaded ("
operator|+
name|report
operator|.
name|getDownloadDetails
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|setJarFile
argument_list|(
name|report
operator|.
name|getLocalFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

