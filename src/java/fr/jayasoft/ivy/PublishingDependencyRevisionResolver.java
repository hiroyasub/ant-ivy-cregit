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

begin_comment
comment|/**  * @author Xavier Hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|PublishingDependencyRevisionResolver
block|{
comment|/**      * Returns the revision of the dependency for the publishing of the 'published' module       * in 'publishedStatus' status.      * @param published      * @param publishedStatus      * @param dependency      * @return the revision of the dependency      */
name|String
name|resolve
parameter_list|(
name|ModuleDescriptor
name|published
parameter_list|,
name|String
name|publishedStatus
parameter_list|,
name|ModuleDescriptor
name|dependency
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

