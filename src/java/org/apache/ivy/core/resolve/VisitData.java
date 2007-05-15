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
name|resolve
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
name|HashMap
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

begin_comment
comment|/**  * This class is used to store data related to one node of the dependency graph visit.  *   * It stores both an {@link IvyNode} and related {@link VisitNode} objects.  *   * Indeed, during the visit of the graph, the algorithm can visit the same node  * from several parents, thus requiring several VisitNode.  *   *  */
end_comment

begin_class
specifier|public
class|class
name|VisitData
block|{
comment|/** 	 * A node in the graph of module dependencies resolution 	 */
specifier|private
name|IvyNode
name|_node
decl_stmt|;
comment|/** 	 * The associated visit nodes, per rootModuleConf 	 * Note that the value is a List, because a node can be visited from 	 * several parents during the resolution process 	 */
specifier|private
name|Map
name|_visitNodes
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Map (String rootModuleConf -> List(VisitNode))
specifier|public
name|VisitData
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|_node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|void
name|addVisitNode
parameter_list|(
name|VisitNode
name|node
parameter_list|)
block|{
name|String
name|rootModuleConf
init|=
name|node
operator|.
name|getRootModuleConf
argument_list|()
decl_stmt|;
name|getVisitNodes
argument_list|(
name|rootModuleConf
argument_list|)
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getVisitNodes
parameter_list|(
name|String
name|rootModuleConf
parameter_list|)
block|{
name|List
name|visits
init|=
operator|(
name|List
operator|)
name|_visitNodes
operator|.
name|get
argument_list|(
name|rootModuleConf
argument_list|)
decl_stmt|;
if|if
condition|(
name|visits
operator|==
literal|null
condition|)
block|{
name|visits
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|_visitNodes
operator|.
name|put
argument_list|(
name|rootModuleConf
argument_list|,
name|visits
argument_list|)
expr_stmt|;
block|}
return|return
name|visits
return|;
block|}
specifier|public
name|IvyNode
name|getNode
parameter_list|()
block|{
return|return
name|_node
return|;
block|}
specifier|public
name|void
name|setNode
parameter_list|(
name|IvyNode
name|node
parameter_list|)
block|{
name|_node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|void
name|addVisitNodes
parameter_list|(
name|String
name|rootModuleConf
parameter_list|,
name|List
name|visitNodes
parameter_list|)
block|{
name|getVisitNodes
argument_list|(
name|rootModuleConf
argument_list|)
operator|.
name|addAll
argument_list|(
name|visitNodes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

