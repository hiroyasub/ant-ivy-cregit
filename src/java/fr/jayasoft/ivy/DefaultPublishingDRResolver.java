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

begin_class
specifier|public
class|class
name|DefaultPublishingDRResolver
implements|implements
name|PublishingDependencyRevisionResolver
block|{
specifier|public
name|String
name|resolve
parameter_list|(
name|ModuleDescriptor
name|published
parameter_list|,
name|String
name|publishedStatus
parameter_list|,
name|ModuleRevisionId
name|depMrid
parameter_list|,
name|String
name|status
parameter_list|)
block|{
return|return
name|depMrid
operator|.
name|getRevision
argument_list|()
return|;
block|}
block|}
end_class

end_unit

