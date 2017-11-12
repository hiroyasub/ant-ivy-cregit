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
name|URL
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
name|Date
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
name|Configuration
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
name|descriptor
operator|.
name|DefaultDependencyDescriptor
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
name|License
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
name|resolve
operator|.
name|ResolveData
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
name|ResolveEngine
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
name|plugins
operator|.
name|parser
operator|.
name|ModuleDescriptorParser
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
name|ParserSettings
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
name|PomModuleDescriptorBuilder
operator|.
name|PomDependencyDescriptor
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
name|PomReader
operator|.
name|PomDependencyData
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
name|PomReader
operator|.
name|PomDependencyMgtElement
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
name|PomReader
operator|.
name|PomPluginElement
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
name|PomReader
operator|.
name|PomProfileElement
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
name|XmlModuleDescriptorWriter
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
name|plugins
operator|.
name|repository
operator|.
name|url
operator|.
name|URLResource
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
name|resolver
operator|.
name|DependencyResolver
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
name|Message
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
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|IvyContext
operator|.
name|getContext
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|Configuration
operator|.
name|Visibility
operator|.
name|PUBLIC
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
name|plugins
operator|.
name|namespace
operator|.
name|NameSpaceHelper
operator|.
name|toSystem
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorBuilder
operator|.
name|MAVEN2_CONFIGURATIONS
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorBuilder
operator|.
name|extractPomProperties
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorBuilder
operator|.
name|getDependencyManagements
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
name|plugins
operator|.
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorBuilder
operator|.
name|getPlugins
import|;
end_import

begin_comment
comment|/**  * A parser for Maven 2 POM.  *<p>  * The configurations used in the generated module descriptor mimics the behavior defined by  * Maven 2 scopes, as documented  *<a href="http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html">here</a>.  * The PomModuleDescriptorParser use a PomDomReader to read the pom, and the  * PomModuleDescriptorBuilder to write the ivy module descriptor using the info read by the  * PomDomReader.  *</p>  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PomModuleDescriptorParser
implements|implements
name|ModuleDescriptorParser
block|{
specifier|private
specifier|static
specifier|final
name|PomModuleDescriptorParser
name|INSTANCE
init|=
operator|new
name|PomModuleDescriptorParser
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|PomModuleDescriptorParser
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|PomModuleDescriptorParser
parameter_list|()
block|{
block|}
specifier|public
name|void
name|toIvyFile
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Resource
name|res
parameter_list|,
name|File
name|destFile
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
try|try
block|{
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Resource
name|res
parameter_list|)
block|{
return|return
name|res
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pom"
argument_list|)
operator|||
name|res
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"pom.xml"
argument_list|)
operator|||
name|res
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"project.xml"
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"pom parser"
return|;
block|}
specifier|public
name|Artifact
name|getMetadataArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Resource
name|res
parameter_list|)
block|{
return|return
name|DefaultArtifact
operator|.
name|newPomArtifact
argument_list|(
name|mrid
argument_list|,
operator|new
name|Date
argument_list|(
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
literal|"pom"
return|;
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|,
name|URL
name|descriptorURL
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|URLResource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|descriptorURL
argument_list|)
decl_stmt|;
return|return
name|parseDescriptor
argument_list|(
name|ivySettings
argument_list|,
name|descriptorURL
argument_list|,
name|resource
argument_list|,
name|validate
argument_list|)
return|;
block|}
specifier|public
name|ModuleDescriptor
name|parseDescriptor
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|,
name|URL
name|descriptorURL
parameter_list|,
name|Resource
name|res
parameter_list|,
name|boolean
name|validate
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|PomModuleDescriptorBuilder
name|mdBuilder
init|=
operator|new
name|PomModuleDescriptorBuilder
argument_list|(
name|this
argument_list|,
name|res
argument_list|,
name|ivySettings
argument_list|)
decl_stmt|;
try|try
block|{
name|PomReader
name|domReader
init|=
operator|new
name|PomReader
argument_list|(
name|descriptorURL
argument_list|,
name|res
argument_list|)
decl_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"parent.version"
argument_list|,
name|domReader
operator|.
name|getParentVersion
argument_list|()
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"parent.groupId"
argument_list|,
name|domReader
operator|.
name|getParentGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"project.parent.version"
argument_list|,
name|domReader
operator|.
name|getParentVersion
argument_list|()
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"project.parent.groupId"
argument_list|,
name|domReader
operator|.
name|getParentGroupId
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prop
range|:
name|domReader
operator|.
name|getPomProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|domReader
operator|.
name|setProperty
argument_list|(
name|prop
operator|.
name|getKey
argument_list|()
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|addProperty
argument_list|(
name|prop
operator|.
name|getKey
argument_list|()
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ModuleDescriptor
name|parentDescr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|domReader
operator|.
name|hasParent
argument_list|()
condition|)
block|{
comment|// Is there any other parent properties?
name|ModuleRevisionId
name|parentModRevID
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|domReader
operator|.
name|getParentGroupId
argument_list|()
argument_list|,
name|domReader
operator|.
name|getParentArtifactId
argument_list|()
argument_list|,
name|domReader
operator|.
name|getParentVersion
argument_list|()
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|parentModule
init|=
name|parseOtherPom
argument_list|(
name|ivySettings
argument_list|,
name|parentModRevID
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentModule
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Impossible to load parent for "
operator|+
name|res
operator|.
name|getName
argument_list|()
operator|+
literal|". Parent="
operator|+
name|parentModRevID
argument_list|)
throw|;
block|}
name|parentDescr
operator|=
name|parentModule
operator|.
name|getDescriptor
argument_list|()
expr_stmt|;
if|if
condition|(
name|parentDescr
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prop
range|:
name|extractPomProperties
argument_list|(
name|parentDescr
operator|.
name|getExtraInfos
argument_list|()
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|domReader
operator|.
name|setProperty
argument_list|(
name|prop
operator|.
name|getKey
argument_list|()
argument_list|,
name|prop
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|groupId
init|=
name|domReader
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|domReader
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|domReader
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|mdBuilder
operator|.
name|setModuleRevId
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|setHomePage
argument_list|(
name|domReader
operator|.
name|getHomePage
argument_list|()
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|setDescription
argument_list|(
name|domReader
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
comment|// if this module doesn't have an explicit license, use the parent's license (if any)
specifier|final
name|License
index|[]
name|licenses
init|=
name|domReader
operator|.
name|getLicenses
argument_list|()
decl_stmt|;
if|if
condition|(
name|licenses
operator|!=
literal|null
operator|&&
name|licenses
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|mdBuilder
operator|.
name|setLicenses
argument_list|(
name|licenses
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|parentDescr
operator|!=
literal|null
condition|)
block|{
name|mdBuilder
operator|.
name|setLicenses
argument_list|(
name|parentDescr
operator|.
name|getLicenses
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ModuleRevisionId
name|relocation
init|=
name|domReader
operator|.
name|getRelocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|relocation
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|groupId
operator|!=
literal|null
operator|&&
name|artifactId
operator|!=
literal|null
operator|&&
name|artifactId
operator|.
name|equals
argument_list|(
name|relocation
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|groupId
operator|.
name|equals
argument_list|(
name|relocation
operator|.
name|getOrganisation
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Relocation to an other version number not supported in ivy : "
operator|+
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|" relocated to "
operator|+
name|relocation
operator|+
literal|". Please update your dependency to directly use the right version."
argument_list|)
expr_stmt|;
name|Message
operator|.
name|warn
argument_list|(
literal|"Resolution will only pick dependencies of the relocated element."
operator|+
literal|"  Artifact and other metadata will be ignored."
argument_list|)
expr_stmt|;
name|ResolvedModuleRevision
name|relocatedModule
init|=
name|parseOtherPom
argument_list|(
name|ivySettings
argument_list|,
name|relocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|relocatedModule
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"impossible to load module "
operator|+
name|relocation
operator|+
literal|" to which "
operator|+
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|" has been relocated"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
for|for
control|(
name|DependencyDescriptor
name|dd
range|:
name|relocatedModule
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|info
argument_list|(
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
operator|+
literal|" is relocated to "
operator|+
name|relocation
operator|+
literal|". Please update your dependencies."
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"Relocated module will be considered as a dependency"
argument_list|)
expr_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
argument_list|,
name|relocation
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|/* Map all public dependencies */
for|for
control|(
name|Configuration
name|m2Conf
range|:
name|MAVEN2_CONFIGURATIONS
control|)
block|{
if|if
condition|(
name|PUBLIC
operator|.
name|equals
argument_list|(
name|m2Conf
operator|.
name|getVisibility
argument_list|()
argument_list|)
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|m2Conf
operator|.
name|getName
argument_list|()
argument_list|,
name|m2Conf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"project.groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"pom.groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"groupId"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"project.artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"pom.artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"artifactId"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"project.version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"pom.version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|domReader
operator|.
name|setProperty
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|parentDescr
operator|!=
literal|null
condition|)
block|{
name|mdBuilder
operator|.
name|addExtraInfos
argument_list|(
name|parentDescr
operator|.
name|getExtraInfos
argument_list|()
argument_list|)
expr_stmt|;
comment|// add dependency management info from parent
for|for
control|(
name|PomDependencyMgt
name|dep
range|:
name|getDependencyManagements
argument_list|(
name|parentDescr
argument_list|)
control|)
block|{
if|if
condition|(
name|dep
operator|instanceof
name|PomDependencyMgtElement
condition|)
block|{
name|dep
operator|=
name|domReader
operator|.
expr|new
name|PomDependencyMgtElement
argument_list|(
operator|(
name|PomDependencyMgtElement
operator|)
name|dep
argument_list|)
expr_stmt|;
block|}
name|mdBuilder
operator|.
name|addDependencyMgt
argument_list|(
name|dep
argument_list|)
expr_stmt|;
block|}
comment|// add plugins from parent
for|for
control|(
name|PomDependencyMgt
name|pomDependencyMgt
range|:
name|getPlugins
argument_list|(
name|parentDescr
argument_list|)
control|)
block|{
name|mdBuilder
operator|.
name|addPlugin
argument_list|(
name|pomDependencyMgt
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|PomDependencyMgt
name|dep
range|:
name|domReader
operator|.
name|getDependencyMgt
argument_list|()
control|)
block|{
name|addTo
argument_list|(
name|mdBuilder
argument_list|,
name|dep
argument_list|,
name|ivySettings
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PomDependencyData
name|dep
range|:
name|domReader
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|res
argument_list|,
name|dep
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PomPluginElement
name|plugin
range|:
name|domReader
operator|.
name|getPlugins
argument_list|()
control|)
block|{
name|mdBuilder
operator|.
name|addPlugin
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
comment|// consult active profiles:
for|for
control|(
name|PomProfileElement
name|profile
range|:
name|domReader
operator|.
name|getProfiles
argument_list|()
control|)
block|{
if|if
condition|(
name|profile
operator|.
name|isActive
argument_list|()
condition|)
block|{
for|for
control|(
name|PomDependencyMgt
name|dep
range|:
name|profile
operator|.
name|getDependencyMgt
argument_list|()
control|)
block|{
name|addTo
argument_list|(
name|mdBuilder
argument_list|,
name|dep
argument_list|,
name|ivySettings
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PomDependencyData
name|dep
range|:
name|profile
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|res
argument_list|,
name|dep
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PomPluginElement
name|plugin
range|:
name|profile
operator|.
name|getPlugins
argument_list|()
control|)
block|{
name|mdBuilder
operator|.
name|addPlugin
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|parentDescr
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DependencyDescriptor
name|descriptor
range|:
name|parentDescr
operator|.
name|getDependencies
argument_list|()
control|)
block|{
if|if
condition|(
name|descriptor
operator|instanceof
name|PomDependencyDescriptor
condition|)
block|{
name|PomDependencyData
name|parentDep
init|=
operator|(
operator|(
name|PomDependencyDescriptor
operator|)
name|descriptor
operator|)
operator|.
name|getPomDependencyData
argument_list|()
decl_stmt|;
name|PomDependencyData
name|dep
init|=
name|domReader
operator|.
expr|new
name|PomDependencyData
argument_list|(
name|parentDep
argument_list|)
decl_stmt|;
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|res
argument_list|,
name|dep
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mdBuilder
operator|.
name|addDependency
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|mdBuilder
operator|.
name|addMainArtifact
argument_list|(
name|artifactId
argument_list|,
name|domReader
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
name|addSourcesAndJavadocArtifactsIfPresent
argument_list|(
name|mdBuilder
argument_list|,
name|ivySettings
argument_list|)
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
name|newParserException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
return|;
block|}
specifier|private
name|void
name|addTo
parameter_list|(
name|PomModuleDescriptorBuilder
name|mdBuilder
parameter_list|,
name|PomDependencyMgt
name|dep
parameter_list|,
name|ParserSettings
name|ivySettings
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
if|if
condition|(
literal|"import"
operator|.
name|equals
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|ModuleRevisionId
name|importModRevID
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|importModule
init|=
name|parseOtherPom
argument_list|(
name|ivySettings
argument_list|,
name|importModRevID
argument_list|)
decl_stmt|;
if|if
condition|(
name|importModule
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Impossible to import module for "
operator|+
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|". Import="
operator|+
name|importModRevID
argument_list|)
throw|;
block|}
name|ModuleDescriptor
name|importDescr
init|=
name|importModule
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
comment|// add dependency management info from imported module
for|for
control|(
name|PomDependencyMgt
name|importedDepMgt
range|:
name|getDependencyManagements
argument_list|(
name|importDescr
argument_list|)
control|)
block|{
name|mdBuilder
operator|.
name|addDependencyMgt
argument_list|(
operator|new
name|DefaultPomDependencyMgt
argument_list|(
name|importedDepMgt
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|importedDepMgt
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|importedDepMgt
operator|.
name|getVersion
argument_list|()
argument_list|,
name|importedDepMgt
operator|.
name|getScope
argument_list|()
argument_list|,
name|importedDepMgt
operator|.
name|getExcludedModules
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|mdBuilder
operator|.
name|addDependencyMgt
argument_list|(
name|dep
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addSourcesAndJavadocArtifactsIfPresent
parameter_list|(
name|PomModuleDescriptorBuilder
name|mdBuilder
parameter_list|,
name|ParserSettings
name|ivySettings
parameter_list|)
block|{
if|if
condition|(
name|mdBuilder
operator|.
name|getMainArtifact
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// no main artifact in pom, we don't need to search for meta artifacts
return|return;
block|}
name|boolean
name|sourcesLookup
init|=
operator|!
literal|"false"
operator|.
name|equals
argument_list|(
name|ivySettings
operator|.
name|getVariable
argument_list|(
literal|"ivy.maven.lookup.sources"
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|javadocLookup
init|=
operator|!
literal|"false"
operator|.
name|equals
argument_list|(
name|ivySettings
operator|.
name|getVariable
argument_list|(
literal|"ivy.maven.lookup.javadoc"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sourcesLookup
operator|&&
operator|!
name|javadocLookup
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Sources and javadocs lookup disabled"
argument_list|)
expr_stmt|;
return|return;
block|}
name|ModuleDescriptor
name|md
init|=
name|mdBuilder
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|mrid
init|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|DependencyResolver
name|resolver
init|=
name|ivySettings
operator|.
name|getResolver
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no resolver found for "
operator|+
name|mrid
operator|+
literal|": no source or javadoc artifact lookup"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ArtifactOrigin
name|mainArtifact
init|=
name|resolver
operator|.
name|locate
argument_list|(
name|mdBuilder
operator|.
name|getMainArtifact
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|mainArtifact
argument_list|)
condition|)
block|{
name|String
name|mainArtifactLocation
init|=
name|mainArtifact
operator|.
name|getLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|sourcesLookup
condition|)
block|{
name|ArtifactOrigin
name|sourceArtifact
init|=
name|resolver
operator|.
name|locate
argument_list|(
name|mdBuilder
operator|.
name|getSourceArtifact
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|sourceArtifact
argument_list|)
operator|&&
operator|!
name|sourceArtifact
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|mainArtifactLocation
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"source artifact found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|addSourceArtifact
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// it seems that sometimes the 'src' classifier is used instead of 'sources'
comment|// Cfr. IVY-1138
name|ArtifactOrigin
name|srcArtifact
init|=
name|resolver
operator|.
name|locate
argument_list|(
name|mdBuilder
operator|.
name|getSrcArtifact
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|srcArtifact
argument_list|)
operator|&&
operator|!
name|srcArtifact
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|mainArtifactLocation
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"source artifact found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|addSrcArtifact
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no source artifact found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"sources lookup disabled"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|javadocLookup
condition|)
block|{
name|ArtifactOrigin
name|javadocArtifact
init|=
name|resolver
operator|.
name|locate
argument_list|(
name|mdBuilder
operator|.
name|getJavadocArtifact
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|javadocArtifact
argument_list|)
operator|&&
operator|!
name|javadocArtifact
operator|.
name|getLocation
argument_list|()
operator|.
name|equals
argument_list|(
name|mainArtifactLocation
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"javadoc artifact found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
name|mdBuilder
operator|.
name|addJavadocArtifact
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no javadoc artifact found for "
operator|+
name|mrid
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"javadocs lookup disabled"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|ResolvedModuleRevision
name|parseOtherPom
parameter_list|(
name|ParserSettings
name|ivySettings
parameter_list|,
name|ModuleRevisionId
name|parentModRevID
parameter_list|)
throws|throws
name|ParseException
block|{
name|DependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|parentModRevID
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ResolveData
name|data
init|=
name|getContext
argument_list|()
operator|.
name|getResolveData
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
name|ResolveEngine
name|engine
init|=
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|.
name|getResolveEngine
argument_list|()
decl_stmt|;
name|ResolveOptions
name|options
init|=
operator|new
name|ResolveOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|setDownload
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|data
operator|=
operator|new
name|ResolveData
argument_list|(
name|engine
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
name|DependencyResolver
name|resolver
init|=
name|ivySettings
operator|.
name|getResolver
argument_list|(
name|parentModRevID
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
comment|// TODO: Throw exception here?
return|return
literal|null
return|;
block|}
name|dd
operator|=
name|toSystem
argument_list|(
name|dd
argument_list|,
name|ivySettings
operator|.
name|getContextNamespace
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resolver
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
return|;
block|}
specifier|private
name|ParseException
name|newParserException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|ParseException
name|pe
init|=
operator|new
name|ParseException
argument_list|(
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
return|return
name|pe
return|;
block|}
block|}
end_class

end_unit

