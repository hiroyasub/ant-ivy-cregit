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
name|core
operator|.
name|cache
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
name|namespace
operator|.
name|Namespace
import|;
end_import

begin_class
specifier|public
class|class
name|CacheMetadataOptions
extends|extends
name|CacheDownloadOptions
block|{
specifier|private
name|boolean
name|validate
init|=
literal|false
decl_stmt|;
specifier|private
name|Namespace
name|namespace
init|=
name|Namespace
operator|.
name|SYSTEM_NAMESPACE
decl_stmt|;
specifier|private
name|Boolean
name|isCheckmodified
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|changingMatcherName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|changingPattern
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|checkTTL
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|useCacheOnly
init|=
literal|false
decl_stmt|;
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setNamespace
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Boolean
name|isCheckmodified
parameter_list|()
block|{
return|return
name|isCheckmodified
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setCheckmodified
parameter_list|(
name|Boolean
name|isCheckmodified
parameter_list|)
block|{
name|this
operator|.
name|isCheckmodified
operator|=
name|isCheckmodified
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getChangingMatcherName
parameter_list|()
block|{
return|return
name|changingMatcherName
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setChangingMatcherName
parameter_list|(
name|String
name|changingMatcherName
parameter_list|)
block|{
name|this
operator|.
name|changingMatcherName
operator|=
name|changingMatcherName
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getChangingPattern
parameter_list|()
block|{
return|return
name|changingPattern
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setChangingPattern
parameter_list|(
name|String
name|changingPattern
parameter_list|)
block|{
name|this
operator|.
name|changingPattern
operator|=
name|changingPattern
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setCheckTTL
parameter_list|(
name|boolean
name|checkTTL
parameter_list|)
block|{
name|this
operator|.
name|checkTTL
operator|=
name|checkTTL
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isCheckTTL
parameter_list|()
block|{
return|return
name|checkTTL
return|;
block|}
specifier|public
name|CacheMetadataOptions
name|setUseCacheOnly
parameter_list|(
name|boolean
name|useCacheOnly
parameter_list|)
block|{
name|this
operator|.
name|useCacheOnly
operator|=
name|useCacheOnly
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUseCacheOnly
parameter_list|()
block|{
return|return
name|useCacheOnly
return|;
block|}
block|}
end_class

end_unit

