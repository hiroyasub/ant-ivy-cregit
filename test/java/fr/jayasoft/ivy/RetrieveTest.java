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
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Delete
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
name|filter
operator|.
name|FilterHelper
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
name|ResolveReport
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|RetrieveTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
name|Ivy
name|_ivy
decl_stmt|;
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|public
name|RetrieveTest
parameter_list|()
throws|throws
name|Exception
block|{
name|_ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|_ivy
operator|.
name|configure
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/ivyconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createCache
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|_cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|_cache
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|cleanCache
argument_list|()
expr_stmt|;
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
operator|new
name|File
argument_list|(
literal|"build/test/retrieve"
argument_list|)
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|cleanCache
parameter_list|()
block|{
name|Delete
name|del
init|=
operator|new
name|Delete
argument_list|()
decl_stmt|;
name|del
operator|.
name|setProject
argument_list|(
operator|new
name|Project
argument_list|()
argument_list|)
expr_stmt|;
name|del
operator|.
name|setDir
argument_list|(
name|_cache
argument_list|)
expr_stmt|;
name|del
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testRetrieveSimple
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mod1.1 depends on mod1.2
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|,
name|_cache
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|String
name|pattern
init|=
literal|"build/test/retrieve/[module]/[conf]/[artifact]-[revision].[ext]"
decl_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|pattern
operator|=
literal|"build/test/retrieve/[module]/[conf]/[type]s/[artifact]-[revision].[ext]"
expr_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRetrieveWithSymlinks
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mod1.1 depends on mod1.2
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|,
name|_cache
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|String
name|pattern
init|=
literal|"build/test/retrieve/[module]/[conf]/[artifact]-[revision].[ext]"
decl_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
name|FilterHelper
operator|.
name|NO_FILTER
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertLink
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|pattern
operator|=
literal|"build/test/retrieve/[module]/[conf]/[type]s/[artifact]-[revision].[ext]"
expr_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|,
literal|null
argument_list|,
name|FilterHelper
operator|.
name|NO_FILTER
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertLink
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertLink
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
comment|// if the OS is known to support symlink, check that the file is a symlink,
comment|// otherwise just check the file exist.
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The file "
operator|+
name|filename
operator|+
literal|" doesn't exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|.
name|equals
argument_list|(
literal|"Linux"
argument_list|)
operator|||
name|os
operator|.
name|equals
argument_list|(
literal|"Solaris"
argument_list|)
operator|||
name|os
operator|.
name|equals
argument_list|(
literal|"FreeBSD"
argument_list|)
condition|)
block|{
comment|// these OS should support symnlink, so check that the file is actually a symlink.
comment|// this is done be checking that the canonical path is different from the absolute
comment|// path.
name|File
name|absFile
init|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|File
name|canFile
init|=
name|file
operator|.
name|getCanonicalFile
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The file "
operator|+
name|filename
operator|+
literal|" isn't a symlink"
argument_list|,
name|absFile
operator|.
name|equals
argument_list|(
name|canFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testRetrieveWithVariable
parameter_list|()
throws|throws
name|Exception
block|{
comment|// mod1.1 depends on mod1.2
name|_ivy
operator|.
name|setVariable
argument_list|(
literal|"retrieve.dir"
argument_list|,
literal|"retrieve"
argument_list|)
expr_stmt|;
name|ResolveReport
name|report
init|=
name|_ivy
operator|.
name|resolve
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org1/mod1.1/ivys/ivy-1.0.xml"
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
argument_list|,
name|_cache
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|report
argument_list|)
expr_stmt|;
name|ModuleDescriptor
name|md
init|=
name|report
operator|.
name|getModuleDescriptor
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|String
name|pattern
init|=
literal|"build/test/${retrieve.dir}/[module]/[conf]/[artifact]-[revision].[ext]"
decl_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|pattern
operator|=
name|IvyPatternHelper
operator|.
name|substituteVariable
argument_list|(
name|pattern
argument_list|,
literal|"retrieve.dir"
argument_list|,
literal|"retrieve"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|pattern
operator|=
literal|"build/test/${retrieve.dir}/[module]/[conf]/[type]s/[artifact]-[revision].[ext]"
expr_stmt|;
name|_ivy
operator|.
name|retrieve
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|md
operator|.
name|getConfigurationsNames
argument_list|()
argument_list|,
name|_cache
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|pattern
operator|=
name|IvyPatternHelper
operator|.
name|substituteVariable
argument_list|(
name|pattern
argument_list|,
literal|"retrieve.dir"
argument_list|,
literal|"retrieve"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

