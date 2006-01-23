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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|HMFilter
implements|implements
name|IFilter
block|{
specifier|public
name|String
index|[]
name|filter
parameter_list|(
name|String
index|[]
name|values
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
return|return
name|values
return|;
block|}
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|string
init|=
name|values
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|string
operator|!=
literal|null
operator|&&
name|string
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

