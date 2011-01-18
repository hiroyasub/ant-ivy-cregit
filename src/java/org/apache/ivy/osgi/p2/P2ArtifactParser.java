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
name|osgi
operator|.
name|p2
package|;
end_package

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
name|Map
operator|.
name|Entry
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
name|ParserConfigurationException
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
name|osgi
operator|.
name|p2
operator|.
name|PropertiesParser
operator|.
name|PropertiesHandler
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
name|osgi
operator|.
name|util
operator|.
name|DelegetingHandler
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
name|osgi
operator|.
name|util
operator|.
name|Version
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
name|XMLHelper
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

begin_class
specifier|public
class|class
name|P2ArtifactParser
implements|implements
name|XMLInputParser
block|{
specifier|private
specifier|final
name|P2Descriptor
name|p2Descriptor
decl_stmt|;
specifier|public
name|P2ArtifactParser
parameter_list|(
name|P2Descriptor
name|p2Descriptor
parameter_list|)
block|{
name|this
operator|.
name|p2Descriptor
operator|=
name|p2Descriptor
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|RepositoryHandler
name|handler
init|=
operator|new
name|RepositoryHandler
argument_list|(
name|p2Descriptor
argument_list|)
decl_stmt|;
try|try
block|{
name|XMLHelper
operator|.
name|parse
argument_list|(
name|in
argument_list|,
literal|null
argument_list|,
name|handler
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|static
class|class
name|RepositoryHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY
init|=
literal|"repository"
decl_stmt|;
comment|// private static final String NAME = "name";
comment|//
comment|// private static final String TYPE = "type";
comment|//
comment|// private static final String VERSION = "version";
specifier|private
name|Map
comment|/*<String, String> */
name|patternsByClassifier
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|RepositoryHandler
parameter_list|(
specifier|final
name|P2Descriptor
name|p2Descriptor
parameter_list|)
block|{
name|super
argument_list|(
name|REPOSITORY
argument_list|)
expr_stmt|;
comment|// addChild(new PropertiesHandler(), new ChildElementHandler() {
comment|// public void childHanlded(DelegetingHandler child) {
comment|// }
comment|// });
name|addChild
argument_list|(
operator|new
name|MappingsHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|Iterator
name|it
init|=
operator|(
operator|(
name|MappingsHandler
operator|)
name|child
operator|)
operator|.
name|outputByFilter
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
name|entry
init|=
operator|(
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|filter
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|filter
operator|.
name|startsWith
argument_list|(
literal|"(& (classifier="
argument_list|)
operator|&&
name|filter
operator|.
name|endsWith
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|String
name|classifier
init|=
name|filter
operator|.
name|substring
argument_list|(
literal|15
argument_list|,
name|filter
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
decl_stmt|;
name|patternsByClassifier
operator|.
name|put
argument_list|(
name|classifier
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ArtifactsHandler
argument_list|(
name|p2Descriptor
argument_list|,
name|patternsByClassifier
argument_list|)
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
comment|// nothing to do
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// protected void handleAttributes(Attributes atts) {
comment|// String name = atts.getValue(NAME);
comment|// String type = atts.getValue(TYPE);
comment|// String version = atts.getValue(VERSION);
comment|// }
block|}
specifier|static
class|class
name|MappingsHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|MAPPINGS
init|=
literal|"mappings"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SIZE
init|=
literal|"size"
decl_stmt|;
name|Map
comment|/*<String, String> */
name|outputByFilter
decl_stmt|;
specifier|public
name|MappingsHandler
parameter_list|()
block|{
name|super
argument_list|(
name|MAPPINGS
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|RuleHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|outputByFilter
operator|.
name|put
argument_list|(
operator|(
operator|(
name|RuleHandler
operator|)
name|child
operator|)
operator|.
name|filter
argument_list|,
operator|(
operator|(
name|RuleHandler
operator|)
name|child
operator|)
operator|.
name|output
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
block|{
name|int
name|size
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|SIZE
argument_list|)
argument_list|)
decl_stmt|;
name|outputByFilter
operator|=
operator|new
name|HashMap
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|RuleHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RULE
init|=
literal|"rule"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FILTER
init|=
literal|"filter"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTPUT
init|=
literal|"output"
decl_stmt|;
specifier|private
name|String
name|filter
decl_stmt|;
specifier|private
name|String
name|output
decl_stmt|;
specifier|public
name|RuleHandler
parameter_list|()
block|{
name|super
argument_list|(
name|RULE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
block|{
name|filter
operator|=
name|atts
operator|.
name|getValue
argument_list|(
name|FILTER
argument_list|)
expr_stmt|;
name|output
operator|=
name|atts
operator|.
name|getValue
argument_list|(
name|OUTPUT
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ArtifactsHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACTS
init|=
literal|"artifacts"
decl_stmt|;
comment|// private static final String SIZE = "size";
specifier|public
name|ArtifactsHandler
parameter_list|(
specifier|final
name|P2Descriptor
name|p2Descriptor
parameter_list|,
specifier|final
name|Map
comment|/*<String, String> */
name|patternsByClassifier
parameter_list|)
block|{
name|super
argument_list|(
name|ARTIFACTS
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ArtifactHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
name|P2Artifact
name|a
init|=
operator|(
operator|(
name|ArtifactHandler
operator|)
name|child
operator|)
operator|.
name|p2Artifact
decl_stmt|;
name|String
name|url
init|=
operator|(
name|String
operator|)
name|patternsByClassifier
operator|.
name|get
argument_list|(
name|a
operator|.
name|getClassifier
argument_list|()
argument_list|)
decl_stmt|;
name|p2Descriptor
operator|.
name|addArtifactUrl
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|,
name|a
operator|.
name|getVersion
argument_list|()
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// protected void handleAttributes(Attributes atts) {
comment|// int size = Integer.parseInt(atts.getValue(SIZE));
comment|// artifacts = new ArrayList(size);
comment|// }
block|}
specifier|static
class|class
name|ArtifactHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT
init|=
literal|"artifact"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSIFIER
init|=
literal|"classifier"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"id"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
decl_stmt|;
name|P2Artifact
name|p2Artifact
decl_stmt|;
specifier|public
name|ArtifactHandler
parameter_list|()
block|{
name|super
argument_list|(
name|ARTIFACT
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|PropertiesHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|()
block|{
specifier|public
name|void
name|childHanlded
parameter_list|(
name|DelegetingHandler
name|child
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
block|{
name|String
name|id
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|ID
argument_list|)
decl_stmt|;
name|Version
name|version
init|=
operator|new
name|Version
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|CLASSIFIER
argument_list|)
decl_stmt|;
name|p2Artifact
operator|=
operator|new
name|P2Artifact
argument_list|(
name|id
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
