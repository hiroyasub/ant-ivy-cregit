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
operator|.
name|resolve
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
name|Ivy
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
name|ModuleDescriptor
import|;
end_import

begin_class
specifier|public
class|class
name|StartResolveEvent
extends|extends
name|ResolveEvent
block|{
specifier|public
name|StartResolveEvent
parameter_list|(
name|Ivy
name|source
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|md
argument_list|,
name|confs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

