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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|cache
operator|.
name|ArtifactOrigin
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
name|ArtifactDownloadReport
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
name|report
operator|.
name|DownloadStatus
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
name|latest
operator|.
name|ArtifactInfo
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
name|latest
operator|.
name|LatestStrategy
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
name|HasLatestStrategy
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
name|StringUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ChainResolver
extends|extends
name|AbstractResolver
block|{
specifier|public
specifier|static
class|class
name|ResolvedModuleRevisionArtifactInfo
implements|implements
name|ArtifactInfo
block|{
specifier|private
name|ResolvedModuleRevision
name|rmr
decl_stmt|;
specifier|public
name|ResolvedModuleRevisionArtifactInfo
parameter_list|(
name|ResolvedModuleRevision
name|rmr
parameter_list|)
block|{
name|this
operator|.
name|rmr
operator|=
name|rmr
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|rmr
operator|.
name|getId
argument_list|()
operator|.
name|getRevision
argument_list|()
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|rmr
operator|.
name|getPublicationDate
argument_list|()
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
specifier|private
name|boolean
name|returnFirst
init|=
literal|false
decl_stmt|;
specifier|private
name|List
name|chain
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|dual
decl_stmt|;
specifier|private
name|Boolean
name|checkmodified
init|=
literal|null
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * True if this resolver should check lastmodified date to know if ivy files are up to date.      *       * @return      */
specifier|public
name|boolean
name|isCheckmodified
parameter_list|()
block|{
if|if
condition|(
name|checkmodified
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getSettings
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|check
init|=
name|getSettings
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.resolver.default.check.modified"
argument_list|)
decl_stmt|;
return|return
name|check
operator|!=
literal|null
condition|?
name|Boolean
operator|.
name|valueOf
argument_list|(
name|check
argument_list|)
operator|.
name|booleanValue
argument_list|()
else|:
literal|false
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
return|return
name|checkmodified
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|setCheckmodified
parameter_list|(
name|boolean
name|check
parameter_list|)
block|{
name|checkmodified
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|check
argument_list|)
expr_stmt|;
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
name|List
name|errors
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ResolvedModuleRevision
name|resolved
init|=
name|data
operator|.
name|getCurrentResolvedModuleRevision
argument_list|()
decl_stmt|;
name|ResolvedModuleRevision
name|mr
init|=
name|resolved
decl_stmt|;
if|if
condition|(
name|mr
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|getName
argument_list|()
operator|+
literal|": Checking cache for: "
operator|+
name|dd
argument_list|)
expr_stmt|;
name|mr
operator|=
name|findModuleInCache
argument_list|(
name|dd
argument_list|,
name|data
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|mr
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|getName
argument_list|()
operator|+
literal|": module revision found in cache: "
operator|+
name|mr
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isCheckmodified
argument_list|()
condition|)
block|{
name|mr
operator|=
name|forcedRevision
argument_list|(
name|mr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|LatestStrategy
name|oldLatest
init|=
name|setLatestIfRequired
argument_list|(
name|resolver
argument_list|,
name|getLatestStrategy
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ResolvedModuleRevision
name|previouslyResolved
init|=
name|mr
decl_stmt|;
name|data
operator|.
name|setCurrentResolvedModuleRevision
argument_list|(
name|previouslyResolved
argument_list|)
expr_stmt|;
name|mr
operator|=
name|resolver
operator|.
name|getDependency
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
expr_stmt|;
if|if
condition|(
name|mr
operator|!=
name|previouslyResolved
operator|&&
name|isReturnFirst
argument_list|()
condition|)
block|{
name|mr
operator|=
name|forcedRevision
argument_list|(
name|mr
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"problem occured while resolving "
operator|+
name|dd
operator|+
literal|" with "
operator|+
name|resolver
operator|+
literal|": "
operator|+
name|StringUtils
operator|.
name|getStackTrace
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
name|errors
operator|.
name|add
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|oldLatest
operator|!=
literal|null
condition|)
block|{
name|setLatest
argument_list|(
name|resolver
argument_list|,
name|oldLatest
argument_list|)
expr_stmt|;
block|}
block|}
name|checkInterrupted
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|mr
operator|==
literal|null
operator|&&
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|errors
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|errors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|ex
throw|;
block|}
if|else if
condition|(
name|ex
operator|instanceof
name|ParseException
condition|)
block|{
throw|throw
operator|(
name|ParseException
operator|)
name|ex
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|StringBuffer
name|err
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|errors
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|err
operator|.
name|append
argument_list|(
literal|"\t"
argument_list|)
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|getErrorMessage
argument_list|(
name|ex
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|err
operator|.
name|setLength
argument_list|(
name|err
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"several problems occured while resolving "
operator|+
name|dd
operator|+
literal|":\n"
operator|+
name|err
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|resolved
operator|==
name|mr
condition|)
block|{
comment|// nothing has actually been resolved here, we don't need to touch the returned rmr
return|return
name|resolved
return|;
block|}
return|return
name|resolvedRevision
argument_list|(
name|mr
argument_list|)
return|;
block|}
specifier|private
name|ResolvedModuleRevision
name|resolvedRevision
parameter_list|(
name|ResolvedModuleRevision
name|mr
parameter_list|)
block|{
if|if
condition|(
name|isDual
argument_list|()
operator|&&
name|mr
operator|!=
literal|null
condition|)
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
argument_list|,
name|mr
operator|.
name|isForce
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|mr
return|;
block|}
block|}
specifier|private
name|ResolvedModuleRevision
name|forcedRevision
parameter_list|(
name|ResolvedModuleRevision
name|rmr
parameter_list|)
block|{
if|if
condition|(
name|rmr
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ResolvedModuleRevision
argument_list|(
name|rmr
operator|.
name|getResolver
argument_list|()
argument_list|,
name|rmr
operator|.
name|getArtifactResolver
argument_list|()
argument_list|,
name|rmr
operator|.
name|getDescriptor
argument_list|()
argument_list|,
name|rmr
operator|.
name|getReport
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|LatestStrategy
name|setLatestIfRequired
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|LatestStrategy
name|latestStrategy
parameter_list|)
block|{
name|String
name|latestName
init|=
name|getLatestStrategyName
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
if|if
condition|(
name|latestName
operator|!=
literal|null
operator|&&
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|latestName
argument_list|)
condition|)
block|{
name|LatestStrategy
name|oldLatest
init|=
name|getLatest
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|setLatest
argument_list|(
name|resolver
argument_list|,
name|latestStrategy
argument_list|)
expr_stmt|;
return|return
name|oldLatest
return|;
block|}
else|else
block|{
return|return
literal|null
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
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ResolvedResource
name|result
init|=
name|resolver
operator|.
name|findIvyFileRef
argument_list|(
name|dd
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|reportFailure
parameter_list|()
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|reportFailure
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|reportFailure
parameter_list|(
name|Artifact
name|art
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|reportFailure
argument_list|(
name|art
argument_list|)
expr_stmt|;
block|}
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
name|List
name|artifactsToDownload
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|artifacts
argument_list|)
argument_list|)
decl_stmt|;
name|DownloadReport
name|report
init|=
operator|new
name|DownloadReport
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|artifactsToDownload
operator|.
name|isEmpty
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|DownloadReport
name|r
init|=
name|resolver
operator|.
name|download
argument_list|(
operator|(
name|Artifact
index|[]
operator|)
name|artifactsToDownload
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|artifactsToDownload
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
name|options
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
index|[]
name|adr
init|=
name|r
operator|.
name|getArtifactsReports
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
name|adr
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|adr
index|[
name|i
index|]
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
name|artifactsToDownload
operator|.
name|remove
argument_list|(
name|adr
index|[
name|i
index|]
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|report
operator|.
name|addArtifactReport
argument_list|(
name|adr
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|artifactsToDownload
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|art
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArtifactDownloadReport
name|adr
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
name|art
argument_list|)
decl_stmt|;
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|FAILED
argument_list|)
expr_stmt|;
name|report
operator|.
name|addArtifactReport
argument_list|(
name|adr
argument_list|)
expr_stmt|;
block|}
return|return
name|report
return|;
block|}
specifier|public
name|List
name|getResolvers
parameter_list|()
block|{
return|return
name|chain
return|;
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
name|getFirstResolver
argument_list|()
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
specifier|public
name|void
name|abortPublishTransaction
parameter_list|()
throws|throws
name|IOException
block|{
name|getFirstResolver
argument_list|()
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
name|getFirstResolver
argument_list|()
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
name|getFirstResolver
argument_list|()
operator|.
name|commitPublishTransaction
argument_list|()
expr_stmt|;
block|}
specifier|private
name|DependencyResolver
name|getFirstResolver
parameter_list|()
block|{
if|if
condition|(
name|chain
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"invalid chain resolver with no sub resolver"
argument_list|)
throw|;
block|}
return|return
operator|(
operator|(
name|DependencyResolver
operator|)
name|chain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
return|;
block|}
specifier|public
name|boolean
name|isReturnFirst
parameter_list|()
block|{
return|return
name|returnFirst
return|;
block|}
specifier|public
name|void
name|setReturnFirst
parameter_list|(
name|boolean
name|returnFirst
parameter_list|)
block|{
name|this
operator|.
name|returnFirst
operator|=
name|returnFirst
expr_stmt|;
block|}
specifier|public
name|void
name|dumpSettings
parameter_list|()
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|" [chain] "
operator|+
name|chain
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\treturn first: "
operator|+
name|isReturnFirst
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tdual: "
operator|+
name|isDual
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|r
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\t-> "
operator|+
name|r
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolver
operator|.
name|exists
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|ArtifactOrigin
name|locate
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArtifactOrigin
name|origin
init|=
name|resolver
operator|.
name|locate
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ArtifactOrigin
operator|.
name|isUnknown
argument_list|(
name|origin
argument_list|)
condition|)
block|{
return|return
name|origin
return|;
block|}
block|}
return|return
name|ArtifactOrigin
operator|.
name|unkwnown
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
name|download
parameter_list|(
name|ArtifactOrigin
name|artifact
parameter_list|,
name|DownloadOptions
name|options
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DependencyResolver
name|resolver
init|=
operator|(
name|DependencyResolver
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArtifactDownloadReport
name|adr
init|=
name|resolver
operator|.
name|download
argument_list|(
name|artifact
argument_list|,
name|options
argument_list|)
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getDownloadStatus
argument_list|()
operator|!=
name|DownloadStatus
operator|.
name|FAILED
condition|)
block|{
return|return
name|adr
return|;
block|}
block|}
name|ArtifactDownloadReport
name|adr
init|=
operator|new
name|ArtifactDownloadReport
argument_list|(
name|artifact
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|adr
operator|.
name|setDownloadStatus
argument_list|(
name|DownloadStatus
operator|.
name|FAILED
argument_list|)
expr_stmt|;
return|return
name|adr
return|;
block|}
specifier|private
specifier|static
name|void
name|setLatest
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|LatestStrategy
name|latest
parameter_list|)
block|{
if|if
condition|(
name|resolver
operator|instanceof
name|HasLatestStrategy
condition|)
block|{
name|HasLatestStrategy
name|r
init|=
operator|(
name|HasLatestStrategy
operator|)
name|resolver
decl_stmt|;
name|r
operator|.
name|setLatestStrategy
argument_list|(
name|latest
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|LatestStrategy
name|getLatest
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|resolver
operator|instanceof
name|HasLatestStrategy
condition|)
block|{
name|HasLatestStrategy
name|r
init|=
operator|(
name|HasLatestStrategy
operator|)
name|resolver
decl_stmt|;
return|return
name|r
operator|.
name|getLatestStrategy
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|String
name|getLatestStrategyName
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|resolver
operator|instanceof
name|HasLatestStrategy
condition|)
block|{
name|HasLatestStrategy
name|r
init|=
operator|(
name|HasLatestStrategy
operator|)
name|resolver
decl_stmt|;
return|return
name|r
operator|.
name|getLatest
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDual
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|dual
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDual
parameter_list|()
block|{
return|return
name|dual
return|;
block|}
block|}
end_class

end_unit

