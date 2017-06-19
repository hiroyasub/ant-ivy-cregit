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
name|cli
operator|.
name|CommandLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|CommandLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|DefaultParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|Options
import|;
end_import

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
comment|/**  * Simple example to show how easy it is to retrieve transitive libs with ivy !!!  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HelloConsole
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
throws|throws
name|Exception
block|{
name|Option
name|msg
init|=
name|Option
operator|.
name|builder
argument_list|(
literal|"m"
argument_list|)
operator|.
name|longOpt
argument_list|(
literal|"message"
argument_list|)
operator|.
name|hasArg
argument_list|()
operator|.
name|desc
argument_list|(
literal|"the message to capitalize"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Options
name|options
init|=
operator|new
name|Options
argument_list|()
decl_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|CommandLineParser
name|parser
init|=
operator|new
name|DefaultParser
argument_list|()
decl_stmt|;
name|CommandLine
name|line
init|=
name|parser
operator|.
name|parse
argument_list|(
name|options
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|String
name|message
init|=
name|line
operator|.
name|getOptionValue
argument_list|(
literal|"m"
argument_list|,
literal|"Hello Ivy!"
argument_list|)
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
block|}
specifier|private
name|HelloConsole
parameter_list|()
block|{
block|}
block|}
end_class

end_unit
