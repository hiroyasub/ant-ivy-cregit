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
name|resolve
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|cache
operator|.
name|CacheManager
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterHelper
import|;
end_import

begin_comment
comment|/**  * A set of options used during resolve related tasks  *   * @see ResolveEngine  * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|ResolveOptions
block|{
comment|/** 	 * an array of configuration names to resolve - must not be null nor empty 	 */
specifier|private
name|String
index|[]
name|confs
init|=
operator|new
name|String
index|[]
block|{
literal|"*"
block|}
decl_stmt|;
comment|/** 	 * the revision of the module for which dependencies should be resolved.      * This revision is considered as the resolved revision of the module, unless it is null.      * If it is null, then a default revision is given if necessary (no revision found in ivy file) 	 */
specifier|private
name|String
name|revision
init|=
literal|null
decl_stmt|;
comment|/** 	 * The cache manager to use during resolve 	 */
specifier|private
name|CacheManager
name|cache
decl_stmt|;
comment|/** 	 * the date for which the dependencies should be resolved.  	 * All obtained artifacts should have a publication date which is before  	 * or equal to the given date. 	 * The date can be null, in which case all artifacts will be considered 	 */
specifier|private
name|Date
name|date
init|=
literal|null
decl_stmt|;
comment|/** 	 * True if validation of module descriptors should done, false otherwise 	 */
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
comment|/** 	 * True if only the cache should be used for resolve, false 	 * if a real resolve with dependency resolvers should be done 	 */
specifier|private
name|boolean
name|useCacheOnly
init|=
literal|false
decl_stmt|;
comment|/** 	 * True if the dependencies should be resolved transitively, false 	 * if only direct dependencies should be resolved 	 */
specifier|private
name|boolean
name|transitive
init|=
literal|true
decl_stmt|;
comment|/** 	 * True if the resolve should also download artifacts, false 	 * if only dependency resolution with module descriptors should be done 	 */
specifier|private
name|boolean
name|download
init|=
literal|true
decl_stmt|;
comment|/** 	 * True if a report of the resolve process should be output at the end of the  	 * process, false otherwise 	 */
specifier|private
name|boolean
name|outputReport
init|=
literal|true
decl_stmt|;
comment|/** 	 * True if the original files from the repositories should be used instead  	 * of downloaded ones, false otherwise. 	 * This means that artifacts which can be used directory in their original location 	 * won't be downloaded if this option is set to true  	 */
specifier|private
name|boolean
name|useOrigin
init|=
literal|false
decl_stmt|;
comment|/** 	 * A filter to use to avoid downloading all artifacts. 	 */
specifier|private
name|Filter
name|artifactFilter
init|=
name|FilterHelper
operator|.
name|NO_FILTER
decl_stmt|;
specifier|public
name|ResolveOptions
parameter_list|()
block|{
block|}
specifier|public
name|ResolveOptions
parameter_list|(
name|ResolveOptions
name|options
parameter_list|)
block|{
name|confs
operator|=
name|options
operator|.
name|confs
expr_stmt|;
name|revision
operator|=
name|options
operator|.
name|revision
expr_stmt|;
name|cache
operator|=
name|options
operator|.
name|cache
expr_stmt|;
name|date
operator|=
name|options
operator|.
name|date
expr_stmt|;
name|validate
operator|=
name|options
operator|.
name|validate
expr_stmt|;
name|useCacheOnly
operator|=
name|options
operator|.
name|useCacheOnly
expr_stmt|;
name|transitive
operator|=
name|options
operator|.
name|transitive
expr_stmt|;
name|download
operator|=
name|options
operator|.
name|download
expr_stmt|;
name|outputReport
operator|=
name|options
operator|.
name|outputReport
expr_stmt|;
name|useOrigin
operator|=
name|options
operator|.
name|useOrigin
expr_stmt|;
name|artifactFilter
operator|=
name|options
operator|.
name|artifactFilter
expr_stmt|;
block|}
specifier|public
name|Filter
name|getArtifactFilter
parameter_list|()
block|{
return|return
name|artifactFilter
return|;
block|}
specifier|public
name|ResolveOptions
name|setArtifactFilter
parameter_list|(
name|Filter
name|artifactFilter
parameter_list|)
block|{
name|this
operator|.
name|artifactFilter
operator|=
name|artifactFilter
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CacheManager
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
specifier|public
name|ResolveOptions
name|setCache
parameter_list|(
name|CacheManager
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
index|[]
name|getConfs
parameter_list|()
block|{
return|return
name|confs
return|;
block|}
specifier|public
name|ResolveOptions
name|setConfs
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|this
operator|.
name|confs
operator|=
name|confs
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
specifier|public
name|ResolveOptions
name|setDate
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isDownload
parameter_list|()
block|{
return|return
name|download
return|;
block|}
specifier|public
name|ResolveOptions
name|setDownload
parameter_list|(
name|boolean
name|download
parameter_list|)
block|{
name|this
operator|.
name|download
operator|=
name|download
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isOutputReport
parameter_list|()
block|{
return|return
name|outputReport
return|;
block|}
specifier|public
name|ResolveOptions
name|setOutputReport
parameter_list|(
name|boolean
name|outputReport
parameter_list|)
block|{
name|this
operator|.
name|outputReport
operator|=
name|outputReport
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isTransitive
parameter_list|()
block|{
return|return
name|transitive
return|;
block|}
specifier|public
name|ResolveOptions
name|setTransitive
parameter_list|(
name|boolean
name|transitive
parameter_list|)
block|{
name|this
operator|.
name|transitive
operator|=
name|transitive
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUseCacheOnly
parameter_list|()
block|{
return|return
name|useCacheOnly
return|;
block|}
specifier|public
name|ResolveOptions
name|setUseCacheOnly
parameter_list|(
name|boolean
name|useCacheOnly
parameter_list|)
block|{
name|this
operator|.
name|useCacheOnly
operator|=
name|useCacheOnly
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
specifier|public
name|ResolveOptions
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
specifier|public
name|ResolveOptions
name|setRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|boolean
name|isUseOrigin
parameter_list|()
block|{
return|return
name|useOrigin
return|;
block|}
specifier|public
name|ResolveOptions
name|setUseOrigin
parameter_list|(
name|boolean
name|useOrigin
parameter_list|)
block|{
name|this
operator|.
name|useOrigin
operator|=
name|useOrigin
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

