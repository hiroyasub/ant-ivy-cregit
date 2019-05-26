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
name|plugins
operator|.
name|resolver
package|;
end_package

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
name|plugins
operator|.
name|repository
operator|.
name|vfs
operator|.
name|VfsRepository
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|VfsResolver
extends|extends
name|RepositoryResolver
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|URL_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[a-z]*://(.+):(.+)@.*"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PASSWORD_GROUP
init|=
literal|2
decl_stmt|;
specifier|public
name|VfsResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|VfsRepository
argument_list|(
operator|new
name|LazyTimeoutConstraint
argument_list|(
name|this
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"vfs"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|hidePassword
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|prepareForDisplay
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|prepareForDisplay
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|URL_PATTERN
operator|.
name|matcher
argument_list|(
name|s
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
specifier|final
name|String
name|password
init|=
name|m
operator|.
name|group
argument_list|(
name|PASSWORD_GROUP
argument_list|)
decl_stmt|;
specifier|final
name|int
name|passwordposi
init|=
name|s
operator|.
name|indexOf
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|StringBuilder
name|stars
init|=
operator|new
name|StringBuilder
argument_list|(
name|password
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|posi
init|=
literal|0
init|;
name|posi
operator|<
name|password
operator|.
name|length
argument_list|()
condition|;
name|posi
operator|++
control|)
block|{
name|stars
operator|.
name|setCharAt
argument_list|(
name|posi
argument_list|,
literal|'*'
argument_list|)
expr_stmt|;
block|}
name|String
name|replacement
init|=
name|stars
operator|.
name|toString
argument_list|()
decl_stmt|;
name|s
operator|=
name|s
operator|.
name|replace
argument_list|(
name|passwordposi
argument_list|,
name|passwordposi
operator|+
name|password
operator|.
name|length
argument_list|()
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

