begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A {@link VersionMatcher} which understands {@code Maven timestamped snapshots}.  */
end_comment

begin_class
specifier|public
class|class
name|MavenTimedSnapshotVersionMatcher
extends|extends
name|AbstractVersionMatcher
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SNAPSHOT_SUFFIX
init|=
literal|"-SNAPSHOT"
decl_stmt|;
comment|// The timestamped snapshot pattern that Maven uses
specifier|private
specifier|static
specifier|final
name|Pattern
name|M2_TIMESTAMPED_SNAPSHOT_REV_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(.*)-([0-9]{8}.[0-9]{6})-([0-9]+)$"
argument_list|)
decl_stmt|;
specifier|public
name|MavenTimedSnapshotVersionMatcher
parameter_list|()
block|{
name|super
argument_list|(
literal|"maven-timed-snapshot"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDynamic
parameter_list|(
specifier|final
name|ModuleRevisionId
name|askedMrid
parameter_list|)
block|{
if|if
condition|(
name|askedMrid
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// we consider only timestamped snapshots as dynamic, since unlike regular snapshots,
comment|// a timestamped snapshot version of the form x.y.z-<timestamped-part> represents the real x.y.z-SNAPSHOT version
specifier|final
name|Matcher
name|snapshotPatternMatcher
init|=
name|M2_TIMESTAMPED_SNAPSHOT_REV_PATTERN
operator|.
name|matcher
argument_list|(
name|askedMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|snapshotPatternMatcher
operator|.
name|matches
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
specifier|final
name|ModuleRevisionId
name|askedMrid
parameter_list|,
specifier|final
name|ModuleRevisionId
name|foundMrid
parameter_list|)
block|{
if|if
condition|(
name|askedMrid
operator|==
literal|null
operator|||
name|foundMrid
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|MavenSnapshotRevision
name|askedSnapshotVersion
init|=
name|computeIfSnapshot
argument_list|(
name|askedMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|askedSnapshotVersion
operator|==
literal|null
condition|)
block|{
comment|// this isn't a snapshot, so we aren't interested in it
return|return
literal|false
return|;
block|}
comment|// this version matcher only comes into picture if we have been asked to deal with a timestamped snapshot.
comment|// In other words, if the asked version isn't a timestamped snapshot, then we don't accept it
if|if
condition|(
operator|!
name|askedSnapshotVersion
operator|.
name|isTimestampedSnapshot
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|MavenSnapshotRevision
name|foundSnapshotVersion
init|=
name|computeIfSnapshot
argument_list|(
name|foundMrid
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|foundSnapshotVersion
operator|==
literal|null
condition|)
block|{
comment|// this isn't a snapshot, so we aren't interested in it
return|return
literal|false
return|;
block|}
comment|// we compare the base revisions of both these snapshot to see if they are the same revision
comment|// and if they are then we accept the "found" MRID for the "asked" MRID
return|return
name|askedSnapshotVersion
operator|.
name|baseRevision
operator|.
name|equals
argument_list|(
name|foundSnapshotVersion
operator|.
name|baseRevision
argument_list|)
return|;
block|}
comment|/**      * Parses the passed {@code revision} and returns a {@link MavenSnapshotRevision}, representing that {@code revision},      * if it is either a regular snapshot (for example: 1.0.2-SNAPSHOT) or a timestamped snapshot (for example: 1.0.2-20100925.223013-19).      * If the passed {@code revision} isn't a snapshot revision, then this method returns null      *      * @param revision The revision to parse      * @return      */
specifier|public
specifier|static
name|MavenSnapshotRevision
name|computeIfSnapshot
parameter_list|(
specifier|final
name|String
name|revision
parameter_list|)
block|{
if|if
condition|(
name|revision
operator|==
literal|null
operator|||
name|revision
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
specifier|final
name|boolean
name|regularSnapshot
init|=
name|revision
operator|.
name|endsWith
argument_list|(
name|SNAPSHOT_SUFFIX
argument_list|)
decl_stmt|;
specifier|final
name|Matcher
name|snapshotPatternMatcher
init|=
name|M2_TIMESTAMPED_SNAPSHOT_REV_PATTERN
operator|.
name|matcher
argument_list|(
name|revision
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|timestampedSnaphost
init|=
name|snapshotPatternMatcher
operator|.
name|matches
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|regularSnapshot
operator|&&
operator|!
name|timestampedSnaphost
condition|)
block|{
comment|// neither a regular snapshot nor a timestamped snapshot
return|return
literal|null
return|;
block|}
comment|// the revision is now identified as a snapshot (either a regular one or a timestamped one)
return|return
name|timestampedSnaphost
condition|?
operator|new
name|MavenSnapshotRevision
argument_list|(
literal|true
argument_list|,
name|revision
argument_list|,
name|snapshotPatternMatcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
else|:
operator|new
name|MavenSnapshotRevision
argument_list|(
literal|false
argument_list|,
name|revision
argument_list|,
name|revision
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|revision
operator|.
name|indexOf
argument_list|(
name|SNAPSHOT_SUFFIX
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Represents a Maven 2 snapshot version, which is either a regular snapshot (for example: 1.0.2-SNAPSHOT)      * or a timestamped snapshot (for example: 1.0.2-20100925.223013-19)      */
specifier|public
specifier|static
specifier|final
class|class
name|MavenSnapshotRevision
block|{
specifier|private
specifier|final
name|boolean
name|timedsnapshot
decl_stmt|;
specifier|private
specifier|final
name|String
name|wholeRevision
decl_stmt|;
specifier|private
specifier|final
name|String
name|baseRevision
decl_stmt|;
specifier|private
name|MavenSnapshotRevision
parameter_list|(
specifier|final
name|boolean
name|timedsnapshot
parameter_list|,
specifier|final
name|String
name|wholeRevision
parameter_list|,
specifier|final
name|String
name|baseRevision
parameter_list|)
block|{
if|if
condition|(
name|wholeRevision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Revision, of a Maven snapshot, cannot be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|baseRevision
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Base revision, of a Maven snapshot revision, cannot be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|timedsnapshot
operator|=
name|timedsnapshot
expr_stmt|;
name|this
operator|.
name|wholeRevision
operator|=
name|wholeRevision
expr_stmt|;
name|this
operator|.
name|baseRevision
operator|=
name|baseRevision
expr_stmt|;
block|}
comment|/**          * Returns true if this {@link MavenSnapshotRevision} represents a timestamped snapshot version. Else returns false.          *          * @return          */
specifier|public
name|boolean
name|isTimestampedSnapshot
parameter_list|()
block|{
return|return
name|this
operator|.
name|timedsnapshot
return|;
block|}
comment|/**          * Returns the "base" revision that this {@link MavenSnapshotRevision} represents. For example, for the regular snapshot revision          * {@link 1.2.3-SNAPSHOT}, the base revision is {@code 1.2.3}. Similarly for timestamped snapshot version          * {@code 1.0.2-20100925.223013-19}, the base revision is {@link 1.0.2}          *          * @return          */
specifier|public
name|String
name|getBaseRevision
parameter_list|()
block|{
return|return
name|this
operator|.
name|baseRevision
return|;
block|}
comment|/**          * Returns the complete/whole revision this {@link MavenSnapshotRevision} represents. For example, if this {@link MavenSnapshotRevision}          * represents a regular snapshot {@code 1.3.4-SNAPSHOT} revision then this method returns {@code 1.3.4-SNAPSHOT}. Similarly, if this          * {@link MavenSnapshotRevision} represents a timestamped snapshot {@code 1.0.2-20100925.223013-19} revision, then this method returns          * {@code 1.0.2-20100925.223013-19}          *          * @return          */
specifier|public
name|String
name|getRevision
parameter_list|()
block|{
return|return
name|this
operator|.
name|wholeRevision
return|;
block|}
block|}
block|}
end_class

end_unit

