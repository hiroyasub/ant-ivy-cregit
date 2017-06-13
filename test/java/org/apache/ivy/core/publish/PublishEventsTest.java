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
name|core
operator|.
name|publish
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
name|IOException
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
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|ivy
operator|.
name|Ivy
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
name|event
operator|.
name|IvyEvent
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
name|publish
operator|.
name|EndArtifactPublishEvent
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
name|publish
operator|.
name|PublishEvent
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
name|publish
operator|.
name|StartArtifactPublishEvent
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
name|MDArtifact
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
name|ArtifactRevisionId
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
name|xml
operator|.
name|XmlModuleDescriptorParser
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
name|MockResolver
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
name|AbstractTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|PublishEventsTest
block|{
comment|// maps ArtifactRevisionId to PublishTestCase instance.
specifier|private
name|HashMap
argument_list|<
name|ArtifactRevisionId
argument_list|,
name|PublishTestCase
argument_list|>
name|expectedPublications
decl_stmt|;
comment|// expected values for the current artifact being published.
specifier|private
name|PublishTestCase
name|currentTestCase
decl_stmt|;
specifier|private
name|boolean
name|expectedOverwrite
decl_stmt|;
comment|// number of times PrePublishTrigger has been invoked successfully
specifier|private
name|int
name|preTriggers
decl_stmt|;
comment|// number of times PostPublishTrigger has been invoked successfully
specifier|private
name|int
name|postTriggers
decl_stmt|;
comment|// number of times an artifact has been successfully published by the resolver
specifier|private
name|int
name|publications
decl_stmt|;
comment|// dummy test data that is reused by all cases.
specifier|private
name|File
name|ivyFile
decl_stmt|;
specifier|private
name|Artifact
name|ivyArtifact
decl_stmt|;
specifier|private
name|File
name|dataFile
decl_stmt|;
specifier|private
name|Artifact
name|dataArtifact
decl_stmt|;
specifier|private
name|ModuleDescriptor
name|publishModule
decl_stmt|;
specifier|private
name|Collection
name|publishSources
decl_stmt|;
specifier|private
name|PublishOptions
name|publishOptions
decl_stmt|;
comment|// if non-null, InstrumentedResolver will throw this exception during publish
specifier|private
name|IOException
name|publishError
decl_stmt|;
comment|// the ivy instance under test
specifier|private
name|Ivy
name|ivy
decl_stmt|;
specifier|private
name|PublishEngine
name|publishEngine
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// reset test case state.
name|resetCounters
argument_list|()
expr_stmt|;
comment|// this ivy settings should configure an InstrumentedResolver, PrePublishTrigger, and
comment|// PostPublishTrigger
comment|// (see inner classes below).
name|ivy
operator|=
name|Ivy
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|ivy
operator|.
name|configure
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"ivysettings-publisheventstest.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|ivy
operator|.
name|pushContext
argument_list|()
expr_stmt|;
name|publishEngine
operator|=
name|ivy
operator|.
name|getPublishEngine
argument_list|()
expr_stmt|;
comment|// setup dummy ivy and data files to test publishing. since we're testing the engine and not
comment|// the resolver,
comment|// we don't really care whether the file actually gets published. we just want to make sure
comment|// that the engine calls the correct methods in the correct order, and fires required
comment|// events.
name|ivyFile
operator|=
operator|new
name|File
argument_list|(
literal|"test/java/org/apache/ivy/core/publish/ivy-1.0-dev.xml"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"path to ivy file not found in test environment"
argument_list|,
name|ivyFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// the contents of the data file don't matter.
name|dataFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"ivydata"
argument_list|,
literal|".jar"
argument_list|)
expr_stmt|;
name|dataFile
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|publishModule
operator|=
name|XmlModuleDescriptorParser
operator|.
name|getInstance
argument_list|()
operator|.
name|parseDescriptor
argument_list|(
name|ivy
operator|.
name|getSettings
argument_list|()
argument_list|,
name|ivyFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// always use the same source data file, no pattern substitution is required.
name|publishSources
operator|=
name|Collections
operator|.
name|singleton
argument_list|(
name|dataFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
comment|// always use the same ivy file, no pattern substitution is required.
name|publishOptions
operator|=
operator|new
name|PublishOptions
argument_list|()
expr_stmt|;
name|publishOptions
operator|.
name|setSrcIvyPattern
argument_list|(
name|ivyFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
comment|// set up our expectations for the test. these variables will
comment|// be checked by the resolver and triggers during publication.
name|dataArtifact
operator|=
name|publishModule
operator|.
name|getAllArtifacts
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sanity check"
argument_list|,
literal|"foo"
argument_list|,
name|dataArtifact
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ivyArtifact
operator|=
name|MDArtifact
operator|.
name|newIvyArtifact
argument_list|(
name|publishModule
argument_list|)
expr_stmt|;
name|expectedPublications
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|expectedPublications
operator|.
name|put
argument_list|(
name|dataArtifact
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|PublishTestCase
argument_list|(
name|dataArtifact
argument_list|,
name|dataFile
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|expectedPublications
operator|.
name|put
argument_list|(
name|ivyArtifact
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|PublishTestCase
argument_list|(
name|ivyArtifact
argument_list|,
name|ivyFile
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hashCode sanity check:  two artifacts expected during publish"
argument_list|,
literal|2
argument_list|,
name|expectedPublications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// push the TestCase instance onto the context stack, so that our
comment|// triggers and resolver instances can interact with it it.
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|push
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
comment|// reset test state.
name|resetCounters
argument_list|()
expr_stmt|;
comment|// test case is finished, pop the test context off the stack.
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|pop
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// cleanup ivy resources
if|if
condition|(
name|ivy
operator|!=
literal|null
condition|)
block|{
name|ivy
operator|.
name|popContext
argument_list|()
expr_stmt|;
name|ivy
operator|=
literal|null
expr_stmt|;
block|}
name|publishEngine
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|dataFile
operator|!=
literal|null
condition|)
block|{
name|dataFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|dataFile
operator|=
literal|null
expr_stmt|;
name|ivyFile
operator|=
literal|null
expr_stmt|;
block|}
specifier|protected
name|void
name|resetCounters
parameter_list|()
block|{
name|preTriggers
operator|=
literal|0
expr_stmt|;
name|postTriggers
operator|=
literal|0
expr_stmt|;
name|publications
operator|=
literal|0
expr_stmt|;
name|expectedPublications
operator|=
literal|null
expr_stmt|;
name|expectedOverwrite
operator|=
literal|false
expr_stmt|;
name|publishError
operator|=
literal|null
expr_stmt|;
name|currentTestCase
operator|=
literal|null
expr_stmt|;
name|ivyArtifact
operator|=
literal|null
expr_stmt|;
name|dataArtifact
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Test a simple artifact publish, without errors or overwrite settings.      */
annotation|@
name|Test
specifier|public
name|void
name|testPublishNoOverwrite
parameter_list|()
throws|throws
name|IOException
block|{
comment|// no modifications to input required for this case -- call out to the resolver, and verify
comment|// that
comment|// all of our test counters have been incremented.
name|Collection
name|missing
init|=
name|publishEngine
operator|.
name|publish
argument_list|(
name|publishModule
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|publishSources
argument_list|,
literal|"default"
argument_list|,
name|publishOptions
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"no missing artifacts"
argument_list|,
literal|0
argument_list|,
name|missing
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// if all tests passed, all of our counter variables should have been updated.
name|assertEquals
argument_list|(
literal|"pre-publish trigger fired and passed all tests"
argument_list|,
literal|2
argument_list|,
name|preTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"post-publish trigger fired and passed all tests"
argument_list|,
literal|2
argument_list|,
name|postTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolver received a publish() call, and passed all tests"
argument_list|,
literal|2
argument_list|,
name|publications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"all expected artifacts have been published"
argument_list|,
literal|0
argument_list|,
name|expectedPublications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a simple artifact publish, with overwrite set to true.      */
annotation|@
name|Test
specifier|public
name|void
name|testPublishWithOverwrite
parameter_list|()
throws|throws
name|IOException
block|{
comment|// we expect the overwrite settings to be passed through the event listeners and into the
comment|// publisher.
name|this
operator|.
name|expectedOverwrite
operator|=
literal|true
expr_stmt|;
comment|// set overwrite to true. InstrumentedResolver will verify that the correct argument value
comment|// was provided.
name|publishOptions
operator|.
name|setOverwrite
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Collection
name|missing
init|=
name|publishEngine
operator|.
name|publish
argument_list|(
name|publishModule
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|publishSources
argument_list|,
literal|"default"
argument_list|,
name|publishOptions
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"no missing artifacts"
argument_list|,
literal|0
argument_list|,
name|missing
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// if all tests passed, all of our counter variables should have been updated.
name|assertEquals
argument_list|(
literal|"pre-publish trigger fired and passed all tests"
argument_list|,
literal|2
argument_list|,
name|preTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"post-publish trigger fired and passed all tests"
argument_list|,
literal|2
argument_list|,
name|postTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolver received a publish() call, and passed all tests"
argument_list|,
literal|2
argument_list|,
name|publications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"all expected artifacts have been published"
argument_list|,
literal|0
argument_list|,
name|expectedPublications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test an attempted publish with an invalid data file path.      */
annotation|@
name|Test
specifier|public
name|void
name|testPublishMissingFile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// delete the datafile. the publish should fail
comment|// and the ivy artifact should still publish successfully.
name|assertTrue
argument_list|(
literal|"datafile has been destroyed"
argument_list|,
name|dataFile
operator|.
name|delete
argument_list|()
argument_list|)
expr_stmt|;
name|PublishTestCase
name|dataPublish
init|=
name|expectedPublications
operator|.
name|get
argument_list|(
name|dataArtifact
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|dataPublish
operator|.
name|expectedSuccess
operator|=
literal|false
expr_stmt|;
name|Collection
name|missing
init|=
name|publishEngine
operator|.
name|publish
argument_list|(
name|publishModule
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|publishSources
argument_list|,
literal|"default"
argument_list|,
name|publishOptions
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"one missing artifact"
argument_list|,
literal|1
argument_list|,
name|missing
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSameArtifact
argument_list|(
literal|"missing artifact was returned"
argument_list|,
name|dataArtifact
argument_list|,
operator|(
name|Artifact
operator|)
name|missing
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
comment|// if all tests passed, all of our counter variables should have been updated.
name|assertEquals
argument_list|(
literal|"pre-publish trigger fired and passed all tests"
argument_list|,
literal|1
argument_list|,
name|preTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"post-publish trigger fired and passed all tests"
argument_list|,
literal|1
argument_list|,
name|postTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"only the ivy file published successfully"
argument_list|,
literal|1
argument_list|,
name|publications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"publish of all expected artifacts has been attempted"
argument_list|,
literal|1
argument_list|,
name|expectedPublications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test an attempted publish in which the target resolver throws an IOException.      */
annotation|@
name|Test
specifier|public
name|void
name|testPublishWithException
parameter_list|()
block|{
comment|// set an error to be thrown during publication of the data file.
name|this
operator|.
name|publishError
operator|=
operator|new
name|IOException
argument_list|(
literal|"boom!"
argument_list|)
expr_stmt|;
comment|// we don't care which artifact is attempted; either will fail with an IOException.
for|for
control|(
name|PublishTestCase
name|publishTestCase
range|:
name|expectedPublications
operator|.
name|values
argument_list|()
control|)
block|{
name|publishTestCase
operator|.
name|expectedSuccess
operator|=
literal|false
expr_stmt|;
block|}
try|try
block|{
name|publishEngine
operator|.
name|publish
argument_list|(
name|publishModule
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|publishSources
argument_list|,
literal|"default"
argument_list|,
name|publishOptions
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"if the resolver throws an exception, the engine should too"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|expected
parameter_list|)
block|{
name|assertSame
argument_list|(
literal|"exception thrown by the resolver should be propagated by the engine"
argument_list|,
name|this
operator|.
name|publishError
argument_list|,
name|expected
argument_list|)
expr_stmt|;
block|}
comment|// the publish engine gives up after the resolver throws an exception on the first artifact,
comment|// so only one set of events should have been fired.
comment|// note that the initial publish error shouldn't prevent the post-publish trigger from
comment|// firing.
name|assertEquals
argument_list|(
literal|"pre-publish trigger fired and passed all tests"
argument_list|,
literal|1
argument_list|,
name|preTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"post-publish trigger fired and passed all tests"
argument_list|,
literal|1
argument_list|,
name|postTriggers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resolver never published successfully"
argument_list|,
literal|0
argument_list|,
name|publications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"publication aborted after first failure"
argument_list|,
literal|1
argument_list|,
name|expectedPublications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Assert that two Artifact instances refer to the same artifact and contain the same metadata.      */
specifier|public
specifier|static
name|void
name|assertSameArtifact
parameter_list|(
name|String
name|message
parameter_list|,
name|Artifact
name|expected
parameter_list|,
name|Artifact
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|message
operator|+
literal|": name"
argument_list|,
name|expected
operator|.
name|getName
argument_list|()
argument_list|,
name|actual
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": id"
argument_list|,
name|expected
operator|.
name|getId
argument_list|()
argument_list|,
name|actual
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": moduleRevisionId"
argument_list|,
name|expected
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|,
name|actual
operator|.
name|getModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|+
literal|": configurations"
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
operator|.
name|getConfigurations
argument_list|()
argument_list|,
name|actual
operator|.
name|getConfigurations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": type"
argument_list|,
name|expected
operator|.
name|getType
argument_list|()
argument_list|,
name|actual
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": ext"
argument_list|,
name|expected
operator|.
name|getExt
argument_list|()
argument_list|,
name|actual
operator|.
name|getExt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": publicationDate"
argument_list|,
name|expected
operator|.
name|getPublicationDate
argument_list|()
argument_list|,
name|actual
operator|.
name|getPublicationDate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": attributes"
argument_list|,
name|expected
operator|.
name|getAttributes
argument_list|()
argument_list|,
name|actual
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|+
literal|": url"
argument_list|,
name|expected
operator|.
name|getUrl
argument_list|()
argument_list|,
name|actual
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|PublishTestCase
block|{
specifier|public
name|Artifact
name|expectedArtifact
decl_stmt|;
specifier|public
name|File
name|expectedData
decl_stmt|;
specifier|public
name|boolean
name|expectedSuccess
decl_stmt|;
specifier|public
name|boolean
name|preTriggerFired
decl_stmt|;
specifier|public
name|boolean
name|published
decl_stmt|;
specifier|public
name|boolean
name|postTriggerFired
decl_stmt|;
specifier|public
name|PublishTestCase
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|File
name|data
parameter_list|,
name|boolean
name|success
parameter_list|)
block|{
name|this
operator|.
name|expectedArtifact
operator|=
name|artifact
expr_stmt|;
name|this
operator|.
name|expectedData
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|expectedSuccess
operator|=
name|success
expr_stmt|;
block|}
block|}
comment|/**      * Base class for pre- and post-publish-artifact triggers. When the trigger receives an event,      * the contents of the publish event are examined to make sure they match the variable settings      * on the calling {@link PublishEventsTest#currentTestCase} instance.      */
specifier|public
specifier|static
class|class
name|TestPublishTrigger
extends|extends
name|AbstractTrigger
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
name|PublishEventsTest
name|test
init|=
operator|(
name|PublishEventsTest
operator|)
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|peek
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|InstrumentedResolver
name|resolver
init|=
operator|(
name|InstrumentedResolver
operator|)
name|test
operator|.
name|ivy
operator|.
name|getSettings
argument_list|()
operator|.
name|getResolver
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"instrumented resolver configured"
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"got a reference to the current unit test case"
argument_list|,
name|test
argument_list|)
expr_stmt|;
comment|// test the proper sequence of events by comparing the number of pre-events,
comment|// post-events, and actual publications.
name|assertTrue
argument_list|(
literal|"event is of correct base type"
argument_list|,
name|event
operator|instanceof
name|PublishEvent
argument_list|)
expr_stmt|;
name|PublishEvent
name|pubEvent
init|=
operator|(
name|PublishEvent
operator|)
name|event
decl_stmt|;
name|Artifact
name|expectedArtifact
init|=
name|test
operator|.
name|currentTestCase
operator|.
name|expectedArtifact
decl_stmt|;
name|File
name|expectedData
init|=
name|test
operator|.
name|currentTestCase
operator|.
name|expectedData
decl_stmt|;
name|assertSameArtifact
argument_list|(
literal|"event records correct artifact"
argument_list|,
name|expectedArtifact
argument_list|,
name|pubEvent
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"event records correct file"
argument_list|,
name|expectedData
operator|.
name|getCanonicalPath
argument_list|()
argument_list|,
name|pubEvent
operator|.
name|getData
argument_list|()
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"event records correct overwrite setting"
argument_list|,
name|test
operator|.
name|expectedOverwrite
argument_list|,
name|pubEvent
operator|.
name|isOverwrite
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"event presents correct resolver"
argument_list|,
name|resolver
argument_list|,
name|pubEvent
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|attributes
init|=
block|{
literal|"organisation"
block|,
literal|"module"
block|,
literal|"revision"
block|,
literal|"artifact"
block|,
literal|"type"
block|,
literal|"ext"
block|,
literal|"resolver"
block|,
literal|"overwrite"
block|}
decl_stmt|;
name|String
index|[]
name|values
init|=
block|{
literal|"apache"
block|,
literal|"PublishEventsTest"
block|,
literal|"1.0-dev"
block|,
name|expectedArtifact
operator|.
name|getName
argument_list|()
block|,
name|expectedArtifact
operator|.
name|getType
argument_list|()
block|,
name|expectedArtifact
operator|.
name|getExt
argument_list|()
block|,
literal|"default"
block|,
name|String
operator|.
name|valueOf
argument_list|(
name|test
operator|.
name|expectedOverwrite
argument_list|)
block|}
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
name|attributes
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|assertEquals
argument_list|(
literal|"event declares correct value for "
operator|+
name|attributes
index|[
name|i
index|]
argument_list|,
name|values
index|[
name|i
index|]
argument_list|,
name|event
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attributes
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// we test file separately, since it is hard to guarantee an exact path match, but we
comment|// want to make sure that both paths point to the same canonical location on the
comment|// filesystem
name|String
name|filePath
init|=
name|event
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"event declares correct value for file"
argument_list|,
name|expectedData
operator|.
name|getCanonicalPath
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|filePath
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ioe
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Extends the tests done by {@link TestPublishTrigger} to check that pre-publish events are      * fired before DependencyResolver.publish() is called, and before post-publish events are      * fired.      */
specifier|public
specifier|static
class|class
name|PrePublishTrigger
extends|extends
name|TestPublishTrigger
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
name|PublishEventsTest
name|test
init|=
operator|(
name|PublishEventsTest
operator|)
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|peek
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"event is of correct concrete type"
argument_list|,
name|event
operator|instanceof
name|StartArtifactPublishEvent
argument_list|)
expr_stmt|;
name|StartArtifactPublishEvent
name|startEvent
init|=
operator|(
name|StartArtifactPublishEvent
operator|)
name|event
decl_stmt|;
comment|// verify that the artifact being publish was in the expected set. set the
comment|// 'currentTestCase'
comment|// pointer so that the resolver and post-publish trigger can check against it.
name|Artifact
name|artifact
init|=
name|startEvent
operator|.
name|getArtifact
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"event defines artifact"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|PublishTestCase
name|currentTestCase
init|=
name|test
operator|.
name|expectedPublications
operator|.
name|remove
argument_list|(
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"artifact "
operator|+
name|artifact
operator|.
name|getId
argument_list|()
operator|+
literal|" was expected for publication"
argument_list|,
name|currentTestCase
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"current publication has not been visited yet"
argument_list|,
name|currentTestCase
operator|.
name|preTriggerFired
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"current publication has not been visited yet"
argument_list|,
name|currentTestCase
operator|.
name|published
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"current publication has not been visited yet"
argument_list|,
name|currentTestCase
operator|.
name|postTriggerFired
argument_list|)
expr_stmt|;
name|test
operator|.
name|currentTestCase
operator|=
name|currentTestCase
expr_stmt|;
comment|// superclass tests common attributes of publish events
name|super
operator|.
name|progress
argument_list|(
name|event
argument_list|)
expr_stmt|;
comment|// increment the call counter in the test
name|currentTestCase
operator|.
name|preTriggerFired
operator|=
literal|true
expr_stmt|;
operator|++
name|test
operator|.
name|preTriggers
expr_stmt|;
block|}
block|}
comment|/**      * Extends the tests done by {@link TestPublishTrigger} to check that post-publish events are      * fired after DependencyResolver.publish() is called, and that the "status" attribute is set to      * the correct value.      */
specifier|public
specifier|static
class|class
name|PostPublishTrigger
extends|extends
name|TestPublishTrigger
block|{
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
comment|// superclass tests common attributes of publish events
name|super
operator|.
name|progress
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|PublishEventsTest
name|test
init|=
operator|(
name|PublishEventsTest
operator|)
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|peek
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// test the proper sequence of events by comparing the current count of pre-events,
comment|// post-events, and actual publications.
name|assertTrue
argument_list|(
literal|"event is of correct concrete type"
argument_list|,
name|event
operator|instanceof
name|EndArtifactPublishEvent
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"pre-publish event has been triggered"
argument_list|,
name|test
operator|.
name|preTriggers
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// test sequence of events
name|assertTrue
argument_list|(
literal|"pre-trigger event has already been fired for this artifact"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|preTriggerFired
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"publication has been done if possible"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|expectedSuccess
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|published
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"post-publish event has not yet been fired for this artifact"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|postTriggerFired
argument_list|)
expr_stmt|;
comment|// test the "status" attribute of the post- event.
name|EndArtifactPublishEvent
name|endEvent
init|=
operator|(
name|EndArtifactPublishEvent
operator|)
name|event
decl_stmt|;
name|assertEquals
argument_list|(
literal|"status bit is set correctly"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|expectedSuccess
argument_list|,
name|endEvent
operator|.
name|isSuccessful
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expectedStatus
init|=
name|test
operator|.
name|currentTestCase
operator|.
name|expectedSuccess
condition|?
literal|"successful"
else|:
literal|"failed"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"status attribute is set to correct value"
argument_list|,
name|expectedStatus
argument_list|,
name|endEvent
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
literal|"status"
argument_list|)
argument_list|)
expr_stmt|;
comment|// increment the call counter in the wrapper test
name|test
operator|.
name|currentTestCase
operator|.
name|postTriggerFired
operator|=
literal|true
expr_stmt|;
operator|++
name|test
operator|.
name|postTriggers
expr_stmt|;
block|}
block|}
comment|/**      * When publish() is called, verifies that a pre-publish event has been fired, and also verifies      * that the method arguments have the correct value. Also simulates an IOException if the      * current test case demands it.      */
specifier|public
specifier|static
class|class
name|InstrumentedResolver
extends|extends
name|MockResolver
block|{
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
comment|// verify that the data from the current test case has been handed down to us
name|PublishEventsTest
name|test
init|=
operator|(
name|PublishEventsTest
operator|)
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|peek
argument_list|(
name|PublishEventsTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// test sequence of events.
name|assertNotNull
argument_list|(
name|test
operator|.
name|currentTestCase
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"preTrigger has already fired"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|preTriggerFired
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"postTrigger has not yet fired"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|postTriggerFired
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"publish has not been called"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|published
argument_list|)
expr_stmt|;
comment|// test event data
name|assertSameArtifact
argument_list|(
literal|"publisher has received correct artifact"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|expectedArtifact
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"publisher has received correct datafile"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|expectedData
operator|.
name|getCanonicalPath
argument_list|()
argument_list|,
name|src
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"publisher has received correct overwrite setting"
argument_list|,
name|test
operator|.
name|expectedOverwrite
argument_list|,
name|overwrite
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"publisher only invoked when source file exists"
argument_list|,
name|test
operator|.
name|currentTestCase
operator|.
name|expectedData
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// simulate a publisher error if the current test case demands it.
if|if
condition|(
name|test
operator|.
name|publishError
operator|!=
literal|null
condition|)
block|{
throw|throw
name|test
operator|.
name|publishError
throw|;
block|}
comment|// all assertions pass. increment the publication count
name|test
operator|.
name|currentTestCase
operator|.
name|published
operator|=
literal|true
expr_stmt|;
operator|++
name|test
operator|.
name|publications
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

