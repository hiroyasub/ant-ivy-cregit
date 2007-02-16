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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
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
name|TransformerFactoryConfigurationError
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
name|sax
operator|.
name|SAXTransformerFactory
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
name|sax
operator|.
name|TransformerHandler
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
name|ArtifactOrigin
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
name|CacheManager
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
name|resolve
operator|.
name|IvyNode
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|AttributesImpl
import|;
end_import

begin_comment
comment|/**  * Generates a report of all artifacts involved during the last resolve.   */
end_comment

begin_class
specifier|public
class|class
name|IvyArtifactReport
extends|extends
name|IvyTask
block|{
specifier|private
name|File
name|_tofile
decl_stmt|;
specifier|private
name|String
name|_conf
decl_stmt|;
specifier|private
name|String
name|_pattern
decl_stmt|;
specifier|private
name|boolean
name|_haltOnFailure
init|=
literal|true
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|public
name|File
name|getTofile
parameter_list|()
block|{
return|return
name|_tofile
return|;
block|}
specifier|public
name|void
name|setTofile
parameter_list|(
name|File
name|tofile
parameter_list|)
block|{
name|_tofile
operator|=
name|tofile
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
name|getPattern
parameter_list|()
block|{
return|return
name|_pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|_pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHaltonfailure
parameter_list|()
block|{
return|return
name|_haltOnFailure
return|;
block|}
specifier|public
name|void
name|setHaltonfailure
parameter_list|(
name|boolean
name|haltOnFailure
parameter_list|)
block|{
name|_haltOnFailure
operator|=
name|haltOnFailure
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
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
if|if
condition|(
name|_tofile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no destination file name: please provide it through parameter 'tofile'"
argument_list|)
throw|;
block|}
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
name|ensureResolved
argument_list|(
name|isHaltonfailure
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|_organisation
init|=
name|getProperty
argument_list|(
literal|null
argument_list|,
name|settings
argument_list|,
literal|"ivy.organisation"
argument_list|)
decl_stmt|;
name|String
name|_module
init|=
name|getProperty
argument_list|(
literal|null
argument_list|,
name|settings
argument_list|,
literal|"ivy.module"
argument_list|)
decl_stmt|;
if|if
condition|(
name|_cache
operator|==
literal|null
condition|)
block|{
name|_cache
operator|=
name|settings
operator|.
name|getDefaultCache
argument_list|()
expr_stmt|;
block|}
name|_pattern
operator|=
name|getProperty
argument_list|(
name|_pattern
argument_list|,
name|settings
argument_list|,
literal|"ivy.retrieve.pattern"
argument_list|)
expr_stmt|;
name|_conf
operator|=
name|getProperty
argument_list|(
name|_conf
argument_list|,
name|settings
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|_conf
argument_list|)
condition|)
block|{
name|_conf
operator|=
name|getProperty
argument_list|(
name|settings
argument_list|,
literal|"ivy.resolved.configurations"
argument_list|)
expr_stmt|;
if|if
condition|(
name|_conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"bad provided for ivy artifact report task: * can only be used with a prior call to<resolve/>"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|_organisation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no organisation provided for ivy artifact report task: It can be set via a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_module
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no module name provided for ivy artifact report task: It can be set via a prior call to<resolve/>"
argument_list|)
throw|;
block|}
if|if
condition|(
name|_conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"no conf provided for ivy artifact report task: It can either be set explicitely via the attribute 'conf' or via 'ivy.resolved.configurations' property or a prior call to<resolve/>"
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
name|IvyNode
index|[]
name|dependencies
init|=
name|ivy
operator|.
name|getResolveEngine
argument_list|()
operator|.
name|getDependencies
argument_list|(
operator|(
name|ModuleDescriptor
operator|)
name|getProject
argument_list|()
operator|.
name|getReference
argument_list|(
literal|"ivy.resolved.descriptor"
argument_list|)
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|settings
argument_list|,
name|_cache
argument_list|)
argument_list|)
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|settings
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Map
name|artifactsToCopy
init|=
name|ivy
operator|.
name|determineArtifactsToCopy
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|)
argument_list|,
name|confs
argument_list|,
name|_cache
argument_list|,
name|_pattern
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Map
name|moduleRevToArtifactsMap
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
name|artifactsToCopy
operator|.
name|keySet
argument_list|()
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Set
name|moduleRevArtifacts
init|=
operator|(
name|Set
operator|)
name|moduleRevToArtifactsMap
operator|.
name|get
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|moduleRevArtifacts
operator|==
literal|null
condition|)
block|{
name|moduleRevArtifacts
operator|=
operator|new
name|HashSet
argument_list|()
expr_stmt|;
name|moduleRevToArtifactsMap
operator|.
name|put
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|moduleRevArtifacts
argument_list|)
expr_stmt|;
block|}
name|moduleRevArtifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
name|CacheManager
name|cache
init|=
name|getCacheManager
argument_list|()
decl_stmt|;
name|generateXml
argument_list|(
name|cache
argument_list|,
name|dependencies
argument_list|,
name|moduleRevToArtifactsMap
argument_list|,
name|artifactsToCopy
argument_list|)
expr_stmt|;
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
literal|"syntax errors in ivy file: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
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
name|generateXml
parameter_list|(
name|CacheManager
name|cache
parameter_list|,
name|IvyNode
index|[]
name|dependencies
parameter_list|,
name|Map
name|moduleRevToArtifactsMap
parameter_list|,
name|Map
name|artifactsToCopy
parameter_list|)
block|{
try|try
block|{
name|FileOutputStream
name|fileOuputStream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|_tofile
argument_list|)
decl_stmt|;
try|try
block|{
name|TransformerHandler
name|saxHandler
init|=
name|createTransformerHandler
argument_list|(
name|fileOuputStream
argument_list|)
decl_stmt|;
name|saxHandler
operator|.
name|startDocument
argument_list|()
expr_stmt|;
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"modules"
argument_list|,
literal|"modules"
argument_list|,
operator|new
name|AttributesImpl
argument_list|()
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
name|dependencies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|IvyNode
name|dependency
init|=
name|dependencies
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|dependency
operator|.
name|getModuleRevision
argument_list|()
operator|==
literal|null
operator|||
name|dependency
operator|.
name|isCompletelyEvicted
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|startModule
argument_list|(
name|saxHandler
argument_list|,
name|dependency
argument_list|)
expr_stmt|;
name|Set
name|artifactsOfModuleRev
init|=
operator|(
name|Set
operator|)
name|moduleRevToArtifactsMap
operator|.
name|get
argument_list|(
name|dependency
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifactsOfModuleRev
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|artifactsOfModuleRev
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
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|startArtifact
argument_list|(
name|saxHandler
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|writeOriginLocationIfPresent
argument_list|(
name|cache
argument_list|,
name|saxHandler
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|writeCacheLocation
argument_list|(
name|cache
argument_list|,
name|saxHandler
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|Set
name|artifactDestPaths
init|=
operator|(
name|Set
operator|)
name|artifactsToCopy
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|artifactDestPaths
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|artifactDestPath
init|=
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|writeRetrieveLocation
argument_list|(
name|saxHandler
argument_list|,
name|artifactDestPath
argument_list|)
expr_stmt|;
block|}
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"artifact"
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
block|}
block|}
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"module"
argument_list|,
literal|"module"
argument_list|)
expr_stmt|;
block|}
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"modules"
argument_list|,
literal|"modules"
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|endDocument
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|fileOuputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SAXException
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
literal|"impossible to generate report"
argument_list|,
name|e
argument_list|)
throw|;
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
name|TransformerHandler
name|createTransformerHandler
parameter_list|(
name|FileOutputStream
name|fileOuputStream
parameter_list|)
throws|throws
name|TransformerFactoryConfigurationError
throws|,
name|TransformerConfigurationException
throws|,
name|SAXException
block|{
name|SAXTransformerFactory
name|transformerFact
init|=
operator|(
name|SAXTransformerFactory
operator|)
name|SAXTransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|TransformerHandler
name|saxHandler
init|=
name|transformerFact
operator|.
name|newTransformerHandler
argument_list|()
decl_stmt|;
name|saxHandler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|getTransformer
argument_list|()
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|setResult
argument_list|(
operator|new
name|StreamResult
argument_list|(
name|fileOuputStream
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|saxHandler
return|;
block|}
specifier|private
name|void
name|startModule
parameter_list|(
name|TransformerHandler
name|saxHandler
parameter_list|,
name|IvyNode
name|dependency
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|moduleAttrs
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|moduleAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"organisation"
argument_list|,
literal|"organisation"
argument_list|,
literal|"CDATA"
argument_list|,
name|dependency
operator|.
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|moduleAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"name"
argument_list|,
literal|"CDATA"
argument_list|,
name|dependency
operator|.
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|moduleAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"rev"
argument_list|,
literal|"rev"
argument_list|,
literal|"CDATA"
argument_list|,
name|dependency
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|moduleAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"status"
argument_list|,
literal|"status"
argument_list|,
literal|"CDATA"
argument_list|,
name|dependency
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"module"
argument_list|,
literal|"module"
argument_list|,
name|moduleAttrs
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|startArtifact
parameter_list|(
name|TransformerHandler
name|saxHandler
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|SAXException
block|{
name|AttributesImpl
name|artifactAttrs
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
name|artifactAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"name"
argument_list|,
literal|"name"
argument_list|,
literal|"CDATA"
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|artifactAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"ext"
argument_list|,
literal|"ext"
argument_list|,
literal|"CDATA"
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|)
expr_stmt|;
name|artifactAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"type"
argument_list|,
literal|"type"
argument_list|,
literal|"CDATA"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"artifact"
argument_list|,
literal|"artifact"
argument_list|,
name|artifactAttrs
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeOriginLocationIfPresent
parameter_list|(
name|CacheManager
name|cache
parameter_list|,
name|TransformerHandler
name|saxHandler
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|ArtifactOrigin
name|origin
init|=
name|cache
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|origin
operator|!=
literal|null
condition|)
block|{
name|String
name|originName
init|=
name|origin
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|boolean
name|isOriginLocal
init|=
name|origin
operator|.
name|isLocal
argument_list|()
decl_stmt|;
name|String
name|originLocation
decl_stmt|;
name|AttributesImpl
name|originLocationAttrs
init|=
operator|new
name|AttributesImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOriginLocal
condition|)
block|{
name|originLocationAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"is-local"
argument_list|,
literal|"is-local"
argument_list|,
literal|"CDATA"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|File
name|originNameFile
init|=
operator|new
name|File
argument_list|(
name|originName
argument_list|)
decl_stmt|;
name|StringBuffer
name|originNameWithSlashes
init|=
operator|new
name|StringBuffer
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|replaceFileSeparatorWithSlash
argument_list|(
name|originNameFile
argument_list|,
name|originNameWithSlashes
argument_list|)
expr_stmt|;
name|originLocation
operator|=
name|originNameWithSlashes
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|originLocationAttrs
operator|.
name|addAttribute
argument_list|(
literal|null
argument_list|,
literal|"is-local"
argument_list|,
literal|"is-local"
argument_list|,
literal|"CDATA"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|originLocation
operator|=
name|originName
expr_stmt|;
block|}
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"origin-location"
argument_list|,
literal|"origin-location"
argument_list|,
name|originLocationAttrs
argument_list|)
expr_stmt|;
name|char
index|[]
name|originLocationAsChars
init|=
name|originLocation
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|saxHandler
operator|.
name|characters
argument_list|(
name|originLocationAsChars
argument_list|,
literal|0
argument_list|,
name|originLocationAsChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"origin-location"
argument_list|,
literal|"origin-location"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeCacheLocation
parameter_list|(
name|CacheManager
name|cache
parameter_list|,
name|TransformerHandler
name|saxHandler
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|SAXException
block|{
name|ArtifactOrigin
name|origin
init|=
name|cache
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|File
name|archiveInCacheFile
init|=
name|cache
operator|.
name|getArchiveFileInCache
argument_list|(
name|artifact
argument_list|,
name|origin
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|StringBuffer
name|archiveInCachePathWithSlashes
init|=
operator|new
name|StringBuffer
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|replaceFileSeparatorWithSlash
argument_list|(
name|archiveInCacheFile
argument_list|,
name|archiveInCachePathWithSlashes
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"cache-location"
argument_list|,
literal|"cache-location"
argument_list|,
operator|new
name|AttributesImpl
argument_list|()
argument_list|)
expr_stmt|;
name|char
index|[]
name|archiveInCachePathAsChars
init|=
name|archiveInCachePathWithSlashes
operator|.
name|toString
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|saxHandler
operator|.
name|characters
argument_list|(
name|archiveInCachePathAsChars
argument_list|,
literal|0
argument_list|,
name|archiveInCachePathAsChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"cache-location"
argument_list|,
literal|"cache-location"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeRetrieveLocation
parameter_list|(
name|TransformerHandler
name|saxHandler
parameter_list|,
name|String
name|artifactDestPath
parameter_list|)
throws|throws
name|SAXException
block|{
name|artifactDestPath
operator|=
name|removeLeadingPath
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|artifactDestPath
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuffer
name|artifactDestPathWithSlashes
init|=
operator|new
name|StringBuffer
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|replaceFileSeparatorWithSlash
argument_list|(
operator|new
name|File
argument_list|(
name|artifactDestPath
argument_list|)
argument_list|,
name|artifactDestPathWithSlashes
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|startElement
argument_list|(
literal|null
argument_list|,
literal|"retrieve-location"
argument_list|,
literal|"retrieve-location"
argument_list|,
operator|new
name|AttributesImpl
argument_list|()
argument_list|)
expr_stmt|;
name|char
index|[]
name|artifactDestPathAsChars
init|=
name|artifactDestPathWithSlashes
operator|.
name|toString
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|saxHandler
operator|.
name|characters
argument_list|(
name|artifactDestPathAsChars
argument_list|,
literal|0
argument_list|,
name|artifactDestPathAsChars
operator|.
name|length
argument_list|)
expr_stmt|;
name|saxHandler
operator|.
name|endElement
argument_list|(
literal|null
argument_list|,
literal|"retrieve-location"
argument_list|,
literal|"retrieve-location"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|replaceFileSeparatorWithSlash
parameter_list|(
name|File
name|file
parameter_list|,
name|StringBuffer
name|resultPath
parameter_list|)
block|{
if|if
condition|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|replaceFileSeparatorWithSlash
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|resultPath
argument_list|)
expr_stmt|;
name|resultPath
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|String
name|fileSeparator
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"file.separator"
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|file
operator|.
name|getPath
argument_list|()
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|fileSeparator
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
name|fileSeparator
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resultPath
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultPath
operator|.
name|append
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// method largely inspired by ant 1.6.5 FileUtils method
specifier|public
name|String
name|removeLeadingPath
parameter_list|(
name|File
name|leading
parameter_list|,
name|File
name|path
parameter_list|)
block|{
name|String
name|l
init|=
name|leading
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|String
name|p
init|=
name|path
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|.
name|equals
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
comment|// ensure that l ends with a /
comment|// so we never think /foo was a parent directory of /foobar
if|if
condition|(
operator|!
name|l
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|l
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
block|}
return|return
operator|(
name|p
operator|.
name|startsWith
argument_list|(
name|l
argument_list|)
operator|)
condition|?
name|p
operator|.
name|substring
argument_list|(
name|l
operator|.
name|length
argument_list|()
argument_list|)
else|:
name|p
return|;
block|}
specifier|protected
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
name|CacheManager
name|cache
init|=
operator|new
name|CacheManager
argument_list|(
name|getSettings
argument_list|()
argument_list|,
name|_cache
argument_list|)
decl_stmt|;
return|return
name|cache
return|;
block|}
block|}
end_class

end_unit

