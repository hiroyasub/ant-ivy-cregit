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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * @author Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifact
extends|extends
name|AbstractArtifact
block|{
specifier|public
specifier|static
name|Artifact
name|cloneWithAnotherType
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|newType
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|newType
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|)
return|;
block|}
name|Date
name|_publicationDate
decl_stmt|;
name|ArtifactRevisionId
name|_arid
decl_stmt|;
specifier|public
name|DefaultArtifact
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Date
name|publicationDate
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
if|if
condition|(
name|mrid
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null mrid not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|publicationDate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null publication date not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null name not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null type not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null ext not allowed"
argument_list|)
throw|;
block|}
name|_publicationDate
operator|=
name|publicationDate
expr_stmt|;
name|_arid
operator|=
name|ArtifactRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|_arid
operator|.
name|getModuleRevisionId
argument_list|()
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_arid
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|_publicationDate
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|_arid
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
name|_arid
operator|.
name|getExt
argument_list|()
return|;
block|}
specifier|public
name|ArtifactRevisionId
name|getId
parameter_list|()
block|{
return|return
name|_arid
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[
literal|0
index|]
return|;
block|}
block|}
end_class

end_unit

