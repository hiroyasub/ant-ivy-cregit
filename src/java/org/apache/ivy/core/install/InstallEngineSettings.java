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
name|install
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
name|module
operator|.
name|status
operator|.
name|StatusManager
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
name|matcher
operator|.
name|PatternMatcher
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
name|report
operator|.
name|ReportOutputter
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
name|resolver
operator|.
name|DependencyResolver
import|;
end_import

begin_interface
specifier|public
interface|interface
name|InstallEngineSettings
extends|extends
name|ParserSettings
block|{
name|DependencyResolver
name|getResolver
parameter_list|(
name|String
name|from
parameter_list|)
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getResolverNames
parameter_list|()
function_decl|;
name|ReportOutputter
index|[]
name|getReportOutputters
parameter_list|()
function_decl|;
name|void
name|setLogNotConvertedExclusionRule
parameter_list|(
name|boolean
name|log
parameter_list|)
function_decl|;
name|StatusManager
name|getStatusManager
parameter_list|()
function_decl|;
name|boolean
name|logNotConvertedExclusionRule
parameter_list|()
function_decl|;
name|PatternMatcher
name|getMatcher
parameter_list|(
name|String
name|matcherName
parameter_list|)
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getMatcherNames
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

