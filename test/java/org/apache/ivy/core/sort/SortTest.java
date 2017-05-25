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
name|Collection
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
name|Iterator
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
name|descriptor
operator|.
name|DependencyDescriptor
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|circular
operator|.
name|CircularDependencyHelper
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
name|circular
operator|.
name|CircularDependencyStrategy
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
name|circular
operator|.
name|WarnCircularDependencyStrategy
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
name|version
operator|.
name|ExactVersionMatcher
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
name|version
operator|.
name|LatestVersionMatcher
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|SortTest
extends|extends
name|TestCase
block|{
specifier|private
name|DefaultModuleDescriptor
name|md1
decl_stmt|;
specifier|private
name|DefaultModuleDescriptor
name|md2
decl_stmt|;
specifier|private
name|DefaultModuleDescriptor
name|md3
decl_stmt|;
specifier|private
name|DefaultModuleDescriptor
name|md4
decl_stmt|;
specifier|private
name|SortEngine
name|sortEngine
decl_stmt|;
specifier|private
name|SimpleSortEngineSettings
name|settings
decl_stmt|;
specifier|private
name|SilentNonMatchingVersionReporter
name|nonMatchReporter
decl_stmt|;
comment|/*      * (non-Javadoc)      *       * @see junit.framework.TestCase#setUp()      */
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
name|md1
operator|=
name|createModuleDescriptorToSort
argument_list|(
literal|"md1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// The revison is often not set in the
comment|// ivy.xml file that are ordered
name|md2
operator|=
name|createModuleDescriptorToSort
argument_list|(
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
comment|// But somtimes they are set
name|md3
operator|=
name|createModuleDescriptorToSort
argument_list|(
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|md4
operator|=
name|createModuleDescriptorToSort
argument_list|(
literal|"md4"
argument_list|,
literal|"rev4"
argument_list|)
expr_stmt|;
name|settings
operator|=
operator|new
name|SimpleSortEngineSettings
argument_list|()
expr_stmt|;
name|settings
operator|.
name|setCircularDependencyStrategy
argument_list|(
name|WarnCircularDependencyStrategy
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVersionMatcher
argument_list|(
operator|new
name|ExactVersionMatcher
argument_list|()
argument_list|)
expr_stmt|;
name|sortEngine
operator|=
operator|new
name|SortEngine
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|nonMatchReporter
operator|=
operator|new
name|SilentNonMatchingVersionReporter
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSort
parameter_list|()
throws|throws
name|Exception
block|{
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
index|[]
index|[]
name|expectedOrder
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
index|[]
block|{
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
block|}
decl_stmt|;
name|Collection
name|permutations
init|=
name|getAllLists
argument_list|(
name|md1
argument_list|,
name|md3
argument_list|,
name|md2
argument_list|,
name|md4
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|permutations
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
name|toSort
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSorted
argument_list|(
name|expectedOrder
argument_list|,
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sorter does not throw circular dependency, circular dependencies are handled at resolve time      * only. However the sort respect the transitive order when it is unambiguous. (if A depends      * transitively of B, but B doesn't depends transitively on A then B always comes before A).      */
specifier|public
name|void
name|testCircularDependency
parameter_list|()
throws|throws
name|Exception
block|{
name|addDependency
argument_list|(
name|md1
argument_list|,
literal|"md4"
argument_list|,
literal|"rev4"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
index|[]
index|[]
name|possibleOrder
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
index|[]
block|{
block|{
name|md2
block|,
name|md3
block|,
name|md4
block|,
name|md1
block|}
block|,
block|{
name|md3
block|,
name|md4
block|,
name|md1
block|,
name|md2
block|}
block|,
block|{
name|md4
block|,
name|md1
block|,
name|md2
block|,
name|md3
block|}
block|,
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
block|}
decl_stmt|;
name|Collection
name|permutations
init|=
name|getAllLists
argument_list|(
name|md1
argument_list|,
name|md3
argument_list|,
name|md2
argument_list|,
name|md4
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|permutations
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
name|toSort
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSorted
argument_list|(
name|possibleOrder
argument_list|,
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCircularDependency2
parameter_list|()
throws|throws
name|Exception
block|{
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
index|[]
index|[]
name|possibleOrder
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
index|[]
block|{
block|{
name|md1
block|,
name|md3
block|,
name|md2
block|,
name|md4
block|}
block|,
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
comment|// ,
comment|// {md3, md1, md2, md4} //we don't have this solution. The loops apear has one contigous
comment|// element.
block|}
decl_stmt|;
name|Collection
name|permutations
init|=
name|getAllLists
argument_list|(
name|md1
argument_list|,
name|md3
argument_list|,
name|md2
argument_list|,
name|md4
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|permutations
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
name|toSort
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSorted
argument_list|(
name|possibleOrder
argument_list|,
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Test IVY-624
specifier|public
name|void
name|testCircularDependencyInfiniteLoop
parameter_list|()
throws|throws
name|Exception
block|{
name|addDependency
argument_list|(
name|md1
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md1
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md4"
argument_list|,
literal|"rev4"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|List
name|toSort
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
argument_list|)
decl_stmt|;
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
expr_stmt|;
comment|// If it ends, it's ok.
block|}
comment|/**      * In case of Circular dependency a warning is generated.      */
specifier|public
name|void
name|testCircularDependencyReport
parameter_list|()
block|{
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
comment|// Would be much easier with a tool like jmock
class|class
name|CircularDependencyReporterMock
implements|implements
name|CircularDependencyStrategy
block|{
specifier|private
name|int
name|nbOfCall
init|=
literal|0
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"CircularDependencyReporterMock"
return|;
block|}
specifier|public
name|void
name|handleCircularDependency
parameter_list|(
name|ModuleRevisionId
index|[]
name|mrids
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"handleCircularDependency is expected to be called only once"
argument_list|,
literal|0
argument_list|,
name|nbOfCall
argument_list|)
expr_stmt|;
name|String
name|assertMsg
init|=
literal|"incorrect cicular dependency invocation"
operator|+
name|CircularDependencyHelper
operator|.
name|formatMessage
argument_list|(
name|mrids
argument_list|)
decl_stmt|;
specifier|final
name|int
name|expectedLength
init|=
literal|3
decl_stmt|;
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|expectedLength
argument_list|,
name|mrids
operator|.
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|mrids
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|md2
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|md3
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mrids
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|md2
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mrids
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|md3
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mrids
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|md2
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mrids
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|assertMsg
argument_list|,
name|md3
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mrids
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
name|nbOfCall
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"handleCircularDependency has nor been called"
argument_list|,
literal|1
argument_list|,
name|nbOfCall
argument_list|)
expr_stmt|;
block|}
block|}
name|CircularDependencyReporterMock
name|circularDepReportMock
init|=
operator|new
name|CircularDependencyReporterMock
argument_list|()
decl_stmt|;
name|settings
operator|.
name|setCircularDependencyStrategy
argument_list|(
name|circularDepReportMock
argument_list|)
expr_stmt|;
name|List
name|toSort
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ModuleDescriptor
index|[]
block|{
name|md4
block|,
name|md3
block|,
name|md2
block|,
name|md1
block|}
argument_list|)
decl_stmt|;
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
expr_stmt|;
name|circularDepReportMock
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
comment|/**      * The dependency can ask for the latest integration. It should match whatever the version      * declared in the modules to order.      */
specifier|public
name|void
name|testLatestIntegration
parameter_list|()
block|{
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"latest.integration"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"latest.integration"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"latest.integration"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setVersionMatcher
argument_list|(
operator|new
name|LatestVersionMatcher
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
index|[]
index|[]
name|expectedOrder
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
index|[]
block|{
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
block|}
decl_stmt|;
name|Collection
name|permutations
init|=
name|getAllLists
argument_list|(
name|md1
argument_list|,
name|md3
argument_list|,
name|md2
argument_list|,
name|md4
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|permutations
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
name|toSort
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSorted
argument_list|(
name|expectedOrder
argument_list|,
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * When the version asked by a dependency is not compatible with the version declared in the      * module to order, the two modules should be considered as independant NB: I'm sure of what      * 'compatible' means !      */
specifier|public
name|void
name|testDifferentVersionNotConsidered
parameter_list|()
block|{
comment|// To test it, I use a 'broken' loop (in one step, I change the revision) in such a way that
comment|// I get only one solution. If the loop was
comment|// complete more solutions where possible.
name|addDependency
argument_list|(
name|md1
argument_list|,
literal|"md4"
argument_list|,
literal|"rev4-other"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
name|DefaultModuleDescriptor
index|[]
index|[]
name|possibleOrder
init|=
operator|new
name|DefaultModuleDescriptor
index|[]
index|[]
block|{
block|{
name|md1
block|,
name|md2
block|,
name|md3
block|,
name|md4
block|}
block|}
decl_stmt|;
name|Collection
name|permutations
init|=
name|getAllLists
argument_list|(
name|md1
argument_list|,
name|md3
argument_list|,
name|md2
argument_list|,
name|md4
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|permutations
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|List
name|toSort
init|=
operator|(
name|List
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertSorted
argument_list|(
name|possibleOrder
argument_list|,
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchReporter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * In case of Different version a warning is generated.      */
specifier|public
name|void
name|testDifferentVersionWarning
parameter_list|()
block|{
specifier|final
name|DependencyDescriptor
name|md4OtherDep
init|=
name|addDependency
argument_list|(
name|md1
argument_list|,
literal|"md4"
argument_list|,
literal|"rev4-other"
argument_list|)
decl_stmt|;
name|addDependency
argument_list|(
name|md2
argument_list|,
literal|"md1"
argument_list|,
literal|"rev1"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md3
argument_list|,
literal|"md2"
argument_list|,
literal|"rev2"
argument_list|)
expr_stmt|;
name|addDependency
argument_list|(
name|md4
argument_list|,
literal|"md3"
argument_list|,
literal|"rev3"
argument_list|)
expr_stmt|;
comment|// Would be much easier with a tool like jmock
class|class
name|NonMatchingVersionReporterMock
implements|implements
name|NonMatchingVersionReporter
block|{
specifier|private
name|int
name|nbOfCall
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|reportNonMatchingVersion
parameter_list|(
name|DependencyDescriptor
name|descriptor
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"reportNonMatchingVersion should be invokded only once"
argument_list|,
literal|0
argument_list|,
name|nbOfCall
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|md4OtherDep
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|md4
argument_list|,
name|md
argument_list|)
expr_stmt|;
name|nbOfCall
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"reportNonMatchingVersion has not be called"
argument_list|,
literal|1
argument_list|,
name|nbOfCall
argument_list|)
expr_stmt|;
block|}
block|}
name|NonMatchingVersionReporterMock
name|nonMatchingVersionReporterMock
init|=
operator|new
name|NonMatchingVersionReporterMock
argument_list|()
decl_stmt|;
name|List
name|toSort
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ModuleDescriptor
index|[]
block|{
name|md4
block|,
name|md3
block|,
name|md2
block|,
name|md1
block|}
argument_list|)
decl_stmt|;
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
name|nonMatchingVersionReporterMock
argument_list|)
expr_stmt|;
name|nonMatchingVersionReporterMock
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
specifier|private
name|List
name|sortModuleDescriptors
parameter_list|(
name|List
name|toSort
parameter_list|,
name|NonMatchingVersionReporter
name|nonMatchingVersionReporter
parameter_list|)
block|{
return|return
name|sortEngine
operator|.
name|sortModuleDescriptors
argument_list|(
name|toSort
argument_list|,
operator|new
name|SortOptions
argument_list|()
operator|.
name|setNonMatchingVersionReporter
argument_list|(
name|nonMatchingVersionReporter
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|DefaultModuleDescriptor
name|createModuleDescriptorToSort
parameter_list|(
name|String
name|moduleName
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
name|moduleName
argument_list|,
name|revision
argument_list|)
decl_stmt|;
return|return
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|DependencyDescriptor
name|addDependency
parameter_list|(
name|DefaultModuleDescriptor
name|parent
parameter_list|,
name|String
name|moduleName
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org"
argument_list|,
name|moduleName
argument_list|,
name|revision
argument_list|)
decl_stmt|;
name|DependencyDescriptor
name|depDescr
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|parent
argument_list|,
name|mrid
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|parent
operator|.
name|addDependency
argument_list|(
name|depDescr
argument_list|)
expr_stmt|;
return|return
name|depDescr
return|;
block|}
comment|/**      * Verifies that sorted in one of the list of listOfPossibleSort.      *       * @param listOfPossibleSort      *            array of possible sort result      * @param sorted      *            actual sortedList to compare      */
specifier|private
name|void
name|assertSorted
parameter_list|(
name|DefaultModuleDescriptor
index|[]
index|[]
name|listOfPossibleSort
parameter_list|,
name|List
name|sorted
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|listOfPossibleSort
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DefaultModuleDescriptor
index|[]
name|expectedList
init|=
name|listOfPossibleSort
index|[
name|i
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedList
operator|.
name|length
argument_list|,
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isExpected
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|expectedList
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|expectedList
index|[
name|j
index|]
operator|.
name|equals
argument_list|(
name|sorted
operator|.
name|get
argument_list|(
name|j
argument_list|)
argument_list|)
condition|)
block|{
name|isExpected
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|isExpected
condition|)
block|{
return|return;
block|}
block|}
comment|// failed, build a nice message
name|StringBuffer
name|errorMessage
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|errorMessage
operator|.
name|append
argument_list|(
literal|"Unexpected order : \n{ "
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
name|sorted
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|errorMessage
operator|.
name|append
argument_list|(
literal|" , "
argument_list|)
expr_stmt|;
block|}
name|errorMessage
operator|.
name|append
argument_list|(
operator|(
operator|(
name|DefaultModuleDescriptor
operator|)
name|sorted
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|errorMessage
operator|.
name|append
argument_list|(
literal|"}\nEpected : \n"
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
name|listOfPossibleSort
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DefaultModuleDescriptor
index|[]
name|expectedList
init|=
name|listOfPossibleSort
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|errorMessage
operator|.
name|append
argument_list|(
literal|" or\n"
argument_list|)
expr_stmt|;
block|}
name|errorMessage
operator|.
name|append
argument_list|(
literal|"{ "
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|expectedList
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|j
operator|>
literal|0
condition|)
block|{
name|errorMessage
operator|.
name|append
argument_list|(
literal|" , "
argument_list|)
expr_stmt|;
block|}
name|errorMessage
operator|.
name|append
argument_list|(
name|expectedList
index|[
name|j
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|errorMessage
operator|.
name|append
argument_list|(
literal|" } "
argument_list|)
expr_stmt|;
block|}
name|fail
argument_list|(
name|errorMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Returns a collection of lists that contains the elements a,b,c and d */
specifier|private
name|Collection
name|getAllLists
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|,
name|Object
name|c
parameter_list|,
name|Object
name|d
parameter_list|)
block|{
specifier|final
name|int
name|nbOfList
init|=
literal|24
decl_stmt|;
name|ArrayList
name|r
init|=
operator|new
name|ArrayList
argument_list|(
name|nbOfList
argument_list|)
decl_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|b
block|,
name|c
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|b
block|,
name|d
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|c
block|,
name|b
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|c
block|,
name|d
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|d
block|,
name|b
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|a
block|,
name|d
block|,
name|c
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|a
block|,
name|c
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|a
block|,
name|d
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|c
block|,
name|a
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|c
block|,
name|d
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|d
block|,
name|a
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|b
block|,
name|d
block|,
name|c
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|b
block|,
name|a
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|b
block|,
name|d
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|a
block|,
name|b
block|,
name|d
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|a
block|,
name|d
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|d
block|,
name|b
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|c
block|,
name|d
block|,
name|a
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|b
block|,
name|c
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|b
block|,
name|a
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|c
block|,
name|b
block|,
name|a
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|c
block|,
name|a
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|a
block|,
name|b
block|,
name|c
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|d
block|,
name|a
block|,
name|c
block|,
name|b
block|}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

