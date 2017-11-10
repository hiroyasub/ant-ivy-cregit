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
operator|.
name|extendable
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|UnmodifiableExtendableItem
implements|implements
name|ExtendableItem
block|{
specifier|private
specifier|final
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|unmodifiableAttributesView
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|attributes
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|unmodifiableExtraAttributesView
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extraAttributes
argument_list|)
decl_stmt|;
comment|/*      * this is the only place where extra attributes are stored in qualified form. In all other maps      * they are stored unqualified.      */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|qualifiedExtraAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|unmodifiableQualifiedExtraAttributesView
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|qualifiedExtraAttributes
argument_list|)
decl_stmt|;
specifier|public
name|UnmodifiableExtendableItem
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|stdAttributes
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
if|if
condition|(
name|stdAttributes
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|attributes
operator|.
name|putAll
argument_list|(
name|stdAttributes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extraAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAtt
range|:
name|extraAttributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|setExtraAttribute
argument_list|(
name|extraAtt
operator|.
name|getKey
argument_list|()
argument_list|,
name|extraAtt
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|)
block|{
name|String
name|v
init|=
name|qualifiedExtraAttributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
name|extraAttributes
operator|.
name|get
argument_list|(
name|attName
argument_list|)
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
specifier|protected
name|void
name|setExtraAttribute
parameter_list|(
name|String
name|attName
parameter_list|,
name|String
name|attValue
parameter_list|)
block|{
name|qualifiedExtraAttributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
argument_list|)
expr_stmt|;
comment|// unqualify att name if required
name|int
name|index
init|=
name|attName
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|attName
operator|=
name|attName
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|extraAttributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setStandardAttribute
parameter_list|(
name|String
name|attName
parameter_list|,
name|String
name|attValue
parameter_list|)
block|{
name|attributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
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
name|unmodifiableAttributesView
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraAttributes
parameter_list|()
block|{
return|return
name|unmodifiableExtraAttributesView
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQualifiedExtraAttributes
parameter_list|()
block|{
return|return
name|unmodifiableQualifiedExtraAttributesView
return|;
block|}
block|}
end_class

end_unit

