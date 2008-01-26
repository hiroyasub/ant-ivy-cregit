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
name|ant
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
name|Ivy
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
name|BuildException
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

begin_comment
comment|/**  * Configure Ivy with an ivysettings.xml file  *   * @deprecated Use the IvyAntSettings instead.  */
end_comment

begin_class
specifier|public
class|class
name|IvyConfigure
extends|extends
name|IvyAntSettings
block|{
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|log
argument_list|(
literal|"ivy:configure is deprecated, please use the data type ivy:settings instead"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
name|super
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Ivy
name|getIvyInstance
parameter_list|()
block|{
return|return
name|getConfiguredIvyInstance
argument_list|()
return|;
block|}
block|}
end_class

end_unit

