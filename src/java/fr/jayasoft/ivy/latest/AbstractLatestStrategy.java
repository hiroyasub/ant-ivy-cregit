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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
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
name|IvyAware
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
name|LatestStrategy
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLatestStrategy
implements|implements
name|LatestStrategy
implements|,
name|IvyAware
block|{
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|Ivy
name|_ivy
decl_stmt|;
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|_ivy
return|;
block|}
specifier|public
name|void
name|setIvy
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
name|_ivy
operator|=
name|ivy
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|ArtifactInfo
name|findLatest
parameter_list|(
name|ArtifactInfo
index|[]
name|infos
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|List
name|l
init|=
name|sort
argument_list|(
name|infos
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|l
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactInfo
name|info
init|=
operator|(
name|ArtifactInfo
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|date
operator|==
literal|null
operator|||
name|info
operator|.
name|getLastModified
argument_list|()
operator|<
name|date
operator|.
name|getTime
argument_list|()
condition|)
block|{
return|return
name|info
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

