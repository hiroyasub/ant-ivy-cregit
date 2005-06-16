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
name|resolver
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
name|repository
operator|.
name|url
operator|.
name|URLRepository
import|;
end_import

begin_comment
comment|/**  * This resolver is able to work with any URLs, it handles latest revisions  * with file and http urls only, and it does not handle publishing  */
end_comment

begin_class
specifier|public
class|class
name|URLResolver
extends|extends
name|RepositoryResolver
block|{
specifier|public
name|URLResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|URLRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"url"
return|;
block|}
block|}
end_class

end_unit

