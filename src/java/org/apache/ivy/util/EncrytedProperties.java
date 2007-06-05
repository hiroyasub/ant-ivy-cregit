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
name|Collection
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
name|Properties
import|;
end_import

begin_comment
comment|/**  * An implementation of Properties which stores the values encrypted. The use is transparent from  * the user point of view (use as any Properties instance), except that get, put and putAll do not  * handle encryption/decryption. This means that get returns the encrypted value, while put and  * putAll puts given values without encrypting them. It this thus recommended to void using them,  * use setProperty and getProperty instead.  */
end_comment

begin_class
specifier|public
class|class
name|EncrytedProperties
extends|extends
name|Properties
block|{
specifier|public
name|EncrytedProperties
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|Object
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|decrypt
argument_list|(
operator|(
name|String
operator|)
name|super
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|StringUtils
operator|.
name|encrypt
argument_list|(
name|value
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|decrypt
argument_list|(
name|super
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|decrypt
argument_list|(
name|super
operator|.
name|getProperty
argument_list|(
name|key
argument_list|,
name|StringUtils
operator|.
name|encrypt
argument_list|(
name|defaultValue
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|containsValue
argument_list|(
name|StringUtils
operator|.
name|encrypt
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|boolean
name|contains
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|contains
argument_list|(
name|StringUtils
operator|.
name|encrypt
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Collection
name|values
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|super
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ret
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ret
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|StringUtils
operator|.
name|decrypt
argument_list|(
operator|(
name|String
operator|)
name|ret
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

