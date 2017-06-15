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
name|m2
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
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
name|ArrayList
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|IvyContext
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
name|ExcludeRule
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
name|core
operator|.
name|settings
operator|.
name|IvyVariableContainer
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
name|parser
operator|.
name|m2
operator|.
name|PomWriterOptions
operator|.
name|ConfigurationScopeMapping
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
name|parser
operator|.
name|m2
operator|.
name|PomWriterOptions
operator|.
name|ExtraDependency
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
name|ConfigurationUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|PomModuleDescriptorWriter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SKIP_LINE
init|=
literal|"SKIP_LINE"
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
specifier|private
specifier|static
specifier|final
name|ConfigurationScopeMapping
name|DEFAULT_MAPPING
init|=
operator|new
name|ConfigurationScopeMapping
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
block|{
name|put
argument_list|(
literal|"compile"
argument_list|,
literal|"compile"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"provided"
argument_list|,
literal|"provided"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|put
argument_list|(
literal|"system"
argument_list|,
literal|"system"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|private
name|PomModuleDescriptorWriter
parameter_list|()
block|{
block|}
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
parameter_list|,
name|PomWriterOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|LineNumberReader
name|in
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|getTemplate
argument_list|()
operator|==
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|PomModuleDescriptorWriter
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"pom.template"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|options
operator|.
name|getTemplate
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|IvySettings
name|settings
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|IvyVariableContainer
name|variables
init|=
operator|new
name|IvyVariableContainerWrapper
argument_list|(
name|settings
operator|.
name|getVariableContainer
argument_list|()
argument_list|)
decl_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.license"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.header"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.groupId"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.artifactId"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.version"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.packaging"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.name"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.description"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.url"
argument_list|,
name|SKIP_LINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|getLicenseHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.license"
argument_list|,
name|options
operator|.
name|getLicenseHeader
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|isPrintIvyInfo
argument_list|()
condition|)
block|{
name|String
name|header
init|=
literal|"<!--\n"
operator|+
literal|"   Apache Maven 2 POM generated by Apache Ivy\n"
operator|+
literal|"   "
operator|+
name|Ivy
operator|.
name|getIvyHomeURL
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"   Apache Ivy version: "
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
operator|+
literal|" "
operator|+
name|Ivy
operator|.
name|getIvyDate
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"-->"
decl_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.header"
argument_list|,
name|header
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|setModuleVariables
argument_list|(
name|md
argument_list|,
name|variables
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|boolean
name|dependenciesPrinted
init|=
literal|false
decl_stmt|;
name|int
name|lastIndent
init|=
literal|0
decl_stmt|;
name|int
name|indent
init|=
literal|0
decl_stmt|;
name|String
name|line
init|=
name|in
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|line
argument_list|,
name|variables
argument_list|)
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|indexOf
argument_list|(
name|SKIP_LINE
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// skip this line
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|line
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// empty line
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
continue|continue;
block|}
name|lastIndent
operator|=
name|indent
expr_stmt|;
name|indent
operator|=
name|line
operator|.
name|indexOf
argument_list|(
literal|'<'
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dependenciesPrinted
operator|&&
name|line
operator|.
name|indexOf
argument_list|(
literal|"</dependencies>"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|printDependencies
argument_list|(
name|md
argument_list|,
name|out
argument_list|,
name|options
argument_list|,
name|indent
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dependenciesPrinted
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|dependenciesPrinted
operator|&&
name|line
operator|.
name|indexOf
argument_list|(
literal|"</project>"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|printDependencies
argument_list|(
name|md
argument_list|,
name|out
argument_list|,
name|options
argument_list|,
name|lastIndent
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dependenciesPrinted
operator|=
literal|true
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|setModuleVariables
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|IvyVariableContainer
name|variables
parameter_list|,
name|PomWriterOptions
name|options
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.groupId"
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|artifactId
init|=
name|options
operator|.
name|getArtifactName
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactId
operator|==
literal|null
condition|)
block|{
name|artifactId
operator|=
name|mrid
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|String
name|packaging
init|=
name|options
operator|.
name|getArtifactPackaging
argument_list|()
decl_stmt|;
if|if
condition|(
name|packaging
operator|==
literal|null
condition|)
block|{
comment|// find artifact to determine the packaging
name|Artifact
name|artifact
init|=
name|findArtifact
argument_list|(
name|md
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
comment|// no suitable artifact found, default to 'pom'
name|packaging
operator|=
literal|"pom"
expr_stmt|;
block|}
else|else
block|{
name|packaging
operator|=
name|artifact
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
block|}
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.artifactId"
argument_list|,
name|artifactId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.packaging"
argument_list|,
name|packaging
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getRevision
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.version"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.description"
argument_list|,
name|options
operator|.
name|getDescription
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|md
operator|.
name|getDescription
argument_list|()
operator|!=
literal|null
operator|&&
name|md
operator|.
name|getDescription
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.description"
argument_list|,
name|md
operator|.
name|getDescription
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|md
operator|.
name|getHomePage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|variables
operator|.
name|setVariable
argument_list|(
literal|"ivy.pom.url"
argument_list|,
name|md
operator|.
name|getHomePage
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the first artifact with the correct name and without a classifier.      */
specifier|private
specifier|static
name|Artifact
name|findArtifact
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|artifactName
parameter_list|)
block|{
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
if|if
condition|(
name|artifacts
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|artifactName
argument_list|)
operator|&&
name|artifacts
index|[
name|i
index|]
operator|.
name|getAttribute
argument_list|(
literal|"classifier"
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|artifacts
index|[
name|i
index|]
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|indent
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indent
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|printDependencies
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|PrintWriter
name|out
parameter_list|,
name|PomWriterOptions
name|options
parameter_list|,
name|int
name|indent
parameter_list|,
name|boolean
name|printDependencies
parameter_list|)
block|{
name|List
name|extraDeps
init|=
name|options
operator|.
name|getExtraDependencies
argument_list|()
decl_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|getDependencies
argument_list|(
name|md
argument_list|,
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|extraDeps
operator|.
name|isEmpty
argument_list|()
operator|||
operator|(
name|dds
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
if|if
condition|(
name|printDependencies
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<dependencies>"
argument_list|)
expr_stmt|;
block|}
comment|// print the extra dependencies first
for|for
control|(
name|Iterator
name|it
init|=
name|extraDeps
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
name|PomWriterOptions
operator|.
name|ExtraDependency
name|dep
init|=
operator|(
name|ExtraDependency
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|groupId
init|=
name|dep
operator|.
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
name|groupId
operator|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
expr_stmt|;
block|}
name|String
name|version
init|=
name|dep
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
expr_stmt|;
block|}
name|printDependency
argument_list|(
name|out
argument_list|,
name|indent
argument_list|,
name|groupId
argument_list|,
name|dep
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|version
argument_list|,
name|dep
operator|.
name|getType
argument_list|()
argument_list|,
name|dep
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|dep
operator|.
name|getScope
argument_list|()
argument_list|,
name|dep
operator|.
name|isOptional
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// now print the dependencies listed in the ModuleDescriptor
name|ConfigurationScopeMapping
name|mapping
init|=
name|options
operator|.
name|getMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapping
operator|==
literal|null
condition|)
block|{
name|mapping
operator|=
name|DEFAULT_MAPPING
expr_stmt|;
block|}
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
name|ModuleRevisionId
name|mrid
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
name|ExcludeRule
index|[]
name|excludes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dds
index|[
name|i
index|]
operator|.
name|canExclude
argument_list|()
condition|)
block|{
name|excludes
operator|=
name|dds
index|[
name|i
index|]
operator|.
name|getAllExcludeRules
argument_list|()
expr_stmt|;
block|}
name|DependencyArtifactDescriptor
index|[]
name|dads
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getAllDependencyArtifacts
argument_list|()
decl_stmt|;
if|if
condition|(
name|dads
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|dads
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|String
name|type
init|=
name|dads
index|[
name|j
index|]
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|classifier
init|=
name|dads
index|[
name|j
index|]
operator|.
name|getExtraAttribute
argument_list|(
literal|"classifier"
argument_list|)
decl_stmt|;
name|String
name|scope
init|=
name|mapping
operator|.
name|getScope
argument_list|(
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|optional
init|=
name|mapping
operator|.
name|isOptional
argument_list|(
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
decl_stmt|;
name|printDependency
argument_list|(
name|out
argument_list|,
name|indent
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
name|type
argument_list|,
name|classifier
argument_list|,
name|scope
argument_list|,
name|optional
argument_list|,
name|dds
index|[
name|i
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|,
name|excludes
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|scope
init|=
name|mapping
operator|.
name|getScope
argument_list|(
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|optional
init|=
name|mapping
operator|.
name|isOptional
argument_list|(
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|classifier
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getExtraAttribute
argument_list|(
literal|"classifier"
argument_list|)
decl_stmt|;
name|printDependency
argument_list|(
name|out
argument_list|,
name|indent
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|null
argument_list|,
name|classifier
argument_list|,
name|scope
argument_list|,
name|optional
argument_list|,
name|dds
index|[
name|i
index|]
operator|.
name|isTransitive
argument_list|()
argument_list|,
name|excludes
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|printDependencies
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</dependencies>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|printDependency
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|int
name|indent
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|scope
parameter_list|,
name|boolean
name|isOptional
parameter_list|,
name|boolean
name|isTransitive
parameter_list|,
name|ExcludeRule
index|[]
name|excludes
parameter_list|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|2
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<dependency>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<groupId>"
operator|+
name|groupId
operator|+
literal|"</groupId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<artifactId>"
operator|+
name|artifactId
operator|+
literal|"</artifactId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<version>"
operator|+
name|version
operator|+
literal|"</version>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
operator|!
literal|"jar"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<type>"
operator|+
name|type
operator|+
literal|"</type>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|classifier
operator|!=
literal|null
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<classifier>"
operator|+
name|classifier
operator|+
literal|"</classifier>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<scope>"
operator|+
name|scope
operator|+
literal|"</scope>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isOptional
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<optional>true</optional>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isTransitive
condition|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<exclusions>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<exclusion>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|5
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<groupId>*</groupId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|5
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<artifactId>*</artifactId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</exclusion>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</exclusions>"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
name|printExclusions
argument_list|(
name|excludes
argument_list|,
name|out
argument_list|,
name|indent
argument_list|)
expr_stmt|;
block|}
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|2
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</dependency>"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|printExclusions
parameter_list|(
name|ExcludeRule
index|[]
name|exclusions
parameter_list|,
name|PrintWriter
name|out
parameter_list|,
name|int
name|indent
parameter_list|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<exclusions>"
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
name|exclusions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<exclusion>"
argument_list|)
expr_stmt|;
name|ExcludeRule
name|rule
init|=
name|exclusions
index|[
name|i
index|]
decl_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|5
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<groupId>"
operator|+
name|rule
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
literal|"</groupId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|5
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"<artifactId>"
operator|+
name|rule
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
literal|"</artifactId>"
argument_list|)
expr_stmt|;
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|4
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</exclusion>"
argument_list|)
expr_stmt|;
block|}
name|indent
argument_list|(
name|out
argument_list|,
name|indent
operator|*
literal|3
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"</exclusions>"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|DependencyDescriptor
index|[]
name|getDependencies
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|PomWriterOptions
name|options
parameter_list|)
block|{
name|String
index|[]
name|confs
init|=
name|ConfigurationUtils
operator|.
name|replaceWildcards
argument_list|(
name|options
operator|.
name|getConfs
argument_list|()
argument_list|,
name|md
argument_list|)
decl_stmt|;
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
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
name|dds
operator|.
name|length
condition|;
name|i
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
name|confs
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|depConfs
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|depConfs
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|dds
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|DependencyDescriptor
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|DependencyDescriptor
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Wraps an {@link IvyVariableContainer} delegating most method calls to the wrapped instance,      * except for a set of variables which are only stored locally in the wrapper, and not      * propagated to the wrapped instance.      */
specifier|private
specifier|static
specifier|final
class|class
name|IvyVariableContainerWrapper
implements|implements
name|IvyVariableContainer
block|{
specifier|private
specifier|final
name|IvyVariableContainer
name|variables
decl_stmt|;
specifier|private
name|Map
name|localVariables
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|IvyVariableContainerWrapper
parameter_list|(
name|IvyVariableContainer
name|variables
parameter_list|)
block|{
name|this
operator|.
name|variables
operator|=
name|variables
expr_stmt|;
block|}
specifier|public
name|void
name|setVariable
parameter_list|(
name|String
name|varName
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
block|{
name|localVariables
operator|.
name|put
argument_list|(
name|varName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setEnvironmentPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|variables
operator|.
name|setEnvironmentPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|result
init|=
name|variables
operator|.
name|getVariable
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|(
name|String
operator|)
name|localVariables
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|Object
name|clone
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

