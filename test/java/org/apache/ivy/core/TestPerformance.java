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
name|Random
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
name|plugins
operator|.
name|parser
operator|.
name|xml
operator|.
name|XmlModuleDescriptorWriter
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
name|FileSystemResolver
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Delete
import|;
end_import

begin_comment
comment|/**  * Not a Junit test, performance depends on the machine on which the test is run...  */
end_comment

begin_class
specifier|public
class|class
name|TestPerformance
block|{
specifier|private
specifier|final
specifier|static
name|String
name|PATTERN
init|=
literal|"build/test/perf/[module]/[artifact]-[revision].[ext]"
decl_stmt|;
specifier|private
specifier|final
name|Ivy
name|_ivy
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|public
name|TestPerformance
parameter_list|()
throws|throws
name|Exception
block|{
name|_ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|FileSystemResolver
name|resolver
init|=
operator|new
name|FileSystemResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"def"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|_ivy
operator|.
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addIvyPattern
argument_list|(
name|PATTERN
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addArtifactPattern
argument_list|(
name|PATTERN
argument_list|)
expr_stmt|;
name|_ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|addResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|_ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|setDefaultResolver
argument_list|(
literal|"def"
argument_list|)
expr_stmt|;
block|}
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
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
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
name|cleanCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanCache
parameter_list|()
block|{
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanRepo
parameter_list|()
block|{
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/perf"
argument_list|)
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|generateModules
parameter_list|(
name|int
name|nbModules
parameter_list|,
name|int
name|minDependencies
parameter_list|,
name|int
name|maxDependencies
parameter_list|,
name|int
name|minVersions
parameter_list|,
name|int
name|maxVersions
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|nb
init|=
literal|0
decl_stmt|;
name|int
name|curDep
init|=
literal|1
decl_stmt|;
name|int
name|varDeps
init|=
name|maxDependencies
operator|-
name|minDependencies
decl_stmt|;
name|int
name|varVersions
init|=
name|maxVersions
operator|-
name|minVersions
decl_stmt|;
name|Random
name|r
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|nb
operator|<
name|nbModules
condition|)
block|{
name|int
name|deps
init|=
name|minDependencies
operator|+
name|r
operator|.
name|nextInt
argument_list|(
name|varDeps
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|versions
init|=
name|minVersions
operator|+
name|r
operator|.
name|nextInt
argument_list|(
name|varVersions
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|prevCurDep
init|=
name|curDep
decl_stmt|;
for|for
control|(
name|int
name|ver
init|=
literal|0
init|;
name|ver
operator|<
name|versions
condition|;
name|ver
operator|++
control|)
block|{
name|DefaultModuleDescriptor
name|md
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"mod"
operator|+
name|nb
argument_list|,
literal|"1."
operator|+
name|ver
argument_list|)
argument_list|,
literal|"integration"
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|curDep
operator|=
name|prevCurDep
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
name|deps
operator|&&
name|curDep
operator|<
name|nbModules
condition|;
name|i
operator|++
control|)
block|{
name|int
name|d
decl_stmt|;
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|1
condition|)
block|{
name|d
operator|=
name|nb
operator|+
name|i
expr_stmt|;
if|if
condition|(
name|d
operator|>=
name|prevCurDep
condition|)
block|{
name|d
operator|=
name|curDep
expr_stmt|;
name|curDep
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
name|d
operator|=
name|curDep
expr_stmt|;
name|curDep
operator|++
expr_stmt|;
block|}
name|DefaultDependencyDescriptor
name|dd
init|=
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|md
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"mod"
operator|+
name|d
argument_list|,
literal|"latest.integration"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|dd
operator|.
name|addDependencyConfiguration
argument_list|(
literal|"default"
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|md
operator|.
name|addDependency
argument_list|(
name|dd
argument_list|)
expr_stmt|;
block|}
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|md
argument_list|,
operator|new
name|File
argument_list|(
literal|"build/test/perf/mod"
operator|+
name|nb
operator|+
literal|"/ivy-1."
operator|+
name|ver
operator|+
literal|".xml"
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/jars/mod1.1-1.0.jar"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"build/test/perf/mod"
operator|+
name|nb
operator|+
literal|"/mod"
operator|+
name|nb
operator|+
literal|"-1."
operator|+
name|ver
operator|+
literal|".jar"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|nb
operator|++
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testPerfs
parameter_list|()
throws|throws
name|Exception
block|{
name|generateModules
argument_list|(
literal|70
argument_list|,
literal|2
argument_list|,
literal|5
argument_list|,
literal|2
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/perf/mod0/ivy-1.0.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|getResolveOptions
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|)
operator|.
name|setRevision
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"resolve "
operator|+
name|report
operator|.
name|getConfigurationReport
argument_list|(
literal|"default"
argument_list|)
operator|.
name|getNodesNumber
argument_list|()
operator|+
literal|" modules took "
operator|+
operator|(
name|end
operator|-
name|start
operator|)
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
name|cleanRepo
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ResolveOptions
name|getResolveOptions
parameter_list|(
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
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|_ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|_cache
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|TestPerformance
name|t
init|=
operator|new
name|TestPerformance
argument_list|()
decl_stmt|;
name|t
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|t
operator|.
name|testPerfs
argument_list|()
expr_stmt|;
name|t
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
