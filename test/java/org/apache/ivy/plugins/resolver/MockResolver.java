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
name|net
operator|.
name|URL
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
name|Date
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
name|DefaultModuleDescriptor
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

begin_class
specifier|public
class|class
name|MockResolver
extends|extends
name|AbstractResolver
block|{
specifier|static
name|MockResolver
name|buildMockResolver
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|findRevision
parameter_list|,
specifier|final
name|Date
name|publicationDate
parameter_list|)
block|{
return|return
name|buildMockResolver
argument_list|(
name|name
argument_list|,
name|findRevision
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|,
literal|"test"
argument_list|)
argument_list|,
name|publicationDate
argument_list|)
return|;
block|}
specifier|static
name|MockResolver
name|buildMockResolver
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|findRevision
parameter_list|,
specifier|final
name|ModuleRevisionId
name|mrid
parameter_list|,
specifier|final
name|Date
name|publicationDate
parameter_list|)
block|{
return|return
name|buildMockResolver
argument_list|(
name|name
argument_list|,
name|findRevision
argument_list|,
name|mrid
argument_list|,
name|publicationDate
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|MockResolver
name|buildMockResolver
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|findRevision
parameter_list|,
specifier|final
name|ModuleRevisionId
name|mrid
parameter_list|,
specifier|final
name|Date
name|publicationDate
parameter_list|,
specifier|final
name|boolean
name|isdefault
parameter_list|)
block|{
specifier|final
name|MockResolver
name|r
init|=
operator|new
name|MockResolver
argument_list|()
decl_stmt|;
name|r
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|findRevision
condition|)
block|{
name|r
operator|.
name|rmr
operator|=
operator|new
name|ResolvedModuleRevision
argument_list|()
block|{
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|r
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getId
parameter_list|()
block|{
return|return
name|mrid
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|publicationDate
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|isdefault
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isDownloaded
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isSearched
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|DependencyResolver
name|getArtifactResolver
parameter_list|()
block|{
return|return
name|r
return|;
block|}
specifier|public
name|URL
name|getLocalMDUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
name|List
name|askedDeps
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
decl_stmt|;
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
name|askedDeps
operator|.
name|add
argument_list|(
name|dd
argument_list|)
expr_stmt|;
return|return
name|rmr
return|;
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
literal|null
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
block|}
block|}
end_class

end_unit
