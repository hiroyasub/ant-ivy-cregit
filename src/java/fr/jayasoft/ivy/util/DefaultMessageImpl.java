begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|util
package|;
end_package

begin_class
specifier|public
class|class
name|DefaultMessageImpl
implements|implements
name|MessageImpl
block|{
specifier|private
name|int
name|_level
init|=
name|Message
operator|.
name|MSG_INFO
decl_stmt|;
comment|/**      * @param level      */
specifier|public
name|DefaultMessageImpl
parameter_list|(
name|int
name|level
parameter_list|)
block|{
name|_level
operator|=
name|level
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
if|if
condition|(
name|level
operator|<=
name|_level
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
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
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"."
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getLevel
parameter_list|()
block|{
return|return
name|_level
return|;
block|}
block|}
end_class

end_unit

