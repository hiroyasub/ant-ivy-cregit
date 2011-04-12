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
name|ArrayList
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
name|P2CompositeParser
implements|implements
name|XMLInputParser
block|{
specifier|private
name|List
comment|/*<String> */
name|childLocations
decl_stmt|;
specifier|public
name|List
name|getChildLocations
parameter_list|()
block|{
return|return
name|childLocations
return|;
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
name|childLocations
operator|=
name|handler
operator|.
name|childLocations
expr_stmt|;
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
name|List
comment|/*<String> */
name|childLocations
decl_stmt|;
specifier|public
name|RepositoryHandler
parameter_list|()
block|{
name|super
argument_list|(
name|REPOSITORY
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ChildrenHandler
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
name|childLocations
operator|=
operator|(
operator|(
name|ChildrenHandler
operator|)
name|child
operator|)
operator|.
name|childLocations
expr_stmt|;
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
name|ChildrenHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CHILDREN
init|=
literal|"children"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SIZE
init|=
literal|"size"
decl_stmt|;
name|List
comment|/*<String> */
name|childLocations
decl_stmt|;
specifier|public
name|ChildrenHandler
parameter_list|()
block|{
name|super
argument_list|(
name|CHILDREN
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
operator|new
name|ChildHandler
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
name|childLocations
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ChildHandler
operator|)
name|child
operator|)
operator|.
name|location
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
name|childLocations
operator|=
operator|new
name|ArrayList
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|ChildHandler
extends|extends
name|DelegetingHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CHILD
init|=
literal|"child"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOCATION
init|=
literal|"location"
decl_stmt|;
name|String
name|location
decl_stmt|;
specifier|public
name|ChildHandler
parameter_list|()
block|{
name|super
argument_list|(
name|CHILD
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
name|location
operator|=
name|atts
operator|.
name|getValue
argument_list|(
name|LOCATION
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

