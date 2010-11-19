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
name|osgi
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
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Provides a bundle name conversion utility.  */
end_comment

begin_class
specifier|public
class|class
name|NameUtil
block|{
specifier|private
specifier|static
specifier|final
name|NameUtil
name|instance
init|=
operator|new
name|NameUtil
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|NameUtil
name|instance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
specifier|final
name|Set
comment|/*<String> */
name|tlds
init|=
operator|new
name|HashSet
comment|/*<String> */
argument_list|()
decl_stmt|;
specifier|private
name|NameUtil
parameter_list|()
block|{
specifier|final
name|InputStream
name|inputStream
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"orgs.list"
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to find required file in classpath: orgs.list"
argument_list|)
throw|;
block|}
specifier|final
name|BufferedReader
name|bis
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|bis
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|tlds
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|OrgAndName
name|asOrgAndName
parameter_list|(
name|String
name|qname
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|tokens
init|=
name|qname
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|tokens
operator|==
literal|null
operator|)
operator|||
operator|(
name|tokens
operator|.
name|length
operator|==
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Qualified name is empty or invalid: "
operator|+
name|qname
argument_list|)
throw|;
block|}
name|String
name|org
init|=
literal|null
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tlds
operator|.
name|contains
argument_list|(
name|tokens
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|org
operator|=
name|append
argument_list|(
name|tokens
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|name
operator|=
name|append
argument_list|(
name|tokens
argument_list|,
literal|2
argument_list|,
name|tokens
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|tokens
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|org
operator|=
name|tokens
index|[
literal|0
index|]
expr_stmt|;
name|name
operator|=
name|tokens
index|[
literal|0
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|tokens
operator|.
name|length
operator|>=
literal|2
condition|)
block|{
name|org
operator|=
name|tokens
index|[
literal|0
index|]
expr_stmt|;
name|name
operator|=
name|append
argument_list|(
name|tokens
argument_list|,
literal|1
argument_list|,
name|tokens
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|org
operator|==
literal|null
operator|||
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Null org/name: org="
operator|+
name|org
operator|+
literal|", name="
operator|+
name|name
operator|+
literal|", qname="
operator|+
name|qname
argument_list|)
throw|;
block|}
return|return
operator|new
name|OrgAndName
argument_list|(
name|org
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|private
name|String
name|append
parameter_list|(
name|String
index|[]
name|strs
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
specifier|final
name|StringBuffer
name|sbuf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|boolean
name|dot
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<=
name|end
operator|&&
name|i
operator|<
name|strs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|dot
condition|)
block|{
name|sbuf
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sbuf
operator|.
name|append
argument_list|(
name|strs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|dot
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|sbuf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|OrgAndName
block|{
specifier|public
specifier|final
name|String
name|org
decl_stmt|;
specifier|public
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
name|OrgAndName
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|org
operator|=
name|org
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

