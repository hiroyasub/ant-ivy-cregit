begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|conflict
package|;
end_package

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
name|IvyNode
import|;
end_import

begin_class
specifier|public
class|class
name|NoConflictManager
extends|extends
name|AbstractConflictManager
block|{
specifier|public
name|NoConflictManager
parameter_list|()
block|{
name|setName
argument_list|(
literal|"all"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|)
block|{
return|return
name|conflicts
return|;
block|}
block|}
end_class

end_unit

