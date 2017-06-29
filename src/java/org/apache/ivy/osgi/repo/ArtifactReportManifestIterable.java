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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|URI
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ArtifactReportManifestIterable
implements|implements
name|Iterable
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
argument_list|>
name|artifactReports
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|sourceTypes
decl_stmt|;
specifier|public
name|ArtifactReportManifestIterable
parameter_list|(
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sourceTypes
parameter_list|)
block|{
name|this
operator|.
name|sourceTypes
operator|=
name|sourceTypes
expr_stmt|;
for|for
control|(
name|ArtifactDownloadReport
name|report
range|:
name|reports
control|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|report
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|moduleReports
init|=
name|artifactReports
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|moduleReports
operator|==
literal|null
condition|)
block|{
name|moduleReports
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|artifactReports
operator|.
name|put
argument_list|(
name|mrid
argument_list|,
name|moduleReports
argument_list|)
expr_stmt|;
block|}
name|moduleReports
operator|.
name|add
argument_list|(
name|report
argument_list|)
expr_stmt|;
block|}
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
name|ArtifactReportManifestIterator
argument_list|()
return|;
block|}
class|class
name|ArtifactReportManifestIterator
implements|implements
name|Iterator
argument_list|<
name|ManifestAndLocation
argument_list|>
block|{
specifier|private
name|ManifestAndLocation
name|next
init|=
literal|null
decl_stmt|;
specifier|private
name|Iterator
argument_list|<
name|ModuleRevisionId
argument_list|>
name|it
decl_stmt|;
specifier|public
name|ArtifactReportManifestIterator
parameter_list|()
block|{
name|it
operator|=
name|artifactReports
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
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
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArtifactDownloadReport
argument_list|>
name|reports
init|=
name|artifactReports
operator|.
name|get
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
name|ArtifactDownloadReport
name|jar
init|=
literal|null
decl_stmt|;
name|ArtifactDownloadReport
name|source
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ArtifactDownloadReport
name|report
range|:
name|reports
control|)
block|{
if|if
condition|(
name|sourceTypes
operator|!=
literal|null
operator|&&
name|sourceTypes
operator|.
name|contains
argument_list|(
name|report
operator|.
name|getArtifact
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|source
operator|=
name|report
expr_stmt|;
block|}
else|else
block|{
name|jar
operator|=
name|report
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jar
operator|==
literal|null
condition|)
block|{
comment|// didn't found any suitable jar
continue|continue;
block|}
name|URI
name|sourceURI
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|source
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sourceURI
operator|=
name|source
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|sourceURI
operator|=
name|source
operator|.
name|getLocalFile
argument_list|()
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|!=
literal|null
operator|&&
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|FileInputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|,
literal|"META-INF/MANIFEST.MF"
argument_list|)
argument_list|)
expr_stmt|;
name|next
operator|=
operator|new
name|ManifestAndLocation
argument_list|(
operator|new
name|Manifest
argument_list|(
name|in
argument_list|)
argument_list|,
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|.
name|toURI
argument_list|()
argument_list|,
name|sourceURI
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Bundle directory file just removed: "
operator|+
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|,
name|e
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
name|debug
argument_list|(
literal|"The Manifest in the bundle directory could not be read: "
operator|+
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
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
comment|// ignore
block|}
block|}
block|}
block|}
else|else
block|{
name|File
name|artifact
decl_stmt|;
if|if
condition|(
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|artifact
operator|=
name|jar
operator|.
name|getUnpackedLocalFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|artifact
operator|=
name|jar
operator|.
name|getLocalFile
argument_list|()
expr_stmt|;
block|}
name|JarInputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
operator|new
name|JarInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
name|Manifest
name|manifest
init|=
name|in
operator|.
name|getManifest
argument_list|()
decl_stmt|;
if|if
condition|(
name|manifest
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
operator|new
name|ManifestAndLocation
argument_list|(
name|manifest
argument_list|,
name|artifact
operator|.
name|toURI
argument_list|()
argument_list|,
name|sourceURI
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"No manifest in jar: "
operator|+
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"Jar file just removed: "
operator|+
name|artifact
argument_list|,
name|e
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
literal|"Unreadable jar: "
operator|+
name|artifact
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
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
comment|// Don't care
block|}
block|}
block|}
block|}
block|}
return|return
name|next
operator|!=
literal|null
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

