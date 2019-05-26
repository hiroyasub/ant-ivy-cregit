begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|io
operator|.
name|File
import|;
end_import

begin_class
specifier|public
class|class
name|CacheCleaner
block|{
comment|/**      * Delete the directory and all it contains. Previously, we used the ant delete task, but it      * occasionally failed (access denied) on my machine for unknown reason.      *      * @param toDelete      *            File      */
specifier|public
specifier|static
name|void
name|deleteDir
parameter_list|(
name|File
name|toDelete
parameter_list|)
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|toDelete
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

