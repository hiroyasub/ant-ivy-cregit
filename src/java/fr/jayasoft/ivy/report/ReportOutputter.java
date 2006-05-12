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
name|report
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ReportOutputter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONSOLE
init|=
literal|"console"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML
init|=
literal|"xml"
decl_stmt|;
specifier|public
specifier|abstract
name|void
name|output
parameter_list|(
name|ResolveReport
name|report
parameter_list|,
name|File
name|destDir
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

