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
name|FileInputStream
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|Ivy
import|;
end_import

begin_comment
comment|/**  * This task let user set ivy variables from ant.  *   * @author Xavier Hanin  */
end_comment

begin_class
specifier|public
class|class
name|IvyVar
extends|extends
name|IvyTask
block|{
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|String
name|_value
decl_stmt|;
specifier|private
name|File
name|_file
decl_stmt|;
specifier|private
name|String
name|_url
decl_stmt|;
specifier|private
name|String
name|_prefix
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
name|file
parameter_list|)
block|{
name|_file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|_prefix
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|_prefix
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|String
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
block|{
name|_url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|_value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|_value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|BuildException
block|{
name|Ivy
name|ivy
init|=
name|getIvyInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|setVariable
argument_list|(
name|getVarName
argument_list|(
name|getName
argument_list|()
argument_list|)
argument_list|,
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|getUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
operator|new
name|URL
argument_list|(
name|getUrl
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
literal|"specify either name or file or url to ivy var task"
argument_list|)
throw|;
block|}
name|props
operator|.
name|load
argument_list|(
name|is
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
literal|"impossible to load variables from file: "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
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
try|try
block|{
name|is
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
block|}
block|}
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|props
operator|.
name|keySet
argument_list|()
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
name|String
name|name
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|ivy
operator|.
name|setVariable
argument_list|(
name|getVarName
argument_list|(
name|name
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getVarName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|prefix
init|=
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
name|prefix
operator|+
name|name
return|;
block|}
else|else
block|{
return|return
name|prefix
operator|+
literal|"."
operator|+
name|name
return|;
block|}
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

