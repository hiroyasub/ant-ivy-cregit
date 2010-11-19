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
name|repo
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|BundleCapability
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
name|ManifestParser
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
name|Version
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
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|BundleRepo
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Long
name|lastModified
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|BundleInfo
argument_list|>
name|bundles
init|=
operator|new
name|HashSet
argument_list|<
name|BundleInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|>
argument_list|>
name|bundleByCapabilities
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|BundleRepo
parameter_list|()
block|{
comment|// default constructor
block|}
specifier|public
name|BundleRepo
parameter_list|(
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
name|it
parameter_list|)
block|{
name|populate
argument_list|(
name|it
argument_list|)
expr_stmt|;
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
name|setLastModified
parameter_list|(
name|Long
name|lastModified
parameter_list|)
block|{
name|this
operator|.
name|lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
specifier|public
name|Long
name|getLastModified
parameter_list|()
block|{
return|return
name|lastModified
return|;
block|}
specifier|public
name|void
name|populate
parameter_list|(
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
name|it
parameter_list|)
block|{
for|for
control|(
name|ManifestAndLocation
name|manifestAndLocation
range|:
name|it
control|)
block|{
try|try
block|{
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|manifestAndLocation
operator|.
name|getManifest
argument_list|()
argument_list|)
decl_stmt|;
name|bundleInfo
operator|.
name|setUri
argument_list|(
name|manifestAndLocation
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|addBundle
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Rejected "
operator|+
name|manifestAndLocation
operator|.
name|getLocation
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|addBundle
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
name|bundles
operator|.
name|add
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
name|populateCapabilities
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
argument_list|,
name|bundleInfo
argument_list|)
expr_stmt|;
for|for
control|(
name|BundleCapability
name|capability
range|:
name|bundleInfo
operator|.
name|getCapabilities
argument_list|()
control|)
block|{
name|populateCapabilities
argument_list|(
name|capability
operator|.
name|getType
argument_list|()
argument_list|,
name|capability
operator|.
name|getName
argument_list|()
argument_list|,
name|capability
operator|.
name|getVersion
argument_list|()
argument_list|,
name|bundleInfo
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|populateCapabilities
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|n
parameter_list|,
name|Version
name|version
parameter_list|,
name|BundleInfo
name|bundleInfo
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|>
name|map
init|=
name|bundleByCapabilities
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|bundleByCapabilities
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
name|bundleReferences
init|=
name|map
operator|.
name|get
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundleReferences
operator|==
literal|null
condition|)
block|{
name|bundleReferences
operator|=
operator|new
name|HashSet
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|n
argument_list|,
name|bundleReferences
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|bundleReferences
operator|.
name|add
argument_list|(
operator|new
name|BundleCapabilityAndLocation
argument_list|(
name|type
argument_list|,
name|n
argument_list|,
name|version
argument_list|,
name|bundleInfo
argument_list|)
argument_list|)
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"The repo did already contains "
operator|+
name|n
operator|+
literal|"#"
operator|+
name|version
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|BundleInfo
argument_list|>
name|getBundles
parameter_list|()
block|{
return|return
name|bundles
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|BundleCapabilityAndLocation
argument_list|>
argument_list|>
argument_list|>
name|getBundleByCapabilities
parameter_list|()
block|{
return|return
name|bundleByCapabilities
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
name|bundles
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|bundles
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|bundles
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|BundleRepo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|BundleRepo
name|other
init|=
operator|(
name|BundleRepo
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|bundles
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|bundles
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|bundles
operator|.
name|equals
argument_list|(
name|other
operator|.
name|bundles
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

