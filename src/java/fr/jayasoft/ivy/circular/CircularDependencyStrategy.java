begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|circular
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
name|ModuleRevisionId
import|;
end_import

begin_interface
specifier|public
interface|interface
name|CircularDependencyStrategy
block|{
name|String
name|getName
parameter_list|()
function_decl|;
name|void
name|handleCircularDependency
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

