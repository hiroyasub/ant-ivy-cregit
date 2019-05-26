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
name|sort
package|;
end_package

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
name|version
operator|.
name|VersionMatcher
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleSortEngineSettings
implements|implements
name|SortEngineSettings
block|{
specifier|private
name|CircularDependencyStrategy
name|circularStrategy
decl_stmt|;
specifier|private
name|VersionMatcher
name|versionMatcher
decl_stmt|;
specifier|public
name|CircularDependencyStrategy
name|getCircularDependencyStrategy
parameter_list|()
block|{
return|return
name|circularStrategy
return|;
block|}
specifier|public
name|VersionMatcher
name|getVersionMatcher
parameter_list|()
block|{
return|return
name|versionMatcher
return|;
block|}
specifier|public
name|void
name|setCircularDependencyStrategy
parameter_list|(
name|CircularDependencyStrategy
name|strategy
parameter_list|)
block|{
name|circularStrategy
operator|=
name|strategy
expr_stmt|;
block|}
specifier|public
name|void
name|setVersionMatcher
parameter_list|(
name|VersionMatcher
name|matcher
parameter_list|)
block|{
name|versionMatcher
operator|=
name|matcher
expr_stmt|;
block|}
block|}
end_class

end_unit

