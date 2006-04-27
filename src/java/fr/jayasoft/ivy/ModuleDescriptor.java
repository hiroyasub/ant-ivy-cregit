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
name|ModuleDescriptor
extends|extends
name|ExtendableItem
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONFIGURATION
init|=
literal|"default"
decl_stmt|;
comment|/**      * Returns true if this descriptor is a default one, i.e.      * one generated for a module not actually having one.       * @return      */
name|boolean
name|isDefault
parameter_list|()
function_decl|;
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * The module revision id returned here is the resolved one,       * i.e. it is never a latest one. If the revision has not been       * resolved, a null revision should be returned by getRevision()      * of the returned ModuleRevisionId.      * This revision must be the same as the module descriptor resolved      * revision id unless no module descriptor is defined      * @return      */
name|ModuleRevisionId
name|getResolvedModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * This method update the resolved module revision id      * @param revId      */
name|void
name|setResolvedModuleRevisionId
parameter_list|(
name|ModuleRevisionId
name|revId
parameter_list|)
function_decl|;
comment|/**      * This method update the resolved publication date      * @param publicationDate      */
name|void
name|setResolvedPublicationDate
parameter_list|(
name|Date
name|publicationDate
parameter_list|)
function_decl|;
name|String
name|getStatus
parameter_list|()
function_decl|;
comment|/**      * may be null if unknown in the descriptor itself      * @return      */
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
comment|/**      * the publication date of the module revision should be the date at which it has been published,      * i.e. in general the date of any of its published artifacts, since all published artifact      * of a module should follow the same publishing cycle.      */
name|Date
name|getResolvedPublicationDate
parameter_list|()
function_decl|;
comment|/**      * Returns all the configurations declared by this module as an array.      * This array is never empty (a 'default' conf is assumed when none is declared      * in the ivy file)       * @return all the configurations declared by this module as an array.      */
name|Configuration
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
name|String
index|[]
name|getConfigurationsNames
parameter_list|()
function_decl|;
name|String
index|[]
name|getPublicConfigurationsNames
parameter_list|()
function_decl|;
name|Artifact
index|[]
name|getArtifacts
parameter_list|(
name|String
name|conf
parameter_list|)
function_decl|;
name|DependencyDescriptor
index|[]
name|getDependencies
parameter_list|()
function_decl|;
comment|/**      * Returns true if the module described by this descriptor dependes directly upon the      * given module descriptor       * @param md      * @return      */
name|boolean
name|dependsOn
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
function_decl|;
comment|/**      * @param confName      * @return      */
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|confName
parameter_list|)
function_decl|;
comment|/**      * Returns the conflict manager to use for the given ModuleId      *       * @param id      * @return      */
name|ConflictManager
name|getConflictManager
parameter_list|(
name|ModuleId
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the licenses of the module described by this descriptor      * @return      */
name|License
index|[]
name|getLicenses
parameter_list|()
function_decl|;
name|String
name|getHomePage
parameter_list|()
function_decl|;
name|long
name|getLastModified
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

