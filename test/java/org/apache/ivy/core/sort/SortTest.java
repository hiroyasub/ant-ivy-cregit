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
name|sort
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Date
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DefaultDependencyDescriptor
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
name|DefaultModuleDescriptor
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

begin_comment
comment|/**  * @author Xavier Hanin  * @author baumkar  */
end_comment

begin_class
specifier|public
class|class
name|SortTest
extends|extends
name|TestCase
block|{
specifier|private
name|ModuleRevisionId
name|mrid1
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid2
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid3
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid4
decl_stmt|;
specifier|private
name|DefaultModuleDescriptor
index|[]
name|md
decl_stmt|;
name|List
name|toSort
decl_stmt|;
comment|/* (non-Javadoc)      * @see junit.framework.TestCase#setUp()      */
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mrid1
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|mrid2
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|mrid3
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|mrid4
operator|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
literal|"md4"
argument_list|,
literal|"rev4"
argument_list|)
expr_stmt|;
name|md
operator|=
operator|new
name|DefaultModuleDescriptor
index|[]
block|{
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid1
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
block|,
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid2
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
block|,
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid3
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
block|,
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid4
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
block|}
expr_stmt|;
name|md
index|[
literal|1
index|]
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid1
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|md
index|[
literal|2
index|]
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid2
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|md
index|[
literal|3
index|]
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid3
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSort
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
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|3
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|0
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSorted
parameter_list|(
name|DefaultModuleDescriptor
index|[]
name|md
parameter_list|,
name|List
name|sorted
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|md
operator|.
name|length
argument_list|,
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|md
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|md
index|[
name|i
index|]
argument_list|,
name|sorted
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// sorter does not throw circular dependency, circular dependencies are handled at resolve time only
comment|// because circular dependencies are more complicated to evaluate than just a callstack comparison
specifier|public
name|void
name|testCircularDependency
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
name|md
index|[
literal|0
index|]
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid4
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// the sorted array may begin by any of the modules since there is a circular dependency
comment|// in this case, the result is the following
name|DefaultModuleDescriptor
index|[]
name|sorted
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
block|{
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|3
index|]
block|,
name|md
index|[
literal|0
index|]
block|}
decl_stmt|;
name|assertSorted
argument_list|(
name|sorted
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCircularDependency2
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
name|md
index|[
literal|1
index|]
operator|.
name|addDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|mrid3
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|toSort
operator|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md
index|[
literal|0
index|]
block|,
name|md
index|[
literal|2
index|]
block|,
name|md
index|[
literal|1
index|]
block|,
name|md
index|[
literal|3
index|]
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertSorted
argument_list|(
name|md
argument_list|,
name|ivy
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

