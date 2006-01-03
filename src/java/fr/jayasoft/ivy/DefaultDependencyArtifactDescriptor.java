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

begin_comment
comment|/**  * TODO write javadoc  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDependencyArtifactDescriptor
implements|implements
name|DependencyArtifactDescriptor
block|{
specifier|private
name|DefaultDependencyDescriptor
name|_dd
decl_stmt|;
specifier|private
name|ArtifactId
name|_id
decl_stmt|;
specifier|private
name|Collection
name|_confs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|_includes
decl_stmt|;
comment|/**      * @param dd      * @param name      * @param type      */
specifier|public
name|DefaultDependencyArtifactDescriptor
parameter_list|(
name|DefaultDependencyDescriptor
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
name|boolean
name|includes
parameter_list|)
block|{
if|if
condition|(
name|dd
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"dependency descriptor must not be null"
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
literal|"name must not be null"
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
literal|"type must not be null"
argument_list|)
throw|;
block|}
name|_dd
operator|=
name|dd
expr_stmt|;
name|_id
operator|=
operator|new
name|ArtifactId
argument_list|(
name|dd
operator|.
name|getDependencyId
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
expr_stmt|;
name|_includes
operator|=
name|includes
expr_stmt|;
block|}
specifier|public
name|DefaultDependencyArtifactDescriptor
parameter_list|(
name|DefaultDependencyDescriptor
name|dd
parameter_list|,
name|ArtifactId
name|aid
parameter_list|,
name|boolean
name|includes
parameter_list|)
block|{
if|if
condition|(
name|dd
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"dependency descriptor must not be null"
argument_list|)
throw|;
block|}
name|_dd
operator|=
name|dd
expr_stmt|;
name|_id
operator|=
name|aid
expr_stmt|;
name|_includes
operator|=
name|includes
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
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|dad
operator|.
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
comment|/**      * Add a configuration for this artifact (includes or excludes depending on this type dependency artifact descriptor).      * This method also updates the corresponding dependency descriptor      * @param conf      */
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|_confs
operator|.
name|add
argument_list|(
name|conf
argument_list|)
expr_stmt|;
if|if
condition|(
name|_includes
condition|)
block|{
name|_dd
operator|.
name|addDependencyArtifactIncludes
argument_list|(
name|conf
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|_dd
operator|.
name|addDependencyArtifactExcludes
argument_list|(
name|conf
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DependencyDescriptor
name|getDependency
parameter_list|()
block|{
return|return
name|_dd
return|;
block|}
specifier|public
name|ArtifactId
name|getId
parameter_list|()
block|{
return|return
name|_id
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_id
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
name|_id
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
name|_id
operator|.
name|getExt
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|_confs
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_confs
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

