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
name|HashMap
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
name|CommandLine
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|optionValues
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
index|[]
name|leftOverArgs
decl_stmt|;
name|void
name|addOptionValues
parameter_list|(
name|String
name|option
parameter_list|,
name|String
index|[]
name|values
parameter_list|)
block|{
name|optionValues
operator|.
name|put
argument_list|(
name|option
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
name|void
name|setLeftOverArgs
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|leftOverArgs
operator|=
name|args
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasOption
parameter_list|(
name|String
name|option
parameter_list|)
block|{
return|return
name|optionValues
operator|.
name|containsKey
argument_list|(
name|option
argument_list|)
return|;
block|}
specifier|public
name|String
name|getOptionValue
parameter_list|(
name|String
name|option
parameter_list|)
block|{
name|String
index|[]
name|values
init|=
name|getOptionValues
argument_list|(
name|option
argument_list|)
decl_stmt|;
return|return
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|length
operator|==
literal|0
condition|?
literal|null
else|:
name|values
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|String
name|getOptionValue
parameter_list|(
name|String
name|option
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|value
init|=
name|getOptionValue
argument_list|(
name|option
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
name|defaultValue
else|:
name|value
return|;
block|}
specifier|public
name|String
index|[]
name|getOptionValues
parameter_list|(
name|String
name|option
parameter_list|)
block|{
return|return
name|optionValues
operator|.
name|get
argument_list|(
name|option
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getLeftOverArgs
parameter_list|()
block|{
return|return
name|leftOverArgs
return|;
block|}
block|}
end_class

end_unit

