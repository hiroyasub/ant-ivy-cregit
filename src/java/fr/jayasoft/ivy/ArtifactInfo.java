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

begin_interface
specifier|public
interface|interface
name|ArtifactInfo
block|{
name|String
name|getRevision
parameter_list|()
function_decl|;
name|long
name|getLastModified
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

