begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|plugins
operator|.
name|resolver
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|core
operator|.
name|IvyPatternHelper
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
name|core
operator|.
name|module
operator|.
name|descriptor
operator|.
name|Artifact
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
name|core
operator|.
name|module
operator|.
name|id
operator|.
name|ModuleRevisionId
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
name|core
operator|.
name|settings
operator|.
name|IvyPattern
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
name|plugins
operator|.
name|repository
operator|.
name|file
operator|.
name|FileRepository
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
name|util
operator|.
name|Checks
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
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|FileSystemResolver
extends|extends
name|RepositoryResolver
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TRANSACTION_DESTINATION_SUFFIX
init|=
literal|".part"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|TRANSACTION_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(.*[/\\\\]\\[revision\\])([/\\\\].+)"
argument_list|)
decl_stmt|;
comment|/**      * Transactional mode.      *      * auto: use transaction if possible, only log verbose message if not true: always use      * transaction, fail if not supported false: never use transactions      */
specifier|private
name|String
name|transactional
init|=
literal|"auto"
decl_stmt|;
comment|// one of 'auto', 'true' or 'false'
comment|/**      * When set indicates if this resolver supports transaction      */
specifier|private
name|Boolean
name|supportTransaction
decl_stmt|;
comment|/**      * The pattern leading to the directory where files are published before being moved at the end      * of a transaction      */
specifier|private
name|String
name|baseTransactionPattern
decl_stmt|;
comment|/**      * Map between actual patterns and patterns used during the transaction to put files in a      * temporary directory      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fullTransactionPatterns
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Location where files are published during the transaction      */
specifier|private
name|File
name|transactionTempDir
decl_stmt|;
comment|/**      * Location where files should end up at the end of the transaction      */
specifier|private
name|File
name|transactionDestDir
decl_stmt|;
specifier|public
name|FileSystemResolver
parameter_list|()
block|{
name|setRepository
argument_list|(
operator|new
name|FileRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"file"
return|;
block|}
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
name|getFileRepository
argument_list|()
operator|.
name|isLocal
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLocal
parameter_list|(
name|boolean
name|local
parameter_list|)
block|{
name|getFileRepository
argument_list|()
operator|.
name|setLocal
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
specifier|private
name|FileRepository
name|getFileRepository
parameter_list|()
block|{
return|return
operator|(
name|FileRepository
operator|)
name|getRepository
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getDestination
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
if|if
condition|(
name|supportTransaction
argument_list|()
operator|&&
name|isTransactionStarted
argument_list|()
condition|)
block|{
name|String
name|destPattern
init|=
name|fullTransactionPatterns
operator|.
name|get
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
if|if
condition|(
name|destPattern
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unsupported pattern for publish destination pattern: "
operator|+
name|pattern
operator|+
literal|". supported patterns: "
operator|+
name|fullTransactionPatterns
operator|.
name|keySet
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|destPattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getDestination
argument_list|(
name|pattern
argument_list|,
name|artifact
argument_list|,
name|mrid
argument_list|)
return|;
block|}
block|}
specifier|private
name|boolean
name|isTransactionStarted
parameter_list|()
block|{
return|return
name|transactionTempDir
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|abortPublishTransaction
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|supportTransaction
argument_list|()
condition|)
block|{
if|if
condition|(
name|isTransactionStarted
argument_list|()
condition|)
block|{
try|try
block|{
name|getFileRepository
argument_list|()
operator|.
name|delete
argument_list|(
name|transactionTempDir
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\tpublish aborted: deleted "
operator|+
name|transactionTempDir
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeTransaction
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"\tpublish aborted: nothing was started"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|commitPublishTransaction
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|supportTransaction
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|isTransactionStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no current transaction!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|transactionDestDir
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"impossible to commit transaction: transaction destination directory "
operator|+
literal|"already exists: "
operator|+
name|transactionDestDir
operator|+
literal|"\npossible cause: usage of identifying tokens after the revision token"
argument_list|)
throw|;
block|}
try|try
block|{
name|getFileRepository
argument_list|()
operator|.
name|move
argument_list|(
name|transactionTempDir
argument_list|,
name|transactionDestDir
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\tpublish committed: moved "
operator|+
name|transactionTempDir
operator|+
literal|" \n\t\tto "
operator|+
name|transactionDestDir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|String
name|message
decl_stmt|;
try|try
block|{
name|getFileRepository
argument_list|()
operator|.
name|delete
argument_list|(
name|transactionTempDir
argument_list|)
expr_stmt|;
name|message
operator|=
literal|"publish transaction commit error for "
operator|+
name|transactionDestDir
operator|+
literal|": rolled back"
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|deleteEx
parameter_list|)
block|{
name|message
operator|=
literal|"publish transaction commit error for "
operator|+
name|transactionDestDir
operator|+
literal|": rollback impossible either, "
operator|+
literal|"please remove "
operator|+
name|transactionTempDir
operator|+
literal|" manually"
expr_stmt|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginPublishTransaction
parameter_list|(
name|ModuleRevisionId
name|module
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|supportTransaction
argument_list|()
condition|)
block|{
if|if
condition|(
name|isTransactionStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"a transaction is already started and not closed!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|overwrite
condition|)
block|{
name|unsupportedTransaction
argument_list|(
literal|"overwrite transaction not supported yet"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|initTransaction
argument_list|(
name|module
argument_list|)
expr_stmt|;
if|if
condition|(
name|transactionDestDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|unsupportedTransaction
argument_list|(
literal|"transaction destination directory already exists: "
operator|+
name|transactionDestDir
operator|+
literal|"\npossible cause: usage of identifying tokens after the revision token"
argument_list|)
expr_stmt|;
name|closeTransaction
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\tstarting transaction: publish during transaction will be done in \n\t\t"
operator|+
name|transactionTempDir
operator|+
literal|"\n\tand on commit moved to \n\t\t"
operator|+
name|transactionDestDir
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|filterNames
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|supportTransaction
argument_list|()
condition|)
block|{
name|values
operator|=
name|super
operator|.
name|filterNames
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|values
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|endsWith
argument_list|(
name|TRANSACTION_DESTINATION_SUFFIX
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|filterNames
argument_list|(
name|values
argument_list|)
return|;
block|}
block|}
specifier|public
name|boolean
name|supportTransaction
parameter_list|()
block|{
if|if
condition|(
literal|"false"
operator|.
name|equals
argument_list|(
name|transactional
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|checkSupportTransaction
argument_list|()
expr_stmt|;
return|return
name|supportTransaction
return|;
block|}
specifier|private
name|void
name|closeTransaction
parameter_list|()
block|{
name|transactionTempDir
operator|=
literal|null
expr_stmt|;
name|transactionDestDir
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|void
name|checkSupportTransaction
parameter_list|()
block|{
if|if
condition|(
name|supportTransaction
operator|==
literal|null
condition|)
block|{
name|supportTransaction
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ivyPatterns
init|=
name|getIvyPatterns
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|artifactPatterns
init|=
name|getArtifactPatterns
argument_list|()
decl_stmt|;
if|if
condition|(
name|ivyPatterns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|pattern
init|=
name|ivyPatterns
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|TRANSACTION_PATTERN
operator|.
name|matcher
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|unsupportedTransaction
argument_list|(
literal|"ivy pattern does not use revision as a directory"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|baseTransactionPattern
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fullTransactionPatterns
operator|.
name|put
argument_list|(
name|pattern
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|+
name|TRANSACTION_DESTINATION_SUFFIX
operator|+
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|artifactPatterns
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
name|pattern
init|=
name|artifactPatterns
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|TRANSACTION_PATTERN
operator|.
name|matcher
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|unsupportedTransaction
argument_list|(
literal|"artifact pattern does not use revision as a directory"
argument_list|)
expr_stmt|;
return|return;
block|}
if|else if
condition|(
name|baseTransactionPattern
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|baseTransactionPattern
operator|.
name|equals
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|unsupportedTransaction
argument_list|(
literal|"ivy pattern and artifact pattern "
operator|+
literal|"do not use the same directory for revision"
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|fullTransactionPatterns
operator|.
name|put
argument_list|(
name|pattern
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|+
name|TRANSACTION_DESTINATION_SUFFIX
operator|+
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|baseTransactionPattern
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fullTransactionPatterns
operator|.
name|put
argument_list|(
name|pattern
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|+
name|TRANSACTION_DESTINATION_SUFFIX
operator|+
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|supportTransaction
operator|=
name|Boolean
operator|.
name|TRUE
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|unsupportedTransaction
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|String
name|fullMsg
init|=
name|getName
argument_list|()
operator|+
literal|" do not support transaction. "
operator|+
name|msg
decl_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|transactional
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|fullMsg
operator|+
literal|". Set transactional attribute to 'auto' or 'false' or fix the problem."
argument_list|)
throw|;
block|}
else|else
block|{
name|Message
operator|.
name|verbose
argument_list|(
name|fullMsg
argument_list|)
expr_stmt|;
name|supportTransaction
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initTransaction
parameter_list|(
name|ModuleRevisionId
name|module
parameter_list|)
block|{
name|ModuleRevisionId
name|mrid
init|=
name|module
decl_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|mrid
operator|=
name|convertM2IdForResourceSearch
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
name|transactionTempDir
operator|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|baseTransactionPattern
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
operator|+
name|TRANSACTION_DESTINATION_SUFFIX
argument_list|)
argument_list|)
argument_list|,
literal|"baseTransactionPattern"
argument_list|)
expr_stmt|;
name|transactionDestDir
operator|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|baseTransactionPattern
argument_list|,
name|mrid
argument_list|)
argument_list|,
literal|"baseTransactionPattern"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTransactional
parameter_list|()
block|{
return|return
name|transactional
return|;
block|}
specifier|public
name|void
name|setTransactional
parameter_list|(
name|String
name|transactional
parameter_list|)
block|{
name|this
operator|.
name|transactional
operator|=
name|transactional
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addConfiguredIvy
parameter_list|(
name|IvyPattern
name|p
parameter_list|)
block|{
name|File
name|file
init|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|p
operator|.
name|getPattern
argument_list|()
argument_list|,
literal|"ivy pattern"
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPattern
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|addConfiguredIvy
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addIvyPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|File
name|file
init|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|pattern
argument_list|,
literal|"ivy pattern"
argument_list|)
decl_stmt|;
name|super
operator|.
name|addIvyPattern
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addConfiguredArtifact
parameter_list|(
name|IvyPattern
name|p
parameter_list|)
block|{
name|File
name|file
init|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|p
operator|.
name|getPattern
argument_list|()
argument_list|,
literal|"artifact pattern"
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPattern
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|addConfiguredArtifact
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addArtifactPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|File
name|file
init|=
name|Checks
operator|.
name|checkAbsolute
argument_list|(
name|pattern
argument_list|,
literal|"artifact pattern"
argument_list|)
decl_stmt|;
name|super
operator|.
name|addArtifactPattern
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

