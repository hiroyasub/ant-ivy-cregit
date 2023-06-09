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
name|version
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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

begin_comment
comment|/**  * This interface defines a version matcher, i.e. a class able to tell if the revision asked by a  * module for a dependency is dynamic (i.e. need to find all revisions to find the good one among  * them) and if a found revision matches the asked one.  *<p>  * Two ways of matching are possible:  *<ul>  *<li>based on the module revision only (known as ModuleRevisionId)</li>  *<li>based on the parsed module descriptor</li>  *</ul>  * The second being much more time consuming than the first, the version matcher should tell if it  * needs such parsing or not using the  * {@link #needModuleDescriptor(ModuleRevisionId, ModuleRevisionId)} method. Anyway, the first way  * is always used, and if a revision is not accepted using the first method, the module descriptor  * won't be parsed. Therefore if a version matcher uses only module descriptors to accept a revision  * or not it should always return true to  * {@link #needModuleDescriptor(ModuleRevisionId, ModuleRevisionId)} and  * {@link #accept(ModuleRevisionId, ModuleDescriptor)}.  */
end_comment

begin_interface
specifier|public
interface|interface
name|VersionMatcher
block|{
comment|/**      * Indicates if the given asked ModuleRevisionId should be considered as dynamic for the current      * VersionMatcher or not.      *      * @param askedMrid      *            the dependency module revision id as asked by a module      * @return true if this revision is considered as a dynamic one, false otherwise      */
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
function_decl|;
comment|/**      * Indicates if this version matcher considers that the module revision found matches the asked      * one.      *      * @param askedMrid ModuleRevisionId      * @param foundMrid ModuleRevisionId      * @return boolean      */
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
comment|/**      * Indicates if this VersionMatcher needs module descriptors to determine if a module revision      * matches the asked one. Note that returning true in this method may imply big performance      * issues.      *      * @param askedMrid ModuleRevisionId      * @param foundMrid ModuleRevisionId      * @return boolean      */
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
comment|/**      * Indicates if this version matcher considers that the module found matches the asked one. This      * method can be called even needModuleDescriptor(ModuleRevisionId askedMrid, ModuleRevisionId      * foundMrid) returns false, so it is required to implement it in any case, a usual default      * implementation being: return accept(askedMrid, foundMD.getResolvedModuleRevisionId());      *      * @param askedMrid ModuleRevisionId      * @param foundMD ModuleDescriptor      * @return boolean      */
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
comment|/**      * Compares a dynamic revision (askedMrid) with a static one (foundMrid) to indicate which one      * should be considered the greater. If there is not enough information to know which one is the      * greater, the dynamic one should be considered greater and this method should return 0. This      * method should never be called with a askedMrid for which isDynamic returns false.      *      * @param askedMrid      *            the dynamic revision to compare      * @param foundMrid      *            the static revision to compare      * @param staticComparator      *            a comparator which can be used to compare static revisions      * @return 0 if it's not possible to know which one is greater, greater than 0 if askedMrid      *         should be considered greater, lower than 0 if it can't be consider greater      */
name|int
name|compare
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|,
name|Comparator
argument_list|<
name|ModuleRevisionId
argument_list|>
name|staticComparator
parameter_list|)
function_decl|;
comment|/**      * Returns the version matcher name identifying this version matcher      *      * @return the version matcher name identifying this version matcher      */
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

