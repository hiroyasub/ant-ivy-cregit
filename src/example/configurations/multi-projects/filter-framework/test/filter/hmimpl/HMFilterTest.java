begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|filter
operator|.
name|hmimpl
package|;
end_package

begin_import
import|import
name|filter
operator|.
name|AbstractTestFilter
import|;
end_import

begin_import
import|import
name|filter
operator|.
name|IFilter
import|;
end_import

begin_class
specifier|public
class|class
name|HMFilterTest
extends|extends
name|AbstractTestFilter
block|{
specifier|public
name|IFilter
name|getIFilter
parameter_list|()
block|{
return|return
operator|new
name|HMFilter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

