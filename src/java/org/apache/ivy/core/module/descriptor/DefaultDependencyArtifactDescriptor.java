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
name|ArrayList
import|;
end_import

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
name|IvyPatternHelper
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
name|util
operator|.
name|Checks
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
name|util
operator|.
name|extendable
operator|.
name|UnmodifiableExtendableItem
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultDependencyArtifactDescriptor
extends|extends
name|UnmodifiableExtendableItem
implements|implements
name|DependencyArtifactDescriptor
implements|,
name|ConfigurationAware
block|{
specifier|private
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|confs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|URL
name|url
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|ext
decl_stmt|;
specifier|private
name|DependencyDescriptor
name|dd
decl_stmt|;
comment|/**      * @param dd DependencyDescriptor      * @param name ditto      * @param type ditto      * @param ext ditto      * @param url ditto      * @param extraAttributes ditto      */
specifier|public
name|DefaultDependencyArtifactDescriptor
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|URL
name|url
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
literal|null
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|dd
argument_list|,
literal|"dd"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|type
argument_list|,
literal|"type"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|ext
argument_list|,
literal|"ext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|dd
operator|=
name|dd
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|ext
operator|=
name|ext
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|initStandardAttributes
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initStandardAttributes
parameter_list|()
block|{
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|ARTIFACT_KEY
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|TYPE_KEY
argument_list|,
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|EXT_KEY
argument_list|,
name|getExt
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
literal|"url"
argument_list|,
name|url
operator|!=
literal|null
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|url
argument_list|)
else|:
literal|""
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
name|DependencyArtifactDescriptor
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DependencyArtifactDescriptor
name|dad
init|=
operator|(
name|DependencyArtifactDescriptor
operator|)
name|obj
decl_stmt|;
return|return
name|getAttributes
argument_list|()
operator|.
name|equals
argument_list|(
name|dad
operator|.
name|getAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getAttributes
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * Add a configuration for this artifact.      *      * @param conf ditto      */
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|confs
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DependencyDescriptor
name|getDependencyDescriptor
parameter_list|()
block|{
return|return
name|dd
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|ext
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
name|confs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|confs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DA:"
operator|+
name|name
operator|+
literal|"."
operator|+
name|ext
operator|+
literal|"("
operator|+
name|type
operator|+
literal|") "
operator|+
literal|"("
operator|+
name|confs
operator|+
literal|")"
operator|+
operator|(
name|url
operator|==
literal|null
condition|?
literal|""
else|:
name|url
operator|.
name|toString
argument_list|()
operator|)
return|;
block|}
block|}
end_class

end_unit

