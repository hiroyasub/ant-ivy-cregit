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

begin_class
specifier|public
class|class
name|ErrorCircularDependencyStrategy
extends|extends
name|AbstractCircularDependencyStrategy
block|{
specifier|private
specifier|static
specifier|final
name|CircularDependencyStrategy
name|INSTANCE
init|=
operator|new
name|ErrorCircularDependencyStrategy
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|CircularDependencyStrategy
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|ErrorCircularDependencyStrategy
parameter_list|()
block|{
name|super
argument_list|(
literal|"error"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleCircularDependency
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
throw|throw
operator|new
name|CircularDependencyException
argument_list|(
name|mrids
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

