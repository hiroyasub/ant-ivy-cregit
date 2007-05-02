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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|descriptor
operator|.
name|Configuration
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
name|descriptor
operator|.
name|ModuleDescriptor
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
name|descriptor
operator|.
name|Configuration
operator|.
name|Visibility
import|;
end_import

begin_comment
comment|/**  * Class containing several utility methods for working with configurations.  *   * @author Maarten Coene  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationUtils
block|{
comment|/** 	 * Private constructor to avoid instantiation of this class. 	 */
specifier|private
name|ConfigurationUtils
parameter_list|()
block|{
block|}
comment|/** 	 * Replace all the wildcards in the given configuration array. Supported wildcards 	 * are: 	 *<ul> 	 *<li><b><tt>*</tt>:</b>all configurations</li> 	 *<li><b><tt>*(public)</tt>:</b>all public configurations</li> 	 *<li><b><tt>*(private)</tt>:</b>all private configurations</li> 	 *</ul> 	 * If the given array of configurations is<tt>null</tt>, all configurations are returned. 	 *  	 * @param confs the configurations, can contain wildcards 	 * @param md the configurations where the wildcards are replaced 	 * @return 	 */
specifier|public
specifier|static
name|String
index|[]
name|replaceWildcards
parameter_list|(
name|String
index|[]
name|confs
parameter_list|,
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|Set
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|()
decl_stmt|;
if|if
condition|(
name|confs
operator|==
literal|null
condition|)
block|{
return|return
name|md
operator|.
name|getConfigurationsNames
argument_list|()
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
literal|"*"
operator|.
name|equals
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|result
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"*(public)"
operator|.
name|equals
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|Configuration
index|[]
name|all
init|=
name|md
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|all
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|all
index|[
name|j
index|]
operator|.
name|getVisibility
argument_list|()
operator|.
name|equals
argument_list|(
name|Visibility
operator|.
name|PUBLIC
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|all
index|[
name|j
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
literal|"*(private)"
operator|.
name|equals
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
condition|)
block|{
name|Configuration
index|[]
name|all
init|=
name|md
operator|.
name|getConfigurations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|all
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|all
index|[
name|j
index|]
operator|.
name|getVisibility
argument_list|()
operator|.
name|equals
argument_list|(
name|Visibility
operator|.
name|PRIVATE
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|all
index|[
name|j
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|result
operator|.
name|add
argument_list|(
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

