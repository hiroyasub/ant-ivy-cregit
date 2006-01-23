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
name|HashMap
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
name|LinkedHashMap
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
name|regex
operator|.
name|Pattern
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
name|namespace
operator|.
name|NameSpaceHelper
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
name|namespace
operator|.
name|NamespaceTransformer
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
comment|/**  * @author X.Hanin  *  */
end_comment

begin_class
specifier|public
class|class
name|DefaultModuleDescriptor
implements|implements
name|ModuleDescriptor
block|{
specifier|public
specifier|static
name|DefaultModuleDescriptor
name|newDefaultInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
return|return
name|newDefaultInstance
argument_list|(
name|mrid
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DefaultModuleDescriptor
name|newDefaultInstance
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|DependencyArtifactDescriptor
index|[]
name|artifacts
parameter_list|)
block|{
name|DefaultModuleDescriptor
name|moduleDescriptor
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|(
name|mrid
argument_list|,
literal|"release"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|moduleDescriptor
operator|.
name|addConfiguration
argument_list|(
operator|new
name|Configuration
argument_list|(
name|DEFAULT_CONFIGURATION
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifacts
operator|!=
literal|null
operator|&&
name|artifacts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|artifacts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|moduleDescriptor
operator|.
name|addArtifact
argument_list|(
name|DEFAULT_CONFIGURATION
argument_list|,
operator|new
name|MDArtifact
argument_list|(
name|moduleDescriptor
argument_list|,
name|artifacts
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|artifacts
index|[
name|i
index|]
operator|.
name|getType
argument_list|()
argument_list|,
name|artifacts
index|[
name|i
index|]
operator|.
name|getExt
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|moduleDescriptor
operator|.
name|addArtifact
argument_list|(
name|DEFAULT_CONFIGURATION
argument_list|,
operator|new
name|MDArtifact
argument_list|(
name|moduleDescriptor
argument_list|,
name|mrid
operator|.
name|getName
argument_list|()
argument_list|,
literal|"jar"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|moduleDescriptor
operator|.
name|setLastModified
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|moduleDescriptor
return|;
block|}
specifier|public
specifier|static
name|ModuleDescriptor
name|transformInstance
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|NamespaceTransformer
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|.
name|isIdentity
argument_list|()
condition|)
block|{
return|return
name|md
return|;
block|}
name|DefaultModuleDescriptor
name|nmd
init|=
operator|new
name|DefaultModuleDescriptor
argument_list|()
decl_stmt|;
name|nmd
operator|.
name|_revId
operator|=
name|t
operator|.
name|transform
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|nmd
operator|.
name|_resolvedRevId
operator|=
name|t
operator|.
name|transform
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|nmd
operator|.
name|_status
operator|=
name|md
operator|.
name|getStatus
argument_list|()
expr_stmt|;
name|nmd
operator|.
name|_publicationDate
operator|=
name|md
operator|.
name|getPublicationDate
argument_list|()
expr_stmt|;
name|nmd
operator|.
name|_resolvedPublicationDate
operator|=
name|md
operator|.
name|getResolvedPublicationDate
argument_list|()
expr_stmt|;
name|DependencyDescriptor
index|[]
name|dd
init|=
name|md
operator|.
name|getDependencies
argument_list|()
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
name|dd
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|nmd
operator|.
name|_dependencies
operator|.
name|add
argument_list|(
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|dd
index|[
name|i
index|]
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Configuration
index|[]
name|confs
init|=
name|md
operator|.
name|getConfigurations
argument_list|()
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
name|confs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|nmd
operator|.
name|_configurations
operator|.
name|put
argument_list|(
name|confs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|confs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|Artifact
index|[]
name|arts
init|=
name|md
operator|.
name|getArtifacts
argument_list|(
name|confs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|arts
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|nmd
operator|.
name|addArtifact
argument_list|(
name|confs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|NameSpaceHelper
operator|.
name|transform
argument_list|(
name|arts
index|[
name|j
index|]
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|nmd
operator|.
name|setDefault
argument_list|(
name|md
operator|.
name|isDefault
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|md
operator|instanceof
name|DefaultModuleDescriptor
condition|)
block|{
name|DefaultModuleDescriptor
name|dmd
init|=
operator|(
name|DefaultModuleDescriptor
operator|)
name|md
decl_stmt|;
name|nmd
operator|.
name|_conflictManagers
operator|.
name|putAll
argument_list|(
name|dmd
operator|.
name|_conflictManagers
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"transformed module descriptor is not a default module descriptor: impossible to copy conflict manager configuration: "
operator|+
name|md
argument_list|)
expr_stmt|;
block|}
name|nmd
operator|.
name|_licenses
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|md
operator|.
name|getLicenses
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nmd
operator|.
name|_homePage
operator|=
name|md
operator|.
name|getHomePage
argument_list|()
expr_stmt|;
name|nmd
operator|.
name|_lastModified
operator|=
name|md
operator|.
name|getLastModified
argument_list|()
expr_stmt|;
return|return
name|nmd
return|;
block|}
specifier|private
name|ModuleRevisionId
name|_revId
decl_stmt|;
specifier|private
name|ModuleRevisionId
name|_resolvedRevId
decl_stmt|;
specifier|private
name|String
name|_status
init|=
name|Status
operator|.
name|DEFAULT_STATUS
decl_stmt|;
specifier|private
name|Date
name|_publicationDate
decl_stmt|;
specifier|private
name|Date
name|_resolvedPublicationDate
decl_stmt|;
specifier|private
name|List
name|_dependencies
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|_configurations
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map(String conf -> Configuration)
specifier|private
name|Map
name|_artifacts
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (String conf -> Collection(Artifact))
specifier|private
name|boolean
name|_isDefault
init|=
literal|false
decl_stmt|;
specifier|private
name|Map
name|_conflictManagers
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
comment|// Map (ModuleId -> )
specifier|private
name|List
name|_licenses
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|// List(License)
specifier|private
name|String
name|_homePage
decl_stmt|;
specifier|private
name|long
name|_lastModified
init|=
literal|0
decl_stmt|;
specifier|public
name|DefaultModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|,
name|String
name|status
parameter_list|,
name|Date
name|pubDate
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
name|status
argument_list|,
name|pubDate
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultModuleDescriptor
parameter_list|(
name|ModuleRevisionId
name|id
parameter_list|,
name|String
name|status
parameter_list|,
name|Date
name|pubDate
parameter_list|,
name|boolean
name|isDefault
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null module revision id not allowed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null status not allowed"
argument_list|)
throw|;
block|}
name|_revId
operator|=
name|id
expr_stmt|;
name|_resolvedRevId
operator|=
name|id
expr_stmt|;
name|_status
operator|=
name|status
expr_stmt|;
name|_publicationDate
operator|=
name|pubDate
expr_stmt|;
name|_resolvedPublicationDate
operator|=
name|_publicationDate
operator|==
literal|null
condition|?
operator|new
name|Date
argument_list|()
else|:
name|_publicationDate
expr_stmt|;
name|_isDefault
operator|=
name|isDefault
expr_stmt|;
block|}
comment|/** 	 * IMPORTANT : at least call setModuleRevisionId and setResolvedPublicationDate 	 * with instances created by this constructor ! 	 * 	 */
specifier|public
name|DefaultModuleDescriptor
parameter_list|()
block|{
block|}
specifier|public
name|boolean
name|isDefault
parameter_list|()
block|{
return|return
name|_isDefault
return|;
block|}
specifier|public
name|void
name|setPublicationDate
parameter_list|(
name|Date
name|publicationDate
parameter_list|)
block|{
name|_publicationDate
operator|=
name|publicationDate
expr_stmt|;
if|if
condition|(
name|_resolvedPublicationDate
operator|==
literal|null
condition|)
block|{
name|_resolvedPublicationDate
operator|=
name|_publicationDate
operator|==
literal|null
condition|?
operator|new
name|Date
argument_list|()
else|:
name|_publicationDate
expr_stmt|;
block|}
block|}
specifier|public
name|Date
name|getPublicationDate
parameter_list|()
block|{
return|return
name|_publicationDate
return|;
block|}
specifier|public
name|void
name|setResolvedPublicationDate
parameter_list|(
name|Date
name|publicationDate
parameter_list|)
block|{
if|if
condition|(
name|publicationDate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null publication date not allowed"
argument_list|)
throw|;
block|}
name|_resolvedPublicationDate
operator|=
name|publicationDate
expr_stmt|;
block|}
specifier|public
name|Date
name|getResolvedPublicationDate
parameter_list|()
block|{
return|return
name|_resolvedPublicationDate
return|;
block|}
specifier|public
name|void
name|setModuleRevisionId
parameter_list|(
name|ModuleRevisionId
name|revId
parameter_list|)
block|{
if|if
condition|(
name|revId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null module revision id not allowed"
argument_list|)
throw|;
block|}
name|_revId
operator|=
name|revId
expr_stmt|;
if|if
condition|(
name|_resolvedRevId
operator|==
literal|null
condition|)
block|{
name|_resolvedRevId
operator|=
name|_revId
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setResolvedModuleRevisionId
parameter_list|(
name|ModuleRevisionId
name|revId
parameter_list|)
block|{
name|_resolvedRevId
operator|=
name|revId
expr_stmt|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|_status
operator|=
name|status
expr_stmt|;
block|}
specifier|public
name|void
name|addDependency
parameter_list|(
name|DependencyDescriptor
name|dependency
parameter_list|)
block|{
name|_dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addConfiguration
parameter_list|(
name|Configuration
name|conf
parameter_list|)
block|{
name|_configurations
operator|.
name|put
argument_list|(
name|conf
operator|.
name|getName
argument_list|()
argument_list|,
name|conf
argument_list|)
expr_stmt|;
block|}
comment|/**      * Artifact configurations are not used since added artifact may not be      * entirely completed, so its configurations data may not be accurate      * @param conf      * @param artifact      */
specifier|public
name|void
name|addArtifact
parameter_list|(
name|String
name|conf
parameter_list|,
name|Artifact
name|artifact
parameter_list|)
block|{
name|Collection
name|artifacts
init|=
operator|(
name|Collection
operator|)
name|_artifacts
operator|.
name|get
argument_list|(
name|conf
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|==
literal|null
condition|)
block|{
name|artifacts
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_artifacts
operator|.
name|put
argument_list|(
name|conf
argument_list|,
name|artifacts
argument_list|)
expr_stmt|;
block|}
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ModuleRevisionId
name|getModuleRevisionId
parameter_list|()
block|{
return|return
name|_revId
return|;
block|}
specifier|public
name|ModuleRevisionId
name|getResolvedModuleRevisionId
parameter_list|()
block|{
return|return
name|_resolvedRevId
return|;
block|}
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|_status
return|;
block|}
specifier|public
name|Configuration
index|[]
name|getConfigurations
parameter_list|()
block|{
return|return
operator|(
name|Configuration
index|[]
operator|)
name|_configurations
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Configuration
index|[
name|_configurations
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getConfigurationsNames
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|_configurations
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|_configurations
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getPublicConfigurationsNames
parameter_list|()
block|{
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_configurations
operator|.
name|values
argument_list|()
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
name|Configuration
name|conf
init|=
operator|(
name|Configuration
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|conf
operator|.
name|getVisibility
argument_list|()
operator|==
name|Configuration
operator|.
name|Visibility
operator|.
name|PUBLIC
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
name|conf
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Returns the configuration object with the given name in the current module descriptor, null      * if not found.      */
specifier|public
name|Configuration
name|getConfiguration
parameter_list|(
name|String
name|confName
parameter_list|)
block|{
return|return
operator|(
name|Configuration
operator|)
name|_configurations
operator|.
name|get
argument_list|(
name|confName
argument_list|)
return|;
block|}
specifier|public
name|Artifact
index|[]
name|getArtifacts
parameter_list|(
name|String
name|conf
parameter_list|)
block|{
name|Collection
name|artifacts
init|=
operator|(
name|Collection
operator|)
name|_artifacts
operator|.
name|get
argument_list|(
name|conf
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Artifact
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
return|return
operator|(
name|Artifact
index|[]
operator|)
name|artifacts
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|artifacts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
specifier|public
name|Artifact
index|[]
name|getAllArtifacts
parameter_list|()
block|{
name|Collection
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|_artifacts
operator|.
name|keySet
argument_list|()
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
name|conf
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|addAll
argument_list|(
operator|(
name|Collection
operator|)
name|_artifacts
operator|.
name|get
argument_list|(
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Artifact
index|[]
operator|)
name|ret
operator|.
name|toArray
argument_list|(
operator|new
name|Artifact
index|[
name|ret
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|DependencyDescriptor
index|[]
name|getDependencies
parameter_list|()
block|{
return|return
operator|(
name|DependencyDescriptor
index|[]
operator|)
name|_dependencies
operator|.
name|toArray
argument_list|(
operator|new
name|DependencyDescriptor
index|[
name|_dependencies
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|dependsOn
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|_dependencies
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
name|DependencyDescriptor
name|dd
init|=
operator|(
name|DependencyDescriptor
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|dd
operator|.
name|getDependencyId
argument_list|()
operator|.
name|equals
argument_list|(
name|md
operator|.
name|getModuleRevisionId
argument_list|()
operator|.
name|getModuleId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|else if
condition|(
name|dd
operator|.
name|getDependencyRevisionId
argument_list|()
operator|.
name|acceptRevision
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"module: "
operator|+
name|_revId
operator|+
literal|" status="
operator|+
name|_status
operator|+
literal|" publication="
operator|+
name|_publicationDate
operator|+
literal|" configurations="
operator|+
name|_configurations
operator|+
literal|" artifacts="
operator|+
name|_artifacts
operator|+
literal|" dependencies="
operator|+
name|_dependencies
return|;
block|}
specifier|public
name|void
name|setDefault
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|_isDefault
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * regular expressions as explained in Pattern class may be used in ModuleId      * organisation and name      *       * @param moduleId      * @param resolverName      */
specifier|public
name|void
name|addConflictManager
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|,
name|ConflictManager
name|manager
parameter_list|)
block|{
name|_conflictManagers
operator|.
name|put
argument_list|(
name|moduleId
argument_list|,
name|manager
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConflictManager
name|getConflictManager
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
name|ConflictManager
name|cm
init|=
operator|(
name|ConflictManager
operator|)
name|_conflictManagers
operator|.
name|get
argument_list|(
name|moduleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|cm
operator|!=
literal|null
condition|)
block|{
return|return
name|cm
return|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|_conflictManagers
operator|.
name|keySet
argument_list|()
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
name|ModuleId
name|mid
init|=
operator|(
name|ModuleId
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|Pattern
operator|.
name|compile
argument_list|(
name|mid
operator|.
name|getOrganisation
argument_list|()
argument_list|)
operator|.
name|matcher
argument_list|(
name|moduleId
operator|.
name|getOrganisation
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|&&
name|Pattern
operator|.
name|compile
argument_list|(
name|mid
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matcher
argument_list|(
name|moduleId
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
operator|(
name|ConflictManager
operator|)
name|_conflictManagers
operator|.
name|get
argument_list|(
name|mid
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addLicense
parameter_list|(
name|License
name|license
parameter_list|)
block|{
name|_licenses
operator|.
name|add
argument_list|(
name|license
argument_list|)
expr_stmt|;
block|}
specifier|public
name|License
index|[]
name|getLicenses
parameter_list|()
block|{
return|return
operator|(
name|License
index|[]
operator|)
name|_licenses
operator|.
name|toArray
argument_list|(
operator|new
name|License
index|[
name|_licenses
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|public
name|String
name|getHomePage
parameter_list|()
block|{
return|return
name|_homePage
return|;
block|}
specifier|public
name|void
name|setHomePage
parameter_list|(
name|String
name|homePage
parameter_list|)
block|{
name|_homePage
operator|=
name|homePage
expr_stmt|;
block|}
specifier|public
name|long
name|getLastModified
parameter_list|()
block|{
return|return
name|_lastModified
return|;
block|}
specifier|public
name|void
name|setLastModified
parameter_list|(
name|long
name|lastModified
parameter_list|)
block|{
name|_lastModified
operator|=
name|lastModified
expr_stmt|;
block|}
block|}
end_class

end_unit

