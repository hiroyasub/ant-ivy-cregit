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

begin_comment
comment|/**  * An abstract base class to ease {@link MessageLogger} implementation.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMessageLogger
implements|implements
name|MessageLogger
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|warns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|showProgress
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|msg
argument_list|,
name|Message
operator|.
name|MSG_DEBUG
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|verbose
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|msg
argument_list|,
name|Message
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deprecated
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
literal|"DEPRECATED: "
operator|+
name|msg
argument_list|,
name|Message
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
name|msg
argument_list|,
name|Message
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|rawinfo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|rawlog
argument_list|(
name|msg
argument_list|,
name|Message
operator|.
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|log
argument_list|(
literal|"WARN: "
operator|+
name|msg
argument_list|,
name|Message
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|problems
operator|.
name|add
argument_list|(
literal|"WARN:  "
operator|+
name|msg
argument_list|)
expr_stmt|;
name|getWarns
argument_list|()
operator|.
name|add
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
comment|// log in verbose mode because message is appended as a problem, and will be
comment|// logged at the end at error level
name|log
argument_list|(
literal|"ERROR: "
operator|+
name|msg
argument_list|,
name|Message
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|problems
operator|.
name|add
argument_list|(
literal|"\tERROR: "
operator|+
name|msg
argument_list|)
expr_stmt|;
name|getErrors
argument_list|()
operator|.
name|add
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProblems
parameter_list|()
block|{
return|return
name|problems
return|;
block|}
specifier|public
name|void
name|sumupProblems
parameter_list|()
block|{
name|MessageLoggerHelper
operator|.
name|sumupProblems
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|clearProblems
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearProblems
parameter_list|()
block|{
name|problems
operator|.
name|clear
argument_list|()
expr_stmt|;
name|warns
operator|.
name|clear
argument_list|()
expr_stmt|;
name|errors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getWarns
parameter_list|()
block|{
return|return
name|warns
return|;
block|}
specifier|public
name|void
name|progress
parameter_list|()
block|{
if|if
condition|(
name|showProgress
condition|)
block|{
name|doProgress
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|endProgress
parameter_list|()
block|{
name|endProgress
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|endProgress
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|showProgress
condition|)
block|{
name|doEndProgress
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isShowProgress
parameter_list|()
block|{
return|return
name|showProgress
return|;
block|}
specifier|public
name|void
name|setShowProgress
parameter_list|(
name|boolean
name|progress
parameter_list|)
block|{
name|showProgress
operator|=
name|progress
expr_stmt|;
block|}
comment|/**      * Indicates a progression for a long running task      */
specifier|protected
specifier|abstract
name|void
name|doProgress
parameter_list|()
function_decl|;
comment|/**      * Indicates the end of a long running task      *      * @param msg      *            the message associated with long running task end.      */
specifier|protected
specifier|abstract
name|void
name|doEndProgress
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
block|}
end_class

end_unit

