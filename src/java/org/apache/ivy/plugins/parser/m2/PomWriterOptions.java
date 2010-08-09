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
name|parser
operator|.
name|m2
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
name|parser
operator|.
name|m2
operator|.
name|PomModuleDescriptorWriter
operator|.
name|ConfigurationScopeMapping
import|;
end_import

begin_class
specifier|public
class|class
name|PomWriterOptions
block|{
specifier|private
name|String
index|[]
name|confs
decl_stmt|;
specifier|private
name|String
name|licenseHeader
decl_stmt|;
specifier|private
name|ConfigurationScopeMapping
name|mapping
decl_stmt|;
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
return|return
name|confs
return|;
block|}
specifier|public
name|PomWriterOptions
name|setConfs
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getLicenseHeader
parameter_list|()
block|{
return|return
name|licenseHeader
return|;
block|}
specifier|public
name|PomWriterOptions
name|setLicenseHeader
parameter_list|(
name|String
name|licenseHeader
parameter_list|)
block|{
name|this
operator|.
name|licenseHeader
operator|=
name|licenseHeader
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ConfigurationScopeMapping
name|getMapping
parameter_list|()
block|{
return|return
name|mapping
return|;
block|}
specifier|public
name|PomWriterOptions
name|setMapping
parameter_list|(
name|ConfigurationScopeMapping
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

