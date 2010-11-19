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
name|core
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
name|List
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
name|Attributes
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
name|VersionRange
import|;
end_import

begin_comment
comment|/**  * Provides an OSGi manifest parser.  *   */
end_comment

begin_class
specifier|public
class|class
name|ManifestParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EXPORT_PACKAGE
init|=
literal|"Export-Package"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IMPORT_PACKAGE
init|=
literal|"Import-Package"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXPORT_SERVICE
init|=
literal|"Export-Service"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|IMPORT_SERVICE
init|=
literal|"Import-Service"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUIRE_BUNDLE
init|=
literal|"Require-Bundle"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_VERSION
init|=
literal|"Bundle-Version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_NAME
init|=
literal|"Bundle-Name"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_DESCRIPTION
init|=
literal|"Bundle-Description"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_SYMBOLIC_NAME
init|=
literal|"Bundle-SymbolicName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_MANIFEST_VERSION
init|=
literal|"Bundle-ManifestVersion"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUNDLE_REQUIRED_EXECUTION_ENVIRONMENT
init|=
literal|"Bundle-RequiredExecutionEnvironment"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ATTR_RESOLUTION
init|=
literal|"resolution"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ATTR_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ATTR_BUNDLE_VERSION
init|=
literal|"bundle-version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ATTR_USE
init|=
literal|"use"
decl_stmt|;
specifier|public
specifier|static
name|BundleInfo
name|parseJarManifest
parameter_list|(
name|InputStream
name|jarStream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
specifier|final
name|JarInputStream
name|jis
init|=
operator|new
name|JarInputStream
argument_list|(
name|jarStream
argument_list|)
decl_stmt|;
specifier|final
name|BundleInfo
name|parseManifest
init|=
name|parseManifest
argument_list|(
name|jis
operator|.
name|getManifest
argument_list|()
argument_list|)
decl_stmt|;
name|jis
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|parseManifest
return|;
block|}
specifier|public
specifier|static
name|BundleInfo
name|parseManifest
parameter_list|(
name|File
name|manifestFile
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
specifier|final
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|manifestFile
argument_list|)
decl_stmt|;
specifier|final
name|BundleInfo
name|parseManifest
init|=
name|parseManifest
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|parseManifest
return|;
block|}
specifier|public
specifier|static
name|BundleInfo
name|parseManifest
parameter_list|(
name|InputStream
name|manifestStream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
specifier|final
name|BundleInfo
name|parseManifest
init|=
name|parseManifest
argument_list|(
operator|new
name|Manifest
argument_list|(
name|manifestStream
argument_list|)
argument_list|)
decl_stmt|;
name|manifestStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|parseManifest
return|;
block|}
specifier|public
specifier|static
name|BundleInfo
name|parseManifest
parameter_list|(
name|Manifest
name|manifest
parameter_list|)
throws|throws
name|ParseException
block|{
name|Attributes
name|mainAttributes
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
name|String
name|manifestVersion
init|=
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_MANIFEST_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifestVersion
operator|==
literal|null
condition|)
block|{
comment|// non OSGi manifest
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"No "
operator|+
name|BUNDLE_MANIFEST_VERSION
operator|+
literal|" in the manifest"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|String
name|symbolicName
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_SYMBOLIC_NAME
argument_list|)
argument_list|)
operator|.
name|getSingleValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|symbolicName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"No "
operator|+
name|BUNDLE_SYMBOLIC_NAME
operator|+
literal|" in the manifest"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|String
name|description
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_DESCRIPTION
argument_list|)
argument_list|)
operator|.
name|getSingleValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_DESCRIPTION
argument_list|)
argument_list|)
operator|.
name|getSingleValue
argument_list|()
expr_stmt|;
block|}
name|String
name|vBundle
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_VERSION
argument_list|)
argument_list|)
operator|.
name|getSingleValue
argument_list|()
decl_stmt|;
name|Version
name|version
decl_stmt|;
try|try
block|{
name|version
operator|=
name|versionOf
argument_list|(
name|vBundle
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"The "
operator|+
name|BUNDLE_VERSION
operator|+
literal|" has an incorrect version: "
operator|+
name|vBundle
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
name|BundleInfo
name|bundleInfo
init|=
operator|new
name|BundleInfo
argument_list|(
name|symbolicName
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|bundleInfo
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|environments
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|BUNDLE_REQUIRED_EXECUTION_ENVIRONMENT
argument_list|)
argument_list|)
operator|.
name|getValues
argument_list|()
decl_stmt|;
name|bundleInfo
operator|.
name|setExecutionEnvironments
argument_list|(
name|environments
argument_list|)
expr_stmt|;
name|parseRequirement
argument_list|(
name|bundleInfo
argument_list|,
name|mainAttributes
argument_list|,
name|REQUIRE_BUNDLE
argument_list|,
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
name|ATTR_BUNDLE_VERSION
argument_list|)
expr_stmt|;
name|parseRequirement
argument_list|(
name|bundleInfo
argument_list|,
name|mainAttributes
argument_list|,
name|IMPORT_PACKAGE
argument_list|,
name|BundleInfo
operator|.
name|PACKAGE_TYPE
argument_list|,
name|ATTR_VERSION
argument_list|)
expr_stmt|;
name|parseRequirement
argument_list|(
name|bundleInfo
argument_list|,
name|mainAttributes
argument_list|,
name|IMPORT_SERVICE
argument_list|,
name|BundleInfo
operator|.
name|SERVICE_TYPE
argument_list|,
name|ATTR_VERSION
argument_list|)
expr_stmt|;
name|ManifestHeaderValue
name|exportElements
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|EXPORT_PACKAGE
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|ManifestHeaderElement
name|exportElement
range|:
name|exportElements
operator|.
name|getElements
argument_list|()
control|)
block|{
name|String
name|vExport
init|=
name|exportElement
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|ATTR_VERSION
argument_list|)
decl_stmt|;
name|Version
name|v
init|=
literal|null
decl_stmt|;
try|try
block|{
name|v
operator|=
name|versionOf
argument_list|(
name|vExport
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"The "
operator|+
name|EXPORT_PACKAGE
operator|+
literal|" has an incorrect version: "
operator|+
name|vExport
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|name
range|:
name|exportElement
operator|.
name|getValues
argument_list|()
control|)
block|{
name|ExportPackage
name|export
init|=
operator|new
name|ExportPackage
argument_list|(
name|name
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|String
name|uses
init|=
name|exportElement
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|ATTR_USE
argument_list|)
decl_stmt|;
if|if
condition|(
name|uses
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|split
init|=
name|uses
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|u
range|:
name|split
control|)
block|{
name|export
operator|.
name|addUse
argument_list|(
name|u
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|bundleInfo
operator|.
name|addCapability
argument_list|(
name|export
argument_list|)
expr_stmt|;
block|}
block|}
name|parseCapability
argument_list|(
name|bundleInfo
argument_list|,
name|mainAttributes
argument_list|,
name|EXPORT_SERVICE
argument_list|,
name|BundleInfo
operator|.
name|SERVICE_TYPE
argument_list|)
expr_stmt|;
return|return
name|bundleInfo
return|;
block|}
specifier|private
specifier|static
name|void
name|parseRequirement
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|,
name|Attributes
name|mainAttributes
parameter_list|,
name|String
name|headerName
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|versionAttr
parameter_list|)
throws|throws
name|ParseException
block|{
name|ManifestHeaderValue
name|elements
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|headerName
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|ManifestHeaderElement
name|element
range|:
name|elements
operator|.
name|getElements
argument_list|()
control|)
block|{
name|String
name|resolution
init|=
name|element
operator|.
name|getDirectives
argument_list|()
operator|.
name|get
argument_list|(
name|ATTR_RESOLUTION
argument_list|)
decl_stmt|;
name|String
name|attVersion
init|=
name|element
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|versionAttr
argument_list|)
decl_stmt|;
name|VersionRange
name|version
init|=
literal|null
decl_stmt|;
try|try
block|{
name|version
operator|=
name|versionRangeOf
argument_list|(
name|attVersion
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"The "
operator|+
name|headerName
operator|+
literal|" has an incorrect version: "
operator|+
name|attVersion
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|name
range|:
name|element
operator|.
name|getValues
argument_list|()
control|)
block|{
name|bundleInfo
operator|.
name|addRequirement
argument_list|(
operator|new
name|BundleRequirement
argument_list|(
name|type
argument_list|,
name|name
argument_list|,
name|version
argument_list|,
name|resolution
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|parseCapability
parameter_list|(
name|BundleInfo
name|bundleInfo
parameter_list|,
name|Attributes
name|mainAttributes
parameter_list|,
name|String
name|headerName
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|ParseException
block|{
name|ManifestHeaderValue
name|elements
init|=
operator|new
name|ManifestHeaderValue
argument_list|(
name|mainAttributes
operator|.
name|getValue
argument_list|(
name|headerName
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|ManifestHeaderElement
name|element
range|:
name|elements
operator|.
name|getElements
argument_list|()
control|)
block|{
name|String
name|attVersion
init|=
name|element
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|ATTR_VERSION
argument_list|)
decl_stmt|;
name|Version
name|version
init|=
literal|null
decl_stmt|;
try|try
block|{
name|version
operator|=
name|versionOf
argument_list|(
name|attVersion
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"The "
operator|+
name|headerName
operator|+
literal|" has an incorrect version: "
operator|+
name|attVersion
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|,
literal|0
argument_list|)
throw|;
block|}
for|for
control|(
name|String
name|name
range|:
name|element
operator|.
name|getValues
argument_list|()
control|)
block|{
name|BundleCapability
name|export
init|=
operator|new
name|BundleCapability
argument_list|(
name|type
argument_list|,
name|name
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|bundleInfo
operator|.
name|addCapability
argument_list|(
name|export
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|VersionRange
name|versionRangeOf
parameter_list|(
name|String
name|v
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|v
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
name|VersionRange
argument_list|(
name|v
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Version
name|versionOf
parameter_list|(
name|String
name|v
parameter_list|)
throws|throws
name|NumberFormatException
block|{
if|if
condition|(
name|v
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
name|Version
argument_list|(
name|v
argument_list|)
return|;
block|}
block|}
end_class

end_unit

