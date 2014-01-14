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
name|retrieve
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
name|Collection
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
name|HashSet
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
name|report
operator|.
name|ArtifactDownloadReport
import|;
end_import

begin_class
specifier|public
class|class
name|RetrieveReport
block|{
specifier|private
name|Collection
comment|/*<File> */
name|upToDateFiles
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|private
name|Collection
comment|/*<File> */
name|copiedFiles
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|private
name|Map
comment|/*<File, ArtifactDownloadReport> */
name|downloadReport
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|File
name|retrieveRoot
decl_stmt|;
comment|/**      * Returns the root directory to where the artifacts are retrieved.      */
specifier|public
name|File
name|getRetrieveRoot
parameter_list|()
block|{
return|return
name|retrieveRoot
return|;
block|}
specifier|public
name|void
name|setRetrieveRoot
parameter_list|(
name|File
name|retrieveRoot
parameter_list|)
block|{
name|this
operator|.
name|retrieveRoot
operator|=
name|retrieveRoot
expr_stmt|;
block|}
specifier|public
name|int
name|getNbrArtifactsCopied
parameter_list|()
block|{
return|return
name|copiedFiles
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getNbrArtifactsUpToDate
parameter_list|()
block|{
return|return
name|upToDateFiles
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|void
name|addCopiedFile
parameter_list|(
name|File
name|file
parameter_list|,
name|ArtifactDownloadReport
name|report
parameter_list|)
block|{
name|copiedFiles
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|downloadReport
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|report
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addUpToDateFile
parameter_list|(
name|File
name|file
parameter_list|,
name|ArtifactDownloadReport
name|report
parameter_list|)
block|{
name|upToDateFiles
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|downloadReport
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|report
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a collection of<tt>File</tt> objects who were actually copied during the retrieve      * process.      */
specifier|public
name|Collection
name|getCopiedFiles
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|(
name|copiedFiles
argument_list|)
return|;
block|}
comment|/**      * Returns a collection of<tt>File</tt> objects who were actually copied during the retrieve      * process.      */
specifier|public
name|Collection
name|getUpToDateFiles
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|(
name|upToDateFiles
argument_list|)
return|;
block|}
comment|/**      * Returns a collection of<tt>File</tt> objects who were retrieved during the retrieve process.      * This is the union of the files being copied and the files that were up-to-date.      */
specifier|public
name|Collection
name|getRetrievedFiles
parameter_list|()
block|{
name|Collection
name|result
init|=
operator|new
name|ArrayList
argument_list|(
name|upToDateFiles
operator|.
name|size
argument_list|()
operator|+
name|copiedFiles
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|upToDateFiles
argument_list|)
expr_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|copiedFiles
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Get the mapping between the copied files and their corresponding download report      */
specifier|public
name|Map
name|getDownloadReport
parameter_list|()
block|{
return|return
name|downloadReport
return|;
block|}
block|}
end_class

end_unit

