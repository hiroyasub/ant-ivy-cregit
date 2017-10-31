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
name|util
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

begin_interface
specifier|public
interface|interface
name|FileResolver
block|{
name|FileResolver
name|DEFAULT
init|=
operator|new
name|FileResolver
argument_list|()
block|{
specifier|public
name|File
name|resolveFile
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/**      * Return the canonical form of a path, or raise an {@link IllegalArgumentException} if the path      * is not valid for this {@link FileResolver}.      *<p>      *      * @param path      *            The path of the file to resolve. Must not be<code>null</code>.      * @param filename      *            The name of the file to resolve. This is used only for exception message if any.      *            Must not be<code>null</code>.      *      * @return the resolved File.      *      */
name|File
name|resolveFile
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|filename
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

