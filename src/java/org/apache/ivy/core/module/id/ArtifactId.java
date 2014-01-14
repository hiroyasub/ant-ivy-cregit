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
name|id
package|;
end_package

begin_comment
comment|/**  * Identifies an artifact in a module, without revision information  *   * @see<a href="package-summary.html">org.apache.ivy.core.module.id</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactId
block|{
specifier|private
name|ModuleId
name|mid
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
comment|/**      * @param mid      *            The ModuleId, which is the base of this artifact.      * @param name      *            The name of the artifact.      * @param type      *            The type of the artifact.      */
specifier|public
name|ArtifactId
parameter_list|(
name|ModuleId
name|mid
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
name|this
operator|.
name|mid
operator|=
name|mid
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
block|}
comment|/** {@inheritDoc} */
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
name|ArtifactId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactId
name|aid
init|=
operator|(
name|ArtifactId
operator|)
name|obj
decl_stmt|;
return|return
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|aid
operator|.
name|getModuleId
argument_list|()
argument_list|)
operator|&&
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|aid
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
name|aid
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
name|aid
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|// CheckStyle:MagicNumber| OFF
name|int
name|hash
init|=
literal|17
decl_stmt|;
name|hash
operator|+=
name|getModuleId
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
name|hash
operator|+=
name|getName
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
name|hash
operator|+=
name|getType
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
comment|// CheckStyle:MagicNumber| OFF
return|return
name|hash
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getModuleId
argument_list|()
operator|+
literal|"!"
operator|+
name|getShortDescription
argument_list|()
return|;
block|}
specifier|public
name|String
name|getShortDescription
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|getExt
argument_list|()
operator|+
operator|(
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
name|getExt
argument_list|()
argument_list|)
condition|?
literal|""
else|:
literal|"("
operator|+
name|getType
argument_list|()
operator|+
literal|")"
operator|)
return|;
block|}
comment|/**      * @return Returns the module id.      */
specifier|public
name|ModuleId
name|getModuleId
parameter_list|()
block|{
return|return
name|mid
return|;
block|}
comment|/**      * @return Returns the name.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @return Returns the type.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * @return Returns the ext.      */
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|ext
return|;
block|}
block|}
end_class

end_unit

