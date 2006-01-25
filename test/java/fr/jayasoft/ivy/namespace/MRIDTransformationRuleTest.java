begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the licence found in LICENCE.TXT in the root directory of the project.  * Copyright Jayasoft 2005 - All rights reserved  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|namespace
package|;
end_package

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
name|MRIDTransformationRuleTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testTransformation
parameter_list|()
block|{
name|MRIDTransformationRule
name|r
init|=
operator|new
name|MRIDTransformationRule
argument_list|()
decl_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"apache"
argument_list|,
literal|"commons.+"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$m0"
argument_list|,
literal|"$m0"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"commons-client"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|MRIDTransformationRule
argument_list|()
expr_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|null
argument_list|,
literal|"commons\\-(.+)"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$o0.commons"
argument_list|,
literal|"$m1"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache.commons"
argument_list|,
literal|"client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|MRIDTransformationRule
argument_list|()
expr_stmt|;
name|r
operator|.
name|addSrc
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"(.+)\\.(.+)"
argument_list|,
literal|".+"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|addDest
argument_list|(
operator|new
name|MRIDRule
argument_list|(
literal|"$o1"
argument_list|,
literal|"$o2-$m0"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"commons-client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache.commons"
argument_list|,
literal|"client"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|r
operator|.
name|transform
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|"apache"
argument_list|,
literal|"module"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

