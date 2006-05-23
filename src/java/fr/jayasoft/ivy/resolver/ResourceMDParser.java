begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
name|Resource
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ResourceMDParser
block|{
name|MDResolvedResource
name|parse
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|String
name|rev
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

