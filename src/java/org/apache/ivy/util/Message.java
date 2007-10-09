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
name|Iterator
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
name|core
operator|.
name|IvyContext
import|;
end_import

begin_comment
comment|/**  * Logging utility class.  *<p>  * To initialize Message you can call {@link #init(MessageImpl)} with  * the {@link MessageImpl} of your choice.  *<p>   * This only takes effect in the current thread.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Message
block|{
comment|// messages level copied from ant project, to avoid dependency on ant
comment|/** Message priority of "error". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_ERR
init|=
literal|0
decl_stmt|;
comment|/** Message priority of "warning". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_WARN
init|=
literal|1
decl_stmt|;
comment|/** Message priority of "information". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_INFO
init|=
literal|2
decl_stmt|;
comment|/** Message priority of "verbose". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_VERBOSE
init|=
literal|3
decl_stmt|;
comment|/** Message priority of "debug". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_DEBUG
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
name|List
name|problems
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|List
name|warns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|List
name|errors
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|showProgress
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|showedInfo
init|=
literal|false
decl_stmt|;
specifier|public
specifier|static
name|void
name|init
parameter_list|(
name|MessageImpl
name|impl
parameter_list|)
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setMessageImpl
argument_list|(
name|impl
argument_list|)
expr_stmt|;
name|showInfo
argument_list|()
expr_stmt|;
block|}
comment|/**      * same as init, but without displaying info      *       * @param impl      */
specifier|public
specifier|static
name|void
name|setImpl
parameter_list|(
name|MessageImpl
name|impl
parameter_list|)
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setMessageImpl
argument_list|(
name|impl
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|MessageImpl
name|getImpl
parameter_list|()
block|{
return|return
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isInitialised
parameter_list|()
block|{
return|return
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
operator|!=
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|showInfo
parameter_list|()
block|{
if|if
condition|(
operator|!
name|showedInfo
condition|)
block|{
name|info
argument_list|(
literal|":: Ivy "
operator|+
name|Ivy
operator|.
name|getIvyVersion
argument_list|()
operator|+
literal|" - "
operator|+
name|Ivy
operator|.
name|getIvyDate
argument_list|()
operator|+
literal|" :: "
operator|+
name|Ivy
operator|.
name|getIvyHomeURL
argument_list|()
operator|+
literal|" ::"
argument_list|)
expr_stmt|;
name|showedInfo
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_DEBUG
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|verbose
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|rawinfo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|rawlog
argument_list|(
name|msg
argument_list|,
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|deprecated
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
literal|"DEPRECATED: "
operator|+
name|msg
argument_list|,
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
literal|"WARN: "
operator|+
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
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
specifier|static
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
comment|// log in verbose mode because message is appended as a problem, and will be
comment|// logged at the end at error level
name|messageImpl
operator|.
name|log
argument_list|(
literal|"ERROR: "
operator|+
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
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
specifier|static
name|List
name|getProblems
parameter_list|()
block|{
return|return
name|problems
return|;
block|}
specifier|public
specifier|static
name|void
name|sumupProblems
parameter_list|()
block|{
if|if
condition|(
name|problems
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|info
argument_list|(
literal|"\n:: problems summary ::"
argument_list|)
expr_stmt|;
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|warns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|info
argument_list|(
literal|":::: WARNINGS"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|warns
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|msg
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
literal|"\t"
operator|+
name|msg
operator|+
literal|"\n"
argument_list|,
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|errors
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|info
argument_list|(
literal|":::: ERRORS"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|errors
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|msg
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|log
argument_list|(
literal|"\t"
operator|+
name|msg
operator|+
literal|"\n"
argument_list|,
name|MSG_ERR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|info
argument_list|(
literal|"\n:: USE VERBOSE OR DEBUG MESSAGE LEVEL FOR MORE DETAILS"
argument_list|)
expr_stmt|;
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
block|}
specifier|public
specifier|static
name|void
name|progress
parameter_list|()
block|{
if|if
condition|(
name|showProgress
condition|)
block|{
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
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
specifier|static
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
name|MessageImpl
name|messageImpl
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageImpl
operator|!=
literal|null
condition|)
block|{
name|messageImpl
operator|.
name|endProgress
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isShowProgress
parameter_list|()
block|{
return|return
name|showProgress
return|;
block|}
specifier|public
specifier|static
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
specifier|public
specifier|static
name|void
name|uninit
parameter_list|()
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setMessageImpl
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

