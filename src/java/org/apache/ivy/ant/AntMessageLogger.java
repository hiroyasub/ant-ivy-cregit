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
name|ant
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|AbstractMessageLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|Checks
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|MessageLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|ProjectComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Task
import|;
end_import

begin_comment
comment|/**  * Implementation of the simple message facility for ant.  */
end_comment

begin_class
specifier|public
class|class
name|AntMessageLogger
extends|extends
name|AbstractMessageLogger
block|{
specifier|private
specifier|static
specifier|final
name|int
name|PROGRESS_LOG_PERIOD
init|=
literal|1500
decl_stmt|;
comment|/**      * Creates and register an {@link AntMessageLogger} for the given {@link Task}, with the given      * {@link Ivy} instance.      *<p>      * The created instance will automatically be unregistered from the Ivy instance when the task      * finishes.      *</p>      *       * @param task      *            the task the logger should use for logging      * @param ivy      *            the ivy instance on which the logger should be registered      */
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|ProjectComponent
name|task
parameter_list|,
specifier|final
name|Ivy
name|ivy
parameter_list|)
block|{
name|MessageLogger
name|current
init|=
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|peekLogger
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|instanceof
name|AntMessageLogger
operator|&&
name|task
operator|instanceof
name|Task
operator|&&
operator|(
operator|(
name|AntMessageLogger
operator|)
name|current
operator|)
operator|.
name|task
operator|instanceof
name|Task
condition|)
block|{
name|Task
name|currentTask
init|=
operator|(
name|Task
operator|)
operator|(
operator|(
name|AntMessageLogger
operator|)
name|current
operator|)
operator|.
name|task
decl_stmt|;
if|if
condition|(
operator|(
name|currentTask
operator|.
name|getTaskName
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
name|currentTask
operator|.
name|getTaskName
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Task
operator|)
name|task
operator|)
operator|.
name|getTaskName
argument_list|()
argument_list|)
condition|)
block|{
comment|// The current AntMessageLogger already logs with the same
comment|// prefix as the given task. So we shouldn't do anything...
return|return;
block|}
block|}
name|AntMessageLogger
name|logger
init|=
operator|new
name|AntMessageLogger
argument_list|(
name|task
argument_list|)
decl_stmt|;
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|pushLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
name|task
operator|.
name|getProject
argument_list|()
operator|.
name|addBuildListener
argument_list|(
operator|new
name|BuildListener
argument_list|()
block|{
specifier|private
name|int
name|stackDepth
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|buildFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
specifier|public
name|void
name|buildStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
specifier|public
name|void
name|targetStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
specifier|public
name|void
name|targetFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
specifier|public
name|void
name|taskStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
name|stackDepth
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|taskFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
comment|// NB: There is sometimes task created by an other task
comment|// in that case, we should not uninit Message. The log should stay associated
comment|// with the initial task, except if it was an antcall, ant or subant target
comment|// NB2 : Testing the identity of the task is not enough, event.getTask() return
comment|// an instance of UnknownElement is wrapping the concrete instance
name|stackDepth
operator|--
expr_stmt|;
if|if
condition|(
name|stackDepth
operator|==
operator|-
literal|1
condition|)
block|{
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|popLogger
argument_list|()
expr_stmt|;
name|event
operator|.
name|getProject
argument_list|()
operator|.
name|removeBuildListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|messageLogged
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ProjectComponent
name|task
decl_stmt|;
specifier|private
name|long
name|lastProgressFlush
init|=
literal|0
decl_stmt|;
specifier|private
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|/**      * Constructs a new AntMessageImpl instance.      *       * @param task      *            the ant project component this message implementation should use for logging. Must      *            not be<code>null</code>.      */
specifier|protected
name|AntMessageLogger
parameter_list|(
name|ProjectComponent
name|task
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|task
argument_list|,
literal|"task"
argument_list|)
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
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
name|task
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
name|rawlog
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|level
parameter_list|)
block|{
name|task
operator|.
name|getProject
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
name|doProgress
parameter_list|()
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
name|lastProgressFlush
operator|==
literal|0
condition|)
block|{
name|lastProgressFlush
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
comment|// log with ant causes a new line -> we do it only once in a while
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|lastProgressFlush
operator|>
name|PROGRESS_LOG_PERIOD
condition|)
block|{
name|task
operator|.
name|log
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|lastProgressFlush
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|doEndProgress
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|task
operator|.
name|log
argument_list|(
name|buf
operator|+
name|msg
argument_list|)
expr_stmt|;
name|buf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|lastProgressFlush
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AntMessageLogger:"
operator|+
name|task
return|;
block|}
block|}
end_class

end_unit

