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
name|core
package|;
end_package

begin_comment
comment|/**  * A simple thread subclass associated the same IvyContext as the thread in which it is instanciated.  *   * If you override the run target, then you will have to call initContext() to do the association  * with the original IvyContext.  *   * @see IvyContext  */
end_comment

begin_class
specifier|public
class|class
name|IvyThread
extends|extends
name|Thread
block|{
specifier|private
name|IvyContext
name|_context
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
specifier|public
name|IvyThread
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|Runnable
name|target
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|Runnable
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|ThreadGroup
name|group
parameter_list|,
name|Runnable
name|target
parameter_list|,
name|String
name|name
parameter_list|,
name|long
name|stackSize
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|,
name|target
argument_list|,
name|name
argument_list|,
name|stackSize
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|ThreadGroup
name|group
parameter_list|,
name|Runnable
name|target
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|,
name|target
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|ThreadGroup
name|group
parameter_list|,
name|Runnable
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
specifier|public
name|IvyThread
parameter_list|(
name|ThreadGroup
name|group
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|initContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initContext
parameter_list|()
block|{
name|IvyContext
operator|.
name|setContext
argument_list|(
name|_context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

