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
name|osgi
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * Provides OSGi version support.  */
end_comment

begin_class
specifier|public
class|class
name|Version
implements|implements
name|Comparable
argument_list|<
name|Version
argument_list|>
block|{
specifier|private
name|int
name|major
decl_stmt|;
specifier|private
name|int
name|minor
decl_stmt|;
specifier|private
name|int
name|patch
decl_stmt|;
specifier|private
name|String
name|qualifier
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|input
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|split
init|=
literal|false
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|toString
init|=
literal|false
decl_stmt|;
specifier|public
name|Version
parameter_list|(
name|String
name|versionStr
parameter_list|,
name|String
name|qualifier
parameter_list|)
block|{
name|this
argument_list|(
name|qualifier
operator|==
literal|null
condition|?
name|versionStr
else|:
operator|(
name|versionStr
operator|+
literal|"."
operator|+
name|qualifier
operator|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Version
parameter_list|(
name|String
name|versionStr
parameter_list|)
block|{
name|this
operator|.
name|input
operator|=
name|versionStr
expr_stmt|;
name|split
operator|=
literal|false
expr_stmt|;
name|toString
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|Version
parameter_list|(
name|int
name|major
parameter_list|,
name|int
name|minor
parameter_list|,
name|int
name|patch
parameter_list|,
name|String
name|qualifier
parameter_list|)
block|{
name|this
operator|.
name|major
operator|=
name|major
expr_stmt|;
name|this
operator|.
name|minor
operator|=
name|minor
expr_stmt|;
name|this
operator|.
name|patch
operator|=
name|patch
expr_stmt|;
name|this
operator|.
name|qualifier
operator|=
name|qualifier
expr_stmt|;
name|split
operator|=
literal|true
expr_stmt|;
name|toString
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Build a version from another one while appending an extra qualifier      *      * @param baseVersion      *            Version      * @param extraQualifier      *            String      */
specifier|public
name|Version
parameter_list|(
name|Version
name|baseVersion
parameter_list|,
name|String
name|extraQualifier
parameter_list|)
block|{
name|this
operator|.
name|major
operator|=
name|baseVersion
operator|.
name|major
expr_stmt|;
name|this
operator|.
name|minor
operator|=
name|baseVersion
operator|.
name|minor
expr_stmt|;
name|this
operator|.
name|patch
operator|=
name|baseVersion
operator|.
name|patch
expr_stmt|;
name|this
operator|.
name|qualifier
operator|=
operator|(
name|baseVersion
operator|.
name|qualifier
operator|==
literal|null
operator|)
condition|?
name|extraQualifier
else|:
operator|(
name|baseVersion
operator|.
name|qualifier
operator|+
name|extraQualifier
operator|)
expr_stmt|;
name|split
operator|=
literal|true
expr_stmt|;
name|toString
operator|=
literal|false
expr_stmt|;
block|}
specifier|private
name|void
name|ensureSplit
parameter_list|()
block|{
if|if
condition|(
operator|!
name|split
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|split
condition|)
block|{
return|return;
block|}
name|String
index|[]
name|splits
init|=
name|input
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
if|if
condition|(
name|splits
operator|==
literal|null
operator|||
name|splits
operator|.
name|length
operator|==
literal|0
operator|||
name|splits
operator|.
name|length
operator|>
literal|4
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
operator|new
name|ParseException
argument_list|(
literal|"Ill-formed OSGi version"
argument_list|,
literal|0
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|major
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|splits
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
operator|new
name|ParseException
argument_list|(
literal|"Major part of an OSGi version should be an integer"
argument_list|,
literal|0
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|minor
operator|=
name|splits
operator|.
name|length
operator|>=
literal|2
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|splits
index|[
literal|1
index|]
argument_list|)
else|:
literal|0
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
operator|new
name|ParseException
argument_list|(
literal|"Minor part of an OSGi version should be an integer"
argument_list|,
literal|0
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|patch
operator|=
name|splits
operator|.
name|length
operator|>=
literal|3
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|splits
index|[
literal|2
index|]
argument_list|)
else|:
literal|0
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
operator|new
name|ParseException
argument_list|(
literal|"Patch part of an OSGi version should be an integer"
argument_list|,
literal|0
argument_list|)
argument_list|)
throw|;
block|}
name|qualifier
operator|=
name|splits
operator|.
name|length
operator|==
literal|4
condition|?
name|splits
index|[
literal|3
index|]
else|:
literal|null
expr_stmt|;
name|split
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|ensureToString
parameter_list|()
block|{
if|if
condition|(
operator|!
name|toString
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|toString
condition|)
block|{
return|return;
block|}
name|ensureSplit
argument_list|()
expr_stmt|;
name|version
operator|=
name|major
operator|+
literal|"."
operator|+
name|minor
operator|+
literal|"."
operator|+
name|patch
operator|+
operator|(
name|qualifier
operator|==
literal|null
condition|?
literal|""
else|:
literal|"."
operator|+
name|qualifier
operator|)
expr_stmt|;
name|toString
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|ensureToString
argument_list|()
expr_stmt|;
return|return
name|version
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|major
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|minor
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|patch
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|qualifier
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|qualifier
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
operator|||
operator|!
operator|(
name|obj
operator|instanceof
name|Version
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Version
name|other
init|=
operator|(
name|Version
operator|)
name|obj
decl_stmt|;
name|ensureSplit
argument_list|()
expr_stmt|;
name|other
operator|.
name|ensureSplit
argument_list|()
expr_stmt|;
return|return
name|major
operator|==
name|other
operator|.
name|major
operator|&&
name|minor
operator|==
name|other
operator|.
name|minor
operator|&&
name|patch
operator|==
name|other
operator|.
name|patch
operator|&&
operator|(
name|qualifier
operator|==
literal|null
condition|?
name|other
operator|.
name|qualifier
operator|==
literal|null
else|:
name|qualifier
operator|.
name|equals
argument_list|(
name|other
operator|.
name|qualifier
argument_list|)
operator|)
return|;
block|}
specifier|public
name|Version
name|withNudgedPatch
parameter_list|()
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
return|return
operator|new
name|Version
argument_list|(
name|major
argument_list|,
name|minor
argument_list|,
name|patch
operator|+
literal|1
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Version
name|withoutQualifier
parameter_list|()
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
return|return
operator|new
name|Version
argument_list|(
name|major
argument_list|,
name|minor
argument_list|,
name|patch
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|String
name|qualifier
parameter_list|()
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
return|return
operator|(
name|qualifier
operator|==
literal|null
operator|)
condition|?
literal|""
else|:
name|qualifier
return|;
block|}
specifier|public
name|int
name|compareUnqualified
parameter_list|(
name|Version
name|other
parameter_list|)
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
name|other
operator|.
name|ensureSplit
argument_list|()
expr_stmt|;
name|int
name|diff
init|=
name|major
operator|-
name|other
operator|.
name|major
decl_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
name|diff
operator|=
name|minor
operator|-
name|other
operator|.
name|minor
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
name|diff
operator|=
name|patch
operator|-
name|other
operator|.
name|patch
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|Version
name|other
parameter_list|)
block|{
name|ensureSplit
argument_list|()
expr_stmt|;
name|other
operator|.
name|ensureSplit
argument_list|()
expr_stmt|;
name|int
name|diff
init|=
name|compareUnqualified
argument_list|(
name|other
argument_list|)
decl_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
if|if
condition|(
name|qualifier
operator|==
literal|null
condition|)
block|{
return|return
name|other
operator|.
name|qualifier
operator|!=
literal|null
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|if
condition|(
name|other
operator|.
name|qualifier
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|qualifier
operator|.
name|compareTo
argument_list|(
name|other
operator|.
name|qualifier
argument_list|)
return|;
block|}
block|}
end_class

end_unit

