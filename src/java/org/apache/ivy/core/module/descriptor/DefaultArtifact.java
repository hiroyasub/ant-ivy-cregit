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
name|Date
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
name|DefaultArtifact
extends|extends
name|AbstractArtifact
block|{
specifier|public
specifier|static
name|Artifact
name|newIvyArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|pubDate
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|pubDate
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Artifact
name|newPomArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|pubDate
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|pubDate
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
literal|"pom"
argument_list|,
literal|"pom"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Artifact
name|cloneWithAnotherType
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|newType
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|newType
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|,
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Artifact
name|cloneWithAnotherTypeAndExt
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|newType
parameter_list|,
name|String
name|newExt
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|newType
argument_list|,
name|newExt
argument_list|,
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Artifact
name|cloneWithAnotherMrid
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|artifact
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|,
name|artifact
operator|.
name|getUrl
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
name|Date
name|publicationDate
decl_stmt|;
name|ArtifactRevisionId
name|arid
decl_stmt|;
name|URL
name|url
decl_stmt|;
specifier|public
name|DefaultArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|publicationDate
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
name|mrid
argument_list|,
name|publicationDate
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
name|DefaultArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|publicationDate
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
name|Map
name|extraAttributes
parameter_list|)
block|{
name|this
argument_list|(
name|mrid
argument_list|,
name|publicationDate
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
literal|null
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|publicationDate
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
name|mrid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null mrid not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|publicationDate
operator|==
literal|null
condition|)
block|{
name|publicationDate
operator|=
operator|new
name|Date
argument_list|()
expr_stmt|;
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
name|publicationDate
operator|=
name|publicationDate
expr_stmt|;
name|this
operator|.
name|arid
operator|=
name|ArtifactRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|arid
operator|.
name|getModuleRevisionId
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|arid
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|publicationDate
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|arid
operator|.
name|getType
argument_list|()
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|arid
operator|.
name|getExt
argument_list|()
return|;
block|}
specifier|public
name|ArtifactRevisionId
name|getId
parameter_list|()
block|{
return|return
name|arid
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
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

