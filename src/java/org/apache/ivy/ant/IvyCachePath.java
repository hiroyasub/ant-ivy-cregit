begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
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
name|List
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
name|ArtifactDownloadReport
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
name|core
operator|.
name|BundleInfo
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
name|core
operator|.
name|ManifestParser
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
name|types
operator|.
name|Path
import|;
end_import

begin_comment
comment|/**  * Creates an ant path consisting in all artifacts found during a resolve.  */
end_comment

begin_class
specifier|public
class|class
name|IvyCachePath
extends|extends
name|IvyCacheTask
block|{
specifier|private
name|String
name|pathid
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|boolean
name|osgi
init|=
literal|false
decl_stmt|;
specifier|public
name|String
name|getPathid
parameter_list|()
block|{
return|return
name|pathid
return|;
block|}
specifier|public
name|void
name|setPathid
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|pathid
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|void
name|setOsgi
parameter_list|(
name|boolean
name|osgi
parameter_list|)
block|{
name|this
operator|.
name|osgi
operator|=
name|osgi
expr_stmt|;
block|}
comment|/**      * @deprecated use setPathid instead      * @param id String      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
if|if
condition|(
name|pathid
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"pathid is required in ivy classpath"
argument_list|)
throw|;
block|}
name|pathid
operator|=
name|id
expr_stmt|;
name|log
argument_list|(
literal|"ID IS DEPRECATED, PLEASE USE PATHID INSTEAD"
argument_list|,
name|Project
operator|.
name|MSG_WARN
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Path
name|path
init|=
operator|new
name|Path
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|pathid
argument_list|,
name|path
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactDownloadReport
name|adr
range|:
name|getArtifactReports
argument_list|()
control|)
block|{
name|File
name|f
init|=
name|adr
operator|.
name|getLocalFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|.
name|getUnpackedLocalFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|f
operator|=
name|adr
operator|.
name|getUnpackedLocalFile
argument_list|()
expr_stmt|;
block|}
name|addToPath
argument_list|(
name|path
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to build ivy path: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|addToPath
parameter_list|(
name|Path
name|path
parameter_list|,
name|File
name|f
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|osgi
operator|||
operator|!
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return;
block|}
name|File
name|manifest
init|=
operator|new
name|File
argument_list|(
name|f
argument_list|,
literal|"META-INF/MANIFEST.MF"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|manifest
operator|.
name|exists
argument_list|()
condition|)
block|{
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return;
block|}
name|BundleInfo
name|bundleInfo
init|=
name|ManifestParser
operator|.
name|parseManifest
argument_list|(
name|manifest
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|cp
init|=
name|bundleInfo
operator|.
name|getClasspath
argument_list|()
decl_stmt|;
if|if
condition|(
name|cp
operator|==
literal|null
condition|)
block|{
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|String
name|p
range|:
name|cp
control|)
block|{
if|if
condition|(
name|p
operator|.
name|equals
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|createPathElement
argument_list|()
operator|.
name|setLocation
argument_list|(
operator|new
name|File
argument_list|(
name|f
argument_list|,
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

