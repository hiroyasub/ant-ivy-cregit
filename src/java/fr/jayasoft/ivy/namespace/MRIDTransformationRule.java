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
name|namespace
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
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|MRIDTransformationRule
implements|implements
name|NamespaceTransformer
block|{
specifier|private
specifier|static
class|class
name|MridRuleMatcher
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|TYPES
init|=
operator|new
name|String
index|[]
block|{
literal|"o"
block|,
literal|"m"
block|,
literal|"b"
block|,
literal|"r"
block|}
decl_stmt|;
specifier|private
name|Matcher
index|[]
name|_matchers
init|=
operator|new
name|Matcher
index|[
literal|4
index|]
decl_stmt|;
specifier|public
name|boolean
name|match
parameter_list|(
name|MRIDRule
name|src
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|_matchers
index|[
literal|0
index|]
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|getPattern
argument_list|(
name|src
operator|.
name|getOrg
argument_list|()
argument_list|)
argument_list|)
operator|.
name|matcher
argument_list|(
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_matchers
index|[
literal|0
index|]
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|_matchers
index|[
literal|1
index|]
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|getPattern
argument_list|(
name|src
operator|.
name|getModule
argument_list|()
argument_list|)
argument_list|)
operator|.
name|matcher
argument_list|(
name|mrid
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_matchers
index|[
literal|1
index|]
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|mrid
operator|.
name|getBranch
argument_list|()
operator|==
literal|null
condition|)
block|{
name|_matchers
index|[
literal|2
index|]
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|_matchers
index|[
literal|2
index|]
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|getPattern
argument_list|(
name|src
operator|.
name|getBranch
argument_list|()
argument_list|)
argument_list|)
operator|.
name|matcher
argument_list|(
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_matchers
index|[
literal|2
index|]
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|_matchers
index|[
literal|3
index|]
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|getPattern
argument_list|(
name|src
operator|.
name|getRev
argument_list|()
argument_list|)
argument_list|)
operator|.
name|matcher
argument_list|(
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_matchers
index|[
literal|3
index|]
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|ModuleRevisionId
name|apply
parameter_list|(
name|MRIDRule
name|dest
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|String
name|org
init|=
name|applyRules
argument_list|(
name|dest
operator|.
name|getOrg
argument_list|()
argument_list|,
literal|"o"
argument_list|)
decl_stmt|;
name|String
name|mod
init|=
name|applyRules
argument_list|(
name|dest
operator|.
name|getModule
argument_list|()
argument_list|,
literal|"m"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|applyRules
argument_list|(
name|dest
operator|.
name|getBranch
argument_list|()
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|String
name|rev
init|=
name|applyRules
argument_list|(
name|dest
operator|.
name|getRev
argument_list|()
argument_list|,
literal|"r"
argument_list|)
decl_stmt|;
return|return
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|mod
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|,
name|mrid
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|applyRules
parameter_list|(
name|String
name|str
parameter_list|,
name|String
name|type
parameter_list|)
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
name|TYPES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|str
operator|=
name|applyTypeRule
argument_list|(
name|str
argument_list|,
name|TYPES
index|[
name|i
index|]
argument_list|,
name|type
argument_list|,
name|_matchers
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
specifier|private
name|String
name|applyTypeRule
parameter_list|(
name|String
name|rule
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ruleType
parameter_list|,
name|Matcher
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
return|return
name|rule
return|;
block|}
name|String
name|res
init|=
name|rule
operator|==
literal|null
condition|?
literal|"$"
operator|+
name|ruleType
operator|+
literal|"0"
else|:
name|rule
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
name|TYPES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|TYPES
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|res
operator|=
name|res
operator|.
name|replaceAll
argument_list|(
literal|"([^\\\\])\\$"
operator|+
name|type
argument_list|,
literal|"$1\\$"
argument_list|)
expr_stmt|;
name|res
operator|=
name|res
operator|.
name|replaceAll
argument_list|(
literal|"^\\$"
operator|+
name|type
argument_list|,
literal|"\\$"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|=
name|res
operator|.
name|replaceAll
argument_list|(
literal|"([^\\\\])\\$"
operator|+
name|TYPES
index|[
name|i
index|]
argument_list|,
literal|"$1\\\\\\$"
operator|+
name|TYPES
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|res
operator|=
name|res
operator|.
name|replaceAll
argument_list|(
literal|"^\\$"
operator|+
name|TYPES
index|[
name|i
index|]
argument_list|,
literal|"\\\\\\$"
operator|+
name|TYPES
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|m
operator|.
name|reset
argument_list|()
expr_stmt|;
name|m
operator|.
name|find
argument_list|()
expr_stmt|;
name|m
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// null rule not replaced, let it be null
if|if
condition|(
name|rule
operator|==
literal|null
operator|&&
operator|(
literal|"$"
operator|+
name|ruleType
operator|+
literal|"0"
operator|)
operator|.
name|equals
argument_list|(
name|str
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|str
return|;
block|}
specifier|private
name|String
name|getPattern
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|==
literal|null
condition|?
literal|".*"
else|:
name|p
return|;
block|}
block|}
specifier|private
name|List
name|_src
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|MRIDRule
name|_dest
decl_stmt|;
specifier|public
name|void
name|addSrc
parameter_list|(
name|MRIDRule
name|src
parameter_list|)
block|{
name|_src
operator|.
name|add
argument_list|(
name|src
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDest
parameter_list|(
name|MRIDRule
name|dest
parameter_list|)
block|{
if|if
condition|(
name|_dest
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"only one dest is allowed per mapping"
argument_list|)
throw|;
block|}
name|_dest
operator|=
name|dest
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|transform
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|MridRuleMatcher
name|matcher
init|=
operator|new
name|MridRuleMatcher
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_src
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
name|MRIDRule
name|rule
init|=
operator|(
name|MRIDRule
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|match
argument_list|(
name|rule
argument_list|,
name|mrid
argument_list|)
condition|)
block|{
name|ModuleRevisionId
name|destMrid
init|=
name|matcher
operator|.
name|apply
argument_list|(
name|_dest
argument_list|,
name|mrid
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"found matching namespace rule: "
operator|+
name|rule
operator|+
literal|". Applied "
operator|+
name|_dest
operator|+
literal|" on "
operator|+
name|mrid
operator|+
literal|". Transformed to "
operator|+
name|destMrid
argument_list|)
expr_stmt|;
return|return
name|destMrid
return|;
block|}
block|}
return|return
name|mrid
return|;
block|}
specifier|public
name|boolean
name|isIdentity
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

