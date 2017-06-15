begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|core
operator|.
name|module
operator|.
name|descriptor
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultDependencyDescriptorTest
block|{
comment|/*      * Test method for      * 'org.apache.ivy.DefaultDependencyDescriptor.replaceSelfFallbackPattern(String, String)'      */
annotation|@
name|Test
specifier|public
name|void
name|testReplaceSelfFallbackPattern
parameter_list|()
block|{
name|String
name|replacedConf
init|=
name|DefaultDependencyDescriptor
operator|.
name|replaceSelfFallbackPattern
argument_list|(
literal|"@(default)"
argument_list|,
literal|"compile"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"compile(default)"
argument_list|,
name|replacedConf
argument_list|)
expr_stmt|;
name|replacedConf
operator|=
name|DefaultDependencyDescriptor
operator|.
name|replaceSelfFallbackPattern
argument_list|(
literal|"default"
argument_list|,
literal|"compile"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|replacedConf
argument_list|)
expr_stmt|;
name|replacedConf
operator|=
name|DefaultDependencyDescriptor
operator|.
name|replaceSelfFallbackPattern
argument_list|(
literal|"@"
argument_list|,
literal|"runtime"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime"
argument_list|)
expr_stmt|;
block|}
comment|/*      * Test method for      * 'org.apache.ivy.DefaultDependencyDescriptor.replaceThisFallbackPattern(String, String)'      */
annotation|@
name|Test
specifier|public
name|void
name|testReplaceThisFallbackPattern
parameter_list|()
block|{
name|String
name|replacedConf
init|=
name|DefaultDependencyDescriptor
operator|.
name|replaceThisFallbackPattern
argument_list|(
literal|"#(default)"
argument_list|,
literal|"compile"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"compile(default)"
argument_list|,
name|replacedConf
argument_list|)
expr_stmt|;
name|replacedConf
operator|=
name|DefaultDependencyDescriptor
operator|.
name|replaceThisFallbackPattern
argument_list|(
literal|"default"
argument_list|,
literal|"compile"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|replacedConf
argument_list|)
expr_stmt|;
name|replacedConf
operator|=
name|DefaultDependencyDescriptor
operator|.
name|replaceThisFallbackPattern
argument_list|(
literal|"#"
argument_list|,
literal|"runtime"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"runtime"
argument_list|,
literal|"runtime"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

