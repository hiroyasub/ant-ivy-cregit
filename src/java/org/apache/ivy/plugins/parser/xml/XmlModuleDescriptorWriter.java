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
name|plugins
operator|.
name|parser
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
name|OutputStreamWriter
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
name|util
operator|.
name|Arrays
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
name|module
operator|.
name|descriptor
operator|.
name|Artifact
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
name|Configuration
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
name|DependencyArtifactDescriptor
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
name|DependencyDescriptor
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

begin_comment
comment|/**  * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|XmlModuleDescriptorWriter
block|{
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
name|OutputStreamWriter
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|output
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|println
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
argument_list|)
expr_stmt|;
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
name|String
name|branch
init|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getBranch
argument_list|()
decl_stmt|;
if|if
condition|(
name|branch
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\tbranch=\""
operator|+
name|branch
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
name|revision
init|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
decl_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\trevision=\""
operator|+
name|revision
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|md
operator|.
name|isDefault
argument_list|()
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\tdefault=\"true\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|md
operator|instanceof
name|DefaultModuleDescriptor
condition|)
block|{
name|DefaultModuleDescriptor
name|dmd
init|=
operator|(
name|DefaultModuleDescriptor
operator|)
name|md
decl_stmt|;
if|if
condition|(
name|dmd
operator|.
name|getNamespace
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|dmd
operator|.
name|getNamespace
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"system"
argument_list|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\tnamespace=\""
operator|+
name|dmd
operator|.
name|getNamespace
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t/>"
argument_list|)
expr_stmt|;
name|Configuration
index|[]
name|confs
init|=
name|md
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|confs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t<configurations>"
argument_list|)
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t<conf"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" name=\""
operator|+
name|confs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" visibility=\""
operator|+
name|confs
index|[
name|i
index|]
operator|.
name|getVisibility
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|confs
index|[
name|i
index|]
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" description=\""
operator|+
name|confs
index|[
name|i
index|]
operator|.
name|getDescription
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|exts
init|=
name|confs
index|[
name|i
index|]
operator|.
name|getExtends
argument_list|()
decl_stmt|;
if|if
condition|(
name|exts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" extends=\""
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|exts
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|exts
index|[
name|j
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|j
operator|+
literal|1
operator|<
name|exts
operator|.
name|length
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t</configurations>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t<publications>"
argument_list|)
expr_stmt|;
name|Artifact
index|[]
name|artifacts
init|=
name|md
operator|.
name|getAllArtifacts
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
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t<artifact"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" name=\""
operator|+
name|artifacts
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" type=\""
operator|+
name|artifacts
index|[
name|i
index|]
operator|.
name|getType
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" ext=\""
operator|+
name|artifacts
index|[
name|i
index|]
operator|.
name|getExt
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" conf=\""
operator|+
name|getConfs
argument_list|(
name|md
argument_list|,
name|artifacts
index|[
name|i
index|]
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t</publications>"
argument_list|)
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|dds
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t<dependencies>"
argument_list|)
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
name|dds
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t<dependency"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" org=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
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
name|print
argument_list|(
literal|" name=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
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
name|print
argument_list|(
literal|" rev=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|dds
index|[
name|i
index|]
operator|.
name|isForce
argument_list|()
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" force=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|isForce
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dds
index|[
name|i
index|]
operator|.
name|isChanging
argument_list|()
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" changing=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|isChanging
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|dds
index|[
name|i
index|]
operator|.
name|isTransitive
argument_list|()
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" transitive=\""
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|isTransitive
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
literal|" conf=\""
argument_list|)
expr_stmt|;
name|String
index|[]
name|modConfs
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|modConfs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|String
index|[]
name|depConfs
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
name|modConfs
index|[
name|j
index|]
argument_list|)
decl_stmt|;
name|out
operator|.
name|print
argument_list|(
name|modConfs
index|[
name|j
index|]
operator|+
literal|"->"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|depConfs
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|depConfs
index|[
name|k
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|+
literal|1
operator|<
name|depConfs
operator|.
name|length
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|j
operator|+
literal|1
operator|<
name|modConfs
operator|.
name|length
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|DependencyArtifactDescriptor
index|[]
name|includes
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getAllDependencyArtifactsIncludes
argument_list|()
decl_stmt|;
if|if
condition|(
name|includes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|includes
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t\t<include"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" name=\""
operator|+
name|includes
index|[
name|j
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" type=\""
operator|+
name|includes
index|[
name|j
index|]
operator|.
name|getType
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" ext=\""
operator|+
name|includes
index|[
name|j
index|]
operator|.
name|getExt
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|String
index|[]
name|dadconfs
init|=
name|includes
index|[
name|j
index|]
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|asList
argument_list|(
name|dadconfs
argument_list|)
operator|.
name|equals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" conf=\""
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|dadconfs
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|dadconfs
index|[
name|k
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|+
literal|1
operator|<
name|dadconfs
operator|.
name|length
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
block|}
name|DependencyArtifactDescriptor
index|[]
name|excludes
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getAllDependencyArtifactsExcludes
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|includes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|excludes
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|"\t\t\t<exclude"
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" org=\""
operator|+
name|excludes
index|[
name|j
index|]
operator|.
name|getId
argument_list|()
operator|.
name|getModuleId
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
name|print
argument_list|(
literal|" module=\""
operator|+
name|excludes
index|[
name|j
index|]
operator|.
name|getId
argument_list|()
operator|.
name|getModuleId
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
name|print
argument_list|(
literal|" name=\""
operator|+
name|excludes
index|[
name|j
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" type=\""
operator|+
name|excludes
index|[
name|j
index|]
operator|.
name|getType
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|" ext=\""
operator|+
name|excludes
index|[
name|j
index|]
operator|.
name|getExt
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|String
index|[]
name|dadconfs
init|=
name|excludes
index|[
name|j
index|]
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|asList
argument_list|(
name|dadconfs
argument_list|)
operator|.
name|equals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|" conf=\""
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|dadconfs
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|dadconfs
index|[
name|k
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|+
literal|1
operator|<
name|dadconfs
operator|.
name|length
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includes
operator|.
name|length
operator|+
name|excludes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"\t\t</dependency>"
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|println
argument_list|(
literal|"\t</dependencies>"
argument_list|)
expr_stmt|;
block|}
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
specifier|private
specifier|static
name|String
name|getConfs
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|StringBuffer
name|ret
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|md
operator|.
name|getConfigurationsNames
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getArtifacts
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|ret
operator|.
name|append
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ret
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ret
operator|.
name|setLength
argument_list|(
name|ret
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

