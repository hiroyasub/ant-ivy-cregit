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
name|FileNotFoundException
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|Project
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
name|Task
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
name|filters
operator|.
name|LineContainsRegExp
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
name|filters
operator|.
name|TokenFilter
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
name|Concat
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
name|types
operator|.
name|FileSet
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
name|types
operator|.
name|FilterChain
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
name|types
operator|.
name|RegularExpression
import|;
end_import

begin_comment
comment|/**  * Extracts imports from a set of java sources and generate corresponding ivy file  */
end_comment

begin_class
specifier|public
class|class
name|IvyExtractFromSources
extends|extends
name|Task
block|{
specifier|public
specifier|static
class|class
name|Ignore
block|{
specifier|private
name|String
name|packageName
decl_stmt|;
specifier|public
name|String
name|getPackage
parameter_list|()
block|{
return|return
name|packageName
return|;
block|}
specifier|public
name|void
name|setPackage
parameter_list|(
name|String
name|package1
parameter_list|)
block|{
name|packageName
operator|=
name|package1
expr_stmt|;
block|}
block|}
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
name|revision
decl_stmt|;
specifier|private
name|String
name|status
decl_stmt|;
specifier|private
name|List
name|ignoredPackaged
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List (String package)
specifier|private
name|Map
name|mapping
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (String package -> ModuleRevisionId)
specifier|private
name|Concat
name|concat
init|=
operator|new
name|Concat
argument_list|()
decl_stmt|;
specifier|private
name|File
name|to
decl_stmt|;
specifier|public
name|void
name|addConfiguredIgnore
parameter_list|(
name|Ignore
name|ignore
parameter_list|)
block|{
name|ignoredPackaged
operator|.
name|add
argument_list|(
name|ignore
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|File
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
specifier|public
name|void
name|setTo
parameter_list|(
name|File
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
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
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredMapping
parameter_list|(
name|PackageMapping
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|.
name|put
argument_list|(
name|mapping
operator|.
name|getPackage
argument_list|()
argument_list|,
name|mapping
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFileSet
parameter_list|(
name|FileSet
name|fileSet
parameter_list|)
block|{
name|concat
operator|.
name|addFileset
argument_list|(
name|fileSet
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|configureConcat
argument_list|()
expr_stmt|;
name|Writer
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|concat
operator|.
name|setWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|concat
operator|.
name|execute
argument_list|()
expr_stmt|;
name|Set
name|importsSet
init|=
operator|new
name|HashSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|out
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Set
name|dependencies
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|importsSet
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|pack
init|=
operator|(
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
operator|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|getMapping
argument_list|(
name|pack
argument_list|)
decl_stmt|;
if|if
condition|(
name|mrid
operator|!=
literal|null
condition|)
block|{
name|dependencies
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|to
argument_list|)
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<ivy-module version=\"1.0\">"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"\t<info organisation=\""
operator|+
name|organisation
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"\t       module=\""
operator|+
name|module
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"\t       revision=\""
operator|+
name|revision
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|status
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"\t       status=\""
operator|+
name|status
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"\t       status=\"integration\""
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"\t/>"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dependencies
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"\t<dependencies>"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|dependencies
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"\t\t<dependency org=\""
operator|+
name|mrid
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|"\" name=\""
operator|+
name|mrid
operator|.
name|getName
argument_list|()
operator|+
literal|"\" rev=\""
operator|+
name|mrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\"/>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"\t</dependencies>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"</ivy-module>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|log
argument_list|(
name|dependencies
operator|.
name|size
argument_list|()
operator|+
literal|" dependencies put in "
operator|+
name|to
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to create file "
operator|+
name|to
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
comment|/**      * @param pack String      * @return ModuleRevisionId      */
specifier|private
name|ModuleRevisionId
name|getMapping
parameter_list|(
name|String
name|pack
parameter_list|)
block|{
name|String
name|askedPack
init|=
name|pack
decl_stmt|;
name|ModuleRevisionId
name|ret
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|ret
operator|==
literal|null
operator|&&
name|pack
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|ignoredPackaged
operator|.
name|contains
argument_list|(
name|pack
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ret
operator|=
operator|(
name|ModuleRevisionId
operator|)
name|mapping
operator|.
name|get
argument_list|(
name|pack
argument_list|)
expr_stmt|;
name|int
name|lastDotIndex
init|=
name|pack
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDotIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|pack
operator|=
name|pack
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastDotIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
name|log
argument_list|(
literal|"no mapping found for "
operator|+
name|askedPack
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|void
name|configureConcat
parameter_list|()
block|{
name|concat
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|concat
operator|.
name|setTaskName
argument_list|(
name|getTaskName
argument_list|()
argument_list|)
expr_stmt|;
name|FilterChain
name|filterChain
init|=
operator|new
name|FilterChain
argument_list|()
decl_stmt|;
name|LineContainsRegExp
name|lcre
init|=
operator|new
name|LineContainsRegExp
argument_list|()
decl_stmt|;
name|RegularExpression
name|regexp
init|=
operator|new
name|RegularExpression
argument_list|()
decl_stmt|;
name|regexp
operator|.
name|setPattern
argument_list|(
literal|"^import .+;"
argument_list|)
expr_stmt|;
name|lcre
operator|.
name|addConfiguredRegexp
argument_list|(
name|regexp
argument_list|)
expr_stmt|;
name|filterChain
operator|.
name|add
argument_list|(
name|lcre
argument_list|)
expr_stmt|;
name|TokenFilter
name|tf
init|=
operator|new
name|TokenFilter
argument_list|()
decl_stmt|;
name|TokenFilter
operator|.
name|ReplaceRegex
name|rre
init|=
operator|new
name|TokenFilter
operator|.
name|ReplaceRegex
argument_list|()
decl_stmt|;
name|rre
operator|.
name|setPattern
argument_list|(
literal|"import (.+);.*"
argument_list|)
expr_stmt|;
name|rre
operator|.
name|setReplace
argument_list|(
literal|"\\1"
argument_list|)
expr_stmt|;
name|tf
operator|.
name|add
argument_list|(
name|rre
argument_list|)
expr_stmt|;
name|filterChain
operator|.
name|add
argument_list|(
name|tf
argument_list|)
expr_stmt|;
name|concat
operator|.
name|addFilterChain
argument_list|(
name|filterChain
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

