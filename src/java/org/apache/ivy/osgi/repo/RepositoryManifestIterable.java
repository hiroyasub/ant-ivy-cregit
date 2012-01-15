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
name|repo
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
name|Collections
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
name|plugins
operator|.
name|repository
operator|.
name|Repository
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
name|util
operator|.
name|ResolverHelper
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryManifestIterable
extends|extends
name|AbstractFSManifestIterable
comment|/*<String> */
block|{
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
comment|/**      * Default constructor      *       * @param root      *            the root directory of the file system to lookup      */
specifier|public
name|RepositoryManifestIterable
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|super
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
block|}
specifier|protected
name|URI
name|buildBundleURI
parameter_list|(
name|Object
comment|/* String */
name|location
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|repo
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|location
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
return|;
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
literal|"Unsupported repository type, resources names cannot be transformed into uri"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|InputStream
name|getInputStream
parameter_list|(
name|Object
comment|/* String */
name|f
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|repo
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|f
argument_list|)
operator|.
name|openStream
argument_list|()
return|;
block|}
specifier|protected
name|List
comment|/*<String> */
name|listBundleFiles
parameter_list|(
name|Object
comment|/* String */
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|asList
argument_list|(
name|ResolverHelper
operator|.
name|listAll
argument_list|(
name|repo
argument_list|,
operator|(
name|String
operator|)
name|dir
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|List
comment|/*<String> */
name|listDirs
parameter_list|(
name|Object
comment|/* String */
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|asList
argument_list|(
name|ResolverHelper
operator|.
name|listAll
argument_list|(
name|repo
argument_list|,
operator|(
name|String
operator|)
name|dir
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|List
comment|/*<String> */
name|asList
parameter_list|(
name|String
index|[]
name|array
parameter_list|)
block|{
return|return
name|array
operator|==
literal|null
condition|?
name|Collections
operator|.
name|EMPTY_LIST
else|:
name|Arrays
operator|.
comment|/*<String> */
name|asList
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
end_class

end_unit

