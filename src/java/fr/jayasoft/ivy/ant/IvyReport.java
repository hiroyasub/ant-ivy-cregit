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
name|ant
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
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|XSLTProcess
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
name|ModuleId
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
name|report
operator|.
name|XmlReportOutputter
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
name|util
operator|.
name|FileUtil
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
name|util
operator|.
name|IvyPatternHelper
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
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|IvyReport
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|_todir
decl_stmt|;
specifier|private
name|String
name|_organisation
decl_stmt|;
specifier|private
name|String
name|_module
decl_stmt|;
specifier|private
name|String
name|_conf
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|boolean
name|_graph
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|_xslFile
decl_stmt|;
specifier|private
name|String
name|_outputpattern
decl_stmt|;
specifier|public
name|File
name|getTodir
parameter_list|()
block|{
return|return
name|_todir
return|;
block|}
specifier|public
name|void
name|setTodir
parameter_list|(
name|File
name|todir
parameter_list|)
block|{
name|_todir
operator|=
name|todir
expr_stmt|;
block|}
specifier|public
name|File
name|getCache
parameter_list|()
block|{
return|return
name|_cache
return|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|_cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|_conf
return|;
block|}
specifier|public
name|void
name|setConf
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|_conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|String
name|getModule
parameter_list|()
block|{
return|return
name|_module
return|;
block|}
specifier|public
name|void
name|setModule
parameter_list|(
name|String
name|module
parameter_list|)
block|{
name|_module
operator|=
name|module
expr_stmt|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|_organisation
return|;
block|}
specifier|public
name|void
name|setOrganisation
parameter_list|(
name|String
name|organisation
parameter_list|)
block|{
name|_organisation
operator|=
name|organisation
expr_stmt|;
block|}
specifier|public
name|boolean
name|isGraph
parameter_list|()
block|{
return|return
name|_graph
return|;
block|}
specifier|public
name|void
name|setGraph
parameter_list|(
name|boolean
name|graph
parameter_list|)
block|{
name|_graph
operator|=
name|graph
expr_stmt|;
block|}
specifier|public
name|String
name|getXslfile
parameter_list|()
block|{
return|return
name|_xslFile
return|;
block|}
specifier|public
name|void
name|setXslfile
parameter_list|(
name|String
name|xslFile
parameter_list|)
block|{
name|_xslFile
operator|=
name|xslFile
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputpattern
parameter_list|()
block|{
return|return
name|_outputpattern
return|;
block|}
specifier|public
name|void
name|setOutputpattern
parameter_list|(
name|String
name|outputpattern
parameter_list|)
block|{
name|_outputpattern
operator|=
name|outputpattern
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
name|_organisation
operator|=
name|getProperty
argument_list|(
name|_organisation
argument_list|,
name|ivy
argument_list|,
literal|"ivy.organisation"
argument_list|)
expr_stmt|;
name|_module
operator|=
name|getProperty
argument_list|(
name|_module
argument_list|,
name|ivy
argument_list|,
literal|"ivy.module"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_cache
operator|==
literal|null
condition|)
block|{
name|_cache
operator|=
name|ivy
operator|.
name|getDefaultCache
argument_list|()
expr_stmt|;
block|}
name|_conf
operator|=
name|getProperty
argument_list|(
name|_conf
argument_list|,
name|ivy
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_conf
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|_conf
operator|=
name|getProperty
argument_list|(
name|ivy
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_todir
operator|==
literal|null
condition|)
block|{
name|String
name|t
init|=
name|getProperty
argument_list|(
name|ivy
argument_list|,
literal|"ivy.report.todir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|_todir
operator|=
operator|new
name|File
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|_outputpattern
operator|=
name|getProperty
argument_list|(
name|_outputpattern
argument_list|,
name|ivy
argument_list|,
literal|"ivy.report.output.pattern"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_todir
operator|!=
literal|null
operator|&&
name|_todir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|_todir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_outputpattern
operator|==
literal|null
condition|)
block|{
name|_outputpattern
operator|=
literal|"[organisation]-[module]-[conf].html"
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|_todir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"destination directory should be a directory !"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_organisation
operator|==
literal|null
operator|||
name|_module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module id provided for retrieve: either call resolve, give paramaters to ivy:retrieve, or provide ivy.module and ivy.organisation properties"
argument_list|)
throw|;
block|}
try|try
block|{
name|String
index|[]
name|confs
init|=
name|splitConfs
argument_list|(
name|_conf
argument_list|)
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|genreport
argument_list|(
name|ivy
argument_list|,
name|_cache
argument_list|,
name|_organisation
argument_list|,
name|_module
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|_graph
condition|)
block|{
name|gengraph
argument_list|(
name|ivy
argument_list|,
name|_cache
argument_list|,
name|_organisation
argument_list|,
name|_module
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to generate report"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|genreport
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|conf
parameter_list|)
throws|throws
name|IOException
block|{
comment|// first process the report with xslt
name|XSLTProcess
name|xslt
init|=
operator|new
name|XSLTProcess
argument_list|()
decl_stmt|;
name|xslt
operator|.
name|setTaskName
argument_list|(
name|getTaskName
argument_list|()
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setIn
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
name|XmlReportOutputter
operator|.
name|getReportFileName
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|,
name|conf
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|out
decl_stmt|;
if|if
condition|(
name|_todir
operator|!=
literal|null
condition|)
block|{
name|out
operator|=
operator|new
name|File
argument_list|(
name|_todir
argument_list|,
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|_outputpattern
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|_outputpattern
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|out
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|out
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|out
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|xslt
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setStyle
argument_list|(
name|getReportStylePath
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|XSLTProcess
operator|.
name|Param
name|param
init|=
name|xslt
operator|.
name|createParam
argument_list|()
decl_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"confs"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setExpression
argument_list|(
name|_conf
argument_list|)
expr_stmt|;
name|param
operator|=
name|xslt
operator|.
name|createParam
argument_list|()
expr_stmt|;
name|param
operator|.
name|setName
argument_list|(
literal|"extension"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setExpression
argument_list|(
literal|"html"
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|execute
argument_list|()
expr_stmt|;
comment|// then copy the css if required
if|if
condition|(
name|_todir
operator|!=
literal|null
operator|&&
name|_xslFile
operator|==
literal|null
condition|)
block|{
name|File
name|css
init|=
operator|new
name|File
argument_list|(
name|_todir
argument_list|,
literal|"ivy-report.css"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|css
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"copying report css to "
operator|+
name|_todir
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ivy-report.css"
argument_list|)
argument_list|,
name|css
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ivy-report.css"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"ivy-report.css"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getReportStylePath
parameter_list|(
name|File
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|_xslFile
operator|!=
literal|null
condition|)
block|{
return|return
name|_xslFile
return|;
block|}
comment|// style should be a file (and not an url)
comment|// so we have to copy it from classpath to cache
name|File
name|style
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"ivy-report.xsl"
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ivy-report.xsl"
argument_list|)
argument_list|,
name|style
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|style
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
specifier|private
name|void
name|gengraph
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|conf
parameter_list|)
throws|throws
name|IOException
block|{
comment|// process the report with xslt to generate graphml
name|XSLTProcess
name|xslt
init|=
operator|new
name|XSLTProcess
argument_list|()
decl_stmt|;
name|xslt
operator|.
name|setTaskName
argument_list|(
name|getTaskName
argument_list|()
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setDestdir
argument_list|(
name|_todir
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setBasedir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setExtension
argument_list|(
literal|".graphml"
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setIncludes
argument_list|(
name|XmlReportOutputter
operator|.
name|getReportFileName
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|,
name|conf
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setStyle
argument_list|(
name|getGraphStylePath
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getGraphStylePath
parameter_list|(
name|File
name|cache
parameter_list|)
throws|throws
name|IOException
block|{
comment|// style should be a file (and not an url)
comment|// so we have to copy it from classpath to cache
name|File
name|style
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"ivy-report-graph.xsl"
argument_list|)
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|XmlReportOutputter
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ivy-report-graph.xsl"
argument_list|)
argument_list|,
name|style
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|style
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

