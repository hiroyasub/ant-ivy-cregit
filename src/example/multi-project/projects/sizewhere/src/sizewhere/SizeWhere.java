begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|sizewhere
package|;
end_package

begin_import
import|import
name|size
operator|.
name|FileSize
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import static
name|find
operator|.
name|FindFile
operator|.
name|find
import|;
end_import

begin_import
import|import static
name|version
operator|.
name|Version
operator|.
name|register
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SizeWhere
block|{
static|static
block|{
name|register
argument_list|(
literal|"sizewhere"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|long
name|totalSize
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|FileSize
operator|.
name|totalSize
argument_list|(
name|find
argument_list|(
name|dir
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|SizeWhere
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

