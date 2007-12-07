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
name|Collection
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
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|CacheManager
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
name|event
operator|.
name|EventManager
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
name|core
operator|.
name|sort
operator|.
name|SortEngine
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
name|plugins
operator|.
name|resolver
operator|.
name|FileSystemResolver
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
name|TestHelper
block|{
specifier|public
specifier|static
name|File
name|getArchiveFileInCache
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|getArchiveFileInCache
argument_list|(
name|ivy
operator|.
name|getCacheManager
argument_list|(
name|cache
argument_list|)
argument_list|,
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|File
name|getArchiveFileInCache
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|,
name|String
name|organisation
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|cacheManager
operator|.
name|getArchiveFileInCache
argument_list|(
operator|new
name|DefaultArtifact
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|module
argument_list|,
name|revision
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Assertion utility methods to test if a collection of {@link ModuleRevisionId} matches a given      * expected set of mrids.      *<p>      * Expected mrids is given as a String of comma separated string representations of      * {@link ModuleRevisionId}.      *       * @param expectedMrids      *            the expected set of mrids      * @param mrids      *            the3 mrids to test      */
specifier|public
specifier|static
name|void
name|assertModuleRevisionIds
parameter_list|(
name|String
name|expectedMrids
parameter_list|,
name|Collection
comment|/*<ModuleRevisionId> */
name|mrids
parameter_list|)
block|{
name|Collection
name|expected
init|=
name|parseMrids
argument_list|(
name|expectedMrids
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|mrids
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a Set of {@link ModuleRevisionId} corresponding to the given comma separated list of      * their text representation.      *       * @param mrids      *            the text representation of the {@link ModuleRevisionId}      * @return a collection of {@link ModuleRevisionId}      */
specifier|public
specifier|static
name|Collection
name|parseMrids
parameter_list|(
name|String
name|mrids
parameter_list|)
block|{
name|String
index|[]
name|m
init|=
name|mrids
operator|.
name|split
argument_list|(
literal|",?\\s+"
argument_list|)
decl_stmt|;
name|Collection
name|c
init|=
operator|new
name|LinkedHashSet
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
name|m
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|c
operator|.
name|add
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|m
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
comment|/**      * Returns an array of {@link ModuleRevisionId} corresponding to the given comma separated list of      * their text representation.      *       * @param mrids      *            the text representation of the {@link ModuleRevisionId}      * @return an array of {@link ModuleRevisionId}      */
specifier|public
specifier|static
name|ModuleRevisionId
index|[]
name|parseMridsToArray
parameter_list|(
name|String
name|mrids
parameter_list|)
block|{
name|Collection
name|parsedMrids
init|=
name|parseMrids
argument_list|(
name|mrids
argument_list|)
decl_stmt|;
return|return
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|parsedMrids
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|parsedMrids
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Parses a string represenation of a module descriptor in micro ivy format.      *<p>      * Examples:      *<pre>      * #A;1      *</pre>      *<hr/>      *<pre>      * #A;2-> #B;[1.0,1.5]      *</pre>      *<hr/>      *<pre>      * #A;3-> { #B;[1.0,1.5] #C;[2.0,2.5] }      *</pre>      *</p>      *       * @param microIvy the micro ivy description of the module descriptor      * @return the parsed module descriptor      */
specifier|public
specifier|static
name|ModuleDescriptor
name|parseMicroIvyDescriptor
parameter_list|(
name|String
name|microIvy
parameter_list|)
block|{
name|Pattern
name|mridPattern
init|=
name|ModuleRevisionId
operator|.
name|NON_CAPTURING_PATTERN
decl_stmt|;
name|Matcher
name|m
init|=
name|mridPattern
operator|.
name|matcher
argument_list|(
name|microIvy
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|DefaultModuleDescriptor
operator|.
name|newBasicInstance
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|microIvy
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
return|;
block|}
name|Pattern
name|oneDependencyPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"("
operator|+
name|mridPattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")\\s*->\\s*("
operator|+
name|mridPattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")"
argument_list|)
decl_stmt|;
name|m
operator|=
name|oneDependencyPattern
operator|.
name|matcher
argument_list|(
name|microIvy
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|DefaultModuleDescriptor
name|md
init|=
name|DefaultModuleDescriptor
operator|.
name|newBasicInstance
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|md
return|;
block|}
name|String
name|p
init|=
literal|"("
operator|+
name|mridPattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")\\s*->\\s*\\{\\s*((?:"
operator|+
name|mridPattern
operator|.
name|pattern
argument_list|()
operator|+
literal|",?\\s+)*"
operator|+
name|mridPattern
operator|.
name|pattern
argument_list|()
operator|+
literal|")?\\s*\\}"
decl_stmt|;
name|Pattern
name|multipleDependenciesPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|m
operator|=
name|multipleDependenciesPattern
operator|.
name|matcher
argument_list|(
name|microIvy
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|DefaultModuleDescriptor
name|md
init|=
name|DefaultModuleDescriptor
operator|.
name|newBasicInstance
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|mrids
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|mrids
operator|!=
literal|null
condition|)
block|{
name|Collection
name|depMrids
init|=
name|parseMrids
argument_list|(
name|mrids
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|depMrids
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
name|ModuleRevisionId
name|dep
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|dep
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|md
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid micro ivy format: "
operator|+
name|microIvy
argument_list|)
throw|;
block|}
comment|/**      * Parses a collection of module descriptors in the micro ivy format, separated by double semi      * columns.      *       * @param microIvy      *            the text representation of the collection of module descriptors      * @return the collection of module descriptors parsed      */
specifier|public
specifier|static
name|Collection
comment|/*<ModuleDescriptor>*/
name|parseMicroIvyDescriptors
parameter_list|(
name|String
name|microIvy
parameter_list|)
block|{
name|String
index|[]
name|mds
init|=
name|microIvy
operator|.
name|split
argument_list|(
literal|"\\s*;;\\s*"
argument_list|)
decl_stmt|;
name|Collection
name|r
init|=
operator|new
name|ArrayList
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
name|mds
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|parseMicroIvyDescriptor
argument_list|(
name|mds
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/**      * Fills a repository with a set of module, using empty files for published artifacts.      *       * @param resolver the resolver to use to publish the modules      * @param mds the descriptors of the modules to put in the repository      * @throws IOException if an IO problem occurs while filling the repository      */
specifier|public
specifier|static
name|void
name|fillRepository
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|Collection
comment|/*<ModuleDescriptor>*/
name|mds
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|tmp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
literal|"tmp"
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|mds
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
name|boolean
name|overwrite
init|=
literal|false
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
operator|(
name|ModuleDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|beginPublishTransaction
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|boolean
name|published
init|=
literal|false
decl_stmt|;
try|try
block|{
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
name|tmp
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|publish
argument_list|(
name|md
operator|.
name|getMetadataArtifact
argument_list|()
argument_list|,
name|tmp
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tmp
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|Artifact
index|[]
name|artifacts
init|=
name|md
operator|.
name|getAllArtifacts
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
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resolver
operator|.
name|publish
argument_list|(
name|artifacts
index|[
name|i
index|]
argument_list|,
name|tmp
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
name|resolver
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
name|published
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|published
condition|)
block|{
name|resolver
operator|.
name|abortPublishTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|tmp
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * A file system resolver which can be used with the      * {@link #fillRepository(DependencyResolver, Collection)} method to create a test case of      * module descriptor.      *<p>      * When finished you should call {@link #cleanTestRepository()}      *</p>      */
specifier|public
specifier|static
name|FileSystemResolver
name|newTestRepository
parameter_list|()
block|{
name|FileSystemResolver
name|testRepository
init|=
operator|new
name|FileSystemResolver
argument_list|()
decl_stmt|;
name|testRepository
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|testRepository
operator|.
name|addIvyPattern
argument_list|(
literal|"build/test/test-repo/[organisation]/[module]/[revision]/[artifact].[ext]"
argument_list|)
expr_stmt|;
name|testRepository
operator|.
name|addArtifactPattern
argument_list|(
literal|"build/test/test-repo/[organisation]/[module]/[revision]/[artifact].[ext]"
argument_list|)
expr_stmt|;
return|return
name|testRepository
return|;
block|}
comment|/**      * Cleans up the test repository.      * @see #newTestRepository()      */
specifier|public
specifier|static
name|void
name|cleanTestRepository
parameter_list|()
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/test-repo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Cleans up the test repository and cache.      * @see #newTestSettings()      */
specifier|public
specifier|static
name|void
name|cleanTest
parameter_list|()
block|{
name|cleanTestRepository
argument_list|()
expr_stmt|;
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/cache"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Init a test resolver as default, useful combined with      * {@link #fillRepository(DependencyResolver, Collection)}.      *       * @param settings      *            the settings to initialize      * @return test settings      */
specifier|public
specifier|static
name|IvySettings
name|loadTestSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|settings
operator|.
name|setDefaultCache
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/cache"
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|addResolver
argument_list|(
name|newTestRepository
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
return|return
name|settings
return|;
block|}
comment|/**      * Create basic resolve data using the given settings      *       * @param settings      *            the settings to use to create the resolve data      * @return basic resolve data useful for testing      */
specifier|public
specifier|static
name|ResolveData
name|newResolveData
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|ResolveData
argument_list|(
operator|new
name|ResolveEngine
argument_list|(
name|settings
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|,
operator|new
name|SortEngine
argument_list|(
name|settings
argument_list|)
argument_list|)
argument_list|,
name|newResolveOptions
argument_list|(
name|settings
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Create basic resolve options using the given settings      *       * @param settings      *            the settings to use to create the resolve options      * @return the basic resolve options, useful for testing      */
specifier|public
specifier|static
name|ResolveOptions
name|newResolveOptions
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|settings
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

