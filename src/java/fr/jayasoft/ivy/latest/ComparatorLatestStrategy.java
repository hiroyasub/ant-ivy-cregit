begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|Date
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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ArtifactInfo
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
name|_comparator
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
name|comparator
parameter_list|)
block|{
name|_comparator
operator|=
name|comparator
expr_stmt|;
block|}
specifier|public
name|ArtifactInfo
name|findLatest
parameter_list|(
name|ArtifactInfo
index|[]
name|artifacts
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
if|if
condition|(
name|artifacts
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ArtifactInfo
name|found
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|ArtifactInfo
name|art
init|=
name|artifacts
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|found
operator|==
literal|null
operator|||
name|_comparator
operator|.
name|compare
argument_list|(
name|art
argument_list|,
name|found
argument_list|)
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
name|long
name|lastModified
init|=
name|art
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastModified
operator|>
name|date
operator|.
name|getTime
argument_list|()
condition|)
block|{
continue|continue;
block|}
block|}
name|found
operator|=
name|art
expr_stmt|;
block|}
block|}
return|return
name|found
return|;
block|}
specifier|public
name|List
name|sort
parameter_list|(
name|ArtifactInfo
index|[]
name|infos
parameter_list|)
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
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
name|_comparator
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|Comparator
name|getComparator
parameter_list|()
block|{
return|return
name|_comparator
return|;
block|}
specifier|public
name|void
name|setComparator
parameter_list|(
name|Comparator
name|comparator
parameter_list|)
block|{
name|_comparator
operator|=
name|comparator
expr_stmt|;
block|}
block|}
end_class

end_unit

