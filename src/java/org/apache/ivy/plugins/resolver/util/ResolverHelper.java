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
name|resolver
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|Arrays
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
name|plugins
operator|.
name|repository
operator|.
name|Repository
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
name|Resource
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

begin_class
specifier|public
class|class
name|ResolverHelper
block|{
comment|// lists all the values a token can take in a pattern, as listed by a given url lister
specifier|public
specifier|static
name|String
index|[]
name|listTokenValues
parameter_list|(
name|Repository
name|rep
parameter_list|,
name|String
name|pattern
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|String
name|fileSep
init|=
name|rep
operator|.
name|getFileSeparator
argument_list|()
decl_stmt|;
name|pattern
operator|=
name|rep
operator|.
name|standardize
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|String
name|tokenString
init|=
name|IvyPatternHelper
operator|.
name|getTokenString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
name|tokenString
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
name|Message
operator|.
name|verbose
argument_list|(
literal|"unable to list "
operator|+
name|token
operator|+
literal|" in "
operator|+
name|pattern
operator|+
literal|": token not found in pattern"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|(
operator|(
name|pattern
operator|.
name|length
argument_list|()
operator|<=
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
operator|)
operator|||
name|fileSep
operator|.
name|equals
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
argument_list|,
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
operator|)
operator|&&
operator|(
name|index
operator|==
literal|0
operator|||
name|fileSep
operator|.
name|equals
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|index
operator|-
literal|1
argument_list|,
name|index
argument_list|)
argument_list|)
operator|)
condition|)
block|{
comment|// the searched token is a whole name
name|String
name|root
init|=
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
decl_stmt|;
return|return
name|listAll
argument_list|(
name|rep
argument_list|,
name|root
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|slashIndex
init|=
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
name|fileSep
argument_list|)
decl_stmt|;
name|String
name|root
init|=
name|slashIndex
operator|==
operator|-
literal|1
condition|?
literal|""
else|:
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|slashIndex
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tusing "
operator|+
name|rep
operator|+
literal|" to list all in "
operator|+
name|root
argument_list|)
expr_stmt|;
name|List
name|all
init|=
name|rep
operator|.
name|list
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|all
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tfound "
operator|+
name|all
operator|.
name|size
argument_list|()
operator|+
literal|" urls"
argument_list|)
expr_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|endNameIndex
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
name|fileSep
argument_list|,
name|slashIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|namePattern
decl_stmt|;
if|if
condition|(
name|endNameIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|namePattern
operator|=
name|pattern
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|,
name|endNameIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|namePattern
operator|=
name|pattern
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|acceptNamePattern
init|=
literal|".*"
operator|+
name|IvyPatternHelper
operator|.
name|substituteToken
argument_list|(
name|namePattern
argument_list|,
name|token
argument_list|,
literal|"([^"
operator|+
name|fileSep
operator|+
literal|"]+)"
argument_list|)
operator|+
literal|".*"
decl_stmt|;
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|acceptNamePattern
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|all
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|value
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\t"
operator|+
name|ret
operator|.
name|size
argument_list|()
operator|+
literal|" matched "
operator|+
name|pattern
argument_list|)
expr_stmt|;
return|return
operator|(
name|String
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while listing resources in "
operator|+
name|root
operator|+
literal|" with "
operator|+
name|rep
operator|+
literal|": "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
specifier|static
name|String
index|[]
name|listAll
parameter_list|(
name|Repository
name|rep
parameter_list|,
name|String
name|parent
parameter_list|)
block|{
try|try
block|{
name|String
name|fileSep
init|=
name|rep
operator|.
name|getFileSeparator
argument_list|()
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\tusing "
operator|+
name|rep
operator|+
literal|" to list all in "
operator|+
name|parent
argument_list|)
expr_stmt|;
name|List
name|all
init|=
name|rep
operator|.
name|list
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|all
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tfound "
operator|+
name|all
operator|.
name|size
argument_list|()
operator|+
literal|" resources"
argument_list|)
expr_stmt|;
name|List
name|names
init|=
operator|new
name|ArrayList
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|all
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|fileSep
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|slashIndex
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
name|fileSep
argument_list|)
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|names
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tno resources found"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while listing resources in "
operator|+
name|parent
operator|+
literal|" with "
operator|+
name|rep
operator|+
literal|": "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|ResolvedResource
index|[]
name|findAll
parameter_list|(
name|Repository
name|rep
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
comment|// substitute all but revision
name|String
name|partiallyResolvedPattern
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|ModuleRevisionId
operator|.
name|newInstance
argument_list|(
name|mrid
argument_list|,
name|IvyPatternHelper
operator|.
name|getTokenString
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
argument_list|)
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\tlisting all in "
operator|+
name|partiallyResolvedPattern
argument_list|)
expr_stmt|;
name|String
index|[]
name|revs
init|=
name|listTokenValues
argument_list|(
name|rep
argument_list|,
name|partiallyResolvedPattern
argument_list|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|revs
operator|!=
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tfound revs: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|revs
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
name|revs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|rres
init|=
name|IvyPatternHelper
operator|.
name|substituteToken
argument_list|(
name|partiallyResolvedPattern
argument_list|,
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|,
name|revs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
try|try
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|ResolvedResource
argument_list|(
name|rep
operator|.
name|getResource
argument_list|(
name|rres
argument_list|)
argument_list|,
name|revs
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"impossible to get resource from name listed by repository: "
operator|+
name|rres
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|revs
operator|.
name|length
operator|!=
name|ret
operator|.
name|size
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tfound resolved res: "
operator|+
name|ret
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|ResolvedResource
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|ResolvedResource
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
if|else if
condition|(
name|partiallyResolvedPattern
operator|.
name|indexOf
argument_list|(
literal|"["
operator|+
name|IvyPatternHelper
operator|.
name|REVISION_KEY
operator|+
literal|"]"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
comment|// the partially resolved pattern is completely resolved, check the resource
try|try
block|{
name|Resource
name|res
init|=
name|rep
operator|.
name|getResource
argument_list|(
name|partiallyResolvedPattern
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tonly one resource found without real listing: using and defining it as working@"
operator|+
name|rep
operator|.
name|getName
argument_list|()
operator|+
literal|" revision: "
operator|+
name|res
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ResolvedResource
index|[]
block|{
operator|new
name|ResolvedResource
argument_list|(
name|res
argument_list|,
literal|"working@"
operator|+
name|rep
operator|.
name|getName
argument_list|()
argument_list|)
block|}
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\timpossible to get resource from name listed by repository: "
operator|+
name|partiallyResolvedPattern
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"\tno revision found"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|//    public static ResolvedResource[] findAll(Repository rep, ModuleRevisionId mrid, String pattern, Artifact artifact, VersionMatcher versionMatcher, ResourceMDParser mdParser) {
comment|//        // substitute all but revision
comment|//        String partiallyResolvedPattern = IvyPatternHelper.substitute(pattern, new ModuleRevisionId(mrid.getModuleId(), IvyPatternHelper.getTokenString(IvyPatternHelper.REVISION_KEY), mrid.getExtraAttributes()), artifact);
comment|//        Message.debug("\tlisting all in "+partiallyResolvedPattern);
comment|//
comment|//        String[] revs = listTokenValues(rep, partiallyResolvedPattern, IvyPatternHelper.REVISION_KEY);
comment|//        if (revs != null) {
comment|//            Message.debug("\tfound revs: "+Arrays.asList(revs));
comment|//            List ret = new ArrayList(revs.length);
comment|//            String rres = null;
comment|//            for (int i = 0; i< revs.length; i++) {
comment|//                ModuleRevisionId foundMrid = new ModuleRevisionId(mrid.getModuleId(), revs[i], mrid.getExtraAttributes());
comment|//                if (versionMatcher.accept(mrid, foundMrid)) {
comment|//                    rres = IvyPatternHelper.substituteToken(partiallyResolvedPattern, IvyPatternHelper.REVISION_KEY, revs[i]);
comment|//                    try {
comment|//                    	ResolvedResource resolvedResource;
comment|//                    	if (versionMatcher.needModuleDescriptor(mrid, foundMrid)) {
comment|//                    		resolvedResource = mdParser.parse(rep.getResource(rres), revs[i]);
comment|//                    		if (!versionMatcher.accept(mrid, ((MDResolvedResource)resolvedResource).getResolvedModuleRevision().getDescriptor())) {
comment|//                    			continue;
comment|//                    		}
comment|//                    	} else {
comment|//                    		resolvedResource = new ResolvedResource(rep.getResource(rres), revs[i]);
comment|//                    	}
comment|//                    	ret.add(resolvedResource);
comment|//                    } catch (IOException e) {
comment|//                        Message.warn("impossible to get resource from name listed by repository: "+rres+": "+e.getMessage());
comment|//                    }
comment|//                }
comment|//            }
comment|//            if (revs.length != ret.size()) {
comment|//                Message.debug("\tfound resolved res: "+ret);
comment|//            }
comment|//            return (ResolvedResource[])ret.toArray(new ResolvedResource[ret.size()]);
comment|//        } else {
comment|//            // maybe the partially resolved pattern is completely resolved ?
comment|//            try {
comment|//                Resource res = rep.getResource(partiallyResolvedPattern);
comment|//                if (res.exists()) {
comment|//                    Message.debug("\tonly one resource found without real listing: using and defining it as working@"+rep.getName()+" revision: "+res.getName());
comment|//                    return new ResolvedResource[] {new ResolvedResource(res, "working@"+rep.getName())};
comment|//                }
comment|//            } catch (IOException e) {
comment|//                Message.debug("\timpossible to get resource from name listed by repository: "+partiallyResolvedPattern+": "+e.getMessage());
comment|//            }
comment|//            Message.debug("\tno revision found");
comment|//        }
comment|//        return null;
comment|//    }
comment|// lists all the values a token can take in a pattern, as listed by a given url lister
specifier|public
specifier|static
name|String
index|[]
name|listTokenValues
parameter_list|(
name|URLLister
name|lister
parameter_list|,
name|String
name|pattern
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|pattern
operator|=
name|standardize
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
if|if
condition|(
name|lister
operator|.
name|accept
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|String
name|tokenString
init|=
name|IvyPatternHelper
operator|.
name|getTokenString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
name|tokenString
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
name|Message
operator|.
name|verbose
argument_list|(
literal|"unable to list "
operator|+
name|token
operator|+
literal|" in "
operator|+
name|pattern
operator|+
literal|": token not found in pattern"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|(
operator|(
name|pattern
operator|.
name|length
argument_list|()
operator|<=
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
operator|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
argument_list|,
name|index
operator|+
name|tokenString
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
operator|)
operator|&&
operator|(
name|index
operator|==
literal|0
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|index
operator|-
literal|1
argument_list|,
name|index
argument_list|)
argument_list|)
operator|)
condition|)
block|{
comment|// the searched token is a whole name
name|String
name|root
init|=
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|listAll
argument_list|(
name|lister
argument_list|,
operator|new
name|URL
argument_list|(
name|root
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"malformed url from pattern root: "
operator|+
name|root
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
name|int
name|slashIndex
init|=
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|String
name|root
init|=
name|slashIndex
operator|==
operator|-
literal|1
condition|?
literal|""
else|:
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|slashIndex
argument_list|)
decl_stmt|;
try|try
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tusing "
operator|+
name|lister
operator|+
literal|" to list all in "
operator|+
name|root
argument_list|)
expr_stmt|;
name|List
name|all
init|=
name|lister
operator|.
name|listAll
argument_list|(
operator|new
name|URL
argument_list|(
name|root
argument_list|)
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tfound "
operator|+
name|all
operator|.
name|size
argument_list|()
operator|+
literal|" urls"
argument_list|)
expr_stmt|;
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|endNameIndex
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|,
name|slashIndex
operator|+
literal|1
argument_list|)
decl_stmt|;
name|String
name|namePattern
decl_stmt|;
if|if
condition|(
name|endNameIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|namePattern
operator|=
name|pattern
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|,
name|endNameIndex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|namePattern
operator|=
name|pattern
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|acceptNamePattern
init|=
literal|".*"
operator|+
name|IvyPatternHelper
operator|.
name|substituteToken
argument_list|(
name|namePattern
argument_list|,
name|token
argument_list|,
literal|"([^/]+)"
argument_list|)
operator|+
literal|".*"
decl_stmt|;
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|acceptNamePattern
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|all
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|URL
name|url
init|=
operator|(
name|URL
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|standardize
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|value
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\t"
operator|+
name|ret
operator|.
name|size
argument_list|()
operator|+
literal|" matched "
operator|+
name|pattern
argument_list|)
expr_stmt|;
return|return
operator|(
name|String
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while listing files in "
operator|+
name|root
operator|+
literal|": "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|String
name|standardize
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|listAll
parameter_list|(
name|URLLister
name|lister
parameter_list|,
name|URL
name|root
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|lister
operator|.
name|accept
argument_list|(
name|root
operator|.
name|toExternalForm
argument_list|()
argument_list|)
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\tusing "
operator|+
name|lister
operator|+
literal|" to list all in "
operator|+
name|root
argument_list|)
expr_stmt|;
name|List
name|all
init|=
name|lister
operator|.
name|listAll
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tfound "
operator|+
name|all
operator|.
name|size
argument_list|()
operator|+
literal|" urls"
argument_list|)
expr_stmt|;
name|List
name|names
init|=
operator|new
name|ArrayList
argument_list|(
name|all
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|all
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|URL
name|dir
init|=
operator|(
name|URL
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|dir
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|slashIndex
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
name|path
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|String
index|[]
operator|)
name|names
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"problem while listing directories in "
operator|+
name|root
operator|+
literal|": "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|+
literal|" "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit
