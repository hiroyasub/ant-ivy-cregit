begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|descriptor
operator|.
name|DefaultModuleDescriptor
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
name|descriptor
operator|.
name|ModuleDescriptor
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
name|report
operator|.
name|ResolveReport
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
name|matcher
operator|.
name|PatternMatcher
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

begin_comment
comment|/**  * Generates a report of dependencies of a set of modules in the repository. The set of modules is  * specified using organisation/module and matcher.  */
end_comment

begin_class
specifier|public
class|class
name|IvyRepositoryReport
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|organisation
init|=
literal|"*"
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|private
name|String
name|branch
decl_stmt|;
specifier|private
name|String
name|revision
init|=
literal|"latest.integration"
decl_stmt|;
specifier|private
name|String
name|matcher
init|=
name|PatternMatcher
operator|.
name|EXACT_OR_REGEXP
decl_stmt|;
specifier|private
name|File
name|todir
decl_stmt|;
specifier|private
name|boolean
name|graph
init|=
literal|false
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
literal|true
decl_stmt|;
specifier|private
name|boolean
name|xsl
init|=
literal|false
decl_stmt|;
specifier|private
name|String
name|xslFile
decl_stmt|;
specifier|private
name|String
name|outputname
init|=
literal|"ivy-repository-report"
decl_stmt|;
specifier|private
name|String
name|xslext
init|=
literal|"html"
decl_stmt|;
specifier|private
name|List
name|params
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|xsl
operator|&&
name|xslFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"xsl file is mandatory when using xsl generation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
operator|&&
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy repository graph task: "
operator|+
literal|"It can either be set explicitly via the attribute 'module' or "
operator|+
literal|"via 'ivy.module' property or a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|else if
condition|(
name|module
operator|==
literal|null
operator|&&
operator|!
name|PatternMatcher
operator|.
name|EXACT
operator|.
name|equals
argument_list|(
name|matcher
argument_list|)
condition|)
block|{
name|module
operator|=
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
expr_stmt|;
block|}
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|)
decl_stmt|;
try|try
block|{
name|ModuleRevisionId
name|criteria
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|revision
operator|==
literal|null
operator|)
operator|||
name|settings
operator|.
name|getVersionMatcher
argument_list|()
operator|.
name|isDynamic
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
name|criteria
operator|=
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|,
name|branch
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|criteria
operator|=
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|)
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|)
expr_stmt|;
block|}
name|ModuleRevisionId
index|[]
name|mrids
init|=
name|ivy
operator|.
name|listModules
argument_list|(
name|criteria
argument_list|,
name|settings
operator|.
name|getMatcher
argument_list|(
name|matcher
argument_list|)
argument_list|)
decl_stmt|;
comment|// replace all found revisions with the original requested revision
name|Set
name|modules
init|=
operator|new
name|HashSet
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
name|mrids
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrids
index|[
name|i
index|]
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mrids
operator|=
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|modules
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|modules
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|md
init|=
name|DefaultModuleDescriptor
operator|.
name|newCallerInstance
argument_list|(
name|mrids
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|resolveId
init|=
name|ResolveOptions
operator|.
name|getDefaultResolveId
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setResolveId
argument_list|(
name|resolveId
argument_list|)
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|ResolutionCacheManager
name|cacheMgr
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolutionCacheManager
argument_list|()
decl_stmt|;
operator|new
name|XmlReportOutputter
argument_list|()
operator|.
name|output
argument_list|(
name|report
argument_list|,
name|cacheMgr
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|graph
condition|)
block|{
name|gengraph
argument_list|(
name|cacheMgr
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dot
condition|)
block|{
name|gendot
argument_list|(
name|cacheMgr
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xml
condition|)
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
name|cacheMgr
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
literal|"default"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|getTodir
argument_list|()
argument_list|,
name|outputname
operator|+
literal|".xml"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xsl
condition|)
block|{
name|genreport
argument_list|(
name|cacheMgr
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to generate graph for "
operator|+
name|mrid
operator|+
literal|": "
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
name|genreport
parameter_list|(
name|ResolutionCacheManager
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
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
name|init
argument_list|()
expr_stmt|;
name|String
name|resolveId
init|=
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
decl_stmt|;
name|xslt
operator|.
name|setIn
argument_list|(
name|cache
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setOut
argument_list|(
operator|new
name|File
argument_list|(
name|getTodir
argument_list|()
argument_list|,
name|outputname
operator|+
literal|"."
operator|+
name|xslext
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setStyle
argument_list|(
name|xslFile
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
literal|"extension"
argument_list|)
expr_stmt|;
name|param
operator|.
name|setExpression
argument_list|(
name|xslext
argument_list|)
expr_stmt|;
comment|// add the provided XSLT parameters
for|for
control|(
name|Iterator
name|it
init|=
name|params
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|param
operator|=
operator|(
name|XSLTProcess
operator|.
name|Param
operator|)
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|XSLTProcess
operator|.
name|Param
name|realParam
init|=
name|xslt
operator|.
name|createParam
argument_list|()
decl_stmt|;
name|realParam
operator|.
name|setName
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|realParam
operator|.
name|setExpression
argument_list|(
name|param
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|xslt
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|gengraph
parameter_list|(
name|ResolutionCacheManager
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|)
throws|throws
name|IOException
block|{
name|gen
argument_list|(
name|cache
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|getGraphStylePath
argument_list|(
name|cache
operator|.
name|getResolutionCacheRoot
argument_list|()
argument_list|)
argument_list|,
literal|"graphml"
argument_list|)
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
literal|"ivy-report-graph-all.xsl"
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
literal|"ivy-report-graph-all.xsl"
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
name|gendot
parameter_list|(
name|ResolutionCacheManager
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|)
throws|throws
name|IOException
block|{
name|gen
argument_list|(
name|cache
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|getDotStylePath
argument_list|(
name|cache
operator|.
name|getResolutionCacheRoot
argument_list|()
argument_list|)
argument_list|,
literal|"dot"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getDotStylePath
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
literal|"ivy-report-dot-all.xsl"
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
literal|"ivy-report-dot-all.xsl"
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
name|gen
parameter_list|(
name|ResolutionCacheManager
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|style
parameter_list|,
name|String
name|ext
parameter_list|)
throws|throws
name|IOException
block|{
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
name|init
argument_list|()
expr_stmt|;
name|String
name|resolveId
init|=
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
decl_stmt|;
name|xslt
operator|.
name|setIn
argument_list|(
name|cache
operator|.
name|getConfigurationResolveReportInCache
argument_list|(
name|resolveId
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setOut
argument_list|(
operator|new
name|File
argument_list|(
name|getTodir
argument_list|()
argument_list|,
name|outputname
operator|+
literal|"."
operator|+
name|ext
argument_list|)
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setBasedir
argument_list|(
name|cache
operator|.
name|getResolutionCacheRoot
argument_list|()
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setStyle
argument_list|(
name|style
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|File
name|getTodir
parameter_list|()
block|{
if|if
condition|(
name|todir
operator|==
literal|null
operator|&&
name|getProject
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
return|;
block|}
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
name|String
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
name|String
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
name|String
name|getOutputname
parameter_list|()
block|{
return|return
name|outputname
return|;
block|}
specifier|public
name|void
name|setOutputname
parameter_list|(
name|String
name|outputpattern
parameter_list|)
block|{
name|outputname
operator|=
name|outputpattern
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
name|getMatcher
parameter_list|()
block|{
return|return
name|matcher
return|;
block|}
specifier|public
name|void
name|setMatcher
parameter_list|(
name|String
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
name|matcher
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
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
block|}
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
specifier|public
name|void
name|setBranch
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
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

