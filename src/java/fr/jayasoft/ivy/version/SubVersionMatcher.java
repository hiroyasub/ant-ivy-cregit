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
name|version
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
name|ModuleDescriptor
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
name|ModuleRevisionId
import|;
end_import

begin_class
specifier|public
class|class
name|SubVersionMatcher
implements|implements
name|VersionMatcher
block|{
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
return|return
name|askedMrid
operator|.
name|getRevision
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
name|String
name|prefix
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|askedMrid
operator|.
name|getRevision
argument_list|()
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|foundMrid
operator|.
name|getRevision
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|needModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleDescriptor
name|foundMD
parameter_list|)
block|{
return|return
name|accept
argument_list|(
name|askedMrid
argument_list|,
name|foundMD
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

