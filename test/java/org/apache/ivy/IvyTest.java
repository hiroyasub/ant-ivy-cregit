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
name|core
operator|.
name|IvyContext
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
name|ResolveReport
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
name|MockMessageLogger
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
name|IvyTest
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
name|createCache
argument_list|()
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
specifier|private
name|void
name|createCache
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
name|cache
operator|.
name|mkdirs
argument_list|()
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
name|testMultipleInstances
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this test checks that IvyContext is properly set and unset when using multiple instances
comment|// of Ivy. We also check logging, because it heavily relies on IvyContext.
comment|// we start by loading one ivy instance and using it to resolve some dependencies
name|MockMessageLogger
name|mockLogger
init|=
operator|new
name|MockMessageLogger
argument_list|()
decl_stmt|;
name|Ivy
name|ivy
init|=
name|Ivy
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|setDefaultLogger
argument_list|(
name|mockLogger
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivysettings.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"IvyContext should be cleared and return a default Ivy instance"
argument_list|,
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|==
name|ivy
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|mockLogger
operator|.
name|assertLogContains
argument_list|(
literal|"mod1.1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"IvyContext should be cleared and return a default Ivy instance"
argument_list|,
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|==
name|ivy
argument_list|)
expr_stmt|;
comment|// then we load another instance, and use it for another resolution
name|MockMessageLogger
name|mockLogger2
init|=
operator|new
name|MockMessageLogger
argument_list|()
decl_stmt|;
name|Ivy
name|ivy2
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy2
operator|.
name|getLoggerEngine
argument_list|()
operator|.
name|setDefaultLogger
argument_list|(
name|mockLogger2
argument_list|)
expr_stmt|;
name|ivy2
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/norev/ivysettings.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
name|report
operator|=
name|ivy2
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/norev/ivy.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|mockLogger2
operator|.
name|assertLogContains
argument_list|(
literal|"norev/ivysettings.xml"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"IvyContext should be cleared and return a default Ivy instance"
argument_list|,
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|==
name|ivy2
argument_list|)
expr_stmt|;
comment|// finally we reuse the first instance to make another resolution
name|report
operator|=
name|ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org6/mod6.1/ivys/ivy-0.3.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|(
name|ivy
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"extension"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|report
operator|.
name|hasError
argument_list|()
argument_list|)
expr_stmt|;
name|mockLogger
operator|.
name|assertLogContains
argument_list|(
literal|"mod6.1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"IvyContext should be cleared and return a default Ivy instance"
argument_list|,
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|==
name|ivy
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|Ivy
name|ivy
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
name|getResolveOptions
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|confs
argument_list|)
return|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|String
index|[]
name|confs
parameter_list|)
block|{
return|return
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setConfs
argument_list|(
name|confs
argument_list|)
return|;
block|}
block|}
end_class

end_unit

