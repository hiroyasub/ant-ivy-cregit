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

begin_interface
specifier|public
interface|interface
name|LatestStrategy
block|{
comment|/**      * Finds the latest artifact among the given artifacts info.      * The definition of 'latest' depends on the strategy itself.      * Given artifacts info are all good candidate. If the given date is not      * null, then found artifact should not be later than this date.       *       * @param infos      * @param date      * @return the latest artifact among the given ones.      */
name|ArtifactInfo
name|findLatest
parameter_list|(
name|ArtifactInfo
index|[]
name|infos
parameter_list|,
name|Date
name|date
parameter_list|)
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

