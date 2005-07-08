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
name|filter
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
name|Collection
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

begin_class
specifier|public
class|class
name|ArtifactTypeFilter
implements|implements
name|Filter
block|{
specifier|private
name|Collection
name|_acceptedTypes
decl_stmt|;
specifier|public
name|ArtifactTypeFilter
parameter_list|(
name|Collection
name|acceptedTypes
parameter_list|)
block|{
name|_acceptedTypes
operator|=
operator|new
name|ArrayList
argument_list|(
name|acceptedTypes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Artifact
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Artifact
name|art
init|=
operator|(
name|Artifact
operator|)
name|o
decl_stmt|;
return|return
name|_acceptedTypes
operator|.
name|contains
argument_list|(
name|art
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

