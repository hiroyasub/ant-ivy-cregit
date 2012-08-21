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
name|settings
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
name|text
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
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
name|DependencyResolver
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
name|IBiblioResolver
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
name|IvyRepResolver
import|;
end_import

begin_class
specifier|public
class|class
name|ConfigureTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configureDefault
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyResolver
name|publicResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"public"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|publicResolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|publicResolver
operator|instanceof
name|IBiblioResolver
argument_list|)
expr_stmt|;
name|IBiblioResolver
name|ibiblio
init|=
operator|(
name|IBiblioResolver
operator|)
name|publicResolver
decl_stmt|;
name|assertTrue
argument_list|(
name|ibiblio
operator|.
name|isM2compatible
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefault14
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|configureDefault14
argument_list|()
expr_stmt|;
name|IvySettings
name|settings
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|settings
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyResolver
name|publicResolver
init|=
name|settings
operator|.
name|getResolver
argument_list|(
literal|"public"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|publicResolver
operator|instanceof
name|IvyRepResolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTypedefWithCustomClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.custom.test.dir"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/core/settings"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|ConfigureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-custom-typedef.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyResolver
name|custom
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolver
argument_list|(
literal|"custom"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|custom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.ivy.plugins.resolver.CustomResolver"
argument_list|,
name|custom
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTypedefWithCustomClasspathWithFile
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
literal|"ivy.custom.test.dir"
argument_list|,
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/core/settings"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|ConfigureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-custom-typedef2.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyResolver
name|custom
init|=
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolver
argument_list|(
literal|"custom"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|custom
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.ivy.plugins.resolver.CustomResolver"
argument_list|,
name|custom
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

