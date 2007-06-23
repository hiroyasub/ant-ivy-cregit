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
name|CacheManager
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
name|retrieve
operator|.
name|RetrieveEngine
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
name|retrieve
operator|.
name|RetrieveOptions
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
name|filter
operator|.
name|Filter
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
comment|/**  * This task allow to retrieve dependencies from the cache to a local directory like a lib dir.  *   * @see RetrieveEngine  */
end_comment

begin_class
specifier|public
class|class
name|IvyRetrieve
extends|extends
name|IvyPostResolveTask
block|{
specifier|private
name|String
name|pattern
decl_stmt|;
specifier|private
name|String
name|ivypattern
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|sync
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|symlink
init|=
literal|false
decl_stmt|;
specifier|public
name|String
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
specifier|public
name|void
name|setPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
name|pattern
operator|=
name|getProperty
argument_list|(
name|pattern
argument_list|,
name|getSettings
argument_list|()
argument_list|,
literal|"ivy.retrieve.pattern"
argument_list|)
expr_stmt|;
try|try
block|{
name|Filter
name|artifactFilter
init|=
name|getArtifactFilter
argument_list|()
decl_stmt|;
name|int
name|targetsCopied
init|=
name|getIvyInstance
argument_list|()
operator|.
name|retrieve
argument_list|(
name|getResolvedMrid
argument_list|()
argument_list|,
name|pattern
argument_list|,
operator|new
name|RetrieveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
name|splitConfs
argument_list|(
name|getConf
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|getIvyInstance
argument_list|()
operator|.
name|getSettings
argument_list|()
argument_list|,
name|getCache
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setDestIvyPattern
argument_list|(
name|ivypattern
argument_list|)
operator|.
name|setArtifactFilter
argument_list|(
name|artifactFilter
argument_list|)
operator|.
name|setSync
argument_list|(
name|sync
argument_list|)
operator|.
name|setUseOrigin
argument_list|(
name|isUseOrigin
argument_list|()
argument_list|)
operator|.
name|setMakeSymlinks
argument_list|(
name|symlink
argument_list|)
operator|.
name|setResolveId
argument_list|(
name|getResolveId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|haveTargetsBeenCopied
init|=
name|targetsCopied
operator|>
literal|0
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.nb.targets.copied"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|targetsCopied
argument_list|)
argument_list|)
expr_stmt|;
name|getProject
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"ivy.targets.copied"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|haveTargetsBeenCopied
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to ivy retrieve: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getIvypattern
parameter_list|()
block|{
return|return
name|ivypattern
return|;
block|}
specifier|public
name|void
name|setIvypattern
parameter_list|(
name|String
name|ivypattern
parameter_list|)
block|{
name|this
operator|.
name|ivypattern
operator|=
name|ivypattern
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
comment|/**      * Option to create symlinks instead of copying.      */
specifier|public
name|void
name|setSymlink
parameter_list|(
name|boolean
name|symlink
parameter_list|)
block|{
name|this
operator|.
name|symlink
operator|=
name|symlink
expr_stmt|;
block|}
block|}
end_class

end_unit

