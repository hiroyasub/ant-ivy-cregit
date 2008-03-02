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
name|install
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
name|matcher
operator|.
name|PatternMatcher
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterHelper
import|;
end_import

begin_class
specifier|public
class|class
name|InstallOptions
block|{
specifier|private
name|boolean
name|transitive
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|overwrite
init|=
literal|false
decl_stmt|;
specifier|private
name|Filter
name|artifactFilter
init|=
name|FilterHelper
operator|.
name|NO_FILTER
decl_stmt|;
specifier|private
name|String
name|matcherName
init|=
name|PatternMatcher
operator|.
name|EXACT
decl_stmt|;
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|transitive
return|;
block|}
specifier|public
name|InstallOptions
name|setTransitive
parameter_list|(
name|boolean
name|transitive
parameter_list|)
block|{
name|this
operator|.
name|transitive
operator|=
name|transitive
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
name|InstallOptions
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
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|overwrite
return|;
block|}
specifier|public
name|InstallOptions
name|setOverwrite
parameter_list|(
name|boolean
name|overwrite
parameter_list|)
block|{
name|this
operator|.
name|overwrite
operator|=
name|overwrite
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Filter
name|getArtifactFilter
parameter_list|()
block|{
return|return
name|artifactFilter
return|;
block|}
specifier|public
name|InstallOptions
name|setArtifactFilter
parameter_list|(
name|Filter
name|artifactFilter
parameter_list|)
block|{
name|this
operator|.
name|artifactFilter
operator|=
name|artifactFilter
operator|==
literal|null
condition|?
name|FilterHelper
operator|.
name|NO_FILTER
else|:
name|artifactFilter
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getMatcherName
parameter_list|()
block|{
return|return
name|matcherName
return|;
block|}
specifier|public
name|InstallOptions
name|setMatcherName
parameter_list|(
name|String
name|matcherName
parameter_list|)
block|{
name|this
operator|.
name|matcherName
operator|=
name|matcherName
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit
