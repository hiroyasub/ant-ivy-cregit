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
name|obr
package|;
end_package

begin_import
import|import static
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
name|IBiblioResolver
operator|.
name|DEFAULT_M2_ROOT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

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
name|util
operator|.
name|Iterator
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
name|osgi
operator|.
name|obr
operator|.
name|xml
operator|.
name|OBRXMLParser
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
name|repo
operator|.
name|BundleRepoDescriptor
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
name|repo
operator|.
name|ModuleDescriptorWrapper
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
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|OBRParserTest
block|{
specifier|private
specifier|final
name|File
name|testObr
init|=
operator|new
name|File
argument_list|(
literal|"test/test-obr"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testParse
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleRepoDescriptor
name|repo
init|=
name|OBRXMLParser
operator|.
name|parse
argument_list|(
name|testObr
operator|.
name|toURI
argument_list|()
argument_list|,
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|testObr
argument_list|,
literal|"obr.xml"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OBR/Releases"
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1253581430652"
argument_list|,
name|repo
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseSource
parameter_list|()
throws|throws
name|Exception
block|{
name|BundleRepoDescriptor
name|repo
init|=
name|OBRXMLParser
operator|.
name|parse
argument_list|(
name|testObr
operator|.
name|toURI
argument_list|()
argument_list|,
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|testObr
argument_list|,
literal|"sources.xml"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|CollectionUtils
operator|.
name|toList
argument_list|(
name|repo
operator|.
name|getModules
argument_list|()
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ModuleDescriptorWrapper
argument_list|>
name|itModule
init|=
name|repo
operator|.
name|getModules
argument_list|()
decl_stmt|;
while|while
condition|(
name|itModule
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleDescriptor
name|md
init|=
name|itModule
operator|.
name|next
argument_list|()
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.felix.eventadmin"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|md
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"org.apache.felix.bundlerepository"
argument_list|,
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|md
operator|.
name|getAllArtifacts
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|type0
init|=
name|md
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|url0
init|=
name|md
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
operator|.
name|getUrl
argument_list|()
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|String
name|type1
init|=
name|md
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|1
index|]
operator|.
name|getType
argument_list|()
decl_stmt|;
name|String
name|url1
init|=
name|md
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|1
index|]
operator|.
name|getUrl
argument_list|()
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Default Maven URL must end with '/'"
argument_list|,
name|DEFAULT_M2_ROOT
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|jarUrl
init|=
name|DEFAULT_M2_ROOT
operator|+
literal|"org/apache/felix/org.apache.felix."
operator|+
literal|"bundlerepository/1.0.3/org.apache.felix.bundlerepository-1.0.3.jar"
decl_stmt|;
name|String
name|srcUrl
init|=
literal|"http://oscar-osgi.sf.net/obr2/org.apache.felix."
operator|+
literal|"bundlerepository/org.apache.felix.bundlerepository-1.0.3-src.jar"
decl_stmt|;
if|if
condition|(
name|type0
operator|.
name|equals
argument_list|(
literal|"jar"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|jarUrl
argument_list|,
name|url0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"source"
argument_list|,
name|type1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|srcUrl
argument_list|,
name|url1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|type1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jarUrl
argument_list|,
name|url1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"source"
argument_list|,
name|type0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|srcUrl
argument_list|,
name|url0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|assertEquals
argument_list|(
literal|"Felix-Releases"
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"20120203022437.168"
argument_list|,
name|repo
operator|.
name|getLastModified
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

