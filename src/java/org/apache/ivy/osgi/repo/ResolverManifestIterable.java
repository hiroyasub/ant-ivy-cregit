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
name|osgi
operator|.
name|repo
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
name|net
operator|.
name|URI
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
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
name|event
operator|.
name|EventManager
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
name|DefaultDependencyDescriptor
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
name|ResolveEngine
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
name|ResolveOptions
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
name|core
operator|.
name|search
operator|.
name|ModuleEntry
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
name|search
operator|.
name|OrganisationEntry
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
name|search
operator|.
name|RevisionEntry
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
name|ivy
operator|.
name|core
operator|.
name|sort
operator|.
name|SortEngine
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
name|osgi
operator|.
name|core
operator|.
name|BundleInfoAdapter
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
name|BasicResolver
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

begin_class
specifier|public
class|class
name|ResolverManifestIterable
implements|implements
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
comment|// We should support the interface DependencyResolver, but the API is not convenient to get
comment|// references to artifact
specifier|private
specifier|final
name|BasicResolver
name|resolver
decl_stmt|;
specifier|public
name|ResolverManifestIterable
parameter_list|(
name|BasicResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
specifier|public
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|ResolverManifestIterator
argument_list|()
return|;
block|}
class|class
name|ResolverManifestIterator
implements|implements
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
specifier|private
name|OrganisationEntry
index|[]
name|organisations
decl_stmt|;
specifier|private
name|int
name|indexOrganisation
init|=
literal|0
decl_stmt|;
specifier|private
name|OrganisationEntry
name|organisation
decl_stmt|;
specifier|private
name|ModuleEntry
index|[]
name|modules
decl_stmt|;
specifier|private
name|int
name|indexModule
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|ModuleEntry
name|module
decl_stmt|;
specifier|private
name|ManifestAndLocation
name|next
init|=
literal|null
decl_stmt|;
specifier|private
name|RevisionEntry
index|[]
name|revisions
decl_stmt|;
specifier|private
name|int
name|indexRevision
decl_stmt|;
specifier|private
name|RevisionEntry
name|revision
decl_stmt|;
specifier|private
name|Artifact
index|[]
name|artifacts
decl_stmt|;
specifier|private
name|int
name|indexArtifact
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid
decl_stmt|;
specifier|private
name|ResolveData
name|data
decl_stmt|;
specifier|public
name|ResolverManifestIterator
parameter_list|()
block|{
name|organisations
operator|=
name|resolver
operator|.
name|listOrganisations
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
name|ResolveEngine
name|engine
init|=
operator|new
name|ResolveEngine
argument_list|(
name|settings
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|,
operator|new
name|SortEngine
argument_list|(
name|settings
argument_list|)
argument_list|)
decl_stmt|;
name|data
operator|=
operator|new
name|ResolveData
argument_list|(
name|engine
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
while|while
condition|(
name|next
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|organisation
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|indexOrganisation
operator|>=
name|organisations
operator|.
name|length
condition|)
block|{
return|return
literal|false
return|;
block|}
name|organisation
operator|=
name|organisations
index|[
name|indexOrganisation
operator|++
index|]
expr_stmt|;
name|modules
operator|=
name|resolver
operator|.
name|listModules
argument_list|(
name|organisation
argument_list|)
expr_stmt|;
name|indexModule
operator|=
literal|0
expr_stmt|;
name|module
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|module
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|indexModule
operator|>=
name|modules
operator|.
name|length
condition|)
block|{
name|organisation
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|module
operator|=
name|modules
index|[
name|indexModule
operator|++
index|]
expr_stmt|;
name|revisions
operator|=
name|resolver
operator|.
name|listRevisions
argument_list|(
name|module
argument_list|)
expr_stmt|;
name|indexRevision
operator|=
literal|0
expr_stmt|;
name|revision
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|revision
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|indexRevision
operator|>=
name|revisions
operator|.
name|length
condition|)
block|{
name|module
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|revision
operator|=
name|revisions
index|[
name|indexRevision
operator|++
index|]
expr_stmt|;
name|mrid
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|organisation
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|module
operator|.
name|getModule
argument_list|()
argument_list|,
name|revision
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ResolvedModuleRevision
name|dependency
decl_stmt|;
try|try
block|{
name|dependency
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
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"Error while resolving "
operator|+
name|mrid
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|revision
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|dependency
operator|==
literal|null
condition|)
block|{
name|revision
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|ModuleDescriptor
name|md
init|=
name|dependency
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|mrid
operator|=
name|md
operator|.
name|getModuleRevisionId
argument_list|()
expr_stmt|;
name|artifacts
operator|=
name|md
operator|.
name|getAllArtifacts
argument_list|()
expr_stmt|;
name|indexArtifact
operator|=
literal|0
expr_stmt|;
name|artifact
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|indexArtifact
operator|>=
name|artifacts
operator|.
name|length
condition|)
block|{
name|revision
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|artifact
operator|=
name|artifacts
index|[
name|indexArtifact
operator|++
index|]
expr_stmt|;
block|}
name|ResolvedResource
name|resource
init|=
name|resolver
operator|.
name|doFindArtifactRef
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|artifact
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|JarInputStream
name|in
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|JarInputStream
argument_list|(
name|resource
operator|.
name|getResource
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"Unreadable jar "
operator|+
name|resource
operator|.
name|getResource
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|artifact
operator|=
literal|null
expr_stmt|;
continue|continue;
block|}
name|Manifest
name|manifest
decl_stmt|;
try|try
block|{
name|manifest
operator|=
name|in
operator|.
name|getManifest
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// don't care
block|}
block|}
if|if
condition|(
name|manifest
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"No manifest on "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|URI
name|uri
init|=
name|BundleInfoAdapter
operator|.
name|buildIvyURI
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|next
operator|=
operator|new
name|ManifestAndLocation
argument_list|(
name|manifest
argument_list|,
name|uri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|artifact
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|ManifestAndLocation
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|ManifestAndLocation
name|manifest
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|manifest
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

