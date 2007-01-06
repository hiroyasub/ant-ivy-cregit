begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|extendable
operator|.
name|ExtendableItem
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|Artifact
extends|extends
name|ExtendableItem
block|{
comment|/**      * Returns the resolved module revision id for this artifact      * @return      */
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * Returns the resolved publication date for this artifact      * @return the resolved publication date      */
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
name|String
name|getType
parameter_list|()
function_decl|;
name|String
name|getExt
parameter_list|()
function_decl|;
comment|/**      * Returns the url at which this artifact can be found independently of ivy configuration.      * This can be null (and is usually for standard artifacts)      * @return url at which this artifact can be found independently of ivy configuration      */
name|URL
name|getUrl
parameter_list|()
function_decl|;
name|String
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * @return the id of the artifact      */
name|ArtifactRevisionId
name|getId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

