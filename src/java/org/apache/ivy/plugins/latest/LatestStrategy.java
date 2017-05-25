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

begin_interface
specifier|public
interface|interface
name|LatestStrategy
block|{
comment|/**      * Finds the latest artifact among the given artifacts info. The definition of 'latest' depends      * on the strategy itself. Given artifacts info are all good candidate. If the given date is not      * null, then found artifact should not be later than this date.      *       * @param infos      * @param date      * @return the latest artifact among the given ones.      */
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
function_decl|;
comment|/**      * Sorts the given artifacts info from the oldest one to the latest one. The definition of      * 'latest' depends on the strategy itself. Given artifacts info are all good candidate.      *       * @param infos      * @return      */
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
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

