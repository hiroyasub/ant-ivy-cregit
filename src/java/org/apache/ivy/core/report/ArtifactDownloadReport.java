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
name|cache
operator|.
name|ArtifactOrigin
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
comment|/**  * Report on the download of an artifact from a repository to a local (cached) file.  *<p>  * Note that depending on cache implementation, the artifact may not be actually downloaded, but  * used directly from its original location.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactDownloadReport
block|{
comment|/**      * download details used when the download "fails" when the artifact is simply missing on the      * remote repository.      *<p>      * For historical reason the status can't be used to distinguish a real failure from a missing      * artifact by using the status, in both cases it's DownloadStatus.FAILED. The details message      * can be used for this purpose though.      *</p>      */
specifier|public
specifier|static
specifier|final
name|String
name|MISSING_ARTIFACT
init|=
literal|"missing artifact"
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|ArtifactOrigin
name|origin
decl_stmt|;
specifier|private
name|File
name|localFile
decl_stmt|;
specifier|private
name|DownloadStatus
name|downloadStatus
decl_stmt|;
specifier|private
name|long
name|size
decl_stmt|;
specifier|private
name|String
name|downloadDetails
init|=
literal|""
decl_stmt|;
specifier|private
name|long
name|downloadTimeMillis
decl_stmt|;
specifier|public
name|ArtifactDownloadReport
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|DownloadStatus
name|getDownloadStatus
parameter_list|()
block|{
return|return
name|downloadStatus
return|;
block|}
specifier|public
name|void
name|setDownloadStatus
parameter_list|(
name|DownloadStatus
name|downloadStatus
parameter_list|)
block|{
name|this
operator|.
name|downloadStatus
operator|=
name|downloadStatus
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|getType
argument_list|()
return|;
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
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|getExt
argument_list|()
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifactOrigin
parameter_list|(
name|ArtifactOrigin
name|origin
parameter_list|)
block|{
name|this
operator|.
name|origin
operator|=
name|origin
expr_stmt|;
block|}
specifier|public
name|ArtifactOrigin
name|getArtifactOrigin
parameter_list|()
block|{
return|return
name|origin
return|;
block|}
specifier|public
name|void
name|setDownloadDetails
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|downloadDetails
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|String
name|getDownloadDetails
parameter_list|()
block|{
return|return
name|downloadDetails
return|;
block|}
specifier|public
name|void
name|setDownloadTimeMillis
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|downloadTimeMillis
operator|=
name|l
expr_stmt|;
block|}
specifier|public
name|long
name|getDownloadTimeMillis
parameter_list|()
block|{
return|return
name|downloadTimeMillis
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|downloadStatus
operator|==
name|DownloadStatus
operator|.
name|SUCCESSFUL
condition|)
block|{
return|return
literal|"[SUCCESSFUL ] "
operator|+
name|artifact
operator|+
literal|" ("
operator|+
name|downloadTimeMillis
operator|+
literal|"ms)"
return|;
block|}
if|else if
condition|(
name|downloadStatus
operator|==
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
if|if
condition|(
name|downloadDetails
operator|==
name|MISSING_ARTIFACT
condition|)
block|{
return|return
literal|"[NOT FOUND  ] "
operator|+
name|artifact
operator|+
literal|" ("
operator|+
name|downloadTimeMillis
operator|+
literal|"ms)"
return|;
block|}
else|else
block|{
return|return
literal|"[FAILED     ] "
operator|+
name|artifact
operator|+
literal|": "
operator|+
name|downloadDetails
operator|+
literal|" ("
operator|+
name|downloadTimeMillis
operator|+
literal|"ms)"
return|;
block|}
block|}
if|else if
condition|(
name|downloadStatus
operator|==
name|DownloadStatus
operator|.
name|NO
condition|)
block|{
return|return
literal|"[NOT REQUIRED] "
operator|+
name|artifact
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the File where the artifact is available on the local filesystem,       * or<code>null</code> if and only if the artifact caching failed.      *       * @return the file where the artifact is now available on the local filesystem.      */
specifier|public
name|File
name|getLocalFile
parameter_list|()
block|{
return|return
name|localFile
return|;
block|}
specifier|public
name|void
name|setLocalFile
parameter_list|(
name|File
name|localFile
parameter_list|)
block|{
name|this
operator|.
name|localFile
operator|=
name|localFile
expr_stmt|;
block|}
block|}
end_class

end_unit

