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
name|net
operator|.
name|URL
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
name|matcher
operator|.
name|PatternMatcher
import|;
end_import

begin_comment
comment|/**  * This describes an artifact that is asked for a dependency.  *   * It is used to resctrict the artifacts asked for a dependency, or describe them  * when there is no ivy file.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyArtifactDescriptor
block|{
comment|/**      * Returns the dependency descriptor in which this artifact is asked      * @return      */
specifier|public
name|DependencyDescriptor
name|getDependency
parameter_list|()
function_decl|;
comment|/**      * Returns the id of the described artifact, without revision information      * @return      */
specifier|public
name|ArtifactId
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns the name of the artifact asked      * @return      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the type of the artifact asked      * @return      */
specifier|public
name|String
name|getType
parameter_list|()
function_decl|;
comment|/**      * Returns the ext of the artifact asked      * @return      */
specifier|public
name|String
name|getExt
parameter_list|()
function_decl|;
comment|/**      * Returns the url to look this artifact at      * @return      */
specifier|public
name|URL
name|getUrl
parameter_list|()
function_decl|;
comment|/**      * Returns the configurations of the module in which the artifact is asked      * @return an array of configuration names in which the artifact is asked      */
specifier|public
name|String
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * Returns the matcher to use to know if an artifact match the current descriptor      * @return      */
specifier|public
name|PatternMatcher
name|getMatcher
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

