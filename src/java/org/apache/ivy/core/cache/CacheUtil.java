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
name|core
operator|.
name|cache
package|;
end_package

begin_comment
comment|/**  * Utility class providing some cache related facilities.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CacheUtil
block|{
comment|/**      * Checks that the given pattern is acceptable as a cache pattern      *       * @param cachePattern      *            the pattern to check      * @throws IllegalArgumentException      *             if the pattern isn't acceptable as cache pattern      */
specifier|public
specifier|static
name|void
name|checkCachePattern
parameter_list|(
name|String
name|cachePattern
parameter_list|)
block|{
if|if
condition|(
name|cachePattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"null cache pattern not allowed."
argument_list|)
throw|;
block|}
if|if
condition|(
name|cachePattern
operator|.
name|startsWith
argument_list|(
literal|".."
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid cache pattern: '"
operator|+
name|cachePattern
operator|+
literal|"': cache patterns must not lead outside cache directory"
argument_list|)
throw|;
block|}
if|if
condition|(
name|cachePattern
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid cache pattern: '"
operator|+
name|cachePattern
operator|+
literal|"': cache patterns must not be absolute"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|CacheUtil
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

