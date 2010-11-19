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
name|osgi
operator|.
name|ivy
operator|.
name|internal
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
name|Resource
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

begin_comment
comment|/**  * A file repository that handles extracting jar file entries using the bang(!) notation to separate the internal  * entry name.  *   * @author alex@radeski.net  */
end_comment

begin_class
specifier|public
class|class
name|JarFileRepository
extends|extends
name|FileRepository
block|{
specifier|private
specifier|final
name|RepositoryJarHandler
name|jarHandler
init|=
operator|new
name|RepositoryJarHandler
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|get
parameter_list|(
name|String
name|source
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|jarHandler
operator|.
name|canHandle
argument_list|(
name|source
argument_list|)
condition|)
block|{
name|this
operator|.
name|jarHandler
operator|.
name|get
argument_list|(
name|source
argument_list|,
name|destination
argument_list|)
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|get
argument_list|(
name|source
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|jarHandler
operator|.
name|canHandle
argument_list|(
name|source
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|jarHandler
operator|.
name|getResource
argument_list|(
name|source
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getResource
argument_list|(
name|source
argument_list|)
return|;
block|}
block|}
end_class

end_unit

