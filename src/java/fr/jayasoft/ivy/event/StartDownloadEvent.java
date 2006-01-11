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

begin_class
specifier|public
class|class
name|StartDownloadEvent
extends|extends
name|DownloadEvent
block|{
specifier|private
name|DependencyResolver
name|_resolver
decl_stmt|;
specifier|public
name|StartDownloadEvent
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|super
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|_resolver
operator|=
name|resolver
expr_stmt|;
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

