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
name|util
operator|.
name|filter
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
name|Collection
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

begin_class
specifier|public
class|class
name|FilterHelper
block|{
specifier|public
specifier|static
name|Filter
name|NO_FILTER
init|=
name|NoFilter
operator|.
name|INSTANCE
decl_stmt|;
specifier|public
specifier|static
name|Filter
name|getArtifactTypeFilter
parameter_list|(
name|String
name|types
parameter_list|)
block|{
if|if
condition|(
name|types
operator|==
literal|null
operator|||
name|types
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
name|NO_FILTER
return|;
block|}
name|String
index|[]
name|t
init|=
name|types
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|List
name|acceptedTypes
init|=
operator|new
name|ArrayList
argument_list|(
name|types
operator|.
name|length
argument_list|()
argument_list|)
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
name|t
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|acceptedTypes
operator|.
name|add
argument_list|(
name|t
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ArtifactTypeFilter
argument_list|(
name|acceptedTypes
argument_list|)
return|;
block|}
comment|/**      * we could have used commons-collections facility for this...      * if we accepted to add dependencies on third party jars      * @param col      * @param filter      * @return      */
specifier|public
specifier|static
name|Collection
name|filter
parameter_list|(
name|Collection
name|col
parameter_list|,
name|Filter
name|filter
parameter_list|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
condition|)
block|{
return|return
name|col
return|;
block|}
name|Collection
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|col
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|ret
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
name|Object
name|element
init|=
operator|(
name|Object
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|filter
operator|.
name|accept
argument_list|(
name|element
argument_list|)
condition|)
block|{
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit
