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
name|event
operator|.
name|publish
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
name|IvyEvent
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

begin_comment
comment|/**  * Base class for events fired during {@link DependencyResolver#publish(Artifact, File, boolean)}.  *  * @see StartArtifactPublishEvent  * @see EndArtifactPublishEvent  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PublishEvent
extends|extends
name|IvyEvent
block|{
specifier|private
specifier|final
name|DependencyResolver
name|resolver
decl_stmt|;
specifier|private
specifier|final
name|Artifact
name|artifact
decl_stmt|;
specifier|private
specifier|final
name|File
name|data
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|overwrite
decl_stmt|;
specifier|protected
name|PublishEvent
parameter_list|(
name|String
name|name
parameter_list|,
name|DependencyResolver
name|resolver
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|File
name|data
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
name|addMridAttributes
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|addAttributes
argument_list|(
name|artifact
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"resolver"
argument_list|,
name|resolver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"file"
argument_list|,
name|data
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"overwrite"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|overwrite
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** @return the resolver into which the artifact is being published */
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
comment|/** @return a local file containing the artifact data */
specifier|public
name|File
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/** @return metadata about the artifact being published */
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
comment|/** @return true iff this event overwrites existing resolver data for this artifact */
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
block|}
end_class

end_unit

