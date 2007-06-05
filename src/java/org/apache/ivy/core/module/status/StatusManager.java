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
name|core
operator|.
name|module
operator|.
name|status
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
name|Arrays
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
name|ListIterator
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
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Note: update methods (such as addStatus) should only be called BEFORE any call to accessor methods  *  */
end_comment

begin_class
specifier|public
class|class
name|StatusManager
block|{
specifier|public
specifier|static
name|StatusManager
name|newDefaultInstance
parameter_list|()
block|{
return|return
operator|new
name|StatusManager
argument_list|(
operator|new
name|Status
index|[]
block|{
operator|new
name|Status
argument_list|(
literal|"release"
argument_list|,
literal|false
argument_list|)
block|,
operator|new
name|Status
argument_list|(
literal|"milestone"
argument_list|,
literal|false
argument_list|)
block|,
operator|new
name|Status
argument_list|(
literal|"integration"
argument_list|,
literal|true
argument_list|)
block|}
argument_list|,
literal|"integration"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|StatusManager
name|getCurrent
parameter_list|()
block|{
return|return
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getStatusManager
argument_list|()
return|;
block|}
specifier|private
name|List
name|status
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|defaultStatus
decl_stmt|;
comment|// for easier querying only
specifier|private
name|Map
name|statusPriorityMap
decl_stmt|;
specifier|private
name|Map
name|statusIntegrationMap
decl_stmt|;
specifier|private
name|String
name|deliveryStatusListString
decl_stmt|;
specifier|public
name|StatusManager
parameter_list|(
name|Status
index|[]
name|status
parameter_list|,
name|String
name|defaultStatus
parameter_list|)
block|{
name|this
operator|.
name|status
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|status
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultStatus
operator|=
name|defaultStatus
expr_stmt|;
name|computeMaps
argument_list|()
expr_stmt|;
block|}
specifier|public
name|StatusManager
parameter_list|()
block|{
block|}
specifier|public
name|void
name|addStatus
parameter_list|(
name|Status
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|.
name|add
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultStatus
parameter_list|(
name|String
name|defaultStatus
parameter_list|)
block|{
name|this
operator|.
name|defaultStatus
operator|=
name|defaultStatus
expr_stmt|;
block|}
specifier|public
name|List
name|getStatuses
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|private
name|void
name|computeMaps
parameter_list|()
block|{
if|if
condition|(
name|status
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"badly configured statuses: no status found"
argument_list|)
throw|;
block|}
name|statusPriorityMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|ListIterator
name|iter
init|=
name|status
operator|.
name|listIterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Status
name|status
init|=
operator|(
name|Status
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|statusPriorityMap
operator|.
name|put
argument_list|(
name|status
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Integer
argument_list|(
name|iter
operator|.
name|previousIndex
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|statusIntegrationMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|status
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
name|Status
name|status
init|=
operator|(
name|Status
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|statusIntegrationMap
operator|.
name|put
argument_list|(
name|status
operator|.
name|getName
argument_list|()
argument_list|,
name|Boolean
operator|.
name|valueOf
argument_list|(
name|status
operator|.
name|isIntegration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
if|if
condition|(
name|statusPriorityMap
operator|==
literal|null
condition|)
block|{
name|computeMaps
argument_list|()
expr_stmt|;
block|}
return|return
name|statusPriorityMap
operator|.
name|containsKey
argument_list|(
name|status
argument_list|)
return|;
block|}
specifier|public
name|int
name|getPriority
parameter_list|(
name|String
name|status
parameter_list|)
block|{
if|if
condition|(
name|statusPriorityMap
operator|==
literal|null
condition|)
block|{
name|computeMaps
argument_list|()
expr_stmt|;
block|}
name|Integer
name|priority
init|=
operator|(
name|Integer
operator|)
name|statusPriorityMap
operator|.
name|get
argument_list|(
name|status
argument_list|)
decl_stmt|;
if|if
condition|(
name|priority
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"unknown status "
operator|+
name|status
operator|+
literal|": assuming lowest priority"
argument_list|)
expr_stmt|;
return|return
name|Integer
operator|.
name|MAX_VALUE
return|;
block|}
return|return
name|priority
operator|.
name|intValue
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isIntegration
parameter_list|(
name|String
name|status
parameter_list|)
block|{
if|if
condition|(
name|statusIntegrationMap
operator|==
literal|null
condition|)
block|{
name|computeMaps
argument_list|()
expr_stmt|;
block|}
name|Boolean
name|isIntegration
init|=
operator|(
name|Boolean
operator|)
name|statusIntegrationMap
operator|.
name|get
argument_list|(
name|status
argument_list|)
decl_stmt|;
if|if
condition|(
name|isIntegration
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"unknown status "
operator|+
name|status
operator|+
literal|": assuming integration"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
name|isIntegration
operator|.
name|booleanValue
argument_list|()
return|;
block|}
specifier|public
name|String
name|getDeliveryStatusListString
parameter_list|()
block|{
if|if
condition|(
name|deliveryStatusListString
operator|==
literal|null
condition|)
block|{
name|StringBuffer
name|ret
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|status
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
name|Status
name|status
init|=
operator|(
name|Status
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|status
operator|.
name|isIntegration
argument_list|()
condition|)
block|{
name|ret
operator|.
name|append
argument_list|(
name|status
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ret
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ret
operator|.
name|deleteCharAt
argument_list|(
name|ret
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|deliveryStatusListString
operator|=
name|ret
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|deliveryStatusListString
return|;
block|}
specifier|public
name|String
name|getDefaultStatus
parameter_list|()
block|{
if|if
condition|(
name|defaultStatus
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|status
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"badly configured statuses: no status found"
argument_list|)
throw|;
block|}
name|defaultStatus
operator|=
operator|(
operator|(
name|Status
operator|)
name|status
operator|.
name|get
argument_list|(
name|status
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|defaultStatus
return|;
block|}
block|}
end_class

end_unit

