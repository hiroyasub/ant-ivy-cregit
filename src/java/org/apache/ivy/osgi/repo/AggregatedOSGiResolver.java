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
name|osgi
operator|.
name|repo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|AggregatedOSGiResolver
extends|extends
name|AbstractOSGiResolver
block|{
specifier|private
name|List
argument_list|<
name|AbstractOSGiResolver
argument_list|>
name|resolvers
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractOSGiResolver
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|AbstractOSGiResolver
name|resolver
parameter_list|)
block|{
name|resolvers
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|List
argument_list|<
name|RepoDescriptor
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<
name|RepoDescriptor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractOSGiResolver
name|resolver
range|:
name|resolvers
control|)
block|{
name|repos
operator|.
name|add
argument_list|(
name|resolver
operator|.
name|getRepoDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setRepoDescriptor
argument_list|(
operator|new
name|AggregatedRepoDescriptor
argument_list|(
name|repos
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
