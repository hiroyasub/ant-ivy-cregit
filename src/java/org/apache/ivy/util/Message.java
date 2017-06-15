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
comment|/**  * Logging utility class.  *<p>  * This class provides static methods for easy access to the current logger in {@link IvyContext}.  *</p>  *<p>  * To configure logging, you should use the methods provided by the {@link MessageLoggerEngine}  * associated with the {@link Ivy} engine.  *</p>  */
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
name|boolean
name|showedInfo
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
name|MessageLogger
name|defaultLogger
init|=
operator|new
name|DefaultMessageLogger
argument_list|(
name|Message
operator|.
name|MSG_INFO
argument_list|)
decl_stmt|;
comment|/**      * Returns the current default logger.      *      * @return the current default logger; is never<code>null</code>.      */
specifier|public
specifier|static
name|MessageLogger
name|getDefaultLogger
parameter_list|()
block|{
return|return
name|defaultLogger
return|;
block|}
comment|/**      * Change the default logger used when no other logger is currently configured      *      * @param logger      *            the new default logger, must not be<code>null</code>      */
specifier|public
specifier|static
name|void
name|setDefaultLogger
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
name|defaultLogger
operator|=
name|logger
expr_stmt|;
block|}
specifier|private
specifier|static
name|MessageLogger
name|getLogger
parameter_list|()
block|{
return|return
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getMessageLogger
argument_list|()
return|;
block|}
specifier|public
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
literal|":: Apache Ivy "
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
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|getLogger
argument_list|()
operator|.
name|verbose
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|getLogger
argument_list|()
operator|.
name|rawinfo
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|getLogger
argument_list|()
operator|.
name|deprecated
argument_list|(
name|msg
argument_list|)
expr_stmt|;
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
name|getLogger
argument_list|()
operator|.
name|warn
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
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|int
name|logLevel
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
switch|switch
condition|(
name|logLevel
condition|)
block|{
case|case
name|MSG_DEBUG
case|:
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
break|break;
case|case
name|MSG_VERBOSE
case|:
name|verbose
argument_list|(
name|msg
argument_list|)
expr_stmt|;
break|break;
case|case
name|MSG_INFO
case|:
name|info
argument_list|(
name|msg
argument_list|)
expr_stmt|;
break|break;
case|case
name|MSG_WARN
case|:
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
break|break;
case|case
name|MSG_ERR
case|:
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown log level "
operator|+
name|logLevel
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getProblems
parameter_list|()
block|{
return|return
name|getLogger
argument_list|()
operator|.
name|getProblems
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|sumupProblems
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|sumupProblems
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|progress
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|endProgress
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|endProgress
argument_list|()
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
name|getLogger
argument_list|()
operator|.
name|endProgress
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|isShowProgress
parameter_list|()
block|{
return|return
name|getLogger
argument_list|()
operator|.
name|isShowProgress
argument_list|()
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
name|getLogger
argument_list|()
operator|.
name|setShowProgress
argument_list|(
name|progress
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
parameter_list|()
block|{
block|}
specifier|public
specifier|static
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|debug
argument_list|(
name|message
operator|+
literal|" ("
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|debug
argument_list|(
name|t
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
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|verbose
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|verbose
argument_list|(
name|message
operator|+
literal|" ("
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|debug
argument_list|(
name|t
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
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|info
argument_list|(
name|message
operator|+
literal|" ("
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|debug
argument_list|(
name|t
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
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|warn
argument_list|(
name|message
operator|+
literal|" ("
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|debug
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|error
argument_list|(
name|message
operator|+
literal|" ("
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|debug
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|debug
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|debug
argument_list|(
name|StringUtils
operator|.
name|getStackTrace
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

