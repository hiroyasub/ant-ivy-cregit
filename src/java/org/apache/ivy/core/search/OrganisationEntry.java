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
name|search
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
name|resolver
operator|.
name|DependencyResolver
import|;
end_import

begin_class
specifier|public
class|class
name|OrganisationEntry
block|{
specifier|private
name|DependencyResolver
name|resolver
decl_stmt|;
specifier|private
name|String
name|organisation
decl_stmt|;
specifier|public
name|OrganisationEntry
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|String
name|organisation
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|organisation
operator|=
name|organisation
expr_stmt|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|organisation
return|;
block|}
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|organisation
return|;
block|}
block|}
end_class

end_unit

