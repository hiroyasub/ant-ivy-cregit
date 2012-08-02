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
name|updatesite
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|osgi
operator|.
name|repo
operator|.
name|AbstractOSGiResolver
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
name|UpdateSiteResolver
extends|extends
name|AbstractOSGiResolver
block|{
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|Long
name|metadataTtl
decl_stmt|;
specifier|private
name|Boolean
name|forceMetadataUpdate
decl_stmt|;
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|setMetadataTtl
parameter_list|(
name|Long
name|metadataTtl
parameter_list|)
block|{
name|this
operator|.
name|metadataTtl
operator|=
name|metadataTtl
expr_stmt|;
block|}
specifier|public
name|void
name|setForceMetadataUpdate
parameter_list|(
name|Boolean
name|forceMetadataUpdate
parameter_list|)
block|{
name|this
operator|.
name|forceMetadataUpdate
operator|=
name|forceMetadataUpdate
expr_stmt|;
block|}
specifier|protected
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing url"
argument_list|)
throw|;
block|}
name|CacheResourceOptions
name|options
init|=
operator|new
name|CacheResourceOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|metadataTtl
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setTtl
argument_list|(
name|metadataTtl
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|forceMetadataUpdate
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setForce
argument_list|(
name|forceMetadataUpdate
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|UpdateSiteLoader
name|loader
init|=
operator|new
name|UpdateSiteLoader
argument_list|(
name|getRepositoryCacheManager
argument_list|()
argument_list|,
name|getEventManager
argument_list|()
argument_list|,
name|options
argument_list|)
decl_stmt|;
try|try
block|{
name|setRepoDescriptor
argument_list|(
name|loader
operator|.
name|load
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
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
literal|"IO issue while trying to read the update site ("
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
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to parse the updatesite ("
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
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Illformed updatesite ("
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
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Illformed url ("
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
block|}
block|}
end_class

end_unit

