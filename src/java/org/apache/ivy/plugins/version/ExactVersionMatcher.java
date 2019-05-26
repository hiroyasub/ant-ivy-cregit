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
name|plugins
operator|.
name|version
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
import|;
end_import

begin_class
specifier|public
class|class
name|ExactVersionMatcher
extends|extends
name|AbstractVersionMatcher
block|{
specifier|public
name|ExactVersionMatcher
parameter_list|()
block|{
name|super
argument_list|(
literal|"exact"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
return|return
name|askedMrid
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|foundMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

