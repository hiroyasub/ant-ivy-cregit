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
name|util
operator|.
name|Message
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
name|MessageImpl
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
name|Task
import|;
end_import

begin_comment
comment|/**  * Implementation of the simple message facility for ant.  *   */
end_comment

begin_class
specifier|public
class|class
name|AntMessageImpl
implements|implements
name|MessageImpl
block|{
specifier|private
name|Task
name|_task
decl_stmt|;
specifier|private
specifier|static
name|long
name|_lastProgressFlush
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
name|StringBuffer
name|_buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|/**      * @param task      */
specifier|public
name|AntMessageImpl
parameter_list|(
name|Task
name|task
parameter_list|)
block|{
name|_task
operator|=
name|task
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
comment|//NB: There is somtimes task created by an other task
comment|//in that case, we should not uninit Message.  The log should stay associated
comment|//with the initial task, except if it was an antcall, ant or subant target
comment|//NB2 : Testing the identity of the task is not enought, event.getTask() return
comment|//an instance of UnknownElement is wrapping the concrete instance
if|if
condition|(
name|stackDepth
operator|==
literal|0
condition|)
block|{
name|Message
operator|.
name|uninit
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
name|stackDepth
operator|--
expr_stmt|;
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
name|_task
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
name|_task
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
name|progress
parameter_list|()
block|{
name|_buf
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
name|_lastProgressFlush
operator|==
literal|0
condition|)
block|{
name|_lastProgressFlush
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_task
operator|!=
literal|null
condition|)
block|{
comment|// log with ant causes a new line -> we do it only once in a while
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|_lastProgressFlush
operator|>
literal|1500
condition|)
block|{
name|_task
operator|.
name|log
argument_list|(
name|_buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|_buf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|_lastProgressFlush
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|endProgress
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|_task
operator|.
name|log
argument_list|(
name|_buf
operator|+
name|msg
argument_list|)
expr_stmt|;
name|_buf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|_lastProgressFlush
operator|=
literal|0
expr_stmt|;
block|}
block|}
end_class

end_unit

