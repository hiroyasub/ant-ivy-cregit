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
name|check
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|resolve
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
name|core
operator|.
name|resolve
operator|.
name|ResolveEngine
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
name|resolve
operator|.
name|ResolveOptions
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
name|resolve
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
name|plugins
operator|.
name|parser
operator|.
name|ModuleDescriptorParserRegistry
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

begin_class
specifier|public
class|class
name|CheckEngine
block|{
specifier|private
name|CheckEngineSettings
name|settings
decl_stmt|;
specifier|private
name|ResolveEngine
name|resolveEngine
decl_stmt|;
specifier|public
name|CheckEngine
parameter_list|(
name|CheckEngineSettings
name|settings
parameter_list|,
name|ResolveEngine
name|resolveEngine
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|resolveEngine
operator|=
name|resolveEngine
expr_stmt|;
block|}
comment|/**      * Checks the given ivy file using current settings to see if all dependencies are available,      * with good confs. If a resolver name is given, it also checks that the declared publications      * are available in the corresponding resolver. Note that the check is not performed      * recursively, i.e. if a dependency has itself dependencies badly described or not available,      * this check will not discover it.      */
specifier|public
name|boolean
name|check
parameter_list|(
name|URL
name|ivyFile
parameter_list|,
name|String
name|resolvername
parameter_list|)
block|{
try|try
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
comment|// parse ivy file
name|ModuleDescriptor
name|md
init|=
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|settings
argument_list|,
name|ivyFile
argument_list|,
name|settings
operator|.
name|doValidate
argument_list|()
argument_list|)
decl_stmt|;
comment|// check publications if possible
if|if
condition|(
name|resolvername
operator|!=
literal|null
condition|)
block|{
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|resolvername
argument_list|)
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|md
operator|.
name|getConfigurationsNames
argument_list|()
decl_stmt|;
name|Set
name|artifacts
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|artifacts
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getArtifacts
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|art
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|resolver
operator|.
name|exists
argument_list|(
name|art
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"declared publication not found: "
operator|+
name|art
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
comment|// check dependencies
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|ResolveData
name|data
init|=
operator|new
name|ResolveData
argument_list|(
name|resolveEngine
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dds
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
comment|// check master confs
name|String
index|[]
name|masterConfs
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getModuleConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|masterConfs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
operator|!
literal|"*"
operator|.
name|equals
argument_list|(
name|masterConfs
index|[
name|j
index|]
operator|.
name|trim
argument_list|()
argument_list|)
operator|&&
name|md
operator|.
name|getConfiguration
argument_list|(
name|masterConfs
index|[
name|j
index|]
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"dependency required in non existing conf for "
operator|+
name|ivyFile
operator|+
literal|" \n\tin "
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|+
literal|": "
operator|+
name|masterConfs
index|[
name|j
index|]
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|// resolve
name|DependencyResolver
name|resolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyId
argument_list|()
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|resolver
operator|.
name|getDependency
argument_list|(
name|dds
index|[
name|i
index|]
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmr
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"dependency not found in "
operator|+
name|ivyFile
operator|+
literal|":\n\t"
operator|+
name|dds
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|depConfs
init|=
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyConfigurations
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|depConfs
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Arrays
operator|.
name|asList
argument_list|(
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|depConfs
index|[
name|j
index|]
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"dependency configuration is missing for "
operator|+
name|ivyFile
operator|+
literal|"\n\tin "
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|+
literal|": "
operator|+
name|depConfs
index|[
name|j
index|]
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
name|Artifact
index|[]
name|arts
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
operator|.
name|getArtifacts
argument_list|(
name|depConfs
index|[
name|j
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|arts
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|resolver
operator|.
name|exists
argument_list|(
name|arts
index|[
name|k
index|]
argument_list|)
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"dependency artifact is missing for "
operator|+
name|ivyFile
operator|+
literal|"\n\t in "
operator|+
name|dds
index|[
name|i
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
operator|+
literal|": "
operator|+
name|arts
index|[
name|k
index|]
argument_list|)
expr_stmt|;
name|result
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"parse problem on "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"io problem on "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"problem on "
operator|+
name|ivyFile
operator|+
literal|": "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

