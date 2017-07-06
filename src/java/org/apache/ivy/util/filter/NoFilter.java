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
specifier|final
class|class
name|NoFilter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Filter
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
specifier|static
specifier|final
name|Filter
name|INSTANCE
init|=
operator|new
name|NoFilter
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Filter
argument_list|<
name|T
argument_list|>
name|instance
parameter_list|()
block|{
return|return
operator|(
name|Filter
argument_list|<
name|T
argument_list|>
operator|)
name|INSTANCE
return|;
block|}
specifier|private
name|NoFilter
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|T
name|o
parameter_list|)
block|{
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
literal|"NoFilter"
return|;
block|}
block|}
end_class

end_unit

