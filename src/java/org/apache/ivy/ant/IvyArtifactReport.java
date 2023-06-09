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
name|RepositoryCacheManager
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
name|ArtifactDownloadReport
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|retrieve
operator|.
name|RetrieveOptions
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
comment|/**  * Generates a report of all artifacts involved during the last resolve.  */
end_comment

begin_class
specifier|public
class|class
name|IvyArtifactReport
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
name|File
name|tofile
decl_stmt|;
specifier|private
name|String
name|pattern
decl_stmt|;
specifier|public
name|File
name|getTofile
parameter_list|()
block|{
return|return
name|tofile
return|;
block|}
specifier|public
name|void
name|setTofile
parameter_list|(
name|File
name|aFile
parameter_list|)
block|{
name|tofile
operator|=
name|aFile
expr_stmt|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|aPattern
parameter_list|)
block|{
name|pattern
operator|=
name|aPattern
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
if|if
condition|(
name|tofile
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
name|pattern
operator|=
name|getProperty
argument_list|(
name|pattern
argument_list|,
name|getSettings
argument_list|()
argument_list|,
literal|"ivy.retrieve.pattern"
argument_list|)
expr_stmt|;
try|try
block|{
name|String
index|[]
name|confs
init|=
name|splitToArray
argument_list|(
name|getConf
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getResolveId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|md
operator|=
name|getResolvedDescriptor
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|md
operator|=
name|getResolvedDescriptor
argument_list|(
name|getResolveId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|IvyNode
index|[]
name|dependencies
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getResolveEngine
argument_list|()
operator|.
name|getDependencies
argument_list|(
name|md
argument_list|,
operator|(
operator|(
name|ResolveOptions
operator|)
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
operator|)
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|getResolveId
argument_list|()
argument_list|)
operator|.
name|setValidate
argument_list|(
name|doValidate
argument_list|(
name|getSettings
argument_list|()
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ArtifactDownloadReport
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|artifactsToCopy
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getRetrieveEngine
argument_list|()
operator|.
name|determineArtifactsToCopy
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|getOrganisation
argument_list|()
argument_list|,
name|getModule
argument_list|()
argument_list|,
name|getRevision
argument_list|()
argument_list|)
argument_list|,
name|pattern
argument_list|,
operator|(
operator|(
name|RetrieveOptions
operator|)
operator|new
name|RetrieveOptions
argument_list|()
operator|.
name|setLog
argument_list|(
name|getLog
argument_list|()
argument_list|)
operator|)
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|getResolveId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|Set
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|>
name|moduleRevToArtifactsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactDownloadReport
name|artifact
range|:
name|artifactsToCopy
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|moduleRevArtifacts
init|=
name|moduleRevToArtifactsMap
operator|.
name|get
argument_list|(
name|artifact
operator|.
name|getArtifact
argument_list|()
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
argument_list|<>
argument_list|()
expr_stmt|;
name|moduleRevToArtifactsMap
operator|.
name|put
argument_list|(
name|artifact
operator|.
name|getArtifact
argument_list|()
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
name|generateXml
argument_list|(
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
name|IvyNode
index|[]
name|dependencies
parameter_list|,
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|Set
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|>
name|moduleRevToArtifactsMap
parameter_list|,
name|Map
argument_list|<
name|ArtifactDownloadReport
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|artifactsToCopy
parameter_list|)
block|{
try|try
block|{
try|try
init|(
name|FileOutputStream
name|fileOutputStream
init|=
operator|new
name|FileOutputStream
argument_list|(
name|tofile
argument_list|)
init|)
block|{
name|TransformerHandler
name|saxHandler
init|=
name|createTransformerHandler
argument_list|(
name|fileOutputStream
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
name|IvyNode
name|dependency
range|:
name|dependencies
control|)
block|{
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
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|artifactsOfModuleRev
init|=
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
name|ArtifactDownloadReport
name|artifact
range|:
name|artifactsOfModuleRev
control|)
block|{
name|RepositoryCacheManager
name|cache
init|=
name|dependency
operator|.
name|getModuleRevision
argument_list|()
operator|.
name|getArtifactResolver
argument_list|()
operator|.
name|getRepositoryCacheManager
argument_list|()
decl_stmt|;
name|startArtifact
argument_list|(
name|saxHandler
argument_list|,
name|artifact
operator|.
name|getArtifact
argument_list|()
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
name|writeCacheLocationIfPresent
argument_list|(
name|cache
argument_list|,
name|saxHandler
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|artifactDestPath
range|:
name|artifactsToCopy
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
control|)
block|{
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
block|}
catch|catch
parameter_list|(
name|SAXException
decl||
name|IOException
decl||
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
block|}
specifier|private
name|TransformerHandler
name|createTransformerHandler
parameter_list|(
name|FileOutputStream
name|fileOutputStream
parameter_list|)
throws|throws
name|TransformerFactoryConfigurationError
throws|,
name|TransformerConfigurationException
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
name|fileOutputStream
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
name|ResolvedModuleRevision
name|moduleRevision
init|=
name|dependency
operator|.
name|getModuleRevision
argument_list|()
decl_stmt|;
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
name|moduleRevision
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
name|moduleRevision
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
name|RepositoryCacheManager
name|cache
parameter_list|,
name|TransformerHandler
name|saxHandler
parameter_list|,
name|ArtifactDownloadReport
name|artifact
parameter_list|)
throws|throws
name|SAXException
block|{
name|ArtifactOrigin
name|origin
init|=
name|artifact
operator|.
name|getArtifactOrigin
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|origin
argument_list|)
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
name|originLocation
operator|=
name|originName
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
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
name|writeCacheLocationIfPresent
parameter_list|(
name|RepositoryCacheManager
name|cache
parameter_list|,
name|TransformerHandler
name|saxHandler
parameter_list|,
name|ArtifactDownloadReport
name|artifact
parameter_list|)
throws|throws
name|SAXException
block|{
name|File
name|archiveInCache
init|=
name|artifact
operator|.
name|getLocalFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|archiveInCache
operator|!=
literal|null
condition|)
block|{
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
name|archiveInCacheAsChars
init|=
name|archiveInCache
operator|.
name|getPath
argument_list|()
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|saxHandler
operator|.
name|characters
argument_list|(
name|archiveInCacheAsChars
argument_list|,
literal|0
argument_list|,
name|archiveInCacheAsChars
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
name|artifactDestPath
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
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
block|}
end_class

end_unit

