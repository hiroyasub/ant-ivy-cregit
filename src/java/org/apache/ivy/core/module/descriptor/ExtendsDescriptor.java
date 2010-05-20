begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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
name|module
operator|.
name|descriptor
package|;
end_package

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

begin_comment
comment|/**  * Describes parent descriptor information for a module descriptor.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExtendsDescriptor
block|{
comment|/** get the module revision id of the declared parent descriptor */
specifier|public
name|ModuleRevisionId
name|getParentRevisionId
parameter_list|()
function_decl|;
comment|/**      * get the resolved revision id for {@link #getParentRevisionId}, see      * {@link org.apache.ivy.core.module.descriptor.ModuleDescriptor#getResolvedModuleRevisionId()} }      */
specifier|public
name|ModuleRevisionId
name|getResolvedParentRevisionId
parameter_list|()
function_decl|;
comment|/**      * If there is an explicit path to check for the parent descriptor, return it.      * Otherwise returns null.      */
specifier|public
name|String
name|getLocation
parameter_list|()
function_decl|;
comment|/**      * Get the parts of the parent descriptor that are inherited.  Default      * supported types are<code>info</code>,<code>description</code>,      *<code>configurations</code>,<code>dependencies</code>, and/or<code>all</code>.      * Ivy extensions may add support for additional extends types.      */
specifier|public
name|String
index|[]
name|getExtendsTypes
parameter_list|()
function_decl|;
comment|/** @return true if the<code>all</code> extend type is specified, implying all other types */
specifier|public
name|boolean
name|isAllInherited
parameter_list|()
function_decl|;
comment|/** @return true if parent info attributes are inherited (organisation, branch, revision, etc)*/
specifier|public
name|boolean
name|isInfoInherited
parameter_list|()
function_decl|;
comment|/** @return true if parent description is inherited */
specifier|public
name|boolean
name|isDescriptionInherited
parameter_list|()
function_decl|;
comment|/** @return true if parent configurations are inherited */
specifier|public
name|boolean
name|areConfigurationsInherited
parameter_list|()
function_decl|;
comment|/** @return true if parent dependencies are inherited */
specifier|public
name|boolean
name|areDependenciesInherited
parameter_list|()
function_decl|;
block|}
end_interface

end_unit
