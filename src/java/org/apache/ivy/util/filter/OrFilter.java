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
name|util
operator|.
name|filter
package|;
end_package

begin_class
specifier|public
class|class
name|OrFilter
implements|implements
name|Filter
block|{
specifier|private
name|Filter
name|op1
decl_stmt|;
specifier|private
name|Filter
name|op2
decl_stmt|;
specifier|public
name|OrFilter
parameter_list|(
name|Filter
name|op1
parameter_list|,
name|Filter
name|op2
parameter_list|)
block|{
name|this
operator|.
name|op1
operator|=
name|op1
expr_stmt|;
name|this
operator|.
name|op2
operator|=
name|op2
expr_stmt|;
block|}
specifier|public
name|Filter
name|getOp1
parameter_list|()
block|{
return|return
name|op1
return|;
block|}
specifier|public
name|Filter
name|getOp2
parameter_list|()
block|{
return|return
name|op2
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|op1
operator|.
name|accept
argument_list|(
name|o
argument_list|)
operator|||
name|op2
operator|.
name|accept
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
end_class

end_unit

