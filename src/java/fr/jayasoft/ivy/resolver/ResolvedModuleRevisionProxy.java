begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|resolver
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DependencyResolver
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleDescriptor
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ModuleRevisionId
import|;
end_import

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ResolvedModuleRevision
import|;
end_import

begin_comment
comment|/**   * the same ResolvedModuleRevision except that we say that it is another resolver  * which resolved the dependency, so that it's it that is used for artifact download  * ==> forward all except getResolver method  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ResolvedModuleRevisionProxy
implements|implements
name|ResolvedModuleRevision
block|{
specifier|private
specifier|final
name|ResolvedModuleRevision
name|_mr
decl_stmt|;
name|DependencyResolver
name|_resolver
decl_stmt|;
specifier|public
name|ResolvedModuleRevisionProxy
parameter_list|(
name|ResolvedModuleRevision
name|mr
parameter_list|,
name|DependencyResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|mr
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null module revision not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null resolver not allowed"
argument_list|)
throw|;
block|}
name|_mr
operator|=
name|mr
expr_stmt|;
name|_resolver
operator|=
name|resolver
expr_stmt|;
block|}
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|_resolver
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getId
parameter_list|()
block|{
return|return
name|_mr
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|_mr
operator|.
name|getPublicationDate
argument_list|()
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
name|_mr
operator|.
name|getDescriptor
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isDownloaded
parameter_list|()
block|{
return|return
name|_mr
operator|.
name|isDownloaded
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isSearched
parameter_list|()
block|{
return|return
name|_mr
operator|.
name|isSearched
argument_list|()
return|;
block|}
block|}
end_class

end_unit

