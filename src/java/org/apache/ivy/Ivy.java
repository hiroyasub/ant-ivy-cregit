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
name|io
operator|.
name|FileNotFoundException
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
name|text
operator|.
name|SimpleDateFormat
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
name|cache
operator|.
name|CacheManager
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
name|check
operator|.
name|CheckEngine
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
name|deliver
operator|.
name|DeliverEngine
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
name|deliver
operator|.
name|DeliverOptions
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
name|install
operator|.
name|InstallEngine
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
name|ModuleId
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
name|publish
operator|.
name|PublishEngine
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
name|publish
operator|.
name|PublishOptions
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
name|ResolveReport
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
name|ResolveEngine
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
name|ResolveOptions
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
name|ResolvedModuleRevision
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
name|retrieve
operator|.
name|RetrieveEngine
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
name|retrieve
operator|.
name|RetrieveOptions
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
name|search
operator|.
name|ModuleEntry
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
name|search
operator|.
name|OrganisationEntry
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
name|search
operator|.
name|RevisionEntry
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
name|search
operator|.
name|SearchEngine
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
name|IvySettings
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
name|sort
operator|.
name|SortEngine
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
name|matcher
operator|.
name|PatternMatcher
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
name|TransferEvent
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
name|TransferListener
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
name|trigger
operator|.
name|Trigger
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
name|HostUtil
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
name|filter
operator|.
name|Filter
import|;
end_import

begin_comment
comment|/**  *<a href="http://incubator.apache.org/ivy/">Ivy</a> is a free java based dependency manager.  *   * This class is the main class of Ivy, which acts as a Facade to all services offered by Ivy:  *<ul>  *<li>resolve dependencies</li>  *<li>retrieve artifacts to a local location</li>  *<li>deliver and publish modules</li>  *<li>repository search and listing</li>  *</li>  *   * Here is one typical usage:  * Ivy ivy = Ivy.newInstance();  * ivy.configure(new URL("ivysettings.xml"));  * ivy.resolve(new URL("ivy.xml"));  *    */
end_comment

begin_class
specifier|public
class|class
name|Ivy
block|{
specifier|public
specifier|static
specifier|final
name|SimpleDateFormat
name|DATE_FORMAT
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMddHHmmss"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|Ivy
name|newInstance
parameter_list|()
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|bind
argument_list|()
expr_stmt|;
return|return
name|ivy
return|;
block|}
specifier|public
specifier|static
name|Ivy
name|newInstance
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|Ivy
name|ivy
init|=
operator|new
name|Ivy
argument_list|()
decl_stmt|;
name|ivy
operator|.
name|setSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|bind
argument_list|()
expr_stmt|;
return|return
name|ivy
return|;
block|}
comment|/** 	 * True if the current processing has been requested to be interrupted, 	 * false otherwise 	 */
specifier|private
name|boolean
name|_interrupted
decl_stmt|;
comment|/**      * True if this instance of Ivy hass already been bound to its dependencies,      * false otherwise.      * @see bind()      */
specifier|private
name|boolean
name|_bound
decl_stmt|;
comment|/*       * Following are dependencies of the Ivy instance on      * instances of engines and manager which actually do the work      *       * The attributes can be set either manually using the corresponding setters,      * or all at once with the default implementations using the bind method      */
specifier|private
name|IvySettings
name|_settings
decl_stmt|;
specifier|private
name|EventManager
name|_eventManager
decl_stmt|;
specifier|private
name|SortEngine
name|_sortEngine
decl_stmt|;
specifier|private
name|SearchEngine
name|_searchEngine
decl_stmt|;
specifier|private
name|CheckEngine
name|_checkEngine
decl_stmt|;
specifier|private
name|ResolveEngine
name|_resolveEngine
decl_stmt|;
specifier|private
name|RetrieveEngine
name|_retrieveEngine
decl_stmt|;
specifier|private
name|DeliverEngine
name|_deliverEngine
decl_stmt|;
specifier|private
name|PublishEngine
name|_publishEngine
decl_stmt|;
specifier|private
name|InstallEngine
name|_installEngine
decl_stmt|;
comment|/** 	 * The default constructor of Ivy allows to create an instance of Ivy with none of  	 * its dependencies (engines, settings, ...) created. 	 * If you use this constructor, it's your responsibility to set the dependencies 	 * of Ivy using the appropriate setters (setResolveEngine, ...). 	 * You can also call the bind method to set all the dependencies except those that you  	 * have provided using the setters. 	 *  	 * If you want to get an instance ready to use, prefer the use of Ivy.newInstance(). 	 */
specifier|public
name|Ivy
parameter_list|()
block|{
block|}
comment|/** 	 * This method is used to bind this Ivy instance to  	 * required dependencies, i.e. instance of settings, engines, and so on. 	 * After thes call Ivy is still not configured, which means that the settings 	 * object is still empty. 	 */
specifier|public
name|void
name|bind
parameter_list|()
block|{
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|setIvy
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|_settings
operator|==
literal|null
condition|)
block|{
name|_settings
operator|=
operator|new
name|IvySettings
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_eventManager
operator|==
literal|null
condition|)
block|{
name|_eventManager
operator|=
operator|new
name|EventManager
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|_sortEngine
operator|==
literal|null
condition|)
block|{
name|_sortEngine
operator|=
operator|new
name|SortEngine
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_searchEngine
operator|==
literal|null
condition|)
block|{
name|_searchEngine
operator|=
operator|new
name|SearchEngine
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_resolveEngine
operator|==
literal|null
condition|)
block|{
name|_resolveEngine
operator|=
operator|new
name|ResolveEngine
argument_list|(
name|_settings
argument_list|,
name|_eventManager
argument_list|,
name|_sortEngine
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_retrieveEngine
operator|==
literal|null
condition|)
block|{
name|_retrieveEngine
operator|=
operator|new
name|RetrieveEngine
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_deliverEngine
operator|==
literal|null
condition|)
block|{
name|_deliverEngine
operator|=
operator|new
name|DeliverEngine
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_publishEngine
operator|==
literal|null
condition|)
block|{
name|_publishEngine
operator|=
operator|new
name|PublishEngine
argument_list|(
name|_settings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|_installEngine
operator|==
literal|null
condition|)
block|{
name|_installEngine
operator|=
operator|new
name|InstallEngine
argument_list|(
name|_settings
argument_list|,
name|_searchEngine
argument_list|,
name|_resolveEngine
argument_list|,
name|_publishEngine
argument_list|)
expr_stmt|;
block|}
name|_eventManager
operator|.
name|addTransferListener
argument_list|(
operator|new
name|TransferListener
argument_list|()
block|{
specifier|public
name|void
name|transferProgress
parameter_list|(
name|TransferEvent
name|evt
parameter_list|)
block|{
switch|switch
condition|(
name|evt
operator|.
name|getEventType
argument_list|()
condition|)
block|{
case|case
name|TransferEvent
operator|.
name|TRANSFER_PROGRESS
case|:
name|Message
operator|.
name|progress
argument_list|()
expr_stmt|;
break|break;
case|case
name|TransferEvent
operator|.
name|TRANSFER_COMPLETED
case|:
name|Message
operator|.
name|endProgress
argument_list|(
literal|" ("
operator|+
operator|(
name|evt
operator|.
name|getTotalLength
argument_list|()
operator|/
literal|1024
operator|)
operator|+
literal|"kB)"
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|_bound
operator|=
literal|true
expr_stmt|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         LOAD SETTINGS
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|void
name|configure
parameter_list|(
name|File
name|settingsFile
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|assertBound
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|load
argument_list|(
name|settingsFile
argument_list|)
expr_stmt|;
name|postConfigure
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|URL
name|settingsURL
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|assertBound
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|load
argument_list|(
name|settingsURL
argument_list|)
expr_stmt|;
name|postConfigure
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|configureDefault
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|assertBound
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|loadDefault
argument_list|()
expr_stmt|;
name|postConfigure
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Configures Ivy with 1.4 compatible default settings 	 */
specifier|public
name|void
name|configureDefault14
parameter_list|()
throws|throws
name|ParseException
throws|,
name|IOException
block|{
name|assertBound
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|loadDefault14
argument_list|()
expr_stmt|;
name|postConfigure
argument_list|()
expr_stmt|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         CHECK
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|boolean
name|check
parameter_list|(
name|URL
name|ivyFile
parameter_list|,
name|String
name|resolvername
parameter_list|)
block|{
return|return
name|_checkEngine
operator|.
name|check
argument_list|(
name|ivyFile
argument_list|,
name|resolvername
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         RESOLVE
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|File
name|ivySource
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
return|return
name|_resolveEngine
operator|.
name|resolve
argument_list|(
name|ivySource
argument_list|)
return|;
block|}
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|URL
name|ivySource
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
return|return
name|_resolveEngine
operator|.
name|resolve
argument_list|(
name|ivySource
argument_list|)
return|;
block|}
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|ResolveOptions
name|options
parameter_list|,
name|boolean
name|changing
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
return|return
name|_resolveEngine
operator|.
name|resolve
argument_list|(
name|mrid
argument_list|,
name|options
argument_list|,
name|changing
argument_list|)
return|;
block|}
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|URL
name|ivySource
parameter_list|,
name|ResolveOptions
name|options
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
block|{
return|return
name|_resolveEngine
operator|.
name|resolve
argument_list|(
name|ivySource
argument_list|,
name|options
argument_list|)
return|;
block|}
specifier|public
name|ResolveReport
name|resolve
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|,
name|ResolveOptions
name|options
parameter_list|)
throws|throws
name|ParseException
throws|,
name|IOException
throws|,
name|FileNotFoundException
block|{
return|return
name|_resolveEngine
operator|.
name|resolve
argument_list|(
name|md
argument_list|,
name|options
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         INSTALL
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|ResolveReport
name|install
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|to
parameter_list|,
name|boolean
name|transitive
parameter_list|,
name|boolean
name|validate
parameter_list|,
name|boolean
name|overwrite
parameter_list|,
name|Filter
name|artifactFilter
parameter_list|,
name|File
name|cache
parameter_list|,
name|String
name|matcherName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_installEngine
operator|.
name|install
argument_list|(
name|mrid
argument_list|,
name|from
argument_list|,
name|to
argument_list|,
name|transitive
argument_list|,
name|validate
argument_list|,
name|overwrite
argument_list|,
name|artifactFilter
argument_list|,
name|cache
argument_list|,
name|matcherName
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         RETRIEVE
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|int
name|retrieve
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|destFilePattern
parameter_list|,
name|RetrieveOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_retrieveEngine
operator|.
name|retrieve
argument_list|(
name|mrid
argument_list|,
name|destFilePattern
argument_list|,
name|options
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         DELIVER
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|void
name|deliver
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|destIvyPattern
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|_deliverEngine
operator|.
name|deliver
argument_list|(
name|mrid
argument_list|,
name|revision
argument_list|,
name|destIvyPattern
argument_list|,
name|DeliverOptions
operator|.
name|newInstance
argument_list|(
name|_settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deliver
parameter_list|(
name|String
name|revision
parameter_list|,
name|String
name|destIvyPattern
parameter_list|,
name|DeliverOptions
name|options
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|_deliverEngine
operator|.
name|deliver
argument_list|(
name|revision
argument_list|,
name|destIvyPattern
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Example of use: 	 * deliver(mrid, "1.5", "target/ivy/ivy-[revision].xml",  	 *         DeliverOptions.newInstance(settings).setStatus("release").setValidate(false)); 	 *          	 * @param mrid 	 * @param revision 	 * @param destIvyPattern 	 * @param options 	 * @throws IOException 	 * @throws ParseException 	 */
specifier|public
name|void
name|deliver
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|String
name|revision
parameter_list|,
name|String
name|destIvyPattern
parameter_list|,
name|DeliverOptions
name|options
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
name|_deliverEngine
operator|.
name|deliver
argument_list|(
name|mrid
argument_list|,
name|revision
argument_list|,
name|destIvyPattern
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         PUBLISH
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|Collection
name|publish
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|,
name|Collection
name|srcArtifactPattern
parameter_list|,
name|String
name|resolverName
parameter_list|,
name|PublishOptions
name|options
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|_publishEngine
operator|.
name|publish
argument_list|(
name|mrid
argument_list|,
name|srcArtifactPattern
argument_list|,
name|resolverName
argument_list|,
name|options
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         SORT
comment|/////////////////////////////////////////////////////////////////////////
comment|/**      * Sorts the collection of IvyNode from the less dependent to the more dependent      */
specifier|public
name|List
name|sortNodes
parameter_list|(
name|Collection
name|nodes
parameter_list|)
block|{
return|return
name|_sortEngine
operator|.
name|sortNodes
argument_list|(
name|nodes
argument_list|)
return|;
block|}
comment|/**      * Sorts the given ModuleDescriptors from the less dependent to the more dependent.      * This sort ensures that a ModuleDescriptor is always found in the list before all       * ModuleDescriptors depending directly on it.      * @param moduleDescriptors a Collection of ModuleDescriptor to sort      * @return a List of sorted ModuleDescriptors      */
specifier|public
name|List
name|sortModuleDescriptors
parameter_list|(
name|Collection
name|moduleDescriptors
parameter_list|)
block|{
return|return
name|_sortEngine
operator|.
name|sortModuleDescriptors
argument_list|(
name|moduleDescriptors
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         SEARCH
comment|/////////////////////////////////////////////////////////////////////////
specifier|public
name|ResolvedModuleRevision
name|findModule
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|ResolveOptions
name|options
init|=
operator|new
name|ResolveOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|setValidate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|options
operator|.
name|setCache
argument_list|(
name|CacheManager
operator|.
name|getInstance
argument_list|(
name|_settings
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|_resolveEngine
operator|.
name|findModule
argument_list|(
name|mrid
argument_list|,
name|options
argument_list|)
return|;
block|}
specifier|public
name|ModuleEntry
index|[]
name|listModuleEntries
parameter_list|(
name|OrganisationEntry
name|org
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listModuleEntries
argument_list|(
name|org
argument_list|)
return|;
block|}
specifier|public
name|ModuleId
index|[]
name|listModules
parameter_list|(
name|ModuleId
name|criteria
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listModules
argument_list|(
name|criteria
argument_list|,
name|matcher
argument_list|)
return|;
block|}
specifier|public
name|ModuleRevisionId
index|[]
name|listModules
parameter_list|(
name|ModuleRevisionId
name|criteria
parameter_list|,
name|PatternMatcher
name|matcher
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listModules
argument_list|(
name|criteria
argument_list|,
name|matcher
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|listModules
parameter_list|(
name|String
name|org
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listModules
argument_list|(
name|org
argument_list|)
return|;
block|}
specifier|public
name|OrganisationEntry
index|[]
name|listOrganisationEntries
parameter_list|()
block|{
return|return
name|_searchEngine
operator|.
name|listOrganisationEntries
argument_list|()
return|;
block|}
specifier|public
name|String
index|[]
name|listOrganisations
parameter_list|()
block|{
return|return
name|_searchEngine
operator|.
name|listOrganisations
argument_list|()
return|;
block|}
specifier|public
name|RevisionEntry
index|[]
name|listRevisionEntries
parameter_list|(
name|ModuleEntry
name|module
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listRevisionEntries
argument_list|(
name|module
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|listRevisions
parameter_list|(
name|String
name|org
parameter_list|,
name|String
name|module
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listRevisions
argument_list|(
name|org
argument_list|,
name|module
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|listTokenValues
parameter_list|(
name|String
name|token
parameter_list|,
name|Map
name|otherTokenValues
parameter_list|)
block|{
return|return
name|_searchEngine
operator|.
name|listTokenValues
argument_list|(
name|token
argument_list|,
name|otherTokenValues
argument_list|)
return|;
block|}
comment|/////////////////////////////////////////////////////////////////////////
comment|//                         INTERRUPTIONS
comment|/////////////////////////////////////////////////////////////////////////
comment|/**      * Interrupts the current running operation, no later than      * interruptTimeout milliseconds after the call      */
specifier|public
name|void
name|interrupt
parameter_list|()
block|{
name|Thread
name|operatingThread
init|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getOperatingThread
argument_list|()
decl_stmt|;
name|interrupt
argument_list|(
name|operatingThread
argument_list|)
expr_stmt|;
block|}
comment|/**      * Interrupts the current running operation in the given operating thread,       * no later than interruptTimeout milliseconds after the call      */
specifier|public
name|void
name|interrupt
parameter_list|(
name|Thread
name|operatingThread
parameter_list|)
block|{
if|if
condition|(
name|operatingThread
operator|!=
literal|null
operator|&&
name|operatingThread
operator|.
name|isAlive
argument_list|()
condition|)
block|{
if|if
condition|(
name|operatingThread
operator|==
name|Thread
operator|.
name|currentThread
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"cannot call interrupt from ivy operating thread"
argument_list|)
throw|;
block|}
name|Message
operator|.
name|verbose
argument_list|(
literal|"interrupting operating thread..."
argument_list|)
expr_stmt|;
name|operatingThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|_interrupted
operator|=
literal|true
expr_stmt|;
block|}
try|try
block|{
name|Message
operator|.
name|verbose
argument_list|(
literal|"waiting clean interruption of operating thread"
argument_list|)
expr_stmt|;
name|operatingThread
operator|.
name|join
argument_list|(
name|_settings
operator|.
name|getInterruptTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
block|}
if|if
condition|(
name|operatingThread
operator|.
name|isAlive
argument_list|()
condition|)
block|{
name|Message
operator|.
name|warn
argument_list|(
literal|"waited clean interruption for too long: stopping operating thread"
argument_list|)
expr_stmt|;
name|operatingThread
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|_interrupted
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|synchronized
name|boolean
name|isInterrupted
parameter_list|()
block|{
return|return
name|_interrupted
return|;
block|}
comment|/**      * Check if the current operation has been interrupted, and if it is the case, throw a runtime exception      */
specifier|public
name|void
name|checkInterrupted
parameter_list|()
block|{
if|if
condition|(
name|isInterrupted
argument_list|()
condition|)
block|{
name|Message
operator|.
name|info
argument_list|(
literal|"operation interrupted"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"operation interrupted"
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getWorkingRevision
parameter_list|()
block|{
return|return
literal|"working@"
operator|+
name|HostUtil
operator|.
name|getLocalHostName
argument_list|()
return|;
block|}
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|(
name|File
name|cache
parameter_list|)
block|{
comment|//TODO : reuse instance
name|CacheManager
name|cacheManager
init|=
operator|new
name|CacheManager
argument_list|(
name|_settings
argument_list|,
name|cache
argument_list|)
decl_stmt|;
return|return
name|cacheManager
return|;
block|}
specifier|private
name|void
name|assertBound
parameter_list|()
block|{
if|if
condition|(
operator|!
name|_bound
condition|)
block|{
name|bind
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|postConfigure
parameter_list|()
block|{
name|Collection
name|triggers
init|=
name|_settings
operator|.
name|getTriggers
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|triggers
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
name|Trigger
name|trigger
init|=
operator|(
name|Trigger
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|_eventManager
operator|.
name|addIvyListener
argument_list|(
name|trigger
argument_list|,
name|trigger
operator|.
name|getEventFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getVariable
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|assertBound
argument_list|()
expr_stmt|;
return|return
name|_settings
operator|.
name|getVariable
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|String
name|substitute
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|assertBound
argument_list|()
expr_stmt|;
return|return
name|_settings
operator|.
name|substitute
argument_list|(
name|str
argument_list|)
return|;
block|}
specifier|public
name|void
name|setVariable
parameter_list|(
name|String
name|varName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|assertBound
argument_list|()
expr_stmt|;
name|_settings
operator|.
name|setVariable
argument_list|(
name|varName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/////////////////////////////////////////////////////////////////////
comment|// GETTERS / SETTERS
comment|/////////////////////////////////////////////////////////////////////
specifier|public
name|IvySettings
name|getSettings
parameter_list|()
block|{
return|return
name|_settings
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|_eventManager
return|;
block|}
specifier|public
name|CheckEngine
name|getCheckEngine
parameter_list|()
block|{
return|return
name|_checkEngine
return|;
block|}
specifier|public
name|void
name|setCheckEngine
parameter_list|(
name|CheckEngine
name|checkEngine
parameter_list|)
block|{
name|_checkEngine
operator|=
name|checkEngine
expr_stmt|;
block|}
specifier|public
name|DeliverEngine
name|getDeliverEngine
parameter_list|()
block|{
return|return
name|_deliverEngine
return|;
block|}
specifier|public
name|void
name|setDeliverEngine
parameter_list|(
name|DeliverEngine
name|deliverEngine
parameter_list|)
block|{
name|_deliverEngine
operator|=
name|deliverEngine
expr_stmt|;
block|}
specifier|public
name|InstallEngine
name|getInstallEngine
parameter_list|()
block|{
return|return
name|_installEngine
return|;
block|}
specifier|public
name|void
name|setInstallEngine
parameter_list|(
name|InstallEngine
name|installEngine
parameter_list|)
block|{
name|_installEngine
operator|=
name|installEngine
expr_stmt|;
block|}
specifier|public
name|PublishEngine
name|getPublishEngine
parameter_list|()
block|{
return|return
name|_publishEngine
return|;
block|}
specifier|public
name|void
name|setPublishEngine
parameter_list|(
name|PublishEngine
name|publishEngine
parameter_list|)
block|{
name|_publishEngine
operator|=
name|publishEngine
expr_stmt|;
block|}
specifier|public
name|ResolveEngine
name|getResolveEngine
parameter_list|()
block|{
return|return
name|_resolveEngine
return|;
block|}
specifier|public
name|void
name|setResolveEngine
parameter_list|(
name|ResolveEngine
name|resolveEngine
parameter_list|)
block|{
name|_resolveEngine
operator|=
name|resolveEngine
expr_stmt|;
block|}
specifier|public
name|RetrieveEngine
name|getRetrieveEngine
parameter_list|()
block|{
return|return
name|_retrieveEngine
return|;
block|}
specifier|public
name|void
name|setRetrieveEngine
parameter_list|(
name|RetrieveEngine
name|retrieveEngine
parameter_list|)
block|{
name|_retrieveEngine
operator|=
name|retrieveEngine
expr_stmt|;
block|}
specifier|public
name|SearchEngine
name|getSearchEngine
parameter_list|()
block|{
return|return
name|_searchEngine
return|;
block|}
specifier|public
name|void
name|setSearchEngine
parameter_list|(
name|SearchEngine
name|searchEngine
parameter_list|)
block|{
name|_searchEngine
operator|=
name|searchEngine
expr_stmt|;
block|}
specifier|public
name|SortEngine
name|getSortEngine
parameter_list|()
block|{
return|return
name|_sortEngine
return|;
block|}
specifier|public
name|void
name|setSortEngine
parameter_list|(
name|SortEngine
name|sortEngine
parameter_list|)
block|{
name|_sortEngine
operator|=
name|sortEngine
expr_stmt|;
block|}
specifier|public
name|void
name|setEventManager
parameter_list|(
name|EventManager
name|eventManager
parameter_list|)
block|{
name|_eventManager
operator|=
name|eventManager
expr_stmt|;
block|}
specifier|public
name|void
name|setSettings
parameter_list|(
name|IvySettings
name|settings
parameter_list|)
block|{
name|_settings
operator|=
name|settings
expr_stmt|;
block|}
block|}
end_class

end_unit

