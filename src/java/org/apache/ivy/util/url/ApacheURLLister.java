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
name|util
operator|.
name|url
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
name|util
operator|.
name|FileUtil
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
name|url
operator|.
name|URLHandler
operator|.
name|URLInfo
import|;
end_import

begin_comment
comment|/**  * Utility class which helps to list urls under a given url. This has been tested with Apache 1.3.33  * server listing, as the one used at ibiblio, and with Apache 2.0.53 server listing, as the one on  * mirrors.sunsite.dk.  */
end_comment

begin_class
specifier|public
class|class
name|ApacheURLLister
block|{
comment|// ~ Static variables/initializers ------------------------------------------
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"<a[^>]*href=\"([^\"]*)\"[^>]*>(?:<[^>]+>)*?([^<>]+?)(?:<[^>]+>)*?</a>"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
comment|// ~ Methods ----------------------------------------------------------------
comment|/**      * Returns a list of sub urls of the given url. The returned list is a list of URL.      *       * @param url      *            The base URL from which to retrieve the listing.      * @return a list of sub urls of the given url.      * @throws IOException      *             If an error occures retrieving the HTML.      */
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|listAll
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|retrieveListing
argument_list|(
name|url
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns a list of sub 'directories' of the given url. The returned list is a list of URL.      *       * @param url      *            The base URL from which to retrieve the listing.      * @return a list of sub 'directories' of the given url.      * @throws IOException      *             If an error occures retrieving the HTML.      */
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|listDirectories
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|retrieveListing
argument_list|(
name|url
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns a list of sub 'files' (in opposition to directories) of the given url. The returned      * list is a list of URL.      *       * @param url      *            The base URL from which to retrieve the listing.      * @return a list of sub 'files' of the given url.      * @throws IOException      *             If an error occures retrieving the HTML.      */
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|listFiles
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|retrieveListing
argument_list|(
name|url
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Retrieves a {@link List} of {@link URL}s corresponding to the files and/or directories found      * at the supplied base URL.      *       * @param url      *            The base URL from which to retrieve the listing.      * @param includeFiles      *            If true include files in the returned list.      * @param includeDirectories      *            If true include directories in the returned list.      * @return A {@link List} of {@link URL}s.      * @throws IOException      *             If an error occures retrieving the HTML.      */
specifier|public
name|List
argument_list|<
name|URL
argument_list|>
name|retrieveListing
parameter_list|(
name|URL
name|url
parameter_list|,
name|boolean
name|includeFiles
parameter_list|,
name|boolean
name|includeDirectories
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|URL
argument_list|>
name|urlList
init|=
operator|new
name|ArrayList
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
comment|// add trailing slash for relative urls
if|if
condition|(
operator|!
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
operator|!
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".html"
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|url
operator|.
name|getHost
argument_list|()
argument_list|,
name|url
operator|.
name|getPort
argument_list|()
argument_list|,
name|url
operator|.
name|getPath
argument_list|()
operator|+
literal|"/"
argument_list|)
expr_stmt|;
block|}
name|URLHandler
name|urlHandler
init|=
name|URLHandlerRegistry
operator|.
name|getDefault
argument_list|()
decl_stmt|;
name|URLInfo
name|urlInfo
init|=
name|urlHandler
operator|.
name|getURLInfo
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|urlInfo
operator|==
name|URLHandler
operator|.
name|UNAVAILABLE
condition|)
block|{
return|return
name|urlList
return|;
comment|// not found => return empty list
block|}
comment|// here, urlInfo is valid
name|String
name|charset
init|=
name|urlInfo
operator|.
name|getBodyCharset
argument_list|()
decl_stmt|;
name|InputStream
name|contentStream
init|=
name|urlHandler
operator|.
name|openStream
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|BufferedReader
name|r
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|contentStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|contentStream
argument_list|,
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|htmlText
init|=
name|FileUtil
operator|.
name|readEntirely
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|htmlText
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
comment|// get the href text and the displayed text
name|String
name|href
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|href
operator|==
literal|null
operator|)
operator|||
operator|(
name|text
operator|==
literal|null
operator|)
condition|)
block|{
comment|// the groups were not found (shouldn't happen, really)
continue|continue;
block|}
name|text
operator|=
name|text
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// handle complete URL listings
if|if
condition|(
name|href
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|href
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
condition|)
block|{
try|try
block|{
name|href
operator|=
operator|new
name|URL
argument_list|(
name|href
argument_list|)
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|href
operator|.
name|startsWith
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
comment|// ignore URLs which aren't children of the base URL
continue|continue;
block|}
name|href
operator|=
name|href
operator|.
name|substring
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{
comment|// incorrect URL, ignore
continue|continue;
block|}
block|}
if|if
condition|(
name|href
operator|.
name|startsWith
argument_list|(
literal|"../"
argument_list|)
condition|)
block|{
comment|// we are only interested in sub-URLs, not parent URLs, so skip this one
continue|continue;
block|}
comment|// absolute href: convert to relative one
if|if
condition|(
name|href
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|int
name|slashIndex
init|=
name|href
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|href
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|href
operator|=
name|href
operator|.
name|substring
argument_list|(
name|slashIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// relative to current href: convert to simple relative one
if|if
condition|(
name|href
operator|.
name|startsWith
argument_list|(
literal|"./"
argument_list|)
condition|)
block|{
name|href
operator|=
name|href
operator|.
name|substring
argument_list|(
literal|"./"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// exclude those where they do not match
comment|// href will never be truncated, text may be truncated by apache
if|if
condition|(
name|text
operator|.
name|endsWith
argument_list|(
literal|"..>"
argument_list|)
condition|)
block|{
comment|// text is probably truncated, we can only check if the href starts with text
if|if
condition|(
operator|!
name|href
operator|.
name|startsWith
argument_list|(
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
if|else if
condition|(
name|text
operator|.
name|endsWith
argument_list|(
literal|"..&gt;"
argument_list|)
condition|)
block|{
comment|// text is probably truncated, we can only check if the href starts with text
if|if
condition|(
operator|!
name|href
operator|.
name|startsWith
argument_list|(
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|length
argument_list|()
operator|-
literal|6
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
else|else
block|{
comment|// text is not truncated, so it must match the url after stripping optional
comment|// trailing slashes
name|String
name|strippedHref
init|=
name|href
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|href
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|href
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|href
decl_stmt|;
name|String
name|strippedText
init|=
name|text
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|text
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|text
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|text
decl_stmt|;
if|if
condition|(
operator|!
name|strippedHref
operator|.
name|equalsIgnoreCase
argument_list|(
name|strippedText
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
name|boolean
name|directory
init|=
name|href
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|directory
operator|&&
name|includeDirectories
operator|)
operator|||
operator|(
operator|!
name|directory
operator|&&
name|includeFiles
operator|)
condition|)
block|{
name|URL
name|child
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|,
name|href
argument_list|)
decl_stmt|;
name|urlList
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"ApacheURLLister found URL=["
operator|+
name|child
operator|+
literal|"]."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|urlList
return|;
block|}
block|}
end_class

end_unit

