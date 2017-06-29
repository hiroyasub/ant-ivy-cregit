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
name|osgi
operator|.
name|core
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_class
specifier|public
class|class
name|ManifestHeaderElement
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|directives
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getValues
parameter_list|()
block|{
return|return
name|values
return|;
block|}
specifier|public
name|void
name|addValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|attributes
return|;
block|}
specifier|public
name|void
name|addAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDirectives
parameter_list|()
block|{
return|return
name|directives
return|;
block|}
specifier|public
name|void
name|addDirective
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|directives
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|ManifestHeaderElement
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ManifestHeaderElement
name|other
init|=
operator|(
name|ManifestHeaderElement
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|other
operator|.
name|values
operator|.
name|size
argument_list|()
operator|!=
name|values
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
operator|!
name|other
operator|.
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|other
operator|.
name|directives
operator|.
name|size
argument_list|()
operator|!=
name|directives
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|directive
range|:
name|directives
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|directive
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|directives
operator|.
name|get
argument_list|(
name|directive
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|other
operator|.
name|attributes
operator|.
name|size
argument_list|()
operator|!=
name|attributes
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attribute
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|attribute
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|other
operator|.
name|attributes
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|string
init|=
literal|""
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|itValues
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itValues
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
name|itValues
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|itValues
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|directive
range|:
name|directives
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
name|directive
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
literal|":="
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
name|directive
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attribute
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
literal|";"
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
name|attribute
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|string
operator|=
name|string
operator|.
name|concat
argument_list|(
name|attribute
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|string
return|;
block|}
block|}
end_class

end_unit

