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
name|updatesite
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|util
operator|.
name|DelegatingHandler
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
name|EclipseUpdateSiteParser
block|{
specifier|public
specifier|static
name|UpdateSite
name|parse
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|SiteHandler
name|handler
init|=
operator|new
name|SiteHandler
argument_list|()
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
return|return
name|handler
operator|.
name|updatesite
return|;
block|}
specifier|private
specifier|static
class|class
name|SiteHandler
extends|extends
name|DelegatingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SITE
init|=
literal|"site"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|URL
init|=
literal|"url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PACK200
init|=
literal|"pack200"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MIRRORS_URL
init|=
literal|"mirrorsURL"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ASSOCIATE_SITES_URL
init|=
literal|"associateSitesURL"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DIGEST_URL
init|=
literal|"digestURL"
decl_stmt|;
name|UpdateSite
name|updatesite
decl_stmt|;
specifier|public
name|SiteHandler
parameter_list|()
block|{
name|super
argument_list|(
name|SITE
argument_list|)
expr_stmt|;
comment|// addChild(new DescriptionHandler(), new ChildElementHandler() {
comment|// public void childHandled(DelegatingHandler child) {
comment|// updateSite.setDescription(child.getBufferedChars().trim());
comment|// }
comment|// });
name|addChild
argument_list|(
operator|new
name|FeatureHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|<
name|FeatureHandler
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|childHandled
parameter_list|(
name|FeatureHandler
name|child
parameter_list|)
block|{
name|updatesite
operator|.
name|addFeature
argument_list|(
name|child
operator|.
name|feature
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// addChild(new ArchiveHandler(), new ChildElementHandler() {
comment|// public void childHandled(DelegatingHandler child) {
comment|// updateSite.addArchive(((ArchiveHandler) child).archive);
comment|// }
comment|// });
comment|// addChild(new CategoryDefHandler(), new ChildElementHandler() {
comment|// public void childHandled(DelegatingHandler child) {
comment|// updateSite.addCategoryDef(((CategoryDefHandler) child).categoryDef);
comment|// }
comment|// });
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
block|{
name|updatesite
operator|=
operator|new
name|UpdateSite
argument_list|()
expr_stmt|;
name|String
name|url
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
operator|(
literal|""
operator|.
name|equals
argument_list|(
name|url
operator|.
name|trim
argument_list|()
argument_list|)
operator|)
condition|)
block|{
if|if
condition|(
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
name|File
operator|.
name|separator
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
try|try
block|{
name|updatesite
operator|.
name|setUri
argument_list|(
operator|new
name|URI
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"illegal url"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|String
name|mirrorsURL
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|MIRRORS_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|mirrorsURL
operator|!=
literal|null
operator|&&
name|mirrorsURL
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|updatesite
operator|.
name|setMirrorsURL
argument_list|(
name|mirrorsURL
argument_list|)
expr_stmt|;
block|}
name|String
name|pack200
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|PACK200
argument_list|)
decl_stmt|;
if|if
condition|(
name|pack200
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|pack200
argument_list|)
condition|)
block|{
name|updatesite
operator|.
name|setPack200
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|String
name|digestURL
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|DIGEST_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|digestURL
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|updatesite
operator|.
name|setDigestUri
argument_list|(
operator|new
name|URI
argument_list|(
name|digestURL
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"illegal url"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|String
name|associateSitesURL
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|ASSOCIATE_SITES_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|associateSitesURL
operator|!=
literal|null
condition|)
block|{
name|updatesite
operator|.
name|setAssociateSitesURL
argument_list|(
name|associateSitesURL
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// private static class DescriptionHandler extends DelegatingHandler {
comment|//
comment|// private static final String DESCRIPTION = "description";
comment|//
comment|// private static final String URL = "url";
comment|//
comment|// public DescriptionHandler() {
comment|// super(DESCRIPTION);
comment|// setBufferingChar(true);
comment|// }
comment|//
comment|// protected void handleAttributes(Attributes atts) {
comment|// String url = atts.getValue(URL);
comment|// }
comment|// }
specifier|private
specifier|static
class|class
name|FeatureHandler
extends|extends
name|DelegatingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FEATURE
init|=
literal|"feature"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"version"
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
name|URL
init|=
literal|"url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATCH
init|=
literal|"patch"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARCH
init|=
literal|"arch"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NL
init|=
literal|"nl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WS
init|=
literal|"ws"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OS
init|=
literal|"os"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LABEL
init|=
literal|"label"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"type"
decl_stmt|;
specifier|private
name|EclipseFeature
name|feature
decl_stmt|;
specifier|public
name|FeatureHandler
parameter_list|()
block|{
name|super
argument_list|(
name|FEATURE
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|CategoryHandler
argument_list|()
argument_list|,
operator|new
name|ChildElementHandler
argument_list|<
name|CategoryHandler
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|childHandled
parameter_list|(
name|CategoryHandler
name|child
parameter_list|)
block|{
name|feature
operator|.
name|addCategory
argument_list|(
name|child
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
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
name|String
name|version
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|VERSION
argument_list|)
decl_stmt|;
name|feature
operator|=
operator|new
name|EclipseFeature
argument_list|(
name|id
argument_list|,
operator|new
name|Version
argument_list|(
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|url
init|=
name|atts
operator|.
name|getValue
argument_list|(
name|URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|feature
operator|.
name|setURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
name|feature
operator|.
name|setType
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setLabel
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|LABEL
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setOS
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|OS
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setWS
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|WS
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setNL
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|NL
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setArch
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|ARCH
argument_list|)
argument_list|)
expr_stmt|;
name|feature
operator|.
name|setPatch
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
name|PATCH
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|CategoryHandler
extends|extends
name|DelegatingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CATEGORY
init|=
literal|"category"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
name|String
name|name
decl_stmt|;
specifier|public
name|CategoryHandler
parameter_list|()
block|{
name|super
argument_list|(
name|CATEGORY
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleAttributes
parameter_list|(
name|Attributes
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
name|name
operator|=
name|atts
operator|.
name|getValue
argument_list|(
name|NAME
argument_list|)
expr_stmt|;
block|}
block|}
comment|// private static class ArchiveHandler extends DelegatingHandler {
comment|//
comment|// private static final String ARCHIVE = "archive";
comment|//
comment|// private static final String URL = "url";
comment|//
comment|// private static final String PATH = "path";
comment|//
comment|// private Archive archive;
comment|//
comment|// public ArchiveHandler() {
comment|// super(ARCHIVE);
comment|// }
comment|//
comment|// protected void handleAttributes(Attributes atts) throws SAXException {
comment|// archive = new Archive();
comment|//
comment|// String path = atts.getValue(PATH);
comment|// archive.setPath(path);
comment|//
comment|// String url = atts.getValue(URL);
comment|// archive.setURL(url);
comment|//
comment|// }
comment|// }
comment|// private static class CategoryDefHandler extends DelegatingHandler {
comment|//
comment|// private static final String CATEGORY_DEF = "category-def";
comment|//
comment|// private static final String NAME = "name";
comment|//
comment|// private static final String LABEL = "label";
comment|//
comment|// private CategoryDef categoryDef;
comment|//
comment|// public CategoryDefHandler() {
comment|// super(CATEGORY_DEF);
comment|// addChild(new DescriptionHandler(), new ChildElementHandler<DescriptionHandler>() {
comment|// public void childHandled(DescriptionHandler child) {
comment|// categoryDef.setDescription(child.getBufferedChars().trim());
comment|// }
comment|// });
comment|// }
comment|//
comment|// protected void handleAttributes(Attributes atts) throws SAXException {
comment|// categoryDef = new CategoryDef();
comment|//
comment|// String name = atts.getValue(NAME);
comment|// categoryDef.setName(name);
comment|//
comment|// String label = atts.getValue(LABEL);
comment|// categoryDef.setLabel(label);
comment|// }
comment|// }
block|}
end_class

end_unit

