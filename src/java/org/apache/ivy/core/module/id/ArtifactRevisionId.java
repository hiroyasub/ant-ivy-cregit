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
name|extendable
operator|.
name|UnmodifiableExtendableItem
import|;
end_import

begin_comment
comment|/**  * Identifies an artifact in a particular module revision  *   * @see<a href="package-summary.html">org.apache.ivy.core.module.id</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactRevisionId
extends|extends
name|UnmodifiableExtendableItem
block|{
specifier|public
specifier|static
name|ArtifactRevisionId
name|newInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
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
return|return
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ArtifactRevisionId
name|newInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
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
name|Map
name|extraAttributes
parameter_list|)
block|{
return|return
operator|new
name|ArtifactRevisionId
argument_list|(
operator|new
name|ArtifactId
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
argument_list|,
name|mrid
argument_list|,
name|extraAttributes
argument_list|)
return|;
block|}
specifier|private
name|ArtifactId
name|artifactId
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|mrid
decl_stmt|;
specifier|public
name|ArtifactRevisionId
parameter_list|(
name|ArtifactId
name|artifactId
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|this
argument_list|(
name|artifactId
argument_list|,
name|mrid
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArtifactRevisionId
parameter_list|(
name|ArtifactId
name|artfId
parameter_list|,
name|ModuleRevisionId
name|mdlRevId
parameter_list|,
name|Map
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
name|artifactId
operator|=
name|artfId
expr_stmt|;
name|mrid
operator|=
name|mdlRevId
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|getModuleRevisionId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|,
name|getModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
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
name|ArtifactRevisionId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactRevisionId
name|arid
init|=
operator|(
name|ArtifactRevisionId
operator|)
name|obj
decl_stmt|;
return|return
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|arid
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|getModuleRevisionId
argument_list|()
operator|.
name|equals
argument_list|(
name|arid
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
operator|&&
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|equals
argument_list|(
name|arid
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
comment|// WARN: uniqueness needs to be relatively strong here
comment|// CheckStyle:MagicNumber| OFF
name|int
name|hash
init|=
literal|17
decl_stmt|;
name|hash
operator|+=
name|getArtifactId
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
name|hash
operator|+=
name|getModuleRevisionId
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
expr_stmt|;
name|hash
operator|+=
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
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
name|getModuleRevisionId
argument_list|()
operator|+
literal|"!"
operator|+
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
comment|/**      * @return Returns the artifactId.      */
specifier|public
name|ArtifactId
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|mrid
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|artifactId
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|artifactId
operator|.
name|getType
argument_list|()
return|;
block|}
specifier|public
name|String
name|getExt
parameter_list|()
block|{
return|return
name|artifactId
operator|.
name|getExt
argument_list|()
return|;
block|}
comment|/**      * @return Returns the revision.      */
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|mrid
operator|.
name|getRevision
argument_list|()
return|;
block|}
block|}
end_class

end_unit

