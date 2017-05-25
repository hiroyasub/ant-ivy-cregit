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
name|InputStream
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
name|HashSet
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
name|Set
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|ManifestParserTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testParseManifest
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleInfo
name|bundleInfo
decl_stmt|;
name|bundleInfo
operator|=
name|ManifestParser
operator|.
name|parseJarManifest
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"com.acme.alpha-1.0.0.20080101.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com.acme.alpha"
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"20080101"
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0.0.20080101"
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bundleInfo
operator|.
name|getRequires
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|BundleRequirement
argument_list|>
name|expectedRequires
init|=
operator|new
name|HashSet
argument_list|<
name|BundleRequirement
argument_list|>
argument_list|()
decl_stmt|;
name|expectedRequires
operator|.
name|add
argument_list|(
operator|new
name|BundleRequirement
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"com.acme.bravo"
argument_list|,
operator|new
name|VersionRange
argument_list|(
literal|"2.0.0"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|expectedRequires
operator|.
name|add
argument_list|(
operator|new
name|BundleRequirement
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"com.acme.delta"
argument_list|,
operator|new
name|VersionRange
argument_list|(
literal|"4.0.0"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRequires
argument_list|,
name|bundleInfo
operator|.
name|getRequires
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|bundleInfo
operator|.
name|getExports
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|bundleInfo
operator|.
name|getImports
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|bundleInfo
operator|.
name|getClasspath
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|importsList
init|=
name|bundleInfo
operator|.
name|getImports
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|importsList
operator|.
name|indexOf
argument_list|(
literal|"com.acme.bravo"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|importsList
operator|.
name|indexOf
argument_list|(
literal|"com.acme.delta"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|bundleInfo
operator|=
name|ManifestParser
operator|.
name|parseJarManifest
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"com.acme.bravo-2.0.0.20080202.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bundleInfo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com.acme.bravo"
argument_list|,
name|bundleInfo
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"20080202"
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
operator|.
name|qualifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0.0.20080202"
argument_list|,
name|bundleInfo
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundleInfo
operator|.
name|getRequires
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|expectedRequires
operator|=
operator|new
name|HashSet
argument_list|<
name|BundleRequirement
argument_list|>
argument_list|()
expr_stmt|;
name|expectedRequires
operator|.
name|add
argument_list|(
operator|new
name|BundleRequirement
argument_list|(
name|BundleInfo
operator|.
name|BUNDLE_TYPE
argument_list|,
literal|"com.acme.charlie"
argument_list|,
operator|new
name|VersionRange
argument_list|(
literal|"3.0.0"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundleInfo
operator|.
name|getExports
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bundleInfo
operator|.
name|getExports
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"com.acme.bravo"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundleInfo
operator|.
name|getImports
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bundleInfo
operator|.
name|getImports
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"com.acme.charlie"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|in
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/ivy/osgi/core/MANIFEST_classpath.MF"
argument_list|)
decl_stmt|;
name|BundleInfo
name|bundleInfo
decl_stmt|;
try|try
block|{
name|bundleInfo
operator|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|cp
init|=
name|bundleInfo
operator|.
name|getClasspath
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|cp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|cp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"lib/ant-antlr.jar"
block|,
literal|"lib/ant-apache-bcel.jar"
block|,
literal|"lib/ant-apache-bsf.jar"
block|,
literal|"lib/ant-apache-log4j.jar"
block|}
argument_list|)
argument_list|,
name|cp
argument_list|)
expr_stmt|;
name|in
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/ivy/osgi/core/MANIFEST_classpath2.MF"
argument_list|)
expr_stmt|;
try|try
block|{
name|bundleInfo
operator|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|cp
operator|=
name|bundleInfo
operator|.
name|getClasspath
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|cp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cp
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"."
block|}
argument_list|)
argument_list|,
name|cp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFormatLines
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"foo bar\n"
argument_list|,
name|ManifestParser
operator|.
name|formatLines
argument_list|(
literal|"foo bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123456789012345678901234567890123456789012345678901234567890123456789012\n"
argument_list|,
name|ManifestParser
operator|.
name|formatLines
argument_list|(
literal|"123456789012345678901234567890123456789012345678901234567890123456789012"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123456789012345678901234567890123456789012345678901234567890123456789012\n 3\n"
argument_list|,
name|ManifestParser
operator|.
name|formatLines
argument_list|(
literal|"1234567890123456789012345678901234567890123456789012345678901234567890123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar\n"
operator|+
literal|"123456789012345678901234567890123456789012345678901234567890123456789012\n"
operator|+
literal|" 12345678901234567890123456789012345678901234567890123456789012345678901\n"
operator|+
literal|" 21234\n"
operator|+
literal|"foo bar\n"
argument_list|,
name|ManifestParser
operator|.
name|formatLines
argument_list|(
literal|"foo bar\n"
operator|+
literal|"123456789012345678901234567890123456789012345678901234567890123456789012"
operator|+
literal|"123456789012345678901234567890123456789012345678901234567890123456789012"
operator|+
literal|"1234\n"
operator|+
literal|"foo bar\n"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

