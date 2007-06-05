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
name|Arrays
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
name|Collections
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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|MatcherHelper
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
name|NameSpaceHelper
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
name|namespace
operator|.
name|NamespaceTransformer
import|;
end_import

begin_comment
comment|/**  * This class can be used as the default implementation for DependencyDescriptor. It implements  * required methods and enables to fill dependency information with the addDependencyConfiguration  * method.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDependencyDescriptor
implements|implements
name|DependencyDescriptor
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|SELF_FALLBACK_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"@(\\(.*\\))?"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|THIS_FALLBACK_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"#(\\(.*\\))?"
argument_list|)
decl_stmt|;
comment|/**      * Transforms the given dependency descriptor of the given namespace and return a new dependency      * descriptor in the system namespace.<i>Note that exclude rules are not converted in system      * namespace, because they aren't transformable (the name space hasn't the ability to convert      * regular expressions). However, method doesExclude will work with system artifacts.</i>      *       * @param md      * @param ns      * @return      */
specifier|public
specifier|static
name|DependencyDescriptor
name|transformInstance
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|Namespace
name|ns
parameter_list|)
block|{
name|NamespaceTransformer
name|t
init|=
name|ns
operator|.
name|getToSystemTransformer
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|isIdentity
argument_list|()
condition|)
block|{
return|return
name|dd
return|;
block|}
name|DefaultDependencyDescriptor
name|newdd
init|=
name|transformInstance
argument_list|(
name|dd
argument_list|,
name|t
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|newdd
operator|.
name|_namespace
operator|=
name|ns
expr_stmt|;
return|return
name|newdd
return|;
block|}
comment|/**      * Transforms a dependency descriptor using the given transformer. Note that no namespace info      * will be attached to the transformed dependency descriptor, so calling doesExclude is not      * recommended (doesExclude only works when namespace is properly set)      *       * @param dd      * @param t      * @return      */
specifier|public
specifier|static
name|DefaultDependencyDescriptor
name|transformInstance
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|NamespaceTransformer
name|t
parameter_list|,
name|boolean
name|fromSystem
parameter_list|)
block|{
name|ModuleRevisionId
name|transformParentId
init|=
name|t
operator|.
name|transform
argument_list|(
name|dd
operator|.
name|getParentRevisionId
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|transformMrid
init|=
name|t
operator|.
name|transform
argument_list|(
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|newdd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
literal|null
argument_list|,
name|transformMrid
argument_list|,
name|dd
operator|.
name|isForce
argument_list|()
argument_list|,
name|dd
operator|.
name|isChanging
argument_list|()
argument_list|,
name|dd
operator|.
name|isTransitive
argument_list|()
argument_list|)
decl_stmt|;
name|newdd
operator|.
name|_parentId
operator|=
name|transformParentId
expr_stmt|;
name|String
index|[]
name|moduleConfs
init|=
name|dd
operator|.
name|getModuleConfigurations
argument_list|()
decl_stmt|;
if|if
condition|(
name|moduleConfs
operator|.
name|length
operator|==
literal|1
operator|&&
literal|"*"
operator|.
name|equals
argument_list|(
name|moduleConfs
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
if|if
condition|(
name|dd
operator|instanceof
name|DefaultDependencyDescriptor
condition|)
block|{
name|DefaultDependencyDescriptor
name|ddd
init|=
operator|(
name|DefaultDependencyDescriptor
operator|)
name|dd
decl_stmt|;
name|newdd
operator|.
name|_confs
operator|=
operator|new
name|LinkedHashMap
argument_list|(
name|ddd
operator|.
name|_confs
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_excludeRules
operator|=
operator|new
name|LinkedHashMap
argument_list|(
name|ddd
operator|.
name|_excludeRules
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_includeRules
operator|=
operator|new
name|LinkedHashMap
argument_list|(
name|ddd
operator|.
name|_includeRules
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_dependencyArtifacts
operator|=
operator|new
name|LinkedHashMap
argument_list|(
name|ddd
operator|.
name|_dependencyArtifacts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"dependency descriptor transformation does not support * module confs with descriptors which aren't DefaultDependencyDescriptor"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|moduleConfs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|newdd
operator|.
name|_confs
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getDependencyConfigurations
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_excludeRules
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getExcludeRules
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_includeRules
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getIncludeRules
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|newdd
operator|.
name|_dependencyArtifacts
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getDependencyArtifacts
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|fromSystem
condition|)
block|{
name|newdd
operator|.
name|_asSystem
operator|=
name|dd
expr_stmt|;
block|}
return|return
name|newdd
return|;
block|}
specifier|private
name|ModuleRevisionId
name|_revId
decl_stmt|;
specifier|private
name|Map
name|_confs
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|_dependencyArtifacts
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map (String masterConf ->
comment|// Collection(DependencyArtifactDescriptor))
specifier|private
name|Map
name|_includeRules
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map (String masterConf ->
comment|// Collection(IncludeRule))
specifier|private
name|Map
name|_excludeRules
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map (String masterConf ->
comment|// Collection(ExcludeRule))
specifier|private
name|Set
name|_extends
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
comment|/**      * Used to indicate that this revision must be used in case of conflicts, independently of      * conflicts manager      */
specifier|private
name|boolean
name|_force
decl_stmt|;
comment|/**      * Used to indicate that the dependency is a changing one, i.e. that ivy should not rely on the      * version to know if it can trust artifacts in cache      */
specifier|private
name|boolean
name|_changing
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|_parentId
decl_stmt|;
specifier|private
name|boolean
name|_transitive
init|=
literal|true
decl_stmt|;
comment|/**      * This namespace should be used to check      */
specifier|private
name|Namespace
name|_namespace
init|=
literal|null
decl_stmt|;
specifier|private
specifier|final
name|ModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|DependencyDescriptor
name|_asSystem
init|=
name|this
decl_stmt|;
specifier|public
name|DefaultDependencyDescriptor
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|_md
operator|=
literal|null
expr_stmt|;
name|_parentId
operator|=
name|dd
operator|.
name|getParentRevisionId
argument_list|()
expr_stmt|;
name|_revId
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
argument_list|,
name|revision
argument_list|)
expr_stmt|;
name|_force
operator|=
name|dd
operator|.
name|isForce
argument_list|()
expr_stmt|;
name|_changing
operator|=
name|dd
operator|.
name|isChanging
argument_list|()
expr_stmt|;
name|_transitive
operator|=
name|dd
operator|.
name|isTransitive
argument_list|()
expr_stmt|;
name|String
index|[]
name|moduleConfs
init|=
name|dd
operator|.
name|getModuleConfigurations
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
name|moduleConfs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|_confs
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getDependencyConfigurations
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|_excludeRules
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getExcludeRules
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|_includeRules
operator|.
name|put
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|,
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dd
operator|.
name|getIncludeRules
argument_list|(
name|moduleConfs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DefaultDependencyDescriptor
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|boolean
name|force
parameter_list|,
name|boolean
name|changing
parameter_list|,
name|boolean
name|transitive
parameter_list|)
block|{
name|_md
operator|=
name|md
expr_stmt|;
name|_revId
operator|=
name|mrid
expr_stmt|;
name|_force
operator|=
name|force
expr_stmt|;
name|_changing
operator|=
name|changing
expr_stmt|;
name|_transitive
operator|=
name|transitive
expr_stmt|;
block|}
specifier|public
name|DefaultDependencyDescriptor
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
name|this
argument_list|(
name|mrid
argument_list|,
name|force
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultDependencyDescriptor
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|boolean
name|force
parameter_list|,
name|boolean
name|changing
parameter_list|)
block|{
name|_md
operator|=
literal|null
expr_stmt|;
name|_revId
operator|=
name|mrid
expr_stmt|;
name|_force
operator|=
name|force
expr_stmt|;
name|_changing
operator|=
name|changing
expr_stmt|;
block|}
specifier|public
name|ModuleId
name|getDependencyId
parameter_list|()
block|{
return|return
name|getDependencyRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getDependencyRevisionId
parameter_list|()
block|{
return|return
name|_revId
return|;
block|}
specifier|public
name|String
index|[]
name|getModuleConfigurations
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|_confs
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_confs
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|)
block|{
return|return
name|getDependencyConfigurations
argument_list|(
name|moduleConfiguration
argument_list|,
name|moduleConfiguration
argument_list|)
return|;
block|}
comment|/**      * Return the dependency configurations mapped to the given moduleConfiguration, actually      * resolved because of the given requestedConfiguration Usually requestedConfiguration and      * moduleConfiguration are the same, except when a conf extends another, then the      * moduleConfiguration is the configuration currently resolved (the extended one), and      * requestedConfiguration is the one actually requested initially (the extending one). Both      * moduleConfiguration and requestedConfiguration are configurations of the caller, the array      * returned is composed of the required configurations of the dependency described by this      * descriptor.      */
specifier|public
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|,
name|String
name|requestedConfiguration
parameter_list|)
block|{
name|List
name|confs
init|=
operator|(
name|List
operator|)
name|_confs
operator|.
name|get
argument_list|(
name|moduleConfiguration
argument_list|)
decl_stmt|;
if|if
condition|(
name|confs
operator|==
literal|null
condition|)
block|{
comment|// there is no mapping defined for this configuration, add the 'other' mappings.
name|confs
operator|=
operator|(
name|List
operator|)
name|_confs
operator|.
name|get
argument_list|(
literal|"%"
argument_list|)
expr_stmt|;
block|}
name|List
name|defConfs
init|=
operator|(
name|List
operator|)
name|_confs
operator|.
name|get
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|Collection
name|ret
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|confs
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|confs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defConfs
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|defConfs
argument_list|)
expr_stmt|;
block|}
name|Collection
name|replacedRet
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|ret
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
name|String
name|c
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|replacedConf
init|=
name|replaceSelfFallbackPattern
argument_list|(
name|c
argument_list|,
name|moduleConfiguration
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacedConf
operator|==
literal|null
condition|)
block|{
name|replacedConf
operator|=
name|replaceThisFallbackPattern
argument_list|(
name|c
argument_list|,
name|requestedConfiguration
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|replacedConf
operator|!=
literal|null
condition|)
block|{
name|c
operator|=
name|replacedConf
expr_stmt|;
block|}
name|replacedRet
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|ret
operator|=
name|replacedRet
expr_stmt|;
if|if
condition|(
name|ret
operator|.
name|remove
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|StringBuffer
name|r
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
comment|// merge excluded configurations as one conf like *!A!B
for|for
control|(
name|Iterator
name|iter
init|=
name|ret
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
name|String
name|c
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|startsWith
argument_list|(
literal|"!"
argument_list|)
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|String
index|[]
block|{
name|r
operator|.
name|toString
argument_list|()
block|}
return|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|String
name|replaceSelfFallbackPattern
parameter_list|(
specifier|final
name|String
name|conf
parameter_list|,
specifier|final
name|String
name|moduleConfiguration
parameter_list|)
block|{
return|return
name|replaceFallbackConfigurationPattern
argument_list|(
name|SELF_FALLBACK_PATTERN
argument_list|,
name|conf
argument_list|,
name|moduleConfiguration
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|String
name|replaceThisFallbackPattern
parameter_list|(
specifier|final
name|String
name|conf
parameter_list|,
specifier|final
name|String
name|requestedConfiguration
parameter_list|)
block|{
return|return
name|replaceFallbackConfigurationPattern
argument_list|(
name|THIS_FALLBACK_PATTERN
argument_list|,
name|conf
argument_list|,
name|requestedConfiguration
argument_list|)
return|;
block|}
comment|/**      * Replaces fallback patterns with correct values if fallback pattern exists.      *       * @param pattern      *            pattern to look for      * @param conf      *            configuration mapping from dependency element      * @param moduleConfiguration      *            module's configuration to use for replacement      * @return Replaced string if pattern matched. Otherwise null.      */
specifier|protected
specifier|static
name|String
name|replaceFallbackConfigurationPattern
parameter_list|(
specifier|final
name|Pattern
name|pattern
parameter_list|,
specifier|final
name|String
name|conf
parameter_list|,
specifier|final
name|String
name|moduleConfiguration
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|conf
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
if|if
condition|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|moduleConfiguration
operator|+
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|moduleConfiguration
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
block|{
name|Set
name|confs
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
name|moduleConfigurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|confs
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getDependencyConfigurations
argument_list|(
name|moduleConfigurations
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|confs
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
return|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|confs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifacts
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|)
block|{
name|Collection
name|artifacts
init|=
name|getCollectionForConfiguration
argument_list|(
name|moduleConfiguration
argument_list|,
name|_dependencyArtifacts
argument_list|)
decl_stmt|;
return|return
operator|(
name|DependencyArtifactDescriptor
index|[]
operator|)
name|artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|DependencyArtifactDescriptor
index|[
name|artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IncludeRule
index|[]
name|getIncludeRules
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|)
block|{
name|Collection
name|rules
init|=
name|getCollectionForConfiguration
argument_list|(
name|moduleConfiguration
argument_list|,
name|_includeRules
argument_list|)
decl_stmt|;
return|return
operator|(
name|IncludeRule
index|[]
operator|)
name|rules
operator|.
name|toArray
argument_list|(
operator|new
name|IncludeRule
index|[
name|rules
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ExcludeRule
index|[]
name|getExcludeRules
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|)
block|{
name|Collection
name|rules
init|=
name|getCollectionForConfiguration
argument_list|(
name|moduleConfiguration
argument_list|,
name|_excludeRules
argument_list|)
decl_stmt|;
return|return
operator|(
name|ExcludeRule
index|[]
operator|)
name|rules
operator|.
name|toArray
argument_list|(
operator|new
name|ExcludeRule
index|[
name|rules
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|Set
name|getCollectionForConfiguration
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|,
name|Map
name|collectionMap
parameter_list|)
block|{
if|if
condition|(
name|collectionMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_SET
return|;
block|}
name|Collection
name|artifacts
init|=
operator|(
name|Collection
operator|)
name|collectionMap
operator|.
name|get
argument_list|(
name|moduleConfiguration
argument_list|)
decl_stmt|;
name|Collection
name|defArtifacts
init|=
operator|(
name|Collection
operator|)
name|collectionMap
operator|.
name|get
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|Set
name|ret
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifacts
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defArtifacts
operator|!=
literal|null
condition|)
block|{
name|ret
operator|.
name|addAll
argument_list|(
name|defArtifacts
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifacts
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
block|{
name|Set
name|artifacts
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
name|moduleConfigurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|artifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getDependencyArtifacts
argument_list|(
name|moduleConfigurations
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|DependencyArtifactDescriptor
index|[]
operator|)
name|artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|DependencyArtifactDescriptor
index|[
name|artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IncludeRule
index|[]
name|getIncludeRules
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
block|{
name|Set
name|rules
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
name|moduleConfigurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|rules
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getIncludeRules
argument_list|(
name|moduleConfigurations
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|IncludeRule
index|[]
operator|)
name|rules
operator|.
name|toArray
argument_list|(
operator|new
name|IncludeRule
index|[
name|rules
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ExcludeRule
index|[]
name|getExcludeRules
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
block|{
name|Set
name|rules
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
name|moduleConfigurations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|rules
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|getExcludeRules
argument_list|(
name|moduleConfigurations
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ExcludeRule
index|[]
operator|)
name|rules
operator|.
name|toArray
argument_list|(
operator|new
name|ExcludeRule
index|[
name|rules
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|DependencyArtifactDescriptor
index|[]
name|getAllDependencyArtifacts
parameter_list|()
block|{
name|Set
name|ret
init|=
name|mergeAll
argument_list|(
name|_dependencyArtifacts
argument_list|)
decl_stmt|;
return|return
operator|(
name|DependencyArtifactDescriptor
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|DependencyArtifactDescriptor
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IncludeRule
index|[]
name|getAllIncludeRules
parameter_list|()
block|{
name|Set
name|ret
init|=
name|mergeAll
argument_list|(
name|_includeRules
argument_list|)
decl_stmt|;
return|return
operator|(
name|IncludeRule
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|IncludeRule
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ExcludeRule
index|[]
name|getAllExcludeRules
parameter_list|()
block|{
name|Set
name|ret
init|=
name|mergeAll
argument_list|(
name|_excludeRules
argument_list|)
decl_stmt|;
return|return
operator|(
name|ExcludeRule
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ExcludeRule
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|Set
name|mergeAll
parameter_list|(
name|Map
name|artifactsMap
parameter_list|)
block|{
name|Set
name|ret
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|artifactsMap
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
name|Collection
name|artifacts
init|=
operator|(
name|Collection
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|void
name|addDependencyConfiguration
parameter_list|(
name|String
name|masterConf
parameter_list|,
name|String
name|depConf
parameter_list|)
block|{
if|if
condition|(
operator|(
name|_md
operator|!=
literal|null
operator|)
operator|&&
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|masterConf
argument_list|)
operator|&&
operator|!
literal|"%"
operator|.
name|equals
argument_list|(
name|masterConf
argument_list|)
condition|)
block|{
name|Configuration
name|config
init|=
name|_md
operator|.
name|getConfiguration
argument_list|(
name|masterConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Configuration '"
operator|+
name|masterConf
operator|+
literal|"' does not exist in module "
operator|+
name|_md
argument_list|)
throw|;
block|}
block|}
name|List
name|confs
init|=
operator|(
name|List
operator|)
name|_confs
operator|.
name|get
argument_list|(
name|masterConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|confs
operator|==
literal|null
condition|)
block|{
name|confs
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_confs
operator|.
name|put
argument_list|(
name|masterConf
argument_list|,
name|confs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|confs
operator|.
name|contains
argument_list|(
name|depConf
argument_list|)
condition|)
block|{
name|confs
operator|.
name|add
argument_list|(
name|depConf
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addDependencyArtifact
parameter_list|(
name|String
name|masterConf
parameter_list|,
name|DependencyArtifactDescriptor
name|dad
parameter_list|)
block|{
name|addObjectToConfiguration
argument_list|(
name|masterConf
argument_list|,
name|dad
argument_list|,
name|_dependencyArtifacts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addIncludeRule
parameter_list|(
name|String
name|masterConf
parameter_list|,
name|IncludeRule
name|rule
parameter_list|)
block|{
name|addObjectToConfiguration
argument_list|(
name|masterConf
argument_list|,
name|rule
argument_list|,
name|_includeRules
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addExcludeRule
parameter_list|(
name|String
name|masterConf
parameter_list|,
name|ExcludeRule
name|rule
parameter_list|)
block|{
name|addObjectToConfiguration
argument_list|(
name|masterConf
argument_list|,
name|rule
argument_list|,
name|_excludeRules
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addObjectToConfiguration
parameter_list|(
name|String
name|callerConf
parameter_list|,
name|Object
name|toAdd
parameter_list|,
name|Map
name|confsMap
parameter_list|)
block|{
name|Collection
name|col
init|=
operator|(
name|Collection
operator|)
name|confsMap
operator|.
name|get
argument_list|(
name|callerConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|col
operator|==
literal|null
condition|)
block|{
name|col
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|confsMap
operator|.
name|put
argument_list|(
name|callerConf
argument_list|,
name|col
argument_list|)
expr_stmt|;
block|}
name|col
operator|.
name|add
argument_list|(
name|toAdd
argument_list|)
expr_stmt|;
block|}
comment|/**      * only works when namespace is properly set. The behaviour is not specified if namespace is not      * set      */
specifier|public
name|boolean
name|doesExclude
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|,
name|ArtifactId
name|artifactId
parameter_list|)
block|{
if|if
condition|(
name|_namespace
operator|!=
literal|null
condition|)
block|{
name|artifactId
operator|=
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|artifactId
argument_list|,
name|_namespace
operator|.
name|getFromSystemTransformer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ExcludeRule
index|[]
name|rules
init|=
name|getExcludeRules
argument_list|(
name|moduleConfigurations
argument_list|)
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
name|rules
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|MatcherHelper
operator|.
name|matches
argument_list|(
name|rules
index|[
name|i
index|]
operator|.
name|getMatcher
argument_list|()
argument_list|,
name|rules
index|[
name|i
index|]
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Returns true if this descriptor contains any exclusion rule      *       * @return      */
specifier|public
name|boolean
name|canExclude
parameter_list|()
block|{
return|return
operator|!
name|_excludeRules
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|addExtends
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|_extends
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"dependency: "
operator|+
name|_revId
operator|+
literal|" "
operator|+
name|_confs
return|;
block|}
specifier|public
name|boolean
name|isForce
parameter_list|()
block|{
return|return
name|_force
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getParentRevisionId
parameter_list|()
block|{
return|return
name|_md
operator|!=
literal|null
condition|?
name|_md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
else|:
name|_parentId
return|;
block|}
specifier|public
name|boolean
name|isChanging
parameter_list|()
block|{
return|return
name|_changing
return|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|_transitive
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
return|return
name|_namespace
return|;
block|}
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
name|_revId
operator|.
name|getAttribute
argument_list|(
name|attName
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getAttributes
parameter_list|()
block|{
return|return
name|_revId
operator|.
name|getAttributes
argument_list|()
return|;
block|}
specifier|public
name|String
name|getExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
name|_revId
operator|.
name|getExtraAttribute
argument_list|(
name|attName
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getExtraAttributes
parameter_list|()
block|{
return|return
name|_revId
operator|.
name|getExtraAttributes
argument_list|()
return|;
block|}
specifier|public
name|String
name|getStandardAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
name|_revId
operator|.
name|getStandardAttribute
argument_list|(
name|attName
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getStandardAttributes
parameter_list|()
block|{
return|return
name|_revId
operator|.
name|getStandardAttributes
argument_list|()
return|;
block|}
specifier|public
name|DependencyDescriptor
name|asSystem
parameter_list|()
block|{
return|return
name|_asSystem
return|;
block|}
block|}
end_class

end_unit

