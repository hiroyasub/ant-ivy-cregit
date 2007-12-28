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
name|resolver
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
name|text
operator|.
name|ParseException
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
name|report
operator|.
name|DownloadReport
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
name|DownloadOptions
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
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
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

begin_comment
comment|/**  * DualResolver is used to resolve dependencies with one dependency revolver, called ivy resolver,  * and then download artifacts found in the resolved dependencies from a second dependency resolver,  * called artifact resolver. It is especially useful with resolvers using repository where there is  * a lot of artifact, but no ivy file, like the maven ibiblio repository. If no ivy file is found by  * the ivy resolver, the artifact resolver is used to check if there is artifact corresponding to  * the request (with default ivy file). For artifact download, however, only the artifact resolver  * is used. Exactly two resolvers should be added to this resolver for it to work properly. The  * first resolver added if the ivy resolver, the second is the artifact one.  */
end_comment

begin_class
specifier|public
class|class
name|DualResolver
extends|extends
name|AbstractResolver
block|{
specifier|private
name|DependencyResolver
name|ivyResolver
decl_stmt|;
specifier|private
name|DependencyResolver
name|artifactResolver
decl_stmt|;
specifier|private
name|boolean
name|allownomd
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|ivyResolver
operator|==
literal|null
condition|)
block|{
name|ivyResolver
operator|=
name|resolver
expr_stmt|;
block|}
if|else if
condition|(
name|artifactResolver
operator|==
literal|null
condition|)
block|{
name|artifactResolver
operator|=
name|resolver
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"exactly two resolvers must be added: ivy(1) and artifact(2) one"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ResolvedModuleRevision
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|ivyResolver
operator|==
literal|null
operator|||
name|artifactResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"exactly two resolvers must be added: ivy(1) and artifact(2) one"
argument_list|)
throw|;
block|}
name|data
operator|=
operator|new
name|ResolveData
argument_list|(
name|data
argument_list|,
name|doValidate
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ResolvedModuleRevision
name|mr
init|=
name|ivyResolver
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|mr
operator|==
literal|null
condition|)
block|{
name|checkInterrupted
argument_list|()
expr_stmt|;
if|if
condition|(
name|isAllownomd
argument_list|()
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"ivy resolver didn't find "
operator|+
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
operator|+
literal|": trying with artifact resolver"
argument_list|)
expr_stmt|;
return|return
name|artifactResolver
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|ResolvedModuleRevision
argument_list|(
name|mr
operator|.
name|getResolver
argument_list|()
argument_list|,
name|this
argument_list|,
name|mr
operator|.
name|getDescriptor
argument_list|()
argument_list|,
name|mr
operator|.
name|getReport
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|public
name|ResolvedResource
name|findIvyFileRef
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
block|{
return|return
name|ivyResolver
operator|.
name|findIvyFileRef
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
return|;
block|}
specifier|public
name|void
name|reportFailure
parameter_list|()
block|{
name|ivyResolver
operator|.
name|reportFailure
argument_list|()
expr_stmt|;
name|artifactResolver
operator|.
name|reportFailure
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|reportFailure
parameter_list|(
name|Artifact
name|art
parameter_list|)
block|{
name|ivyResolver
operator|.
name|reportFailure
argument_list|(
name|art
argument_list|)
expr_stmt|;
name|artifactResolver
operator|.
name|reportFailure
argument_list|(
name|art
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|DownloadOptions
name|options
parameter_list|)
block|{
return|return
name|artifactResolver
operator|.
name|download
argument_list|(
name|artifacts
argument_list|,
name|options
argument_list|)
return|;
block|}
specifier|public
name|DependencyResolver
name|getArtifactResolver
parameter_list|()
block|{
return|return
name|artifactResolver
return|;
block|}
specifier|public
name|void
name|setArtifactResolver
parameter_list|(
name|DependencyResolver
name|artifactResolver
parameter_list|)
block|{
name|this
operator|.
name|artifactResolver
operator|=
name|artifactResolver
expr_stmt|;
block|}
specifier|public
name|DependencyResolver
name|getIvyResolver
parameter_list|()
block|{
return|return
name|ivyResolver
return|;
block|}
specifier|public
name|void
name|setIvyResolver
parameter_list|(
name|DependencyResolver
name|ivyResolver
parameter_list|)
block|{
name|this
operator|.
name|ivyResolver
operator|=
name|ivyResolver
expr_stmt|;
block|}
specifier|public
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|ivyResolver
operator|.
name|publish
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifactResolver
operator|.
name|publish
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|abortPublishTransaction
parameter_list|()
throws|throws
name|IOException
block|{
name|ivyResolver
operator|.
name|abortPublishTransaction
argument_list|()
expr_stmt|;
name|artifactResolver
operator|.
name|abortPublishTransaction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|beginPublishTransaction
parameter_list|(
name|ModuleRevisionId
name|module
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|ivyResolver
operator|.
name|beginPublishTransaction
argument_list|(
name|module
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|artifactResolver
operator|.
name|beginPublishTransaction
argument_list|(
name|module
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|commitPublishTransaction
parameter_list|()
throws|throws
name|IOException
block|{
name|ivyResolver
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
name|artifactResolver
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|dumpSettings
parameter_list|()
block|{
if|if
condition|(
name|ivyResolver
operator|==
literal|null
operator|||
name|artifactResolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"exactly two resolvers must be added: ivy(1) and artifact(2) one"
argument_list|)
throw|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|" [dual "
operator|+
name|ivyResolver
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|artifactResolver
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifactResolver
operator|.
name|exists
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isAllownomd
parameter_list|()
block|{
return|return
name|allownomd
return|;
block|}
specifier|public
name|void
name|setAllownomd
parameter_list|(
name|boolean
name|allownomd
parameter_list|)
block|{
name|this
operator|.
name|allownomd
operator|=
name|allownomd
expr_stmt|;
block|}
block|}
end_class

end_unit

