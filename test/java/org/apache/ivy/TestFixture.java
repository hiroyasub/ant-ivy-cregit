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
name|net
operator|.
name|MalformedURLException
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
name|Collection
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|DefaultDependencyDescriptor
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|core
operator|.
name|report
operator|.
name|ResolveReport
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
name|core
operator|.
name|settings
operator|.
name|IvySettings
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
name|plugins
operator|.
name|repository
operator|.
name|file
operator|.
name|FileResource
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
name|plugins
operator|.
name|resolver
operator|.
name|DependencyResolver
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
name|plugins
operator|.
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
import|;
end_import

begin_comment
comment|/**  * Fixture easing the development of tests requiring to set up a simple repository with some  * modules, using micro ivy format to describe the repository.<br/> Example of use:  *   *<pre>  * public class MyTest extends TestCase {  *     private TestFixture fixture;  *   *     protected void setUp() throws Exception {  *         fixture = new TestFixture();  *         // additional setup here  *     }  *   *     protected void tearDown() throws Exception {  *         fixture.clean();  *     }  *       *     public void testXXX() throws Exception {  *        fixture  *            .addMD("#A;1-> { #B;[1.5,1.6] #C;2.5 }")  *            .addMD("#B;1.5->#D;2.0")  *            .addMD("#B;1.6->#D;2.0")  *            .addMD("#C;2.5->#D;[1.0,1.6]")  *            .addMD("#D;1.5").addMD("#D;1.6").addMD("#D;2.0")  *            .init();  *        ResolveReport r = fixture.resolve("#A;1");  *        // assertions go here  *     }  * }  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|TestFixture
block|{
specifier|private
name|Collection
name|mds
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|public
name|TestFixture
parameter_list|()
block|{
try|try
block|{
name|this
operator|.
name|ivy
operator|=
operator|new
name|Ivy
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configureDefault
argument_list|()
expr_stmt|;
name|TestHelper
operator|.
name|loadTestSettings
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|TestFixture
name|addMD
parameter_list|(
name|String
name|microIvy
parameter_list|)
block|{
name|mds
operator|.
name|add
argument_list|(
name|TestHelper
operator|.
name|parseMicroIvyDescriptor
argument_list|(
name|microIvy
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|TestFixture
name|init
parameter_list|()
throws|throws
name|IOException
block|{
name|TestHelper
operator|.
name|fillRepository
argument_list|(
name|getTestRepository
argument_list|()
argument_list|,
name|mds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|DependencyResolver
name|getTestRepository
parameter_list|()
block|{
return|return
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolver
argument_list|(
literal|"test"
argument_list|)
return|;
block|}
specifier|public
name|IvySettings
name|getSettings
parameter_list|()
block|{
return|return
name|ivy
operator|.
name|getSettings
argument_list|()
return|;
block|}
specifier|public
name|Ivy
name|getIvy
parameter_list|()
block|{
return|return
name|ivy
return|;
block|}
specifier|public
name|void
name|clean
parameter_list|()
block|{
name|TestHelper
operator|.
name|cleanTest
argument_list|()
expr_stmt|;
block|}
specifier|public
name|File
name|getIvyFile
parameter_list|(
name|String
name|mrid
parameter_list|)
block|{
name|ResolvedResource
name|r
init|=
name|getTestRepository
argument_list|()
operator|.
name|findIvyFileRef
argument_list|(
operator|new
name|DefaultDependencyDescriptor
argument_list|(
name|ModuleRevisionId
operator|.
name|parse
argument_list|(
name|mrid
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|,
name|TestHelper
operator|.
name|newResolveData
argument_list|(
name|getSettings
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"module not found: "
operator|+
name|mrid
argument_list|)
throw|;
block|}
return|return
operator|(
operator|(
name|FileResource
operator|)
name|r
operator|.
name|getResource
argument_list|()
operator|)
operator|.
name|getFile
argument_list|()
return|;
block|}
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|String
name|mrid
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|ParseException
throws|,
name|IOException
block|{
return|return
name|ivy
operator|.
name|resolve
argument_list|(
name|getIvyFile
argument_list|(
name|mrid
argument_list|)
operator|.
name|toURL
argument_list|()
argument_list|,
name|TestHelper
operator|.
name|newResolveOptions
argument_list|(
name|getSettings
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
