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

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|extendable
operator|.
name|ExtendableItem
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|Artifact
extends|extends
name|ExtendableItem
block|{
comment|/**      * Returns the resolved module revision id for this artifact      * @return      */
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
function_decl|;
comment|/**      * Returns the resolved publication date for this artifact      * @return the resolved publication date      */
name|Date
name|getPublicationDate
parameter_list|()
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
name|String
name|getType
parameter_list|()
function_decl|;
name|String
name|getExt
parameter_list|()
function_decl|;
name|String
index|[]
name|getConfigurations
parameter_list|()
function_decl|;
comment|/**      * @return the id of the artifact      */
name|ArtifactRevisionId
name|getId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

