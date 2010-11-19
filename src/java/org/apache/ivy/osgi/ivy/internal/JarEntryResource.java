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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
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
name|FileResource
import|;
end_import

begin_comment
comment|/**  * A resource decorator that handles extracting jar file entries using the bang(!) notation to  * separate the internal entry name.  */
end_comment

begin_class
specifier|public
class|class
name|JarEntryResource
implements|implements
name|Resource
block|{
specifier|private
specifier|final
name|String
name|entryName
decl_stmt|;
specifier|private
specifier|final
name|Resource
name|resource
decl_stmt|;
specifier|public
name|JarEntryResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|tokens
init|=
name|name
operator|.
name|split
argument_list|(
literal|"[!]"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|tokens
index|[
literal|0
index|]
decl_stmt|;
name|resource
operator|=
operator|new
name|FileResource
argument_list|(
operator|new
name|FileRepository
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|entryName
operator|=
name|tokens
index|[
literal|1
index|]
expr_stmt|;
block|}
specifier|public
name|JarEntryResource
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|String
name|entryName
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|this
operator|.
name|entryName
operator|=
name|entryName
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"resource:"
operator|+
name|resource
operator|+
literal|", jarEntry="
operator|+
name|entryName
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
name|resource
operator|.
name|clone
argument_list|(
name|cloneName
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
name|resource
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
return|return
name|resource
operator|.
name|getContentLength
argument_list|()
return|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|resource
operator|.
name|getLastModified
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|resource
operator|.
name|getName
argument_list|()
operator|+
literal|"!"
operator|+
name|entryName
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|resource
operator|.
name|isLocal
argument_list|()
return|;
block|}
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|ZipInputStream
name|zis
init|=
operator|new
name|ZipInputStream
argument_list|(
name|resource
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|zis
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|entryName
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Jar entry: "
operator|+
name|entryName
operator|+
literal|", not in resource:"
operator|+
name|resource
argument_list|)
throw|;
block|}
return|return
name|zis
return|;
block|}
block|}
end_class

end_unit
