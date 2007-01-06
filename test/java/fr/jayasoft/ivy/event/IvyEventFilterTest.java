begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DefaultModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
operator|.
name|resolve
operator|.
name|EndResolveEvent
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
operator|.
name|resolve
operator|.
name|StartResolveEvent
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|report
operator|.
name|ResolveReport
import|;
end_import

begin_class
specifier|public
class|class
name|IvyEventFilterTest
extends|extends
name|TestCase
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md2
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"foo2"
argument_list|,
literal|"bar"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md3
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"foo3"
argument_list|,
literal|"baz"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md4
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"foo"
argument_list|,
literal|"baz"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|void
name|testSimple
parameter_list|()
block|{
name|IvyEventFilter
name|f
init|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|EndResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|,
operator|new
name|ResolveReport
argument_list|(
name|md
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSimpleExpression
parameter_list|()
block|{
name|IvyEventFilter
name|f
init|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"organisation = foo"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"module = bar"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md3
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"organisation = foo, foo2"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md3
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAndExpression
parameter_list|()
block|{
name|IvyEventFilter
name|f
init|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"organisation = foo AND module = bar"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"organisation = foo,foo2 AND module = bar"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md3
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOrExpression
parameter_list|()
block|{
name|IvyEventFilter
name|f
init|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"organisation = foo3 OR module = bar"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md3
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotExpression
parameter_list|()
block|{
name|IvyEventFilter
name|f
init|=
operator|new
name|IvyEventFilter
argument_list|(
literal|"pre-resolve"
argument_list|,
literal|"NOT organisation = foo"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md2
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md3
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|f
operator|.
name|accept
argument_list|(
operator|new
name|StartResolveEvent
argument_list|(
name|ivy
argument_list|,
name|md4
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"default"
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

