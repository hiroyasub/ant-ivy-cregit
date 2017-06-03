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

begin_comment
comment|/**  * DependencyDescriptorMediator used to override some dependency descriptors values, such as the  * branch or version of the dependency.  */
end_comment

begin_class
specifier|public
class|class
name|OverrideDependencyDescriptorMediator
implements|implements
name|DependencyDescriptorMediator
block|{
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|branch
decl_stmt|;
comment|/**      * Constructs a new instance.      *      * @param branch      *            the branch to give to mediated dependency descriptors,<code>null</code> to keep      *            the original branch.      * @param version      *            the version to give to mediated dependency descriptors,<code>null</code> to keep      *            the original one.      */
specifier|public
name|OverrideDependencyDescriptorMediator
parameter_list|(
name|String
name|branch
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
comment|/**      * Returns the version this mediator will give to mediated descriptors, or<code>null</code> if      * this mediator does not override version.      *      * @return the version this mediator will give to mediated descriptors.      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * Returns the branch this mediator will give to mediated descriptors, or<code>null</code> if      * this mediator does not override branch.      *      * @return the branch this mediator will give to mediated descriptors.      */
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
specifier|public
name|DependencyDescriptor
name|mediate
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|version
operator|==
literal|null
operator|||
name|version
operator|.
name|equals
argument_list|(
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
operator|)
operator|&&
operator|(
name|branch
operator|==
literal|null
operator|||
name|branch
operator|.
name|equals
argument_list|(
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|dd
return|;
block|}
name|String
name|version
init|=
name|this
operator|.
name|version
operator|==
literal|null
condition|?
name|mrid
operator|.
name|getRevision
argument_list|()
else|:
name|this
operator|.
name|version
decl_stmt|;
name|String
name|branch
init|=
name|this
operator|.
name|branch
operator|==
literal|null
condition|?
name|mrid
operator|.
name|getBranch
argument_list|()
else|:
name|this
operator|.
name|branch
decl_stmt|;
comment|// if this is a noop, do not construct any new object
if|if
condition|(
name|version
operator|.
name|equals
argument_list|(
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
operator|&&
name|branch
operator|.
name|equals
argument_list|(
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|getBranch
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|dd
return|;
block|}
return|return
name|dd
operator|.
name|clone
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|branch
argument_list|,
name|version
argument_list|,
name|mrid
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

