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
name|util
package|;
end_package

begin_comment
comment|/**  * Convenient class used only for uncapitalization  * Usually use commons lang but here we do not want to have such   * a dependency for only one feature  *   * @author X. Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|StringUtils
block|{
specifier|public
specifier|static
name|String
name|uncapitalize
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|==
literal|null
operator|||
name|string
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|string
return|;
block|}
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|string
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
return|return
name|string
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|string
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
comment|/**      * Joins the given object array in one string, each separated by the given separator.      * Example: join(new String[] {"one", "two", "three"}, ", ") -> "one, two, three"      *       * @param objs      * @param sep      * @return      */
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|Object
index|[]
name|objs
parameter_list|,
name|String
name|sep
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
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
name|objs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|objs
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
name|sep
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|objs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|setLength
argument_list|(
name|buf
operator|.
name|length
argument_list|()
operator|-
name|sep
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// delete sep
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

