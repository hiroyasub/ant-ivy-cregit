begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|version
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Version
block|{
static|static
block|{
name|versions
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|register
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|versions
decl_stmt|;
specifier|public
specifier|static
name|void
name|register
parameter_list|(
name|String
name|module
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|moduleVersion
init|=
name|Version
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|module
operator|+
literal|".properties"
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|moduleVersion
argument_list|)
expr_stmt|;
name|String
name|version
init|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
literal|"version"
argument_list|)
decl_stmt|;
name|versions
operator|.
name|put
argument_list|(
name|module
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"--- using "
operator|+
name|module
operator|+
literal|" v"
operator|+
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"an error occurred while registering "
operator|+
name|module
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Version
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

