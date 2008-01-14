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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ModuleId
implements|implements
name|Comparable
block|{
specifier|static
specifier|final
name|String
name|ENCODE_SEPARATOR
init|=
literal|":#@#:"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
comment|/*<ModuleId, ModuleId>*/
name|CACHE
init|=
operator|new
name|WeakHashMap
argument_list|()
decl_stmt|;
comment|/**      * Returns a ModuleId for the given organization and module name.      *       * @param org      *            the module's organization, can be<code>null</code>      * @param name      *            the module's name, must not be<code>null</code>      * @return a ModuleId instance      */
specifier|public
specifier|static
name|ModuleId
name|newInstance
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|intern
argument_list|(
operator|new
name|ModuleId
argument_list|(
name|org
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns an intern instance of a ModuleId equals to the given ModuleId if any, or the given      * ModuleId.      *<p>      * This is useful to reduce the number of instances of ModuleId kept in memory, and thus reduce      * memory footprint.      *</p>      *       * @param moduleId      *            the module id to return      * @return a unit instance of the given module id.      */
specifier|public
specifier|static
name|ModuleId
name|intern
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
name|ModuleId
name|r
init|=
operator|(
name|ModuleId
operator|)
name|CACHE
operator|.
name|get
argument_list|(
name|moduleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
name|moduleId
expr_stmt|;
name|CACHE
operator|.
name|put
argument_list|(
name|r
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
specifier|private
name|String
name|organisation
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|hash
decl_stmt|;
comment|/**      * Constructor.      * @param  organisation  The organisation which creates the module.      * @param  name  The name of the module.      */
specifier|public
name|ModuleId
parameter_list|(
name|String
name|organisation
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"null name not allowed"
argument_list|)
throw|;
block|}
name|this
operator|.
name|organisation
operator|=
name|organisation
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Returns the name of the module.      * @return  The name of the module.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Returns the name of the organisation.      * @return  The name of the organisation.      */
specifier|public
name|String
name|getOrganisation
parameter_list|()
block|{
return|return
name|organisation
return|;
block|}
comment|/** {@inheritDoc} */
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
name|ModuleId
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ModuleId
name|other
init|=
operator|(
name|ModuleId
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|other
operator|.
name|organisation
operator|==
literal|null
condition|)
block|{
return|return
name|organisation
operator|==
literal|null
operator|&&
name|other
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|other
operator|.
name|organisation
operator|.
name|equals
argument_list|(
name|organisation
argument_list|)
operator|&&
name|other
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
comment|/** {@inheritDoc} */
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
comment|//CheckStyle:MagicNumber| OFF
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
name|organisation
operator|==
literal|null
condition|?
literal|0
else|:
name|organisation
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
name|name
operator|.
name|hashCode
argument_list|()
expr_stmt|;
comment|//CheckStyle:MagicNumber| ON
block|}
return|return
name|hash
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|organisation
operator|+
literal|"#"
operator|+
name|name
return|;
block|}
comment|/** {@inheritDoc} */
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|ModuleId
name|that
init|=
operator|(
name|ModuleId
operator|)
name|obj
decl_stmt|;
name|int
name|result
init|=
name|organisation
operator|.
name|compareTo
argument_list|(
name|that
operator|.
name|organisation
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|0
condition|)
block|{
name|result
operator|=
name|name
operator|.
name|compareTo
argument_list|(
name|that
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Returns the encoded String representing this ModuleId.      * @return  The ModuleId encoded as String.      */
specifier|public
name|String
name|encodeToString
parameter_list|()
block|{
return|return
name|getOrganisation
argument_list|()
operator|+
name|ENCODE_SEPARATOR
operator|+
name|getName
argument_list|()
return|;
block|}
comment|/**      * Returns a ModuleId        * @param  encoded        * @return  The new ModuleId.      * @throws  IllegalArgumentException  If the given String could not be decoded.      */
specifier|public
specifier|static
name|ModuleId
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
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"badly encoded module id: '"
operator|+
name|encoded
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ModuleId
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
return|;
block|}
comment|/**      * Pattern to use to matched mid text representation.      * @see #parse(String)      */
specifier|public
specifier|static
specifier|final
name|Pattern
name|MID_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"("
operator|+
name|ModuleRevisionId
operator|.
name|STRICT_CHARS_PATTERN
operator|+
literal|"*)"
operator|+
literal|"#("
operator|+
name|ModuleRevisionId
operator|.
name|STRICT_CHARS_PATTERN
operator|+
literal|"+)"
argument_list|)
decl_stmt|;
comment|/**      * Parses the module id text representation and returns it as a {@link ModuleId} instance.      *       * @param mid      *            the module id text representation to parse      * @return the ModuleId instance corresponding to the representation      * @throws IllegalArgumentException      *             if the given text representation cannot be parsed      */
specifier|public
specifier|static
name|ModuleId
name|parse
parameter_list|(
name|String
name|mid
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|MID_PATTERN
operator|.
name|matcher
argument_list|(
name|mid
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
literal|"module text representation do not match expected pattern."
operator|+
literal|" given mid='"
operator|+
name|mid
operator|+
literal|"' expected form="
operator|+
name|MID_PATTERN
operator|.
name|pattern
argument_list|()
argument_list|)
throw|;
block|}
comment|//CheckStyle:MagicNumber| OFF
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
argument_list|)
return|;
comment|//CheckStyle:MagicNumber| ON
block|}
block|}
end_class

end_unit

