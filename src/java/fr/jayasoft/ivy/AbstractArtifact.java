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
comment|/**  * @author Hanin  *  */
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
literal|true
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
literal|"/"
operator|+
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|getExt
argument_list|()
operator|+
literal|"["
operator|+
name|getType
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

