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
name|settings
package|;
end_package

begin_comment
comment|/**  * Represents the timeouts that are applicable while dealing with resources.  *<p>  * An example of its usage is  * {@link org.apache.ivy.plugins.resolver.DependencyResolver dependency resolvers}  * when they are resolving module descriptor and/or are downloading the artifacts.  */
end_comment

begin_interface
specifier|public
interface|interface
name|TimeoutConstraint
block|{
comment|/**      * @return Returns the timeout, in milliseconds, that's to be used while establishing a      * connection to a resource. A value greater than zero indicates the specific timeout to be      * used. A value of 0 indicates no timeout and essentially translates to wait-forever      * semantics. A value lesser than 0 lets the users of this {@link TimeoutConstraint}      * decide what kind of timeout semantics to use while establishing a connection (for example,      * some implementations can decide to use some default value).      */
name|int
name|getConnectionTimeout
parameter_list|()
function_decl|;
comment|/**      * @return Returns the timeout, in milliseconds, that's to be used while reading content from      * a resource. A value greater than zero indicates the specific timeout to be used. A value of      * 0 indicates no timeout and essentially translates to wait-forever semantics. A value lesser      * than 0 lets the users of this {@link TimeoutConstraint} decide what kind of timeout      * semantics to use reading from the resource (for example, some implementations can decide to      * use some default value).      */
name|int
name|getReadTimeout
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

