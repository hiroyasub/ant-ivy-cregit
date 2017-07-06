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
operator|.
name|module
operator|.
name|descriptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ArtifactId
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

begin_class
specifier|public
class|class
name|DefaultIncludeRule
extends|extends
name|AbstractIncludeExcludeRule
implements|implements
name|IncludeRule
block|{
specifier|public
name|DefaultIncludeRule
parameter_list|(
name|ArtifactId
name|aid
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
name|super
argument_list|(
name|aid
argument_list|,
name|matcher
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"I:"
operator|+
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

