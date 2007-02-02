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
name|util
operator|.
name|extendable
operator|.
name|ExtendableItem
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyDescriptor
extends|extends
name|ExtendableItem
block|{
name|ModuleId
name|getDependencyId
parameter_list|()
function_decl|;
comment|/**      * Used to indicate that this revision must be used in case of conflicts, independently      * of conflicts manager. This only works for direct dependencies, and not transitive ones.      * @return true if this dependency should be used, false if conflicts manager      * can do its work.      */
name|boolean
name|isForce
parameter_list|()
function_decl|;
comment|/**      * Used to indicate that this dependency is a changing one.      * A changing dependency in ivy means that the revision may have its artifacts modified      * without revision change. When new artifacts are published a new ivy file should also      * be published with a new publication date to indicate to ivy that artifacts have changed and that they       * should be downloaded again.       * @return true if this dependency is a changing one      */
name|boolean
name|isChanging
parameter_list|()
function_decl|;
name|boolean
name|isTransitive
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getParentRevisionId
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getDependencyRevisionId
parameter_list|()
function_decl|;
name|String
index|[]
name|getModuleConfigurations
parameter_list|()
function_decl|;
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|,
name|String
name|requestedConfiguration
parameter_list|)
function_decl|;
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
name|moduleConfiguration
parameter_list|)
function_decl|;
name|String
index|[]
name|getDependencyConfigurations
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
function_decl|;
name|Namespace
name|getNamespace
parameter_list|()
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getAllDependencyArtifactsIncludes
parameter_list|()
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifactsIncludes
parameter_list|(
name|String
name|moduleConfigurations
parameter_list|)
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifactsIncludes
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getAllDependencyArtifactsExcludes
parameter_list|()
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifactsExcludes
parameter_list|(
name|String
name|moduleConfigurations
parameter_list|)
function_decl|;
name|DependencyArtifactDescriptor
index|[]
name|getDependencyArtifactsExcludes
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|)
function_decl|;
name|boolean
name|doesExclude
parameter_list|(
name|String
index|[]
name|moduleConfigurations
parameter_list|,
name|ArtifactId
name|artifactId
parameter_list|)
function_decl|;
comment|/**      * Returns true if this descriptor contains any exclusion rule      * @return true if this descriptor contains any exclusion rule      */
specifier|public
name|boolean
name|canExclude
parameter_list|()
function_decl|;
name|DependencyDescriptor
name|asSystem
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

