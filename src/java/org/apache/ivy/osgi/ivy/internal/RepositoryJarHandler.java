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
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryJarHandler
block|{
specifier|public
name|boolean
name|canHandle
parameter_list|(
name|String
name|source
parameter_list|)
block|{
return|return
name|source
operator|.
name|toUpperCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|".JAR!"
argument_list|)
return|;
block|}
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
specifier|final
name|InputStream
name|inputStream
init|=
operator|new
name|JarEntryResource
argument_list|(
name|source
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|FileUtil
operator|.
name|copy
argument_list|(
name|inputStream
argument_list|,
name|destination
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
return|return
operator|new
name|JarEntryResource
argument_list|(
name|source
argument_list|)
return|;
block|}
block|}
end_class

end_unit

