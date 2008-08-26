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
name|search
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|matcher
operator|.
name|Matcher
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
name|resolver
operator|.
name|AbstractResolver
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

begin_class
specifier|public
class|class
name|SearchEngine
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|public
name|SearchEngine
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
comment|/**      * Returns an empty array when no token values are found.      *       * @param token      * @param otherTokenValues      * @return      */
specifier|public
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|token
parameter_list|,
name|Map
name|otherTokenValues
parameter_list|)
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|values
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|token
block|}
argument_list|,
name|otherTokenValues
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
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|values
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisationEntries
parameter_list|()
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|orgs
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
block|}
argument_list|,
operator|new
name|HashMap
argument_list|()
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
name|orgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|org
init|=
operator|(
name|String
operator|)
name|orgs
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|OrganisationEntry
argument_list|(
name|resolver
argument_list|,
name|org
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|OrganisationEntry
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|OrganisationEntry
index|[
name|entries
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
name|listOrganisations
parameter_list|()
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|orgs
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
block|}
argument_list|,
operator|new
name|HashMap
argument_list|()
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
name|orgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|orgs
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ModuleEntry
index|[]
name|listModuleEntries
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|org
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|modules
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|}
argument_list|,
name|tokenValues
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
name|modules
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|module
init|=
operator|(
name|String
operator|)
name|modules
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|ModuleEntry
argument_list|(
name|org
argument_list|,
name|module
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ModuleEntry
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleEntry
index|[
name|entries
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
name|listModules
parameter_list|(
name|String
name|org
parameter_list|)
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|org
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|modules
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|}
argument_list|,
name|tokenValues
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
name|modules
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|modules
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisionEntries
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|module
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|module
operator|.
name|getModule
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|revisions
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|REVISION_KEY
block|}
argument_list|,
name|tokenValues
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
name|revisions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|revision
init|=
operator|(
name|String
operator|)
name|revisions
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
decl_stmt|;
name|entries
operator|.
name|add
argument_list|(
operator|new
name|RevisionEntry
argument_list|(
name|module
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|RevisionEntry
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|RevisionEntry
index|[
name|entries
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
name|listRevisions
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|)
block|{
name|Set
name|entries
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
name|Map
name|tokenValues
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|org
argument_list|)
expr_stmt|;
name|tokenValues
operator|.
name|put
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|module
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|revisions
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|REVISION_KEY
block|}
argument_list|,
name|tokenValues
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
name|revisions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|revisions
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|entries
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|entries
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * List module ids of the module accessible through the current resolvers matching the given mid      * criteria according to the given matcher.      *<p>      * ModuleId are returned in the system namespace.      *</p>      *       * @param criteria      * @param matcher      * @return      */
specifier|public
name|ModuleId
index|[]
name|listModules
parameter_list|(
name|ModuleId
name|moduleCrit
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Map
name|criteria
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getName
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
expr_stmt|;
name|String
index|[]
name|tokensToList
init|=
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
block|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|}
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|moduleIdAsMap
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
name|tokensToList
argument_list|,
name|criteria
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
name|moduleIdAsMap
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|org
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|ModuleId
name|modId
init|=
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|modId
argument_list|,
name|resolver
operator|.
name|getNamespace
argument_list|()
operator|.
name|getToSystemTransformer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ModuleId
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleId
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * List module revision ids of the module accessible through the current resolvers matching the      * given mrid criteria according to the given matcher.      *<p>      * ModuleRevisionId are returned in the system namespace.      *</p>      *       * @param criteria      * @param matcher      * @return      */
specifier|public
name|ModuleRevisionId
index|[]
name|listModules
parameter_list|(
name|ModuleRevisionId
name|moduleCrit
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Map
name|criteria
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getName
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getBranch
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getRevision
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
expr_stmt|;
name|String
index|[]
name|tokensToList
init|=
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
block|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|,
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
block|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
block|}
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|settings
operator|.
name|getResolvers
argument_list|()
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
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
index|[]
name|moduleIdAsMap
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
name|tokensToList
argument_list|,
name|criteria
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
name|moduleIdAsMap
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|org
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|)
decl_stmt|;
name|String
name|rev
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|modRevId
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|name
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|)
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|resolver
operator|.
name|getNamespace
argument_list|()
operator|.
name|getToSystemTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|modRevId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * List modules matching a given criteria, available in the given dependency resolver.      *<p>      * ModuleRevisionId are returned in the system namespace.      *</p>      *        * @param resolver the resolver in which modules should looked up      * @param moduleCrit the criteria to match      * @param matcher the matcher to use to match criteria      * @return an array of matching module revision ids      */
specifier|public
name|ModuleRevisionId
index|[]
name|listModules
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ModuleRevisionId
name|moduleCrit
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|Map
name|criteria
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getName
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getBranch
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|)
expr_stmt|;
name|addMatcher
argument_list|(
name|matcher
argument_list|,
name|moduleCrit
operator|.
name|getRevision
argument_list|()
argument_list|,
name|criteria
argument_list|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
expr_stmt|;
name|String
index|[]
name|tokensToList
init|=
operator|new
name|String
index|[]
block|{
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
block|,
name|IvyPatternHelper
operator|.
name|MODULE_KEY
block|,
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
block|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
block|}
decl_stmt|;
name|Map
index|[]
name|moduleIdAsMap
init|=
name|resolver
operator|.
name|listTokenValues
argument_list|(
name|tokensToList
argument_list|,
name|criteria
argument_list|)
decl_stmt|;
name|Set
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
comment|// we use a Set to remove duplicates
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|moduleIdAsMap
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|org
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|)
decl_stmt|;
name|String
name|rev
init|=
operator|(
name|String
operator|)
name|moduleIdAsMap
index|[
name|i
index|]
operator|.
name|get
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|resolver
operator|.
name|getNamespace
argument_list|()
operator|.
name|getToSystemTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|name
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ModuleRevisionId
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
name|void
name|addMatcher
parameter_list|(
name|PatternMatcher
name|patternMatcher
parameter_list|,
name|String
name|expression
parameter_list|,
name|Map
name|criteria
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Matcher
name|matcher
init|=
name|patternMatcher
operator|.
name|getMatcher
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|isExact
argument_list|()
condition|)
block|{
name|criteria
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|criteria
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|matcher
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
name|findModuleRevisionIds
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ModuleRevisionId
name|pattern
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
name|Collection
name|mrids
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|String
name|resolverName
init|=
name|resolver
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"looking for modules matching "
operator|+
name|pattern
operator|+
literal|" using "
operator|+
name|matcher
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Namespace
name|fromNamespace
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resolver
operator|instanceof
name|AbstractResolver
condition|)
block|{
name|fromNamespace
operator|=
operator|(
operator|(
name|AbstractResolver
operator|)
name|resolver
operator|)
operator|.
name|getNamespace
argument_list|()
expr_stmt|;
block|}
name|Collection
name|modules
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|OrganisationEntry
index|[]
name|orgs
init|=
name|resolver
operator|.
name|listOrganisations
argument_list|()
decl_stmt|;
if|if
condition|(
name|orgs
operator|==
literal|null
operator|||
name|orgs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|// hack for resolvers which are not able to list organisation, we try to see if the
comment|// asked organisation is not an exact one:
name|String
name|org
init|=
name|pattern
operator|.
name|getOrganisation
argument_list|()
decl_stmt|;
if|if
condition|(
name|fromNamespace
operator|!=
literal|null
condition|)
block|{
name|org
operator|=
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|pattern
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|fromNamespace
operator|.
name|getFromSystemTransformer
argument_list|()
argument_list|)
operator|.
name|getOrganisation
argument_list|()
expr_stmt|;
block|}
name|modules
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|resolver
operator|.
name|listModules
argument_list|(
operator|new
name|OrganisationEntry
argument_list|(
name|resolver
argument_list|,
name|org
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Matcher
name|orgMatcher
init|=
name|matcher
operator|.
name|getMatcher
argument_list|(
name|pattern
operator|.
name|getOrganisation
argument_list|()
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
name|orgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|org
init|=
name|orgs
index|[
name|i
index|]
operator|.
name|getOrganisation
argument_list|()
decl_stmt|;
name|String
name|systemOrg
init|=
name|org
decl_stmt|;
if|if
condition|(
name|fromNamespace
operator|!=
literal|null
condition|)
block|{
name|systemOrg
operator|=
name|NameSpaceHelper
operator|.
name|transformOrganisation
argument_list|(
name|org
argument_list|,
name|fromNamespace
operator|.
name|getToSystemTransformer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|orgMatcher
operator|.
name|matches
argument_list|(
name|systemOrg
argument_list|)
condition|)
block|{
name|modules
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|resolver
operator|.
name|listModules
argument_list|(
operator|new
name|OrganisationEntry
argument_list|(
name|resolver
argument_list|,
name|org
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"found "
operator|+
name|modules
operator|.
name|size
argument_list|()
operator|+
literal|" modules for "
operator|+
name|pattern
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|" on "
operator|+
name|resolverName
argument_list|)
expr_stmt|;
name|boolean
name|foundModule
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|modules
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
name|ModuleEntry
name|mEntry
init|=
operator|(
name|ModuleEntry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleId
name|foundMid
init|=
operator|new
name|ModuleId
argument_list|(
name|mEntry
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mEntry
operator|.
name|getModule
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleId
name|systemMid
init|=
name|foundMid
decl_stmt|;
if|if
condition|(
name|fromNamespace
operator|!=
literal|null
condition|)
block|{
name|systemMid
operator|=
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|foundMid
argument_list|,
name|fromNamespace
operator|.
name|getToSystemTransformer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|MatcherHelper
operator|.
name|matches
argument_list|(
name|matcher
argument_list|,
name|pattern
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|systemMid
argument_list|)
condition|)
block|{
comment|// The module corresponds to the searched module pattern
name|foundModule
operator|=
literal|true
expr_stmt|;
name|RevisionEntry
index|[]
name|rEntries
init|=
name|resolver
operator|.
name|listRevisions
argument_list|(
name|mEntry
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"found "
operator|+
name|rEntries
operator|.
name|length
operator|+
literal|" revisions for ["
operator|+
name|mEntry
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|", "
operator|+
name|mEntry
operator|.
name|getModule
argument_list|()
operator|+
literal|"] on "
operator|+
name|resolverName
argument_list|)
expr_stmt|;
name|boolean
name|foundRevision
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|rEntries
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|RevisionEntry
name|rEntry
init|=
name|rEntries
index|[
name|j
index|]
decl_stmt|;
name|ModuleRevisionId
name|foundMrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mEntry
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mEntry
operator|.
name|getModule
argument_list|()
argument_list|,
name|rEntry
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|systemMrid
init|=
name|foundMrid
decl_stmt|;
if|if
condition|(
name|fromNamespace
operator|!=
literal|null
condition|)
block|{
name|systemMrid
operator|=
name|fromNamespace
operator|.
name|getToSystemTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|foundMrid
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|MatcherHelper
operator|.
name|matches
argument_list|(
name|matcher
argument_list|,
name|pattern
argument_list|,
name|systemMrid
argument_list|)
condition|)
block|{
comment|// We have a matching module revision
name|foundRevision
operator|=
literal|true
expr_stmt|;
name|mrids
operator|.
name|add
argument_list|(
name|systemMrid
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|foundRevision
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no revision found matching "
operator|+
name|pattern
operator|+
literal|" in ["
operator|+
name|mEntry
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|","
operator|+
name|mEntry
operator|.
name|getModule
argument_list|()
operator|+
literal|"] using "
operator|+
name|resolverName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|foundModule
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no module found matching "
operator|+
name|pattern
operator|+
literal|" using "
operator|+
name|resolverName
argument_list|)
expr_stmt|;
block|}
return|return
name|mrids
return|;
block|}
block|}
end_class

end_unit

