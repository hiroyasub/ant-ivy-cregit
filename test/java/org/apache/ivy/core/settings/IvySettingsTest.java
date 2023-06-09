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
name|junit
operator|.
name|Test
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
name|assertNotSame
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
name|assertNull
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
name|assertSame
import|;
end_import

begin_class
specifier|public
class|class
name|IvySettingsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testChangeDefaultResolver
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
name|DependencyResolver
name|defaultResolver
init|=
name|settings
operator|.
name|getDefaultResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultResolver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|defaultResolver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"default resolver cached"
argument_list|,
name|defaultResolver
argument_list|,
name|settings
operator|.
name|getDefaultResolver
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setDefaultResolver
argument_list|(
literal|"public"
argument_list|)
expr_stmt|;
name|DependencyResolver
name|newDefault
init|=
name|settings
operator|.
name|getDefaultResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|newDefault
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"default resolver has changed"
argument_list|,
name|defaultResolver
argument_list|,
name|newDefault
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolver changed successfully"
argument_list|,
literal|"public"
argument_list|,
name|newDefault
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVariables
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
comment|// test set
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test no override
name|settings
operator|.
name|setVariable
argument_list|(
literal|"foo"
argument_list|,
literal|"wrong"
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test override
name|settings
operator|.
name|setVariable
argument_list|(
literal|"foo"
argument_list|,
literal|"right"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"right"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset no exist
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|,
literal|"noexist"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset exist
name|settings
operator|.
name|setVariable
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|,
literal|"foo"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test unlessset exist
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"thing"
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"thing"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"thing"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test unlessset noexist
name|settings
operator|.
name|setVariable
argument_list|(
literal|"thing"
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|"noexist"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"thing"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset no exist and unlessset exist
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy"
argument_list|)
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy"
argument_list|,
literal|"rocks"
argument_list|,
literal|true
argument_list|,
literal|"noexist"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset no exist and unlessset no exist
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy"
argument_list|,
literal|"rocks"
argument_list|,
literal|true
argument_list|,
literal|"noexist"
argument_list|,
literal|"noexist"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset exist and unlessset exist
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy"
argument_list|,
literal|"rocks"
argument_list|,
literal|true
argument_list|,
literal|"foo"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test ifset exist and unlessset no exist
name|settings
operator|.
name|setVariable
argument_list|(
literal|"ivy"
argument_list|,
literal|"rocks"
argument_list|,
literal|true
argument_list|,
literal|"foo"
argument_list|,
literal|"noexist"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rocks"
argument_list|,
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

