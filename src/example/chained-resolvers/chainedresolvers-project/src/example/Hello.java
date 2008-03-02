begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|example
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|WordUtils
import|;
end_import

begin_comment
comment|/**  * Simple example world to show how easy it is to retreive libs with ivy !!!   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Hello
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
name|message
init|=
literal|"example world !"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"standard message :"
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
literal|"capitalized by "
operator|+
name|WordUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|WordUtils
operator|.
name|capitalizeFully
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"upperCased by "
operator|+
name|test
operator|.
name|StringUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|test
operator|.
name|StringUtils
operator|.
name|upperCase
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Hello
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

