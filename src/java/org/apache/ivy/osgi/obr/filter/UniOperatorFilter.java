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
name|osgi
operator|.
name|obr
operator|.
name|filter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|RequirementFilter
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|UniOperatorFilter
extends|extends
name|RequirementFilter
block|{
specifier|private
specifier|final
name|RequirementFilter
name|subFilter
decl_stmt|;
specifier|public
name|UniOperatorFilter
parameter_list|(
name|RequirementFilter
name|subFilter
parameter_list|)
block|{
name|this
operator|.
name|subFilter
operator|=
name|subFilter
expr_stmt|;
block|}
specifier|abstract
specifier|protected
name|char
name|operator
parameter_list|()
function_decl|;
specifier|public
name|void
name|append
parameter_list|(
name|StringBuffer
name|builder
parameter_list|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|operator
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|subFilter
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RequirementFilter
name|getSubFilter
parameter_list|()
block|{
return|return
name|subFilter
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|subFilter
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|subFilter
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|UniOperatorFilter
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|UniOperatorFilter
name|other
init|=
operator|(
name|UniOperatorFilter
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|subFilter
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|subFilter
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|subFilter
operator|.
name|equals
argument_list|(
name|other
operator|.
name|subFilter
argument_list|)
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
block|}
end_class

end_unit

