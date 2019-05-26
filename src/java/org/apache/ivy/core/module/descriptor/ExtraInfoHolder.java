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
name|core
operator|.
name|module
operator|.
name|descriptor
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
name|LinkedHashMap
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
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|ExtraInfoHolder
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|content
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ExtraInfoHolder
argument_list|>
name|nestedExtraInfoHolder
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ExtraInfoHolder
parameter_list|()
block|{
block|}
specifier|public
name|ExtraInfoHolder
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
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
name|attributes
return|;
block|}
specifier|public
name|void
name|setAttributes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
name|attributes
expr_stmt|;
block|}
specifier|public
name|String
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
specifier|public
name|void
name|setContent
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ExtraInfoHolder
argument_list|>
name|getNestedExtraInfoHolder
parameter_list|()
block|{
return|return
name|nestedExtraInfoHolder
return|;
block|}
specifier|public
name|void
name|setNestedExtraInfoHolder
parameter_list|(
name|List
argument_list|<
name|ExtraInfoHolder
argument_list|>
name|nestedExtraInfoHolder
parameter_list|)
block|{
name|this
operator|.
name|nestedExtraInfoHolder
operator|=
name|nestedExtraInfoHolder
expr_stmt|;
block|}
block|}
end_class

end_unit

