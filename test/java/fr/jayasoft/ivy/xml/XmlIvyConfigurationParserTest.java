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
name|xml
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
name|List
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
name|LatestStrategy
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
name|ModuleId
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
name|latest
operator|.
name|LatestRevisionStrategy
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
name|latest
operator|.
name|LatestTimeStrategy
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
name|resolver
operator|.
name|ChainResolver
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
name|resolver
operator|.
name|FileSystemResolver
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
name|resolver
operator|.
name|MockResolver
import|;
end_import

begin_comment
comment|/**  * TODO write javadoc  */
end_comment

begin_class
specifier|public
class|class
name|XmlIvyConfigurationParserTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|XmlIvyConfigurationParser
name|parser
init|=
operator|new
name|XmlIvyConfigurationParser
argument_list|(
name|ivy
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|XmlIvyConfigurationParserTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivyconf-test.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|defaultCache
init|=
name|ivy
operator|.
name|getDefaultCache
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultCache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mycache"
argument_list|,
name|defaultCache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ivy
operator|.
name|isCheckUpToDate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ivy
operator|.
name|doValidate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[module]/ivys/ivy-[revision].xml"
argument_list|,
name|ivy
operator|.
name|getCacheIvyPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[module]/[type]s/[artifact]-[revision].[ext]"
argument_list|,
name|ivy
operator|.
name|getCacheArtifactPattern
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyResolver
name|defaultResolver
init|=
name|ivy
operator|.
name|getDefaultResolver
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|defaultResolver
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"libraries"
argument_list|,
name|defaultResolver
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|defaultResolver
operator|instanceof
name|FileSystemResolver
argument_list|)
expr_stmt|;
name|FileSystemResolver
name|fsres
init|=
operator|(
name|FileSystemResolver
operator|)
name|defaultResolver
decl_stmt|;
name|List
name|ivyPatterns
init|=
name|fsres
operator|.
name|getIvyPatterns
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivyPatterns
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ivyPatterns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lib/[organisation]/[module]/ivys/ivy-[revision].xml"
argument_list|,
name|ivyPatterns
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|LatestStrategy
name|strategy
init|=
name|fsres
operator|.
name|getLatestStrategy
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|strategy
operator|instanceof
name|LatestRevisionStrategy
argument_list|)
expr_stmt|;
name|DependencyResolver
name|internal
init|=
name|ivy
operator|.
name|getResolver
argument_list|(
literal|"internal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|internal
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|internal
operator|instanceof
name|ChainResolver
argument_list|)
expr_stmt|;
name|ChainResolver
name|chain
init|=
operator|(
name|ChainResolver
operator|)
name|internal
decl_stmt|;
name|List
name|subresolvers
init|=
name|chain
operator|.
name|getResolvers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|subresolvers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subresolvers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FileSystemResolver
name|fsInt1
init|=
operator|(
name|FileSystemResolver
operator|)
name|subresolvers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"int1"
argument_list|,
name|fsInt1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"int2"
argument_list|,
operator|(
operator|(
name|DependencyResolver
operator|)
name|subresolvers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|strategy
operator|=
name|fsInt1
operator|.
name|getLatestStrategy
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|strategy
operator|instanceof
name|LatestTimeStrategy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"libraries"
argument_list|,
name|ivy
operator|.
name|getResolver
argument_list|(
operator|new
name|ModuleId
argument_list|(
literal|"unknown"
argument_list|,
literal|"lib"
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"internal"
argument_list|,
name|ivy
operator|.
name|getResolver
argument_list|(
operator|new
name|ModuleId
argument_list|(
literal|"jayasoft"
argument_list|,
literal|"ivy"
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testTypedef
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|XmlIvyConfigurationParser
name|parser
init|=
operator|new
name|XmlIvyConfigurationParser
argument_list|(
name|ivy
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|XmlIvyConfigurationParserTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivyconf-typedef.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyResolver
name|mock
init|=
name|ivy
operator|.
name|getResolver
argument_list|(
literal|"mock3"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mock
operator|instanceof
name|MockResolver
argument_list|)
expr_stmt|;
name|DependencyResolver
name|internal
init|=
name|ivy
operator|.
name|getResolver
argument_list|(
literal|"internal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|internal
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|internal
operator|instanceof
name|ChainResolver
argument_list|)
expr_stmt|;
name|ChainResolver
name|chain
init|=
operator|(
name|ChainResolver
operator|)
name|internal
decl_stmt|;
name|List
name|subresolvers
init|=
name|chain
operator|.
name|getResolvers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|subresolvers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subresolvers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock1"
argument_list|,
operator|(
operator|(
name|DependencyResolver
operator|)
name|subresolvers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock2"
argument_list|,
operator|(
operator|(
name|DependencyResolver
operator|)
name|subresolvers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subresolvers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|MockResolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subresolvers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|MockResolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRef
parameter_list|()
throws|throws
name|Exception
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|XmlIvyConfigurationParser
name|parser
init|=
operator|new
name|XmlIvyConfigurationParser
argument_list|(
name|ivy
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|XmlIvyConfigurationParserTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivyconf-ref.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|DependencyResolver
name|internal
init|=
name|ivy
operator|.
name|getResolver
argument_list|(
literal|"internal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|internal
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|internal
operator|instanceof
name|ChainResolver
argument_list|)
expr_stmt|;
name|ChainResolver
name|chain
init|=
operator|(
name|ChainResolver
operator|)
name|internal
decl_stmt|;
name|List
name|subresolvers
init|=
name|chain
operator|.
name|getResolvers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|subresolvers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|subresolvers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FileSystemResolver
name|fsInt1
init|=
operator|(
name|FileSystemResolver
operator|)
name|subresolvers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"fs"
argument_list|,
name|fsInt1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|ivyPatterns
init|=
name|fsInt1
operator|.
name|getIvyPatterns
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ivyPatterns
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ivyPatterns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sharedrep/[organisation]/[module]/ivys/ivy-[revision].xml"
argument_list|,
name|ivyPatterns
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

