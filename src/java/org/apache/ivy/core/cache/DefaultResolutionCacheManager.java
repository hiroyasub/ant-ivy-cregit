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
name|cache
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
name|FileInputStream
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
name|FilenameFilter
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
name|net
operator|.
name|MalformedURLException
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|RelativeUrlResolver
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
name|ExtendsDescriptor
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
name|status
operator|.
name|StatusManager
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
name|settings
operator|.
name|IvySettings
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
name|IvySettingsAware
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
name|namespace
operator|.
name|Namespace
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
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultResolutionCacheManager
implements|implements
name|ResolutionCacheManager
implements|,
name|IvySettingsAware
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CACHE_RESOLVED_IVY_PATTERN
init|=
literal|"resolved-[organisation]-[module]-[revision].xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CACHE_RESOLVED_IVY_PROPERTIES_PATTERN
init|=
literal|"resolved-[organisation]-[module]-[revision].properties"
decl_stmt|;
specifier|private
name|String
name|resolvedIvyPattern
init|=
name|DEFAULT_CACHE_RESOLVED_IVY_PATTERN
decl_stmt|;
specifier|private
name|String
name|resolvedIvyPropertiesPattern
init|=
name|DEFAULT_CACHE_RESOLVED_IVY_PROPERTIES_PATTERN
decl_stmt|;
specifier|private
name|File
name|basedir
decl_stmt|;
specifier|private
name|String
name|name
init|=
literal|"resolution-cache"
decl_stmt|;
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|public
name|DefaultResolutionCacheManager
parameter_list|()
block|{
block|}
specifier|public
name|DefaultResolutionCacheManager
parameter_list|(
name|File
name|basedir
parameter_list|)
block|{
name|setBasedir
argument_list|(
name|basedir
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
specifier|public
name|File
name|getResolutionCacheRoot
parameter_list|()
block|{
return|return
name|basedir
return|;
block|}
specifier|public
name|File
name|getBasedir
parameter_list|()
block|{
return|return
name|basedir
return|;
block|}
specifier|public
name|void
name|setBasedir
parameter_list|(
name|File
name|basedir
parameter_list|)
block|{
name|this
operator|.
name|basedir
operator|=
name|basedir
expr_stmt|;
block|}
specifier|public
name|String
name|getResolvedIvyPattern
parameter_list|()
block|{
return|return
name|resolvedIvyPattern
return|;
block|}
specifier|public
name|void
name|setResolvedIvyPattern
parameter_list|(
name|String
name|cacheResolvedIvyPattern
parameter_list|)
block|{
name|this
operator|.
name|resolvedIvyPattern
operator|=
name|cacheResolvedIvyPattern
expr_stmt|;
block|}
specifier|public
name|String
name|getResolvedIvyPropertiesPattern
parameter_list|()
block|{
return|return
name|resolvedIvyPropertiesPattern
return|;
block|}
specifier|public
name|void
name|setResolvedIvyPropertiesPattern
parameter_list|(
name|String
name|cacheResolvedIvyPropertiesPattern
parameter_list|)
block|{
name|this
operator|.
name|resolvedIvyPropertiesPattern
operator|=
name|cacheResolvedIvyPropertiesPattern
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|File
name|getResolvedIvyFileInCache
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|String
name|file
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|getResolvedIvyPattern
argument_list|()
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|getResolutionCacheRoot
argument_list|()
argument_list|,
name|file
argument_list|)
return|;
block|}
specifier|public
name|File
name|getResolvedIvyPropertiesInCache
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|String
name|file
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|getResolvedIvyPropertiesPattern
argument_list|()
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|getResolutionCacheRoot
argument_list|()
argument_list|,
name|file
argument_list|)
return|;
block|}
specifier|public
name|File
name|getConfigurationResolveReportInCache
parameter_list|(
name|String
name|resolveId
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|getResolutionCacheRoot
argument_list|()
argument_list|,
name|resolveId
operator|+
literal|"-"
operator|+
name|conf
operator|+
literal|".xml"
argument_list|)
return|;
block|}
specifier|public
name|File
index|[]
name|getConfigurationResolveReportsInCache
parameter_list|(
specifier|final
name|String
name|resolveId
parameter_list|)
block|{
specifier|final
name|String
name|prefix
init|=
name|resolveId
operator|+
literal|"-"
decl_stmt|;
specifier|final
name|String
name|suffix
init|=
literal|".xml"
decl_stmt|;
return|return
name|getResolutionCacheRoot
argument_list|()
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getResolvedModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|File
name|ivyFile
init|=
name|getResolvedIvyFileInCache
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ivyFile
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Ivy file not found in cache for "
operator|+
name|mrid
operator|+
literal|"!"
argument_list|)
throw|;
block|}
name|Properties
name|paths
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|File
name|parentsFile
init|=
name|getResolvedIvyPropertiesInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
operator|+
literal|"-parents"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentsFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileInputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|parentsFile
argument_list|)
decl_stmt|;
name|paths
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|ParserSettings
name|pSettings
init|=
operator|new
name|CacheParserSettings
argument_list|(
name|settings
argument_list|,
name|paths
argument_list|)
decl_stmt|;
name|URL
name|ivyFileURL
init|=
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
return|return
name|getModuleDescriptorParser
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|pSettings
argument_list|,
name|ivyFileURL
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|ModuleDescriptorParser
name|getModuleDescriptorParser
parameter_list|()
block|{
return|return
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
return|;
block|}
specifier|public
name|void
name|saveResolvedModuleDescriptor
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|ModuleRevisionId
name|mrevId
init|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
decl_stmt|;
name|File
name|ivyFileInCache
init|=
name|getResolvedIvyFileInCache
argument_list|(
name|mrevId
argument_list|)
decl_stmt|;
name|md
operator|.
name|toIvyFile
argument_list|(
name|ivyFileInCache
argument_list|)
expr_stmt|;
name|Properties
name|paths
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|saveLocalParents
argument_list|(
name|mrevId
argument_list|,
name|md
argument_list|,
name|ivyFileInCache
argument_list|,
name|paths
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|paths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|File
name|parentsFile
init|=
name|getResolvedIvyPropertiesInCache
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrevId
argument_list|,
name|mrevId
operator|.
name|getRevision
argument_list|()
operator|+
literal|"-parents"
argument_list|)
argument_list|)
decl_stmt|;
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|parentsFile
argument_list|)
decl_stmt|;
name|paths
operator|.
name|store
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|saveLocalParents
parameter_list|(
name|ModuleRevisionId
name|baseMrevId
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|,
name|File
name|mdFile
parameter_list|,
name|Properties
name|paths
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|ExtendsDescriptor
index|[]
name|parents
init|=
name|md
operator|.
name|getInheritedDescriptors
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
name|parents
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|parents
index|[
name|i
index|]
operator|.
name|isLocal
argument_list|()
condition|)
block|{
comment|// we store only local parents in the cache!
continue|continue;
block|}
name|ModuleDescriptor
name|parent
init|=
name|parents
index|[
name|i
index|]
operator|.
name|getParentMd
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|pRevId
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|baseMrevId
argument_list|,
name|baseMrevId
operator|.
name|getRevision
argument_list|()
operator|+
literal|"-parent."
operator|+
name|paths
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|parentFile
init|=
name|getResolvedIvyFileInCache
argument_list|(
name|pRevId
argument_list|)
decl_stmt|;
name|parent
operator|.
name|toIvyFile
argument_list|(
name|parentFile
argument_list|)
expr_stmt|;
name|paths
operator|.
name|setProperty
argument_list|(
name|mdFile
operator|.
name|getName
argument_list|()
operator|+
literal|"|"
operator|+
name|parents
index|[
name|i
index|]
operator|.
name|getLocation
argument_list|()
argument_list|,
name|parentFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|saveLocalParents
argument_list|(
name|baseMrevId
argument_list|,
name|parent
argument_list|,
name|parentFile
argument_list|,
name|paths
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|clean
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|getBasedir
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|CacheParserSettings
implements|implements
name|ParserSettings
block|{
specifier|private
name|ParserSettings
name|delegate
decl_stmt|;
specifier|private
name|Map
name|parentPaths
decl_stmt|;
specifier|public
name|CacheParserSettings
parameter_list|(
name|ParserSettings
name|delegate
parameter_list|,
name|Map
name|parentPaths
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|parentPaths
operator|=
name|parentPaths
expr_stmt|;
block|}
specifier|public
name|String
name|substitute
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|substitute
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|Map
name|substitute
parameter_list|(
name|Map
name|strings
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|substitute
argument_list|(
name|strings
argument_list|)
return|;
block|}
specifier|public
name|ResolutionCacheManager
name|getResolutionCacheManager
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getResolutionCacheManager
argument_list|()
return|;
block|}
specifier|public
name|ConflictManager
name|getConflictManager
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getConflictManager
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|PatternMatcher
name|getMatcher
parameter_list|(
name|String
name|matcherName
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getMatcher
argument_list|(
name|matcherName
argument_list|)
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getNamespace
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|StatusManager
name|getStatusManager
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getStatusManager
argument_list|()
return|;
block|}
specifier|public
name|RelativeUrlResolver
name|getRelativeUrlResolver
parameter_list|()
block|{
return|return
operator|new
name|MapURLResolver
argument_list|(
name|parentPaths
argument_list|,
name|delegate
operator|.
name|getRelativeUrlResolver
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|(
name|ModuleRevisionId
name|mRevId
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getResolver
argument_list|(
name|mRevId
argument_list|)
return|;
block|}
specifier|public
name|File
name|resolveFile
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|resolveFile
argument_list|(
name|filename
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDefaultBranch
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getDefaultBranch
argument_list|(
name|moduleId
argument_list|)
return|;
block|}
specifier|public
name|Namespace
name|getContextNamespace
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getContextNamespace
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|MapURLResolver
extends|extends
name|RelativeUrlResolver
block|{
specifier|private
name|Map
name|paths
decl_stmt|;
specifier|private
name|RelativeUrlResolver
name|delegate
decl_stmt|;
specifier|private
name|MapURLResolver
parameter_list|(
name|Map
name|paths
parameter_list|,
name|RelativeUrlResolver
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|paths
operator|=
name|paths
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|(
name|URL
name|context
parameter_list|,
name|String
name|url
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
name|path
init|=
name|context
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|String
name|file
init|=
name|path
operator|.
name|substring
argument_list|(
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|paths
operator|.
name|containsKey
argument_list|(
name|file
operator|+
literal|"|"
operator|+
name|url
argument_list|)
condition|)
block|{
name|File
name|result
init|=
operator|new
name|File
argument_list|(
name|paths
operator|.
name|get
argument_list|(
name|file
operator|+
literal|"|"
operator|+
name|url
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|result
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
return|;
block|}
block|}
return|return
name|delegate
operator|.
name|getURL
argument_list|(
name|context
argument_list|,
name|url
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

