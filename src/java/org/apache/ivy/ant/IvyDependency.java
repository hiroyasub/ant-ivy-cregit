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
name|ant
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DefaultDependencyDescriptor
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
name|DefaultExcludeRule
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
name|DefaultIncludeRule
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
name|settings
operator|.
name|IvySettings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_class
specifier|public
class|class
name|IvyDependency
block|{
specifier|private
name|List
argument_list|<
name|IvyDependencyConf
argument_list|>
name|confs
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyDependencyConf
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IvyDependencyArtifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyDependencyArtifact
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IvyDependencyExclude
argument_list|>
name|excludes
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyDependencyExclude
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|IvyDependencyInclude
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|IvyDependencyInclude
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|org
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|rev
decl_stmt|;
specifier|private
name|String
name|branch
decl_stmt|;
specifier|private
name|String
name|conf
decl_stmt|;
specifier|private
name|boolean
name|changing
decl_stmt|;
specifier|private
name|boolean
name|force
decl_stmt|;
specifier|private
name|boolean
name|transitive
init|=
literal|true
decl_stmt|;
specifier|public
name|IvyDependencyConf
name|createConf
parameter_list|()
block|{
name|IvyDependencyConf
name|c
init|=
operator|new
name|IvyDependencyConf
argument_list|()
decl_stmt|;
name|confs
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
specifier|public
name|IvyDependencyArtifact
name|createArtifact
parameter_list|()
block|{
name|IvyDependencyArtifact
name|artifact
init|=
operator|new
name|IvyDependencyArtifact
argument_list|()
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|public
name|IvyDependencyExclude
name|createExclude
parameter_list|()
block|{
name|IvyDependencyExclude
name|exclude
init|=
operator|new
name|IvyDependencyExclude
argument_list|()
decl_stmt|;
name|excludes
operator|.
name|add
argument_list|(
name|exclude
argument_list|)
expr_stmt|;
return|return
name|exclude
return|;
block|}
specifier|public
name|IvyDependencyInclude
name|createInclude
parameter_list|()
block|{
name|IvyDependencyInclude
name|include
init|=
operator|new
name|IvyDependencyInclude
argument_list|()
decl_stmt|;
name|includes
operator|.
name|add
argument_list|(
name|include
argument_list|)
expr_stmt|;
return|return
name|include
return|;
block|}
specifier|public
name|String
name|getOrg
parameter_list|()
block|{
return|return
name|org
return|;
block|}
specifier|public
name|void
name|setOrg
parameter_list|(
name|String
name|org
parameter_list|)
block|{
name|this
operator|.
name|org
operator|=
name|org
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
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
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getRev
parameter_list|()
block|{
return|return
name|rev
return|;
block|}
specifier|public
name|void
name|setRev
parameter_list|(
name|String
name|rev
parameter_list|)
block|{
name|this
operator|.
name|rev
operator|=
name|rev
expr_stmt|;
block|}
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
specifier|public
name|void
name|setBranch
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
block|}
specifier|public
name|String
name|getConf
parameter_list|()
block|{
return|return
name|conf
return|;
block|}
specifier|public
name|void
name|setConf
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|boolean
name|isChanging
parameter_list|()
block|{
return|return
name|changing
return|;
block|}
specifier|public
name|void
name|setChanging
parameter_list|(
name|boolean
name|changing
parameter_list|)
block|{
name|this
operator|.
name|changing
operator|=
name|changing
expr_stmt|;
block|}
specifier|public
name|boolean
name|isForce
parameter_list|()
block|{
return|return
name|force
return|;
block|}
specifier|public
name|void
name|setForce
parameter_list|(
name|boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
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
name|void
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
block|}
name|DependencyDescriptor
name|asDependencyDescriptor
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|String
name|masterConf
parameter_list|,
name|IvySettings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|org
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'org' is required on "
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"'name' is required when using inline mode"
argument_list|)
throw|;
block|}
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|name
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|)
decl_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|md
argument_list|,
name|mrid
argument_list|,
name|force
argument_list|,
name|changing
argument_list|,
name|transitive
argument_list|)
decl_stmt|;
if|if
condition|(
name|conf
operator|!=
literal|null
condition|)
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|masterConf
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
name|masterConf
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyDependencyConf
name|c
range|:
name|confs
control|)
block|{
name|c
operator|.
name|addConf
argument_list|(
name|dd
argument_list|,
name|masterConf
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyDependencyArtifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|artifact
operator|.
name|addArtifact
argument_list|(
name|dd
argument_list|,
name|masterConf
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyDependencyExclude
name|exclude
range|:
name|excludes
control|)
block|{
name|DefaultExcludeRule
name|rule
init|=
name|exclude
operator|.
name|asRule
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addExcludeRule
argument_list|(
name|masterConf
argument_list|,
name|rule
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|IvyDependencyInclude
name|include
range|:
name|includes
control|)
block|{
name|DefaultIncludeRule
name|rule
init|=
name|include
operator|.
name|asRule
argument_list|(
name|settings
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addIncludeRule
argument_list|(
name|masterConf
argument_list|,
name|rule
argument_list|)
expr_stmt|;
block|}
return|return
name|dd
return|;
block|}
block|}
end_class

end_unit

