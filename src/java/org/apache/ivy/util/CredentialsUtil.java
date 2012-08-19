begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GridBagConstraints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GridBagLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Insets
import|;
end_import

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ImageIcon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JCheckBox
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CredentialsUtil
block|{
specifier|private
specifier|static
specifier|final
class|class
name|CredentialPanel
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
specifier|final
name|int
name|FIELD_LENGTH
init|=
literal|20
decl_stmt|;
specifier|private
name|JTextField
name|userNameField
init|=
operator|new
name|JTextField
argument_list|(
name|FIELD_LENGTH
argument_list|)
decl_stmt|;
specifier|private
name|JTextField
name|passwordField
init|=
operator|new
name|JPasswordField
argument_list|(
name|FIELD_LENGTH
argument_list|)
decl_stmt|;
specifier|private
name|JCheckBox
name|rememberDataCB
init|=
operator|new
name|JCheckBox
argument_list|(
literal|"remember my information"
argument_list|)
decl_stmt|;
name|CredentialPanel
parameter_list|(
name|Credentials
name|credentials
parameter_list|,
name|File
name|passfile
parameter_list|)
block|{
name|GridBagLayout
name|layout
init|=
operator|new
name|GridBagLayout
argument_list|()
decl_stmt|;
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|GridBagConstraints
name|c
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|c
operator|.
name|insets
operator|=
operator|new
name|Insets
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridx
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|gridheight
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|gridwidth
operator|=
literal|2
expr_stmt|;
name|String
name|prompt
init|=
name|credentials
operator|.
name|getRealm
argument_list|()
operator|!=
literal|null
condition|?
literal|"Enter username and password for \""
operator|+
name|credentials
operator|.
name|getRealm
argument_list|()
operator|+
literal|"\" at "
operator|+
name|credentials
operator|.
name|getHost
argument_list|()
else|:
literal|"Enter username and password for "
operator|+
name|credentials
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
name|prompt
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridy
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|gridwidth
operator|=
literal|1
expr_stmt|;
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"username: "
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridx
operator|=
literal|2
expr_stmt|;
name|add
argument_list|(
name|userNameField
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridx
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|gridy
operator|++
expr_stmt|;
if|if
condition|(
name|credentials
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|userNameField
operator|.
name|setText
argument_list|(
name|credentials
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|credentials
operator|.
name|getPasswd
argument_list|()
operator|==
literal|null
condition|)
block|{
name|add
argument_list|(
operator|new
name|JLabel
argument_list|(
literal|"passwd:  "
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridx
operator|=
literal|2
expr_stmt|;
name|add
argument_list|(
name|passwordField
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridx
operator|=
literal|1
expr_stmt|;
name|c
operator|.
name|gridy
operator|++
expr_stmt|;
block|}
else|else
block|{
name|passwordField
operator|.
name|setText
argument_list|(
name|credentials
operator|.
name|getPasswd
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|passfile
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|gridwidth
operator|=
literal|2
expr_stmt|;
name|add
argument_list|(
name|rememberDataCB
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|c
operator|.
name|gridy
operator|++
expr_stmt|;
block|}
name|c
operator|.
name|gridwidth
operator|=
literal|2
expr_stmt|;
name|add
argument_list|(
operator|new
name|JLabel
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
comment|// spacer
block|}
block|}
specifier|public
specifier|static
name|Credentials
name|promptCredentials
parameter_list|(
name|Credentials
name|c
parameter_list|,
name|File
name|passfile
parameter_list|)
block|{
name|c
operator|=
name|loadPassfile
argument_list|(
name|c
argument_list|,
name|passfile
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getUserName
argument_list|()
operator|!=
literal|null
operator|&&
name|c
operator|.
name|getPasswd
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
name|CredentialPanel
name|credentialPanel
init|=
operator|new
name|CredentialPanel
argument_list|(
name|c
argument_list|,
name|passfile
argument_list|)
decl_stmt|;
if|if
condition|(
name|JOptionPane
operator|.
name|showOptionDialog
argument_list|(
literal|null
argument_list|,
name|credentialPanel
argument_list|,
name|c
operator|.
name|getHost
argument_list|()
operator|+
literal|" credentials"
argument_list|,
name|JOptionPane
operator|.
name|OK_CANCEL_OPTION
argument_list|,
literal|0
argument_list|,
operator|new
name|ImageIcon
argument_list|(
name|Ivy
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"logo.png"
argument_list|)
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|Integer
argument_list|(
name|JOptionPane
operator|.
name|OK_OPTION
argument_list|)
argument_list|)
operator|==
name|JOptionPane
operator|.
name|OK_OPTION
condition|)
block|{
name|String
name|username
init|=
name|credentialPanel
operator|.
name|userNameField
operator|.
name|getText
argument_list|()
decl_stmt|;
name|String
name|passwd
init|=
name|credentialPanel
operator|.
name|passwordField
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|credentialPanel
operator|.
name|rememberDataCB
operator|.
name|isSelected
argument_list|()
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|EncrytedProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"passwd"
argument_list|,
name|passwd
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|passfile
argument_list|)
expr_stmt|;
name|props
operator|.
name|store
argument_list|(
name|fos
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"error occurred while saving password file "
operator|+
name|passfile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fos
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignored
block|}
block|}
block|}
block|}
name|c
operator|=
operator|new
name|Credentials
argument_list|(
name|c
operator|.
name|getRealm
argument_list|()
argument_list|,
name|c
operator|.
name|getHost
argument_list|()
argument_list|,
name|username
argument_list|,
name|passwd
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|public
specifier|static
name|Credentials
name|loadPassfile
parameter_list|(
name|Credentials
name|c
parameter_list|,
name|File
name|passfile
parameter_list|)
block|{
if|if
condition|(
name|passfile
operator|!=
literal|null
operator|&&
name|passfile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|EncrytedProperties
argument_list|()
decl_stmt|;
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|passfile
argument_list|)
expr_stmt|;
name|props
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
name|String
name|username
init|=
name|c
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|String
name|passwd
init|=
name|c
operator|.
name|getPasswd
argument_list|()
decl_stmt|;
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|passwd
operator|==
literal|null
condition|)
block|{
name|passwd
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"passwd"
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Credentials
argument_list|(
name|c
operator|.
name|getRealm
argument_list|()
argument_list|,
name|c
operator|.
name|getHost
argument_list|()
argument_list|,
name|username
argument_list|,
name|passwd
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"error occurred while loading password file "
operator|+
name|passfile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fis
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignored
block|}
block|}
block|}
block|}
return|return
name|c
return|;
block|}
specifier|private
name|CredentialsUtil
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

