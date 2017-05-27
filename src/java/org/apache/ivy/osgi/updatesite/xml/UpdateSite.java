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
operator|.
name|xml
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

begin_class
specifier|public
class|class
name|UpdateSite
block|{
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|String
name|mirrorsURL
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|boolean
name|pack200
decl_stmt|;
specifier|private
name|URI
name|digestUri
decl_stmt|;
specifier|private
name|List
argument_list|<
name|EclipseFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|EclipseFeature
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
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
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
specifier|public
name|void
name|setMirrorsURL
parameter_list|(
name|String
name|mirrorsURL
parameter_list|)
block|{
name|this
operator|.
name|mirrorsURL
operator|=
name|mirrorsURL
expr_stmt|;
block|}
specifier|public
name|void
name|setPack200
parameter_list|(
name|boolean
name|pack200
parameter_list|)
block|{
name|this
operator|.
name|pack200
operator|=
name|pack200
expr_stmt|;
block|}
specifier|public
name|void
name|setDigestUri
parameter_list|(
name|URI
name|digestUri
parameter_list|)
block|{
name|this
operator|.
name|digestUri
operator|=
name|digestUri
expr_stmt|;
block|}
specifier|public
name|URI
name|getDigestUri
parameter_list|()
block|{
return|return
name|digestUri
return|;
block|}
specifier|public
name|void
name|addFeature
parameter_list|(
name|EclipseFeature
name|feature
parameter_list|)
block|{
name|features
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|EclipseFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
return|;
block|}
specifier|public
name|void
name|setAssociateSitesURL
parameter_list|(
name|String
name|associateSitesURL
parameter_list|)
block|{
comment|// TODO what's that ?
block|}
block|}
end_class

end_unit

