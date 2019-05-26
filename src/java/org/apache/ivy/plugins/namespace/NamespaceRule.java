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
name|namespace
package|;
end_package

begin_class
specifier|public
class|class
name|NamespaceRule
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|MRIDTransformationRule
name|fromSystem
decl_stmt|;
specifier|private
name|MRIDTransformationRule
name|toSystem
decl_stmt|;
specifier|public
name|MRIDTransformationRule
name|getFromSystem
parameter_list|()
block|{
return|return
name|fromSystem
return|;
block|}
specifier|public
name|void
name|addFromsystem
parameter_list|(
name|MRIDTransformationRule
name|fromSystem
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|fromSystem
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"only one fromsystem is allowed per rule"
argument_list|)
throw|;
block|}
name|this
operator|.
name|fromSystem
operator|=
name|fromSystem
expr_stmt|;
block|}
specifier|public
name|MRIDTransformationRule
name|getToSystem
parameter_list|()
block|{
return|return
name|toSystem
return|;
block|}
specifier|public
name|void
name|addTosystem
parameter_list|(
name|MRIDTransformationRule
name|toSystem
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|toSystem
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"only one tosystem is allowed per rule"
argument_list|)
throw|;
block|}
name|this
operator|.
name|toSystem
operator|=
name|toSystem
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

