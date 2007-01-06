begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|version
package|;
end_package

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_comment
comment|/**  * This interface defines a version matcher, i.e. a class able to tell if the revision  * asked by a module for a dependency is dynamic (i.e. need to find all revisions to find the good one among them)  * and if a found revision matches the asked one.  *   * Two ways of matching are possible:  * - based on the module revision only (known as ModuleRevisionId)  * - based on the parsed module descriptor  *   * The second being much more time consuming than the first, the version matcher should tell if it needs such parsing   * or not using the needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) method. Anyway, the first way is always used, and if a revision is not accepted using the first  * method, the module descriptor won't be parsed.  *   * Therefore if a version matcher uses only module descriptors to accept a revision or not it should always return true  * to needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid) and accept(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid).  *   * @author Xavier Hanin  */
end_comment

begin_interface
specifier|public
interface|interface
name|VersionMatcher
block|{
comment|/**      * Indicates if the given asked ModuleRevisionId should be considered as dynamic for      * the current VersionMatcher or not.      * @param askedMrid the dependency module revision id as asked by a module      * @return true if this revision is considered as a dynamic one, false otherwise      */
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
function_decl|;
comment|/**      * Indicates if this version matcher considers that the module revision found matches the asked one.      * @param askedMrid      * @param foundMrid      * @return      */
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
function_decl|;
comment|/**      * Indicates if this VersionMatcher needs module descriptors to determine if a module revision       * matches the asked one.      * Note that returning true in this method may imply big performance issues.       * @return      */
specifier|public
name|boolean
name|needModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
function_decl|;
comment|/**      * Indicates if this version matcher considers that the module found matches the asked one.      * This method can be called even needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId foundMrid)      * returns false, so it is required to implement it in any case, a usual default implementation being:      *       * return accept(askedMrid, foundMD.getResolvedModuleRevisionId());      *       * @param askedMrid      * @param foundMD      * @return      */
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleDescriptor
name|foundMD
parameter_list|)
function_decl|;
comment|/**      * Returns the version matcher name identifying this version matcher      * @return the version matcher name identifying this version matcher      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

