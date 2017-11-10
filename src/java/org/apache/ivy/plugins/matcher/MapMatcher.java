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
name|matcher
package|;
end_package

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
name|HashMap
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

begin_class
specifier|public
class|class
name|MapMatcher
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Matcher
argument_list|>
name|matchers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|PatternMatcher
name|pm
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
decl_stmt|;
specifier|public
name|MapMatcher
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
parameter_list|,
name|PatternMatcher
name|pm
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
name|this
operator|.
name|pm
operator|=
name|pm
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|matchers
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|pm
operator|.
name|getMatcher
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Matcher
argument_list|>
name|entry
range|:
name|matchers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Matcher
name|matcher
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|m
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|value
operator|==
literal|null
operator|)
operator|||
operator|!
name|matcher
operator|.
name|matches
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|attributes
operator|+
literal|" ("
operator|+
name|pm
operator|.
name|getName
argument_list|()
operator|+
literal|")"
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|attributes
argument_list|)
return|;
block|}
specifier|public
name|PatternMatcher
name|getPatternMatcher
parameter_list|()
block|{
return|return
name|pm
return|;
block|}
block|}
end_class

end_unit

