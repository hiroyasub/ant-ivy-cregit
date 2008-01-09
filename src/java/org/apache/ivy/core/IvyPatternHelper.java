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
name|Collections
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
name|Stack
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
name|cache
operator|.
name|ArtifactOrigin
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
name|cache
operator|.
name|RepositoryCacheManager
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
name|descriptor
operator|.
name|DefaultArtifact
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
name|IvyVariableContainer
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
name|IvyVariableContainerImpl
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
specifier|final
class|class
name|IvyPatternHelper
block|{
specifier|private
name|IvyPatternHelper
parameter_list|()
block|{
comment|//Helper class
block|}
specifier|public
specifier|static
specifier|final
name|String
name|CONF_KEY
init|=
literal|"conf"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_KEY
init|=
literal|"type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EXT_KEY
init|=
literal|"ext"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ARTIFACT_KEY
init|=
literal|"artifact"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BRANCH_KEY
init|=
literal|"branch"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REVISION_KEY
init|=
literal|"revision"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MODULE_KEY
init|=
literal|"module"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORGANISATION_KEY
init|=
literal|"organisation"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORGANISATION_KEY2
init|=
literal|"organization"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_ARTIFACTNAME_KEY
init|=
literal|"originalname"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|PARAM_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\@\\{(.*?)\\}"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|VAR_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{(.*?)\\}"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ModuleRevisionId
name|moduleRevision
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|moduleRevision
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|moduleRevision
operator|.
name|getName
argument_list|()
argument_list|,
name|moduleRevision
operator|.
name|getRevision
argument_list|()
argument_list|,
literal|"ivy"
argument_list|,
literal|"ivy"
argument_list|,
literal|"xml"
argument_list|,
literal|null
argument_list|,
name|moduleRevision
operator|.
name|getAttributes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ModuleRevisionId
name|moduleRevision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|moduleRevision
argument_list|,
operator|new
name|DefaultArtifact
argument_list|(
name|moduleRevision
argument_list|,
literal|null
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|artifact
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
argument_list|,
literal|null
argument_list|,
name|origin
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|conf
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|,
name|conf
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|conf
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|)
block|{
name|Map
name|attributes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|putAll
argument_list|(
name|mrid
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|putAll
argument_list|(
name|artifact
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
operator|.
name|getOrganisation
argument_list|()
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
name|mrid
operator|.
name|getBranch
argument_list|()
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|,
name|artifact
operator|.
name|getName
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|,
name|conf
argument_list|,
name|origin
argument_list|,
name|attributes
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|conf
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|conf
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|conf
parameter_list|,
name|Map
name|extraAttributes
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|conf
argument_list|,
literal|null
argument_list|,
name|extraAttributes
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|conf
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|,
name|Map
name|extraAttributes
parameter_list|)
block|{
return|return
name|substitute
argument_list|(
name|pattern
argument_list|,
name|org
argument_list|,
name|module
argument_list|,
literal|null
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|,
name|conf
argument_list|,
name|origin
argument_list|,
name|extraAttributes
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substitute
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifact
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|ext
parameter_list|,
name|String
name|conf
parameter_list|,
name|ArtifactOrigin
name|origin
parameter_list|,
name|Map
name|extraAttributes
parameter_list|)
block|{
name|Map
name|tokens
init|=
operator|new
name|HashMap
argument_list|(
name|extraAttributes
operator|==
literal|null
condition|?
name|Collections
operator|.
name|EMPTY_MAP
else|:
name|extraAttributes
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|ORGANISATION_KEY
argument_list|,
name|org
operator|==
literal|null
condition|?
literal|""
else|:
name|org
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|ORGANISATION_KEY2
argument_list|,
name|org
operator|==
literal|null
condition|?
literal|""
else|:
name|org
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|MODULE_KEY
argument_list|,
name|module
operator|==
literal|null
condition|?
literal|""
else|:
name|module
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|BRANCH_KEY
argument_list|,
name|branch
operator|==
literal|null
condition|?
literal|""
else|:
name|branch
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|REVISION_KEY
argument_list|,
name|revision
operator|==
literal|null
condition|?
literal|""
else|:
name|revision
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|ARTIFACT_KEY
argument_list|,
name|artifact
operator|==
literal|null
condition|?
name|module
else|:
name|artifact
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|TYPE_KEY
argument_list|,
name|type
operator|==
literal|null
condition|?
literal|"jar"
else|:
name|type
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|EXT_KEY
argument_list|,
name|ext
operator|==
literal|null
condition|?
literal|"jar"
else|:
name|ext
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|CONF_KEY
argument_list|,
name|conf
operator|==
literal|null
condition|?
literal|"default"
else|:
name|conf
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|put
argument_list|(
name|ORIGINAL_ARTIFACTNAME_KEY
argument_list|,
name|origin
operator|==
literal|null
condition|?
operator|new
name|OriginalArtifactNameValue
argument_list|(
name|org
argument_list|,
name|module
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|,
name|artifact
argument_list|,
name|type
argument_list|,
name|ext
argument_list|)
else|:
operator|new
name|OriginalArtifactNameValue
argument_list|(
name|origin
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|substituteTokens
argument_list|(
name|pattern
argument_list|,
name|tokens
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substituteVariables
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Map
name|variables
parameter_list|)
block|{
return|return
name|substituteVariables
argument_list|(
name|pattern
argument_list|,
operator|new
name|IvyVariableContainerImpl
argument_list|(
name|variables
argument_list|)
argument_list|,
operator|new
name|Stack
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|substituteVariables
parameter_list|(
name|String
name|pattern
parameter_list|,
name|IvyVariableContainer
name|variables
parameter_list|)
block|{
return|return
name|substituteVariables
argument_list|(
name|pattern
argument_list|,
name|variables
argument_list|,
operator|new
name|Stack
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|substituteVariables
parameter_list|(
name|String
name|pattern
parameter_list|,
name|IvyVariableContainer
name|variables
parameter_list|,
name|Stack
name|substituting
parameter_list|)
block|{
comment|// if you supply null, null is what you get
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Matcher
name|m
init|=
name|VAR_PATTERN
operator|.
name|matcher
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|var
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|val
init|=
operator|(
name|String
operator|)
name|variables
operator|.
name|getVariable
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|substituting
operator|.
name|indexOf
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|List
name|cycle
init|=
operator|new
name|ArrayList
argument_list|(
name|substituting
operator|.
name|subList
argument_list|(
name|index
argument_list|,
name|substituting
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|cycle
operator|.
name|add
argument_list|(
name|var
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"cyclic variable definition: cycle = "
operator|+
name|cycle
argument_list|)
throw|;
block|}
name|substituting
operator|.
name|push
argument_list|(
name|var
argument_list|)
expr_stmt|;
name|val
operator|=
name|substituteVariables
argument_list|(
name|val
argument_list|,
name|variables
argument_list|,
name|substituting
argument_list|)
expr_stmt|;
name|substituting
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|m
operator|.
name|group
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|val
operator|.
name|replaceAll
argument_list|(
literal|"\\\\"
argument_list|,
literal|"\\\\\\\\"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\$"
argument_list|,
literal|"\\\\\\$"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|appendTail
argument_list|(
name|sb
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|substituteTokens
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Map
name|tokens
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|pattern
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|StringBuffer
name|optionalPart
init|=
literal|null
decl_stmt|;
name|StringBuffer
name|tokenBuffer
init|=
literal|null
decl_stmt|;
name|boolean
name|insideOptionalPart
init|=
literal|false
decl_stmt|;
name|boolean
name|insideToken
init|=
literal|false
decl_stmt|;
name|boolean
name|tokenHadValue
init|=
literal|false
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
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|chars
index|[
name|i
index|]
condition|)
block|{
case|case
literal|'('
case|:
if|if
condition|(
name|insideOptionalPart
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid start of optional part at position "
operator|+
name|i
operator|+
literal|" in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
name|optionalPart
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|insideOptionalPart
operator|=
literal|true
expr_stmt|;
name|tokenHadValue
operator|=
literal|false
expr_stmt|;
break|break;
case|case
literal|')'
case|:
if|if
condition|(
operator|!
name|insideOptionalPart
operator|||
name|insideToken
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid end of optional part at position "
operator|+
name|i
operator|+
literal|" in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
if|if
condition|(
name|tokenHadValue
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|optionalPart
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|insideOptionalPart
operator|=
literal|false
expr_stmt|;
break|break;
case|case
literal|'['
case|:
if|if
condition|(
name|insideToken
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid start of token at position "
operator|+
name|i
operator|+
literal|" in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
name|tokenBuffer
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|insideToken
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|']'
case|:
if|if
condition|(
operator|!
name|insideToken
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid end of token at position "
operator|+
name|i
operator|+
literal|" in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
name|String
name|token
init|=
name|tokenBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Object
name|tokenValue
init|=
name|tokens
operator|.
name|get
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|String
name|value
init|=
operator|(
name|tokenValue
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|tokenValue
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|insideOptionalPart
condition|)
block|{
name|tokenHadValue
operator|=
operator|(
name|value
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|value
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
expr_stmt|;
name|optionalPart
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// the token wasn't set, it's kept as is
name|value
operator|=
literal|"["
operator|+
name|token
operator|+
literal|"]"
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|insideToken
operator|=
literal|false
expr_stmt|;
break|break;
default|default:
if|if
condition|(
name|insideToken
condition|)
block|{
name|tokenBuffer
operator|.
name|append
argument_list|(
name|chars
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|insideOptionalPart
condition|)
block|{
name|optionalPart
operator|.
name|append
argument_list|(
name|chars
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|chars
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|insideToken
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"last token hasn't been closed in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
if|if
condition|(
name|insideOptionalPart
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"optional part hasn't been closed in pattern "
operator|+
name|pattern
argument_list|)
throw|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|substituteVariable
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|variable
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|substituteVariable
argument_list|(
name|buf
argument_list|,
name|variable
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|substituteVariable
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|String
name|variable
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|String
name|from
init|=
literal|"${"
operator|+
name|variable
operator|+
literal|"}"
decl_stmt|;
name|int
name|fromLength
init|=
name|from
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
name|buf
operator|.
name|indexOf
argument_list|(
name|from
argument_list|)
init|;
name|index
operator|!=
operator|-
literal|1
condition|;
name|index
operator|=
name|buf
operator|.
name|indexOf
argument_list|(
name|from
argument_list|,
name|index
argument_list|)
control|)
block|{
name|buf
operator|.
name|replace
argument_list|(
name|index
argument_list|,
name|index
operator|+
name|fromLength
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|String
name|substituteToken
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|substituteToken
argument_list|(
name|buf
argument_list|,
name|token
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|substituteToken
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|String
name|from
init|=
name|getTokenString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|int
name|fromLength
init|=
name|from
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
name|buf
operator|.
name|indexOf
argument_list|(
name|from
argument_list|)
init|;
name|index
operator|!=
operator|-
literal|1
condition|;
name|index
operator|=
name|buf
operator|.
name|indexOf
argument_list|(
name|from
argument_list|,
name|index
argument_list|)
control|)
block|{
name|buf
operator|.
name|replace
argument_list|(
name|index
argument_list|,
name|index
operator|+
name|fromLength
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getTokenString
parameter_list|(
name|String
name|token
parameter_list|)
block|{
return|return
literal|"["
operator|+
name|token
operator|+
literal|"]"
return|;
block|}
specifier|public
specifier|static
name|String
name|substituteParams
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Map
name|params
parameter_list|)
block|{
return|return
name|substituteParams
argument_list|(
name|pattern
argument_list|,
operator|new
name|IvyVariableContainerImpl
argument_list|(
name|params
argument_list|)
argument_list|,
operator|new
name|Stack
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|substituteParams
parameter_list|(
name|String
name|pattern
parameter_list|,
name|IvyVariableContainer
name|params
parameter_list|,
name|Stack
name|substituting
parameter_list|)
block|{
comment|// TODO : refactor this with substituteVariables
comment|// if you supply null, null is what you get
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Matcher
name|m
init|=
name|PARAM_PATTERN
operator|.
name|matcher
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
while|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|var
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|val
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|getVariable
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|int
name|index
init|=
name|substituting
operator|.
name|indexOf
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|List
name|cycle
init|=
operator|new
name|ArrayList
argument_list|(
name|substituting
operator|.
name|subList
argument_list|(
name|index
argument_list|,
name|substituting
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|cycle
operator|.
name|add
argument_list|(
name|var
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"cyclic param definition: cycle = "
operator|+
name|cycle
argument_list|)
throw|;
block|}
name|substituting
operator|.
name|push
argument_list|(
name|var
argument_list|)
expr_stmt|;
name|val
operator|=
name|substituteVariables
argument_list|(
name|val
argument_list|,
name|params
argument_list|,
name|substituting
argument_list|)
expr_stmt|;
name|substituting
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|val
operator|=
name|m
operator|.
name|group
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|appendReplacement
argument_list|(
name|sb
argument_list|,
name|val
operator|.
name|replaceAll
argument_list|(
literal|"\\\\"
argument_list|,
literal|"\\\\\\\\"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\\@"
argument_list|,
literal|"\\\\\\@"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|appendTail
argument_list|(
name|sb
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * This class returns the original name of the artifact 'on demand'. This is done to avoid      * having to read the cached datafile containing the original location of the artifact if we      * don't need it.      */
specifier|private
specifier|static
class|class
name|OriginalArtifactNameValue
block|{
comment|// module properties
specifier|private
name|String
name|org
decl_stmt|;
specifier|private
name|String
name|moduleName
decl_stmt|;
specifier|private
name|String
name|branch
decl_stmt|;
specifier|private
name|String
name|revision
decl_stmt|;
comment|// artifact properties
specifier|private
name|String
name|artifactName
decl_stmt|;
specifier|private
name|String
name|artifactType
decl_stmt|;
specifier|private
name|String
name|artifactExt
decl_stmt|;
comment|// cached origin;
specifier|private
name|ArtifactOrigin
name|origin
decl_stmt|;
specifier|public
name|OriginalArtifactNameValue
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|moduleName
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|artifactName
parameter_list|,
name|String
name|artifactType
parameter_list|,
name|String
name|artifactExt
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
name|moduleName
operator|=
name|moduleName
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|artifactName
operator|=
name|artifactName
expr_stmt|;
name|this
operator|.
name|artifactType
operator|=
name|artifactType
expr_stmt|;
name|this
operator|.
name|artifactExt
operator|=
name|artifactExt
expr_stmt|;
block|}
comment|/**          * @param origin          */
specifier|public
name|OriginalArtifactNameValue
parameter_list|(
name|ArtifactOrigin
name|origin
parameter_list|)
block|{
name|this
operator|.
name|origin
operator|=
name|origin
expr_stmt|;
block|}
comment|// Called by substituteTokens only if the original artifact name is needed
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|origin
operator|==
literal|null
condition|)
block|{
name|ModuleRevisionId
name|revId
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|org
argument_list|,
name|moduleName
argument_list|,
name|branch
argument_list|,
name|revision
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|revId
argument_list|,
literal|null
argument_list|,
name|artifactName
argument_list|,
name|artifactType
argument_list|,
name|artifactExt
argument_list|)
decl_stmt|;
comment|// TODO cache: see how we could know which actual cache manager to use, since this
comment|// will fail when using a resolver in a chain with a specific cache manager
name|RepositoryCacheManager
name|cacheManager
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolver
argument_list|(
name|revId
argument_list|)
operator|.
name|getRepositoryCacheManager
argument_list|()
decl_stmt|;
name|origin
operator|=
name|cacheManager
operator|.
name|getSavedArtifactOrigin
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
if|if
condition|(
name|origin
operator|==
name|ArtifactOrigin
operator|.
name|UNKNOWN
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"no artifact origin found for "
operator|+
name|artifact
operator|+
literal|" in "
operator|+
name|cacheManager
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
if|if
condition|(
name|origin
operator|==
name|ArtifactOrigin
operator|.
name|UNKNOWN
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// we assume that the original filename is the last part of the original file location
name|String
name|location
init|=
name|origin
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|int
name|lastPathIndex
init|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastPathIndex
operator|==
operator|-
literal|1
condition|)
block|{
name|lastPathIndex
operator|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
name|int
name|lastColonIndex
init|=
name|location
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
name|location
operator|.
name|substring
argument_list|(
name|lastPathIndex
operator|+
literal|1
argument_list|,
name|lastColonIndex
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getTokenRoot
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|int
name|index
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|'['
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|pattern
return|;
block|}
else|else
block|{
return|return
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

