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
package|;
end_package

begin_comment
comment|/**  * This class contains information about the origin of an artifact.  *   * @author maartenc  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactOrigin
block|{
specifier|private
name|boolean
name|_isLocal
decl_stmt|;
specifier|private
name|String
name|_location
decl_stmt|;
specifier|public
name|ArtifactOrigin
parameter_list|(
name|boolean
name|isLocal
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|_isLocal
operator|=
name|isLocal
expr_stmt|;
name|_location
operator|=
name|location
expr_stmt|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|_isLocal
return|;
block|}
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|_location
return|;
block|}
block|}
end_class

end_unit

