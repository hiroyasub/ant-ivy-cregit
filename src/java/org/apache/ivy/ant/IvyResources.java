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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Location
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
name|Reference
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
name|ResourceCollection
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
name|resources
operator|.
name|BaseResourceCollectionWrapper
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
name|resources
operator|.
name|FileResource
import|;
end_import

begin_class
specifier|public
class|class
name|IvyResources
extends|extends
name|IvyCacheTask
implements|implements
name|ResourceCollection
block|{
comment|/**      * Delegate for the implementation of the resource collection      */
specifier|private
class|class
name|IvyBaseResourceCollectionWrapper
extends|extends
name|BaseResourceCollectionWrapper
block|{
specifier|protected
name|Collection
name|getCollection
parameter_list|()
block|{
return|return
name|resolveResources
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
specifier|private
name|IvyBaseResourceCollectionWrapper
name|wrapper
init|=
operator|new
name|IvyBaseResourceCollectionWrapper
argument_list|()
decl_stmt|;
comment|// delegate the ProjectComponent API on the wrapper
specifier|public
name|void
name|setLocation
parameter_list|(
name|Location
name|location
parameter_list|)
block|{
name|super
operator|.
name|setLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|setLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProject
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|super
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|setProject
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|desc
parameter_list|)
block|{
name|super
operator|.
name|setDescription
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|setDescription
argument_list|(
name|desc
argument_list|)
expr_stmt|;
block|}
comment|// delegate the DataType API on the wrapper
specifier|public
name|void
name|setRefid
parameter_list|(
name|Reference
name|ref
parameter_list|)
block|{
name|wrapper
operator|.
name|setRefid
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
comment|// delegate the AbstractResourceCollectionWrapper API on the wrapper
specifier|public
name|void
name|setCache
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|wrapper
operator|.
name|setCache
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|// implementation of the Resource Collection API
specifier|public
name|boolean
name|isFilesystemOnly
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|Iterator
name|iterator
parameter_list|()
block|{
return|return
name|wrapper
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|wrapper
operator|.
name|size
argument_list|()
return|;
block|}
comment|// convert the ivy reports into an Ant Resource collection
specifier|private
name|Collection
name|resolveResources
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|BuildException
block|{
name|prepareAndCheck
argument_list|()
expr_stmt|;
try|try
block|{
name|List
comment|/*<FileResource> */
name|resources
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|getProject
argument_list|()
operator|.
name|addReference
argument_list|(
name|id
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|getArtifactReports
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactDownloadReport
name|a
init|=
operator|(
name|ArtifactDownloadReport
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|resources
operator|.
name|add
argument_list|(
operator|new
name|FileResource
argument_list|(
name|a
operator|.
name|getLocalFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
return|;
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
literal|"impossible to build ivy resources: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// implementation of the IvyPostResolveTask API
specifier|public
name|void
name|doExecute
parameter_list|()
throws|throws
name|BuildException
block|{
comment|// TODO : maybe there is a way to implement it ?
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"ivy:resources should not be used as a Ant Task"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit
