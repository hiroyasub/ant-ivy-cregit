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
name|latest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_class
specifier|public
class|class
name|LatestTimeStrategy
extends|extends
name|ComparatorLatestStrategy
block|{
specifier|private
specifier|static
name|Comparator
name|COMPARATOR
init|=
operator|new
name|Comparator
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|long
name|d1
init|=
operator|(
operator|(
name|ArtifactInfo
operator|)
name|o1
operator|)
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
name|long
name|d2
init|=
operator|(
operator|(
name|ArtifactInfo
operator|)
name|o2
operator|)
operator|.
name|getLastModified
argument_list|()
decl_stmt|;
return|return
operator|new
name|Long
argument_list|(
name|d1
argument_list|)
operator|.
name|compareTo
argument_list|(
operator|new
name|Long
argument_list|(
name|d2
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
name|LatestTimeStrategy
parameter_list|()
block|{
name|super
argument_list|(
name|COMPARATOR
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"latest-time"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
