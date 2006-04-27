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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Artifact
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
name|DefaultArtifact
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
name|ModuleRevisionId
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
name|extendable
operator|.
name|ExtendableItemHelper
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
name|_mrids
decl_stmt|;
specifier|private
name|List
name|_defaultMrids
decl_stmt|;
specifier|private
name|List
name|_realMrids
decl_stmt|;
specifier|private
name|List
name|_artifacts
decl_stmt|;
specifier|private
name|File
name|_report
decl_stmt|;
name|SaxXmlReportParser
parameter_list|(
name|File
name|report
parameter_list|)
block|{
name|_artifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_mrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_defaultMrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_realMrids
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_report
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
name|_report
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
block|{
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
name|_revision
decl_stmt|;
specifier|private
name|int
name|_position
decl_stmt|;
specifier|private
name|Date
name|_pubdate
decl_stmt|;
specifier|private
name|boolean
name|_skip
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|_mrid
decl_stmt|;
specifier|private
name|boolean
name|_default
decl_stmt|;
specifier|private
name|SortedMap
name|_revisionsMap
init|=
operator|new
name|TreeMap
argument_list|()
decl_stmt|;
comment|// Use a TreeMap to order by position (position = key)
specifier|private
name|List
name|_revisionArtifacts
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|_maxPos
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
name|_organisation
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"organisation"
argument_list|)
expr_stmt|;
name|_module
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
name|_revisionArtifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_revision
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|_default
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
comment|// retrieve position from file. If no position is found, it may be an old report generated with a previous version,
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
name|_position
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
name|_skip
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|_revisionsMap
operator|.
name|put
argument_list|(
operator|new
name|Integer
argument_list|(
name|_position
argument_list|)
argument_list|,
name|_revisionArtifacts
argument_list|)
expr_stmt|;
name|_mrid
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|_organisation
argument_list|,
name|_module
argument_list|,
name|_revision
argument_list|,
name|ExtendableItemHelper
operator|.
name|getExtraAttributes
argument_list|(
name|attributes
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"position"
block|,
literal|"name"
block|,
literal|"default"
block|,
literal|"evicted"
block|,
literal|"error"
block|,
literal|"pubdate"
block|,
literal|"conf"
block|,
literal|"searched"
block|,
literal|"downloaded"
block|,
literal|"resolver"
block|,
literal|"artresolver"
block|,
literal|"status"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|_mrids
operator|.
name|add
argument_list|(
name|_mrid
argument_list|)
expr_stmt|;
if|if
condition|(
name|_default
condition|)
block|{
name|_defaultMrids
operator|.
name|add
argument_list|(
name|_mrid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|_realMrids
operator|.
name|add
argument_list|(
name|_mrid
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|_pubdate
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
name|_skip
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
name|_organisation
operator|+
literal|" "
operator|+
name|_module
operator|+
literal|" "
operator|+
name|_revision
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
name|_skip
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
if|if
condition|(
name|status
operator|!=
literal|null
operator|&&
literal|"failed"
operator|.
name|equals
argument_list|(
name|status
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|_mrid
argument_list|,
name|_pubdate
argument_list|,
name|artifactName
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
decl_stmt|;
name|_revisionArtifacts
operator|.
name|add
argument_list|(
name|artifact
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
name|_revisionsMap
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
name|artifacts
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|_artifacts
operator|.
name|addAll
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|int
name|getMaxPos
parameter_list|()
block|{
return|return
name|_revisionsMap
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
name|_revisionsMap
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
name|_revisionsMap
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
name|_artifacts
return|;
block|}
specifier|public
name|List
name|getModuleRevisionIds
parameter_list|()
block|{
return|return
name|_mrids
return|;
block|}
specifier|public
name|List
name|getRealModuleRevisionIds
parameter_list|()
block|{
return|return
name|_realMrids
return|;
block|}
block|}
specifier|public
name|Artifact
index|[]
name|getArtifacts
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|conf
parameter_list|,
name|File
name|cache
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|File
name|report
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
name|XmlReportOutputter
operator|.
name|getReportFileName
argument_list|(
name|moduleId
argument_list|,
name|conf
argument_list|)
argument_list|)
decl_stmt|;
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
literal|"no report file found for "
operator|+
name|moduleId
operator|+
literal|" "
operator|+
name|conf
operator|+
literal|" in "
operator|+
name|cache
operator|+
literal|": ivy was looking for "
operator|+
name|report
argument_list|)
throw|;
block|}
return|return
name|getArtifacts
argument_list|(
name|report
argument_list|)
return|;
block|}
specifier|private
name|Artifact
index|[]
name|getArtifacts
parameter_list|(
name|File
name|report
parameter_list|)
throws|throws
name|ParseException
block|{
try|try
block|{
name|SaxXmlReportParser
name|parser
init|=
operator|new
name|SaxXmlReportParser
argument_list|(
name|report
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
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
catch|catch
parameter_list|(
name|Exception
name|ex
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
name|ex
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
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
specifier|public
name|ModuleRevisionId
index|[]
name|getDependencyRevisionIds
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|conf
parameter_list|,
name|File
name|cache
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|File
name|report
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
name|XmlReportOutputter
operator|.
name|getReportFileName
argument_list|(
name|moduleId
argument_list|,
name|conf
argument_list|)
argument_list|)
decl_stmt|;
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
literal|"no report file found for "
operator|+
name|moduleId
operator|+
literal|" "
operator|+
name|conf
operator|+
literal|" in "
operator|+
name|cache
operator|+
literal|": ivy was looking for "
operator|+
name|report
argument_list|)
throw|;
block|}
return|return
name|getDependencyRevisionIds
argument_list|(
name|report
argument_list|)
return|;
block|}
specifier|private
name|ModuleRevisionId
index|[]
name|getDependencyRevisionIds
parameter_list|(
name|File
name|report
parameter_list|)
throws|throws
name|ParseException
block|{
try|try
block|{
name|SaxXmlReportParser
name|parser
init|=
operator|new
name|SaxXmlReportParser
argument_list|(
name|report
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
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
catch|catch
parameter_list|(
name|Exception
name|ex
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
name|ex
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
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
comment|/**      * Returns all the mrids of the dependencies which have a real module descriptor, i.e. not a default one      */
specifier|public
name|ModuleRevisionId
index|[]
name|getRealDependencyRevisionIds
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|conf
parameter_list|,
name|File
name|cache
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|File
name|report
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
name|XmlReportOutputter
operator|.
name|getReportFileName
argument_list|(
name|moduleId
argument_list|,
name|conf
argument_list|)
argument_list|)
decl_stmt|;
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
literal|"no report file found for "
operator|+
name|moduleId
operator|+
literal|" "
operator|+
name|conf
operator|+
literal|" in "
operator|+
name|cache
operator|+
literal|": ivy was looking for "
operator|+
name|report
argument_list|)
throw|;
block|}
return|return
name|getRealDependencyRevisionIds
argument_list|(
name|report
argument_list|)
return|;
block|}
specifier|private
name|ModuleRevisionId
index|[]
name|getRealDependencyRevisionIds
parameter_list|(
name|File
name|report
parameter_list|)
throws|throws
name|ParseException
block|{
try|try
block|{
name|SaxXmlReportParser
name|parser
init|=
operator|new
name|SaxXmlReportParser
argument_list|(
name|report
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
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
catch|catch
parameter_list|(
name|Exception
name|ex
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
name|ex
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
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
block|}
block|}
end_class

end_unit

