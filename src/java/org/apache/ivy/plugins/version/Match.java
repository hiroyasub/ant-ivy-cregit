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
name|StringTokenizer
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
name|IvyContext
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
name|PatternMatcher
import|;
end_import

begin_comment
comment|/**  *   * @author Maarten Coene  */
end_comment

begin_class
specifier|public
class|class
name|Match
block|{
specifier|private
name|String
name|_revision
decl_stmt|;
specifier|private
name|String
name|_pattern
decl_stmt|;
specifier|private
name|String
name|_args
decl_stmt|;
specifier|private
name|String
name|_matcher
decl_stmt|;
specifier|public
name|String
name|getArgs
parameter_list|()
block|{
return|return
name|_args
return|;
block|}
specifier|public
name|void
name|setArgs
parameter_list|(
name|String
name|args
parameter_list|)
block|{
name|this
operator|.
name|_args
operator|=
name|args
expr_stmt|;
block|}
specifier|public
name|String
name|getMatcher
parameter_list|()
block|{
return|return
name|_matcher
return|;
block|}
specifier|public
name|void
name|setMatcher
parameter_list|(
name|String
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|_matcher
operator|=
name|matcher
expr_stmt|;
block|}
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|_pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|_pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_revision
return|;
block|}
specifier|public
name|void
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|_revision
operator|=
name|revision
expr_stmt|;
block|}
specifier|public
name|Matcher
name|getPatternMatcher
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
name|split
argument_list|(
name|getArgs
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|argValues
init|=
name|getRevisionArgs
argument_list|(
name|revision
argument_list|)
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|!=
name|argValues
operator|.
name|length
condition|)
block|{
return|return
operator|new
name|NoMatchMatcher
argument_list|()
return|;
block|}
name|Map
name|variables
init|=
operator|new
name|HashMap
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|variables
operator|.
name|put
argument_list|(
name|args
index|[
name|i
index|]
argument_list|,
name|argValues
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|String
name|pattern
init|=
name|getPattern
argument_list|()
decl_stmt|;
name|pattern
operator|=
name|IvyPatternHelper
operator|.
name|substituteVariables
argument_list|(
name|pattern
argument_list|,
name|variables
argument_list|)
expr_stmt|;
name|PatternMatcher
name|pMatcher
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getMatcher
argument_list|(
name|_matcher
argument_list|)
decl_stmt|;
return|return
name|pMatcher
operator|.
name|getMatcher
argument_list|(
name|pattern
argument_list|)
return|;
block|}
specifier|private
name|String
index|[]
name|getRevisionArgs
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|int
name|bracketStartIndex
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
name|bracketStartIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
name|int
name|bracketEndIndex
init|=
name|revision
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
decl_stmt|;
if|if
condition|(
name|bracketEndIndex
operator|<=
operator|(
name|bracketStartIndex
operator|+
literal|1
operator|)
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
name|String
name|args
init|=
name|revision
operator|.
name|substring
argument_list|(
name|bracketStartIndex
operator|+
literal|1
argument_list|,
name|bracketEndIndex
argument_list|)
decl_stmt|;
return|return
name|split
argument_list|(
name|args
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
index|[]
name|split
parameter_list|(
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|string
argument_list|,
literal|", "
argument_list|)
decl_stmt|;
name|List
name|tokens
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|tokenizer
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|tokens
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|tokens
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|NoMatchMatcher
implements|implements
name|Matcher
block|{
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

