begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
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
package|;
end_package

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
name|_artifact
decl_stmt|;
specifier|public
name|DownloadEvent
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|_artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|_artifact
return|;
block|}
block|}
end_class

end_unit

