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
operator|.
name|url
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
name|io
operator|.
name|InputStream
import|;
end_import

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
name|util
operator|.
name|CopyProgressListener
import|;
end_import

begin_comment
comment|/**  * This interface is responsible for handling some URL manipulation  * (stream opening, downloading, check reachability, ...).   *   * @author Xavier Hanin  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|URLHandler
block|{
comment|/**      * Returns true if the given url is reachable, and without      * error code in case of http urls.      * @param url the url to check      * @return true if the given url is reachable      */
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|)
function_decl|;
comment|/**      * Returns true if the given url is reachable, and without      * error code in case of http urls.      * @param url the url to check      * @param timeout the maximum time before considering an url is not reachable      *        a timeout of zero indicates no timeout      * @return true if the given url is reachable      */
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|URL
name|url
parameter_list|,
name|int
name|timeout
parameter_list|)
function_decl|;
specifier|public
name|InputStream
name|openStream
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
function_decl|;
specifier|public
name|void
name|download
parameter_list|(
name|URL
name|src
parameter_list|,
name|File
name|dest
parameter_list|,
name|CopyProgressListener
name|l
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

