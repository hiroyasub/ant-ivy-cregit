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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
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
specifier|private
name|boolean
name|printIvyInfo
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|artifactName
decl_stmt|;
specifier|private
name|String
name|artifactPackaging
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ExtraDependency
argument_list|>
name|extraDependencies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|File
name|template
decl_stmt|;
specifier|public
name|File
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
specifier|public
name|PomWriterOptions
name|setTemplate
parameter_list|(
name|File
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
return|return
name|this
return|;
block|}
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
if|if
condition|(
name|this
operator|.
name|licenseHeader
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|licenseHeader
operator|=
name|this
operator|.
name|licenseHeader
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
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
specifier|public
name|boolean
name|isPrintIvyInfo
parameter_list|()
block|{
return|return
name|printIvyInfo
return|;
block|}
specifier|public
name|PomWriterOptions
name|setPrintIvyInfo
parameter_list|(
name|boolean
name|printIvyInfo
parameter_list|)
block|{
name|this
operator|.
name|printIvyInfo
operator|=
name|printIvyInfo
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|List
argument_list|<
name|ExtraDependency
argument_list|>
name|getExtraDependencies
parameter_list|()
block|{
return|return
name|extraDependencies
return|;
block|}
specifier|public
name|PomWriterOptions
name|setExtraDependencies
parameter_list|(
name|List
argument_list|<
name|ExtraDependency
argument_list|>
name|extraDependencies
parameter_list|)
block|{
name|this
operator|.
name|extraDependencies
operator|=
name|extraDependencies
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getArtifactName
parameter_list|()
block|{
return|return
name|artifactName
return|;
block|}
specifier|public
name|PomWriterOptions
name|setArtifactName
parameter_list|(
name|String
name|artifactName
parameter_list|)
block|{
name|this
operator|.
name|artifactName
operator|=
name|artifactName
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getArtifactPackaging
parameter_list|()
block|{
return|return
name|artifactPackaging
return|;
block|}
specifier|public
name|PomWriterOptions
name|setArtifactPackaging
parameter_list|(
name|String
name|artifactPackaging
parameter_list|)
block|{
name|this
operator|.
name|artifactPackaging
operator|=
name|artifactPackaging
expr_stmt|;
return|return
name|this
return|;
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
name|PomWriterOptions
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
return|return
name|this
return|;
block|}
specifier|public
specifier|static
class|class
name|ConfigurationScopeMapping
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|scopes
decl_stmt|;
specifier|public
name|ConfigurationScopeMapping
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|scopesMapping
parameter_list|)
block|{
comment|// preserve the order
name|this
operator|.
name|scopes
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|scopesMapping
argument_list|)
expr_stmt|;
block|}
comment|/**          * Returns the scope mapped to the given configuration array.          *          * @param confs          *            the configurations for which the scope should be returned          * @return the scope to which the conf is mapped          */
specifier|public
name|String
name|getScope
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
for|for
control|(
name|String
name|conf
range|:
name|confs
control|)
block|{
if|if
condition|(
name|scopes
operator|.
name|containsKey
argument_list|(
name|conf
argument_list|)
condition|)
block|{
return|return
name|scopes
operator|.
name|get
argument_list|(
name|conf
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
name|getScope
argument_list|(
name|confs
argument_list|)
operator|==
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|ExtraDependency
block|{
specifier|private
name|String
name|group
decl_stmt|;
specifier|private
name|String
name|artifact
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|boolean
name|optional
decl_stmt|;
specifier|public
name|ExtraDependency
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|scope
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|,
name|boolean
name|optional
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
name|this
operator|.
name|optional
operator|=
name|optional
expr_stmt|;
block|}
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
specifier|public
name|String
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|optional
return|;
block|}
block|}
block|}
end_class

end_unit

