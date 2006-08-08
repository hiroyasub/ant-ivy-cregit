begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|tools
operator|.
name|analyser
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
name|ModuleDescriptor
import|;
end_import

begin_interface
specifier|public
interface|interface
name|DependencyAnalyser
block|{
specifier|public
name|ModuleDescriptor
index|[]
name|analyze
parameter_list|(
name|JarModule
index|[]
name|modules
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

