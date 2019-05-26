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
operator|.
name|xml
package|;
end_package

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
name|List
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

begin_class
specifier|public
class|class
name|EclipseFeature
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|Version
name|version
decl_stmt|;
specifier|private
name|List
argument_list|<
name|EclipsePlugin
argument_list|>
name|plugins
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Require
argument_list|>
name|requires
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|String
name|license
decl_stmt|;
specifier|public
name|EclipseFeature
parameter_list|(
name|String
name|id
parameter_list|,
name|Version
name|version
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|url
operator|=
literal|"features/"
operator|+
name|id
operator|+
literal|'_'
operator|+
name|version
operator|+
literal|".jar"
expr_stmt|;
block|}
specifier|public
name|void
name|setURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
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
name|Version
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setOS
parameter_list|(
name|String
name|os
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setWS
parameter_list|(
name|String
name|ws
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setNL
parameter_list|(
name|String
name|nl
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setArch
parameter_list|(
name|String
name|arch
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setPatch
parameter_list|(
name|String
name|patch
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|addCategory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
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
name|setCopyright
parameter_list|(
name|String
name|trim
parameter_list|)
block|{
comment|// not useful
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
name|addPlugin
parameter_list|(
name|EclipsePlugin
name|plugin
parameter_list|)
block|{
name|plugins
operator|.
name|add
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|EclipsePlugin
argument_list|>
name|getPlugins
parameter_list|()
block|{
return|return
name|plugins
return|;
block|}
specifier|public
name|void
name|addRequire
parameter_list|(
name|Require
name|require
parameter_list|)
block|{
name|requires
operator|.
name|add
argument_list|(
name|require
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Require
argument_list|>
name|getRequires
parameter_list|()
block|{
return|return
name|requires
return|;
block|}
specifier|public
name|void
name|setApplication
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setPlugin
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setExclusive
parameter_list|(
name|boolean
name|booleanValue
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setPrimary
parameter_list|(
name|boolean
name|booleanValue
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setColocationAffinity
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setProviderName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|setImage
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|id
operator|+
literal|"#"
operator|+
name|version
return|;
block|}
block|}
end_class

end_unit

