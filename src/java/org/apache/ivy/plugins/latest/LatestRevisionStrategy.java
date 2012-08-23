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
name|plugins
operator|.
name|latest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Locale
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
name|plugins
operator|.
name|version
operator|.
name|VersionMatcher
import|;
end_import

begin_class
specifier|public
class|class
name|LatestRevisionStrategy
extends|extends
name|ComparatorLatestStrategy
block|{
comment|/**      * Compares two ModuleRevisionId by their revision. Revisions are compared using an algorithm      * inspired by PHP version_compare one.      */
specifier|final
class|class
name|MridComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|rev1
init|=
operator|(
operator|(
name|ModuleRevisionId
operator|)
name|o1
operator|)
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|String
name|rev2
init|=
operator|(
operator|(
name|ModuleRevisionId
operator|)
name|o2
operator|)
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|rev1
operator|=
name|rev1
operator|.
name|replaceAll
argument_list|(
literal|"([a-zA-Z])(\\d)"
argument_list|,
literal|"$1.$2"
argument_list|)
expr_stmt|;
name|rev1
operator|=
name|rev1
operator|.
name|replaceAll
argument_list|(
literal|"(\\d)([a-zA-Z])"
argument_list|,
literal|"$1.$2"
argument_list|)
expr_stmt|;
name|rev2
operator|=
name|rev2
operator|.
name|replaceAll
argument_list|(
literal|"([a-zA-Z])(\\d)"
argument_list|,
literal|"$1.$2"
argument_list|)
expr_stmt|;
name|rev2
operator|=
name|rev2
operator|.
name|replaceAll
argument_list|(
literal|"(\\d)([a-zA-Z])"
argument_list|,
literal|"$1.$2"
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts1
init|=
name|rev1
operator|.
name|split
argument_list|(
literal|"[\\._\\-\\+]"
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts2
init|=
name|rev2
operator|.
name|split
argument_list|(
literal|"[\\._\\-\\+]"
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|parts1
operator|.
name|length
operator|&&
name|i
operator|<
name|parts2
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|parts1
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|parts2
index|[
name|i
index|]
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|boolean
name|is1Number
init|=
name|isNumber
argument_list|(
name|parts1
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|boolean
name|is2Number
init|=
name|isNumber
argument_list|(
name|parts2
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|is1Number
operator|&&
operator|!
name|is2Number
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|is2Number
operator|&&
operator|!
name|is1Number
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|is1Number
operator|&&
name|is2Number
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|parts1
index|[
name|i
index|]
argument_list|)
operator|.
name|compareTo
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|parts2
index|[
name|i
index|]
argument_list|)
argument_list|)
return|;
block|}
comment|// both are strings, we compare them taking into account special meaning
name|Map
name|specialMeanings
init|=
name|getSpecialMeanings
argument_list|()
decl_stmt|;
name|Integer
name|sm1
init|=
operator|(
name|Integer
operator|)
name|specialMeanings
operator|.
name|get
argument_list|(
name|parts1
index|[
name|i
index|]
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|sm2
init|=
operator|(
name|Integer
operator|)
name|specialMeanings
operator|.
name|get
argument_list|(
name|parts2
index|[
name|i
index|]
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sm1
operator|!=
literal|null
condition|)
block|{
name|sm2
operator|=
name|sm2
operator|==
literal|null
condition|?
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
else|:
name|sm2
expr_stmt|;
return|return
name|sm1
operator|.
name|compareTo
argument_list|(
name|sm2
argument_list|)
return|;
block|}
if|if
condition|(
name|sm2
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
operator|.
name|compareTo
argument_list|(
name|sm2
argument_list|)
return|;
block|}
return|return
name|parts1
index|[
name|i
index|]
operator|.
name|compareTo
argument_list|(
name|parts2
index|[
name|i
index|]
argument_list|)
return|;
block|}
if|if
condition|(
name|i
operator|<
name|parts1
operator|.
name|length
condition|)
block|{
return|return
name|isNumber
argument_list|(
name|parts1
index|[
name|i
index|]
argument_list|)
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|i
operator|<
name|parts2
operator|.
name|length
condition|)
block|{
return|return
name|isNumber
argument_list|(
name|parts2
index|[
name|i
index|]
argument_list|)
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
specifier|private
name|boolean
name|isNumber
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|.
name|matches
argument_list|(
literal|"\\d+"
argument_list|)
return|;
block|}
block|}
comment|/**      * Compares two ArtifactInfo by their revision. Revisions are compared using an algorithm      * inspired by PHP version_compare one, unless a dynamic revision is given, in which case the      * version matcher is used to perform the comparison.      */
specifier|final
class|class
name|ArtifactInfoComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|rev1
init|=
operator|(
operator|(
name|ArtifactInfo
operator|)
name|o1
operator|)
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|String
name|rev2
init|=
operator|(
operator|(
name|ArtifactInfo
operator|)
name|o2
operator|)
operator|.
name|getRevision
argument_list|()
decl_stmt|;
comment|/*              * The revisions can still be not resolved, so we use the current version matcher to              * know if one revision is dynamic, and in this case if it should be considered greater              * or lower than the other one. Note that if the version matcher compare method returns              * 0, it's because it's not possible to know which revision is greater. In this case we              * consider the dynamic one to be greater, because most of the time it will then be              * actually resolved and a real comparison will occur.              */
name|VersionMatcher
name|vmatcher
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
name|ModuleRevisionId
name|mrid1
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|rev1
argument_list|)
decl_stmt|;
name|ModuleRevisionId
name|mrid2
init|=
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|rev2
argument_list|)
decl_stmt|;
if|if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid1
argument_list|)
operator|&&
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid2
argument_list|)
condition|)
block|{
return|return
name|vmatcher
operator|.
name|compare
argument_list|(
name|mrid1
argument_list|,
name|mrid2
argument_list|,
name|mridComparator
argument_list|)
return|;
block|}
if|else if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid1
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|vmatcher
operator|.
name|compare
argument_list|(
name|mrid1
argument_list|,
name|mrid2
argument_list|,
name|mridComparator
argument_list|)
decl_stmt|;
return|return
name|c
operator|>=
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|vmatcher
operator|.
name|isDynamic
argument_list|(
name|mrid2
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|vmatcher
operator|.
name|compare
argument_list|(
name|mrid2
argument_list|,
name|mrid1
argument_list|,
name|mridComparator
argument_list|)
decl_stmt|;
return|return
name|c
operator|>=
literal|0
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
return|return
name|mridComparator
operator|.
name|compare
argument_list|(
name|mrid1
argument_list|,
name|mrid2
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|SpecialMeaning
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Integer
name|value
decl_stmt|;
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
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Integer
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
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
name|IllegalStateException
argument_list|(
literal|"a special meaning should have a name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"a special meaning should have a value"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
specifier|final
name|Map
name|DEFAULT_SPECIAL_MEANINGS
decl_stmt|;
static|static
block|{
name|DEFAULT_SPECIAL_MEANINGS
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|DEFAULT_SPECIAL_MEANINGS
operator|.
name|put
argument_list|(
literal|"dev"
argument_list|,
operator|new
name|Integer
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|DEFAULT_SPECIAL_MEANINGS
operator|.
name|put
argument_list|(
literal|"rc"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|DEFAULT_SPECIAL_MEANINGS
operator|.
name|put
argument_list|(
literal|"final"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|Comparator
name|mridComparator
init|=
operator|new
name|MridComparator
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Comparator
name|artifactInfoComparator
init|=
operator|new
name|ArtifactInfoComparator
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|specialMeanings
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|usedefaultspecialmeanings
init|=
literal|true
decl_stmt|;
specifier|public
name|LatestRevisionStrategy
parameter_list|()
block|{
name|setComparator
argument_list|(
name|artifactInfoComparator
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"latest-revision"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguredSpecialMeaning
parameter_list|(
name|SpecialMeaning
name|meaning
parameter_list|)
block|{
name|meaning
operator|.
name|validate
argument_list|()
expr_stmt|;
name|getSpecialMeanings
argument_list|()
operator|.
name|put
argument_list|(
name|meaning
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|,
name|meaning
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|Map
name|getSpecialMeanings
parameter_list|()
block|{
if|if
condition|(
name|specialMeanings
operator|==
literal|null
condition|)
block|{
name|specialMeanings
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
if|if
condition|(
name|isUsedefaultspecialmeanings
argument_list|()
condition|)
block|{
name|specialMeanings
operator|.
name|putAll
argument_list|(
name|DEFAULT_SPECIAL_MEANINGS
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|specialMeanings
return|;
block|}
specifier|public
name|boolean
name|isUsedefaultspecialmeanings
parameter_list|()
block|{
return|return
name|usedefaultspecialmeanings
return|;
block|}
specifier|public
name|void
name|setUsedefaultspecialmeanings
parameter_list|(
name|boolean
name|usedefaultspecialmeanings
parameter_list|)
block|{
name|this
operator|.
name|usedefaultspecialmeanings
operator|=
name|usedefaultspecialmeanings
expr_stmt|;
block|}
block|}
end_class

end_unit

