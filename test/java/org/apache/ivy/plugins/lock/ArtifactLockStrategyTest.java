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
name|lock
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
name|core
operator|.
name|cache
operator|.
name|DefaultRepositoryCacheManager
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
name|RepositoryCacheManager
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
name|event
operator|.
name|EventManager
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
name|resolve
operator|.
name|ResolveData
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
name|ResolveEngine
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|core
operator|.
name|sort
operator|.
name|SortEngine
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
name|repository
operator|.
name|RepositoryCopyProgressListener
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
name|repository
operator|.
name|file
operator|.
name|FileRepository
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
name|CopyProgressEvent
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
name|ivy
operator|.
name|util
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ArtifactLockStrategyTest
extends|extends
name|TestCase
block|{
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/cache"
argument_list|)
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
name|FileUtil
operator|.
name|forceDelete
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/cache"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConcurrentResolve
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we use different settings because Ivy do not support multi thread resolve with the same
comment|// settings yet and this is not what this test is about: the focus of this test is running
comment|// concurrent resolves in separate vms but using the same cache. We don't span the test on
comment|// multiple vms, but using separate settings we should only run into shared cache related
comment|// issues, and not multi thread related issues.
name|IvySettings
name|settings1
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
name|IvySettings
name|settings2
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
name|IvySettings
name|settings3
init|=
operator|new
name|IvySettings
argument_list|()
decl_stmt|;
comment|// run 3 concurrent resolves, one taking 100ms to download files, one 20ms and one 5ms
comment|// the first one do 10 resolves, the second one 20 and the third 50
comment|// note that the download time is useful only at the very beginning, then the cached file is used
name|ResolveThread
name|t1
init|=
name|asyncResolve
argument_list|(
name|settings1
argument_list|,
name|createSlowResolver
argument_list|(
name|settings1
argument_list|,
literal|100
argument_list|)
argument_list|,
literal|"org6#mod6.4;3"
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|ResolveThread
name|t2
init|=
name|asyncResolve
argument_list|(
name|settings2
argument_list|,
name|createSlowResolver
argument_list|(
name|settings2
argument_list|,
literal|20
argument_list|)
argument_list|,
literal|"org6#mod6.4;3"
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|ResolveThread
name|t3
init|=
name|asyncResolve
argument_list|(
name|settings3
argument_list|,
name|createSlowResolver
argument_list|(
name|settings3
argument_list|,
literal|5
argument_list|)
argument_list|,
literal|"org6#mod6.4;3"
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|t1
operator|.
name|join
argument_list|(
literal|100000
argument_list|)
expr_stmt|;
name|t2
operator|.
name|join
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|t3
operator|.
name|join
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|t1
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertFound
argument_list|(
literal|"org6#mod6.4;3"
argument_list|,
name|t1
operator|.
name|getFinalResult
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|t2
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertFound
argument_list|(
literal|"org6#mod6.4;3"
argument_list|,
name|t2
operator|.
name|getFinalResult
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|t3
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertFound
argument_list|(
literal|"org6#mod6.4;3"
argument_list|,
name|t3
operator|.
name|getFinalResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|RepositoryCacheManager
name|newCacheManager
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|DefaultRepositoryCacheManager
name|cacheManager
init|=
operator|new
name|DefaultRepositoryCacheManager
argument_list|(
literal|"cache"
argument_list|,
name|settings
argument_list|,
operator|new
name|File
argument_list|(
literal|"build/test/cache"
argument_list|)
argument_list|)
decl_stmt|;
name|cacheManager
operator|.
name|setLockStrategy
argument_list|(
operator|new
name|ArtifactLockStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cacheManager
return|;
block|}
specifier|private
name|FileSystemResolver
name|createSlowResolver
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
specifier|final
name|int
name|sleep
parameter_list|)
block|{
name|FileSystemResolver
name|resolver
init|=
operator|new
name|FileSystemResolver
argument_list|()
decl_stmt|;
name|resolver
operator|.
name|setRepositoryCacheManager
argument_list|(
name|newCacheManager
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setRepository
argument_list|(
operator|new
name|FileRepository
argument_list|()
block|{
specifier|private
name|RepositoryCopyProgressListener
name|progress
init|=
operator|new
name|RepositoryCopyProgressListener
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|CopyProgressEvent
name|evt
parameter_list|)
block|{
name|super
operator|.
name|progress
argument_list|(
name|evt
argument_list|)
expr_stmt|;
name|sleepSilently
argument_list|(
name|sleep
argument_list|)
expr_stmt|;
comment|// makes the file copy longer to test concurrency issues
block|}
block|}
decl_stmt|;
specifier|protected
name|RepositoryCopyProgressListener
name|getProgressListener
parameter_list|()
block|{
return|return
name|progress
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addIvyPattern
argument_list|(
literal|"test/repositories/1/[organisation]/[module]/[type]s/[artifact]-[revision].[ext]"
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addArtifactPattern
argument_list|(
literal|"test/repositories/1/[organisation]/[module]/[type]s/[artifact]-[revision].[ext]"
argument_list|)
expr_stmt|;
return|return
name|resolver
return|;
block|}
specifier|private
name|ResolveThread
name|asyncResolve
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|FileSystemResolver
name|resolver
parameter_list|,
name|String
name|module
parameter_list|,
name|int
name|loop
parameter_list|)
block|{
name|ResolveThread
name|thread
init|=
operator|new
name|ResolveThread
argument_list|(
name|settings
argument_list|,
name|resolver
argument_list|,
name|module
argument_list|,
name|loop
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|thread
return|;
block|}
specifier|private
name|void
name|assertFound
parameter_list|(
name|String
name|module
parameter_list|,
name|ResolvedModuleRevision
name|rmr
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|rmr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|module
argument_list|,
name|rmr
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ResolvedModuleRevision
name|resolveModule
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|FileSystemResolver
name|resolver
parameter_list|,
name|String
name|module
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
name|resolver
operator|.
name|getDependency
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|module
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|ResolveData
argument_list|(
operator|new
name|ResolveEngine
argument_list|(
name|settings
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|,
operator|new
name|SortEngine
argument_list|(
name|settings
argument_list|)
argument_list|)
argument_list|,
operator|new
name|ResolveOptions
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|void
name|sleepSilently
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
block|}
block|}
specifier|private
class|class
name|ResolveThread
extends|extends
name|Thread
block|{
specifier|private
name|IvySettings
name|settings
decl_stmt|;
specifier|private
name|FileSystemResolver
name|resolver
decl_stmt|;
specifier|private
name|String
name|module
decl_stmt|;
specifier|private
specifier|final
name|int
name|loop
decl_stmt|;
specifier|private
name|ResolvedModuleRevision
name|finalResult
decl_stmt|;
specifier|private
name|int
name|count
decl_stmt|;
specifier|public
name|ResolveThread
parameter_list|(
name|IvySettings
name|settings
parameter_list|,
name|FileSystemResolver
name|resolver
parameter_list|,
name|String
name|module
parameter_list|,
name|int
name|loop
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
name|this
operator|.
name|loop
operator|=
name|loop
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|ResolvedModuleRevision
name|getFinalResult
parameter_list|()
block|{
return|return
name|finalResult
return|;
block|}
specifier|public
specifier|synchronized
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ResolvedModuleRevision
name|rmr
init|=
literal|null
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
name|loop
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|rmr
operator|=
name|resolveModule
argument_list|(
name|settings
argument_list|,
name|resolver
argument_list|,
name|module
argument_list|)
expr_stmt|;
if|if
condition|(
name|rmr
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"module not found: "
operator|+
name|module
argument_list|)
throw|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|//Message.info(this.toString() + " count = " + count);
name|count
operator|++
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"parse exception "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"exception "
operator|+
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"exception "
operator|+
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|finalResult
operator|=
name|rmr
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

