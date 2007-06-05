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
name|download
package|;
end_package

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

begin_class
specifier|public
specifier|abstract
class|class
name|DownloadEvent
extends|extends
name|IvyEvent
block|{
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|public
name|DownloadEvent
parameter_list|(
name|String
name|name
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
name|addArtifactAttributes
argument_list|(
name|this
operator|.
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addArtifactAttributes
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
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
block|}
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

