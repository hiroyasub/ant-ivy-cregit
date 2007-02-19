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
name|plugins
operator|.
name|repository
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
name|io
operator|.
name|IOException
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
name|Artifact
import|;
end_import

begin_comment
comment|/**  * Represents a collection of resources available to Ivy. Ivy uses one or more  * repositories as both a source of resources for Ivy enabled build systems and  * as a distribution center for resources generated by Ivy enabled build systems.  *</p>  *<p>A repository supports the following fundamental operations  *<ul>  *<li>retrieving a resource from the repository.</li>  *<li>transfering a resource to the repository.</li>  *<li>retrieving a listing of resources.</li>  *</ul>  *</p>  *<h4>Resource Retrieval</h4>  *</p>  *<p>{@link #get} retrieves a resource specified by a provided identifier creating a new file .</p>  *</p>  *<h4>resource Publication</h4>  *</p>  *<p>{@link #put} transfers a file to the repository.  *</p>  *</p>  *<h4>resource Listing</h4>  *</p>  *<p>{@link #list} returns a listing of file like objects  *    belonging to a specified parent directory.</p>  *</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Repository
block|{
comment|/** 	 * Return the resource associated with a specified identifier. 	 *  	 * If the resource does not exist, it should return a Resource with exists() returning false. 	 *  	 * An IOException should only be thrown when a real IO problem occurs, like the impossibility to 	 * connect to a server. 	 *  	 * @param source A string identifying the resource. 	 * @return The resource associated with the resource identifier. 	 * @throws IOException On error whle trying to get resource. 	 */
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Fetch a resource from the repository.      *       * @param source A string identifying the resource to be fetched.      * @param destination Where to place the fetched resource.      * @throws IOException On retrieval failure.      */
name|void
name|get
parameter_list|(
name|String
name|source
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Transfer a resource to the repository      * @param artifact The artifact to be transferred.      * @param source The local file to be transferred.      * @param destination Where to transfer the resource.      * @param overwrite Whether the transfer should overwrite an existing resource.      *       * @throws IOException On publication failure.      */
name|void
name|put
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|source
parameter_list|,
name|String
name|destination
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Return a listing of resources names      *       * @param parent The parent directory from which to generate the listing.      * @return A listing of the parent directory's file content, as a List of String.      * @throws IOException On listing failure.      */
name|List
name|list
parameter_list|(
name|String
name|parent
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Add a listener to the repository.      *       * @param listener The listener to attach to the repository.      */
name|void
name|addTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Remove a listener on the repository      *       * @param listener The listener to remove      */
name|void
name|removeTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Determine if a given listener is attached to the repository.      *       * @param listener The listener being quireied      * @return<code>true</code> if the provided listener is attached to the repository,      *<code>false</code> if not.      */
name|boolean
name|hasTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Get the repository's file separator string.      *       * @return The repository's file separator delimiter      */
name|String
name|getFileSeparator
parameter_list|()
function_decl|;
comment|/**      * Normalize a string.      *       * @param source The string to normalize.      * @return The normalized string.      */
name|String
name|standardize
parameter_list|(
name|String
name|source
parameter_list|)
function_decl|;
comment|/**      * Return the name of the repository      *       */
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit
