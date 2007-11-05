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
name|module
operator|.
name|descriptor
package|;
end_package

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
name|ArrayList
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
name|ArtifactRevisionId
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
name|ModuleRevisionId
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|MDArtifact
extends|extends
name|AbstractArtifact
block|{
specifier|public
specifier|static
name|Artifact
name|newIvyArtifact
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
return|return
operator|new
name|MDArtifact
argument_list|(
name|md
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
return|;
block|}
specifier|private
name|ModuleDescriptor
name|md
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|ext
decl_stmt|;
specifier|private
name|List
name|confs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|extraAttributes
init|=
literal|null
decl_stmt|;
specifier|private
name|URL
name|url
decl_stmt|;
specifier|public
name|MDArtifact
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
name|this
argument_list|(
name|md
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MDArtifact
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|URL
name|url
parameter_list|,
name|Map
name|extraAttributes
parameter_list|)
block|{
if|if
condition|(
name|md
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null module descriptor not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null name not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null type not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null ext not allowed"
argument_list|)
throw|;
block|}
name|this
operator|.
name|md
operator|=
name|md
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|ext
operator|=
name|ext
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|extraAttributes
operator|=
name|extraAttributes
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|md
operator|.
name|getResolvedPublicationDate
argument_list|()
return|;
block|}
specifier|public
name|ArtifactRevisionId
name|getId
parameter_list|()
block|{
comment|// do not cache the result because the resolvedModuleRevisionId can change!
return|return
name|ArtifactRevisionId
operator|.
name|newInstance
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|extraAttributes
argument_list|)
return|;
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
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|ext
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|confs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|confs
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

