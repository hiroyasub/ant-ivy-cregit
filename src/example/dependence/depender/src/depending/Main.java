begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|depending
package|;
end_package

begin_comment
comment|/**  * TODO write javadoc  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Main
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
name|standaloneVersion
init|=
name|standalone
operator|.
name|Main
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|standaloneVersion
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"you are using version "
operator|+
name|standaloneVersion
operator|+
literal|" of class "
operator|+
name|standalone
operator|.
name|Main
operator|.
name|class
operator|.
name|getName
argument_list|()
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
literal|"failed to get version of "
operator|+
name|standalone
operator|.
name|Main
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|message
init|=
literal|"i am "
operator|+
name|Main
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" and "
operator|+
name|standalone
operator|.
name|Main
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" will do the job for me"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"standard message : "
operator|+
name|message
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"capitalized message : "
operator|+
name|standalone
operator|.
name|Main
operator|.
name|capitalizeWords
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Main
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

