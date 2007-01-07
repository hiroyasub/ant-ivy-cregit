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
name|resolver
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
name|Artifact
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
name|DependencyDescriptor
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
name|Ivy
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
name|IvyAware
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
name|IvyNode
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
name|LatestStrategy
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
name|ModuleDescriptor
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
name|ResolveData
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
name|ResolvedModuleRevision
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
name|matcher
operator|.
name|Matcher
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
name|matcher
operator|.
name|NoMatcher
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
name|namespace
operator|.
name|NameSpaceHelper
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
name|report
operator|.
name|ArtifactDownloadReport
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
name|report
operator|.
name|DownloadReport
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
name|report
operator|.
name|DownloadStatus
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
comment|/**  * This abstract resolver only provides handling for resolver name  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractResolver
implements|implements
name|DependencyResolver
implements|,
name|IvyAware
implements|,
name|HasLatestStrategy
block|{
comment|/**      * True if parsed ivy files should be validated against xsd, false if they should not,      * null if default behaviour should be used      */
specifier|private
name|Boolean
name|_validate
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|String
name|_changingPattern
decl_stmt|;
specifier|private
name|String
name|_changingMatcherName
init|=
name|PatternMatcher
operator|.
name|EXACT_OR_REGEXP
decl_stmt|;
specifier|private
name|Ivy
name|_ivy
decl_stmt|;
comment|/**      * The latest strategy to use to find latest among several artifacts      */
specifier|private
name|LatestStrategy
name|_latestStrategy
decl_stmt|;
specifier|private
name|String
name|_latestStrategyName
decl_stmt|;
comment|/**      * The namespace to which this resolver belongs      */
specifier|private
name|Namespace
name|_namespace
decl_stmt|;
specifier|private
name|String
name|_namespaceName
decl_stmt|;
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|_ivy
return|;
block|}
specifier|public
name|void
name|setIvy
parameter_list|(
name|Ivy
name|ivy
parameter_list|)
block|{
name|_ivy
operator|=
name|ivy
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * this method should remove sensitive information from a location to be displayed in a log      * @param name location      * @return location with sensitive data replaced by stars      */
specifier|public
name|String
name|hidePassword
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
return|;
block|}
specifier|protected
name|boolean
name|doValidate
parameter_list|(
name|ResolveData
name|data
parameter_list|)
block|{
if|if
condition|(
name|_validate
operator|!=
literal|null
condition|)
block|{
return|return
name|_validate
operator|.
name|booleanValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|data
operator|.
name|isValidate
argument_list|()
return|;
block|}
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|_validate
operator|==
literal|null
condition|?
literal|true
else|:
name|_validate
operator|.
name|booleanValue
argument_list|()
return|;
block|}
specifier|public
name|void
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|_validate
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|validate
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|checkInterrupted
parameter_list|()
block|{
if|if
condition|(
name|_ivy
operator|!=
literal|null
condition|)
block|{
name|_ivy
operator|.
name|checkInterrupted
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|reportFailure
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no failure report implemented by "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|reportFailure
parameter_list|(
name|Artifact
name|art
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"no failure report implemented by "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|token
parameter_list|,
name|Map
name|otherTokenValues
parameter_list|)
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
block|{
return|return
operator|new
name|OrganisationEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|ModuleEntry
index|[]
name|listModules
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
return|return
operator|new
name|ModuleEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
block|{
return|return
operator|new
name|RevisionEntry
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|void
name|dumpConfig
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|" ["
operator|+
name|getTypeName
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tchangingPattern: "
operator|+
name|getChangingPattern
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tchangingMatcher: "
operator|+
name|getChangingMatcherName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Default implementation actually download the artifact      * Subclasses should overwrite this to avoid the download      */
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|DownloadReport
name|dr
init|=
name|download
argument_list|(
operator|new
name|Artifact
index|[]
block|{
name|artifact
block|}
argument_list|,
name|getIvy
argument_list|()
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getDefaultCache
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|adr
init|=
name|dr
operator|.
name|getArtifactReport
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|DownloadStatus
operator|.
name|FAILED
return|;
block|}
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
return|return
name|download
argument_list|(
name|artifacts
argument_list|,
name|ivy
argument_list|,
name|cache
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|LatestStrategy
name|getLatestStrategy
parameter_list|()
block|{
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getIvy
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_latestStrategyName
operator|!=
literal|null
operator|&&
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|_latestStrategyName
argument_list|)
condition|)
block|{
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getLatestStrategy
argument_list|(
name|_latestStrategyName
argument_list|)
expr_stmt|;
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"unknown latest strategy: "
operator|+
name|_latestStrategyName
argument_list|)
expr_stmt|;
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|_latestStrategy
operator|=
name|getIvy
argument_list|()
operator|.
name|getDefaultLatestStrategy
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|getName
argument_list|()
operator|+
literal|": no latest strategy defined: using default"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no ivy instance found: impossible to get a latest strategy without ivy instance"
argument_list|)
throw|;
block|}
block|}
return|return
name|_latestStrategy
return|;
block|}
specifier|public
name|void
name|setLatestStrategy
parameter_list|(
name|LatestStrategy
name|latestStrategy
parameter_list|)
block|{
name|_latestStrategy
operator|=
name|latestStrategy
expr_stmt|;
block|}
specifier|public
name|void
name|setLatest
parameter_list|(
name|String
name|strategyName
parameter_list|)
block|{
name|_latestStrategyName
operator|=
name|strategyName
expr_stmt|;
block|}
specifier|public
name|String
name|getLatest
parameter_list|()
block|{
if|if
condition|(
name|_latestStrategyName
operator|==
literal|null
condition|)
block|{
name|_latestStrategyName
operator|=
literal|"default"
expr_stmt|;
block|}
return|return
name|_latestStrategyName
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
if|if
condition|(
name|_namespace
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getIvy
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|_namespaceName
operator|!=
literal|null
condition|)
block|{
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getNamespace
argument_list|(
name|_namespaceName
argument_list|)
expr_stmt|;
if|if
condition|(
name|_namespace
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"unknown namespace: "
operator|+
name|_namespaceName
argument_list|)
expr_stmt|;
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getSystemNamespace
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|_namespace
operator|=
name|getIvy
argument_list|()
operator|.
name|getSystemNamespace
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
name|getName
argument_list|()
operator|+
literal|": no namespace defined: using system"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|getName
argument_list|()
operator|+
literal|": no namespace defined nor ivy instance: using system namespace"
argument_list|)
expr_stmt|;
name|_namespace
operator|=
name|Namespace
operator|.
name|SYSTEM_NAMESPACE
expr_stmt|;
block|}
block|}
return|return
name|_namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespaceName
parameter_list|)
block|{
name|_namespaceName
operator|=
name|namespaceName
expr_stmt|;
block|}
comment|// Namespace conversion methods
specifier|protected
name|ModuleDescriptor
name|toSystem
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
return|return
name|NameSpaceHelper
operator|.
name|toSystem
argument_list|(
name|md
argument_list|,
name|getNamespace
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Artifact
name|fromSystem
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|artifact
argument_list|,
name|getNamespace
argument_list|()
operator|.
name|getFromSystemTransformer
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Artifact
name|toSystem
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|artifact
argument_list|,
name|getNamespace
argument_list|()
operator|.
name|getToSystemTransformer
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ResolvedModuleRevision
name|toSystem
parameter_list|(
name|ResolvedModuleRevision
name|rmr
parameter_list|)
block|{
return|return
name|NameSpaceHelper
operator|.
name|toSystem
argument_list|(
name|rmr
argument_list|,
name|getNamespace
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|ModuleRevisionId
name|toSystem
parameter_list|(
name|ModuleRevisionId
name|resolvedMrid
parameter_list|)
block|{
return|return
name|getNamespace
argument_list|()
operator|.
name|getToSystemTransformer
argument_list|()
operator|.
name|transform
argument_list|(
name|resolvedMrid
argument_list|)
return|;
block|}
specifier|protected
name|DependencyDescriptor
name|fromSystem
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
return|return
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|dd
argument_list|,
name|getNamespace
argument_list|()
operator|.
name|getFromSystemTransformer
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|protected
name|IvyNode
name|getSystemNode
parameter_list|(
name|ResolveData
name|data
parameter_list|,
name|ModuleRevisionId
name|resolvedMrid
parameter_list|)
block|{
return|return
name|data
operator|.
name|getNode
argument_list|(
name|toSystem
argument_list|(
name|resolvedMrid
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|ResolvedModuleRevision
name|findModuleInCache
parameter_list|(
name|ResolveData
name|data
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|ResolvedModuleRevision
name|moduleFromCache
init|=
name|data
operator|.
name|getIvy
argument_list|()
operator|.
name|findModuleInCache
argument_list|(
name|toSystem
argument_list|(
name|mrid
argument_list|)
argument_list|,
name|data
operator|.
name|getCache
argument_list|()
argument_list|,
name|doValidate
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|moduleFromCache
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|(
name|getName
argument_list|()
operator|==
literal|null
condition|?
name|moduleFromCache
operator|.
name|getResolver
argument_list|()
operator|.
name|getName
argument_list|()
operator|==
literal|null
else|:
name|moduleFromCache
operator|.
name|getResolver
argument_list|()
operator|==
literal|null
condition|?
literal|false
else|:
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|moduleFromCache
operator|.
name|getResolver
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|moduleFromCache
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"found module in cache but with a different resolver: discarding: "
operator|+
name|moduleFromCache
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|String
name|getChangingMatcherName
parameter_list|()
block|{
return|return
name|_changingMatcherName
return|;
block|}
specifier|public
name|void
name|setChangingMatcher
parameter_list|(
name|String
name|changingMatcherName
parameter_list|)
block|{
name|_changingMatcherName
operator|=
name|changingMatcherName
expr_stmt|;
block|}
specifier|public
name|String
name|getChangingPattern
parameter_list|()
block|{
return|return
name|_changingPattern
return|;
block|}
specifier|public
name|void
name|setChangingPattern
parameter_list|(
name|String
name|changingPattern
parameter_list|)
block|{
name|_changingPattern
operator|=
name|changingPattern
expr_stmt|;
block|}
specifier|public
name|Matcher
name|getChangingMatcher
parameter_list|()
block|{
if|if
condition|(
name|_changingPattern
operator|==
literal|null
condition|)
block|{
return|return
name|NoMatcher
operator|.
name|INSTANCE
return|;
block|}
name|PatternMatcher
name|matcher
init|=
name|_ivy
operator|.
name|getMatcher
argument_list|(
name|_changingMatcherName
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unknown matcher '"
operator|+
name|_changingMatcherName
operator|+
literal|"'. It is set as changing matcher in "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|matcher
operator|.
name|getMatcher
argument_list|(
name|_changingPattern
argument_list|)
return|;
block|}
block|}
end_class

end_unit

