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
name|updatesite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|osgi
operator|.
name|core
operator|.
name|ExecutionEnvironmentProfileProvider
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
name|osgi
operator|.
name|repo
operator|.
name|EditableRepoDescriptor
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
name|osgi
operator|.
name|updatesite
operator|.
name|xml
operator|.
name|EclipseFeature
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
name|osgi
operator|.
name|updatesite
operator|.
name|xml
operator|.
name|EclipsePlugin
import|;
end_import

begin_class
specifier|public
class|class
name|UpdateSiteDescriptor
extends|extends
name|EditableRepoDescriptor
block|{
specifier|public
name|UpdateSiteDescriptor
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|ExecutionEnvironmentProfileProvider
name|profileProvider
parameter_list|)
block|{
name|super
argument_list|(
name|baseUri
argument_list|,
name|profileProvider
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFeature
parameter_list|(
name|EclipseFeature
name|feature
parameter_list|)
block|{
name|addBundle
argument_list|(
name|PluginAdapter
operator|.
name|featureAsBundle
argument_list|(
name|getBaseUri
argument_list|()
argument_list|,
name|feature
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|EclipsePlugin
name|plugin
range|:
name|feature
operator|.
name|getPlugins
argument_list|()
control|)
block|{
name|addBundle
argument_list|(
name|PluginAdapter
operator|.
name|pluginAsBundle
argument_list|(
name|getBaseUri
argument_list|()
argument_list|,
name|plugin
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

