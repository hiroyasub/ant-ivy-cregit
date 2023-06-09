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
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|core
operator|.
name|cache
operator|.
name|RepositoryCacheManager
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleId
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
name|latest
operator|.
name|LatestStrategy
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
name|namespace
operator|.
name|Namespace
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
name|parser
operator|.
name|ParserSettings
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
name|signer
operator|.
name|SignatureGenerator
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

begin_interface
specifier|public
interface|interface
name|ResolverSettings
extends|extends
name|ParserSettings
block|{
name|LatestStrategy
name|getLatestStrategy
parameter_list|(
name|String
name|latestStrategyName
parameter_list|)
function_decl|;
name|LatestStrategy
name|getDefaultLatestStrategy
parameter_list|()
function_decl|;
name|RepositoryCacheManager
name|getRepositoryCacheManager
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
name|RepositoryCacheManager
name|getDefaultRepositoryCacheManager
parameter_list|()
function_decl|;
name|RepositoryCacheManager
index|[]
name|getRepositoryCacheManagers
parameter_list|()
function_decl|;
name|Namespace
name|getNamespace
parameter_list|(
name|String
name|namespaceName
parameter_list|)
function_decl|;
name|Namespace
name|getSystemNamespace
parameter_list|()
function_decl|;
name|String
name|getVariable
parameter_list|(
name|String
name|string
parameter_list|)
function_decl|;
name|void
name|configureRepositories
parameter_list|(
name|boolean
name|b
parameter_list|)
function_decl|;
name|VersionMatcher
name|getVersionMatcher
parameter_list|()
function_decl|;
name|String
name|getResolveMode
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
function_decl|;
name|void
name|filterIgnore
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
function_decl|;
name|SignatureGenerator
name|getSignatureGenerator
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

