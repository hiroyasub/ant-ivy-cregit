begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|tools
operator|.
name|analyser
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|DefaultArtifact
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
name|util
operator|.
name|IvyPatternHelper
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
name|util
operator|.
name|Message
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
name|xml
operator|.
name|XmlModuleDescriptorWriter
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryAnalyser
block|{
specifier|public
name|void
name|analyse
parameter_list|(
name|String
name|pattern
parameter_list|,
name|DependencyAnalyser
name|depAnalyser
parameter_list|)
block|{
name|JarModuleFinder
name|finder
init|=
operator|new
name|JarModuleFinder
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|ModuleDescriptor
index|[]
name|mds
init|=
name|depAnalyser
operator|.
name|analyze
argument_list|(
name|finder
operator|.
name|findJarModules
argument_list|()
argument_list|)
decl_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"found "
operator|+
name|mds
operator|.
name|length
operator|+
literal|" modules"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|mds
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|ivyFile
init|=
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|DefaultArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|mds
index|[
name|i
index|]
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|mds
index|[
name|i
index|]
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"generating "
operator|+
name|ivyFile
argument_list|)
expr_stmt|;
name|XmlModuleDescriptorWriter
operator|.
name|write
argument_list|(
name|mds
index|[
name|i
index|]
argument_list|,
name|ivyFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"usage: ivyanalyser path/to/jarjar.jar absolute-ivy-repository-pattern"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|jarjarLocation
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|pattern
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
name|JarJarDependencyAnalyser
name|a
init|=
operator|new
name|JarJarDependencyAnalyser
argument_list|(
operator|new
name|File
argument_list|(
name|jarjarLocation
argument_list|)
argument_list|)
decl_stmt|;
operator|new
name|RepositoryAnalyser
argument_list|()
operator|.
name|analyse
argument_list|(
name|pattern
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

