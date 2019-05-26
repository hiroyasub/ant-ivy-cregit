begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

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
name|FileInputStream
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|IvyPatternHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|cache
operator|.
name|ResolutionCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|resolve
operator|.
name|ResolveOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|settings
operator|.
name|IvySettings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|report
operator|.
name|XmlReportOutputter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|report
operator|.
name|XmlReportParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|Message
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
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|util
operator|.
name|JAXPUtils
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|splitToArray
import|;
end_import

begin_comment
comment|/**  * This ant task let users generates reports (html, xml, graphml, ...) from the last resolve done.  */
end_comment

begin_class
specifier|public
class|class
name|IvyReport
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|todir
decl_stmt|;
specifier|private
name|String
name|organisation
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|private
name|String
name|conf
decl_stmt|;
specifier|private
name|boolean
name|graph
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|dot
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|xml
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|xsl
init|=
literal|true
decl_stmt|;
specifier|private
name|File
name|xslFile
decl_stmt|;
specifier|private
name|String
name|outputpattern
decl_stmt|;
specifier|private
name|String
name|xslext
init|=
literal|"html"
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|XSLTProcess
operator|.
name|Param
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|resolveId
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mRevId
decl_stmt|;
specifier|public
name|File
name|getTodir
parameter_list|()
block|{
return|return
name|todir
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
name|this
operator|.
name|todir
operator|=
name|todir
expr_stmt|;
block|}
specifier|public
name|void
name|setCache
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
name|cacheAttributeNotSupported
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
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
name|this
operator|.
name|conf
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
name|module
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
name|this
operator|.
name|module
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
name|organisation
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
name|this
operator|.
name|organisation
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
name|graph
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
name|this
operator|.
name|graph
operator|=
name|graph
expr_stmt|;
block|}
specifier|public
name|File
name|getXslfile
parameter_list|()
block|{
return|return
name|xslFile
return|;
block|}
specifier|public
name|void
name|setXslfile
parameter_list|(
name|File
name|xslFile
parameter_list|)
block|{
name|this
operator|.
name|xslFile
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
name|outputpattern
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
name|this
operator|.
name|outputpattern
operator|=
name|outputpattern
expr_stmt|;
block|}
specifier|public
name|String
name|getResolveId
parameter_list|()
block|{
return|return
name|resolveId
return|;
block|}
specifier|public
name|void
name|setResolveId
parameter_list|(
name|String
name|resolveId
parameter_list|)
block|{
name|this
operator|.
name|resolveId
operator|=
name|resolveId
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
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
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|conf
operator|=
name|getProperty
argument_list|(
name|conf
argument_list|,
name|settings
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|,
name|resolveId
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|conf
argument_list|)
condition|)
block|{
name|conf
operator|=
name|getProperty
argument_list|(
name|settings
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|,
name|resolveId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no conf provided for ivy report task: "
operator|+
literal|"It can either be set explicitly via the attribute 'conf' or "
operator|+
literal|"via 'ivy.resolved.configurations' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|todir
operator|==
literal|null
condition|)
block|{
name|String
name|t
init|=
name|getProperty
argument_list|(
name|settings
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
name|todir
operator|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|todir
operator|!=
literal|null
operator|&&
name|todir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|todir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|outputpattern
operator|=
name|getProperty
argument_list|(
name|outputpattern
argument_list|,
name|settings
argument_list|,
literal|"ivy.report.output.pattern"
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputpattern
operator|==
literal|null
condition|)
block|{
name|outputpattern
operator|=
literal|"[organisation]-[module]-[conf].[ext]"
expr_stmt|;
block|}
if|if
condition|(
name|todir
operator|!=
literal|null
operator|&&
name|todir
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|todir
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
name|resolveId
operator|==
literal|null
condition|)
block|{
name|organisation
operator|=
name|getProperty
argument_list|(
name|organisation
argument_list|,
name|settings
argument_list|,
literal|"ivy.organisation"
argument_list|,
name|resolveId
argument_list|)
expr_stmt|;
name|module
operator|=
name|getProperty
argument_list|(
name|module
argument_list|,
name|settings
argument_list|,
literal|"ivy.module"
argument_list|,
name|resolveId
argument_list|)
expr_stmt|;
if|if
condition|(
name|organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy report task: "
operator|+
literal|"It can either be set explicitly via the attribute 'organisation' or "
operator|+
literal|"via 'ivy.organisation' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy report task: "
operator|+
literal|"It can either be set explicitly via the attribute 'module' or "
operator|+
literal|"via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
name|resolveId
operator|=
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|String
index|[]
name|confs
init|=
name|splitToArray
argument_list|(
name|conf
argument_list|)
decl_stmt|;
if|if
condition|(
name|xsl
condition|)
block|{
name|genreport
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xml
condition|)
block|{
name|genxml
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|graph
condition|)
block|{
name|genStyled
argument_list|(
name|confs
argument_list|,
name|getStylePath
argument_list|(
literal|"ivy-report-graph.xsl"
argument_list|)
argument_list|,
literal|"graphml"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dot
condition|)
block|{
name|genStyled
argument_list|(
name|confs
argument_list|,
name|getStylePath
argument_list|(
literal|"ivy-report-dot.xsl"
argument_list|)
argument_list|,
literal|"dot"
argument_list|)
expr_stmt|;
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
literal|"impossible to generate report: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|genxml
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
throws|throws
name|IOException
block|{
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|config
range|:
name|confs
control|)
block|{
name|File
name|xml
init|=
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|File
name|out
decl_stmt|;
if|if
condition|(
name|todir
operator|==
literal|null
condition|)
block|{
name|out
operator|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|getOutputPattern
argument_list|(
name|config
argument_list|,
literal|"xml"
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
name|todir
argument_list|,
name|getOutputPattern
argument_list|(
name|config
argument_list|,
literal|"xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FileUtil
operator|.
name|copy
argument_list|(
name|xml
argument_list|,
name|out
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|genreport
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
throws|throws
name|IOException
block|{
name|genStyled
argument_list|(
name|confs
argument_list|,
name|getReportStylePath
argument_list|()
argument_list|,
name|xslext
argument_list|)
expr_stmt|;
comment|// copy the css if required
if|if
condition|(
name|xslFile
operator|==
literal|null
condition|)
block|{
name|File
name|css
decl_stmt|;
if|if
condition|(
name|todir
operator|==
literal|null
condition|)
block|{
name|css
operator|=
name|getProject
argument_list|()
operator|.
name|resolveFile
argument_list|(
literal|"ivy-report.css"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|css
operator|=
operator|new
name|File
argument_list|(
name|todir
argument_list|,
literal|"ivy-report.css"
argument_list|)
expr_stmt|;
block|}
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
name|css
operator|.
name|getAbsolutePath
argument_list|()
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
block|}
block|}
specifier|private
name|File
name|getReportStylePath
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|xslFile
operator|!=
literal|null
condition|)
block|{
return|return
name|xslFile
return|;
block|}
comment|// style should be a file (and not an url)
comment|// so we have to copy it from classpath to cache
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
name|File
name|style
init|=
operator|new
name|File
argument_list|(
name|cacheMgr
operator|.
name|getResolutionCacheRoot
argument_list|()
argument_list|,
literal|"ivy-report.xsl"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|style
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"copying ivy-report.xsl to "
operator|+
name|style
operator|.
name|getAbsolutePath
argument_list|()
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
literal|"ivy-report.xsl"
argument_list|)
argument_list|,
name|style
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|style
return|;
block|}
specifier|private
name|String
name|getOutputPattern
parameter_list|(
name|String
name|conf
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
if|if
condition|(
name|mRevId
operator|==
literal|null
condition|)
block|{
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
name|XmlReportParser
name|parser
init|=
operator|new
name|XmlReportParser
argument_list|()
decl_stmt|;
name|File
name|reportFile
init|=
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
name|conf
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
name|reportFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Error occurred while parsing reportfile '"
operator|+
name|reportFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// get the resolve module
name|mRevId
operator|=
name|parser
operator|.
name|getResolvedModule
argument_list|()
expr_stmt|;
block|}
return|return
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|outputpattern
argument_list|,
name|mRevId
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mRevId
operator|.
name|getName
argument_list|()
argument_list|,
name|mRevId
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|ext
argument_list|,
name|conf
argument_list|,
name|mRevId
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|void
name|genStyled
parameter_list|(
name|String
index|[]
name|confs
parameter_list|,
name|File
name|style
parameter_list|,
name|String
name|ext
parameter_list|)
throws|throws
name|IOException
block|{
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
comment|// process the report with xslt to generate dot file
name|File
name|out
decl_stmt|;
if|if
condition|(
name|todir
operator|!=
literal|null
condition|)
block|{
name|out
operator|=
name|todir
expr_stmt|;
block|}
else|else
block|{
name|out
operator|=
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|InputStream
name|xsltStream
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|style
argument_list|)
argument_list|)
init|)
block|{
comment|// create stream to stylesheet
name|Source
name|xsltSource
init|=
operator|new
name|StreamSource
argument_list|(
name|xsltStream
argument_list|,
name|JAXPUtils
operator|.
name|getSystemId
argument_list|(
name|style
argument_list|)
argument_list|)
decl_stmt|;
comment|// create transformer
name|TransformerFactory
name|tFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Transformer
name|transformer
init|=
name|tFactory
operator|.
name|newTransformer
argument_list|(
name|xsltSource
argument_list|)
decl_stmt|;
comment|// add standard parameters
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"confs"
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setParameter
argument_list|(
literal|"extension"
argument_list|,
name|xslext
argument_list|)
expr_stmt|;
comment|// add the provided XSLT parameters
for|for
control|(
name|XSLTProcess
operator|.
name|Param
name|param
range|:
name|params
control|)
block|{
name|transformer
operator|.
name|setParameter
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|,
name|param
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// create the report
for|for
control|(
name|String
name|config
range|:
name|confs
control|)
block|{
name|File
name|reportFile
init|=
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|out
argument_list|,
name|getOutputPattern
argument_list|(
name|config
argument_list|,
name|ext
argument_list|)
argument_list|)
decl_stmt|;
name|log
argument_list|(
literal|"Processing "
operator|+
name|reportFile
operator|+
literal|" to "
operator|+
name|outFile
argument_list|)
expr_stmt|;
comment|// make sure the output directory exist
name|File
name|outFileDir
init|=
name|outFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|outFileDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|outFileDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"Unable to create directory: "
operator|+
name|outFileDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
try|try
init|(
name|InputStream
name|inStream
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|reportFile
argument_list|)
argument_list|)
init|;
name|OutputStream
name|outStream
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
init|)
block|{
name|StreamResult
name|res
init|=
operator|new
name|StreamResult
argument_list|(
name|outStream
argument_list|)
decl_stmt|;
name|Source
name|src
init|=
operator|new
name|StreamSource
argument_list|(
name|inStream
argument_list|,
name|JAXPUtils
operator|.
name|getSystemId
argument_list|(
name|style
argument_list|)
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|src
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|File
name|getStylePath
parameter_list|(
name|String
name|styleResourceName
parameter_list|)
throws|throws
name|IOException
block|{
comment|// style should be a file (and not an url)
comment|// so we have to copy it from classpath to cache
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
name|File
name|style
init|=
operator|new
name|File
argument_list|(
name|cacheMgr
operator|.
name|getResolutionCacheRoot
argument_list|()
argument_list|,
name|styleResourceName
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
name|styleResourceName
argument_list|)
argument_list|,
name|style
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|style
return|;
block|}
specifier|public
name|boolean
name|isXml
parameter_list|()
block|{
return|return
name|xml
return|;
block|}
specifier|public
name|void
name|setXml
parameter_list|(
name|boolean
name|xml
parameter_list|)
block|{
name|this
operator|.
name|xml
operator|=
name|xml
expr_stmt|;
block|}
specifier|public
name|boolean
name|isXsl
parameter_list|()
block|{
return|return
name|xsl
return|;
block|}
specifier|public
name|void
name|setXsl
parameter_list|(
name|boolean
name|xsl
parameter_list|)
block|{
name|this
operator|.
name|xsl
operator|=
name|xsl
expr_stmt|;
block|}
specifier|public
name|String
name|getXslext
parameter_list|()
block|{
return|return
name|xslext
return|;
block|}
specifier|public
name|void
name|setXslext
parameter_list|(
name|String
name|xslext
parameter_list|)
block|{
name|this
operator|.
name|xslext
operator|=
name|xslext
expr_stmt|;
block|}
specifier|public
name|XSLTProcess
operator|.
name|Param
name|createParam
parameter_list|()
block|{
name|XSLTProcess
operator|.
name|Param
name|result
init|=
operator|new
name|XSLTProcess
operator|.
name|Param
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|isDot
parameter_list|()
block|{
return|return
name|dot
return|;
block|}
specifier|public
name|void
name|setDot
parameter_list|(
name|boolean
name|dot
parameter_list|)
block|{
name|this
operator|.
name|dot
operator|=
name|dot
expr_stmt|;
block|}
block|}
end_class

end_unit

