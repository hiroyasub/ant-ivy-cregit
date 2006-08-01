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
name|util
package|;
end_package

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
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author x.hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|Message
block|{
comment|// messages level copied from ant project, to avoid dependency on ant
comment|/** Message priority of "error". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_ERR
init|=
literal|0
decl_stmt|;
comment|/** Message priority of "warning". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_WARN
init|=
literal|1
decl_stmt|;
comment|/** Message priority of "information". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_INFO
init|=
literal|2
decl_stmt|;
comment|/** Message priority of "verbose". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_VERBOSE
init|=
literal|3
decl_stmt|;
comment|/** Message priority of "debug". */
specifier|public
specifier|static
specifier|final
name|int
name|MSG_DEBUG
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
name|MessageImpl
name|_impl
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
name|StringBuffer
name|_problems
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|_showProgress
init|=
literal|true
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|_showedInfo
init|=
literal|false
decl_stmt|;
specifier|public
specifier|static
name|void
name|init
parameter_list|(
name|MessageImpl
name|impl
parameter_list|)
block|{
name|_impl
operator|=
name|impl
expr_stmt|;
name|showInfo
argument_list|()
expr_stmt|;
block|}
comment|/**       * same as init, but without displaying info      * @param impl      */
specifier|public
specifier|static
name|void
name|setImpl
parameter_list|(
name|MessageImpl
name|impl
parameter_list|)
block|{
name|_impl
operator|=
name|impl
expr_stmt|;
block|}
specifier|public
specifier|static
name|MessageImpl
name|getImpl
parameter_list|()
block|{
return|return
name|_impl
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isInitialised
parameter_list|()
block|{
return|return
name|_impl
operator|!=
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|showInfo
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_showedInfo
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|module
init|=
name|Message
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/module.properties"
argument_list|)
decl_stmt|;
if|if
condition|(
name|module
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|props
operator|.
name|load
argument_list|(
name|module
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|":: Ivy "
operator|+
name|props
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|)
operator|+
literal|" - "
operator|+
name|props
operator|.
name|getProperty
argument_list|(
literal|"date"
argument_list|)
operator|+
literal|" :: http://ivy.jayasoft.org/ ::"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|info
argument_list|(
literal|":: Ivy non official version :: http://ivy.jayasoft.org/ ::"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|info
argument_list|(
literal|":: Ivy non official version :: http://ivy.jayasoft.org/ ::"
argument_list|)
expr_stmt|;
block|}
name|_showedInfo
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|debug
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_DEBUG
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|verbose
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|info
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|MSG_INFO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|warn
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|log
argument_list|(
literal|"WARN: "
operator|+
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|_problems
operator|.
name|append
argument_list|(
literal|"\tWARN:  "
operator|+
name|msg
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|error
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
comment|// log in verbose mode because message is appended as a problem, and will be
comment|// logged at the end at error level
name|_impl
operator|.
name|log
argument_list|(
literal|"ERROR: "
operator|+
name|msg
argument_list|,
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|_problems
operator|.
name|append
argument_list|(
literal|"\tERROR: "
operator|+
name|msg
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|sumupProblems
parameter_list|()
block|{
if|if
condition|(
name|_problems
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|info
argument_list|(
literal|"\n:: problems summary ::"
argument_list|)
expr_stmt|;
name|info
argument_list|(
name|_problems
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|info
argument_list|(
literal|"\t--- USE VERBOSE OR DEBUG MESSAGE LEVEL FOR MORE DETAILS ---"
argument_list|)
expr_stmt|;
name|_problems
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|progress
parameter_list|()
block|{
if|if
condition|(
name|_showProgress
condition|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|progress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|endProgress
parameter_list|()
block|{
name|endProgress
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|endProgress
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|_showProgress
condition|)
block|{
if|if
condition|(
name|_impl
operator|!=
literal|null
condition|)
block|{
name|_impl
operator|.
name|endProgress
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isShowProgress
parameter_list|()
block|{
return|return
name|_showProgress
return|;
block|}
specifier|public
specifier|static
name|void
name|setShowProgress
parameter_list|(
name|boolean
name|progress
parameter_list|)
block|{
name|_showProgress
operator|=
name|progress
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|uninit
parameter_list|()
block|{
name|_impl
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

