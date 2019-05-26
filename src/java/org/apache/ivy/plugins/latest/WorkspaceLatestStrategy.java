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
name|latest
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

begin_comment
comment|/**  * A strategy which delegate to another strategy, unless for the latest and working revisions which  * are considered as superior to any other revision.  *<p>NB : it is for internal usage of Ivy only!</p>  */
end_comment

begin_class
specifier|public
class|class
name|WorkspaceLatestStrategy
extends|extends
name|AbstractLatestStrategy
block|{
specifier|private
name|LatestStrategy
name|delegate
decl_stmt|;
specifier|public
name|WorkspaceLatestStrategy
parameter_list|(
name|LatestStrategy
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|setName
argument_list|(
literal|"workspace-"
operator|+
name|delegate
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ArtifactInfo
argument_list|>
name|sort
parameter_list|(
name|ArtifactInfo
index|[]
name|infos
parameter_list|)
block|{
name|List
argument_list|<
name|ArtifactInfo
argument_list|>
name|head
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArtifactInfo
argument_list|>
name|tail
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactInfo
name|ai
range|:
name|delegate
operator|.
name|sort
argument_list|(
name|infos
argument_list|)
control|)
block|{
name|String
name|rev
init|=
name|ai
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|boolean
name|latestRev
init|=
name|rev
operator|.
name|startsWith
argument_list|(
literal|"latest"
argument_list|)
operator|||
name|rev
operator|.
name|startsWith
argument_list|(
literal|"working"
argument_list|)
decl_stmt|;
if|if
condition|(
name|latestRev
condition|)
block|{
name|head
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tail
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
block|}
block|}
name|head
operator|.
name|addAll
argument_list|(
name|tail
argument_list|)
expr_stmt|;
return|return
name|head
return|;
block|}
block|}
end_class

end_unit

