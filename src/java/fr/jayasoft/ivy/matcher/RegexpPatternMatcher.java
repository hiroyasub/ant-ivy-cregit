begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|matcher
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|RegexpPatternMatcher
implements|implements
name|PatternMatcher
block|{
specifier|public
specifier|static
class|class
name|RegexpMatcher
implements|implements
name|Matcher
block|{
specifier|private
name|Pattern
name|_p
decl_stmt|;
specifier|public
name|RegexpMatcher
parameter_list|(
name|String
name|exp
parameter_list|)
block|{
name|_p
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|exp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|_p
operator|.
name|matcher
argument_list|(
name|str
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
name|RegexpPatternMatcher
name|INSTANCE
init|=
operator|new
name|RegexpPatternMatcher
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|PatternMatcher
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|RegexpPatternMatcher
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|str
parameter_list|,
name|String
name|exp
parameter_list|)
block|{
if|if
condition|(
name|exp
operator|==
literal|null
condition|)
block|{
return|return
name|str
operator|==
literal|null
return|;
block|}
return|return
name|Pattern
operator|.
name|matches
argument_list|(
name|exp
argument_list|,
name|str
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|REGEXP
return|;
block|}
specifier|public
name|Matcher
name|getMatcher
parameter_list|(
name|String
name|exp
parameter_list|)
block|{
if|if
condition|(
name|ANY_EXPRESSION
operator|.
name|equals
argument_list|(
name|exp
argument_list|)
condition|)
block|{
return|return
name|AnyMatcher
operator|.
name|getInstance
argument_list|()
return|;
block|}
return|return
operator|new
name|RegexpMatcher
argument_list|(
name|exp
argument_list|)
return|;
block|}
block|}
end_class

end_unit

