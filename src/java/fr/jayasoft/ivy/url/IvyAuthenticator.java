begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * version 1.3.1  */
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
name|net
operator|.
name|Authenticator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|PasswordAuthentication
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
name|Credentials
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
name|Message
import|;
end_import

begin_comment
comment|/**  *   * @author Christian Riege  * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|IvyAuthenticator
extends|extends
name|Authenticator
block|{
comment|/**      * The sole instance.      */
specifier|public
specifier|final
specifier|static
name|IvyAuthenticator
name|INSTANCE
init|=
operator|new
name|IvyAuthenticator
argument_list|()
decl_stmt|;
comment|/**      * Private c'tor to prevent instantiation. Also installs this as the default      * Authenticator to use by the JVM.      */
specifier|private
name|IvyAuthenticator
parameter_list|()
block|{
comment|// Install this as the default Authenticator object.
name|Authenticator
operator|.
name|setDefault
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|// API ******************************************************************
comment|// Overriding Authenticator *********************************************
specifier|protected
name|PasswordAuthentication
name|getPasswordAuthentication
parameter_list|()
block|{
name|Credentials
name|c
init|=
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|getCredentials
argument_list|(
name|getRequestingPrompt
argument_list|()
argument_list|,
name|getRequestingHost
argument_list|()
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"authentication: k='"
operator|+
name|Credentials
operator|.
name|buildKey
argument_list|(
name|getRequestingPrompt
argument_list|()
argument_list|,
name|getRequestingHost
argument_list|()
argument_list|)
operator|+
literal|"' c='"
operator|+
name|c
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
name|c
operator|!=
literal|null
condition|?
operator|new
name|PasswordAuthentication
argument_list|(
name|c
operator|.
name|getUserName
argument_list|()
argument_list|,
name|c
operator|.
name|getPasswd
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

