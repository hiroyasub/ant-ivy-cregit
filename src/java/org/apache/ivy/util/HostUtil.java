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
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_comment
comment|/**  * This class contains basic helper methods for the Host.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HostUtil
block|{
specifier|private
specifier|static
name|String
name|localHostName
init|=
literal|null
decl_stmt|;
comment|/**      * This default constructor is to hide this class from initialization through other classes.      */
specifier|private
name|HostUtil
parameter_list|()
block|{
block|}
comment|/**      * This method returns the name of the current Host, if this name cannot be determined,      * localhost will be returned.      *       * @return The name of the current "local" Host.      */
specifier|public
specifier|static
name|String
name|getLocalHostName
parameter_list|()
block|{
if|if
condition|(
name|localHostName
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|localHostName
operator|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
name|localHostName
operator|=
literal|"localhost"
expr_stmt|;
block|}
block|}
return|return
name|localHostName
return|;
block|}
block|}
end_class

end_unit

