begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|id
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
name|Collections
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
name|plugins
operator|.
name|matcher
operator|.
name|MapMatcher
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
name|Checks
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
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|NoFilter
import|;
end_import

begin_comment
comment|/**  * A list of module specific rules.  *<p>  * This class defines a list of module specific rules. For each module only one rule apply,  * sometimes none.  *</p>  *<p>  * To know which rule to apply, they are configured using matchers. So you can define a rule  * applying to all module from one particular organization, or to all modules with a revisions  * matching a pattern, and so on.  *</p>  *<p>  * Rules condition are evaluated in order, so the first matching rule is returned.  *</p>  *<p>  * Rules themselves can be represented by any object, depending on the purpose of the rule (define  * which resolver to use, which TTL in cache, ...)  *</p>  *  * @param<T> a type parameter  */
end_comment

begin_class
specifier|public
class|class
name|ModuleRules
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|Map
argument_list|<
name|MapMatcher
argument_list|,
name|T
argument_list|>
name|rules
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|MatcherLookup
name|matcherLookup
init|=
operator|new
name|MatcherLookup
argument_list|()
decl_stmt|;
comment|/**      * Constructs an empty ModuleRules.      */
specifier|public
name|ModuleRules
parameter_list|()
block|{
block|}
specifier|private
name|ModuleRules
parameter_list|(
name|Map
argument_list|<
name|MapMatcher
argument_list|,
name|T
argument_list|>
name|rules
parameter_list|)
block|{
name|this
operator|.
name|rules
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|rules
argument_list|)
expr_stmt|;
for|for
control|(
name|MapMatcher
name|matcher
range|:
name|rules
operator|.
name|keySet
argument_list|()
control|)
block|{
name|matcherLookup
operator|.
name|add
argument_list|(
name|matcher
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Defines a new rule for the given condition.      *      * @param condition      *            the condition for which the rule should be applied. Must not be<code>null</code>.      * @param rule      *            the rule to apply. Must not be<code>null</code>.      */
specifier|public
name|void
name|defineRule
parameter_list|(
name|MapMatcher
name|condition
parameter_list|,
name|T
name|rule
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|condition
argument_list|,
literal|"condition"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|rule
argument_list|,
literal|"rule"
argument_list|)
expr_stmt|;
name|rules
operator|.
name|put
argument_list|(
name|condition
argument_list|,
name|rule
argument_list|)
expr_stmt|;
name|matcherLookup
operator|.
name|add
argument_list|(
name|condition
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the rule object matching the given {@link ModuleId}, or<code>null</code> if no rule      * applies.      *      * @param mid      *            the {@link ModuleId} to search the rule for. Must not be<code>null</code>.      * @return the rule object matching the given {@link ModuleId}, or<code>null</code> if no rule      *         applies.      * @see #getRule(ModuleId, Filter)      */
specifier|public
name|T
name|getRule
parameter_list|(
name|ModuleId
name|mid
parameter_list|)
block|{
return|return
name|getRule
argument_list|(
name|mid
argument_list|,
name|NoFilter
operator|.
expr|<
name|T
operator|>
name|instance
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the rules objects matching the given {@link ModuleId}, or an empty array if no rule      * applies.      *      * @param mid      *            the {@link ModuleId} to search the rule for. Must not be<code>null</code>.      * @return an array of rule objects matching the given {@link ModuleId}.      */
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|getRules
parameter_list|(
name|ModuleId
name|mid
parameter_list|)
block|{
return|return
name|getRules
argument_list|(
name|mid
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|NoFilter
operator|.
expr|<
name|T
operator|>
name|instance
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the rule object matching the given {@link ModuleRevisionId}, or<code>null</code> if      * no rule applies.      *      * @param mrid      *            the {@link ModuleRevisionId} to search the rule for. Must not be<code>null</code>      *            .      * @return the rule object matching the given {@link ModuleRevisionId}, or<code>null</code> if      *         no rule applies.      * @see #getRule(ModuleRevisionId, Filter)      */
specifier|public
name|T
name|getRule
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
name|getRule
argument_list|(
name|mrid
argument_list|,
name|NoFilter
operator|.
expr|<
name|T
operator|>
name|instance
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the rule object matching the given {@link ModuleId} and accepted by the given      * {@link Filter}, or<code>null</code> if no rule applies.      *      * @param mid      *            the {@link ModuleRevisionId} to search the rule for. Must not be<code>null</code>      *            .      * @param filter      *            the filter to use to filter the rule to return. The {@link Filter#accept(Object)}      *            method will be called only with rule objects matching the given {@link ModuleId},      *            and the first rule object accepted by the filter will be returned. Must not be      *<code>null</code>.      * @return the rule object matching the given {@link ModuleId}, or<code>null</code> if no rule      *         applies.      * @see #getRule(ModuleRevisionId, Filter)      */
specifier|public
name|T
name|getRule
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|Filter
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|mid
argument_list|,
literal|"mid"
argument_list|)
expr_stmt|;
return|return
name|getRule
argument_list|(
name|mid
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|filter
argument_list|)
return|;
block|}
comment|/**      * Returns the rule object matching the given {@link ModuleRevisionId} and accepted by the given      * {@link Filter}, or<code>null</code> if no rule applies.      *      * @param mrid      *            the {@link ModuleRevisionId} to search the rule for. Must not be<code>null</code>      *            .      * @param filter      *            the filter to use to filter the rule to return. The {@link Filter#accept(Object)}      *            method will be called only with rule objects matching the given      *            {@link ModuleRevisionId}, and the first rule object accepted by the filter will be      *            returned. Must not be<code>null</code>.      * @return the rule object matching the given {@link ModuleRevisionId}, or<code>null</code> if      *         no rule applies.      * @see #getRule(ModuleRevisionId)      */
specifier|public
name|T
name|getRule
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Filter
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|mrid
argument_list|,
literal|"mrid"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|filter
argument_list|,
literal|"filter"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|moduleAttributes
init|=
name|mrid
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
return|return
name|getRule
argument_list|(
name|moduleAttributes
argument_list|,
name|filter
argument_list|)
return|;
block|}
specifier|private
name|T
name|getRule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|moduleAttributes
parameter_list|,
name|Filter
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|)
block|{
for|for
control|(
name|MapMatcher
name|midm
range|:
name|matcherLookup
operator|.
name|get
argument_list|(
name|moduleAttributes
argument_list|)
control|)
block|{
name|T
name|rule
init|=
name|rules
operator|.
name|get
argument_list|(
name|midm
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|.
name|accept
argument_list|(
name|rule
argument_list|)
condition|)
block|{
return|return
name|rule
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the rules object matching the given {@link ModuleRevisionId} and accepted by the      * given {@link Filter}, or an empty array if no rule applies.      *      * @param mrid      *            the {@link ModuleRevisionId} to search the rule for. Must not be<code>null</code>      *            .      * @param filter      *            the filter to use to filter the rule to return. The {@link Filter#accept(Object)}      *            method will be called only with rule objects matching the given      *            {@link ModuleRevisionId}. Must not be<code>null</code>.      * @return an array of rule objects matching the given {@link ModuleRevisionId}.      */
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|getRules
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Filter
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|mrid
argument_list|,
literal|"mrid"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|filter
argument_list|,
literal|"filter"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|moduleAttributes
init|=
name|mrid
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
return|return
name|getRules
argument_list|(
name|moduleAttributes
argument_list|,
name|filter
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|T
argument_list|>
name|getRules
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|moduleAttributes
parameter_list|,
name|Filter
argument_list|<
name|T
argument_list|>
name|filter
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|matchingRules
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|MapMatcher
name|midm
range|:
name|matcherLookup
operator|.
name|get
argument_list|(
name|moduleAttributes
argument_list|)
control|)
block|{
name|T
name|rule
init|=
name|rules
operator|.
name|get
argument_list|(
name|midm
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|.
name|accept
argument_list|(
name|rule
argument_list|)
condition|)
block|{
name|matchingRules
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|matchingRules
return|;
block|}
comment|/**      * Dump the list of rules to {@link Message#debug(String)}      *      * @param prefix      *            the prefix to use for each line dumped      */
specifier|public
name|void
name|dump
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|rules
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
name|prefix
operator|+
literal|"NONE"
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|MapMatcher
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|rules
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|MapMatcher
name|midm
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|T
name|rule
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|prefix
operator|+
name|midm
operator|+
literal|" -> "
operator|+
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns an unmodifiable view of all the rules defined on this ModuleRules.      *<p>      * The rules are returned in a Map where they keys are the MapMatchers matching the rules      * object, and the values are the rules object themselves.      *</p>      *      * @return an unmodifiable view of all the rules defined on this ModuleRules.      */
specifier|public
name|Map
argument_list|<
name|MapMatcher
argument_list|,
name|T
argument_list|>
name|getAllRules
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|rules
argument_list|)
return|;
block|}
specifier|public
name|ModuleRules
argument_list|<
name|T
argument_list|>
name|clone
parameter_list|()
block|{
return|return
operator|new
name|ModuleRules
argument_list|<>
argument_list|(
name|rules
argument_list|)
return|;
block|}
block|}
end_class

end_unit

