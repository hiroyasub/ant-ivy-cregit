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
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_class
specifier|public
class|class
name|ManifestAndLocation
block|{
specifier|private
specifier|final
name|Manifest
name|manifest
decl_stmt|;
comment|/**      * location of the jar      */
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
comment|/**      * location of the source jar      */
specifier|private
specifier|final
name|URI
name|sourceURI
decl_stmt|;
specifier|public
name|ManifestAndLocation
parameter_list|(
name|Manifest
name|manifest
parameter_list|,
name|URI
name|uri
parameter_list|,
name|URI
name|sourceURI
parameter_list|)
block|{
name|this
operator|.
name|manifest
operator|=
name|manifest
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|sourceURI
operator|=
name|sourceURI
expr_stmt|;
block|}
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|Manifest
name|getManifest
parameter_list|()
block|{
return|return
name|manifest
return|;
block|}
specifier|public
name|URI
name|getSourceURI
parameter_list|()
block|{
return|return
name|sourceURI
return|;
block|}
block|}
end_class

end_unit

