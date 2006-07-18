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
name|url
package|;
end_package

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
name|url
operator|.
name|URLHandler
operator|.
name|URLInfo
import|;
end_import

begin_class
specifier|public
class|class
name|URLResource
implements|implements
name|Resource
block|{
specifier|private
name|URL
name|_url
decl_stmt|;
specifier|private
name|boolean
name|_init
init|=
literal|false
decl_stmt|;
specifier|private
name|long
name|_lastModified
decl_stmt|;
specifier|private
name|long
name|_contentLength
decl_stmt|;
specifier|private
name|boolean
name|_exists
decl_stmt|;
specifier|public
name|URLResource
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|_url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_url
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
specifier|public
name|Resource
name|clone
parameter_list|(
name|String
name|cloneName
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URLResource
argument_list|(
operator|new
name|URL
argument_list|(
name|cloneName
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"bad clone name provided: not suitable for an URLResource: "
operator|+
name|cloneName
argument_list|)
throw|;
block|}
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|_lastModified
return|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|URLInfo
name|info
init|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
operator|.
name|getURLInfo
argument_list|(
name|_url
argument_list|)
decl_stmt|;
name|_contentLength
operator|=
name|info
operator|.
name|getContentLength
argument_list|()
expr_stmt|;
name|_lastModified
operator|=
name|info
operator|.
name|getLastModified
argument_list|()
expr_stmt|;
name|_exists
operator|=
name|info
operator|.
name|isReachable
argument_list|()
expr_stmt|;
name|_init
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|_contentLength
return|;
block|}
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
return|return
name|_exists
return|;
block|}
specifier|public
name|URL
name|getURL
parameter_list|()
block|{
return|return
name|_url
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

