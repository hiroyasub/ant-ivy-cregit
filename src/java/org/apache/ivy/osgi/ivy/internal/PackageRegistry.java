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
name|FilenameFilter
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|ModuleId
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
name|osgi
operator|.
name|ivy
operator|.
name|OsgiManifestParser
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
name|util
operator|.
name|Version
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
name|util
operator|.
name|VersionComparator
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
name|util
operator|.
name|VersionRange
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
name|Resource
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
import|import static
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
name|OsgiManifestParser
operator|.
name|PACKAGE
import|;
end_import

begin_class
specifier|public
class|class
name|PackageRegistry
block|{
specifier|private
specifier|final
specifier|static
name|PackageRegistry
name|instance
init|=
operator|new
name|PackageRegistry
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|PackageRegistry
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
specifier|static
specifier|final
name|String
name|PKGREF
init|=
literal|".pkgref"
decl_stmt|;
specifier|private
specifier|final
name|OsgiManifestParser
name|osgiManifestParser
init|=
operator|new
name|OsgiManifestParser
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|processedEntries
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|File
name|cacheDir
decl_stmt|;
specifier|private
name|PackageRegistry
parameter_list|()
block|{
comment|// nothing to initialize
block|}
specifier|public
name|void
name|processExports
parameter_list|(
name|File
name|cacheDirectory
parameter_list|,
name|Resource
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|cacheDir
operator|=
name|cacheDirectory
expr_stmt|;
if|if
condition|(
name|processedEntries
operator|.
name|contains
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
operator|!
name|osgiManifestParser
operator|.
name|accept
argument_list|(
name|res
argument_list|)
condition|)
block|{
return|return;
block|}
name|ModuleDescriptor
name|md
decl_stmt|;
try|try
block|{
name|md
operator|=
name|osgiManifestParser
operator|.
name|parseExports
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|error
argument_list|(
literal|"\t\tFailed to parse package resource descriptor: "
operator|+
name|res
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|md
operator|==
literal|null
condition|)
block|{
return|return;
block|}
specifier|final
name|ModuleRevisionId
name|mrid
init|=
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
decl_stmt|;
specifier|final
name|File
name|pkgRootDir
init|=
operator|new
name|File
argument_list|(
name|cacheDir
argument_list|,
name|PACKAGE
argument_list|)
decl_stmt|;
name|pkgRootDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
for|for
control|(
name|DependencyDescriptor
name|dep
range|:
name|md
operator|.
name|getDependencies
argument_list|()
control|)
block|{
specifier|final
name|ModuleRevisionId
name|depMrid
init|=
name|dep
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|depMrid
operator|.
name|getOrganisation
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|PACKAGE
argument_list|)
condition|)
block|{
specifier|final
name|File
name|pkgDir
init|=
operator|new
name|File
argument_list|(
name|pkgRootDir
argument_list|,
operator|(
name|depMrid
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|)
argument_list|)
decl_stmt|;
name|pkgDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
specifier|final
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|pkgDir
argument_list|,
name|mrid
operator|+
name|PKGREF
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tWriting pkg ref: "
operator|+
name|file
argument_list|)
expr_stmt|;
name|file
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|processedEntries
operator|.
name|add
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|processImports
parameter_list|(
specifier|final
name|String
name|pkgName
parameter_list|,
specifier|final
name|VersionRange
name|importRange
parameter_list|)
block|{
specifier|final
name|File
name|pkgRootDir
init|=
operator|new
name|File
argument_list|(
name|cacheDir
argument_list|,
name|PACKAGE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pkgRootDir
operator|.
name|canRead
argument_list|()
operator|||
operator|!
name|pkgRootDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|File
name|pkgDir
init|=
operator|new
name|File
argument_list|(
name|pkgRootDir
argument_list|,
name|pkgName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
argument_list|)
decl_stmt|;
specifier|final
name|TreeMap
argument_list|<
name|Version
argument_list|,
name|ModuleRevisionId
argument_list|>
name|pkgMrids
init|=
operator|new
name|TreeMap
argument_list|<
name|Version
argument_list|,
name|ModuleRevisionId
argument_list|>
argument_list|(
name|VersionComparator
operator|.
name|DESCENDING
argument_list|)
decl_stmt|;
name|pkgDir
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|PKGREF
argument_list|)
condition|)
block|{
specifier|final
name|String
name|baseName
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
operator|(
name|name
operator|.
name|length
argument_list|()
operator|-
name|PKGREF
operator|.
name|length
argument_list|()
operator|)
argument_list|)
decl_stmt|;
specifier|final
name|int
name|hashIdx
init|=
name|baseName
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
decl_stmt|;
specifier|final
name|int
name|semicolIdx
init|=
name|baseName
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
specifier|final
name|String
name|mridOrg
init|=
name|baseName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|hashIdx
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|tokens
init|=
name|baseName
operator|.
name|substring
argument_list|(
name|hashIdx
operator|+
literal|1
argument_list|,
name|semicolIdx
argument_list|)
operator|.
name|split
argument_list|(
literal|"[#]"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|mridName
init|=
name|tokens
index|[
literal|0
index|]
decl_stmt|;
specifier|final
name|String
name|mridBranch
init|=
operator|(
name|tokens
operator|.
name|length
operator|>
literal|1
condition|?
name|tokens
index|[
literal|1
index|]
else|:
literal|null
operator|)
decl_stmt|;
specifier|final
name|String
name|mridRev
init|=
name|baseName
operator|.
name|substring
argument_list|(
name|semicolIdx
operator|+
literal|1
argument_list|)
decl_stmt|;
name|pkgMrids
operator|.
name|put
argument_list|(
operator|new
name|Version
argument_list|(
name|mridRev
argument_list|)
argument_list|,
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|mridOrg
argument_list|,
name|mridName
argument_list|)
argument_list|,
name|mridBranch
argument_list|,
name|mridRev
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ModuleRevisionId
name|matchingMrid
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Version
argument_list|,
name|ModuleRevisionId
argument_list|>
name|entry
range|:
name|pkgMrids
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|importRange
operator|==
literal|null
operator|||
name|importRange
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|matchingMrid
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
return|return
name|matchingMrid
return|;
block|}
block|}
end_class

end_unit

