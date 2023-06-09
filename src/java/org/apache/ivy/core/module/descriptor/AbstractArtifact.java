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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractArtifact
implements|implements
name|Artifact
block|{
specifier|public
name|AbstractArtifact
parameter_list|()
block|{
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
name|Artifact
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Artifact
name|art
init|=
operator|(
name|Artifact
operator|)
name|obj
decl_stmt|;
return|return
name|getModuleRevisionId
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
operator|&&
name|getPublicationDate
argument_list|()
operator|==
literal|null
condition|?
operator|(
name|art
operator|.
name|getPublicationDate
argument_list|()
operator|==
literal|null
operator|)
else|:
name|getPublicationDate
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
operator|&&
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|getExt
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getExt
argument_list|()
argument_list|)
operator|&&
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|equals
argument_list|(
name|art
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// CheckStyle:MagicNumber| OFF
name|int
name|hash
init|=
literal|33
decl_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getModuleRevisionId
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
if|if
condition|(
name|getPublicationDate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getPublicationDate
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getName
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getExt
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getType
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|17
operator|+
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
comment|// CheckStyle:MagicNumber| ON
return|return
name|hash
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|getId
argument_list|()
argument_list|)
return|;
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
name|getId
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|attName
argument_list|)
return|;
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
name|getId
argument_list|()
operator|.
name|getAttributes
argument_list|()
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
return|return
name|getId
argument_list|()
operator|.
name|getExtraAttribute
argument_list|(
name|attName
argument_list|)
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
name|getId
argument_list|()
operator|.
name|getExtraAttributes
argument_list|()
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
name|getId
argument_list|()
operator|.
name|getQualifiedExtraAttributes
argument_list|()
return|;
block|}
block|}
end_class

end_unit

