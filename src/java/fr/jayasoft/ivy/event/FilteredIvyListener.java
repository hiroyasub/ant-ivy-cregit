begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one or more  *  contributor license agreements.  See the NOTICE file distributed with  *  this work for additional information regarding copyright ownership.  *  The ASF licenses this file to You under the Apache License, Version 2.0  *  (the "License"); you may not use this file except in compliance with  *  the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  */
end_comment

begin_package
package|package
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|event
package|;
end_package

begin_import
import|import
name|fr
operator|.
name|jayasoft
operator|.
name|ivy
operator|.
name|filter
operator|.
name|Filter
import|;
end_import

begin_class
specifier|public
class|class
name|FilteredIvyListener
implements|implements
name|IvyListener
block|{
specifier|private
name|IvyListener
name|_listener
decl_stmt|;
specifier|private
name|Filter
name|_filter
decl_stmt|;
specifier|public
name|FilteredIvyListener
parameter_list|(
name|IvyListener
name|listener
parameter_list|,
name|Filter
name|filter
parameter_list|)
block|{
name|_listener
operator|=
name|listener
expr_stmt|;
name|_filter
operator|=
name|filter
expr_stmt|;
block|}
specifier|public
name|IvyListener
name|getIvyListener
parameter_list|()
block|{
return|return
name|_listener
return|;
block|}
specifier|public
name|Filter
name|getFilter
parameter_list|()
block|{
return|return
name|_filter
return|;
block|}
specifier|public
name|void
name|progress
parameter_list|(
name|IvyEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|_filter
operator|.
name|accept
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|_listener
operator|.
name|progress
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

