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
name|repository
operator|.
name|sftp
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPasswordField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|SftpATTRS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|SftpException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|SftpProgressMonitor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|UserInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
operator|.
name|LsEntry
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
name|IvyContext
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
name|event
operator|.
name|IvyEvent
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
name|event
operator|.
name|IvyListener
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
name|event
operator|.
name|resolve
operator|.
name|EndResolveEvent
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
name|repository
operator|.
name|AbstractRepository
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
name|repository
operator|.
name|BasicResource
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
name|repository
operator|.
name|Resource
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
name|repository
operator|.
name|TransferEvent
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
comment|/**  * SFTP Repository, allow to use a repository accessed by sftp protocol.  *   * It supports all operations: get, put and list.  *   * It relies on jsch for sftp handling, and thus is compatible with sftp version 0, 1, 2 and 3  *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|SFTPRepository
extends|extends
name|AbstractRepository
block|{
specifier|private
specifier|final
class|class
name|MyProgressMonitor
implements|implements
name|SftpProgressMonitor
block|{
specifier|private
name|long
name|_totalLength
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|int
name|op
parameter_list|,
name|String
name|src
parameter_list|,
name|String
name|dest
parameter_list|,
name|long
name|max
parameter_list|)
block|{
name|_totalLength
operator|=
name|max
expr_stmt|;
name|fireTransferStarted
argument_list|(
name|max
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|end
parameter_list|()
block|{
name|fireTransferCompleted
argument_list|(
name|_totalLength
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|count
parameter_list|(
name|long
name|count
parameter_list|)
block|{
name|fireTransferProgress
argument_list|(
literal|null
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// mandatory attributes
specifier|private
name|String
name|_host
decl_stmt|;
comment|//optional attribute
specifier|private
name|int
name|_port
init|=
literal|22
decl_stmt|;
comment|// optional attributes, asked using a dialog if not provided
specifier|private
name|String
name|_username
decl_stmt|;
specifier|private
name|String
name|_passwd
decl_stmt|;
specifier|private
specifier|transient
name|ChannelSftp
name|_channel
decl_stmt|;
specifier|private
specifier|transient
name|Session
name|_session
decl_stmt|;
specifier|public
name|SFTPRepository
parameter_list|()
block|{
block|}
specifier|public
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
return|return
operator|new
name|SFTPResource
argument_list|(
name|this
argument_list|,
name|source
argument_list|)
return|;
block|}
comment|/**      * This method is similar to getResource, except that the returned resource is fully initialised       * (resolved in the sftp repository), and that the given string is a full remote path      * @param path the full remote path in the repository of the resource      * @return a fully initialised resource, able to answer to all its methods without needing      * 	any further connection      */
specifier|public
name|Resource
name|resolveResource
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|()
decl_stmt|;
name|Collection
name|r
init|=
name|c
operator|.
name|ls
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|r
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|LsEntry
condition|)
block|{
name|LsEntry
name|entry
init|=
operator|(
name|LsEntry
operator|)
name|obj
decl_stmt|;
name|SftpATTRS
name|attrs
init|=
name|entry
operator|.
name|getAttrs
argument_list|()
decl_stmt|;
return|return
operator|new
name|BasicResource
argument_list|(
name|path
argument_list|,
literal|true
argument_list|,
name|attrs
operator|.
name|getSize
argument_list|()
argument_list|,
name|attrs
operator|.
name|getMTime
argument_list|()
operator|*
literal|1000
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//silent fail, return unexisting resource
block|}
return|return
operator|new
name|BasicResource
argument_list|(
name|path
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|void
name|get
parameter_list|(
name|String
name|source
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|IOException
block|{
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|source
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_GET
argument_list|)
expr_stmt|;
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|()
decl_stmt|;
try|try
block|{
name|c
operator|.
name|get
argument_list|(
name|source
argument_list|,
name|destination
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
operator|new
name|MyProgressMonitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
literal|"impossible to get "
operator|+
name|source
operator|+
literal|" on "
operator|+
name|getHost
argument_list|()
operator|+
operator|(
name|e
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
else|:
literal|""
operator|)
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|public
name|void
name|put
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|destination
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|fireTransferInitiated
argument_list|(
name|getResource
argument_list|(
name|destination
argument_list|)
argument_list|,
name|TransferEvent
operator|.
name|REQUEST_PUT
argument_list|)
expr_stmt|;
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|destination
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|mkdirs
argument_list|(
name|destination
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|destination
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|put
argument_list|(
name|source
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|destination
argument_list|,
operator|new
name|MyProgressMonitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SftpException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
specifier|private
name|void
name|mkdirs
parameter_list|(
name|String
name|directory
parameter_list|)
throws|throws
name|IOException
throws|,
name|SftpException
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|()
decl_stmt|;
try|try
block|{
name|SftpATTRS
name|att
init|=
name|c
operator|.
name|stat
argument_list|(
name|directory
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|att
operator|.
name|isDir
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SftpException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|directory
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|mkdirs
argument_list|(
name|directory
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|directory
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|mkdir
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
name|list
parameter_list|(
name|String
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|ChannelSftp
name|c
init|=
name|getSftpChannel
argument_list|()
decl_stmt|;
name|Collection
name|r
init|=
name|c
operator|.
name|ls
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|parent
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|parent
operator|=
name|parent
operator|+
literal|"/"
expr_stmt|;
block|}
name|List
name|result
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|r
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|LsEntry
condition|)
block|{
name|LsEntry
name|entry
init|=
operator|(
name|LsEntry
operator|)
name|obj
decl_stmt|;
if|if
condition|(
literal|"."
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
operator|||
literal|".."
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|result
operator|.
name|add
argument_list|(
name|parent
operator|+
name|entry
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//silent fail, return null listing
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Establish the connection to the server if not yet connected, and listen to ivy events 	 * for closing connection when resolve is finished. 	 *  	 * Not meant to be used in multi threaded environment. 	 *  	 * @return the ChannelSftp with which a connection is established 	 * @throws IOException if any connection problem occurs 	 */
specifier|private
name|ChannelSftp
name|getSftpChannel
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|_channel
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
name|_session
operator|=
name|jsch
operator|.
name|getSession
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|_session
operator|.
name|setUserInfo
argument_list|(
operator|new
name|UserInfo
argument_list|()
block|{
specifier|public
name|void
name|showMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|Message
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|promptYesNo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptPassword
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|promptPassphrase
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|SFTPRepository
operator|.
name|this
operator|.
name|getPasswd
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getIvy
argument_list|()
operator|.
name|addIvyListener
argument_list|(
operator|new
name|IvyListener
argument_list|()
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|_channel
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: disconnecting from "
operator|+
name|getHost
argument_list|()
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|_channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|_channel
operator|=
literal|null
expr_stmt|;
name|_session
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|_session
operator|=
literal|null
expr_stmt|;
name|event
operator|.
name|getSource
argument_list|()
operator|.
name|removeIvyListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: disconnected from "
operator|+
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|EndResolveEvent
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: connecting to "
operator|+
name|getHost
argument_list|()
operator|+
literal|"..."
argument_list|)
expr_stmt|;
name|_session
operator|.
name|connect
argument_list|()
expr_stmt|;
name|_channel
operator|=
operator|(
name|ChannelSftp
operator|)
name|_session
operator|.
name|openChannel
argument_list|(
literal|"sftp"
argument_list|)
expr_stmt|;
name|_channel
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|":: SFTP :: connected to "
operator|+
name|getHost
argument_list|()
operator|+
literal|"!"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
return|return
name|_channel
return|;
block|}
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|_host
return|;
block|}
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|_host
operator|=
name|host
expr_stmt|;
block|}
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
if|if
condition|(
name|_passwd
operator|==
literal|null
condition|)
block|{
name|promptCredentials
argument_list|()
expr_stmt|;
block|}
return|return
name|_passwd
return|;
block|}
specifier|public
name|void
name|setPasswd
parameter_list|(
name|String
name|passwd
parameter_list|)
block|{
name|_passwd
operator|=
name|passwd
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
if|if
condition|(
name|_username
operator|==
literal|null
condition|)
block|{
name|promptCredentials
argument_list|()
expr_stmt|;
block|}
return|return
name|_username
return|;
block|}
name|JTextField
name|userNameField
init|=
operator|new
name|JTextField
argument_list|(
literal|20
argument_list|)
decl_stmt|;
name|JTextField
name|passwordField
init|=
operator|new
name|JPasswordField
argument_list|(
literal|20
argument_list|)
decl_stmt|;
specifier|private
name|void
name|promptCredentials
parameter_list|()
block|{
name|List
name|components
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|_username
operator|==
literal|null
condition|)
block|{
name|JPanel
name|pane
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|pane
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"username: "
argument_list|)
argument_list|)
expr_stmt|;
name|pane
operator|.
name|add
argument_list|(
name|userNameField
argument_list|)
expr_stmt|;
name|components
operator|.
name|add
argument_list|(
name|pane
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|userNameField
operator|.
name|setText
argument_list|(
name|_username
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_passwd
operator|==
literal|null
condition|)
block|{
name|JPanel
name|pane
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
name|pane
operator|.
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"passwd:  "
argument_list|)
argument_list|)
expr_stmt|;
name|pane
operator|.
name|add
argument_list|(
name|passwordField
argument_list|)
expr_stmt|;
name|components
operator|.
name|add
argument_list|(
name|pane
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|passwordField
operator|.
name|setText
argument_list|(
name|_passwd
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|components
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|JOptionPane
operator|.
name|showConfirmDialog
argument_list|(
literal|null
argument_list|,
name|components
operator|.
name|toArray
argument_list|()
argument_list|,
name|getHost
argument_list|()
operator|+
literal|" credentials"
argument_list|,
name|JOptionPane
operator|.
name|OK_OPTION
argument_list|)
expr_stmt|;
name|_username
operator|=
name|userNameField
operator|.
name|getText
argument_list|()
expr_stmt|;
name|_passwd
operator|=
name|passwordField
operator|.
name|getText
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|_username
operator|=
name|username
expr_stmt|;
block|}
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|_port
return|;
block|}
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|_port
operator|=
name|port
expr_stmt|;
block|}
block|}
end_class

end_unit

