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
operator|.
name|testutil
package|;
end_package

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

begin_comment
comment|/**  * Our own personal build listener.  */
end_comment

begin_class
specifier|public
class|class
name|AntTestListener
implements|implements
name|BuildListener
block|{
specifier|private
name|int
name|logLevel
decl_stmt|;
specifier|private
name|StringBuffer
name|buildLog
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|/**      * Constructs a test listener which will ignore log events above the given level.      */
specifier|public
name|AntTestListener
parameter_list|(
name|int
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|logLevel
expr_stmt|;
block|}
comment|/**      * Fired before any targets are started.      */
specifier|public
name|void
name|buildStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
comment|/**      * Fired after the last target has finished. This event will still be thrown if an error      * occurred during the build.      *       * @see BuildEvent#getException()      */
specifier|public
name|void
name|buildFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
block|}
comment|/**      * Fired when a target is started.      *       * @see BuildEvent#getTarget()      */
specifier|public
name|void
name|targetStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
comment|// System.out.println("targetStarted " + event.getTarget().getName());
block|}
comment|/**      * Fired when a target has finished. This event will still be thrown if an error occurred during      * the build.      *       * @see BuildEvent#getException()      */
specifier|public
name|void
name|targetFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
comment|// System.out.println("targetFinished " + event.getTarget().getName());
block|}
comment|/**      * Fired when a task is started.      *       * @see BuildEvent#getTask()      */
specifier|public
name|void
name|taskStarted
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
comment|// System.out.println("taskStarted " + event.getTask().getTaskName());
block|}
comment|/**      * Fired when a task has finished. This event will still be throw if an error occurred during      * the build.      *       * @see BuildEvent#getException()      */
specifier|public
name|void
name|taskFinished
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
comment|// System.out.println("taskFinished " + event.getTask().getTaskName());
block|}
comment|/**      * Fired whenever a message is logged.      *       * @see BuildEvent#getMessage()      * @see BuildEvent#getPriority()      */
specifier|public
name|void
name|messageLogged
parameter_list|(
name|BuildEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getPriority
argument_list|()
operator|>
name|logLevel
condition|)
block|{
comment|// ignore event
return|return;
block|}
name|buildLog
operator|.
name|append
argument_list|(
name|event
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLog
parameter_list|()
block|{
return|return
name|buildLog
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

