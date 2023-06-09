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
name|pack
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
name|util
operator|.
name|FileUtil
import|;
end_import

begin_comment
comment|/**  * Packaging which handle OSGi bundles with inner packed jar  */
end_comment

begin_class
specifier|public
class|class
name|OsgiBundlePacking
extends|extends
name|ZipPacking
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|NAMES
init|=
block|{
literal|"bundle"
block|}
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getNames
parameter_list|()
block|{
return|return
name|NAMES
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writeFile
parameter_list|(
name|InputStream
name|zip
parameter_list|,
name|File
name|f
parameter_list|)
throws|throws
name|IOException
block|{
comment|// XXX maybe we should only unpack file listed by the 'Bundle-ClassPath' MANIFEST header ?
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jar.pack.gz"
argument_list|)
condition|)
block|{
name|zip
operator|=
name|FileUtil
operator|.
name|unwrapPack200
argument_list|(
name|zip
argument_list|)
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|f
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|f
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|f
operator|.
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|writeFile
argument_list|(
name|zip
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

