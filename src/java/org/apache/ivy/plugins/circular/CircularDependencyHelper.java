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
name|circular
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|module
operator|.
name|descriptor
operator|.
name|ModuleDescriptor
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

begin_class
specifier|public
specifier|final
class|class
name|CircularDependencyHelper
block|{
comment|/** CircularDependencyHelper is not designed to be an instance */
specifier|private
name|CircularDependencyHelper
parameter_list|()
block|{
block|}
comment|/**      * Returns a string representation of this circular dependency graph      *      * @param mrids      *            in order of circular dependency      * @return a string representation of this circular dependency graph      */
specifier|public
specifier|static
name|String
name|formatMessage
parameter_list|(
specifier|final
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
name|Set
argument_list|<
name|ModuleRevisionId
argument_list|>
name|alreadyAdded
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|buff
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleRevisionId
name|mrid
range|:
name|mrids
control|)
block|{
if|if
condition|(
name|buff
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buff
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|alreadyAdded
operator|.
name|add
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
name|buff
operator|.
name|append
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buff
operator|.
name|append
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|buff
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|formatMessage
parameter_list|(
specifier|final
name|ModuleDescriptor
index|[]
name|descriptors
parameter_list|)
block|{
return|return
name|formatMessageFromDescriptors
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|descriptors
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param loopElements      *            a List&lt;ModuleDescriptor&gt;      * @return String      */
specifier|public
specifier|static
name|String
name|formatMessageFromDescriptors
parameter_list|(
name|List
argument_list|<
name|ModuleDescriptor
argument_list|>
name|loopElements
parameter_list|)
block|{
name|List
argument_list|<
name|ModuleRevisionId
argument_list|>
name|mrids
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ModuleDescriptor
name|descriptor
range|:
name|loopElements
control|)
block|{
name|mrids
operator|.
name|add
argument_list|(
name|descriptor
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|formatMessage
argument_list|(
name|mrids
operator|.
name|toArray
argument_list|(
operator|new
name|ModuleRevisionId
index|[
name|mrids
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

