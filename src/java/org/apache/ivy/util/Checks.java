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

begin_comment
comment|/**  * Utility class used to perform some checks.  */
end_comment

begin_class
specifier|public
class|class
name|Checks
block|{
comment|/**      * Checks that an object is not null, and throw an exception if the object is null.      *       * @param o      *            the object to check      * @param objectName      *            the name of the object to check. This name will be used in the exception message.      * @throws IllegalArgumentException      *             if the object is null      */
specifier|public
specifier|static
name|void
name|checkNotNull
parameter_list|(
name|Object
name|o
parameter_list|,
name|String
name|objectName
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|objectName
operator|+
literal|" must not be null"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

