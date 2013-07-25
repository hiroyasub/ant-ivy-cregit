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
name|plugins
operator|.
name|lock
package|;
end_package

begin_class
specifier|public
class|class
name|CreateFileLockStrategy
extends|extends
name|ArtifactLockStrategy
block|{
specifier|public
name|CreateFileLockStrategy
parameter_list|(
name|boolean
name|debugLocking
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|CreateFileLocker
argument_list|(
name|debugLocking
argument_list|)
argument_list|,
name|debugLocking
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"artifact-lock"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

