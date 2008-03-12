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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|LinkedList
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
name|plugins
operator|.
name|repository
operator|.
name|Resource
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
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|SAXParseException
import|;
end_import

begin_comment
comment|/**  * Provides the method to read some data out of the DOM tree of a pom   * file.  */
end_comment

begin_class
specifier|public
class|class
name|PomReader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGING
init|=
literal|"packaging"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEPENDENCY
init|=
literal|"dependency"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEPENDENCIES
init|=
literal|"dependencies"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEPENDENCY_MGT
init|=
literal|"dependencyManagement"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROJECT
init|=
literal|"project"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_ID
init|=
literal|"artifactId"
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
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HOMEPAGE
init|=
literal|"url"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PARENT
init|=
literal|"parent"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SCOPE
init|=
literal|"scope"
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
name|OPTIONAL
init|=
literal|"optional"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUSIONS
init|=
literal|"exclusions"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUSION
init|=
literal|"exclusion"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DISTRIBUTION_MGT
init|=
literal|"distributionManagement"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RELOCATION
init|=
literal|"relocation"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTIES
init|=
literal|"properties"
decl_stmt|;
specifier|private
name|HashMap
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Element
name|projectElement
decl_stmt|;
specifier|private
specifier|final
name|Element
name|parentElement
decl_stmt|;
specifier|public
name|PomReader
parameter_list|(
name|URL
name|descriptorURL
parameter_list|,
name|Resource
name|res
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|Document
name|pomDomDoc
init|=
name|XMLHelper
operator|.
name|parseToDom
argument_list|(
name|descriptorURL
argument_list|,
name|res
argument_list|)
decl_stmt|;
name|projectElement
operator|=
name|pomDomDoc
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|PROJECT
operator|.
name|equals
argument_list|(
name|projectElement
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SAXParseException
argument_list|(
literal|"project must be the root tag"
argument_list|,
name|res
operator|.
name|getName
argument_list|()
argument_list|,
name|res
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|parentElement
operator|=
name|getFirstChildElement
argument_list|(
name|projectElement
argument_list|,
name|PARENT
argument_list|)
expr_stmt|;
comment|//TODO read the properties because it must be used to interpret every other field
block|}
specifier|public
name|boolean
name|hasParent
parameter_list|()
block|{
return|return
name|parentElement
operator|!=
literal|null
return|;
block|}
comment|/**      * Add a property if not yet set and value is not null.      * This garantee that property keep the first value that is put on it and that the properties      * are never null.      */
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|prop
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
operator|!
name|properties
operator|.
name|containsKey
argument_list|(
name|prop
argument_list|)
operator|&&
name|val
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|prop
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|GROUP_ID
argument_list|)
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
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|GROUP_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|groupId
argument_list|)
return|;
block|}
specifier|public
name|String
name|getParentGroupId
parameter_list|()
block|{
name|String
name|groupId
init|=
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|GROUP_ID
argument_list|)
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
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|GROUP_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|groupId
argument_list|)
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|ARTIFACT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|ARTIFACT_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getParentArtifactId
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|ARTIFACT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|ARTIFACT_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|VERSION
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getParentVersion
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|parentElement
argument_list|,
name|VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|VERSION
argument_list|)
expr_stmt|;
block|}
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPackaging
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|PACKAGING
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
literal|"jar"
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
specifier|public
name|String
name|getHomePage
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|HOMEPAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|val
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|projectElement
argument_list|,
name|DESCRIPTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|val
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|val
operator|.
name|trim
argument_list|()
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getRelocation
parameter_list|()
block|{
name|Element
name|distrMgt
init|=
name|getFirstChildElement
argument_list|(
name|projectElement
argument_list|,
name|DISTRIBUTION_MGT
argument_list|)
decl_stmt|;
name|Element
name|relocation
init|=
name|getFirstChildElement
argument_list|(
name|distrMgt
argument_list|,
name|RELOCATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|relocation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|String
name|relocGroupId
init|=
name|getFirstChildText
argument_list|(
name|relocation
argument_list|,
name|GROUP_ID
argument_list|)
decl_stmt|;
name|String
name|relocArtId
init|=
name|getFirstChildText
argument_list|(
name|relocation
argument_list|,
name|ARTIFACT_ID
argument_list|)
decl_stmt|;
name|String
name|relocVersion
init|=
name|getFirstChildText
argument_list|(
name|relocation
argument_list|,
name|VERSION
argument_list|)
decl_stmt|;
name|relocGroupId
operator|=
name|relocGroupId
operator|==
literal|null
condition|?
name|getGroupId
argument_list|()
else|:
name|relocGroupId
expr_stmt|;
name|relocArtId
operator|=
name|relocArtId
operator|==
literal|null
condition|?
name|getArtifactId
argument_list|()
else|:
name|relocArtId
expr_stmt|;
name|relocVersion
operator|=
name|relocVersion
operator|==
literal|null
condition|?
name|getVersion
argument_list|()
else|:
name|relocVersion
expr_stmt|;
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|relocGroupId
argument_list|,
name|relocArtId
argument_list|,
name|relocVersion
argument_list|)
return|;
block|}
block|}
specifier|public
name|List
comment|/*<PomDependencyData> */
name|getDependencies
parameter_list|()
block|{
name|Element
name|dependenciesElement
init|=
name|getFirstChildElement
argument_list|(
name|projectElement
argument_list|,
name|DEPENDENCIES
argument_list|)
decl_stmt|;
name|LinkedList
name|dependencies
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependenciesElement
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|childs
init|=
name|dependenciesElement
operator|.
name|getChildNodes
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
name|childs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
name|DEPENDENCY
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|dependencies
operator|.
name|add
argument_list|(
operator|new
name|PomDependencyData
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|dependencies
return|;
block|}
specifier|public
name|List
comment|/*<PomDependencyMgt> */
name|getDependencyMgt
parameter_list|()
block|{
name|Element
name|dependenciesElement
init|=
name|getFirstChildElement
argument_list|(
name|projectElement
argument_list|,
name|DEPENDENCY_MGT
argument_list|)
decl_stmt|;
name|dependenciesElement
operator|=
name|getFirstChildElement
argument_list|(
name|dependenciesElement
argument_list|,
name|DEPENDENCIES
argument_list|)
expr_stmt|;
name|LinkedList
name|dependencies
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|dependenciesElement
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|childs
init|=
name|dependenciesElement
operator|.
name|getChildNodes
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
name|childs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
name|DEPENDENCY
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|dependencies
operator|.
name|add
argument_list|(
operator|new
name|PomDependencyMgt
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|dependencies
return|;
block|}
specifier|public
class|class
name|PomDependencyMgt
block|{
specifier|private
specifier|final
name|Element
name|depElement
decl_stmt|;
name|PomDependencyMgt
parameter_list|(
name|Element
name|depElement
parameter_list|)
block|{
name|this
operator|.
name|depElement
operator|=
name|depElement
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|depElement
argument_list|,
name|GROUP_ID
argument_list|)
decl_stmt|;
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getArtifaceId
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|depElement
argument_list|,
name|ARTIFACT_ID
argument_list|)
decl_stmt|;
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|depElement
argument_list|,
name|VERSION
argument_list|)
decl_stmt|;
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
specifier|public
class|class
name|PomDependencyData
extends|extends
name|PomDependencyMgt
block|{
specifier|private
specifier|final
name|Element
name|depElement
decl_stmt|;
name|PomDependencyData
parameter_list|(
name|Element
name|depElement
parameter_list|)
block|{
name|super
argument_list|(
name|depElement
argument_list|)
expr_stmt|;
name|this
operator|.
name|depElement
operator|=
name|depElement
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|depElement
argument_list|,
name|SCOPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|"compile"
return|;
block|}
else|else
block|{
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
name|String
name|val
init|=
name|getFirstChildText
argument_list|(
name|depElement
argument_list|,
name|CLASSIFIER
argument_list|)
decl_stmt|;
return|return
name|replaceProps
argument_list|(
name|val
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|getFirstChildElement
argument_list|(
name|depElement
argument_list|,
name|OPTIONAL
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|List
comment|/*<ModuleId>*/
name|getExcludedModules
parameter_list|()
block|{
name|Element
name|exclusionsElement
init|=
name|getFirstChildElement
argument_list|(
name|depElement
argument_list|,
name|EXCLUSIONS
argument_list|)
decl_stmt|;
name|LinkedList
name|exclusions
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|exclusionsElement
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|childs
init|=
name|exclusionsElement
operator|.
name|getChildNodes
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
name|childs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
name|EXCLUSION
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|groupId
init|=
name|getFirstChildText
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|,
name|GROUP_ID
argument_list|)
decl_stmt|;
name|String
name|arteficatId
init|=
name|getFirstChildText
argument_list|(
operator|(
name|Element
operator|)
name|node
argument_list|,
name|ARTIFACT_ID
argument_list|)
decl_stmt|;
name|exclusions
operator|.
name|add
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|groupId
argument_list|,
name|arteficatId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|exclusions
return|;
block|}
block|}
comment|/**      * @return the content of the properties tag into the pom.      */
specifier|public
name|Map
comment|/*<String,String> */
name|getPomProperties
parameter_list|()
block|{
name|Map
name|pomProperties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|Element
name|propsEl
init|=
name|getFirstChildElement
argument_list|(
name|projectElement
argument_list|,
name|PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|propsEl
operator|!=
literal|null
condition|)
block|{
name|propsEl
operator|.
name|normalize
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|it
init|=
name|getAllChilds
argument_list|(
name|propsEl
argument_list|)
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
name|Element
name|prop
init|=
operator|(
name|Element
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|pomProperties
operator|.
name|put
argument_list|(
name|prop
operator|.
name|getNodeName
argument_list|()
argument_list|,
name|getTextContent
argument_list|(
name|prop
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|pomProperties
return|;
block|}
specifier|private
name|String
name|replaceProps
parameter_list|(
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|val
argument_list|,
name|properties
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getTextContent
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|NodeList
name|childNodes
init|=
name|element
operator|.
name|getChildNodes
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
name|childNodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|childNodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|child
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
case|case
name|Node
operator|.
name|CDATA_SECTION_NODE
case|:
case|case
name|Node
operator|.
name|TEXT_NODE
case|:
name|result
operator|.
name|append
argument_list|(
name|child
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|getFirstChildText
parameter_list|(
name|Element
name|parentElem
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Element
name|node
init|=
name|getFirstChildElement
argument_list|(
name|parentElem
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
return|return
name|getTextContent
argument_list|(
name|node
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|Element
name|getFirstChildElement
parameter_list|(
name|Element
name|parentElem
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|parentElem
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|NodeList
name|childs
init|=
name|parentElem
operator|.
name|getChildNodes
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
name|childs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|node
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|List
comment|/*<Element> */
name|getAllChilds
parameter_list|(
name|Element
name|parent
parameter_list|)
block|{
name|List
name|r
init|=
operator|new
name|LinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|childs
init|=
name|parent
operator|.
name|getChildNodes
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
name|childs
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childs
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

