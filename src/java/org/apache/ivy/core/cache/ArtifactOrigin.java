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
name|cache
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
name|descriptor
operator|.
name|Artifact
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

begin_comment
comment|/**  * This class contains information about the origin of an artifact.  *   * @see org.apache.ivy.plugins.resolver.BasicResolver  * @see org.apache.ivy.plugins.resolver.util.ResolvedResource  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactOrigin
block|{
specifier|private
specifier|static
specifier|final
name|String
name|UNKNOWN
init|=
literal|"UNKNOWN"
decl_stmt|;
comment|/**      * ArtifactOrigin instance used when the origin is unknown.      */
specifier|public
specifier|static
specifier|final
name|ArtifactOrigin
name|unkwnown
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
operator|new
name|ArtifactOrigin
argument_list|(
name|artifact
argument_list|,
literal|false
argument_list|,
name|UNKNOWN
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|final
name|boolean
name|isUnknown
parameter_list|(
name|ArtifactOrigin
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|==
literal|null
operator|||
name|UNKNOWN
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getLocation
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
specifier|final
name|boolean
name|isUnknown
parameter_list|(
name|String
name|location
parameter_list|)
block|{
return|return
name|location
operator|==
literal|null
operator|||
name|UNKNOWN
operator|.
name|equals
argument_list|(
name|location
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
name|int
name|MAGIC_HASH_VALUE
init|=
literal|31
decl_stmt|;
specifier|private
name|boolean
name|isLocal
decl_stmt|;
specifier|private
name|String
name|location
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|Long
name|lastChecked
decl_stmt|;
specifier|private
name|boolean
name|exists
init|=
literal|true
decl_stmt|;
comment|/**      * Create a new instance      *       * @param artifact      *            the artifact pointed by this location. Must not be<code>null</code>.      * @param isLocal      *<code>boolean</code> value indicating if the resource is local (on the      *            filesystem).      * @param location      *            the location of the resource (normally a url). Must not be<code>null</code>.      */
specifier|public
name|ArtifactOrigin
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|boolean
name|isLocal
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|artifact
argument_list|,
literal|"artifact"
argument_list|)
expr_stmt|;
name|Checks
operator|.
name|checkNotNull
argument_list|(
name|location
argument_list|,
literal|"location"
argument_list|)
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
name|this
operator|.
name|isLocal
operator|=
name|isLocal
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
comment|/**      * Is this resource local to this host, i.e. is it on the file system?      *       * @return<code>boolean</code> value indicating if the resource is local.      */
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|isLocal
return|;
block|}
comment|/**      * Return the location of the resource (normally a url)      *       * @return the location of the resource      */
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
comment|/**      * Return the artifact that this location is pointing at.      *       * @return the artifact that this location is pointing at.      */
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
comment|/**      * The last time the resource was checked to be up to date. Maybe<code>null</code> if this information is      * not actually used by in some case.      *       * @return      */
specifier|public
name|Long
name|getLastChecked
parameter_list|()
block|{
return|return
name|lastChecked
return|;
block|}
specifier|public
name|void
name|setLastChecked
parameter_list|(
name|Long
name|lastChecked
parameter_list|)
block|{
name|this
operator|.
name|lastChecked
operator|=
name|lastChecked
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExists
parameter_list|()
block|{
return|return
name|exists
return|;
block|}
specifier|public
name|void
name|setExist
parameter_list|(
name|boolean
name|exists
parameter_list|)
block|{
name|this
operator|.
name|exists
operator|=
name|exists
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ArtifactOrigin { isLocal="
operator|+
name|isLocal
operator|+
literal|", location="
operator|+
name|location
operator|+
literal|", lastChecked="
operator|+
name|lastChecked
operator|+
literal|", exists="
operator|+
name|exists
operator|+
literal|"}"
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactOrigin
name|that
init|=
operator|(
name|ArtifactOrigin
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|isLocal
operator|!=
name|that
operator|.
name|isLocal
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|location
operator|.
name|equals
argument_list|(
name|that
operator|.
name|location
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|lastChecked
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|that
operator|.
name|lastChecked
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|lastChecked
operator|.
name|equals
argument_list|(
name|that
operator|.
name|lastChecked
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|exists
operator|!=
name|that
operator|.
name|exists
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
decl_stmt|;
name|result
operator|=
operator|(
name|isLocal
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
name|MAGIC_HASH_VALUE
operator|*
name|result
operator|+
name|location
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|result
operator|=
name|MAGIC_HASH_VALUE
operator|*
name|result
operator|+
operator|(
operator|(
name|lastChecked
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|lastChecked
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|MAGIC_HASH_VALUE
operator|*
name|result
operator|+
operator|(
name|exists
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

