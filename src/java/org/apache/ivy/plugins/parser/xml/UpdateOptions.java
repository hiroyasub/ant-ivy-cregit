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
name|plugins
operator|.
name|parser
operator|.
name|xml
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
name|Date
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
name|plugins
operator|.
name|namespace
operator|.
name|Namespace
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
name|parser
operator|.
name|ParserSettings
import|;
end_import

begin_class
specifier|public
class|class
name|UpdateOptions
block|{
comment|/**      * Settings to use for update, may be<code>null</code>.      */
specifier|private
name|ParserSettings
name|settings
init|=
literal|null
decl_stmt|;
comment|/**      * Namespace in which the module to update is, may be<code>null</code>.      */
specifier|private
name|Namespace
name|namespace
init|=
literal|null
decl_stmt|;
comment|/**      * Map from ModuleId of dependencies to new revision (as String)      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedRevisions
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
comment|/**      * Map from ModuleId of dependencies to new branch (as String)      */
specifier|private
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedBranches
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
comment|/**      * the new status,<code>null</code> to keep the old one      */
specifier|private
name|String
name|status
init|=
literal|null
decl_stmt|;
comment|/**      * the new revision,<code>null</code> to keep the old one      */
specifier|private
name|String
name|revision
init|=
literal|null
decl_stmt|;
comment|/**      * the new publication date,<code>null</code> to keep the old one      */
specifier|private
name|Date
name|pubdate
init|=
literal|null
decl_stmt|;
comment|/**      * Should included information be replaced      */
specifier|private
name|boolean
name|replaceInclude
init|=
literal|true
decl_stmt|;
comment|/**      * Should parent descriptor be merged inline      */
specifier|private
name|boolean
name|merge
init|=
literal|true
decl_stmt|;
specifier|private
name|ModuleDescriptor
name|mergedDescriptor
init|=
literal|null
decl_stmt|;
comment|/**      * Configurations to exclude during update, or<code>null</code> to keep all confs.      */
specifier|private
name|String
index|[]
name|confsToExclude
init|=
literal|null
decl_stmt|;
comment|/**      * True to set branch information on dependencies to default branch when omitted, false to keep      * it as is.      */
specifier|private
name|boolean
name|updateBranch
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|branch
decl_stmt|;
comment|/**      * True to indicate that the revConstraint attribute should be generated if applicable, false to      * never generate the revConstraint attribute.      */
specifier|private
name|boolean
name|generateRevConstraint
init|=
literal|true
decl_stmt|;
specifier|public
name|ParserSettings
name|getSettings
parameter_list|()
block|{
return|return
name|settings
return|;
block|}
specifier|public
name|UpdateOptions
name|setSettings
parameter_list|(
name|ParserSettings
name|settings
parameter_list|)
block|{
name|this
operator|.
name|settings
operator|=
name|settings
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|UpdateOptions
name|setNamespace
parameter_list|(
name|Namespace
name|ns
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|ns
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|getResolvedRevisions
parameter_list|()
block|{
return|return
name|resolvedRevisions
return|;
block|}
specifier|public
name|UpdateOptions
name|setResolvedRevisions
parameter_list|(
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedRevisions
parameter_list|)
block|{
name|this
operator|.
name|resolvedRevisions
operator|=
name|resolvedRevisions
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|UpdateOptions
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
specifier|public
name|UpdateOptions
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Date
name|getPubdate
parameter_list|()
block|{
return|return
name|pubdate
return|;
block|}
specifier|public
name|UpdateOptions
name|setPubdate
parameter_list|(
name|Date
name|pubdate
parameter_list|)
block|{
name|this
operator|.
name|pubdate
operator|=
name|pubdate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isReplaceInclude
parameter_list|()
block|{
return|return
name|replaceInclude
return|;
block|}
specifier|public
name|UpdateOptions
name|setReplaceInclude
parameter_list|(
name|boolean
name|replaceInclude
parameter_list|)
block|{
name|this
operator|.
name|replaceInclude
operator|=
name|replaceInclude
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isMerge
parameter_list|()
block|{
comment|// only return true if merge is set to true and if there is something to merge!
return|return
name|merge
operator|&&
operator|(
name|mergedDescriptor
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|mergedDescriptor
operator|.
name|getInheritedDescriptors
argument_list|()
operator|.
name|length
operator|>
literal|0
operator|)
return|;
block|}
specifier|public
name|UpdateOptions
name|setMerge
parameter_list|(
name|boolean
name|merge
parameter_list|)
block|{
name|this
operator|.
name|merge
operator|=
name|merge
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getMergedDescriptor
parameter_list|()
block|{
return|return
name|mergedDescriptor
return|;
block|}
specifier|public
name|UpdateOptions
name|setMergedDescriptor
parameter_list|(
name|ModuleDescriptor
name|mergedDescriptor
parameter_list|)
block|{
name|this
operator|.
name|mergedDescriptor
operator|=
name|mergedDescriptor
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
index|[]
name|getConfsToExclude
parameter_list|()
block|{
return|return
name|confsToExclude
return|;
block|}
specifier|public
name|UpdateOptions
name|setConfsToExclude
parameter_list|(
name|String
index|[]
name|confsToExclude
parameter_list|)
block|{
name|this
operator|.
name|confsToExclude
operator|=
name|confsToExclude
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUpdateBranch
parameter_list|()
block|{
return|return
name|updateBranch
return|;
block|}
specifier|public
name|UpdateOptions
name|setUpdateBranch
parameter_list|(
name|boolean
name|updateBranch
parameter_list|)
block|{
name|this
operator|.
name|updateBranch
operator|=
name|updateBranch
expr_stmt|;
return|return
name|this
return|;
block|}
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
name|UpdateOptions
name|setBranch
parameter_list|(
name|String
name|pubBranch
parameter_list|)
block|{
name|this
operator|.
name|branch
operator|=
name|pubBranch
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isGenerateRevConstraint
parameter_list|()
block|{
return|return
name|generateRevConstraint
return|;
block|}
specifier|public
name|UpdateOptions
name|setGenerateRevConstraint
parameter_list|(
name|boolean
name|generateRevConstraint
parameter_list|)
block|{
name|this
operator|.
name|generateRevConstraint
operator|=
name|generateRevConstraint
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|getResolvedBranches
parameter_list|()
block|{
return|return
name|resolvedBranches
return|;
block|}
specifier|public
name|UpdateOptions
name|setResolvedBranches
parameter_list|(
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|String
argument_list|>
name|resolvedBranches
parameter_list|)
block|{
name|this
operator|.
name|resolvedBranches
operator|=
name|resolvedBranches
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

