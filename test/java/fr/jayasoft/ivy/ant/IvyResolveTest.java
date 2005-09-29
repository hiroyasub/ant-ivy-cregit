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
name|ant
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|BuildException
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
name|ModuleRevisionId
import|;
end_import

begin_class
specifier|public
class|class
name|IvyResolveTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|_cache
decl_stmt|;
specifier|private
name|IvyResolve
name|_resolve
decl_stmt|;
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
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.conf.file"
argument_list|,
literal|"test/repositories/ivyconf.xml"
argument_list|)
expr_stmt|;
name|_resolve
operator|=
operator|new
name|IvyResolve
argument_list|()
expr_stmt|;
name|_resolve
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setCache
argument_list|(
name|_cache
argument_list|)
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
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvy
argument_list|()
operator|.
name|getResolvedIvyFileInCache
argument_list|(
name|_cache
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"jayasoft"
argument_list|,
literal|"resolve-simple"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// dependencies
name|assertTrue
argument_list|(
name|getIvy
argument_list|()
operator|.
name|getIvyFileInCache
argument_list|(
name|_cache
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"org1"
argument_list|,
literal|"mod1.2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getIvy
argument_list|()
operator|.
name|getArchiveFileInCache
argument_list|(
name|_cache
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
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDepsChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConflictingDepsChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/2/mod4.1/ivy-4.1.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.deps.changed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDouble
parameter_list|()
throws|throws
name|Exception
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-simple"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-double.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolve-double"
argument_list|,
name|getIvy
argument_list|()
operator|.
name|getVariable
argument_list|(
literal|"ivy.module"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-failure.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure didn't raised an exception with default haltonfailure setting"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
comment|// ok => should raised an exception
block|}
block|}
specifier|public
name|void
name|testHaltOnFailure
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|_resolve
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/fr/jayasoft/ivy/ant/ivy-failure.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|setHaltonfailure
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|_resolve
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failure raised an exception with haltonfailure set to false"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|_resolve
operator|.
name|getIvyInstance
argument_list|()
return|;
block|}
block|}
end_class

end_unit

