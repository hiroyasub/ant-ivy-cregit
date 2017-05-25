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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_comment
comment|/**  * A {@link MessageLogger} implementation delegating the work to the current top logger on a stack.  *<p>  * When the logger stack is empty, it delegates the work to a default logger, which by default is  * the {@link Message#getDefaultLogger()}.  *</p>  *<p>  * {@link #pushLogger(MessageLogger)} should be called to delegate to a new logger, and  * {@link #popLogger()} should be called when the context of this logger is finished.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|MessageLoggerEngine
implements|implements
name|MessageLogger
block|{
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|Stack
argument_list|<
name|MessageLogger
argument_list|>
argument_list|>
name|loggerStacks
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Stack
argument_list|<
name|MessageLogger
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|MessageLogger
name|defaultLogger
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
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
argument_list|<
name|String
argument_list|>
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
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|MessageLogger
argument_list|>
name|getLoggerStack
parameter_list|()
block|{
name|Stack
argument_list|<
name|MessageLogger
argument_list|>
name|stack
init|=
name|loggerStacks
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|stack
operator|==
literal|null
condition|)
block|{
name|stack
operator|=
operator|new
name|Stack
argument_list|<
name|MessageLogger
argument_list|>
argument_list|()
expr_stmt|;
name|loggerStacks
operator|.
name|set
argument_list|(
name|stack
argument_list|)
expr_stmt|;
block|}
return|return
name|stack
return|;
block|}
specifier|public
name|MessageLoggerEngine
parameter_list|()
block|{
block|}
comment|/**      * Sets the logger used when the stack is empty.      *       * @param defaultLogger      *            the logger to use when the stack is empty.      */
specifier|public
name|void
name|setDefaultLogger
parameter_list|(
name|MessageLogger
name|defaultLogger
parameter_list|)
block|{
name|this
operator|.
name|defaultLogger
operator|=
name|defaultLogger
expr_stmt|;
block|}
comment|/**      * Push a logger on the stack.      *       * @param logger      *            the logger to push. Must not be<code>null</code>.      */
specifier|public
name|void
name|pushLogger
parameter_list|(
name|MessageLogger
name|logger
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|logger
argument_list|,
literal|"logger"
argument_list|)
expr_stmt|;
name|getLoggerStack
argument_list|()
operator|.
name|push
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pops a logger from the logger stack.      *<p>      * Does nothing if the logger stack is empty      *</p>      */
specifier|public
name|void
name|popLogger
parameter_list|()
block|{
if|if
condition|(
operator|!
name|getLoggerStack
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLoggerStack
argument_list|()
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the current logger, or the default one if there is no logger in the stack      *       * @return the current logger, or the default one if there is no logger in the stack      */
specifier|public
name|MessageLogger
name|peekLogger
parameter_list|()
block|{
if|if
condition|(
name|getLoggerStack
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|getDefaultLogger
argument_list|()
return|;
block|}
return|return
name|getLoggerStack
argument_list|()
operator|.
name|peek
argument_list|()
return|;
block|}
specifier|private
name|MessageLogger
name|getDefaultLogger
parameter_list|()
block|{
comment|// we don't store the logger returned by Message.getDefaultLogger() to always stay in sync
comment|// as long as our default logger has not been set explicitly with setDefaultLogger()
return|return
name|defaultLogger
operator|==
literal|null
condition|?
name|Message
operator|.
name|getDefaultLogger
argument_list|()
else|:
name|defaultLogger
return|;
block|}
comment|// consolidated methods
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|peekLogger
argument_list|()
operator|.
name|warn
argument_list|(
name|msg
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
name|warns
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
name|peekLogger
argument_list|()
operator|.
name|error
argument_list|(
name|msg
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
name|errors
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
name|getProblems
parameter_list|()
block|{
return|return
name|problems
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
name|getDefaultLogger
argument_list|()
operator|.
name|clearProblems
argument_list|()
expr_stmt|;
for|for
control|(
name|MessageLogger
name|l
range|:
name|getLoggerStack
argument_list|()
control|)
block|{
name|l
operator|.
name|clearProblems
argument_list|()
expr_stmt|;
block|}
name|problems
operator|.
name|clear
argument_list|()
expr_stmt|;
name|errors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|warns
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setShowProgress
parameter_list|(
name|boolean
name|progress
parameter_list|)
block|{
name|getDefaultLogger
argument_list|()
operator|.
name|setShowProgress
argument_list|(
name|progress
argument_list|)
expr_stmt|;
comment|// updates all loggers in the stack
for|for
control|(
name|MessageLogger
name|l
range|:
name|getLoggerStack
argument_list|()
control|)
block|{
name|l
operator|.
name|setShowProgress
argument_list|(
name|progress
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isShowProgress
parameter_list|()
block|{
comment|// testing the default logger is enough, all loggers should be in sync
return|return
name|getDefaultLogger
argument_list|()
operator|.
name|isShowProgress
argument_list|()
return|;
block|}
comment|// delegation methods
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|peekLogger
argument_list|()
operator|.
name|debug
argument_list|(
name|msg
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
name|peekLogger
argument_list|()
operator|.
name|deprecated
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|endProgress
parameter_list|()
block|{
name|peekLogger
argument_list|()
operator|.
name|endProgress
argument_list|()
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
name|peekLogger
argument_list|()
operator|.
name|endProgress
argument_list|(
name|msg
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
name|peekLogger
argument_list|()
operator|.
name|info
argument_list|(
name|msg
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
name|peekLogger
argument_list|()
operator|.
name|rawinfo
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
block|{
name|peekLogger
argument_list|()
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|level
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|progress
parameter_list|()
block|{
name|peekLogger
argument_list|()
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|rawlog
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
block|{
name|peekLogger
argument_list|()
operator|.
name|rawlog
argument_list|(
name|msg
argument_list|,
name|level
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
name|peekLogger
argument_list|()
operator|.
name|verbose
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

