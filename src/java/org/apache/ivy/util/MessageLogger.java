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
name|List
import|;
end_import

begin_comment
comment|/**  * A MessageLogger is used to log messages.  *<p>  * Where the messages are logged is depending on the implementation.  *</p>  *<p>  * This interface provides both level specific methods ({@link #info(String)}, {@link #warn(String)}  * , ...) and generic methods ({@link #log(String, int)}, {@link #rawlog(String, int)}). Note that  * calling level specific methods is usually not equivalent to calling the generic method with the  * corresponding level. Indeed, for warn and error level, the implementation will actually log the  * message at a lower level (usually {@link Message#MSG_VERBOSE}) and log the message at the actual  * level only when {@link #sumupProblems()} is called.  *</p>  *   * @see Message  */
end_comment

begin_interface
specifier|public
interface|interface
name|MessageLogger
block|{
comment|/**      * Logs a message at the given level.      *<p>      *<code>level</code> constants are defined in the {@link Message} class.      *</p>      *       * @param msg      *            the message to log      * @param level      *            the level at which the message should be logged.      * @see Message#MSG_DEBUG      * @see Message#MSG_VERBOSE      * @see Message#MSG_INFO      * @see Message#MSG_WARN      * @see Message#MSG_ERR      */
specifier|public
specifier|abstract
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
function_decl|;
comment|/**      * Same as {@link #log(String, int)}, but without adding any contextual information to the      * message.      *       * @param msg      *            the message to log      * @param level      *            the level at which the message should be logged.      */
specifier|public
specifier|abstract
name|void
name|rawlog
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|verbose
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|deprecated
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|rawinfo
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|getProblems
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|getWarns
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|getErrors
parameter_list|()
function_decl|;
comment|/**      * Clears the list of problems, warns and errors.      */
specifier|public
specifier|abstract
name|void
name|clearProblems
parameter_list|()
function_decl|;
comment|/**      * Sumup all problems encountered so far, and clear them.      */
specifier|public
specifier|abstract
name|void
name|sumupProblems
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|progress
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|endProgress
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|endProgress
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|isShowProgress
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|setShowProgress
parameter_list|(
name|boolean
name|progress
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

