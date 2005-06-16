begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|example
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|WordUtils
import|;
end_import

begin_comment
comment|/**  * Simple example world to show how easy it is to retreive libs with ivy !!!   */
end_comment

begin_class
specifier|public
class|class
name|Hello
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
name|message
init|=
literal|"hello ivy !"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"standard message : "
operator|+
name|message
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"capitalized by "
operator|+
name|WordUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|WordUtils
operator|.
name|capitalizeFully
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

