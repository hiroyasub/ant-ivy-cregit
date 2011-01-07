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
name|Require
block|{
specifier|private
name|String
name|plugin
decl_stmt|;
specifier|private
name|String
name|feature
decl_stmt|;
specifier|private
name|Version
name|version
decl_stmt|;
specifier|private
name|String
name|match
decl_stmt|;
specifier|private
name|String
name|filter
decl_stmt|;
specifier|public
name|void
name|setFeature
parameter_list|(
name|String
name|feature
parameter_list|)
block|{
name|this
operator|.
name|feature
operator|=
name|feature
expr_stmt|;
block|}
specifier|public
name|String
name|getFeature
parameter_list|()
block|{
return|return
name|feature
return|;
block|}
specifier|public
name|void
name|setPlugin
parameter_list|(
name|String
name|plugin
parameter_list|)
block|{
name|this
operator|.
name|plugin
operator|=
name|plugin
expr_stmt|;
block|}
specifier|public
name|String
name|getPlugin
parameter_list|()
block|{
return|return
name|plugin
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|Version
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
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
name|setMatch
parameter_list|(
name|String
name|match
parameter_list|)
block|{
name|this
operator|.
name|match
operator|=
name|match
expr_stmt|;
block|}
specifier|public
name|String
name|getMatch
parameter_list|()
block|{
return|return
name|match
return|;
block|}
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
block|}
end_class

end_unit

