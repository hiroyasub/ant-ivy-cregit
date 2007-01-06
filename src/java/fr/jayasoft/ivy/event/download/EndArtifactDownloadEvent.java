begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
operator|.
name|download
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ArtifactOrigin
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DependencyResolver
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|report
operator|.
name|ArtifactDownloadReport
import|;
end_import

begin_class
specifier|public
class|class
name|EndArtifactDownloadEvent
extends|extends
name|DownloadEvent
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"post-download-artifact"
decl_stmt|;
specifier|private
name|DependencyResolver
name|_resolver
decl_stmt|;
specifier|private
name|ArtifactDownloadReport
name|_report
decl_stmt|;
specifier|public
name|EndArtifactDownloadEvent
parameter_list|(
name|Ivy
name|source
parameter_list|,
name|DependencyResolver
name|resolver
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactDownloadReport
name|report
parameter_list|,
name|File
name|dest
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|NAME
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|_resolver
operator|=
name|resolver
expr_stmt|;
name|_report
operator|=
name|report
expr_stmt|;
name|addAttribute
argument_list|(
literal|"resolver"
argument_list|,
name|_resolver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"status"
argument_list|,
name|_report
operator|.
name|getDownloadStatus
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"size"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|_report
operator|.
name|getSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"file"
argument_list|,
name|dest
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ArtifactOrigin
name|origin
init|=
name|report
operator|.
name|getArtifactOrigin
argument_list|()
decl_stmt|;
if|if
condition|(
name|origin
operator|!=
literal|null
condition|)
block|{
name|addAttribute
argument_list|(
literal|"origin"
argument_list|,
name|_report
operator|.
name|getArtifactOrigin
argument_list|()
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"local"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|_report
operator|.
name|getArtifactOrigin
argument_list|()
operator|.
name|isLocal
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addAttribute
argument_list|(
literal|"origin"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"local"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ArtifactDownloadReport
name|getReport
parameter_list|()
block|{
return|return
name|_report
return|;
block|}
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|_resolver
return|;
block|}
block|}
end_class

end_unit

