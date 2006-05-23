begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is subject to the license found in LICENCE.TXT in the root directory of the project.  *   * #SNAPSHOT#  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
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
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ArtifactInfo
import|;
end_import

begin_class
specifier|public
class|class
name|LatestLexicographicStrategy
extends|extends
name|ComparatorLatestStrategy
block|{
comment|/**      * Compares two revisions.      * Revisions are compared lexicographically unless      * a 'latest' revision is found. If the latest revision found      * is an absolute latest (latest. like), then it is assumed to be the greater.      * If a partial latest is found, then it is assumed to be greater      * than any matching fixed revision.       */
specifier|private
specifier|static
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
if|if
condition|(
name|rev1
operator|.
name|startsWith
argument_list|(
literal|"latest"
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|rev1
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
operator|&&
name|rev2
operator|.
name|startsWith
argument_list|(
name|rev1
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|rev1
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|rev2
operator|.
name|startsWith
argument_list|(
literal|"latest"
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
name|rev2
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
operator|&&
name|rev1
operator|.
name|startsWith
argument_list|(
name|rev2
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|rev2
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|rev1
operator|.
name|compareTo
argument_list|(
name|rev2
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
name|LatestLexicographicStrategy
parameter_list|()
block|{
name|super
argument_list|(
name|COMPARATOR
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"latest-lexico"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

