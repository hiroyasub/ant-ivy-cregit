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
name|plugins
operator|.
name|repository
operator|.
name|jar
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
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

begin_class
specifier|public
class|class
name|JarResource
implements|implements
name|Resource
block|{
specifier|private
specifier|final
name|JarFile
name|jarFile
decl_stmt|;
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
specifier|private
name|ZipEntry
name|entry
decl_stmt|;
specifier|public
name|JarResource
parameter_list|(
name|JarFile
name|jarFile
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|jarFile
operator|=
name|jarFile
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|entry
operator|=
name|jarFile
operator|.
name|getEntry
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getTime
argument_list|()
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|entry
operator|.
name|getSize
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|entry
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
comment|// not local as it is not a directly accessible file
return|return
literal|false
return|;
block|}
specifier|public
name|Resource
name|clone
parameter_list|(
name|String
name|cloneName
parameter_list|)
block|{
return|return
operator|new
name|JarResource
argument_list|(
name|jarFile
argument_list|,
name|cloneName
argument_list|)
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|jarFile
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|jarFile
operator|.
name|getName
argument_list|()
operator|+
literal|"!"
operator|+
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

