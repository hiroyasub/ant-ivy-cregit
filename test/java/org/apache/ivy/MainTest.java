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
name|java
operator|.
name|io
operator|.
name|File
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
name|MainTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
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
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheCleaner
operator|.
name|deleteDir
argument_list|(
name|cache
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|testBadOption
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
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
name|fail
argument_list|(
literal|"running Ivy Main with -bad option should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Unrecognized option: -bad"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testMissingParameter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
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
name|fail
argument_list|(
literal|"running Ivy Main with missing argument for -ivy option should raise an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"no argument for: ivy"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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

