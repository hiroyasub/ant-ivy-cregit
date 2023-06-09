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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|ComparatorLatestStrategy
extends|extends
name|AbstractLatestStrategy
block|{
specifier|private
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
name|comparator
decl_stmt|;
specifier|public
name|ComparatorLatestStrategy
parameter_list|()
block|{
block|}
specifier|public
name|ComparatorLatestStrategy
parameter_list|(
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|comparator
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
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|infos
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|ret
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
name|getComparator
parameter_list|()
block|{
return|return
name|comparator
return|;
block|}
specifier|public
name|void
name|setComparator
parameter_list|(
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
block|}
end_class

end_unit

