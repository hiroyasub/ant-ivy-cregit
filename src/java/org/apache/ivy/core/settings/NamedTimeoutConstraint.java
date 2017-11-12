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
name|settings
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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link TimeoutConstraint} which can be identified by a name  */
end_comment

begin_class
specifier|public
class|class
name|NamedTimeoutConstraint
implements|implements
name|TimeoutConstraint
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|connectionTimeout
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|readTimeout
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
name|NamedTimeoutConstraint
parameter_list|()
block|{
block|}
specifier|public
name|NamedTimeoutConstraint
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|StringUtils
operator|.
name|assertNotNullNorEmpty
argument_list|(
name|name
argument_list|,
literal|"Name of a timeout constraint cannot be null or empty string"
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|StringUtils
operator|.
name|assertNotNullNorEmpty
argument_list|(
name|name
argument_list|,
literal|"Name of a timeout constraint cannot be null or empty string"
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @return Returns the name of the timeout constraint      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|connectionTimeout
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|readTimeout
return|;
block|}
comment|/**      * Sets the connection timeout of this timeout constraint      * @param connectionTimeout The connection timeout in milliseconds.      */
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
specifier|final
name|int
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
comment|/**      * Sets the read timeout of this timeout constraint      * @param readTimeout The read timeout in milliseconds.      */
specifier|public
name|void
name|setReadTimeout
parameter_list|(
specifier|final
name|int
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
block|}
end_class

end_unit

