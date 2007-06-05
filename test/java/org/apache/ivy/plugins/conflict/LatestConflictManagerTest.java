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
name|conflict
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
name|cache
operator|.
name|CacheManager
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
name|core
operator|.
name|report
operator|.
name|ConfigurationResolveReport
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
name|IvyNode
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|LatestConflictManagerTest
extends|extends
name|TestCase
block|{
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-latest.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|_cache
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
name|FileUtil
operator|.
name|forceDelete
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
block|}
comment|// Test case for issue IVY-388
specifier|public
name|void
name|testIvy388
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-388.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|deps
init|=
name|report
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|Iterator
name|dependencies
init|=
name|deps
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
index|[]
name|confs
init|=
name|report
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
while|while
condition|(
name|dependencies
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|IvyNode
name|node
init|=
operator|(
name|IvyNode
operator|)
name|dependencies
operator|.
name|next
argument_list|()
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|conf
init|=
name|confs
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|isEvicted
argument_list|(
name|conf
argument_list|)
condition|)
block|{
name|boolean
name|flag1
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
name|conf
argument_list|)
operator|.
name|getDependency
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|boolean
name|flag2
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
name|conf
argument_list|)
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|contains
argument_list|(
name|node
operator|.
name|getResolvedId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Inconsistent data for node "
operator|+
name|node
operator|+
literal|" in conf "
operator|+
name|conf
argument_list|,
name|flag1
argument_list|,
name|flag2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// Test case for issue IVY-383
specifier|public
name|void
name|testIvy383
parameter_list|()
throws|throws
name|Exception
block|{
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-383.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
name|ConfigurationResolveReport
name|defaultReport
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|defaultReport
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.1"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.2"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"2.2"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Test case for issue IVY-407
specifier|public
name|void
name|testLatestTime1
parameter_list|()
throws|throws
name|Exception
block|{
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-latest-time.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|setVariable
argument_list|(
literal|"ivy.log.conflict.resolution"
argument_list|,
literal|"true"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// set timestamps, because svn is not preserving this information,
comment|// and the latest time strategy is relying on it
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
literal|10000
decl_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.2/jars/mod1.2-2.0.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.2/jars/mod1.2-2.2.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
operator|+
literal|2000
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-latest-time-1.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
name|ConfigurationResolveReport
name|defaultReport
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|defaultReport
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.1"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.2"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"2.2"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|testLatestTime2
parameter_list|()
throws|throws
name|Exception
block|{
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-latest-time.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|setVariable
argument_list|(
literal|"ivy.log.conflict.resolution"
argument_list|,
literal|"true"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// set timestamps, because svn is not preserving this information,
comment|// and the latest time strategy is relying on it
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
literal|10000
decl_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.2/jars/mod1.2-2.0.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.2/jars/mod1.2-2.2.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
operator|+
literal|2000
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-latest-time-2.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
name|ConfigurationResolveReport
name|defaultReport
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|defaultReport
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.1"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"mod1.2"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"2.2"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*      * Test case for issue IVY-407 (with transitivity) There are 5 modules A, B, C, D and E. 1)      * publish C-1.0.0, C-1.0.1 and C-1.0.2 2) B needs C-1.0.0 : retrieve ok and publish B-1.0.0 3)      * A needs B-1.0.0 and C-1.0.2 : retrieve ok and publish A-1.0.0 4) D needs C-1.0.1 : retrieve      * ok and publish D-1.0.0 5) E needs D-1.0.0 and A-1.0.0 (D before A in ivy file) retrieve      * failed to get C-1.0.2 from A (get apparently C-1.0.1 from D)      */
specifier|public
name|void
name|testLatestTimeTransitivity
parameter_list|()
throws|throws
name|Exception
block|{
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-latest-time-transitivity.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|setVariable
argument_list|(
literal|"ivy.log.conflict.resolution"
argument_list|,
literal|"true"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// set timestamps, because svn is not preserving this information,
comment|// and the latest time strategy is relying on it
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
literal|10000
decl_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-407/MyCompany/C/ivy-1.0.0.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-407/MyCompany/C/ivy-1.0.1.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
operator|+
literal|2000
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"test/repositories/IVY-407/MyCompany/C/ivy-1.0.2.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|time
operator|+
literal|4000
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|ivy
operator|.
name|resolve
argument_list|(
name|LatestConflictManagerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy-latest-time-transitivity.xml"
argument_list|)
argument_list|,
name|getResolveOptions
argument_list|()
argument_list|)
decl_stmt|;
name|ConfigurationResolveReport
name|defaultReport
init|=
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|Iterator
name|iter
init|=
name|defaultReport
operator|.
name|getModuleRevisionIds
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ModuleRevisionId
name|mrid
init|=
operator|(
name|ModuleRevisionId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"A"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"A revision should be 1.0.0"
argument_list|,
literal|"1.0.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"D"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"D revision should be 1.0.0"
argument_list|,
literal|"1.0.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// by transitivity
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"B"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"B revision should be 1.0.0"
argument_list|,
literal|"1.0.0"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|mrid
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"C"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"C revision should be 1.0.2"
argument_list|,
literal|"1.0.2"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|()
block|{
return|return
operator|new
name|ResolveOptions
argument_list|()
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setValidate
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

