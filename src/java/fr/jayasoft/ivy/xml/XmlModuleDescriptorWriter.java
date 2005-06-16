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
name|xml
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_comment
comment|/**  * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|XmlModuleDescriptorWriter
block|{
comment|// only handle header and info for the moment
specifier|public
specifier|static
name|void
name|write
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|File
name|output
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|output
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|output
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<ivy-module version=\"1.0\">"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t<info organisation=\""
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tmodule=\""
operator|+
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\trevision=\""
operator|+
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tstatus=\""
operator|+
name|md
operator|.
name|getStatus
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tpublication=\""
operator|+
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|format
argument_list|(
name|md
operator|.
name|getResolvedPublicationDate
argument_list|()
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tresolver=\""
operator|+
name|md
operator|.
name|getResolverName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t\tdefault=\""
operator|+
operator|(
name|md
operator|.
name|isDefault
argument_list|()
condition|?
literal|"true"
else|:
literal|"false"
operator|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"\t/>"
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</ivy-module>"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

