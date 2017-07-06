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
name|module
operator|.
name|descriptor
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
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|IvyPatternHelper
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ArtifactId
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
name|extendable
operator|.
name|UnmodifiableExtendableItem
import|;
end_import

begin_comment
comment|/**  * Abstract class used as implementation for both {@link IncludeRule} and {@link ExcludeRule}, since  * their contract is almost identical  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractIncludeExcludeRule
extends|extends
name|UnmodifiableExtendableItem
implements|implements
name|ConfigurationAware
block|{
specifier|private
name|ArtifactId
name|id
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|confs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|PatternMatcher
name|patternMatcher
decl_stmt|;
specifier|public
name|AbstractIncludeExcludeRule
parameter_list|(
name|ArtifactId
name|aid
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
name|id
operator|=
name|aid
expr_stmt|;
name|patternMatcher
operator|=
name|matcher
expr_stmt|;
name|initStandardAttributes
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initStandardAttributes
parameter_list|()
block|{
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|id
operator|.
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|id
operator|.
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|ARTIFACT_KEY
argument_list|,
name|id
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
name|id
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
name|id
operator|.
name|getExt
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
literal|"matcher"
argument_list|,
name|patternMatcher
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
operator|!
operator|(
name|obj
operator|instanceof
name|AbstractIncludeExcludeRule
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AbstractIncludeExcludeRule
name|rule
init|=
operator|(
name|AbstractIncludeExcludeRule
operator|)
name|obj
decl_stmt|;
return|return
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|rule
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getId
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * Add a configuration for this rule      *      * @param conf String      */
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|confs
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArtifactId
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
name|confs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|PatternMatcher
name|getMatcher
parameter_list|()
block|{
return|return
name|patternMatcher
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|id
operator|+
literal|"("
operator|+
name|confs
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

