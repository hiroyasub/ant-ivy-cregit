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
name|circular
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_comment
comment|/**  * Unchecked exception thrown when a circular dependency exists between projects.  * @author baumkar  *  */
end_comment

begin_class
specifier|public
class|class
name|CircularDependencyException
extends|extends
name|RuntimeException
block|{
specifier|private
name|ModuleRevisionId
index|[]
name|_mrids
decl_stmt|;
comment|/**      *       * @param descriptors module descriptors in order of circular dependency      */
specifier|public
name|CircularDependencyException
parameter_list|(
specifier|final
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
name|super
argument_list|(
name|CircularDependencyHelper
operator|.
name|formatMessage
argument_list|(
name|mrids
argument_list|)
argument_list|)
expr_stmt|;
name|_mrids
operator|=
name|mrids
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
index|[]
name|getPath
parameter_list|()
block|{
return|return
name|_mrids
return|;
block|}
block|}
end_class

end_unit

