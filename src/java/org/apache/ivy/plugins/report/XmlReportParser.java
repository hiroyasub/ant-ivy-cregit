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
name|report
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
name|Date
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
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParserFactory
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
name|DefaultArtifact
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
name|report
operator|.
name|DownloadStatus
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
name|extendable
operator|.
name|ExtendableItemHelper
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
name|Attributes
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
name|DefaultHandler
import|;
end_import

begin_class
specifier|public
class|class
name|XmlReportParser
block|{
specifier|private
specifier|static
class|class
name|SaxXmlReportParser
block|{
specifier|private
name|List
name|mrids
decl_stmt|;
specifier|private
name|List
name|defaultMrids
decl_stmt|;
specifier|private
name|List
name|realMrids
decl_stmt|;
specifier|private
name|List
name|artifacts
decl_stmt|;
specifier|private
name|List
name|artifactReports
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mRevisionId
decl_stmt|;
specifier|private
name|File
name|report
decl_stmt|;
name|SaxXmlReportParser
parameter_list|(
name|File
name|report
parameter_list|)
block|{
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|artifactReports
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|mrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|defaultMrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|realMrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|report
operator|=
name|report
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|Exception
block|{
name|SAXParser
name|saxParser
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
name|saxParser
operator|.
name|parse
argument_list|(
name|report
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
block|{
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
name|branch
decl_stmt|;
specifier|private
name|String
name|revision
decl_stmt|;
specifier|private
name|int
name|position
decl_stmt|;
specifier|private
name|Date
name|pubdate
decl_stmt|;
specifier|private
name|boolean
name|skip
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid
decl_stmt|;
specifier|private
name|boolean
name|isDefault
decl_stmt|;
specifier|private
name|SortedMap
name|revisionsMap
init|=
operator|new
name|TreeMap
argument_list|()
decl_stmt|;
comment|// Use a TreeMap to order by
comment|// position (position = key)
specifier|private
name|List
name|revisionArtifacts
init|=
literal|null
decl_stmt|;
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
literal|"module"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|organisation
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"organisation"
argument_list|)
expr_stmt|;
name|module
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"revision"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|revisionArtifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|branch
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"branch"
argument_list|)
expr_stmt|;
name|revision
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|isDefault
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"default"
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
expr_stmt|;
comment|// retrieve position from file. If no position is found, it may be an old
comment|// report generated with a previous version,
comment|// in which case, we put it at the last position
name|String
name|pos
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"position"
argument_list|)
decl_stmt|;
name|position
operator|=
name|pos
operator|==
literal|null
condition|?
name|getMaxPos
argument_list|()
operator|+
literal|1
else|:
name|Integer
operator|.
name|valueOf
argument_list|(
name|pos
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
if|if
condition|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"error"
argument_list|)
operator|!=
literal|null
operator|||
name|attributes
operator|.
name|getValue
argument_list|(
literal|"evicted"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|skip
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|revisionsMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|position
argument_list|)
argument_list|,
name|revisionArtifacts
argument_list|)
expr_stmt|;
name|mrid
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|ExtendableItemHelper
operator|.
name|getExtraAttributes
argument_list|(
name|attributes
argument_list|,
literal|"extra-"
argument_list|)
argument_list|)
expr_stmt|;
name|mrids
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
if|if
condition|(
name|isDefault
condition|)
block|{
name|defaultMrids
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|realMrids
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|pubdate
operator|=
name|Ivy
operator|.
name|DATE_FORMAT
operator|.
name|parse
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"pubdate"
argument_list|)
argument_list|)
expr_stmt|;
name|skip
operator|=
literal|false
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
name|IllegalArgumentException
argument_list|(
literal|"invalid publication date for "
operator|+
name|organisation
operator|+
literal|" "
operator|+
name|module
operator|+
literal|" "
operator|+
name|revision
operator|+
literal|": "
operator|+
name|attributes
operator|.
name|getValue
argument_list|(
literal|"pubdate"
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
if|else if
condition|(
literal|"artifact"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
name|String
name|status
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"status"
argument_list|)
decl_stmt|;
name|String
name|artifactName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|ext
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"ext"
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
name|pubdate
argument_list|,
name|artifactName
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|ExtendableItemHelper
operator|.
name|getExtraAttributes
argument_list|(
name|attributes
argument_list|,
literal|"extra-"
argument_list|)
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|aReport
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|aReport
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|fromString
argument_list|(
name|status
argument_list|)
argument_list|)
expr_stmt|;
name|aReport
operator|.
name|setDownloadDetails
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"details"
argument_list|)
argument_list|)
expr_stmt|;
name|aReport
operator|.
name|setSize
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"size"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|aReport
operator|.
name|setDownloadTimeMillis
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"time"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"location"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|aReport
operator|.
name|setLocalFile
argument_list|(
operator|new
name|File
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"location"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|revisionArtifacts
operator|.
name|add
argument_list|(
name|aReport
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"origin-location"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
if|if
condition|(
name|skip
condition|)
block|{
return|return;
block|}
name|ArtifactDownloadReport
name|aReport
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|revisionArtifacts
operator|.
name|get
argument_list|(
name|revisionArtifacts
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|aReport
operator|.
name|setArtifactOrigin
argument_list|(
operator|new
name|ArtifactOrigin
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"is-local"
argument_list|)
argument_list|)
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
literal|"location"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"info"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|organisation
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"organisation"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"module"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"branch"
argument_list|)
decl_stmt|;
name|String
name|revision
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"revision"
argument_list|)
decl_stmt|;
name|Map
name|extraAttributes
init|=
operator|new
name|HashMap
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
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|attName
init|=
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|attName
operator|.
name|startsWith
argument_list|(
literal|"extra-"
argument_list|)
condition|)
block|{
name|String
name|extraAttrName
init|=
name|attName
operator|.
name|substring
argument_list|(
literal|"extra-"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|extraAttrValue
init|=
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
name|extraAttrName
argument_list|,
name|extraAttrValue
argument_list|)
expr_stmt|;
block|}
block|}
name|mRevisionId
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qname
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
literal|"dependencies"
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
comment|// add the artifacts in the correct order
for|for
control|(
name|Iterator
name|it
init|=
name|revisionsMap
operator|.
name|values
argument_list|()
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
name|List
name|artifactReports
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|SaxXmlReportParser
operator|.
name|this
operator|.
name|artifactReports
operator|.
name|addAll
argument_list|(
name|artifactReports
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|artifactReports
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
name|ArtifactDownloadReport
name|artifactReport
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifactReport
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|artifactReport
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|int
name|getMaxPos
parameter_list|()
block|{
return|return
name|revisionsMap
operator|.
name|isEmpty
argument_list|()
condition|?
operator|-
literal|1
else|:
operator|(
operator|(
name|Integer
operator|)
name|revisionsMap
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
name|revisionsMap
operator|.
name|size
argument_list|()
operator|-
literal|1
index|]
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getArtifacts
parameter_list|()
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|List
name|getArtifactReports
parameter_list|()
block|{
return|return
name|artifactReports
return|;
block|}
specifier|public
name|List
name|getModuleRevisionIds
parameter_list|()
block|{
return|return
name|mrids
return|;
block|}
specifier|public
name|List
name|getRealModuleRevisionIds
parameter_list|()
block|{
return|return
name|realMrids
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getResolvedModule
parameter_list|()
block|{
return|return
name|mRevisionId
return|;
block|}
block|}
specifier|private
name|SaxXmlReportParser
name|parser
init|=
literal|null
decl_stmt|;
specifier|public
name|void
name|parse
parameter_list|(
name|File
name|report
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
operator|!
name|report
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Report file '"
operator|+
name|report
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' does not exist."
argument_list|)
throw|;
block|}
name|parser
operator|=
operator|new
name|SaxXmlReportParser
argument_list|(
name|report
argument_list|)
expr_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ParseException
name|pe
init|=
operator|new
name|ParseException
argument_list|(
literal|"failed to parse report: "
operator|+
name|report
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
specifier|public
name|Artifact
index|[]
name|getArtifacts
parameter_list|()
block|{
return|return
operator|(
name|Artifact
index|[]
operator|)
name|parser
operator|.
name|getArtifacts
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|parser
operator|.
name|getArtifacts
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
index|[]
name|getArtifactReports
parameter_list|()
block|{
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|parser
operator|.
name|getArtifactReports
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|parser
operator|.
name|getArtifactReports
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ModuleRevisionId
index|[]
name|getDependencyRevisionIds
parameter_list|()
block|{
return|return
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|parser
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|parser
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ModuleRevisionId
index|[]
name|getRealDependencyRevisionIds
parameter_list|()
block|{
return|return
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|parser
operator|.
name|getRealModuleRevisionIds
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|parser
operator|.
name|getRealModuleRevisionIds
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Returns the<tt>ModuleRevisionId</tt> of the resolved module.      */
specifier|public
name|ModuleRevisionId
name|getResolvedModule
parameter_list|()
block|{
return|return
name|parser
operator|.
name|getResolvedModule
argument_list|()
return|;
block|}
block|}
end_class

end_unit

