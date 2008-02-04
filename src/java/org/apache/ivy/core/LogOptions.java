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

begin_class
specifier|public
class|class
name|LogOptions
block|{
comment|/**      * Defaults log settings. Output all usual messages during the resolve process.      */
specifier|public
specifier|static
specifier|final
name|String
name|LOG_DEFAULT
init|=
literal|"default"
decl_stmt|;
comment|/**      * This log setting disable all usual messages but download ones.      */
specifier|public
specifier|static
specifier|final
name|String
name|LOG_DOWNLOAD_ONLY
init|=
literal|"download-only"
decl_stmt|;
comment|/**      * This log setting disable all usual messages during the resolve process.       */
specifier|public
specifier|static
specifier|final
name|String
name|LOG_QUIET
init|=
literal|"quiet"
decl_stmt|;
comment|/**      * The log settings to use.      * One of {@link #LOG_DEFAULT}, {@link #LOG_DOWNLOAD_ONLY}, {@link #LOG_QUIET}      */
specifier|private
name|String
name|log
init|=
name|LOG_DEFAULT
decl_stmt|;
specifier|public
name|LogOptions
parameter_list|()
block|{
block|}
specifier|public
name|LogOptions
parameter_list|(
name|LogOptions
name|options
parameter_list|)
block|{
name|log
operator|=
name|options
operator|.
name|log
expr_stmt|;
block|}
specifier|public
name|String
name|getLog
parameter_list|()
block|{
return|return
name|log
return|;
block|}
specifier|public
name|LogOptions
name|setLog
parameter_list|(
name|String
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit
