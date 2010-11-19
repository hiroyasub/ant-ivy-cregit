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
name|ivy
operator|.
name|internal
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
name|Collection
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
name|DefaultRepositoryCacheManager
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
name|repository
operator|.
name|file
operator|.
name|FileResource
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
name|ResolverSettings
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
name|FilePackageScanner
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|File
argument_list|>
argument_list|>
name|patternFiles
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|File
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|useFileCache
init|=
literal|false
decl_stmt|;
specifier|public
name|void
name|scanAllPackageExportHeaders
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ivyPatterns
parameter_list|,
name|ResolverSettings
name|settings
parameter_list|)
block|{
specifier|final
name|DefaultRepositoryCacheManager
name|cacheManager
init|=
operator|(
name|DefaultRepositoryCacheManager
operator|)
name|settings
operator|.
name|getDefaultRepositoryCacheManager
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|ivyPattern
range|:
name|ivyPatterns
control|)
block|{
name|Collection
argument_list|<
name|File
argument_list|>
name|fileList
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|(
name|fileList
operator|=
name|patternFiles
operator|.
name|get
argument_list|(
name|ivyPattern
argument_list|)
operator|)
operator|==
literal|null
operator|||
operator|!
name|useFileCache
condition|)
block|{
name|patternFiles
operator|.
name|put
argument_list|(
name|ivyPattern
argument_list|,
operator|(
name|fileList
operator|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
operator|)
argument_list|)
expr_stmt|;
specifier|final
name|File
name|rootDir
init|=
operator|new
name|File
argument_list|(
name|ivyPattern
operator|.
name|split
argument_list|(
literal|"\\[[\\w]+\\]"
argument_list|)
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|scanDir
argument_list|(
name|rootDir
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|File
name|currFile
range|:
name|fileList
control|)
block|{
try|try
block|{
name|PackageRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|processExports
argument_list|(
name|cacheManager
operator|.
name|getBasedir
argument_list|()
argument_list|,
operator|new
name|FileResource
argument_list|(
literal|null
argument_list|,
name|currFile
argument_list|)
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
name|error
argument_list|(
literal|"Failed to process exports for file: "
operator|+
name|currFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|scanDir
parameter_list|(
name|File
name|currFile
parameter_list|,
name|Collection
argument_list|<
name|File
argument_list|>
name|fileList
parameter_list|)
block|{
if|if
condition|(
operator|!
name|currFile
operator|.
name|canRead
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|currFile
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|currFile
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|files
operator|.
name|add
argument_list|(
name|file
argument_list|)
expr_stmt|;
comment|//pre-process to check if we have recursed into an exploded bundle
if|if
condition|(
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"META-INF"
argument_list|)
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|,
literal|"MANIFEST.MF"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|//continue scanning...
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|scanDir
argument_list|(
name|file
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|currFile
operator|.
name|isFile
argument_list|()
condition|)
block|{
specifier|final
name|String
name|path
init|=
name|currFile
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"META-INF/MANIFEST.MF"
argument_list|)
operator|||
name|path
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".JAR"
argument_list|)
condition|)
block|{
name|fileList
operator|.
name|add
argument_list|(
name|currFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

