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
name|text
operator|.
name|ParseException
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
name|report
operator|.
name|DownloadReport
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
name|resolver
operator|.
name|ModuleEntry
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
name|resolver
operator|.
name|OrganisationEntry
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
name|resolver
operator|.
name|RevisionEntry
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyResolver
block|{
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Should only be used by configurator      * @param name the new name of the resolver      */
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Resolve a module by id, getting its module descriptor and      * resolving the revision if it's a latest one (i.e. a revision      * uniquely identifying the revision of a module in the current environment -      * If this revision is not able to identify uniquelely the revision of the module      * outside of the current environment, then the resolved revision must begin by ##)      * @throws ParseException      */
name|ResolvedModuleRevision
name|getDependency
parameter_list|(
name|DependencyDescriptor
name|dd
parameter_list|,
name|ResolveData
name|data
parameter_list|)
throws|throws
name|ParseException
function_decl|;
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|Ivy
name|ivy
parameter_list|,
name|File
name|cache
parameter_list|)
function_decl|;
name|boolean
name|exists
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Reports last resolve failure as Messages      */
name|void
name|reportFailure
parameter_list|()
function_decl|;
comment|/**      * Reports last artifact download failure as Messages      * @param art      */
name|void
name|reportFailure
parameter_list|(
name|Artifact
name|art
parameter_list|)
function_decl|;
comment|// listing methods, enable to know what is available from this resolver
comment|// the listing methods must only list entries directly
comment|// available from them, no recursion is needed as long as sub resolvers
comment|// are registered in ivy too.
name|OrganisationEntry
index|[]
name|listOrganisations
parameter_list|()
function_decl|;
name|ModuleEntry
index|[]
name|listModules
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
function_decl|;
name|RevisionEntry
index|[]
name|listRevisions
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
function_decl|;
name|void
name|dumpConfig
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

