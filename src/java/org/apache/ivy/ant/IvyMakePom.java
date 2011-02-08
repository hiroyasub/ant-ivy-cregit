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
name|net
operator|.
name|MalformedURLException
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
name|Collection
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorWriter
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
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|Project
import|;
end_import

begin_comment
comment|/**  * Convert an ivy file to a pom  */
end_comment

begin_class
specifier|public
class|class
name|IvyMakePom
extends|extends
name|IvyTask
block|{
specifier|public
class|class
name|Mapping
block|{
specifier|private
name|String
name|conf
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
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
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|Dependency
block|{
specifier|private
name|String
name|group
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|artifact
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|version
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|scope
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|type
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|classifier
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|optional
init|=
literal|false
decl_stmt|;
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|setArtifact
parameter_list|(
name|String
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|boolean
name|getOptional
parameter_list|()
block|{
return|return
name|optional
return|;
block|}
specifier|public
name|void
name|setOptional
parameter_list|(
name|boolean
name|optional
parameter_list|)
block|{
name|this
operator|.
name|optional
operator|=
name|optional
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|artifactName
decl_stmt|;
specifier|private
name|String
name|artifactPackaging
decl_stmt|;
specifier|private
name|File
name|pomFile
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|headerFile
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|templateFile
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|printIvyInfo
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|conf
decl_stmt|;
specifier|private
name|File
name|ivyFile
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|Collection
name|mappings
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Collection
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|File
name|getPomFile
parameter_list|()
block|{
return|return
name|pomFile
return|;
block|}
specifier|public
name|void
name|setPomFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|pomFile
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|File
name|getIvyFile
parameter_list|()
block|{
return|return
name|ivyFile
return|;
block|}
specifier|public
name|void
name|setIvyFile
parameter_list|(
name|File
name|ivyFile
parameter_list|)
block|{
name|this
operator|.
name|ivyFile
operator|=
name|ivyFile
expr_stmt|;
block|}
specifier|public
name|File
name|getHeaderFile
parameter_list|()
block|{
return|return
name|headerFile
return|;
block|}
specifier|public
name|void
name|setHeaderFile
parameter_list|(
name|File
name|headerFile
parameter_list|)
block|{
name|this
operator|.
name|headerFile
operator|=
name|headerFile
expr_stmt|;
block|}
specifier|public
name|File
name|getTemplateFile
parameter_list|()
block|{
return|return
name|templateFile
return|;
block|}
specifier|public
name|void
name|setTemplateFile
parameter_list|(
name|File
name|templateFile
parameter_list|)
block|{
name|this
operator|.
name|templateFile
operator|=
name|templateFile
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPrintIvyInfo
parameter_list|()
block|{
return|return
name|printIvyInfo
return|;
block|}
specifier|public
name|void
name|setPrintIvyInfo
parameter_list|(
name|boolean
name|printIvyInfo
parameter_list|)
block|{
name|this
operator|.
name|printIvyInfo
operator|=
name|printIvyInfo
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
name|getArtifactName
parameter_list|()
block|{
return|return
name|artifactName
return|;
block|}
specifier|public
name|void
name|setArtifactName
parameter_list|(
name|String
name|artifactName
parameter_list|)
block|{
name|this
operator|.
name|artifactName
operator|=
name|artifactName
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifactPackaging
parameter_list|()
block|{
return|return
name|artifactPackaging
return|;
block|}
specifier|public
name|void
name|setArtifactPackaging
parameter_list|(
name|String
name|artifactPackaging
parameter_list|)
block|{
name|this
operator|.
name|artifactPackaging
operator|=
name|artifactPackaging
expr_stmt|;
block|}
specifier|public
name|Mapping
name|createMapping
parameter_list|()
block|{
name|Mapping
name|mapping
init|=
operator|new
name|Mapping
argument_list|()
decl_stmt|;
name|this
operator|.
name|mappings
operator|.
name|add
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
return|return
name|mapping
return|;
block|}
specifier|public
name|Dependency
name|createDependency
parameter_list|()
block|{
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
name|this
operator|.
name|dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
return|return
name|dependency
return|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
try|try
block|{
if|if
condition|(
name|ivyFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"source ivy file is required for makepom task"
argument_list|)
throw|;
block|}
if|if
condition|(
name|pomFile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"destination pom file is required for makepom task"
argument_list|)
throw|;
block|}
name|ModuleDescriptor
name|md
init|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|PomModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|pomFile
argument_list|,
name|getPomWriterOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unable to convert given ivy file to url: "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"syntax errors in ivy file "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
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
literal|"impossible convert given ivy file to pom file: "
operator|+
name|e
operator|+
literal|" from="
operator|+
name|ivyFile
operator|+
literal|" to="
operator|+
name|pomFile
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|PomWriterOptions
name|getPomWriterOptions
parameter_list|()
throws|throws
name|IOException
block|{
name|PomWriterOptions
name|options
init|=
operator|new
name|PomWriterOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|setConfs
argument_list|(
name|splitConfs
argument_list|(
name|conf
argument_list|)
argument_list|)
operator|.
name|setArtifactName
argument_list|(
name|getArtifactName
argument_list|()
argument_list|)
operator|.
name|setArtifactPackaging
argument_list|(
name|getArtifactPackaging
argument_list|()
argument_list|)
operator|.
name|setPrintIvyInfo
argument_list|(
name|isPrintIvyInfo
argument_list|()
argument_list|)
operator|.
name|setDescription
argument_list|(
name|getDescription
argument_list|()
argument_list|)
operator|.
name|setExtraDependencies
argument_list|(
name|getDependencies
argument_list|()
argument_list|)
operator|.
name|setTemplate
argument_list|(
name|getTemplateFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|mappings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|options
operator|.
name|setMapping
argument_list|(
operator|new
name|PomWriterOptions
operator|.
name|ConfigurationScopeMapping
argument_list|(
name|getMappingsMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerFile
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setLicenseHeader
argument_list|(
name|FileUtil
operator|.
name|readEntirely
argument_list|(
name|getHeaderFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|options
return|;
block|}
specifier|private
name|Map
name|getMappingsMap
parameter_list|()
block|{
name|Map
name|mappingsMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|mappings
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
name|Mapping
name|mapping
init|=
operator|(
name|Mapping
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
index|[]
name|mappingConfs
init|=
name|splitConfs
argument_list|(
name|mapping
operator|.
name|getConf
argument_list|()
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
name|mappingConfs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|mappingsMap
operator|.
name|containsKey
argument_list|(
name|mappingConfs
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|mappingsMap
operator|.
name|put
argument_list|(
name|mappingConfs
index|[
name|i
index|]
argument_list|,
name|mapping
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|mappingsMap
return|;
block|}
specifier|private
name|List
name|getDependencies
parameter_list|()
block|{
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
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
name|Dependency
name|dependency
init|=
operator|(
name|Dependency
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|PomWriterOptions
operator|.
name|ExtraDependency
argument_list|(
name|dependency
operator|.
name|getGroup
argument_list|()
argument_list|,
name|dependency
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|,
name|dependency
operator|.
name|getScope
argument_list|()
argument_list|,
name|dependency
operator|.
name|getType
argument_list|()
argument_list|,
name|dependency
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|dependency
operator|.
name|getOptional
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

