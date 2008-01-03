begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|myapp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|filter
operator|.
name|FilterProvider
import|;
end_import

begin_import
import|import
name|filter
operator|.
name|IFilter
import|;
end_import

begin_class
specifier|public
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
index|[]
name|toFilter
init|=
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"tree"
block|,
literal|"four"
block|}
decl_stmt|;
name|IFilter
name|filter
init|=
name|FilterProvider
operator|.
name|getFilter
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Filtering with:"
operator|+
name|filter
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|filtered
init|=
name|filter
operator|.
name|filter
argument_list|(
name|toFilter
argument_list|,
literal|"t"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Result :"
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|filtered
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

