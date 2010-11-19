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
name|osgi
operator|.
name|util
package|;
end_package

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
name|ivy
operator|.
name|osgi
operator|.
name|util
operator|.
name|NameUtil
operator|.
name|OrgAndName
import|;
end_import

begin_class
specifier|public
class|class
name|NameUtilTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testAsOrgAndName
parameter_list|()
block|{
name|OrgAndName
name|oan
decl_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"java.foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"java.foo.bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"javax.foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"javax"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"javax.foo.bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"javax"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"org.eclipse.foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.eclipse"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"org.eclipse.foo.bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.eclipse"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"com.eclipse.foo.bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com.eclipse"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
name|oan
operator|=
name|NameUtil
operator|.
name|instance
argument_list|()
operator|.
name|asOrgAndName
argument_list|(
literal|"net.eclipse.foo.bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"net.eclipse"
argument_list|,
name|oan
operator|.
name|org
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.bar"
argument_list|,
name|oan
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
