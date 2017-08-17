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
name|core
operator|.
name|module
operator|.
name|id
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|WeakHashMap
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
name|Ivy
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
name|IvyContext
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
name|util
operator|.
name|extendable
operator|.
name|UnmodifiableExtendableItem
import|;
end_import

begin_comment
comment|/**  * Identifies a module in a particular version  *  * @see<a href="package-summary.html">org.apache.ivy.core.module.id</a>  */
end_comment

begin_class
specifier|public
class|class
name|ModuleRevisionId
extends|extends
name|UnmodifiableExtendableItem
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENCODE_SEPARATOR
init|=
name|ModuleId
operator|.
name|ENCODE_SEPARATOR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODE_PREFIX
init|=
literal|"+"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NULL_ENCODE
init|=
literal|"@#:NULL:#@"
decl_stmt|;
specifier|static
specifier|final
name|String
name|STRICT_CHARS_PATTERN
init|=
literal|"[a-zA-Z0-9\\-/\\._+=]"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REV_STRICT_CHARS_PATTERN
init|=
literal|"[a-zA-Z0-9\\-/\\._+=,\\[\\]\\{\\}\\(\\):@]"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|ModuleRevisionId
argument_list|,
name|WeakReference
argument_list|<
name|ModuleRevisionId
argument_list|>
argument_list|>
name|CACHE
init|=
operator|new
name|WeakHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Pattern to use to matched mrid text representation.      *      * @see #parse(String)      */
specifier|public
specifier|static
specifier|final
name|Pattern
name|MRID_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"("
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"*)"
operator|+
literal|"#("
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"+)"
operator|+
literal|"(?:#("
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"+))?"
operator|+
literal|";("
operator|+
name|REV_STRICT_CHARS_PATTERN
operator|+
literal|"+)"
argument_list|)
decl_stmt|;
comment|/**      * Same as MRID_PATTERN but using non capturing groups, useful to build larger regexp      */
specifier|public
specifier|static
specifier|final
name|Pattern
name|NON_CAPTURING_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(?:"
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"*)"
operator|+
literal|"#(?:"
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"+)"
operator|+
literal|"(?:#(?:"
operator|+
name|STRICT_CHARS_PATTERN
operator|+
literal|"+))?"
operator|+
literal|";(?:"
operator|+
name|REV_STRICT_CHARS_PATTERN
operator|+
literal|"+)"
argument_list|)
decl_stmt|;
comment|/**      * Parses a module revision id text representation and returns a new {@link ModuleRevisionId}      * instance corresponding to the parsed String.      *<p>      * The result is unspecified if the module doesn't respect strict name conventions.      *</p>      *      * @param mrid      *            the text representation of the module (as returned by {@link #toString()}). Must      *            not be<code>null</code>.      * @return a {@link ModuleRevisionId} corresponding to the given text representation      * @throws IllegalArgumentException      *             if the given text representation does not match the {@link ModuleRevisionId} text      *             representation rules.      */
specifier|public
specifier|static
name|ModuleRevisionId
name|parse
parameter_list|(
name|String
name|mrid
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|MRID_PATTERN
operator|.
name|matcher
argument_list|(
name|mrid
operator|.
name|trim
argument_list|()
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"module revision text representation do not match expected pattern."
operator|+
literal|" given mrid='"
operator|+
name|mrid
operator|+
literal|"' expected form="
operator|+
name|MRID_PATTERN
operator|.
name|pattern
argument_list|()
argument_list|)
throw|;
block|}
comment|// CheckStyle:MagicNumber| OFF
return|return
name|newInstance
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|4
argument_list|)
argument_list|)
return|;
comment|// CheckStyle:MagicNumber| ON
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|revision
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|,
name|boolean
name|replaceNullBranchWithDefault
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|ModuleId
operator|.
name|newInstance
argument_list|(
name|organisation
argument_list|,
name|name
argument_list|)
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|,
name|replaceNullBranchWithDefault
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|rev
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|,
name|rev
argument_list|,
name|mrid
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|newInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|rev
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleRevisionId
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|,
name|mrid
operator|.
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an intern instance of the given ModuleRevisionId if any, or put the given      * ModuleRevisionId in a cache of intern instances and returns it.      *<p>      * This method should be called on ModuleRevisionId created with one of the constructor to      * decrease memory footprint.      *</p>      *<p>      * When using static newInstances methods, this method is already called.      *</p>      *      * @param moduleRevisionId      *            the module revision id to intern      * @return an interned ModuleRevisionId      */
specifier|public
specifier|static
name|ModuleRevisionId
name|intern
parameter_list|(
name|ModuleRevisionId
name|moduleRevisionId
parameter_list|)
block|{
name|ModuleRevisionId
name|r
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|CACHE
init|)
block|{
name|WeakReference
argument_list|<
name|ModuleRevisionId
argument_list|>
name|ref
init|=
name|CACHE
operator|.
name|get
argument_list|(
name|moduleRevisionId
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|r
operator|=
name|ref
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|moduleRevisionId
expr_stmt|;
name|CACHE
operator|.
name|put
argument_list|(
name|r
argument_list|,
operator|new
name|WeakReference
argument_list|<>
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
specifier|private
specifier|final
name|ModuleId
name|moduleId
decl_stmt|;
specifier|private
specifier|final
name|String
name|branch
decl_stmt|;
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
specifier|private
name|int
name|hash
decl_stmt|;
comment|// TODO: make these constructors private and use only static factory methods
specifier|public
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|this
argument_list|(
name|moduleId
argument_list|,
literal|null
argument_list|,
name|revision
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|this
argument_list|(
name|moduleId
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
name|this
argument_list|(
name|moduleId
argument_list|,
literal|null
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|)
block|{
name|this
argument_list|(
name|moduleId
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|extraAttributes
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ModuleRevisionId
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraAttributes
parameter_list|,
name|boolean
name|replaceNullBranchWithDefault
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|extraAttributes
argument_list|)
expr_stmt|;
name|this
operator|.
name|moduleId
operator|=
name|moduleId
expr_stmt|;
name|IvyContext
name|context
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|this
operator|.
name|branch
operator|=
operator|(
name|replaceNullBranchWithDefault
operator|&&
name|branch
operator|==
literal|null
operator|)
comment|// we test if there's already an Ivy instance loaded, to avoid loading a default one
comment|// just to get the default branch
condition|?
operator|(
name|context
operator|.
name|peekIvy
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|context
operator|.
name|getSettings
argument_list|()
operator|.
name|getDefaultBranch
argument_list|(
name|moduleId
argument_list|)
operator|)
else|:
name|branch
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
operator|==
literal|null
condition|?
name|Ivy
operator|.
name|getWorkingRevision
argument_list|()
else|:
name|normalizeRevision
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|,
name|this
operator|.
name|moduleId
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|,
name|this
operator|.
name|moduleId
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|,
name|this
operator|.
name|branch
argument_list|)
expr_stmt|;
name|setStandardAttribute
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|,
name|this
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleId
name|getModuleId
parameter_list|()
block|{
return|return
name|moduleId
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getModuleId
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|getModuleId
argument_list|()
operator|.
name|getOrganisation
argument_list|()
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
annotation|@
name|Override
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
operator|!
operator|(
name|obj
operator|instanceof
name|ModuleRevisionId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ModuleRevisionId
name|other
init|=
operator|(
name|ModuleRevisionId
operator|)
name|obj
decl_stmt|;
return|return
name|other
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|getRevision
argument_list|()
argument_list|)
operator|&&
operator|!
operator|(
name|other
operator|.
name|getBranch
argument_list|()
operator|==
literal|null
operator|&&
name|getBranch
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
operator|!
operator|(
name|other
operator|.
name|getBranch
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|other
operator|.
name|getBranch
argument_list|()
operator|.
name|equals
argument_list|(
name|getBranch
argument_list|()
argument_list|)
operator|)
operator|&&
name|other
operator|.
name|getModuleId
argument_list|()
operator|.
name|equals
argument_list|(
name|getModuleId
argument_list|()
argument_list|)
operator|&&
name|other
operator|.
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|equals
argument_list|(
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|hash
operator|==
literal|0
condition|)
block|{
comment|// CheckStyle:MagicNumber| OFF
name|hash
operator|=
literal|31
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
operator|(
name|getBranch
argument_list|()
operator|==
literal|null
condition|?
literal|0
else|:
name|getBranch
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|getRevision
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|getModuleId
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
name|hash
operator|*
literal|13
operator|+
name|getQualifiedExtraAttributes
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
comment|// CheckStyle:MagicNumber| ON
block|}
return|return
name|hash
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|moduleId
operator|+
operator|(
name|branch
operator|==
literal|null
operator|||
name|branch
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|""
else|:
literal|"#"
operator|+
name|branch
operator|)
operator|+
literal|";"
operator|+
operator|(
name|revision
operator|==
literal|null
condition|?
literal|"NONE"
else|:
name|revision
operator|)
return|;
block|}
specifier|public
name|String
name|encodeToString
parameter_list|()
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|getAttributes
argument_list|()
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|keySet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|getExtraAttributes
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|putAll
argument_list|(
name|getQualifiedExtraAttributes
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|att
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|att
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|value
operator|=
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
name|NULL_ENCODE
else|:
name|value
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|ENCODE_PREFIX
argument_list|)
operator|.
name|append
argument_list|(
name|att
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|ENCODE_SEPARATOR
argument_list|)
operator|.
name|append
argument_list|(
name|ENCODE_PREFIX
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
name|ENCODE_SEPARATOR
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ModuleRevisionId
name|decode
parameter_list|(
name|String
name|encoded
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|encoded
operator|.
name|split
argument_list|(
name|ENCODE_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
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
name|parts
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|String
name|attName
init|=
name|parts
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|attName
operator|.
name|startsWith
argument_list|(
name|ENCODE_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"': "
operator|+
name|attName
operator|+
literal|" doesn't start with "
operator|+
name|ENCODE_PREFIX
argument_list|)
throw|;
block|}
else|else
block|{
name|attName
operator|=
name|attName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|attValue
init|=
name|parts
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|attValue
operator|.
name|startsWith
argument_list|(
name|ENCODE_PREFIX
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"': "
operator|+
name|attValue
operator|+
literal|" doesn't start with "
operator|+
name|ENCODE_PREFIX
argument_list|)
throw|;
block|}
else|else
block|{
name|attValue
operator|=
name|attValue
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|NULL_ENCODE
operator|.
name|equals
argument_list|(
name|attValue
argument_list|)
condition|)
block|{
name|attValue
operator|=
literal|null
expr_stmt|;
block|}
name|attributes
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|attValue
argument_list|)
expr_stmt|;
block|}
name|String
name|org
init|=
name|attributes
operator|.
name|remove
argument_list|(
name|IvyPatternHelper
operator|.
name|ORGANISATION_KEY
argument_list|)
decl_stmt|;
name|String
name|mod
init|=
name|attributes
operator|.
name|remove
argument_list|(
name|IvyPatternHelper
operator|.
name|MODULE_KEY
argument_list|)
decl_stmt|;
name|String
name|rev
init|=
name|attributes
operator|.
name|remove
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|attributes
operator|.
name|remove
argument_list|(
name|IvyPatternHelper
operator|.
name|BRANCH_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|org
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"': no organisation"
argument_list|)
throw|;
block|}
if|if
condition|(
name|mod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"': no module name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rev
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module revision id: '"
operator|+
name|encoded
operator|+
literal|"': no revision"
argument_list|)
throw|;
block|}
return|return
name|newInstance
argument_list|(
name|org
argument_list|,
name|mod
argument_list|,
name|branch
argument_list|,
name|rev
argument_list|,
name|attributes
argument_list|)
return|;
block|}
specifier|public
name|String
name|getBranch
parameter_list|()
block|{
return|return
name|branch
return|;
block|}
comment|/**      * [revision] is a valid revision in maven. This method strips the '[' and ']' characters. Cfr.      * http://docs.codehaus.org/x/IGU      */
specifier|private
specifier|static
name|String
name|normalizeRevision
parameter_list|(
name|String
name|revision
parameter_list|)
block|{
if|if
condition|(
name|revision
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|&&
name|revision
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
operator|&&
name|revision
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|IvyPatternHelper
operator|.
name|getTokenString
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
operator|.
name|equals
argument_list|(
name|revision
argument_list|)
condition|)
block|{
comment|// this is the case when listing dynamic revisions
return|return
name|revision
return|;
block|}
return|return
name|revision
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|revision
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|revision
return|;
block|}
block|}
block|}
end_class

end_unit

