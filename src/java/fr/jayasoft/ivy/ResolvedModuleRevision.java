begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResolvedModuleRevision
block|{
name|DependencyResolver
name|getResolver
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getId
parameter_list|()
function_decl|;
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
name|ModuleDescriptor
name|getDescriptor
parameter_list|()
function_decl|;
name|boolean
name|isDownloaded
parameter_list|()
function_decl|;
name|boolean
name|isSearched
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

