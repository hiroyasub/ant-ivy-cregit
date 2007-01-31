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

begin_class
specifier|public
class|class
name|StrictConflictManager
extends|extends
name|AbstractConflictManager
block|{
specifier|public
name|StrictConflictManager
parameter_list|()
block|{
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
name|lastNode
operator|.
name|equals
argument_list|(
name|node
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
name|lastNode
operator|+
literal|" (needed by "
operator|+
name|lastNode
operator|.
name|getParent
argument_list|()
operator|+
literal|") conflicts with "
operator|+
name|node
operator|+
literal|" (needed by "
operator|+
name|node
operator|.
name|getParent
argument_list|()
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
name|lastNode
operator|=
name|node
expr_stmt|;
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
block|}
end_class

end_unit

