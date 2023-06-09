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
name|core
operator|.
name|cache
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
name|HashMap
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
name|RelativeUrlResolver
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
name|ModuleId
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
name|ModuleRevisionId
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
name|status
operator|.
name|StatusManager
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
name|settings
operator|.
name|TimeoutConstraint
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
name|conflict
operator|.
name|ConflictManager
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
name|plugins
operator|.
name|namespace
operator|.
name|Namespace
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
name|parser
operator|.
name|ParserSettings
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
name|resolver
operator|.
name|DependencyResolver
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

begin_comment
comment|/**  * Keep traces of the usage of a ParserSettings in order to check afterwards that the relevant  * settings didn't changed.  *<p>  * A ParserSettingsMonitor provide a ParserSettings that must be used in place of the original one.  *</p>  *<p>  * The current implementation consider that a settings changed iff one of the used variable has  * changed.  *</p>  */
end_comment

begin_class
class|class
name|ParserSettingsMonitor
block|{
specifier|private
name|ParserSettings
name|delegatedSettings
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|substitutes
decl_stmt|;
specifier|public
name|ParserSettingsMonitor
parameter_list|(
name|ParserSettings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|delegatedSettings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|substitutes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return The parser settings that must be used in place of the original settings The returned      *         object delegates all the call to the original settings.      */
specifier|public
name|ParserSettings
name|getMonitoredSettings
parameter_list|()
block|{
return|return
name|monitoredSettings
return|;
block|}
comment|/**      * Free the resource used during the monitoring, keeping only the info required to evaluate      * hasChanged.      */
specifier|public
name|void
name|endMonitoring
parameter_list|()
block|{
name|monitoredSettings
operator|=
literal|null
expr_stmt|;
name|delegatedSettings
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Check if the newSettings is compatible with the original settings that has been monitored.      * Only the info that was actually used is compared.      */
specifier|public
name|boolean
name|hasChanged
parameter_list|(
name|ParserSettings
name|newSettings
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|substitutes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|newSettings
operator|.
name|substitute
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"settings variable has changed for : "
operator|+
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|ParserSettings
name|monitoredSettings
init|=
operator|new
name|ParserSettings
argument_list|()
block|{
specifier|public
name|ConflictManager
name|getConflictManager
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getConflictManager
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|PatternMatcher
name|getMatcher
parameter_list|(
name|String
name|matcherName
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getMatcher
argument_list|(
name|matcherName
argument_list|)
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getNamespace
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
name|RelativeUrlResolver
name|getRelativeUrlResolver
parameter_list|()
block|{
return|return
name|delegatedSettings
operator|.
name|getRelativeUrlResolver
argument_list|()
return|;
block|}
specifier|public
name|ResolutionCacheManager
name|getResolutionCacheManager
parameter_list|()
block|{
return|return
name|delegatedSettings
operator|.
name|getResolutionCacheManager
argument_list|()
return|;
block|}
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|(
name|ModuleRevisionId
name|mRevId
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getResolver
argument_list|(
name|mRevId
argument_list|)
return|;
block|}
specifier|public
name|StatusManager
name|getStatusManager
parameter_list|()
block|{
return|return
name|delegatedSettings
operator|.
name|getStatusManager
argument_list|()
return|;
block|}
specifier|public
name|File
name|resolveFile
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|resolveFile
argument_list|(
name|filename
argument_list|)
return|;
block|}
specifier|public
name|String
name|getDefaultBranch
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getDefaultBranch
argument_list|(
name|moduleId
argument_list|)
return|;
block|}
specifier|public
name|Namespace
name|getContextNamespace
parameter_list|()
block|{
return|return
name|delegatedSettings
operator|.
name|getContextNamespace
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|substitute
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|strings
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|substituted
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|strings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|substituted
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|substitute
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|substituted
return|;
block|}
specifier|public
name|String
name|substitute
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
name|r
init|=
name|delegatedSettings
operator|.
name|substitute
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
name|r
argument_list|)
condition|)
block|{
name|substitutes
operator|.
name|put
argument_list|(
name|value
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getVariable
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|TimeoutConstraint
name|getTimeoutConstraint
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|delegatedSettings
operator|.
name|getTimeoutConstraint
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

