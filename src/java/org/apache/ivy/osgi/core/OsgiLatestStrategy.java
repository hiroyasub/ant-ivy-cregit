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
name|osgi
operator|.
name|core
package|;
end_package

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
name|Comparator
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
name|IvyContext
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
name|DependencyDescriptor
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
name|ModuleDescriptor
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
name|resolve
operator|.
name|ResolvedModuleRevision
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
name|Version
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
name|latest
operator|.
name|ArtifactInfo
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
name|latest
operator|.
name|ComparatorLatestStrategy
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
name|MDResolvedResource
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
name|version
operator|.
name|VersionMatcher
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiLatestStrategy
extends|extends
name|ComparatorLatestStrategy
block|{
specifier|final
class|class
name|MridComparator
implements|implements
name|Comparator
argument_list|<
name|ModuleRevisionId
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ModuleRevisionId
name|o1
parameter_list|,
name|ModuleRevisionId
name|o2
parameter_list|)
block|{
name|Version
name|v1
init|=
operator|new
name|Version
argument_list|(
name|o1
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
name|Version
name|v2
init|=
operator|new
name|Version
argument_list|(
name|o2
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|v1
operator|.
name|compareTo
argument_list|(
name|v2
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ParseException
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Uncomparable versions:"
operator|+
name|o1
operator|.
name|getRevision
argument_list|()
operator|+
literal|" and "
operator|+
name|o2
operator|.
name|getRevision
argument_list|()
operator|+
literal|" ("
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
block|}
specifier|final
class|class
name|ArtifactInfoComparator
implements|implements
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ArtifactInfo
name|o1
parameter_list|,
name|ArtifactInfo
name|o2
parameter_list|)
block|{
name|String
name|rev1
init|=
name|o1
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|String
name|rev2
init|=
name|o2
operator|.
name|getRevision
argument_list|()
decl_stmt|;
comment|/*              * The revisions can still be not resolved, so we use the current version matcher to              * know if one revision is dynamic, and in this case if it should be considered greater              * or lower than the other one. Note that if the version matcher compare method returns              * 0, it's because it's not possible to know which revision is greater. In this case we              * consider the dynamic one to be greater, because most of the time it will then be              * actually resolved and a real comparison will occur.              */
name|VersionMatcher
name|vmatcher
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|mrid1
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|rev1
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid2
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|rev2
argument_list|)
decl_stmt|;
if|if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid1
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|vmatcher
operator|.
name|compare
argument_list|(
name|mrid1
argument_list|,
name|mrid2
argument_list|,
name|mridComparator
argument_list|)
decl_stmt|;
return|return
name|c
operator|>=
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid2
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|vmatcher
operator|.
name|compare
argument_list|(
name|mrid2
argument_list|,
name|mrid1
argument_list|,
name|mridComparator
argument_list|)
decl_stmt|;
return|return
name|c
operator|>=
literal|0
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
name|int
name|res
init|=
name|mridComparator
operator|.
name|compare
argument_list|(
name|mrid1
argument_list|,
name|mrid2
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|0
condition|)
block|{
comment|// if same requirements, maybe we can make a difference on the implementation ?
name|ModuleRevisionId
name|implMrid1
init|=
name|getImplMrid
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|implMrid2
init|=
name|getImplMrid
argument_list|(
name|o2
argument_list|)
decl_stmt|;
if|if
condition|(
name|implMrid1
operator|!=
literal|null
operator|&&
name|implMrid2
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|implMrid1
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|implMrid2
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
comment|// same implementation, compare the versions
name|res
operator|=
name|mridComparator
operator|.
name|compare
argument_list|(
name|implMrid1
argument_list|,
name|implMrid2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// not same bundle
comment|// to keep a total order, compare module names even if it means nothing
name|res
operator|=
name|implMrid1
operator|.
name|getModuleId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|implMrid2
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|res
return|;
block|}
comment|/*          * In the resolve process, a resolved requirement is represented in a special way. Here we          * deconstruct the resolved resource to know which implementation is actually resolved. See          * AbstractOSGiResolver.buildResolvedCapabilityMd()          */
specifier|private
name|ModuleRevisionId
name|getImplMrid
parameter_list|(
name|ArtifactInfo
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|MDResolvedResource
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|MDResolvedResource
name|mdrr
init|=
operator|(
name|MDResolvedResource
operator|)
name|o
decl_stmt|;
name|ResolvedModuleRevision
name|rmr
init|=
name|mdrr
operator|.
name|getResolvedModuleRevision
argument_list|()
decl_stmt|;
if|if
condition|(
name|rmr
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ModuleDescriptor
name|md
init|=
name|rmr
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|md
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
operator|.
name|equals
argument_list|(
name|BundleInfo
operator|.
name|PACKAGE_TYPE
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DependencyDescriptor
index|[]
name|dds
init|=
name|md
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
if|if
condition|(
name|dds
operator|==
literal|null
operator|||
name|dds
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|dds
index|[
literal|0
index|]
operator|.
name|getDependencyRevisionId
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|final
name|Comparator
argument_list|<
name|ModuleRevisionId
argument_list|>
name|mridComparator
init|=
operator|new
name|MridComparator
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Comparator
argument_list|<
name|ArtifactInfo
argument_list|>
name|artifactInfoComparator
init|=
operator|new
name|ArtifactInfoComparator
argument_list|()
decl_stmt|;
specifier|public
name|OsgiLatestStrategy
parameter_list|()
block|{
name|setComparator
argument_list|(
name|artifactInfoComparator
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"latest-osgi"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

