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
name|report
package|;
end_package

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
name|HashMap
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
name|descriptor
operator|.
name|Artifact
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|DownloadReport
block|{
specifier|private
name|Map
name|_artifacts
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|void
name|addArtifactReport
parameter_list|(
name|ArtifactDownloadReport
name|adr
parameter_list|)
block|{
name|_artifacts
operator|.
name|put
argument_list|(
name|adr
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|adr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArtifactDownloadReport
index|[]
name|getArtifactsReports
parameter_list|()
block|{
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|_artifacts
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|_artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
index|[]
name|getArtifactsReports
parameter_list|(
name|DownloadStatus
name|status
parameter_list|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|_artifacts
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_artifacts
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactDownloadReport
name|adr
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|==
name|status
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|adr
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
name|getArtifactReport
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
operator|(
name|ArtifactDownloadReport
operator|)
name|_artifacts
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
return|;
block|}
block|}
end_class

end_unit
