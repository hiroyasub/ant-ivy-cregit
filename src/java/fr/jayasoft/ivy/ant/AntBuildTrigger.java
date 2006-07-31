begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|ant
package|;
end_package

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
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|taskdefs
operator|.
name|Ant
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
name|event
operator|.
name|AbstractTrigger
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
name|event
operator|.
name|IvyEvent
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
name|event
operator|.
name|Trigger
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
name|util
operator|.
name|IvyPatternHelper
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
name|util
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Triggers an ant build on an event occurence.  *   * Example of use:  *<ant-build-trigger event="pre-resolve-dependency" filter="revision=latest.integration"  *                    antfile="/path/to/[module]/build.xml" target="compile"/>  * Triggers an ant build for any dependency in asked in latest.integration, just before resolving the   * dependency.  *   * @author Xavier Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|AntBuildTrigger
extends|extends
name|AbstractTrigger
implements|implements
name|Trigger
block|{
specifier|private
name|boolean
name|_onlyonce
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|_target
init|=
literal|null
decl_stmt|;
specifier|private
name|Collection
name|_builds
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|String
name|_buildFilePattern
decl_stmt|;
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
name|File
name|f
init|=
name|getBuildFile
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|_onlyonce
operator|&&
name|isBuilt
argument_list|(
name|f
argument_list|)
condition|)
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"dependency already built, skipping: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Ant
name|ant
init|=
operator|new
name|Ant
argument_list|()
decl_stmt|;
name|ant
operator|.
name|setTaskName
argument_list|(
literal|"ant"
argument_list|)
expr_stmt|;
name|ant
operator|.
name|setAntfile
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|target
init|=
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|ant
operator|.
name|setTarget
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
name|ant
operator|.
name|execute
argument_list|()
expr_stmt|;
name|markBuilt
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"no build file found for dependency, skipping: "
operator|+
name|f
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|markBuilt
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|_builds
operator|.
name|add
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isBuilt
parameter_list|(
name|File
name|f
parameter_list|)
block|{
return|return
name|_builds
operator|.
name|contains
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|File
name|getBuildFile
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|IvyPatternHelper
operator|.
name|substituteTokens
argument_list|(
name|getBuildFilePattern
argument_list|()
argument_list|,
name|event
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|getBuildFilePattern
parameter_list|()
block|{
return|return
name|_buildFilePattern
return|;
block|}
specifier|private
name|void
name|setAntfile
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|_buildFilePattern
operator|=
name|pattern
expr_stmt|;
block|}
specifier|public
name|String
name|getTarget
parameter_list|()
block|{
return|return
name|_target
return|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|target
parameter_list|)
block|{
name|_target
operator|=
name|target
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOnlyonce
parameter_list|()
block|{
return|return
name|_onlyonce
return|;
block|}
specifier|public
name|void
name|setOnlyonce
parameter_list|(
name|boolean
name|onlyonce
parameter_list|)
block|{
name|_onlyonce
operator|=
name|onlyonce
expr_stmt|;
block|}
block|}
end_class

end_unit

