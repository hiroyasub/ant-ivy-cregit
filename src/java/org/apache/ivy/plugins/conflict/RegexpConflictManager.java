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
name|conflict
package|;
end_package

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
name|resolve
operator|.
name|IvyNode
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
comment|/**  * A ConflictManager that can be used to resolve conflicts based on regular  * expressions of the revision of the module. The conflict manager is added like  * this:  *   *<pre>  *&lt;!-- Match all revisions, but ignore the last dot(.) and the character after it.  *        Used to match api changes in out milestones. --&gt;  *&lt;conflict-managers&gt;  *&lt;regexp-cm name=&quot;regexp&quot; regexp=&quot;(.*)\..$&quot; ignoreNonMatching=&quot;true&quot;/&gt;  *&lt;/conflict-managers&gt;  *</pre>  *   * The regular expression must contain a capturing group. The group will be used  * to resolve the conflicts by an String.equals() test. If ignoreNonMatching is  * false non matching modules will result in an exception. If it is true they  * will be compaired by their full revision.  *   * @author Anders janmyr  *   */
end_comment

begin_class
specifier|public
class|class
name|RegexpConflictManager
extends|extends
name|AbstractConflictManager
block|{
specifier|private
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.*)"
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|mIgnoreNonMatching
decl_stmt|;
specifier|public
name|RegexpConflictManager
parameter_list|()
block|{
block|}
specifier|public
name|void
name|setRegexp
parameter_list|(
name|String
name|regexp
parameter_list|)
block|{
name|pattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regexp
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
literal|"abcdef"
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|groupCount
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|String
name|message
init|=
literal|"Pattern does not contain ONE (capturing group): '"
operator|+
name|pattern
operator|+
literal|"'"
decl_stmt|;
name|Message
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setIgnoreNonMatching
parameter_list|(
name|boolean
name|ignoreNonMatching
parameter_list|)
block|{
name|mIgnoreNonMatching
operator|=
name|ignoreNonMatching
expr_stmt|;
block|}
specifier|public
name|Collection
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
name|conflicts
parameter_list|)
block|{
name|IvyNode
name|lastNode
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|conflicts
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
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastNode
operator|!=
literal|null
operator|&&
operator|!
name|matchEquals
argument_list|(
name|node
argument_list|,
name|lastNode
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|lastNode
operator|+
literal|":"
operator|+
name|getMatch
argument_list|(
name|lastNode
argument_list|)
operator|+
literal|" (needed by "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|lastNode
operator|.
name|getAllCallers
argument_list|()
argument_list|)
operator|+
literal|") conflicts with "
operator|+
name|node
operator|+
literal|":"
operator|+
name|getMatch
argument_list|(
name|node
argument_list|)
operator|+
literal|" (needed by "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|node
operator|.
name|getAllCallers
argument_list|()
argument_list|)
operator|+
literal|")"
decl_stmt|;
name|Message
operator|.
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|Message
operator|.
name|sumupProblems
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|StrictConflictException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|lastNode
operator|==
literal|null
operator|||
name|nodeIsGreater
argument_list|(
name|node
argument_list|,
name|lastNode
argument_list|)
condition|)
block|{
name|lastNode
operator|=
name|node
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|lastNode
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|nodeIsGreater
parameter_list|(
name|IvyNode
name|node
parameter_list|,
name|IvyNode
name|lastNode
parameter_list|)
block|{
return|return
name|getMatch
argument_list|(
name|node
argument_list|)
operator|.
name|compareTo
argument_list|(
name|getMatch
argument_list|(
name|lastNode
argument_list|)
argument_list|)
operator|>
literal|0
return|;
block|}
specifier|private
name|boolean
name|matchEquals
parameter_list|(
name|IvyNode
name|lastNode
parameter_list|,
name|IvyNode
name|node
parameter_list|)
block|{
return|return
name|getMatch
argument_list|(
name|lastNode
argument_list|)
operator|.
name|equals
argument_list|(
name|getMatch
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|getMatch
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|String
name|revision
init|=
name|node
operator|.
name|getId
argument_list|()
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|revision
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
name|String
name|match
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|!=
literal|null
condition|)
block|{
return|return
name|match
return|;
block|}
name|warnOrThrow
argument_list|(
literal|"First group of pattern: '"
operator|+
name|pattern
operator|+
literal|"' does not match: "
operator|+
name|revision
operator|+
literal|" "
operator|+
name|node
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|warnOrThrow
argument_list|(
literal|"Pattern: '"
operator|+
name|pattern
operator|+
literal|"' does not match: "
operator|+
name|revision
operator|+
literal|" "
operator|+
name|node
argument_list|)
expr_stmt|;
block|}
return|return
name|revision
return|;
block|}
specifier|private
name|void
name|warnOrThrow
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|mIgnoreNonMatching
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|StrictConflictException
argument_list|(
name|message
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

