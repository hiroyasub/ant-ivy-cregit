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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|cache
operator|.
name|RepositoryCacheManager
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

begin_comment
comment|/**  * Cleans the content of Ivy cache(s).  */
end_comment

begin_class
specifier|public
class|class
name|IvyCleanCache
extends|extends
name|IvyTask
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ALL
init|=
literal|"*"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NONE
init|=
literal|"NONE"
decl_stmt|;
specifier|private
name|boolean
name|resolution
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|cache
init|=
name|ALL
decl_stmt|;
specifier|public
name|String
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
comment|/**      * Sets the name of the repository cache to clean, '*' for all caches, 'NONE' for no repository      * cache cleaning at all.      *      * @param cache      *            the name of the cache to clean. Must not be<code>null</code>.      */
specifier|public
name|void
name|setCache
parameter_list|(
name|String
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
specifier|public
name|boolean
name|isResolution
parameter_list|()
block|{
return|return
name|resolution
return|;
block|}
comment|/**      * Sets whether the resolution cache should be cleaned or not.      *      * @param resolution      *<code>true</code> if the resolution cache should be cleaned,<code>false</code>      *            otherwise.      */
specifier|public
name|void
name|setResolution
parameter_list|(
name|boolean
name|resolution
parameter_list|)
block|{
name|this
operator|.
name|resolution
operator|=
name|resolution
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|IvySettings
name|settings
init|=
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
decl_stmt|;
if|if
condition|(
name|isResolution
argument_list|()
condition|)
block|{
name|settings
operator|.
name|getResolutionCacheManager
argument_list|()
operator|.
name|clean
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ALL
operator|.
name|equals
argument_list|(
name|getCache
argument_list|()
argument_list|)
condition|)
block|{
name|RepositoryCacheManager
index|[]
name|caches
init|=
name|settings
operator|.
name|getRepositoryCacheManagers
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
name|caches
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|caches
index|[
name|i
index|]
operator|.
name|clean
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
operator|!
name|NONE
operator|.
name|equals
argument_list|(
name|getCache
argument_list|()
argument_list|)
condition|)
block|{
name|RepositoryCacheManager
name|cache
init|=
name|settings
operator|.
name|getRepositoryCacheManager
argument_list|(
name|getCache
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"unknown cache '"
operator|+
name|getCache
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
else|else
block|{
name|cache
operator|.
name|clean
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

