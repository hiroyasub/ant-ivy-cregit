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
name|report
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
name|util
operator|.
name|Arrays
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|IvyNode
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

begin_comment
comment|/**  * Represents a whole resolution report for a module  */
end_comment

begin_class
specifier|public
class|class
name|ResolveReport
block|{
specifier|private
name|ModuleDescriptor
name|_md
decl_stmt|;
specifier|private
name|Map
name|_confReports
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|ResolveReport
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|_md
operator|=
name|md
expr_stmt|;
block|}
specifier|public
name|void
name|addReport
parameter_list|(
name|String
name|conf
parameter_list|,
name|ConfigurationResolveReport
name|report
parameter_list|)
block|{
name|_confReports
operator|.
name|put
argument_list|(
name|conf
argument_list|,
name|report
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConfigurationResolveReport
name|getConfigurationReport
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
return|return
operator|(
name|ConfigurationResolveReport
operator|)
name|_confReports
operator|.
name|get
argument_list|(
name|conf
argument_list|)
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
name|_confReports
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_confReports
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasError
parameter_list|()
block|{
name|boolean
name|hasError
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|hasError
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|hasError
operator||=
name|report
operator|.
name|hasError
argument_list|()
expr_stmt|;
block|}
return|return
name|hasError
return|;
block|}
specifier|public
name|void
name|output
parameter_list|(
name|ReportOutputter
index|[]
name|outputters
parameter_list|,
name|File
name|cache
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|outputters
index|[
name|i
index|]
operator|.
name|output
argument_list|(
name|this
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ModuleDescriptor
name|getModuleDescriptor
parameter_list|()
block|{
return|return
name|_md
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getEvictedNodes
parameter_list|()
block|{
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getEvictedNodes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|IvyNode
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|all
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|IvyNode
index|[]
name|getUnresolvedDependencies
parameter_list|()
block|{
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getUnresolvedDependencies
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|IvyNode
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|IvyNode
index|[
name|all
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|ArtifactDownloadReport
index|[]
name|getFailedArtifactsReports
parameter_list|()
block|{
name|Collection
name|all
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|report
operator|.
name|getFailedArtifactsReports
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ArtifactDownloadReport
index|[]
operator|)
name|all
operator|.
name|toArray
argument_list|(
operator|new
name|ArtifactDownloadReport
index|[
name|all
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasChanged
parameter_list|()
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_confReports
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConfigurationResolveReport
name|report
init|=
operator|(
name|ConfigurationResolveReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|report
operator|.
name|hasChanged
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

