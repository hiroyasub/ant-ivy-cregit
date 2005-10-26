begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
package|;
end_package

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|ModuleRevisionId
block|{
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
return|return
operator|new
name|ModuleRevisionId
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|revision
argument_list|)
return|;
block|}
specifier|private
name|ModuleId
name|_moduleId
decl_stmt|;
specifier|private
name|String
name|_revision
decl_stmt|;
specifier|public
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|_moduleId
operator|=
name|moduleId
expr_stmt|;
name|_revision
operator|=
name|revision
expr_stmt|;
block|}
specifier|public
name|ModuleId
name|getModuleId
parameter_list|()
block|{
return|return
name|_moduleId
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_revision
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
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|ModuleRevisionId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ModuleRevisionId
name|other
init|=
operator|(
name|ModuleRevisionId
operator|)
name|obj
decl_stmt|;
return|return
operator|(
name|other
operator|.
name|getRevision
argument_list|()
operator|==
literal|null
condition|?
name|getRevision
argument_list|()
operator|==
literal|null
else|:
name|other
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|getRevision
argument_list|()
argument_list|)
operator|)
operator|&&
name|other
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|getModuleId
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
literal|31
decl_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
operator|(
name|getRevision
argument_list|()
operator|==
literal|null
condition|?
literal|0
else|:
name|getRevision
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|getModuleId
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
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
literal|"[ "
operator|+
name|_moduleId
operator|.
name|getOrganisation
argument_list|()
operator|+
literal|" | "
operator|+
name|_moduleId
operator|.
name|getName
argument_list|()
operator|+
literal|" | "
operator|+
name|_revision
operator|+
literal|" ]"
return|;
block|}
comment|/**      * Returns true if the given revision can be considered as a revision of this module revision id.      * This is the case if the revision is equal to the current revision, or if the      * current revision is a 'latest.' one, or if it is a xx+ one matching the given one.      * @param revision      * @return true if the given revision can be considered as a revision of this module revision id.      */
specifier|public
name|boolean
name|acceptRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
return|return
name|acceptRevision
argument_list|(
name|_revision
argument_list|,
name|revision
argument_list|)
return|;
block|}
comment|/**      * @return true if the revision is an exact one, i.e. not a 'latest.' nor a xx+ one.      */
specifier|public
name|boolean
name|isExactRevision
parameter_list|()
block|{
return|return
name|isExactRevision
argument_list|(
name|_revision
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|acceptRevision
parameter_list|(
name|String
name|askedRevision
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
if|if
condition|(
name|askedRevision
operator|.
name|equals
argument_list|(
name|revision
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|askedRevision
operator|.
name|startsWith
argument_list|(
literal|"latest."
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|askedRevision
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
operator|&&
name|revision
operator|.
name|startsWith
argument_list|(
name|askedRevision
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|askedRevision
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @return true if the revision is an exact one, i.e. not a 'latest.' nor a xx+ one.      */
specifier|public
specifier|static
name|boolean
name|isExactRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
return|return
operator|!
name|revision
operator|.
name|startsWith
argument_list|(
literal|"latest."
argument_list|)
operator|&&
operator|!
name|revision
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

