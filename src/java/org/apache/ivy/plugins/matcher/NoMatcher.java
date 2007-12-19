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

begin_comment
comment|/**  * A matcher that matches nothing.  */
end_comment

begin_class
specifier|public
specifier|final
comment|/* @Immutable */
class|class
name|NoMatcher
implements|implements
name|Matcher
block|{
specifier|public
specifier|static
specifier|final
name|Matcher
name|INSTANCE
init|=
operator|new
name|NoMatcher
argument_list|()
decl_stmt|;
specifier|public
name|NoMatcher
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|matches
parameter_list|(
name|String
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isExact
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

