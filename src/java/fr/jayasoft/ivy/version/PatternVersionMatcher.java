begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|version
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|matcher
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|version
operator|.
name|AbstractVersionMatcher
import|;
end_import

begin_comment
comment|/**  *   * @author Maarten Coene  */
end_comment

begin_class
specifier|public
class|class
name|PatternVersionMatcher
extends|extends
name|AbstractVersionMatcher
block|{
specifier|private
name|List
name|_matches
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|_RevisionMatches
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// revision -> list of Match instances
specifier|private
name|boolean
name|_init
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|addMatch
parameter_list|(
name|Match
name|match
parameter_list|)
block|{
name|_matches
operator|.
name|add
argument_list|(
name|match
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_init
condition|)
block|{
for|for
control|(
name|Iterator
name|it
init|=
name|_matches
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
name|Match
name|match
init|=
operator|(
name|Match
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
name|matches
init|=
operator|(
name|List
operator|)
name|_RevisionMatches
operator|.
name|get
argument_list|(
name|match
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
operator|==
literal|null
condition|)
block|{
name|matches
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_RevisionMatches
operator|.
name|put
argument_list|(
name|match
operator|.
name|getRevision
argument_list|()
argument_list|,
name|matches
argument_list|)
expr_stmt|;
block|}
name|matches
operator|.
name|add
argument_list|(
name|match
argument_list|)
expr_stmt|;
block|}
name|_init
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/** 	 * {@inheritDoc} 	 */
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|boolean
name|accept
init|=
literal|false
decl_stmt|;
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|int
name|bracketIndex
init|=
name|revision
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
decl_stmt|;
if|if
condition|(
name|bracketIndex
operator|>
literal|0
condition|)
block|{
name|revision
operator|=
name|revision
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|bracketIndex
argument_list|)
expr_stmt|;
block|}
name|List
name|matches
init|=
operator|(
name|List
operator|)
name|_RevisionMatches
operator|.
name|get
argument_list|(
name|revision
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|matches
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|accept
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Match
name|match
init|=
operator|(
name|Match
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Matcher
name|matcher
init|=
name|match
operator|.
name|getPatternMatcher
argument_list|(
name|askedMrid
argument_list|)
decl_stmt|;
name|accept
operator|=
name|matcher
operator|.
name|matches
argument_list|(
name|foundMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|accept
return|;
block|}
comment|/** 	 * {@inheritDoc} 	 */
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
name|init
argument_list|()
expr_stmt|;
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|int
name|bracketIndex
init|=
name|revision
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
decl_stmt|;
if|if
condition|(
name|bracketIndex
operator|>
literal|0
condition|)
block|{
name|revision
operator|=
name|revision
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|bracketIndex
argument_list|)
expr_stmt|;
block|}
return|return
name|_RevisionMatches
operator|.
name|containsKey
argument_list|(
name|revision
argument_list|)
return|;
block|}
block|}
end_class

end_unit

