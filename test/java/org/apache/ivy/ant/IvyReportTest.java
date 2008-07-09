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
name|java
operator|.
name|util
operator|.
name|Locale
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
name|ivy
operator|.
name|util
operator|.
name|FileUtil
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

begin_class
specifier|public
class|class
name|IvyReportTest
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|cache
decl_stmt|;
specifier|private
name|IvyReport
name|report
decl_stmt|;
specifier|private
name|Project
name|project
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
name|project
operator|=
operator|new
name|Project
argument_list|()
expr_stmt|;
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.settings.file"
argument_list|,
literal|"test/repositories/ivysettings.xml"
argument_list|)
expr_stmt|;
name|report
operator|=
operator|new
name|IvyReport
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTaskName
argument_list|(
literal|"report"
argument_list|)
expr_stmt|;
name|report
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"ivy.cache.dir"
argument_list|,
name|cache
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCache
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|File
argument_list|(
literal|"build/cache"
argument_list|)
expr_stmt|;
name|cache
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
name|cache
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
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/ivy-report.css"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// IVY-826
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testWithLatest
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/repositories/1/org6/mod6.2/ivys/ivy-0.7.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|setXml
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|File
name|xmlReport
init|=
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/org6-mod6.2-default.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xmlReport
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check that revision 2.2 of mod1.2 is only present once
name|String
name|reportContent
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
name|xmlReport
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|reportContent
operator|.
name|indexOf
argument_list|(
literal|"<revision name=\"2.2\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|index
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|index
operator|=
name|reportContent
operator|.
name|indexOf
argument_list|(
literal|"<revision name=\"2.2\""
argument_list|,
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|index
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCopyCssIfTodirNotSet
parameter_list|()
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setGraph
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"apache-resolve-simple-default.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
literal|"ivy-report.css"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// IVY-826
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"apache-resolve-simple-default.html"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
literal|"ivy-report.css"
argument_list|)
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testNoRevisionInOutputPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-simple.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|setOutputpattern
argument_list|(
literal|"[organisation]-[module]-[revision].[ext]"
argument_list|)
expr_stmt|;
name|report
operator|.
name|setConf
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-1.0.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-1.0.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testMultipleConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
name|Locale
name|oldLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
try|try
block|{
comment|// set the locale to UK as workaround for SUN bug 6240963
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|UK
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/ant/ivy-multiconf.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-default.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-compile.html"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/apache-resolve-simple-compile.graphml"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|oldLocale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testRegularCircular
parameter_list|()
throws|throws
name|Exception
block|{
name|project
operator|.
name|setProperty
argument_list|(
literal|"ivy.dep.file"
argument_list|,
literal|"test/repositories/2/mod11.1/ivy-1.0.xml"
argument_list|)
expr_stmt|;
name|IvyResolve
name|res
init|=
operator|new
name|IvyResolve
argument_list|()
decl_stmt|;
name|res
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|res
operator|.
name|execute
argument_list|()
expr_stmt|;
name|report
operator|.
name|setTodir
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report"
argument_list|)
argument_list|)
expr_stmt|;
name|report
operator|.
name|setXml
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// do not test any xsl transformation here, because of problems of build in our continuous
comment|// integration server
name|report
operator|.
name|setXsl
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|report
operator|.
name|setGraph
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|report
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|cache
argument_list|,
literal|"report/org11-mod11.1-compile.xml"
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

