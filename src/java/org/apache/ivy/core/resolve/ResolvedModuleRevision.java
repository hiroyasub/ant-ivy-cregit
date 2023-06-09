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
name|resolve
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|id
operator|.
name|ModuleRevisionId
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
name|report
operator|.
name|MetadataArtifactDownloadReport
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

begin_comment
comment|/**  * Represents a module revision provisioned on the local filesystem.  */
end_comment

begin_class
specifier|public
class|class
name|ResolvedModuleRevision
block|{
specifier|private
name|DependencyResolver
name|resolver
decl_stmt|;
specifier|private
name|DependencyResolver
name|artifactResolver
decl_stmt|;
specifier|private
name|ModuleDescriptor
name|descriptor
decl_stmt|;
specifier|private
name|MetadataArtifactDownloadReport
name|report
decl_stmt|;
specifier|private
name|boolean
name|force
init|=
literal|false
decl_stmt|;
specifier|public
name|ResolvedModuleRevision
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|DependencyResolver
name|artifactResolver
parameter_list|,
name|ModuleDescriptor
name|descriptor
parameter_list|,
name|MetadataArtifactDownloadReport
name|report
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|artifactResolver
operator|=
name|artifactResolver
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|report
operator|=
name|report
expr_stmt|;
block|}
specifier|public
name|ResolvedModuleRevision
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|DependencyResolver
name|artifactResolver
parameter_list|,
name|ModuleDescriptor
name|descriptor
parameter_list|,
name|MetadataArtifactDownloadReport
name|report
parameter_list|,
name|boolean
name|force
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|artifactResolver
operator|=
name|artifactResolver
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|report
operator|=
name|report
expr_stmt|;
name|this
operator|.
name|force
operator|=
name|force
expr_stmt|;
block|}
comment|/**      * Returns the identifier of the resolved module.      *      * @return the identifier of the resolved module.      */
specifier|public
name|ModuleRevisionId
name|getId
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getResolvedModuleRevisionId
argument_list|()
return|;
block|}
comment|/**      * Returns the date of publication of the resolved module.      *      * @return the date of publication of the resolved module.      */
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getResolvedPublicationDate
argument_list|()
return|;
block|}
comment|/**      * Returns the descriptor of the resolved module.      *      * @return the descriptor of the resolved module.      */
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
comment|/**      * The resolver which resolved this ResolvedModuleRevision      *      * @return The resolver which resolved this ResolvedModuleRevision      */
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
comment|/**      * The resolver to use to download artifacts      *      * @return The resolver to use to download artifacts      */
specifier|public
name|DependencyResolver
name|getArtifactResolver
parameter_list|()
block|{
return|return
name|artifactResolver
return|;
block|}
comment|/**      * Returns a report of the resolved module metadata artifact provisioning.      *      * @return a report of the resolved module metadata artifact provisioning.      */
specifier|public
name|MetadataArtifactDownloadReport
name|getReport
parameter_list|()
block|{
return|return
name|report
return|;
block|}
comment|/**      * Returns<code>true</code> if this resolved module revision should be forced as the one being      * returned.      *<p>      * This is used as an indication for CompositeResolver, to know if they should continue to look      * for a better ResolvedModuleRevision if possible, or stop with this instance.      *</p>      *      * @return<code>true</code> if this resolved module revision should be forced as the one being      *         returned.      */
specifier|public
name|boolean
name|isForce
parameter_list|()
block|{
return|return
name|force
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|ResolvedModuleRevision
operator|&&
operator|(
operator|(
name|ResolvedModuleRevision
operator|)
name|obj
operator|)
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|getId
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
name|getId
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

