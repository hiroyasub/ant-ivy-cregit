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
name|plugins
operator|.
name|conflict
package|;
end_package

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
name|resolve
operator|.
name|IvyNode
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ConflictManager
block|{
comment|/**      * Resolves the eventual conflicts found in the given collection of IvyNode. This method return      * a Collection of IvyNode which have not been evicted. The given conflicts Collection contains      * at least one IvyNode. This method can be called with IvyNodes which are not yet loaded. If      * this conflict manager is not able to resolve conflicts with the current data found in the      * IvyNodes and need them to be fully loaded, it will return null to indicate that no conflict      * resolution has been done.      *      * @param parent      *            the ivy node parent for which the conflict is to be resolved      * @param conflicts      *            the collection of IvyNode to check for conflicts      * @return a Collection of IvyNode which have not been evicted, or null if conflict management      *         resolution is not possible yet      */
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|resolveConflicts
parameter_list|(
name|IvyNode
name|parent
parameter_list|,
name|Collection
argument_list|<
name|IvyNode
argument_list|>
name|conflicts
parameter_list|)
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Method called when all revisions available for a version constraint have been blacklisted,      * and thus the dependency can't be resolved.      *<p>      * This will never happen if the conflict manager doesn't blacklist any module, so providing an      * empty implementation in this case is fine.      *</p>      *      * @param dd      *            the dependency descriptor for which all revisions are blacklisted.      * @param foundBlacklisted      *            the list of all ModuleRevisionId found which are blacklisted      */
name|void
name|handleAllBlacklistedRevisions
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|Collection
argument_list|<
name|ModuleRevisionId
argument_list|>
name|foundBlacklisted
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

