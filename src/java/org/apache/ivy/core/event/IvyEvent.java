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
name|event
package|;
end_package

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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * The root of all ivy events  *   * Any ivy event knows which ivy instance triggered the event (the source)  * and also has a name and a map of attributes.  *   * The name of the event represents the event type, usually there is a one - one  * mapping between event names and IvyEvent subclass, even if this is not mandatory.  * Example:   * pre-resolve  * pre-resolve-dependency  * post-download  *   * The map of attributes is a Map from String keys to String values.  * It is especially useful to filter events, and to get some of their essential data in   * some context where access to Java types is not easy (in an ant build file, for example),  * Example:   * pre-resolve (organisation=foo, module=bar, revision=1.0, conf=default)  * post-download (organisation=foo, module=bar, revision=1.0, artifact=foo-test, type=jar, ext=jar)  *   *  */
end_comment

begin_class
specifier|public
class|class
name|IvyEvent
block|{
specifier|private
name|EventManager
name|_source
decl_stmt|;
specifier|private
name|String
name|_name
decl_stmt|;
specifier|private
name|Map
name|_attributes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|IvyEvent
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|_source
operator|=
name|IvyContext
operator|.
name|getContext
argument_list|()
operator|.
name|getEventManager
argument_list|()
expr_stmt|;
name|_name
operator|=
name|name
expr_stmt|;
block|}
comment|/** 	 * Should only be called during event object construction, since events should be immutable 	 * @param key 	 * @param value 	 */
specifier|protected
name|void
name|addAttribute
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|_attributes
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addMDAttributes
parameter_list|(
name|ModuleDescriptor
name|md
parameter_list|)
block|{
name|addMridAttributes
argument_list|(
name|md
operator|.
name|getResolvedModuleRevisionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addMridAttributes
parameter_list|(
name|ModuleRevisionId
name|mrid
parameter_list|)
block|{
name|addModuleIdAttributes
argument_list|(
name|mrid
operator|.
name|getModuleId
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"revision"
argument_list|,
name|mrid
operator|.
name|getRevision
argument_list|()
argument_list|)
expr_stmt|;
name|addAttributes
argument_list|(
name|mrid
operator|.
name|getExtraAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addModuleIdAttributes
parameter_list|(
name|ModuleId
name|moduleId
parameter_list|)
block|{
name|addAttribute
argument_list|(
literal|"organisation"
argument_list|,
name|moduleId
operator|.
name|getOrganisation
argument_list|()
argument_list|)
expr_stmt|;
name|addAttribute
argument_list|(
literal|"module"
argument_list|,
name|moduleId
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addConfsAttribute
parameter_list|(
name|String
index|[]
name|confs
parameter_list|)
block|{
name|addAttribute
argument_list|(
literal|"conf"
argument_list|,
name|StringUtils
operator|.
name|join
argument_list|(
name|confs
argument_list|,
literal|", "
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addAttributes
parameter_list|(
name|Map
name|attributes
parameter_list|)
block|{
name|_attributes
operator|.
name|putAll
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EventManager
name|getSource
parameter_list|()
block|{
return|return
name|_source
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|_name
return|;
block|}
comment|/** 	 * Returns the attributes of this event, as a Map(String->String) 	 * @return the attributes of this event, as a Map(String->String) 	 */
specifier|public
name|Map
name|getAttributes
parameter_list|()
block|{
return|return
operator|new
name|HashMap
argument_list|(
name|_attributes
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|getAttributes
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|IvyEvent
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|IvyEvent
name|e
init|=
operator|(
name|IvyEvent
operator|)
name|obj
decl_stmt|;
return|return
name|getSource
argument_list|()
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getSource
argument_list|()
argument_list|)
operator|&&
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|_attributes
operator|.
name|equals
argument_list|(
name|e
operator|.
name|_attributes
argument_list|)
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|hash
init|=
literal|37
decl_stmt|;
name|hash
operator|=
literal|13
operator|*
name|hash
operator|+
name|getSource
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
literal|13
operator|*
name|hash
operator|+
name|getName
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|hash
operator|=
literal|13
operator|*
name|hash
operator|+
name|_attributes
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|hash
return|;
block|}
block|}
end_class

end_unit

