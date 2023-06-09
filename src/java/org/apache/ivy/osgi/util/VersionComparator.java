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
name|osgi
operator|.
name|util
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
name|VersionComparator
implements|implements
name|Comparator
argument_list|<
name|Version
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Version
argument_list|>
name|ASCENDING
init|=
operator|new
name|VersionComparator
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Version
argument_list|>
name|DESCENDING
init|=
operator|new
name|VersionComparator
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|public
specifier|final
name|boolean
name|reverse
decl_stmt|;
specifier|private
name|VersionComparator
parameter_list|(
name|boolean
name|reverse
parameter_list|)
block|{
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|Version
name|objA
parameter_list|,
name|Version
name|objB
parameter_list|)
block|{
specifier|final
name|int
name|val
init|=
name|objA
operator|.
name|compareTo
argument_list|(
name|objB
argument_list|)
decl_stmt|;
return|return
name|reverse
condition|?
operator|-
name|val
else|:
name|val
return|;
block|}
block|}
end_class

end_unit

