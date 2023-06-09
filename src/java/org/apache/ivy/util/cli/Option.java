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
name|util
operator|.
name|cli
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_class
specifier|public
class|class
name|Option
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
index|[]
name|args
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|boolean
name|required
decl_stmt|;
specifier|private
name|boolean
name|countArgs
decl_stmt|;
specifier|private
name|boolean
name|deprecated
decl_stmt|;
name|Option
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|String
name|description
parameter_list|,
name|boolean
name|required
parameter_list|,
name|boolean
name|countArgs
parameter_list|,
name|boolean
name|deprecated
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
name|this
operator|.
name|countArgs
operator|=
name|countArgs
expr_stmt|;
name|this
operator|.
name|deprecated
operator|=
name|deprecated
expr_stmt|;
if|if
condition|(
name|required
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"required option not supported yet"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
index|[]
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|boolean
name|isRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
specifier|public
name|boolean
name|isCountArgs
parameter_list|()
block|{
return|return
name|countArgs
return|;
block|}
specifier|public
name|boolean
name|isDeprecated
parameter_list|()
block|{
return|return
name|deprecated
return|;
block|}
name|String
index|[]
name|parse
parameter_list|(
name|ListIterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|isCountArgs
argument_list|()
condition|)
block|{
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[
name|args
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|missingArgument
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|values
index|[
name|i
index|]
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|values
index|[
name|i
index|]
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|missingArgument
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|String
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|previous
argument_list|()
expr_stmt|;
break|break;
block|}
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|values
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
specifier|private
name|void
name|missingArgument
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"no argument for: "
operator|+
name|name
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"missing argument for: "
operator|+
name|name
operator|+
literal|". Expected: "
operator|+
name|getArgsSpec
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getSpec
parameter_list|()
block|{
return|return
literal|"-"
operator|+
name|name
operator|+
literal|" "
operator|+
name|getArgsSpec
argument_list|()
return|;
block|}
specifier|private
name|String
name|getArgsSpec
parameter_list|()
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|arg
argument_list|)
operator|.
name|append
argument_list|(
literal|"> "
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

