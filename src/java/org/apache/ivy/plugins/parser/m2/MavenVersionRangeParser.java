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
name|parser
operator|.
name|m2
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * Parser that understands Maven version ranges of the form {@code (,1.0]} and such.  * More details about such ranges in Maven, can be found  * {@link https://cwiki.apache.org/confluence/display/MAVENOLD/Dependency+Mediation+and+Conflict+Resolution#DependencyMediationandConflictResolution-DependencyVersionRanges here}  */
end_comment

begin_class
class|class
name|MavenVersionRangeParser
block|{
specifier|private
specifier|static
specifier|final
name|DeweyDecimal
name|javaVersion
decl_stmt|;
static|static
block|{
name|DeweyDecimal
name|v
init|=
literal|null
decl_stmt|;
try|try
block|{
name|v
operator|=
operator|new
name|DeweyDecimal
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|v
operator|=
literal|null
expr_stmt|;
block|}
name|javaVersion
operator|=
name|v
expr_stmt|;
block|}
comment|/**      * @param range The range to compare against      * @return Returns true if the current Java version, in which the instance of this class is running,      * is within the specified {@code range}. Else returns false.      */
specifier|static
name|boolean
name|currentJavaVersionInRange
parameter_list|(
specifier|final
name|String
name|range
parameter_list|)
block|{
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|javaVersion
operator|==
literal|null
condition|)
block|{
comment|// this will almost never be the case, but if we couldn't
comment|// determine the version of Java this system is running on,
comment|// then there's nothing we can do
return|return
literal|false
return|;
block|}
specifier|final
name|Range
name|parsedRange
init|=
name|parse
argument_list|(
name|range
argument_list|)
decl_stmt|;
return|return
name|parsedRange
operator|!=
literal|null
operator|&&
name|parsedRange
operator|.
name|accepts
argument_list|(
name|javaVersion
argument_list|)
return|;
block|}
comment|/**      * @param range The range to compare against      * @param value The value being compared      * @return Compares the {@code value} against the {@code range} and returns true if the {@code value}      * lies within the {@code range}. Else returns false.      */
specifier|static
name|boolean
name|rangeAccepts
parameter_list|(
specifier|final
name|String
name|range
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|DeweyDecimal
name|valToCompare
decl_stmt|;
try|try
block|{
name|valToCompare
operator|=
operator|new
name|DeweyDecimal
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|Range
name|parsedRange
init|=
name|parse
argument_list|(
name|range
argument_list|)
decl_stmt|;
return|return
name|parsedRange
operator|!=
literal|null
operator|&&
name|parsedRange
operator|.
name|accepts
argument_list|(
name|valToCompare
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Range
name|parse
parameter_list|(
specifier|final
name|String
name|rangeValue
parameter_list|)
block|{
if|if
condition|(
name|rangeValue
operator|==
literal|null
operator|||
name|rangeValue
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|// split the version by ","
specifier|final
name|String
index|[]
name|versionParts
init|=
name|rangeValue
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|versionParts
operator|.
name|length
operator|==
literal|1
condition|)
block|{
specifier|final
name|String
name|boundVal
init|=
name|versionParts
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
specifier|final
name|String
name|stripped
init|=
name|stripBoundChars
argument_list|(
name|boundVal
argument_list|)
decl_stmt|;
if|if
condition|(
name|stripped
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|DeweyDecimal
name|bound
init|=
operator|new
name|DeweyDecimal
argument_list|(
name|stripped
argument_list|)
decl_stmt|;
return|return
operator|new
name|BasicRange
argument_list|(
name|bound
argument_list|,
operator|!
name|boundVal
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
argument_list|,
name|bound
argument_list|,
operator|!
name|boundVal
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|versionParts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
specifier|final
name|String
name|lowerBoundVal
init|=
name|versionParts
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
specifier|final
name|String
name|strippedLowerBound
init|=
name|stripBoundChars
argument_list|(
name|lowerBoundVal
argument_list|)
decl_stmt|;
specifier|final
name|DeweyDecimal
name|lowerBound
decl_stmt|;
if|if
condition|(
name|strippedLowerBound
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|lowerBound
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|lowerBound
operator|=
operator|new
name|DeweyDecimal
argument_list|(
name|strippedLowerBound
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|upperBoundVal
init|=
name|versionParts
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
specifier|final
name|String
name|strippedUpperBound
init|=
name|stripBoundChars
argument_list|(
name|upperBoundVal
argument_list|)
decl_stmt|;
specifier|final
name|DeweyDecimal
name|upperBound
decl_stmt|;
if|if
condition|(
name|strippedUpperBound
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|upperBound
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|upperBound
operator|=
operator|new
name|DeweyDecimal
argument_list|(
name|strippedUpperBound
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|BasicRange
argument_list|(
name|lowerBound
argument_list|,
operator|!
name|lowerBoundVal
operator|.
name|startsWith
argument_list|(
literal|"("
argument_list|)
argument_list|,
name|upperBound
argument_list|,
operator|!
name|upperBoundVal
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|versionParts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
comment|// each range part can itself be a range, which is valid in maven
specifier|final
name|Collection
argument_list|<
name|Range
argument_list|>
name|ranges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|versionParts
operator|.
name|length
condition|;
name|i
operator|=
operator|(
name|i
operator|+
literal|2
operator|<
name|versionParts
operator|.
name|length
operator|)
condition|?
name|i
operator|+
literal|2
else|:
name|i
operator|+
literal|1
control|)
block|{
specifier|final
name|String
name|partOne
init|=
name|versionParts
index|[
name|i
index|]
decl_stmt|;
specifier|final
name|String
name|partTwo
decl_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|versionParts
operator|.
name|length
condition|)
block|{
name|partTwo
operator|=
name|versionParts
index|[
name|i
operator|+
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
name|partTwo
operator|=
literal|""
expr_stmt|;
block|}
specifier|final
name|Range
name|rangePart
init|=
name|parse
argument_list|(
name|partOne
operator|+
literal|","
operator|+
name|partTwo
argument_list|)
decl_stmt|;
if|if
condition|(
name|rangePart
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|ranges
operator|.
name|add
argument_list|(
name|rangePart
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ranges
operator|==
literal|null
operator|||
name|ranges
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|null
else|:
operator|new
name|MultiSetRange
argument_list|(
name|ranges
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|stripBoundChars
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|value
operator|.
name|replace
argument_list|(
literal|"("
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|")"
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"["
argument_list|,
literal|""
argument_list|)
operator|.
name|replace
argument_list|(
literal|"]"
argument_list|,
literal|""
argument_list|)
return|;
block|}
specifier|private
interface|interface
name|Range
block|{
name|boolean
name|accepts
parameter_list|(
specifier|final
name|DeweyDecimal
name|value
parameter_list|)
function_decl|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|BasicRange
implements|implements
name|Range
block|{
specifier|private
specifier|final
name|DeweyDecimal
name|lowerBound
decl_stmt|;
specifier|private
specifier|final
name|DeweyDecimal
name|upperBound
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|lowerInclusive
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|upperInclusive
decl_stmt|;
specifier|private
name|BasicRange
parameter_list|(
specifier|final
name|DeweyDecimal
name|lowerBound
parameter_list|,
specifier|final
name|boolean
name|lowerInclusive
parameter_list|,
specifier|final
name|DeweyDecimal
name|upperBound
parameter_list|,
specifier|final
name|boolean
name|upperInclusive
parameter_list|)
block|{
name|this
operator|.
name|lowerBound
operator|=
name|lowerBound
expr_stmt|;
name|this
operator|.
name|lowerInclusive
operator|=
name|lowerInclusive
expr_stmt|;
name|this
operator|.
name|upperBound
operator|=
name|upperBound
expr_stmt|;
name|this
operator|.
name|upperInclusive
operator|=
name|upperInclusive
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|accepts
parameter_list|(
specifier|final
name|DeweyDecimal
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
operator|&&
operator|(
name|this
operator|.
name|lowerBound
operator|==
literal|null
operator|||
operator|(
name|this
operator|.
name|lowerInclusive
condition|?
name|value
operator|.
name|isGreaterThanOrEqual
argument_list|(
name|lowerBound
argument_list|)
else|:
name|value
operator|.
name|isGreaterThan
argument_list|(
name|lowerBound
argument_list|)
operator|)
operator|)
operator|&&
operator|(
name|this
operator|.
name|upperBound
operator|==
literal|null
operator|||
operator|(
name|this
operator|.
name|upperInclusive
condition|?
name|value
operator|.
name|isLessThanOrEqual
argument_list|(
name|upperBound
argument_list|)
else|:
name|value
operator|.
name|isLessThan
argument_list|(
name|upperBound
argument_list|)
operator|)
operator|)
return|;
block|}
block|}
specifier|private
specifier|static
specifier|final
class|class
name|MultiSetRange
implements|implements
name|Range
block|{
specifier|private
specifier|final
name|Collection
argument_list|<
name|Range
argument_list|>
name|ranges
decl_stmt|;
specifier|private
name|MultiSetRange
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Range
argument_list|>
name|ranges
parameter_list|)
block|{
name|this
operator|.
name|ranges
operator|=
name|ranges
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|Range
operator|>
name|emptySet
argument_list|()
else|:
name|ranges
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|accepts
parameter_list|(
specifier|final
name|DeweyDecimal
name|value
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
specifier|final
name|Range
name|range
range|:
name|this
operator|.
name|ranges
control|)
block|{
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
comment|// if any range matches, we consider it a match
if|if
condition|(
name|range
operator|.
name|accepts
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
comment|// This class is a copy of the one in Ant project, but since Ivy *core* module
comment|// (intentionally) doesn't rely on Ant, we use a copied version here
specifier|private
specifier|static
specifier|final
class|class
name|DeweyDecimal
block|{
comment|/**          * Array of components that make up DeweyDecimal          */
specifier|private
specifier|final
name|int
index|[]
name|components
decl_stmt|;
comment|/**          * Construct a DeweyDecimal from an array of integer components.          *          * @param components an array of integer components.          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|DeweyDecimal
parameter_list|(
specifier|final
name|int
index|[]
name|components
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
operator|new
name|int
index|[
name|components
operator|.
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|components
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|components
argument_list|,
literal|0
argument_list|,
name|components
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**          * Construct a DeweyDecimal from string in DeweyDecimal format.          *          * @param string the string in dewey decimal format          * @throws NumberFormatException if string is malformed          */
specifier|public
name|DeweyDecimal
parameter_list|(
specifier|final
name|String
name|string
parameter_list|)
throws|throws
name|NumberFormatException
block|{
specifier|final
name|StringTokenizer
name|tokenizer
init|=
operator|new
name|StringTokenizer
argument_list|(
name|string
argument_list|,
literal|"."
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|tokenizer
operator|.
name|countTokens
argument_list|()
decl_stmt|;
name|components
operator|=
operator|new
name|int
index|[
operator|(
name|size
operator|+
literal|1
operator|)
operator|/
literal|2
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|components
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|String
name|component
init|=
name|tokenizer
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"Empty component in string"
argument_list|)
throw|;
block|}
name|components
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|component
argument_list|)
expr_stmt|;
comment|// Strip '.' token
if|if
condition|(
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|tokenizer
operator|.
name|nextToken
argument_list|()
expr_stmt|;
comment|// If it ended in a dot, throw an exception
if|if
condition|(
operator|!
name|tokenizer
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"DeweyDecimal ended in a '.'"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**          * Return number of components in<code>DeweyDecimal</code>.          *          * @return the number of components in dewey decimal          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|components
operator|.
name|length
return|;
block|}
comment|/**          * Return the component at specified index.          *          * @param index the index of components          * @return the value of component at index          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|int
name|get
parameter_list|(
specifier|final
name|int
name|index
parameter_list|)
block|{
return|return
name|components
index|[
name|index
index|]
return|;
block|}
comment|/**          * Return<code>true</code> if this<code>DeweyDecimal</code> is          * equal to the other<code>DeweyDecimal</code>.          *          * @param other the other DeweyDecimal          * @return true if equal to other DeweyDecimal, false otherwise          */
specifier|public
name|boolean
name|isEqual
parameter_list|(
specifier|final
name|DeweyDecimal
name|other
parameter_list|)
block|{
specifier|final
name|int
name|max
init|=
name|Math
operator|.
name|max
argument_list|(
name|other
operator|.
name|components
operator|.
name|length
argument_list|,
name|components
operator|.
name|length
argument_list|)
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
name|max
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|component1
init|=
operator|(
name|i
operator|<
name|components
operator|.
name|length
operator|)
condition|?
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
specifier|final
name|int
name|component2
init|=
operator|(
name|i
operator|<
name|other
operator|.
name|components
operator|.
name|length
operator|)
condition|?
name|other
operator|.
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|component2
operator|!=
name|component1
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
comment|// Exact match
block|}
comment|/**          * Return<code>true</code> if this<code>DeweyDecimal</code> is          * less than the other<code>DeweyDecimal</code>.          *          * @param other the other DeweyDecimal          * @return true if less than other DeweyDecimal, false otherwise          */
specifier|public
name|boolean
name|isLessThan
parameter_list|(
specifier|final
name|DeweyDecimal
name|other
parameter_list|)
block|{
return|return
operator|!
name|isGreaterThanOrEqual
argument_list|(
name|other
argument_list|)
return|;
block|}
comment|/**          * Return<code>true</code> if this<code>DeweyDecimal</code> is          * less than or equal to the other<code>DeweyDecimal</code>.          *          * @param other the other DeweyDecimal          * @return true if less than or equal to other DeweyDecimal, false otherwise          */
specifier|public
name|boolean
name|isLessThanOrEqual
parameter_list|(
specifier|final
name|DeweyDecimal
name|other
parameter_list|)
block|{
return|return
operator|!
name|isGreaterThan
argument_list|(
name|other
argument_list|)
return|;
block|}
comment|/**          * Return<code>true</code> if this<code>DeweyDecimal</code> is          * greater than the other<code>DeweyDecimal</code>.          *          * @param other the other DeweyDecimal          * @return true if greater than other DeweyDecimal, false otherwise          */
specifier|public
name|boolean
name|isGreaterThan
parameter_list|(
specifier|final
name|DeweyDecimal
name|other
parameter_list|)
block|{
specifier|final
name|int
name|max
init|=
name|Math
operator|.
name|max
argument_list|(
name|other
operator|.
name|components
operator|.
name|length
argument_list|,
name|components
operator|.
name|length
argument_list|)
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
name|max
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|component1
init|=
operator|(
name|i
operator|<
name|components
operator|.
name|length
operator|)
condition|?
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
specifier|final
name|int
name|component2
init|=
operator|(
name|i
operator|<
name|other
operator|.
name|components
operator|.
name|length
operator|)
condition|?
name|other
operator|.
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|component2
operator|>
name|component1
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|component2
operator|<
name|component1
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
comment|// Exact match
block|}
comment|/**          * Return<code>true</code> if this<code>DeweyDecimal</code> is          * greater than or equal to the other<code>DeweyDecimal</code>.          *          * @param other the other DeweyDecimal          * @return true if greater than or equal to other DeweyDecimal, false otherwise          */
specifier|public
name|boolean
name|isGreaterThanOrEqual
parameter_list|(
specifier|final
name|DeweyDecimal
name|other
parameter_list|)
block|{
specifier|final
name|int
name|max
init|=
name|Math
operator|.
name|max
argument_list|(
name|other
operator|.
name|components
operator|.
name|length
argument_list|,
name|components
operator|.
name|length
argument_list|)
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
name|max
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|component1
init|=
operator|(
name|i
operator|<
name|components
operator|.
name|length
operator|)
condition|?
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
specifier|final
name|int
name|component2
init|=
operator|(
name|i
operator|<
name|other
operator|.
name|components
operator|.
name|length
operator|)
condition|?
name|other
operator|.
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|component2
operator|>
name|component1
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|component2
operator|<
name|component1
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|true
return|;
comment|// Exact match
block|}
comment|/**          * Return string representation of<code>DeweyDecimal</code>.          *          * @return the string representation of DeweyDecimal.          */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|component
range|:
name|components
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|component
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
comment|/**          * Compares this DeweyDecimal with another one.          *          * @param other another DeweyDecimal to compare with          * @return result          * @see java.lang.Comparable#compareTo(Object)          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
name|int
name|compareTo
parameter_list|(
name|DeweyDecimal
name|other
parameter_list|)
block|{
specifier|final
name|int
name|max
init|=
name|Math
operator|.
name|max
argument_list|(
name|other
operator|.
name|components
operator|.
name|length
argument_list|,
name|components
operator|.
name|length
argument_list|)
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
name|max
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|component1
init|=
operator|(
name|i
operator|<
name|components
operator|.
name|length
operator|)
condition|?
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
specifier|final
name|int
name|component2
init|=
operator|(
name|i
operator|<
name|other
operator|.
name|components
operator|.
name|length
operator|)
condition|?
name|other
operator|.
name|components
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|component1
operator|!=
name|component2
condition|)
block|{
return|return
name|component1
operator|-
name|component2
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|DeweyDecimal
operator|&&
name|isEqual
argument_list|(
operator|(
name|DeweyDecimal
operator|)
name|o
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

