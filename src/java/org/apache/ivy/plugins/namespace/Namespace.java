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
name|plugins
operator|.
name|namespace
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ModuleRevisionId
import|;
end_import

begin_class
specifier|public
class|class
name|Namespace
block|{
specifier|public
specifier|static
specifier|final
name|Namespace
name|SYSTEM_NAMESPACE
decl_stmt|;
static|static
block|{
name|SYSTEM_NAMESPACE
operator|=
operator|new
name|Namespace
argument_list|()
expr_stmt|;
block|}
specifier|private
name|List
name|_rules
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|boolean
name|_chainRules
init|=
literal|false
decl_stmt|;
specifier|private
name|NamespaceTransformer
name|_fromSystemTransformer
init|=
operator|new
name|NamespaceTransformer
argument_list|()
block|{
specifier|public
name|ModuleRevisionId
name|transform
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_rules
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|NamespaceRule
name|rule
init|=
operator|(
name|NamespaceRule
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|nmrid
init|=
name|rule
operator|.
name|getFromSystem
argument_list|()
operator|.
name|transform
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|_chainRules
condition|)
block|{
name|mrid
operator|=
name|nmrid
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|nmrid
operator|.
name|equals
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
return|return
name|nmrid
return|;
block|}
block|}
return|return
name|mrid
return|;
block|}
specifier|public
name|boolean
name|isIdentity
parameter_list|()
block|{
return|return
name|_rules
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
decl_stmt|;
specifier|private
name|NamespaceTransformer
name|_toSystemTransformer
init|=
operator|new
name|NamespaceTransformer
argument_list|()
block|{
specifier|public
name|ModuleRevisionId
name|transform
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_rules
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|NamespaceRule
name|rule
init|=
operator|(
name|NamespaceRule
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|nmrid
init|=
name|rule
operator|.
name|getToSystem
argument_list|()
operator|.
name|transform
argument_list|(
name|mrid
argument_list|)
decl_stmt|;
if|if
condition|(
name|_chainRules
condition|)
block|{
name|mrid
operator|=
name|nmrid
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|nmrid
operator|.
name|equals
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
return|return
name|nmrid
return|;
block|}
block|}
return|return
name|mrid
return|;
block|}
specifier|public
name|boolean
name|isIdentity
parameter_list|()
block|{
return|return
name|_rules
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
decl_stmt|;
specifier|public
name|void
name|addRule
parameter_list|(
name|NamespaceRule
name|rule
parameter_list|)
block|{
name|_rules
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|NamespaceTransformer
name|getFromSystemTransformer
parameter_list|()
block|{
return|return
name|_fromSystemTransformer
return|;
block|}
specifier|public
name|NamespaceTransformer
name|getToSystemTransformer
parameter_list|()
block|{
return|return
name|_toSystemTransformer
return|;
block|}
specifier|public
name|boolean
name|isChainrules
parameter_list|()
block|{
return|return
name|_chainRules
return|;
block|}
specifier|public
name|void
name|setChainrules
parameter_list|(
name|boolean
name|chainRules
parameter_list|)
block|{
name|_chainRules
operator|=
name|chainRules
expr_stmt|;
block|}
block|}
end_class

end_unit
