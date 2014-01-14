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
name|core
operator|.
name|module
operator|.
name|descriptor
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
name|Date
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
name|id
operator|.
name|ArtifactId
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRules
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
name|conflict
operator|.
name|ConflictManager
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
name|latest
operator|.
name|ArtifactInfo
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
name|version
operator|.
name|VersionMatcher
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
name|ExtendableItem
import|;
end_import

begin_comment
comment|/**  * Descriptor of a module. This is the Java representation of an ivy.xml  */
end_comment

begin_interface
specifier|public
interface|interface
name|ModuleDescriptor
extends|extends
name|ExtendableItem
extends|,
name|ArtifactInfo
extends|,
name|DependencyDescriptorMediator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONFIGURATION
init|=
literal|"default"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CALLER_ALL_CONFIGURATION
init|=
literal|"all"
decl_stmt|;
comment|/**      * Returns true if this descriptor is a default one, i.e. one generated for a module not      * actually having one.      *       * @return      */
name|boolean
name|isDefault
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * The module revision id returned here is the resolved one, i.e. it is never a latest one. If      * the revision has not been resolved, a null revision should be returned by getRevision() of      * the returned ModuleRevisionId. This revision must be the same as the module descriptor      * resolved revision id unless no module descriptor is defined      *       * @return      */
name|ModuleRevisionId
name|getResolvedModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * This method update the resolved module revision id      *       * @param revId      */
name|void
name|setResolvedModuleRevisionId
parameter_list|(
name|ModuleRevisionId
name|revId
parameter_list|)
function_decl|;
comment|/**      * Get the list of parent descriptors imported via an&lt;extends&gt; element. Only directly      * imported descriptors are included; the parent's parents are not included.      */
name|ExtendsDescriptor
index|[]
name|getInheritedDescriptors
parameter_list|()
function_decl|;
comment|/**      * This method update the resolved publication date      *       * @param publicationDate      */
name|void
name|setResolvedPublicationDate
parameter_list|(
name|Date
name|publicationDate
parameter_list|)
function_decl|;
name|String
name|getStatus
parameter_list|()
function_decl|;
comment|/**      * May be<code>null</code> if unknown in the descriptor itself.      *       * @return The publication date or<code>null</code> when not knwon.      */
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
comment|/**      * The publication date of the module revision should be the date at which it has been      * published, i.e. in general the date of any of its published artifacts, since all published      * artifact of a module should follow the same publishing cycle.      */
name|Date
name|getResolvedPublicationDate
parameter_list|()
function_decl|;
comment|/**      * Returns all the configurations declared by this module as an array. This array is never empty      * (a 'default' conf is assumed when none is declared in the ivy file)      *       * @return all the configurations declared by this module as an array.      */
name|Configuration
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
name|String
index|[]
name|getConfigurationsNames
parameter_list|()
function_decl|;
name|String
index|[]
name|getPublicConfigurationsNames
parameter_list|()
function_decl|;
name|Artifact
index|[]
name|getArtifacts
parameter_list|(
name|String
name|conf
parameter_list|)
function_decl|;
comment|/**      * Returns all artifacts of this module, excluding the artifact corresponding to the module      * descriptor.      *       * @return all published artifacts of this module      * @see #getMetadataArtifact()      */
name|Artifact
index|[]
name|getAllArtifacts
parameter_list|()
function_decl|;
comment|/**      * @retun The dependencies of the module. If there is no dependencies return an empty array (non      *        null)      */
name|DependencyDescriptor
index|[]
name|getDependencies
parameter_list|()
function_decl|;
comment|/**      * Returns true if the module described by this descriptor dependes directly upon the given      * module descriptor      *       * @param md      * @return      */
name|boolean
name|dependsOn
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
function_decl|;
comment|/**      * @param confName      * @return      */
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|confName
parameter_list|)
function_decl|;
comment|/**      * Returns the conflict manager to use for the given ModuleId, or<code>null</code> if no      * specific conflict manager is associated with the given module id in this module descriptor.      *       * @param id      * @return      */
name|ConflictManager
name|getConflictManager
parameter_list|(
name|ModuleId
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the licenses of the module described by this descriptor      *       * @return      */
name|License
index|[]
name|getLicenses
parameter_list|()
function_decl|;
name|String
name|getHomePage
parameter_list|()
function_decl|;
name|String
name|getDescription
parameter_list|()
function_decl|;
name|long
name|getLastModified
parameter_list|()
function_decl|;
comment|/**      * Writes this module descriptor as an ivy file. If this descriptor was obtained through the      * parsing of an ivy file, it should keep the layout of the file the most possible similar to      * the original one.      *       * @param ivyFile      *            the destination ivy file      */
name|void
name|toIvyFile
parameter_list|(
name|File
name|ivyFile
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
function_decl|;
comment|/**      * The ModuleDescriptorParser used to parse this module descriptor, null is no parser was used.      *       * @return      */
name|ModuleDescriptorParser
name|getParser
parameter_list|()
function_decl|;
comment|/**      * The resource being the source of this module descriptor, null if no resource corresponds to      * this module descriptor      *       * @return      */
name|Resource
name|getResource
parameter_list|()
function_decl|;
comment|/**      * Returns the Artifact representing this module descriptor itself.      *<p>      * Even though the module descriptor is never described as a published artifact of a module in      * the module descriptor itself, it is often useful to consider it as any other artifact of the      * module. This method allows to access to the Artifact object representing this module      * descriptor for this purpose.      *</p>      *       * @return the Artifact representing this module descriptor itself.      */
name|Artifact
name|getMetadataArtifact
parameter_list|()
function_decl|;
comment|/**      * Returns true if this descriptor contains any exclusion rule      *       * @return true if this descriptor contains any exclusion rule      */
name|boolean
name|canExclude
parameter_list|()
function_decl|;
comment|/**      * Returns true if an exclude rule of this module attached to any of the given configurations      * matches the given artifact id, and thus exclude it      *       * @param moduleConfs      * @param artifactId      * @return      */
name|boolean
name|doesExclude
parameter_list|(
name|String
index|[]
name|moduleConfs
parameter_list|,
name|ArtifactId
name|artifactId
parameter_list|)
function_decl|;
comment|/**      * Returns an array of all the exclude rules this module descriptor currently holds. Module      * Descriptor exclude rules are used to exclude (usually transitive) dependencies for the whole      * module.      *       * @return an array of {@link ExcludeRule} this module descriptor holds      */
specifier|public
name|ExcludeRule
index|[]
name|getAllExcludeRules
parameter_list|()
function_decl|;
comment|/**      * Returns all the dependency descriptor mediators used by this {@link ModuleDescriptor}, as an      * instance of {@link ModuleRules}.      *<p>      * All rules in the {@link ModuleRules} object returned are {@link DependencyDescriptorMediator}      * .      *</p>      *       * @return all the dependency descriptor mediators used by this {@link ModuleDescriptor}.      */
specifier|public
name|ModuleRules
comment|/*<DependencyDescriptorMediator> */
name|getAllDependencyDescriptorMediators
parameter_list|()
function_decl|;
comment|/**      * Returns the list of xml namespaces used by extra attributes, as Map from prefix to namespace      * URIs.      *<p>      * The returned list is never<code>null</code>, it is empty when no extra attribute is used or      * if extra attributes are used without xml namespaces      *</p>      *       * @return the list of xml namespaces used by extra attributes, as Map from prefix to namespace      *         URIs.      */
name|Map
comment|/*<String,String> */
name|getExtraAttributesNamespaces
parameter_list|()
function_decl|;
comment|/**      * Returns the custom info provided in the info tag. All the tags except the description are      * given. The key is the name of the tag, the value is its content.      *       * @return      */
annotation|@
name|Deprecated
name|Map
comment|/*<String,String> */
name|getExtraInfo
parameter_list|()
function_decl|;
comment|/**      * Returns a list of extras infos (tag name, attributes and content). All the tags except the      * description are given.      *       * @return      */
name|List
argument_list|<
name|ExtraInfoHolder
argument_list|>
name|getExtraInfos
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

