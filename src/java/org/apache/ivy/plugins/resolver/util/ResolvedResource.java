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
name|resolver
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|latest
operator|.
name|ArtifactInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|repository
operator|.
name|Resource
import|;
end_import

begin_class
specifier|public
class|class
name|ResolvedResource
implements|implements
name|ArtifactInfo
block|{
specifier|private
name|Resource
name|res
decl_stmt|;
specifier|private
name|String
name|rev
decl_stmt|;
specifier|public
name|ResolvedResource
parameter_list|(
name|Resource
name|res
parameter_list|,
name|String
name|rev
parameter_list|)
block|{
name|this
operator|.
name|res
operator|=
name|res
expr_stmt|;
name|this
operator|.
name|rev
operator|=
name|rev
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|rev
return|;
block|}
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
return|return
name|res
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|res
operator|+
literal|" ("
operator|+
name|rev
operator|+
literal|")"
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|getResource
argument_list|()
operator|.
name|getLastModified
argument_list|()
return|;
block|}
block|}
end_class

end_unit

