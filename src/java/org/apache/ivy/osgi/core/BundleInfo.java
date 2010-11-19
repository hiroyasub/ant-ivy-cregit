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
name|core
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|util
operator|.
name|Version
import|;
end_import

begin_comment
comment|/**  * Bundle info extracted from the bundle manifest.  *   */
end_comment

begin_class
specifier|public
class|class
name|BundleInfo
block|{
specifier|public
specifier|static
specifier|final
name|Version
name|DEFAULT_VERSION
init|=
operator|new
name|Version
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PACKAGE_TYPE
init|=
literal|"package"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BUNDLE_TYPE
init|=
literal|"bundle"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_TYPE
init|=
literal|"service"
decl_stmt|;
specifier|private
name|String
name|symbolicName
decl_stmt|;
specifier|private
name|String
name|presentationName
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|Version
name|version
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|requirements
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|BundleRequirement
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|BundleCapability
argument_list|>
name|capabilities
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|BundleCapability
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|executionEnvironments
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|String
name|documentation
decl_stmt|;
specifier|private
name|String
name|license
decl_stmt|;
specifier|private
name|Integer
name|size
decl_stmt|;
specifier|private
name|String
name|uri
decl_stmt|;
specifier|public
name|BundleInfo
parameter_list|(
name|String
name|name
parameter_list|,
name|Version
name|version
parameter_list|)
block|{
name|this
operator|.
name|symbolicName
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"BundleInfo [executionEnvironments="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|executionEnvironments
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", capabilities="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|capabilities
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", requirements="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|requirements
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", symbolicName="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|symbolicName
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", version="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getSymbolicName
parameter_list|()
block|{
return|return
name|symbolicName
return|;
block|}
specifier|public
name|Version
name|getVersion
parameter_list|()
block|{
return|return
name|version
operator|==
literal|null
condition|?
name|DEFAULT_VERSION
else|:
name|version
return|;
block|}
specifier|public
name|Version
name|getRawVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setPresentationName
parameter_list|(
name|String
name|presentationName
parameter_list|)
block|{
name|this
operator|.
name|presentationName
operator|=
name|presentationName
expr_stmt|;
block|}
specifier|public
name|String
name|getPresentationName
parameter_list|()
block|{
return|return
name|presentationName
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDocumentation
parameter_list|(
name|String
name|documentation
parameter_list|)
block|{
name|this
operator|.
name|documentation
operator|=
name|documentation
expr_stmt|;
block|}
specifier|public
name|String
name|getDocumentation
parameter_list|()
block|{
return|return
name|documentation
return|;
block|}
specifier|public
name|void
name|setLicense
parameter_list|(
name|String
name|license
parameter_list|)
block|{
name|this
operator|.
name|license
operator|=
name|license
expr_stmt|;
block|}
specifier|public
name|String
name|getLicense
parameter_list|()
block|{
return|return
name|license
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|Integer
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|addRequirement
parameter_list|(
name|BundleRequirement
name|requirement
parameter_list|)
block|{
name|requirements
operator|.
name|add
argument_list|(
name|requirement
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|getRequirements
parameter_list|()
block|{
return|return
name|requirements
return|;
block|}
specifier|public
name|void
name|addCapability
parameter_list|(
name|BundleCapability
name|capability
parameter_list|)
block|{
name|capabilities
operator|.
name|add
argument_list|(
name|capability
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|BundleCapability
argument_list|>
name|getCapabilities
parameter_list|()
block|{
return|return
name|capabilities
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExecutionEnvironments
parameter_list|()
block|{
return|return
name|executionEnvironments
return|;
block|}
specifier|public
name|void
name|setExecutionEnvironments
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|executionEnvironment
parameter_list|)
block|{
name|this
operator|.
name|executionEnvironments
operator|=
name|executionEnvironment
expr_stmt|;
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
name|capabilities
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|capabilities
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|requirements
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|requirements
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|symbolicName
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|symbolicName
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|version
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|version
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|executionEnvironments
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|executionEnvironments
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
name|BundleInfo
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|BundleInfo
name|other
init|=
operator|(
name|BundleInfo
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|capabilities
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|capabilities
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
name|capabilities
operator|.
name|equals
argument_list|(
name|other
operator|.
name|capabilities
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|requirements
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|requirements
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
name|requirements
operator|.
name|equals
argument_list|(
name|other
operator|.
name|requirements
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|symbolicName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|symbolicName
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
name|symbolicName
operator|.
name|equals
argument_list|(
name|other
operator|.
name|symbolicName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|version
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
name|version
operator|.
name|equals
argument_list|(
name|other
operator|.
name|version
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|executionEnvironments
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|executionEnvironments
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
name|executionEnvironments
operator|.
name|equals
argument_list|(
name|other
operator|.
name|executionEnvironments
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
annotation|@
name|Deprecated
specifier|public
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|getRequires
parameter_list|()
block|{
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|set
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|BundleRequirement
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleRequirement
name|requirement
range|:
name|requirements
control|)
block|{
if|if
condition|(
name|requirement
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|BUNDLE_TYPE
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|requirement
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|getImports
parameter_list|()
block|{
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|set
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|BundleRequirement
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleRequirement
name|requirement
range|:
name|requirements
control|)
block|{
if|if
condition|(
name|requirement
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|PACKAGE_TYPE
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|requirement
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|Set
argument_list|<
name|ExportPackage
argument_list|>
name|getExports
parameter_list|()
block|{
name|Set
argument_list|<
name|ExportPackage
argument_list|>
name|set
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ExportPackage
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleCapability
name|capability
range|:
name|capabilities
control|)
block|{
if|if
condition|(
name|capability
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|PACKAGE_TYPE
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
operator|(
name|ExportPackage
operator|)
name|capability
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|Set
argument_list|<
name|BundleCapability
argument_list|>
name|getServices
parameter_list|()
block|{
name|Set
argument_list|<
name|BundleCapability
argument_list|>
name|set
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|BundleCapability
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|BundleCapability
name|capability
range|:
name|capabilities
control|)
block|{
if|if
condition|(
name|capability
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|SERVICE_TYPE
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|capability
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
block|}
end_class

end_unit

