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
name|version
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
name|latest
operator|.
name|ArtifactInfo
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
name|latest
operator|.
name|LatestStrategy
import|;
end_import

begin_comment
comment|/**  * Matches version ranges:  * [1.0,2.0] matches all versions greater or equal to 1.0 and lower or equal to 2.0  * [1.0,2.0[ matches all versions greater or equal to 1.0 and lower than 2.0  * ]1.0,2.0] matches all versions greater than 1.0 and lower or equal to 2.0  * ]1.0,2.0[ matches all versions greater than 1.0 and lower than 2.0  * [1.0,) matches all versions greater or equal to 1.0  * ]1.0,) matches all versions greater than 1.0  * (,2.0] matches all versions lower or equal to 2.0  * (,2.0[ matches all versions lower than 2.0  *   * This class uses a latest strategy to compare revisions.  * If none is set, it uses the default one of the ivy instance set through setIvy().  * If neither a latest strategy nor a ivy instance is set, an IllegalStateException  * will be thrown when calling accept().  *   * Note that it can't work with latest time strategy, cause no time is known for the limits of the range.  * Therefore only purely revision based LatestStrategy can be used.    *   * @author xavier hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|VersionRangeMatcher
extends|extends
name|AbstractVersionMatcher
block|{
comment|// todo: check these constants
specifier|private
specifier|final
specifier|static
name|String
name|OPEN_INC
init|=
literal|"["
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|OPEN_EXC
init|=
literal|"]"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|CLOSE_INC
init|=
literal|"]"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|CLOSE_EXC
init|=
literal|"["
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|LOWER_INFINITE
init|=
literal|"("
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|UPPER_INFINITE
init|=
literal|")"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|SEPARATOR
init|=
literal|","
decl_stmt|;
comment|// following patterns are built upon constants above and should not be modified
specifier|private
specifier|final
specifier|static
name|String
name|OPEN_INC_PATTERN
init|=
literal|"\\"
operator|+
name|OPEN_INC
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|OPEN_EXC_PATTERN
init|=
literal|"\\"
operator|+
name|OPEN_EXC
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|CLOSE_INC_PATTERN
init|=
literal|"\\"
operator|+
name|CLOSE_INC
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|CLOSE_EXC_PATTERN
init|=
literal|"\\"
operator|+
name|CLOSE_EXC
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|LI_PATTERN
init|=
literal|"\\"
operator|+
name|LOWER_INFINITE
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|UI_PATTERN
init|=
literal|"\\"
operator|+
name|UPPER_INFINITE
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|SEP_PATTERN
init|=
literal|"\\"
operator|+
name|SEPARATOR
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|OPEN_PATTERN
init|=
literal|"["
operator|+
name|OPEN_INC_PATTERN
operator|+
name|OPEN_EXC_PATTERN
operator|+
literal|"]"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|CLOSE_PATTERN
init|=
literal|"["
operator|+
name|CLOSE_INC_PATTERN
operator|+
name|CLOSE_EXC_PATTERN
operator|+
literal|"]"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|ANY_NON_SPECIAL_PATTERN
init|=
literal|"[^"
operator|+
name|SEP_PATTERN
operator|+
name|OPEN_INC_PATTERN
operator|+
name|OPEN_EXC_PATTERN
operator|+
name|CLOSE_INC_PATTERN
operator|+
name|CLOSE_EXC_PATTERN
operator|+
name|LI_PATTERN
operator|+
name|UI_PATTERN
operator|+
literal|"]"
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|FINITE_PATTERN
init|=
name|OPEN_PATTERN
operator|+
literal|"("
operator|+
name|ANY_NON_SPECIAL_PATTERN
operator|+
literal|"+)"
operator|+
name|SEP_PATTERN
operator|+
literal|"("
operator|+
name|ANY_NON_SPECIAL_PATTERN
operator|+
literal|"+)"
operator|+
name|CLOSE_PATTERN
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|LOWER_INFINITE_PATTERN
init|=
name|LI_PATTERN
operator|+
literal|"\\,("
operator|+
name|ANY_NON_SPECIAL_PATTERN
operator|+
literal|"+)"
operator|+
name|CLOSE_PATTERN
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|String
name|UPPER_INFINITE_PATTERN
init|=
name|OPEN_PATTERN
operator|+
literal|"("
operator|+
name|ANY_NON_SPECIAL_PATTERN
operator|+
literal|"+)\\,"
operator|+
name|UI_PATTERN
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Pattern
name|FINITE_RANGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|FINITE_PATTERN
argument_list|)
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Pattern
name|LOWER_INFINITE_RANGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|LOWER_INFINITE_PATTERN
argument_list|)
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Pattern
name|UPPER_INFINITE_RANGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|UPPER_INFINITE_PATTERN
argument_list|)
decl_stmt|;
specifier|private
specifier|final
specifier|static
name|Pattern
name|ALL_RANGE
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|FINITE_PATTERN
operator|+
literal|"|"
operator|+
name|LOWER_INFINITE_PATTERN
operator|+
literal|"|"
operator|+
name|UPPER_INFINITE_PATTERN
argument_list|)
decl_stmt|;
specifier|private
specifier|final
class|class
name|MRIDArtifactInfo
implements|implements
name|ArtifactInfo
block|{
specifier|private
name|ModuleRevisionId
name|_mrid
decl_stmt|;
specifier|public
name|MRIDArtifactInfo
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|)
block|{
name|_mrid
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|_mrid
operator|.
name|getRevision
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|final
name|Comparator
name|COMPARATOR
init|=
operator|new
name|Comparator
argument_list|()
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
if|if
condition|(
name|o1
operator|.
name|equals
argument_list|(
name|o2
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|ArtifactInfo
name|art1
init|=
operator|new
name|MRIDArtifactInfo
argument_list|(
operator|(
name|ModuleRevisionId
operator|)
name|o1
argument_list|)
decl_stmt|;
name|ArtifactInfo
name|art2
init|=
operator|new
name|MRIDArtifactInfo
argument_list|(
operator|(
name|ModuleRevisionId
operator|)
name|o2
argument_list|)
decl_stmt|;
name|ArtifactInfo
name|art
init|=
name|getLatestStrategy
argument_list|()
operator|.
name|findLatest
argument_list|(
operator|new
name|ArtifactInfo
index|[]
block|{
name|art1
block|,
name|art2
block|}
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|art
operator|==
name|art1
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
block|}
decl_stmt|;
specifier|private
name|LatestStrategy
name|_latestStrategy
decl_stmt|;
specifier|private
name|String
name|_latestStrategyName
init|=
literal|"default"
decl_stmt|;
specifier|public
name|VersionRangeMatcher
parameter_list|()
block|{
name|super
argument_list|(
literal|"version-range"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|VersionRangeMatcher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|VersionRangeMatcher
parameter_list|(
name|String
name|name
parameter_list|,
name|LatestStrategy
name|strategy
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|_latestStrategy
operator|=
name|strategy
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDynamic
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
return|return
name|ALL_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|Matcher
name|m
decl_stmt|;
name|m
operator|=
name|FINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|lower
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|upper
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
return|return
name|isUpper
argument_list|(
name|askedMrid
argument_list|,
name|lower
argument_list|,
name|foundMrid
argument_list|,
name|revision
operator|.
name|startsWith
argument_list|(
name|OPEN_INC
argument_list|)
argument_list|)
operator|&&
name|isLower
argument_list|(
name|askedMrid
argument_list|,
name|upper
argument_list|,
name|foundMrid
argument_list|,
name|revision
operator|.
name|endsWith
argument_list|(
name|CLOSE_INC
argument_list|)
argument_list|)
return|;
block|}
name|m
operator|=
name|LOWER_INFINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|upper
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
name|isLower
argument_list|(
name|askedMrid
argument_list|,
name|upper
argument_list|,
name|foundMrid
argument_list|,
name|revision
operator|.
name|endsWith
argument_list|(
name|CLOSE_INC
argument_list|)
argument_list|)
return|;
block|}
name|m
operator|=
name|UPPER_INFINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|lower
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
name|isUpper
argument_list|(
name|askedMrid
argument_list|,
name|lower
argument_list|,
name|foundMrid
argument_list|,
name|revision
operator|.
name|startsWith
argument_list|(
name|OPEN_INC
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|isLower
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|String
name|revision
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|COMPARATOR
operator|.
name|compare
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|askedMrid
argument_list|,
name|revision
argument_list|)
argument_list|,
name|foundMrid
argument_list|)
operator|<=
operator|(
name|inclusive
condition|?
literal|0
else|:
operator|-
literal|1
operator|)
return|;
block|}
specifier|private
name|boolean
name|isUpper
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|String
name|revision
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|COMPARATOR
operator|.
name|compare
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|askedMrid
argument_list|,
name|revision
argument_list|)
argument_list|,
name|foundMrid
argument_list|)
operator|>=
operator|(
name|inclusive
condition|?
literal|0
else|:
literal|1
operator|)
return|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|ModuleRevisionId
name|askedMrid
parameter_list|,
name|ModuleRevisionId
name|foundMrid
parameter_list|,
name|Comparator
name|staticComparator
parameter_list|)
block|{
name|String
name|revision
init|=
name|askedMrid
operator|.
name|getRevision
argument_list|()
decl_stmt|;
name|Matcher
name|m
decl_stmt|;
name|m
operator|=
name|UPPER_INFINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// no upper limit, the dynamic revision can always be considered greater
return|return
literal|1
return|;
block|}
name|String
name|upper
decl_stmt|;
name|m
operator|=
name|FINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|upper
operator|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m
operator|=
name|LOWER_INFINITE_RANGE
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|upper
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"impossible to compare: askedMrid is not a dynamic revision: "
operator|+
name|askedMrid
argument_list|)
throw|;
block|}
block|}
name|int
name|c
init|=
name|staticComparator
operator|.
name|compare
argument_list|(
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|askedMrid
argument_list|,
name|upper
argument_list|)
argument_list|,
name|foundMrid
argument_list|)
decl_stmt|;
comment|// if the comparison consider them equal, we must return -1, because we can't consider the
comment|// dynamic revision to be greater. Otherwise we can safeely return the result of the static comparison
return|return
name|c
operator|==
literal|0
condition|?
operator|-
literal|1
else|:
name|c
return|;
block|}
specifier|public
name|LatestStrategy
name|getLatestStrategy
parameter_list|()
block|{
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getSettings
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"no ivy instance nor latest strategy configured in version range matcher "
operator|+
name|this
argument_list|)
throw|;
block|}
if|if
condition|(
name|_latestStrategyName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"null latest strategy defined in version range matcher "
operator|+
name|this
argument_list|)
throw|;
block|}
name|_latestStrategy
operator|=
name|getSettings
argument_list|()
operator|.
name|getLatestStrategy
argument_list|(
name|_latestStrategyName
argument_list|)
expr_stmt|;
if|if
condition|(
name|_latestStrategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unknown latest strategy '"
operator|+
name|_latestStrategyName
operator|+
literal|"' configured in version range matcher "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
return|return
name|_latestStrategy
return|;
block|}
specifier|public
name|void
name|setLatestStrategy
parameter_list|(
name|LatestStrategy
name|latestStrategy
parameter_list|)
block|{
name|_latestStrategy
operator|=
name|latestStrategy
expr_stmt|;
block|}
specifier|public
name|void
name|setLatest
parameter_list|(
name|String
name|latestStrategyName
parameter_list|)
block|{
name|_latestStrategyName
operator|=
name|latestStrategyName
expr_stmt|;
block|}
block|}
end_class

end_unit

