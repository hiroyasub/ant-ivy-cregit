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
name|BundleInfo
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
name|BundleRequirement
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
name|Require
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
name|util
operator|.
name|VersionRange
import|;
end_import

begin_class
specifier|public
class|class
name|PluginAdapter
block|{
specifier|public
specifier|static
name|BundleInfo
name|featureAsBundle
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|EclipseFeature
name|feature
parameter_list|)
block|{
name|BundleInfo
name|b
init|=
operator|new
name|BundleInfo
argument_list|(
name|feature
operator|.
name|getId
argument_list|()
argument_list|,
name|feature
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|feature
operator|.
name|getUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|b
operator|.
name|setUri
argument_list|(
name|baseUri
operator|.
name|resolve
argument_list|(
literal|"features/"
operator|+
name|feature
operator|.
name|getId
argument_list|()
operator|+
literal|'_'
operator|+
name|feature
operator|.
name|getVersion
argument_list|()
operator|+
literal|".jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|setUri
argument_list|(
name|baseUri
operator|.
name|resolve
argument_list|(
name|feature
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|setDescription
argument_list|(
name|feature
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|setLicense
argument_list|(
name|feature
operator|.
name|getLicense
argument_list|()
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
name|BundleRequirement
name|r
init|=
operator|new
name|BundleRequirement
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|plugin
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|VersionRange
argument_list|(
name|plugin
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|b
operator|.
name|addRequirement
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Require
name|require
range|:
name|feature
operator|.
name|getRequires
argument_list|()
control|)
block|{
name|String
name|id
decl_stmt|;
if|if
condition|(
name|require
operator|.
name|getPlugin
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|require
operator|.
name|getPlugin
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|id
operator|=
name|require
operator|.
name|getFeature
argument_list|()
expr_stmt|;
block|}
name|VersionRange
name|range
decl_stmt|;
if|if
condition|(
name|require
operator|.
name|getMatch
argument_list|()
operator|.
name|equals
argument_list|(
literal|"greaterOrEqual"
argument_list|)
condition|)
block|{
name|range
operator|=
operator|new
name|VersionRange
argument_list|(
name|require
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unsupported match "
operator|+
name|require
operator|.
name|getMatch
argument_list|()
argument_list|)
throw|;
block|}
name|BundleRequirement
name|r
init|=
operator|new
name|BundleRequirement
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|id
argument_list|,
name|range
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|b
operator|.
name|addRequirement
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|b
return|;
block|}
specifier|public
specifier|static
name|BundleInfo
name|pluginAsBundle
parameter_list|(
name|URI
name|baseUri
parameter_list|,
name|EclipsePlugin
name|plugin
parameter_list|)
block|{
name|BundleInfo
name|b
init|=
operator|new
name|BundleInfo
argument_list|(
name|plugin
operator|.
name|getId
argument_list|()
argument_list|,
name|plugin
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|b
operator|.
name|setUri
argument_list|(
name|baseUri
operator|.
name|resolve
argument_list|(
literal|"plugins/"
operator|+
name|plugin
operator|.
name|getId
argument_list|()
operator|+
literal|'_'
operator|+
name|plugin
operator|.
name|getVersion
argument_list|()
operator|+
literal|".jar"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

