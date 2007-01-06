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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|ModuleRevisionIdTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testEncodeDecodeToString
parameter_list|()
block|{
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"name"
argument_list|,
literal|"revision"
argument_list|)
argument_list|)
expr_stmt|;
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"name"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org.jayasoft"
argument_list|,
literal|"name-post"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org/jayasoft"
argument_list|,
literal|"pre/name"
argument_list|,
literal|"1.0-dev8/2"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
name|extraAttributes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
literal|"extra"
argument_list|,
literal|"extravalue"
argument_list|)
expr_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
literal|"att/name"
argument_list|,
literal|"att/value"
argument_list|)
expr_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
literal|"att.name"
argument_list|,
literal|"att.value"
argument_list|)
expr_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
literal|"att<name"
argument_list|,
literal|"att<value"
argument_list|)
expr_stmt|;
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org/jayasoft"
argument_list|,
literal|"pre/name"
argument_list|,
literal|"1.0-dev8/2"
argument_list|,
name|extraAttributes
argument_list|)
argument_list|)
expr_stmt|;
name|extraAttributes
operator|.
name|put
argument_list|(
literal|"nullatt"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|testEncodeDecodeToString
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org/jayasoft"
argument_list|,
literal|"pre/name"
argument_list|,
literal|"1.0-dev8/2"
argument_list|,
name|extraAttributes
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testEncodeDecodeToString
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|mrid
argument_list|,
name|ModuleRevisionId
operator|.
name|decode
argument_list|(
name|mrid
operator|.
name|encodeToString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

