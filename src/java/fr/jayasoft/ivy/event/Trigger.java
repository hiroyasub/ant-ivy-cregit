begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
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
name|filter
operator|.
name|Filter
import|;
end_import

begin_interface
specifier|public
interface|interface
name|Trigger
extends|extends
name|IvyListener
block|{
name|Filter
name|getEventFilter
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

