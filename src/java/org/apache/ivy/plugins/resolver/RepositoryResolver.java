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
name|resolver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|text
operator|.
name|ParseException
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|event
operator|.
name|EventManager
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
name|descriptor
operator|.
name|ModuleDescriptor
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
name|report
operator|.
name|DownloadReport
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
name|resolve
operator|.
name|DownloadOptions
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
name|parser
operator|.
name|ModuleDescriptorParser
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
name|parser
operator|.
name|ModuleDescriptorParserRegistry
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
name|AbstractRepository
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
name|plugins
operator|.
name|resolver
operator|.
name|util
operator|.
name|ResolvedResource
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
name|resolver
operator|.
name|util
operator|.
name|ResolverHelper
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
name|resolver
operator|.
name|util
operator|.
name|ResourceMDParser
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
name|signer
operator|.
name|SignatureGenerator
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
name|ChecksumHelper
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
import|import static
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|util
operator|.
name|StringUtils
operator|.
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryResolver
extends|extends
name|AbstractPatternsBasedResolver
block|{
specifier|private
name|Repository
name|repository
decl_stmt|;
specifier|private
name|Boolean
name|alwaysCheckExactRevision
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|signerName
init|=
literal|null
decl_stmt|;
specifier|public
name|RepositoryResolver
parameter_list|()
block|{
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|instanceof
name|AbstractRepository
condition|)
block|{
operator|(
operator|(
name|AbstractRepository
operator|)
name|repository
operator|)
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSigner
parameter_list|(
name|String
name|signerName
parameter_list|)
block|{
name|this
operator|.
name|signerName
operator|=
name|signerName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ResolvedResource
name|findResourceUsingPattern
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ResourceMDParser
name|rmdparser
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
name|VersionMatcher
name|versionMatcher
init|=
name|getSettings
argument_list|()
operator|.
name|getVersionMatcher
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|versionMatcher
operator|.
name|isDynamic
argument_list|(
name|mrid
argument_list|)
operator|||
name|isAlwaysCheckExactRevision
argument_list|()
condition|)
block|{
name|String
name|resourceName
init|=
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t trying "
operator|+
name|resourceName
argument_list|)
expr_stmt|;
name|logAttempt
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
name|Resource
name|res
init|=
name|repository
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
name|boolean
name|reachable
init|=
name|res
operator|.
name|exists
argument_list|()
decl_stmt|;
if|if
condition|(
name|reachable
condition|)
block|{
name|String
name|revision
decl_stmt|;
if|if
condition|(
name|pattern
operator|.
name|contains
argument_list|(
name|IvyPatternHelper
operator|.
name|REVISION_KEY
argument_list|)
condition|)
block|{
name|revision
operator|=
name|mrid
operator|.
name|getRevision
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|||
literal|"pom"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
comment|// we can't determine the revision from the pattern, get it
comment|// from the module descriptor itself
name|File
name|temp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivy"
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
argument_list|)
decl_stmt|;
name|temp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|repository
operator|.
name|get
argument_list|(
name|res
operator|.
name|getName
argument_list|()
argument_list|,
name|temp
argument_list|)
expr_stmt|;
name|ModuleDescriptorParser
name|parser
init|=
name|ModuleDescriptorParserRegistry
operator|.
name|getInstance
argument_list|()
operator|.
name|getParser
argument_list|(
name|res
argument_list|)
decl_stmt|;
name|ModuleDescriptor
name|md
init|=
name|parser
operator|.
name|parseDescriptor
argument_list|(
name|getParserSettings
argument_list|()
argument_list|,
name|temp
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|res
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|revision
operator|=
name|md
operator|.
name|getRevision
argument_list|()
expr_stmt|;
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|revision
argument_list|)
condition|)
block|{
name|revision
operator|=
literal|"working@"
operator|+
name|name
expr_stmt|;
block|}
block|}
else|else
block|{
name|revision
operator|=
literal|"working@"
operator|+
name|name
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ResolvedResource
argument_list|(
name|res
argument_list|,
name|revision
argument_list|)
return|;
block|}
if|else if
condition|(
name|versionMatcher
operator|.
name|isDynamic
argument_list|(
name|mrid
argument_list|)
condition|)
block|{
return|return
name|findDynamicResourceUsingPattern
argument_list|(
name|rmdparser
argument_list|,
name|mrid
argument_list|,
name|pattern
argument_list|,
name|artifact
argument_list|,
name|date
argument_list|)
return|;
block|}
else|else
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": resource not reachable for "
operator|+
name|mrid
operator|+
literal|": res="
operator|+
name|res
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
name|findDynamicResourceUsingPattern
argument_list|(
name|rmdparser
argument_list|,
name|mrid
argument_list|,
name|pattern
argument_list|,
name|artifact
argument_list|,
name|date
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ParseException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|name
operator|+
literal|": unable to get resource for "
operator|+
name|mrid
operator|+
literal|": res="
operator|+
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|)
operator|+
literal|": "
operator|+
name|ex
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|ResolvedResource
name|findDynamicResourceUsingPattern
parameter_list|(
name|ResourceMDParser
name|rmdparser
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|Date
name|date
parameter_list|)
block|{
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
name|logAttempt
argument_list|(
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
argument_list|)
expr_stmt|;
name|ResolvedResource
index|[]
name|rress
init|=
name|listResources
argument_list|(
name|repository
argument_list|,
name|mrid
argument_list|,
name|pattern
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|rress
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": unable to list resources for "
operator|+
name|mrid
operator|+
literal|": pattern="
operator|+
name|pattern
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
name|ResolvedResource
name|found
init|=
name|findResource
argument_list|(
name|rress
argument_list|,
name|rmdparser
argument_list|,
name|mrid
argument_list|,
name|date
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
name|Message
operator|.
name|debug
argument_list|(
literal|"\t"
operator|+
name|name
operator|+
literal|": no resource found for "
operator|+
name|mrid
operator|+
literal|": pattern="
operator|+
name|pattern
argument_list|)
expr_stmt|;
block|}
return|return
name|found
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Resource
name|getResource
parameter_list|(
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|repository
operator|.
name|getResource
argument_list|(
name|source
argument_list|)
return|;
block|}
comment|/**      * List all revisions as resolved resources for the given artifact in the given repository using      * the given pattern, and using the given mrid except its revision.      *      * @param repository      *            the repository in which revisions should be located      * @param mrid      *            the module revision id to look for (except revision)      * @param pattern      *            the pattern to use to locate the revisions      * @param artifact      *            the artifact to find      * @return an array of ResolvedResource, all pointing to a different revision of the given      *         Artifact.      */
specifier|protected
name|ResolvedResource
index|[]
name|listResources
parameter_list|(
name|Repository
name|repository
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
return|return
name|ResolverHelper
operator|.
name|findAll
argument_list|(
name|repository
argument_list|,
name|mrid
argument_list|,
name|pattern
argument_list|,
name|artifact
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|long
name|get
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|File
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"\t"
operator|+
name|getName
argument_list|()
operator|+
literal|": downloading "
operator|+
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\tto "
operator|+
name|dest
argument_list|)
expr_stmt|;
if|if
condition|(
name|dest
operator|.
name|getParentFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dest
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|repository
operator|.
name|get
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|,
name|dest
argument_list|)
expr_stmt|;
return|return
name|dest
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|void
name|publish
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|destPattern
decl_stmt|;
if|if
condition|(
literal|"ivy"
operator|.
name|equals
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
operator|!
name|getIvyPatterns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|destPattern
operator|=
name|getIvyPatterns
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|getArtifactPatterns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|destPattern
operator|=
name|getArtifactPatterns
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"impossible to publish "
operator|+
name|artifact
operator|+
literal|" using "
operator|+
name|this
operator|+
literal|": no artifact pattern defined"
argument_list|)
throw|;
block|}
comment|// Check for m2 compatibility
name|ModuleRevisionId
name|mrid
init|=
name|artifact
operator|.
name|getModuleRevisionId
argument_list|()
decl_stmt|;
if|if
condition|(
name|isM2compatible
argument_list|()
condition|)
block|{
name|mrid
operator|=
name|convertM2IdForResourceSearch
argument_list|(
name|mrid
argument_list|)
expr_stmt|;
block|}
name|String
name|dest
init|=
name|getDestination
argument_list|(
name|destPattern
argument_list|,
name|artifact
argument_list|,
name|mrid
argument_list|)
decl_stmt|;
name|put
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|dest
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|Message
operator|.
name|info
argument_list|(
literal|"\tpublished "
operator|+
name|artifact
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|hidePassword
argument_list|(
name|repository
operator|.
name|standardize
argument_list|(
name|dest
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getDestination
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
name|IvyPatternHelper
operator|.
name|substitute
argument_list|(
name|pattern
argument_list|,
name|mrid
argument_list|,
name|artifact
argument_list|)
return|;
block|}
specifier|protected
name|void
name|put
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|String
name|dest
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
comment|// verify the checksum algorithms before uploading artifacts!
name|String
index|[]
name|checksums
init|=
name|getChecksumAlgorithms
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|checksum
range|:
name|checksums
control|)
block|{
if|if
condition|(
operator|!
name|ChecksumHelper
operator|.
name|isKnownAlgorithm
argument_list|(
name|checksum
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown checksum algorithm: "
operator|+
name|checksum
argument_list|)
throw|;
block|}
block|}
name|repository
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|dest
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|checksum
range|:
name|checksums
control|)
block|{
name|putChecksum
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|dest
argument_list|,
name|overwrite
argument_list|,
name|checksum
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signerName
operator|!=
literal|null
condition|)
block|{
name|putSignature
argument_list|(
name|artifact
argument_list|,
name|src
argument_list|,
name|dest
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|putChecksum
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|String
name|dest
parameter_list|,
name|boolean
name|overwrite
parameter_list|,
name|String
name|algorithm
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|csFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivytemp"
argument_list|,
name|algorithm
argument_list|)
decl_stmt|;
try|try
block|{
name|FileUtil
operator|.
name|copy
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|ChecksumHelper
operator|.
name|computeAsString
argument_list|(
name|src
argument_list|,
name|algorithm
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|csFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|repository
operator|.
name|put
argument_list|(
name|DefaultArtifact
operator|.
name|cloneWithAnotherTypeAndExt
argument_list|(
name|artifact
argument_list|,
name|algorithm
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
operator|+
literal|"."
operator|+
name|algorithm
argument_list|)
argument_list|,
name|csFile
argument_list|,
name|chopQuery
argument_list|(
name|dest
argument_list|,
name|algorithm
argument_list|)
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|csFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|putSignature
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|src
parameter_list|,
name|String
name|dest
parameter_list|,
name|boolean
name|overwrite
parameter_list|)
throws|throws
name|IOException
block|{
name|SignatureGenerator
name|gen
init|=
name|getSettings
argument_list|()
operator|.
name|getSignatureGenerator
argument_list|(
name|signerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|gen
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Couldn't sign the artifacts! "
operator|+
literal|"Unknown signer name: '"
operator|+
name|signerName
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|File
name|tempFile
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivytemp"
argument_list|,
name|gen
operator|.
name|getExtension
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|gen
operator|.
name|sign
argument_list|(
name|src
argument_list|,
name|tempFile
argument_list|)
expr_stmt|;
name|repository
operator|.
name|put
argument_list|(
name|DefaultArtifact
operator|.
name|cloneWithAnotherTypeAndExt
argument_list|(
name|artifact
argument_list|,
name|gen
operator|.
name|getExtension
argument_list|()
argument_list|,
name|artifact
operator|.
name|getExt
argument_list|()
operator|+
literal|"."
operator|+
name|gen
operator|.
name|getExtension
argument_list|()
argument_list|)
argument_list|,
name|tempFile
argument_list|,
name|chopQuery
argument_list|(
name|dest
argument_list|,
name|gen
operator|.
name|getExtension
argument_list|()
argument_list|)
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|tempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|chopQuery
parameter_list|(
name|String
name|dest
parameter_list|,
name|String
name|algorithm
parameter_list|)
block|{
if|if
condition|(
operator|!
name|dest
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
return|return
name|dest
operator|+
literal|"."
operator|+
name|algorithm
return|;
block|}
try|try
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|dest
argument_list|)
decl_stmt|;
name|String
name|query
init|=
name|url
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|query
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|dest
operator|.
name|replace
argument_list|(
literal|"?"
operator|+
name|query
argument_list|,
literal|""
argument_list|)
operator|+
literal|"."
operator|+
name|algorithm
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|DownloadReport
name|download
parameter_list|(
name|Artifact
index|[]
name|artifacts
parameter_list|,
name|DownloadOptions
name|options
parameter_list|)
block|{
name|EventManager
name|eventManager
init|=
name|getEventManager
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|download
argument_list|(
name|artifacts
argument_list|,
name|options
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|eventManager
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|removeTransferListener
argument_list|(
name|eventManager
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|findTokenValues
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|names
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tokenValues
parameter_list|,
name|String
name|token
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|String
name|partiallyResolvedPattern
init|=
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
name|pattern
argument_list|,
name|tokenValues
argument_list|)
decl_stmt|;
name|String
index|[]
name|values
init|=
name|ResolverHelper
operator|.
name|listTokenValues
argument_list|(
name|repository
argument_list|,
name|partiallyResolvedPattern
argument_list|,
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|!=
literal|null
condition|)
block|{
name|names
operator|.
name|addAll
argument_list|(
name|filterNames
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|token
parameter_list|)
block|{
return|return
name|ResolverHelper
operator|.
name|listTokenValues
argument_list|(
name|repository
argument_list|,
name|pattern
argument_list|,
name|token
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|exist
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|Resource
name|resource
init|=
name|repository
operator|.
name|getResource
argument_list|(
name|path
argument_list|)
decl_stmt|;
return|return
name|resource
operator|.
name|exists
argument_list|()
return|;
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
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTypeName
parameter_list|()
block|{
return|return
literal|"repository"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dumpSettings
parameter_list|()
block|{
name|super
operator|.
name|dumpSettings
argument_list|()
expr_stmt|;
name|Message
operator|.
name|debug
argument_list|(
literal|"\t\trepository: "
operator|+
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setSettings
parameter_list|(
name|ResolverSettings
name|settings
parameter_list|)
block|{
name|super
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
if|if
condition|(
name|settings
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|alwaysCheckExactRevision
operator|==
literal|null
condition|)
block|{
name|alwaysCheckExactRevision
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|settings
operator|.
name|getVariable
argument_list|(
literal|"ivy.default.always.check.exact.revision"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isAlwaysCheckExactRevision
parameter_list|()
block|{
return|return
name|alwaysCheckExactRevision
operator|==
literal|null
operator|||
name|alwaysCheckExactRevision
return|;
block|}
specifier|public
name|void
name|setAlwaysCheckExactRevision
parameter_list|(
name|boolean
name|alwaysCheckExactRevision
parameter_list|)
block|{
name|this
operator|.
name|alwaysCheckExactRevision
operator|=
name|alwaysCheckExactRevision
expr_stmt|;
block|}
block|}
end_class

end_unit

