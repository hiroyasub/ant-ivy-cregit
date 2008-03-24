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
name|LinkedHashMap
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
name|DefaultDependencyArtifactDescriptor
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
name|DefaultExcludeRule
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
name|DefaultModuleDescriptor
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
name|descriptor
operator|.
name|OverrideDependencyDescriptorMediator
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
operator|.
name|Visibility
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
name|plugins
operator|.
name|matcher
operator|.
name|ExactPatternMatcher
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
name|matcher
operator|.
name|PatternMatcher
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
name|Message
import|;
end_import

begin_comment
comment|/**  * Build a module descriptor.  This class handle the complexity of the structure of an ivy   * ModuleDescriptor and isolate the PomModuleDescriptorParser from it.   */
end_comment

begin_class
specifier|public
class|class
name|PomModuleDescriptorBuilder
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEPENDENCY_MANAGEMENT_KEY_PARTS_COUNT
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Configuration
index|[]
name|MAVEN2_CONFIGURATIONS
init|=
operator|new
name|Configuration
index|[]
block|{
operator|new
name|Configuration
argument_list|(
literal|"default"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"runtime dependencies and master artifact can be used with this conf"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"runtime"
block|,
literal|"master"
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"master"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"contains only the artifact published by this module itself, "
operator|+
literal|"with no transitive dependencies"
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"compile"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this is the default scope, used if none is specified. "
operator|+
literal|"Compile dependencies are available in all classpaths."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"provided"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this is much like compile, but indicates you expect the JDK or a container "
operator|+
literal|"to provide it. "
operator|+
literal|"It is only available on the compilation classpath, and is not transitive."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"runtime"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this scope indicates that the dependency is not required for compilation, "
operator|+
literal|"but is for execution. It is in the runtime and test classpaths, "
operator|+
literal|"but not the compile classpath."
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"compile"
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"test"
argument_list|,
name|Visibility
operator|.
name|PRIVATE
argument_list|,
literal|"this scope indicates that the dependency is not required for normal use of "
operator|+
literal|"the application, and is only available for the test compilation and "
operator|+
literal|"execution phases."
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"runtime"
block|}
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"system"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"this scope is similar to provided except that you have to provide the JAR "
operator|+
literal|"which contains it explicitly. The artifact is always available and is not "
operator|+
literal|"looked up in a repository."
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|,
operator|new
name|Configuration
argument_list|(
literal|"optional"
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|,
literal|"contains all optional dependencies"
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
block|}
decl_stmt|;
specifier|static
specifier|final
name|Map
name|MAVEN2_CONF_MAPPING
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEPENDENCY_MANAGEMENT
init|=
literal|"m:dependency.management"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTIES
init|=
literal|"m:properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXTRA_INFO_DELIMITER
init|=
literal|"__"
decl_stmt|;
specifier|static
interface|interface
name|ConfMapper
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
function_decl|;
block|}
static|static
block|{
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"compile"
argument_list|,
operator|new
name|ConfMapper
argument_list|()
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
block|{
if|if
condition|(
name|isOptional
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
comment|//dd.addDependencyConfiguration("optional", "provided(*)");
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"compile"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
comment|//dd.addDependencyConfiguration("compile", "provided(*)");
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"compile"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime(*)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"provided"
argument_list|,
operator|new
name|ConfMapper
argument_list|()
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
block|{
if|if
condition|(
name|isOptional
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"provided(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"runtime(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"provided"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"provided"
argument_list|,
literal|"provided(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"provided"
argument_list|,
literal|"runtime(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"provided"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"runtime"
argument_list|,
operator|new
name|ConfMapper
argument_list|()
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
block|{
if|if
condition|(
name|isOptional
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"provided(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"optional"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"runtime"
argument_list|,
literal|"compile(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"runtime"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"test"
argument_list|,
operator|new
name|ConfMapper
argument_list|()
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
block|{
comment|//optional doesn't make sense in the test scope
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"test"
argument_list|,
literal|"runtime(*)"
argument_list|)
expr_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"test"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MAVEN2_CONF_MAPPING
operator|.
name|put
argument_list|(
literal|"system"
argument_list|,
operator|new
name|ConfMapper
argument_list|()
block|{
specifier|public
name|void
name|addMappingConfs
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|boolean
name|isOptional
parameter_list|)
block|{
comment|//optional doesn't make sense in the system scope
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"system"
argument_list|,
literal|"master(*)"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|DefaultModuleDescriptor
name|ivyModuleDescriptor
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid
decl_stmt|;
specifier|public
name|PomModuleDescriptorBuilder
parameter_list|(
name|ModuleDescriptorParser
name|parser
parameter_list|,
name|Resource
name|res
parameter_list|)
block|{
name|ivyModuleDescriptor
operator|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|parser
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|ivyModuleDescriptor
operator|.
name|setResolvedPublicationDate
argument_list|(
operator|new
name|Date
argument_list|(
name|res
operator|.
name|getLastModified
argument_list|()
argument_list|)
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
name|MAVEN2_CONFIGURATIONS
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ivyModuleDescriptor
operator|.
name|addConfiguration
argument_list|(
name|MAVEN2_CONFIGURATIONS
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|ivyModuleDescriptor
operator|.
name|setMappingOverride
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ivyModuleDescriptor
operator|.
name|addExtraAttributeNamespace
argument_list|(
literal|"m"
argument_list|,
name|Ivy
operator|.
name|getIvyHomeURL
argument_list|()
operator|+
literal|"maven"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|ivyModuleDescriptor
return|;
block|}
specifier|public
name|void
name|setModuleRevId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|mrid
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|ivyModuleDescriptor
operator|.
name|setModuleRevisionId
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHomePage
parameter_list|(
name|String
name|homePage
parameter_list|)
block|{
name|ivyModuleDescriptor
operator|.
name|setHomePage
argument_list|(
name|homePage
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|ivyModuleDescriptor
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|packaging
parameter_list|)
block|{
name|ivyModuleDescriptor
operator|.
name|addArtifact
argument_list|(
literal|"master"
argument_list|,
operator|new
name|DefaultArtifact
argument_list|(
name|mrid
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|artifactId
argument_list|,
name|packaging
argument_list|,
name|packaging
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|Resource
name|res
parameter_list|,
name|PomDependencyData
name|dep
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
operator|!
name|MAVEN2_CONF_MAPPING
operator|.
name|containsKey
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
literal|"Unknown scope "
operator|+
name|dep
operator|.
name|getScope
argument_list|()
operator|+
literal|" for dependency "
operator|+
name|ModuleId
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
name|getArtifaceId
argument_list|()
argument_list|)
operator|+
literal|" in "
operator|+
name|res
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ParseException
argument_list|(
name|msg
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|String
name|version
init|=
name|dep
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|version
operator|=
operator|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|?
name|getDefaultVersion
argument_list|(
name|dep
argument_list|)
else|:
name|version
expr_stmt|;
name|ModuleRevisionId
name|moduleRevId
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
name|getArtifaceId
argument_list|()
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ivyModuleDescriptor
argument_list|,
name|moduleRevId
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ConfMapper
name|mapping
init|=
operator|(
name|ConfMapper
operator|)
name|MAVEN2_CONF_MAPPING
operator|.
name|get
argument_list|(
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
decl_stmt|;
name|mapping
operator|.
name|addMappingConfs
argument_list|(
name|dd
argument_list|,
name|dep
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|extraAtt
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|dep
operator|.
name|getClassifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// we deal with classifiers by setting an extra attribute and forcing the
comment|// dependency to assume such an artifact is published
name|extraAtt
operator|.
name|put
argument_list|(
literal|"m:classifier"
argument_list|,
name|dep
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultDependencyArtifactDescriptor
name|depArtifact
init|=
operator|new
name|DefaultDependencyArtifactDescriptor
argument_list|(
name|dd
operator|.
name|getDependencyId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|null
argument_list|,
name|extraAtt
argument_list|)
decl_stmt|;
comment|// here we have to assume a type and ext for the artifact, so this is a limitation
comment|// compared to how m2 behave with classifiers
name|String
name|optionalizedScope
init|=
name|dep
operator|.
name|isOptional
argument_list|()
condition|?
literal|"optional"
else|:
name|dep
operator|.
name|getScope
argument_list|()
decl_stmt|;
name|dd
operator|.
name|addDependencyArtifact
argument_list|(
name|optionalizedScope
argument_list|,
name|depArtifact
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|itExcl
init|=
name|dep
operator|.
name|getExcludedModules
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|itExcl
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ModuleId
name|excludedModule
init|=
operator|(
name|ModuleId
operator|)
name|itExcl
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|dd
operator|.
name|getModuleConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|confs
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|dd
operator|.
name|addExcludeRule
argument_list|(
name|confs
index|[
name|k
index|]
argument_list|,
operator|new
name|DefaultExcludeRule
argument_list|(
operator|new
name|ArtifactId
argument_list|(
name|excludedModule
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|,
name|PatternMatcher
operator|.
name|ANY_EXPRESSION
argument_list|)
argument_list|,
name|ExactPatternMatcher
operator|.
name|INSTANCE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ivyModuleDescriptor
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|DependencyDescriptor
name|descriptor
parameter_list|)
block|{
name|ivyModuleDescriptor
operator|.
name|addDependency
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDependencyMgt
parameter_list|(
name|PomDependencyMgt
name|dep
parameter_list|)
block|{
name|String
name|key
init|=
name|getDependencyMgtExtraInfoKey
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifaceId
argument_list|()
argument_list|)
decl_stmt|;
name|ivyModuleDescriptor
operator|.
name|addExtraInfo
argument_list|(
name|key
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// dependency management info is also used for version mediation of transitive dependencies
name|ivyModuleDescriptor
operator|.
name|addDependencyDescriptorMediator
argument_list|(
name|ModuleId
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
name|getArtifaceId
argument_list|()
argument_list|)
argument_list|,
name|ExactPatternMatcher
operator|.
name|INSTANCE
argument_list|,
operator|new
name|OverrideDependencyDescriptorMediator
argument_list|(
literal|null
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getDefaultVersion
parameter_list|(
name|PomDependencyData
name|dep
parameter_list|)
block|{
name|String
name|key
init|=
name|getDependencyMgtExtraInfoKey
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifaceId
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|String
operator|)
name|ivyModuleDescriptor
operator|.
name|getExtraInfo
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|getDependencyMgtExtraInfoKey
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifaceId
parameter_list|)
block|{
return|return
name|DEPENDENCY_MANAGEMENT
operator|+
name|EXTRA_INFO_DELIMITER
operator|+
name|groupId
operator|+
name|EXTRA_INFO_DELIMITER
operator|+
name|artifaceId
return|;
block|}
specifier|private
specifier|static
name|String
name|getPropertyExtraInfoKey
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|PROPERTIES
operator|+
name|EXTRA_INFO_DELIMITER
operator|+
name|propertyName
return|;
block|}
specifier|public
specifier|static
name|Map
comment|/*<ModuleId, String version>*/
name|getDependencyManagementMap
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|Map
name|ret
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|md
operator|.
name|getExtraInfo
argument_list|()
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
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
operator|(
name|key
operator|)
operator|.
name|startsWith
argument_list|(
name|DEPENDENCY_MANAGEMENT
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|key
operator|.
name|split
argument_list|(
name|EXTRA_INFO_DELIMITER
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
name|DEPENDENCY_MANAGEMENT_KEY_PARTS_COUNT
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"what seem to be a dependency management extra info "
operator|+
literal|"doesn't match expected pattern: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ret
operator|.
name|put
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|,
name|parts
index|[
literal|2
index|]
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|addExtraInfos
parameter_list|(
name|Map
name|extraAttributes
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|extraAttributes
operator|.
name|entrySet
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
name|Map
operator|.
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
name|key
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|addExtraInfo
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addExtraInfo
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ivyModuleDescriptor
operator|.
name|getExtraInfo
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|ivyModuleDescriptor
operator|.
name|addExtraInfo
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Map
name|extractPomProperties
parameter_list|(
name|Map
name|extraInfo
parameter_list|)
block|{
name|Map
name|r
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|extraInfo
operator|.
name|entrySet
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
name|Map
operator|.
name|Entry
name|extraInfoEntry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|(
name|String
operator|)
name|extraInfoEntry
operator|.
name|getKey
argument_list|()
operator|)
operator|.
name|startsWith
argument_list|(
name|PROPERTIES
argument_list|)
condition|)
block|{
name|String
name|prop
init|=
operator|(
operator|(
name|String
operator|)
name|extraInfoEntry
operator|.
name|getKey
argument_list|()
operator|)
operator|.
name|substring
argument_list|(
name|PROPERTIES
operator|.
name|length
argument_list|()
operator|+
name|EXTRA_INFO_DELIMITER
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|put
argument_list|(
name|prop
argument_list|,
name|extraInfoEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
specifier|public
name|void
name|addProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|addExtraInfo
argument_list|(
name|getPropertyExtraInfoKey
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

