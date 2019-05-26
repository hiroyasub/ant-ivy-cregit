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
name|lock
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
comment|/**  * A lock strategy determines when and how lock should be performed when downloading data to a  * cache.  *<p>  * Note that some implementations may actually choose to NOT perform locking, when no lock is  * necessary (cache not shared). Some other implementations may choose to lock the cache for the  * download of a whole module (not possible yet), or at the artifact level.  *</p>  *<p>  * The lock methods should return true when the lock is either actually acquired or not performed by  * the strategy.  *</p>  *<p>  * Locking used in the locking strategy must support reentrant lock. Reentrant locking should be  * performed for the whole strategy.  *</p>  */
end_comment

begin_interface
specifier|public
interface|interface
name|LockStrategy
block|{
comment|/**      * Returns the name of the strategy      *      * @return the name of the strategy      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Performs a lock before downloading the given {@link Artifact} to the given file.      *      * @param artifact      *            the artifact about to be downloaded      * @param artifactFileToDownload      *            the file where the artifact will be downloaded      * @return true if the artifact is locked, false otherwise      * @throws InterruptedException      *             if the thread is interrupted while waiting to acquire the lock      */
name|boolean
name|lockArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|artifactFileToDownload
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
comment|/**      * Release the lock acquired for an artifact download.      *      * @param artifact      *            the artifact for which the lock was acquired      * @param artifactFileToDownload      *            the file where the artifact is supposed to have been downloaded      */
name|void
name|unlockArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|artifactFileToDownload
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

