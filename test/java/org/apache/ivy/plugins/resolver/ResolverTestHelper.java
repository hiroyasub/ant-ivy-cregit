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
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertNotNull
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
name|assertTrue
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
name|fail
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ResolverTestHelper
block|{
specifier|static
name|void
name|assertOrganisationEntries
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|String
index|[]
name|orgNames
parameter_list|,
name|OrganisationEntry
index|[]
name|orgs
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|orgs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"invalid organisation entries: unmatched number: expected: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|orgNames
argument_list|)
operator|+
literal|" but was "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|orgs
argument_list|)
argument_list|,
name|orgNames
operator|.
name|length
argument_list|,
name|orgs
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertOrganisationEntriesContains
argument_list|(
name|resolver
argument_list|,
name|orgNames
argument_list|,
name|orgs
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|assertOrganisationEntriesContains
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|String
index|[]
name|orgNames
parameter_list|,
name|OrganisationEntry
index|[]
name|orgs
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|orgs
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|orgName
range|:
name|orgNames
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|OrganisationEntry
name|org
range|:
name|orgs
control|)
block|{
if|if
condition|(
name|orgName
operator|.
name|equals
argument_list|(
name|org
operator|.
name|getOrganisation
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|resolver
argument_list|,
name|org
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"organisation not found: "
operator|+
name|orgName
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|void
name|assertModuleEntries
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|OrganisationEntry
name|org
parameter_list|,
name|String
index|[]
name|names
parameter_list|,
name|ModuleEntry
index|[]
name|mods
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|mods
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"invalid module entries: unmatched number: expected: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
operator|+
literal|" but was "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|mods
argument_list|)
argument_list|,
name|names
operator|.
name|length
argument_list|,
name|mods
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertModuleEntriesContains
argument_list|(
name|resolver
argument_list|,
name|org
argument_list|,
name|names
argument_list|,
name|mods
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|assertModuleEntriesContains
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|OrganisationEntry
name|org
parameter_list|,
name|String
index|[]
name|names
parameter_list|,
name|ModuleEntry
index|[]
name|mods
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|mods
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ModuleEntry
name|mod
range|:
name|mods
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|mod
operator|.
name|getModule
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|resolver
argument_list|,
name|mod
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|org
argument_list|,
name|mod
operator|.
name|getOrganisationEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"module not found: "
operator|+
name|name
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|void
name|assertRevisionEntries
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ModuleEntry
name|mod
parameter_list|,
name|String
index|[]
name|names
parameter_list|,
name|RevisionEntry
index|[]
name|revs
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|revs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"invalid revision entries: unmatched number: expected: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
operator|+
literal|" but was "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|revs
argument_list|)
argument_list|,
name|names
operator|.
name|length
argument_list|,
name|revs
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertRevisionEntriesContains
argument_list|(
name|resolver
argument_list|,
name|mod
argument_list|,
name|names
argument_list|,
name|revs
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|assertRevisionEntriesContains
parameter_list|(
name|DependencyResolver
name|resolver
parameter_list|,
name|ModuleEntry
name|mod
parameter_list|,
name|String
index|[]
name|names
parameter_list|,
name|RevisionEntry
index|[]
name|revs
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|revs
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|RevisionEntry
name|rev
range|:
name|revs
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|rev
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|resolver
argument_list|,
name|rev
operator|.
name|getResolver
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mod
argument_list|,
name|rev
operator|.
name|getModuleEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"revision not found: "
operator|+
name|name
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
name|OrganisationEntry
name|getEntry
parameter_list|(
name|OrganisationEntry
index|[]
name|orgs
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|OrganisationEntry
name|org
range|:
name|orgs
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|org
operator|.
name|getOrganisation
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|org
return|;
block|}
block|}
name|fail
argument_list|(
literal|"organisation not found: "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
comment|// for compilation only
block|}
specifier|static
name|ModuleEntry
name|getEntry
parameter_list|(
name|ModuleEntry
index|[]
name|mods
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|ModuleEntry
name|mod
range|:
name|mods
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|mod
operator|.
name|getModule
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|mod
return|;
block|}
block|}
name|fail
argument_list|(
literal|"module not found: "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
comment|// for compilation only
block|}
block|}
end_class

end_unit

