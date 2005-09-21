begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
comment|/**  * Unchecked exception thrown when a circular dependency exists between projects.  * @author baumkar  *  */
end_comment

begin_class
specifier|public
class|class
name|CircularDependencyException
extends|extends
name|RuntimeException
block|{
comment|/**      *       * @param descriptors module descriptors in order of circular dependency      */
specifier|public
name|CircularDependencyException
parameter_list|(
specifier|final
name|ModuleDescriptor
index|[]
name|descriptors
parameter_list|)
block|{
name|super
argument_list|(
name|formatMessage
argument_list|(
name|descriptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a string representation of this circular dependency graph      * @param descriptors in order of circular dependency      * @return      */
specifier|private
specifier|static
name|String
name|formatMessage
parameter_list|(
specifier|final
name|ModuleDescriptor
index|[]
name|descriptors
parameter_list|)
block|{
name|StringBuffer
name|buff
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buff
operator|.
name|append
argument_list|(
name|descriptors
index|[
literal|0
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|descriptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buff
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
name|buff
operator|.
name|append
argument_list|(
name|descriptors
index|[
name|i
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|buff
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

