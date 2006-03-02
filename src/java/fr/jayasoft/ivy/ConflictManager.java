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

begin_interface
specifier|public
interface|interface
name|ConflictManager
block|{
comment|/**      * Resolves the eventual conflicts found in the given collection of IvyNode.      * This method return a Collection of IvyNode which have not been evicted.      * The given conflicts Collection contains at least one IvyNode.      * @param parent the ivy node parent for which the conflict is to be resolved      * @param conflicts the collection of IvyNode to check for conflicts      * @return a Collection of IvyNode which have not been evicted      */
name|Collection
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|)
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

