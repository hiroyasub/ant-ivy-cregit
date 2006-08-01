begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|StringUtilsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testEncryption
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"jayasoft"
argument_list|,
name|StringUtils
operator|.
name|decrypt
argument_list|(
name|StringUtils
operator|.
name|encrypt
argument_list|(
literal|"jayasoft"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yet another string with 126 digits and others :;%_-$& characters"
argument_list|,
name|StringUtils
operator|.
name|decrypt
argument_list|(
name|StringUtils
operator|.
name|encrypt
argument_list|(
literal|"yet another string with 126 digits and others :;%_-$& characters"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"jayasoft"
operator|.
name|equals
argument_list|(
name|StringUtils
operator|.
name|encrypt
argument_list|(
literal|"jayasoft"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

