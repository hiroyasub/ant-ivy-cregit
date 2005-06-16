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
package|;
end_package

begin_comment
comment|/**  * Represents a module configuration  */
end_comment

begin_class
specifier|public
class|class
name|Configuration
block|{
specifier|public
specifier|static
class|class
name|Visibility
block|{
specifier|public
specifier|static
name|Visibility
name|PUBLIC
init|=
operator|new
name|Visibility
argument_list|(
literal|"public"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|Visibility
name|PRIVATE
init|=
operator|new
name|Visibility
argument_list|(
literal|"private"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|Visibility
name|getVisibility
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"private"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|PRIVATE
return|;
block|}
if|else if
condition|(
literal|"public"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|PUBLIC
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknwon visibility "
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|Visibility
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|String
name|_description
decl_stmt|;
specifier|private
name|String
index|[]
name|_extends
decl_stmt|;
specifier|private
name|Visibility
name|_visibility
decl_stmt|;
comment|/**      * @param name      * @param visibility      * @param description      * @param ext      */
specifier|public
name|Configuration
parameter_list|(
name|String
name|name
parameter_list|,
name|Visibility
name|visibility
parameter_list|,
name|String
name|description
parameter_list|,
name|String
index|[]
name|ext
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null configuration name not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|visibility
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null visibility not allowed"
argument_list|)
throw|;
block|}
name|_name
operator|=
name|name
expr_stmt|;
name|_visibility
operator|=
name|visibility
expr_stmt|;
name|_description
operator|=
name|description
expr_stmt|;
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
name|_extends
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
name|_extends
operator|=
operator|new
name|String
index|[
name|ext
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ext
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|_extends
index|[
name|i
index|]
operator|=
name|ext
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @param name      */
specifier|public
name|Configuration
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the description. It may be null.      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|_description
return|;
block|}
comment|/**      * @return Returns the extends. May be empty, but never null.      */
specifier|public
name|String
index|[]
name|getExtends
parameter_list|()
block|{
return|return
name|_extends
return|;
block|}
comment|/**      * @return Returns the name. Never null;      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
comment|/**      * @return Returns the visibility. Never null.      */
specifier|public
name|Visibility
name|getVisibility
parameter_list|()
block|{
return|return
name|_visibility
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Configuration
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
operator|(
name|Configuration
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

