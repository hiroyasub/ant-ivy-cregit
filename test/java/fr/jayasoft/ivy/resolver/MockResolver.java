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
operator|.
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Artifact
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
name|DependencyDescriptor
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
name|Ivy
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
name|ResolveData
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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|report
operator|.
name|DownloadReport
import|;
end_import

begin_class
specifier|public
class|class
name|MockResolver
extends|extends
name|AbstractResolver
block|{
specifier|static
name|MockResolver
name|buildMockResolver
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|findRevision
parameter_list|,
specifier|final
name|Date
name|publicationDate
parameter_list|)
block|{
specifier|final
name|MockResolver
name|r
init|=
operator|new
name|MockResolver
argument_list|()
decl_stmt|;
name|r
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|findRevision
condition|)
block|{
name|r
operator|.
name|rmr
operator|=
operator|new
name|ResolvedModuleRevision
argument_list|()
block|{
specifier|public
name|DependencyResolver
name|getResolver
parameter_list|()
block|{
return|return
name|r
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|publicationDate
return|;
block|}
specifier|public
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isDownloaded
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isSearched
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
name|List
name|askedDeps
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
decl_stmt|;
specifier|public
name|ResolvedModuleRevision
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
throws|throws
name|ParseException
block|{
name|askedDeps
operator|.
name|add
argument_list|(
name|dd
argument_list|)
expr_stmt|;
return|return
name|rmr
return|;
block|}
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
block|}
block|}
end_class

end_unit

