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
name|core
operator|.
name|pack
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
name|DefaultArtifact
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
name|plugins
operator|.
name|IvySettingsAware
import|;
end_import

begin_class
specifier|public
class|class
name|PackagingManager
implements|implements
name|IvySettingsAware
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|public
name|void
name|setSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
block|}
specifier|public
name|Artifact
name|getUnpackedArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|String
name|packaging
init|=
name|artifact
operator|.
name|getExtraAttribute
argument_list|(
literal|"packaging"
argument_list|)
decl_stmt|;
if|if
condition|(
name|packaging
operator|==
literal|null
condition|)
block|{
comment|// not declared as packed, nothing to do
return|return
literal|null
return|;
block|}
name|String
name|ext
init|=
name|artifact
operator|.
name|getExt
argument_list|()
decl_stmt|;
name|String
index|[]
name|packings
init|=
name|packaging
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|packings
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|1
condition|;
name|i
operator|--
control|)
block|{
name|ArchivePacking
name|packing
init|=
name|settings
operator|.
name|getPackingRegistry
argument_list|()
operator|.
name|get
argument_list|(
name|packings
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|packing
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown packing type '"
operator|+
name|packings
index|[
name|i
index|]
operator|+
literal|"' in the packing chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|packing
operator|instanceof
name|StreamPacking
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported archive only packing type '"
operator|+
name|packings
index|[
name|i
index|]
operator|+
literal|"' in the streamed chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
name|ext
operator|=
operator|(
operator|(
name|StreamPacking
operator|)
name|packing
operator|)
operator|.
name|getUnpackedExtension
argument_list|(
name|ext
argument_list|)
expr_stmt|;
block|}
name|ArchivePacking
name|packing
init|=
name|settings
operator|.
name|getPackingRegistry
argument_list|()
operator|.
name|get
argument_list|(
name|packings
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|packing
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown packing type '"
operator|+
name|packings
index|[
literal|0
index|]
operator|+
literal|"' in the packing chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
name|ext
operator|=
name|packing
operator|.
name|getUnpackedExtension
argument_list|(
name|ext
argument_list|)
expr_stmt|;
name|DefaultArtifact
name|unpacked
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
operator|+
literal|"_unpacked"
argument_list|,
name|ext
argument_list|)
decl_stmt|;
return|return
name|unpacked
return|;
block|}
specifier|public
name|void
name|unpackArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|localFile
parameter_list|,
name|File
name|archiveFile
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|packaging
init|=
name|artifact
operator|.
name|getExtraAttribute
argument_list|(
literal|"packaging"
argument_list|)
decl_stmt|;
if|if
condition|(
name|packaging
operator|==
literal|null
condition|)
block|{
comment|// not declared as packed, nothing to do
return|return;
block|}
name|String
index|[]
name|packings
init|=
name|packaging
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|InputStream
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
name|localFile
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|packings
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|1
condition|;
name|i
operator|--
control|)
block|{
name|ArchivePacking
name|packing
init|=
name|settings
operator|.
name|getPackingRegistry
argument_list|()
operator|.
name|get
argument_list|(
name|packings
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|packing
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown packing type '"
operator|+
name|packings
index|[
name|i
index|]
operator|+
literal|"' in the packing chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|packing
operator|instanceof
name|StreamPacking
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported archive only packing type '"
operator|+
name|packings
index|[
name|i
index|]
operator|+
literal|"' in the streamed chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
name|in
operator|=
operator|(
operator|(
name|StreamPacking
operator|)
name|packing
operator|)
operator|.
name|unpack
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
name|ArchivePacking
name|packing
init|=
name|settings
operator|.
name|getPackingRegistry
argument_list|()
operator|.
name|get
argument_list|(
name|packings
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|packing
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown packing type '"
operator|+
name|packings
index|[
literal|0
index|]
operator|+
literal|"' in the packing chain: "
operator|+
name|packaging
argument_list|)
throw|;
block|}
name|packing
operator|.
name|unpack
argument_list|(
name|in
argument_list|,
name|archiveFile
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
block|}
end_class

end_unit

