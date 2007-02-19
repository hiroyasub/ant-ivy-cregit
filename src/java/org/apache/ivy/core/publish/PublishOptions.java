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
name|publish
package|;
end_package

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
name|CacheManager
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
name|Artifact
import|;
end_import

begin_comment
comment|/**  * A set of options used during publish related tasks  *   * The publish can update the ivy file to publish if update is set to true. In this case it will use  * the given pubrevision, pubdate and status. If pudate is null it will default to the current date.  * If status is null it will default to the current ivy file status (which itself defaults to integration if none is found).  * If update is false, then if the revision is not the same in the ivy file than the one expected (given as parameter),  * this method will fail with an  IllegalArgumentException.  * pubdate and status are not used if update is false.  * extra artifacts can be used to publish more artifacts than actually declared in the ivy file.  * This can be useful to publish additional metadata or reports.  * The extra artifacts array can be null (= no extra artifacts), and if non null only the name, type, ext url   * and extra attributes of the artifacts are really used. Other methods (on the artifacts) can return null safely.  *    * @see PublishEngine  * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|PublishOptions
block|{
specifier|private
name|CacheManager
name|cache
decl_stmt|;
specifier|private
name|String
name|srcIvyPattern
decl_stmt|;
specifier|private
name|String
name|pubrevision
decl_stmt|;
specifier|private
name|String
name|status
decl_stmt|;
specifier|private
name|Date
name|pubdate
decl_stmt|;
specifier|private
name|Artifact
index|[]
name|extraArtifacts
decl_stmt|;
specifier|private
name|boolean
name|validate
decl_stmt|;
specifier|private
name|boolean
name|overwrite
decl_stmt|;
specifier|private
name|boolean
name|update
decl_stmt|;
specifier|private
name|String
index|[]
name|confs
decl_stmt|;
specifier|public
name|CacheManager
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
specifier|public
name|PublishOptions
name|setCache
parameter_list|(
name|CacheManager
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
return|return
name|confs
return|;
block|}
specifier|public
name|PublishOptions
name|setConfs
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Artifact
index|[]
name|getExtraArtifacts
parameter_list|()
block|{
return|return
name|extraArtifacts
return|;
block|}
specifier|public
name|PublishOptions
name|setExtraArtifacts
parameter_list|(
name|Artifact
index|[]
name|extraArtifacts
parameter_list|)
block|{
name|this
operator|.
name|extraArtifacts
operator|=
name|extraArtifacts
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|PublishOptions
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Date
name|getPubdate
parameter_list|()
block|{
return|return
name|pubdate
return|;
block|}
specifier|public
name|PublishOptions
name|setPubdate
parameter_list|(
name|Date
name|pubdate
parameter_list|)
block|{
name|this
operator|.
name|pubdate
operator|=
name|pubdate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getPubrevision
parameter_list|()
block|{
return|return
name|pubrevision
return|;
block|}
specifier|public
name|PublishOptions
name|setPubrevision
parameter_list|(
name|String
name|pubrevision
parameter_list|)
block|{
name|this
operator|.
name|pubrevision
operator|=
name|pubrevision
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getSrcIvyPattern
parameter_list|()
block|{
return|return
name|srcIvyPattern
return|;
block|}
specifier|public
name|PublishOptions
name|setSrcIvyPattern
parameter_list|(
name|String
name|srcIvyPattern
parameter_list|)
block|{
name|this
operator|.
name|srcIvyPattern
operator|=
name|srcIvyPattern
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|PublishOptions
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUpdate
parameter_list|()
block|{
return|return
name|update
return|;
block|}
specifier|public
name|PublishOptions
name|setUpdate
parameter_list|(
name|boolean
name|update
parameter_list|)
block|{
name|this
operator|.
name|update
operator|=
name|update
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
specifier|public
name|PublishOptions
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit
