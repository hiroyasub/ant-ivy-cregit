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
name|net
operator|.
name|URI
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
name|Iterator
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
name|BundleArtifact
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
name|BundleInfo
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
name|ExecutionEnvironmentProfileProvider
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
name|ManifestParser
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
class|class
name|BundleRepoDescriptor
extends|extends
name|EditableRepoDescriptor
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|lastModified
decl_stmt|;
specifier|public
name|BundleRepoDescriptor
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
block|{
name|super
argument_list|(
name|baseUri
argument_list|,
name|profileProvider
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setLastModified
parameter_list|(
name|String
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
specifier|public
name|String
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
specifier|public
name|void
name|populate
parameter_list|(
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
name|it
parameter_list|)
block|{
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ManifestAndLocation
name|manifestAndLocation
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|manifestAndLocation
operator|.
name|getManifest
argument_list|()
argument_list|)
decl_stmt|;
name|bundleInfo
operator|.
name|addArtifact
argument_list|(
operator|new
name|BundleArtifact
argument_list|(
literal|false
argument_list|,
name|manifestAndLocation
operator|.
name|getUri
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|addBundle
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Rejected "
operator|+
name|manifestAndLocation
operator|.
name|getUri
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

