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
name|resolve
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|descriptor
operator|.
name|DependencyArtifactDescriptor
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
name|descriptor
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|IncludeRule
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
name|descriptor
operator|.
name|WorkspaceModuleDescriptor
import|;
end_import

begin_comment
comment|/**  * Class collecting usage data for an IvyNode.  *<p>  * Usage data contains the configurations required by callers for each root module configuration,  * the configurations required by caller node and caller configuration, dependency artifacts  * descriptors declared by callers, include rules declared by callers, and blacklisted data by root  * module conf.  *</p>  */
end_comment

begin_class
specifier|public
class|class
name|IvyNodeUsage
block|{
specifier|private
specifier|static
specifier|final
class|class
name|NodeConf
block|{
specifier|private
name|IvyNode
name|node
decl_stmt|;
specifier|private
name|String
name|conf
decl_stmt|;
specifier|public
name|NodeConf
parameter_list|(
name|IvyNode
name|node
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"node must not null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"conf must not null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
specifier|final
name|IvyNode
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|NodeConf
operator|&&
name|getNode
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|NodeConf
operator|)
name|obj
operator|)
operator|.
name|getNode
argument_list|()
argument_list|)
operator|&&
name|getConf
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|NodeConf
operator|)
name|obj
operator|)
operator|.
name|getConf
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// CheckStyle:MagicNumber| OFF
name|int
name|hash
init|=
literal|33
decl_stmt|;
name|hash
operator|+=
name|getNode
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|17
expr_stmt|;
name|hash
operator|+=
name|getConf
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|17
expr_stmt|;
comment|// CheckStyle:MagicNumber| OFF
return|return
name|hash
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"NodeConf("
operator|+
name|conf
operator|+
literal|")"
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|Depender
block|{
specifier|private
name|DependencyDescriptor
name|dd
decl_stmt|;
specifier|private
name|String
name|dependerConf
decl_stmt|;
specifier|public
name|Depender
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|String
name|dependerConf
parameter_list|)
block|{
name|this
operator|.
name|dd
operator|=
name|dd
expr_stmt|;
name|this
operator|.
name|dependerConf
operator|=
name|dependerConf
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|dd
operator|+
literal|" ["
operator|+
name|dependerConf
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
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
name|Depender
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Depender
name|other
init|=
operator|(
name|Depender
operator|)
name|obj
decl_stmt|;
return|return
name|other
operator|.
name|dd
operator|==
name|dd
operator|&&
name|other
operator|.
name|dependerConf
operator|.
name|equals
argument_list|(
name|dependerConf
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
literal|33
decl_stmt|;
name|hash
operator|+=
name|dd
operator|.
name|hashCode
argument_list|()
operator|*
literal|13
expr_stmt|;
name|hash
operator|+=
name|dependerConf
operator|.
name|hashCode
argument_list|()
operator|*
literal|13
expr_stmt|;
return|return
name|hash
return|;
block|}
block|}
specifier|private
name|IvyNode
name|node
decl_stmt|;
comment|// used to know which configurations of the dependency are required
comment|// for each root module configuration
comment|// rootConfName -> confNames
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|rootModuleConfs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|NodeConf
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|requiredConfs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|Depender
argument_list|>
argument_list|>
name|dependers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// rootModuleConf -> black list
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|IvyNodeBlacklist
argument_list|>
name|blacklisted
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|IvyNodeUsage
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|getRequiredConfigurations
parameter_list|(
name|IvyNode
name|in
parameter_list|,
name|String
name|inConf
parameter_list|)
block|{
return|return
name|requiredConfs
operator|.
name|get
argument_list|(
operator|new
name|NodeConf
argument_list|(
name|in
argument_list|,
name|inConf
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setRequiredConfs
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|String
name|parentConf
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|confs
parameter_list|)
block|{
name|requiredConfs
operator|.
name|put
argument_list|(
operator|new
name|NodeConf
argument_list|(
name|parent
argument_list|,
name|parentConf
argument_list|)
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|confs
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the configurations of the dependency required in a given root module configuration.      *      * @param rootModuleConf ditto      * @return Set&lt;String&gt;      */
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|getConfigurations
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
return|return
name|rootModuleConfs
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|addAndGetConfigurations
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|depConfs
init|=
name|rootModuleConfs
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|depConfs
operator|==
literal|null
condition|)
block|{
name|depConfs
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|rootModuleConfs
operator|.
name|put
argument_list|(
name|rootModuleConf
argument_list|,
name|depConfs
argument_list|)
expr_stmt|;
block|}
return|return
name|depConfs
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|getRootModuleConfigurations
parameter_list|()
block|{
return|return
name|rootModuleConfs
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|void
name|updateDataFrom
parameter_list|(
name|Collection
argument_list|<
name|IvyNodeUsage
argument_list|>
name|usages
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
for|for
control|(
name|IvyNodeUsage
name|usage
range|:
name|usages
control|)
block|{
name|updateDataFrom
argument_list|(
name|usage
argument_list|,
name|rootModuleConf
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateDataFrom
parameter_list|(
name|IvyNodeUsage
name|usage
parameter_list|,
name|String
name|rootModuleConf
parameter_list|)
block|{
comment|// update requiredConfs
name|updateMapOfSet
argument_list|(
name|usage
operator|.
name|requiredConfs
argument_list|,
name|requiredConfs
argument_list|)
expr_stmt|;
comment|// update rootModuleConfs
name|updateMapOfSetForKey
argument_list|(
name|usage
operator|.
name|rootModuleConfs
argument_list|,
name|rootModuleConfs
argument_list|,
name|rootModuleConf
argument_list|)
expr_stmt|;
comment|// update dependencyArtifacts
name|updateMapOfSetForKey
argument_list|(
name|usage
operator|.
name|dependers
argument_list|,
name|dependers
argument_list|,
name|rootModuleConf
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|updateMapOfSet
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|K
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|to
parameter_list|)
block|{
for|for
control|(
name|K
name|key
range|:
name|from
operator|.
name|keySet
argument_list|()
control|)
block|{
name|updateMapOfSetForKey
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|updateMapOfSetForKey
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|from
parameter_list|,
name|Map
argument_list|<
name|K
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|to
parameter_list|,
name|K
name|key
parameter_list|)
block|{
name|Set
argument_list|<
name|V
argument_list|>
name|set
init|=
name|from
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|V
argument_list|>
name|toupdate
init|=
name|to
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|toupdate
operator|!=
literal|null
condition|)
block|{
name|toupdate
operator|.
name|addAll
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|to
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|set
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|addObjectsForConf
parameter_list|(
name|K
name|rootModuleConf
parameter_list|,
name|V
name|objectToAdd
parameter_list|,
name|Map
argument_list|<
name|K
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|Set
argument_list|<
name|V
argument_list|>
name|set
init|=
name|map
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|set
operator|==
literal|null
condition|)
block|{
name|set
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|rootModuleConf
argument_list|,
name|set
argument_list|)
expr_stmt|;
block|}
name|set
operator|.
name|add
argument_list|(
name|objectToAdd
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addUsage
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|DependencyDescriptor
name|dd
parameter_list|,
name|String
name|parentConf
parameter_list|)
block|{
name|addObjectsForConf
argument_list|(
name|rootModuleConf
argument_list|,
operator|new
name|Depender
argument_list|(
name|dd
argument_list|,
name|parentConf
argument_list|)
argument_list|,
name|dependers
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Set
argument_list|<
name|DependencyArtifactDescriptor
argument_list|>
name|getDependencyArtifactsSet
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getDescriptor
argument_list|()
operator|instanceof
name|WorkspaceModuleDescriptor
condition|)
block|{
comment|// for a module in the "workspace" artifacts will be actually declared by the resolver
return|return
literal|null
return|;
block|}
name|Collection
argument_list|<
name|Depender
argument_list|>
name|dependersInConf
init|=
name|dependers
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependersInConf
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|DependencyArtifactDescriptor
argument_list|>
name|dependencyArtifacts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Depender
name|depender
range|:
name|dependersInConf
control|)
block|{
name|DependencyArtifactDescriptor
index|[]
name|dads
init|=
name|depender
operator|.
name|dd
operator|.
name|getDependencyArtifacts
argument_list|(
name|depender
operator|.
name|dependerConf
argument_list|)
decl_stmt|;
name|dependencyArtifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|dads
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dependencyArtifacts
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|IncludeRule
argument_list|>
name|getDependencyIncludesSet
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Collection
argument_list|<
name|Depender
argument_list|>
name|dependersInConf
init|=
name|dependers
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependersInConf
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|IncludeRule
argument_list|>
name|dependencyIncludes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Depender
name|depender
range|:
name|dependersInConf
control|)
block|{
name|IncludeRule
index|[]
name|rules
init|=
name|depender
operator|.
name|dd
operator|.
name|getIncludeRules
argument_list|(
name|depender
operator|.
name|dependerConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|rules
operator|==
literal|null
operator|||
name|rules
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|// no include rule in at least one depender -> we must include everything,
comment|// and so return no include rule at all
return|return
literal|null
return|;
block|}
name|dependencyIncludes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|rules
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dependencyIncludes
return|;
block|}
specifier|protected
name|void
name|removeRootModuleConf
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|rootModuleConfs
operator|.
name|remove
argument_list|(
name|rootModuleConf
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|blacklist
parameter_list|(
name|IvyNodeBlacklist
name|bdata
parameter_list|)
block|{
name|blacklisted
operator|.
name|put
argument_list|(
name|bdata
operator|.
name|getRootModuleConf
argument_list|()
argument_list|,
name|bdata
argument_list|)
expr_stmt|;
block|}
comment|/**      * Indicates if this node has been blacklisted in the given root module conf.      *<p>      * A blacklisted node should be considered as if it doesn't even exist on the repository.      *</p>      *      * @param rootModuleConf      *            the root module conf for which we'd like to know if the node is blacklisted      *      * @return true if this node is blacklisted int he given root module conf, false otherwise      * @see #blacklist(IvyNodeBlacklist)      */
specifier|protected
name|boolean
name|isBlacklisted
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
return|return
name|blacklisted
operator|.
name|containsKey
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
comment|/**      * Returns the blacklist data of this node in the given root module conf, or<code>null</code>      * if this node is not blacklisted in this root module conf.      *      * @param rootModuleConf      *            the root module configuration to consider      * @return the blacklist data if any      */
specifier|protected
name|IvyNodeBlacklist
name|getBlacklistData
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
return|return
name|blacklisted
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
return|;
block|}
specifier|protected
name|IvyNode
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
comment|/**      * Indicates if at least one depender has a transitive dependency descriptor for the given root      * module conf.      *      * @param rootModuleConf      *            the root module conf to consider      * @return<code>true</code> if at least one depender has a transitive dependency descriptor for      *         the given root module conf,<code>false</code> otherwise.      */
specifier|public
name|boolean
name|hasTransitiveDepender
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|Set
argument_list|<
name|Depender
argument_list|>
name|dependersSet
init|=
name|dependers
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|dependersSet
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Depender
name|depender
range|:
name|dependersSet
control|)
block|{
if|if
condition|(
name|depender
operator|.
name|dd
operator|.
name|isTransitive
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

