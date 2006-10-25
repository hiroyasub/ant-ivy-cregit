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
name|ant
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
name|MalformedURLException
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Property
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
name|Ivy
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
name|url
operator|.
name|CredentialsStore
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
name|url
operator|.
name|URLHandler
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
name|url
operator|.
name|URLHandlerDispatcher
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
name|url
operator|.
name|URLHandlerRegistry
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
comment|/**  * Configure Ivy with an ivyconf.xml file  *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|IvyConfigure
extends|extends
name|IvyTask
block|{
specifier|public
specifier|static
class|class
name|Credentials
block|{
specifier|private
name|String
name|_realm
decl_stmt|;
specifier|private
name|String
name|_host
decl_stmt|;
specifier|private
name|String
name|_username
decl_stmt|;
specifier|private
name|String
name|_passwd
decl_stmt|;
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
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
name|getRealm
parameter_list|()
block|{
return|return
name|_realm
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|_realm
operator|=
name|format
argument_list|(
name|realm
argument_list|)
expr_stmt|;
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
name|format
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|_username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|_username
operator|=
name|format
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|File
name|_file
init|=
literal|null
decl_stmt|;
specifier|private
name|URL
name|_url
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_realm
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_host
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_userName
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|_passwd
init|=
literal|null
decl_stmt|;
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|_file
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|conf
parameter_list|)
block|{
name|_file
operator|=
name|conf
expr_stmt|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|_url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|_url
operator|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPasswd
parameter_list|()
block|{
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
name|getRealm
parameter_list|()
block|{
return|return
name|_realm
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|_realm
operator|=
name|format
argument_list|(
name|realm
argument_list|)
expr_stmt|;
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
name|format
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|_userName
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|_userName
operator|=
name|format
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|format
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|==
literal|null
condition|?
name|str
else|:
operator|(
name|str
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|str
operator|.
name|trim
argument_list|()
operator|)
return|;
block|}
specifier|public
name|void
name|addConfiguredCredentials
parameter_list|(
name|Credentials
name|c
parameter_list|)
block|{
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|addCredentials
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
name|c
operator|.
name|getUsername
argument_list|()
argument_list|,
name|c
operator|.
name|getPasswd
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
try|try
block|{
name|loadDefaultProperties
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to load ivy default properties file: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|ensureMessageInitialised
argument_list|()
expr_stmt|;
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
try|try
block|{
name|configureURLHandler
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|addAllVariables
argument_list|(
name|getProject
argument_list|()
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|_file
operator|==
literal|null
operator|&&
name|_url
operator|==
literal|null
condition|)
block|{
name|_file
operator|=
operator|new
name|File
argument_list|(
name|getProject
argument_list|()
operator|.
name|getBaseDir
argument_list|()
argument_list|,
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.conf.file"
argument_list|)
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"searching ivyconf file: trying "
operator|+
name|_file
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|_file
operator|=
operator|new
name|File
argument_list|(
name|getProject
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"ivy.conf.file"
argument_list|)
argument_list|)
expr_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"searching ivyconf file: trying "
operator|+
name|_file
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|_file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"no configuration file found, using default..."
argument_list|)
expr_stmt|;
name|_file
operator|=
literal|null
expr_stmt|;
name|_url
operator|=
name|Ivy
operator|.
name|getDefaultConfigurationURL
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|_file
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|_file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"configuration file does not exist: "
operator|+
name|_file
argument_list|)
throw|;
block|}
else|else
block|{
name|ivy
operator|.
name|configure
argument_list|(
name|_file
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ivy
operator|.
name|configure
argument_list|(
name|_url
argument_list|)
expr_stmt|;
block|}
name|setIvyInstance
argument_list|(
name|ivy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"impossible to configure ivy with given "
operator|+
operator|(
name|_file
operator|!=
literal|null
condition|?
literal|"file: "
operator|+
name|_file
else|:
literal|"url :"
operator|+
name|_url
operator|)
operator|+
literal|" :"
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|loadDefaultProperties
parameter_list|()
block|{
name|Property
name|prop
init|=
operator|new
name|Property
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|URL
name|url
init|=
name|Ivy
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivy.properties"
argument_list|)
decl_stmt|;
comment|// this is copy of loadURL code from ant Property task  (not available in 1.5.1)
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|Message
operator|.
name|verbose
argument_list|(
literal|"Loading "
operator|+
name|url
argument_list|)
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|addProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|ex
argument_list|,
name|getLocation
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
name|prop
operator|.
name|setProject
argument_list|(
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|configureURLHandler
parameter_list|()
block|{
name|CredentialsStore
operator|.
name|INSTANCE
operator|.
name|addCredentials
argument_list|(
name|getRealm
argument_list|()
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getUsername
argument_list|()
argument_list|,
name|getPasswd
argument_list|()
argument_list|)
expr_stmt|;
name|URLHandlerDispatcher
name|dispatcher
init|=
operator|new
name|URLHandlerDispatcher
argument_list|()
decl_stmt|;
name|URLHandler
name|httpHandler
init|=
name|URLHandlerRegistry
operator|.
name|getHttp
argument_list|()
decl_stmt|;
name|dispatcher
operator|.
name|setDownloader
argument_list|(
literal|"http"
argument_list|,
name|httpHandler
argument_list|)
expr_stmt|;
name|dispatcher
operator|.
name|setDownloader
argument_list|(
literal|"https"
argument_list|,
name|httpHandler
argument_list|)
expr_stmt|;
name|URLHandlerRegistry
operator|.
name|setDefault
argument_list|(
name|dispatcher
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

