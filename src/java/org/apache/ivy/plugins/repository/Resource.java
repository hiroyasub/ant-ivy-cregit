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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Represents a resource in an Ivy {@link Repository}. The resource interface allows one to obtain  * the following information about a resource:  *<ul>  *<li>resource name/identifier in repository syntax</li>  *<li>date the resource was last modified.</li>  *<li>size of the resource in bytes.</li>  *<li>if the resource is available.</li>  *</ul>  *<h3>Implementation Notes</h3>  * In implementing the interface you need to ensure the following behaviors:  *<ul>  *<li>All of the methods specified in the interface fail by returning an empty value  * (<code>false</code>,<code>0</code>,<code>""</code>). In other words, the specified interface  * methods should not throw RuntimeExceptions.</li>  *<li>Failure conditions should be logged using the {@link org.apache.ivy.util.Message#verbose}  * method.</li>  *<li>Failure of one of the interface's specified methods results in all other interface specified  * methods returning an empty value (<code>false</code>,<code>0</code>,<code>""</code>).</li>  *</ul>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Resource
block|{
comment|/**      * Get the name of the resource.      *      * @return the repository's assigned resource name/identifier.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Get the date the resource was last modified      *      * @return A<code>long</code> value representing the time the file was last modified, measured      *         in milliseconds since the epoch (00:00:00 GMT, January 1, 1970), or<code>0L</code>      *         if the file does not exist or if an I/O error occurs.      */
name|long
name|getLastModified
parameter_list|()
function_decl|;
comment|/**      * Get the resource size      *      * @return a<code>long</code> value representing the size of the resource in bytes.      */
name|long
name|getContentLength
parameter_list|()
function_decl|;
comment|/**      * Determine if the resource is available. Note that this method only checks for      * availability, not for actual existence.      *      * @return<code>boolean</code> value indicating if the resource is available.      */
name|boolean
name|exists
parameter_list|()
function_decl|;
comment|/**      * Is this resource local to this host, i.e. is it on the file system?      *      * @return<code>boolean</code> value indicating if the resource is local.      */
name|boolean
name|isLocal
parameter_list|()
function_decl|;
comment|/**      * Clones this resource with a new resource with a different name      *      * @param cloneName      *            the name of the clone      * @return the cloned resource      */
name|Resource
name|clone
parameter_list|(
name|String
name|cloneName
parameter_list|)
function_decl|;
comment|/**      * Opens a stream on this resource      *      * @return the opened input stream      * @throws IOException if something goes wrong      */
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

