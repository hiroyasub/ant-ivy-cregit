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

begin_class
specifier|public
specifier|final
class|class
name|MessageLoggerHelper
block|{
specifier|public
specifier|static
name|void
name|sumupProblems
parameter_list|(
name|MessageLogger
name|logger
parameter_list|)
block|{
name|List
name|myProblems
init|=
name|logger
operator|.
name|getProblems
argument_list|()
decl_stmt|;
if|if
condition|(
name|myProblems
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"\n:: problems summary ::"
argument_list|)
expr_stmt|;
name|List
name|myWarns
init|=
name|logger
operator|.
name|getWarns
argument_list|()
decl_stmt|;
if|if
condition|(
name|myWarns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
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
name|myWarns
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
name|logger
operator|.
name|log
argument_list|(
literal|"\t"
operator|+
name|msg
operator|+
literal|"\n"
argument_list|,
name|Message
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
block|}
name|List
name|myErrors
init|=
name|logger
operator|.
name|getErrors
argument_list|()
decl_stmt|;
if|if
condition|(
name|myErrors
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|logger
operator|.
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
name|myErrors
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
name|logger
operator|.
name|log
argument_list|(
literal|"\t"
operator|+
name|msg
operator|+
literal|"\n"
argument_list|,
name|Message
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
block|}
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"\n:: USE VERBOSE OR DEBUG MESSAGE LEVEL FOR MORE DETAILS"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|MessageLoggerHelper
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

