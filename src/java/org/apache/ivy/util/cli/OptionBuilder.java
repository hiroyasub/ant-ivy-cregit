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
name|cli
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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|OptionBuilder
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|List
comment|/*<String> */
name|args
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|""
decl_stmt|;
specifier|private
name|boolean
name|required
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|countArgs
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|deprecated
init|=
literal|false
decl_stmt|;
specifier|public
name|OptionBuilder
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
name|OptionBuilder
name|required
parameter_list|(
name|boolean
name|required
parameter_list|)
block|{
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|OptionBuilder
name|description
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
return|return
name|this
return|;
block|}
specifier|public
name|OptionBuilder
name|arg
parameter_list|(
name|String
name|argName
parameter_list|)
block|{
name|this
operator|.
name|args
operator|.
name|add
argument_list|(
name|argName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|OptionBuilder
name|countArgs
parameter_list|(
name|boolean
name|countArgs
parameter_list|)
block|{
name|this
operator|.
name|countArgs
operator|=
name|countArgs
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|OptionBuilder
name|deprecated
parameter_list|()
block|{
name|this
operator|.
name|deprecated
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Option
name|create
parameter_list|()
block|{
return|return
operator|new
name|Option
argument_list|(
name|name
argument_list|,
operator|(
name|String
index|[]
operator|)
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|description
argument_list|,
name|required
argument_list|,
name|countArgs
argument_list|,
name|deprecated
argument_list|)
return|;
block|}
block|}
end_class

end_unit

