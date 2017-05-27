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
name|version
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|ModuleDescriptor
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|core
operator|.
name|settings
operator|.
name|IvySettings
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
name|IvySettingsAware
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
name|util
operator|.
name|Checks
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractVersionMatcher
implements|implements
name|VersionMatcher
implements|,
name|IvySettingsAware
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|public
name|AbstractVersionMatcher
parameter_list|()
block|{
block|}
specifier|public
name|AbstractVersionMatcher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
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
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|boolean
name|needModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleDescriptor
name|foundMD
parameter_list|)
block|{
return|return
name|accept
argument_list|(
name|askedMrid
argument_list|,
name|foundMD
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This method should be overridden in most cases, because it uses the default contract to      * return 1 when it's not possible to know which revision is greater.      */
specifier|public
name|int
name|compare
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|,
name|Comparator
name|staticComparator
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|IvySettings
name|getSettings
parameter_list|()
block|{
return|return
name|settings
return|;
block|}
specifier|public
name|void
name|setSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|settings
argument_list|,
literal|"settings"
argument_list|)
expr_stmt|;
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
block|}
end_class

end_unit

