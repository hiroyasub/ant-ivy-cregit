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
name|plugins
operator|.
name|latest
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
name|Collections
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

begin_class
specifier|public
class|class
name|LatestRevisionStrategyTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|ArtifactInfo
index|[]
name|revs
init|=
name|toMockAI
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"0.2a"
block|,
literal|"0.2_b"
block|,
literal|"0.2rc1"
block|,
literal|"0.2-final"
block|,
literal|"1.0-dev1"
block|,
literal|"1.0-dev2"
block|,
literal|"1.0-alpha1"
block|,
literal|"1.0-alpha2"
block|,
literal|"1.0-beta1"
block|,
literal|"1.0-beta2"
block|,
literal|"1.0-gamma"
block|,
literal|"1.0-rc1"
block|,
literal|"1.0-rc2"
block|,
literal|"1.0"
block|,
literal|"1.0.1"
block|,
literal|"2.0"
block|}
argument_list|)
decl_stmt|;
name|List
name|shuffled
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|shuffled
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|shuffled
argument_list|,
operator|new
name|LatestRevisionStrategy
argument_list|()
operator|.
name|COMPARATOR
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|,
name|shuffled
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSort
parameter_list|()
block|{
name|ArtifactInfo
index|[]
name|revs
init|=
name|toMockAI
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"0.2a"
block|,
literal|"0.2_b"
block|,
literal|"0.2rc1"
block|,
literal|"0.2-final"
block|,
literal|"1.0-dev1"
block|,
literal|"1.0-dev2"
block|,
literal|"1.0-alpha1"
block|,
literal|"1.0-alpha2"
block|,
literal|"1.0-beta1"
block|,
literal|"1.0-beta2"
block|,
literal|"1.0-gamma"
block|,
literal|"1.0-rc1"
block|,
literal|"1.0-rc2"
block|,
literal|"1.0"
block|,
literal|"1.0.1"
block|,
literal|"2.0"
block|}
argument_list|)
decl_stmt|;
name|List
name|shuffled
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|)
decl_stmt|;
name|ArtifactInfo
index|[]
name|shuffledRevs
init|=
operator|(
name|ArtifactInfo
index|[]
operator|)
name|shuffled
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactInfo
index|[
name|revs
operator|.
name|length
index|]
argument_list|)
decl_stmt|;
name|LatestRevisionStrategy
name|latestRevisionStrategy
init|=
operator|new
name|LatestRevisionStrategy
argument_list|()
decl_stmt|;
name|List
name|sorted
init|=
name|latestRevisionStrategy
operator|.
name|sort
argument_list|(
name|shuffledRevs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFindLatest
parameter_list|()
block|{
name|ArtifactInfo
index|[]
name|revs
init|=
name|toMockAI
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"0.2a"
block|,
literal|"0.2_b"
block|,
literal|"0.2rc1"
block|,
literal|"0.2-final"
block|,
literal|"1.0-dev1"
block|,
literal|"1.0-dev2"
block|,
literal|"1.0-alpha1"
block|,
literal|"1.0-alpha2"
block|,
literal|"1.0-beta1"
block|,
literal|"1.0-beta2"
block|,
literal|"1.0-gamma"
block|,
literal|"1.0-rc1"
block|,
literal|"1.0-rc2"
block|,
literal|"1.0"
block|,
literal|"1.0.1"
block|,
literal|"2.0"
block|}
argument_list|)
decl_stmt|;
name|List
name|shuffled
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|)
decl_stmt|;
name|ArtifactInfo
index|[]
name|shuffledRevs
init|=
operator|(
name|ArtifactInfo
index|[]
operator|)
name|shuffled
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactInfo
index|[
name|revs
operator|.
name|length
index|]
argument_list|)
decl_stmt|;
name|LatestRevisionStrategy
name|latestRevisionStrategy
init|=
operator|new
name|LatestRevisionStrategy
argument_list|()
decl_stmt|;
name|ArtifactInfo
name|latest
init|=
name|latestRevisionStrategy
operator|.
name|findLatest
argument_list|(
name|shuffledRevs
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|latest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2.0"
argument_list|,
name|latest
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSpecialMeaningComparator
parameter_list|()
block|{
name|ArtifactInfo
index|[]
name|revs
init|=
name|toMockAI
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"0.1"
block|,
literal|"0.2-pre"
block|,
literal|"0.2-dev"
block|,
literal|"0.2-rc1"
block|,
literal|"0.2-final"
block|,
literal|"0.2-QA"
block|,
literal|"1.0-dev1"
block|,}
argument_list|)
decl_stmt|;
name|List
name|shuffled
init|=
operator|new
name|ArrayList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|shuffle
argument_list|(
name|shuffled
argument_list|)
expr_stmt|;
name|LatestRevisionStrategy
name|latestRevisionStrategy
init|=
operator|new
name|LatestRevisionStrategy
argument_list|()
decl_stmt|;
name|LatestRevisionStrategy
operator|.
name|SpecialMeaning
name|specialMeaning
init|=
operator|new
name|LatestRevisionStrategy
operator|.
name|SpecialMeaning
argument_list|()
decl_stmt|;
name|specialMeaning
operator|.
name|setName
argument_list|(
literal|"pre"
argument_list|)
expr_stmt|;
name|specialMeaning
operator|.
name|setValue
argument_list|(
operator|new
name|Integer
argument_list|(
operator|-
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|latestRevisionStrategy
operator|.
name|addConfiguredSpecialMeaning
argument_list|(
name|specialMeaning
argument_list|)
expr_stmt|;
name|specialMeaning
operator|=
operator|new
name|LatestRevisionStrategy
operator|.
name|SpecialMeaning
argument_list|()
expr_stmt|;
name|specialMeaning
operator|.
name|setName
argument_list|(
literal|"QA"
argument_list|)
expr_stmt|;
name|specialMeaning
operator|.
name|setValue
argument_list|(
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|latestRevisionStrategy
operator|.
name|addConfiguredSpecialMeaning
argument_list|(
name|specialMeaning
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|shuffled
argument_list|,
name|latestRevisionStrategy
operator|.
name|COMPARATOR
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|,
name|shuffled
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|MockArtifactInfo
implements|implements
name|ArtifactInfo
block|{
specifier|private
name|long
name|_lastModified
decl_stmt|;
specifier|private
name|String
name|_rev
decl_stmt|;
specifier|public
name|MockArtifactInfo
parameter_list|(
name|String
name|rev
parameter_list|,
name|long
name|lastModified
parameter_list|)
block|{
name|_rev
operator|=
name|rev
expr_stmt|;
name|_lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_rev
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|_lastModified
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|_rev
return|;
block|}
block|}
specifier|private
name|ArtifactInfo
index|[]
name|toMockAI
parameter_list|(
name|String
index|[]
name|revs
parameter_list|)
block|{
name|ArtifactInfo
index|[]
name|artifactInfos
init|=
operator|new
name|ArtifactInfo
index|[
name|revs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifactInfos
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|artifactInfos
index|[
name|i
index|]
operator|=
operator|new
name|MockArtifactInfo
argument_list|(
name|revs
index|[
name|i
index|]
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactInfos
return|;
block|}
block|}
end_class

end_unit

