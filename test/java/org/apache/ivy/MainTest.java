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
package|;
end_package

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
name|retrieve
operator|.
name|RetrieveOptions
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
name|CacheCleaner
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
name|cli
operator|.
name|CommandLine
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
name|cli
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Set
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

begin_class
specifier|public
class|class
name|MainTest
block|{
specifier|private
name|File
name|cache
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|expExc
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelp
parameter_list|()
throws|throws
name|Exception
block|{
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-?"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadOption
parameter_list|()
throws|throws
name|Exception
block|{
name|expExc
operator|.
name|expect
argument_list|(
name|ParseException
operator|.
name|class
argument_list|)
expr_stmt|;
name|expExc
operator|.
name|expectMessage
argument_list|(
literal|"Unrecognized option: -bad"
argument_list|)
expr_stmt|;
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-bad"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|expExc
operator|.
name|expect
argument_list|(
name|ParseException
operator|.
name|class
argument_list|)
expr_stmt|;
name|expExc
operator|.
name|expectMessage
argument_list|(
literal|"no argument for: ivy"
argument_list|)
expr_stmt|;
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-ivy"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/cache/org1/mod1.2/ivy-2.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveSimpleWithConfs
parameter_list|()
throws|throws
name|Exception
block|{
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|,
literal|"-confs"
block|,
literal|"default"
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/cache/org1/mod1.2/ivy-2.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolveSimpleWithConfs2
parameter_list|()
throws|throws
name|Exception
block|{
name|run
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-confs"
block|,
literal|"default"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/cache/org1/mod1.2/ivy-2.0.xml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraParams1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|params
init|=
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-confs"
block|,
literal|"default"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|,
literal|"foo1"
block|,
literal|"foo2"
block|}
decl_stmt|;
name|CommandLine
name|line
init|=
name|Main
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|String
index|[]
name|leftOver
init|=
name|line
operator|.
name|getLeftOverArgs
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|leftOver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|leftOver
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo1"
argument_list|,
name|leftOver
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo2"
argument_list|,
name|leftOver
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraParams2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|params
init|=
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-confs"
block|,
literal|"default"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|,
literal|"--"
block|,
literal|"foo1"
block|,
literal|"foo2"
block|}
decl_stmt|;
name|CommandLine
name|line
init|=
name|Main
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|String
index|[]
name|leftOver
init|=
name|line
operator|.
name|getLeftOverArgs
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|leftOver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|leftOver
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo1"
argument_list|,
name|leftOver
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo2"
argument_list|,
name|leftOver
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraParams3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|params
init|=
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-confs"
block|,
literal|"default"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
block|}
decl_stmt|;
name|CommandLine
name|line
init|=
name|Main
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|String
index|[]
name|leftOver
init|=
name|line
operator|.
name|getLeftOverArgs
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|leftOver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|leftOver
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for IVY-1355.      * {@code types} argument to the command line must be parsed correctly when it's passed      * more than one value for the argument.      *      * @throws Exception if something goes wrong      * @see<a href="https://issues.apache.org/jira/browse/IVY-1355">IVY-1355</a>      */
annotation|@
name|Test
specifier|public
name|void
name|testTypes
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
index|[]
name|params
init|=
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-retrieve"
block|,
literal|"build/test/main/retrieve/[module]/[conf]/[artifact]-[revision].[ext]"
block|,
literal|"-types"
block|,
literal|"jar"
block|,
literal|"source"
block|}
decl_stmt|;
specifier|final
name|CommandLine
name|parsedCommand
init|=
name|Main
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|params
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|parsedTypes
init|=
name|parsedCommand
operator|.
name|getOptionValues
argument_list|(
literal|"types"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Values for types argument is missing"
argument_list|,
name|parsedTypes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of values parsed for types argument"
argument_list|,
literal|2
argument_list|,
name|parsedTypes
operator|.
name|length
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|uniqueParsedTypes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|parsedTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"jar type is missing from the parsed types argument"
argument_list|,
name|uniqueParsedTypes
operator|.
name|contains
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"jar type is missing from the parsed types argument"
argument_list|,
name|uniqueParsedTypes
operator|.
name|contains
argument_list|(
literal|"source"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that the {@code overwriteMode} passed for the retrieve command works as expected      *      * @throws Exception if something goes wrong      */
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveOverwriteMode
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"-settings"
block|,
literal|"test/repositories/ivysettings.xml"
block|,
literal|"-retrieve"
block|,
literal|"build/test/main/retrieve/overwrite-test/[artifact].[ext]"
block|,
literal|"-overwriteMode"
block|,
literal|"different"
block|,
literal|"-ivy"
block|,
literal|"test/repositories/1/org/mod1/ivys/ivy-5.0.xml"
block|}
decl_stmt|;
specifier|final
name|CommandLine
name|parsedCommand
init|=
name|Main
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|args
argument_list|)
decl_stmt|;
specifier|final
name|String
name|parsedOverwriteMode
init|=
name|parsedCommand
operator|.
name|getOptionValue
argument_list|(
literal|"overwriteMode"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected overwriteMode parsed"
argument_list|,
name|RetrieveOptions
operator|.
name|OVERWRITEMODE_DIFFERENT
argument_list|,
name|parsedOverwriteMode
argument_list|)
expr_stmt|;
comment|// create a dummy file which we expect the retrieve task to overwrite
specifier|final
name|Path
name|retrieveArtifactPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"build/test/main/retrieve/overwrite-test/foo-bar.jar"
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|retrieveArtifactPath
operator|.
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|retrieveArtifactPath
argument_list|,
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected content at "
operator|+
name|retrieveArtifactPath
argument_list|,
literal|0
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|retrieveArtifactPath
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// issue the retrieve (which retrieves the org:foo-bar:2.3.4 artifact)
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
comment|// expect the existing jar to be overwritten
name|assertTrue
argument_list|(
literal|"Content at "
operator|+
name|retrieveArtifactPath
operator|+
literal|" was not overwritten by retrieve task"
argument_list|,
name|Files
operator|.
name|readAllBytes
argument_list|(
name|retrieveArtifactPath
argument_list|)
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|run
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Main
operator|.
name|run
argument_list|(
name|Main
operator|.
name|getParser
argument_list|()
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

